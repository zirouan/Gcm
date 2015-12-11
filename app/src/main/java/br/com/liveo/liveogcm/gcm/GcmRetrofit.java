package br.com.liveo.liveogcm.gcm;

import retrofit.Call;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.POST;

/**
 * Created by Rudsonlive on 01/12/15.
 */
public interface GcmRetrofit {
    @FormUrlEncoded
    @POST("gcmserver.php")
    Call<String> toSendRegistration(@Field("acao") String action,
                                    @Field("regId") String id);
}
