package com.cardyapp.Models;

import com.google.gson.Gson;

import java.io.File;

/**
 * Created by Priyanka on 1/8/2018.
 */

public class UploadProfilePicModel {

    private String userid;
    private File profilepic;

    public UploadProfilePicModel() {

    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public File getProfilepic() {
        return profilepic;
    }

    public void setProfilepic(File profilepic) {
        this.profilepic = profilepic;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
