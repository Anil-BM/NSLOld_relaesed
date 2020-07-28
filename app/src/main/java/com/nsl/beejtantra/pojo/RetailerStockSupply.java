package com.nsl.beejtantra.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by SupraSoft on 3/15/2019.
 */

public class RetailerStockSupply {
    @SerializedName("mobile_id")
    @Expose
    public int mobileId;
    @SerializedName("stock_supply_id")
    @Expose
    public String stockSupplyId;
    @SerializedName("user_id")
    @Expose
    public String userId;
    @SerializedName("distributer_id")
    @Expose
    public String distributerId;
    @SerializedName("retailer_id")
    @Expose
    public String retailerId;
    @SerializedName("crop_id")
    @Expose
    public String cropId;
    @SerializedName("product_id")
    @Expose
    public String productId;
    @SerializedName("supplied_date")
    @Expose
    public String suppliedDate;
    @SerializedName("quantity")
    @Expose
    public String quantity;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("user_type")
    @Expose
    public String userType;
    @SerializedName("created_by")
    @Expose
    public String createdBy;
    @SerializedName("created_date_time")
    @Expose
    public String createdDateTime;
    @SerializedName("modified_by")
    @Expose
    public String modifiedBy;
    @SerializedName("modified_date_time")
    @Expose
    public String modifiedDateTime;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("mobileId", mobileId).append("stockSupplyId", stockSupplyId).append("userId", userId).append("distributerId", distributerId).append("retailerId", retailerId).append("cropId", cropId).append("productId", productId).append("suppliedDate", suppliedDate).append("quantity", quantity).append("status", status).append("userType", userType).append("createdBy", createdBy).append("createdDateTime", createdDateTime).append("modifiedBy", modifiedBy).append("modifiedDateTime", modifiedDateTime).toString();
    }
}
