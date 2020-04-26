package com.example.team24p;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

public class userAdminActivity extends AppCompatActivity {

    TextView userToEditTextView;
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

        editUserButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stEditUser = new Intent (getApplicationContext(),EditUserDetailsActivity.class);
                stEditUser.putExtra("userNameLoggedIn",userNameLoggedIn);
                stEditUser.putExtra("userToEdit",userToEdit);
                startActivity(stEditUser);


            }
        });


    }
}
