package Model.Classes;

public class Game extends Item{

    public Boolean psPlus;


    public Game(){

    }
    public Game(Boolean psPlus) {
        this.psPlus = psPlus;
    }

    public Game(String title, String platform, double price, Shipping shipping, String description, String images, String pickupLocation, Boolean psPlus) {
        super(title, platform, price, shipping, description, images, pickupLocation);
        this.psPlus = psPlus;
    }

    public Boolean getPsPlus() {
        return psPlus;
    }

    public void setPsPlus(Boolean psPlus) {
        this.psPlus = psPlus;
    }
}
