package com.nsl.beejtantra.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class ProductPricingSurvey {
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
    @SerializedName("division_name")
    @Expose
    public String divisionName;
    @SerializedName("crop_id")
    @Expose
    public Integer cropId;
    @SerializedName("crop_name")
    @Expose
    public String cropName;
    @SerializedName("competitor_company_name")
    @Expose
    public String competitorCompanyName;
    @SerializedName("competitor_product_name")
    @Expose
    public String competitorProductName;
    @SerializedName("segment")
    @Expose
    public String segment;
    @SerializedName("sale_quantity")
    @Expose
    public String saleQuantity;
    @SerializedName("pack_size")
    @Expose
    public String packSize;
    @SerializedName("distributor_net_landing_price")
    @Expose
    public String distributorNetLandingPrice;
    @SerializedName("company_billing_price")
    @Expose
    public String companyBillingPrice;
    @SerializedName("distributor_billing_price_to_retailer")
    @Expose
    public String distributorBillingPriceToRetailer;
    @SerializedName("farmer_price")
    @Expose
    public String farmerPrice;
    @SerializedName("mrp")
    @Expose
    public String mrp;
    @SerializedName("last_year_sale_in_village")
    @Expose
    public String lastYearSaleInVillage;
    @SerializedName("current_year_sale_in_village")
    @Expose
    public String currentYearSaleInVillage;
    @SerializedName("next_year_estimated_sale_in_village")
    @Expose
    public String nextYearEstimatedSaleInVillage;
    @SerializedName("ffm_id")
    @Expose
    public String ffmId;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("userId", userId).append("district", district).append("taluka", taluka).append("village", village).append("divisionId", divisionId).append("divisionName", divisionName).append("cropId", cropId).append("cropName", cropName).append("competitorCompanyName", competitorCompanyName).append("competitorProductName", competitorProductName).append("segment", segment).append("saleQuantity", saleQuantity).append("packSize", packSize).append("distributorNetLandingPrice", distributorNetLandingPrice).append("companyBillingPrice", companyBillingPrice).append("distributorBillingPriceToRetailer", distributorBillingPriceToRetailer).append("farmerPrice", farmerPrice).append("mrp", mrp).append("lastYearSaleInVillage", lastYearSaleInVillage).append("currentYearSaleInVillage", currentYearSaleInVillage).append("nextYearEstimatedSaleInVillage", nextYearEstimatedSaleInVillage).toString();
    }
}
