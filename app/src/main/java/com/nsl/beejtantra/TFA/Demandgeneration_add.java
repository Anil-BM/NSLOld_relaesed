package com.nsl.beejtantra.TFA;

public class Demandgeneration_add {

    private String userId;
    private String district_id;
    private String division_id;
    private String crop_id;
    private String event_name;
    private String address;
    private String planneddate;
    private String nooffarmers;
    private String status;
    private String createddate;
    private String updated;


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public void setDistrict_id(String district_id) {
        this.district_id = district_id;
    }

    public void setDivision_id(String division_id) {
        this.division_id = division_id;
    }

    public void setCrop_id(String crop_id) {
        this.crop_id = crop_id;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setPlanneddate(String planneddate) {
        this.planneddate = planneddate;
    }

    public void setNooffarmers(String nooffarmers) {
        this.nooffarmers = nooffarmers;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public void setCreateddate(String createddate) {
        this.createddate = createddate;
    }

    public void setUpdated(String updated) {
        this.updated = updated;
    }

    public String getDistrict_id() {
        return district_id;
    }

    public String getDivision_id() {
        return division_id;
    }

    public String getCrop_id() {
        return crop_id;
    }

    public String getEvent_name() {
        return event_name;
    }

    public String getAddress() {
        return address;
    }

    public String getPlanneddate() {
        return planneddate;
    }

    public String getNooffarmers() {
        return nooffarmers;
    }

    public String getStatus() {
        return status;
    }

    public String getCreateddate() {
        return createddate;
    }

    public String getUpdated() {
        return updated;
    }

    public Demandgeneration_add(String userId, String district_id,
                                String division_id, String crop_id, String event_name, String address,
                                String planneddate, String nooffarmers,
                                String status, String createddate, String updated) {
        this.userId = userId;
        this.district_id = district_id;
        this.division_id = division_id;
        this.crop_id = crop_id;
        this.event_name = event_name;
        this.address = address;

        this.planneddate = planneddate;
        this.nooffarmers = nooffarmers;
        this.status = status;
        this.createddate = createddate;
        this.updated = updated;
    }


}
