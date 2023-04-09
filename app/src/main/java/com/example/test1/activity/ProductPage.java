package com.example.test1.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.test1.R;
import com.squareup.picasso.Picasso;

public class ProductPage extends AppCompatActivity {

    private ImageView showProductImage;
    private TextView showTitle, showDescription, showPrice, showCondition, showLocation;
    private Button messageButton, backToDashboardButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_page);

        Intent intent = getIntent();
        String title = intent.getStringExtra("title");
        String price = intent.getStringExtra("price");
        String description = intent.getStringExtra("description");
        String location = intent.getStringExtra("location");
        String condition = intent.getStringExtra("condition");
        String imageUrl = intent.getStringExtra("imageUrl");

        showProductImage =findViewById(R.id.showProductImage);
        showTitle = findViewById(R.id.showTitle);
        showPrice = findViewById(R.id.showPrice);
        showDescription = findViewById(R.id.showDescription);
        showCondition = findViewById(R.id.showCondition);
        showLocation = findViewById(R.id.showLocation);

        backToDashboardButton = findViewById(R.id.backToDashboardButton);



        // display the product details in the UI components
        showTitle.setText(title);
        showPrice.setText("£"+price);
        showCondition.setText("Condition: " + condition);
        showDescription.setText("Description: \n"+description);
        showLocation.setText("Location: "+location);

        Picasso.get().load(imageUrl).into(showProductImage);








        backToDashboardButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });


    }



}


