package com.nsl.beejtantra;


import android.app.Activity;
import android.app.ProgressDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;

import com.nsl.beejtantra.FarmerCoupans.Activityfarmercoupon;
import com.nsl.beejtantra.TFA.ActivityIndent;
import com.nsl.beejtantra.TFA.Main2Activity;
import com.nsl.beejtantra.advancebooking.AdvanceBookingMainActivity;
import com.nsl.beejtantra.commonutils.Common;
import com.nsl.beejtantra.commonutils.DatabaseUtil;
import com.nsl.beejtantra.complaints.Complaintsselectactivity;
import com.nsl.beejtantra.distributors.DIstributorsListActivity;
import com.nsl.beejtantra.feedback.FeedbackallActivity;
import com.nsl.beejtantra.fieldestimation.FieldEstimationMainAcivity;
import com.nsl.beejtantra.globalagrigenetics.GlobalAgrigeneticsActivity;
import com.nsl.beejtantra.marketintelligence.MarketIntelligenceAcivity;
import com.nsl.beejtantra.marketintelligencenew.MarketIntelligenceAcivityCopy;
import com.nsl.beejtantra.orderindent.OrderIndentMainActivity;
import com.nsl.beejtantra.oreo.AlarmReceiver;
import com.nsl.beejtantra.oreo.JobScheduleService;
import com.nsl.beejtantra.oreo.LocationUpdatesService;
import com.nsl.beejtantra.pojo.Menu;
import com.nsl.beejtantra.pojo.MenuNav;
import com.nsl.beejtantra.product_catalogue.ProductActivity1;
import com.nsl.beejtantra.scheduler.LocationProviderChanged;
import com.nsl.beejtantra.stockmovement.NewStockMovementChooseActivity;
import com.nsl.beejtantra.stockreturns.NewStockReturnsChooseActivity;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_COMPANIES_COMPANY_CODE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_COMPANIES_COMPANY_SAP_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_COMPANIES_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_COMPANIES_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USERS_DESIGNATION;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USERS_EMAIL;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USERS_FIRST_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USERS_HEADQUARTER;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USERS_IMAGE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USERS_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USERS_MOBILE_NO;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USERS_REGION_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USERS_SAP_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USER_COMPANY_DIVISION_COMPANY_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USER_COMPANY_DIVISION_USER_ID;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_COMPANIES;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_USERS;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_USER_COMPANY_DIVISION;
import static com.nsl.beejtantra.oreo.LocationUpdatesService.isCheckedIn;
import static com.nsl.beejtantra.oreo.PlanerOneActivity.mJobId;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentCategories extends Fragment implements View.OnClickListener {
    // JSON parser class

    private GridView gridView;

    String jsonData, userId;
    int role;
    private static final int PICK_IMAGE_ID = 234;
    String regionid, headquarter, image_string, updated_img_string;
    TextView tv_firstname, tv_designation, tv_email, tv_mobile, tv_emp_id, tv_headquarter, tv_workingfor;
    //private SliderLayout imageSlider;
    DatabaseHandler db;
    SQLiteDatabase sdbw, sdbr;
    String User_id;;
    MarshMallowPermission marshMallowPermission;


    StringBuilder builder = new StringBuilder();
    private RoundedImageView profile_image;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    /*String[] Name = {"Planner",*//* "Distributors", "Products", "Schemes", "Advance Booking", "Order Indent", "Payment Collections",
            "Market Intelligence", "Stock Supply", "Stock Returns",*//* "Route Map"*//*, "Feedback", "Complaints", "Daily Diary", "Yield Estimation", "Geotagging"*//*};

    // Array of integers points to images stored in /res/drawable-ldpi/
    int[] flags = new int[]{
            R.mipmap.ic_planner*//*,
            R.mipmap.ic_distributors,
            R.mipmap.ic_products,
            R.mipmap.ic_schemes,
            R.mipmap.ic_advancebooking,
            R.mipmap.ic_orderindent,
            R.mipmap.ic_paymentcollection,
            R.mipmap.ic_marketintelligence,
            R.mipmap.ic_stockmovements,
            R.mipmap.ic_stockmovements*//*,
            R.mipmap.ic_routemap*//*,
            R.mipmap.ic_feedback,
            R.mipmap.ic_complaints,
            R.mipmap.ic_dailydairy,
            R.drawable.go_down,
            R.drawable.ic_location_pin_icon*//*
    };*/
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    private String team;
    ProgressDialog progressDialog;
    private MenuNav menuNav;
    private boolean global = false;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        marshMallowPermission = new MarshMallowPermission(getActivity());
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view = inflater.inflate(R.layout.categories, container, false);
        sharedpreferences = getActivity().getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        userId = sharedpreferences.getString("userId", "");
        team = sharedpreferences.getString("team", "");
        role = sharedpreferences.getInt(Constants.SharedPrefrancesKey.ROLE, 0);
        menuNav = Common.getUserPermissions(getActivity());
        db = new DatabaseHandler(getActivity());
        Log.d("isTimeAutomatic", " " + Common.isTimeAutomatic(getActivity()));
        //db.deleteDataByTableName(new String []{db.TABLE_CATALOGUE_CROPS,db.TABLE_CATALOGUE_CROPS_PRODUCTS});
        profile_image = (RoundedImageView) view.findViewById(R.id.profile_image);
        tv_firstname = (TextView) view.findViewById(R.id.tv_firstname);
        tv_designation = (TextView) view.findViewById(R.id.tv_designation);
        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);

        profile_image.setOnClickListener(this);

        if (Common.haveInternet(getActivity())) {
            new Async_getallucd().execute();
        }

        try {
            File externalDirectory = new File(DatabaseUtil.FOLDER_EXTERNAL_DIRECTORY);
            deleteDirectory(externalDirectory);
        } catch (Exception e) {
            e.printStackTrace();
        }

        //   DatabaseUtil.copyDatabaseToExtStg(getActivity());
        try {
            String selectQuery = "SELECT " + KEY_TABLE_USERS_FIRST_NAME + "," + KEY_TABLE_USERS_SAP_ID + "," + KEY_TABLE_USERS_MOBILE_NO + "," + KEY_TABLE_USERS_EMAIL + "," + KEY_TABLE_USERS_DESIGNATION + "," + KEY_TABLE_USERS_HEADQUARTER + "," + KEY_TABLE_USERS_REGION_ID + "," + KEY_TABLE_USERS_IMAGE + " FROM " + TABLE_USERS + "  WHERE " + KEY_TABLE_USERS_MASTER_ID + " = " + userId;
            sdbw = db.getWritableDatabase();
            Cursor cursor = sdbw.rawQuery(selectQuery, null);
            //System.out.println("cursor count "+cursor.getCount());
            if (cursor.moveToFirst()) {
                //  Log.e("-----", "fname : " + cursor.getString(0) + "lname : " + cursor.getString(1) + "mobile : " + cursor.getString(2) + "email : " + cursor.getString(3));
                tv_firstname.setText(cursor.getString(0));
                tv_designation.setText(cursor.getString(4));
                image_string = cursor.getString(7);
            }
            if (image_string != null) {
                Bitmap bitmap = stringToBitMap(image_string);
                if (bitmap != null) {
                    profile_image.setImageBitmap(bitmap);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }

        try {
            String selectQuery = "SELECT  " + "CR." + KEY_TABLE_COMPANIES_MASTER_ID + "," + KEY_TABLE_COMPANIES_NAME + "," + KEY_TABLE_COMPANIES_COMPANY_CODE + "," + KEY_TABLE_COMPANIES_COMPANY_SAP_ID + " FROM " + TABLE_USER_COMPANY_DIVISION + " AS CDC JOIN " + TABLE_COMPANIES + " AS CR ON CDC." + KEY_TABLE_USER_COMPANY_DIVISION_COMPANY_ID + " = CR." + KEY_TABLE_COMPANIES_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_DIVISION_USER_ID + " in ( " + db.getTeam(Common.getUserIdFromSP(getActivity())) + " ) group by(CR." + KEY_TABLE_COMPANIES_MASTER_ID + ")";
            Common.Log.i("Querrry " + selectQuery);
            sdbw = db.getWritableDatabase();

            Cursor cursor = sdbw.rawQuery(selectQuery, null);
            //System.out.println("cursor count "+cursor.getCount());
            if (cursor.moveToFirst()) {
                do {
                   /* Companies companies = new Companies();

                    companies.setCompanyMasterId(cursor.getString(0));
                    companies.setCompanyName(cursor.getString(1));
                    companies.setCompanycode(cursor.getString(3));
                    companies.setCompanysapid(cursor.getString(2));*/
                    if (cursor.getString(0) != null && cursor.getString(0).equalsIgnoreCase("9")) {
                        global = true;
                    }

                } while (cursor.moveToNext());
            }

        } catch (Exception e) {

        }
        gridView = (GridView) view.findViewById(R.id.gridView);

        // Each row in the list stores country name, currency and flag
        final List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();
        List<Menu> menu = menuNav.menu;
        Log.d("hui",menu.toString());
        for (int i = 0; i < menu.size(); i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            if (menu.get(i).read.equalsIgnoreCase("1")) {
                hm.put("txt", menu.get(i).name);
                hm.put("flag", Integer.toString(getFlag(menu.get(i).name)));
                aList.add(hm);
            }
        }
        if (global) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("txt", Constants.Names.GLOBAL_AGRIGENETICS);
            hm.put("flag", Integer.toString(getFlag(Constants.Names.GLOBAL_AGRIGENETICS)));
            aList.add(hm);
        }

        if(role==Constants.Roles.ROLE_7||role==Constants.Roles.ROLE_6) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("txt", Constants.Names.Demand_generate);
            hm.put("flag", Integer.toString(getFlag(Constants.Names.Demand_generate)));
            aList.add(hm);
        }

        /*User_id= sharedpreferences.getString("userId", "");
        if (role == Constants.Roles.ROLE_6 || role == Constants.Roles.ROLE_7) {
            String selectQuery = "SELECT "+KEY_TABLE_USERS_REGION_ID+" FROM " + TABLE_USERS + "  WHERE " + KEY_TABLE_USERS_MASTER_ID + " = " + User_id;

            sdbw = db.getWritableDatabase();
            Cursor cursor = sdbw.rawQuery(selectQuery, null);
            if (cursor.moveToFirst()) {
                do {
                    regionid = cursor.getString(0);
                    if(regionid.equals("13")||regionid.equals("13")) {
                        HashMap<String, String> hm = new HashMap<String, String>();
                        hm.put("txt", Constants.Names.Farmer_Coupans);
                        hm.put("flag", Integer.toString(getFlag(Constants.Names.Farmer_Coupans)));
                        aList.add(hm);
                    }
                } while (cursor.moveToNext());
            }

        }*/




        // Keys used in Hashmap
        String[] from = {"txt", "flag"};

        // Ids of views in listview_layout
        int[] to = {R.id.tv_gridview, R.id.iv_gridView};

        // Instantiating an adapter to store each items
        // R.layout.listview_layout defines the layout of each item
        SimpleAdapter adapter = new SimpleAdapter(getActivity(), aList, R.layout.gridview_row, from, to);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                view.setSelected(true);
                String name = aList.get(i).get("txt");
                if (name.equalsIgnoreCase(Constants.Names.PLANNER)) {

                    Intent planner = new Intent(getActivity(), PlanerMainActivity.class);
                    startActivity(planner);
//                    MainActivity.showExceedingAlert(getContext(),aList.get(i).get("txt")+"\n"+ Common.getStringResourceText(R.string.will_be_live_soon));
                } else if (name.equalsIgnoreCase(Constants.Names.DISTRIBUTOR)) {

                    Intent distributors = new Intent(getActivity(), DIstributorsListActivity.class);
                    startActivity(distributors);
                } else if (name.equalsIgnoreCase(Constants.Names.PRODUCTS)) {

//                    Integer.parseInt("a");

                    Intent customers = new Intent(getActivity(), ProductActivity1.class);
                    /*customers.putExtra("TITLE", favouriteItem.get(i).get("productName"));
                    customers.putExtra("URL",   favouriteItem.get(i).get("productUrl"));*/
                    startActivity(customers);

                } else if (name.equalsIgnoreCase(Constants.Names.SCHEMES)) {

                    Intent schemes = new Intent(getActivity(), SchemesActivity.class);
                    startActivity(schemes);
                } else if (name.equalsIgnoreCase(Constants.Names.ADVANCE_BOOKING)) {

                    Intent advancebooking = new Intent(getActivity(), AdvanceBookingMainActivity.class);
                    startActivity(advancebooking);
                } else if (name.equalsIgnoreCase(Constants.Names.ORDER_INDENT)) {

                    Intent orderindent = new Intent(getActivity(), OrderIndentMainActivity.class);
                    Common.saveDataInSP(getActivity(),Constants.Names.GLOBAL_AGRIGENETICS,false);
                    startActivity(orderindent);
                } else if (name.equalsIgnoreCase(Constants.Names.PAYMENT_COLLECTIONS)) {

                    Intent payment = new Intent(getActivity(), PaymentActivity.class);
                    Common.saveDataInSP(getActivity(), Constants.Names.GLOBAL_AGRIGENETICS, false);
                    startActivity(payment);
                    // MainActivity.showExceedingAlert(getContext(),aList.get(i).get("txt")+"\n"+ Common.getStringResourceText(R.string.will_be_live_soon));

                } else if (name.equalsIgnoreCase(Constants.Names.MARKET_INTELLIGENCE)) {

                    Intent payment = new Intent(getActivity(), MarketIntelligenceAcivityCopy.class);
                    startActivity(payment);
                    // MainActivity.showExceedingAlert(getContext(),aList.get(i).get("txt")+"\n"+ Common.getStringResourceText(R.string.will_be_live_soon));
                } else if (name.equalsIgnoreCase(Constants.Names.STOCK_SUPPLY)) {

                    // Intent stockmovement = new Intent(getActivity(), StockInformation.class);
//                    Intent stockmovement = new Intent(getActivity(), StockReturnsMainActivity.class);
//                    startActivity(stockmovement);
                    //stockmovement.putExtra("selection", "adv");
                    //startActivity(stockmovement);
                    // MainActivity.showExceedingAlert(getContext(),aList.get(i).get("txt")+"\n"+ Common.getStringResourceText(R.string.will_be_live_soon));
                    Intent newbooking = new Intent(getActivity(), NewStockMovementChooseActivity.class);
                    newbooking.putExtra("selection", "adv");
                    startActivity(newbooking);

                } else if (name.equalsIgnoreCase(Constants.Names.STOCK_RETURNS)) {

                    Intent newbooking = new Intent(getActivity(), NewStockReturnsChooseActivity.class);
                    newbooking.putExtra("selection", "adv");
                    startActivity(newbooking);
//                    MainActivity.showExceedingAlert(getContext(),aList.get(i).get("txt")+"\n"+ Common.getStringResourceText(R.string.will_be_live_soon));
                } else if (name.equalsIgnoreCase(Constants.Names.ROUTE_MAP)) {

                    //  if (!isCheckedIn(getActivity())) {
                    Intent map = new Intent(getActivity(), Main2Activity.class);
                    map.putExtra("map_from_planner", false);
                    startActivity(map);
                    //  }else{
                    //    Toast.makeText(getActivity(),"Please use Route map after checkout.",Toast.LENGTH_LONG).show();
                    //  }
                } else if (name.equalsIgnoreCase(Constants.Names.FEEDBACK)) {
                    Intent feedback = new Intent(getActivity(), FeedbackallActivity.class);
                    startActivity(feedback);
                } else if (name.equalsIgnoreCase(Constants.Names.COMPLAINTS)) {

                    Intent complaints = new Intent(getActivity(), Complaintsselectactivity.class);
                    startActivity(complaints);
                } else if (name.equalsIgnoreCase(Constants.Names.DAILY_DIARY)) {
                    Intent route = new Intent(getActivity(), MainDailyDiaryActivity.class);
                    startActivity(route);
//                    MainActivity.showExceedingAlert(getContext(),aList.get(i).get("txt")+"\n"+ Common.getStringResourceText(R.string.will_be_live_soon));
                } else if (name.equalsIgnoreCase(Constants.Names.YIELD_ESTIMATION)) {
                    Intent route = new Intent(getActivity(), FieldEstimationMainAcivity.class);
                    startActivity(route);
//                   String [] tbl= {"geo_tracking","employee_visit_management"};
//                    db.deleteDataByTableName(tbl);
//                    MainActivity.showExceedingAlert(getContext(),aList.get(i).get("txt")+"\n"+ Common.getStringResourceText(R.string.will_be_live_soon));
                } else if (name.equalsIgnoreCase(Constants.Names.GODOWN)) {
                    Intent godown = new Intent(getActivity(), GodownActivity.class);
                    startActivity(godown);
                } else if (name.equalsIgnoreCase(Constants.Names.GEOTAGGING)) {
                    Intent godown = new Intent(getActivity(), DistRetailerActivity.class);
                    startActivity(godown);
                }else if (name.equalsIgnoreCase(Constants.Names.GLOBAL_AGRIGENETICS)) {
                    Intent godown = new Intent(getActivity(), GlobalAgrigeneticsActivity.class);
                    startActivity(godown);
                }
                else if (name.equalsIgnoreCase(Constants.Names.Activity_PLANNER)) {
                    Intent godown = new Intent(getActivity(), ActivityIndent.class);
                    startActivity(godown);
                }
                else if (name.equalsIgnoreCase(Constants.Names.Field_Activity)) {
                    Intent godown = new Intent(getActivity(), Activityfarmercoupon.class);
                    startActivity(godown);
                }

            }
        });

        if (isCheckedIn(getActivity()) && !LocationUpdatesService.serviceIsRunningInForeground(getActivity())) {
            AlarmReceiver.setAlarm(false, getActivity());
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                ComponentName componentName = new ComponentName(getActivity(), JobScheduleService.class);
                final JobInfo jobInfo = new JobInfo.Builder(mJobId, componentName)
                        .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                        .build();

                JobScheduler jobScheduler = (JobScheduler) getActivity().getSystemService(
                        Context.JOB_SCHEDULER_SERVICE);
                jobScheduler.schedule(jobInfo);
            }
            LocationProviderChanged.saveGpsStatus(getActivity(), "");
        }
        return view;
    }

    public static int getFlag(String name) {
        int flag = R.mipmap.ic_planner;
        switch (name) {
            case Constants.Names.PLANNER:
                flag = R.mipmap.ic_planner;
                break;
            case Constants.Names.DISTRIBUTOR:
                flag = R.mipmap.ic_distributors;
                break;
            case Constants.Names.PRODUCTS:
                flag = R.mipmap.ic_products;
                break;
            case Constants.Names.SCHEMES:
                flag = R.mipmap.ic_schemes;
                break;
            case Constants.Names.ADVANCE_BOOKING:
                flag = R.mipmap.ic_advancebooking;
                break;
            case Constants.Names.ORDER_INDENT:
                flag = R.mipmap.ic_orderindent;
                break;
            case Constants.Names.PAYMENT_COLLECTIONS:
                flag = R.mipmap.ic_paymentcollection;
                break;
            case Constants.Names.MARKET_INTELLIGENCE:
                flag = R.mipmap.ic_marketintelligence;
                break;
            case Constants.Names.STOCK_SUPPLY:
                flag = R.mipmap.ic_stockmovements;
                break;
            case Constants.Names.STOCK_RETURNS:
                flag = R.mipmap.ic_stockmovements;
                break;
            case Constants.Names.ROUTE_MAP:
                flag = R.mipmap.ic_routemap;
                break;
            case Constants.Names.FEEDBACK:
                flag = R.mipmap.ic_feedback;
                break;
            case Constants.Names.COMPLAINTS:
                flag = R.mipmap.ic_complaints;
                break;
            case Constants.Names.DAILY_DIARY:
                flag = R.mipmap.ic_dailydairy;
                break;
            case Constants.Names.YIELD_ESTIMATION:
                flag = R.mipmap.ic_yieldestimation;
                break;
            case Constants.Names.GODOWN:
                flag = R.drawable.go_down;
                break;
            case Constants.Names.GEOTAGGING:
                flag = R.drawable.ic_location_pin_icon;
                break;
            case Constants.Names.GLOBAL_AGRIGENETICS:
                flag = R.drawable.global;
                break;
            case Constants.Names.Demand_generate:
                flag = R.drawable.checklist;
                break;


            case Constants.Names.Farmer_Coupans:
                flag = R.drawable.farmercupon;
                break;


            case Constants.Names.Activity_PLANNER:
                flag = R.drawable.checklist;
                break;

            case Constants.Names.Field_Activity:
                flag = R.mipmap.activityindent;
                break;
        }
        return flag;
    }


    public void handleActivityResult(Bitmap profile_photo) {
        if (profile_photo != null) {
            profile_image.setImageBitmap(profile_photo);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            if (profile_photo != null) {
                profile_photo.compress(Bitmap.CompressFormat.JPEG, 100, baos);
                byte[] data = baos.toByteArray();
                String photo = Base64.encodeToString(data, Base64.DEFAULT);
                db.updateusers(userId, photo);
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("img", "0");
                editor.commit();
                Log.e("updated on sqlite", sharedpreferences.getString("img", ""));

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
                if (Common.haveInternet(getActivity())) {
                    new Async_UpdateProfilePicture().execute(updated_img_string);
                } else {
                    editor = sharedpreferences.edit();
                    editor.putString("img", "0");
                    editor.commit();
                    Log.e("updated  offline", sharedpreferences.getString("img", ""));
                    getActivity().setResult(Activity.RESULT_OK);
                    //getActivity().finish();
                    //   Toast.makeText(getContext(),Common.INTERNET_UNABLEABLE,Toast.LENGTH_SHORT).show();
                }
            }
        }
    }


    /**
     * @param encodedString
     * @return bitmap (from given string)
     */
    public Bitmap stringToBitMap(String encodedString) {
        try {
            byte[] encodeByte = Base64.decode(encodedString, Base64.DEFAULT);
            Bitmap bitmap = BitmapFactory.decodeByteArray(encodeByte, 0, encodeByte.length);
            return bitmap;
        } catch (Exception e) {
            e.getMessage();
            return null;
        }
    }

    @Override
    public void onClick(View v) {
        if (v.getId() == R.id.profile_image) {
            selectImage();
        }
    }

    private void selectImage() {
        if (!marshMallowPermission.checkPermissionForCamera()) {
            marshMallowPermission.requestPermissionForCamera();
        } else if (!marshMallowPermission.checkPermissionForExternalStorage()) {
            marshMallowPermission.requestPermissionForExternalStorage();
        } else {
            Intent chooseImageIntent = ImagePicker.getPickImageIntent(getActivity());
            startActivityForResult(chooseImageIntent, PICK_IMAGE_ID);
        }
    }


    private class Async_getallucd extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = Common.showProgressDialog(getActivity());
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
                        // db.deleteDataByTableName(db.TABLE_USER_COMPANY_DIVISION);

                        JSONArray companyarray = new JSONArray(jsonData);


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
            Common.dismissProgressDialog(progressDialog);

        }
    }

    private class Async_UpdateProfilePicture extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {
            try {

                OkHttpClient client = new OkHttpClient();
                /*For passing parameters*/

                RequestBody formBody = new FormEncodingBuilder()
                        .add("profile_pic", params[0])
                        .add("user_id", userId)
                        .build();

                Response responses = null;
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
                    JSONObject jsonobj = new JSONObject(jsonData);
                    String status = jsonobj.getString("status");
                    if (status.equalsIgnoreCase("SUCCESS")) {
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("img", "1");
                        editor.commit();
                        Log.e("updated on server", sharedpreferences.getString("img", ""));
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);


        }

    }

    public static void deleteDirectory(File dir) {

        if (dir.isDirectory()) {
            String[] children = dir.list();
            if (children == null) {
                return;
            }
            for (int i = 0; i < children.length; i++) {
                File child = new File(dir, children[i]);
                if (child.isDirectory()) {
                    deleteDirectory(child);
                    child.delete();
                } else {
                    child.delete();

                }
            }
            dir.delete();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Common.dismissProgressDialog(progressDialog);
    }
}

