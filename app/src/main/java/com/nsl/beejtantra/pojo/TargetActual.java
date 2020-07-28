package com.nsl.beejtantra.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by SupraSoft on 2/26/2019.
 */

public class TargetActual {
    @SerializedName("product_name")
    @Expose
    public String productName;
    @SerializedName("target_quantity")
    @Expose
    public int targetQuantity;
    @SerializedName("actual_quantity")
    @Expose
    public int actualQuantity;
    @SerializedName("sale_percentage")
    @Expose
    public int salePercentage;
    @SerializedName("year")
    @Expose
    public int year;
    @SerializedName("month")
    @Expose
    public int month;
    @SerializedName("customer_target_id")
    @Expose
    public String customerTargetId;
    @SerializedName("customer_id")
    @Expose
    public String customerId;

    public int primaryId;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("customerId", customerId).append("customerTargetId", customerTargetId).append("primaryId", primaryId).append("month", month).append("year", year).append("productName", productName).append("targetQuantity", targetQuantity).append("actualQuantity", actualQuantity).append("salePercentage", salePercentage).toString();
    }
}
