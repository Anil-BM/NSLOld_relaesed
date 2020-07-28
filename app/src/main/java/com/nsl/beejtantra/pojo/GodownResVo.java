package com.nsl.beejtantra.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * Created by SupraSoft on 3/8/2019.
 */

public class GodownResVo {
    @SerializedName("godown")
    @Expose
    public List<Godown> godown = null;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("godown", godown).toString();
    }
}
