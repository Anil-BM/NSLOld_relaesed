package com.nsl.beejtantra.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * Created by SupraSoft on 3/15/2019.
 */

public class StockDispatch {
    public int primaryId;
    @SerializedName("stock_dispatch_id")
    @Expose
    public String stockDispatchId;
    @SerializedName("user_id")
    @Expose
    public String userId;
    @SerializedName("company_id")
    @Expose
    public String companyId;
    @SerializedName("order_sap_id")
    @Expose
    public String orderSapId;
    @SerializedName("so_no")
    @Expose
    public String soNo;
    @SerializedName("order_id")
    @Expose
    public String orderId;
    @SerializedName("distributor_sap_id")
    @Expose
    public String distributorSapId;
    @SerializedName("distributor_id")
    @Expose
    public String distributorId;
    @SerializedName("division_id")
    @Expose
    public String divisionId;
    @SerializedName("dispatch_date")
    @Expose
    public String dispatchDate;
    @SerializedName("order_created_date")
    @Expose
    public String orderCreatedDate;
    @SerializedName("stock_dispatch_line_items")
    @Expose
    public List<StockDispatchLineItem> stockDispatchLineItems = null;
    @SerializedName("retailer_stock_supply")
    @Expose
    public List<RetailerStockSupply> retailerStockSupply = null;
    @SerializedName("fiscal_year")
    @Expose
    public String fiscalYear;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("primaryId", primaryId).append("stockDispatchId", stockDispatchId).append("userId", userId).append("companyId", companyId).append("orderSapId", orderSapId).append("soNo", soNo).append("orderId", orderId).append("distributorSapId", distributorSapId).append("distributorId", distributorId).append("divisionId", divisionId).append("dispatchDate", dispatchDate).append("orderCreatedDate", orderCreatedDate).append("stockDispatchLineItems", stockDispatchLineItems).append("retailerStockSupply", retailerStockSupply).append("fiscalYear", fiscalYear).toString();
    }

}
