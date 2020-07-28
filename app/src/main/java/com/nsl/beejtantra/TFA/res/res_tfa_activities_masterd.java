package com.nsl.beejtantra.TFA.res;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.util.List;

public class res_tfa_activities_masterd {
    @SerializedName("status")
    @Expose
    public String status;

    public List<MasterDatum> getMasterData() {
        return masterData;
    }

    public String getStatus() {
        return status;
    }

    @SerializedName("master_data")
    @Expose
    public List<MasterDatum> masterData = null;

    public class MasterDatum {

        @SerializedName("tfa_master_id")
        @Expose
        public String tfaMasterId;
        @SerializedName("tfa_title")
        @Expose
        public String tfaTitle;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("created_datetime")
        @Expose
        public String createdDatetime;
        @SerializedName("updated_datetime")
        @Expose
        public String updatedDatetime;
    }
}
