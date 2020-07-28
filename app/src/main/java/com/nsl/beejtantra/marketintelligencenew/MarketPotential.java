package com.nsl.beejtantra.marketintelligencenew;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by SupraSoft on 2/18/2019.
 */

public class MarketPotential {
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("user_id")
    @Expose
    public Integer userId;
    @SerializedName("region_id")
    @Expose
    public String regionId;
    @SerializedName("district")
    @Expose
    public String district;
    @SerializedName("taluka")
    @Expose
    public String taluka;
    @SerializedName("village")
    @Expose
    public String village;
    @SerializedName("division_id")
    @Expose
    public Integer divisionId;
    @SerializedName("crop_id")
    @Expose
    public Integer cropId;
    @SerializedName("kharif_crop_acreage")
    @Expose
    public String kharifCropAcreage;
    @SerializedName("rabi_crop_acreage")
    @Expose
    public String rabiCropAcreage;
    @SerializedName("summer_crop_acreage")
    @Expose
    public String summerCropAcreage;
    @SerializedName("total_potential_acreage")
    @Expose
    public String totalPotentialAcreage;
    @SerializedName("seed_usage_quanity")
    @Expose
    public String seedUsageQuanity;
    @SerializedName("total_market_potential_volume")
    @Expose
    public String totalMarketPotentialVolume;
    @SerializedName("nsl_sale")
    @Expose
    public String nslSale;
    @SerializedName("top_company_name_1")
    @Expose
    public String topCompanyName1;
    @SerializedName("company_1_qty")
    @Expose
    public String company1Qty;
    @SerializedName("top_company_name_2")
    @Expose
    public String topCompanyName2;
    @SerializedName("company_2_qty")
    @Expose
    public String company2Qty;
    @SerializedName("top_company_name_3")
    @Expose
    public String topCompanyName3;
    @SerializedName("company_3_qty")
    @Expose
    public String company3Qty;
    @SerializedName("top_company_name_4")
    @Expose
    public String topCompanyName4;
    @SerializedName("company_4_qty")
    @Expose
    public String company4Qty;
    @SerializedName("top_company_name_5")
    @Expose
    public String topCompanyName5;
    @SerializedName("company_5_qty")
    @Expose
    public String company5Qty;
    @SerializedName("ffm_id")
    @Expose
    public String ffmId;
    @SerializedName("crop_name")
    @Expose
    public String cropName;
    @SerializedName("division_name")
    @Expose
    public String divisionName;


    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("regionId",regionId).append("cropName", cropName).append("divisionName", divisionName).append("userId", userId).append("district", district).append("taluka", taluka).append("village", village).append("divisionId", divisionId).append("cropId", cropId).append("kharifCropAcreage", kharifCropAcreage).append("rabiCropAcreage", rabiCropAcreage).append("summerCropAcreage", summerCropAcreage).append("totalPotentialAcreage", totalPotentialAcreage).append("seedUsageQuanity", seedUsageQuanity).append("totalMarketPotentialVolume", totalMarketPotentialVolume).append("nslSale", nslSale).append("topCompanyName1", topCompanyName1).append("company1Qty", company1Qty).append("topCompanyName2", topCompanyName2).append("company2Qty", company2Qty).append("topCompanyName3", topCompanyName3).append("company3Qty", company3Qty).append("topCompanyName4", topCompanyName4).append("company4Qty", company4Qty).append("topCompanyName5", topCompanyName5).append("company5Qty", company5Qty).toString();
    }


}
