package com.nsl.beejtantra.products;

import java.util.ArrayList;

/**
 * Created by sowmy on 6/14/2018.
 */

public class ProductCatelogPojo extends ArrayList<ProductCatelogPojo> {
    String hybride_name;
    String hybrid_description;

    public ProductCatelogPojo(String hybride_name, String hybrid_description) {
        this.hybride_name = hybride_name;
        this.hybrid_description = hybrid_description;
    }

    public String getHybride_name() {
        return hybride_name;
    }

    public void setHybride_name(String hybride_name) {
        this.hybride_name = hybride_name;
    }

    public String getHybrid_description() {
        return hybrid_description;
    }

    public void setHybrid_description(String hybrid_description) {
        this.hybrid_description = hybrid_description;
    }
}
