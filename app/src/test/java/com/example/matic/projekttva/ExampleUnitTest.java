package com.example.matic.projekttva;

import org.junit.Test;

import Model.Classes.Item;
import Model.Classes.Message;
import Model.Classes.User;
import Model.UserValidation;

import static org.junit.Assert.*;

/**
 * Example local unit test, which will execute on the development machine (host).
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
public class ExampleUnitTest {
    @Test
    public void addition_isCorrect() {
        assertEquals(4, 2 + 2);
    }

    @Test
    public void user_isImplemented(){
        //id, email, provider, images, password, rating
        User user = new User();
        user.setFullName("Vercko");
        user.setLastName("Vercko");
        user.setEmail("matic.vercko@random.com");
        user.setImage("images.png");
        user.setPassword("password123");
        user.setRating(4);

        assertEquals(3,  user.calculateRating(user.getRating(), 2) , 0.02);

    }

    @Test
    public void item_isImplemented(){

        Item item = new Item();
        item.setTitle("Assassin's creed");
        item.setPlatform("PS4");
        item.setPrice(32.22);
        item.setShipping("Osebni prevzem");
        item.setDescription("Brand new item.");
        item.setImages("images.png");

        item.setPrice(35);

        item.setShipping("Osebni prevzem");

        assertEquals(item.getPrice(), 35, 0);
    }

    @Test
    public void message_isImplemented(){

        Message message = new Message();

    }
    @Test
    public void registerValidation(){

        User user = new User();
        user.setEmail("matic.vercko@random.com");
        user.setImage("images.png");
        user.setPassword("password123");
        user.setRating(4);

        assertEquals(true, UserValidation.validateRegistrationEmail(user.getEmail()));
        assertEquals(true, UserValidation.validateRegistrationPassword(user.getPassword()));

    }

    @Test
    public void loginValidation(){

        User user = new User();
        user.setEmail("matic.vercko@random.com");
        user.setImage("images.png");
        user.setPassword("password123");
        user.setRating(4);

        assertEquals(true, UserValidation.validateLoginEmail(user.getEmail()));
        assertEquals(true, UserValidation.validateLoginPassword(user.getPassword()));
    }
}