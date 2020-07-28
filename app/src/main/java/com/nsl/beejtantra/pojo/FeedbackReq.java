package com.nsl.beejtantra.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by SupraSoft on 2/11/2019.
 */

public class FeedbackReq {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("user_id")
    @Expose
    public String userId;
    @SerializedName("farmer_name")
    @Expose
    public String farmerName;
    @SerializedName("place")
    @Expose
    public String place;
    @SerializedName("contact_no")
    @Expose
    public String contactNo;
    @SerializedName("crop_id")
    @Expose
    public String cropId;
    @SerializedName("hybrid")
    @Expose
    public String hybrid;
    @SerializedName("sowing_date")
    @Expose
    public String sowingDate;
    @SerializedName("feedback_message")
    @Expose
    public String feedbackMessage;
    @SerializedName("created_date")
    @Expose
    public String createdDate;
    @SerializedName("transplanting_date")
    @Expose
    public String transplantingDate;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("userId", userId).append("farmerName", farmerName).append("place", place).append("contactNo", contactNo).append("cropId", cropId).append("hybrid", hybrid).append("sowingDate", sowingDate).append("feedbackMessage", feedbackMessage).append("createdDate", createdDate).append("transplantingDate", transplantingDate).toString();
    }

}
