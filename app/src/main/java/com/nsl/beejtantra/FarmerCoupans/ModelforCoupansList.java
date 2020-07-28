package com.nsl.beejtantra.FarmerCoupans;

public class ModelforCoupansList {
    String name,mobile,date,place,coupan,village,thaluka,coupontype;

    public String getVillage() {
        return village;
    }

    public String getThaluka() {
        return thaluka;
    }

    public String getCoupontype() {
        return coupontype;
    }

    public ModelforCoupansList(String coupan, String name, String mobile, String place, String date, String village, String thaluka, String coupontype) {
        this.coupan=coupan;
        this.name=name;
        this.mobile=mobile;
        this.date=date;
        this.place=place;
        this.village=village;
        this.thaluka=thaluka;
        this.coupontype=coupontype;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getCoupan() {
        return coupan;
    }

    public void setCoupan(String coupan) {
        this.coupan = coupan;
    }
}
