package com.example.test1.activity;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.example.test1.R;
import com.google.firebase.auth.FirebaseAuth;

import java.util.Random;

public class MenuActivity extends AppCompatActivity {

    AppCompatButton btnShowProducts, btnSellProducts, btnChat, btnLogout,btnAbout;
    TextView tips;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_menu);

        btnShowProducts = findViewById(R.id.btnShowProducts);
        btnSellProducts = findViewById(R.id.btnSellProducts);
        btnChat = findViewById(R.id.btnChat);
        btnLogout = findViewById(R.id.btnLogout);
        tips = findViewById(R.id.tips);
        btnAbout = findViewById(R.id.btnAbout);





        String[] tip = {
                "Buy used items instead of new ones whenever possible. This reduces pollution.",
                "Use energy-efficient appliances and light bulbs to save on electricity.",
                "Buy local and organic produce to reduce the environmental impact of transportation and pesticides.",
                "Consider repairing items that are broken instead of replacing them.",
                "Reduce waste by recycling and composting as much as possible."};


        Random rand = new Random();
        int index = rand.nextInt(tip.length);
        tips.setText("Sustainable Tip! \n"+tip[index]);



    btnAbout.setOnClickListener(new View.OnClickListener() {
    @Override
    public void onClick(View v) {
        startActivity(new Intent(MenuActivity.this, AboutUs.class));
             }
        });

        btnShowProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, MainDashBoard.class));
            }
        });

        btnSellProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, UploadProduct.class));
            }
        });

        btnChat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MenuActivity.this, UserListActivity.class));
            }
        });

        btnLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });

    }
}