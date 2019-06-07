package Model.ResponsePOJO;


public class MessagePOJO {
    private String id;
    private String text;
    private String email;
    private String email_receiver;
    private String color;
    private String room;
    private boolean belongsToCurrentUser;

    public MessagePOJO(String id, String text, String email, String email_receiver, String color, String room, boolean belongsToCurrentUser) {
        this.id = id;
        this.text = text;
        this.email = email;
        this.email_receiver = email_receiver;
        this.color = color;
        this.room = room;
        this.belongsToCurrentUser = belongsToCurrentUser;
    }

    public MessagePOJO(){

    }


    public MessagePOJO( String text, String email, String email_receiver, String color, String room, boolean belongsToCurrentUser) {
        this.text = text;
        this.email = email;
        this.email_receiver = email_receiver;
        this.color = color;
        this.room = room;
        this.belongsToCurrentUser = belongsToCurrentUser;
    }
    public String getEmail_receiver() {
        return email_receiver;
    }

    public void setEmail_receiver(String email_receiver) {
        this.email_receiver = email_receiver;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }

    public void setBelongsToCurrentUser(boolean belongsToCurrentUser) {
        this.belongsToCurrentUser = belongsToCurrentUser;
    }

    public boolean isBelongsToCurrentUser() {
        return belongsToCurrentUser;
    }


}
