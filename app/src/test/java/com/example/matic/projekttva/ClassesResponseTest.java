package com.example.matic.projekttva;

import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

import Model.Classes.Category;
import Model.Classes.Item;
import Model.Classes.Shipping;
import Model.ResponsePOJO.CategoriesResponsePOJOlist;
import Model.ResponsePOJO.ItemResponsePOJO;
import Model.ResponsePOJO.ItemResponsePOJOlist;
import Model.ResponsePOJO.MessagePOJO;
import Model.ResponsePOJO.UserResponsePOJO;
import Model.ResponsePOJO.UserResponsePOJOlist;

import static org.junit.Assert.assertEquals;

public class ClassesResponseTest {

    @Test
    public void MessagePOJOTest(){

        //    private String id;
        //    private String text;
        //    private String email;
        //    private String email_receiver;
        //    private String color;
        //    private String room;
        //    private boolean belongsToCurrentUser;
        MessagePOJO test = new MessagePOJO();

        test.setId("test");
        assertEquals(test.getId(), "test");

        test.setBelongsToCurrentUser(true);
        assertEquals(test.isBelongsToCurrentUser(), true);

        test.setColor("test");
        assertEquals(test.getColor(), "test");

        test.setEmail("test");
        assertEquals(test.getEmail(), "test");

        test.setEmail_receiver("test");
        assertEquals(test.getEmail_receiver(), "test");

        test.setRoom("test");
        assertEquals(test.getRoom(), "test");

        test.setText("test");
        assertEquals(test.getText(), "test");
    }

    @Test
    public void CategoriesResponsePOJOlistTest(){

        //  private List<List<Category>> categories

        CategoriesResponsePOJOlist test = new CategoriesResponsePOJOlist();

        List<List<Category>> categories = new ArrayList<List<Category>>();
        List<Category> catList = new ArrayList<Category>();
        Category category = new Category("test");
        catList.add(category);
        categories.add(catList);

        test.setCategories(categories);
        assertEquals(test.getCategories().get(0).get(0).getName(), "test");
    }


    @Test
    public void ItemResponsePOJO(){


        ItemResponsePOJO test = new ItemResponsePOJO();

        test.setTitle("test");
        assertEquals(test.getTitle(), "test");

        test.setPlatform("test");
        assertEquals(test.getPlatform(), "test");

        test.setPrice("3");
        assertEquals(test.getPrice(), "3");

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


    }

    @Test
    public void ItemResponsePOJOlist(){


        ItemResponsePOJOlist test = new ItemResponsePOJOlist();

        List<ItemResponsePOJO> listOfList = new ArrayList<ItemResponsePOJO>();
        ItemResponsePOJO oneItem = new ItemResponsePOJO();
        oneItem.setIdItem(2);
        listOfList.add(oneItem);

        test.setItemResponsePOJOlist(listOfList);
        assertEquals(test.getItemResponsePOJOlist().get(0).getIdItem(), 2);


    }

    @Test
    public void UserResponsePOJOlistTest(){


        UserResponsePOJOlist test = new UserResponsePOJOlist();

        List<UserResponsePOJO> userResponsePOJOS = new ArrayList<UserResponsePOJO>();
        UserResponsePOJO oneItem = new UserResponsePOJO();
        oneItem.setEmail("test");
        userResponsePOJOS.add(oneItem);

        test.setUserResponsePOJOS(userResponsePOJOS);
        assertEquals(test.getUserResponsePOJOS().get(0).getEmail(), "test");


    }



    @Test
    public void UserResponsePOJOTest(){

        /*public int idUser;
        public String fullName;
        public String email;
        public String image;
        public String password;
        public double rating;*/

        UserResponsePOJO test = new UserResponsePOJO();
        test.setEmail("test");
        test.setFullName("test");
        test.setIdUser(2);
        test.setImage("test");
        test.setPassword("test");
        test.setRating(22);

        assertEquals(test.getEmail(), "test");
        assertEquals(test.getFullName(), "test");
        assertEquals(test.getIdUser(), 2);
        assertEquals(test.getImage(), "test");
        assertEquals(test.getPassword(), "test");
        assertEquals(test.getRating(), 22, 0);
    }
}
