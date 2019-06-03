package Model.Classes;

import java.util.List;

public class Game extends Item{

    public Integer subscription;


    public Game(){

    }

    public Game(Integer subscription) {
        this.subscription = subscription;
    }

    public Game(String title, String type, String platform, double price, Shipping shipping, String description, String images, int user_idUser, List<Category> category, Integer subscription) {
        super(title, type, platform, price, shipping, description, images, user_idUser, category);
        this.subscription = subscription;
    }

    public Integer getSubscription() {
        return subscription;
    }

    public void setSubscription(Integer subscription) {
        this.subscription = subscription;
    }
}
