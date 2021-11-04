package net.umaass_providers.app.data.remote.models;

import com.google.gson.annotations.SerializedName;

public class Api<T> {
    @SerializedName("data")
    T data;

    @SerializedName("message")
    private String message;

    @SerializedName("status")
    private int status;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }

}
