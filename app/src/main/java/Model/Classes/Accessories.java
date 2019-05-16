package Model.Classes;

public class Accessories extends Item {

    public String itemType;

    public Accessories(){

    }

    public Accessories(String itemType) {
        this.itemType = itemType;
    }

    public Accessories(String title, String platform, double price, Shipping shipping, String description, String images, String pickupLocation, String itemType) {
        super(title, platform, price, shipping, description, images, pickupLocation);
        this.itemType = itemType;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }
}
