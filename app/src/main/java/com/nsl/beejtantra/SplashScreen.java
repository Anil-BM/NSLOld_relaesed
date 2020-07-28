package com.nsl.beejtantra;


import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.net.ConnectivityManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.util.TimeUtils;
import android.view.Gravity;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.gson.Gson;
import com.nsl.beejtantra.commonutils.Common;
import com.nsl.beejtantra.network.RetrofitAPI;
import com.nsl.beejtantra.pojo.ComplaintReq;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.apache.commons.lang3.time.DurationFormatUtils;
import org.json.JSONObject;
import org.jsoup.Jsoup;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.TimeUnit;

import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.nsl.beejtantra.MainActivity.DATABASE_NAME;

/**
 * An example full-screen activity that shows and hides the system UI (i.e.
 * status bar and navigation/system bar) with user interaction.
 */
public class SplashScreen extends Activity {

    // Splash screen timer
    private static int SPLASH_TIME_OUT = 3000;
    String userrId, jsonData;
    DatabaseHandler db;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    ProgressDialog progressDialog;
    String fcm_id;
    private String currentVersion;
    private AlertDialog dialog;
    String json="{\"user_id\":\"802\",\n" +
            "\"company_id\":\"1\",\n" +
            "\"division_id\":\"1\",\n" +
            "\"crop_id\":\"1\",\n" +
            "\"crop_id\":\"1\",\n" +
            "\"product_id\":\"1\",\n" +
            "\"marketing_lot_number\":\"12345\",\n" +
            "\"complaint_type\":\"others\",\n" +
            "\"complaint_percentage\":\"\",\n" +
            "\"complaint_remarks\":\"others\",\n" +
            "\"farmer_name\":\"Narasimha\",\n" +
            "\"contact_no\":\"9885778934\",\n" +
            "\"complaint_area_acres\":\"1\",\n" +
            "\"soil_type\":\"Rainfed\",\n" +
            "\"others\":\"\",\n" +
            "\"purchased_quantity\":\"10\",\n" +
            "\"complaint_quantity\":\"2\",\n" +
            "\"purchase_date\":\"2019-01-01\",\n" +
            "\"bill_number\":\"123456\",\n" +
            "\"retailer_name\":\"Retailer\",\n" +
            "\"distributor\":\"1\",\n" +
            "\"mandal\":\"IBM\",\n" +
            "\"zone\":\"South Zone\",\n" +
            "\"region\":\"AP\",\n" +
            "\"village\":\"Mulapadu\",\n" +
            "\"dealer\":\"Deal\",\n" +
            "\"regulatory_type\":\"\",\n" +
            "\"date_of_sowing\":\"2019-01-10\",\n" +
            "\"sampling_date\":\"2019-02-11\",\n" +
            "\"performance_on_other_fields\":\"Good\",\n" +
            "\"place_sampling\":\"Mulapadu\",\n" +
            "\"sampling_officer_name\":\"Narasimha\",\n" +
            "\"sampling_officer_contact\":\"9885778934\",\n" +
            "\"comments\":\"\",\n" +
            "\"remarks\":\"\",\n" +
            "\"status\":\"0\",\n" +
            "\"created_datetime\":\"\",\n" +
            "\"updated_datetime\":\"\",\n" +
            "\"state\":\"AP\",\n" +
            "\"inspected_date\":\"2019-02-11\",\n" +
            "\"date_of_complaint\":\"2019-02-11\"\n" +
            "}";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_fullscreen);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        userrId = sharedpreferences.getString("userId", "");
        SharedPreferences.Editor editor = sharedpreferences.edit();
        new DatabaseHandler(this);

        fcm_id = FirebaseInstanceId.getInstance().getToken();
        editor.putString("fcm_id", fcm_id);
        editor.commit();

        ImageView splashText = (ImageView) findViewById(R.id.splashtext);

        if (Build.VERSION.SDK_INT >= 21) {
            //getWindow().setNavigationBarColor(getResources().getColor(R.color.Theme_Dark_primary));
            getWindow().setStatusBarColor(getResources().getColor(R.color.white));
        }

        db = new DatabaseHandler(this);
        db.getWritableDatabase();


        getCurrentVersion();

     /*   new Handler().postDelayed(new Runnable() {

            @Override
            public void run() {
                // This method will be executed once the timer is over
                // Start your app main activity
                Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(i);
                finish();

            }
        }, SPLASH_TIME_OUT);*/

        /*HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okhttp3.OkHttpClient client = new okhttp3.OkHttpClient.Builder().addInterceptor(interceptor)
                //.addInterceptor(new BasicAuthInterceptor("rest", "umdaa@admin"))
                .connectTimeout(Constants.CONNECTION_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(Constants.WRITE_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(Constants.READ_TIME_OUT, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.URL_NSL_MAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        RetrofitAPI apiInterface = retrofit.create(RetrofitAPI.class);

        ComplaintReq comp = Common.getSpecificDataObject(json, ComplaintReq.class);
        apiInterface.uploadProfile("complaint",null,comp).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {

            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });*/

    }

    private void getCurrentVersion() {


        PackageManager pm = this.getPackageManager();

        PackageInfo pInfo = null;

        try {
            pInfo = pm.getPackageInfo(this.getPackageName(), 0);

        } catch (PackageManager.NameNotFoundException e1) {
            // TODO Auto-generated catch block
            e1.printStackTrace();
        }

        currentVersion = pInfo.versionName;

        System.out.println("? - My local app version====>>>>>" + currentVersion);

//        Common.customToast(this,"currentVersion : "+currentVersion);


        if (!Common.haveInternet(getApplicationContext())) {
            new Handler().postDelayed(new Runnable() {

                @Override
                public void run() {
                    // This method will be executed once the timer is over
                    // Start your app main activity
                    Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                    startActivity(i);
                    finish();

                }
            }, SPLASH_TIME_OUT);


        } else {

//            setContentView(R.layout.splash);


            new GetAppCurrentVersion().execute();


        }


    }

    private class GetLatestVersion extends AsyncTask<String, String, JSONObject> {


        private String latestVersion;

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
                        .get(2)
                        .ownText();

               /* org.jsoup.nodes.Document doc = Jsoup.connect("https://play.google.com/store/apps/details?id=com.nsl.beejtantra").timeout(20*1000).get();
                latestVersion = doc.getElementsByAttributeValue("itemprop","softwareVersion").first().text();*/
                System.out.println("? - Latest version of my app from playstore====>>>>" + latestVersion);
            } catch (Exception e) {
                e.printStackTrace();
                Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                startActivity(i);
                finish();
            }

            return new JSONObject();
        }

        @Override
        protected void onPostExecute(JSONObject jsonObject) {
            if (latestVersion != null) {

                if (!currentVersion.equalsIgnoreCase(latestVersion)) {
                    Common.Log.i("Common.versionCompare(currentVersion,latestVersion) : " + Common.versionCompare(currentVersion, latestVersion));

                    // if (Common.versionCompare(currentVersion,latestVersion) < 0)

                    showUpdateDialog();
                } else {
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            // This method will be executed once the timer is over
                            // Start your app main activity
                            Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                            startActivity(i);
                            finish();

                        }
                    }, SPLASH_TIME_OUT);


                }

            }
            super.onPostExecute(jsonObject);


        }
    }


    private void showUpdateDialog() {

        //Common.getDefaultSP(this).edit().putBoolean(Constants.SharedPreferencesKeys.LOGIN_STATUS,false);


        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle(Common.getStringResourceText(R.string.a_new_update_is_available));
        builder.setPositiveButton(Common.getStringResourceText(R.string.update), new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("market://details?id=com.nsl.beejtantra"));


                startActivity(intent);


            }
        });


        builder.setCancelable(false);
        dialog = builder.show();
    }

    private void Popup() {
        Dialog dialog = new Dialog(SplashScreen.this);
        // it remove the dialog title
        dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
        // set the laytout in the dialog
        dialog.setContentView(R.layout.popup);
        // set the background partial transparent
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        Window window = dialog.getWindow();
        WindowManager.LayoutParams param = window.getAttributes();
        // set the layout at right bottom
        param.gravity = Gravity.CENTER | Gravity.CENTER;
        // it dismiss the dialog when click outside the dialog frame
        dialog.setCanceledOnTouchOutside(false);
        dialog.setCancelable(false);
        dialog.show();
    }


    private class GetAppCurrentVersion extends AsyncTask<Void, Void, String> {

        private String latestVersion;
        private String URL;
        private String user_status="1";

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
if(Common.getUserIdFromSP(SplashScreen.this)!=null && !Common.getUserIdFromSP(SplashScreen.this).equalsIgnoreCase("")){
   URL= Constants.URL_NSL_MAIN + Constants.APP_CURRENT_VERSION+"?user_id="+Common.getUserIdFromSP(SplashScreen.this);
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
                //  Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
            }
            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (user_status!=null && user_status.equalsIgnoreCase("0")){
                logoutFunction(SplashScreen.this);
                return;
            }

            if (latestVersion != null) {

                if (!currentVersion.equalsIgnoreCase(latestVersion)) {
                    Common.Log.i("Common.versionCompare(currentVersion,latestVersion) : " + Common.versionCompare(currentVersion, latestVersion));

                    // if (Common.versionCompare(currentVersion,latestVersion) < 0)

                    showUpdateDialog();
                } else {
                    new Handler().postDelayed(new Runnable() {

                        @Override
                        public void run() {
                            // This method will be executed once the timer is over
                            // Start your app main activity
                            Intent i = new Intent(SplashScreen.this, LoginActivity.class);
                            startActivity(i);
                            finish();

                        }
                    }, SPLASH_TIME_OUT);


                }


            }
        }
    }

public void netxtPoint(){
    double meters = 50;

// number of km per degree = ~111km (111.32 in google maps, but range varies
 //   between 110.567km at the equator and 111.699km at the poles)
// 1km in degree = 1 / 111.32km = 0.0089
// 1m in degree = 0.0089 / 1000 = 0.0000089
    double coef = meters * 0.0000089;

    double new_lat = 17.46303821+ coef;

// pi / 180 = 0.018
    double new_long = 78.47240362 + coef / Math.cos(17.46303821 * 0.018);
    Log.d("M","Latlng: "+new_lat+ ","+new_long);
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

        Intent login = new Intent(getApplicationContext(), LoginActivity.class);
        startActivity(login);
        finish();

    }

}
