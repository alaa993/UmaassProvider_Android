package net.umaass_providers.app.data.remote.models;

import com.google.gson.annotations.SerializedName;

import net.umaass_providers.app.R;
import net.umaass_providers.app.interfac.ListItem;
import net.umaass_providers.app.utils.Utils;


public class Suggestion implements ListItem {

    @SerializedName("start")
    private String start;

    @SerializedName("end")
    private String end;

    public void setStart(String start) {
        this.start = start;
    }

    public String getStart() {
        return start;
    }

    public void setEnd(String end) {
        this.end = end;
    }

    public String getEnd() {
        return end;
    }

    @Override
    public String getItemName() {
        return "\n" + Utils.getString(R.string.start) + " : " + start + "\n" + Utils.getString(R.string.end) + " : " + end + "\n";
    }

    @Override
    public String getItemId() {
        return end;
    }
}