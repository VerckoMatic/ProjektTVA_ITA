package com.example.matic.projekttva;

import android.content.Context;

import org.junit.Test;
import org.junit.runner.RunWith;

import Model.Classes.Accessories;
import Model.Classes.Devices;
import Model.Classes.Game;
import Model.Classes.Item;
import Model.Classes.Response;
import Model.Classes.User;
import Model.ResponsePOJO.CategoriesResponsePOJOlist;
import Model.ResponsePOJO.ItemResponsePOJOlist;
import Model.ResponsePOJO.UserResponsePOJOlist;
import network.IRetrofit;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import rx.Observable;
import static org.junit.Assert.assertEquals;
import retrofit2.mock.*;

public class IRetrofitServiceStub implements IRetrofit {

    private final BehaviorDelegate<IRetrofit> delegate;

    public IRetrofitServiceStub(BehaviorDelegate<IRetrofit> service) {
        this.delegate = service;
    }

    @Override
    public Observable<Response> login(User user) {
        Response serviceResponse = new Response();
        serviceResponse.setMessage("solski.test@gmail.com");
        serviceResponse.setIdUser("23");
        serviceResponse.setImage("/uploads/af22b351031c58a09d685c9953146d15.png");
        serviceResponse.setToken("s2141afs");

        User userService = new User();
        return delegate.returningResponse(serviceResponse).login(userService);
    }


    @Override
    public Observable<Response> register(User user) {
        return null;
    }

    @Override
    public Observable<Response> resetPasswordInit(String email) {
        return null;
    }

    @Override
    public Call<UserResponsePOJOlist> getUserById(int User_idUser) {
        return null;
    }

    @Override
    public Call<CategoriesResponsePOJOlist> getCategoryNames(int Item_idItem) {
        return null;
    }

    @Override
    public Observable<Response> resetPasswordFinish(String email, User user) {
        return null;
    }

    @Override
    public Call<Response> updateUsersImage(int idUser, User user) {
        return null;
    }

    @Override
    public Call<ItemResponsePOJOlist> getAllItems() {
        return null;
    }

    @Override
    public Call<ItemResponsePOJOlist> getAllItemsByUserId(int User_idUser) {
        return null;
    }

    @Override
    public Observable<Response> createItem(Item item) {
        return null;
    }

    @Override
    public Observable<Response> createGame(Game game) {
        return null;
    }

    @Override
    public Observable<Response> createDevices(Devices devices) {
        return null;
    }

    @Override
    public Observable<Response> createAccessories(Accessories accessories) {
        return null;
    }

    @Override
    public Observable<Response> postImage(MultipartBody.Part image, RequestBody name) {
        return null;
    }

    @Override
    public Call<Response> deleteOneItem(int idItem) {
        return null;
    }

    @Override
    public Observable<Response> updateOneItem(int idItem, Game game) {
        return null;
    }

    @Override
    public Observable<Response> updateDeviceItem(int idItem, Devices devices) {
        return null;
    }

    @Override
    public Observable<Response> updateAccessoriesItem(int idItem, Accessories accessories) {
        return null;
    }
}
