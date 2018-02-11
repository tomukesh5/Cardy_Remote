package com.cardyapp.Models;

/**
 * Created by Priyanka on 2/11/2018.
 */

public class GetUserProfileModel extends BaseResponse {

    private Userdata data;

    public Userdata getData() {
        return data;
    }

    public void setData(Userdata data) {
        this.data = data;
    }
}
