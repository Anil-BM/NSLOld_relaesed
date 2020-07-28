package com.nsl.beejtantra.oreo;

import android.app.ActivityManager;
import android.app.AlarmManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.PowerManager;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;

import com.nsl.beejtantra.commonutils.Common;
import com.nsl.beejtantra.scheduler.DailySchedulerReceiver;

import static com.nsl.beejtantra.oreo.LocationUpdatesService.SCREEN_WAKE_TIME;

/**
 * Created by suprasoft on 7/31/2018.
 */

public class MyWakefulReceiver extends WakefulBroadcastReceiver {

    private AlarmManager alarmMgr;
    private PendingIntent alarmIntent;

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("MyWakefulReceiver", " onReceive ");
        // Start the service, keeping the device awake while the service is
        // launching. This is the Intent to deliver to the service.


        PowerManager pm = (PowerManager) context.getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();
        Log.e("screen on....", "" + isScreenOn);
        if (isScreenOn == false && (System.currentTimeMillis() - Common.getLongDataFromSP(context, SCREEN_WAKE_TIME)) > 20 * 60 * 1000) {
           /* Common.saveLongDataInSP(context, SCREEN_WAKE_TIME, System.currentTimeMillis());
            PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "MyLock");
           // wl.acquire(2000);
            PowerManager.WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyCpuLock");
            wl_cpu.acquire();
            // wl_cpu.acquire(2000);*/
        }
        if (serviceIsRunningInForeground(context)) {
            Intent service = new Intent(context, LocationUpdatesService.class);
            startWakefulService(context, service);
        }
    }

    public void setAlarm(Context context) {
        Log.d("setAlarm", "setAlarm..");
        alarmMgr = (AlarmManager) context.getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(context, MyWakefulReceiver.class);
        alarmIntent = PendingIntent.getBroadcast(context, 0, intent, 0);
        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 20000, 1 * 60 * 1000, alarmIntent);

        // Set the alarm to fire at approximately 8:30 a.m., according to the device's
//        // clock, and to repeat once a day.
//        alarmMgr.setInexactRepeating(AlarmManager.RTC_WAKEUP,
//                calendar.getTimeInMillis(),AlarmManager.INTERVAL_DAY, alarmIntent);

        // Enable {@code SampleBootReceiver} to automatically restart the alarm when the
        // device is rebooted.
        ComponentName receiver = new ComponentName(context, MyWakefulReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_ENABLED,
                PackageManager.DONT_KILL_APP);
    }
    // END_INCLUDE(set_alarm)

    /**
     * Cancels the alarm.
     *
     * @param context
     */
    // BEGIN_INCLUDE(cancel_alarm)
    public void cancelAlarm(Context context) {
        // If the alarm has been set, cancel it.
        if (alarmMgr != null) {
            alarmMgr.cancel(alarmIntent);
        }

        // Disable {@code SampleBootReceiver} so that it doesn't automatically restart the
        // alarm when the device is rebooted.
        ComponentName receiver = new ComponentName(context, MyWakefulReceiver.class);
        PackageManager pm = context.getPackageManager();

        pm.setComponentEnabledSetting(receiver,
                PackageManager.COMPONENT_ENABLED_STATE_DISABLED,
                PackageManager.DONT_KILL_APP);
    }

    public boolean serviceIsRunningInForeground(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(
                Integer.MAX_VALUE)) {
            if (LocationUpdatesService.class.getName().equals(service.service.getClassName())) {
                if (service.foreground) {
                    return true;
                }
            }
        }
        return false;
    }
}
