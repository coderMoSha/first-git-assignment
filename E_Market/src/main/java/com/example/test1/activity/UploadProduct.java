package com.example.test1.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.os.Looper;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.test1.Adapter.ImagesAdapter;
import com.example.test1.R;
import com.example.test1.viewmodels.EnterProductDetails;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;


import java.io.InputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

public class UploadProduct extends AppCompatActivity {

    private RecyclerView addProductImage;
    private EditText addTitle, addDescription, addPrice, addCondition, addLocation;
    private Button listProductButton, addImageButton;
    private FirebaseAuth mAuth;
    private Uri filepath;
    private Bitmap bitmap;
    ArrayList<Uri> mUri = new ArrayList<>();
    ImagesAdapter adapter;
    StorageTask uploadProgress;

    LocationRequest locationRequest;
    FusedLocationProviderClient fusedLocationClient;
    LocationCallback locationCallback;
    private double latitude;
    public double longitude;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_product);
        if (requestPermissions()) {
            getLatLngAddress();
        }
        addProductImage = findViewById(R.id.addProductImage);
        addTitle = findViewById(R.id.addTitle);
        addPrice = findViewById(R.id.addPrice);
        addDescription = findViewById(R.id.addDescription);
        addCondition = findViewById(R.id.addCondition);
        addLocation = findViewById(R.id.addLocation);
        listProductButton = findViewById(R.id.listProductButton);
        addImageButton = findViewById(R.id.addImageButton);

        mAuth = FirebaseAuth.getInstance();
        FirebaseUser fbUser = mAuth.getCurrentUser();
        adapter = new ImagesAdapter(mUri, this);
        addProductImage.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false));
        addProductImage.setAdapter(adapter);
        FirebaseStorage storage = FirebaseStorage.getInstance();


        addImageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Dexter.withActivity(UploadProduct.this)
                        .withPermission(Manifest.permission.READ_EXTERNAL_STORAGE)
                        .withListener(new PermissionListener() {
                            @Override
                            public void onPermissionGranted(PermissionGrantedResponse response) {
                                Intent intent = new Intent(Intent.ACTION_PICK);
                                intent.setType("image/*");
                                intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                                startActivityForResult(Intent.createChooser(intent, "Select Image File"), 1);
                            }

                            @Override
                            public void onPermissionDenied(PermissionDeniedResponse response) {

                            }

                            @Override
                            public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                                token.continuePermissionRequest();
                            }
                        }).check();
            }
        });
        listProductButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String title, price, description, condition, location;

                title = String.valueOf(addTitle.getText());
                price = String.valueOf(addPrice.getText());
                description = String.valueOf(addDescription.getText());
                condition = String.valueOf(addCondition.getText());
                location = String.valueOf(addLocation.getText());
                if (mUri.isEmpty()){
                    Toast.makeText(UploadProduct.this, "Please Add Images", Toast.LENGTH_SHORT).show();
                    return;
                }
                addProductDataBase(title, price, description, condition, location);

                listProductButton.setEnabled(false);
                // }
            }

            private void addProductDataBase(String title, String price, String description, String condition, String location) {


                //  String uridb = uri.toString();
                String uniqueIdProduct = FirebaseDatabase.getInstance().getReference().push().getKey();

                EnterProductDetails productDetails = new EnterProductDetails(uniqueIdProduct, FirebaseAuth.getInstance().getCurrentUser().getUid(),
                        title, price, description, condition, location);

                String uniqueIdProductDB = uniqueIdProduct + title;
                FirebaseUser fbUser = mAuth.getCurrentUser();
                DatabaseReference reference = FirebaseDatabase.getInstance("https://e-marcket-68d42-default-rtdb.europe-west1.firebasedatabase.app")
                        .getReference("Products");

                reference.child(uniqueIdProductDB).setValue(productDetails).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if (task.isSuccessful()) {

                            for (Uri uri : mUri) {
                                final StorageReference uploadImage = storage.getReference("productImage/" + title + new Random().nextInt(50));

                                uploadProgress = uploadImage.putFile(uri)
                                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                            @Override
                                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                                uploadImage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                                    @Override
                                                    public void onSuccess(Uri uri) {
                                                        HashMap<String, Object> hashMap = new HashMap<>();
                                                        hashMap.put("image", uri.toString());
                                                        reference.child(uniqueIdProductDB).child("images").push().setValue(hashMap);
                                                    }
                                                });

                                            }
                                        });

                            }
                            finish();

                       /*     Intent intent = new Intent(getApplicationContext(), MainDashBoard.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                            startActivity(intent);
                            listProductButton.setEnabled(true);*/
                        } else {
                            Toast.makeText(UploadProduct.this, "Please fill the required field", Toast.LENGTH_SHORT).show();
                            listProductButton.setEnabled(true);
                        }
                    }
                });


            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK) {
            if (data.getClipData() != null) {
                int cout = data.getClipData().getItemCount();
                for (int i = 0; i < cout; i++) {
                    // adding imageuri in array
                    Uri imageurl = data.getClipData().getItemAt(i).getUri();
                    mUri.add(imageurl);
                    adapter.notifyItemInserted(mUri.size() - 1);
                }
                // setting 1st selected image into image switcher
            }else {
                mUri.add(data.getData());
                adapter.notifyItemInserted(mUri.size()-1);
            }
        } else {
            filepath = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(filepath);
                bitmap = BitmapFactory.decodeStream(inputStream);

            } catch (Exception ex) {

            }
        }
    }
    public boolean requestPermissions() {
        int REQUEST_PERMISSION = 3000;
        String permissions[] = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION};
        boolean grantFinePermission =
                ContextCompat.checkSelfPermission(this, permissions[0]) == PackageManager.PERMISSION_GRANTED;
        boolean grantCoarsePermission =
                ContextCompat.checkSelfPermission(this, permissions[1]) == PackageManager.PERMISSION_GRANTED;

        if (!grantFinePermission && !grantCoarsePermission) {
            ActivityCompat.requestPermissions(this, permissions, REQUEST_PERMISSION);
        } else if (!grantFinePermission) {
            ActivityCompat.requestPermissions(this, new String[]{permissions[0]}, REQUEST_PERMISSION);
        } else if (!grantCoarsePermission) {
            ActivityCompat.requestPermissions(this, new String[]{permissions[1]}, REQUEST_PERMISSION);
        }

        return grantFinePermission && grantCoarsePermission;
    }
    public synchronized void getLatLngAddress() {
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }

        fusedLocationClient.getLastLocation()
                .addOnSuccessListener(this, new OnSuccessListener<Location>() {
                    @Override
                    public void onSuccess(Location location) {
                        createLocationRequestLocationCallback();
                        startLocationUpdates();
                        // Got last known location. In some rare situations this can be null.
                        if (location != null) {
                            latitude = location.getLatitude();
                            longitude = location.getLongitude();
                            String address = getStreetAddress(latitude, longitude);
                            addLocation.setText(address);

                        }
                    }
                });
    }
    public void createLocationRequestLocationCallback() {
        locationRequest = com.google.android.gms.location.LocationRequest.create();
        long ms = 10000; // milliseconds
        // Sets the desired interval for active location updates.
        locationRequest.setInterval(ms);
        // Sets the fastest rate for active location updates.
        locationRequest.setFastestInterval(ms / 2);
        locationRequest.setPriority(com.google.android.gms.location.LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
            }
        };
    }

    public void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        fusedLocationClient.requestLocationUpdates(locationRequest,
                locationCallback,
                Looper.getMainLooper());
    }

    public String getStreetAddress(double latitude, double longitude) {
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        String streetAddress = "";
        try {
            List<Address> addresses = geocoder.getFromLocation(latitude, longitude, 1);
            if (addresses != null) {
                Address address = addresses.get(0);
                for (int i = 0; i <= address.getMaxAddressLineIndex(); i++) {
                    streetAddress += address.getAddressLine(i) + "\n";
                }
            } else {
                streetAddress = "Unknown";
            }
        } catch (Exception e) {
            streetAddress = "Service not available";
            e.printStackTrace();
        }
        return streetAddress;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode==3000 && grantResults.length>0){
            getLatLngAddress();
        }
    }
}



