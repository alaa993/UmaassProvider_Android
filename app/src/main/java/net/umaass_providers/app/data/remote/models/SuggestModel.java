package net.umaass_providers.app.data.remote.models;

import com.google.gson.annotations.SerializedName;

public class SuggestModel {

    @SerializedName("fullname")
    private String fullname;
    @SerializedName("mobile")
    private String mobile;
    @SerializedName("country")
    private String country;
    @SerializedName("province")
    private String province;
    @SerializedName("specialty")
    private String specialty;


    public SuggestModel(String fullname, String mobile, String country, String province, String specialty) {
        this.fullname = fullname;
        this.mobile = mobile;
        this.country = country;
        this.province = province;
        this.specialty = specialty;
    }

    public String getFullname() {
        return fullname;
    }

    public void setFullname(String fullname) {
        this.fullname = fullname;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getSpeciality() {
        return specialty;
    }

    public void setSpeciality(String speciality) {
        this.specialty = specialty;
    }

}
