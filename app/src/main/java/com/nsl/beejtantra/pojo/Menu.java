package com.nsl.beejtantra.pojo;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

/**
 * Created by SupraSoft on 2/7/2019.
 */

public class Menu {
    @SerializedName("id")
    @Expose
    public String id;
    @SerializedName("name")
    @Expose
    public String name;
    @SerializedName("read")
    @Expose
    public String read;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("name", name).append("read", read).toString();
    }

}
