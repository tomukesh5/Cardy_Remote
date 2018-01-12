package com.cardyapp.Models;

import com.google.gson.Gson;

import java.io.Serializable;

/**
 * Created by rac on 12/1/18.
 */

public class RequestConnection implements Serializable {

    private String userid;
    private String requesttouserid;

    public RequestConnection() {

    }

    public String getUserid() {
        return userid;
    }

    public void setUserid(String userid) {
        this.userid = userid;
    }

    public String getRequesttouserid() {
        return requesttouserid;
    }

    public void setRequesttouserid(String requesttouserid) {
        this.requesttouserid = requesttouserid;
    }

    @Override
    public String toString() {
        return new Gson().toJson(this);
    }
}
