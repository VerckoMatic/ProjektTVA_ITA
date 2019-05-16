package Model.Classes;

public class Shipping {

    public String shippingType;
    public String pickUpLocation;

    public Shipping() {
    }

    public Shipping(String shippingType, String pickUpLocation) {
        this.shippingType = shippingType;
        this.pickUpLocation = pickUpLocation;
    }

    public String getShippingType() {
        return shippingType;
    }

    public void setShippingType(String shippingType) {
        this.shippingType = shippingType;
    }

    public String getPickUpLocation() {
        return pickUpLocation;
    }

    public void setPickUpLocation(String pickUpLocation) {
        this.pickUpLocation = pickUpLocation;
    }
}
