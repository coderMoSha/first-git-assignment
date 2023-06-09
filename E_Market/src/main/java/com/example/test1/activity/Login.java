package com.example.test1.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.test1.R;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthInvalidUserException;
import com.google.firebase.auth.FirebaseUser;

public class Login extends AppCompatActivity {

    EditText emailInput, passInput;
    Button regButton, loginButton;
    FirebaseAuth mAuth;
    public static String TAG = "Login";

    @Override
    public void onStart() {
        super.onStart();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = mAuth.getCurrentUser();
        if(currentUser != null){
            Intent intent = new Intent(Login.this, MenuActivity.class);
            startActivity(intent);
            finish();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        emailInput = findViewById(R.id.emailInput);
        passInput = findViewById(R.id.passInput);
        regButton = findViewById(R.id.regButton);
        loginButton =findViewById(R.id.loginButton);

        mAuth = FirebaseAuth.getInstance();

        regButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getApplicationContext(), Register.class);
                startActivity(intent);
                finish();
            }
        });


        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String email, password;
                email = String.valueOf(emailInput.getText());
                password = String.valueOf(passInput.getText());

                if (TextUtils.isEmpty(email)){

                    Toast.makeText(Login.this,"Please Enter Your Email", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (TextUtils.isEmpty(password)){

                    Toast.makeText(Login.this,"Please Enter Your Password",Toast.LENGTH_SHORT).show();
                    return;
                }
                //Get input from users and check against the database
                mAuth.signInWithEmailAndPassword(email, password)
                        .addOnCompleteListener( new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                //check If the user email and password is correct
                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information
                                    Toast.makeText(Login.this,"Log In Successfully", Toast.LENGTH_SHORT).show();
                                    //Instance of current user
                                    FirebaseUser user = mAuth.getCurrentUser();

                                    Intent intent = new Intent(getApplicationContext(), MenuActivity.class);

                                    //intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP|Intent.FLAG_ACTIVITY_CLEAR_TASK| Intent.FLAG_ACTIVITY_NEW_TASK);

                                    startActivity(intent);
                                    finish();



                                } else {
                                    // If sign in fails, display a message to the user.

                                    try{
                                        //throws exception to handle errors or unauthorised user
                                        throw task.getException();
                                        //exception if a user input wrong email or password
                                    } catch (FirebaseAuthInvalidCredentialsException e) {
                                        //message for user
                                        Toast.makeText(Login.this, "Incorrect email or password", Toast.LENGTH_SHORT).show();
                                        //focus on email input text box
                                        emailInput.requestFocus();
                                        //exception if the user is invalid
                                    } catch (FirebaseAuthInvalidUserException e){
                                        Toast.makeText(Login.this, "Invalid user, Please register", Toast.LENGTH_SHORT).show();
                                        emailInput.requestFocus();
                                        //general exception if any other error occurs
                                    } catch (Exception e){
                                        Log.e(TAG, e.getMessage());
                                        Toast.makeText(Login.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }

                                }

                            }
                        });
            }
        });

    }
}