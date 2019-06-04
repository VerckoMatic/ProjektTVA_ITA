package Model.ResponsePOJO;

public class UserResponsePOJO {

    public int idUser;
    public String fullName;
    public String email;
    public String image;
    public String password;
    public double rating;

    public UserResponsePOJO() {
    }

    public UserResponsePOJO(int idUser, String fullName, String email, String image, String password, double rating) {
        this.idUser = idUser;
        this.fullName = fullName;
        this.email = email;
        this.image = image;
        this.password = password;
        this.rating = rating;
    }

    public int getIdUser() {
        return idUser;
    }

    public void setIdUser(int idUser) {
        this.idUser = idUser;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public double getRating() {
        return rating;
    }

    public void setRating(double rating) {
        this.rating = rating;
    }
}
