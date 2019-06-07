package Model.Classes;


public class Response {

    private String message;
    private String token;
    private String idUser;
    private String image;

    public Response() {
    }

    public Response(String message, String token, String idUser, String image) {
        this.message = message;
        this.token = token;
        this.idUser = idUser;
        this.image = image;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public String getIdUser() {
        return idUser;
    }

    public void setIdUser(String idUser) {
        this.idUser = idUser;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }
}