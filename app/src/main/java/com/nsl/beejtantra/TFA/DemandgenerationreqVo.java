package com.nsl.beejtantra.TFA;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nsl.beejtantra.pojo.CropShift;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

public class DemandgenerationreqVo {
    @SerializedName("dg_monitoring_data")
    @Expose
    public List<Demandgeneation> dg_monitoring_data = null;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("dg_monitoring_data", dg_monitoring_data).toString();
    }
}
