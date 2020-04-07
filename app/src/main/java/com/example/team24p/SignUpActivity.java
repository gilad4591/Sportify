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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private FirebaseAuth firebaseAuth;
    private EditText editTextEmail, editTextPassword, editTextValidatePassword;
    private Button buttonRegister;
    private Button buttonLogin;
    private ProgressDialog progressDialog;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign_up);

        progressDialog = new ProgressDialog(this);
        findViewById(R.id.SignInButton).setOnClickListener(this);
        editTextValidatePassword = (EditText) findViewById(R.id.ValidatePasswordEditText);
        buttonRegister = (Button)findViewById(R.id.RegisterButton);
        buttonLogin = (Button)findViewById(R.id.SignInButton);
        editTextEmail = (EditText) findViewById(R.id.EmailTextView);
        editTextPassword = (EditText) findViewById(R.id.PasswordTextView);
        buttonRegister.setOnClickListener(this);



        firebaseAuth = FirebaseAuth.getInstance();
    }
    private void registerUser(){
        String email = editTextEmail.getText().toString().trim();
        String password = editTextPassword.getText().toString().trim();
        String validate = editTextValidatePassword.getText().toString().trim();

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
        if (!validate.equals(password)){
            editTextValidatePassword.setError("Password and validate not the same");
            editTextValidatePassword.requestFocus();
            return;

        }

        progressDialog.setMessage("Registering User...");
        progressDialog.show();


        firebaseAuth.createUserWithEmailAndPassword(email,password).addOnCompleteListener(new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()){
                    Toast.makeText(getApplicationContext(),"User Registered successfully", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(new Intent(getApplicationContext(),ProfileActivity.class));
                }else{
                    Toast.makeText(getApplicationContext(),"Couldn't registered user, please try again", Toast.LENGTH_SHORT).show();

                }
                progressDialog.hide();
            }
        });




    }
    @Override
    public void onClick(View view) {

        if (view == buttonRegister){
            registerUser();
        }
        if (view == buttonLogin)
        {
            finish();
            startActivity(new Intent(this,LoginActivity.class));
        }
    }
}
