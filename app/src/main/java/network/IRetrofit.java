package network;

import Model.Classes.Accessories;
import Model.Classes.Devices;
import Model.Classes.Game;
import Model.Classes.Item;
import Model.ResponsePOJO.CategoriesResponsePOJOlist;
import Model.ResponsePOJO.ItemResponsePOJOlist;
import Model.Classes.Response;
import Model.Classes.User;
import Model.ResponsePOJO.UserResponsePOJOlist;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import rx.Observable;

public interface IRetrofit {


    //Users
    @POST("users/auth")
    Observable<Response> login(@Body User user);

    @POST("users")
    Observable<Response> register(@Body User user);

    @POST("users/{email}/passwordInit")
    Observable<Response> resetPasswordInit(@Path("email") String email);

    @GET("users/{User_idUser}")
    Call<UserResponsePOJOlist> getUserById(@Path("User_idUser") int User_idUser);

    @GET("items/categoryHasItem/{Item_idItem}")
    Call<CategoriesResponsePOJOlist> getCategoryNames(@Path("Item_idItem") int Item_idItem);

    @POST("users/{email}/passwordFinish")
    Observable<Response> resetPasswordFinish(@Path("email") String email, @Body User user);

    @PUT("users/{idUser}")
    Call<Response> updateUsersImage(@Path("idUser") int idUser, @Body User user);


    //Items
    @GET("items")
    Call<ItemResponsePOJOlist> getAllItems();

    @GET("items/{User_idUser}")
    Call<ItemResponsePOJOlist> getAllItemsByUserId(@Path("User_idUser") int User_idUser);

    @POST("items")
    Observable<Response> createItem(@Body Item item);

    @POST("items/game")
    Observable<Response> createGame(@Body Game game);

    @POST("items/game")
    Observable<Response> createDevices(@Body Devices devices);

    @POST("items/game")
    Observable<Response> createAccessories(@Body Accessories accessories);

    @Multipart
    @POST("/items/upload")
    Observable<Response> postImage(@Part MultipartBody.Part image, @Part("file") RequestBody name);

    @DELETE("/items/{idItem}")
    Call<Response> deleteOneItem(@Path("idItem") int idItem);


}
