package com.nsl.beejtantra.marketintelligencenew;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by SupraSoft on 4/5/2019.
 */

public class CompetitorChannel {
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
    @SerializedName("territory")
    @Expose
    public String territory;
    @SerializedName("total_no_of_villages")
    @Expose
    public String totalNoOfVillages;
    @SerializedName("total_no_of_distributors")
    @Expose
    public String totalNoOfDistributors;
    @SerializedName("total_no_of_retailers")
    @Expose
    public String totalNoOfRetailers;
    @SerializedName("no_of_nsl_villages")
    @Expose
    public String noOfNslVillages;
    @SerializedName("no_of_nsl_distributors")
    @Expose
    public String noOfNslDistributors;
    @SerializedName("no_of_nsl_retailers")
    @Expose
    public String noOfNslRetailers;
    @SerializedName("competitor_company_name_1")
    @Expose
    public String competitorCompanyName1;
    @SerializedName("no_of_distributors_1")
    @Expose
    public String noOfDistributors1;
    @SerializedName("no_of_retailers_1")
    @Expose
    public String noOfRetailers1;
    @SerializedName("market_penetration_1")
    @Expose
    public String marketPenetration1;
    @SerializedName("competitor_company_name_2")
    @Expose
    public String competitorCompanyName2;
    @SerializedName("no_of_distributors_2")
    @Expose
    public String noOfDistributors2;
    @SerializedName("no_of_retailers_2")
    @Expose
    public String noOfRetailers2;
    @SerializedName("market_penetration_2")
    @Expose
    public String marketPenetration2;
    @SerializedName("competitor_company_name_3")
    @Expose
    public String competitorCompanyName3;
    @SerializedName("no_of_distributors_3")
    @Expose
    public String noOfDistributors3;
    @SerializedName("no_of_retailers_3")
    @Expose
    public String noOfRetailers3;
    @SerializedName("market_penetration_3")
    @Expose
    public String marketPenetration3;
    @SerializedName("competitor_company_name_4")
    @Expose
    public String competitorCompanyName4;
    @SerializedName("no_of_distributors_4")
    @Expose
    public String noOfDistributors4;
    @SerializedName("no_of_retailers_4")
    @Expose
    public String noOfRetailers4;
    @SerializedName("market_penetration_4")
    @Expose
    public String marketPenetration4;
    @SerializedName("competitor_company_name_5")
    @Expose
    public String competitorCompanyName5;
    @SerializedName("no_of_distributors_5")
    @Expose
    public String noOfDistributors5;
    @SerializedName("no_of_retailers_5")
    @Expose
    public String noOfRetailers5;
    @SerializedName("market_penetration_5")
    @Expose
    public String marketPenetration5;
    @SerializedName("ffm_id")
    @Expose
    public String ffmId;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("regionId",regionId).append("ffmId",ffmId).append("id", id).append("userId", userId).append("district", district).append("territory", territory).append("totalNoOfVillages", totalNoOfVillages).append("totalNoOfDistributors", totalNoOfDistributors).append("totalNoOfRetailers", totalNoOfRetailers).append("noOfNslVillages", noOfNslVillages).append("noOfNslDistributors", noOfNslDistributors).append("noOfNslRetailers", noOfNslRetailers).append("competitorCompanyName1", competitorCompanyName1).append("noOfDistributors1", noOfDistributors1).append("noOfRetailers1", noOfRetailers1).append("marketPenetration1", marketPenetration1).append("competitorCompanyName2", competitorCompanyName2).append("noOfDistributors2", noOfDistributors2).append("noOfRetailers2", noOfRetailers2).append("marketPenetration2", marketPenetration2).append("competitorCompanyName3", competitorCompanyName3).append("noOfDistributors3", noOfDistributors3).append("noOfRetailers3", noOfRetailers3).append("marketPenetration3", marketPenetration3).append("competitorCompanyName4", competitorCompanyName4).append("noOfDistributors4", noOfDistributors4).append("noOfRetailers4", noOfRetailers4).append("marketPenetration4", marketPenetration4).append("competitorCompanyName5", competitorCompanyName5).append("noOfDistributors5", noOfDistributors5).append("noOfRetailers5", noOfRetailers5).append("marketPenetration5", marketPenetration5).toString();
    }
}
