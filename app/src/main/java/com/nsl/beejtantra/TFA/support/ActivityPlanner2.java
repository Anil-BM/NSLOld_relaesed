package com.nsl.beejtantra.TFA.support;

public  class ActivityPlanner2 {
    int tfa_list_id,tfa_activity_master_id,used_farmers,non_used_farmers,tfa_activity_master_id2,actual_no_farmers,
            actual_no_farmers2,actual_estimation_per_head,actual_total_expences,
            approval_status;

    String location_lat_lang,owner_number,owner_name;

    public int getTfa_list_id() {
        return tfa_list_id;
    }

    public int getTfa_activity_master_id() {
        return tfa_activity_master_id;
    }

    public int getUsed_farmers() {
        return used_farmers;
    }

    public int getNon_used_farmers() {
        return non_used_farmers;
    }

    public int getTfa_activity_master_id2() {
        return tfa_activity_master_id2;
    }

    public int getActual_no_farmers() {
        return actual_no_farmers;
    }

    public int getActual_no_farmers2() {
        return actual_no_farmers2;
    }

    public int getActual_estimation_per_head() {
        return actual_estimation_per_head;
    }

    public int getActual_total_expences() {
        return actual_total_expences;
    }






    public int getApproval_status() {
        return approval_status;
    }

    public String getLocation_lat_lang() {
        return location_lat_lang;
    }

    public String getOwner_number() {
        return owner_number;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public ActivityPlanner2(int tfa_list_id, int tfa_activity_master_id, int used_farmers, int non_used_farmers, int actual_no_farmers, int actual_estimation_per_head, int actual_total_expences, String location_lat_lang, String owner_number, String owner_name, int approval_status) {
        this.tfa_list_id = tfa_list_id;
        this.tfa_activity_master_id = tfa_activity_master_id;
        this.used_farmers = used_farmers;
        this.non_used_farmers = non_used_farmers;
        this.tfa_activity_master_id2 = tfa_activity_master_id2;
        this.actual_no_farmers = actual_no_farmers;
        this.actual_no_farmers2 = actual_no_farmers2;
        this.actual_estimation_per_head = actual_estimation_per_head;
        this.actual_total_expences = actual_total_expences;
        this.location_lat_lang = location_lat_lang;
        this.owner_number = owner_number;
        this.owner_name = owner_name;
        this.approval_status = approval_status;
    }
}
