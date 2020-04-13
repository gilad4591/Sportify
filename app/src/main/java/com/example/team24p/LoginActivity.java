package com.example.team24p;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonSignIn;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonSignUp;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRef = mDatabase.getReference().child("UsersAndPasswords");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
//        firebaseAuth = FirebaseAuth.getInstance();
//        if (firebaseAuth.getCurrentUser()!= null){
//            finish();
//            startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
//        }


        editTextPassword = (EditText) findViewById(R.id.EditTextPassword);
        editTextEmail =(EditText) findViewById(R.id.EditTextEmail);
        buttonSignIn = (Button) findViewById(R.id.loginButton);
        buttonSignUp = (Button) findViewById(R.id.signUpButton);

        progressDialog = new ProgressDialog(this);

        buttonSignUp.setOnClickListener(this);
        buttonSignIn.setOnClickListener(this);
    }

    private void userLogin() {
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        if (email.isEmpty()){
            editTextEmail.setError("Email is required");
            editTextEmail.requestFocus();
            return;
        }
        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            editTextEmail.setError("Please enter a valid email");
            editTextEmail.requestFocus();
            return;
        }
        if (password.isEmpty()){
            editTextPassword.setError("Password is required");
            editTextPassword.requestFocus();
            return;
        }
        if (password.length() < 6){
            editTextPassword.setError("Minimum length of password should be 6");
            editTextPassword.requestFocus();
            return;
        }
        progressDialog.setMessage("Login User, please wait");
        progressDialog.show();
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                ArrayList<UsersAndPasswords> userAndPassArrayList =new ArrayList<>();
                ArrayList<Map<String, String>> usersList = (ArrayList<Map<String, String>>) dataSnapshot.getValue();
                for (Map<String, String> entry : usersList) {
                    UsersAndPasswords userAndPass = new UsersAndPasswords();
                    for (String key : entry.keySet()) {
                        String value = String.valueOf(entry.get(key));
                        System.out.println(key + ":" + value);
                        switch (key) {
                            case "UserName":
                                userAndPass.setUserName(value);
                                break;
                            case "Password":
                                userAndPass.setPassword(value);
                                break;
                        }

                    }
                    userAndPassArrayList.add(userAndPass);
                }
                progressDialog.dismiss();
                int succeded = 0;
                for(UsersAndPasswords us : userAndPassArrayList){
                    if (email.equalsIgnoreCase(us.getUserName()) && password.equals(us.getPassword())) {
                        Toast.makeText(getApplicationContext(), "User login successfully", Toast.LENGTH_SHORT).show();
                        finish();
                        succeded = 1;
                        Intent intent = new Intent(LoginActivity.this , MainActivity.class);
                        intent.putExtra("userNameLoggedIn", email);

                        startActivity(new Intent(getApplicationContext(), MainActivity.class));
                    }
                }
                if (succeded == 0) {
                    Toast.makeText(getApplicationContext(), "User or password incorrect", Toast.LENGTH_SHORT).show();
                    return;
                }

            }



            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

            @Override
    public void onClick(View view) {
        if (view == buttonSignIn){
            userLogin();
        }
        if (view == buttonSignUp){
            finish();
            startActivity(new Intent(this,SignUpActivity.class));
        }
    }


}
