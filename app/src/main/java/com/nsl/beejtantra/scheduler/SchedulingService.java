package com.nsl.beejtantra.scheduler;

import android.app.IntentService;
import android.app.NotificationManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v4.app.NotificationCompat;
import android.util.Log;
import android.widget.Toast;

import com.nsl.beejtantra.Companies;
import com.nsl.beejtantra.Constants;
import com.nsl.beejtantra.Crops;
import com.nsl.beejtantra.Customer_Details;
import com.nsl.beejtantra.Customers;
import com.nsl.beejtantra.DatabaseHandler;
import com.nsl.beejtantra.Divisions;
import com.nsl.beejtantra.LoginActivity;
import com.nsl.beejtantra.MainActivity;
import com.nsl.beejtantra.MyApplication;
import com.nsl.beejtantra.Payment_collection;
import com.nsl.beejtantra.Products_Pojo;
import com.nsl.beejtantra.Regions;
import com.nsl.beejtantra.Scheme_Products;
import com.nsl.beejtantra.Schemes;
import com.nsl.beejtantra.ServiceOrderDetailMaster;
import com.nsl.beejtantra.ServiceOrderMaster;
import com.nsl.beejtantra.Users;
import com.nsl.beejtantra.commonutils.Common;
import com.nsl.beejtantra.network.RetrofitRequester;
import com.nsl.beejtantra.network.RetrofitResponseListener;
import com.nsl.beejtantra.pojo.FormResponseResVo;
import com.nsl.beejtantra.pojo.GradePojo;
import com.nsl.beejtantra.pojo.RetailerStockSupply;
import com.nsl.beejtantra.pojo.Season;
import com.nsl.beejtantra.pojo.SeasonLineItem;
import com.nsl.beejtantra.pojo.SeasonResVo;
import com.nsl.beejtantra.pojo.ServiceOrderApproval;
import com.nsl.beejtantra.pojo.ServiceOrderHistory;
import com.nsl.beejtantra.pojo.StockDispatch;
import com.nsl.beejtantra.pojo.StockDispatchLineItem;
import com.nsl.beejtantra.pojo.StockDispatchResVo;
import com.nsl.beejtantra.pojo.StockDispatchResp;
import com.nsl.beejtantra.pojo.VersionControlVo;
import com.nsl.beejtantra.product_catalogue.CatalogueIntentService;
import com.nsl.beejtantra.stockmovement.NewCropsFragmentStockMovementList;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;

import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_SERVICEORDER_FFM_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_SERVICEORDER_ID;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_PAYMENT_COLLECTION;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_RETAILER_STOCK_SUPPLY;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_SEASON;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_SEASON_LINE_ITEMS;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_SERVICEORDER;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_SERVICE_ORDER_APPROVAL;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_STOCK_DISPATCH;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_STOCK_DISPATCH_ITEM;
import static com.nsl.beejtantra.MainActivity.DATABASE_NAME;

/**
 * This {@code IntentService} does the app's actual work.
 * {@code DailySchedulerReceiver} (a {@code WakefulBroadcastReceiver}) holds a
 * partial wake lock for this service while the service does its work. When the
 * service is finished, it calls {@code completeWakefulIntent()} to release the
 * wake lock.
 */
public class SchedulingService extends IntentService implements RetrofitResponseListener {
    private boolean isUpdateTable = false;
    private String jsonData;

    public SchedulingService() {
        super("SchedulingService");
    }

    private NotificationManager mNotificationManager;
    NotificationCompat.Builder builder;
    public static final String mypreference = "mypref";
    private static final String TAG = SchedulingService.class.getName();
    public SharedPreferences sharedpreferences;
    DatabaseHandler db;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    String userId, team;
    int role;
    private Context mContext;
    private String checkuid;
    private SQLiteDatabase sdbw;
    private ArrayList<String> tableMustInsertList;
    private JSONArray mainArray;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.i("Scheduler", "No doodle found.onCreate :-(");
        db = new DatabaseHandler(this);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        this.mContext = getApplicationContext();

