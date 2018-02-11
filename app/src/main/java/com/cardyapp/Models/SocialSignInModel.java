package com.cardyapp.Models;

/**
 * Created by Priyanka on 2/11/2018.
 */

public class SocialSignInModel extends BaseResponse {

    private boolean is_social_account_verified;
    private Userdata data;

    public boolean getIs_social_account_verified() {
        return is_social_account_verified;
    }

    public void setIs_social_account_verified(boolean is_social_account_verified) {
        this.is_social_account_verified = is_social_account_verified;
    }

    public Userdata getData() {
        return data;
    }

    public void setData(Userdata data) {
        this.data = data;
    }
}
