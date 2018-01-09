package com.cardyapp.Webservices;


import com.cardyapp.Models.BaseResponse;
import com.cardyapp.Models.PendingResuestModel;
import com.cardyapp.Models.SignInModel;
import com.cardyapp.Models.SignUpModel;
import com.cardyapp.Models.UploadProfilePicModel;
import com.cardyapp.Models.Userdata;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Query;

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
    Call<BaseResponse> updateLocation(@Field("userid") String userid, @Field("latitude") String latitude, @Field("longitude")String longitude);

    @POST("users_api/updateprofile")
    Call<BaseResponse> updateProfile(@Body Userdata userData);

    @POST("users_api/uploadprofilepic")
    Call<BaseResponse> updateProfilePic(@Body UploadProfilePicModel uploadProfilePicModel);

    @GET("users_api/getpendingrequests")
    Call<PendingResuestModel> getPendingRequests(@Query("id") String id);

    @GET("users_api/getconnections")
    Call<PendingResuestModel> getConnections(@Query("id") String id);

    @FormUrlEncoded
    @POST("users_api/searchusers")
    Call<PendingResuestModel> searchUserNearMe(@Field("userid") String userid);

    @FormUrlEncoded
    @POST("users_api/acceptrequest")
    Call<BaseResponse> acceptRequest(@Field("userid") String userid, @Field("requestbyuserid") String requestbyuserid);

    @FormUrlEncoded
    @POST("users_api/rejectrequest")
    Call<BaseResponse> rejectRequest(@Field("userid") String userid, @Field("requestbyuserid") String requestbyuserid);

    @FormUrlEncoded
    @POST("users_api/rejectrequest")
    Call<BaseResponse> accepAndRevertRequest(@Field("userid") String userid, @Field("requestbyuserid") String requestbyuserid);
}
