package com.nsl.beejtantra.TFA;

import com.mapbox.mapboxsdk.geometry.LatLng;

public  class MarkerData2 {
    public LatLng latLng;
    public String str_name = "";
    public String str_address = "";

    public MarkerData2(LatLng latLng, String str_name, String str_address) {
        this.latLng = latLng;
        this.str_name = str_name;
        this.str_address = str_address;
    }
}
