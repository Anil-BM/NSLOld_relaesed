package com.nsl.beejtantra.oreo;

import android.app.IntentService;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Color;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.view.View;

import com.nsl.beejtantra.Constants;
import com.nsl.beejtantra.DatabaseHandler;
import com.nsl.beejtantra.R;
import com.nsl.beejtantra.commonutils.Common;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Calendar;

import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_PLAN_DATE_TIME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_STATUS;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_VISIT_USER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_GEO_TRACKING_CHECK_OUT_LAT_LONG;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_GEO_TRACKING_CHECK_OUT_TIME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_GEO_TRACKING_CREATED_DATETIME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_GEO_TRACKING_DISTANCE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_GEO_TRACKING_FFMID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_GEO_TRACKING_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_GEO_TRACKING_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_GEO_TRACKING_POLYLINE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_GEO_TRACKING_ROUTE_PATH_LAT_LONG;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_GEO_TRACKING_UPDATED_STATUS;
import static com.nsl.beejtantra.DatabaseHandler.METER_READING_CHECKOUT_IMAGE;
import static com.nsl.beejtantra.DatabaseHandler.PAUSE;
import static com.nsl.beejtantra.DatabaseHandler.PERSONAL_USES_KM;
import static com.nsl.beejtantra.DatabaseHandler.RESUME;
import static com.nsl.beejtantra.DatabaseHandler.SYNC_STATUS;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_EMPLOYEE_VISIT_MANAGEMENT;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_GEO_TRACKING;
import static com.nsl.beejtantra.oreo.PlanerOneActivity.mypreference;

/**
 * Created by suprasoft on 12/10/2018.
 */

public class CheckoutService extends IntentService {


    private static final String CHANNEL_ID = "channel_auto_checkout_01";
    private int MID = 10001;
    private String[] data;
    private SharedPreferences sharedpreferences;
    private DatabaseHandler db;
    private NotificationManager mNotificationManager;

    public CheckoutService() {
        super("CheckoutService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {

        sharedpreferences = this.getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        db = new DatabaseHandler(this);

        mNotificationManager = (NotificationManager) getSystemService(NOTIFICATION_SERVICE);

        // Android O requires a Notification Channel.
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            CharSequence name = getString(R.string.app_name);
            // Create the channel for the notification
            NotificationChannel mChannel =
                    new NotificationChannel(CHANNEL_ID, name, NotificationManager.IMPORTANCE_DEFAULT);
            mChannel.setDescription("no sound");
            mChannel.setSound(null, null);
            mChannel.enableLights(false);
            mChannel.setLightColor(Color.BLUE);
            mChannel.enableVibration(false);

            // Set the Notification Channel for the Notification Manager.
            mNotificationManager.createNotificationChannel(mChannel);
        }


        long when = System.currentTimeMillis();

        Intent notificationIntent = new Intent();
        notificationIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0,
                notificationIntent, PendingIntent.FLAG_UPDATE_CURRENT);


