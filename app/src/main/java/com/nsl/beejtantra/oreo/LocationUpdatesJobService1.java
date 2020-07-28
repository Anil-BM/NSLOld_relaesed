/**
 * Copyright 2017 Google Inc. All Rights Reserved.
 * <p>
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 * <p>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package com.nsl.beejtantra.oreo;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Binder;
import android.os.Build;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.IBinder;
import android.os.Looper;
import android.os.PowerManager;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.firebase.jobdispatcher.JobParameters;
import com.firebase.jobdispatcher.JobService;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.nsl.beejtantra.Constants;
import com.nsl.beejtantra.DatabaseHandler;
import com.nsl.beejtantra.R;
import com.nsl.beejtantra.SplashScreen;
import com.nsl.beejtantra.Utility;
import com.nsl.beejtantra.commonutils.Common;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_GEO_TRACKING_CHECK_IN_TIME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_GEO_TRACKING_CHECK_OUT_TIME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_GEO_TRACKING_FFMID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_GEO_TRACKING_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_GEO_TRACKING_ROUTE_PATH_LAT_LONG;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_GEO_TRACKING_USER_ID;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_GEO_TRACKING;
import static com.nsl.beejtantra.oreo.LocationUpdatesService.SCREEN_WAKE_TIME;

/**
 * A bound and started service that is promoted to a foreground service when location updates have
 * been requested and all clients unbind.
 * <p>
 * For apps running in the background on "O" devices, location is computed only once every 10
 * minutes and delivered batched every 30 minutes. This restriction applies even to apps
 * targeting "N" or lower which are run on "O" devices.
 * <p>
 * This sample show how to use a long-running service for location updates. When an activity is
 * bound to this service, frequent location updates are permitted. When the activity is removed
 * from the foreground, the service promotes itself to a foreground service, and location updates
 * continue. When the activity comes back to the foreground, the foreground service stops, and the
 * notification assocaited with that service is removed.
 */
public class LocationUpdatesJobService1 extends JobService {

    private static final String PACKAGE_NAME =
            "com.google.android.gms.location.sample.locationupdatesforegroundservice";

    private static final String TAG = LocationUpdatesJobService1.class.getSimpleName();

    /**
     * The name of the channel for notifications.
     */
    private static final String CHANNEL_ID = "channel_01";

    static final String ACTION_BROADCAST = PACKAGE_NAME + ".broadcast";

    static final String EXTRA_LOCATION = PACKAGE_NAME + ".location";
    private static final String EXTRA_STARTED_FROM_NOTIFICATION = PACKAGE_NAME +
            ".started_from_notification";

    private final IBinder mBinder = new LocalBinder();

    /**
     * The desired interval for location updates. Inexact. Updates may be more or less frequent.
     */
    private static final long UPDATE_INTERVAL_IN_MILLISECONDS = 10000;

    /**
     * The fastest rate for active location updates. Updates will never be more frequent
     * than this value.
     */
    private static final long FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS =
            UPDATE_INTERVAL_IN_MILLISECONDS / 2;

    /**
     * The identifier for the notification displayed for the foreground service.
     */
    private static final int NOTIFICATION_ID = 12345678;

    /**
     * Used to check whether the bound activity has really gone away and not unbound as part of an
     * orientation change. We create a foreground service notification only if the former takes
     * place.
     */
    private boolean mChangingConfiguration = false;

    private NotificationManager mNotificationManager;

    /**
     * Contains parameters used by {@link com.google.android.gms.location.FusedLocationProviderApi}.
     */
    private LocationRequest mLocationRequest;

    /**
     * Provides access to the Fused Location Provider API.
     */
    private FusedLocationProviderClient mFusedLocationClient;

    /**
     * Callback for changes in location.
     */
    private LocationCallback mLocationCallback;

    private Handler mServiceHandler;

