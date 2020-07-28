package com.nsl.beejtantra.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by SupraSoft on 3/8/2019.
 */

public class GodownStock {

    public int primaryId;
    @SerializedName("stock_godown_id")
    @Expose
    public String stockGodownId;
    @SerializedName("godown_id")
    @Expose
    public String godownId;
    @SerializedName("division_name")
    @Expose
    public String divisionName;
    @SerializedName("crop_name")
    @Expose
    public String cropName;
    @SerializedName("product_name")
    @Expose
    public String productName;
    @SerializedName("quantity")
    @Expose
    public String quantity;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("primaryId",primaryId).append("stockGodownId", stockGodownId).append("godownId", godownId).append("divisionName", divisionName).append("cropName", cropName).append("productName", productName).append("quantity", quantity).toString();
    }
}
