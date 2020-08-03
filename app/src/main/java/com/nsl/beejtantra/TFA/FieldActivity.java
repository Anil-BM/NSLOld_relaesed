package com.nsl.beejtantra.TFA;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.provider.Settings;
import android.support.annotation.NonNull;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nsl.beejtantra.CompletedTfaActivities;
import com.nsl.beejtantra.Constants;
import com.nsl.beejtantra.DatabaseHandler;
import com.nsl.beejtantra.FarmerCoupans.CoupanForm;
import com.nsl.beejtantra.MainActivity;
import com.nsl.beejtantra.R;
import com.nsl.beejtantra.SelectedCities;
import com.nsl.beejtantra.advancebooking.ViewABSDetailsActivity;
import com.nsl.beejtantra.advancebooking.ViewBookings2Activity;
import com.nsl.beejtantra.commonutils.Common;

import com.nsl.beejtantra.oreo.PlanerOneActivity;
import com.nsl.beejtantra.pojo.ServiceOrderApproval;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USERS_FIRST_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USERS_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_SEASON_LINE_ITEMS;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_TFA_ACTIVITYLIST;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_TFA_APPROVAL_HISTORY;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_TFA_VILLAGELIST;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_USERS;
import static com.nsl.beejtantra.orderindent.FragmentOrderIndent.REQUEST_TYPE;

public class FieldActivity extends BaseActivity {
    TextView tv_activity,tv_place,tv_crop,tv_farmers,tv_status;
    boolean iScheckedInClicked = false;
    ArrayList<String> adaptercity;
    private ArrayList<SelectedCities> organisations;
    Spinner selectDistributor,spin_status;
    String userId,userName,team;
    public static final String mypreference = "mypref";
    SharedPreferences sharedpreferences;
    ItemfavitemAdapter adapter;
    DatabaseHandler db;
    SQLiteDatabase sdbw, sdbr;
    int role;
    RecyclerView rv;
    ArrayList<String> status = new ArrayList<String>();
    public TextView event_name, event_purpose, event_status, tv_approvalstatus, tv_accept,approvalInfo;
    public ImageView tv_reject;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    String approval_status,jsonData,seluserId;
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            new Async_gettfalist().execute();

        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        init();
        callapi(1);


