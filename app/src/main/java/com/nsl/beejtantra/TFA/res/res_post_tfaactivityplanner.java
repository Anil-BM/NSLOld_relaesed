package com.nsl.beejtantra.TFA.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class res_post_tfaactivityplanner {
    @SerializedName("Status")
    @Expose
    public String status;
    @SerializedName("msg")
    @Expose
    public String msg;
    @SerializedName("InsertedListServerData")
    @Expose
    public Integer insertedListServerData;
    @SerializedName("InsertedListLocalData")
    @Expose
    public String insertedListLocalData;
    @SerializedName("InsertedVillServerData")
    @Expose
    public List<Integer> insertedVillServerData = null;
    @SerializedName("InsertedVillLocalData")
    @Expose
    public List<InsertedVillLocalDatum> insertedVillLocalData = null;

    private class InsertedVillLocalDatum {

        @SerializedName("tfa_village_id")
        @Expose
        public String tfaVillageId;
        @SerializedName("tfa_list_id")
        @Expose
        public String tfaListId;
        @SerializedName("village_name")
        @Expose
        public String villageName;
        @SerializedName("current_sal")
        @Expose
        public String currentSal;
        @SerializedName("potential")
        @Expose
        public String potential;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("sync_status")
        @Expose
        public String syncStatus;
        @SerializedName("created_datetime")
        @Expose
        public String createdDatetime;
    }
}

