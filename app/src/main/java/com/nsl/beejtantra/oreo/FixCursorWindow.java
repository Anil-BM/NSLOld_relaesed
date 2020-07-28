package com.nsl.beejtantra.oreo;

import android.database.CursorWindow;

import java.lang.reflect.Field;

/**
 * Created by suprasoft on 12/8/2018.
 */

public class FixCursorWindow {
    public static void fix() {
        try {
            Field field = CursorWindow.class.getDeclaredField("sCursorWindowSize");
            field.setAccessible(true);
            field.set(null, 10240 * 1024); //the 102400 is the new size added
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
