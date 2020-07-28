package com.nsl.beejtantra.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.List;

/**
 * Created by SupraSoft on 2/7/2019.
 */

public class MenuNav {
    @SerializedName("menu")
    @Expose
    public List<Menu> menu = null;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("menu", menu).toString();
    }
}
