package com.nsl.beejtantra.pojo;

/**
 * Created by SupraSoft on 1/28/2019.
 */

public class FarmerPojo {
    private int primaryId;
    private String farmerId;
    private String farmerName;
    private String mobile;
    private String state;
    private String locationDistrict;
    private String regionId;
    private String locationTaluka;
    private String locationVillage;
    private String totalLandHolding;
    private String status;
    private String ffmId;
    private String cDateTime;
    private String uDateTime;

    public FarmerPojo(){

    }
    public FarmerPojo(String farmerId, String farmerName, String mobile, String state, String locationDistrict, String locationTaluka, String locationVillage, String regionId, String totalLandHolding, String status, String ffmId) {
        setFarmerId(farmerId);
        setFarmerName(farmerName);
        setMobile(mobile);
        setState(state);
        setLocationDistrict(locationDistrict);
        setRegionId(regionId);
        setLocationTaluka(locationTaluka);
        setLocationVillage(locationVillage);
        setTotalLandHolding(totalLandHolding);
        setStatus(status);
        setFfmId(ffmId);
    }

    public int getPrimaryId() {
        return primaryId;
    }

    public void setPrimaryId(int primaryId) {
        this.primaryId = primaryId;
    }

    public String getFarmerId() {
        return farmerId;
    }

    public void setFarmerId(String farmerId) {
        this.farmerId = farmerId;
    }

    public String getFarmerName() {
        return farmerName;
    }

    public void setFarmerName(String farmerName) {
        this.farmerName = farmerName;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getLocationDistrict() {
        return locationDistrict;
    }

    public void setLocationDistrict(String locationDistrict) {
        this.locationDistrict = locationDistrict;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getLocationTaluka() {
        return locationTaluka;
    }

    public void setLocationTaluka(String locationTaluka) {
        this.locationTaluka = locationTaluka;
    }

    public String getLocationVillage() {
        return locationVillage;
    }

    public void setLocationVillage(String locationVillage) {
        this.locationVillage = locationVillage;
    }

    public String getTotalLandHolding() {
        return totalLandHolding;
    }

    public void setTotalLandHolding(String totalLandHolding) {
        this.totalLandHolding = totalLandHolding;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getcDateTime() {
        return cDateTime;
    }

    public void setcDateTime(String cDateTime) {
        this.cDateTime = cDateTime;
    }

    public String getuDateTime() {
        return uDateTime;
    }

    public void setuDateTime(String uDateTime) {
        this.uDateTime = uDateTime;
    }

    public String getFfmId() {
        return ffmId;
    }

    public void setFfmId(String ffmId) {
        this.ffmId = ffmId;
    }
}
