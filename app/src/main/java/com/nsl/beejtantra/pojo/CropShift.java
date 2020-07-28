package com.nsl.beejtantra.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class CropShift {
    @SerializedName("id")
    @Expose
    public Integer id;
    @SerializedName("user_id")
    @Expose
    public Integer userId;
    @SerializedName("district")
    @Expose
    public Integer district;
    @SerializedName("taluka")
    @Expose
    public String taluka;
    @SerializedName("village")
    @Expose
    public String village;
    @SerializedName("division_id")
    @Expose
    public String divisionId;
    @SerializedName("division_name")
    @Expose
    public String divisionName;
    @SerializedName("crop_id")
    @Expose
    public String cropId;
    @SerializedName("crop_name")
    @Expose
    public String cropName;
    @SerializedName("segment")
    @Expose
    public String segment;
    @SerializedName("previous_year_area_in_acres")
    @Expose
    public String previousYearAreaInAcres;
    @SerializedName("current_year_area_in_acres")
    @Expose
    public String currentYearAreaInAcres;
    @SerializedName("percentage_increase_or_decrease")
    @Expose
    public String percentageIncreaseOrDecrease;
    @SerializedName("reason_for_crop_shift")
    @Expose
    public String reasonForCropShift;
    @SerializedName("previous_year_srr")
    @Expose
    public String previousYearSrr;
    @SerializedName("current_year_srr")
    @Expose
    public String currentYearSrr;
    @SerializedName("next_year_estimated_srr")
    @Expose
    public String nextYearEstimatedSrr;
    @SerializedName("created_datetime")
    @Expose
    public String createdDatetime;
    @SerializedName("updated_datetime")
    @Expose
    public String updatedDatetime;
    public String ffmId;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("userId", userId).append("district", district).append("taluka", taluka).append("village", village).append("divisionId", divisionId).append("divisionName", divisionName).append("cropId", cropId).append("cropName", cropName).append("segment", segment).append("previousYearAreaInAcres", previousYearAreaInAcres).append("currentYearAreaInAcres", currentYearAreaInAcres).append("percentageIncreaseOrDecrease", percentageIncreaseOrDecrease).append("reasonForCropShift", reasonForCropShift).append("previousYearSrr", previousYearSrr).append("currentYearSrr", currentYearSrr).append("nextYearEstimatedSrr", nextYearEstimatedSrr).append("createdDatetime", createdDatetime).append("updatedDatetime", updatedDatetime).toString();
    }

}
