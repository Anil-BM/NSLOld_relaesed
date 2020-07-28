package com.nsl.beejtantra.TFA.support;

public class tfaactivitylist {
    String tfa_master_id;
    String tfa_title;
    String status;
    String created_datetime;
    String updated_datetime;

    public tfaactivitylist(String tfa_master_id, String tfa_title, String status, String created_datetime, String updated_datetime) {
        this.tfa_master_id = tfa_master_id;
        this.tfa_title = tfa_title;
        this.status = status;
        this.created_datetime = created_datetime;
        this.updated_datetime = updated_datetime;
    }

    public String getTfa_master_id() {
        return tfa_master_id;
    }

    public String getTfa_title() {
        return tfa_title;
    }

    public String getStatus() {
        return status;
    }

    public String getCreated_datetime() {
        return created_datetime;
    }

    public String getUpdated_datetime() {
        return updated_datetime;
    }
}