    /**
     * The current location.
     */
    private Location mLocation;
    private Location oldLocation1;
    private float DISTANCE = 150;
    private float accuracyLocation;
    private float MAX_SPEED = 10;
    private String checkinlatlong;
    String trackingId, tid;
    String existedroutepath="";
    DatabaseHandler db;
    SQLiteDatabase sdbw, sdbr;
    private String ffmID;
    public SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    private String geoTrackingId;



    @Override
    public boolean onStartJob(JobParameters job) {
        Log.d("onStart", "onStartJob() :: ");

       /* PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
        boolean isScreenOn = pm.isScreenOn();
        Log.e("screen on....", "" + isScreenOn);
        if (isScreenOn == false *//*&& (System.currentTimeMillis() - Common.getLongDataFromSP(this, SCREEN_WAKE_TIME)) > 20 * 60 * 1000*//*) {
           // Common.saveLongDataInSP(context, SCREEN_WAKE_TIME, System.currentTimeMillis());
           // PowerManager.WakeLock wl = pm.newWakeLock(PowerManager.FULL_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP | PowerManager.ON_AFTER_RELEASE, "MyLock");
           // wl.acquire(2000);
            PowerManager.WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyCpuLock");
            wl_cpu.acquire();
            // wl_cpu.acquire(2000);
        }

        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        db = new DatabaseHandler(this);

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(this);


        mLocationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult == null) {
                    return;
                }
                onNewLocation(locationResult.getLastLocation());
                savePointsFromSP(LocationUpdatesJobService1.this, getPointsFromSP(LocationUpdatesJobService1.this) + "\n" + getLocationResultText(locationResult.getLocations()));
                for (Location location : locationResult.getLocations()) {
                    // Update UI with location data
                    Common.Log.i("Size : " + locationResult.getLocations().size() + "\n" + location.toString());
                    if (location != null && location.hasAccuracy()) {

                       *//* if (oldLocation1 != null && !Common.isSpeedBelow120KPH(oldLocation1.distanceTo(location), location.getTime() - oldLocation1.getTime(), 27)) {
                            oldLocation1 = location;
                            continue;
                        } else if (oldLocation1 == null) {
                            oldLocation1 = location;
                        }*//*

                        trackingId = sharedpreferences.getString("tracking_id", null);
                        if (trackingId!=null && trackingId!="") {
                            afficher(location);
                        }
                    }

                }

            }
        };

        createLocationRequest();
        getLastLocation();

        HandlerThread handlerThread = new HandlerThread(TAG);
        handlerThread.start();
        mServiceHandler = new Handler(handlerThread.getLooper());
        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Android O requires a Notification Channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            // Create the channel for the notification
            NotificationChannel mChannel =
                    new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);

            // Set the Notification Channel for the Notification Manager.
            mNotificationManager.createNotificationChannel(mChannel);
        }
        startForeground(0,getNotification());
        requestLocationUpdates();
        // TODO(developer). If targeting O, use the following code.
      *//*  if (Build.VERSION.SDK_INT == Build.VERSION_CODES.O) {
            ContextCompat.startForegroundService(this,new Intent(this,
                    LocationUpdatesJobService1.class));
            //.startServiceInForeground(new Intent(this,
            // LocationUpdatesService.class), NOTIFICATION_ID, getNotification());
        } else {
            startForeground(NOTIFICATION_ID, getNotification());
        }*/
        return true;
    }

    @Override
    public boolean onStopJob(JobParameters job) {
        return false;
    }








    /**
     * Makes a request for location updates. Note that in this sample we merely log the
     * {@link SecurityException}.
     */
    public void requestLocationUpdates() {
        Log.i(TAG, "Requesting location updates");
        Utils.setRequestingLocationUpdates(this, true);
       // startService(new Intent(getApplicationContext(), LocationUpdatesJobService1.class));
        try {
            mFusedLocationClient.requestLocationUpdates(mLocationRequest,
                    mLocationCallback, Looper.myLooper());
        } catch (SecurityException unlikely) {
            Utils.setRequestingLocationUpdates(this, false);
            Log.e(TAG, "Lost location permission. Could not request updates. " + unlikely);
        }
    }

