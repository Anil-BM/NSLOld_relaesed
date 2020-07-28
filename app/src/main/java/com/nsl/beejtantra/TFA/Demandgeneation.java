package com.nsl.beejtantra.TFA;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Demandgeneation {
    @SerializedName("dg_id")
    @Expose
    public String dg_id;
    @SerializedName("user_id")
    @Expose
    public String user_id;
    @SerializedName("district_id")
    @Expose
    public String district_id;
    @SerializedName("division_id")
    @Expose
    public String division_id;
    @SerializedName("crop_id")
    @Expose
    public String crop_id;
    @SerializedName("activity_event")
    @Expose
    public String activity_event;
    @SerializedName("no_of_farmers")
    @Expose
    public String no_of_farmers;
    @SerializedName("date_of_event")
    @Expose
    public String date_of_event;
    @SerializedName("address")
    @Expose
    public String address;


    @Override
    public String toString() {
        return new ToStringBuilder(this).append("user_id", user_id).append("district_id", district_id).append("division_id", division_id).append("crop_id", crop_id).append("activity_event", activity_event).append("no_of_farmers", no_of_farmers).append("date_of_event", date_of_event).append("address", address).append("dg_id", dg_id).toString();
    }
}
