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
import com.google.firebase.firestore.EventListener;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import androidx.appcompat.app.AppCompatActivity;

public class SignUpActivity extends AppCompatActivity implements View.OnClickListener {
    private EditText editTextEmail, editTextPassword, editTextValidatePassword, editTextId,editTextFullName,editTextAddress,editTextPhoneNumber,editTextAge;
    private Button buttonRegister;
    private Button buttonLogin;
    private ProgressDialog progressDialog;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference m_up = mDatabase.getReference().child("UsersAndPasswords");
    private DatabaseReference m_users = mDatabase.getReference().child("Users");

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
        editTextId = (EditText) findViewById(R.id.IdTextView);
        editTextFullName = (EditText) findViewById(R.id.NameTextView);
        editTextAddress = (EditText) findViewById(R.id.AddressTextView);
        editTextPhoneNumber = (EditText) findViewById(R.id.PhoneTextView);
        editTextAge = (EditText) findViewById(R.id.AgeTextView);
        buttonRegister.setOnClickListener(this);
    }

    private void registerUser(){
        final String email = editTextEmail.getText().toString().trim();
        final String password = editTextPassword.getText().toString().trim();
        String validate = editTextValidatePassword.getText().toString().trim();
        final String id = editTextId.getText().toString().trim();
        final String fullName = editTextFullName.getText().toString().trim();
        final String adress = editTextAddress.getText().toString().trim();
        final String phone = editTextPhoneNumber.getText().toString().trim();
        final String age = editTextAge.getText().toString().trim();

        if (!fullName.matches("^[a-zA-Z]+ [ a-zA-Z]+$")){
            editTextFullName.setError("Please enter valid name");
            editTextFullName.requestFocus();
        }
        if (adress.isEmpty()){
            editTextAddress.setError("Address cannot be empty");
            editTextAddress.requestFocus();
        }
        if (!id.matches("^[0-9]{9}$")){
            editTextId.setError("Please enter valid id");
            editTextId.requestFocus();
        }
        if (!phone.matches("^05[0-9]{8}$")){
            editTextPhoneNumber.setError("Please enter valid phone number");
            editTextPhoneNumber.requestFocus();
        }
        if (!age.matches("^[1-9][0-9]{1,2}$")){
            editTextAge.setError("Please enter valid age");
            editTextAge.requestFocus();
        }
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
        m_up.orderByChild("UserName").equalTo(email).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                if(dataSnapshot.exists()) {
                    editTextEmail.setError("Email is already signed up");
                    editTextEmail.requestFocus();
                }
                else
                {
                    progressDialog.setMessage("Registering User...");
                    progressDialog.show();
                    Map<String, String> userData = new HashMap<String, String>();
                    userData.put("Password",password);
                    userData.put("UserName",email);
                    userData.put("isAdmin","False");
                    userData.put("enabled","True");
                    m_up.push().setValue(userData);
                    userData.clear();
                    userData.put("Name",fullName);
                    userData.put("address",adress);
                    userData.put("age",age);
                    userData.put("id",id);
                    userData.put("phone",phone);
                    userData.put("username",email);
                    m_users.push().setValue(userData);
                    Intent intent = new Intent(SignUpActivity.this , MainActivity.class);
                    intent.putExtra("userNameLoggedIn", email);
                    progressDialog.dismiss();
                    Toast.makeText(getApplicationContext(), "User registered successfully", Toast.LENGTH_SHORT).show();
                    finish();
                    startActivity(intent);

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

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
