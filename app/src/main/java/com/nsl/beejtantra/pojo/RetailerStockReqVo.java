package com.nsl.beejtantra.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * Created by SupraSoft on 3/18/2019.
 */

public class RetailerStockReqVo {
    @SerializedName("retailer_stock")
    @Expose
    public List<RetailerStockSupply> retailerStock = null;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("retailerStock", retailerStock).toString();
    }

}
