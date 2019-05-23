package network;

import Model.Classes.Response;
import Model.Classes.User;
import retrofit2.http.Body;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Path;
import rx.Observable;

public interface IRetrofit {

    @POST("users/auth")
    Observable<Response> login(@Body User user);

    @POST("users")
    Observable<Response> register(@Body User user);
}
