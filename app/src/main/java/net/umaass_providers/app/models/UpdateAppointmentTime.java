package net.umaass_providers.app.models;

import com.google.gson.annotations.SerializedName;

public class UpdateAppointmentTime {

    @SerializedName("status")
    private String status;
    @SerializedName("appointment_id")
    public String appointmentId;

    @SerializedName("prescription")
    public String prescription;

    @SerializedName("new_date_time")
    NewDateTime dateTime;

    public NewDateTime getDateTime() {
        return dateTime;
    }

    public void setDateTime(NewDateTime dateTime) {
        this.dateTime = dateTime;
    }

    public String getAppointmentId() {
        return appointmentId;
    }

    public void setAppointmentId(String appointmentId) {
        this.appointmentId = appointmentId;
    }

    public String getPrescription() {
        return prescription;
    }

    public void setPrescription(String prescription) {
        this.prescription = prescription;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}