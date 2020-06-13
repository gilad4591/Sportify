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
    private DatabaseReference refToDelete = mDatabase.getReference().child("UsersAndPasswords"); //connect to relevant table from db
    private DatabaseReference refChangeToDisable; // refernce if need to delete user
    Button deleteUserButton;
    Button editAccessButton;
    Button editUserButton;
    String userNameLoggedIn="";
    String userToEdit = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        //--------init and link buttons
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_admin);
        userNameLoggedIn = getIntent().getStringExtra("userNameLoggedIn");
        userToEditTextView = (TextView)findViewById(R.id.userToEditTextView);
        deleteUserButton = (Button)findViewById(R.id.deleteUserButton);
        editUserButton = (Button)findViewById(R.id.editUserDetailsButton);
        editAccessButton = (Button)findViewById(R.id.buttonEditAccess);
        userToEdit = getIntent().getStringExtra("userToEdit");
        userToEditTextView.setText(userToEdit);

        deleteUserButton.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {

                refToDelete.addValueEventListener(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot)
                    { //---move strings to hashmap so we can user them
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
                                    // if user deleted it will change the "enabled" part to false, then the user wont be able to connect.
                                    // if we want to delete it permnantely we need to delete from db, its just safe delete.
                                    refChangeToDisable = refToDelete.child(key);
                                    refChangeToDisable.setValue(userData);
                                    Toast.makeText(getApplicationContext(), "User deleted successfully", Toast.LENGTH_SHORT).show();
                                }
                                else {
                                    Toast.makeText(getApplicationContext(), "User already deleted", Toast.LENGTH_SHORT).show();
                                    //if we try to delete already deleted user
                                }
                                Intent stDeleteUser = new Intent(getApplicationContext(),MainActivity.class);
                                // pass args to other activity
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
                Intent stEditUser = new Intent(getApplicationContext(),EditUserDetailsActivity.class);
                // pass args to other activity
                stEditUser.putExtra("userNameLoggedIn",userNameLoggedIn);
                stEditUser.putExtra("userToEdit",userToEdit);
                stEditUser.putExtra("isAdmin",getIntent().getStringExtra("isAdmin"));
                startActivity(stEditUser);


            }
        });

        editAccessButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stEditUserAccess = new Intent(getApplicationContext(),editUserAccessActivity.class);
                // pass args to other activity
                stEditUserAccess.putExtra("userNameLoggedIn",userNameLoggedIn);
                stEditUserAccess.putExtra("userToEdit",userToEdit);
                stEditUserAccess.putExtra("isAdmin",getIntent().getStringExtra("isAdmin"));
                startActivity(stEditUserAccess);
            }
        });


    }
}
