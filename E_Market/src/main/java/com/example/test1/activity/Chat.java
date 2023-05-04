package com.example.test1.activity;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.test1.Adapter.MessageAdapter;
import com.example.test1.R;
import com.example.test1.viewmodels.EnterUserDetails;
import com.example.test1.viewmodels.MessagesDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class Chat extends AppCompatActivity {

    //Creating Variables
    private MessageAdapter adapter;
    private RecyclerView chatRecycleView;
    private List<MessagesDetails> chatList;
    private EditText etMessage;
    private FirebaseUser currentUser;
    private LinearLayoutManager linearLm;
    ImageView ivSend, ivLink, ivImage;
    TextView username;
    CardView cvImage;
    String strImageURI = "";

    private FirebaseAuth mAuth;
    private DatabaseReference messagesRef, usersRef;
    private StorageReference storageReference;
    private String userId;
    ProgressDialog progressDialog;

    int PICK_IMAGE = 123;
    Uri imageUri;

    private String chatReceiverID, chatReceiverName, chatSenderID;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        ivLink = findViewById(R.id.ivLink);
        ivSend = findViewById(R.id.ivSend);
        username = findViewById(R.id.username);
        etMessage = findViewById(R.id.etMessage);
        chatRecycleView = findViewById(R.id.chatRecycleView);
        cvImage = findViewById(R.id.cvImage);
        ivImage = findViewById(R.id.ivImage);

        //Initializing arrayList and Setting the Adapter
        chatList = new ArrayList<>();
        adapter = new MessageAdapter(this, chatList);
        chatRecycleView.setLayoutManager(new LinearLayoutManager(this));
        chatRecycleView.setAdapter(adapter);
        //Initializing the Progress Dialog
        progressDialog = new ProgressDialog(this);
        //Initializing the Firebase Authentication
        mAuth = FirebaseAuth.getInstance();
        //Getting Data from previous Adapter
        userId = getIntent().getStringExtra("userId");
        //Initializing the Firebase Database for messages
        messagesRef = FirebaseDatabase.getInstance("https://e-marcket-68d42-default-rtdb.europe-west1.firebasedatabase.app").getReference("Chats");
        //Initializing the Firebase Database for user info
        usersRef = FirebaseDatabase.getInstance("https://e-marcket-68d42-default-rtdb.europe-west1.firebasedatabase.app")
                .getReference("Registered Users");
        //Initializing the Firebase Storage for images
        storageReference = FirebaseStorage.getInstance().getReference("ChatImages");
        //Calling send Message method when click on send image
        ivSend.setOnClickListener(view -> sendMessage());
        //Clicking on link and moving to gallery
        ivLink.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Profile Image"), PICK_IMAGE);
            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        //Calling methods for loading the User Details
        loadUserDetails();
        //Calling methods for loading the Messages
        loadMessages();
    }

    private void loadMessages() {
        //Calling Method to get the data
        messagesRef.addValueEventListener(new ValueEventListener() {
            @SuppressLint("NotifyDataSetChanged")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                //Clearing the list
                chatList.clear();
                for (DataSnapshot snapshot : dataSnapshot.getChildren()) {
                    MessagesDetails message = snapshot.getValue(MessagesDetails.class);
                    //Putting data into list
                    if (message.getSenderId().equals(mAuth.getCurrentUser().getUid()) && message.getReceiverId().equals(userId)) {
                        chatList.add(message);
                    } else if (message.getSenderId().equals(userId) && message.getReceiverId().equals(mAuth.getCurrentUser().getUid())) {
                        chatList.add(message);
                    }
                }
                //notifying the adapter
                adapter.notifyDataSetChanged();
                chatRecycleView.scrollToPosition(chatList.size() - 1);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    //Send Message Method
    private void sendMessage() {
        @SuppressLint("SimpleDateFormat")
        //Getting Current date
        String formattedDate = new SimpleDateFormat("dd-MM-yyyy").format(Calendar.getInstance().getTime());
        String messageText = etMessage.getText().toString().trim();
        //Checking if image string is empty
        if (strImageURI.isEmpty()){
            //If input text is empty showing the toast message
            if (messageText.isEmpty()){
                Toast.makeText(this, "Please write message", Toast.LENGTH_SHORT).show();
            }else{
                //If image is empty and text is not empty then storing data into database with type message and image empty
                String messageId = messagesRef.push().getKey();
                MessagesDetails message = new MessagesDetails(messageId, mAuth.getCurrentUser().getUid(), userId, messageText, formattedDate, "", "message");
                messagesRef.child(messageId).setValue(message);
                etMessage.setText("");
            }
        }else{
            //If the image contains the data
            //if message is empty then calling the send Image Method and putting the message empty
            if (messageText.isEmpty()){
                sendImageMessage("", formattedDate);
            }else{
                //if message is non empty then calling the send Image Method and putting the message text
                sendImageMessage(messageText, formattedDate);
            }

        }
    }

    private void sendImageMessage(String message, String date){
        // Show Progress Dialog
        progressDialog.setTitle("Going Good...");
        progressDialog.setMessage("It takes Just a few Seconds... ");
        progressDialog.setIcon(R.drawable.happy);
        progressDialog.setCancelable(false);
        progressDialog.show();
        //Making child of image path
        StorageReference imageReference = storageReference.child(imageUri.getLastPathSegment());
        //Putting image into firebase storage
        imageReference.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {
                //Downloading the url of the image from storage
                imageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        //Storing the uri of image into string
                        String downloadedUrl = uri.toString();
                        String messageId = messagesRef.push().getKey();
                        //Passing values into model
                        MessagesDetails model = new MessagesDetails(messageId, mAuth.getCurrentUser().getUid(), userId, message, date, downloadedUrl, "image");
                        //Putting all the data into Firebase Database
                        messagesRef.child(messageId).setValue(model).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                if (task.isSuccessful()){
                                    strImageURI = "";
                                    etMessage.setText("");
                                    cvImage.setVisibility(View.GONE);
                                    progressDialog.dismiss();
                                }
                            }
                        }).addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                progressDialog.dismiss();
                                Toast.makeText(Chat.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                            }
                        });
                    }
                }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Toast.makeText(Chat.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                    }
                });

            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                progressDialog.dismiss();
                Toast.makeText(Chat.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void loadUserDetails() {
        //Reading the data from firebase to show the username
        usersRef.child(userId).addListenerForSingleValueEvent(new ValueEventListener() {
            @SuppressLint("SetTextI18n")
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                EnterUserDetails user = dataSnapshot.getValue(EnterUserDetails.class);
                username.setText(user.fnameDB + " " + user.lnameDB);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE && resultCode == RESULT_OK && data.getData() != null) {
            imageUri = data.getData();
            strImageURI = String.valueOf(imageUri);
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), imageUri);
                ivImage.setImageBitmap(bitmap);
                cvImage.setVisibility(View.VISIBLE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

}