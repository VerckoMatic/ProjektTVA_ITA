package network;

import Model.Classes.Item;
import Model.Classes.Response;
import Model.Classes.User;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Part;
import retrofit2.http.Path;
import rx.Observable;

public interface IRetrofit {

    @POST("users/auth")
    Observable<Response> login(@Body User user);

    @POST("users")
    Observable<Response> register(@Body User user);

    @POST("users/{email}/passwordInit")
    Observable<Response> resetPasswordInit(@Path("email") String email);

    @POST("users/{email}/passwordFinish")
    Observable<Response> resetPasswordFinish(@Path("email") String email, @Body User user);

    @POST("items")
    Observable<Response> createItem(@Body Item item);

    @Multipart
    @POST("/items/upload")
    Observable<Response> postImage(@Part MultipartBody.Part image, @Part("file") RequestBody name);
}
