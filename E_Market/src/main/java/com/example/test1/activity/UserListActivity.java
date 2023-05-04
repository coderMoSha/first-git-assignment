package com.example.test1.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatTextView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.view.View;

import com.example.test1.Adapter.UsersAdapter;
import com.example.test1.R;
import com.example.test1.viewmodels.EnterUserDetails;
import com.example.test1.viewmodels.MessagesDetails;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.Set;

public class UserListActivity extends AppCompatActivity {

    RecyclerView rvUsers;
    UsersAdapter adapter;
    AppCompatTextView tvNoData;

    ArrayList<EnterUserDetails> allUsersList = new ArrayList<>();
    ArrayList<MessagesDetails> allMessagesList = new ArrayList<>();
    ArrayList<EnterUserDetails> chatUsersList = new ArrayList<>();
    ArrayList<String> idList = new ArrayList<>();
    FirebaseAuth auth;
    DatabaseReference dbReferenceAllUsers;
    DatabaseReference dbReferenceAllMessages;
    ProgressDialog progressDialog;
    Set<String> partenerID = new HashSet<>();

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_list);

        //Initializing Views
        rvUsers = findViewById(R.id.rvUsers);
        tvNoData = findViewById(R.id.tvNoData);

        //Initializing auth and database
        auth = FirebaseAuth.getInstance();
        dbReferenceAllUsers = FirebaseDatabase.getInstance("https://e-marcket-68d42-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("Registered Users");
        dbReferenceAllMessages = FirebaseDatabase.getInstance("https://e-marcket-68d42-default-rtdb.europe-west1.firebasedatabase.app").getReference("Chats");

        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Going Good...");
        progressDialog.setMessage("It takes Just a few Seconds... ");
        progressDialog.setIcon(R.drawable.happy);
        progressDialog.show();

        getData();
        setAdapters();

    }

    private void getData() {
        dbReferenceAllMessages.orderByChild("receiverId").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                String senderID = snapshot1.child("senderId").getValue(String.class);
                                partenerID.add(senderID);
                            }
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {

                    }
                });

        dbReferenceAllMessages.orderByChild("senderId").equalTo(FirebaseAuth.getInstance().getCurrentUser().getUid()).
                addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot snapshot) {
                        if (snapshot.exists()) {
                            for (DataSnapshot snapshot1 : snapshot.getChildren()) {
                                String senderID = snapshot1.child("receiverId").getValue(String.class);
                                partenerID.add(senderID);
                            }
                        }

                        for (String id : partenerID) {
                            tvNoData.setVisibility(View.GONE);
                            dbReferenceAllUsers.child(id).addListenerForSingleValueEvent(new ValueEventListener() {
                                @Override
                                public void onDataChange(@NonNull DataSnapshot snapshot) {
                                    progressDialog.dismiss();
                                    if (snapshot.exists()) {
                                        EnterUserDetails userDetails = snapshot.getValue(EnterUserDetails.class);
                                        chatUsersList.add(userDetails);
                                        setAdapters();
                                    }
                                }

                                @Override
                                public void onCancelled(@NonNull DatabaseError error) {
                                    progressDialog.dismiss();
                                }
                            });
                        }
                        if (chatUsersList.isEmpty()){
                            progressDialog.dismiss();
                        }
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError error) {
                        progressDialog.dismiss();
                    }
                });



    }

    private void setAdapters() {
        adapter = new UsersAdapter(this, chatUsersList);
        rvUsers.setLayoutManager(new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false));
        rvUsers.setAdapter(adapter);
    }
}