    /**
     * Removes location updates. Note that in this sample we merely log the
     * {@link SecurityException}.
     */
    public void removeLocationUpdates() {
        Log.i(TAG, "Removing location updates");
        try {
            mFusedLocationClient.removeLocationUpdates(mLocationCallback);
            Utils.setRequestingLocationUpdates(this, false);
            stopSelf();
        } catch (SecurityException unlikely) {
            Utils.setRequestingLocationUpdates(this, true);
            Log.e(TAG, "Lost location permission. Could not remove updates. " + unlikely);
        }
    }

    /**
     * Returns the {@link NotificationCompat} used as part of the foreground service.
     */
    private Notification getNotification() {
        Intent intent = new Intent();

        CharSequence text = Utils.getLocationText(mLocation);

        // Extra to help us figure out if we arrived in onStartCommand via the notification or not.
        intent.putExtra(EXTRA_STARTED_FROM_NOTIFICATION, true);

        // The PendingIntent that leads to a call to onStartCommand() in this service.
        PendingIntent servicePendingIntent = PendingIntent.getService(this, 0, intent,
                PendingIntent.FLAG_UPDATE_CURRENT);

        // The PendingIntent to launch activity.
        PendingIntent activityPendingIntent = PendingIntent.getActivity(this, 0,
                new Intent(this, SplashScreen.class), 0);

        NotificationCompat.Builder builder = new NotificationCompat.Builder(this)
                // .addAction(R.drawable.ic_launch, getString(R.string.launch_activity),
                //         activityPendingIntent)
                .setContentIntent(activityPendingIntent)
               /* .addAction(R.drawable.ic_cancel, getString(R.string.remove_location_updates),
                        servicePendingIntent)*/
                .setContentText("Touch to Open app")
                .setContentTitle("NSL Saathi ")
                .setOngoing(true)
                .setPriority(Notification.PRIORITY_HIGH)
                .setAutoCancel(true)
                .setSmallIcon(R.mipmap.ic_launcher)
                //   .setTicker(text)
                .setWhen(System.currentTimeMillis());
        // Set the Channel ID for Android O.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            builder.setChannelId(CHANNEL_ID); // Channel ID
        }

