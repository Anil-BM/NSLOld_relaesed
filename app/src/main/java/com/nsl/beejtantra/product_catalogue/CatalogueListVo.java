package com.nsl.beejtantra.product_catalogue;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import org.apache.commons.lang3.builder.ToStringBuilder;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suprasoft on 6/29/2018.
 */

public class CatalogueListVo {
    @SerializedName("catalogue_crops_list")
    @Expose
    public List<CatalogueCropsPojo> catalogueCropsList=new ArrayList<>();

    @Override
    public String toString() {
        return new ToStringBuilder(this).append("catalogue_crops_list",catalogueCropsList).toString();
    }
}
