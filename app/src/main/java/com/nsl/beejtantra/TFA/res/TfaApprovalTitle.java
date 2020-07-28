package com.nsl.beejtantra.TFA.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class TfaApprovalTitle {
    @SerializedName("tfa_title_id")
    @Expose
    public String tfaTitleId;
    @SerializedName("approval_title")
    @Expose
    public String approvalTitle;
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("created_datetime")
    @Expose
    public String createdDatetime;
    @SerializedName("updated_datetime")
    @Expose
    public String updatedDatetime;

}
