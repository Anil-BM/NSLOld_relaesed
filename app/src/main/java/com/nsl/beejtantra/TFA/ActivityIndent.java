package com.nsl.beejtantra.TFA;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CalendarView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.nsl.beejtantra.Constants;
import com.nsl.beejtantra.DatabaseHandler;
import com.nsl.beejtantra.MarshMallowPermission;
import com.nsl.beejtantra.R;
import com.nsl.beejtantra.SelectedCities;
import com.nsl.beejtantra.Utility;
import com.nsl.beejtantra.commonutils.Common;
import com.nsl.beejtantra.commonutils.NetworkChangeListenerActivity;
import com.nsl.beejtantra.orderindent.ViewSalesOrderCustomerDetailsActivity;
import com.nsl.beejtantra.pojo.RejectionCommentVo;
import com.prolificinteractive.materialcalendarview.CalendarDay;
import com.prolificinteractive.materialcalendarview.DayViewDecorator;
import com.prolificinteractive.materialcalendarview.DayViewFacade;
import com.prolificinteractive.materialcalendarview.MaterialCalendarView;
import com.prolificinteractive.materialcalendarview.OnDateSelectedListener;
import com.prolificinteractive.materialcalendarview.spans.DotSpan;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collection;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CROPS_CROP_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CROPS_CROP_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CROPS_DIVISION_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_DIVISION_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_DIVISION_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_SERVICEORDER_DETAIL_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_SERVICEORDER_DETAIL_PRODUCT_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USERS_FIRST_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USERS_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_USER_ID;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_CROPS;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_DEMANDGENERATION;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_DIVISION;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_SEASON_LINE_ITEMS;

import static com.nsl.beejtantra.DatabaseHandler.TABLE_TFA_ACTIVITYLIST;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_TFA_APPROVAL_HISTORY;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_TFA_VILLAGELIST;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_USERS;

public class ActivityIndent extends NetworkChangeListenerActivity  {
    public static final String mypreference = "mypref";
    private static final int PLANNER_ONE_ACTIVITY = 333;
    public static boolean isSignedIn = false;
    LocationManager locationManager;
    boolean mBound = false;
    String i;
    String jsonData, userId, companyinfo, chkevm, sel_company_id, userName,region,regionId,seluserId;
    int role;
    Thread t1,t2;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    SharedPreferences sharedpreferences;
    TextView tv_date;
    DatabaseHandler db;
    SQLiteDatabase sdbw, sdbr;
    // FloatingActionButton fab;
    DateFormat dateFormat;
    SimpleDateFormat sdf;
    Date myDate;
    RelativeLayout checkinlayout;
    DateFormat timeFormat, currenttimeFormat;
    Date mytime;
    Date checkout, currenttime;
    String datefromcalander, dateString,  datefromrecords, cname, caddress;
    boolean iScheckedInClicked = false;
    ArrayList<String> adaptercity;
    ArrayList<String> list_dates=new ArrayList<>();
    private RecyclerView rv;
    private ItemfavitemAdapter adapter;
    private String tid, team;
    private int approval_status;
    private RelativeLayout ll_select;
    private Spinner selectDistributor;
    private ArrayList<SelectedCities> organisations;
    private String emp_visit_id, sql_id, event_approval_status;

    private String strDate,currentdate;
    private boolean isNoUsers = false;
    private boolean isDirectCustomers = false;
    private int versionCode = 0;
    FloatingActionButton fab;
    TextView tv_noresults;

    private ArrayList<SpeedDialActionItem> items;
    Calendar calendar;
    CalendarView calendarView;
    MaterialCalendarView materialCalendarView;


