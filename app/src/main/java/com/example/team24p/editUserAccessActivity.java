package com.example.team24p;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.TextView;
import android.widget.Toast;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.HashMap;
import java.util.Map;

public class editUserAccessActivity extends AppCompatActivity {

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference refToChange = mDatabase.getReference().child("UsersAndPasswords");
    private DatabaseReference refChangeAccess;
    private CheckBox checkBoxButton;
    private String userNameLoggedIn;
    private Button applyButton;
    private TextView userNameToEdit;
    String switchFlag = "False";
    String keyToChange = "";
    String userToEdit ="";
    Map<String, String> userData = new HashMap<String, String>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_user_access);
        applyButton = (Button)findViewById(R.id.buttonApply);
        userNameToEdit = (TextView)findViewById(R.id.userNameTextView);
        checkBoxButton = (CheckBox) findViewById(R.id.checkBox);
        userToEdit = getIntent().getStringExtra("userToEdit");
        userNameToEdit.setText(userToEdit);
        userNameLoggedIn = getIntent().getStringExtra("userNameLoggedIn");
        refToChange.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> usersList = (HashMap<String, Object>) dataSnapshot.getValue();
                for (String key : usersList.keySet()) {
                    Map<String, Object> value = (HashMap<String, Object>) usersList.get(key);
                    if (userToEdit.equalsIgnoreCase(value.get("UserName").toString())) {
                        keyToChange = key;
                        if (value.get("isAdmin").toString().equals("True")) {
                            checkBoxButton.setChecked(true);
                            switchFlag ="True";
                        }
                          userData.put("Password", value.get("Password").toString());
                          userData.put("UserName", userToEdit);
                          userData.put("enabled",value.get("enabled").toString());
                    }
                }
            }
            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        applyButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (checkBoxButton.isChecked())
                    switchFlag = "True";
                else
                    switchFlag = "False";
                userData.put("isAdmin", switchFlag);
                refChangeAccess = refToChange.child(keyToChange);
                refChangeAccess.setValue(userData);
                Toast.makeText(getApplicationContext(), "User's access change successfully", Toast.LENGTH_SHORT).show();
                Intent stEditUser = new Intent(getApplicationContext(),MainActivity.class);
                stEditUser.putExtra("userNameLoggedIn",userNameLoggedIn);
                stEditUser.putExtra("userToEdit",userToEdit);
                stEditUser.putExtra("isAdmin",getIntent().getStringExtra("isAdmin"));
                startActivity(stEditUser);
            }
        });


    }
}
