package com.example.test1.activity;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test1.EnterProductDetails;
import com.example.test1.R;
import com.example.test1.viewmodels.EnterUserDetails;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.PermissionListener;


import java.io.InputStream;
import java.util.Random;

public class UploadProduct extends AppCompatActivity {

    ImageView addProductImage;
    EditText addTitle, addDescription, addPrice, addCondition, addLocation;
    Button listProductButton, addImageButton;
    FirebaseAuth mAuth;
    Uri filepath;

    Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_product);

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
                if (fbUser == null) {
                    Toast.makeText(UploadProduct.this, "User invalid!", Toast.LENGTH_SHORT).show();

                } else {

                    String title, price, description, condition, location;

                    title = String.valueOf(addTitle.getText());
                    price = String.valueOf(addPrice.getText());
                    description = String.valueOf(addDescription.getText());
                    condition = String.valueOf(addCondition.getText());
                    location = String.valueOf(addLocation.getText());


                    addProductDataBase(title, price, description, condition, location);
                }
            }








            private void addProductDataBase(String title, String price, String description, String condition, String location) {

                final StorageReference uploadImage = storage.getReference("productImage" + new Random().nextInt(50));

                uploadImage.putFile(filepath)
                        .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                                uploadImage.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                    @Override
                                    public void onSuccess(Uri uri) {

                                        String uridb = uri.toString();

                                        EnterProductDetails productDetails = new EnterProductDetails(title, price, description, condition, location, uridb);
                                        FirebaseUser fbUser = mAuth.getCurrentUser();
                                        DatabaseReference reference = FirebaseDatabase.getInstance("https://e-marcket-68d42-default-rtdb.europe-west1.firebasedatabase.app")
                                                .getReference("Products");


                                    }
                                });

                            }
                        });


            }
        });


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK) {
            filepath = data.getData();
            try {
                InputStream inputStream = getContentResolver().openInputStream(filepath);
                bitmap = BitmapFactory.decodeStream(inputStream);
                addProductImage.setImageBitmap(bitmap);
            } catch (Exception ex) {

            }
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

}