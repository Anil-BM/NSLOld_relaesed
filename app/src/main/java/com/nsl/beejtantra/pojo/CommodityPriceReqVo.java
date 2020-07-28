package com.nsl.beejtantra.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

public class CommodityPriceReqVo {
    @SerializedName("commodityPrice")
    @Expose
    public List<CommodityPrice> commodityPrice = null;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("commodityPrice", commodityPrice).toString();
    }
}
