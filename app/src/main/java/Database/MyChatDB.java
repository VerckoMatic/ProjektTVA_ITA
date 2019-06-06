package Database;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteException;
import android.database.sqlite.SQLiteOpenHelper;

import java.util.ArrayList;
import java.util.List;

import Model.Classes.ChatRoom;
import Model.Classes.MessagePOJO;
import Model.Classes.User;

public class MyChatDB extends SQLiteOpenHelper {
    private static final int DATABASE_VERSION = 1;
    private static final String DATABASE_NAME = "chatDb.db";


    //TABLE UPORABNIKI
    public static final String TABLE_UPORABNIKI = "uporabniki";
    public static final String COLUMN_ID_U = "_id";
    public static final String COLUMN_NAME = "name";
    public static final String COLUMN_EMAIL = "email";
    public static final String COLUMN_PASSWORD = "password";

    //TABLE JUNCTION
    public static final String TABLE_USER_MESSAGES = "UsersMessages";
    public static final String COLUMN_ID_UM = "_id";
    public static final String COLUMN_FK_U = "fk_u";
    public static final String COLUMN_FK_M = "fk_m";
    public static final String COLUMN_FK_U_R = "fk_r";

    //TABLE MESSAGES
    public static final String TABLE_MESSAGES = "messages";
    public static final String COLUMN_ID_M = "_id";
    public static final String COLUMN_MESSAGE = "message";
    public static final String COLUMN_EMAIL_M = "email_m";
    public static final String COLUMN_EMAIL_RECIEVER = "email_r";
    public static final String COLUMN_COLOR_M = "color_m";
    public static final String COLUMN_ROOM = "room_m";
    public static final String COLUMN_UP_STANJE = "up_stanje";



    public MyChatDB(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, DATABASE_NAME, factory, DATABASE_VERSION);
    }


    @Override
    public void onCreate(SQLiteDatabase db) {

        String queryUser = "CREATE TABLE " + TABLE_UPORABNIKI + "(" +
                COLUMN_ID_U + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_EMAIL + " TEXT " + ");";


        //CREATE JUNCTION
        String queryUM = "CREATE TABLE " + TABLE_USER_MESSAGES + "(" +
                COLUMN_ID_UM + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_FK_M + " TEXT," +
                COLUMN_EMAIL_M + " integer," +
                COLUMN_FK_U_R + " TEXT,"
                + " FOREIGN KEY ("+COLUMN_FK_M+") REFERENCES "+ TABLE_MESSAGES +"("+COLUMN_ROOM+"),"
                + " FOREIGN KEY ("+COLUMN_FK_U_R+") REFERENCES "+ TABLE_MESSAGES +"("+COLUMN_EMAIL_RECIEVER+"),"
                + " FOREIGN KEY ("+COLUMN_FK_U+") REFERENCES "+ TABLE_MESSAGES +"("+COLUMN_EMAIL_M+") ON DELETE CASCADE);";

        //CREATE Messages
        String queryM = "CREATE TABLE " + TABLE_MESSAGES + "(" +
                COLUMN_ID_M + " INTEGER PRIMARY KEY AUTOINCREMENT, " +
                COLUMN_MESSAGE + " TEXT, " +
                COLUMN_EMAIL_M + " TEXT, " +
                COLUMN_EMAIL_RECIEVER + " TEXT, " +
                COLUMN_COLOR_M + " TEXT, " +
                COLUMN_ROOM + " TEXT, " +
                COLUMN_UP_STANJE + " INTEGER DEFAULT 0);";

        db.execSQL(queryM);
        db.execSQL(queryUser);
        db.execSQL(queryUM);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int i, int i1) {
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_MESSAGES);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_UPORABNIKI);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USER_MESSAGES);
        onCreate(db);
    }


    //MESSAGE KODA

    public void add(MessagePOJO message) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_MESSAGE, message.getText());
        values.put(COLUMN_EMAIL_M, message.getEmail());

        values.put(COLUMN_COLOR_M, message.getColor());
        values.put(COLUMN_ROOM, message.getRoom());
        values.put(COLUMN_UP_STANJE, message.isBelongsToCurrentUser());
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_MESSAGES, null, values);
        db.close();

    }

    //MESSAGE KODA


    // Getting All Shops
    public List<MessagePOJO> pridobiVseMessege(String roomName) {
        List<MessagePOJO> listMessagov = new ArrayList<MessagePOJO>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_MESSAGES + " WHERE " + COLUMN_ROOM + " = " + "'" + roomName + "'" + "";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                MessagePOJO message = new MessagePOJO();
                message.setId(cursor.getString(0));
                message.setText(cursor.getString(1));
                message.setEmail(cursor.getString(2));
                message.setColor(cursor.getString(3));
                message.setRoom(cursor.getString(4));
                message.setBelongsToCurrentUser(cursor.getString(5).equals("1"));
                // Adding contact to list
                listMessagov.add(message);
            } while (cursor.moveToNext());
        }

        // return contact list
        return listMessagov;
    }

    //user, room, reciever so prarametri
    public void add(String FK_U, String FK_M, String FK_U_R) {
        ContentValues values = new ContentValues();
        values.put(COLUMN_FK_M, FK_M);
        values.put(COLUMN_FK_U, FK_U);
        values.put(COLUMN_FK_U_R, FK_U);
        SQLiteDatabase db = getWritableDatabase();
        db.insert(TABLE_USER_MESSAGES, null, values);
        db.close();

    }
    // Getting All Shops
    public List<ChatRoom> getUsersRooms(String email, String email_r) {
        List<ChatRoom> chatRooms = new ArrayList<ChatRoom>();
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_USER_MESSAGES + " WHERE " + COLUMN_FK_U + " = " + "'" + email + "' OR" +
                " " + COLUMN_FK_U_R + " = " + "'" + email_r + "' + ";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ChatRoom room = new ChatRoom();
                room.setId(cursor.getString(0));
                room.setIdRoom(cursor.getString(1));
                room.setReciever(cursor.getString(2));
                room.setSender(cursor.getString(3));
                // Adding contact to list
                chatRooms.add(room);
            } while (cursor.moveToNext());
        }

        // return contact list
        return chatRooms;
    }

    // Does this room allready exist
    public String doesRoomExist(String email, String email_r) {
        String roomName = "none";
        // Select All Query
        String selectQuery = "SELECT  * FROM " + TABLE_USER_MESSAGES + " WHERE " + COLUMN_FK_U + " = " + "'" + email + "' AND" +
                " " + COLUMN_FK_U_R + " = " + "'" + email_r + "'";

        SQLiteDatabase db = this.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);
        try{



        // looping through all rows and adding to list
        if (cursor.moveToFirst()) {
            do {
                ChatRoom room = new ChatRoom();
                room.setId(cursor.getString(0));
                room.setIdRoom(cursor.getString(1));
                room.setReciever(cursor.getString(2));
                room.setSender(cursor.getString(3));
                // Adding contact to list
                roomName = cursor.getString(1);
            } while (cursor.moveToNext());
        }
        }catch(SQLiteException e){

            //database does't exist yet.

        }

        // return contact list
        return roomName;
    }
}
