package Model.Classes;

import java.util.List;

public class Accessories extends Item {

    public String itemType;

    public Accessories(){

    }

    public Accessories(String itemType) {
        this.itemType = itemType;
    }

    public Accessories(String title, String type, String platform, double price, Shipping shipping, String description, String images, int user_idUser, List<Category> category, String accessorieType) {
        super(title, type, platform, price, shipping, description, images, user_idUser, category);
        this.itemType = accessorieType;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }
}
