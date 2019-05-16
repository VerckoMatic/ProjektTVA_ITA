package Model;

public class UserValidation {

    public static boolean validateRegistrationEmail(String email){
        if(email != null){
            return true;
        }else{
            return false;
        }
    }

    public static boolean validateRegistrationPassword(String password) {
        if(password != null){
            return true;
        }else{
            return false;
        }
    }

    public static boolean validateRegistrationName(String name) {
        if(name != null){
            return true;
        }else{
            return false;
        }
    }

    public static boolean validateRegistrationConfirmPassword(String name) {
        if(name != null){
            return true;
        }else{
            return false;
        }
    }

    public static boolean validateLoginEmail(String email) {
        if(email != null){
            return true;
        }else{
            return false;
        }
    }

    public static boolean validateLoginPassword(String password) {
        if(password != null){
            return true;
        }else{
            return false;
        }
    }
}
