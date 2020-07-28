package com.nsl.beejtantra.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by SupraSoft on 3/18/2019.
 */

public class InsertedRetailer {
    @SerializedName("ffm_id")
    @Expose
    public Integer ffmId;
    @SerializedName("mobile_id")
    @Expose
    public Integer mobileId;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("ffmId", ffmId).append("mobileId", mobileId).toString();
    }
}
