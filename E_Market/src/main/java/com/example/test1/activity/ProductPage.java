package com.example.test1.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.viewpager2.widget.ViewPager2;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test1.Adapter.FirebaseImagesAdapter;
import com.example.test1.R;
import com.example.test1.viewmodels.EnterProductDetails;
import com.example.test1.viewmodels.Image;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;

public class ProductPage extends AppCompatActivity {

    private ViewPager2 showProductImage;
    private TextView showTitle, showDescription, showPrice, showCondition, showLocation;
    private Button messageButton, backToDashboardButton,maps;

    private FirebaseAuth mAuth;
    private FirebaseUser currentUser;
    EnterProductDetails details;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);

        //Getting Data from the adapter
        details= (EnterProductDetails) getIntent().getSerializableExtra("data");

        showProductImage =findViewById(R.id.showProductImage);
        showTitle = findViewById(R.id.showTitle);
        showPrice = findViewById(R.id.showPrice);
        showDescription = findViewById(R.id.showDescription);
        showCondition = findViewById(R.id.showCondition);
        showLocation = findViewById(R.id.showLocation);

        backToDashboardButton = findViewById(R.id.backToDashboardButton);
        messageButton = findViewById(R.id.messageButton);
        maps = findViewById(R.id.maps);

        mAuth = FirebaseAuth.getInstance();
        currentUser = mAuth.getCurrentUser();

        // display the product details in the UI components
        showTitle.setText(details.getTitleDB());
        showPrice.setText("£"+details.getPriceDB());
        showCondition.setText("Condition: " + details.getConditionDB());
        showDescription.setText("Description: "+details.getDescriptionDB());
        showLocation.setText("Location: "+details.getLocationDB());

        String productID = details.getId();
        String productTitle = details.getTitleDB();

        //Checking if the product is of current user then we have to not show the chat button
        if (mAuth.getCurrentUser().getUid().equals(details.userId)){
           // messageButton.setVisibility(View.GONE);
            messageButton.setText("Mark as Sold");

        }

        //Creating the list for images and putting the images into list
        List<Image> list=new ArrayList<>();
        if (details.getImages()!=null){
            list.addAll(details.getImages().values());
        }

        //Setting the Adapter for images
        showProductImage.setAdapter(new FirebaseImagesAdapter((ArrayList<Image>) list,this));

        //Open Map Activity with location
        maps.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1=new Intent(ProductPage.this,MapsActivity.class);
                intent1.putExtra("data",details.getLocationDB());
                startActivity(intent1);
            }
        });

        backToDashboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }

        });

        messageButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!messageButton.getText().equals("Mark as Sold")&&currentUser != null) {
                    Intent intent = new Intent(ProductPage.this, Chat.class);
                    intent.putExtra("userId", details.userId);
                    startActivity(intent);
                    finish();

                }

                else if (messageButton.getText().equals("Mark as Sold")) {
                    FirebaseAuth mAuth = FirebaseAuth.getInstance();
                    DatabaseReference productReference = FirebaseDatabase.getInstance("https://e-marcket-68d42-default-rtdb.europe-west1.firebasedatabase.app")
                            .getReference("Products").child(productID+productTitle);
                    productReference.removeValue();

                    productReference.removeValue(new DatabaseReference.CompletionListener() {
                        @Override
                        public void onComplete(@Nullable DatabaseError error, @NonNull DatabaseReference ref) {
                            if (error == null) {
                                // The delete operation was successful.
                                Toast.makeText(ProductPage.this, "This product is marked as sold", Toast.LENGTH_SHORT).show();

                                Intent intent = new Intent(ProductPage.this, MainDashBoard.class);
                                startActivity(intent);
                                finish();
                            } else {
                                // There was an error deleting the data.
                            }
                        }
                    });




                } else {
                    Toast.makeText(ProductPage.this, "No user", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

}


