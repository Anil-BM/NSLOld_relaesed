package com.nsl.beejtantra.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * Created by SupraSoft on 2/19/2019.
 */

public class FormResponseResVo {
    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("msg")
    @Expose
    public String msg;
    @SerializedName("mobile_id")
    @Expose
    public int mobileId;
    @SerializedName("form_response")
    @Expose
    public List<FormResponse> formResponse = null;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("mobileId", mobileId).append("status", status).append("msg", msg).append("formResponse", formResponse).toString();
    }
}
