package com.nsl.beejtantra.TFA;

public class ActivityPlanner {
     int actual_estimation_per_head;
    int district_id,division_id,crop_id,product_id,tfa_activity_master_id,no_of_farmers,estimation_per_head,sync_status,
            total_expences,advance_required,user_id,created_user_id,status,tfa_list_id;
    String activity_date,created_datetime,updated_datetime;
    String taluka,village,conducting_place,user_email,approved_date,location_lat_lang,owner_name,owner_number,images,images1,images2,images3,images4,images5,images6;

    String approval_name,approval_role,approval_mail,approval_pnno;
    String pending_by_name,pending_by_role;


    int approval_status;String approval_comments;int approved_by;String approval_date;
    int used_farmers,non_used_farmers,actual_no_farmers,actual_total_expences;

    public int getDistrict_id() {
        return district_id;
    }

    public int getDivision_id() {
        return division_id;
    }

    public int getCrop_id() {
        return crop_id;
    }

    public int getProduct_id() {
        return product_id;
    }

    public int getTfa_activity_master_id() {
        return tfa_activity_master_id;
    }

    public int getNo_of_farmers() {
        return no_of_farmers;
    }

    public int getEstimation_per_head() {
        return estimation_per_head;
    }

    public int getTotal_expences() {
        return total_expences;
    }

    public int getAdvance_required() {
        return advance_required;
    }

    public int getUser_id() {
        return user_id;
    }

    public int getCreated_user_id() {
        return created_user_id;
    }

    public int getStatus() {
        return status;
    }

    public String getActivity_date() {
        return activity_date;
    }

    public String getCreated_datetime() {
        return created_datetime;
    }

    public String getUpdated_datetime() {
        return updated_datetime;
    }

    public String getTaluka() {
        return taluka;
    }

    public String getVillage() {
        return village;
    }

    public String getConducting_place() {
        return conducting_place;
    }

    public String getUser_email() {
        return user_email;
    }

    public int getApproval_status() {
        return approval_status;
    }

    public String getApproval_comments() {
        return approval_comments;
    }

    public int getApproved_by() {
        return approved_by;
    }

    public String getApproval_date() {
        return approval_date;
    }



    public String getApproval_mail() {
        return approval_mail;
    }

    public String getApproval_pnno() {
        return approval_pnno;
    }

    public ActivityPlanner(int district_id, int division_id, int crop_id, int product_id, int tfa_activity_master_id, int no_of_farmers, int estimation_per_head, int total_expences, int advance_required, int user_id, int created_user_id, int status, String activity_date, String created_datetime, String updated_datetime, String taluka, String village, String conducting_place, String user_email,
                           int approval_status, String approval_comments, int approved_by, String approval_date,
                           String approval_name, String approval_role, String approval_mail, String approval_pnno) {
        this.district_id = district_id;
        this.division_id = division_id;
        this.crop_id = crop_id;
        this.product_id = product_id;
        this.tfa_activity_master_id = tfa_activity_master_id;
        this.no_of_farmers = no_of_farmers;
        this.estimation_per_head = estimation_per_head;
        this.total_expences = total_expences;
        this.advance_required = advance_required;
        this.user_id = user_id;
        this.created_user_id = created_user_id;
        this.status = status;
        this.activity_date = activity_date;
        this.created_datetime = created_datetime;
        this.updated_datetime = updated_datetime;
        this.taluka = taluka;
        this.village = village;
        this.conducting_place = conducting_place;
        this.user_email = user_email;
        this.approval_status = approval_status;
        this.approval_comments = approval_comments;
        this.approved_by = approved_by;
        this.approval_date = approval_date;

        this.approval_name = approval_name;
        this.approval_role = approval_role;
        this.approval_mail = approval_mail;
        this.approval_pnno = approval_pnno;
    }

    public int getTfa_list_id() {
        return tfa_list_id;
    }

