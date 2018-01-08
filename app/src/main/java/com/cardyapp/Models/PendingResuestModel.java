package com.cardyapp.Models;

import java.util.List;

/**
 * Created by rac on 8/1/18.
 */

public class PendingResuestModel extends BaseResponse {

    private List<Userdata> data;

    public List<Userdata> getData() {
        return data;
    }

    public void setData(List<Userdata> data) {
        this.data = data;
    }
}
