package com.example.team24p;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.SparseBooleanArray;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class UsersInGame extends AppCompatActivity {

    private ArrayList<String> listPeople;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRef;
    private ArrayList<Events> eventlistGame;
    private ArrayList<User> UserArrayList;
    private int flag = 0;
    private ListView userView;
    private String emailUserLoggedIn,age,phone,name,maxP,type;
    private String hour,groundName,date;
    private ImageView basket,tennis,soccer;
    private TextView numOfus;
    private FloatingActionButton inviteBut;
    private TextView inviteText;
    public View inviteWindow;
    public Map<Integer, Boolean> booleanArray;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_users_in_game);
        numOfus = (TextView)findViewById(R.id.numOfUs);
        inviteBut = (FloatingActionButton)findViewById(R.id.inviteButton);
        inviteText = (TextView)findViewById(R.id.inviteText);
        userView = (ListView)findViewById(R.id.userListView);
        Intent i = getIntent();
        userView.setAdapter(null);
        booleanArray = new HashMap<Integer, Boolean>();
        listPeople = new ArrayList<>();
        inviteWindow = (View)findViewById(R.id.view2);
        eventlistGame =  (ArrayList<Events>)i.getSerializableExtra("eventlistGame");
        emailUserLoggedIn = i.getStringExtra("userNameLoggedIn");
        hour = i.getStringExtra("hour");
        groundName = i.getStringExtra("markerName");
        date = i.getStringExtra("date");
        inviteBut.setVisibility(View.INVISIBLE);
        inviteText.setVisibility(View.INVISIBLE);
        basket = (ImageView)findViewById(R.id.basketball);
        tennis = (ImageView)findViewById(R.id.tennis);
        soccer = (ImageView)findViewById(R.id.soccer);


        //String emailUserLoggedIn = "";
        Button joinButton = (Button)findViewById(R.id.joinButton);
        String key = null;
        for(Events ev : eventlistGame){
            if((ev.getDate().equals(date)) && (ev.getGround().equals(groundName)) && (ev.getHour().equals(hour))){//the selected event
                key = ev.getId();
                UserArrayList = ev.getUsername();
                maxP = ev.getMaxP();
                type = ev.getType();

                if(type.equals("כדורגל"))soccer.setVisibility(View.VISIBLE);
                if(type.equals("כדורסל"))basket.setVisibility(View.VISIBLE);
                if(type.equals("טניס"))tennis.setVisibility(View.VISIBLE);
            }
        }
        if(UserArrayList!=null)
        numOfus.setText(UserArrayList.size() + "/" + maxP);
        else{
            numOfus.setText("0" + "/" + maxP);
        }
        if((emailUserLoggedIn!=null))
        {
            inviteBut.setVisibility(View.VISIBLE);
            inviteText.setVisibility(View.VISIBLE);
            if(UserArrayList!=null)
            for(User us:UserArrayList){//if exist in game already
                if(emailUserLoggedIn.equals(us.getUserName()))flag=1;
            }
            if(flag==1)joinButton.setVisibility(View.INVISIBLE);
            else if(Integer.parseInt(maxP)<=UserArrayList.size()){
                joinButton.setVisibility(View.INVISIBLE);
                inviteBut.setVisibility(View.INVISIBLE);
                inviteText.setVisibility(View.INVISIBLE);
                Toast.makeText(UsersInGame.this, "לא ניתן להצטרף כרגע, מספר המשתתפים שהצטרפו למשחק הינו מקסימלי" , Toast.LENGTH_SHORT).show();
            }
        }
        else {
            joinButton.setVisibility(View.INVISIBLE);
            Toast.makeText(UsersInGame.this, "אינך מחובר \n נא להתחבר על מנת להצטרף למשחק" , Toast.LENGTH_SHORT).show();
        }


        mRef = mDatabase.getReference().child("Users");
        mRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Map<String, Object> usersList = (HashMap<String, Object>) dataSnapshot.getValue();
                for (String key : usersList.keySet()) {
                    Map<String, Object> value = (HashMap<String, Object>) usersList.get(key);
                    if(emailUserLoggedIn!=null) {
                        if (value.get("username").toString().equals(emailUserLoggedIn)) {
                            age = value.get("age").toString();
                            phone = value.get("phone").toString();
                            name = value.get("Name").toString();
                        }
                    }
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

        mRef = mDatabase.getReference().child("Events").child(key).child("userlist");

        joinButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Map<String, Object > rate = new HashMap<String, Object>();
                rate.put("age", age);
                rate.put("email", emailUserLoggedIn);
                rate.put("name", name);
                rate.put("phone", phone);
                if(flag!=1){
                    mRef.push().updateChildren(rate);
                }
                finish();
            }
        });


        final ArrayList<String> items = new ArrayList<>();
        items.clear();

                for (User us : UserArrayList) {

                    items.add(us.getName() + "  -  " + us.getPhoneNumber());
                }
                ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(),android.R.layout.simple_list_item_1,items);
                userView.setAdapter(null);
                userView.setAdapter(adapter);

        inviteBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mRef = mRef.getDatabase().getReference().child("Friends");
                mRef.orderByChild("username").equalTo(emailUserLoggedIn).addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                        Map<String, Object> allUsers = (Map<String, Object>) dataSnapshot.getValue();//hash map for all events 0 - 50 f.e
                        String singleKey = allUsers.keySet().toString().substring(1,allUsers.keySet().toString().length()-1);
                        Map<String, Object> singleUser = (Map<String, Object>) allUsers.get(singleKey);
                        Map<String, String> friendlist = (Map<String, String>) singleUser.get("friendlist");
                        for(Object key:friendlist.values()) {
                            Map<String, Object> singlefriend = (Map<String, Object>) key;
                            if (singlefriend.get("enabled").equals(true) && singlefriend.get("confirmed").equals(true))
                                listPeople.add(singlefriend.get("username").toString());
                        }

                        onButtonShowPopupWindowClick(inviteWindow);
                    }

                    @Override
                    public void onCancelled(@NonNull DatabaseError databaseError) {

                    }
                });

            }
        });

    }
    public void onButtonShowPopupWindowClick(View view) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window2, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.FILL_PARENT;
        int height = LinearLayout.LayoutParams.MATCH_PARENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        FloatingActionButton cb = popupView.findViewById(R.id.closeBut3);
        final ListView peopleList = (ListView)popupView.findViewById(R.id.inviteList);
        final TextView friendtext = (TextView) popupView.findViewById(R.id.friendtext);
        friendtext.setVisibility(View.INVISIBLE);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
                listPeople.clear();
                peopleList.setAdapter(null);
                peopleList.clearChoices();

            }
        });

        peopleList.setAdapter(null);
        peopleList.setAdapter(new UsersInGame.ListResources(popupView));
        if(listPeople.size()==0)friendtext.setVisibility(View.VISIBLE);
        Button invitePeople = (Button)popupView.findViewById(R.id.invitePeople);
        if(emailUserLoggedIn!=null) {
            invitePeople.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    for (int i = 0; i < peopleList.getAdapter().getCount(); i++) {
                        boolean ch = false;
                        if(booleanArray.get(i)!=null) {
                            ch = booleanArray.get(i);
                            if (ch) {
                                mRef = mRef.getDatabase().getReference().child("Invites");
                                Map<String, Object> User = new HashMap<String, Object>();
                                User.put("guest", peopleList.getItemAtPosition(i));
                                User.put("Inviter", emailUserLoggedIn);
                                User.put("enabled", "True");
                                User.put("Text", date + " - " + groundName + " - " + hour);
                                String key = mRef.push().getKey();
                                mRef.push().updateChildren(User);
                                User.clear();
                            }
                        }
                    }
                    Toast.makeText(UsersInGame.this, "Invitation sent successfully" , Toast.LENGTH_SHORT).show();
                    listPeople.clear();
                    peopleList.setAdapter(null);
                    peopleList.clearChoices();
                    popupWindow.dismiss();
                }
            });

        }


    }

    class ListResources extends BaseAdapter {

        ArrayList<String> mydata;
        String temp;
        View context;

        ListResources(View context) {
            this.context = context;
            mydata = listPeople;

        }

        @Override
        public int getCount() {
            return listPeople.size();
        }

        @Override
        public Object getItem(int position) {
            return listPeople.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }
        public boolean getChecked(int position){return booleanArray.get(position);}
        @Override
        public View getView(final int position, final View convertView, ViewGroup parent) {
            final LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.listrow3, parent, false);
            TextView usText = (TextView) row.findViewById(R.id.usernameText);
            CheckBox checkBoxUs = (CheckBox) row.findViewById(R.id.inviteCheckBox);
            checkBoxUs.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    booleanArray.put(position,true);
                }
            });
            usText.setText(getItem(position).toString());
            return row;
        }
    }
}
