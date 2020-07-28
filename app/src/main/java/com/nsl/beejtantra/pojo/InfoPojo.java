package com.nsl.beejtantra.pojo;

/**
 * Created by User on 6/14/2018.
 */

public class InfoPojo {

    String Name;
    int Image;

    public InfoPojo(String name, int image) {
        Name = name;
        Image = image;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public int getImage() {
        return Image;
    }

    public void setImage(int image) {
        Image = image;
    }
}
