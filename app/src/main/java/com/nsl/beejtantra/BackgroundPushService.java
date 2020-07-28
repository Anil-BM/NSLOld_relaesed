package com.nsl.beejtantra;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.ResultReceiver;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.util.Log;
import android.webkit.MimeTypeMap;
import android.widget.Toast;

import com.nsl.beejtantra.FarmerCoupans.FarmerCouponsreqVo;
import com.nsl.beejtantra.FarmerCoupans.Getcoupans;
import com.nsl.beejtantra.RetroResponses.resmain_farmer_coupons_detail;
import com.nsl.beejtantra.TFA.Demandgeneation;
import com.nsl.beejtantra.TFA.DemandgenerationreqVo;
import com.nsl.beejtantra.commonutils.Common;
import com.nsl.beejtantra.complaints.Complaints;
import com.nsl.beejtantra.dailydairy.DailyDairy;
import com.nsl.beejtantra.feedback.Feedback;
import com.nsl.beejtantra.fieldestimation.YieldReqVo;
import com.nsl.beejtantra.marketintelligence.Crop_Shifts;
import com.nsl.beejtantra.marketintelligence.Price_Survey;
import com.nsl.beejtantra.marketintelligence.Product_Survey;
import com.nsl.beejtantra.marketintelligencenew.CompetitorChannel;
import com.nsl.beejtantra.marketintelligencenew.CompetitorChannelReqVo;
import com.nsl.beejtantra.marketintelligencenew.MarketPotential;
import com.nsl.beejtantra.marketintelligencenew.MarketPotentialReqVo;
import com.nsl.beejtantra.network.BasicAuthInterceptor;
import com.nsl.beejtantra.network.RetrofitAPI;
import com.nsl.beejtantra.network.RetrofitRequester;
import com.nsl.beejtantra.network.RetrofitResponseListener;
import com.nsl.beejtantra.pojo.ChannelPreference;
import com.nsl.beejtantra.pojo.ChannelPreferenceReqVo;
import com.nsl.beejtantra.pojo.CommodityPrice;
import com.nsl.beejtantra.pojo.CommodityPriceReqVo;
import com.nsl.beejtantra.pojo.CompetitorStrength;
import com.nsl.beejtantra.pojo.CompetitorStrengthReqVo;
import com.nsl.beejtantra.pojo.ComplaintReq;
import com.nsl.beejtantra.pojo.CropShift;
import com.nsl.beejtantra.pojo.CropShiftReqVo;
import com.nsl.beejtantra.pojo.DistributorsRetailerPojo;
import com.nsl.beejtantra.pojo.FarmerPojo;
import com.nsl.beejtantra.pojo.FeedbackReq;
import com.nsl.beejtantra.pojo.FormResponse;
import com.nsl.beejtantra.pojo.FormResponseResVo;
import com.nsl.beejtantra.pojo.InsertedRetailer;
import com.nsl.beejtantra.pojo.ProductPricingReqVo;
import com.nsl.beejtantra.pojo.ProductPricingSurvey;
import com.nsl.beejtantra.pojo.RetailerStockReqVo;
import com.nsl.beejtantra.pojo.RetailerStockResVo;
import com.nsl.beejtantra.pojo.RetailerStockSupply;
import com.nsl.beejtantra.pojo.ServiceOrderApproval;
import com.nsl.beejtantra.pojo.ServiceOrderHistory;
import com.nsl.beejtantra.pojo.StockMovementUnSynedPojo;
import com.nsl.beejtantra.pojo.StockReturnUnSynedPojo;
import com.nsl.beejtantra.pojo.VersionControlVo;
import com.nsl.beejtantra.retailers.Retailer;
import com.nsl.beejtantra.scheduler.SyncUserDataService;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.nsl.beejtantra.Constants.BASE_URL;
import static com.nsl.beejtantra.DatabaseHandler.KEY_CHANNEL_PREF_FFM_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_CHANNEL_PREF_PRIMARY_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_COMPETITOR_STRENGTH_FFM_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_COMPETITOR_STRENGTH_PRIMARY_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_COMP_CHANNEL_FFM_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_COMP_CHANNEL_PRIMARY_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_CP_FFM_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_CP_PRIMARY_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_CROP_SHIFTS_FFMID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_CROP_SHIFTS_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_CROP_SHIFTS_PRIMARY_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_DD_FFMID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_DD_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_APPROVAL_STATUS;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_CHECK_IN_TIME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_COMMENTS;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_CONCERN_PERSON_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_CREATED_BY;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_CREATED_DATETIME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_EVENT_END_DATE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_EVENT_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_EVENT_PARTICIPANTS;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_EVENT_PURPOSE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_EVENT_VENUE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_FARMER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_FEILD_AREA;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_FFM_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_GEO_TRACKING_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_GSTIN_NO;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_LOCATION_ADDRESS;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_LOCATION_DISTRICT;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_LOCATION_TALUKA;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_LOCATION_VILLAGE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_MOBILE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_PLAN_DATE_TIME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_PURPOSE_VISIT;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_PURPOSE_VISIT_IDS;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_REGION_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_RETAILER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_SERVER_FLAG;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_STATUS;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_TYPE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_UPDATED_BY;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_UPDATE_DATETIME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_VILLAGE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_VISIT_CROP_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_VISIT_CUSTOMER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_VISIT_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_VISIT_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_VISIT_PLAN_TYPE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_VISIT_USER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_MARKET_POTENTIAL_FFM_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_MARKET_POTENTIAL_PRIMARY_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_PAYMENT_COLLECTION_FFMID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_PAYMENT_COLLECTION_PAYMENT_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_PRICE_SURVEY_FFMID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_PRICE_SURVEY_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_SURVEY_FFM_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_SURVEY_PRIMARY_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_COMPLAINTS_FFMID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_COMPLAINTS_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_COMPLAINTS_IMAGE_UPLOAD;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_COMPLAINTS_STAGES;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_COMPLAINTS_STATUS;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_FEEDBACK_FEEDBACK_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_FEEDBACK_FFMID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_FEEDBACK_IMAGE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_GEO_TRACKING_FFMID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_GEO_TRACKING_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_SERVICEORDER_FFM_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_SERVICEORDER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_SERVICEORDER_SAP_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_SMD_DETAIL_ID;
import static com.nsl.beejtantra.DatabaseHandler.SYNC_STATUS;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_CHANNEL_PREFERENCE;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_COMMODITY_PRICE;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_COMPETITOR_CHANNEL;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_COMPETITOR_STRENGTH;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_COMPLAINT;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_CROP_SHIFTS;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_DAILYDAIRY;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_EMPLOYEE_VISIT_MANAGEMENT;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_FEEDBACK;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_GEO_TRACKING;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_MARKET_POTENTIAL;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_MI_CROP_SHIFTS;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_MI_PRICE_SURVEY;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_MI_PRODUCT_SURVEY;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_PAYMENT_COLLECTION;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_PRODUCT_PRICING_SURVEY;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_SERVICEORDER;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_SMD;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_STOCK_MOVEMENT_RETAILER_DETAILS;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_STOCK_RETURNS_DETAILS;
import static com.nsl.beejtantra.advancebooking.CropsFragmentAdvancebookingActivity.toJson;
import static com.nsl.beejtantra.commonutils.Common.isStringNull;

/**
 * Created by Apresh on 1/26/2017.
 */

public class BackgroundPushService extends Service implements RetrofitResponseListener {

    public static final String mypreference = "mypref";
    private static final String TAG = BackgroundPushService.class.getName();
    public static final int UPDATE_PROGRESS = 1000;
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
    private Intent intent;

