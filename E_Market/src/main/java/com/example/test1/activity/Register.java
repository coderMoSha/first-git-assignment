package com.example.test1.activity;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import com.example.test1.R;
import com.example.test1.viewmodels.EnterUserDetails;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;


import java.util.Calendar;

public class Register extends AppCompatActivity {


    EditText firstnameInput, lastnameInput, dobInput, emailInput, passInput, repassInput;
    Button regButton, loginButton;
    RadioGroup genderRadioGroup;

    RadioButton selectedGenderButton;

    FirebaseAuth mAuth;
    DatePickerDialog datePicker;
    public static String TAG = "Register";

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
        genderRadioGroup = findViewById(R.id.genderRadioGroup);
        genderRadioGroup.clearCheck();

        regButton = findViewById(R.id.regButton);
        loginButton = findViewById(R.id.loginButton);

        mAuth = FirebaseAuth.getInstance();

        dobInput.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final Calendar dobCalender = Calendar.getInstance();
                int day = dobCalender.get(Calendar.DAY_OF_MONTH);
                int month =dobCalender.get(Calendar.MONTH);
                int year =  dobCalender.get(Calendar.YEAR);


                datePicker = new DatePickerDialog(Register.this, new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker datePicker, int year, int month, int dayOfMonth) {

                        dobInput.setText(dayOfMonth+"/"+(month+ 1) +"/"+year);

                    }
                },year,month,day);

                long restrict13 = dobCalender.getTimeInMillis() - 410280000000L;
                datePicker.getDatePicker().setMaxDate(restrict13);

                datePicker.show();
            }
        });


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
                String firstname, lastname, email, dob, password, repassword, gender;
                int genderId;
                firstname = String.valueOf(firstnameInput.getText());
                lastname = String.valueOf(lastnameInput.getText());
                dob = String.valueOf(dobInput.getText());
                email = String.valueOf(emailInput.getText());
                password = String.valueOf(passInput.getText());
                repassword = String.valueOf(repassInput.getText());




                genderId = genderRadioGroup.getCheckedRadioButtonId();
                selectedGenderButton = findViewById(genderId);










                if (TextUtils.isEmpty(firstname)) {
                    Toast.makeText(Register.this, "Please enter your first name", Toast.LENGTH_SHORT).show();
                    firstnameInput.requestFocus();
                }

                else if (TextUtils.isEmpty(lastname)) {
                    Toast.makeText(Register.this, "Please enter your last name", Toast.LENGTH_SHORT).show();
                    lastnameInput.requestFocus();
                }

                else if(genderRadioGroup.getCheckedRadioButtonId() == -1){
                    Toast.makeText(Register.this, "Please select your gender", Toast.LENGTH_SHORT).show();
                    genderRadioGroup.requestFocus();
                }
                else if (TextUtils.isEmpty(dob)) {
                    Toast.makeText(Register.this, "Please enter your date of birth", Toast.LENGTH_SHORT).show();
                    dobInput.requestFocus();
                }

                else if (TextUtils.isEmpty(email)) {
                    Toast.makeText(Register.this, "Please enter your email", Toast.LENGTH_SHORT).show();
                    emailInput.requestFocus();
                }


                else if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){

                    Toast.makeText(Register.this, "Please enter a valid email", Toast.LENGTH_SHORT).show();
                    emailInput.requestFocus();

                }
                else if (TextUtils.isEmpty(repassword)) {
                    Toast.makeText(Register.this, "Please confirm the password", Toast.LENGTH_SHORT).show();
                    repassInput.requestFocus();

                }





                else if (TextUtils.isEmpty(password)) {
                    Toast.makeText(Register.this, "Please enter a password", Toast.LENGTH_SHORT).show();
                    passInput.requestFocus();
                    return;
                }

                else if (TextUtils.isEmpty(repassword)) {
                    Toast.makeText(Register.this, "Please Confirm the password", Toast.LENGTH_SHORT).show();
                    return;
                } else if (!password.equals(repassword)) {
                    Toast.makeText(Register.this,"Please re-enter your password",Toast.LENGTH_SHORT).show();

                    repassInput.clearComposingText();
                    passInput.clearComposingText();
                    repassInput.requestFocus();
                }

                else{
                    gender = selectedGenderButton.getText().toString();
                    userRegister(firstname, lastname, dob, gender, email,password);

                }



            }

                private void userRegister (String firstname, String lastname, String dob, String gender, String email, String password){


                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(Register.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {



                                if (task.isSuccessful()) {
                                    // Sign in success, update UI with the signed-in user's information


                                    FirebaseUser fbUser = mAuth.getCurrentUser();


                                    EnterUserDetails userDetail = new EnterUserDetails(firstname, lastname, dob, gender, mAuth.getCurrentUser().getUid());



                                    DatabaseReference reference = FirebaseDatabase.getInstance("https://e-marcket-68d42-default-rtdb.europe-west1.firebasedatabase.app")
                                            .getReference("Registered Users");


                                    reference.child(fbUser.getUid()).setValue(userDetail).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {
                                            if (task.isSuccessful()) {

                                              //  fbUser.sendEmailVerification();
                                                Toast.makeText(Register.this, "Account Created Successfully", Toast.LENGTH_SHORT).show();

                                                Intent intent = new Intent(getApplicationContext(), MenuActivity.class);

                                                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);


                                                startActivity(intent);
                                            } else {

                                                Toast.makeText(Register.this, "Authentication Failed.", Toast.LENGTH_SHORT).show();
                                            }


                                        }
                                    });


                                } else {
                                    try {
                                        throw task.getException();

                                    }catch (FirebaseAuthWeakPasswordException e){
                                        passInput.setError("Password is too weak, Please use a mix of alphabets, numbers and special characters");
                                        passInput.requestFocus();
                                    }
                                    catch (FirebaseAuthInvalidCredentialsException e){
                                        emailInput.setError("Email is invalid or already in use");
                                        emailInput.requestFocus();
                                    }
                                    catch (FirebaseAuthUserCollisionException e){
                                        emailInput.setError("Email is already in use");
                                        emailInput.requestFocus();
                                    }
                                    catch (Exception e) {
                                        Log.e(TAG, e.getMessage());
                                        Toast.makeText(Register.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                                    }
                                }
                            }
                        });

            }
                    



        });


    }
}