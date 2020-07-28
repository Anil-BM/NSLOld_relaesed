package com.nsl.beejtantra;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Coupans_add {
    String farmerCouponId;String userId;String uniqueNo;String points;String regionId;String companyId;String couponType;String s;
    String farmerName;String farmerEmail;String farmerMobile;String farmerAdharNo;String image;String crop;String location;
    String address;String syncStatus;String createdDatetime;String updatedDatetime,village,thaluka,ss;

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

    public String getS() {
        return s;
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

    public String getVillage() {
        return village;
    }

    public String getSs() {
        return ss;
    }

    public String getThaluka() {
        return thaluka;
    }

    public Coupans_add(String farmerCouponId, String userId, String uniqueNo, String points, String regionId, String companyId, String couponType, String s, String farmerName, String farmerEmail, String farmerMobile, String farmerAdharNo, String image, String crop, String location, String address, String syncStatus, String createdDatetime, String updatedDatetime, String village, String thaluka,String ss) {
        this.farmerCouponId=farmerCouponId;
        this.userId=userId;
        this.uniqueNo=uniqueNo;
        this.points=points;
        this.regionId=regionId;
        this.companyId=companyId;
        this.couponType=couponType;
        this.s=s;
        this.farmerName=farmerName;
        this.farmerEmail=farmerEmail;
        this.farmerMobile=farmerMobile;
        this.farmerAdharNo=farmerAdharNo;
        this.image=image;
        this.crop=crop;
        this.location=location;
        this.address=address;
        this.syncStatus=syncStatus;
        this.createdDatetime=createdDatetime;
        this.updatedDatetime=updatedDatetime;
        this.thaluka=thaluka;
        this.village=village;
        this.ss=ss;
    }
}
