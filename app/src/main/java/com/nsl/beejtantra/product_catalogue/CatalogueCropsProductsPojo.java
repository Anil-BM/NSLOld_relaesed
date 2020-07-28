package com.nsl.beejtantra.product_catalogue;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.Serializable;

/**
 * Created by suprasoft on 6/20/2018.
 */

public class CatalogueCropsProductsPojo implements Serializable{
    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("crop_relation_id")
    @Expose
    public int cropRelationId;
    @SerializedName("server_pk")
    @Expose
    public int serverPk;
    @SerializedName("product_name")
    @Expose
    public String productName;
    @SerializedName("product_img")
    @Expose
    public String productImg;
    @SerializedName("description")
    @Expose
    public String description;
    @SerializedName("status")
    @Expose
    public int status;
    @SerializedName("version")
    @Expose
    public String version;
    @SerializedName("date_time")
    @Expose
    public String dateTime;
    @SerializedName("img_uri")
    @Expose
    public String imgURI;
    @SerializedName("sql_primary_key")
    @Expose
    public int sqlPrimaryKey;


    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("cropRelationId", cropRelationId).append("serverPk", serverPk).append("productName", productName).append("productImg", productImg).append("description", description).append("status", status).append("version", version).append("dateTime", dateTime).toString();
    }

}
