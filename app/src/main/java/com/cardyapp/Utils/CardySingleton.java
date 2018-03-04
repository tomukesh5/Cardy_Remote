package com.cardyapp.Utils;

import com.cardyapp.App.Cardy;
import com.cardyapp.Models.BaseResponse;
import com.cardyapp.Models.GetUserProfileModel;
import com.cardyapp.Models.PendingResuestModel;
import com.cardyapp.Models.RequestConnection;
import com.cardyapp.Models.SendOTPForgotPasswordModel;
import com.cardyapp.Models.SignInModel;
import com.cardyapp.Models.SignUpModel;
import com.cardyapp.Models.SocialSignInModel;
import com.cardyapp.Models.UploadProfilePicModel;
import com.cardyapp.Models.Userdata;

import java.io.File;
import java.util.List;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
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

    public void callToSignUpAPI(String mobile, String password, String socialusertype, String socialUserData, Callback callback) {
        Call<SignUpModel> call = Cardy.instance().getApi().signUp(mobile, password, socialusertype, socialUserData, AppConstants.DEVICE_AOS);
        call.enqueue(callback);
    }

    public void callToSignInAPI(String email, String password, String socialusertype, String socialUserData, Callback callback) {
        Call<SignInModel> call = Cardy.instance().getApi().signIn(email, password, socialusertype, socialUserData, AppConstants.DEVICE_AOS);
        call.enqueue(callback);
    }

    public void callToSocialSignInAPI(String email, String password, String socialusertype, String socialUserData, Callback callback) {
        Call<SocialSignInModel> call = Cardy.instance().getApi().socialSignIn(email, password, socialusertype, socialUserData, AppConstants.DEVICE_AOS);
        call.enqueue(callback);
    }

    public void callToVerifyOTPAPI(String userid, String otp, Callback callback) {
        Call<BaseResponse> call = Cardy.instance().getApi().verifyOTP(userid, otp);
        call.enqueue(callback);
    }

    public void callToUpdateUserLocationAPI(String userid, String latitude, String longitude, Callback callback) {
        Call<BaseResponse> call = Cardy.instance().getApi().updateLocation(userid, latitude, longitude);
        call.enqueue(callback);
    }

    public void callToGetProfileAPI(String id, Callback callback) {
        Call<GetUserProfileModel> call = Cardy.instance().getApi().getUserDetails(id);
        call.enqueue(callback);
    }

    public void callToUpdateUserProfileAPI(Userdata userdata, Callback callback) {
        Call<BaseResponse> call = Cardy.instance().getApi().updateProfile(userdata);
        call.enqueue(callback);
    }

    public void callToUpdateProfilePicAPI(String userid, File profilepic, Callback callback) {

        MultipartBody.Part file = null;
        if (null != profilepic)
            file = MultipartBody.Part.createFormData("profilePic", profilepic.getName(), RequestBody.create(MediaType.parse(CommonUtil.getMimeType(profilepic)), profilepic));

        Call<BaseResponse> call = Cardy.instance().getApi().updateProfilePic(userid, file);
        call.enqueue(callback);
    }

    public void callToGetPendingRequestsAPI(String id, Callback callback) {
        Call<PendingResuestModel> call = Cardy.instance().getApi().getPendingRequests(id);
        call.enqueue(callback);
    }

    public void callToGetConnectionsAPI(String id, Callback callback) {
        Call<PendingResuestModel> call = Cardy.instance().getApi().getConnections(id);
        call.enqueue(callback);
    }

    public void callToSearchUserNearMeAPI(String id, String distance, Callback callback) {
        Call<PendingResuestModel> call = Cardy.instance().getApi().searchUserNearMe(id, distance);
        call.enqueue(callback);
    }

    public void callToAcceptRequestAPI(String id, String requestbyuserid, Callback callback) {
        Call<BaseResponse> call = Cardy.instance().getApi().acceptRequest(id, requestbyuserid);
        call.enqueue(callback);
    }

    public void callToRejectRequestAPI(String id, String requestbyuserid, Callback callback) {
        Call<BaseResponse> call = Cardy.instance().getApi().rejectRequest(id, requestbyuserid);
        call.enqueue(callback);
    }

    public void callToAcceptAndRevertRequestAPI(String id, String requestbyuserid, Callback callback) {
        Call<BaseResponse> call = Cardy.instance().getApi().accepAndRevertRequest(id, requestbyuserid);
        call.enqueue(callback);
    }

    public void callToSendMultipleRequestAPI(List<RequestConnection> list, Callback callback) {
        Call<BaseResponse> call = Cardy.instance().getApi().sendMultipleConnections(list);
        call.enqueue(callback);
    }

    public void callToSendVerificationAPI(String userid, Callback callback) {
        Call<BaseResponse> call = Cardy.instance().getApi().sendVerificationOTP(userid);
        call.enqueue(callback);
    }

    public void callToResetPasswordAPI(String userid, String newpassword, Callback callback) {
        Call<BaseResponse> call = Cardy.instance().getApi().resetPassword(userid, newpassword);
        call.enqueue(callback);
    }

    public void callToSendOTPForgotPasswordAPI(String mobile, Callback callback) {
        Call<SendOTPForgotPasswordModel> call = Cardy.instance().getApi().sendOTPForgotPassword(mobile);
        call.enqueue(callback);
    }

    public void callToSyncContactAPI(String userid, List<String> contactsdata, Callback callback) {
        Call<BaseResponse> call = Cardy.instance().getApi().syncContacts(userid, contactsdata);
        call.enqueue(callback);
    }
}
