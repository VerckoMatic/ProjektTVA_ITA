package Model.Classes;

public class Devices extends Item{

    public String warranty;

    public Devices(){

    }

    public Devices(String warranty) {
        this.warranty = warranty;
    }

    public Devices(String title, String platform, double price, Shipping shipping, String description, String images, String pickupLocation, String warranty) {
        super(title, platform, price, shipping, description, images, pickupLocation);
        this.warranty = warranty;
    }

    public String getWarranty() {
        return warranty;
    }

    public void setWarranty(String warranty) {
        this.warranty = warranty;
    }
}
