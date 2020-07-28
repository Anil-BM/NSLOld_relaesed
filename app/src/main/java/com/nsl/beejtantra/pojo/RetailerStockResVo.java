package com.nsl.beejtantra.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * Created by SupraSoft on 3/18/2019.
 */

public class RetailerStockResVo {
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("msg")
    @Expose
    public String msg;
    @SerializedName("inserted_retailer")
    @Expose
    public List<InsertedRetailer> insertedRetailer = null;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("status", status).append("msg", msg).append("insertedRetailer", insertedRetailer).toString();
    }

}
