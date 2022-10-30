package app.calc.alarmmaster.model;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.Query;

public interface UserService {
    @POST("login")
    Call<User> signIn(@Body User user);

    @POST("register")
    Call<SignUp> signUp(@Body SignUp sign);

    @POST("resendverefy")
    Call<SendVerify> resendVerefication(@Body SendVerify sign);

    @POST("verefyuser")
    Call<SignUp> verefyuser(@Body SignUp sign);

    @POST("cheskverefy")
    Call<SignUp> cheskverefy(@Body SignUp sign);

    @GET("gettimes")
    Call<Object> getTimes(@Header("Authorization") String token);

    @POST("addtime")
    Call<GetTime> addTime(@Header("Authorization") String token, @Body GetTime getTime);

    @POST("updatetime?index=0")
    Call<GetTime> updateTime(@Query("index") int param,@Header("Authorization") String token, @Body GetTime getTime);
}
