package Model.Classes;

public class Game extends Item{

    public String subscription;


    public Game(){

    }

    public Game(String subsrciption) {
        this.subscription = subsrciption;
    }

    public Game(String title, String type, String platform, double price, Shipping shipping, String description, String images, String subsrciption) {
        super(title, type, platform, price, shipping, description, images);
        this.subscription = subsrciption;
    }

    public String getSubscription() {
        return subscription;
    }

    public void setSubscription(String subscription) {
        this.subscription = subscription;
    }
}
