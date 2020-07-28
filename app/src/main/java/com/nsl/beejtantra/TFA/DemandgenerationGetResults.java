package com.nsl.beejtantra.TFA;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class DemandgenerationGetResults {

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("msg")
    @Expose
    public String msg;
    @SerializedName("get_response")
    @Expose
    public List<DemandgenerationGetList> getResponse = null;



    public class DemandgenerationGetList {
        @SerializedName("dg_id")
        @Expose
        public String dgId;
        @SerializedName("user_id")
        @Expose
        public String userId;
        @SerializedName("district_id")
        @Expose
        public String districtId;
        @SerializedName("division_id")
        @Expose
        public String divisionId;
        @SerializedName("crop_id")
        @Expose
        public String cropId;
        @SerializedName("activity_event")
        @Expose
        public String activityEvent;
        @SerializedName("no_of_farmers")
        @Expose
        public String noOfFarmers;
        @SerializedName("date_of_event")
        @Expose
        public String dateOfEvent;
        @SerializedName("address")
        @Expose
        public String address;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("created_datetime")
        @Expose
        public String createdDatetime;
        @SerializedName("updated_datetime")
        @Expose
        public String updatedDatetime;

    }
}

