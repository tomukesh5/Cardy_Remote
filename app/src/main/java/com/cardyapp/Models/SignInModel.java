package com.cardyapp.Models;

/**
 * Created by webwerks on 31/12/17.
 */

public class SignInModel extends BaseResponse {

    private Userdata userdata;

    public Userdata getUserdata() {
        return userdata;
    }

    public void setUserdata(Userdata userdata) {
        this.userdata = userdata;
    }

    @Override
    public String toString() {
        return "ClassPojo [message = " + getMessage() + ", userdata = " + userdata + ", status = " + getIsStatus() + "]";
    }
}
