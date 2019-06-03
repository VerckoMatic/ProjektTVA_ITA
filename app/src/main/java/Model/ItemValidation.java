package Model;

import java.util.regex.Pattern;

public class ItemValidation {

 public static boolean validateDescriptionCreateItemFragments(String description){
     if(description != null && description.length() < 250 ){
         return true;
     }else{
         return false;
     }

 }

    public static boolean validateTitleCreateItemFragments(String title){
        if(title != null && title.length() < 40 && !title.equals("")){
            return true;
        }else{
            return false;
        }

    }

    public static boolean validatePriceBasicFragment(String price){

        if(!price.equals("")){
            try {
                double x = Double.parseDouble(price);
                return true;
            }
        catch(NumberFormatException e) {
                return false;
            }

        }else{
            return false;
        }

    }

    public static boolean validateLocationBasicFragment(String location) {
        if (location != null && location.length() < 45 && !location.equals("")) {
            return true;
        } else {
            return false;
        }
    }

    public static boolean validateItemTypeAccessoriesFragment(String itemType) {
        if (itemType != null && itemType.length() < 45 && !itemType.equals("")) {
            return true;
        } else {
            return false;
        }
    }

}
