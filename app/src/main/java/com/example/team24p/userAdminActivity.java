package com.example.team24p;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.HashMap;
import java.util.Map;

public class userAdminActivity extends AppCompatActivity {

    TextView userToEditTextView;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference refToDelete = mDatabase.getReference().child("UsersAndPasswords");
    private DatabaseReference refChangeToDisable;
    Button deleteUserButton;
    Button editUserButton;
    String userNameLoggedIn="";
    String userToEdit = "";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_admin);
        userNameLoggedIn = getIntent().getStringExtra("userNameLoggedIn");
        userToEditTextView = (TextView)findViewById(R.id.userToEditTextView);
        deleteUserButton = (Button)findViewById(R.id.deleteUserButton);
        editUserButton = (Button)findViewById(R.id.editUserDetailsButton);
        userToEdit = getIntent().getStringExtra("userToEdit");
        userToEditTextView.setText(userToEdit);

        deleteUserButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                refToDelete.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    {
                        String keyToChange = "";
                        Map<String, Object> usersList = (HashMap<String, Object>) dataSnapshot.getValue();
                        for (String key : usersList.keySet()) {
                            Map<String,Object> value = (HashMap<String,Object>)usersList.get(key);
                            if (userToEdit.equalsIgnoreCase(value.get("UserName").toString())){
                                keyToChange = key;
                                Map<String, String> userData = new HashMap<String, String>();
                                if (value.get("enabled").toString().equals("True")) {
                                    userData.put("Password", value.get("Password").toString());
                                    userData.put("UserName", userToEdit);
                                    userData.put("isAdmin", value.get("isAdmin").toString());
                                    userData.put("enabled", "False");
                                    refChangeToDisable = refToDelete.child(key);
                                    refChangeToDisable.setValue(userData);
                                    Toast.makeText(getApplicationContext(), "User deleted successfully", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "User already deleted", Toast.LENGTH_SHORT).show();
                                }
                                Intent stDeleteUser = new Intent (getApplicationContext(),MainActivity.class);
                                stDeleteUser.putExtra("userNameLoggedIn",userNameLoggedIn);
                                stDeleteUser.putExtra("isAdmin",getIntent().getStringExtra("isAdmin"));
                                startActivity(stDeleteUser);

                            }
                        }
                    }
                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

        editUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stEditUser = new Intent (getApplicationContext(),EditUserDetailsActivity.class);
                stEditUser.putExtra("userNameLoggedIn",userNameLoggedIn);
                stEditUser.putExtra("userToEdit",userToEdit);
                stEditUser.putExtra("isAdmin",getIntent().getStringExtra("isAdmin"));
                startActivity(stEditUser);


            }
        });


    }
}
