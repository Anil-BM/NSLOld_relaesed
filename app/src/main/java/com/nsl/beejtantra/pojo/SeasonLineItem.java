package com.nsl.beejtantra.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by SupraSoft on 3/8/2019.
 */

public class SeasonLineItem {
    public int primaryId;
    @SerializedName("season_line_item_id")
    @Expose
    public String seasonLineItemId;
    @SerializedName("season_master_id")
    @Expose
    public String seasonMasterId;
    @SerializedName("division_id")
    @Expose
    public String divisionId;
    @SerializedName("valid_from")
    @Expose
    public String validFrom;
    @SerializedName("valid_to")
    @Expose
    public String validTo;
    @SerializedName("created_by")
    @Expose
    public Object createdBy;
    @SerializedName("modified_by")
    @Expose
    public Object modifiedBy;
    @SerializedName("created_date_time")
    @Expose
    public String createdDateTime;
    @SerializedName("modified_date_time")
    @Expose
    public String modifiedDateTime;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("primaryId",primaryId).append("seasonLineItemId", seasonLineItemId).append("seasonMasterId", seasonMasterId).append("divisionId", divisionId).append("validFrom", validFrom).append("validTo", validTo).append("createdBy", createdBy).append("modifiedBy", modifiedBy).append("createdDateTime", createdDateTime).append("modifiedDateTime", modifiedDateTime).toString();
    }
}
