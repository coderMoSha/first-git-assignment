package com.example.test1;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.FirebaseApp;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

public class Register extends AppCompatActivity {


    EditText firstnameInput, lastnameInput, dobInput, emailInput, passInput, repassInput;
    Button regButton, loginButton;

    FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        firstnameInput = findViewById(R.id.firstnameInput);
        lastnameInput = findViewById(R.id.lastnameInput);
        dobInput = findViewById(R.id.dobInput);
        repassInput = findViewById(R.id.repassInput);
        passInput = findViewById(R.id.passInput);
        emailInput = findViewById(R.id.emailInput);

        regButton = findViewById(R.id.regButton);
        loginButton = findViewById(R.id.loginButton);

        mAuth = FirebaseAuth.getInstance();


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Login.class);
                startActivity(intent);
                finish();
            }
        });


        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String firstname, lastname, email, dob, password, repassword;
                firstname = String.valueOf(firstnameInput.getText());
                lastname = String.valueOf(lastnameInput.getText());
                dob = String.valueOf(dobInput.getText());
                email = String.valueOf(emailInput.getText());
                password = String.valueOf(passInput.getText());
                repassword = String.valueOf(repassInput.getText());

                System.out.println(firstname + lastname + dob);

         /*       if (TextUtils.isEmpty(firstname)) {
                    Toast.makeText(Register.this, "Please Enter Your First Name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(lastname)) {
                    Toast.makeText(Register.this, "Please Enter Your Last Name", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Register.this, "Please Enter Your Email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Register.this, "Please Enter A Password", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(repassword)) {
                    Toast.makeText(Register.this, "Please Confirm the Password", Toast.LENGTH_SHORT).show();
                    return;


                } */

                userRegister(firstname, lastname, dob, email,password);
            }

                private void userRegister (String firstname, String lastname, String dob, String email, String password){


                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {



                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information


                                    FirebaseUser fbUser = mAuth.getCurrentUser();


                                    EnterUserDetails userDetail = new EnterUserDetails(firstname, lastname, dob);



                                    DatabaseReference reference = FirebaseDatabase.getInstance("https://e-marcket-68d42-default-rtdb.europe-west1.firebasedatabase.app")
                                            .getReference("Registered Users");


                                    reference.child(fbUser.getUid()).setValue(userDetail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {
                                                System.out.println("printttttttttttttt" + task.isSuccessful());



                                                fbUser.sendEmailVerification();
                                                Toast.makeText(Register.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);

                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);


                                                startActivity(intent);
                                            } else {

                                                Toast.makeText(Register.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                                            }


                                        }
                                    });


                                } else {
                                    // If sign in fails, display a message to the user.
                                    Toast.makeText(Register.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                                }
                            }
                        });

            }
                    // }



        });


    }
}