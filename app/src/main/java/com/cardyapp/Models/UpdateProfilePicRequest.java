package com.cardyapp.Models;

import java.io.Serializable;

import okhttp3.MultipartBody;

/**
 * Created by Priyanka on 3/11/2018.
 */

public class UpdateProfilePicRequest implements Serializable {

    private String userid;
    private MultipartBody.Part profilepic;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public MultipartBody.Part getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(MultipartBody.Part profilepic) {
        this.profilepic = profilepic;
    }
}
