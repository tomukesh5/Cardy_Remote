package com.cardyapp.Models;

/**
 * Created by Priyanka on 1/26/2018.
 */

public class ErrorBody {

    private boolean status;
    private String message;

    public boolean getStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
