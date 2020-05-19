package com.example.team24p;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
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
import java.util.HashMap;
import java.util.Map;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonSignIn;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonSignUp;
    private ProgressDialog progressDialog;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRef = mDatabase.getReference().child("UsersAndPasswords");

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);


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
                int succeded = 0;
                SharedPreferences.Editor editor = getSharedPreferences("mypref",MODE_PRIVATE).edit();
                Map<String, Object> usersList = (HashMap<String, Object>) dataSnapshot.getValue();
                    for (String key : usersList.keySet()) {
                        Map<String,Object> value = (HashMap<String,Object>)usersList.get(key);
                        if (email.equalsIgnoreCase(value.get("UserName").toString()) && password.equals(value.get("Password").toString()) && value.get("enabled").toString().equals("True"))  {
                            String isAdmin = value.get("isAdmin").toString();
                            Toast.makeText(getApplicationContext(), "User logged in successfully", Toast.LENGTH_SHORT).show();
                            finish();
                            succeded = 1;
                            //put your value
                            editor.putString("name", email);
                            editor.putString("isAdmin",isAdmin);
                            //commits your edits
                            editor.apply();
                            Intent intent = new Intent(LoginActivity.this , MainActivity.class);
                            intent.putExtra("userNameLoggedIn", email);
                            intent.putExtra("isAdmin",isAdmin);
                            MainActivity.count+=1;
                            startActivity(intent);

                    }

                progressDialog.dismiss();
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
