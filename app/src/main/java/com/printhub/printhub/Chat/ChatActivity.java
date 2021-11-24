package com.printhub.printhub.Chat;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.ChildEventListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.printhub.printhub.HomeScreen.MainnewActivity;
import com.printhub.printhub.R;
import com.printhub.printhub.bunkManager.BunkActivity;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ChatActivity extends AppCompatActivity {

    EditText editText;
    TextView groupNameV;
    TextView groupNameInits;
    FirebaseUser user;
    String groupId;
    String userName;
    String groupName;
    String GROUPPATH;
    private MessageAdapter messageAdapter;
    private ListView messagesView;
    private String Uid;
    SharedPreferences detail = null,cityNameSharedPref,collegeNameSharedPref,userIdSharedPref,userNameSharedPref;
    String collegeName;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);


        editText = findViewById(R.id.editText);
        groupNameV = findViewById(R.id.groupName);
        groupNameInits = findViewById(R.id.groupNameInitsV);
        messageAdapter = new MessageAdapter(this);
        messagesView = findViewById(R.id.messages_view);
        messagesView.setAdapter(messageAdapter);
        detail = getSharedPreferences("com.printhub.printhub", MODE_PRIVATE);
        userIdSharedPref = getSharedPreferences("com.printhub.printhub", MODE_PRIVATE);
        collegeNameSharedPref = getSharedPreferences("com.printhub.printhub", MODE_PRIVATE);
        cityNameSharedPref = getSharedPreferences("com.printhub.printhub", MODE_PRIVATE);
        userNameSharedPref= getSharedPreferences("com.printhub.printhub", MODE_PRIVATE);
        collegeName=collegeNameSharedPref.getString("collegeName","");
        String cityName = cityNameSharedPref.getString("cityName", "");


        if (savedInstanceState == null) {
            Bundle extras = getIntent().getExtras();
            if (extras == null) {
                groupId = "xxx";
                groupName = "xxx";
            } else {
                groupId = extras.getString("GROUP_ID");
                groupName = extras.getString("GROUP_NAME");
            }
        } else {
            groupId = (String) savedInstanceState.getSerializable("GROUP_ID");
            groupName = (String) savedInstanceState.getSerializable("GROUP_NAME");
        }
//Here we have to update the path for the groups
        GROUPPATH = collegeName+"Groups/" + groupId + "/Messages/";
        // Write a message to the database
        FirebaseDatabase database = FirebaseDatabase.getInstance("https://myapplication-2ca64.firebaseio.com/");
        DatabaseReference myRef = database.getReference(GROUPPATH);
        user = FirebaseAuth.getInstance().getCurrentUser();
        Uid = user.getUid();
        userName = userNameSharedPref.getString("userName","");
       // Toast.makeText(this, userName,Toast.LENGTH_LONG).show();

        groupNameV.setText(groupName);
        groupNameInits.setText(groupName.substring(0, 2).toUpperCase());
        final List<Message> msgs = new ArrayList<Message>();


        //Toast.makeText(getApplicationContext(), , Toast.LENGTH_SHORT).show();


        /*
        FirebaseListAdapter adapter = new FirebaseListAdapter<Message>(this, Message.class,
                R.layout.their_message, myRef) {
            @Override
            protected void populateView(View v, Message model, int position) {
                // Get references to the views of message.xml

                TextView messageText = (TextView)v.findViewById(R.id.message_body);
                if (model.getSender().equals("Kostas")){
                    messageText.setBackgroundColor(14);

                }
                // Set their text
                messageText.setText(model.getText());

            }
        };

        messagesView.setAdapter(adapter);
    /*





        Query q = myRef.orderByPriority();
        q.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Iterator iterator = dataSnapshot.getChildren().iterator();

                while(iterator.hasNext()){

                    Message mes = (Message) ((DataSnapshot)iterator.next()).getValue(Message.class);

                    //String str = ((DataSnapshot)iterator.next()).get.getValue().toString();
                    messageAdapter.add(mes);
                    //TODO CHANGE TO GROUP NAME
                }
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
*/


        myRef.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(@NonNull DataSnapshot dataSnapshot, @Nullable String s) {

                Message message = dataSnapshot.getValue(Message.class);
                //String str = (String) ((DataSnapshot)iterator.next()).getValue().toString();
                boolean self = true;
                //TODO CHANGE KOSTAS TO USER.GETID()
                //if (Uid.equals(message.getSender())){message.setBelongsToCurrentUser(true);}
                msgs.add(message);
                //notifyDataSetChanged(msgs);
                messageAdapter.add(message);
                messagesView.setSelection(messagesView.getCount() - 1);

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

    public void notifyDataSetChanged(List<Message> values) {
        for (Message obj : values) {
            messageAdapter.add(obj);
        }
    }

    public void backButton(View view) {

        startActivity(new Intent(this, MainActivityChat.class));
    }

    public void sendMessage(View view) {

        String msg = editText.getText().toString();
        if (msg.length() > 0) {

            editText.getText().clear();


            // Write a message to the database
            FirebaseDatabase database = FirebaseDatabase.getInstance("https://myapplication-2ca64.firebaseio.com/");
            DatabaseReference myRef = database.getReference(GROUPPATH);

            Date date = new Date(); // This object contains the current date value
            SimpleDateFormat formatter = new SimpleDateFormat("dd-MM-yyyy HH:mm:ss");
            Message chatMessage = new Message(msg, user.getUid(), userName, formatter.format(date));
            myRef.push().setValue(chatMessage);


        }


    }
}
