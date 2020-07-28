package com.nsl.beejtantra.scheduler;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.util.Log;
import android.widget.Toast;

import com.nsl.beejtantra.Constants;
import com.nsl.beejtantra.Customer_Details;
import com.nsl.beejtantra.Customers;
import com.nsl.beejtantra.DatabaseHandler;
import com.nsl.beejtantra.LoginActivity;
import com.nsl.beejtantra.MainActivity;
import com.nsl.beejtantra.Products_Pojo;
import com.nsl.beejtantra.Scheme_Products;
import com.nsl.beejtantra.Schemes;
import com.nsl.beejtantra.User_Company_Customer;
import com.nsl.beejtantra.User_Company_Division;
import com.nsl.beejtantra.Users;
import com.nsl.beejtantra.commonutils.Common;
import com.nsl.beejtantra.product_catalogue.CatalogueIntentService;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static com.nsl.beejtantra.MainActivity.DATABASE_NAME;

/**
 * Created by suprasoft on 3/16/2018.
 */

public class SyncUserDataService extends Service {
    public static final String mypreference = "mypref";
    private static final String TAG = SyncUserDataService.class.getName();
    public static final int UPDATE_PROGRESS = 1000;
    public SharedPreferences sharedpreferences;
    DatabaseHandler db;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    String userId, team;
    int role;
    private Context mContext;

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "onCreate");
        db = new DatabaseHandler(SyncUserDataService.this);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        this.mContext = getApplicationContext();

        userId = sharedpreferences.getString("userId", "");
        role = sharedpreferences.getInt(Constants.SharedPrefrancesKey.ROLE, 0);

        team = sharedpreferences.getString("team", "");

        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Log.v(TAG, "onStart");
        if (Common.haveInternet(getApplicationContext())) {  // connected to the internet

            new Async_getallucd().execute();

            new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
                @Override
                public void run() {
                    if (LocationService.isMyServiceRunning(CatalogueIntentService.class,getApplicationContext()))

                      try{ stopService(new Intent(getApplicationContext(), CatalogueIntentService.class));
                       startService(new Intent(getApplicationContext(), CatalogueIntentService.class));
                      }catch (Exception e){
                          e.printStackTrace();
                      }     }
            }, 15000);
        } else {
            Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    private class Async_getallproducts_price extends AsyncTask<Void, Void, String> {

        private String jsonData;

        protected String doInBackground(Void... params) {

            try {
                OkHttpClient client = new OkHttpClient();


                Response responses = null;


                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

                Request request = new Request.Builder()
                        .url(Constants.URL_NSL_MAIN + Constants.URL_MASTER_PRODUCT_PRICE+"&user_id="+Common.getUserIdFromSP(SyncUserDataService.this))
                        .get()
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();

                try {
                    responses = client.newCall(request).execute();
                    jsonData = responses.body().string();
                    System.out.println("!!!!!!!1 SyncUserData" + jsonData);
                    if (jsonData != null) {
                      //  db.deleteDataById(db.TABLE_PRODUCT_PRICE, "region_id", "'null'");
                        db.deleteDataByTableName(db.TABLE_PRODUCT_PRICE);
                        JSONArray companyarray = new JSONArray(jsonData);
                        // db.deleteDataByTableName(TABLE_PRODUCT_PRICE);
                        for (int n = 0; n < companyarray.length(); n++) {
                            JSONObject objinfo = companyarray.getJSONObject(n);

                            db.addProductPrice(new Products_Pojo(objinfo.getString("product_id"), objinfo.getString("price"), objinfo.getString("discount"), objinfo.getString("from_date"), objinfo.getString("to_date"), objinfo.getString("status"), objinfo.getString("region_id")));

                        }
                    }
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
            //  bar.setVisibility(View.GONE);

            if (jsonData != null) {
                try {


                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
            }

            if (Common.haveInternet(mContext)) {  // connected to the internet
                new Async_getallschemes().execute();

            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            }


        }
    }


    private class Async_getallschemes extends AsyncTask<Void, Void, String> {

        private String jsonData;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // bar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(Void... params) {

            //odb.delete(TABLE_DIVISION,null,null);

            try {
                OkHttpClient client = new OkHttpClient();


                Response responses = null;


                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

                Request request = new Request.Builder()
                        .url(Constants.URL_NSL_MAIN + Constants.URL_MASTER_SCHEMES + userId)
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

            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            /// bar.setVisibility(View.GONE);

            if (jsonData != null) {
                try {
//                    db.deleteDataByTableName(TABLE_SCHEMES);
//                    db.deleteDataByTableName(TABLE_SCHEME_PRODUCTS);
                    JSONObject jsonObject = new JSONObject(jsonData);

                    JSONArray companyarray = jsonObject.getJSONArray("scheme_information");

                    for (int n = 0; n < companyarray.length(); n++) {
                        JSONObject objinfo = companyarray.getJSONObject(n);
                        JSONObject schemeDataObj = objinfo.getJSONObject("scheme_data");

                        String scheme_id = schemeDataObj.getString("scheme_id");
                        String scheme_title = schemeDataObj.getString("scheme_title");
                        String product_sap_code = schemeDataObj.getString("scheme_sap_code");
                        String file_location = schemeDataObj.getString("file_location");
                        String status = schemeDataObj.getString("status");

                        db.addSchemes(new Schemes(scheme_id, scheme_title, product_sap_code, file_location, status));

                        JSONArray schemeProductDetailsArray = objinfo.getJSONArray("scheme_product_details");

                        for (int j = 0; j < schemeProductDetailsArray.length(); j++) {
                            JSONObject schemeProductDetailsObjinfo = schemeProductDetailsArray.getJSONObject(j);

                            String scheme_id1 = schemeProductDetailsObjinfo.getString("scheme_id");
                            String product_id = schemeProductDetailsObjinfo.getString("product_id");
                            String scheme_price = schemeProductDetailsObjinfo.getString("price_per_packet");
                            String region_id = schemeProductDetailsObjinfo.getString("region_id");
                            String company_id = schemeProductDetailsObjinfo.getString("company_id");
                            String crop_id = schemeProductDetailsObjinfo.getString("crop_id");
                            String slab_id = schemeProductDetailsObjinfo.getString("slab_id");
                            String booking_incentive = schemeProductDetailsObjinfo.getString("booking_incentive");
                            String valid_from = schemeProductDetailsObjinfo.getString("valid_from");
                            String valid_to = schemeProductDetailsObjinfo.getString("valid_to");
                            String booking_year = schemeProductDetailsObjinfo.getString("booking_year");
                            String season_code = schemeProductDetailsObjinfo.getString("season_code");
                            String extenstion_date = schemeProductDetailsObjinfo.getString("extenstion_date");


                            // Log.d("Insert: ", "Inserting Divisions ..");
                            db.addScheme_Products(new Scheme_Products(scheme_id1, product_id, scheme_price, region_id, company_id, crop_id, slab_id, booking_incentive, valid_from, valid_to, booking_year, season_code, extenstion_date));

                        }


                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
            }


            try {

                new Async_getallusers().execute();

            } catch (Exception e) {
                Log.d("Tag", e.toString());
            }

        }

        private class Async_getallusers extends AsyncTask<Void, Void, String> {

            private String userStatus="1";

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


                    MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

                    Request request = new Request.Builder()
                            .url(Common.getCompleteURLUsers(Constants.URL_NSL_MAIN + Constants.URL_MASTER_USERS, Common.getUserIdFromSP(mContext), team))
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

                        JSONArray companyarray = new JSONArray(jsonData);
                        //  db.deleteDataByTableName(TABLE_USERS);
                        for (int n = 0; n < companyarray.length(); n++) {
                            JSONObject objinfo = companyarray.getJSONObject(n);

                            String user_id = objinfo.getString("user_id");
                            String first_name = objinfo.getString("first_name");
                            String last_name = objinfo.getString("last_name");
                        String mobile_no = objinfo.getString("mobile_no");
                        String email = objinfo.getString("email");
                            String sap_id = objinfo.getString("sap_id");
//                        String password = objinfo.getString("password");
                            String role_id = objinfo.getString("role_id");
                            String reporting_manager_id = objinfo.getString("reporting_manager_id");

                            userStatus = objinfo.getString("status");
//                        String created_datetime = objinfo.getString("created_datetime");
//                        String updated_datetime = objinfo.getString("updated_datetime");
                            String designation = objinfo.getString("designation");
                        String headquarter = objinfo.getString("headquarter");
//                        String location = objinfo.getString("location");
                            String region_id = objinfo.getString("region_id");
                            String grade = objinfo.getString("grade");
                            String cost_center = objinfo.getString("cost_center");
                            String profile_base64 = objinfo.getString("profile_base64");
                            Log.d("Insert: ", "Inserting Users ..");
                            db.addusers(new Users(user_id, first_name, last_name, mobile_no, email, sap_id, null, role_id, reporting_manager_id, userStatus, null, null, designation, headquarter, null, region_id, grade, cost_center, profile_base64));

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.e("ServiceHandler", "Couldn't get any data from the url");
                    Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                }
                return jsonData;
            }

            @Override
            protected void onPostExecute(String s) {
                super.onPostExecute(s);
               // new Async_getallcustomers().execute();
                if (userStatus!=null && userStatus.equalsIgnoreCase("0")) {
                    logoutFunction(SyncUserDataService.this);
                    Log.d("logged out","3..");

                    return ;
                }

            }
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

        Intent login = new Intent(getApplicationContext(), LoginActivity.class);
        context.startActivity(login);
        System.exit(1);


    }
    public class Async_getallcustomers extends AsyncTask<Void, Void, String> {

        private String jsonData;

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


                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

                Request request = new Request.Builder()
                        .url(Common.getCompleteURL(role, Constants.GET_CUSTOMERS_AND_CUSTOMER_DETAILS, userId, team))
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

            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            // bar.setVisibility(View.GONE);

            if (jsonData != null) {
                try {


                    JSONObject jsonObject = new JSONObject(jsonData);

                    if (!jsonObject.has("error")) {
                          // if (!isUpdateTable)
                            //db.deleteDataByTableName(new String[]{"customers", "customer_details"});

                        JSONArray companyarray = jsonObject.getJSONArray("customer_information");

                        for (int n = 0; n < companyarray.length(); n++) {


                            JSONObject jsonObjectCstomerData = companyarray.getJSONObject(n);

                            JSONObject objinfo = jsonObjectCstomerData.getJSONObject("customer_data");

                            String customer_id = objinfo.getString("customer_id");
                            String customer_name = objinfo.getString("customer_name");
                            String customer_code = objinfo.getString("customer_code");
                            String address = objinfo.getString("address");
                            String street = objinfo.getString("street");
                            String company_id = objinfo.getString("company_id");
                            String city = objinfo.getString("city");
                            String region_id = objinfo.getString("region_id");
                            String country = objinfo.getString("country");
                            String telephone = objinfo.getString("telephone");
                            String created_datetime = objinfo.getString("created_datetime");
                            String updated_datetime = objinfo.getString("updated_datetime");
                            String status = objinfo.getString("status");
                            String password = objinfo.getString("password");
                            String email = objinfo.getString("email");
                            String state = objinfo.getString("state");
                            String district = objinfo.getString("district");
                            String lat_lon = objinfo.getString("lat_lon");


                            Log.d("Insert: ", "Inserting Customers ..");
                            db.addCustomers(new Customers(customer_id,
                                    customer_name,
                                    customer_code,
                                    address,
                                    street,
                                    city,
                                    country,
                                    region_id,
                                    telephone,
                                    company_id,
                                    status,
                                    created_datetime,
                                    updated_datetime, password, email, state, district, lat_lon));

                            Log.d("Inserted: ", "Inserted Customers ..");
                            if (jsonObjectCstomerData.has("customer_data_details")) {
                                JSONArray jsonArrayCustomerDetails = jsonObjectCstomerData.getJSONArray("customer_data_details");

                                for (int i = 0; i < jsonArrayCustomerDetails.length(); i++) {
                                    JSONObject jsonObjectData = jsonArrayCustomerDetails.getJSONObject(i);

                                    String customer_id1 = jsonObjectData.getString("customer_id");
                                    String divsion_id = jsonObjectData.getString("divsion_id");
                                    String credit_limit = jsonObjectData.getString("credit_limit");
                                    String inside_bucket = jsonObjectData.getString("inside_bucket");
                                    String outside_bucket = jsonObjectData.getString("outside_bucket");
                                    String status1 = jsonObjectData.getString("status");
                                    String created_datetime1 = jsonObjectData.getString("created_datetime");
                                    String updated_datetime1 = jsonObjectData.getString("updated_datetime");
                                    // String credit_balance = jsonObjectData.getString("credit_balance");
                                    String credit_balance = "0";
                                    Log.d("Insert: ", "Inserting Customer details ..");
                                    db.addCustomer_details(new Customer_Details(customer_id1, divsion_id, credit_limit, inside_bucket, outside_bucket, status1, created_datetime1, updated_datetime1, credit_balance));

                                }
                            }

                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                // Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
            }
            try{
            Intent syncData = new Intent(mContext, SchedulingService.class);
            mContext.startService(syncData);
            }catch (Exception e){
                e.printStackTrace();
            }

        }
    }


    private class Async_getallucd extends AsyncTask<Void, Void, String> {

        private String jsonData;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //  bar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(Void... params) {

            //odb.delete(TABLE_DIVISION,null,null);

            try {
                OkHttpClient client = new OkHttpClient();


                Response responses = null;


                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

                Request request = new Request.Builder()
                        .url(Constants.URL_NSL_MAIN + Constants.URL_MASTER_USER_COMPANY_DIVISION + team)
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


                if (jsonData != null) {
                    try {

                        JSONArray companyarray = new JSONArray(jsonData);
                        // db.deleteDataByTableName(TABLE_USER_COMPANY_DIVISION);
                        for (int n = 0; n < companyarray.length(); n++) {
                            JSONObject objinfo = companyarray.getJSONObject(n);
                            int junction_id = objinfo.getInt("junction_id");
                            String user_id = objinfo.getString("user_id");
                            String company_id = objinfo.getString("company_id");
                            String division_id = objinfo.getString("division_id");


                            // Log.d("Insert: ", "Inserting user company division ..");
                            db.addUser_Company_Division(new User_Company_Division(junction_id, user_id, company_id, division_id));
                            // do some stuff....

                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Log.e("ServiceHandler", "Couldn't get any data from the url");
                    //Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            new Async_getallucc().execute();
        }
    }

    private class Async_getallucc extends AsyncTask<Void, Void, String> {

        private String jsonData;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //  bar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(Void... params) {

            //odb.delete(TABLE_DIVISION,null,null);

            try {
                OkHttpClient client = new OkHttpClient();


                Response responses = null;


                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

                Request request = new Request.Builder()
                        .url(Common.getCompleteURL(role, Constants.URL_NSL_MAIN + Constants.URL_MASTER_USER_COMPANY_CUSTOMER, userId, team))
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

                    JSONArray companyarray = new JSONArray(jsonData);
                    //   db.deleteDataByTableName(TABLE_USER_COMPANY_CUSTOMER);
                    for (int n = 0; n < companyarray.length(); n++) {
                        JSONObject objinfo = companyarray.getJSONObject(n);
                        if (!objinfo.has("error")) {

                            int junction_id = objinfo.getInt("juction_id");
                            String user_id = objinfo.getString("user_id");
                            String company_id = objinfo.getString("company_id");
                            String customer_id = objinfo.getString("customer_id");
                            String status = objinfo.getString("status");
                            String map_start_date = objinfo.getString("map_start_date");
                            String unmap_date = objinfo.getString("unmap_date");
                            Log.d("Insert: ", "Inserting user company customer ..");
                            db.addUser_Company_Customer(new User_Company_Customer(junction_id, user_id, company_id, customer_id, status, map_start_date, unmap_date));
                        }

                        // do some stuff....

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                // Log.e("ServiceHandler", "Couldn't get any data from the url");
                //Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
            }
            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            //new Async_getallserviceorders().execute();

           new Async_getallcustomers().execute();

        }
    }

}
