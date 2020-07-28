package com.nsl.beejtantra.pojo;

/**
 * Created by SupraSoft on 2/1/2019.
 */

public class ServiceOrderHistory {

    public int primaryId;
    public String orderHistoryId;
    public String orderId;
    public String orderDetailsId;
    public String orderApprovalId;
    public String cropId;
    public String schemeId;
    public String slabId;
    public String productId;
    public String orderQuantity;
    public String quantity;
    public String orderPrice;
    public String advanceAmount;
    public String status;
    public String createdBy;
    public String cDateTime;
    public String modifiedBy;

    public ServiceOrderHistory() {

    }

    public ServiceOrderHistory(String orderHistoryId, String orderId, String orderDetailsId, String cropId, String schemeId, String slabId
            , String productId, String orderQuantity, String quantity, String orderPrice, String advanceAmount, String status, String createdBy, String cDateTime, String modifiedBy,String orderApprovalId) {
        this.orderHistoryId = orderHistoryId;
        this.orderId = orderId;
        this.orderDetailsId = orderDetailsId;
        this.cropId = cropId;
        this.schemeId = schemeId;
        this.slabId = slabId;
        this.productId = productId;
        this.quantity = quantity;
        this.orderPrice = orderPrice;
        this.advanceAmount = advanceAmount;
        this.status = status;
        this.createdBy = createdBy;
        this.cDateTime = cDateTime;
        this.orderQuantity = orderQuantity;
        this.modifiedBy = modifiedBy;
        this.orderApprovalId = orderApprovalId;
    }

}
