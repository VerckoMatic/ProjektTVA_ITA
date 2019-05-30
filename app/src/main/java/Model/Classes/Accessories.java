package Model.Classes;

public class Accessories extends Item {

    public String accessorieType;

    public Accessories(){

    }

    public Accessories(String itemType) {
        this.accessorieType = itemType;
    }

    public Accessories(String title, String type, String platform, double price, Shipping shipping, String description, String images, String accessorieType) {
        super(title, type, platform, price, shipping, description, images);
        this.accessorieType = accessorieType;
    }

    public String getAccessorieType() {
        return accessorieType;
    }

    public void setAccessorieType(String accessorieType) {
        this.accessorieType = accessorieType;
    }
}
