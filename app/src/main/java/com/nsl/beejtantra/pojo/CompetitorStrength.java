package com.nsl.beejtantra.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class CompetitorStrength {
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("user_id")
    @Expose
    public Integer userId;
    @SerializedName("district")
    @Expose
    public String district;
    @SerializedName("territory")
    @Expose
    public String territory;
    @SerializedName("competitor_company_name")
    @Expose
    public String competitorCompanyName;
    @SerializedName("business_covering_villages")
    @Expose
    public String businessCoveringVillages;
    @SerializedName("no_of_products_sold")
    @Expose
    public String noOfProductsSold;
    @SerializedName("no_of_farmer_clubs")
    @Expose
    public String noOfFarmerClubs;
    @SerializedName("no_of_demo_plots")
    @Expose
    public String noOfDemoPlots;
    @SerializedName("no_of_temporary_fas_counter_boys")
    @Expose
    public String noOfTemporaryFasCounterBoys;
    @SerializedName("no_of_permanent_fa")
    @Expose
    public String noOfPermanentFa;
    @SerializedName("no_of_company_staff")
    @Expose
    public String noOfCompanyStaff;
    @SerializedName("no_of_fds_conducted")
    @Expose
    public String noOfFdsConducted;
    @SerializedName("no_of_mfds_conducted")
    @Expose
    public String noOfMfdsConducted;
    @SerializedName("no_of_new_product_minikit_trail_plots")
    @Expose
    public String noOfNewProductMinikitTrailPlots;
    @SerializedName("no_of_workshops_conducted")
    @Expose
    public String noOfWorkshopsConducted;

    public String ffmId;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("userId", userId).append("district", district).append("territory", territory).append("competitorCompanyName", competitorCompanyName).append("businessCoveringVillages", businessCoveringVillages).append("noOfProductsSold", noOfProductsSold).append("noOfFarmerClubs", noOfFarmerClubs).append("noOfDemoPlots", noOfDemoPlots).append("noOfTemporaryFasCounterBoys", noOfTemporaryFasCounterBoys).append("noOfPermanentFa", noOfPermanentFa).append("noOfCompanyStaff", noOfCompanyStaff).append("noOfFdsConducted", noOfFdsConducted).append("noOfMfdsConducted", noOfMfdsConducted).append("noOfNewProductMinikitTrailPlots", noOfNewProductMinikitTrailPlots).append("noOfWorkshopsConducted", noOfWorkshopsConducted).toString();
    }

}
