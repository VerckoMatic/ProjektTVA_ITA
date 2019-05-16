package Model.Classes;

public class User {
    public String fullName;
    public String email;
    public String image;
    public String password;
    public double rating;
    public String token;

    public User() {
    }

    public User(String fullName, String email, String image, String password, double rating, String token) {
        this.fullName = fullName;
        this.email = email;
        this.image = image;
        this.password = password;
        this.rating = rating;
        this.token = token;
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

    public String getToken() {
        return token;
    }

    public void setToken(String token) {
        this.token = token;
    }

    public static double calculateRating(double currentRating, double newRating){
        return (currentRating + newRating) / 2;
    }
}