    @Override
    public void onCreate() {
        super.onCreate();
        Log.v(TAG, "onCreate");
        db = new DatabaseHandler(BackgroundPushService.this);
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
        this.intent = intent;
        if (userId.equals("")) {
            stopSelf();
            return;
        }
        pushDEMANDGENERATIONThread();
        pushFARMERCOUPANSTHREAD();

//        pushRetailerThread();
//        pushFarmerThread();
                                          pushFeedBackThread();
//        pushAdvanceBookingThread();
//        pushOrderIndentThread();
//        pushComplaintprodThread();
//        pushComplaintThread();
//        pushPlanerScheduleThread();
//        getNewOrUpdatedServiceOrder();
//        pushDailyDairyThread();
//        pushMICommodityPriceThread();
//        pushMICropShiftsThread();
//        pushMIPriceSurveyThread();
//        pushMIProductSurveyThread();
//        pushGeoThread();
//        pushPlanerEventThread();
//        pushPaymentThread();
//        pushProfileImageThread();
//        pushYEThread();
//        prepareStockMovementDataAndPush();
//        prepareStockMovementRetailerDataAndPush();
//        prepareStockReturnsDataAndPush(intent);
//        prepareMarketPotentialDataAndPush();


        if (intent != null && intent.hasExtra("scheduler.SyncUserDataService")) {
            Log.d("SyncUserDataService", " scheduler.SyncUserDataService");
            try {
                mContext.startService(new Intent(mContext, SyncUserDataService.class));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }

        if (Common.haveInternet(this)) {/*
            if (sharedpreferences.contains(Constants.SharedPrefrancesKey.CURRENT_DATE) && sharedpreferences.getString(Constants.SharedPrefrancesKey.CURRENT_DATE, "").equals(Common.getCurrentDate())) {


            } else {
                if (Common.isTime3AM()) {
                    sharedpreferences.edit().putString(Constants.SharedPrefrancesKey.CURRENT_DATE, Common.getCurrentDate()).commit();
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("name", "update_table_names");

                    new RetrofitRequester(this).callGetApi(map);
                }

            }


       */
        }


    }

    private void pushYEThread() {                                   //13
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                prepareYEDataPush();
            }
        }, 5000);
    }

    private void prepareYEDataPush() {                               //14
        try {
            List<YieldReqVo> yieldReqVos = db.getYERecords();
            for (int i = 0; i < yieldReqVos.size(); i++) {
                new RetrofitRequester(this).sendRequest(Constants.URL_INSERTING_YIELD_ESTIMATION, yieldReqVos.get(i), false);

            }
        } catch (Exception e) {
            prepareStockMovementDataAndPush();
        }
    }

    private void pushFarmerThread() {

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                prepareFarmerOfflineDataPush();
            }
        }, 5000);
    }

    private void prepareFarmerOfflineDataPush() {
        try {
            List<FarmerPojo> offlineFarmers = db.getOfflineFarmers();
            for (int i = 0; i < offlineFarmers.size(); i++) {

                try {
                    FarmerPojo farmer = offlineFarmers.get(i);
                    OkHttpClient client = new OkHttpClient();
                    /*For passing parameters*/

                    RequestBody formBody = new FormEncodingBuilder()
                            .add("farmer_primary_id", String.valueOf(farmer.getPrimaryId()))
                            .add("farmer_name", farmer.getFarmerName())
                            .add("location_district", farmer.getLocationDistrict())
                            .add("location_taluka", farmer.getLocationTaluka())
                            .add("location_village", farmer.getLocationVillage())
                            .add("region_id", farmer.getRegionId())
                            .add("mobile", farmer.getMobile())

//                        .add("customer_id", checkuid)
                            .build();

                    Response responses = null;

//                System.out.println("---- retailer data -----" + params[0] + params[1] + params[2] + params[3] + params[4] + params[5] + params[6] + params[7]);

                /*MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType,
                        "type=check_in_lat_lon&visit_type=1&user_id=7&latlon=17.4411%2C78.3911&visit_date=2016-12-05%2C18%3A27%3A30&check_in_time=18%3A27%3A30&tracking_id=0");*/
                    Request request = new Request.Builder()
                            .url(Constants.URL_INSERTING_FARMER)
                            .post(formBody)
                            .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                            .addHeader("content-type", "application/x-www-form-urlencoded")
                            .addHeader("cache-control", "no-cache")
                            .build();


                    try {
                        responses = client.newCall(request).execute();
                        String jsonData = responses.body().string();
                        System.out.println("!!!!!!!1 InsertRetailer" + jsonData);


                        if (jsonData != null) {
                            JSONArray jsonarray;
                            try {
                                JSONObject jsonobject = new JSONObject(jsonData);
                                System.out.println("!!!!!!!1 postexecuteInsertRetailer" + jsonData);

                                String status = jsonobject.getString("status");
                                if (status.equalsIgnoreCase("success")) {
                                    String exists = jsonobject.getString("exists");
                                    if (exists.equalsIgnoreCase("0")) {
                                        farmer.setPrimaryId(Integer.parseInt(jsonobject.getString("mobile_id")));
                                        farmer.setFfmId(jsonobject.getString("ffm_id"));
                                        farmer.setFarmerId(jsonobject.getString("farmer_id"));
                                        db.addFarmer(farmer);
                                    } else {
                                        db.deleteFarmer(Integer.parseInt(jsonobject.getString("mobile_id")));
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            Log.e("ServiceHandler", "Couldn't get any data from the url");
                            // Toast.makeText(getApplicationContext(),"No data found",Toast.LENGTH_LONG).show();
                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void pushRetailerThread() {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                prepareRetailerOfflineDataPush();
            }
        }, 5000);
    }

    private void prepareRetailerOfflineDataPush() {
        try {
            List<Retailer> offlineRetailers = db.getOfflineRetailers();
            for (int i = 0; i < offlineRetailers.size(); i++) {
                try {
                    Retailer retailer = offlineRetailers.get(i);
                    OkHttpClient client = new OkHttpClient();
                    /*For passing parameters*/

                    RequestBody formBody = new FormEncodingBuilder()
                            .add("mobile_id", String.valueOf(retailer.getID()))
                            .add("retailer_name", retailer.get_ret_name())
                            .add("retailer_tin_no", retailer.get_ret_tin_no())
                            .add("location_district", retailer.get_ret_district())
                            .add("location_taluka", retailer.get_ret_taluka())
                            .add("location_village", retailer.get_ret_village())
                            .add("retailer_gstin_no", retailer.get_ret_gstin_no())
                            .add("region_id", retailer.get_ret_region_id())
                            .add("mobile", retailer.get_ret_mobile())
                            .add("phone", retailer.get_ret_phone())
                            .add("email_id", retailer.get_email())
                            .add("distributor_id", retailer.get_ret_dist_id())
                            .add("status", retailer.get_ret_status())
                            .add("ffmid", retailer.get_ffmid())

//                        .add("customer_id", checkuid)
                            .build();

                    Response responses = null;

//                System.out.println("---- retailer data -----" + params[0] + params[1] + params[2] + params[3] + params[4] + params[5] + params[6] + params[7]);

                /*MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType,
                        "type=check_in_lat_lon&visit_type=1&user_id=7&latlon=17.4411%2C78.3911&visit_date=2016-12-05%2C18%3A27%3A30&check_in_time=18%3A27%3A30&tracking_id=0");*/
                    Request request = new Request.Builder()
                            .url(Constants.URL_INSERTING_RETAILER)
                            .post(formBody)
                            .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                            .addHeader("content-type", "application/x-www-form-urlencoded")
                            .addHeader("cache-control", "no-cache")
                            .build();


                    try {
                        responses = client.newCall(request).execute();
                        String jsonData = responses.body().string();
                        System.out.println("!!!!!!!1 InsertRetailer" + jsonData);
                        if (jsonData != null) {
                            JSONArray jsonarray;
                            try {
                                JSONObject jsonobject = new JSONObject(jsonData);
                                System.out.println("!!!!!!!1 postexecuteInsertRetailer" + jsonData);
                                String status = jsonobject.getString("status");
                                if (status.equalsIgnoreCase("success")) {
                                    String exists = jsonobject.getString("mobile_id");
                                    if (exists.equalsIgnoreCase("0")) {
                                        retailer.setID(Integer.parseInt(jsonobject.getString("mobile_id")));
                                        retailer.set_ffmid(jsonobject.getString("ffm_id"));
                                        retailer.set_ret_masterid(jsonobject.getString("retailer_id"));
                                        String retailerId = retailer.get_ret_masterid();
                                        db.addRetailers(retailer);
                                        DistributorsRetailerPojo distributorsRetailerPojo = new DistributorsRetailerPojo();
                                        distributorsRetailerPojo.distributorId = retailer.get_ret_dist_id();
                                        distributorsRetailerPojo.retailerId = String.valueOf(db.getSqlPrimaryKeyByFFMID(retailerId));
                                        db.insertDistributorRetailers(distributorsRetailerPojo);
                                    } else {
                                        db.deleteRetailer(Integer.parseInt(jsonobject.getString("mobile_id")));
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            Log.e("ServiceHandler", "Couldn't get any data from the url");
                            // Toast.makeText(getApplicationContext(),"No data found",Toast.LENGTH_LONG).show();
                        }
                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void prepareMarketPotentialDataAndPush() {
        List<MarketPotential> marketPotentials = db.getofflineMarketPotentials(Common.getTeamFromSP(this));
        if (marketPotentials != null && marketPotentials.size() > 0) {
            MarketPotentialReqVo marketPotentialReqVo = new MarketPotentialReqVo();
            marketPotentialReqVo.marketPotential = marketPotentials;
            new RetrofitRequester(this).sendRequest(Constants.URL_INSERTING_MARKET_POTENTIAL, marketPotentialReqVo, false);
        }

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.v(TAG, "onDestroy");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Nullable


    //Pushes the CheckIn,Check out,Path Data
    public void pushGeoThread() {                            //9
        Log.v(TAG, "pushPlannerThread");
        new Thread(new Runnable() {
            @Override
            public void run() {
                prepareGEOOfflineDataAndPush();
            }
        }).start();
    }

    public void getNewOrUpdatedServiceOrder() {             //7
        new Thread(new Runnable() {
            @Override
            public void run() {
                new Async_getNewOrUpdateserviceorders().execute();
            }
        }).start();
    }

    public void pushPaymentThread() {                       //11
        Log.v(TAG, "pushPaymentThread");
        new Thread(new Runnable() {
            @Override
            public void run() {
                preparePaymentOfflineDataAndPush();
            }
        }).start();
    }

    public void pushProfileImageThread() {                    //12
        Log.v(TAG, "pushPaymentThread");
        new Thread(new Runnable() {
            @Override
            public void run() {
                prepareProfileImageOfflineDataAndPush();
            }
        }).start();
    }

    //Pushes the Complaints regulatory saved in offline
    public void pushComplaintThread() {
        Log.v(TAG, "pushComplaintregThread");
        new Thread(new Runnable() {
            @Override
            public void run() {
                prepareComplainregOfflineDataAndPush();

            }
        }).start();
    }

    public void pushComplaintprodThread() {                   //5
        Log.v(TAG, "pushComplaintprodThread");
        new Thread(new Runnable() {
            @Override
            public void run() {

                prepareComplainprodOfflineDataAndPush();
            }
        }).start();
    }

    //Pushes the Feedback saved in offline
    public void pushFeedBackThread() {  //1
        Log.v(TAG, "pushFeedBackThread");
        new Thread(new Runnable() {
            @Override
            public void run() {
                prepareFeedBackOfflineDataAndPush();
            }
        }).start();
    }

    public void pushDailyDairyThread() {                   //8
        Log.v(TAG, "pushDailyDairyThread");
        new Thread(new Runnable() {
            @Override
            public void run() {

                prepareDailyDairyOfflineDataAndPush();
            }
        }).start();
    }

  /*  //Pushes the Check out,Path Data where checkin not null
    public void pushPlannerCheckinThread() {
        Log.v(TAG, "pushPlannerCheckinThread");
        new Thread(new Runnable() {
            @Override
            public void run() {
                prepareGEOOfflinecheckinDataAndPush();
            }
        }).start();
    }*/

    //Pushes the Advance booking data
    public void pushPlanerEventThread() {                       //10
        Log.v(TAG, "pushPlanerEventThread");
        new Thread(new Runnable() {
            @Override
            public void run() {
                preparePlanerEventAndPush();
            }
        }).start();
    }


    public void pushPlanerScheduleThread() {                        //6

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.v(TAG, "pushPlanerScheduleThread");
                preparePlanerScheduleAndPush();
            }
        }, 7000);

    }

    //Pushes the Service order data
    public void pushOrderIndentThread() {             //4
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.v(TAG, "pushOrderIndentThread");
                prepareOfflineOrderIndentDataAndPush();
            }
        }, 10000);

    }

    //Pushes the Advance booking data
    public void pushAdvanceBookingThread() {
        //  Looper.prepare();
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                Log.v(TAG, "pushAdvancebookingThread");
                prepareBookingOfflinecheckinDataAndPush();            //2
            }
        }, 10000);

    }


    //Pushes the Approve/Reject data
    public void pushApproveRejectThread() {                           //3
        Log.v(TAG, "pushApproveRejectThread");
        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {
            @Override
            public void run() {
                prepareApproveRejectOfflineDataAndPush();
            }
        }, 6000);
    }

    public void pushMICommodityPriceThread() {
//        Log.v(TAG, "pushFeedBackThread");
//        new Thread(new Runnable() {
//            @Override
//            public void run() {
//                prepareMICommodityPriceOfflineDataAndPush();
//            }
//        }).start();
    }

    public void pushMICropShiftsThread() {
        Log.v(TAG, "pushFeedBackThread");
        new Thread(new Runnable() {
            @Override
            public void run() {
                prepareMICropShiftsOfflineDataAndPush();
            }
        }).start();
    }

    public void pushMIPriceSurveyThread() {
        Log.v(TAG, "pushFeedBackThread");
        new Thread(new Runnable() {
            @Override
            public void run() {
                prepareMIPriceSurveyOfflineDataAndPush();
            }
        }).start();
    }

    public void pushMIProductSurveyThread() {
        Log.v(TAG, "pushFeedBackThread");
        new Thread(new Runnable() {
            @Override
            public void run() {
                prepareMIProductSurveyOfflineDataAndPush();
            }
        }).start();
    }


    public void pushDEMANDGENERATIONThread() {
        Log.v(TAG, "pushpushDEMANDGENERATIONThread");
        new Thread(new Runnable() {
            @Override
            public void run() {
                prepareEMANDGENERATIONAndpush();

                // ();anil imporatant            //replace this with coupans data
            }
        }).start();
    }
    public void pushFARMERCOUPANSTHREAD() {
        Log.v(TAG, "pushFARMERCOUPANSTHREAD");
        new Thread(new Runnable() {
            @Override
            public void run() {
                prepareFARMERCOUPANSAndpush(userId);

                // ();anil imporatant            //replace this with coupans data
            }
        }).start();
    }
    private void prepareOfflineOrderIndentDataAndPush() {


        try {
            List<ServiceOrderMaster> serviceOrderMasterList = db.getOfflineServiceorders();
            Log.i("  -json array -", "returned Anil" + serviceOrderMasterList.size());
            JSONArray mainArray = new JSONArray();
            JSONArray adbBookArray = new JSONArray();
            JSONObject naimObject = new JSONObject();
            if (serviceOrderMasterList == null || serviceOrderMasterList.size() == 0) {
                Log.i("  -json array -", "returned.." + mainArray.toString());
                return;

            }
            for (int i = 0; i < serviceOrderMasterList.size(); i++) {
                ServiceOrderMaster serviceOrderMaster = serviceOrderMasterList.get(i);

                JSONObject advBookObj = new JSONObject();

                advBookObj.put("CompanyID", serviceOrderMaster._serviceorder_company_id);
                advBookObj.put("customer_id", serviceOrderMaster._serviceorder_customer_id);
                advBookObj.put("DivisionID", serviceOrderMaster._serviceorder_division_id);

                advBookObj.put("id", serviceOrderMaster._serviceorder_masterid);
                advBookObj.put("user_id", serviceOrderMaster._serviceorder_user_id);
                advBookObj.put("created_by", userId);

                advBookObj.put("OrderDate", serviceOrderMaster._serviceorder_date);
                advBookObj.put("status", serviceOrderMaster._serviceorder_status);


                //JSONArray productArray = new JSONArray();
                // for (int k = 0; k < globalGroup.size(); k++) {
                JSONArray cropArray = new JSONArray();
                for (int m = 0; m < serviceOrderMaster.getServiceOrderDetailMasterListGrpByCropID().size(); m++) {
                    String sel_crop_id = "";
                    JSONArray productArray = new JSONArray();

                    for (int l = 0; l < serviceOrderMaster.getServiceOrderDetailMasterList().size(); l++) {
                        ServiceOrderDetailMaster serviceOrderDetailMaster = serviceOrderMaster.getServiceOrderDetailMasterList().get(l);
                        if (serviceOrderDetailMaster.get_serviceorderdetail_crop_id().equalsIgnoreCase(serviceOrderMaster.getServiceOrderDetailMasterListGrpByCropID().get(m).get_serviceorderdetail_crop_id())) {
                            JSONObject object_one = new JSONObject();

                            object_one.put("OrderPrice", serviceOrderDetailMaster.getserviceorderdetail_order_price());
                            object_one.put("ProductID", serviceOrderDetailMaster.getserviceorderdetail_product_id());
                            object_one.put("Qunatity", serviceOrderDetailMaster.get_serviceorderdetail_quantity());
                            object_one.put("mobile_serivce_details_id", serviceOrderDetailMaster.getserviceorderdetail_masterid());

                            productArray.put(object_one);
                        }
                    }


                    // for (int j = 0; j < globalGroup.size(); j++) {
                    JSONObject cropObj = new JSONObject();
                    sel_crop_id = serviceOrderMaster.getServiceOrderDetailMasterListGrpByCropID().get(m).get_serviceorderdetail_crop_id();
                    cropObj.put("Products", productArray);
                    cropObj.put("CropId", sel_crop_id);

                    cropArray.put(cropObj);


                }
                advBookObj.put("Crops", cropArray);
                adbBookArray.put(advBookObj);
                naimObject.put("salesorder", adbBookArray);
            }
            mainArray.put(naimObject);

            Log.i("  -json array -", "" + mainArray.toString());

            if (serviceOrderMasterList.size() > 0) {
                OkHttpClient client = new OkHttpClient();

                MediaType mediaType = MediaType.parse("application/octet-stream");
                RequestBody body = RequestBody.create(mediaType, mainArray.toString());
                Request request = new Request.Builder()
                        .url(Constants.URL_INSERTING_ORDERINDENT)
                        .post(body)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        // .addHeader("postman-token", "6bb3394a-3eaa-d4ae-cd37-0257afbdf3db")
                        .build();

                Response response = client.newCall(request).execute();
                String jsonData = response.body().string();
                System.out.println("!!!!!!!1Oreder Indent" + jsonData);


                if (jsonData != null) {
                    try {
                        System.out.println("!!!!!!!1 Service order post execute" + jsonData + "\n");
                        // JSONArray jsonArray=new JSONArray(jsonData);
                        //  String sdfds = new Gson().toJson(jsonData);
                        //  System.out.println("!!!!!!!1 Advance Booking" + sdfds+"\n"+s);
                        JSONArray jsonArray = toJson(jsonData);

                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);

                            String status = jsonObject.getString("status");
                            if (status.equalsIgnoreCase("success")) {
                                //Toast.makeText(getActivity(),"Complaints products inserted sucessfully",Toast.LENGTH_SHORT).show();

                                String service_id = jsonObject.getString("service_id");
                                String ffmid = jsonObject.getString("ffm_id");
                                String order_sap_id = jsonObject.getString("order_sap_id");
                                //   Toast.makeText(getActivity(), "sqlite id and ffm id " + service_id + " , " + ffmid, Toast.LENGTH_SHORT).show();
                                Log.i("NSL sqlite id", service_id);
                                Log.i("NSL ffmid", ffmid);
                                sdbw = db.getWritableDatabase();
                                // updateFeedback(Feedback feedback);
                                String updatequery = "UPDATE " + TABLE_SERVICEORDER + " SET " + KEY_TABLE_SERVICEORDER_FFM_ID + " = '" + ffmid + "'," + KEY_TABLE_SERVICEORDER_SAP_ID + " = '" + order_sap_id + "' WHERE " + KEY_TABLE_SERVICEORDER_ID + " = " + service_id;
                                sdbw.execSQL(updatequery);

                                JSONArray detailsArray = jsonObject.getJSONArray("details");
                                for (int k = 0; k < detailsArray.length(); k++) {
                                    String ffm_id = detailsArray.getJSONObject(k).getString("ffm_id");
                                    String order_detail_id = detailsArray.getJSONObject(k).getString("order_detail_id");
                                    db.updateSOD(ffm_id, order_detail_id);
//                                    String updatequery1 = "UPDATE " + TABLE_SERVICEORDERDETAILS + " SET " + KEY_TABLE_SERVICEORDER_DETAIL_MASTER_ID + " = " + ffm_id + " , " + KEY_TABLE_SERVICEORDER_FFM_ID + " = " + ffm_id + " WHERE " + KEY_TABLE_SERVICEORDER_DETAIL_ORDER_ID + " = " + order_detail_id;
//                                    sdbw.execSQL(updatequery1);
                                }
                                JSONArray approvalArray = jsonObject.getJSONArray("order_approval");
                                for (int j = 0; j < approvalArray.length(); j++) {
                                    JSONObject order_approval = approvalArray.getJSONObject(j);
                                    ServiceOrderApproval soa = new ServiceOrderApproval();
                                    soa.orderApprovalId = order_approval.getString("service_order_approval_id");
                                    soa.orderId = order_approval.getString("order_id");
                                    soa.orderStatus = order_approval.getString("order_status");
                                    soa.userId = order_approval.getString("user_id");
                                    soa.assignedUserId = order_approval.getString("assigned_user_id");
                                    soa.syncStatus = order_approval.getString("sync_status");
                                    soa.createdBy = order_approval.getString("created_by");
                                    soa.modifiedBy = order_approval.getString("modified_by");
                                    soa.cDateTime = order_approval.getString("created_date_time");
                                    soa.uDateTime = order_approval.getString("modified_date_time");
                                    soa.pendingBy = order_approval.getString("pending_by");
                                    db.insertServiceOrderApproval(soa);
                                }
                                JSONArray historyArray = jsonObject.getJSONArray("service_order_history");
                                for (int j = 0; j < historyArray.length(); j++) {
                                    JSONObject historyObj = historyArray.getJSONObject(j);
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

                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pushComplaintprodThread();
        }

        // pushAdvanceBookingThread();
    }


    private void preparePlannerOfflineDataAndPush() {


        Log.d("Reading: ", "Reading all payment collection..");

        String selectQuery = "SELECT DISTINCT "
                + KEY_TABLE_GEO_TRACKING_ID + ","
                + DatabaseHandler.KEY_TABLE_GEO_TRACKING_USER_ID + ","
                + DatabaseHandler.KEY_TABLE_GEO_TRACKING_CHECK_IN_LAT_LONG + ","
                + DatabaseHandler.KEY_TABLE_GEO_TRACKING_CHECK_OUT_LAT_LONG + ","
                + DatabaseHandler.KEY_TABLE_GEO_TRACKING_ROUTE_PATH_LAT_LONG + ","
                + DatabaseHandler.KEY_TABLE_GEO_TRACKING_DISTANCE + ","
                + DatabaseHandler.KEY_TABLE_GEO_TRACKING_VISIT_DATE + ","
                + DatabaseHandler.KEY_TABLE_GEO_TRACKING_CHECK_IN_TIME + ","
                + DatabaseHandler.KEY_TABLE_GEO_TRACKING_CHECK_OUT_TIME
                + " FROM " + DatabaseHandler.TABLE_GEO_TRACKING + " where " + DatabaseHandler.KEY_TABLE_GEO_TRACKING_USER_ID + " = " + sharedpreferences.getString("userId", null);
        //+ " and " + DatabaseHandler.KEY_TABLE_GEO_TRACKING_FFMID + " IS NULL" ;


        Log.e("selectQuery", selectQuery);
        sdbw = db.getWritableDatabase();
        Cursor cursor = sdbw.rawQuery(selectQuery, null);
        if (cursor != null && cursor.moveToFirst()) {
            do {

                String jsonData;
                OkHttpClient client = new OkHttpClient();
                /*For passing parameters*/
                RequestBody formBody = new FormEncodingBuilder()
                        .add("user_id", cursor.getString(1))
                        .add("table", "geo_tracking")
                        .add("mobile_id", cursor.getString(0))
                        .add("check_in_lat_lon", cursor.getString(2))
                        .add("check_out_lat_lon", cursor.getString(3))
                        .add("route_path_lat_lon", cursor.getString(4))
                        .add("visit_date", cursor.getString(6))
                        .add("check_in_time", cursor.getString(7))
                        .add("check_out_time", cursor.getString(8))
                        .build();

                Response responses = null;

                Request request = new Request.Builder()
                        .url(Constants.URL_INSERTING_PUSHTABLE)
                        .post(formBody)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();


                try {
                    responses = client.newCall(request).execute();
                    jsonData = responses.body().string();
                    System.out.println("!!!!!!!1 GEO inserting" + jsonData);

                    if (jsonData != null) {
                        JSONArray jsonarray;
                        try {
                            JSONObject jsonobject = new JSONObject(jsonData);
                            String status = jsonobject.getString("status");
                            if (status.equalsIgnoreCase("success")) {


                                String sqliteid = jsonobject.getString("sqlite");
                                String ffmid = jsonobject.getString("ffm_id");
                                //   Toast.makeText(getActivity(), "sqlite id and ffm id " + sqliteid + " , " + ffmid, Toast.LENGTH_SHORT).show();

                                SQLiteDatabase sql = db.getWritableDatabase();
                                // updatecomplaints
                                String updatequery = "UPDATE " + DatabaseHandler.TABLE_GEO_TRACKING + " SET " + DatabaseHandler.KEY_TABLE_GEO_TRACKING_FFMID + " = " + ffmid + " WHERE " + DatabaseHandler.KEY_TABLE_GEO_TRACKING_ID + " = " + sqliteid;
                                sql.execSQL(updatequery);
                                Log.v("UPDATE", "UPDATE Success Geo tracking");

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        Log.e("ServiceHandler", "Couldn't get any data from the url");
                        // Toast.makeText(getApplicationContext(),"No data found",Toast.LENGTH_LONG).show();
                    }


                } catch (IOException e) {
                    e.printStackTrace();
                }


            } while (cursor.moveToNext());
        } else Log.d("LOG", "returned null!");


    }

    private void prepareGEOOfflineDataAndPush() {

        try {
            Log.d("Reading: ", "Reading all geo tracking..");

            List<Geo_Tracking_POJO> geo_tracking_pojo = db.getUnSyncAllGeotracking(userId);

            Log.e("list size", String.valueOf(geo_tracking_pojo.size()));

            if (geo_tracking_pojo.size() > 0) {


                for (Geo_Tracking_POJO geo : geo_tracking_pojo) {

                    if (geo.getGeo_route_path_lat_lon() != null && !geo.getGeo_route_path_lat_lon().equalsIgnoreCase("") && !geo.getGeo_route_path_lat_lon().equalsIgnoreCase("null") && geo.getGeo_route_path_lat_lon().length() > 8) {
                        new Async_Routepath().execute(geo.getGeo_route_path_lat_lon(), geo.getGeoffmid(), geo.getPause(), geo.getResume());

                    }


        /*        if (geo.getGeoffmid() == null || geo.getGeoffmid() == "" || geo.getGeoffmid() == "0") {

                    String jsonData;
                    OkHttpClient client = new OkHttpClient();
                        *//*For passing parameters*//*
                    RequestBody formBody = new FormEncodingBuilder()
                            .add("user_id", geo.get_Geo_user_id())
                            .add("table", "geo_tracking")
                            .add("mobile_id", String.valueOf(geo.getID()))
                            .add("check_in_lat_lon", geo.get_Geo_check_in_lat_lon())
                            .add("check_out_lat_lon", geo.getGeo_check_out_lat_lon())
                            .add("route_path_lat_lon", geo.getGeo_route_path_lat_lon())
                            .add("visit_date", geo.getGeo_visit_date())
                            .add("check_in_time", geo.getGeo_check_in_time())
                            .add("check_out_time", checkOutTime)
                            .build();

                    Response responses = null;

                    Request request = new Request.Builder()
                            .url(Constants.URL_INSERTING_PUSHTABLE)
                            .post(formBody)
                            .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                            .addHeader("content-type", "application/x-www-form-urlencoded")
                            .addHeader("cache-control", "no-cache")
                            .build();


                    try {
                        responses = client.newCall(request).execute();
                        jsonData = responses.body().string();
                        System.out.println("!!!!!!!1 GEO inserting" + jsonData);

                        if (jsonData != null) {
                            JSONArray jsonarray;
                            try {
                                JSONObject jsonobject = new JSONObject(jsonData);
                                String status = jsonobject.getString("status");
                                if (status.equalsIgnoreCase("success")) {


                                    String sqliteid = jsonobject.getString("sqlite");
                                    String ffmid = jsonobject.getString("ffm_id");

                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.putString("tracking_id", ffmid);
                                    editor.commit();

                                    //   Toast.makeText(getActivity(), "sqlite id and ffm id " + sqliteid + " , " + ffmid, Toast.LENGTH_SHORT).show();

                                    SQLiteDatabase sql = db.getWritableDatabase();
                                    // updatecomplaints
                                    String updatequery = "UPDATE " + DatabaseHandler.TABLE_GEO_TRACKING + " SET " + DatabaseHandler.KEY_TABLE_GEO_TRACKING_FFMID + " = " + ffmid + " WHERE " + DatabaseHandler.KEY_TABLE_GEO_TRACKING_ID + " = " + sqliteid;
                                    sql.execSQL(updatequery);
                                    Log.v("UPDATE", "UPDATE Success Geo tracking");

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            Log.e("ServiceHandler", "Couldn't get any data from the url");
                            // Toast.makeText(getApplicationContext(),"No data found",Toast.LENGTH_LONG).show();
                        }


                    } catch (IOException e) {
                        e.printStackTrace();
                    }

                } else {


                }
*/

                }


            }
        } catch (Exception e) {

        } finally {
            pushPlanerEventThread();
        }
    }

    private void prepareGEOOfflinecheckinDataAndPush() {
        checkuid = sharedpreferences.getString("userId", "");

        Log.d("Reading: ", "Reading all geo tracking checkin..");

        List<Geo_Tracking_POJO> geo_tracking_pojo = db.getAllGeotrackingwithcheckin(checkuid);

        Log.e("list size", String.valueOf(geo_tracking_pojo.size()));

        if (geo_tracking_pojo.size() > 0) {


            for (Geo_Tracking_POJO geo : geo_tracking_pojo) {


                String jsonData;
                OkHttpClient client = new OkHttpClient();
                /*For passing parameters*/
                RequestBody formBody = new FormEncodingBuilder()
                        .add("tracking_id", geo.getGeoffmid())
                        .add("check_out_lat_lon", geo.getGeo_check_out_lat_lon())
                        .add("route_path_lat_lon", geo.getGeo_route_path_lat_lon())
                        .add("check_out_time", geo.getGeo_check_out_time())
                        .build();

                Response responses = null;

                Request request = new Request.Builder()
                        .url(Constants.URL_INSERTING_GEO_PUSH_FULL_PATH)
                        .post(formBody)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();


                try {
                    responses = client.newCall(request).execute();
                    jsonData = responses.body().string();
                    // System.out.println("!!!!!!!1 GEO inserting" + jsonData);

                    /*if (jsonData != null) {
                        JSONArray jsonarray;
                        try {
                            JSONObject jsonobject = new JSONObject(jsonData);
                            String status = jsonobject.getString("status");
                            if (status.equalsIgnoreCase("success")) {


                                String sqliteid = jsonobject.getString("sqlite");
                                String ffmid = jsonobject.getString("ffm_id");

                                //   Toast.makeText(getActivity(), "sqlite id and ffm id " + sqliteid + " , " + ffmid, Toast.LENGTH_SHORT).show();

                                SQLiteDatabase sql = db.getWritableDatabase();
                                // updatecomplaints
                                String updatequery = "UPDATE " + DatabaseHandler.TABLE_GEO_TRACKING + " SET " + DatabaseHandler.KEY_TABLE_GEO_TRACKING_FFMID + " = " + ffmid + " WHERE " + DatabaseHandler.KEY_TABLE_GEO_TRACKING_ID + " = " + sqliteid;
                                sql.execSQL(updatequery);
                                Log.v("UPDATE", "UPDATE Success Geo tracking");

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        Log.e("ServiceHandler", "Couldn't get any data from the url");
                        // Toast.makeText(getApplicationContext(),"No data found",Toast.LENGTH_LONG).show();
                    }*/


                } catch (IOException e) {
                    e.printStackTrace();
                }

            }


        }


        Log.e(" ##### ", " Push planer event");
    }

    private void preparePaymentOfflineDataAndPush() {
        try {
            checkuid = sharedpreferences.getString("userId", "");

            Log.d("Reading: ", "Reading all Payment collection..");

            List<Payment_collection> paymentCollections = null;
            try {
                paymentCollections = db.getAllPaymentCollection(checkuid);
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
            }

            //  Log.e("list size", String.valueOf(paymentCollections.size()));

            if (paymentCollections != null && paymentCollections.size() > 0) {


                for (Payment_collection pc : paymentCollections) {
                    String log = "Id: " + pc.getID() + " ,pay_type: " + pc.get_payment_type() + " ,userId: " + pc.get_user_id() +
                            " ,comp_id: " + pc.get_company_id() + " ,div_id: " + pc.get_division_id() + " ,cust_id: " + pc.get_customer_id() +
                            " ,amt : " + pc.get_total_amount() + " ,paymode:" + pc.get_payment_mode() + ",bank name:" + pc.get_bank_name() +
                            "rtgs:" + pc.get_rtgs_or_neft_no() + ",date time:" + pc.get_payment_datetime() + ",date on cheque:" + pc.get_date_on_cheque_no() +
                            ",cheque number:" + pc.get_cheque_no_dd_no() + ",status:" + pc.get_status() + ",created datetime:" + pc.get_created_datetime() +
                            ",updated datetime:" + pc.get_updated_datetime() + ",ffmid:" + pc.get_ffmid();

                    Log.e("payment collection: ", log);
                    String payment_type = pc.get_payment_type();

                    try {
                        String jsonData = null;
                        Response responses = null;

                        OkHttpClient client = new OkHttpClient();
                        /*For passing parameters*/
                        RequestBody formBody = new FormEncodingBuilder()
                                .add("id", String.valueOf(pc.getID()))
                                .add("sqlite_id", String.valueOf(pc.getID()))
                                .add("payment_type", String.valueOf(payment_type))
                                .add("user_id", String.valueOf(pc.get_user_id()))
                                .add("bank_name", pc.get_bank_name())
                                .add("company_id", String.valueOf(pc.get_company_id()))
                                .add("division_id", String.valueOf(pc.get_division_id()))
                                .add("payment_datetime", pc.get_payment_datetime())
                                .add("customer_id", String.valueOf(pc.get_customer_id()))
                                .add("total_amount", String.valueOf(pc.get_total_amount()))
                                .add("payment_mode", String.valueOf(pc.get_payment_mode()))
                                .add("rtgs_or_neft_no", String.valueOf(pc.get_rtgs_or_neft_no()))
                                .add("created_by", String.valueOf(pc.get_user_id()))
                                .add("cheque_no_dd_no", String.valueOf(pc.get_cheque_no_dd_no()))
                                .add("date_on_cheque_no", String.valueOf(pc.get_date_on_cheque_no()))
                                .build();

                        Request request = new Request.Builder()
                                .url(Constants.URL_INSERTING_PAYMENT_COLLECTION)
                                .post(formBody)
                                .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                                .addHeader("content-type", "application/x-www-form-urlencoded")
                                .addHeader("cache-control", "no-cache")
                                .build();


                        try {
                            responses = client.newCall(request).execute();
                            jsonData = responses.body().string();
                            System.out.println("!!!!!!!1 InsertPayment" + jsonData);

                            if (jsonData != null) {
                                JSONArray jsonarray;
                                try {
                                    JSONObject jsonobject = new JSONObject(jsonData);
                                    String status = jsonobject.getString("status");

                                    if (status.equalsIgnoreCase("success")) {
                                        String sqliteid = jsonobject.getString("sqlite");
                                        String ffmid = jsonobject.getString("ffm_id");

                                        Log.e("sqlite id", sqliteid);
                                        Log.e("ffmid", ffmid);
                                        sdbw = db.getWritableDatabase();
                                        // updateFeedback(Feedback feedback);
                                        String updatequery = "UPDATE " + TABLE_PAYMENT_COLLECTION + " SET " + KEY_PAYMENT_COLLECTION_FFMID + " = " + ffmid + " WHERE " + KEY_PAYMENT_COLLECTION_PAYMENT_ID + " = " + sqliteid;
                                        sdbw.execSQL(updatequery);
                                        System.out.println(updatequery);

                                        List<Payment_collection> paymentListData = db.getAllPaymentCollection(checkuid);

                                        for (Payment_collection p : paymentListData) {
                                            log = "Id: " + pc.getID() + " ,pay_type: " + p.get_payment_type() + " ,userId: " + pc.get_user_id() + " ,comp_id: " + pc.get_company_id() + " ,div_id: " + pc.get_division_id() + " ,cust_id: " + pc.get_customer_id() + " ,amt : " + pc.get_total_amount()
                                                    + " ,paymode:" + pc.get_payment_mode() + ",bank name:" + pc.get_bank_name() + "rtgs:" + pc.get_rtgs_or_neft_no() + ",date time:" + pc.get_payment_datetime() + ",date on cheque:" + pc.get_date_on_cheque_no() + ",cheque number:" + pc.get_cheque_no_dd_no() +
                                                    ",status:" + pc.get_status() + ",created datetime:" + pc.get_created_datetime() + ",updated datetime:" + pc.get_updated_datetime() + ",ffmid:" + pc.get_ffmid();

                                            Log.e("paymentcollectionaftr: ", log);
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            }

                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                }
            }
        } catch (Exception e) {

        } finally {
            pushProfileImageThread();
        }


    }


    private void prepareComplainregOfflineDataAndPush() {
        checkuid = sharedpreferences.getString("userId", "");
        Log.d("Reading: ", "Reading all complaintsreg..");
        List<Complaints> complaints = db.getAllComplaintsregulatory(checkuid);

        Log.e("list size", String.valueOf(complaints.size()));

        if (complaints.size() > 0) {


            for (Complaints cm : complaints) {
                String log = "Id: " + cm.getID() + " ,company: " + cm.getCompanyId() + " ,div: " + cm.get_division_id() + ",crop:" + cm.getCropid() + " ,product: " + cm.getProductid() + ",mkt_lot:" +
                        cm.get_marketing_lot_number() + ",comptype" + cm.get_complaint_type() + ",farmer:" + cm.get_farmer_name() + ",contact" + cm.get_contact_no() + ",complarea" + cm.get_complaint_area_acres()
                        + ",soiltype" + cm.get_soil_type() + ",others" + cm.get_others() + ",purqty" + cm.get_purchased_quantity() + ",cmpqty" + cm.get_complaint_quantity() + ",purdate" + cm.get_purchase_date() + ",billnum" + cm.get_bill_number()
                        + ",retlrname" + cm.get_retailer_name() + ",distributor" + cm.get_distributor() + ",mandal" + cm.get_mandal() + ",village" + cm.get_village() + "image:" + cm.get_image() + ",regtype" + cm.get_regulatory_type() + ",samplingdate" + cm.get_sampling_date()
                        + ",samplingplace" + cm.get_place_sampling() + ",ofcrname" + cm.get_sampling_officer_name() + ",ofcrcontact" + cm.get_sampling_officer_contact() + ",comments" + cm.get_comments() + ",status" + cm.get_status() +
                        ",createddatetime" + cm.get_created_datetime() + ",updateddatetime" + cm.get_updated_datetime() + ",ffmid " + cm.get_ffmid();
                Log.e("complaintsbefore: ", log);

                try {


                    String imageStr1 = "";
                    String imageStr2 = "";
                    String imageStr3 = "";
                    String imageStr4 = "";
                    String imageStr5 = "";
                    String imageStr6 = "";

                    try {
                        JSONArray jsonArray = new JSONArray(cm.get_image());

                        //  String[] imagesDb = fb.getImage().split(",nsl,");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String image = jsonObject.getString("image_64");

                            if (i == 0) {
                                imageStr1 = image;
                            }
                            if (i == 1) {
                                imageStr2 = image;
                            }
                            if (i == 2) {
                                imageStr3 = image;
                            }
                            if (i == 3) {
                                imageStr4 = image;
                            }
                            if (i == 4) {
                                imageStr5 = image;
                            }
                            if (i == 5) {
                                imageStr6 = image;
                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                    String jsonData = null;
                    Response responses = null;

                    OkHttpClient client = new OkHttpClient();
                    /*For passing parameters*/
                    RequestBody formBody = new FormEncodingBuilder()
                            .add("id", String.valueOf(cm.getID()))
                            .add("user_id", String.valueOf(cm.get_user_id()))
                            .add("company_id", String.valueOf(cm.getCompanyId()))
                            .add("division_id", String.valueOf(cm.get_division_id()))
                            .add("complaint_type", "regulatory")
                            .add("crop_id", String.valueOf(cm.getCropid()))
                            .add("product_id", String.valueOf(cm.getProductid()))
                            .add("marketing_lot_number", cm.get_marketing_lot_number())
                            .add("regulatory_type", cm.get_regulatory_type())
                            .add("sampling_date", cm.get_sampling_date())
                            .add("place_sampling", cm.get_place_sampling())
                            .add("retailer_name", cm.get_retailer_name())
                            .add("distributor", String.valueOf(cm.get_distributor()))
                            .add("sampling_officer_name", cm.get_sampling_officer_name())
                            .add("sampling_officer_contact", cm.get_sampling_officer_contact())
                            .add("comments", cm.get_comments())
                            .add("image_1", imageStr1)
                            .add("image_2", imageStr2)
                            .add("image_3", imageStr3)
                            .add("image_4", imageStr4)
                            .add("image_5", imageStr5)
                            .add("image_6", imageStr6)

                            .build();

                    Request request = new Request.Builder()
                            .url(Constants.URL_INSERTING_COMPLAINTS)
                            .post(formBody)
                            .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                            .addHeader("content-type", "application/x-www-form-urlencoded")
                            .addHeader("cache-control", "no-cache")
                            .build();


                    try {
                        responses = client.newCall(request).execute();
                        jsonData = responses.body().string();
                        System.out.println("!!!!!!!1 InsertComplaintsregulatory" + jsonData);

                        if (jsonData != null) {
                            JSONArray jsonarray;
                            try {
                                JSONObject jsonobject = new JSONObject(jsonData);
                                String status = jsonobject.getString("status");
                                if (status.equalsIgnoreCase("success")) {
                                    String sqliteid = jsonobject.getString("sqlite");
                                    String ffmid = jsonobject.getString("ffm_id");

                                    Log.e("sqlite id", sqliteid);
                                    Log.e("ffmid", ffmid);
                                    sdbw = db.getWritableDatabase();
                                    // updateFeedback(Feedback feedback);
                                    String updatequery = "UPDATE " + TABLE_COMPLAINT + " SET " + KEY_TABLE_COMPLAINTS_FFMID + " = " + ffmid + " WHERE " + KEY_TABLE_COMPLAINTS_ID + " = " + sqliteid;
                                    sdbw.execSQL(updatequery);
                                    System.out.println(updatequery);
                                    List<Complaints> complaintsdata = db.getAllComplaintsregulatory(checkuid);

                                    for (Complaints cmp : complaintsdata) {
                                        log = "Id: " + cm.getID() + " ,company: " + cmp.getCompanyId() + " ,div: " + cm.get_division_id() + ",crop:" + cm.getCropid() + " ,product: " + cm.getProductid() + ",mkt_lot:" +
                                                cm.get_marketing_lot_number() + ",comptype" + cm.get_complaint_type() + ",farmer:" + cm.get_farmer_name() + ",contact" + cm.get_contact_no() + ",complarea" + cm.get_complaint_area_acres()
                                                + ",soiltype" + cm.get_soil_type() + ",others" + cm.get_others() + ",purqty" + cm.get_purchased_quantity() + ",cmpqty" + cm.get_complaint_quantity() + ",purdate" + cm.get_purchase_date() + ",billnum" + cm.get_bill_number()
                                                + ",retlrname" + cm.get_retailer_name() + ",distributor" + cm.get_distributor() + ",mandal" + cm.get_mandal() + ",village" + cm.get_village() + "image:" + cm.get_image() + ",regtype" + cm.get_regulatory_type() + ",samplingdate" + cm.get_sampling_date()
                                                + ",samplingplace" + cm.get_place_sampling() + ",ofcrname" + cm.get_sampling_officer_name() + ",ofcrcontact" + cm.get_sampling_officer_contact() + ",comments" + cm.get_comments() + ",status" + cm.get_status() +
                                                ",createddatetime" + cm.get_created_datetime() + ",updateddatetime" + cm.get_updated_datetime() + ",ffmid " + cm.get_ffmid();
                                        Log.e("complaintsafterupdate: ", log);


                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }

        }

        //pushComplaintprodThread();
    }


    private void prepareComplainprodOfflineDataAndPush() {
        try {
            checkuid = sharedpreferences.getString("userId", "");
            Log.d("Reading: ", "Reading all complaintsprod..");
            List<Complaints> complaints = db.getAllComplaintsproducts(Common.getTeamFromSP(this));

            Log.e("list size", String.valueOf(complaints.size()));

            if (complaints.size() > 0) {


                for (Complaints cm : complaints) {
                    String log = "Id: " + cm.getID() + " ,company: " + cm.getCompanyId() + " ,div: " + cm.get_division_id() + ",crop:" + cm.getCropid() + " ,product: " + cm.getProductid() + ",mkt_lot:" +
                            cm.get_marketing_lot_number() + ",comptype" + cm.get_complaint_type() + ",farmer:" + cm.get_farmer_name() + ",contact" + cm.get_contact_no() + ",complarea" + cm.get_complaint_area_acres()
                            + ",soiltype" + cm.get_soil_type() + ",others" + cm.get_others() + ",purqty" + cm.get_purchased_quantity() + ",cmpqty" + cm.get_complaint_quantity() + ",purdate" + cm.get_purchase_date() + ",billnum" + cm.get_bill_number()
                            + ",retlrname" + cm.get_retailer_name() + ",distributor" + cm.get_distributor() + ",mandal" + cm.get_mandal() + ",village" + cm.get_village() + "image:" + cm.get_image() + ",regtype" + cm.get_regulatory_type() + ",samplingdate" + cm.get_sampling_date()
                            + ",samplingplace" + cm.get_place_sampling() + ",ofcrname" + cm.get_sampling_officer_name() + ",ofcrcontact" + cm.get_sampling_officer_contact() + ",comments" + cm.get_comments() + ",status" + cm.get_status() +
                            ",createddatetime" + cm.get_created_datetime() + ",updateddatetime" + cm.get_updated_datetime() + ",ffmid " + cm.get_ffmid();
                    Log.e("complaintsbefore: ", log);


                    ComplaintReq complaintReq = new ComplaintReq();
                    complaintReq.id = String.valueOf(cm.getID());
                    complaintReq.companyId = String.valueOf(cm.getCompanyId());
                    complaintReq.divisionId = String.valueOf(cm.get_division_id());
                    complaintReq.cropId = String.valueOf(cm.getCropid());
                    complaintReq.productId = String.valueOf(cm.getProductid());
                    complaintReq.marketingLotNumber = cm.get_marketing_lot_number();
                    complaintReq.others = cm.get_others();
                    complaintReq.farmerName = cm.get_farmer_name();
                    complaintReq.contactNo = cm.get_contact_no();
                    complaintReq.complaintAreaAcres = cm.get_complaint_area_acres();
                    complaintReq.soilType = cm.get_soil_type();
                    complaintReq.purchasedQuantity = cm.get_purchased_quantity();
                    complaintReq.complaintQuantity = cm.get_complaint_quantity();
                    complaintReq.purchaseDate = cm.get_purchase_date();
                    complaintReq.billNumber = cm.get_bill_number();
                    complaintReq.retailerName = cm.get_retailer_name();
                    complaintReq.retailerId = cm.getRetailerId();
                    complaintReq.distributor = String.valueOf(cm.get_distributor());
//                complaintReq.mandal = cm.get_mandal();
//                complaintReq.village = cm.get_village();
                    complaintReq.comments = cm.get_comments();
                    complaintReq.userId = String.valueOf(cm.get_user_id());
                    complaintReq.complaintType = cm.get_complaint_type();
                    complaintReq.complaintPercentage = cm.getComplaintPercentage();
                    complaintReq.complaintRemarks = cm.getComplaintRemarks();
//                complaintReq.zone = cm.getZone();
//                complaintReq.region = cm.getRegion();
//                complaintReq.dealer = cm.getDealer();
                    complaintReq.performanceType = cm.getPerformanceLot();
//                complaintReq.state = cm.getState();
                    complaintReq.dateOfSowing = cm.getDateOfSowing();
                    complaintReq.dateOfComplaint = cm.getDateOfComplaint();
                    complaintReq.inspectedDate = cm.getInspectedDate();
                    String images = cm.get_image();
                    List<File> files = new ArrayList<>();
                    if (images != null) {
                        String[] imagePaths = images.split(",");
                        for (int i = 0; i < imagePaths.length; i++) {
                            files.add(new File(imagePaths[i]));
                        }
                    }
                    sendImage(complaintReq, null, files);


                 /* try {


                    String imageStr1 = "";
                    String imageStr2 = "";
                    String imageStr3 = "";
                    String imageStr4 = "";
                    String imageStr5 = "";
                    String imageStr6 = "";

                    try {
                        JSONArray jsonArray = new JSONArray(cm.get_image());

                        //  String[] imagesDb = fb.getImage().split(",nsl,");
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonObject = jsonArray.getJSONObject(i);
                            String image = jsonObject.getString("image_64");

                            if (i == 0) {
                                imageStr1 = image;
                            }
                            if (i == 1) {
                                imageStr2 = image;
                            }
                            if (i == 2) {
                                imageStr3 = image;
                            }
                            if (i == 3) {
                                imageStr4 = image;
                            }
                            if (i == 4) {
                                imageStr5 = image;
                            }
                            if (i == 5) {
                                imageStr6 = image;
                            }

                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                  String jsonData = null;
                    Response responses = null;

                    OkHttpClient client = new OkHttpClient();
                     *//*For passing parameters*//*
                    RequestBody formBody = new FormEncodingBuilder()
                            .add("id", String.valueOf(cm.getID()))
                            .add("user_id", String.valueOf(cm.get_user_id()))
                            .add("company_id", String.valueOf(cm.getCompanyId()))
                            .add("division_id", String.valueOf(cm.get_division_id()))
                            .add("complaint_type", "product")
                            .add("crop_id", String.valueOf(cm.getCropid()))
                            .add("product_id", String.valueOf(cm.getProductid()))
                            .add("marketing_lot_number", cm.get_marketing_lot_number())
                            .add("others", cm.get_others())
                            .add("farmer_name", cm.get_farmer_name())
                            .add("contact_no", cm.get_contact_no())
                            .add("complaint_area_acres", cm.get_complaint_area_acres())
                            .add("soil_type", String.valueOf(cm.get_soil_type()))
                            .add("purchased_quantity", cm.get_purchased_quantity())
                            .add("complaint_quantity", cm.get_complaint_quantity())
                            .add("purchase_date", cm.get_purchase_date())
                            .add("bill_number", cm.get_bill_number())
                            .add("retailer_name", cm.get_retailer_name())
                            .add("distributor", String.valueOf(cm.get_distributor()))
                            .add("mandal", cm.get_mandal())
                            .add("village", cm.get_village())
                            .add("comments", cm.get_comments())
                            .add("image_1", imageStr1)
                            .add("image_2", imageStr2)
                            .add("image_3", imageStr3)
                            .add("image_4", imageStr4)
                            .add("image_5", imageStr5)
                            .add("image_6", imageStr6)

                            .build();

                    Request request = new Request.Builder()
                            .url(Constants.URL_INSERTING_COMPLAINTS)
                            .post(formBody)
                            .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                            .addHeader("content-type", "application/x-www-form-urlencoded")
                            .addHeader("cache-control", "no-cache")
                            .build();


                    try {
                        responses = client.newCall(request).execute();
                        jsonData = responses.body().string();
                        System.out.println("!!!!!!!1 InsertComplaintsproduct" + jsonData);

                        if (jsonData != null) {
                            JSONArray jsonarray;
                            try {
                                JSONObject jsonobject = new JSONObject(jsonData);
                                String status = jsonobject.getString("status");
                                if (status.equalsIgnoreCase("success")) {
                                    String sqliteid = jsonobject.getString("sqlite");
                                    String ffmid = jsonobject.getString("ffm_id");

                                    Log.e("sqlite id", sqliteid);
                                    Log.e("ffmid", ffmid);
                                    sdbw = db.getWritableDatabase();
                                    // updateFeedback(Feedback feedback);
                                    String updatequery = "UPDATE " + TABLE_COMPLAINT + " SET " + KEY_TABLE_COMPLAINTS_FFMID + " = " + ffmid + " WHERE " + KEY_TABLE_COMPLAINTS_ID + " = " + sqliteid;
                                    sdbw.execSQL(updatequery);
                                    System.out.println(updatequery);
                                    List<Complaints> complaintsdata = db.getAllComplaintsregulatory(checkuid);

                                    for (Complaints cmp : complaintsdata) {
                                        log = "Id: " + cm.getID() + " ,company: " + cmp.getCompanyId() + " ,div: " + cm.get_division_id() + ",crop:" + cm.getCropid() + " ,product: " + cm.getProductid() + ",mkt_lot:" +
                                                cm.get_marketing_lot_number() + ",comptype" + cm.get_complaint_type() + ",farmer:" + cm.get_farmer_name() + ",contact" + cm.get_contact_no() + ",complarea" + cm.get_complaint_area_acres()
                                                + ",soiltype" + cm.get_soil_type() + ",others" + cm.get_others() + ",purqty" + cm.get_purchased_quantity() + ",cmpqty" + cm.get_complaint_quantity() + ",purdate" + cm.get_purchase_date() + ",billnum" + cm.get_bill_number()
                                                + ",retlrname" + cm.get_retailer_name() + ",distributor" + cm.get_distributor() + ",mandal" + cm.get_mandal() + ",village" + cm.get_village() + "image:" + cm.get_image() + ",regtype" + cm.get_regulatory_type() + ",samplingdate" + cm.get_sampling_date()
                                                + ",samplingplace" + cm.get_place_sampling() + ",ofcrname" + cm.get_sampling_officer_name() + ",ofcrcontact" + cm.get_sampling_officer_contact() + ",comments" + cm.get_comments() + ",status" + cm.get_status() +
                                                ",createddatetime" + cm.get_created_datetime() + ",updateddatetime" + cm.get_updated_datetime() + ",ffmid " + cm.get_ffmid();
                                        Log.e("complaintsafterupdate: ", log);


                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }*/


                }

            }
        } catch (Exception e) {

        } finally {
            pushPlanerScheduleThread();
        }
        //pushFeedBackThread();

    }

    private void sendImage(final ComplaintReq complaintReq, final FeedbackReq feedbackReq, List<File> files) {
        List<MultipartBody.Part> muPartList = new ArrayList<>();
        if (files != null) {
            for (int i = 0; i < files.size(); i++) {
                muPartList.add(prepareFilePart("image[]", Uri.fromFile(files.get(i)), files.get(i)));
            }

        }
        MultipartBody.Part[] fileParts = muPartList.toArray(new MultipartBody.Part[muPartList.size()]);

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
        RetrofitAPI apiInterface = retrofit.create(RetrofitAPI.class);
        Call<ResponseBody> responseBodyCall = null;
        if (complaintReq != null)
            responseBodyCall = apiInterface.uploadProfile("complaint", fileParts, complaintReq);
        if (feedbackReq != null)
            responseBodyCall = apiInterface.uploadProfile("feedback", fileParts, feedbackReq);
        responseBodyCall.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<okhttp3.ResponseBody> call, retrofit2.Response<okhttp3.ResponseBody> response) {
                try {
                    String jsonData = response.body().string();
                    if (jsonData != null) {
                        JSONArray jsonarray;
                        try {
                            JSONObject jsonobject = new JSONObject(jsonData);
                            String status = jsonobject.getString("status");
                            if (status.equalsIgnoreCase("success")) {
                                if (complaintReq != null) {
                                    String sqliteid = jsonobject.getString("sqlite");
                                    String ffmid = jsonobject.getString("ffm_id");
                                    String image_url = jsonobject.getString("image_url");
                                    int complaintStatus = jsonobject.getInt("complaint_status");
                                    int stages = jsonobject.getInt("stages");
                                    Log.e("sqlite id", sqliteid);
                                    Log.e("ffmid", ffmid);
                                    SQLiteDatabase sql = db.getWritableDatabase();
                                    // updatecomplaints
                                    String updatequery = "UPDATE " + TABLE_COMPLAINT + " SET " + KEY_TABLE_COMPLAINTS_FFMID + " = " + ffmid + " , " + KEY_TABLE_COMPLAINTS_STATUS + " = " + complaintStatus + " , " + KEY_TABLE_COMPLAINTS_STAGES + " = " + stages + " , " + KEY_TABLE_COMPLAINTS_IMAGE_UPLOAD + " = '" + image_url + "' WHERE " + KEY_TABLE_COMPLAINTS_ID + " = " + sqliteid;
                                    sql.execSQL(updatequery);
//                                    sql.close();
                                }
                                if (feedbackReq != null) {
                                    String sqliteid = jsonobject.getString("sqlite");
                                    int ffmid = Integer.parseInt(jsonobject.getString("ffm_id"));
                                    String image_url = jsonobject.getString("image_url");

                                    //   Toast.makeText(getActivity(), "sqlite id and ffm id " + sqliteid + " , " + ffmid, Toast.LENGTH_SHORT).show();
                                    sdbw = db.getWritableDatabase();
                                    // updateFeedback(Feedback feedback);
                                    String updatequery = "UPDATE " + TABLE_FEEDBACK + " SET " + KEY_TABLE_FEEDBACK_FFMID + " = " + ffmid + " , " + KEY_TABLE_FEEDBACK_IMAGE + " = '" + image_url + "' WHERE " + KEY_TABLE_FEEDBACK_FEEDBACK_ID + " = '" + sqliteid + "'";
                                    sdbw.execSQL(updatequery);
//                                    sdbw.close();

                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        Log.e("ServiceHandler", "Couldn't get any data from the url");
                        // Toast.makeText(getApplicationContext(),"No data found",Toast.LENGTH_LONG).show();
                    }
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<okhttp3.ResponseBody> call, Throwable t) {
            }
        });


    }

    private MultipartBody.Part prepareFilePart(String file_i, Uri uri, File file) {
        okhttp3.RequestBody requestFile =
                okhttp3.RequestBody.create(okhttp3.MediaType.parse(getMimeType(this, uri)), file);
        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(file_i, file.getName(), requestFile);
    }

    private String getMimeType(Context context, Uri uri) {
        String extension;

        //Check uri format to avoid null
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //If scheme is a content
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(context.getContentResolver().getType(uri));
        } else {
            //If scheme is a File
            //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());

        }

        return extension;
    }

    private void prepareFeedBackOfflineDataAndPush() {
        try {
            checkuid = sharedpreferences.getString("userId", "");
            Log.d("Reading: ", "Reading all Feedback..");
            List<Feedback> feedback = db.getAllFeedback(Common.getTeamFromSP(this));
            Log.e("list size", String.valueOf(feedback.size()));

            if (feedback.size() > 0) {


                for (Feedback fb : feedback) {
                    String log = "Id: " + fb.getID() + ",Userid: " + checkuid + " ,Name: " + fb.getFarmerName() + " ,place: " + fb.getplace() + " ,mobile: " + fb.getContactNo() + " ,crop: " + fb.getCrop() + " ,hybrid: " + fb.getHybrid() + " ,sowing date : " + fb.getSowingDate() + " ,feedback message:" + fb.getfeedbackmessage() + ",image:" + fb.getImage() + ",ffmid:" + fb.get_ffmid();

                    Log.e("feedback before : ", log);

                    FeedbackReq feedbackReq = new FeedbackReq();
                    feedbackReq.id = String.valueOf(fb.getID());
                    feedbackReq.contactNo = fb.getContactNo();
                    feedbackReq.cropId = fb.getCrop();
                    feedbackReq.farmerName = fb.getFarmerName();
                    feedbackReq.feedbackMessage = fb.getfeedbackmessage();
                    feedbackReq.hybrid = fb.getHybrid();
                    feedbackReq.place = fb.getplace();
                    feedbackReq.sowingDate = fb.getSowingDate();
                    feedbackReq.transplantingDate = fb.getTransplantingDate();
                    feedbackReq.userId = String.valueOf(fb.get_user_id());
                    String images = fb.getImage();
                    List<File> files = new ArrayList<>();
                    if (images != null) {
                        String[] imagePaths = images.split(",");
                        for (int i = 0; i < imagePaths.length; i++) {
                            files.add(new File(imagePaths[i]));
                        }
                    }
                    sendImage(null, feedbackReq, files);


                }
            }
        } catch (Exception e) {

        } finally {
            pushAdvanceBookingThread();
        }

//        pushDailyDairyThread();
    }


    private void preparePlanerScheduleAndPush() {
        try {
            String jsonData = null;
            String selectQuery = "SELECT DISTINCT "
                    + KEY_EMP_CONCERN_PERSON_NAME + ","
                    + KEY_EMP_RETAILER_ID + ","
                    + KEY_EMP_FARMER_ID + ","
                    + KEY_EMP_VISIT_PLAN_TYPE + ","
                    + KEY_EMP_STATUS + ","
                    + KEY_EMP_PLAN_DATE_TIME + ","
                    + KEY_EMP_PURPOSE_VISIT + ","
                    + KEY_EMP_TYPE + ","
                    + KEY_EMP_GEO_TRACKING_ID + ","
                    + KEY_EMP_VISIT_USER_ID + ","
                    + KEY_EMP_VISIT_CUSTOMER_ID + ","
                    + KEY_EMP_MOBILE + ","
                    + KEY_EMP_VILLAGE + ","
                    + KEY_EMP_LOCATION_ADDRESS + ","
                    + KEY_EMP_VISIT_CROP_ID + ","
                    + KEY_EMP_REGION_ID + ","
                    + KEY_EMP_LOCATION_DISTRICT + ","
                    + KEY_EMP_LOCATION_TALUKA + ","
                    + KEY_EMP_LOCATION_VILLAGE + ","
                    + KEY_EMP_GSTIN_NO + ","
                    + KEY_EMP_FEILD_AREA + ","
                    + KEY_EMP_CHECK_IN_TIME + ","
                    + KEY_EMP_COMMENTS + ","
                    + KEY_EMP_APPROVAL_STATUS + ","
                    + KEY_EMP_EVENT_END_DATE + ","
                    + KEY_EMP_EVENT_PURPOSE + ","
                    + KEY_EMP_VISIT_MASTER_ID + ","
                    + KEY_EMP_EVENT_VENUE + ","
                    + KEY_EMP_EVENT_PARTICIPANTS + ","
                    + KEY_EMP_FFM_ID + ","
                    + KEY_EMP_CREATED_BY + ","
                    + KEY_EMP_UPDATED_BY + ","
                    + KEY_EMP_CREATED_DATETIME + ","
                    + KEY_EMP_UPDATE_DATETIME + ","
                    + KEY_EMP_EVENT_NAME + ","
                    + KEY_EMP_VISIT_ID + ","
                    + KEY_EMP_PURPOSE_VISIT_IDS + ","
                    + KEY_EMP_SERVER_FLAG
                    + " FROM " + TABLE_EMPLOYEE_VISIT_MANAGEMENT + "  where " + KEY_EMP_VISIT_USER_ID + " in (" + team + ") and " + KEY_EMP_FFM_ID + " = 0" + " and " + KEY_EMP_TYPE + "= 1";


            Log.e("selectQueryPlaner", selectQuery);
            sdbw = db.getWritableDatabase();

            Cursor cursor = sdbw.rawQuery(selectQuery, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {

                    Log.e(" +++ Values +++ ", "type :" + cursor.getString(5) + " : " + cursor.getString(6) + " : " + cursor.getString(21) + ": checkintime" + cursor.getString(13) + ": sqlite id" + cursor.getString(27) + "approvalstatus" + cursor.getString(16));

                    try {
                        int serverFlag = cursor.getInt(cursor.getColumnIndex(KEY_EMP_SERVER_FLAG));
                        String retailerId = cursor.getString(cursor.getColumnIndex(KEY_EMP_RETAILER_ID));
                        String farmerId = cursor.getString(cursor.getColumnIndex(KEY_EMP_FARMER_ID));
//                    if (serverFlag == 0) {
//                        if (retailerId != null && !retailerId.equalsIgnoreCase("0") && retailerId.length() > 0)
//                            retailerId = db.getRetailerFFMID(retailerId);
//                        if (farmerId != null && !farmerId.equalsIgnoreCase("0") && farmerId.length() > 0)
//                            farmerId = db.getFarmerFFMID(farmerId);
//                    }
                        OkHttpClient client = new OkHttpClient();
                        /*For passing parameters*/
                        RequestBody formBody = new FormEncodingBuilder()
                                .add("type", cursor.getString(cursor.getColumnIndex(KEY_EMP_TYPE)))
                                .add("geo_tracking_id", cursor.getString(cursor.getColumnIndex(KEY_EMP_GEO_TRACKING_ID)))
                                .add("user_id", cursor.getString(cursor.getColumnIndex(KEY_EMP_VISIT_USER_ID)))
                                .add("customer_id", cursor.getString(cursor.getColumnIndex(KEY_EMP_VISIT_CUSTOMER_ID)))
                                .add("visit_plan_type", cursor.getString(cursor.getColumnIndex(KEY_EMP_VISIT_PLAN_TYPE)))
                                .add("purpose_visit", cursor.getString(cursor.getColumnIndex(KEY_EMP_PURPOSE_VISIT_IDS)))
                                .add("plan_date_time", cursor.getString(cursor.getColumnIndex(KEY_EMP_PLAN_DATE_TIME)))
                                .add("concern_person_name", cursor.getString(cursor.getColumnIndex(KEY_EMP_CONCERN_PERSON_NAME)))
                                .add("retailer_id", retailerId)
                                .add("farmer_id", farmerId)
                                .add("mobile", cursor.getString(cursor.getColumnIndex(KEY_EMP_MOBILE)))
                                .add("village", cursor.getString(cursor.getColumnIndex(KEY_EMP_VILLAGE)))
                                .add("location_address", cursor.getString(cursor.getColumnIndex(KEY_EMP_LOCATION_ADDRESS)))
                                .add("crop_id", cursor.getString(cursor.getColumnIndex(KEY_EMP_VISIT_CROP_ID)))
                                .add("region_id", cursor.getString(cursor.getColumnIndex(KEY_EMP_REGION_ID)))
                                .add("location_district", cursor.getString(cursor.getColumnIndex(KEY_EMP_LOCATION_DISTRICT)))
                                .add("location_taluka", cursor.getString(cursor.getColumnIndex(KEY_EMP_LOCATION_TALUKA)))
                                .add("location_village", cursor.getString(cursor.getColumnIndex(KEY_EMP_LOCATION_VILLAGE)))
                                .add("retailer_gstin_no", cursor.getString(cursor.getColumnIndex(KEY_EMP_GSTIN_NO)))
                                .add("field_area", cursor.getString(cursor.getColumnIndex(KEY_EMP_FEILD_AREA)))
                                .add("id", cursor.getString(cursor.getColumnIndex(KEY_EMP_VISIT_ID)))
                                .add("comments", cursor.getString(cursor.getColumnIndex(KEY_EMP_COMMENTS)))
                                .add("status", cursor.getString(cursor.getColumnIndex(KEY_EMP_STATUS)))
                                .add("approval_status", cursor.getString(cursor.getColumnIndex(KEY_EMP_APPROVAL_STATUS)))
                                .add("event_name", cursor.getString(cursor.getColumnIndex(KEY_EMP_EVENT_NAME)))
                                .add("event_end_date", cursor.getString(cursor.getColumnIndex(KEY_EMP_EVENT_END_DATE)))
                                .add("event_purpose", cursor.getString(cursor.getColumnIndex(KEY_EMP_EVENT_PURPOSE)))
                                .add("event_venue", cursor.getString(cursor.getColumnIndex(KEY_EMP_EVENT_VENUE)))
                                .add("event_participants", cursor.getString(cursor.getColumnIndex(KEY_EMP_EVENT_PARTICIPANTS)))
                                .add("created_by", cursor.getString(cursor.getColumnIndex(KEY_EMP_CREATED_BY)))
                                .add("updated_by", cursor.getString(cursor.getColumnIndex(KEY_EMP_UPDATED_BY)))
                                .build();

                        Response responses = null;


                /*MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType,
                        "type=check_in_lat_lon&visit_type=1&user_id=7&latlon=17.4411%2C78.3911&visit_date=2016-12-05%2C18%3A27%3A30&check_in_time=18%3A27%3A30&tracking_id=0");*/
                        Request request = new Request.Builder()
                                .url(Constants.URL_INSERTING_EMP_VISIT_MANAGEMENT)
                                .post(formBody)
                                .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                                .addHeader("content-type", "application/x-www-form-urlencoded")
                                .addHeader("cache-control", "no-cache")
                                .build();


                        responses = client.newCall(request).execute();
                        jsonData = responses.body().string();
                        System.out.println("!!!!!!!1 Planner inserting" + jsonData);

                        if (jsonData != null) {
                            JSONArray jsonarray;
                            JSONObject jsonobject = new JSONObject(jsonData);
                            String status = jsonobject.getString("status");
                            if (status.equalsIgnoreCase("success")) {


                                String sqliteid = jsonobject.getString("sqlite");
                                String ffmid = jsonobject.getString("ffm_id");

                                //   Toast.makeText(getActivity(), "sqlite id and ffm id " + sqliteid + " , " + ffmid, Toast.LENGTH_SHORT).show();

                                SQLiteDatabase sql = db.getWritableDatabase();
                                // updatecomplaints
                                String updatequery = "UPDATE " + TABLE_EMPLOYEE_VISIT_MANAGEMENT + " SET " + KEY_EMP_FFM_ID + " = " + ffmid + "," + KEY_EMP_RETAILER_ID + " = " + retailerId + "," + KEY_EMP_FARMER_ID + " = " + farmerId + "," + KEY_EMP_SERVER_FLAG + " = 1 WHERE " + KEY_EMP_VISIT_ID + " = " + sqliteid;
                                sql.execSQL(updatequery);
//                            sql.close();

                           /* JSONObject planner_approval = jsonobject.getJSONObject("planner_approval");
                            PlannerApproval plannerApproval = new PlannerApproval();
                            plannerApproval.plannerApprovalId = planner_approval.getString("planner_approval_id");
                            plannerApproval.empVisitId = planner_approval.getString("emp_visit_id");
                            plannerApproval.assignedUserId = planner_approval.getString("assigned_user_id");
                            plannerApproval.userId = planner_approval.getString("user_id");
                            plannerApproval.plannerStatus = planner_approval.getString("planner_status");
                            plannerApproval.syncStatus = planner_approval.getString("sync_status");
                            plannerApproval.createdBy = planner_approval.getString("created_by");
                            plannerApproval.modifiedBy = planner_approval.getString("modified_by");
                            plannerApproval.cDateTime = planner_approval.getString("created_date_time");
                            plannerApproval.mDateTime = planner_approval.getString("modified_date_time");
                            db.insertPlannerApproval(plannerApproval);*/

                            }


                        } else {
                            Log.e("ServiceHandler", "Couldn't get any data from the url");
                            // Toast.makeText(getApplicationContext(),"No data found",Toast.LENGTH_LONG).show();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } while (cursor.moveToNext());
                Common.closeCursor(cursor);
            } else Log.d("LOG", "returned null!");
        } catch (Exception e) {

        } finally {
            getNewOrUpdatedServiceOrder();
        }
        //   pushAdvanceBookingThread();
        //pushOrderIndentThread();
    }

    private void preparePlanerEventAndPush() {
        try {
            String jsonData = null;
            String selectQuery = "SELECT DISTINCT "
                    + KEY_EMP_CONCERN_PERSON_NAME + ","
                    + KEY_EMP_RETAILER_ID + ","
                    + KEY_EMP_FARMER_ID + ","
                    + KEY_EMP_VISIT_PLAN_TYPE + ","
                    + KEY_EMP_STATUS + ","
                    + KEY_EMP_PLAN_DATE_TIME + ","
                    + KEY_EMP_PURPOSE_VISIT + ","
                    + KEY_EMP_TYPE + ","
                    + KEY_EMP_GEO_TRACKING_ID + ","
                    + KEY_EMP_VISIT_USER_ID + ","
                    + KEY_EMP_VISIT_CUSTOMER_ID + ","
                    + KEY_EMP_MOBILE + ","
                    + KEY_EMP_VILLAGE + ","
                    + KEY_EMP_LOCATION_ADDRESS + ","
                    + KEY_EMP_VISIT_CROP_ID + ","
                    + KEY_EMP_REGION_ID + ","
                    + KEY_EMP_LOCATION_DISTRICT + ","
                    + KEY_EMP_LOCATION_TALUKA + ","
                    + KEY_EMP_LOCATION_VILLAGE + ","
                    + KEY_EMP_GSTIN_NO + ","
                    + KEY_EMP_FEILD_AREA + ","
                    + KEY_EMP_CHECK_IN_TIME + ","
                    + KEY_EMP_COMMENTS + ","
                    + KEY_EMP_APPROVAL_STATUS + ","
                    + KEY_EMP_EVENT_END_DATE + ","
                    + KEY_EMP_EVENT_PURPOSE + ","
                    + KEY_EMP_VISIT_MASTER_ID + ","
                    + KEY_EMP_EVENT_VENUE + ","
                    + KEY_EMP_EVENT_PARTICIPANTS + ","
                    + KEY_EMP_FFM_ID + ","
                    + KEY_EMP_CREATED_BY + ","
                    + KEY_EMP_UPDATED_BY + ","
                    + KEY_EMP_CREATED_DATETIME + ","
                    + KEY_EMP_UPDATE_DATETIME + ","
                    + KEY_EMP_EVENT_NAME + ","
                    + KEY_EMP_VISIT_ID + ","
                    + KEY_EMP_PURPOSE_VISIT_IDS
                    + " FROM " + TABLE_EMPLOYEE_VISIT_MANAGEMENT + "  where " + KEY_EMP_VISIT_USER_ID + "=" + sharedpreferences.getString("userId", null) + " and " + KEY_EMP_FFM_ID + "=0" + " and " + KEY_EMP_TYPE + "=2";


            Log.e("selectQuery", selectQuery);
            sdbw = db.getWritableDatabase();


            Cursor cursor = sdbw.rawQuery(selectQuery, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    //Log.e(" +++ Values +++ ", "type :" + cursor.getString(5)+"concern person name :" + cursor.getString(0) + " : " + cursor.getString(6) + " : " + cursor.getString(21) + ": checkintime" + cursor.getString(13) + ": sqlite id" + cursor.getString(27));
                    Log.e(" +++ Values +++ ", "concern person name :" + cursor.getString(0) + "type :" + cursor.getString(5));

                    try {


                        OkHttpClient client = new OkHttpClient();
                        /*For passing parameters*/
                        RequestBody formBody = new FormEncodingBuilder()
                                .add("type", cursor.getString(cursor.getColumnIndex(KEY_EMP_TYPE)))
                                .add("geo_tracking_id", cursor.getString(cursor.getColumnIndex(KEY_EMP_GEO_TRACKING_ID)))
                                .add("user_id", cursor.getString(cursor.getColumnIndex(KEY_EMP_VISIT_USER_ID)))
                                .add("customer_id", cursor.getString(cursor.getColumnIndex(KEY_EMP_VISIT_CUSTOMER_ID)))
                                .add("visit_plan_type", cursor.getString(cursor.getColumnIndex(KEY_EMP_VISIT_PLAN_TYPE)))
                                .add("purpose_visit", cursor.getString(cursor.getColumnIndex(KEY_EMP_PURPOSE_VISIT)))
                                .add("plan_date_time", cursor.getString(cursor.getColumnIndex(KEY_EMP_PLAN_DATE_TIME)))
                                .add("concern_person_name", cursor.getString(cursor.getColumnIndex(KEY_EMP_CONCERN_PERSON_NAME)))
                                .add("retailer_id", cursor.getString(cursor.getColumnIndex(KEY_EMP_RETAILER_ID)))
                                .add("farmer_id", cursor.getString(cursor.getColumnIndex(KEY_EMP_FARMER_ID)))
                                .add("mobile", cursor.getString(cursor.getColumnIndex(KEY_EMP_MOBILE)))
                                .add("village", cursor.getString(cursor.getColumnIndex(KEY_EMP_VILLAGE)))
                                .add("location_address", cursor.getString(cursor.getColumnIndex(KEY_EMP_LOCATION_ADDRESS)))
                                .add("crop_id", cursor.getString(cursor.getColumnIndex(KEY_EMP_VISIT_CROP_ID)))
                                .add("region_id", cursor.getString(cursor.getColumnIndex(KEY_EMP_REGION_ID)))
                                .add("location_district", cursor.getString(cursor.getColumnIndex(KEY_EMP_LOCATION_DISTRICT)))
                                .add("location_taluka", cursor.getString(cursor.getColumnIndex(KEY_EMP_LOCATION_TALUKA)))
                                .add("location_village", cursor.getString(cursor.getColumnIndex(KEY_EMP_LOCATION_VILLAGE)))
                                .add("retailer_gstin_no", cursor.getString(cursor.getColumnIndex(KEY_EMP_GSTIN_NO)))
                                .add("field_area", cursor.getString(cursor.getColumnIndex(KEY_EMP_FEILD_AREA)))
                                .add("id", cursor.getString(cursor.getColumnIndex(KEY_EMP_VISIT_ID)))
                                .add("comments", cursor.getString(cursor.getColumnIndex(KEY_EMP_COMMENTS)))
                                .add("status", cursor.getString(cursor.getColumnIndex(KEY_EMP_STATUS)))
                                .add("approval_status", cursor.getString(cursor.getColumnIndex(KEY_EMP_APPROVAL_STATUS)))
                                .add("event_name", cursor.getString(cursor.getColumnIndex(KEY_EMP_EVENT_NAME)))
                                .add("event_end_date", cursor.getString(cursor.getColumnIndex(KEY_EMP_EVENT_END_DATE)))
                                .add("event_purpose", cursor.getString(cursor.getColumnIndex(KEY_EMP_EVENT_PURPOSE)))
                                .add("event_venue", cursor.getString(cursor.getColumnIndex(KEY_EMP_EVENT_VENUE)))
                                .add("event_participants", cursor.getString(cursor.getColumnIndex(KEY_EMP_EVENT_PARTICIPANTS)))
                                .add("created_by", cursor.getString(cursor.getColumnIndex(KEY_EMP_CREATED_BY)))
                                .add("updated_by", cursor.getString(cursor.getColumnIndex(KEY_EMP_UPDATED_BY)))
                                .build();

                        Response responses = null;


                /*MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType,
                        "type=check_in_lat_lon&visit_type=1&user_id=7&latlon=17.4411%2C78.3911&visit_date=2016-12-05%2C18%3A27%3A30&check_in_time=18%3A27%3A30&tracking_id=0");*/
                        Request request = new Request.Builder()
                                .url(Constants.URL_INSERTING_EMP_VISIT_MANAGEMENT)
                                .post(formBody)
                                .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                                .addHeader("content-type", "application/x-www-form-urlencoded")
                                .addHeader("cache-control", "no-cache")
                                .build();


                        try {
                            responses = client.newCall(request).execute();
                            jsonData = responses.body().string();
                            System.out.println("!!!!!!!1 Planner inserting" + jsonData);

                            if (jsonData != null) {
                                JSONArray jsonarray;
                                try {
                                    JSONObject jsonobject = new JSONObject(jsonData);
                                    String status = jsonobject.getString("status");
                                    if (status.equalsIgnoreCase("success")) {


                                        int sqliteid = jsonobject.getInt("sqlite");
                                        int ffmid = jsonobject.getInt("ffm_id");

                                        //   Toast.makeText(getActivity(), "sqlite id and ffm id " + sqliteid + " , " + ffmid, Toast.LENGTH_SHORT).show();

                                        SQLiteDatabase sql = db.getWritableDatabase();
                                        // updatecomplaints
                                        String updatequery = "UPDATE " + TABLE_EMPLOYEE_VISIT_MANAGEMENT + " SET " + KEY_EMP_FFM_ID + " = " + ffmid + " WHERE " + KEY_EMP_VISIT_ID + " = " + sqliteid;
                                        sql.execSQL(updatequery);

                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            } else {
                                Log.e("ServiceHandler", "Couldn't get any data from the url");
                                // Toast.makeText(getApplicationContext(),"No data found",Toast.LENGTH_LONG).show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } while (cursor.moveToNext());
            } else Log.d("LOG", "returned null!");
        } catch (Exception e) {

        } finally {
            pushPaymentThread();
        }
        //  pushPlanerScheduleThread();
    }








    private void prepareEMANDGENERATIONAndpush() {


        List<Demandgeneation> dg_monitoring_data = db.getOfflineDemandgeneration(Common.getUserIdFromSP(this));
        DemandgenerationreqVo cropShiftReqVo = new DemandgenerationreqVo();
        cropShiftReqVo.dg_monitoring_data = dg_monitoring_data;


        if (Common.haveInternet(BackgroundPushService.this))
            new RetrofitRequester(BackgroundPushService.this).sendRequestofdemandgeneration(BASE_URL+Constants.POST_DEMANDGENERATION_lISt, cropShiftReqVo, true);
























      /*  String user_id,date,cropid,event;
        try {
            String jsonData = null;
            String selectQuery = "SELECT DISTINCT *  FROM " + TABLE_DEMANDGENERATION + "  where "+KEY_DEMANDGENERATION_STATUS + "=0";
            Log.e("Sucess", " demandgeneration");

            Log.e("selectQuery", selectQuery);
            sdbw = db.getWritableDatabase();


            Cursor cursor = sdbw.rawQuery(selectQuery, null);
            if (cursor != null && cursor.moveToFirst()) {
                Log.e("Sucess", " demandgeneration2");
                do {
                    //Log.e("Sucess", cursor.getString(cursor.getColumnIndex("1")));

                    try {

                        user_id=cursor.getString(1);
                        date=cursor.getString(7);
                        cropid=cursor.getString(4);
                        event=cursor.getString(5);
                        OkHttpClient client = new OkHttpClient();
                        *//*For passing parameters*//*
                        RequestBody formBody = new FormEncodingBuilder()
                                .add("user_id", user_id)
                                .add("district_id", cursor.getString(2))
                                .add("division_id", cursor.getString(3))
                                .add("crop_id", cropid)
                                .add("activity_event", event)
                                .add("no_of_farmers", cursor.getString(8))
                                .add("date_of_event", date)
                                .add("address", cursor.getString(6))
                                .add("status", cursor.getString(9))
                                .add("created_datetime", cursor.getString(10))
                                .build();



                        Response responses = null;



                        Request request = new Request.Builder()
                                .url(Constants.BASE_URL+Constants.POST_DEMANDGENERATION)
                                .post(formBody)
                                .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                                .addHeader("content-type", "application/x-www-form-urlencoded")
                                .addHeader("cache-control", "no-cache")
                                .build();


                        try {
                            responses = client.newCall(request).execute();
                            jsonData = responses.body().string();
                            JSONObject jsonobject = new JSONObject(jsonData);
                            System.out.println("!!!!!!!1 Planner inserting" + jsonData);

                            if(jsonobject.getString("Status").equals("Success"))
                            {
                                String ss=db.update_demandgeneration(user_id,date,cropid,event);
                                if(ss.equals("Sucess")) {
                                    Log.e("Sucess", " demandgeneration Sucess");
                                }

                            }
                           *//* else {
                                Toast.makeText(getApplicationContext(),jsonobject.getString("msg"),Toast.LENGTH_SHORT).show();
                            }*//* else {
                                Log.e("demandgeneration", jsonobject.getString("msg"));
                                // Toast.makeText(getApplicationContext(),"No data found",Toast.LENGTH_LONG).show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } while (cursor.moveToNext());
            } else Log.d("LOG", "returned null!");
        } catch (Exception e) {

        } finally {

        }*/
        //  pushPlanerScheduleThread();
    }


    private void prepareFARMERCOUPANSAndpush(String uid) {


        List<Getcoupans> farmer_coupon_data = db.getcoupans(uid);
        FarmerCouponsreqVo cropShiftReqVo = new FarmerCouponsreqVo();
        cropShiftReqVo.farmer_coupon_data= farmer_coupon_data;


        if (Common.haveInternet(BackgroundPushService.this))
            new RetrofitRequester(BackgroundPushService.this).sendRequestoffarmercoupon(BASE_URL + Constants.POST_FARMERCOUPONS_lISt, cropShiftReqVo, true);

    }












    private void prepareDailyDairyOfflineDataAndPush() {
        try {
            checkuid = sharedpreferences.getString("userId", "");
            Log.d("Reading: ", "Reading all dailydairy..");

            List<DailyDairy> dailydairy = db.getAllnullDailyDairy(checkuid);

            Log.e("list size", String.valueOf(dailydairy.size()));


            if (dailydairy.size() > 0) {


                for (DailyDairy fb : dailydairy) {
                    String log = "Id: " + fb.getID() + ",Userid: " + checkuid + " : " + fb.get_title() + " : " + fb.get_createddate() + " : " + fb.get_date() + " : " + fb.get_time();

                    Log.e("Dairy before : ", log);

                    try {
                        String jsonData = null;
                        Response responses = null;

                        OkHttpClient client = new OkHttpClient();

                        RequestBody formBody = new FormEncodingBuilder()
                                .add("id", String.valueOf(fb.getID()))
                                .add("title", fb.get_title())
                                .add("user_id", String.valueOf(fb.get_userid()))
                                .add("note", fb.get_comments())
                                .add("time_slot", fb.get_time())
                                .add("dairy_date", fb.get_date())
                                .add("tentative_time", fb.get_tentative_time())
                                .add("type", String.valueOf(fb.get_type()))
                                .add("status", String.valueOf(fb.get_status()))

                                .build();

                        Request request = new Request.Builder()
                                .url(Constants.URL_INSERTING_DAILYDAIRY)
                                .post(formBody)
                                .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                                .addHeader("content-type", "application/x-www-form-urlencoded")
                                .addHeader("cache-control", "no-cache")
                                .build();


                        try {
                            responses = client.newCall(request).execute();
                            jsonData = responses.body().string();
                            System.out.println("!!!!!!!1 Insertdairy" + jsonData);
                            if (jsonData != null) {
                                JSONArray jsonarray;
                                try {
                                    JSONObject jsonobject = new JSONObject(jsonData);
                                    String status = jsonobject.getString("status");
                                    if (status.equalsIgnoreCase("success")) {
                                        String sqliteid = jsonobject.getString("sqlite");
                                        String ffmid = jsonobject.getString("ffm_id");

                                        Log.e("sqlite id", sqliteid);
                                        Log.e("ffmid", ffmid);
                                        sdbw = db.getWritableDatabase();
                                        // updateFeedback(Feedback feedback);
                                        String updatequery = "UPDATE " + TABLE_DAILYDAIRY + " SET " + KEY_DD_FFMID + " = " + ffmid + " WHERE " + KEY_DD_ID + " = " + sqliteid;
                                        sdbw.execSQL(updatequery);
                                        System.out.println(updatequery);

                                        List<DailyDairy> dailydairy1 = db.getAllnullDailyDairy(checkuid);

                                        for (DailyDairy fb1 : dailydairy1) {
                                            String log1 = "Id: " + fb.getID() + ",Userid: " + checkuid + " : " + fb.get_title() + " : " + fb.get_createddate() + " : " + fb.get_date() + " : " + fb.get_time();

                                            Log.e("Dairy before : ", log1);
                                        }
                                    }
                                } catch (Exception e) {
                                    e.printStackTrace();
                                }
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }

//            pushMICommodityPriceThread();
            }
        } catch (Exception e) {

        } finally {
            pushGeoThread();
        }
        // pushPlannerCheckinThread();
    }

    private void prepareProfileImageOfflineDataAndPush() {
        try {
            String img_status = sharedpreferences.getString("img", "");
            String updated_img_string = "";
            Log.d("Reading: ", "Reading all dailydairy..");

            String selectQuery = "SELECT  " + db.KEY_TABLE_USERS_IMAGE + " FROM " + db.TABLE_USERS + " WHERE " + db.KEY_TABLE_USERS_MASTER_ID + " = " + userId;                //String selectQuery = "SELECT  " + KEY_TABLE_CROPS_CROP_MASTER_ID + ","+ KEY_TABLE_CROPS_CROP_NAME + " FROM " + TABLE_COMPANY_DIVISION_CROPS + " AS CDC JOIN " + TABLE_CROPS + " AS CR ON CDC."+KEY_TABLE_COMPANY_DIVISION_CROPS_CROP_ID + " = CR."+ KEY_TABLE_CROPS_CROP_MASTER_ID + "  where " + KEY_TABLE_COMPANY_DIVISION_CROPS_COMPANY_ID + " = " + company_id + " and " + KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID + " = " + sel_division_id;

            System.out.println(selectQuery);
            sdbw = db.getWritableDatabase();
            Cursor cursor = sdbw.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    Users users = new Users();

                    Log.e("image ", cursor.getString(0));

                    updated_img_string = cursor.getString(0);

                } while (cursor.moveToNext());
            }
            if (updated_img_string != null && img_status.equalsIgnoreCase("0")) {

                try {
                    String jsonData = null;
                    Response responses = null;

                    OkHttpClient client = new OkHttpClient();

                    RequestBody formBody = new FormEncodingBuilder()
                            .add("profile_pic", updated_img_string)
                            .add("user_id", userId)
                            .build();

                    Request request = new Request.Builder()
                            .url(Constants.URL_UPDATING_PROFILEPIC)
                            .post(formBody)
                            .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                            .addHeader("content-type", "application/x-www-form-urlencoded")
                            .addHeader("cache-control", "no-cache")
                            .build();


                    try {
                        responses = client.newCall(request).execute();
                        jsonData = responses.body().string();
                        System.out.println("!!!!!!!1 updateedprofilepic" + jsonData);
                        if (jsonData != null) {
                            try {
                                JSONObject jsonobject = new JSONObject(jsonData);
                                String status = jsonobject.getString("status");
                                if (status.equalsIgnoreCase("success")) {
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.putString("img", "1");
                                    editor.commit();
                                    Log.e("updated on server", sharedpreferences.getString("img", ""));

                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        } catch (Exception e) {

        } finally {
            pushYEThread();
        }

    }

    private void prepareBookingOfflinecheckinDataAndPush() {

        try {
            List<ServiceOrderMaster> serviceOrderMasterList = db.getOfflineAdvanceBookingData();
            JSONArray mainArray = new JSONArray();
            JSONArray adbBookArray = new JSONArray();
            JSONObject naimObject = new JSONObject();
            ;
            if (serviceOrderMasterList == null || serviceOrderMasterList.size() == 0) {
                Log.i("  -json array -", "returned.." + mainArray.toString());
                return;

            }
            for (int i = 0; i < serviceOrderMasterList.size(); i++) {
                ServiceOrderMaster serviceOrderMaster = serviceOrderMasterList.get(i);
                JSONObject advBookObj = new JSONObject();

                advBookObj.put("CompanyID", serviceOrderMasterList.get(i)._serviceorder_company_id);
                advBookObj.put("customer_id", serviceOrderMasterList.get(i)._serviceorder_customer_id);
                advBookObj.put("Tokenamount", serviceOrderMasterList.get(i)._token_amount);

                advBookObj.put("DivisionID", serviceOrderMasterList.get(i)._serviceorder_division_id);


                advBookObj.put("id", serviceOrderMasterList.get(i)._serviceorder_masterid);
                advBookObj.put("user_id", serviceOrderMasterList.get(i)._serviceorder_user_id);
                advBookObj.put("created_by", userId);


                // JSONArray productArray = new JSONArray();
                // for (int k = 0; k < globalGroup.size(); k++) {
                JSONArray cropArray = new JSONArray();
                for (int m = 0; m < serviceOrderMaster.getServiceOrderDetailMasterListGrpByCropID().size(); m++) {
                    String sel_crop_id = "";
                    JSONArray productArray = new JSONArray();

                    for (int l = 0; l < serviceOrderMaster.getServiceOrderDetailMasterList().size(); l++) {
                        ServiceOrderDetailMaster serviceOrderDetailMaster = serviceOrderMaster.getServiceOrderDetailMasterList().get(l);
                        if (serviceOrderDetailMaster.get_serviceorderdetail_crop_id().equalsIgnoreCase(serviceOrderMaster.getServiceOrderDetailMasterListGrpByCropID().get(m).get_serviceorderdetail_crop_id())) {

                            JSONObject object_one = new JSONObject();
                            object_one.put("advance_amount", serviceOrderDetailMaster.getserviceorderdetail_advance_amount());
                            object_one.put("scheme_id", serviceOrderDetailMaster.getserviceorderdetail_scheme_id());
                            object_one.put("slab_id", serviceOrderDetailMaster.getSlabId());

                            object_one.put("ProductID", serviceOrderDetailMaster.getserviceorderdetail_product_id());
                            object_one.put("Qunatity", serviceOrderDetailMaster.get_serviceorderdetail_quantity());
                            object_one.put("mobile_serivce_details_id", serviceOrderDetailMaster.getserviceorderdetail_masterid());

                            productArray.put(object_one);

                        }
                    }
                    JSONObject cropObj = new JSONObject();
                    sel_crop_id = serviceOrderMaster.getServiceOrderDetailMasterListGrpByCropID().get(m).get_serviceorderdetail_crop_id();
                    cropObj.put("Products", productArray);
                    cropObj.put("CropId", sel_crop_id);
                    cropObj.put("scheme_id", "");

                    cropArray.put(cropObj);

                }
                advBookObj.put("Crops", cropArray);
                adbBookArray.put(advBookObj);
                naimObject.put("AdvanceBookings", adbBookArray);


            }
            mainArray.put(naimObject);
            Log.i("AdvanceB-json array -", "" + mainArray.toString());
            if (serviceOrderMasterList.size() > 0) {


                OkHttpClient client = new OkHttpClient();

                MediaType mediaType = MediaType.parse("application/octet-stream");
                RequestBody body = RequestBody.create(mediaType, mainArray.toString());
                Request request = new Request.Builder()
                        .url(Constants.URL_NSL_MAIN + Constants.URL_INSERT_ADVANCEBOOKING)
                        .post(body)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        // .addHeader("postman-token", "6bb3394a-3eaa-d4ae-cd37-0257afbdf3db")
                        .build();

                Response response = client.newCall(request).execute();
                String jsonData = response.body().string();
                System.out.println("!!!!!!!1 Advance booking" + jsonData);

                if (jsonData != null) {
                    JSONArray jsonArray;

                    jsonArray = new JSONArray(jsonData);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = jsonArray.getJSONObject(i);

                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("success")) {
                            //Toast.makeText(getActivity(),"Complaints products inserted sucessfully",Toast.LENGTH_SHORT).show();

                            String service_id = jsonObject.getString("service_id");
                            String ffmid = jsonObject.getString("ffm_id");

                            //   Toast.makeText(getActivity(), "sqlite id and ffm id " + service_id + " , " + ffmid, Toast.LENGTH_SHORT).show();
                            Log.e("sqlite id", service_id);
                            Log.e("ffmid", ffmid);
                            sdbw = db.getWritableDatabase();
                            // updateFeedback(Feedback feedback);
                            String updatequery = "UPDATE " + TABLE_SERVICEORDER + " SET " + KEY_TABLE_SERVICEORDER_FFM_ID + " = " + ffmid + " WHERE " + KEY_TABLE_SERVICEORDER_ID + " = " + service_id;
                            sdbw.execSQL(updatequery);

                            JSONArray detailsArray = jsonObject.getJSONArray("details");
                            for (int k = 0; k < detailsArray.length(); k++) {
                                String ffm_id = detailsArray.getJSONObject(k).getString("ffm_id");
                                String order_detail_id = detailsArray.getJSONObject(k).getString("order_detail_id");
                                db.updateSOD(ffm_id, order_detail_id);
//                                String updatequery1 = "UPDATE " + TABLE_SERVICEORDERDETAILS + " SET " + KEY_TABLE_SERVICEORDER_DETAIL_MASTER_ID + " = " + ffm_id + " , " + KEY_TABLE_SERVICEORDER_FFM_ID + " = " + ffm_id + " WHERE " + KEY_TABLE_SERVICEORDER_DETAIL_ORDER_ID + " = " + order_detail_id;
//                                sdbw.execSQL(updatequery1);
                            }
                            JSONArray approvalArray = jsonObject.getJSONArray("order_approval");
                            for (int j = 0; j < approvalArray.length(); j++) {
                                JSONObject order_approval = approvalArray.getJSONObject(j);
                                ServiceOrderApproval soa = new ServiceOrderApproval();
                                soa.orderApprovalId = order_approval.getString("service_order_approval_id");
                                soa.orderId = order_approval.getString("order_id");
                                soa.orderStatus = order_approval.getString("order_status");
                                soa.userId = order_approval.getString("user_id");
                                soa.assignedUserId = order_approval.getString("assigned_user_id");
                                soa.syncStatus = order_approval.getString("sync_status");
                                soa.createdBy = order_approval.getString("created_by");
                                soa.modifiedBy = order_approval.getString("modified_by");
                                soa.cDateTime = order_approval.getString("created_date_time");
                                soa.uDateTime = order_approval.getString("modified_date_time");
                                soa.pendingBy = order_approval.getString("pending_by");
                                db.insertServiceOrderApproval(soa);
                            }
                            JSONArray historyArray = jsonObject.getJSONArray("service_order_history");
                            for (int j = 0; j < historyArray.length(); j++) {
                                JSONObject historyObj = historyArray.getJSONObject(j);
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


                }

            }
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pushApproveRejectThread();
        }


    }


    private void prepareMICropShiftsOfflineDataAndPush() {
        checkuid = sharedpreferences.getString("userId", "");
        Log.d("Reading: ", "Reading all CommodityPrice..");
        List<Crop_Shifts> commodity_prices = db.getAllnullCrop_Shifts(userId);
        Log.e("list size", String.valueOf(commodity_prices.size()));

        if (commodity_prices.size() > 0) {


            for (Crop_Shifts cm : commodity_prices) {
                String log = "Id: " + cm.getID() + ",userid: " + userId + " ,cropname: " + cm.get_crop_shifts_crop_name() + " ,variety: " + cm.get_crop_shifts_variety_type() + ",previous_year_area:" + cm.get_crop_shifts_previous_year_area() + " ,current_year_expected_area: " + cm.get_crop_shifts_current_year_expected_area() + ",percentage_increase_decrease:" +
                        cm.get_crop_shifts_percentage_increase_decrease() + ",created_by" + cm.get_crop_shifts_created_by() + ",created_on:" + cm.get_crop_shifts_created_on() + ",ffmid:" + cm.get_crop_shifts_ffmid();
                Log.e("Crop_Shifts before : ", log);
                try {
                    String jsonData = null;
                    Response responses = null;

                    OkHttpClient client = new OkHttpClient();
                    /*For passing parameters*/
                    RequestBody formBody = new FormEncodingBuilder()
                            .add("table", TABLE_MI_CROP_SHIFTS)
                            .add("mobile_id", cm.getID())
                            // .add("user_id", String.valueOf(favouriteItem.get(1)))
                            .add("crop_name", cm.get_crop_shifts_crop_name())
                            .add("variety_type", cm.get_crop_shifts_variety_type())
                            .add("previous_year_area", cm.get_crop_shifts_previous_year_area())
                            .add("current_year_expected_area", cm.get_crop_shifts_current_year_expected_area())
                            .add("percentage_increase_decrease", cm.get_crop_shifts_percentage_increase_decrease())
                            .add("reason_crop_shift", cm.get_crop_shifts_reason_crop_shift())
                            .add("created_by", cm.get_crop_shifts_created_by())
                            .add("crop_in_saved_seed", cm.get_crop_shifs_crop_in_saved_seed())
                            .add("previous_year_srr", cm.get_crop_shifs_crop_in_previous_year())
                            .add("current_year_srr", cm.get_crop_shifs_crop_in_current_year())
                            .add("next_year_srr", cm.get_crop_shifs_crop_in_next_year())
                            .build();

                    Request request = new Request.Builder()
                            .url(Constants.URL_INSERTING_PUSHTABLE)
                            .post(formBody)
                            .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                            .addHeader("content-type", "application/x-www-form-urlencoded")
                            .addHeader("cache-control", "no-cache")
                            .build();


                    try {
                        responses = client.newCall(request).execute();
                        jsonData = responses.body().string();
                        System.out.println("!!!!!!!1 InsertFeedback" + jsonData);

                        if (jsonData != null) {
                            JSONArray jsonarray;
                            try {
                                JSONObject jsonobject = new JSONObject(jsonData);
                                String status = jsonobject.getString("status");
                                if (status.equalsIgnoreCase("success")) {
                                    String sqliteid = jsonobject.getString("sqlite");
                                    String ffmid = jsonobject.getString("ffm_id");

                                    Log.e("sqlite id", sqliteid);
                                    Log.e("ffmid", ffmid);
                                    sdbw = db.getWritableDatabase();
                                    // updateFeedback(Feedback feedback);
                                    String updatequery = "UPDATE " + TABLE_MI_CROP_SHIFTS + " SET " + KEY_CROP_SHIFTS_FFMID + " = " + ffmid + " WHERE " + KEY_CROP_SHIFTS_ID + " = " + sqliteid;
                                    sdbw.execSQL(updatequery);
                                    System.out.println(updatequery);
                                    List<Crop_Shifts> cropshift = db.getAllCrop_Shifts(userId);

                                    for (Crop_Shifts cs : cropshift) {
                                        log = "Id: " + cs.getID() + ",userid: " + userId + " ,cropname: " + cs.get_crop_shifts_crop_name() + " ,variety: " + cs.get_crop_shifts_variety_type() + ",previous_year_area:" + cs.get_crop_shifts_previous_year_area() + " ,current_year_expected_area: " + cs.get_crop_shifts_current_year_expected_area() + ",percentage_increase_decrease:" +
                                                cs.get_crop_shifts_percentage_increase_decrease() + ",created_by" + cs.get_crop_shifts_created_by() + ",created_on:" + cs.get_crop_shifts_created_on() + ",ffmid:" + cs.get_crop_shifts_ffmid();


                                        Log.e("feedbacksqlite: ", log);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

                prepareMIPriceSurveyOfflineDataAndPush();
            }
        }


    }
    //Last one to be pushed


    private void prepareMIPriceSurveyOfflineDataAndPush() {
        checkuid = sharedpreferences.getString("userId", "");
        Log.d("Reading: ", "Reading all CommodityPrice..");
        List<Price_Survey> commodity_prices = db.getAllnullPrice_Survey(userId);
        Log.e("list size", String.valueOf(commodity_prices.size()));

        if (commodity_prices.size() > 0) {


            for (Price_Survey cm : commodity_prices) {
                String log = "Id: " + cm.getID() + ",userid: " + userId + " ,company_name: " + cm.get_price_survey_company_name() + " ,product_name: " + cm.get_price_survey_product_name() + ",sku_pack_size:" + cm.get_price_survey_sku_pack_size() + " ,retail_price: " + cm.get_price_survey_retail_price() + ",invoice_price:" +
                        cm.get_price_survey_invoice_price() + ",net_distributor_landing_price" + cm.get_price_survey_net_distributor_landing_price() + ",created_by" + cm.get_price_survey_created_by() + ",created_on:" + cm.get_price_survey_created_on() + ",ffmid:" + cm.get_price_survey_ffmid();
                Log.e("commoditypricesqlite: ", log);
                try {
                    String jsonData = null;
                    Response responses = null;

                    OkHttpClient client = new OkHttpClient();
                    /*For passing parameters*/
                    RequestBody formBody = new FormEncodingBuilder()
                            .add("table", TABLE_MI_PRICE_SURVEY)
                            .add("mobile_id", String.valueOf(cm.getID()))
                            // .add("user_id", String.valueOf(favouriteItem.get(1)))
                            .add("company_name", cm.get_price_survey_company_name())
                            .add("product_name", cm.get_price_survey_product_name())
                            .add("sku_pack_size", cm.get_price_survey_sku_pack_size())
                            .add("retail_price", cm.get_price_survey_retail_price())
                            .add("invoice_price", cm.get_price_survey_invoice_price())
                            .add("net_distributor_landing_price", cm.get_price_survey_net_distributor_landing_price())
                            .add("created_by", cm.get_price_survey_created_by())
                            .build();

                    Request request = new Request.Builder()
                            .url(Constants.URL_INSERTING_PUSHTABLE)
                            .post(formBody)
                            .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                            .addHeader("content-type", "application/x-www-form-urlencoded")
                            .addHeader("cache-control", "no-cache")
                            .build();


                    try {
                        responses = client.newCall(request).execute();
                        jsonData = responses.body().string();
                        System.out.println("!!!!!!!1 InsertFeedback" + jsonData);

                        if (jsonData != null) {
                            JSONArray jsonarray;
                            try {
                                JSONObject jsonobject = new JSONObject(jsonData);
                                String status = jsonobject.getString("status");
                                if (status.equalsIgnoreCase("success")) {
                                    String sqliteid = jsonobject.getString("sqlite");
                                    String ffmid = jsonobject.getString("ffm_id");

                                    Log.e("sqlite id", sqliteid);
                                    Log.e("ffmid", ffmid);
                                    sdbw = db.getWritableDatabase();
                                    // updateFeedback(Feedback feedback);
                                    String updatequery = "UPDATE " + TABLE_MI_PRICE_SURVEY + " SET " + KEY_PRICE_SURVEY_FFMID + " = " + ffmid + " WHERE " + KEY_PRICE_SURVEY_ID + " = " + sqliteid;
                                    sdbw.execSQL(updatequery);
                                    System.out.println(updatequery);
                                    List<Price_Survey> price = db.getAllPrice_Survey(checkuid);

                                    for (Price_Survey cp : price) {
                                        log = "Id: " + cp.getID() + ",Name: " + cp.get_price_survey_company_name() + " product_name: " + cp.get_price_survey_product_name() + cp.get_price_survey_sku_pack_size() + cp.get_price_survey_retail_price() + cp.get_price_survey_invoice_price() + cp.get_price_survey_net_distributor_landing_price() + cp.get_price_survey_ffmid();

                                        Log.e("Price_Survey: ", log);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }

                prepareMIProductSurveyOfflineDataAndPush();
            }
        }


    }


    private void prepareMIProductSurveyOfflineDataAndPush() {
        checkuid = sharedpreferences.getString("userId", "");
        Log.d("Reading: ", "Reading all CommodityPrice..");
        List<Product_Survey> commodity_prices = db.getAllnullProduct_Survey(userId);
        Log.e("list size", String.valueOf(commodity_prices.size()));

        if (commodity_prices.size() > 0) {


            for (Product_Survey cm : commodity_prices) {
                String log = "Id: " + cm.getID() + ",userid: " + userId + " ,company_name: " + cm.get_product_survey_company_name() + " ,product_name: " + cm.get_product_survey_product_name() + "," + cm.get_product_survey_name_of_the_check_segment() + "," + cm.get_product_survey_launch_year() + "," +
                        cm.get_product_survey_no_units_sold() + "," + cm.get_product_survey_area_crop_sown_new_product() + cm.get_product_survey_remarks_unique_feature() + ",created_by" + cm.get_product_survey_created_by() + ",created_on:" + cm.get_product_survey_created_on() + ",ffmid:" + cm.get_product_survey_ffmid();

                Log.e("commoditypricesqlite: ", log);
                try {
                    String jsonData = null;
                    Response responses = null;

                    OkHttpClient client = new OkHttpClient();
                    /*For passing parameters*/
                    RequestBody formBody = new FormEncodingBuilder()
                            .add("table", TABLE_MI_PRODUCT_SURVEY)
                            .add("mobile_id", String.valueOf(cm.getID()))
                            // .add("user_id", String.valueOf(favouriteItem.get(1)))
                            .add("company_name", cm.get_product_survey_company_name())
                            .add("product_name", cm.get_product_survey_product_name())
                            .add("name_of_the_check_segment", cm.get_product_survey_name_of_the_check_segment())
                            .add("launch_year", cm.get_product_survey_launch_year())
                            .add("no_units_sold", cm.get_product_survey_no_units_sold())
                            .add("area_crop_sown_new_product", cm.get_product_survey_area_crop_sown_new_product())
                            .add("remarks_unique_feature", cm.get_product_survey_remarks_unique_feature())
                            .add("created_by", checkuid)
                            .build();

                    Request request = new Request.Builder()
                            .url(Constants.URL_INSERTING_PUSHTABLE)
                            .post(formBody)
                            .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                            .addHeader("content-type", "application/x-www-form-urlencoded")
                            .addHeader("cache-control", "no-cache")
                            .build();


                    try {
                        responses = client.newCall(request).execute();
                        jsonData = responses.body().string();
                        System.out.println("!!!!!!!1 InsertFeedback" + jsonData);

                        if (jsonData != null) {
                            JSONArray jsonarray;
                            try {
                                JSONObject jsonobject = new JSONObject(jsonData);
                                String status = jsonobject.getString("status");
                                if (status.equalsIgnoreCase("success")) {
                                    String sqliteid = jsonobject.getString("sqlite");
                                    String ffmid = jsonobject.getString("ffm_id");

                                    Log.e("sqlite id", sqliteid);
                                    Log.e("ffmid", ffmid);
                                    sdbw = db.getWritableDatabase();
                                    // updateFeedback(Feedback feedback);
                                    String updatequery = "UPDATE " + TABLE_MI_PRICE_SURVEY + " SET " + KEY_PRICE_SURVEY_FFMID + " = " + ffmid + " WHERE " + KEY_PRICE_SURVEY_ID + " = " + sqliteid;
                                    sdbw.execSQL(updatequery);
                                    System.out.println(updatequery);
                                    List<Price_Survey> price = db.getAllPrice_Survey(checkuid);

                                    for (Price_Survey cp : price) {
                                        log = "Id: " + cp.getID() + ",Name: " + cp.get_price_survey_company_name() + " product_name: " + cp.get_price_survey_product_name() + cp.get_price_survey_sku_pack_size() + cp.get_price_survey_retail_price() + cp.get_price_survey_invoice_price() + cp.get_price_survey_net_distributor_landing_price() + cp.get_price_survey_ffmid();

                                        Log.e("Price_Survey: ", log);
                                    }
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        }

                    } catch (IOException e) {
                        e.printStackTrace();
                    }


                } catch (Exception e) {
                    e.printStackTrace();
                }


            }
        }


    }

    private void prepareApproveRejectOfflineDataAndPush() {

        try {
            List<ServiceOrderMaster> approvalRejectOrderList = db.getOfflineApproveRejectData();

            for (ServiceOrderMaster serviceOrderMaster : approvalRejectOrderList) {
                String requestBody = "approval_status=" + serviceOrderMaster.get_approval_status() + "&updated_by=" + serviceOrderMaster.get_updated_by() + "&order_id=" + serviceOrderMaster.getID();
                OkHttpClient client = new OkHttpClient();
                Response responses = null;
                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType, requestBody);
                Request request = new Request.Builder()
                        .url(Constants.URL_ORDER_APPROVAL)
                        .post(body)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();

                responses = client.newCall(request).execute();
                String jsonData = responses.body().string();
                System.out.println("!!!!!!!1" + jsonData);
                JSONObject responseObject = new JSONObject(jsonData);
                if (responseObject.get("status").equals("success")) {

                    db.updateApprovalOrRejectStatus(serviceOrderMaster.get_updated_by(), serviceOrderMaster.get_approval_status(), serviceOrderMaster.get_approval_comments(), (String) responseObject.get("ffm_id"), false);

                }
            }

        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            pushOrderIndentThread();
        }
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

                for (VersionControlVo dbVersionCode : dbVersionControl) {

                    if (versionControlVo.tableName.equalsIgnoreCase(dbVersionCode.tableName) && !versionControlVo.status.equalsIgnoreCase(dbVersionCode.status)) {
                        db.updateVersionControlData(versionControlVo);
                        tableMustInsertList.add(versionControlVo.tableName);


                    }

                }
            }
        }

//if (tableMustInsertList.contains("customers") || tableMustInsertList.contains("customer_details")){


        new Async_getallcustomers().execute();
//}
    }

    @Override
    public void onFormResponseSuccess(String url, FormResponseResVo formResponseResVo, ProgressDialog dialog) {
        switch (url) {
            case Constants.URL_INSERTING_MARKET_POTENTIAL:
                try {
                    List<FormResponse> formResponseList = formResponseResVo.formResponse;
                    if (formResponseList != null && formResponseList.size() > 0) {
                        for (int i = 0; i < formResponseList.size(); i++) {
                            FormResponse formResponse = formResponseList.get(i);
                            db.updateTable(KEY_MARKET_POTENTIAL_FFM_ID, TABLE_MARKET_POTENTIAL, KEY_MARKET_POTENTIAL_PRIMARY_ID, formResponse.sqliteId, formResponse.ffmId);
                        }
                    } else {
                    }
                } catch (Exception e) {
                    Common.Log.i("RESPONSE EXCEPTION " + e.getMessage());
                }finally{
                    prepareCompetitorChannelDataAndPush();
                }
                break;
            case Constants.URL_INSERTING_COMPETITOR_CHANNEL:
                try {
                    List<FormResponse> formResponseList = formResponseResVo.formResponse;
                    if (formResponseList != null && formResponseList.size() > 0) {
                        for (int i = 0; i < formResponseList.size(); i++) {
                            FormResponse formResponse = formResponseList.get(i);
                            db.updateTable(KEY_COMP_CHANNEL_FFM_ID, TABLE_COMPETITOR_CHANNEL, KEY_COMP_CHANNEL_PRIMARY_ID, formResponse.sqliteId, formResponse.ffmId);
                        }
                    } else {
                    }
                } catch (Exception e) {
                    Common.Log.i("RESPONSE EXCEPTION " + e.getMessage());
                }finally{
                    prepareProductPricingDataAndPush();
                }
                break;
            case Constants.URL_INSERTING_COMPETITOR_STRENGTH:
                try {
                    List<FormResponse> formResponseList = formResponseResVo.formResponse;
                    if (formResponseList != null && formResponseList.size() > 0) {
                        for (int i = 0; i < formResponseList.size(); i++) {
                            FormResponse formResponse = formResponseList.get(i);
                            db.updateTable(KEY_COMPETITOR_STRENGTH_FFM_ID, TABLE_COMPETITOR_STRENGTH, KEY_COMPETITOR_STRENGTH_PRIMARY_ID, formResponse.sqliteId, formResponse.ffmId);
                        }
                    } else {
                    }
                } catch (Exception e) {
                    Common.Log.i("RESPONSE EXCEPTION " + e.getMessage());
                }finally{
                    prepareCommodityPriceDataAndPush();
                }
                break;
            case Constants.URL_INSERTING_COMMODITY_PRICE:
                try {
                    List<FormResponse> formResponseList = formResponseResVo.formResponse;
                    if (formResponseList != null && formResponseList.size() > 0) {
                        for (int i = 0; i < formResponseList.size(); i++) {
                            FormResponse formResponse = formResponseList.get(i);
                            db.updateTable(KEY_CP_FFM_ID, TABLE_COMMODITY_PRICE, KEY_CP_PRIMARY_ID, formResponse.sqliteId, formResponse.ffmId);
                        }
                    } else {
                    }
                } catch (Exception e) {
                    Common.Log.i("RESPONSE EXCEPTION " + e.getMessage());
                }finally{
                    prepareCropShiftsDataAndPush();
                }
                break;
            case Constants.URL_INSERTING_CROP_SHIFTS:
                try {
                    List<FormResponse> formResponseList = formResponseResVo.formResponse;
                    if (formResponseList != null && formResponseList.size() > 0) {
                        for (int i = 0; i < formResponseList.size(); i++) {
                            FormResponse formResponse = formResponseList.get(i);
                            db.updateTable(KEY_CROP_SHIFTS_FFMID, TABLE_CROP_SHIFTS, KEY_CROP_SHIFTS_PRIMARY_ID, formResponse.sqliteId, formResponse.ffmId);
                        }
                    } else {
                    }
                } catch (Exception e) {
                    Common.Log.i("RESPONSE EXCEPTION " + e.getMessage());
                }finally {
                    prepareChannelPrefDataAndPush();
                }
                break;



            case BASE_URL+Constants.POST_DEMANDGENERATION_lISt:
                try {
                    List<FormResponse> formResponseList = formResponseResVo.formResponse;
                    if (formResponseList != null && formResponseList.size() > 0) {
                        for (int i = 0; i < formResponseList.size(); i++) {
                            FormResponse formResponse = formResponseList.get(i);
                            Log.d("sqliteAnil", String.valueOf(formResponse.sqliteId));

                            String ss=db.update_demandgeneration(userId,String.valueOf(formResponse.sqliteId));
                            if(!ss.equals("0")) {
                                Log.d("sqlireAnil", "demand generation sqlite sucess");
                            }
                           else if(!ss.equals("0")) {
                                Log.d("sqliteAnil", "demand generation exists");
                            }
                            else  {
                                Log.d("sqliteAnil", "demand generation failled");
                            }
                        }
                    } else {
                    }
                } catch (Exception e) {
                    Common.Log.i("RESPONSEEXCEPTIONANIL" + e.getMessage());
                }finally {
                    Common.Log.i("RESPONSEEXCEPTIONANIL" +BASE_URL+Constants.POST_DEMANDGENERATION_lISt);
                }
                break;

            case BASE_URL + Constants.POST_FARMERCOUPONS_lISt:
                try {
                    List<FormResponse> formResponseList = formResponseResVo.formResponse;
                    if (formResponseList != null && formResponseList.size() > 0) {
                        for (int i = 0; i < formResponseList.size(); i++) {
                            FormResponse formResponse = formResponseList.get(i);
                            Log.d("sqliteAnil456", String.valueOf(formResponse.sqliteId));

                            String ss=db.update_Coupan_Server_Status(userId,String.valueOf(formResponse.sqliteId));
                            if(!ss.equals("0")) {
                                Log.d("sqlireAnil", "demand generation sqlite sucess");
                            }
                            else if(!ss.equals("0")) {
                                Log.d("sqliteAnil", "demand generation exists");
                            }
                            else  {
                                Log.d("sqliteAnil", "demand generation failled");
                            }
                        }
                    } else {
                    }
                } catch (Exception e) {
                    Common.Log.i("RESPONSEEXCEPTIONANIL" + e.getMessage());
                }finally {
                    Common.Log.i("RESPONSEEXCEPTIONANIL" +BASE_URL + Constants.POST_FARMERCOUPONS_lISt);
                }
                break;


            case Constants.URL_INSERTING_CHANNEL_PREFERENCE:
                try {
                    List<FormResponse> formResponseList = formResponseResVo.formResponse;
                    if (formResponseList != null && formResponseList.size() > 0) {
                        for (int i = 0; i < formResponseList.size(); i++) {
                            FormResponse formResponse = formResponseList.get(i);
                            db.updateTable(KEY_CHANNEL_PREF_FFM_ID, TABLE_CHANNEL_PREFERENCE, KEY_CHANNEL_PREF_PRIMARY_ID, formResponse.sqliteId, formResponse.ffmId);
                        }
                    } else {
                    }
                } catch (Exception e) {
                    Common.Log.i("RESPONSEEXCEPTION " + e.getMessage());
                }
                break;
            case Constants.URL_INSERTING_PRODUCT_SURVEY:
                try {
                    List<FormResponse> formResponseList = formResponseResVo.formResponse;
                    if (formResponseList != null && formResponseList.size() > 0) {
                        for (int i = 0; i < formResponseList.size(); i++) {
                            FormResponse formResponse = formResponseList.get(i);
                            db.updateTable(KEY_SURVEY_FFM_ID, TABLE_PRODUCT_PRICING_SURVEY, KEY_SURVEY_PRIMARY_ID, formResponse.sqliteId, formResponse.ffmId);
                        }
                    } else {
                    }
                } catch (Exception e) {
                    Common.Log.i("RESPONSEEXCEPTION " + e.getMessage());
                }finally{
                    prepareCompetitorStrengthDataAndPush();
                }
                break;
            case Constants.URL_INSERTING_YIELD_ESTIMATION:
                try {
                    if (formResponseResVo != null) {
                        db.deleteYE(formResponseResVo.mobileId);
                    }
                } catch (Exception e) {
                    Common.Log.i("YEEXCEPTION " + e.getMessage());
                } finally {
                    prepareStockMovementDataAndPush();
                }
                break;
            default:
                break;
        }
    }


    public class Async_getNewOrUpdateserviceorders extends AsyncTask<Void, Void, String> {

        private String jsonData;
        private StringBuilder sb = new StringBuilder();
        private String orderidfromserviceorder;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();


        }

        protected String doInBackground(Void... params) {


            try {
                OkHttpClient client = new OkHttpClient();


                Response responses = null;
                String days = "60";
                String latestRecordDate = db.getLatestDateFromTable(TABLE_SERVICEORDER);
                if (latestRecordDate != null && !latestRecordDate.equalsIgnoreCase("null") && !latestRecordDate.equalsIgnoreCase(""))
                    days = String.valueOf(Common.getNoOfdays(latestRecordDate));
                else
                    days = "60";
                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                Common.Log.i("BOOKINGS URL " + Common.getCompleteURLEVM(Constants.NEW_OR_UPDATED_RECORDS_SERVICE_ORDER, userId, team) + "&days=" + days);
                Request request = new Request.Builder()
                        .url(Common.getCompleteURLEVM(Constants.NEW_OR_UPDATED_RECORDS_SERVICE_ORDER, userId, team) + "&days=" + days)
                        .get()
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();


                try {
                    responses = client.newCall(request).execute();
                    jsonData = responses.body().string();
                    System.out.println("!!!!!!!1 NewOrUpdate Ser" + jsonData);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            if (jsonData != null) {
                try {
                    JSONObject resultobject = new JSONObject(jsonData);
                    JSONArray adavancebooking = resultobject.getJSONArray("newrecord");

                    for (int n = 0; n < adavancebooking.length(); n++) {


                        JSONObject result_service_order = adavancebooking.getJSONObject(n);
                        JSONObject service_order = result_service_order.getJSONObject("service_order");

                        JSONArray service_order_details = result_service_order.getJSONArray("service_order_details");
                        JSONArray historyArray = result_service_order.getJSONArray("service_order_history");
                        for (int i = 0; i < historyArray.length(); i++) {
                            JSONObject historyObj = historyArray.getJSONObject(i);
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
                        JSONArray adavancebooking1 = result_service_order.getJSONArray("service_order_approvals");

                        for (int k = 0; k < adavancebooking1.length(); k++) {
                            JSONObject service_order_approval = adavancebooking1.getJSONObject(k);
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
                        String approval_status = service_order.getString("approval_status");
                        String approval_comments = service_order.getString("approval_comments");
                        String created_datetime = service_order.getString("created_datetime");
                        String updated_datetime = service_order.getString("updated_datetime");
                        if (n > 0) {
                            sb.append(",");
                        }
                        sb.append(ffm_id);


                        String selectQuery = "SELECT * FROM " + TABLE_SERVICEORDER + " WHERE " + KEY_TABLE_SERVICEORDER_FFM_ID + " = '" + ffm_id + "'";


                        sdbw = db.getWritableDatabase();
                        Cursor cc = sdbw.rawQuery(selectQuery, null);
                        cc.getCount();
                        // looping through all rows and adding to list
                        if (cc.getCount() == 0) {
                            //doesn't exists therefore insert record.
                            db.addServiceorder(new ServiceOrderMaster("", order_type, order_date, user_id, customer_id, division_id, company_id, status, created_datetime, updated_datetime, ffm_id, advance_amount, created_by, approval_status, approval_comments, order_sap_id,""));
                            String selectQuerys = "SELECT  " + KEY_TABLE_SERVICEORDER_ID + " FROM " + TABLE_SERVICEORDER + " ORDER BY " + KEY_TABLE_SERVICEORDER_ID + " DESC LIMIT 1 ";
                            sdbw = db.getWritableDatabase();

                            Cursor c = sdbw.rawQuery(selectQuerys, null);
                            //System.out.println("cursor count "+cursor.getCount());
                            if (c != null && c.moveToFirst()) {
                                orderidfromserviceorder = String.valueOf(c.getLong(0)); //The 0 is the column index, we only have 1 column, so the index is 0

                                Log.e("++++ lastId ++++", orderidfromserviceorder);
                            }

                            try {

                                for (int m = 0; m < service_order_details.length(); m++) {
                                    JSONObject objinfo = service_order_details.getJSONObject(m);
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
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {

                            db.updateApprovalOrRejectStatus(user_id, approval_status, approval_comments, ffm_id, false);
                        }

                        Common.closeCursor(cc);
                    }

                    JSONArray updated = resultobject.getJSONArray("updated");
                    for (int n = 0; n < updated.length(); n++) {


                        JSONObject result_service_order = updated.getJSONObject(n);
                        JSONObject service_order = result_service_order.getJSONObject("service_order");

                        JSONArray service_order_details = result_service_order.getJSONArray("service_order_details");

                        String ffm_id = service_order.getString("order_id");//ffm_id =order_id PRIMARY KEY
                        String order_type = service_order.getString("order_type");
                        String order_date = service_order.getString("order_date");
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
                        if (sb.toString().length() > 0) {
                            sb.append(",");
                        }
                        sb.append(ffm_id);

                        db.updateApprovalOrRejectStatus(userId, approval_status, approval_comments, ffm_id, false);

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
           /* if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }*/
            if (sb.toString() != null && sb.toString().length() > 0)
                new Async_UpdateAprovalStatus().execute("service_order", "order_id", sb.toString());
            else {
                pushDailyDairyThread();
            }

        }
    }

    private class Async_UpdateAprovalStatus extends AsyncTask<String, String, String> {

        private String jsonData;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* progressDialog = new ProgressDialog(PlanerOneActivity.this);
            progressDialog.setMessage("Updating Aproval Status");
            progressDialog.show();*/
        }

        protected String doInBackground(String... params) {

            try {

                OkHttpClient client = new OkHttpClient();


                Response responses = null;


                RequestBody formBody = new FormEncodingBuilder()
                        .add("table", params[0])
                        .add("field", params[1])
                        .add("updated_ids", params[2])
                        .add("user_id", userId)
                        .build();

                Common.Log.i("Acknowledge" + params[0] + "\n " + params[1] + "\n " + params[2]);

                Request request = new Request.Builder()
                        .url(Constants.ACKNOWLEDGE_TO_SERVER)
                        .post(formBody)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();


                try {
                    responses = client.newCall(request).execute();
                    jsonData = responses.body().string();
                    System.out.println("!!!!!!!1ACKNOWLEDGE_TO_SERVER" + formBody.toString() + "\n" + jsonData + "\n" + params[0]);
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
            pushDailyDairyThread();

        }
    }


    private void prepareStockMovementDataAndPush() {
        try {
            mainArray = new JSONArray();
            JSONArray adbBookArray = new JSONArray();
            JSONObject naimObject = null;

            ArrayList<StockMovementUnSynedPojo> stockMovementUnSynedPojoList = db.getOfflineStockPlacementListUnSyncData1();
            for (StockMovementUnSynedPojo stockMovementUnSynedPojo : stockMovementUnSynedPojoList) {

                ArrayList<StockMovementUnSynedPojo> stockMovementUnSynedPojos = db.getOfflineStockPlacementListUnSyncData(stockMovementUnSynedPojo.stockMovementId);
                for (StockMovementUnSynedPojo stockMovementUnSynedPojo1 : stockMovementUnSynedPojos) {

                    JSONObject advBookObj = new JSONObject();

                    try {
                        advBookObj.put("CompanyID", stockMovementUnSynedPojo1.companyId);
                        advBookObj.put("customer_id", stockMovementUnSynedPojo1.customerId);
                        advBookObj.put("role", stockMovementUnSynedPojo1.userType);
                        advBookObj.put("DivisionID", stockMovementUnSynedPojo1.divisionId);
                        advBookObj.put("id", stockMovementUnSynedPojo1.stockMovementId);
                        advBookObj.put("user_id", userId);
                        advBookObj.put("placed_date", stockMovementUnSynedPojo1.placedDate);
                        advBookObj.put("movement_type", "0");


                        JSONArray cropArray = new JSONArray();

                        JSONArray productArray = new JSONArray();


                        JSONObject object_one = new JSONObject();
                        object_one.put("stock_placed", stockMovementUnSynedPojo1.stockPlaced);
                        object_one.put("ProductID", stockMovementUnSynedPojo1.productId);
                        object_one.put("current_stock", stockMovementUnSynedPojo1.currentStock);
                        object_one.put("pog", stockMovementUnSynedPojo1.pog);
                        object_one.put("sqlite_id_detail", stockMovementUnSynedPojo1.stockMovementDetailId);
                        productArray.put(object_one);


                        JSONObject cropObj = new JSONObject();
                        cropObj.put("CropId", stockMovementUnSynedPojo1.cropId);

                        cropArray.put(cropObj);
                        advBookObj.put("Crops", cropArray);
                        cropObj.put("Products", productArray);
                        //}
                        naimObject = new JSONObject();
                        adbBookArray.put(advBookObj);
                        naimObject.put("stockmovement", adbBookArray);


                    } catch (JSONException e) {
                    }

                }
            }
            if (naimObject != null) {
                mainArray.put(naimObject);
            }
            Log.i("  -json array -", "" + mainArray.toString());
            if (mainArray.length() > 0) {
                new callAPIToPushStockmovementData().execute();
            }
        } catch (Exception e) {
            prepareStockMovementRetailerDataAndPush();
        }
    }

    private class callAPIToPushStockmovementData extends AsyncTask<Void, Void, String> {
        private String jsonData;

        @Override
        protected String doInBackground(Void... params) {

            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/octet-stream");
            RequestBody body = RequestBody.create(mediaType, mainArray.toString());
            Request request = new Request.Builder()
                    .url(Constants.URL_INSERT_STOCKMOVEMENT)
                    .post(body)
                    .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .addHeader("cache-control", "no-cache")
                    // .addHeader("postman-token", "6bb3394a-3eaa-d4ae-cd37-0257afbdf3db")
                    .build();

            Response response = null;

            try {
                response = client.newCall(request).execute();


                jsonData = response.body().string();
                System.out.println("!!!!!!!1Sales Oreder" + jsonData);
            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            if (jsonData != null) {
                JSONArray jsonArray;
                try {
                    jsonArray = new JSONArray(jsonData);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = null;

                        jsonObject = jsonArray.getJSONObject(i);
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("success")) {
                            JSONArray jsonArrayDetails = jsonObject.getJSONArray("details");
                            for (int j = 0; j < jsonArrayDetails.length(); j++) {
                                JSONObject jsonObject1 = jsonArrayDetails.getJSONObject(j);


                                String sqlite_id = jsonObject1.getString("sqlite_id");
                                String ffmid = jsonObject1.getString("ffm_id");

                                //   Toast.makeText(getActivity(), "sqlite id and ffm id " + service_id + " , " + ffmid, Toast.LENGTH_SHORT).show();
                                Log.e("sqlite id", sqlite_id);
                                Log.e("ffmid", ffmid);
                                sdbw = db.getWritableDatabase();
                                // updateFeedback(Feedback feedback);
                                String updatequery = "UPDATE " + TABLE_SMD + " SET " + db.FFM_ID + " = " + ffmid + " WHERE " + KEY_TABLE_SMD_DETAIL_ID + " = " + sqlite_id;
                                sdbw.execSQL(updatequery);
                            }

                        }


                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }
            // Common.dismissProgressDialog(progressDialog);
            prepareStockMovementRetailerDataAndPush();
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
                    System.out.println("!!!!!!!1" + Common.getCompleteURL(role, Constants.GET_CUSTOMERS_AND_CUSTOMER_DETAILS, userId, team) + "\n" + jsonData);
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
                    db.deleteDataByTableName(new String[]{"customers", "customer_details"});

                    JSONObject jsonObject = new JSONObject(jsonData);


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
                                //String credit_balance = jsonObjectData.getString("credit_balance");
                                String credit_balance = "0";

                                Log.d("Insert: ", "Inserting Customer details ..");
                                db.addCustomer_details(new Customer_Details(customer_id1, divsion_id, credit_limit, inside_bucket, outside_bucket, status1, created_datetime1, updated_datetime1, credit_balance));

                            }
                        }

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
            }
            /*Log.d("Reading: ", "Reading all customers..");

            List<Customers> customers = db.getAllCustomers();

            for (Customers cus : customers) {
                String log = "Id: "+cus.getID() + " \n customers master ID"+ cus.getCusMasterId()+" , \n customersname: " + cus.getCusName() + "\n cus code "+cus.getCuscode()+" \n Address : "+cus.getCusaddress()+" \n Street : "+cus.getCusstreet()+  "\n cus city & country "+cus.getCus_city() +":"+ cus.getCuscountry()+ " \n C region id"+ cus.getCusregion_Id()+ " \n C telephone"+ cus.getCustelephone()+" \n C C date"+ cus.getCuscdatetime()+" \n C company Id"+ cus.getCuscompany_Id()+" \n C status"+ cus.getCusstatus()+" \n Cus cdate"+cus.getCuscdatetime()+" \n Cus udate"+cus.getCusudatetime();
                // Writing Contacts to log
                Log.e("Customers: ", log);

            }*/


        }
    }


    public class Async_Routepath extends AsyncTask<String, Void, String> {

        private String jsonData;

        protected String doInBackground(String... params) {

            try {

                OkHttpClient client = new OkHttpClient();
                /*For passing parameters*/
                RequestBody formBody = new FormEncodingBuilder()
                        .add("latlon", params[0])
                        .add("tracking_id", params[1])
                        .add("pause", isStringNull(params[2]))
                        .add("resume", isStringNull(params[3]))
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
                    if (jsonData != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(jsonData);
                            if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                                ContentValues contentValues = new ContentValues();
                                contentValues.put(SYNC_STATUS, 1);
                                db.updateTable(TABLE_GEO_TRACKING, contentValues, KEY_TABLE_GEO_TRACKING_FFMID, jsonObject.getString("tracking_id"));

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
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


    }


    private void prepareStockMovementRetailerDataAndPush() {
     /*   mainArray = new JSONArray();

        JSONArray adbBookArray = new JSONArray();
        JSONObject naimObject = null;
        ArrayList<StockMovementRetailerDetails> stockMovementUnSynedPojos = new ArrayList<>();

        ArrayList<StockMovementRetailerDetails> stockMovementUnSynedPojoList = db.getOfflineStockPlacementSMRDListUnSyncData1();
        for (StockMovementRetailerDetails stockMovementUnSynedPojo : stockMovementUnSynedPojoList) {

            stockMovementUnSynedPojos = db.getOfflineStockPlacementSMRDListUnSyncData(Integer.parseInt(stockMovementUnSynedPojo.stockMovementId));
            for (StockMovementRetailerDetails stockMovementUnSynedPojo1 : stockMovementUnSynedPojos) {

                JSONObject advBookObj = new JSONObject();

                try {
                    advBookObj.put("CompanyID", stockMovementUnSynedPojo1.companyId);
                    advBookObj.put("role", stockMovementUnSynedPojo1.userType);
                    advBookObj.put("DivisionID", stockMovementUnSynedPojo1.divisionId);
                    advBookObj.put("id", stockMovementUnSynedPojo1.stockMovementId);
                    advBookObj.put("user_id", userId);
                    advBookObj.put("placed_date", stockMovementUnSynedPojo1.placedDate);
                    advBookObj.put("movement_type", "0");
                    advBookObj.put("retailer_id", stockMovementUnSynedPojo1.retailerId);


                    JSONArray cropArray = new JSONArray();

                    JSONArray productArray = new JSONArray();


                    JSONObject object_one = new JSONObject();
                    object_one.put("stock_placed", stockMovementUnSynedPojo1.stockPlaced);
                    object_one.put("ProductID", stockMovementUnSynedPojo1.productId);
                    object_one.put("current_stock", stockMovementUnSynedPojo1.currentStock);
                    object_one.put("pog", stockMovementUnSynedPojo1.pog);
                    object_one.put("sqlite_id_detail", stockMovementUnSynedPojo1.stockMovementRetailerId);
                    productArray.put(object_one);


                    JSONObject cropObj = new JSONObject();
                    cropObj.put("CropId", stockMovementUnSynedPojo1.cropId);

                    cropArray.put(cropObj);
                    advBookObj.put("Crops", cropArray);
                    cropObj.put("Products", productArray);
                    //}
                    naimObject = new JSONObject();
                    adbBookArray.put(advBookObj);
                    naimObject.put("stockmovement", adbBookArray);


                } catch (JSONException e) {
                }

            }
        }
        if (naimObject != null) {
            mainArray.put(naimObject);
        }
        Log.i("  -json array -", "" + mainArray.toString());
        if (mainArray.length() > 0 || stockMovementUnSynedPojos.size() > 0) {
            new callAPIToPushStockmovementRetailerData().execute();
        }*/
        try {
            List<RetailerStockSupply> offlineRetailerStockSupply = db.getOfflineRetailerStockSupply();
            final RetailerStockReqVo retailerStockReqVo = new RetailerStockReqVo();
            retailerStockReqVo.retailerStock = offlineRetailerStockSupply;
            MyApplication.getInstance().getRetrofitAPI().postRetailerStockSupply(retailerStockReqVo).enqueue(new Callback<RetailerStockResVo>() {
                @Override
                public void onResponse(Call<RetailerStockResVo> call, retrofit2.Response<RetailerStockResVo> response) {
                    try {
                        RetailerStockResVo retailerStockResVo = response.body();
                        if (retailerStockResVo != null) {
                            if (retailerStockResVo.status.equalsIgnoreCase("success")) {
                                List<InsertedRetailer> insertedRetailers = retailerStockResVo.insertedRetailer;
                                if (insertedRetailers != null && insertedRetailers.size() > 0) {
                                    for (int i = 0; i < insertedRetailers.size(); i++) {
                                        db.updateRetailerStockSupply(insertedRetailers.get(i));
                                    }
                                }
                            }
                        }
                    } catch (Exception e) {

                    } finally {
                        prepareStockReturnsDataAndPush(intent);
                    }
                }

                @Override
                public void onFailure(Call<RetailerStockResVo> call, Throwable t) {
                    prepareStockReturnsDataAndPush(intent);
                }
            });
        } catch (Exception e) {
            prepareStockReturnsDataAndPush(intent);
        }
    }


    private class callAPIToPushStockmovementRetailerData extends AsyncTask<String, Void, String> {
        private String jsonData;

        @Override
        protected String doInBackground(String... params) {

            OkHttpClient client = new OkHttpClient();
            try {
                MediaType mediaType = MediaType.parse("application/octet-stream");
                RequestBody body = RequestBody.create(mediaType, mainArray.toString());
                Request request = new Request.Builder()
                        .url(Constants.URL_INSERT_STOCKMOVEMENT_RETAILER)
                        .post(body)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        // .addHeader("postman-token", "6bb3394a-3eaa-d4ae-cd37-0257afbdf3db")
                        .build();

                Response response = null;


                response = client.newCall(request).execute();


                jsonData = response.body().string();
                System.out.println("!!!!!!!1Sales Oreder" + jsonData);
            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            if (jsonData != null) {
                JSONArray jsonArray;
                try {
                    jsonArray = new JSONArray(jsonData);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = null;

                        jsonObject = jsonArray.getJSONObject(i);
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("success")) {
                            JSONArray jsonArrayDetails = jsonObject.getJSONArray("details");
                            for (int j = 0; j < jsonArrayDetails.length(); j++) {
                                JSONObject jsonObject1 = jsonArrayDetails.getJSONObject(j);


                                String sqlite_id = jsonObject1.getString("sqlite_id");
                                String ffmid = jsonObject1.getString("ffm_id");

                                //   Toast.makeText(getActivity(), "sqlite id and ffm id " + service_id + " , " + ffmid, Toast.LENGTH_SHORT).show();
                                Log.e("sqlite id", sqlite_id);
                                Log.e("ffmid", ffmid);
                                sdbw = db.getWritableDatabase();
                                // updateFeedback(Feedback feedback);
                                String updatequery = "UPDATE " + TABLE_STOCK_MOVEMENT_RETAILER_DETAILS + " SET " + db.FFM_ID + " = " + ffmid + " WHERE stock_movement_retailer_id" + " = " + sqlite_id;
                                sdbw.execSQL(updatequery);
                            }

                        }


                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }


        }
    }


    private void prepareStockReturnsDataAndPush(Intent intent) {
        try {
            mainArray = new JSONArray();
            JSONArray adbBookArray = new JSONArray();
            JSONObject naimObject = null;

            ArrayList<StockReturnUnSynedPojo> stockMovementUnSynedPojoList = db.getOfflineStockReturnListUnSyncData1();
            for (StockReturnUnSynedPojo stockMovementUnSynedPojo : stockMovementUnSynedPojoList) {

                ArrayList<StockReturnUnSynedPojo> stockMovementUnSynedPojos = db.getOfflineStockReturnsListUnSyncData(stockMovementUnSynedPojo.stockReturnId);
                for (StockReturnUnSynedPojo stockMovementUnSynedPojo1 : stockMovementUnSynedPojos) {

                    JSONObject advBookObj = new JSONObject();

                    try {
                        advBookObj.put("CompanyID", stockMovementUnSynedPojo1.companyId);
                        advBookObj.put("customer_id", stockMovementUnSynedPojo1.customerId);
                        // advBookObj.put("role", stockMovementUnSynedPojo1.userType);
                        advBookObj.put("DivisionID", stockMovementUnSynedPojo1.divisionId);
                        advBookObj.put("id", stockMovementUnSynedPojo1.stockReturnId);
                        advBookObj.put("user_id", userId);


                        JSONArray cropArray = new JSONArray();

                        JSONArray productArray = new JSONArray();


                        JSONObject object_one = new JSONObject();
                        object_one.put("mobile_stock_return_details_id", stockMovementUnSynedPojo1.stockReturnsDetailsId);
                        object_one.put("ProductID", stockMovementUnSynedPojo1.productId);
                        object_one.put("CropId", stockMovementUnSynedPojo1.cropId);
                        object_one.put("Qunatity", stockMovementUnSynedPojo1.quantity);
                        productArray.put(object_one);


                        JSONObject cropObj = new JSONObject();
                        cropObj.put("CropId", stockMovementUnSynedPojo1.cropId);

                        cropArray.put(cropObj);
                        advBookObj.put("Crops", cropArray);
                        cropObj.put("Products", productArray);
                        //}
                        naimObject = new JSONObject();
                        adbBookArray.put(advBookObj);
                        naimObject.put("stockreturns", adbBookArray);


                    } catch (JSONException e) {
                    }

                }
            }
            if (naimObject != null)
                mainArray.put(naimObject);
            Log.i("  -json array -", "" + mainArray.toString());
            if (mainArray.length() > 0) {
                new callAPIToPushStockReturnsData(intent).execute();
            } else {
                logoutBroadcast(intent);
            }

        } catch (Exception e) {

        } finally {
            prepareMarketPotentialDataAndPush();
        }
    }

    private void prepareChannelPrefDataAndPush() {
        List<ChannelPreference> channelPreferences = db.getOfflineChannelPreferences(Common.getUserIdFromSP(this));
        if (channelPreferences != null && channelPreferences.size() > 0) {
            ChannelPreferenceReqVo  channelPreferenceReqVo = new ChannelPreferenceReqVo();
            channelPreferenceReqVo.channelPreference = channelPreferences;
            new RetrofitRequester(this).sendRequest(Constants.URL_INSERTING_CHANNEL_PREFERENCE, channelPreferenceReqVo, false);
        }
    }

    private void prepareCropShiftsDataAndPush() {
        List<CropShift> cropShifts = db.getOfflineCropShifts(Common.getUserIdFromSP(this));
        if (cropShifts != null && cropShifts.size() > 0) {
            CropShiftReqVo cropShiftReqVo = new CropShiftReqVo();
            cropShiftReqVo.cropShifts = cropShifts;
            new RetrofitRequester(this).sendRequest(Constants.URL_INSERTING_CROP_SHIFTS, cropShiftReqVo, false);
        }
    }

    private void prepareCommodityPriceDataAndPush() {
        List<CommodityPrice> commodityPrices = db.getOfflineCommodityPrices(Common.getUserIdFromSP(this));
        if (commodityPrices != null && commodityPrices.size() > 0) {
            CommodityPriceReqVo commodityPriceReqVo = new CommodityPriceReqVo();
            commodityPriceReqVo.commodityPrice = commodityPrices;
            new RetrofitRequester(this).sendRequest(Constants.URL_INSERTING_COMMODITY_PRICE, commodityPriceReqVo, false);
        }
    }

    private void prepareCompetitorStrengthDataAndPush() {
        List<CompetitorStrength> competitorStrengths = db.getOfflineCompetitorStrengths(Common.getUserIdFromSP(this));
        if (competitorStrengths != null && competitorStrengths.size() > 0) {
            CompetitorStrengthReqVo competitorStrengthReqVo = new CompetitorStrengthReqVo();
            competitorStrengthReqVo.competitorStrength = competitorStrengths;
            new RetrofitRequester(this).sendRequest(Constants.URL_INSERTING_COMPETITOR_STRENGTH, competitorStrengthReqVo, false);
        }
    }

    private void prepareProductPricingDataAndPush() {
        List<ProductPricingSurvey> productPricingSurveys = db.getofflineProductPricingSurvey(Common.getUserIdFromSP(this));
        if (productPricingSurveys != null && productPricingSurveys.size() > 0) {
            ProductPricingReqVo productPricingReqVo = new ProductPricingReqVo();
            productPricingReqVo.productPriceSurvey = productPricingSurveys;
            new RetrofitRequester(this).sendRequest(Constants.URL_INSERTING_PRODUCT_SURVEY, productPricingReqVo, true);
        }
    }

    private void prepareCompetitorChannelDataAndPush() {
        List<CompetitorChannel> competitorChannels = db.getOfflineCompetitorChannels(Common.getUserIdFromSP(this));
        if (competitorChannels != null && competitorChannels.size() > 0) {
            CompetitorChannelReqVo competitorChannelReqVo = new CompetitorChannelReqVo();
            competitorChannelReqVo.competitorChannel = competitorChannels;
            new RetrofitRequester(this).sendRequest(Constants.URL_INSERTING_COMPETITOR_CHANNEL, competitorChannelReqVo, false);
        }
    }


    private void logoutBroadcast(Intent intent) {
        if (intent != null && intent.hasExtra("logout")) {
            if (Common.haveInternet(mContext)) {
                ResultReceiver receiver = (ResultReceiver) intent.getParcelableExtra("logout_receiver");
                Bundle resultData = new Bundle();
                resultData.putBoolean("logout", true);
                receiver.send(UPDATE_PROGRESS, resultData);
                Log.d("logout", "BackPushService.....");
            }
        }
    }


    private class callAPIToPushStockReturnsData extends AsyncTask<Void, Void, String> {
        private String jsonData;
        Intent intent;

        public callAPIToPushStockReturnsData(Intent intent) {
            this.intent = intent;
        }

        @Override
        protected String doInBackground(Void... params) {

            OkHttpClient client = new OkHttpClient();

            MediaType mediaType = MediaType.parse("application/octet-stream");
            RequestBody body = RequestBody.create(mediaType, mainArray.toString());
            Request request = new Request.Builder()
                    .url(Constants.URL_INSERTING_STOCKRETURNS)
                    .post(body)
                    .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                    .addHeader("content-type", "application/x-www-form-urlencoded")
                    .addHeader("cache-control", "no-cache")
                    // .addHeader("postman-token", "6bb3394a-3eaa-d4ae-cd37-0257afbdf3db")
                    .build();

            Response response = null;

            try {
                response = client.newCall(request).execute();


                jsonData = response.body().string();
                System.out.println("!!!!!!!1Sales Oreder" + jsonData);
            } catch (Exception e) {

            }
            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


            if (jsonData != null) {
                JSONArray jsonArray;
                try {
                    jsonArray = new JSONArray(jsonData);

                    for (int i = 0; i < jsonArray.length(); i++) {
                        JSONObject jsonObject = null;

                        jsonObject = jsonArray.getJSONObject(i);
                        String status = jsonObject.getString("status");
                        if (status.equalsIgnoreCase("success")) {
                            JSONArray jsonArrayDetails = jsonObject.getJSONArray("details");
                            for (int j = 0; j < jsonArrayDetails.length(); j++) {
                                JSONObject jsonObject1 = jsonArrayDetails.getJSONObject(j);


                                String sqlite_id = jsonObject1.getString("return_detail_id");
                                String ffmid = jsonObject1.getString("ffm_id");

                                //   Toast.makeText(getActivity(), "sqlite id and ffm id " + service_id + " , " + ffmid, Toast.LENGTH_SHORT).show();
                                Log.e("sqlite id", sqlite_id);
                                Log.e("ffmid", ffmid);
                                sdbw = db.getWritableDatabase();
                                // updateFeedback(Feedback feedback);
                                String updatequery = "UPDATE " + TABLE_STOCK_RETURNS_DETAILS + " SET " + db.KEY_FFMID + " = " + ffmid + " WHERE " + db.KEY_STOCK_RETURNS_DETAILS_ID + " = " + sqlite_id;
                                sdbw.execSQL(updatequery);
                            }

                        }


                    }
                } catch (JSONException e) {

                    e.printStackTrace();
                }
            }

            logoutBroadcast(intent);
        }
    }

}


