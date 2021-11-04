package net.umaass_providers.app.models;

import net.umaass_providers.app.data.remote.models.WorkingHoursItem;

import java.util.ArrayList;
import java.util.List;

public class NewIndustry {

    public String category_id;
    public String title;
    public String address;
    public String city_id;
    public String phone;
    public double lat;
    public double lng;
    public String admin_password = "1234";
    public String coworker_password = "1234";
    public String assistant_password = "1234";
    public List<WorkingHoursItem> working_hours = new ArrayList<>();
    public String description;

}




