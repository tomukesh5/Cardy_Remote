package com.cardyapp.Models;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Priyanka on 3/11/2018.
 */

public class SendMultipleRequest implements Serializable {

    private List<RequestConnection> requestdata;

    public List<RequestConnection> getRequestdata() {
        return requestdata;
    }

    public void setRequestdata(List<RequestConnection> requestdata) {
        this.requestdata = requestdata;
    }
}
