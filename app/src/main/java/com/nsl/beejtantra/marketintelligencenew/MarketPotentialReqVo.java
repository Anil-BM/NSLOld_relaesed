package com.nsl.beejtantra.marketintelligencenew;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * Created by SupraSoft on 2/18/2019.
 */

public class MarketPotentialReqVo {
    @SerializedName("market_potential")
    @Expose
    public List<MarketPotential> marketPotential = null;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("marketPotential", marketPotential).toString();
    }

}
