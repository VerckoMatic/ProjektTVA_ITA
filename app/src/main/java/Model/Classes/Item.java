package Model.Classes;

public class Item {
    public String title;
    public String type;
    public String platform;
    public double price;
    public Shipping shipping;
    public String description;
    public String images;


    public Item(){

    }

    public Item(String title, String type, String platform, double price, Shipping shipping, String description, String images) {
        this.title = title;
        this.type = type;
        this.platform = platform;
        this.price = price;
        this.shipping = shipping;
        this.description = description;
        this.images = images;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public double getPrice() {
        return price;
    }

    public void setPrice(double price) {
        this.price = price;
    }

    public Shipping getShipping() {
        return shipping;
    }

    public void setShipping(Shipping shipping) {
        this.shipping = shipping;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getImages() {
        return images;
    }

    public void setImages(String images) {
        this.images = images;
    }

}
