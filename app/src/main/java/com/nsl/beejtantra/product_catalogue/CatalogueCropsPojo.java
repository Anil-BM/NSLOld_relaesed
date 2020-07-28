package com.nsl.beejtantra.product_catalogue;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by suprasoft on 6/20/2018.
 */

public class CatalogueCropsPojo {
    @SerializedName("id")
    @Expose
    public int id;
    @SerializedName("crop_name")
    @Expose
    public String cropName;
    @SerializedName("crop_img_path")
    @Expose
    public String cropImgPath;
    @SerializedName("server_pk")
    @Expose
    public int serverPk;
    @SerializedName("status")
    @Expose
    public int status;
    @SerializedName("version")
    @Expose
    public String version;
    @SerializedName("date_time")
    @Expose
    public String dateTime;
    public File file;
    @SerializedName("catalogue_crops_products_list")
    @Expose
    public List<CatalogueCropsProductsPojo> catalogueCropsProductsList=new ArrayList<>();
    @SerializedName("img_uri")
    @Expose
    public String imgURI;
    @SerializedName("sql_primary_key")
    @Expose
    public int sqlPrimaryKey;

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("id", id).append("cropName", cropName).append("cropImgPath", cropImgPath).append("serverPk", serverPk).append("status", status).append("version", version).append("date_time",dateTime).append("catalogue_crops_products_list",catalogueCropsProductsList).toString();
    }
}
