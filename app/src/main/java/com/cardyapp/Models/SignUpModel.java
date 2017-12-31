package com.cardyapp.Models;

public class SignUpModel
{
    private String message;

    private String status;

    private String userid;

    public String getMessage ()
    {
        return message;
    }

    public void setMessage (String message)
    {
        this.message = message;
    }

    public String getStatus ()
    {
        return status;
    }

    public void setStatus (String status)
    {
        this.status = status;
    }

    public String getUserid ()
    {
        return userid;
    }

    public void setUserid (String userid)
    {
        this.userid = userid;
    }

    @Override
    public String toString()
    {
        return "ClassPojo [message = "+message+", status = "+status+", userid = "+userid+"]";
    }
}

			