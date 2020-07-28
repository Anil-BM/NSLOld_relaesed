package com.nsl.beejtantra.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class ChannelPreference {

    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("user_id")
    @Expose
    public Integer userId;
    @SerializedName("distributor_id")
    @Expose
    public String distributorId;
    @SerializedName("crop_id")
    @Expose
    public String cropId;
    @SerializedName("crop_name")
    @Expose
    public String cropName;
    @SerializedName("company_name_1")
    @Expose
    public String companyName1;
    @SerializedName("company_1_turnover")
    @Expose
    public String company1Turnover;
    @SerializedName("company_name_2")
    @Expose
    public String companyName2;
    @SerializedName("company_2_turnover")
    @Expose
    public String company2Turnover;
    @SerializedName("company_name_3")
    @Expose
    public String companyName3;
    @SerializedName("company_3_turnover")
    @Expose
    public String company3Turnover;
    @SerializedName("company_name_4")
    @Expose
    public String companyName4;
    @SerializedName("company_4_turnover")
    @Expose
    public String company4Turnover;
    @SerializedName("company_name_5")
    @Expose
    public String companyName5;
    @SerializedName("company_5_turnover")
    @Expose
    public String company5Turnover;
    @SerializedName("company_name_6")
    @Expose
    public String companyName6;
    @SerializedName("company_6_turnover")
    @Expose
    public String company6Turnover;
    @SerializedName("company_name_7")
    @Expose
    public String companyName7;
    @SerializedName("company_7_turnover")
    @Expose
    public String company7Turnover;
    @SerializedName("company_name_8")
    @Expose
    public String companyName8;
    @SerializedName("company_8_turnover")
    @Expose
    public String company8Turnover;
    @SerializedName("company_name_9")
    @Expose
    public String companyName9;
    @SerializedName("company_9_turnover")
    @Expose
    public String company9Turnover;
    @SerializedName("company_name_10")
    @Expose
    public String companyName10;
    @SerializedName("company_10_turnover")
    @Expose
    public String company10Turnover;
    public String ffmId;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("userId", userId).append("distributorId", distributorId).append("cropId", cropId).append("cropName", cropName).append("companyName1", companyName1).append("company1Turnover", company1Turnover).append("companyName2", companyName2).append("company2Turnover", company2Turnover).append("companyName3", companyName3).append("company3Turnover", company3Turnover).append("companyName4", companyName4).append("company4Turnover", company4Turnover).append("companyName5", companyName5).append("company5Turnover", company5Turnover).append("companyName6", companyName6).append("company6Turnover", company6Turnover).append("companyName7", companyName7).append("company7Turnover", company7Turnover).append("companyName8", companyName8).append("company8Turnover", company8Turnover).append("companyName9", companyName9).append("company9Turnover", company9Turnover).append("companyName10", companyName10).append("company10Turnover", company10Turnover).toString();
    }
}
