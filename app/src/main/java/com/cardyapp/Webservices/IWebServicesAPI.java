package com.cardyapp.Webservices;


import com.cardyapp.Models.SignInModel;
import com.cardyapp.Models.SignUpModel;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

/**
 * Created by webwerks on 17/08/17.
 */

public interface IWebServicesAPI {

    @FormUrlEncoded
    @POST("api/apiName")
    Call<ResponseBody> login(
            @Field("email") String userName,
            @Field("password") String pwd,
            @Field("android_registration_id") String token_id,
            @Field("languages_id") String languageId
    );

    @FormUrlEncoded
    @POST("users_api/createuser")
    Call<SignUpModel> signUp(@Field("email") String email, @Field("password") String password, @Field("socialusertype") String socialusertype, @Field("socialuserdata") String socialuserdata, @Field("devicetype") String devicetype);

    @FormUrlEncoded
    @POST("users_api/login")
    Call<SignInModel> signIn(@Field("email") String email, @Field("password") String password, @Field("socialusertype") String socialusertype, @Field("socialuserdata") String socialuserdata, @Field("devicetype") String devicetype);

    @FormUrlEncoded
    @POST("users_api/updateusersettings")
    Call<ResponseBody> updateLocation(@Field("userid") String userid, @Field("latitude") String latitude, @Field("longitude")String longitude);
}
