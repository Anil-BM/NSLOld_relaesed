package com.nsl.beejtantra.TFA.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public  class tfalist_get {
    @SerializedName("Status")
    @Expose
    public String status;
    @SerializedName("tfa_list_data")
    @Expose
    public List<TfaListData> tfaListData = null;
    @SerializedName("tfa_vill_data")
    @Expose
    public List<List<Object>> tfaVillData = null;
    @SerializedName("tfa_approval_data")
    @Expose
    public List<List<Object>> tfaApprovalData = null;
    @SerializedName("tfa_approval_titles")
    @Expose
    public List<TfaApprovalTitle> tfaApprovalTitles = null;

    public String getStatus() {
        return status;
    }

    public List<TfaListData> getTfaListData() {
        return tfaListData;
    }

    public List<List<Object>> getTfaVillData() {
        return tfaVillData;
    }

    public List<List<Object>> getTfaApprovalData() {
        return tfaApprovalData;
    }

    public List<TfaApprovalTitle> getTfaApprovalTitles() {
        return tfaApprovalTitles;
    }
}
