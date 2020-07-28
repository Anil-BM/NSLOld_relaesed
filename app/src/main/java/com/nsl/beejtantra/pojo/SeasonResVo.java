package com.nsl.beejtantra.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * Created by SupraSoft on 3/7/2019.
 */

public class SeasonResVo {
    @SerializedName("season")
    @Expose
    public List<Season> season = null;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("season", season).toString();
    }
}
