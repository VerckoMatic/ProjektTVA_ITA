package Model.Classes;

public class Message {

    public String message;
    public String receiverEmail;

    public Message() {
    }

    public Message(String message, String receiverEmail) {
        this.message = message;
        this.receiverEmail = receiverEmail;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getReceiverEmail() {
        return receiverEmail;
    }

    public void setReceiverEmail(String receiverEmail) {
        this.receiverEmail = receiverEmail;
    }
}
