package com.example.matic.projekttva;

import android.content.Context;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mockito;

import java.util.ArrayList;
import java.util.List;

import Model.Classes.Accessories;
import Model.Classes.Category;
import Model.Classes.ChatRoom;
import Model.Classes.Devices;
import Model.Classes.Game;
import Model.Classes.Item;
import Model.Classes.Message;
import Model.Classes.Response;
import Model.Classes.Shipping;
import Model.Classes.User;
import Model.ResponsePOJO.MessagePOJO;

import static org.mockito.Mockito.*;

import static org.junit.Assert.assertEquals;

public class ClassesUnitTest {
    @Test
    public void AccessoriesTest() {

        //  public String title;
        //    public String type;
        //    public String platform;
        //    public double price;
        //    public Shipping shipping;
        //    public String description;
        //    public String images;
        //    public int User_idUser;
        //    public List<Category> category;

        Accessories accessories = new Accessories();

        accessories.setItemType("test");
        assertEquals(accessories.getItemType(), "test");

        accessories.setTitle("test");
        assertEquals(accessories.getTitle(), "test");

        accessories.setPlatform("test");
        assertEquals(accessories.getPlatform(), "test");

        accessories.setPrice(33.2);
        assertEquals(accessories.getPrice(), 33.2, 0.0);

        Shipping shipping = new Shipping("test", "test");
        accessories.setShipping(shipping);
        assertEquals(accessories.getShipping().getPickUpLocation(), "test");
        assertEquals(accessories.getShipping().getShippingType(), "test");

        accessories.setDescription("test");
        assertEquals(accessories.getDescription(), "test");

        accessories.setImages("test");
        assertEquals(accessories.getImages(), "test");
        accessories.setUser_idUser(1);
        assertEquals(accessories.getUser_idUser(), 1);

        Category category = new Category();
        category.setName("test");
        List<Category> categories = new ArrayList<Category>();
        categories.add(category);
        accessories.setCategory(categories);
        assertEquals(accessories.getCategory().get(0).getName(), "test");

    }
       @Test
    public void CategoryTest() {
           Category category = mock(Category.class);

           when(category.getName()).thenReturn("test");

           assertEquals(category.getName(), "test");

    }

    @Test
    public void ChatRoomTest() {

        ChatRoom test = mock(ChatRoom.class);

        when(test.getId()).thenReturn("test");
        when(test.getIdRoom()).thenReturn("test");
        when(test.getReciever()).thenReturn("test");
        when(test.getSender()).thenReturn("test");

        assertEquals(test.getId(), "test");
        assertEquals(test.getIdRoom(), "test");
        assertEquals(test.getReciever(), "test");
        assertEquals(test.getSender(), "test");
    }

    @Test
    public void DevicesTest() {

        //  public String title;
        //    public String type;
        //    public String platform;
        //    public double price;
        //    public Shipping shipping;
        //    public String description;
        //    public String images;
        //    public int User_idUser;
        //    public List<Category> category;
        //    public int warranty
        Devices test = new Devices();

        test.setWarranty(1);
        assertEquals(test.getWarranty(), 1);

        test.setTitle("test");
        assertEquals(test.getTitle(), "test");

        test.setPlatform("test");
        assertEquals(test.getPlatform(), "test");

        test.setPrice(33.2);
        assertEquals(test.getPrice(), 33.2, 0.0);

        Shipping shipping = new Shipping("test", "test");
        test.setShipping(shipping);
        assertEquals(test.getShipping().getPickUpLocation(), "test");
        assertEquals(test.getShipping().getShippingType(), "test");

        test.setDescription("test");
        assertEquals(test.getDescription(), "test");

        test.setImages("test");
        assertEquals(test.getImages(), "test");
        test.setUser_idUser(1);
        assertEquals(test.getUser_idUser(), 1);

        Category category = new Category();
        category.setName("test");
        List<Category> categories = new ArrayList<Category>();
        categories.add(category);
        test.setCategory(categories);
        assertEquals(test.getCategory().get(0).getName(), "test");
    }

