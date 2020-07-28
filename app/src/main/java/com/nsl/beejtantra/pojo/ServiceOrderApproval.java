package com.nsl.beejtantra.pojo;

/**
 * Created by SupraSoft on 1/31/2019.
 */

public class ServiceOrderApproval {
    public int primaryId;
    public String orderApprovalId;
    public String orderId;
    public String userId;
    public String assignedUserId;
    public String orderStatus;
    public String pendingBy;
    public String createdBy;
    public String modifiedBy;
    public String cDateTime;
    public String uDateTime;
    public String syncStatus;

    public ServiceOrderApproval() {

    }

    public ServiceOrderApproval(String orderApprovalId, String orderId, String userId, String assignedUserId, String orderStatus, String createdBy, String modifiedBy, String cDateTime, String uDateTime, String syncStatus,String pendingBy) {
        this.orderApprovalId = orderApprovalId;
        this.orderId = orderId;
        this.userId = userId;
        this.assignedUserId = assignedUserId;
        this.orderStatus = orderStatus;
        this.createdBy = createdBy;
        this.modifiedBy = modifiedBy;
        this.cDateTime = cDateTime;
        this.uDateTime = uDateTime;
        this.syncStatus = syncStatus;
        this.pendingBy = pendingBy;
    }
}
