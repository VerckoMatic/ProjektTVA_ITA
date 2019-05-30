package Model.Classes;

public class Devices extends Item{

    public boolean warranty;

    public Devices(){

    }

    public Devices(boolean warranty) {
        this.warranty = warranty;
    }

    public Devices(String title, String type, String platform, double price, Shipping shipping, String description, String images, boolean warranty) {
        super(title, type, platform, price, shipping, description, images);
        this.warranty = warranty;
    }

    public boolean getWarranty() {
        return warranty;
    }

    public void setWarranty(boolean warranty) {
        this.warranty = warranty;
    }
}
