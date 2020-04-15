package com.example.team24p;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UsersInGame extends AppCompatActivity {

    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRef;
    private ArrayList<User> UserArrayList;
    private int flag = 0;
    private String emailUserLoggedIn;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_in_game);
        //mRef = mDatabase.getReference().child("Events").child("userlist").child();
        ListView userView = (ListView)findViewById(R.id.userListView);
        Intent i = getIntent();
        UserArrayList =  (ArrayList<User>)i.getSerializableExtra("userlistGame");
        emailUserLoggedIn = i.getStringExtra("userNameLoggedIn");
        //String emailUserLoggedIn = "";
        Button joinButton = (Button)findViewById(R.id.joinButton);


        if((emailUserLoggedIn!=null))
        {
            for(User us:UserArrayList){//if exist in game already
                if(emailUserLoggedIn.equals(us.getUserName()))flag=1;
            }
            if(flag==1)joinButton.setVisibility(View.INVISIBLE);
        }
        else {
            joinButton.setVisibility(View.INVISIBLE);
            Toast.makeText(UsersInGame.this, "You are not logged in. \n log in before join game" , Toast.LENGTH_SHORT).show();
        }



        ArrayList<String> items = new ArrayList<>();
        for(User us : UserArrayList){
                items.add(us.getName());
            }

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,items);
        userView.setAdapter(adapter);
    }
}
