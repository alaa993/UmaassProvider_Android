package net.umaass_providers.app.application;


import java.util.UUID;

public class Preference {

    public static boolean isLogin() {
        return getToken() != null;
    }

    public static void setValidLogin(boolean valid) {
        G.sharedPref.edit().putBoolean("isValidLogin", valid).apply();
    }

    public static boolean isValidLogin() {
        return G.sharedPref.getBoolean("isValidLogin", false);
    }

    public static void setIdUser(String idUser) {
        G.sharedPref.edit().putString("idUser", idUser).apply();
    }

    public static String getIdUser() {
        return G.sharedPref.getString("idUser", null);
    }

    public static String getPhone() {
        return G.sharedPref.getString("phone", null);
    }

    public static float getRate() {
        return G.sharedPref.getFloat("rate", 0);
    }

    public static void setToken(String token) {
        G.sharedPref.edit().putString("token", token).apply();
    }

    public static void setRate(float rate) {
        G.sharedPref.edit().putFloat("rate", rate).apply();
    }

    public static void setActiveIndustryId(String id) {
        G.sharedPref.edit().putString("ActiveIndustry", id).apply();
    }

    public static void setActiveStaffId(String id) {
        G.sharedPref.edit().putString("ActiveStaffId", id).apply();
    }

    public static String getActiveStaffId() {
        return G.sharedPref.getString("ActiveStaffId", null);
    }

    public static String getActiveIndustryId() {
        return G.sharedPref.getString("ActiveIndustry", null);
    }

    public static String getImage() {
        return G.sharedPref.getString("image", "image");
    }

    public static String getToken() {
        return G.sharedPref.getString("token", null);
    }

    public static String getFirstName() {
        return G.sharedPref.getString("firstName", null);
    }
    public static String getEmail() {
        return G.sharedPref.getString("email", null);
    }

    public static int getGender() {
        return G.sharedPref.getInt("gender", 1);
    }

    public static int getIdLanguage() {
        return G.sharedPref.getInt("idLanguage", 1);
    }

    public static void setFirstName(String firstName) {
        G.sharedPref.edit().putString("firstName", firstName).apply();
    }

    public static void setLastName(String lastName) {
        G.sharedPref.edit().putString("lastName", lastName).apply();
    }

    public static void setImage(String image) {
        G.sharedPref.edit().putString("image", image).apply();
    }


    public static void setEmail(String email) {
        G.sharedPref.edit().putString("email", email).apply();
    }

    public static void setPhone(String phone) {
        G.sharedPref.edit().putString("phone", phone).apply();
    }

    public static void setGender(int gender) {
        G.sharedPref.edit().putInt("gender", gender).apply();
    }

    public static void setDeviceId(String deviceId) {
        G.sharedPref.edit().putString("deviceId", deviceId).apply();
    }

    public static String getDeviceId() {
        String idDevice = G.sharedPref.getString("deviceId", null);
        if (idDevice == null) {
            setDeviceId(UUID.randomUUID().toString());
        }
        return G.sharedPref.getString("deviceId", UUID.randomUUID().toString());
    }

    public static void setLanguage(String lan) {
        G.sharedPref.edit().putString("language", lan).apply();
    }

    public static String getLanguage() {
        return G.sharedPref.getString("language","ar");
    }
    public static void logOut() {
        G.sharedPref.edit().clear().apply();
    }
}
