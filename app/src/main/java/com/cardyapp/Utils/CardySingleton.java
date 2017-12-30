package com.cardyapp.Utils;

import com.cardyapp.App.Cardy;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by webwerks on 04/12/17.
 */

public class CardySingleton {
    private static CardySingleton csInstance;

    public static CardySingleton getInstance()
    {
        if(csInstance == null){
            csInstance = new CardySingleton();
        }
        return csInstance;
    }

    public void callToEndUserLoginAPI(String endUserEmail, String endUserPwd, String pushTokenId, String languageId,String token, Callback callback){
        Call<ResponseBody> call = Cardy.instance().getApi().login(endUserEmail,endUserPwd,token,languageId);
        call.enqueue(callback);
    }

    public void callToSignUpAPI(String email, String password, String socialusertype, String socialUserData, Callback callback) {
        Call<ResponseBody> call = Cardy.instance().getApi().signUp(email, password, socialusertype, socialUserData, AppConstants.DEVICE_AOS);
        call.enqueue(callback);
    }

    public void callToSignInAPI(String email, String password, String socialusertype, String socialUserData, Callback callback) {
        Call<ResponseBody> call = Cardy.instance().getApi().signIn(email, password, socialusertype, socialUserData, AppConstants.DEVICE_AOS);
        call.enqueue(callback);
    }

    public void callToUpdateUserLocationAPI(String userid, String latitude, String longitude) {
        Call<ResponseBody> call = Cardy.instance().getApi().updateLocation(userid, latitude, longitude);
    }
}
