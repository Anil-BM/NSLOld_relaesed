package com.nsl.beejtantra.TFA;

public class ActivityIndentModel {
    private String event_name, address, event_status,location_district,location_taluka,location_village,
    event_approval_status,event_customer_id,event_datetime,event_purpose,event_id,emp_visit_id;

    public ActivityIndentModel(ActivityIndentModel activityIndentModel) {
        this.event_name = activityIndentModel.event_name;
        this.address = activityIndentModel.address;
        this.event_status = activityIndentModel.event_status;
    }


    // adding each child node to HashMap key => value

    public String getEvent_name() {
        return event_name;
    }

    public String getAddress() {
        return address;
    }

    public String getEvent_status() {
        return event_status;
    }

    public void setEvent_name(String event_name) {
        this.event_name = event_name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setEvent_status(String event_status) {
        this.event_status = event_status;
    }

    public void setLocation_district(String location_district) {
        this.location_district = location_district;
    }

    public void setLocation_taluka(String location_taluka) {
        this.location_taluka = location_taluka;
    }

    public void setLocation_village(String location_village) {
        this.location_village = location_village;
    }

    public void setEvent_approval_status(String event_approval_status) {
        this.event_approval_status = event_approval_status;
    }

    public void setEvent_customer_id(String event_customer_id) {
        this.event_customer_id = event_customer_id;
    }

    public void setEvent_datetime(String event_datetime) {
        this.event_datetime = event_datetime;
    }

    public void setEvent_purpose(String event_purpose) {
        this.event_purpose = event_purpose;
    }

    public void setEvent_id(String event_id) {
        this.event_id = event_id;
    }

    public void setEmp_visit_id(String emp_visit_id) {
        this.emp_visit_id = emp_visit_id;
    }

    public String getLocation_district() {
        return location_district;
    }

    public String getLocation_taluka() {
        return location_taluka;
    }

    public String getLocation_village() {
        return location_village;
    }

    public String getEvent_approval_status() {
        return event_approval_status;
    }

    public String getEvent_customer_id() {
        return event_customer_id;
    }

    public String getEvent_datetime() {
        return event_datetime;
    }

    public String getEvent_purpose() {
        return event_purpose;
    }

    public String getEvent_id() {
        return event_id;
    }

    public String getEmp_visit_id() {
        return emp_visit_id;
    }

    public ActivityIndentModel(String event_name, String address, String event_status) {
        this.event_name = event_name;
        this.address = address;
        this.event_status = event_status;
        this.location_district = location_district;
        this.location_taluka = location_taluka;
        this.location_village = location_village;
        this.event_approval_status = event_approval_status;
        this.event_customer_id = event_customer_id;
        this.event_datetime = event_datetime;

        this.event_purpose = event_purpose;
        this.event_id = event_id;
        this.emp_visit_id = emp_visit_id;
    }


}
