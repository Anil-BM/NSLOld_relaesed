package com.nsl.beejtantra.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

public class CompetitorStrengthReqVo {
    @SerializedName("competitorStrength")
    @Expose
    public List<CompetitorStrength> competitorStrength = null;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("competitorStrength", competitorStrength).toString();
    }

}
