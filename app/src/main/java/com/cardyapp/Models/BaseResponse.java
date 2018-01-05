package com.cardyapp.Models;

/**
 * Created by Priyanka on 1/5/2018.
 */

public class BaseResponse {

    private boolean status;
    private String message;

    public boolean getIsStatus() {
        return status;
    }

    public void setIsStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
