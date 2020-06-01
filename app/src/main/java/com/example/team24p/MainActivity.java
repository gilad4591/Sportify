package com.example.team24p;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.NotificationCompat;
import androidx.core.app.NotificationManagerCompat;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.ParsePosition;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;


public class MainActivity extends AppCompatActivity {
    static int count = 0;
    private TextView welcomeTextView;
    private TextView editPrivateText;
    private FirebaseDatabase mDatabase = FirebaseDatabase.getInstance();
    private DatabaseReference mRef = mDatabase.getReference().child("Events");
    private FloatingActionButton adminPanelButton;
    private FloatingActionButton messageBut;
    private FloatingActionButton helpBut;
    private String userNameLoggedIn;
    private ArrayList<String> items;
    private View help;
    private ListView myAct;
    private TextView firstText;
    private TextView mesText;
    private TextView logoutText;
    private TextView loginText;
    private SharedPreferences sharedPref;
    private TextView myPendingText;
    private TextView myGamesText;
    public ListView pendingGames;
    private Map<String, Object > pendingGamesList;
    private Map<String, Object > pendingGamesKeys;
    final ArrayList<Events> eventsArrayList = new ArrayList<>();


    @Override
    protected void onCreate(final Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        items = new ArrayList<>();
        pendingGamesList = new HashMap<>();
        pendingGamesKeys = new HashMap<>();

        sharedPref= getSharedPreferences("mypref", MODE_PRIVATE);
        userNameLoggedIn = sharedPref.getString("name", null);


        items.clear();
        messageBut = (FloatingActionButton) findViewById(R.id.messageButton);
        firstText = (TextView)findViewById(R.id.firstText);
        messageBut.setVisibility(View.INVISIBLE);
        welcomeTextView = (TextView) findViewById(R.id.welcomeTextView);
        myGamesText = (TextView) findViewById(R.id.myGamesText);
        myPendingText = (TextView) findViewById(R.id.PendingGamesText);
        editPrivateText = (TextView)findViewById(R.id.editPrivateText);
        mesText = (TextView)findViewById(R.id.mestext);
        logoutText = (TextView)findViewById(R.id.logouttext);
        loginText = (TextView)findViewById(R.id.logtext);
        editPrivateText.setFocusable(false);
        editPrivateText.setClickable(true);
        editPrivateText.setVisibility(View.INVISIBLE);
        firstText.setVisibility(View.VISIBLE);
        mesText.setVisibility(View.INVISIBLE);
        myGamesText.setVisibility(View.INVISIBLE);
        myPendingText.setVisibility(View.INVISIBLE);
        logoutText.setVisibility(View.INVISIBLE);
        loginText.setVisibility(View.VISIBLE);
        helpBut = (FloatingActionButton)findViewById(R.id.helpBut);
        help = (View)findViewById(R.id.helpView);

        helpBut.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onButtonShowPopupWindowClick(help);
            }
        });



        adminPanelButton = (FloatingActionButton) findViewById(R.id.buttonManagePanel);
        FloatingActionButton logoutButton = (FloatingActionButton) findViewById(R.id.logoutButton);
        adminPanelButton.setVisibility(View.INVISIBLE);
        logoutButton.setVisibility(View.INVISIBLE);
        myAct = (ListView)findViewById(R.id.myActivityList);
        pendingGames = (ListView)findViewById(R.id.myPendingGames);
        myAct.setVisibility(View.INVISIBLE);
        pendingGames.setVisibility((View.INVISIBLE));
        String isAdmin = sharedPref.getString("isAdmin", "False");
        myAct.setAdapter(null);

        FloatingActionButton menuButton = (FloatingActionButton)findViewById(R.id.menuButton);
        welcomeTextView.setText("");
        try {
            if(userNameLoggedIn==null) {
                userNameLoggedIn = getIntent().getStringExtra("userNameLoggedIn");
            }
            if (isAdmin.equals("True")){
                adminPanelButton.setVisibility(View.VISIBLE);
            }
        } catch (Exception e){
        }
        FloatingActionButton button = (FloatingActionButton) findViewById(R.id.button2);
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = getIntent();
                finish();
                startActivity(intent);

                Intent stNav = new Intent(getApplicationContext(), MapsActivity.class);
                stNav.putExtra("userNameLoggedIn",userNameLoggedIn);
                startActivity(stNav);
            }
        });
            FloatingActionButton LoginButton = (FloatingActionButton) findViewById(R.id.LoginButton);
            if (count != 0){
                count++;
            }
            if (userNameLoggedIn != null){
                firstText.setVisibility(View.INVISIBLE);
                LoginButton.setVisibility(View.INVISIBLE);
                loginText.setVisibility(View.INVISIBLE);
                mesText.setVisibility(View.VISIBLE);
                logoutText.setVisibility(View.VISIBLE);
                logoutButton.setVisibility(View.VISIBLE);
                myAct.setVisibility(View.VISIBLE);
                myPendingText.setVisibility(View.VISIBLE);
                myGamesText.setVisibility(View.VISIBLE);
                pendingGames.setVisibility(View.VISIBLE);
                welcomeTextView.setText("Welcome"+" " +(userNameLoggedIn));
                editPrivateText.setVisibility(View.VISIBLE);
                messageBut.setVisibility(View.VISIBLE);

            }
            menuButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = getIntent();
                    finish();
                    startActivity(intent);
                    Intent stMenu = new Intent(getApplicationContext(),todayGamesActivity.class);
                    if(userNameLoggedIn!=null)stMenu.putExtra("username",userNameLoggedIn);
                    startActivity(stMenu);
                }
            });

            LoginButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent stLogin = new Intent(getApplicationContext(), LoginActivity.class);
                    userNameLoggedIn = "";
                    startActivity(stLogin);
                }
            });


            logoutButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    SharedPreferences sharedPref= getSharedPreferences("mypref",0);
                    SharedPreferences.Editor editor = sharedPref.edit();
                    editor.remove("name");
                    editor.remove("isAdmin");//its remove name field from your SharedPreferences
                    editor.commit(); //Don't forgot to commit  SharedPreferences.

                    Intent intent = getIntent();
                    intent.removeExtra("isAdmin");
                    intent.removeExtra("userNameLoggedIn");
                    finish();
                    startActivity(intent);
                }
            });

        editPrivateText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stSelfEditDetails = new Intent(getApplicationContext(), EditUserDetailsActivity.class);
                stSelfEditDetails.putExtra("userNameLoggedIn",userNameLoggedIn);
                stSelfEditDetails.putExtra("userToEdit",userNameLoggedIn);
                stSelfEditDetails.putExtra("isAdmin",getIntent().getStringExtra("isAdmin"));
                startActivity(stSelfEditDetails);
            }
        });
        adminPanelButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent stAdmin = new Intent(getApplicationContext(), AdminActivity.class);
                stAdmin.putExtra("userNameLoggedIn",userNameLoggedIn);
                stAdmin.putExtra("isAdmin",getIntent().getStringExtra("isAdmin"));
                startActivity(stAdmin);
            }
        });
        items.clear();
        if(userNameLoggedIn!=null) {

            mRef.addValueEventListener(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String d = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/M/yyyy"));
                    Date d1, d2;
                    DateFormat dtf = new SimpleDateFormat("dd/M/yyyy");
                    d1 = dtf.parse(d, new ParsePosition(0));


                    Map<String, Object> eventsAll = (Map<String, Object>) dataSnapshot.getValue();//hash map for all events 0 - 50 f.e
                    for (Object key : eventsAll.values()) {
                        Map<String, Object> singleEvent = (Map<String, Object>) key;
                        for (Object key2 : singleEvent.keySet()) {
                            d2 = dtf.parse(singleEvent.get("date").toString(), new ParsePosition(0));
                            if ((key2.toString().equals("userlist")) && (d2.compareTo(d1) >= 0)) {
                                Map<String, String> listOfUsers = (Map<String, String>) singleEvent.get("userlist");

                                for (Object lists : listOfUsers.values()) {
                                    Map<String, String> singleUser = (Map<String, String>) lists;
                                    for (Object key3 : singleUser.keySet()) {
                                        if ("email".equals(key3.toString())) {
                                            if (userNameLoggedIn.equals(singleUser.get("email").toString())) {
                                                String x =
                                                        singleEvent.get("date").toString() + " " +
                                                                singleEvent.get("ground").toString() + " " +
                                                                singleEvent.get("hour").toString();
                                                items.add(x);
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }
                    if(items.isEmpty())items.add("עדיין לא הצטרפת לאף משחק");
                    ArrayAdapter<String> adapter = new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_list_item_1, items);
                    myAct.setAdapter(null);
                    myAct.setAdapter(adapter);
                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

//           mRef = mDatabase.getReference().child("messages");
//            mRef.addListenerForSingleValueEvent(new ValueEventListener() {
//                @Override
//                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
//                    Map<String, Object> messages = (HashMap<String, Object>) dataSnapshot.getValue();
//                    for(Object key: messages.keySet()) {
//                        Map<String, Object> singleMes = (HashMap<String, Object>) messages.get(key);
//                        if (singleMes.get("user1").toString().equals(userNameLoggedIn) || (singleMes.get("user2").toString().equals(userNameLoggedIn))) {
//                            mRef=mRef.child(key.toString()).child("messageList");
//                            mRef.addChildEventListener(new ChildEventListener() {
//                                @Override
//                                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//                                    Map<String, String> newMs = (HashMap<String, String>) dataSnapshot.getValue();
//                                    String user = newMs.get("sender");
//                                    notification(user);
//                                }
//                                @Override
//                                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                                }
//
//                                @Override
//                                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {
//
//                                }
//
//                                @Override
//                                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
//
//                                }
//
//                                @Override
//                                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                                }
//                            });
//                        }
//
//                    }
//                }
//
//                @Override
//                public void onCancelled(@NonNull DatabaseError databaseError) {
//
//                }
//            });



            messageBut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent stMess = new Intent(MainActivity.this, messageActivity.class);
                    stMess.putExtra("userNameLoggedIn", userNameLoggedIn);
                    startActivity(stMess);
                }
            });

            mRef = mDatabase.getReference().child("Invites");
            mRef.addChildEventListener(new ChildEventListener() {
                @Override
                public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {
                    Map<String, Object > invites = (HashMap<String, Object>)  dataSnapshot.getValue();
                    if(invites.get("guest").equals(userNameLoggedIn) && invites.get("enabled").equals("True")){
                        pendingGamesList.put(String.valueOf(pendingGamesList.size()),invites);
                        pendingGamesKeys.put(String.valueOf(pendingGamesList.size()),dataSnapshot.getKey());

                        pendingGames.setAdapter(null);
                        pendingGames.setAdapter(new MainActivity.ListResources(MainActivity.this));
                    }
                }

                @Override
                public void onChildChanged(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onChildRemoved(@NonNull DataSnapshot dataSnapshot) {

                }

                @Override
                public void onChildMoved(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

        }
    }
    public void onButtonShowPopupWindowClick(View view) {

        // inflate the layout of the popup window
        LayoutInflater inflater = (LayoutInflater)
                getSystemService(LAYOUT_INFLATER_SERVICE);
        View popupView = inflater.inflate(R.layout.popup_window, null);

        // create the popup window
        int width = LinearLayout.LayoutParams.WRAP_CONTENT;
        int height = LinearLayout.LayoutParams.WRAP_CONTENT;
        boolean focusable = true; // lets taps outside the popup also dismiss it
        final PopupWindow popupWindow = new PopupWindow(popupView, width, height, focusable);

        // show the popup window
        // which view you pass in doesn't matter, it is only used for the window tolken
        popupWindow.showAtLocation(view, Gravity.CENTER, 0, 0);

        // dismiss the popup window when touched
        FloatingActionButton cb = popupView.findViewById(R.id.closeBut2);
        cb.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                popupWindow.dismiss();
            }
        });

    }


    private void notification(String user) {
        String id = createNotificationChannel();
        NotificationCompat.Builder builder = new NotificationCompat.Builder(MainActivity.this, id)
                .setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("new message")
                .setContentText("you got new message from" + user)
                .setPriority(NotificationCompat.PRIORITY_DEFAULT);

        NotificationManagerCompat notificationManagerCompat = NotificationManagerCompat.from(MainActivity.this);

        notificationManagerCompat.notify(100,builder.build());

    }
    private String createNotificationChannel() {
        // Create the NotificationChannel, but only on API 26+ because
        // the NotificationChannel class is new and not in the support library
        String id = null;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            String name = "messageChannel";
            id = String.valueOf(System.currentTimeMillis());
            String description = "This is notification for new messages";
            int importance = NotificationManager.IMPORTANCE_DEFAULT;
            NotificationChannel channel = new NotificationChannel(id, name, importance);
            channel.setDescription(description);
            // Register the channel with the system; you can't change the importance
            // or other notification behaviors after this
            NotificationManager notificationManager = getSystemService(NotificationManager.class);
            notificationManager.createNotificationChannel(channel);
        }
        return id;
    }
    class ListResources extends BaseAdapter {
        Map<String, Object >mydata;
        String temp;
        Context context;
        ListResources(Context context){
            this.context = context;
            mydata=pendingGamesList;

        }
        @Override
        public int getCount() {
            return pendingGamesList.size();
        }

        @Override
        public Object getItem(int position) {
            return pendingGamesList.get(String.valueOf(position));
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, final View convertView, ViewGroup parent) {
            final LayoutInflater inflater = getLayoutInflater();
            View row = inflater.inflate(R.layout.listrow2, parent, false);
            TextView usText = (TextView) row.findViewById(R.id.usernameText);
            TextView inviteby = (TextView) row.findViewById(R.id.invite_by);
            FloatingActionButton accBut = (FloatingActionButton) row.findViewById(R.id.acceptButton);
            FloatingActionButton decBut = (FloatingActionButton) row.findViewById(R.id.declineButton);

            final Map<String, Object > obj = (Map<String, Object >) getItem(position);
            inviteby.setText("הוזמנת על ידי " + obj.get("Inviter"));
            usText.setText(obj.get("Text").toString());

            decBut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    mRef = mRef.getDatabase().getReference().child("Invites").child(pendingGamesKeys.get(String.valueOf(position+1)).toString()).child("enabled");
                    mRef.setValue("False");
                    pendingGamesList.remove(position);
                    pendingGames.setAdapter(null);
                }
            });

            mRef = mRef.getDatabase().getReference().child("Events");
            mRef.addValueEventListener(new ValueEventListener() {
                @RequiresApi(api = Build.VERSION_CODES.O)
                @Override
                public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                    String d = LocalDate.now().format(DateTimeFormatter.ofPattern("dd/M/yyyy"));
                    Date d1,d2;
                    DateFormat dtf = new SimpleDateFormat("dd/M/yyyy");
                    d1 = dtf.parse(d,new ParsePosition(0));

                    Map<String, Object> eventsAll = (Map<String, Object>) dataSnapshot.getValue();//hash map for all events 0 - 50 f.e
                    int i = 0;
                    Object[] keysets =  eventsAll.keySet().toArray();
                    ArrayList <String> keyRelevant = new ArrayList<>();
                    for (Object key : eventsAll.values()) {
                        ArrayList<User> usersArrayList = new ArrayList<>();
                        Events event = new Events();
                        Map<String, Object> singleEvent = (Map<String, Object>) key;
                        for (Object key2 : singleEvent.keySet()) {
                            d2 = dtf.parse(singleEvent.get("date").toString(),new ParsePosition(0));
                            if((key2.toString().equals("userlist"))&&(d2.compareTo(d1)>=0)){
                                Map<String, String> listOfUsers = (Map<String, String>) singleEvent.get("userlist");
                                String x = singleEvent.get("date").toString() + " - " +
                                        singleEvent.get("ground").toString() + " - " +
                                        singleEvent.get("hour").toString();

                                for (Object lists : listOfUsers.values()) {
                                    User user = new User();
                                    String value2=" ";
                                    Map<String, String> singleUser = (Map<String, String>) lists;
                                    for(Object key3 : singleUser.keySet())
                                        if ("age".equals(key3.toString())) {
                                            value2 = singleUser.get("age").toString();
                                            user.setAge(value2);
                                        } else if ("name".equals(key3.toString())) {
                                            value2 = singleUser.get("name").toString();
                                            user.setName(value2);
                                        } else if ("phone".equals(key3.toString())) {
                                            value2 = singleUser.get("phone").toString();
                                            user.setPhoneNumber(value2);
                                        } else if ("email".equals(key3.toString())) {
                                            value2 = singleUser.get("email").toString();
                                            user.setUserName(value2);
                                        }


                                    usersArrayList.add(user);
                                }

                                event.setHour(singleEvent.get("hour").toString());
                                event.setGround(singleEvent.get("ground").toString());
                                event.setDate(singleEvent.get("date").toString());
                                event.setMaxp(singleEvent.get("maxParticipants").toString());
                                if(singleEvent.get("type")!=null)event.setType(singleEvent.get("type").toString());
                                event.setId(keysets[i].toString());
                                event.setUsername(usersArrayList);
                                eventsArrayList.add(event);
                            }


                        }
                        i++;
                    }

                }

                @Override
                public void onCancelled(@NonNull DatabaseError databaseError) {

                }
            });

            accBut.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String details = obj.get("Text").toString();
                    String[] x = details.split(" - ");
                    mRef = mRef.getDatabase().getReference().child("Events");
                    Intent appInfo = new Intent(getApplicationContext(), UsersInGame.class);
                    appInfo.putExtra("userNameLoggedIn", userNameLoggedIn);
                    appInfo.putExtra("eventlistGame", (Serializable) eventsArrayList);
                    appInfo.putExtra("hour", x[2]);
                    appInfo.putExtra("markerName", x[1]);
                    appInfo.putExtra("date", x[0]);

                    mRef = mRef.getDatabase().getReference().child("Invites").child(pendingGamesKeys.get(String.valueOf(position+1)).toString()).child("enabled");
                    mRef.setValue("False");
                    pendingGamesList.remove(position);
                    pendingGames.setAdapter(null);

                    startActivity(appInfo);
                }
            });

            return row;
        }
    }
}


