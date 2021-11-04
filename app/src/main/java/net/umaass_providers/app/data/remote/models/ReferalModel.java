package net.umaass_providers.app.data.remote.models;

import com.google.gson.annotations.SerializedName;

public class ReferalModel {

    @SerializedName("id")
    private int id;

    @SerializedName("user")
    private User user;

    public int getId(){
        return id;
    }

    public User getUser(){
        return user;
    }
}