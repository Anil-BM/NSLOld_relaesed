package com.nsl.beejtantra.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * Created by SupraSoft on 3/7/2019.
 */

public class Season {

    public int primaryId;
    @SerializedName("season_master_id")
    @Expose
    public String seasonMasterId;
    @SerializedName("season_name")
    @Expose
    public String seasonName;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("line_items")
    @Expose
    public List<SeasonLineItem> lineItems = null;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("primaryId",primaryId).append("seasonMasterId", seasonMasterId).append("seasonName", seasonName).append("status", status).append("lineItems", lineItems).toString();
    }
}