        Uri alarmSound = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);

        NotificationCompat.Builder mNotifyBuilder = new NotificationCompat.Builder(
                this).setSmallIcon(R.mipmap.ic_launcher)
                .setContentTitle("Service Auto Fired")
                .setContentText("Gps tracking stopped before 11:59 PM").setSound(alarmSound)
                .setAutoCancel(true).setWhen(when)
                .setContentIntent(pendingIntent)
                .setVibrate(new long[]{1000, 1000, 1000, 1000, 1000});

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            mNotifyBuilder.setChannelId(CHANNEL_ID); // Channel ID
        }

        data = isCheckedIn(this);
        if (data != null && data.length > 0) {
            mNotificationManager.notify(MID, mNotifyBuilder.build());
            if (Common.haveInternet(this))
            new Async_Checkout(this).execute(data);
            else{
                ContentValues contentValues = new ContentValues();
                contentValues.put(SYNC_STATUS, 0);
                new DatabaseHandler(this).updateTable(TABLE_GEO_TRACKING, contentValues, KEY_TABLE_GEO_TRACKING_FFMID, data[0]);

            }

        }

    }

    @Override
    public void onCreate() {
        super.onCreate();
    }

    private class Async_Checkout extends AsyncTask<String[], Void, String> {

        final MediaType MEDIA_TYPE = MediaType.parse("image/jpg");
        Context context;
        private String jsonData;
        private String strDate;
        private DatabaseHandler db;

        private Async_Checkout(Context context) {
            this.context = context;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressDialog = new ProgressDialog(PlanerOneActivity.this);
            // progressDialog.setMessage("Submitting Check out");
            // progressDialog.show();
        }

        protected String doInBackground(String[]... params) {


            db = new DatabaseHandler(context);

            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdftime = new SimpleDateFormat("HH:mm:ss");
            strDate = sdftime.format(c.getTime());
            Log.e("Check_out_time1", strDate);


            try {
                String[] path = params[0][1].split(":");
                final String checkoutLatLng = path[path.length - 1];
                OkHttpClient client = new OkHttpClient();
                 /*For passing parameters*/
                MultipartBuilder multipartBuilder = new MultipartBuilder();
                multipartBuilder.type(MultipartBuilder.FORM);


                multipartBuilder.addFormDataPart("type", "check_out_lat_lon");
                multipartBuilder.addFormDataPart("latlon", checkoutLatLng);
                multipartBuilder.addFormDataPart("check_out_time", strDate);
                multipartBuilder.addFormDataPart("tracking_id", params[0][0]);
                multipartBuilder.addFormDataPart("user_id", params[0][4]);
                //  multipartBuilder.addFormDataPart("distance", params[1]);
//                multipartBuilder.addFormDataPart("route_snap", String.valueOf(snapRoadCallsSuccess));
//                multipartBuilder.addFormDataPart("route_snap_all", String.valueOf(snapRoadCallsAll));
//                multipartBuilder.addFormDataPart("route_snap_failure", String.valueOf(snapRoadCallsFailure));
//                multipartBuilder.addFormDataPart("google_direction", String.valueOf(directionServiceCallsSuccess));
//                multipartBuilder.addFormDataPart("google_direction_all", String.valueOf(directionServiceCallsAll));
//                multipartBuilder.addFormDataPart("google_direction_failure", String.valueOf(directionServiceCallsFailure));
                multipartBuilder.addFormDataPart("pause", Common.isStringNull(params[0][2]));
                multipartBuilder.addFormDataPart("resume", Common.isStringNull(params[0][3]));
                multipartBuilder.addFormDataPart("route_path_lat_lon", params[0][1]);
                // multipartBuilder.addFormDataPart("polyline", params[0]);
                multipartBuilder.addFormDataPart("check_out_by", "5");
               /* if (starting_kms_img.getVisibility() == View.VISIBLE) {
                    multipartBuilder.addFormDataPart("meter_reading_checkout_image", outputFileKm.getAbsolutePath(), RequestBody.create(MEDIA_TYPE, outputFileKm));
                    multipartBuilder.addFormDataPart("meter_reading_checkout_text", meter_reading_checkin_text);

                }

                if (checkInComment != null) {
                    if (tv_comment_hint.getText().toString() != null && tv_comment_hint.getText().toString().trim().equalsIgnoreCase("Personal Uses KM")) {
                        multipartBuilder.addFormDataPart("personal_uses_km", checkInComment);
                    } else {

                        multipartBuilder.addFormDataPart("checkin_comment", checkInComment);
                    }

                }*/
                RequestBody formBody = multipartBuilder.build();
                Response responses = null;

              //  Log.d("body", "body:  " + Common.bodyToString(formBody));


                /*MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType,
                        "type=check_in_lat_lon&visit_type=1&user_id=7&latlon=17.4411%2C78.3911&visit_date=2016-12-05%2C18%3A27%3A30&check_in_time=18%3A27%3A30&tracking_id=0");*/
                Request request = new Request.Builder()
                        .url(Constants.URL_NSL_MAIN + Constants.URL_CHECKINOUT)
                        .post(formBody)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        // .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    public SQLiteDatabase sdbw;

                    @Override
                    public void onFailure(Request request, IOException e) {
                       /* if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            mService.cancelAlarm();
                        }
                        mService.removeLocationUpdates();
                        Common.dismissProgressDialog(progressDialogInOut);*/

                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        jsonData = response.body().string();
                        System.out.println("!!!!!!!1 checkout" + jsonData);
                        if (jsonData != null) {
                            try {
                                //deleteFile();
                                // {"status":"success","msg":"successfully! done","tracking_id":"11723"}
                                JSONObject jsonObject = new JSONObject(jsonData);
                                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                                    String polyline = jsonObject.getString("polyline");
                                    String distance = jsonObject.getString("distance");
                                    String meter_reading_checkout_image = jsonObject.getString("meter_reading_checkout_image");
                                    String tracking_id = jsonObject.getString("tracking_id");
                                    String personal_uses_km = jsonObject.getString("personal_uses_km");
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.putString("tracking_id", "");
                                    editor.putString("checkinlatlong", "");
                                    editor.putString("checkin", "false");
                                    editor.commit();

                                    sdbw = db.getWritableDatabase();
                                    String updatequerys = "UPDATE " + TABLE_EMPLOYEE_VISIT_MANAGEMENT + " SET " + KEY_EMP_STATUS + " = '" + "4" + "' WHERE " + KEY_EMP_VISIT_USER_ID + " = " + Common.getUserIdFromSP(CheckoutService.this) + " and " + KEY_EMP_PLAN_DATE_TIME + " like '" + Common.getCurrentDateYYYYMMDD() + "%'";
                                    sdbw.execSQL(updatequerys);

                                    String updatequery1 = "UPDATE " + TABLE_GEO_TRACKING + " SET " + KEY_TABLE_GEO_TRACKING_UPDATED_STATUS + " = 1 ," + KEY_TABLE_GEO_TRACKING_CHECK_OUT_LAT_LONG + " = '" + checkoutLatLng + "'," + KEY_TABLE_GEO_TRACKING_CHECK_OUT_TIME + " = '" + strDate + "'," + KEY_TABLE_GEO_TRACKING_POLYLINE + " = '" + polyline + "'," + KEY_TABLE_GEO_TRACKING_DISTANCE + " = '" + distance + "'," + METER_READING_CHECKOUT_IMAGE + " = '" + meter_reading_checkout_image + "'," + PERSONAL_USES_KM + " = '" + personal_uses_km + "' WHERE " + KEY_TABLE_GEO_TRACKING_MASTER_ID + " = " + tracking_id;

                                    sdbw.execSQL(updatequery1);

                                    new AsyncCheckoutAcknowledge(CheckoutService.this, tracking_id).execute();

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                if (sdbw.isOpen())
                                    sdbw.close();
                            }
                        }
                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }

            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


           /* Intent intent1 = new Intent(PlanerOneActivity.this, LocationService.class);
            stopService(intent1);
            Log.d("onDestroy", "onDestroy 2.......................");*/


        }
    }

    public static String[] isCheckedIn(Context context) {
        String[] id = new String[5];

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String strDate = sdf.format(c.getTime());
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat(Constants.DateFormat.COMMON_DATE_FORMAT);
        String datefromcalander = df.format(c.getTime());
        String selectQueryss = "SELECT  " + KEY_TABLE_GEO_TRACKING_ID + "," + KEY_TABLE_GEO_TRACKING_ROUTE_PATH_LAT_LONG + "," + KEY_TABLE_GEO_TRACKING_FFMID + "," + KEY_TABLE_GEO_TRACKING_CHECK_OUT_TIME + "," + KEY_TABLE_GEO_TRACKING_CREATED_DATETIME + "," + PAUSE + "," + RESUME + "," + KEY_EMP_VISIT_USER_ID + " FROM " + TABLE_GEO_TRACKING + " where " + " visit_date like '" + datefromcalander + "%' and user_id ='" + Common.getUserIdFromSP(context) + "'" + " ORDER BY " + KEY_TABLE_GEO_TRACKING_ID + " DESC LIMIT 1 ";
        SQLiteDatabase sdbw1 = new DatabaseHandler(context).getWritableDatabase();
        try {

            Cursor ccc = sdbw1.rawQuery(selectQueryss, null);
            System.out.println("cursor count " + ccc.getCount() + "\n" + selectQueryss);
            if (ccc != null && ccc.moveToFirst()) {
                if ((ccc.getString(3) == null || ccc.getString(3).equalsIgnoreCase("") || ccc.getString(3).equalsIgnoreCase("null")) && (ccc.getString(4) != null && ccc.getString(4).length() > 5)) {
                    id[0] = ccc.getString(2);
                    id[1] = ccc.getString(1);
                    id[2] = ccc.getString(5);
                    id[3] = ccc.getString(6);
                    id[4] = ccc.getString(7);
                    return id;
                }
            }
        } catch (Exception e) {

        } finally {
           /* if (sdbw1!=sdbw1sdbw1.isOpen())
                sdbw1.close();*/
            Common.Log.i("DB Closed: " + "finally called");
        }
        return null;
    }

    public class AsyncCheckoutAcknowledge extends AsyncTask<Void, Void, String> {

        private final Context context;
        private final String trackingId;
        private String path;
        private String jsonData;

        public AsyncCheckoutAcknowledge(Context context, String id) {
            this.context = context;
            this.trackingId = id;

            // this.path = path;
        }

        protected String doInBackground(Void... params) {

            try {

                OkHttpClient client = new OkHttpClient();
                 /*For passing parameters*/
                RequestBody formBody = new FormEncodingBuilder()
                        //.add("latlon", path)
                        .add("tracking_id", trackingId)
                        .build();

                Response responses = null;
                Log.d("AsyncCheckout", "AsyncCheckoutAcknowledge..");
                /*MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType,
                        "type=check_in_lat_lon&visit_type=1&user_id=7&latlon=17.4411%2C78.3911&visit_date=2016-12-05%2C18%3A27%3A30&check_in_time=18%3A27%3A30&tracking_id=0");*/
                Request request = new Request.Builder()
                        .url(Constants.URL_NSL_MAIN + Constants.URL_GEO_POLYLINE)
                        .post(formBody)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();


                client.newCall(request).enqueue(new Callback() {
                    public SQLiteDatabase sdbw;


                    @Override
                    public void onFailure(Request request, IOException e) {

                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        jsonData = response.body().string();
                        System.out.println("!!!!!!!1 URL_GEO_POLYLINE : " + jsonData);

                        if (jsonData != null) {
                            try {
                                //deleteFile();
                                // {"status":"success","msg":"successfully! done","tracking_id":"11723"}
                                JSONObject jsonObject = new JSONObject(jsonData);
                                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                                    String polyline = jsonObject.getString("polyline");
                                    String distance = jsonObject.getString("distance");
                                    String meter_reading_checkout_image = jsonObject.getString("meter_reading_checkout_image");
                                    String tracking_id = jsonObject.getString("tracking_id");
                                    String personal_uses_km = jsonObject.getString("personal_uses_km");
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.putString("tracking_id", "");
                                    editor.putString("checkinlatlong", "");
                                    editor.putString("checkin", "false");
                                    editor.commit();
                                    sdbw = db.getWritableDatabase();
                                    String updatequerys = "UPDATE " + TABLE_EMPLOYEE_VISIT_MANAGEMENT + " SET " + KEY_EMP_STATUS + " = '" + "4" + "' WHERE " + KEY_EMP_VISIT_USER_ID + " = " + Common.getUserIdFromSP(CheckoutService.this) + " and " + KEY_EMP_PLAN_DATE_TIME + " like '" + Common.getCurrentDateYYYYMMDD() + "%'";
                                    sdbw.execSQL(updatequerys);

                                    String updatequery1 = "UPDATE " + TABLE_GEO_TRACKING + " SET " + KEY_TABLE_GEO_TRACKING_UPDATED_STATUS + " = 1 ," + KEY_TABLE_GEO_TRACKING_POLYLINE + " = '" + polyline + "'," + KEY_TABLE_GEO_TRACKING_DISTANCE + " = '" + distance + "'," + METER_READING_CHECKOUT_IMAGE + " = '" + meter_reading_checkout_image + "'," + PERSONAL_USES_KM + " = '" + personal_uses_km + "'," + SYNC_STATUS + " = 1 WHERE " + KEY_TABLE_GEO_TRACKING_MASTER_ID + " = " + tracking_id;

                                    sdbw.execSQL(updatequery1);


                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                if (sdbw.isOpen())
                                    sdbw.close();
                            }
                        }

                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }

            return jsonData;
        }


    }
}
