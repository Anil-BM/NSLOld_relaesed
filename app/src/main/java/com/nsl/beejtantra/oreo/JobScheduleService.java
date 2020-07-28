package com.nsl.beejtantra.oreo;

import android.app.ActivityManager;
import android.app.job.JobParameters;
import android.app.job.JobService;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.Message;
import android.os.Messenger;
import android.os.PowerManager;
import android.os.RemoteException;
import android.support.annotation.Nullable;
import android.util.Log;

import com.firebase.jobdispatcher.Job;
import com.nsl.beejtantra.Constants;
import com.nsl.beejtantra.DatabaseHandler;
import com.nsl.beejtantra.commonutils.Common;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Timer;
import java.util.TimerTask;

import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_GEO_TRACKING_CHECK_IN_TIME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_GEO_TRACKING_CHECK_OUT_TIME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_GEO_TRACKING_CREATED_DATETIME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_GEO_TRACKING_FFMID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_GEO_TRACKING_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_GEO_TRACKING_ROUTE_PATH_LAT_LONG;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_GEO_TRACKING;


public class JobScheduleService extends JobService {
    private static final String TAG = JobScheduleService.class.getSimpleName();
    private static final String CHANNEL_ID = "abc";
    public static final String STARTED_FROM = "JobScheduleService";
    private Messenger mActivityMessenger;
    private static final int NOTIFICATION_ID = 12345678;
    private DatabaseHandler db;
    private SQLiteDatabase sdbw;
    private String geoTrackingId;
    private Timer timer;

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "onStartCommand");
    //    mActivityMessenger = intent.getParcelableExtra(STARTED_FROM);
        return START_NOT_STICKY;
    }

    @Override
    public boolean onStartJob(JobParameters jobParameters) {

        Log.d(TAG, "onStartJob");
        db = new DatabaseHandler(this);
      /*  sendMessage(MSG_JOB_START, "Job Started" + "\n Job Id : " +
                                   jobParameters.getJobId());*/

        new JobAsyncTask(this).execute(jobParameters);
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters jobParameters) {
        Log.d(TAG, "onStopJob");
     //   Toast.makeText(this, getString(R.string.job_stop), Toast.LENGTH_SHORT).show();

      //  sendMessage(MSG_JOB_STOP, getString(R.string.job_stop));
        if (timer!=null){
            timer.cancel();
        }
        return false;
    }

    private class JobAsyncTask extends AsyncTask<JobParameters, Void, JobParameters> {
        private final JobService jobService;

        JobAsyncTask(JobService jobService) {
            Log.d(TAG, "JobAsyncTask");
            this.jobService = jobService;
        }

        @Override
        protected JobParameters doInBackground(JobParameters... jobParameters) {
          //  SystemClock.sleep(5000);
            return jobParameters[0];
        }

        @Override
        protected void onPostExecute(JobParameters jobParameters) {
            Log.d(TAG, "JobAsyncTask completed");


            // Finish the job service if required
//            jobService.jobFinished(jobParameters, false);

             timer=new Timer();
            timer.scheduleAtFixedRate(new TimerTask() {
                @Override
                public void run() {
                    Log.d(TAG, "JobAsyncTask Running: "+System.currentTimeMillis()+"\n"+isCheckedIn()+"\n"+serviceIsRunningInForeground(JobScheduleService.this)+"\n"+LocationUpdatesService.isPaused(JobScheduleService.this));
                    if (isCheckedIn() && !serviceIsRunningInForeground(JobScheduleService.this) && !LocationUpdatesService.isPaused(JobScheduleService.this)){
                        Log.d(TAG, "Services stared from job: ");
                        try{
                            turnOnScreen(JobScheduleService.this,10*1000);
                        Intent serviceIntent = new Intent(JobScheduleService.this, LocationUpdatesService.class);
                        serviceIntent.putExtra(STARTED_FROM, true);
                        startService(serviceIntent);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                    }else{
                      //  stopSelf();
                    }

                }
            },2*1000, 60*1000);
        }
    }

    private void turnOnScreen(Context context,long time) {
        PowerManager.WakeLock screenLock = null;
        if ((context.getSystemService(POWER_SERVICE)) != null) {
            screenLock = ((PowerManager) context.getSystemService(POWER_SERVICE)).newWakeLock(
                    PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
            screenLock.acquire(time /*10 minutes*/);


            //  screenLock.release();
        }
    }
    private void sendMessage(int messageID, @Nullable Object params) {
        if (mActivityMessenger == null) {
            Log.d(TAG, "Service is bound, not started. There's no callback to send a message to.");
            return;
        }
        Message m = Message.obtain();
        m.what = messageID;
        m.obj = params;
        try {
            mActivityMessenger.send(m);
        } catch (RemoteException e) {
            Log.e(TAG, "Error passing service object back to activity.");
        }
    }

    public boolean isCheckedIn() {

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String strDate = sdf.format(c.getTime());
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat(Constants.DateFormat.COMMON_DATE_FORMAT);
        String datefromcalander = df.format(c.getTime());
        String selectQueryss = "SELECT  " + KEY_TABLE_GEO_TRACKING_ID + "," + KEY_TABLE_GEO_TRACKING_ROUTE_PATH_LAT_LONG + "," + KEY_TABLE_GEO_TRACKING_FFMID + "," + KEY_TABLE_GEO_TRACKING_CHECK_OUT_TIME + "," + KEY_TABLE_GEO_TRACKING_CHECK_IN_TIME + " FROM " + TABLE_GEO_TRACKING + " where " + " visit_date like '" + datefromcalander + "%' and user_id ='" + Common.getUserIdFromSP(JobScheduleService.this)+"'"+ " ORDER BY " + KEY_TABLE_GEO_TRACKING_ID + " DESC LIMIT 1 ";
        try {


        sdbw = db.getWritableDatabase();

        Cursor ccc = sdbw.rawQuery(selectQueryss, null);
        System.out.println("cursor count " + ccc.getCount() + "\n" + selectQueryss);
        if (ccc != null && ccc.moveToFirst()) {
                if ((ccc.getString(3) == null || ccc.getString(3).equalsIgnoreCase("") || ccc.getString(3).equalsIgnoreCase("null")) && (ccc.getString(4) != null && ccc.getString(4).length() > 5)) {
                    geoTrackingId = ccc.getString(0);
                    String checkInTime = ccc.getString(4);
                    return true;
                }
        }
        }catch (Exception e){
            e.printStackTrace();
        }finally {

        }
        return false;
    }


    public boolean serviceIsRunningInForeground(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(
                Integer.MAX_VALUE)) {
            if (LocationUpdatesService.class.getName().equals(service.service.getClassName())) {
                //if (service.foreground || service.started) {
                    return true;
               // }
            }
        }
        return false;
    }


}
