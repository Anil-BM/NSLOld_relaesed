package com.nsl.beejtantra.oreo;

import android.accessibilityservice.AccessibilityService;
import android.app.Notification;
import android.util.Log;
import android.view.accessibility.AccessibilityEvent;
import android.widget.RemoteViews;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by suprasoft on 12/11/2018.
 */

public class MyAccessibilityService extends AccessibilityService {
    @Override
    public void onAccessibilityEvent(AccessibilityEvent accessibilityEvent) {
        Log.e("onAccessibilityEvent","onAccessibilityEvent");
        /*Notification notification = (Notification) accessibilityEvent.getParcelableData();
        RemoteViews views = notification.contentView;
        Class secretClass = views.getClass();
        Log.e("onAccessibilityEvent","onAccessibilityEvent");
        try {
            Map<Integer, String> text = new HashMap<Integer, String>();

            Field outerFields[] = secretClass.getDeclaredFields();
            for (int i = 0; i < outerFields.length; i++) {
                if (!outerFields[i].getName().equals("mActions")) continue;

                outerFields[i].setAccessible(true);

                ArrayList<Object> actions = (ArrayList<Object>) outerFields[i]
                        .get(views);
                for (Object action : actions) {
                    Field innerFields[] = action.getClass().getDeclaredFields();

                    Object value = null;
                    Integer type = null;
                    Integer viewId = null;
                    for (Field field : innerFields) {
                        field.setAccessible(true);
                        if (field.getName().equals("value")) {
                            value = field.get(action);
                        } else if (field.getName().equals("type")) {
                            type = field.getInt(action);
                        } else if (field.getName().equals("viewId")) {
                            viewId = field.getInt(action);
                        }
                    }

                    if (type == 9 || type == 10) {
                        text.put(viewId, value.toString());
                    }
                }

                System.out.println("title is: " + text.get(16908310));
                System.out.println("info is: " + text.get(16909082));
                System.out.println("text is: " + text.get(16908358));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }*/
            }


    @Override
    protected void onServiceConnected() {
        super.onServiceConnected();
        Log.e("onAccessibilityEvent","onAccessibilityEvent1..");
    }

    @Override
    public void onInterrupt() {
        Log.e("onAccessibilityEvent","onInterrupt..");
    }
}