        userId = sharedpreferences.getString("userId", "");
        role = sharedpreferences.getInt(Constants.SharedPrefrancesKey.ROLE, 0);
        team = sharedpreferences.getString("team", "");
    }

    @Override
    protected void onHandleIntent(Intent intent) {

        if (intent != null && intent.hasExtra("scheduler.connectivity_change")) {
            Log.d("connectivity_change", " scheduler.connectivity_change");
            isUpdateTable = true;
        }

        Log.i("Scheduler", "No doodle found. :-(");
        if (Common.haveInternet(this) && !userId.equals("")) {
        /*    if (sharedpreferences.contains(Constants.SharedPrefrancesKey.CURRENT_DATE) && sharedpreferences.getString(Constants.SharedPrefrancesKey.CURRENT_DATE, "").equals(Common.getCurrentDate())) {
                Log.i("Scheduler", "haveInternet1..");

            } else {
                Log.i("Scheduler", "else 2..");
                if (Common.isTime3AM()) {*/
            Log.i("Scheduler", "isTime3AM...");
            sharedpreferences.edit().putString(Constants.SharedPrefrancesKey.CURRENT_DATE, Common.getCurrentDate()).commit();
            Map<String, String> map = new HashMap<String, String>();
            map.put("name", "update_table_names");

            new RetrofitRequester(this).callGetApi(map);
            //  }

            //    }


        }


        // Release the wake lock provided by the BroadcastReceiver.
        //    DailySchedulerReceiver.completeWakefulIntent(intent);
        // END_INCLUDE(service_onhandle)
    }


    @Override
    public void onResponseSuccess(ArrayList<Object> object, Map<String, String> requestParams, int requestId) {
        //VersionControlListVo versionControlListVo=Common.getSpecificDataObject(object, VersionControlListVo.class);
        List<VersionControlVo> versionControlVoList = new ArrayList<>();
        tableMustInsertList = new ArrayList<>();
        tableMustInsertList.clear();

        for (Object objectt : object) {

            VersionControlVo versionControlListVo = Common.getSpecificDataObject(objectt, VersionControlVo.class);
            versionControlVoList.add(versionControlListVo);


        }


        if (db.getVersionControlList().size() == 0) {

            db.insertVersionControlData(versionControlVoList);

        } else {

            List<VersionControlVo> dbVersionControl = db.getVersionControlList();
            for (VersionControlVo versionControlVo : versionControlVoList) {

                if (!contains(dbVersionControl, versionControlVo.tableName)) {
                    List<VersionControlVo> tableNotExistList = new ArrayList<>();
                    tableNotExistList.clear();
                    VersionControlVo versionControlVo2 = new VersionControlVo();
                    versionControlVo2 = versionControlVo;
                    versionControlVo2.status = "0.0";
                    tableNotExistList.add(versionControlVo2);
                    db.insertVersionControlData(tableNotExistList);
                    tableMustInsertList.add(versionControlVo.tableName);
                    // dbVersionControl = db.getVersionControlList();
                }

                for (VersionControlVo dbVersionCode : db.getVersionControlList()) {

                    if (versionControlVo.tableName.equalsIgnoreCase(dbVersionCode.tableName) && !versionControlVo.status.equalsIgnoreCase(dbVersionCode.status)) {

                        Log.d("tableMustInsertList", versionControlVo.tableName + "dbVersionCode.status: " + dbVersionCode.status + "versionControlVo.status: " + versionControlVo.status);
                        tableMustInsertList.add(versionControlVo.tableName);
                        db.updateVersionControlData(versionControlVo);


                    }

                }
            }
        }

//if (tableMustInsertList.contains("customers") || tableMustInsertList.contains("customer_details")){


        if (tableMustInsertList.contains(Constants.MasterTableNames.TABLE_DIVISION)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new Async_getalldivisions().execute();
                }
            }, 32000);


        }
        if (tableMustInsertList.contains(Constants.MasterTableNames.TABLE_REGION)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new Async_getallreions().execute();
                }
            }, 28000);


        }
        if (tableMustInsertList.contains(Constants.MasterTableNames.TABLE_COMPANIES)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new Async_getallcompanies().execute();
                }
            }, 24000);


        }
        if (tableMustInsertList.contains(Constants.MasterTableNames.TABLE_CROPS)) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new Async_getallcrops().execute();
                }
            }, 3000);

        }
        if (tableMustInsertList.contains(Constants.MasterTableNames.TABLE_PRODUCTS)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    new Async_getallproducts().execute();
                }
            }, 10);


        }

        if (tableMustInsertList.contains(Constants.MasterTableNames.TABLE_PRODUCT_PRICE)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    new Async_getallproducts_price().execute();
                }
            }, 20000);


        }
        if (tableMustInsertList.contains(Constants.MasterTableNames.TABLE_SCHEMES)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    new Async_getallschemes().execute();
                }
            }, 16000);

        }

        if (tableMustInsertList.contains(Constants.MasterTableNames.TABLE_USERS)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    new Async_getallusers().execute();
                }
            }, 40000);


        }

        if (tableMustInsertList.contains(Constants.MasterTableNames.TABLE_GRADE)) {

            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {

                    new Async_getAllGrade().execute();
                }
            }, 12000);


        }
        if (tableMustInsertList.contains(Constants.MasterTableNames.TABLE_CUSTOMERS) || tableMustInsertList.contains(Constants.MasterTableNames.TABLE_CUSTOMER_DETAILS)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new Async_getallcustomers().execute();
                }
            }, 8000);

        }
        if(tableMustInsertList.contains(TABLE_PAYMENT_COLLECTION)){
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    new Async_getPaymentCollection().execute();
                }
            }, 8000);
        }
        if (tableMustInsertList.contains(TABLE_SEASON)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    MyApplication.getInstance().getRetrofitAPI().getSeasons().enqueue(new Callback<SeasonResVo>() {
                        @Override
                        public void onResponse(Call<SeasonResVo> call, retrofit2.Response<SeasonResVo> response) {
                            try {
                                SeasonResVo seasonResVo = response.body();
                                if (seasonResVo != null) {
                                    db.deleteDataByTableName(TABLE_SEASON);
                                    db.deleteDataByTableName(TABLE_SEASON_LINE_ITEMS);
                                    List<Season> seasons = seasonResVo.season;
                                    if (seasons != null && seasons.size() > 0) {
                                        for (int i = 0; i < seasons.size(); i++) {
                                            db.insertSeason(seasons.get(i));
                                            List<SeasonLineItem> lineItems = seasons.get(i).lineItems;
                                            if (lineItems != null && lineItems.size() > 0) {
                                                for (int j = 0; j < lineItems.size(); j++)
                                                    db.insertSeasonLineItem(lineItems.get(j));
                                            }
                                        }
                                    }
                                    new Async_getallserviceorders().execute();
                                }
                            } catch (Exception e) {

                            }

                        }

                        @Override
                        public void onFailure(Call<SeasonResVo> call, Throwable t) {

                        }
                    });


                }
            }, 10000);
        }
        if (tableMustInsertList.contains(TABLE_STOCK_DISPATCH)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    MyApplication.getInstance().getRetrofitAPI().getStockDispatch(team).enqueue(new Callback<StockDispatchResp>() {
                        @Override
                        public void onResponse(Call<StockDispatchResp> call, retrofit2.Response<StockDispatchResp> response) {
                            try {
                                StockDispatchResp resp = response.body();
                                if (resp != null && resp.status.equalsIgnoreCase("success")) {
                                    db.deleteDataByTableName(TABLE_STOCK_DISPATCH);
                                    db.deleteDataByTableName(TABLE_STOCK_DISPATCH_ITEM);
                                    db.deleteDataByTableName(TABLE_RETAILER_STOCK_SUPPLY);
                                    StockDispatchResVo stockDispatchResVo = resp.result;
                                    if (stockDispatchResVo != null) {
                                        List<StockDispatch> stockDispatch = stockDispatchResVo.stockDispatch;
                                        if (stockDispatch != null && stockDispatch.size() > 0) {
                                            for (int i = 0; i < stockDispatch.size(); i++) {
                                                db.insertStockDispatch(stockDispatch.get(i));
                                                List<StockDispatchLineItem> stockDispatchLineItems = stockDispatch.get(i).stockDispatchLineItems;
                                                List<RetailerStockSupply> retailerStockSupply = stockDispatch.get(i).retailerStockSupply;
                                                Common.insertDispatchLineItems(db, stockDispatchLineItems);
                                                Common.insertRetailerStockSupply(db, retailerStockSupply);
                                            }

                                        }
                                    }
                                }
                            } catch (Exception e) {
                            }

                        }

                        @Override
                        public void onFailure(Call<StockDispatchResp> call, Throwable t) {

                        }
                    });
                }
            }, 10000);

        }
        if (tableMustInsertList.contains(Constants.MasterTableNames.TABLE_CATALOGUE_CROPS) || tableMustInsertList.contains(Constants.MasterTableNames.TABLE_CATALOGUE_CROPS_PRODUCTS)) {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {
                        startService(new Intent(SchedulingService.this, CatalogueIntentService.class));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }
            }, 8000);

        }
    }

    @Override
    public void onFormResponseSuccess(String url, FormResponseResVo formResponseResVo, ProgressDialog dialog) {

    }

    public class Async_getallserviceorders extends AsyncTask<Void, Void, String> {
        ProgressDialog progressDialog;
        private SQLiteDatabase sdbw;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog = new ProgressDialog(LoginActivity.this);
//            progressDialog.setMessage("Please Wait ...");
//            progressDialog.setCancelable(false);
//            progressDialog.show();

        }

        protected String doInBackground(Void... params) {


            try {
                OkHttpClient client = new OkHttpClient();


                Response responses = null;


                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                Common.Log.i("SERVICE ORDER URL " + Common.getCompleteURL(role, Constants.URL_MASTER_SERVICE_ORDER, userId, team));
                Request request = new Request.Builder()
                        .url(Common.getCompleteURL(role, Constants.URL_MASTER_SERVICE_ORDER, userId, team))
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
                    JSONObject resultobject = new JSONObject(jsonData);

                    if (!resultobject.has("error")) {
                        db.deleteDataByTableName(db.TABLE_SERVICEORDER);
                        db.deleteDataByTableName(db.TABLE_SERVICEORDERDETAILS);
                        db.deleteDataByTableName(db.TABLE_SERVICE_ORDER_HISTORY);

                        JSONArray adavancebooking = resultobject.getJSONArray("adavancebooking");

                        for (int n = 0; n < adavancebooking.length(); n++) {


                            JSONObject result_service_order = adavancebooking.getJSONObject(n);
                            JSONObject service_order = /*result_service_order.getJSONObject("service_order");*/adavancebooking.getJSONObject(n);
                            if (!result_service_order.has("service_order_details")) {
                                continue;
                            }

                            JSONArray service_order_details = result_service_order.getJSONArray("service_order_details");


                            String ffm_id = service_order.getString("order_id");//ffm_id =order_id PRIMARY KEY
                            String order_type = service_order.getString("order_type");
                            String order_date = service_order.getString("order_date");
                            String order_sap_id = service_order.getString("order_sap_id");
                            String user_id = service_order.getString("user_id");
                            String customer_id = service_order.getString("customer_id");


                            String division_id = service_order.getString("division_id");
                            String company_id = service_order.getString("company_id");
                            String advance_amount = service_order.getString("advance_amount");
                            String created_by = service_order.getString("created_by");
                            String status = service_order.getString("status");

                            String created_datetime = service_order.getString("created_datetime");
                            String updated_datetime = service_order.getString("updated_datetime");
                            String approval_status = service_order.getString("approval_status");
                            String approval_comments = service_order.getString("approval_comments");


                            String selectQuery = "SELECT * FROM " + TABLE_SERVICEORDER + " WHERE " + KEY_TABLE_SERVICEORDER_FFM_ID + " = '" + ffm_id + "'";


                            sdbw = db.getWritableDatabase();
                            Cursor cc = sdbw.rawQuery(selectQuery, null);
                            cc.getCount();
                            // looping through all rows and adding to list
//                            if (cc.getCount() == 0) {
                            //doesn't exists therefore insert record.
                            db.addServiceorder(new ServiceOrderMaster("", order_type, order_date, user_id, customer_id, division_id, company_id, status, created_datetime, updated_datetime, ffm_id, advance_amount, created_by, approval_status, approval_comments, order_sap_id,""));
                            String selectQuerys = "SELECT  " + KEY_TABLE_SERVICEORDER_ID + " FROM " + TABLE_SERVICEORDER + " ORDER BY " + KEY_TABLE_SERVICEORDER_ID + " DESC LIMIT 1 ";
                            sdbw = db.getWritableDatabase();

                            Cursor c = sdbw.rawQuery(selectQuerys, null);
                            //System.out.println("cursor count "+cursor.getCount());
                            String orderidfromserviceorder = null;
                            if (c != null && c.moveToFirst()) {
                                orderidfromserviceorder = String.valueOf(c.getLong(0)); //The 0 is the column index, we only have 1 column, so the index is 0

                                Log.e("++++ lastId ++++", orderidfromserviceorder);
                            }

                            try {

                                for (int m = 0; m < service_order_details.length(); m++) {
                                    JSONObject objinfo = service_order_details.getJSONObject(m);
                                    JSONArray service_order_history = objinfo.getJSONArray("service_order_history");
                                    String service_order_details_order_id = objinfo.getString("order_id");
                                    String service_order_details_crop_id = objinfo.getString("crop_id");
                                    String scheme_id = objinfo.getString("scheme_id");
                                    String product_id = objinfo.getString("product_id");
                                    String quantity = objinfo.getString("quantity");
                                    String orderQuantity = objinfo.getString("order_quantity");

                                    String order_price = objinfo.getString("order_price");
                                    String service_order_details_advance_amount = objinfo.getString("advance_amount");
                                    String service_order_details_status = objinfo.getString("status");
                                    // String created_by            = objinfo.getString("created_by");
                                    String service_order_details_created_datetime = objinfo.getString("created_datetime");
                                    String service_order_details_updated_datetime = objinfo.getString("updated_datetime");
                                    String ffmID = objinfo.getString("service_order_detail_id");
                                    String slab_id = objinfo.getString("slab_id");
                                    slab_id = slab_id != null || !slab_id.equalsIgnoreCase("null") || !slab_id.equalsIgnoreCase("") ? slab_id : "";


                                    db.addServiceorderdetails(new ServiceOrderDetailMaster(ffmID, orderidfromserviceorder, service_order_details_crop_id, scheme_id,
                                            product_id, quantity, order_price,
                                            service_order_details_advance_amount, service_order_details_status, service_order_details_created_datetime, service_order_details_updated_datetime, ffmID, slab_id, orderQuantity));
                                    if (service_order_history != null) {
                                        for (int k = 0; k < service_order_history.length(); k++) {
                                            JSONObject historyObj = service_order_history.getJSONObject(k);
                                            ServiceOrderHistory soh = new ServiceOrderHistory();
                                            soh.orderHistoryId = historyObj.getString("order_history_id");
                                            soh.orderId = historyObj.getString("order_id");
                                            soh.orderDetailsId = historyObj.getString("service_order_detail_id");
                                            soh.productId = historyObj.getString("product_id");
                                            soh.orderQuantity = historyObj.getString("order_quantity");
                                            soh.quantity = historyObj.getString("quantity");
                                            soh.orderPrice = historyObj.getString("order_price");
                                            soh.status = historyObj.getString("status");
                                            soh.advanceAmount = historyObj.getString("advance_amount");
                                            soh.createdBy = historyObj.getString("createdby");
                                            soh.cDateTime = historyObj.getString("created_datetime");
                                            soh.modifiedBy = historyObj.getString("modified_by");
                                            soh.orderApprovalId = historyObj.getString("service_order_approval_id");
                                            db.insertServiceOrderHistory(soh);
                                        }
                                    }
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
//                            }


                        }

                    }

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
            super.onPostExecute(s);
            new Async_getallServiceOrderApprovals().execute();
        }
    }

    public class Async_getallServiceOrderApprovals extends AsyncTask<Void, Void, String> {
        ProgressDialog progressDialog;
        private SQLiteDatabase sdbw;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog = new ProgressDialog(LoginActivity.this);
//            progressDialog.setMessage("Please Wait ...");
//            progressDialog.setCancelable(false);
//            progressDialog.show();

        }

        protected String doInBackground(Void... params) {


            try {
                OkHttpClient client = new OkHttpClient();


                Response responses = null;


                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                Common.Log.i("SERVICE ORDER APPROVAL URL " + Common.getCompleteURL(Constants.URL_MASTER_SERVICE_ORDER_APPROVAL, userId));
                Request request = new Request.Builder()
                        .url(Common.getCompleteURL(Constants.URL_NSL_MAIN + Constants.URL_MASTER_SERVICE_ORDER_APPROVAL, userId))
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
                    JSONObject resultobject = new JSONObject(jsonData);

                    if (!resultobject.has("error")) {
                        db.deleteDataByTableName(TABLE_SERVICE_ORDER_APPROVAL);
                        JSONArray adavancebooking = resultobject.getJSONArray("service_order_approvals");

                        for (int n = 0; n < adavancebooking.length(); n++) {
                            JSONObject service_order_approval = adavancebooking.getJSONObject(n);
                            String soa_id = service_order_approval.getString("service_order_approval_id");
                            String order_id = service_order_approval.getString("order_id");
                            String assigned_user_id = service_order_approval.getString("assigned_user_id");
                            String user_id = service_order_approval.getString("user_id");
                            String order_status = service_order_approval.getString("order_status");
                            String sync_status = service_order_approval.getString("sync_status");
                            String created_by = service_order_approval.getString("created_by");
                            String modified_by = service_order_approval.getString("modified_by");
                            String c_date_time = service_order_approval.getString("created_date_time");
                            String u_date_time = service_order_approval.getString("modified_date_time");
                            String pendingBy = service_order_approval.getString("pending_by");
                            ServiceOrderApproval serviceOrderApproval = new ServiceOrderApproval(soa_id, order_id, user_id, assigned_user_id, order_status, created_by, modified_by, c_date_time, u_date_time, sync_status, pendingBy);
                            db.insertServiceOrderApproval(serviceOrderApproval);


                        }

                    }

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
            super.onPostExecute(s);
            new Async_getPaymentCollection().execute();
        }
    }

    private class Async_getPaymentCollection extends AsyncTask<Void, Void, String> {

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
                        .url(Constants.GET_PAYMENT_COLLECTION_DETAILS + team)
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

                    JSONArray companyarray = new JSONArray(jsonData);
                    db.deleteDataByTableName(db.TABLE_PAYMENT_COLLECTION);
                    for (int n = 0; n < companyarray.length(); n++) {
                        JSONObject objinfo = companyarray.getJSONObject(n);

                        if (!objinfo.has("error")) {

                            String payment_id = objinfo.getString("payment_id");
                            String sqlite_id = objinfo.getString("sqlite_id");
                            String payment_type = objinfo.getString("payment_type");
                            String user_id = objinfo.getString("user_id");
                            String company_id = objinfo.getString("company_id");
                            String division_id = objinfo.getString("division_id");
                            String customer_id = objinfo.getString("customer_id");
                            String total_amount = objinfo.getString("total_amount");
                            String payment_mode = objinfo.getString("payment_mode");
                            String bank_name = objinfo.getString("bank_name");
                            String rtgs_or_neft_no = objinfo.getString("rtgs_or_neft_no");
                            String payment_datetime = objinfo.getString("payment_datetime");
                            String date_on_cheque_no = objinfo.getString("date_on_cheque_no");
                            String cheque_no_dd_no = objinfo.getString("cheque_no_dd_no");
                            String status = objinfo.getString("status");
                            String created_by = objinfo.getString("created_by");
                            String created_datetime = objinfo.getString("created_datetime");
                            String updated_datetime = objinfo.getString("updated_datetime");
                            if (status.equalsIgnoreCase(null) || status.equalsIgnoreCase("null") || status.equalsIgnoreCase("")) {
                                status = "0";
                            }

                            Log.d("Insert: ", "Inserting Customer details ..");
                            db.addPaymentCollection(new Payment_collection(payment_id, payment_type, user_id, company_id, division_id, customer_id, total_amount, payment_mode, bank_name, rtgs_or_neft_no, payment_datetime, date_on_cheque_no, cheque_no_dd_no, Integer.parseInt(status), created_datetime, updated_datetime, payment_id));
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
            }

        }
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
                        if (!isUpdateTable)
                            db.deleteDataByTableName(new String[]{"customers", "customer_details"});

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


        }
    }

    private class Async_getAllGrade extends AsyncTask<Void, Void, String> {

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
                        .url(Constants.URL_NSL_MAIN + Constants.URL_MASTER_GRADE)
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
                    //  db.deleteDataByTableName(new String[]{Constants.MasterTableNames.TABLE_GRADE});
                    JSONArray companyarray = new JSONArray(jsonData);

                    for (int n = 0; n < companyarray.length(); n++) {
                        JSONObject objinfo = companyarray.getJSONObject(n);

                        String grade_id = objinfo.getString("grade_id");
                        String grade_name = objinfo.getString("grade_name");
                        String price_per_km = objinfo.getString("price_per_km");
                        GradePojo gradePojo = new GradePojo();
                        gradePojo.gradeId = grade_id;
                        gradePojo.gradeName = grade_name;
                        gradePojo.pricePerKm = price_per_km;

                        Log.d("Insert: ", "Inserting Users ..");
                        db.insertGrade(db.getWritableDatabase(), gradePojo);
                    }

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


        }
    }

    private class Async_getallusers extends AsyncTask<Void, Void, String> {

        private String jsonData;
        private String userStatus = "1";

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
                        .url(Common.getCompleteURLUsers(Constants.URL_NSL_MAIN + Constants.URL_MASTER_USERS, Common.getUserIdFromSP(getApplicationContext()), team))
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
                    //  db.deleteDataByTableName(new String[]{Constants.MasterTableNames.TABLE_USERS});


                    JSONArray companyarray = new JSONArray(jsonData);

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
                //   Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
            }
            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (userStatus != null && userStatus.equalsIgnoreCase("0")) {
                logoutFunction(SchedulingService.this);
                Log.d("logged out", "2..");

                return;
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
                    //  db.deleteDataByTableName(new String[]{Constants.MasterTableNames.TABLE_SCHEME_PRODUCTS, Constants.MasterTableNames.TABLE_SCHEMES});
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
                            JSONObject schemeProductDetailsObjinfo = companyarray.getJSONObject(j);

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


        }


    }


    private class Async_getallproducts_price extends AsyncTask<Void, Void, String> {

        private String jsonData;

        protected String doInBackground(Void... params) {

            try {
                OkHttpClient client = new OkHttpClient();


                Response responses = null;


                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

                Request request = new Request.Builder()
                        .url(Constants.URL_NSL_MAIN + Constants.URL_MASTER_PRODUCT_PRICE+"&user_id="+Common.getUserIdFromSP(SchedulingService.this))
                        .get()
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();

                try {
                    responses = client.newCall(request).execute();
                    jsonData = responses.body().string();
                    System.out.println("!!!!!!!1" + jsonData);
                    if (jsonData != null) {
                        //  db.deleteDataById(db.TABLE_PRODUCT_PRICE,"region_id","'null'");
                        // if (!isUpdateTable)
                        db.deleteDataByTableName(db.TABLE_PRODUCT_PRICE);
                        JSONArray companyarray = new JSONArray(jsonData);

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
                    // db.deleteDataByTableName(new String[]{Constants.MasterTableNames.TABLE_PRODUCT_PRICE});

                   /* JSONArray companyarray = new JSONArray(jsonData);

                    for (int n = 0; n < companyarray.length(); n++) {
                        JSONObject objinfo = companyarray.getJSONObject(n);

                        db.addProductPrice(new Products_Pojo(objinfo.getString("product_id"), objinfo.getString("price"), objinfo.getString("discount"), objinfo.getString("from_date"), objinfo.getString("to_date"), objinfo.getString("status"), objinfo.getString("region_id")));

                    }*/

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                // Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
            }


        }
    }

    private class Async_getallproducts extends AsyncTask<Void, Void, String> {

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
                        .url(Constants.URL_NSL_MAIN + Constants.URL_MASTER_PRODUCTS)
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
            //  bar.setVisibility(View.GONE);

            if (jsonData != null) {
                try {
                    JSONArray companyarray = new JSONArray(jsonData);
                    if (!isUpdateTable)
                        db.deleteDataByTableName(new String[]{Constants.MasterTableNames.TABLE_PRODUCTS});

                    for (int n = 0; n < companyarray.length(); n++) {
                        JSONObject objinfo = companyarray.getJSONObject(n);

                        // Log.d("Insert: ", "Inserting Products ..");
                        db.addProducts(new Products_Pojo(objinfo.getString("product_id"),
                                objinfo.getString("product_name"),
                                objinfo.getString("product_description"),
                                objinfo.getString("product_sap_code"),
                                objinfo.getString("crop_id"),
                                objinfo.getString("company_id"),
                                objinfo.getString("division_id"),
                                "", "", "", "", "",
                                objinfo.getString("status"),
                                objinfo.getString("products_images"),
                                objinfo.getString("product_videos"),
                                objinfo.getString("created_datetime"),
                                objinfo.getString("updated_datetime"),
                                objinfo.getString("no_packets"),
                                objinfo.getString("catlog_url"),
                                objinfo.getString("brand_name")));
                        //Log.d("Reading: ", "Reading all products..");

                        /*List<Products_Pojo> products_pojo = db.getAllProducts();

                        for (Products_Pojo div : products_pojo) {
                            String log = "Id: "+div.getID()+ " \n Product master ID "+div.getProductMasterId()+" \n ,Product: " + div.getProductName()+ " \n ,Product division_id: " + div.getProductdivisionid()+ "\n  ,Product_region: " + div.getProductregeion() + " ,Product cropid: " + div.getProductcropid();
                            // Writing Contacts to log
                            Log.e("Products : ", log);

                        }*/
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                //Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
            }


        }
    }


    private class Async_getallcrops extends AsyncTask<Void, Void, String> {

        private String jsonData;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //bar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(Void... params) {


            try {
                OkHttpClient client = new OkHttpClient();


                Response responses = null;


                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

                Request request = new Request.Builder()
                        .url(Constants.URL_NSL_MAIN + Constants.URL_MASTER_CROPS)
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
            //bar.setVisibility(View.GONE);

            if (jsonData != null) {
                try {
                    if (!isUpdateTable)
                        db.deleteDataByTableName(new String[]{Constants.MasterTableNames.TABLE_CROPS});
                    JSONArray companyarray = new JSONArray(jsonData);

                    for (int n = 0; n < companyarray.length(); n++) {
                        JSONObject objinfo = companyarray.getJSONObject(n);

                        String crop_id = objinfo.getString("crop_id");
                        String crop_name = objinfo.getString("crop_name");
                        String crop_code = objinfo.getString("crop_code");
                        String divsion_id = objinfo.getString("divsion_id");
                        String crop_sap_id = objinfo.getString("crop_sap_id");
                        String created_datetime = objinfo.getString("created_datetime");
                        String updated_datetime = objinfo.getString("updated_datetime");


                        //Log.d("Insert: ", "Inserting Crops ..");
                        db.addCrops(new Crops(crop_id, crop_name, crop_code, crop_sap_id, divsion_id, created_datetime, updated_datetime));


                        // do some stuff....

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                //  Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
            }

        }

    }


    private class Async_getallreions extends AsyncTask<Void, Void, String> {

        private String jsonData;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // bar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(Void... params) {


            try {
                OkHttpClient client = new OkHttpClient();
                Response responses = null;
                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

                Request request = new Request.Builder()
                        .url(Constants.URL_NSL_MAIN + Constants.URL_MASTER_REGIONS)
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
                    // db.deleteDataByTableName(new String[]{Constants.MasterTableNames.TABLE_REGION});
                    JSONArray companyarray = new JSONArray(jsonData);

                    for (int n = 0; n < companyarray.length(); n++) {
                        JSONObject objinfo = companyarray.getJSONObject(n);

                        String region_id = objinfo.getString("region_id");
                        String region_name = objinfo.getString("region_name");
                        String region_code = objinfo.getString("region_code");
                        String status = objinfo.getString("status");

                        // Log.d("Insert: ", "Inserting Regions ..");
                        db.addRegions(new Regions(region_id, region_name, region_code, status));

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                //  Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
            }


        }
    }


    private class Async_getallcompanies extends AsyncTask<Void, Void, String> {

        private String jsonData;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //  bar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(Void... params) {
            //odb.delete(db.TABLE_COMPANIES,null,null);

            try {
                OkHttpClient client = new OkHttpClient();


                Response responses = null;


                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

                Request request = new Request.Builder()
                        .url(Constants.URL_NSL_MAIN + Constants.URL_MASTER_COMPANIES)
                        .get()
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();

                //System.out.println("######"+Constants.URL_NSL_MAIN + Constants.URL_CUSTOMERS + userId);


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
                    // db.deleteDataByTableName(new String[]{Constants.MasterTableNames.TABLE_COMPANIES});
                    JSONArray companyarray = new JSONArray(jsonData);

                    for (int n = 0; n < companyarray.length(); n++) {
                        JSONObject objinfo = companyarray.getJSONObject(n);

                        String company_id = objinfo.getString("company_id");
                        String name = objinfo.getString("name");
                        String company_code = objinfo.getString("company_code");
                        String company_sap_id = objinfo.getString("company_sap_id");
                        String status = objinfo.getString("status");
                        String created_datetime = objinfo.getString("created_datetime");
                        String updated_datetime = objinfo.getString("updated_datetime");


                        // Log.d("Insert: ", "Inserting Divisions ..");
                        db.addCompanies(new Companies(company_id, name, company_sap_id, company_code, status, created_datetime, updated_datetime));

                        // do some stuff....

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                // Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
            }


        }
    }


    private class Async_getalldivisions extends AsyncTask<Void, Void, String> {

        private String jsonData;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*
            progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setMessage(LoginActivity.this.getString(R.string.loading_masters));
            progressDialog.setCancelable(false);
            progressDialog.show();*/

            //  bar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(Void... params) {

            //odb.delete(TABLE_DIVISION,null,null);

            try {
                OkHttpClient client = new OkHttpClient();


                Response responses = null;


                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

                Request request = new Request.Builder()
                        .url(Constants.URL_NSL_MAIN + Constants.URL_MASTER_DIVISIONS)
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
            //  bar.setVisibility(View.GONE);

            if (jsonData != null) {
                try {
                    // db.deleteDataByTableName(new String[]{Constants.MasterTableNames.TABLE_DIVISION});
                    JSONArray companyarray = new JSONArray(jsonData);

                    for (int n = 0; n < companyarray.length(); n++) {
                        JSONObject objinfo = companyarray.getJSONObject(n);

                        String division_id = objinfo.getString("division_id");
                        String division_name = objinfo.getString("division_name");
                        String division_code = objinfo.getString("division_code");
                        String division_sap_id = objinfo.getString("division_sap_id");
                        String status = objinfo.getString("status");
                        String created_datetime = objinfo.getString("created_datetime");
                        String updated_datetime = objinfo.getString("updated_datetime");


                        // Log.d("Insert: ", "Inserting Divisions ..");
                        db.addDivisions(new Divisions(division_id, division_name, division_code, division_sap_id, status, created_datetime, updated_datetime));


                       /* HashMap<String, String> map = new HashMap<String, String>();
                        // adding each child node to HashMap key => value
                        map.put("division_id",        division_id   );
                        map.put("division_name",      division_name);
                        map.put("division_code",      division_code);
                        map.put("division_sap_id",    division_sap_id);
                        map.put("status",             status);
                        map.put("created_datetime",   created_datetime);
                        map.put("updated_datetime",   updated_datetime);


                        favouriteItem.add(map);*/
                        // do some stuff....

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                //  Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
            }


        }
    }

    boolean contains(List<VersionControlVo> list, String name) {
        for (VersionControlVo item : list) {
            if (item.tableName.equals(name)) {
                return true;
            }
        }
        return false;
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
        login.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        context.startActivity(login);


    }

}