    @Test
    public void GameTest() {

        //  public String title;
        //    public String type;
        //    public String platform;
        //    public double price;
        //    public Shipping shipping;
        //    public String description;
        //    public String images;
        //    public int User_idUser;
        //    public List<Category> category;
        //    public int subscription
        Game test = new Game();

        test.setSubscription(1);
        test.setTitle("test");
        assertEquals(test.getTitle(), "test");

        test.setPlatform("test");
        assertEquals(test.getPlatform(), "test");

        test.setPrice(33.2);
        assertEquals(test.getPrice(), 33.2, 0.0);

        Shipping shipping = new Shipping("test", "test");
        test.setShipping(shipping);
        assertEquals(test.getShipping().getPickUpLocation(), "test");
        assertEquals(test.getShipping().getShippingType(), "test");

        test.setDescription("test");
        assertEquals(test.getDescription(), "test");

        test.setImages("test");
        assertEquals(test.getImages(), "test");
        test.setUser_idUser(1);
        assertEquals(test.getUser_idUser(), 1);

        Category category = new Category();
        category.setName("test");
        List<Category> categories = new ArrayList<Category>();
        categories.add(category);
        test.setCategory(categories);
        assertEquals(test.getCategory().get(0).getName(), "test");
    }

    @Test
    public void ItemTest() {

        //  public String title;
        //    public String type;
        //    public String platform;
        //    public double price;
        //    public Shipping shipping;
        //    public String description;
        //    public String images;
        //    public int User_idUser;
        //    public List<Category> category;
        Item test = new Item();

        test.setTitle("test");
        assertEquals(test.getTitle(), "test");

        test.setPlatform("test");
        assertEquals(test.getPlatform(), "test");

        test.setPrice(33.2);
        assertEquals(test.getPrice(), 33.2, 0.0);

        Shipping shipping = new Shipping("test", "test");
        test.setShipping(shipping);
        assertEquals(test.getShipping().getPickUpLocation(), "test");
        assertEquals(test.getShipping().getShippingType(), "test");

        test.setDescription("test");
        assertEquals(test.getDescription(), "test");

        test.setImages("test");
        assertEquals(test.getImages(), "test");
        test.setUser_idUser(1);
        assertEquals(test.getUser_idUser(), 1);

        Category category = new Category();
        category.setName("test");
        List<Category> categories = new ArrayList<Category>();
        categories.add(category);
        test.setCategory(categories);
        assertEquals(test.getCategory().get(0).getName(), "test");
    }




    @Test
    public void MessageTest(){
        //   public String message;
        //    public String receiverEmail;
        Message test = new Message();

        test.setMessage("test");
        assertEquals(test.getMessage(), "test");
        test.setReceiverEmail("test");
        assertEquals(test.getReceiverEmail(), "test");

    }


    @Test
    public void ResponseTest(){

        // private String message;
        //    private String token;
        //    private String idUser;
        //    private String image;
        Response test = new Response();

        test.setToken("test");
        assertEquals(test.getToken(), "test");

        test.setImage("test");
        assertEquals(test.getImage(), "test");

        test.setIdUser("test");
        assertEquals(test.getIdUser(), "test");

        test.setMessage("test");
        assertEquals(test.getMessage(), "test");
    }


    @Test
    public void ShippingTest(){

        // public String shippingType;
        //    public String pickUpLocation;
        Shipping test = new Shipping();

        test.setPickUpLocation("test");
        assertEquals(test.getPickUpLocation(), "test");

        test.setShippingType("test");
        assertEquals(test.getShippingType(), "test");

    }



    @Test
    public void UserTest(){

        //    public String fullName;
        //    public String email;
        //    public String image;
        //    public String password;
        //    public double rating;
        //    public String token;
        //    public String temp_password_time;
        //    public String temp_password;

        User test = new User();

        test.setImage("test");
        assertEquals(test.getImage(), "test");

        test.setToken("test");
        assertEquals(test.getToken(), "test");

        test.setPassword("test");
        assertEquals(test.getPassword(), "test");

        test.setEmail("test");
        assertEquals(test.getEmail(), "test");

        test.setFullName("test");
        assertEquals(test.getFullName(), "test");

        test.setRating(3.3);
        assertEquals(test.getRating(), 3.3, 0);

        test.setTemp_password("test");
        assertEquals(test.getTemp_password(), "test");

        test.setTemp_password_time("test");
        assertEquals(test.getTemp_password_time(), "test");
    }
}
