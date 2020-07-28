package com.nsl.beejtantra.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * Created by SupraSoft on 3/15/2019.
 */

public class StockDispatchResVo {
    @SerializedName("stock_dispatch")
    @Expose
    public List<StockDispatch> stockDispatch = null;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("stockDispatch", stockDispatch).toString();
    }
}