    public ActivityPlanner(int tfa_list_id, int district_id, int division_id, int crop_id, int product_id, int tfa_activity_master_id, int no_of_farmers, int estimation_per_head, int total_expences, int advance_required, int user_id, int created_user_id, int status, String activity_date, String created_datetime, String updated_datetime, String taluka, String village, String conducting_place, String user_email) {
        this.tfa_list_id = tfa_list_id;
        this.district_id = district_id;
        this.division_id = division_id;
        this.crop_id = crop_id;
        this.product_id = product_id;
        this.tfa_activity_master_id = tfa_activity_master_id;
        this.no_of_farmers = no_of_farmers;
        this.estimation_per_head = estimation_per_head;
        this.total_expences = total_expences;
        this.advance_required = advance_required;
        this.user_id = user_id;
        this.created_user_id = created_user_id;
        this.status = status;
        this.activity_date = activity_date;
        this.created_datetime = created_datetime;
        this.updated_datetime = updated_datetime;
        this.taluka = taluka;
        this.village = village;
        this.conducting_place = conducting_place;
        this.user_email = user_email;
    }


    public int getActual_estimation_per_head() {
        return actual_estimation_per_head;
    }

    public int getSync_status() {
        return sync_status;
    }

    public String getApproved_date() {
        return approved_date;
    }

    public String getLocation_lat_lang() {
        return location_lat_lang;
    }

    public String getOwner_name() {
        return owner_name;
    }

    public String getOwner_number() {
        return owner_number;
    }

    public String getImages() {
        return images;
    }

    public String getImages1() {
        return images1;
    }

    public String getImages2() {
        return images2;
    }

    public String getImages3() {
        return images3;
    }

    public String getImages4() {
        return images4;
    }

    public String getImages5() {
        return images5;
    }

    public String getImages6() {
        return images6;
    }

    public int getUsed_farmers() {
        return used_farmers;
    }

    public int getNon_used_farmers() {
        return non_used_farmers;
    }

    public int getActual_no_farmers() {
        return actual_no_farmers;
    }

    public int getActual_total_expences() {
        return actual_total_expences;
    }



    public ActivityPlanner(int tfa_list_id,
                           int district_id,
                           int division_id,
                           int crop_id,
                           int product_id,
                           int tfa_activity_master_id,
                           String activity_date,
                           String taluka,
                           String village,
                           int no_of_farmers,
                           int used_farmers,
                           int non_used_farmers,
                           int actual_no_farmers,
                           int estimation_per_head,
                           int actual_estimation_per_head,
                           int total_expences,
                           int actual_total_expences,
                           int advance_required,
                           String conducting_place,
                           int user_id,
                           int created_user_id,
                           String user_email,
                           int status,
                           int approval_status,
                           String approval_comments,
                           int approved_by,
                           String approved_date,
                           String location_lat_lang,
                           String owner_number,
                           String owner_name,
                           String images,
                           String images1,
                           String images2,
                           String images3,
                           String images4,
                           String images5,
                           String images6,
                           int sync_status,
                           String created_datetime,
                           String updated_datetime) {



        this.tfa_list_id = tfa_list_id;
        this.district_id = district_id;
        this.division_id = division_id;
        this.crop_id = crop_id;
        this.product_id = product_id;
        this.tfa_activity_master_id = tfa_activity_master_id;
        this.no_of_farmers = no_of_farmers;
        this.used_farmers=used_farmers;
        this.estimation_per_head = estimation_per_head;
        this.total_expences = total_expences;
        this.advance_required = advance_required;
        this.user_id = user_id;
        this.created_user_id = created_user_id;
        this.status = status;
        this.activity_date = activity_date;
        this.created_datetime = created_datetime;
        this.updated_datetime = updated_datetime;
        this.taluka = taluka;
        this.village = village;
        this.conducting_place = conducting_place;
        this.user_email = user_email;
        this.non_used_farmers = non_used_farmers;
        this.actual_no_farmers = actual_no_farmers;
        this.actual_estimation_per_head=actual_estimation_per_head;
        this.actual_total_expences=actual_total_expences;
        this.approval_status = approval_status;
        this.approval_comments = approval_comments;
        this.approved_by = approved_by;
        this.approved_date = approved_date;
        this.location_lat_lang=location_lat_lang;
        this.owner_name = owner_name;
        this.owner_number = owner_number;
        this.images = images;
        this.images1 = images1;
        this.images2=images2;
        this.images3 = images3;
        this.images4=images4;
        this.images5 = images5;
        this.images6=images6;
        this.sync_status=sync_status;
        this.pending_by_name = pending_by_name;
        this.pending_by_role = pending_by_role;
    }


}