        selectDistributor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                seluserId = organisations.get(position).getCityId();
               // Toast("sj"+seluserId);
                if(position>0)
                {
                    if(selectDistributor.getSelectedItemPosition()!=0)

                        new Async_getalltfalistactivities().execute();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spin_status.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {

               // Toast(approval_status);
                if(selectDistributor.getSelectedItemPosition()!=0)
                    new Async_getalltfalistactivities().execute();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        String accept="accept";

        if (accept.equals("accept"))
        //(results.get(position).get("event_approval_status").equalsIgnoreCase("1"))
        {
            tv_approvalstatus.setText("Approved");
            tv_approvalstatus.setTextColor(getResources().getColor(R.color.white));
            tv_approvalstatus.setBackgroundResource(R.drawable.background_green);
            tv_accept.setVisibility(View.GONE);
            tv_reject.setVisibility(View.GONE);
        } else if (!accept.equals("accept"))
        //(results.get(position).get("event_approval_status").equalsIgnoreCase("2"))
        {
            tv_approvalstatus.setText("Rejected");
            tv_approvalstatus.setTextColor(getResources().getColor(R.color.white));
            tv_approvalstatus.setBackgroundResource(R.drawable.background_orange);
            tv_accept.setVisibility(View.GONE);
            tv_reject.setVisibility(View.GONE);
        } else {
            tv_approvalstatus.setText("Pending");
            tv_approvalstatus.setTextColor(getResources().getColor(R.color.black));
            tv_approvalstatus.setBackgroundResource(R.drawable.background_yellow);
            if (userId.equalsIgnoreCase(Common.getUserIdFromSP(getApplicationContext()))) {
                tv_accept.setVisibility(View.GONE);
                tv_reject.setVisibility(View.GONE);
            } else {
                tv_accept.setVisibility(View.VISIBLE);
                tv_reject.setVisibility(View.VISIBLE);
            }
        }





    /*    if (results.get(position).get("approval_status").equalsIgnoreCase("1")) {
            holder.tv_approvalstatus.setText("Approved");
            holder.tv_approvalstatus.setTextColor(getResources().getColor(R.color.white));
            holder.tv_approvalstatus.setBackgroundResource(R.drawable.background_green);
            // holder.btn_view.setVisibility(View.GONE);
        } else if (results.get(position).get("approval_status").equalsIgnoreCase("2")) {
            holder.tv_approvalstatus.setText("Rejected");
            holder.tv_approvalstatus.setTextColor(getResources().getColor(R.color.white));
            holder.tv_approvalstatus.setBackgroundResource(R.drawable.background_orange);
            //holder.btn_view.setVisibility(View.VISIBLE);
        } else {
            holder.tv_approvalstatus.setText("Pending");
            holder.tv_approvalstatus.setBackgroundResource(R.drawable.background_yellow);
            holder.tv_approvalstatus.setTextColor(getResources().getColor(R.color.black));
            // holder.btn_view.setVisibility(View.GONE);
        }*/




        /*approvalInfo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                    final PopupWindow popup = new PopupWindow(ViewBookings2Activity.this);
                final AlertDialog popup = new AlertDialog.Builder(FieldActivity.this).create();
                View dialogView = getLayoutInflater().inflate(R.layout.approval_layout, null);
//                    int displayWidth = getWindowManager().getDefaultDisplay().getWidth() - (getWindowManager().getDefaultDisplay().getWidth() / 6);
//                    int displayHeight = getWindowManager().getDefaultDisplay().getHeight();
//                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(displayWidth, displayHeight);
//                    dialogView.setLayoutParams(layoutParams);
                popup.setView(dialogView);
                popup.show();
                List<Approvaldetails> orderApprovals = db.getOrderApprovalsByOrderId("71818");//results.get(position).get("order_id")

                ImageView closeView = dialogView.findViewById(R.id.close_icon);
                ListView approvalsRecycler = dialogView.findViewById(R.id.approvals_recycler);
                TextView alertView = dialogView.findViewById(R.id.alert_view);
                if (orderApprovals != null && orderApprovals.size() > 0) {
                    approvalsRecycler.setVisibility(View.VISIBLE);
                    alertView.setVisibility(View.GONE);
                    ApprovalsAdapter approvalsAdapter = new ApprovalsAdapter(FieldActivity.this, orderApprovals);
//                    LinearLayoutManager llm = new LinearLayoutManager(ViewBookings2Activity.this, LinearLayoutManager.VERTICAL, false);
//                    approvalsRecycler.setLayoutManager(llm);
                    approvalsRecycler.setAdapter(approvalsAdapter);
                    Common.setListViewHeightBasedOnChildren(approvalsRecycler);
                } else {
                    alertView.setVisibility(View.VISIBLE);
                    approvalsRecycler.setVisibility(View.GONE);
                }
                closeView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        popup.dismiss();
                    }
                });
            }
        });*/















      /*  if (serviceOrderApproval.orderStatus.equalsIgnoreCase("0")) {
            approvalHolder.approvalStatus.setImageResource(R.drawable.pending_icon);
        } else if (serviceOrderApproval.orderStatus.equalsIgnoreCase("1")) {
            approvalHolder.approvalStatus.setImageResource(R.drawable.approved_icon);
        } else if (serviceOrderApproval.orderStatus.equalsIgnoreCase("2")) {
            approvalHolder.approvalStatus.setImageResource(R.drawable.rejected_icon);

        }*/
    }

    private void init() {
        db = new DatabaseHandler(this);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        userId = sharedpreferences.getString("userId", "");
        team = sharedpreferences.getString("team", "");
        role = sharedpreferences.getInt(Constants.SharedPrefrancesKey.ROLE, 0);
        selectDistributor = (Spinner) findViewById(R.id.spin_user);
        spin_status = (Spinner) findViewById(R.id.spin_status);
        tv_activity=(TextView)findViewById(R.id.tv_activity);
        tv_place=(TextView)findViewById(R.id.tv_place);
        tv_crop=(TextView)findViewById(R.id.tv_crop);
        tv_farmers=(TextView)findViewById(R.id.tv_formers1);
        tv_status=(TextView)findViewById(R.id.tv_status);

        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/SEGOEWP-LIGHT.TTF");
        tv_activity.setTypeface(custom_font);
        tv_activity.setTypeface(custom_font);

        tv_place.setTypeface(custom_font);
        tv_place.setTypeface(custom_font);

        tv_crop.setTypeface(custom_font);
        tv_crop.setTypeface(custom_font);

        tv_farmers.setTypeface(custom_font);
        tv_farmers.setTypeface(custom_font);

        rv = (RecyclerView) findViewById(R.id.rv);
        LinearLayoutManager llm = new LinearLayoutManager(FieldActivity.this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(llm);
        // tv_status.setTextColor(getResources().getColor(R.color.black));
        /*
        tv_status.setBackgroundResource(R.drawable.background_yellow);*/


        event_name = (TextView)findViewById(R.id.tv_customer);
        //holder.tv_status       = (TextView) convertView.findViewById(R.id.status);
        event_purpose = (TextView)findViewById(R.id.tv_purpose);
        event_status = (TextView)findViewById(R.id.tv_status);
        tv_approvalstatus = (TextView) findViewById(R.id.tv_approvalstatus);
        tv_accept = (TextView) findViewById(R.id.tv_accept);
        tv_reject = (ImageView) findViewById(R.id.tv_reject);

        approvalInfo = (TextView) findViewById(R.id.history_info);
        new Async_gettfalist().execute();
    }

    private void callapi(int i) {
        SQLiteDatabase sdbw = null;
        Cursor cursor = null;
        organisations = new ArrayList<SelectedCities>();
        adaptercity = new ArrayList<String>();
        organisations.clear();
        SelectedCities citiez = new SelectedCities();
        citiez.setCityId("0");
        citiez.setCityName("Select User");
        // System.out.println("city id is :" + cityId + "city name is :" + cityName);
        organisations.add(citiez);
        adaptercity.add("Select User");
        try {
            String selectQuery;
//            if (i == 0) {
//                String[] newTeam = team.split(",");
//                StringBuilder sb = new StringBuilder();
//                for (String a : newTeam) {
//                    if (!Common.getUserIdFromSP(getApplicationContext()).equalsIgnoreCase(a)) {
//                        if (sb.toString().length() > 0) {
//                            sb.append(",");
//                        }
//
//                        sb.append(a);
//                    }
//                }
//                String teamnew = sb.toString();
//                selectQuery = "SELECT " + KEY_TABLE_USERS_FIRST_NAME + "," + KEY_TABLE_USERS_MASTER_ID + " FROM " + TABLE_USERS + "  WHERE " + db.KEY_TABLE_USERS_STATUS + " = 1 and  user_id in (" + teamnew + ")";
//            } else {

            selectQuery = "SELECT " + KEY_TABLE_USERS_FIRST_NAME + "," + KEY_TABLE_USERS_MASTER_ID + " FROM " + TABLE_USERS + "  WHERE " + db.KEY_TABLE_USERS_STATUS + " = 1 and  role_id  ='19'";

//            }
            //List<Company_division_crops> cdclist = db.getAllCompany_division_crops();

            sdbw = db.getWritableDatabase();

            cursor = sdbw.rawQuery(selectQuery, null);
            System.out.println("cursor count " + "\n selectQuery" + selectQuery);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                   /* Users users = new Users();

                    users.setUserMasterID(cursor.getString(1));
                    users.setUser_first_name(cursor.getString(0));*/

                    SelectedCities cities2 = new SelectedCities();

                    // System.out.println("city id is :" + cityId + "city name is :" + cityName);
                    organisations.add(cities2);
                    if (String.valueOf(cities2.getCityId()).equalsIgnoreCase(Common.getUserIdFromSP(this))) {
                      //  adaptercity.add("SELF");
                        cities2.setCityId(cursor.getString(1));
                        cities2.setCityName(cursor.getString(0));
                    }else {
                        adaptercity.add(cursor.getString(0));
                        cities2.setCityId(cursor.getString(1));
                        cities2.setCityName(cursor.getString(0));
                    }

                } while (cursor.moveToNext());
            } else {
                //isNoUsers = true;
            }

            // do some stuff....
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Common.closeCursor(cursor);
            Common.closeDataBase(sdbw);
        }

        selectDistributor.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, R.id.customSpinnerItemTextView, adaptercity));




        status.add("Inprogress");
        status.add("Pending for approvals");
        status.add("Completed");
        spin_status.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, R.id.customSpinnerItemTextView, status));

        /*new Async_getalltfalistactivities().execute();*/


    }


    private class Async_getalltfalistactivities extends AsyncTask<Void, Void, String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            favouriteItem.clear();
        }
        @Override
        protected String doInBackground(Void... voids) {

            try {
                if(role==Constants.Roles.ROLE_17)
                {
                    seluserId=userId;
                }
                //Toast("sj"+seluserId);
                String selectQuery = null;
                if(spin_status.getSelectedItemPosition()==0) {
                    /* selectQuery = "select tfa.tfa_list_id," +
                            "            d.district_name," +
                            "                        div.division_name," +
                            "                        pd.product_name," +
                            "                        tfaa.tfa_title," +
                            "                        tfa.activity_date," +
                            "                        tfa.taluka," +
                            "                        tfa.village," +
                            "                        tfa.no_of_farmers," +
                            "                        tfa.estimation_per_head," +
                            "                        tfa.total_expences," +
                            "                        tfa.advance_required," +
                            "                        tfa.conducting_place," +
                            "                        tfa.user_id," +
                            "                        tfa.created_user_id," +
                            "                        aprv.approval_status," +
                             "                        tfaa.tfa_master_id," +
                             "                        aprv.tfa_approval_id," +
                             "                        aprv.approved_user_id" +
                            "                from tfa_activity_list tfa" +
                            "                INNER JOIN districts d on tfa.district_id = d.district_id" +
                            "                INNER JOIN division div on tfa.division_id = div.division_id" +
                            "                INNER JOIN products pd  on tfa.product_id = pd.product_id" +
                            "                INNER JOIN tfa_activity_master tfaa on tfa.tfa_activity_master_id = tfaa.tfa_master_id" +
                            "                INNER JOIN tfa_approval_history aprv  on tfa.tfa_list_id = aprv.tfa_list_id" +
                             " where tfa.approval_status='4'  " +
                             "and created_user_id='" + seluserId + "'";*/


                    selectQuery ="select tfa.tfa_list_id,"
                            +"(SELECT district_name FROM districts WHERE districts.district_id=tfa.district_id) AS district_name ,"
                            +"(SELECT division_name FROM division WHERE division.division_id=tfa.division_id) AS division_name ,"
                            +"(SELECT brand_name FROM products WHERE products.product_id=tfa.product_id) AS brand_name ,"
                            +"(SELECT tfa_title FROM tfa_activity_master WHERE tfa_activity_master.tfa_master_id=tfa.tfa_activity_master_id) AS tfa_title ,"
                            +"tfa.activity_date,"
                            + "tfa.taluka,"
                            +"tfa.village,"
                            +"tfa.no_of_farmers,"
                            +"tfa.estimation_per_head,"
                            +"tfa.total_expences,"
                            +"tfa.advance_required,"
                            +"tfa.conducting_place,"
                            +"tfa.user_id,"
                            +"tfa.created_user_id,"
                            +"tfa.approval_status,"
                            +"(SELECT tfa_master_id FROM tfa_activity_master WHERE tfa_activity_master.tfa_master_id=tfa.tfa_activity_master_id) AS tfa_master_id ,"
                            +"(SELECT tfa_approval_id FROM tfa_approval_history WHERE tfa_approval_history.approval_status=tfa.approval_status) AS tfa_approval_id ,"
                            +"(SELECT approved_user_id FROM tfa_approval_history WHERE tfa_approval_history.approval_status=tfa.approval_status) AS approved_user_id ,"
                            +"(SELECT crop_name FROM crops WHERE crops.crop_id=tfa.crop_id) AS crop_name, "
                            +"tfa.used_farmers, "
                            +"tfa.non_used_farmers, "
                            +"tfa.tfa_activity_master_id, "
                            +"tfa.no_of_farmers, "
                            +"tfa.total_expences, "
                            +"tfa.location_lat_lng "
                            +" from tfa_activity_list tfa where       tfa.approval_status='4' "
                            +"                and user_id='"+seluserId+"'"
                            +"                order by tfa.tfa_list_id DESC";
                    Log.d("plans", selectQuery);
                }
                if(spin_status.getSelectedItemPosition()==1) {
                    selectQuery ="select tfa.tfa_list_id,"
                            +"(SELECT district_name FROM districts WHERE districts.district_id=tfa.district_id) AS district_name ,"
                            +"(SELECT division_name FROM division WHERE division.division_id=tfa.division_id) AS division_name ,"
                            +"(SELECT brand_name FROM products WHERE products.product_id=tfa.product_id) AS brand_name ,"
                            +"(SELECT tfa_title FROM tfa_activity_master WHERE tfa_activity_master.tfa_master_id=tfa.tfa_activity_master_id) AS tfa_title ,"
                            +"tfa.activity_date,"
                            + "tfa.taluka,"
                            +"tfa.village,"
                            +"tfa.actual_no_farmers,"
                            +"tfa.estimation_per_head,"
                            +"tfa.actual_total_expences,"
                            +"tfa.advance_required,"
                            +"tfa.conducting_place,"
                            +"tfa.user_id,"
                            +"tfa.created_user_id,"
                            +"tfa.approval_status,"
                            +"(SELECT tfa_master_id FROM tfa_activity_master WHERE tfa_activity_master.tfa_master_id=tfa.tfa_activity_master_id) AS tfa_master_id ,"
                            +"(SELECT tfa_approval_id FROM tfa_approval_history WHERE tfa_approval_history.approval_status=tfa.approval_status) AS tfa_approval_id ,"
                            +"(SELECT approved_user_id FROM tfa_approval_history WHERE tfa_approval_history.approval_status=tfa.approval_status) AS approved_user_id ,"
                            +"(SELECT crop_name FROM crops WHERE crops.crop_id=tfa.crop_id) AS crop_name, "
                            +"tfa.used_farmers, "
                            +"tfa.non_used_farmers, "
                            +"tfa.tfa_activity_master_id, "
                            +"tfa.no_of_farmers, "
                            +"tfa.total_expences, "
                            +"tfa.location_lat_lng "
                            +" from tfa_activity_list tfa where       (tfa.approval_status='5' or  tfa.approval_status='6' or tfa.approval_status='7' )"
                            +"                and user_id='"+seluserId+"'"
                            +"                order by tfa.tfa_list_id DESC";
                    Log.d("plans", selectQuery);
                }
                if(spin_status.getSelectedItemPosition()==2)
                {
                    selectQuery ="select tfa.tfa_list_id,"
                            +"(SELECT district_name FROM districts WHERE districts.district_id=tfa.district_id) AS district_name ,"
                            +"(SELECT division_name FROM division WHERE division.division_id=tfa.division_id) AS division_name ,"
                            +"(SELECT brand_name FROM products WHERE products.product_id=tfa.product_id) AS brand_name ,"
                            +"(SELECT tfa_title FROM tfa_activity_master WHERE tfa_activity_master.tfa_master_id=tfa.tfa_activity_master_id) AS tfa_title ,"
                            +"tfa.activity_date,"
                            + "tfa.taluka,"
                            +"tfa.village,"
                            +"tfa.actual_no_farmers,"
                            +"tfa.estimation_per_head,"
                            +"tfa.actual_total_expences,"
                            +"tfa.advance_required,"
                            +"tfa.conducting_place,"
                            +"tfa.user_id,"
                            +"tfa.created_user_id,"
                            +"tfa.approval_status,"
                            +"(SELECT tfa_master_id FROM tfa_activity_master WHERE tfa_activity_master.tfa_master_id=tfa.tfa_activity_master_id) AS tfa_master_id ,"
                            +"(SELECT tfa_approval_id FROM tfa_approval_history WHERE tfa_approval_history.approval_status=tfa.approval_status) AS tfa_approval_id ,"
                            +"(SELECT approved_user_id FROM tfa_approval_history WHERE tfa_approval_history.approval_status=tfa.approval_status) AS approved_user_id ,"
                            +"(SELECT crop_name FROM crops WHERE crops.crop_id=tfa.crop_id) AS crop_name, "
                            +"tfa.used_farmers, "
                            +"tfa.non_used_farmers, "
                            +"tfa.tfa_activity_master_id, "
                            +"tfa.no_of_farmers, "
                            +"tfa.total_expences, "
                            +"tfa.location_lat_lng "
                            +" from tfa_activity_list tfa where       (tfa.approval_status='8' or  tfa.approval_status='9' )"
                            +"                and user_id='"+seluserId+"'"
                            +"                order by tfa.tfa_list_id DESC";
                    Log.d("plans", selectQuery);
                }
                sdbw = db.getWritableDatabase();



                /* */


                Cursor cursor = sdbw.rawQuery(selectQuery, null);
                if (cursor.moveToFirst()) {
                    do {
                        Log.d("plans", "no plans11");
                        // list_dates.add(cursor.getString(0));
                        HashMap<String,String> map=new HashMap();

                        map.put("tfa_list_id",cursor.getString(0));
                        map.put("district_name",cursor.getString(1));
                        map.put("division_name",cursor.getString(2));
                        map.put("brand_name",cursor.getString(3)); //imp anil;
                        map.put("product_id",cursor.getString(3));
                        map.put("tfa_title",cursor.getString(4));
                        map.put("activity_date",cursor.getString(5));
                        map.put("taluka",cursor.getString(6));
                        map.put("village",cursor.getString(7));
                        map.put("no_of_farmers",cursor.getString(8));//actul
                        map.put("estimation_per_head",cursor.getString(9));//actul
                        map.put("total_expences",cursor.getString(10));
                        map.put("advance_required",cursor.getString(11));
                        map.put("conducting_place",cursor.getString(12));
                        map.put("user_id",cursor.getString(13));
                        map.put("created_user_id",cursor.getString(14));
                        map.put("approval_status",cursor.getString(15));
                        map.put("tfa_approval_id",cursor.getString(17));
                        map.put("crop_name",cursor.getString(19));
                        map.put("used_farmers",cursor.getString(20));
                        map.put("non_used_farmers",cursor.getString(21));
                        map.put("tfa_activity_master_id",cursor.getString(22));
                        map.put("one_farmers",cursor.getString(23));
                        map.put("two_cost",cursor.getString(24));
                        map.put("three_latlng",cursor.getString(25));
                        favouriteItem.add(map);

                    } while (cursor.moveToNext());
                } else {
                    Log.d("plans", "no plans");
                }

            } catch (Exception e) {
                e.printStackTrace();
            }
            return jsonData;
        }
        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Log.d("plans", favouriteItem.toString());
            if(favouriteItem.size()==0)
            {
                adapter = new ItemfavitemAdapter(getApplicationContext(), favouriteItem);
                rv.setAdapter(adapter);
            }
            else
            {
                //tv_noresults.setVisibility(View.GONE);
                adapter = new ItemfavitemAdapter(getApplicationContext(), favouriteItem);
                rv.setAdapter(adapter);
            }
            //adapter.updateResults(favouriteItem);
        }
    }

    class ItemfavitemAdapter extends RecyclerView.Adapter<ItemfavitemAdapter.ViewHolder>
    {

        Context context;
        ArrayList<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();


        public ItemfavitemAdapter(Context context, ArrayList<HashMap<String, String>> results) {
            this.context = context;
            this.results = results;

        }

        @NonNull
        @Override
        public ItemfavitemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            ItemfavitemAdapter.ViewHolder viewHolder = new ItemfavitemAdapter.ViewHolder(getLayoutInflater().inflate(R.layout.row_tfalist, null));
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ItemfavitemAdapter.ViewHolder holder, final int position) {
           /* Type  : Crop cutting experiment
            Place : Wanaparthy,kothaguda
            Crop  : Field Crops,Paddy
            Count : 40 farmers(20,000)*/
            holder.tv_date.setVisibility(View.VISIBLE);
            holder.tv_activity.setText("Type  : "+(favouriteItem.get(position).get("tfa_title")));
            holder.tv_place.setText("Place : "+(favouriteItem.get(position).get("village")+" ,"+
                    favouriteItem.get(position).get("taluka")+" ,"+favouriteItem.get(position).get("district_name")));
            holder.tv_crop.setText("Product   : "+(favouriteItem.get(position).get("brand_name")+" ("+
                    favouriteItem.get(position).get("crop_name")+" - "+favouriteItem.get(position).get("division_name")+")"));
            holder.tv_date.setText(favouriteItem.get(position).get("activity_date"));
            holder.tv_formers1.setText("Count : "+(favouriteItem.get(position).get("no_of_farmers")+" farmers ("
                    +favouriteItem.get(position).get("total_expences")+" Rs)"));
            holder.tv_adv.setText("Advance taken: "+(favouriteItem.get(position).get("advance_required")+"( Rs)"));

            approval_status=favouriteItem.get(position).get("approval_status");



            //Toast(approval_status);

            if(Integer.valueOf(approval_status)==5||Integer.valueOf(approval_status)==6
                    ||Integer.valueOf(approval_status)==7){
                holder.tv_accept.setVisibility(View.GONE);
                holder.tv_reject.setVisibility(View.GONE);
                holder.tv_approvalstatus.setText("Pending");
                holder.tv_approvalstatus.setBackgroundResource(R.drawable.background_yellow);
                holder.tv_approvalstatus.setTextColor(getResources().getColor(R.color.black));
            }
            else if(Integer.valueOf(approval_status)==4){
                holder.tv_accept.setVisibility(View.GONE);
                holder.approvalInfo.setVisibility(View.GONE);
                holder.tv_reject.setVisibility(View.GONE);
                holder.tv_approvalstatus.setText("Inprogress");
                holder.tv_approvalstatus.setBackgroundResource(R.drawable.background_yellow);
                holder.tv_approvalstatus.setTextColor(getResources().getColor(R.color.black));
            }
            else if (Integer.valueOf(approval_status)==8) {
                holder.tv_approvalstatus.setText("Approved");
                holder.approvalInfo.setVisibility(View.GONE);
                holder.tv_accept.setVisibility(View.GONE);
                holder.tv_reject.setVisibility(View.GONE);
                holder.tv_approvalstatus.setTextColor(getResources().getColor(R.color.white));
                holder.tv_approvalstatus.setBackgroundResource(R.drawable.background_green);
            } else if (Integer.valueOf(approval_status)==9) {
                holder.tv_reject.setVisibility(View.GONE);
                holder.tv_accept.setVisibility(View.GONE);
                holder.tv_approvalstatus.setText("Rejected");
                holder.tv_approvalstatus.setTextColor(getResources().getColor(R.color.white));
                holder.tv_approvalstatus.setBackgroundResource(R.drawable.background_orange);//no use anil
            }
            holder.ll_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<HashMap<String, String>> favouriteItem2 = new ArrayList<HashMap<String, String>>();
                    favouriteItem2.add(favouriteItem.get(position));

                    if(approval_status.equals("4")&&role==Constants.Roles.ROLE_17)
                    {
                        Calendar c1 = Calendar.getInstance();
                        SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                        String cdate=sdf1.format(c1.getTime());
                       /* if(favouriteItem.get(0).get("activity_date").equals(cdate))
                        {*/
                       /* Intent intent = new Intent(FieldActivity.this, FieldActivityForm.class);
                        intent.putExtra("favouriteItem", favouriteItem2);
                        intent.putExtra("selectedid", userId);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);*/
                        if(spin_status.getSelectedItemPosition()==2)
                        {
                            Intent intent = new Intent(FieldActivity.this, CompletedTfaActivities.class);
                            intent.putExtra("favouriteItem", favouriteItem2);
                            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(intent);
                        }
                        else { movetofieldactivityform(favouriteItem2);}

                       /* }
                        else {
                            Toast("You can't open future activities");
                        }*/
                    }
                    else if(Integer.valueOf(approval_status)>4)
                    {
                        if(Integer.valueOf(approval_status)==4&&role==Constants.Roles.ROLE_17)
                        {
                            Toast("You can't open until tfa completes this activity");

                            movetofieldactivityform(favouriteItem2);
                        }
                        else
                        {
                            if(spin_status.getSelectedItemPosition()==2)
                            {
                                Intent intent = new Intent(FieldActivity.this, CompletedTfaActivities.class);
                                intent.putExtra("favouriteItem", favouriteItem2);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                            else {
                                Intent intent = new Intent(FieldActivity.this, DetailledActivtylist2.class);
                                intent.putExtra("favouriteItem", favouriteItem2);
                                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                startActivity(intent);
                            }
                        }
                    }


                }
            });

       /*      holder.tv_accept.setTag(position);
            holder.tv_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //isApproval = true;
                    int position = (int) v.getTag();
                    String order_id = results.get(position).get("order_id");
                    if (Utility.isNetworkAvailable(ActivityIndent.this, true)) {
                        //showCommentsDialog(results.get(position).get("order_id"), v, 1);
                    } else {
                        //  db.updateApprovalOrRejectStatus(Common.getUserIdFromSP(getApplicationContext()),"1","",order_id,true);
                    }
                }
            });

            holder.tv_reject.setTag(position);
            holder.tv_reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                   // isApproval = false;
                    int position = (int) v.getTag();

                    //showCommentsDialog(results.get(position).get("order_id"), v, 2);

                }
            });

            holder.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                  *//*  Intent customers = new Intent(getApplicationContext(), ViewSalesOrderCustomerDetailsActivity.class);
                    customers.putExtra("customer_name", results.get(position).get("customer_name"));
                    customers.putExtra("customer_code", results.get(position).get("customer_code"));
                    customers.putExtra("ABS", results.get(position).get("ABS"));
                    customers.putExtra("customer_id", results.get(position).get("customer_id"));
                    customers.putExtra("Token_Amount", results.get(position).get("Token_Amount"));
                    customers.putExtra("company_name", results.get(position).get("company_name"));
                    customers.putExtra("divison_id", results.get(position).get("DivisionId"));
                    customers.putExtra(REQUEST_TYPE, 1);
                    customers.putExtra("TYPE", "rm");
                    customers.putExtra("userId", userId);
                    customers.putExtra("service_id", results.get(position).get("service_id"));
                    customers.putExtra("order_id", results.get(position).get("order_id"));
                    customers.putExtra("approval_status", results.get(position).get("approval_status"));

                    startActivityForResult(customers, REQUEST_CODE_APPROVAL);*//*
                }
            });
*/
            final ItemfavitemAdapter.ViewHolder finalHolder = holder;
            holder.approvalInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final AlertDialog popup = new AlertDialog.Builder(FieldActivity.this).create();
                    final View dialogView = getLayoutInflater().inflate(R.layout.approval_layout, null);
                    popup.setView(dialogView);
                    popup.show();
                    List<Approvaldetails> orderApprovals = db.getOrderApprovalsByTfaListId(favouriteItem.get(position).get("tfa_list_id"));


                    ImageView closeView = dialogView.findViewById(R.id.close_icon);
                    ListView approvalsRecycler = dialogView.findViewById(R.id.approvals_recycler);
                    TextView alertView = dialogView.findViewById(R.id.alert_view);
                    if (orderApprovals != null && orderApprovals.size() > 0)
                    {
                        approvalsRecycler.setVisibility(View.VISIBLE);
                        alertView.setVisibility(View.GONE);
                        ApprovalsAdapter approvalsAdapter = new ApprovalsAdapter(FieldActivity.this, orderApprovals);
                        approvalsRecycler.setAdapter(approvalsAdapter);
                        Common.Log.i("Approvals");
                        Common.Log.i("--------");
                        Common.setListViewHeightBasedOnChildren(approvalsRecycler);
                    }
                    else
                    {
                        alertView.setVisibility(View.VISIBLE);
                        approvalsRecycler.setVisibility(View.GONE);
                    }
                    closeView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popup.dismiss();
                        }
                    });
                }
            });
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public int getItemCount() {
            return results.size();
        }


        public class ViewHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.ll_layout)
            LinearLayout ll_layout;
            @BindView(R.id.ll_accept_reject)
            LinearLayout ll_accept_reject;
            @BindView(R.id.tv_activity)
            TextView tv_activity;
            @BindView(R.id.tv_place)
            TextView tv_place;
            @BindView(R.id.tv_crop)
            TextView tv_crop;

            @BindView(R.id.tv_formers1)
            TextView tv_formers1;
            @BindView(R.id.tv_adv)
            TextView tv_adv;
            @BindView(R.id.tv_approvalstatus)
            TextView tv_approvalstatus;
            @BindView(R.id.tv_reject)
            ImageView tv_reject;
            @BindView(R.id.tv_accept)
            TextView tv_accept;
            @BindView(R.id.history_info)
            TextView approvalInfo;

            @BindView(R.id.tv_date)
            TextView tv_date;

            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

        public void updateResults(ArrayList<HashMap<String, String>> results) {
           /* if (results.size() == favouriteItemFinal.size())
                loadMore.setVisibility(View.GONE);
            this.results = results;
            notifyDataSetChanged();
            if (loadDialog != null && loadDialog.isShowing())
                loadDialog.dismiss();*/
        } //Anil
    }



    private class Async_gettfalist extends AsyncTask<Void, Void, String> {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            favouriteItem.clear();
            progressDialog = new ProgressDialog(FieldActivity.this);
            progressDialog.setMessage("Please Wait ...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            // bar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(Void... params) {

            //odb.delete(TABLE_DIVISION,null,null);

            try {
                OkHttpClient client = new OkHttpClient();


                Response responses = null;


                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

                Request request = new Request.Builder()
                        .url(Constants.URL_NSL_MAIN + Constants.URL_TfA_SERVER_TO_LOCAl+"?user_id="+userId)
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

                    JSONObject companyarray = new JSONObject(jsonData);
                    if(companyarray.get("Status").equals("Success"))
                    {
                        db.deleteDataByTableName(TABLE_TFA_ACTIVITYLIST);
                        db.deleteDataByTableName(TABLE_TFA_VILLAGELIST);

                        db.deleteDataByTableName(TABLE_SEASON_LINE_ITEMS);
                        JSONArray tfa_list_data=companyarray.getJSONArray("tfa_list_data");
                        for(int i=0;i<tfa_list_data.length();i++)
                        {

                            JSONObject object = tfa_list_data.getJSONObject(i);
                            ActivityPlanner act=new ActivityPlanner(
                                    object.getInt("tfa_list_id"),
                                    object.getInt("district_id"),
                                    object.getInt("division_id"),
                                    object.getInt("crop_id"),
                                    object.getInt("product_id"),
                                    object.getInt("tfa_activity_master_id"),
                                    object.getString("activity_date"),
                                    object.getString("taluka"),
                                    object.getString("village"),
                                    object.getInt("no_of_farmers"),
                                    object.getInt("used_farmers"),
                                    object.getInt("non_used_farmers"),
                                    object.getInt("actual_no_farmers"),
                                    object.getInt("estimation_per_head"),
                                    object.getInt("actual_estimation_per_head"),
                                    object.getInt("total_expences"),
                                    object.getInt("actual_total_expences"),
                                    object.getInt("advance_required"),
                                    object.getString("conducting_place"),
                                    object.getInt("user_id"),
                                    object.getInt("created_user_id"),
                                    object.getString("user_email"),
                                    object.getInt("status"),
                                    object.getInt("approval_status"),
                                    object.getString("approval_comments"),
                                    object.getInt("approved_by"),
                                    object.getString("approved_date"),
                                    object.getString("location_lat_lang"),
                                    object.getString("owner_number"),
                                    object.getString("owner_name"),
                                    "","","","","","","",
                                    object.getInt("sync_status"),
                                    object.getString("created_datetime"),
                                    object.getString("updated_datetime"));
                            db.addPlannerActivity(act,"server");
                            Log.e("Tbale_tfalist","inserting");


                        }

                        JSONArray tfa_vill_data=companyarray.getJSONArray("tfa_vill_data");
                        for(int j=0;j<tfa_vill_data.length();j++)
                        {
                            JSONArray jsonArray = tfa_vill_data.getJSONArray(j);
                            for(int i=0;i<jsonArray.length();i++)
                            {
                                JSONObject object2 = jsonArray.getJSONObject(i);
                                Village_list village_list = new Village_list(
                                        object2.getInt("tfa_village_id"),
                                        object2.getInt("tfa_list_id"),
                                        object2.getString("current_sal"),
                                        object2.getString("potential"),
                                        object2.getInt("status"),
                                        object2.getInt("sync_status"),
                                        object2.getString("village_name"),
                                        object2.getString("created_datetime"),
                                        object2.getString("updated_datetime")
                                );
                                //Integer tfa_village_id,Integer tfa_list_id, String current_sal, String potential, Integer status, Integer sync_status, String village_name, String created_datetime
                                long ch1=db.addVillageList(village_list,"server");
                                if(ch1>0)
                                    Log.d("table_vill","inserted");
                            }
                        }
                        db.deleteDataByTableName(TABLE_TFA_APPROVAL_HISTORY);
                        JSONArray tfa_approval_data=companyarray.getJSONArray("tfa_approval_data");
                        for(int j=0;j<tfa_approval_data.length();j++)
                        {
                            JSONArray jsonArray2 = tfa_approval_data.getJSONArray(j);

                            for(int i=0;i<jsonArray2.length();i++)
                            {
                                JSONObject object3 = jsonArray2.getJSONObject(i);
                                Approvaldetails aprvdet=new Approvaldetails(
                                        object3.getInt("tfa_approval_id"),
                                        object3.getInt("tfa_list_id"),
                                        object3.getInt("tfa_approval_id"),
                                        object3.getInt("approval_status"),
                                        object3.getString("approval_comment"),
                                        object3.getString("approved_user_id"),//by
                                        object3.getString("updated_datetime"),
                                        object3.getString("approved_user_id"),
                                        "1",

                                        object3.getString("created_datetime"),
                                        object3.getString("updated_datetime"),
                                        "1",
                                        object3.getString("approved_user_name"),
                                        object3.getString("approved_user_role"),
                                        object3.getString("approved_user_email"),
                                        object3.getString("approved_user_mobile"),
                                        object3.getString("pending_by_name"),
                                        object3.getString("pending_by_role"));

                                String sts =db.updatetfa_approval(aprvdet,"server");

                                if(sts.equals("sucess"))
                                {
                                    Log.d("Inserted","tfa_approval_history");
                                }
                            }

                        }
                    }




                } catch (Exception e) {
                    e.printStackTrace();

                }
                progressDialog.dismiss();
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                //Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
            }

            if (Common.haveInternet(getApplicationContext())) {  // connected to the internet



                // new Async_getallproducts_price().execute(); //here
                //     new Async_getallproducts_price().execute(); //here
            } else {
                //     Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            }

            progressDialog.dismiss();
        }
    }
    @Override
    protected void onResume() {
        super.onResume();
        LocalBroadcastManager.getInstance(getApplicationContext()).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-message"));
    }

    @Override
    protected void onPause() {
        super.onPause();

    }
    private void movetofieldactivityform(ArrayList<HashMap<String, String>> favouriteItem2) {
        if (Common.haveInternet(FieldActivity.this)) {


            LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
            boolean gps_enabled = false;
            boolean network_enabled = false;

            try {
                gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
            } catch (Exception ex) {
            }

            try {
                network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
            } catch (Exception ex) {
            }

            if (!gps_enabled && !network_enabled) {
                Toast.makeText(getApplicationContext(), "Please Enable Location", Toast.LENGTH_LONG).show();
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            } else {
                Intent intent = new Intent(FieldActivity.this, FieldActivityForm.class);
                intent.putExtra("favouriteItem", favouriteItem2);
                intent.putExtra("selectedid", userId);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                startActivity(intent);
            }

        }
        else
        {
            Toast("Please make sure that you have internet connectivity");
        }

    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent it=new Intent(FieldActivity.this, MainActivity.class);
        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(it);
    }
}
