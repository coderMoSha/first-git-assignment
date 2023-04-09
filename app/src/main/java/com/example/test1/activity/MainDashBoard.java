package com.example.test1.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.example.test1.Adapter.ProductDetailsAdapter;
import com.example.test1.R;
import com.example.test1.viewmodels.EnterProductDetails;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;

public class MainDashBoard extends AppCompatActivity {


    private RecyclerView productRecyclerView;
    private List<EnterProductDetails> dashboardUpload;


    private FirebaseAuth mAuth;
    private FirebaseUser fbUser;
    private DatabaseReference databaseReference;
    private ProductDetailsAdapter dashboardAdapter;

    private Button logoutButton;
    private FloatingActionButton addProductPage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_recycle_dashboard);



        productRecyclerView = findViewById(R.id.productRecyclerView);
        productRecyclerView.setHasFixedSize(true);
        logoutButton = findViewById(R.id.logoutButton);
        addProductPage = findViewById(R.id.addProductPage);



        productRecyclerView.setLayoutManager(new LinearLayoutManager(this));


        dashboardUpload = new ArrayList<>();

        mAuth = FirebaseAuth.getInstance();
        fbUser = mAuth.getCurrentUser();

        DatabaseReference usersRef = FirebaseDatabase.getInstance("https://e-marcket-68d42-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference().child("Registered Users");
        usersRef.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                for (DataSnapshot childSnapshot : snapshot.getChildren()) {
                    // This loop iterates through all subchildren under "Registered Users"
                    String userId = childSnapshot.getKey();
                    DatabaseReference userRef = usersRef.child(userId);
                    // Now you have a reference to each subchild under "users"
                    // Your code to read or write data to each subchild goes here

                databaseReference = FirebaseDatabase
                .getInstance("https://e-marcket-68d42-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("Products/"+userId);



                            databaseReference.addValueEventListener(new ValueEventListener() {
                             @Override
                            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                            for(DataSnapshot postSnapshot : dataSnapshot.getChildren()){
                            EnterProductDetails productDetails = postSnapshot.getValue(EnterProductDetails.class);
                            dashboardUpload.add(productDetails);

                                    }
                                    dashboardAdapter = new ProductDetailsAdapter(MainDashBoard.this, dashboardUpload);

                                    productRecyclerView.setAdapter(dashboardAdapter);
                                }
                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    Toast.makeText(MainDashBoard.this, error.getMessage(), Toast.LENGTH_SHORT).show();



                                }
                            });

                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                // Handle error
            }
        });





























       logoutButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                FirebaseAuth.getInstance().signOut();
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });


        addProductPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), UploadProduct.class);
                startActivity(intent);
                finish();

            }
        });






    }


}