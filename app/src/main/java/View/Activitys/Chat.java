package View.Activitys;

import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import com.example.matic.projekttva.R;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.scaledrone.lib.Listener;
import com.scaledrone.lib.Member;
import com.scaledrone.lib.Room;
import com.scaledrone.lib.RoomListener;
import com.scaledrone.lib.Scaledrone;

import java.util.List;
import java.util.Random;

import Database.MyChatDB;
import Model.Classes.MessagePOJO;
import View.Activitys.Adapters.MessageAdapter;
import utils.Constants;

public class Chat extends AppCompatActivity implements RoomListener {
    String soba;
    private String channelID = "10vFEOCqsdWjOma0";

    private EditText editText;
    private Scaledrone scaledrone;
    private MessageAdapter messageAdapter;
    private ListView messagesView;
    String email ;
    MyChatDB chatDB;
    private String mEmail;
    private int idItem;
    String sellerEmail;
    private String roomName = "observable-" + sellerEmail + idItem;
    private SharedPreferences mSharedPreferences;
    String uporabnik;


    Room room;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        chatDB = new MyChatDB(this, null, null, 1);
        editText = (EditText) findViewById(R.id.editText);

        messageAdapter = new MessageAdapter(this);
        messagesView = (ListView) findViewById(R.id.messages_view);
        messagesView.setAdapter(messageAdapter);

        Intent intentProfil = getIntent();
        mSharedPreferences = PreferenceManager.getDefaultSharedPreferences(getApplicationContext());
        uporabnik = mSharedPreferences.getString(Constants.EMAIL,"");
        idItem = intentProfil.getIntExtra("IDITEM",0);
        sellerEmail = intentProfil.getStringExtra("SELLEREMAIL");
        email = uporabnik;
        roomName = "observable-" + sellerEmail + idItem;
        List<MessagePOJO> listP =  chatDB.pridobiVseMessege(roomName);

        for(int i = 0; i < listP.size(); i++) {
            if (listP.get(i).getRoom().equals(roomName)) {
                if (listP != null) {
                    listP = null;
                }
                listP = chatDB.pridobiVseMessege(roomName);
                messageAdapter.add(listP,uporabnik);
                // scroll the ListView to the last added element
                messagesView.setSelection(messagesView.getCount() - 1);

            }
        }



        scaledrone = new Scaledrone(channelID, uporabnik);
        scaledrone.connect(new Listener() {
            @Override
            public void onOpen() {
                System.out.println("Scaledrone connection open");
                scaledrone.subscribe(roomName, Chat.this);
            }

            @Override
            public void onOpenFailure(Exception ex) {
                System.err.println(ex);
            }

            @Override
            public void onFailure(Exception ex) {
                System.err.println(ex);
            }

            @Override
            public void onClosed(String reason) {
                System.err.println(reason);

            }

        });
    }

    // Successfully connected to Scaledrone room
    @Override
    public void onOpen(Room room) {
        System.out.println("Conneted to room");
        this.room = room;
    }

    // Connecting to Scaledrone room failed
    @Override
    public void onOpenFailure(Room room, Exception ex) {
        System.err.println(ex);
    }

    // Received a message from Scaledrone room
    @Override
    public void onMessage(Room room, final JsonNode json, final Member member) {
        final ObjectMapper mapper = new ObjectMapper();
        // member.clientData is a MemberData object, let's parse it as such
        String email = uporabnik;
        // if the clientID of the message sender is the same as our's it was sent by us
        boolean belongsToCurrentUser = member.getId().equals(scaledrone.getClientID());
        // since the message body is a simple string in our case we can use json.asText() to parse it as such
        // if it was instead an object we could use a similar pattern to data parsing
        final MessagePOJO message2 = new MessagePOJO(json.asText(), email, sellerEmail, getRandomColor(), roomName, belongsToCurrentUser);

        chatDB.add(message2);

        runOnUiThread(new Runnable() {
            @Override
            public void run() {

                if(roomName.equals(roomName)){

                    List<MessagePOJO> listP =  chatDB.pridobiVseMessege(roomName);

                    for(int i = 0; i < listP.size(); i++){
                        if(listP.get(i).getRoom().equals(roomName)){
                            if(listP != null){
                                listP = null;
                            }
                            listP =  chatDB.pridobiVseMessege(roomName);
                            messageAdapter.add(listP,uporabnik);
                            // scroll the ListView to the last added element
                            messagesView.setSelection(messagesView.getCount() - 1);

                        }
                    }
                }
            }
        });


    }

    private String getRandomName() {
        String ime = email;
        return ime;
    }

    private String getRandomColor() {
        Random r = new Random();
        StringBuffer sb = new StringBuffer("#87BCAD");
        return sb.toString().substring(0, 7);
    }

    public void sendMessage(View view) {
        String message = editText.getText().toString();
        if (message.length() > 0) {
            scaledrone.publish(roomName, message);
            editText.getText().clear();
        }
    }

    @Override
    protected void onStop() {
        scaledrone.unsubscribe(room);
        super.onStop();
    }

}

