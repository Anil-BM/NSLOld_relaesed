package com.nsl.beejtantra.RetroResponses;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class resmain_farmer_coupons_detail {

    @SerializedName("status")
    @Expose
    public String status;
    @SerializedName("msg")
    @Expose
    public String msg;
    @SerializedName("get_response")
    @Expose
    public List<GetCoupans1> getResponse = null;

  public   class GetCoupans1 {
        @SerializedName("farmer_coupon_id")
        @Expose
        public String farmerCouponId;

      public String getFarmerCouponId() {
          return farmerCouponId;
      }

      public String getUserId() {
          return userId;
      }

      public String getUniqueNo() {
          return uniqueNo;
      }

      public String getPoints() {
          return points;
      }

      public String getRegionId() {
          return regionId;
      }

      public String getCompanyId() {
          return companyId;
      }

      public String getCouponType() {
          return couponType;
      }

      public String getStatus() {
          return status;
      }

      public String getFarmerName() {
          return farmerName;
      }

      public String getFarmerEmail() {
          return farmerEmail;
      }

      public String getFarmerMobile() {
          return farmerMobile;
      }

      public String getFarmerAdharNo() {
          return farmerAdharNo;
      }

      public String getImage() {
          return image;
      }

      public String getCrop() {
          return crop;
      }

      public String getLocation() {
          return location;
      }

      public String getAddress() {
          return address;
      }

      public String getSyncStatus() {
          return syncStatus;
      }

      public String getCreatedDatetime() {
          return createdDatetime;
      }

      public String getUpdatedDatetime() {
          return updatedDatetime;
      }

      @SerializedName("user_id")
        @Expose
        public String userId;
        @SerializedName("unique_no")
        @Expose
        public String uniqueNo;
        @SerializedName("points")
        @Expose
        public String points;
        @SerializedName("region_id")
        @Expose
        public String regionId;
        @SerializedName("company_id")
        @Expose
        public String companyId;
        @SerializedName("coupon_type")
        @Expose
        public String couponType;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("farmer_name")
        @Expose
        public String farmerName;
        @SerializedName("farmer_email")
        @Expose
        public String farmerEmail;
        @SerializedName("farmer_mobile")
        @Expose
        public String farmerMobile;
        @SerializedName("farmer_adhar_no")
        @Expose
        public String farmerAdharNo;
        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("crop")
        @Expose
        public String crop;
        @SerializedName("location")
        @Expose
        public String location;
        @SerializedName("address")
        @Expose
        public String address;
        @SerializedName("sync_status")
        @Expose
        public String syncStatus;
        @SerializedName("created_datetime")
        @Expose
        public String createdDatetime;
        @SerializedName("updated_datetime")
        @Expose
        public String updatedDatetime;

      @SerializedName("village")
      @Expose
      public String village;

      public String getVillage() {
          return village;
      }

      public String getThaluka() {
          return thaluka;
      }

      @SerializedName("thaluka")
      @Expose
      public String thaluka;


    }

}
