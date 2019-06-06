package Model.Classes;

public class ChatRoom {

    public String id;
    public String idRoom;
    public String reciever;
    public String sender;

    public ChatRoom(String id, String idRoom, String reciever, String sender) {
        this.id = id;
        this.idRoom = idRoom;
        this.reciever = reciever;
        this.sender = sender;
    }

    public ChatRoom() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getIdRoom() {
        return idRoom;
    }

    public void setIdRoom(String idRoom) {
        this.idRoom = idRoom;
    }

    public String getReciever() {
        return reciever;
    }

    public void setReciever(String reciever) {
        this.reciever = reciever;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }
}
