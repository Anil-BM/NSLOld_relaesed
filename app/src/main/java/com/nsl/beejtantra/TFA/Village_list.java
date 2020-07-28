package com.nsl.beejtantra.TFA;

public class Village_list {
    Integer tfa_list_id,status,sync_status,tfa_village_id;
    String village_name,created_datetime,current_sal,potential,updated_datetime;

    public Village_list(int tfa_village_id, int tfa_list_id, String current_sal, String potential, int status, int sync_status, String village_name, String created_datetime, String updated_datetime) {
        this.tfa_village_id = tfa_village_id;
        this.tfa_list_id = tfa_list_id;
        this.current_sal = current_sal;
        this.potential = potential;
        this.status = status;
        this.sync_status = sync_status;
        this.village_name = village_name;
        this.created_datetime = created_datetime;
        this.updated_datetime = updated_datetime;
    }

    public Integer getTfa_list_id() {
        return tfa_list_id;
    }

    public Integer getStatus() {
        return status;
    }

    public Integer getSync_status() {
        return sync_status;
    }

    public String getVillage_name() {
        return village_name;
    }

    public String getCreated_datetime() {
        return created_datetime;
    }

    public String getCurrent_sal() {
        return current_sal;
    }

    public String getPotential() {
        return potential;
    }

    public Village_list(Integer tfa_list_id, String current_sal, String potential, Integer status, Integer sync_status, String village_name, String created_datetime) {
        this.tfa_list_id = tfa_list_id;
        this.current_sal = current_sal;
        this.potential = potential;
        this.status = status;
        this.sync_status = sync_status;
        this.village_name = village_name;
        this.created_datetime = created_datetime;
    }

    public Integer getTfa_village_id() {
        return tfa_village_id;
    }

    public String getUpdated_datetime() {
        return updated_datetime;
    }


}
