package com.example.test1.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
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
import java.util.Locale;

public class MainDashBoard extends AppCompatActivity {


    private RecyclerView productRecyclerView;
    private List<EnterProductDetails> dashboardUpload;
    private FirebaseAuth mAuth;
    private FirebaseUser fbUser;
    private ProductDetailsAdapter dashboardAdapter;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_recycle_dashboard);

        productRecyclerView = findViewById(R.id.productRecyclerView);
        productRecyclerView.setHasFixedSize(true);
        searchView=findViewById(R.id.search);
        productRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        dashboardUpload = new ArrayList<>();
        dashboardAdapter = new ProductDetailsAdapter(this, dashboardUpload);
        productRecyclerView.setAdapter(dashboardAdapter);

        mAuth = FirebaseAuth.getInstance();
        fbUser = mAuth.getCurrentUser();

        //location of database
        DatabaseReference usersRef = FirebaseDatabase
                .getInstance("https://e-marcket-68d42-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference().child("Products");

        usersRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                // Clear the list of dashboard items
                dashboardUpload.clear();
                // Check if the snapshot exists
                if (snapshot.exists()) {
                    // Loop through the children of the snapshot and add each one to the list of dashboard items
                    for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                        EnterProductDetails productDetails = snapshot1.getValue(EnterProductDetails.class);
                        dashboardUpload.add(productDetails);
                    }
                    // Notify the adapter that the data set has changed
                    dashboardAdapter.notifyDataSetChanged();
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if (newText.isEmpty()){
                    dashboardAdapter.setmUploads(dashboardUpload);
                }else {
                    filter(newText);
                }
                return false;
            }
        });

    }
    private void filter(String text){
        List<EnterProductDetails> list=new ArrayList<>();
        for (EnterProductDetails details: dashboardUpload){
            if (details.getTitleDB().toLowerCase(Locale.ROOT).contains(text.toLowerCase(Locale.ROOT))){
                list.add(details);
            }
        }
        dashboardAdapter.setmUploads(list);
    }

}