    List<CalendarDay> list = new ArrayList<CalendarDay>();
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indent);



        Log.d("plans", "no plans11s");
        init();

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),datefromcalander,Toast.LENGTH_SHORT).show();

                if(selectDistributor.getSelectedItemPosition()!=0) {
                    Intent it = new Intent(ActivityIndent.this, ActivityIndentForm.class);
                    it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    it.putExtra("dateselected", datefromcalander);
                    it.putExtra("idselected", seluserId);
                    Log.d("hi",seluserId+"  "+userId);
                    startActivity(it);
                }



            }
        });



        materialCalendarView.setOnDateChangedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(@NonNull MaterialCalendarView materialCalendarView, @NonNull CalendarDay calendarDay, boolean b) {


                int i1 = calendarDay.getMonth() + 1;
                int i2 = calendarDay.getDay();
                int i = calendarDay.getYear();
                datefromcalander = CalendarDay.today().getYear() + "-" + (i1 < 10 ? ("0" + i1) : (i1)) + "-" + (i2 < 10 ? ("0" + i2) : (i2));


                if(selectDistributor.getSelectedItemPosition()!=0)
                {

                   // new Async_getalldates().execute();
                    activityinbackgroundthread();

                }
                else
                {
                    // list_dates.clear();
                   // new Async_getalldates().execute();
                    activityinbackgroundthread();
                    Toast.makeText(getApplicationContext(),"Please select user",Toast.LENGTH_LONG).show();
                }


            }
        });

        selectDistributor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                seluserId = organisations.get(position).getCityId();
                if(position>0)
                {
                    GetalldayesAndCurrentDateActivity();
                    // new ActivityIndent.Async_getalltfalistactivities();
                }
                else
                {

                    GetalldayesAndCurrentDateActivity();
                }



            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });





        ll_select.setVisibility(View.GONE);
        int i1 = CalendarDay.today().getMonth() + 1;
        int i2 = CalendarDay.today().getDay();
        int i =  CalendarDay.today().getYear();
        datefromcalander = CalendarDay.today().getYear() + "-" + (i1 < 10 ? ("0" + i1) : (i1)) + "-" + (i2 < 10 ? ("0" + i2) : (i2));



        if (role == Constants.Roles.ROLE_17||(role == Constants.Roles.ROLE_7))
        {
            if(role == Constants.Roles.ROLE_7)
            {
                ll_select.setVisibility(View.VISIBLE);
            }
            else
            {
                seluserId = userId;
                GetalldayesAndCurrentDateActivity();


            }

            fab.setVisibility(View.VISIBLE);
        }
        else
        {
            ll_select.setVisibility(View.VISIBLE);
            fab.setVisibility(View.VISIBLE);
        }
        //Toast.makeText(getApplicationContext(),String.valueOf(datefromcalander),Toast.LENGTH_LONG).show();
        if (role != Constants.Roles.ROLE_17) {
            //ll_select.setVisibility(View.VISIBLE);
            callapi(1);

        }

    }



    private void init() {


        db = new DatabaseHandler(this);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        userId = sharedpreferences.getString("userId", "");
        // selid = sharedpreferences.getString("userId", "");
        team = sharedpreferences.getString("team", "");
        role = sharedpreferences.getInt(Constants.SharedPrefrancesKey.ROLE, 0);

        ll_select = (RelativeLayout) findViewById(R.id.ll_select);
        selectDistributor = (Spinner) findViewById(R.id.spin_user);
        fab = (FloatingActionButton) findViewById(R.id.flaot);
        tv_noresults=(TextView)findViewById(R.id.tv_noresults);
        rv = (RecyclerView) findViewById(R.id.rv);

        materialCalendarView = (MaterialCalendarView) findViewById(R.id.calendarView);
        materialCalendarView.setSelectedDate(CalendarDay.today());

        LinearLayoutManager llm = new LinearLayoutManager(ActivityIndent.this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(llm);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        new Async_gettfalist().execute();

    }


    @Override
    protected void onInternetConnected() {

    }

    @Override
    protected void onInternetDisconnected() {

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
            holder.tv_activity.setText("Type       : "+(favouriteItem.get(position).get("tfa_title")));
            holder.tv_place.setText("Place        : "+(favouriteItem.get(position).get("village")+" ,"+
                    favouriteItem.get(position).get("taluka")+" ,"+favouriteItem.get(position).get("district_name")));
            holder.tv_crop.setText("Product   : "+(favouriteItem.get(position).get("brand_name")+" ("+
                    favouriteItem.get(position).get("crop_name")+" - "+favouriteItem.get(position).get("division_name")+")"));
            holder.tv_formers1.setText("Count       : "+(favouriteItem.get(position).get("no_of_farmers")+" farmers ("
                    +favouriteItem.get(position).get("total_expences")+" Rs)"));
            holder.tv_adv.setText("Advance  : "+(favouriteItem.get(position).get("advance_required")+" Rs"));


            if(Integer.valueOf(results.get(position).get("approval_status"))<=3){
                holder.tv_accept.setVisibility(View.GONE);
                holder.tv_reject.setVisibility(View.GONE);
                holder.tv_approvalstatus.setText("Pending");
                holder.tv_approvalstatus.setBackgroundResource(R.drawable.background_yellow);
                holder.tv_approvalstatus.setTextColor(getResources().getColor(R.color.black));
            }
            else if (Integer.valueOf(results.get(position).get("approval_status"))==4)
            {
                holder.tv_approvalstatus.setText("Approved");
                holder.tv_accept.setVisibility(View.GONE);
                holder.tv_reject.setVisibility(View.GONE);
                holder.tv_approvalstatus.setTextColor(getResources().getColor(R.color.white));
                holder.tv_approvalstatus.setBackgroundResource(R.drawable.background_green);
            } else if (Integer.valueOf(results.get(position).get("approval_status"))==9)
            {
                holder.tv_reject.setVisibility(View.GONE);
                holder.tv_accept.setVisibility(View.GONE);
                holder.tv_approvalstatus.setText("Rejected");
                holder.tv_approvalstatus.setTextColor(getResources().getColor(R.color.white));
                holder.tv_approvalstatus.setBackgroundResource(R.drawable.background_orange);
            }
            holder.ll_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    ArrayList<HashMap<String, String>> favouriteItem2 = new ArrayList<HashMap<String, String>>();
                    favouriteItem2.add(favouriteItem.get(position));


                    Intent intent = new Intent(ActivityIndent.this, DetailledActivtylist.class);
                    intent.putExtra("favouriteItem", favouriteItem2);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(intent);

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
                    final android.app.AlertDialog popup = new AlertDialog.Builder(ActivityIndent.this).create();
                    final View dialogView = getLayoutInflater().inflate(R.layout.approval_layout, null);
                    popup.setView(dialogView);
                    popup.show();
                    List<Approvaldetails> orderApprovals = db.getOrderApprovalsByTfaListId2(favouriteItem.get(position).get("tfa_list_id"));


                    ImageView closeView = dialogView.findViewById(R.id.close_icon);
                    ListView approvalsRecycler = dialogView.findViewById(R.id.approvals_recycler);
                    TextView alertView = dialogView.findViewById(R.id.alert_view);
                    if (orderApprovals != null && orderApprovals.size() > 0)
                    {
                        approvalsRecycler.setVisibility(View.VISIBLE);
                        alertView.setVisibility(View.GONE);
                        com.nsl.beejtantra.TFA.ApprovalsAdapter approvalsAdapter = new ApprovalsAdapter(ActivityIndent.this, orderApprovals);
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













    @Override
    protected void onStart() {
        super.onStart();


    }


    @Override
    protected void onResume() {
        super.onResume();

    }

    @Override
    protected void onPause() {
        super.onPause();

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
        organisations.add(citiez);
        adaptercity.add("Select User");
        try {
            String selectQuery;
            selectQuery = "SELECT " + KEY_TABLE_USERS_FIRST_NAME + "," + KEY_TABLE_USERS_MASTER_ID + " FROM " + TABLE_USERS + "  WHERE " + db.KEY_TABLE_USERS_STATUS + " = 1 and  user_id in (" + team + ")  ";
            sdbw = db.getWritableDatabase();
            cursor = sdbw.rawQuery(selectQuery, null);
            System.out.println("cursor count " + "\n selectQuery" + selectQuery);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    SelectedCities cities2 = new SelectedCities();

                    if (String.valueOf(cursor.getString(1)).equalsIgnoreCase(Common.getUserIdFromSP(this))) {
                       // adaptercity.add("SELF");
                    }
                    else
                    {
                        adaptercity.add(cursor.getString(0));
                        cities2.setCityId(cursor.getString(1));
                        cities2.setCityName(cursor.getString(0));
                        organisations.add(cities2);
                    }

                } while (cursor.moveToNext());
            } else {
                isNoUsers = true;
            }

            // do some stuff....
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Common.closeCursor(cursor);
            Common.closeDataBase(sdbw);
        }

        selectDistributor.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, R.id.customSpinnerItemTextView, adaptercity));

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


                /*String selectQuery = "select tfa.tfa_list_id," +
                        "            d.district_name," +
                        "                        div.division_name," +
                        "                        pd.brand_name," +
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
                        "                        aprv.approved_user_id," +
                        "                        cp.crop_name" +
                        "                from tfa_activity_list tfa" +
                        "                INNER JOIN districts d on tfa.district_id = d.district_id" +
                        "                INNER JOIN division div on tfa.division_id = div.division_id" +
                        "                INNER JOIN products pd  on tfa.product_id = pd.product_id" +
                        "                INNER JOIN crops cp  on tfa.crop_id = cp.crop_id" +
                        "                INNER JOIN tfa_activity_master tfaa on tfa.tfa_activity_master_id = tfaa.tfa_master_id" +
                        "                INNER JOIN tfa_approval_history aprv  on tfa.tfa_list_id = aprv.tfa_list_id"+" where" +
                        "                (tfa.approval_status<='4' or  tfa.approval_status='9') and " +
                        "                activity_date='"+datefromcalander+"' and user_id='"+seluserId+"'" +
                        "                order by aprv.tfa_approval_id DESC limit 1";*/


                String selectQuery =" select tfa.tfa_list_id,"
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
                        +"(SELECT crop_name FROM crops WHERE crops.crop_id=tfa.crop_id) AS crop_name"
                        +" from tfa_activity_list tfa where       (tfa.approval_status<='4' or  tfa.approval_status='9') and "
                        +"                activity_date='"+datefromcalander+"' and user_id='"+seluserId+"'"
                        +"                order by tfa_approval_id DESC limit 1";

                sdbw = db.getWritableDatabase();



                /* */

                Log.d("plans", selectQuery);
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
                        map.put("no_of_farmers",cursor.getString(8));
                        map.put("estimation_per_head",cursor.getString(9));
                        map.put("total_expences",cursor.getString(10));
                        map.put("advance_required",cursor.getString(11));
                        map.put("conducting_place",cursor.getString(12));
                        map.put("user_id",cursor.getString(13));
                        map.put("created_user_id",cursor.getString(14));
                        map.put("approval_status",cursor.getString(15));
                        map.put("crop_name",cursor.getString(19));
                        favouriteItem.clear();
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
                tv_noresults.setVisibility(View.VISIBLE);
                adapter = new ItemfavitemAdapter(getApplicationContext(), favouriteItem);
                rv.setAdapter(adapter);
            }
            else
            {

                tv_noresults.setVisibility(View.GONE);
                adapter = new ItemfavitemAdapter(getApplicationContext(), favouriteItem);
                adapter.notifyDataSetChanged();
                rv.setAdapter(adapter);
            }
            //adapter.updateResults(favouriteItem);
        }
    }

    private class Async_getalldates extends AsyncTask<Void, Void, String> {


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            list_dates.clear();
            //  materialCalendarView.clearSelection();
            materialCalendarView.removeDecorators();
        }

        protected String doInBackground(Void... params) {

            try {
                String selectQuery = "SELECT DISTINCT"+ " activity_date " +"FROM " +  TABLE_TFA_ACTIVITYLIST + "  where "+  "(approval_status<='4' or  approval_status<='9') and " + KEY_USER_ID + " = " + seluserId+"  "+" order by  activity_date asc";

                sdbw = db.getWritableDatabase();
                Log.e("no plans", selectQuery);
             /*   materialCalendarView.clearSelection();
                materialCalendarView.removeDecorators();*/
                list_dates.clear();
                Cursor cursor = sdbw.rawQuery(selectQuery, null);
                if (cursor.moveToFirst()) {
                    do {
                        list_dates.add(cursor.getString(0));

                    } while (cursor.moveToNext());
                } else {
                    Log.e("no plans", "no plans");
                    //  materialCalendarView.clearSelection();

                    new Async_getalltfalistactivities().execute();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
          /*  Thread t= new Thread(() ->
            {*/
            for(int i22=0;i22<list_dates.size();i22++)
            {
                Log.d("list_dates",list_dates.get(0).toString());
                SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());

                Date date33 = null;
                try
                {
                    date33 = simpleDateFormat.parse(list_dates.get(i22));

                    CalendarDay day = CalendarDay.from(date33);



                            list.add(day);
                            Collection<CalendarDay> c = list;
                            EventDecorator ed = new EventDecorator(R.color.colorPrimary, c);
                            materialCalendarView.removeDecorator(ed);
                            materialCalendarView.addDecorator(ed);


                   // System.out.println(t.isAlive());

                } catch (ParseException e)
                {
                    e.printStackTrace();
                }
                // new ActivityIndent.Async_getalloffline().execute();

            }
       /* });
                    t.start();*/
           new Async_getalltfalistactivities().execute();

        }


    }
    public class EventDecorator implements DayViewDecorator {

        private final int color;
        private final HashSet<CalendarDay> dates;

        public EventDecorator(int color, Collection<CalendarDay> dates) {
            this.color = color;
            this.dates = new HashSet<>(dates);
        }

        @Override
        public boolean shouldDecorate(CalendarDay day) {
            return dates.contains(day);
        }

        @Override
        public void decorate(DayViewFacade view) {
            view.addSpan(new DotSpan(5, color));
        }
    }
    private class Async_gettfalist extends AsyncTask<Void, Void, String> {
        ProgressDialog progressDialog;
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            favouriteItem.clear();
            progressDialog = new ProgressDialog(ActivityIndent.this);
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
                                    object.getString("updated_datetime")

                            );
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
    private void activityinbackgroundthread()
    {
        t2=new Thread(()->
        {
            try {
                t1.join();
            } catch (InterruptedException e) {   //join will throw interrupted exception
                e.printStackTrace();
            }
            new Async_getalltfalistactivities().execute();
        });
        //main thread
        t2.start();
        try {
            t2.join();
        } catch (InterruptedException e) {   //join will throw interrupted exception
            e.printStackTrace();
        }
    }
    private void GetalldayesAndCurrentDateActivity() {
        t1=new Thread(()->
        {
            try {
                String selectQuery = "SELECT DISTINCT"+ " activity_date " +"FROM " +  TABLE_TFA_ACTIVITYLIST + "  where "+  "(approval_status<='4' or  approval_status<='9') and " + KEY_USER_ID + " = " + seluserId+"  "+" order by  activity_date asc";

                sdbw = db.getWritableDatabase();
                Log.e("no plans", selectQuery);
                list_dates.clear();
               // materialCalendarView.removeDecorators();
                Cursor cursor = sdbw.rawQuery(selectQuery, null);
                if (cursor.moveToFirst())
                {
                    do {
                        list_dates.add(cursor.getString(0));

                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        });
        t2=new Thread(()->
        {
            try {
                t1.join();
            } catch (InterruptedException e) {   //join will throw interrupted exception
                e.printStackTrace();
            }
            new Async_getalltfalistactivities().execute();
        });
        //main thread
        materialCalendarView.removeDecorators();
        t1.start();
        t2.start();
        try {
            t2.join();
        } catch (InterruptedException e) {   //join will throw interrupted exception
            e.printStackTrace();
        }
        //main thread
        for(int i22=0;i22<list_dates.size();i22++)
        {
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd", Locale.getDefault());
            Date date33 = null;
            try
            {
                date33 = simpleDateFormat.parse(list_dates.get(i22));
                CalendarDay day = CalendarDay.from(date33);
                list.add(day);
                Collection<CalendarDay> c = list;
                EventDecorator ed = new EventDecorator(R.color.colorPrimary, c);
                materialCalendarView.removeDecorator(ed);
                materialCalendarView.addDecorator(ed);


            } catch (ParseException e)
            {
                e.printStackTrace();
            }
        }

    }

}
