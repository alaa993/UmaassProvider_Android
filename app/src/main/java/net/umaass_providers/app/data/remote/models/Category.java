package net.umaass_providers.app.data.remote.models;

import com.google.gson.annotations.SerializedName;

import net.umaass_providers.app.interfac.ListItem;

public class Category implements ListItem {

    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private int id;

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getId() {
        return id;
    }

    @Override
    public String getItemName() {
        return name;
    }

    @Override
    public String getItemId() {
        return id + "";
    }
}