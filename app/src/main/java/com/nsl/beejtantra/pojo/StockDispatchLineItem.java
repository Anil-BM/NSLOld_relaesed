package com.nsl.beejtantra.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by SupraSoft on 3/15/2019.
 */

public class StockDispatchLineItem {
    public int primaryId;
    @SerializedName("stock_dispatch_line_item_id")
    @Expose
    public String stockDispatchLineItemId;
    @SerializedName("stock_dispatch_id")
    @Expose
    public String stockDispatchId;
    @SerializedName("crop_id")
    @Expose
    public String cropId;
    @SerializedName("product_id")
    @Expose
    public String productId;
    @SerializedName("quantity")
    @Expose
    public String quantity;
    @SerializedName("dispatch_date")
    @Expose
    public String dispatchDate;
    @SerializedName("price")
    @Expose
    public String price;
    @SerializedName("order_sap_id")
    @Expose
    public String orderSapId;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("primaryId",primaryId).append("stockDispatchLineItemId", stockDispatchLineItemId).append("stockDispatchId", stockDispatchId).append("cropId", cropId).append("productId", productId).append("quantity", quantity).append("dispatchDate", dispatchDate).append("price", price).append("orderSapId",orderSapId).toString();
    }
}
