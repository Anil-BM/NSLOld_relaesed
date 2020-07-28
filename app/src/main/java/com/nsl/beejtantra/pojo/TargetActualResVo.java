package com.nsl.beejtantra.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * Created by SupraSoft on 2/26/2019.
 */

public class TargetActualResVo {
    @SerializedName("targetsvsactuals")
    @Expose
    public List<TargetActual> targetsvsactuals = null;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("targetsvsactuals", targetsvsactuals).toString();
    }
}
