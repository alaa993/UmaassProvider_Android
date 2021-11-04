package net.umaass_providers.app.data.remote.utils;

import androidx.annotation.NonNull;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class Errors {

    @SerializedName("client_phone")
    private List<String> clientPhone;

    @SerializedName("from_to")
    private List<String> fromTo;

    @SerializedName("phone")
    private List<String> phone;

    public List<String> getPhone() {
        return phone;
    }

    public void setPhone(List<String> phone) {
        this.phone = phone;
    }

    public void setClientPhone(List<String> clientPhone) {
        this.clientPhone = clientPhone;
    }

    public List<String> getClientPhone() {
        return clientPhone;
    }

    public void setFromTo(List<String> fromTo) {
        this.fromTo = fromTo;
    }

    public List<String> getFromTo() {
        return fromTo;
    }

    @NonNull
    @Override
    public String toString() {
        String cl = clientPhone != null && clientPhone.size() > 0 ? clientPhone.get(0) : "";
        String ft = fromTo != null && fromTo.size() > 0 ? fromTo.get(0) : "";
        String ph = phone != null && phone.size() > 0 ? phone.get(0) : "";
        return ft + " " + cl + " " + ph;
    }
}