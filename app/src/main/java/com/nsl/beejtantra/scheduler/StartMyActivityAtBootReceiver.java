package com.nsl.beejtantra.scheduler;

import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.util.Log;

import com.nsl.beejtantra.oreo.AlarmReceiver;
import com.nsl.beejtantra.oreo.JobScheduleService;
import com.nsl.beejtantra.oreo.LocationUpdatesService;

import static com.nsl.beejtantra.oreo.LocationUpdatesService.isCheckedIn;
import static com.nsl.beejtantra.oreo.PlanerOneActivity.mJobId;

/**
 * Created by suprasoft on 2/6/2018.
 */

public class StartMyActivityAtBootReceiver extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
        Log.d("StartMyActivityAtBoot", "onReceive......");
        if (Intent.ACTION_BOOT_COMPLETED.equals(intent.getAction()) || "android.intent.action.QUICKBOOT_POWERON".equals(intent.getAction())) {
            Log.d("StartMyActivityAtBoot", "ACTION_BOOT_COMPLETED......");
           /* Intent intent1=new Intent(context,LocationService.class);
            context.startService(intent1);*/
            if( isCheckedIn(context) && !LocationUpdatesService.serviceIsRunningInForeground(context)) {
                AlarmReceiver.setAlarm(false, context);
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                    ComponentName componentName = new ComponentName(context, JobScheduleService.class);
                    final JobInfo jobInfo = new JobInfo.Builder(mJobId, componentName)
                            .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                            .build();

                    JobScheduler jobScheduler = (JobScheduler) context.getSystemService(
                            Context.JOB_SCHEDULER_SERVICE);
                    jobScheduler.schedule(jobInfo);
                }
            }
        }
    }
}
