package com.nsl.beejtantra.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class CommodityPrice {
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("user_id")
    @Expose
    public Integer userId;
    @SerializedName("region_id")
    @Expose
    public Integer regionId;
    @SerializedName("district")
    @Expose
    public Integer district;
    @SerializedName("taluka")
    @Expose
    public String taluka;
    @SerializedName("village")
    @Expose
    public String village;
    @SerializedName("division_id")
    @Expose
    public String divisionId;
    @SerializedName("division_name")
    @Expose
    public String divisionName;
    @SerializedName("crop_id")
    @Expose
    public String cropId;
    @SerializedName("crop_name")
    @Expose
    public String cropName;
    @SerializedName("segment")
    @Expose
    public String segment;
    @SerializedName("apmc_mandi_price")
    @Expose
    public String apmcMandiPrice;
    @SerializedName("commodity_dealer_commission_agent_price")
    @Expose
    public String commodityDealerCommissionAgentPrice;
    @SerializedName("purchase_price_by_industry")
    @Expose
    public String purchasePriceByIndustry;
    @SerializedName("created_datetime")
    @Expose
    public String createdDatetime;
    @SerializedName("updated_datetime")
    @Expose
    public String updatedDatetime;
    public String ffmId;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("userId", userId).append("regionId", regionId).append("district", district).append("taluka", taluka).append("village", village).append("divisionId", divisionId).append("divisionName", divisionName).append("cropId", cropId).append("cropName", cropName).append("segment", segment).append("apmcMandiPrice", apmcMandiPrice).append("commodityDealerCommissionAgentPrice", commodityDealerCommissionAgentPrice).append("purchasePriceByIndustry", purchasePriceByIndustry).append("createdDatetime", createdDatetime).append("updatedDatetime", updatedDatetime).toString();
    }
}