        return builder.build();
    }

    private void getLastLocation() {
        try {
            mFusedLocationClient.getLastLocation()
                    .addOnCompleteListener(new OnCompleteListener<Location>() {
                        @Override
                        public void onComplete(@NonNull Task<Location> task) {
                            if (task.isSuccessful() && task.getResult() != null) {
                                mLocation = task.getResult();
                            } else {
                                Log.w(TAG, "Failed to get location.");
                            }
                        }
                    });
        } catch (SecurityException unlikely) {
            Log.e(TAG, "Lost location permission." + unlikely);
        }
    }

    private void onNewLocation(Location location) {
        Log.i(TAG, "New location: " + location);

        mLocation = location;

        // Notify anyone listening for broadcasts about the new location.
        Intent intent = new Intent(ACTION_BROADCAST);
        intent.putExtra(EXTRA_LOCATION, location);
        LocalBroadcastManager.getInstance(getApplicationContext()).sendBroadcast(intent);

        // Update notification content if running as a foreground service.
        if (serviceIsRunningInForeground(this)) {
           //  mNotificationManager.notify(NOTIFICATION_ID, getNotification());
        }
    }

    /**
     * Sets the location request parameters.
     */
    private void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setFastestInterval(FASTEST_UPDATE_INTERVAL_IN_MILLISECONDS);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    /**
     * Class used for the client Binder.  Since this service runs in the same process as its
     * clients, we don't need to deal with IPC.
     */
    public class LocalBinder extends Binder {
        LocationUpdatesJobService1 getService() {
            return LocationUpdatesJobService1.this;
        }
    }

    /**
     * Returns true if this is a foreground service.
     *
     * @param context The {@link Context}.
     */
    public boolean serviceIsRunningInForeground(Context context) {
        ActivityManager manager = (ActivityManager) context.getSystemService(
                Context.ACTIVITY_SERVICE);
        for (ActivityManager.RunningServiceInfo service : manager.getRunningServices(
                Integer.MAX_VALUE)) {
            if (getClass().getName().equals(service.service.getClassName())) {
                if (service.foreground) {
                    return true;
                }
            }
        }
        return false;
    }

    private String getLocationResultText(List<Location> mLocations) {
        if (mLocations.isEmpty()) {
            return "Unknown Location";
        }
        StringBuilder sb = new StringBuilder();
        for (Location location : mLocations) {
            sb.append("(");
            sb.append(location.getLatitude());
            sb.append(", ");
            sb.append(location.getLongitude());
            sb.append(", ");
            sb.append(location.getAccuracy());
            sb.append(", ");
            sb.append(getCurrentDateAndTime());
            sb.append(", ");
            sb.append(location.getProvider());
            sb.append(")");
            sb.append("\n");
        }
        return sb.toString();
    }

    public static SharedPreferences getDefaultSP(Context context) {

        return PreferenceManager.getDefaultSharedPreferences(context);

    }


    public static void savePointsFromSP(Context context, String sb) {

        getDefaultSP(context).edit().putString("points", sb).commit();

    }

    public static String getPointsFromSP(Context context) {

        return getDefaultSP(context).getString("points", "");

    }

    public static String getCurrentDateAndTime() {
        return new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
    }

    private void afficher(Location mLocation) {

        DISTANCE = Common.getDistanceByAccuracy(mLocation.getAccuracy());
        double latitude = mLocation.getLatitude();
        double longitude = mLocation.getLongitude();
        accuracyLocation = mLocation.getAccuracy();

        Location location = new Location("");
        location.setLatitude(latitude);
        location.setLongitude(longitude);

        Log.d("save_lat_long.1", "latitude " + latitude);
        Location locationFromSp = Common.getCurrentLocationFromSP(this);
      /*  if (locationFromSp.getLatitude() == 0 && locationFromSp.getLongitude() == 0) {
            Log.d("save_lat_long.2", "latitude " + latitude);
            Common.saveCurrentLocationInSP(this, latitude, longitude, mLocation.getAccuracy(), System.currentTimeMillis(), 15);
        } else {
            float distance = location.distanceTo(locationFromSp);
            float bearingTo = location.bearingTo(locationFromSp);
            float speedFromLastPoint = Common.calculateMaxSpeed(distance, System.currentTimeMillis() - locationFromSp.getTime());
            MAX_SPEED = speedFromLastPoint > locationFromSp.getSpeed() ? speedFromLastPoint : locationFromSp.getSpeed();
            MAX_SPEED = MAX_SPEED > 27 ? 27 : MAX_SPEED;
            Log.d("save_lat_long.3", "latitude " + latitude + "\n distance: " + distance + " bearingTo: " + bearingTo);
            if (distance < DISTANCE || !Common.isSpeedBelow120KPH(distance, System.currentTimeMillis() - locationFromSp.getTime(), MAX_SPEED)) {
                Log.d("save_lat_long.5", "latitude " + latitude);
                return;
            } else {*/
                Log.d("save_lat_long.6", "latitude " + latitude);
                Common.saveCurrentLocationInSP(this, latitude, longitude, mLocation.getAccuracy(), System.currentTimeMillis(), MAX_SPEED);
                int a = Common.haveInternet(this) ? 1 : 0;

                checkinlatlong = String.valueOf(latitude) + "," + String.valueOf(longitude) + "," + accuracyLocation + "," + a + "," + Common.currentTimeFromMS(System.currentTimeMillis());
                //Toast.makeText(getApplicationContext(),checkinlatlong,Toast.LENGTH_LONG).show();
                Calendar c = Calendar.getInstance();
                SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
                String strDate = sdf.format(c.getTime());
                System.out.println("Current time => " + c.getTime());

                SimpleDateFormat df = new SimpleDateFormat(Constants.DateFormat.COMMON_DATE_FORMAT);
                String datefromcalander = df.format(c.getTime());

                /**
                 * CRUD Operations
                 * */
                // Inserting Contacts
                Log.d("Insert: ", "Inserting ..");
               // String customerId = Common.getDataFromSP(this, Constants.CUSTOMER_ID_GEO);

                String selectQueryss = "SELECT  " + KEY_TABLE_GEO_TRACKING_ID + "," + KEY_TABLE_GEO_TRACKING_ROUTE_PATH_LAT_LONG + "," + KEY_TABLE_GEO_TRACKING_FFMID + " FROM " + TABLE_GEO_TRACKING + " where " + KEY_TABLE_GEO_TRACKING_USER_ID + " = " + Common.getUserIdFromSP(this) + " and  visit_date like '" + datefromcalander + "%'"+ " ORDER BY " + KEY_TABLE_GEO_TRACKING_ID + " DESC LIMIT 1 ";
                sdbw = db.getWritableDatabase();

                Cursor ccc = sdbw.rawQuery(selectQueryss, null);
                //System.out.println("cursor count "+cursor.getCount());
                if (ccc != null && ccc.moveToFirst()) {
                    tid = String.valueOf(ccc.getLong(0)); //The 0 is the column index, we only have 1 column, so the index is 0
                    ffmID = ccc.getString(2); //The 0 is the column index, we only have 1 column, so the index is 0
                    existedroutepath = String.valueOf(ccc.getString(1));
                    if (existedroutepath.equalsIgnoreCase("0")) {
                        existedroutepath = checkinlatlong;
                    } else {
                        existedroutepath = existedroutepath + ":" + checkinlatlong;

                    }

                    Log.e("++++ lastId ++++", tid + ": existed route path " + existedroutepath);
                }


                String updatequerys = "UPDATE " + TABLE_GEO_TRACKING + " SET " + KEY_TABLE_GEO_TRACKING_ROUTE_PATH_LAT_LONG + " = '" + existedroutepath + "' WHERE " + KEY_TABLE_GEO_TRACKING_ID + " = " + tid;
                sdbw.execSQL(updatequerys);

                if (Utility.isNetworkAvailable(this, false)) {
                    trackingId = sharedpreferences.getString("tracking_id", null);
                    if (ffmID == null || ffmID == "0" || trackingId == null) {
                        return;
                    } else {
                        new Async_Routepath().execute();
                    }
                }
          // }
      // }
    }


    public class Async_Routepath extends AsyncTask<Void, Void, String> {

        private String jsonData;

        protected String doInBackground(Void... params) {

            try {

                OkHttpClient client = new OkHttpClient();
                 /*For passing parameters*/
                RequestBody formBody = new FormEncodingBuilder()
                        .add("latlon", existedroutepath)
                        .add("tracking_id", trackingId)
                        .build();

                Response responses = null;

                /*MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType,
                        "type=check_in_lat_lon&visit_type=1&user_id=7&latlon=17.4411%2C78.3911&visit_date=2016-12-05%2C18%3A27%3A30&check_in_time=18%3A27%3A30&tracking_id=0");*/
                Request request = new Request.Builder()
                        .url(Constants.URL_NSL_MAIN + Constants.URL_ROUTEPATH_UPDATE_INTERVAL)
                        .post(formBody)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();


                try {
                    responses = client.newCall(request).execute();
                    jsonData = responses.body().string();
                    System.out.println("!!!!!!!1" + jsonData);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return jsonData;
        }


    }


}
