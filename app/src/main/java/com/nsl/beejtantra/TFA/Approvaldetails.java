package com.nsl.beejtantra.TFA;
import android.content.ContentValues;

public class Approvaldetails {
    int list_id;
    int approval_id_server,tfa_approval_id;
    int approval_status;
    String approval_comments,approved_by,approved_date,tfa_approved_user_id,
            status,created_datetime,updated_datetime,sync_status;
    String approval_name,approval_role,approval_mail,approval_pnno;
   public String approval_name2,approval_role2;
    public int approval_status2;
    public String pending_approval_name, pending_approval_role;
    String pending_by_name;String pending_by_role;

    public int getList_id() {
        return list_id;
    }
    public Approvaldetails(int list_id, int approval_id_server, int approval_status, String approval_comments,
                           String approved_by, String approved_date, String tfa_approved_user_id, String status, String created_datetime,
                           String updated_datetime, String sync_status,String approval_name, String approval_role) {
        this.list_id = list_id;
        this.approval_id_server = approval_id_server;
        this.approval_status = approval_status;
        this.approval_comments = approval_comments;
        this.approved_by = approved_by;
        this.approved_date = approved_date;
        this.tfa_approved_user_id = tfa_approved_user_id;
        this.status = status;
        this.created_datetime = created_datetime;
        this.updated_datetime = updated_datetime;
        this.sync_status = sync_status;

        this.approval_name = approval_name;
        this.approval_role = approval_role;
        this.approval_mail = approval_mail;//removed anil
        this.approval_pnno = approval_pnno;
    }

    public int getTfa_approval_id() {
        return tfa_approval_id;
    }

    public String getApproval_name() {
        return approval_name;
    }

    public String getApproval_role() {
        return approval_role;
    }

    public String getApproval_mail() {
        return approval_mail;
    }

    public String getApproval_pnno() {
        return approval_pnno;
    }
    public String getPending_by_name() {
        return pending_by_name;
    }

    public String getPending_by_role() {
        return pending_by_role;
    }

    public Approvaldetails(int tfa_approval_id, int list_id, int approval_id_server, int approval_status, String approval_comments,
                           String approved_by, String approved_date, String tfa_approved_user_id, String status, String created_datetime,
                           String updated_datetime, String sync_status, String approval_name, String approval_role, String approval_mail, String approval_pnno,
                           String pending_by_name,String pending_by_role) {
        this.tfa_approval_id = tfa_approval_id;
        this.list_id = list_id;
        this.approval_id_server = approval_id_server;
        this.approval_status = approval_status;
        this.approval_comments = approval_comments;
        this.approved_by = approved_by;
        this.approved_date = approved_date;
        this.tfa_approved_user_id = tfa_approved_user_id;
        this.status = status;
        this.created_datetime = created_datetime;
        this.updated_datetime = updated_datetime;
        this.sync_status = sync_status;

        this.approval_name = approval_name;
        this.approval_role = approval_role;
        this.approval_mail = approval_mail;
        this.approval_pnno = approval_pnno;
        this.pending_by_name = pending_by_name;
        this.pending_by_role = pending_by_role;
    }
    public int getApproval_id_server() {
        return approval_id_server;
    }
    public int getApproval_status() {
        return approval_status;
    }
    public String getApproval_comments() {
        return approval_comments;
    }
    public String getApproved_by() {
        return approved_by;
    }
    public String getApproved_date() {
        return approved_date;
    }
    public String getTfa_approved_user_id() {
        return tfa_approved_user_id;
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
    public String getSync_status() {
        return sync_status;
    }


    public String getApproval_name2() {
        return approval_name2;
    }

    public String getApproval_role2() {
        return approval_role2;
    }

    public int getApproval_status2() {
        return approval_status2;
    }

    public String getPenfding_approval_name() {
        return pending_approval_name;
    }

    public String getPenfding_approval_role() {
        return pending_approval_role;
    }

    public Approvaldetails(String pending_approval_name, String penfding_approval_role, int approval_status,
                           String approval_name, String approval_role) {

        this.approval_status2 = approval_status;
        this.approval_name2 = approval_name;
        this.approval_role2 = approval_role;

        this.pending_approval_name = pending_approval_name;
        this.pending_approval_role = penfding_approval_role;

    }
}
