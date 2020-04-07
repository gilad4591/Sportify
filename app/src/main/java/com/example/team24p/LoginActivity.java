package com.example.team24p;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.provider.ContactsContract;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class LoginActivity extends AppCompatActivity implements View.OnClickListener {

    private Button buttonSignIn;
    private EditText editTextEmail;
    private EditText editTextPassword;
    private Button buttonSignUp;
    private ProgressDialog progressDialog;
    private FirebaseAuth firebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login2);
        firebaseAuth = FirebaseAuth.getInstance();
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
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
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

        firebaseAuth.signInWithEmailAndPassword(email,password).addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                progressDialog.dismiss();
                if (task.isSuccessful()){
                    finish();
                    startActivity(new Intent(getApplicationContext(),ProfileActivity.class));

                }
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
