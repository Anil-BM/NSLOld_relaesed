package com.nsl.beejtantra.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by SupraSoft on 3/15/2019.
 */

public class StockDispatchResp {
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("result")
    @Expose
    public StockDispatchResVo result;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("status", status).append("result", result).toString();
    }

}
