package com.nsl.beejtantra.FarmerCoupans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.nsl.beejtantra.TFA.Demandgeneation;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

public class FarmerCouponsreqVo {
    @SerializedName("farmer_coupon_data")
    @Expose
    public List<Getcoupans> farmer_coupon_data = null;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("farmer_coupon_data", farmer_coupon_data).toString();
    }
}
