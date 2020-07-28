package com.nsl.beejtantra.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by SupraSoft on 2/19/2019.
 */

public class FormResponse {
    @SerializedName("ffm_id")
    @Expose
    public String ffmId;
    @SerializedName("sqlite_id")
    @Expose
    public Integer sqliteId;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("ffmId", ffmId).append("sqliteId", sqliteId).toString();
    }
}
