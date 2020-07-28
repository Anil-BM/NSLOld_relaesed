package com.nsl.beejtantra.commonutils;

import android.graphics.Bitmap;

/**
 * Created by User on 11/6/2018.
 */

public class BitmapFile {
    private String bitmapPath;
    private Bitmap bitmap;

    public String getBitmapPath() {
        return bitmapPath;
    }

    public void setBitmapPath(String bitmapPath) {
        this.bitmapPath = bitmapPath;
    }

    public Bitmap getBitmap() {
        return bitmap;
    }

    public void setBitmap(Bitmap bitmap) {
        this.bitmap = bitmap;
    }
}
