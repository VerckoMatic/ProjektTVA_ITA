package Model;

import android.util.Patterns;

public class UserValidation {

    public static boolean validateRegistrationEmail(String email){
        if(email != null && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return true;
        }else{
            return false;
        }
    }

    public static boolean validateRegistrationPassword(String password) {
        if(password != null && !password.equals("") && password.length() > 5){
            return true;
        }else{
            return false;
        }
    }

    public static boolean validateRegistrationName(String name) {
        if(name != null && !name.equals("") && name.length() > 2){
            return true;
        }else{
            return false;
        }
    }

    public static boolean validateRegistrationConfirmPassword(String cofirmPassword, String password) {
        if(cofirmPassword != null && !password.equals("") && !cofirmPassword.equals("") && cofirmPassword.equals(password)){
            return true;
        }else{
            return false;
        }
    }

    public static boolean validateLoginEmail(String email) {
        if(email != null && !email.equals("") && Patterns.EMAIL_ADDRESS.matcher(email).matches()){
            return true;
        }else{
            return false;
        }
    }

    public static boolean validateLoginPassword(String password) {
        if(password != null && !password.equals("")){
            return true;
        }else{
            return false;
        }
    }

}
