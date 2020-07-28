package com.nsl.beejtantra.marketintelligencenew;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * Created by SupraSoft on 4/5/2019.
 */

public class CompetitorChannelReqVo {
    @SerializedName("competitorChannel")
    @Expose
    public List<CompetitorChannel> competitorChannel = null;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("competitorChannel", competitorChannel).toString();
    }
}
