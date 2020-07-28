package com.nsl.beejtantra.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * Created by SupraSoft on 3/8/2019.
 */

public class Godown {
    public int primaryId;
    @SerializedName("godown_id")
    @Expose
    public String godownId;
    @SerializedName("godown_name")
    @Expose
    public String godownName;
    @SerializedName("godown_code")
    @Expose
    public String godownCode;
    @SerializedName("company_name")
    @Expose
    public String companyName;
    @SerializedName("region_name")
    @Expose
    public String regionName;
    @SerializedName("godown_stock")
    @Expose
    public List<GodownStock> godownStock = null;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("primaryId",primaryId).append("godownId", godownId).append("godownName", godownName).append("godownCode", godownCode).append("companyName", companyName).append("regionName", regionName).append("godownStock", godownStock).toString();
    }

}
