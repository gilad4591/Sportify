package com.example.team24p;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class EditUserDetailsActivity extends AppCompatActivity implements View.OnClickListener {
    EditText nameEditText,idEditText,ageEditText,adressEditText,phoneEditText;
    TextView userNameTextView;
    String userToEdit ="";
    static int flag = 0;
    String userNameLoggedIn="";
    String keyToChange;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRefUsersDetails = mDatabase.getReference().child("Users");
    private Button applyButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_details);

        userToEdit = getIntent().getStringExtra("userToEdit");
        userNameLoggedIn = getIntent().getStringExtra("userNameLoggedIn");
        userNameTextView = (TextView)findViewById(R.id.userNameTextView);
        nameEditText = (EditText)findViewById(R.id.nameEditText);
        idEditText = (EditText)findViewById(R.id.idEditText);
        ageEditText = (EditText)findViewById(R.id.ageEditText);
        adressEditText = (EditText)findViewById(R.id.adressEditText);
        phoneEditText = (EditText)findViewById(R.id.phoneEditText);
        applyButton = (Button)findViewById(R.id.buttonApply);
        applyButton.setOnClickListener(this);

        userNameTextView.setText(userToEdit);
        mRefUsersDetails.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot)
            {
                Map<String, Object> usersList = (HashMap<String, Object>) dataSnapshot.getValue();
                for (String key : usersList.keySet()) {
                    Map<String,Object> value = (HashMap<String,Object>)usersList.get(key);
                    if (userToEdit.equalsIgnoreCase(value.get("username").toString())){
                        keyToChange = key;
                        nameEditText.setText(value.get("Name").toString());
                        idEditText.setText(value.get("id").toString());
                        ageEditText.setText(value.get("age").toString());
                        adressEditText.setText(value.get("address").toString());
                        phoneEditText.setText(value.get("phone").toString());

                    }

                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }

    @Override
    public void onClick(View v) {

        if (v == applyButton){
            applyChanges();
            if (flag == 1) {
                Intent stMainActivity = new Intent(getApplicationContext(), MainActivity.class);
                stMainActivity.putExtra("userNameLoggedIn", userNameLoggedIn);
                stMainActivity.putExtra("isAdmin", getIntent().getStringExtra("isAdmin"));
                startActivity(stMainActivity);
            }
        }
    }

    private void applyChanges() {
        final String id = idEditText.getText().toString().trim();
        final String fullName = nameEditText.getText().toString().trim();
        final String adress = adressEditText.getText().toString().trim();
        final String phone = phoneEditText.getText().toString().trim();
        final String age = ageEditText.getText().toString().trim();

        EditUserDetailsActivity.flag = 0;

        if (!fullName.matches("^[a-zA-Z]+ [ a-zA-Z]+$")){
            nameEditText.setError("Please enter valid name");
            nameEditText.requestFocus();
        }
        else if (adress.isEmpty()){
            adressEditText.setError("Address cannot be empty");
            adressEditText.requestFocus();
        }
        else if (!id.matches("^[0-9]{9}$")){
            idEditText.setError("Please enter valid id");
            idEditText.requestFocus();
        }
        else if (!phone.matches("^05[0-9]{8}$")){
            phoneEditText.setError("Please enter valid phone number");
            phoneEditText.requestFocus();
        }
        else if (!age.matches("^[1-9][0-9]{1,2}$")){
            ageEditText.setError("Please enter valid age");
            ageEditText.requestFocus();

        }
        else
            EditUserDetailsActivity.flag = 1;


        final Map<String, String> userData = new HashMap<String, String>();
        userData.put("Name",fullName);
        userData.put("username",userToEdit);
        userData.put("id",id);
        userData.put("address",adress);
        userData.put("phone",phone);
        userData.put("age",age);
        DatabaseReference refChildKey = mRefUsersDetails.child(keyToChange);
        refChildKey.setValue(userData);
        if (flag == 1)
        Toast.makeText(getApplicationContext(), "User details changed successfully", Toast.LENGTH_SHORT).show();

    }
}
