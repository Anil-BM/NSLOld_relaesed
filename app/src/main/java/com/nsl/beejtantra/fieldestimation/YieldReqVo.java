package com.nsl.beejtantra.fieldestimation;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by SupraSoft on 3/7/2019.
 */

public class YieldReqVo {
    @SerializedName("mobile_id")
    @Expose
    public int primaryId;
    @SerializedName("division_id")
    @Expose
    public String divisionId;
    @SerializedName("crop_id")
    @Expose
    public String cropId;
    @SerializedName("product_id")
    @Expose
    public String productId;
    @SerializedName("avg_no_balls_plant")
    @Expose
    public String avgNoOfBallsPlant;
    @SerializedName("avg_ball_weight_in_gm")
    @Expose
    public String avgBallWeight;
    @SerializedName("row_to_row_distance_sqm")
    @Expose
    public String rowToRowDistance;
    @SerializedName("plant_to_plant_distance_sqm")
    @Expose
    public String plantToPlantDistance;
    @SerializedName("area_in_acres")
    @Expose
    public String area;
    @SerializedName("yeild_in_sqm")
    @Expose
    public String yield;
    @SerializedName("result")
    @Expose
    public String result;
    @SerializedName("created_by")
    @Expose
    public String createdBy;

    @Override
    public String toString() {
        return ToStringBuilder.reflectionToString(this);
    }
}
