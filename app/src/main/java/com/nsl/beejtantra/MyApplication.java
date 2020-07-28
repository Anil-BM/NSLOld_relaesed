package com.nsl.beejtantra;

import android.app.Activity;
import android.app.AlarmManager;
import android.app.Application;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.os.PowerManager;
import android.os.StrictMode;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.multidex.MultiDex;

import com.crashlytics.android.Crashlytics;
import com.facebook.stetho.Stetho;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.places.Places;
import com.nsl.beejtantra.commonutils.Common;
import com.nsl.beejtantra.network.BasicAuthInterceptor;
import com.nsl.beejtantra.network.RetrofitAPI;
import com.nsl.beejtantra.oreo.DeleteOldRecordsAlarmReceiver;
import com.nsl.beejtantra.oreo.FixCursorWindow;
import com.nsl.beejtantra.scheduler.DailySchedulerReceiver;
import com.squareup.okhttp.OkHttpClient;

import io.fabric.sdk.android.Fabric;

import java.util.concurrent.TimeUnit;

import okhttp3.logging.HttpLoggingInterceptor;
import retrofit.RestAdapter;
import retrofit.android.AndroidLog;
import retrofit.client.OkClient;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;


public class MyApplication extends Application implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {

    private static MyApplication mInstance;
    private static final long INTERVAL = 1000 * 5;
    private static final long FASTEST_INTERVAL = 1000 * 3;
    LocationRequest mLocationRequest;
    GoogleApiClient mGoogleApiClient;
    private RetrofitAPI retrofitAPI;

    protected void createLocationRequest() {
        mLocationRequest = new LocationRequest();
        mLocationRequest.setInterval(INTERVAL);
        mLocationRequest.setFastestInterval(FASTEST_INTERVAL);
        mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
    }

    DailySchedulerReceiver alarm = new DailySchedulerReceiver();

    @Override
    public void onCreate() {
        super.onCreate();
        Fabric.with(this, new Crashlytics());
        // Sherlock.init(this);
       /* Intent intent=new Intent(this,MyAccessibilityService.class);
        startService(intent);*/
        ConnectivityReceiver connectivityReceiver = new ConnectivityReceiver();
        IntentFilter intentFilter = new IntentFilter("android.net.conn.CONNECTIVITY_CHANGE");
        registerReceiver(connectivityReceiver, intentFilter);

        mInstance = this;
        Stetho.initializeWithDefaults(this);
        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        createLocationRequest();
        //   alarm.setAlarm(this);

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(LocationServices.API)
                .addApi(Places.GEO_DATA_API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();

        mGoogleApiClient.connect();


//        OkHttpClient okHttpClient = new OkHttpClient();
//        okHttpClient.setConnectTimeout(Constants.CONNECTION_TIME_OUT, TimeUnit.SECONDS);
//        okHttpClient.setReadTimeout(Constants.READ_TIME_OUT, TimeUnit.SECONDS);
//        okHttpClient.setRetryOnConnectionFailure(true);
//        okHttpClient.setWriteTimeout(Constants.WRITE_TIME_OUT, TimeUnit.SECONDS);

//        RestAdapter restAdapter = new RestAdapter.Builder()
//                .setClient(new OkClient(okHttpClient))
//                .setEndpoint(Constants.BASE_URL)
//                .setLogLevel(RestAdapter.LogLevel.FULL)
//                .setLog(new AndroidLog("NSL"))
//                .build();

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okhttp3.OkHttpClient client = new okhttp3.OkHttpClient.Builder().addInterceptor(interceptor)
                .addInterceptor(new BasicAuthInterceptor("rest", "seeds@admin"))
                .connectTimeout(Constants.CONNECTION_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(Constants.WRITE_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(Constants.READ_TIME_OUT, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.URL_NSL_MAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        retrofitAPI = retrofit.create(RetrofitAPI.class);


//        retrofitAPI = restAdapter.create(RetrofitAPI.class);
        DeleteOldRecordsAlarmReceiver.setAlarm(false, this);
        // FixCursorWindow.fix();
        registerActivityLifecycleCallbacks(new ActivityLifecycleCallbacks() {
            @Override
            public void onActivityCreated(Activity activity, Bundle savedInstanceState) {
                activity.setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_PORTRAIT);
            }

            @Override
            public void onActivityStarted(Activity activity) {

            }

            @Override
            public void onActivityResumed(Activity activity) {

            }

            @Override
            public void onActivityPaused(Activity activity) {

            }

            @Override
            public void onActivityStopped(Activity activity) {

            }

            @Override
            public void onActivitySaveInstanceState(Activity activity, Bundle outState) {

            }

            @Override
            public void onActivityDestroyed(Activity activity) {

            }
        });
/*
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread t, Throwable e) {
                Common.Log.i("MyApplication : "+e.toString());
               // turnOnScreen(getApplicationContext());
               // restartApp(e);
            }
        });*/
    }


    private void restartApp(Throwable e) {
        e.printStackTrace();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        /*int mPendingIntentId = 11;
        PendingIntent mPendingIntent = PendingIntent.getActivity(getApplicationContext(), mPendingIntentId, intent, PendingIntent.FLAG_CANCEL_CURRENT);
        AlarmManager mgr = (AlarmManager) getApplicationContext().getSystemService(Context.ALARM_SERVICE);
        mgr.set(AlarmManager.RTC, System.currentTimeMillis() + 100, mPendingIntent);*/
        // Intent intent = new Intent ();
        intent.setAction("com.mydomain.SEND_LOG"); // see step 5.
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_NEW_TASK); // required when starting from Application
        startActivity(intent);
        System.exit(1); // kill off the crashed app
    }

    private void turnOnScreen(Context context) {
        PowerManager.WakeLock screenLock = null;
        if ((context.getSystemService(POWER_SERVICE)) != null) {
            screenLock = ((PowerManager) context.getSystemService(POWER_SERVICE)).newWakeLock(
                    PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
            screenLock.acquire(20 * 1000 /*10 minutes*/);


            //  screenLock.release();
        }
    }

    public static MyApplication getInstance() {
        return mInstance;
    }

    public GoogleApiClient getGoogleApiClient() {
        return mGoogleApiClient;
    }

    public LocationRequest getLocationRequest() {

        return mLocationRequest;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    public RetrofitAPI getRetrofitAPI() {
        return retrofitAPI;
    }

    @Override
    public void onConnected(@Nullable Bundle bundle) {

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

    }

    @Override
    public void onTerminate() {
        Common.Log.i("MyApplication terminated");
        //   Common.writeFile("MyApplication terminated");
        super.onTerminate();

    }

    @Override
    public void onLowMemory() {
        Common.Log.i("MyApplication onLowMemory()");
        //   Common.writeFile("MyApplication onLowMemory()");
        super.onLowMemory();
    }

    @Override
    public void onTrimMemory(int level) {
        Common.Log.i("MyApplication onTrimMemory() " + level);
        super.onTrimMemory(level);
    }


}