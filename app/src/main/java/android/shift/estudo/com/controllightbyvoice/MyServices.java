package android.shift.estudo.com.controllightbyvoice;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

/**
 * Created by ricardoogliari on 7/22/16.
 */

public interface MyServices {

    @GET("ligar")
    Call<Object> ligar();

    @GET("desligar")
    Call<Object> desligar();

}
