package com.example.team24p;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.Gravity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;
import com.google.android.material.textfield.TextInputEditText;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import java.util.ArrayList;
import java.util.Map;

public class AdminActivity extends AppCompatActivity {
    private ListView usersListView;
    private ArrayList<String> items;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRefUsersDetails = mDatabase.getReference().child("Users"); //connect to relevant table in db
    String userNameLoggedIn="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin);
        //init and clear items list to avoid duplicate values from other opening activity
        items = new ArrayList<>();
        items.clear();
        final TextInputEditText searchInput = (TextInputEditText)findViewById(R.id.searchUsers);

        usersListView = (ListView)findViewById(R.id.userListView);
        mRefUsersDetails.addValueEventListener(new ValueEventListener() {
            @RequiresApi(api = Build.VERSION_CODES.O)
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) { //add users from db to hash so we can use them later
                Map<String, Object> usersAll = (Map<String, Object>) dataSnapshot.getValue();
                for (Object key : usersAll.values()) {
                    Map<String, Object> singleUser = (Map<String, Object>) key;
                    for (Object key2 : singleUser.keySet()) {
                        if ((key2.toString().equals("username"))) {
                            String username = singleUser.get("username").toString();
                                items.add(username);
                        }
                    }
                }
// create list item of users that we can edit from admin users.
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,items);
                usersListView.setAdapter(null);
                usersListView.setAdapter(adapter);
                adapter.notifyDataSetChanged();
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        searchInput.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) { //this part is for the search users
                String text=searchInput.getText().toString(); //getting the text from user
                int textLength = searchInput.length();
                ArrayList<String> searchResults = new ArrayList<String>();
                ArrayList<String> empty = new ArrayList<String>();
                searchResults.clear(); //clear the current list
                ArrayAdapter<String> adapter = null;
                for(int i=0;i<items.size();i++) // set the list with search text
                {
                    String name=items.get(i);
                    if(textLength<=name.length()){
                        //compare the String in EditText with Names in the ArrayList
                        if(text.equalsIgnoreCase(name.substring(0,textLength))){
                            searchResults.add(items.get(i));
                            adapter= new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,searchResults);

                        }

                    }


                }
                if(searchResults.isEmpty()){ // if user type unregistered user it will show this text
                    Toast toast= Toast.makeText(getApplicationContext(),
                            "No Items Matched", Toast.LENGTH_SHORT);
                    toast.setGravity(Gravity.CENTER_VERTICAL|Gravity.CENTER_HORIZONTAL, 0, 0);
                    toast.show();

                    adapter= new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,empty);
                }

                    usersListView.setAdapter(null);
                    usersListView.setAdapter(adapter);
                    adapter.notifyDataSetChanged();

            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });

        usersListView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?>adapter, View v, int position, long id) { //after click on some user it will go to edituser activity
                String user = adapter.getItemAtPosition(position).toString();
                System.out.println(user); //just to check that click do something

                Intent stuserAdmin = new Intent(getApplicationContext(), userAdminActivity.class);
                stuserAdmin.putExtra("userNameLoggedIn",getIntent().getStringExtra("userNameLoggedIn"));
                stuserAdmin.putExtra("userToEdit",user);
                stuserAdmin.putExtra("isAdmin",getIntent().getStringExtra("isAdmin")); //pass values to userAdminActivity
                startActivity(stuserAdmin);


            }
        });


    }
}
