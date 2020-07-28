package com.nsl.beejtantra.pojo;

/**
 * Created by SupraSoft on 1/24/2019.
 */

public class UserRegions {
    private int id;
    private String userRegionsId;
    private String userId;
    private String regionId;
    private String status;

    public UserRegions() {
    }

    public UserRegions(String userRegionsId, String userId, String regionId, String status) {
        this.userRegionsId = userRegionsId;
        this.userId = userId;
        this.regionId = regionId;
        this.status = status;
    }

    public String getUserRegionsId() {
        return userRegionsId;
    }

    public void setUserRegionsId(String userRegionsId) {
        this.userRegionsId = userRegionsId;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getRegionId() {
        return regionId;
    }

    public void setRegionId(String regionId) {
        this.regionId = regionId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }
}
