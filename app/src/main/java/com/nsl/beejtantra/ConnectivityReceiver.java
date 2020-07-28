package com.nsl.beejtantra;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.media.MediaDrm;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.nsl.beejtantra.commonutils.Common;
import com.nsl.beejtantra.commonutils.CustomerAlertDialog3;
import com.nsl.beejtantra.commonutils.DialogClickListener;
import com.nsl.beejtantra.scheduler.DailySchedulerReceiver;
import com.nsl.beejtantra.scheduler.SchedulingService;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.UUID;

import static android.content.Intent.FLAG_ACTIVITY_NEW_TASK;
import static com.nsl.beejtantra.MainActivity.DATABASE_NAME;

public class ConnectivityReceiver extends BroadcastReceiver {

    public static ConnectivityReceiverListener connectivityReceiverListener;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    String userId;
    private Context context;
    private String android_id;
    boolean isConnected;
    private static final String DATABASE_NAME = "NSL.db";
    private String currentVersion;

    public ConnectivityReceiver() {
        super();
    }

    @Override
    public void onReceive(Context context, Intent arg1) {
        this.context = context;
        Log.d("ConnectivityManager", String.valueOf("ConnectivityManager..."));
        ConnectivityManager cm = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        isConnected = activeNetwork != null && activeNetwork.isConnected();
        sharedpreferences = context.getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        if(Build.VERSION.SDK_INT<=28) {
            android_id = Settings.Secure.getString(context.getContentResolver(),
                    Settings.Secure.ANDROID_ID);
        }
        if(Build.VERSION.SDK_INT>=29) {
            UUID wideVineUuid = new UUID(-0x121074568629b532L, -0x5c37d8232ae2de13L);
            try {
                MediaDrm wvDrm = new MediaDrm(wideVineUuid);
                byte[] wideVineId = wvDrm.getPropertyByteArray(MediaDrm.PROPERTY_DEVICE_UNIQUE_ID);
                android_id=getshaoneofstring(Arrays.toString(wideVineId));
                Log.d("hi",android_id);
            } catch (Exception e) {
                // Inspect exception

            }
        }


        if (connectivityReceiverListener != null) {
            connectivityReceiverListener.onNetworkConnectionChanged(isConnected);
        }
        if (isConnected) {
            Intent intent = new Intent("custom-message");
            LocalBroadcastManager.getInstance(MyApplication.getInstance()).sendBroadcast(intent);

            userId = sharedpreferences.getString("userId", "");
            if (userId != "") {
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        new Async_IsAppLoggedIn().execute();
                    }
                }, 4000);


                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        getCurrentVersion();
                    }
                }, 7000);
            }

            //  Toast.makeText(context, "Network Available Do operations",Toast.LENGTH_LONG).show();
        } else {
            Log.d("ConnectivityManager", String.valueOf(isConnected));
            sharedpreferences.edit().putLong(Constants.SharedPrefrancesKey.NETWORK_CHANGED_LAST_TIME, 0).commit();
            SharedPreferences.Editor editor = sharedpreferences.edit();
            editor.putString("sync_offline_coordinates", "true");
            editor.commit();
        }


    }

    public static boolean isConnected() {
        ConnectivityManager
                cm = (ConnectivityManager) MyApplication.getInstance().getApplicationContext()
                .getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    }


    public interface ConnectivityReceiverListener {
        void onNetworkConnectionChanged(boolean isConnected);
    }


    private class Async_IsAppLoggedIn extends AsyncTask<Void, Void, String> {
        String jsonData;
        String status;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        protected String doInBackground(Void... params) {


            try {
                OkHttpClient client = new OkHttpClient();
                 /*For passing parameters*/

                Response responses = null;

                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType, "user_id=" + userId + "&imei_number=" + android_id);
                Request request = new Request.Builder()
                        .url(Constants.URL_APP_LOGGED_IN)
                        .post(body)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();

                try {
                    responses = client.newCall(request).execute();
                    jsonData = responses.body().string();
                    System.out.println("!!!!!!!1login" + jsonData);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (s != null) {
                try {
                    JSONObject jsonobject = new JSONObject(s);
                    status = jsonobject.getString("status");
                    if (status.equalsIgnoreCase("true")) {
                        if (sharedpreferences.contains(Constants.SharedPrefrancesKey.NETWORK_CHANGED_LAST_TIME) && (System.currentTimeMillis() - sharedpreferences.getLong(Constants.SharedPrefrancesKey.NETWORK_CHANGED_LAST_TIME, 0)) > 60000) {

                            Log.d("ConnectivityManager", String.valueOf(isConnected));
                            Intent backgroundIntent = new Intent(context, BackgroundPushService.class);
                            Intent syncData = new Intent(context, SchedulingService.class);
                            syncData.putExtra("scheduler.connectivity_change",true);


                            /*if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                ContextCompat.startForegroundService(context,backgroundIntent);
                                context.startForegroundService(syncData);
                            }else {*/
                            try{
                                context.startService(backgroundIntent);
                                context.startService(syncData);
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                           // }



                            } else if (!sharedpreferences.contains(Constants.SharedPrefrancesKey.NETWORK_CHANGED_LAST_TIME)) {

                                Log.d("ConnectivityManager", String.valueOf(isConnected));
                                Intent backgroundIntent = new Intent(context, BackgroundPushService.class);
                            Intent syncData = new Intent(context, SchedulingService.class);
                            syncData.putExtra("scheduler.connectivity_change",true);

                          /*  if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
                                ContextCompat.startForegroundService(context,backgroundIntent);
                                context.startForegroundService(syncData);
                            }else {*/
                          try{
                                context.startService(backgroundIntent);
                                context.startService(syncData);
                          }catch (Exception e){
                              e.printStackTrace();
                          }
                           // }



                            }
                            sharedpreferences.edit().putLong(Constants.SharedPrefrancesKey.NETWORK_CHANGED_LAST_TIME, System.currentTimeMillis()).commit();


                    } else {
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("Divisions", "false");
                        editor.putString("userId", "");
                        editor.putInt(Constants.SharedPrefrancesKey.ROLE, 0);
                        editor.putString("fcm_id", "");
                        editor.commit();
                        context.deleteDatabase(DATABASE_NAME);
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");

            }


        }
    }

    private void getCurrentVersion() {


        PackageManager pm = context.getPackageManager();

        PackageInfo pInfo = null;

        try {
            pInfo = pm.getPackageInfo(context.getPackageName(), 0);

        } catch (PackageManager.NameNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        currentVersion = pInfo.versionName;

        System.out.println("? - My local app version====>>>>>" + currentVersion);


        new GetAppCurrentVersion().execute();


    }

    private class GetLatestVersion extends AsyncTask<String, String, JSONObject> {


        private String latestVersion;
        private CustomerAlertDialog3 dailog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected JSONObject doInBackground(String... params) {
            try {
                latestVersion = Jsoup.connect("https://play.google.com/store/apps/details?id=com.nsl.beejtantra")
                        .timeout(30 * 1000)
                        .userAgent("Mozilla/5.0 (Windows; U; WindowsNT 5.1; en-US; rv1.8.1.6) Gecko/20070725 Firefox/2.0.0.6")
                        .referrer("http://www.google.com")
                        .get()
                        .select(".hAyfc .htlgb")
                        .get(3)
                        .ownText();

//                org.jsoup.nodes.Document doc = Jsoup.connect("https://play.google.com/store/apps/details?id=com.nsl.beejtantra").timeout(20 * 1000).get();
//                latestVersion = doc.getElementsByAttributeValue("itemprop", "softwareVersion").first().text();
                System.out.println("? - Latest version of my app from playstore====>>>>" + latestVersion);
            } catch (Exception e) {
                e.printStackTrace();

            }

            return new JSONObject();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (latestVersion != null) {

                if (!currentVersion.equalsIgnoreCase(latestVersion)) {
                    Common.Log.i("Common.versionCompare(currentVersion,latestVersion) : " + Common.versionCompare(currentVersion, latestVersion));

                    // if (Common.versionCompare(currentVersion,latestVersion) < 0)

                    dailog = new CustomerAlertDialog3(context, "App Update!", "Please update your app", "Update", new DialogClickListener() {
                        @Override
                        public void onCancelClick() {

                        }

                        @Override
                        public void onOkClick() {
                            Common.dismissDialog(dailog);
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.nsl.beejtantra"));
                            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);

                        }
                    });
//                    dailog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
//                    dailog.setCancelable(false);
                    if (!Common.isAppIsInBackground(context)) {
                        dailog.show();
                    }
                }


            }
            super.onPostExecute(jsonObject);


        }
    }


    private class GetAppCurrentVersion extends AsyncTask<Void, Void, String> {

        private String latestVersion;
        private String jsonData;
        private CustomerAlertDialog3 dailog;
        private String user_status="1";
        private String URL;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //bar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(Void... params) {

            //odb.delete(TABLE_DIVISION,null,null);

            try {
                OkHttpClient client = new OkHttpClient();


                Response responses = null;
                URL= Constants.URL_NSL_MAIN + Constants.APP_CURRENT_VERSION;
                if(Common.getUserIdFromSP(context)!=null && !Common.getUserIdFromSP(context).equalsIgnoreCase("")){
                    URL= Constants.URL_NSL_MAIN + Constants.APP_CURRENT_VERSION+"?user_id="+Common.getUserIdFromSP(context);
                }

                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

                Request request = new Request.Builder()
                        .url(URL)
                        .get()
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
            if (jsonData != null) {
                try {

                    JSONObject object = new JSONObject(jsonData);
                    JSONObject objApp_version = object.getJSONObject("app_version");
                    String app_version_id = objApp_version.getString("app_version_id");
                    latestVersion = objApp_version.getString("app_version_name");
                    user_status = objApp_version.getString("user_status");


                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");

            }
            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {

            if (user_status!=null && user_status.equalsIgnoreCase("0") && Common.getUserIdFromSP(context)!=null && !Common.getUserIdFromSP(context).equalsIgnoreCase("")){
                logoutFunction(context);
                return;
            }

            if (latestVersion != null) {

                if (!currentVersion.equalsIgnoreCase(latestVersion)) {
                    Common.Log.i("Common.versionCompare(currentVersion,latestVersion) : " + Common.versionCompare(currentVersion, latestVersion));

                    dailog = new CustomerAlertDialog3(context, "App Update!", "Please update your app", "Update", new DialogClickListener() {
                        @Override
                        public void onCancelClick() {

                        }

                        @Override
                        public void onOkClick() {
                            Common.dismissDialog(dailog);
                            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.nsl.beejtantra"));
                            intent.addFlags(FLAG_ACTIVITY_NEW_TASK);
                            context.startActivity(intent);

                        }
                    });
//                    dailog.getWindow().setType(WindowManager.LayoutParams.TYPE_TOAST);
//                    dailog.setCancelable(false);
                    if (!Common.isAppIsInBackground(context)) {
                        dailog.show();
                    }


                }
            }
            super.onPostExecute(s);
        }
    }

    public void logoutFunction(Context context) {


        SharedPreferences.Editor editor = sharedpreferences.edit();
        editor.putString("Divisions", "false");
        editor.putString("userId", "");
        editor.putInt(Constants.SharedPrefrancesKey.ROLE, 0);
        editor.putString("fcm_id", "");
        editor.commit();
        // dialog.dismiss();
        context.deleteDatabase(DATABASE_NAME);
        //  Common.dismissDialog(syncDialog);

        System.exit(0);

    }
    private String getshaoneofstring(String data) {

        try
        {
            byte[] b = data.getBytes();
            MessageDigest md = MessageDigest.getInstance("SHA-1");
            md.reset();
            md.update(b);
            byte messageDigest[] = md.digest();
            StringBuilder result = new StringBuilder();
            for (int i = 0; i < messageDigest.length; i++)
            {
                result.append(Integer.toString((messageDigest[i] & 0xff) + 0x100, 16).substring(1));
            }

            return result.toString();

        } catch (NoSuchAlgorithmException e)
        {

            //  Log.e("ARTags", "SHA1 is not a supported algorithm");
        }
        return null;

    }
}