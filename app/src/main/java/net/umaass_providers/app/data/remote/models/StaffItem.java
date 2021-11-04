package net.umaass_providers.app.data.remote.models;

import com.google.gson.annotations.JsonAdapter;
import com.google.gson.annotations.SerializedName;

import net.umaass_providers.app.data.remote.utils.EmptyStringAsNullTypeAdapter;

import java.util.List;


public class StaffItem {

    @SerializedName("industry_id")
    private int industryId;

    @SerializedName("role")
    private String role;

    @SerializedName("user_id")
    private int userId;

    @SerializedName("phone")
    private String phone;
    @SerializedName("rate")
    private float rate;
    @SerializedName("permissions")
    private List<Integer> permissions;

    @SerializedName("name")
    private String name;

    @SerializedName("id")
    private int id;

    @SerializedName("avatar")
    @JsonAdapter(EmptyStringAsNullTypeAdapter.class)
    private Avatar avatar;

    @SerializedName("email")
    private String email;

    public void setIndustryId(int industryId) {
        this.industryId = industryId;
    }

    public int getIndustryId() {
        return industryId;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getRole() {
        return role;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getPhone() {
        return phone;
    }

    public void setPermissions(List<Integer> permissions) {
        this.permissions = permissions;
    }

    public List<Integer> getPermissions() {
        return permissions;
    }

    public float getRate() {
        return rate;
    }

    public void setRate(float rate) {
        this.rate = rate;
    }

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


    public void setAvatar(Avatar avatar) {
        this.avatar = avatar;
    }

    public Avatar getAvatar() {
        return avatar;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getEmail() {
        return email;
    }
}