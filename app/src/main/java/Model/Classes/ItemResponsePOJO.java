package Model.Classes;

import java.util.List;

public class ItemResponsePOJO {

    public int idItem;
    public String title;
    public String type;
    public String platform;
    public String price;
    public Shipping shipping;
    public String description;
    public String images;
    public int User_idUser;
    String shippingType;
    String pickupLocation;
    public Integer subscription;
    public int warranty;
    public String itemType;

    public ItemResponsePOJO() {
    }

    public ItemResponsePOJO(int idItem, String title, String type, String platform, String price, Shipping shipping, String description, String images, int user_idUser, String shippingType, String pickupLocation, Integer subscription, int warranty, String itemType) {
        this.idItem = idItem;
        this.title = title;
        this.type = type;
        this.platform = platform;
        this.price = price;
        this.shipping = shipping;
        this.description = description;
        this.images = images;
        User_idUser = user_idUser;
        this.shippingType = shippingType;
        this.pickupLocation = pickupLocation;
        this.subscription = subscription;
        this.warranty = warranty;
        this.itemType = itemType;
    }

    public int getIdItem() {
        return idItem;
    }

    public void setIdItem(int idItem) {
        this.idItem = idItem;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getPlatform() {
        return platform;
    }

    public void setPlatform(String platform) {
        this.platform = platform;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
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

    public int getUser_idUser() {
        return User_idUser;
    }

    public void setUser_idUser(int user_idUser) {
        User_idUser = user_idUser;
    }

    public Integer getSubscription() {
        return subscription;
    }

    public void setSubscription(Integer subscription) {
        this.subscription = subscription;
    }

    public int getWarranty() {
        return warranty;
    }

    public void setWarranty(int warranty) {
        this.warranty = warranty;
    }

    public String getItemType() {
        return itemType;
    }

    public void setItemType(String itemType) {
        this.itemType = itemType;
    }
}
