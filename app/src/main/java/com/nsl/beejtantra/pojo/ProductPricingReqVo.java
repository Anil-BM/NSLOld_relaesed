package com.nsl.beejtantra.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

public class ProductPricingReqVo {
    @SerializedName("product_price_survey")
    @Expose
    public List<ProductPricingSurvey> productPriceSurvey = null;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("productPriceSurvey", productPriceSurvey).toString();
    }
}
