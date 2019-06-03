package Model.Classes;

import java.util.List;

public class Devices extends Item{

    public int warranty;

    public Devices(){

    }

    public Devices(String title, String type, String platform, double price, Shipping shipping, String description, String images, int user_idUser, List<Category> category) {
        super(title, type, platform, price, shipping, description, images, user_idUser, category);
    }

    public Devices(int warranty) {
        this.warranty = warranty;
    }


    public int getWarranty() {
        return warranty;
    }

    public void setWarranty(int warranty) {
        this.warranty = warranty;
    }
}
