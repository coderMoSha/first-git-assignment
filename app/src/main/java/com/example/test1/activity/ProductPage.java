package com.example.test1.activity;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
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
        String imageUrl = intent.getStringExtra("imageUrl");

        showProductImage =findViewById(R.id.showProductImage);
        showTitle = findViewById(R.id.showTitle);



        // display the product details in the UI components
        showTitle.setText(title);
        Picasso.get().load(imageUrl).into(showProductImage);









    }
}