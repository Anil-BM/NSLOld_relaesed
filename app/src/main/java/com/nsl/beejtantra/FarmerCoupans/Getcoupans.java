package com.nsl.beejtantra.FarmerCoupans;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;
import com.squareup.okhttp.RequestBody;

import org.apache.commons.lang3.builder.ToStringBuilder;

public class Getcoupans {



    @SerializedName("farmer_mobile")
    @Expose
    public String farmer_mobile;

    @SerializedName("farmer_adhar_no")
    @Expose
    public String farmer_adhar_no;


    @SerializedName("location")
    @Expose
    public String location;

    @SerializedName("address")
    @Expose
    public String address;

    @SerializedName("farmer_coupon_id")
    @Expose
    public String farmer_coupon_id;

    @SerializedName("user_id")
    @Expose
    public String user_id;


    @SerializedName("farmer_name")
    @Expose
    public String farmer_name;


    @SerializedName("village")
    @Expose
    public String village;
    @SerializedName("thaluka")
    @Expose
    public String thaluka;

    @Override
    public String toString() {









        return new ToStringBuilder(this).
                append("farmer_coupon_id", farmer_coupon_id).
                append("user_id", user_id).
                append("farmer_name", farmer_name).
                append("farmer_mobile", farmer_mobile).
                append("farmer_adhar_no", farmer_adhar_no).
                append("location", location).
                append("address", address).
                append("village", village).
                append("thaluka", thaluka).toString();
    }
}
