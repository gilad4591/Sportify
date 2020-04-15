package com.example.team24p;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class UsersInGame extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_in_game);
        ListView userView = (ListView)findViewById(R.id.userListView);

        ArrayList<User> UserArrayList = (ArrayList<User>)getIntent().getSerializableExtra("userlist");

        ArrayList<String> items = new ArrayList<>();
        for(User us : UserArrayList){
                items.add(us.getName());
            }


        ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,items);
        userView.setAdapter(adapter);
    }
}
