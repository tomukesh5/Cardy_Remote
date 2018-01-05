package com.cardyapp.Models;

public class SignUpModel extends BaseResponse {

    private String userid;

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    @Override
    public String toString() {
        return "ClassPojo [message = " + getMessage() + ", status = " + getIsStatus() + ", userid = " + userid + "]";
    }
}

			