package com.nsl.beejtantra.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by SupraSoft on 2/11/2019.
 */

public class ComplaintReq {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("company_id")
    @Expose
    public String companyId;
    @SerializedName("division_id")
    @Expose
    public String divisionId;
    @SerializedName("crop_id")
    @Expose
    public String cropId;
    @SerializedName("product_id")
    @Expose
    public String productId;
    @SerializedName("marketing_lot_number")
    @Expose
    public String marketingLotNumber;
    @SerializedName("others")
    @Expose
    public String others;
    @SerializedName("farmer_name")
    @Expose
    public String farmerName;
    @SerializedName("contact_no")
    @Expose
    public String contactNo;
    @SerializedName("complaint_area_acres")
    @Expose
    public String complaintAreaAcres;
    @SerializedName("soil_type")
    @Expose
    public String soilType;
    @SerializedName("purchased_quantity")
    @Expose
    public String purchasedQuantity;
    @SerializedName("complaint_quantity")
    @Expose
    public String complaintQuantity;
    @SerializedName("purchase_date")
    @Expose
    public String purchaseDate;
    @SerializedName("bill_number")
    @Expose
    public String billNumber;
    @SerializedName("retailer_name")
    @Expose
    public String retailerName;
    @SerializedName("distributor")
    @Expose
    public String distributor;
    @SerializedName("mandal")
    @Expose
    public String mandal;
    @SerializedName("village")
    @Expose
    public String village;
    @SerializedName("comments")
    @Expose
    public String comments;
    @SerializedName("user_id")
    @Expose
    public String userId;
    @SerializedName("complaint_type")
    @Expose
    public String complaintType;
    @SerializedName("complaint_percentage")
    @Expose
    public String complaintPercentage;
    @SerializedName("complaint_remarks")
    @Expose
    public String complaintRemarks;
    @SerializedName("zone")
    @Expose
    public String zone;
    @SerializedName("region")
    @Expose
    public String region;
    @SerializedName("dealer")
    @Expose
    public String dealer;
    @SerializedName("performance_on_other_fields")
    @Expose
    public String performanceType;
    @SerializedName("state")
    @Expose
    public String state;
    @SerializedName("date_of_sowing")
    @Expose
    public String dateOfSowing;
    @SerializedName("date_of_complaint")
    @Expose
    public String dateOfComplaint;
    @SerializedName("inspected_date")
    @Expose
    public String inspectedDate;
    @SerializedName("retailer_id")
    @Expose
    public String retailerId;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("companyId", companyId).append("divisionId", divisionId).append("complaintType", complaintType).append("cropId", cropId).append("productId", productId).append("marketingLotNumber", marketingLotNumber).append("others", others).append("farmerName", farmerName).append("contactNo", contactNo).append("complaintAreaAcres", complaintAreaAcres).append("soilType", soilType).append("purchasedQuantity", purchasedQuantity).append("complaintQuantity", complaintQuantity).append("purchaseDate", purchaseDate).append("billNumber", billNumber).append("retailerName", retailerName).append("distributor", distributor).append("mandal", mandal).append("village", village).append("comments", comments).append("userId", userId).append("retailerId",retailerId).toString();
    }
}
