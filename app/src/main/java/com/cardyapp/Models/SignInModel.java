package com.cardyapp.Models;

/**
 * Created by webwerks on 31/12/17.
 */

public class SignInModel {

    private String message;

    private Userdata userdata;

    private String status;

    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    public Userdata getUserdata ()
    {
        return userdata;
    }

    public void setUserdata (Userdata userdata)
    {
        this.userdata = userdata;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [message = "+message+", userdata = "+userdata+", status = "+status+"]";
    }
}
