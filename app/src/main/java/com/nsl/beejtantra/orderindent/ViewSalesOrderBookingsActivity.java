package com.nsl.beejtantra.orderindent;


import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nsl.beejtantra.Constants;
import com.nsl.beejtantra.DatabaseHandler;
import com.nsl.beejtantra.MainActivity;
import com.nsl.beejtantra.R;
import com.nsl.beejtantra.ServiceOrderDetailMaster;
import com.nsl.beejtantra.ServiceOrderMaster;
import com.nsl.beejtantra.advancebooking.ViewBookings2Activity;
import com.nsl.beejtantra.commonutils.Common;
import com.nsl.beejtantra.pojo.ServiceOrderApproval;
import com.nsl.beejtantra.pojo.ServiceOrderHistory;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.nsl.beejtantra.DatabaseHandler.KEY_SOA_ORDER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_SOA_PENDING_BY;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_COMPANIES_COMPANY_CODE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_COMPANIES_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CUSTOMER_CODE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CUSTOMER_COMPANY_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CUSTOMER_DISTRICT;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CUSTOMER_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CUSTOMER_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_DIVISION_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_DIVISION_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_SERVICEORDER_APPROVAL_STATUS;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_SERVICEORDER_COMPANY_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_SERVICEORDER_DETAIL_ORDERPRICE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_SERVICEORDER_DETAIL_ORDER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_SERVICEORDER_FFM_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_SERVICEORDER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_SERVICEORDER_ORDERDATE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_SERVICEORDER_SAP_ID;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_COMPANIES;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_EMPLOYEE_VISIT_MANAGEMENT;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_SERVICEORDER;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_SERVICE_ORDER_APPROVAL;
import static com.nsl.beejtantra.orderindent.FragmentOrderIndent.REQUEST_TYPE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewSalesOrderBookingsActivity extends AppCompatActivity {
    // JSON parser class
    private JSONObject JSONObj;
    private JSONArray JSONArr = null;
    ProgressDialog progressDialog;
    private RecyclerView listView;
    private Button loadMore;
    private ItemfavitemAdapter adapter;
    DatabaseHandler db;
    int osa;
    SQLiteDatabase sdbw, sdbr;
    String jsonData;
    String userId;
    String company_id;
    int role;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    public ArrayList<HashMap<String, String>> favouriteItemFinal = new ArrayList<>();
    public ArrayList<HashMap<String, String>> tempList = new ArrayList<>();

    int requestType;
    private StringBuilder sb = new StringBuilder();
    private String team;

    SharedPreferences sharedpreferences;
    ProgressDialog loadDialog;
    public static final String mypreference = "mypref";
    private TextView tv_list_all;
    private TextView toolbarTitle;
    private int itemHeight = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_salesorderbookings);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        userId = sharedpreferences.getString("userId", "");
        team = sharedpreferences.getString("team", "");
        role = sharedpreferences.getInt(Constants.SharedPrefrancesKey.ROLE, 0);
        db = new DatabaseHandler(this);
        loadDialog = new ProgressDialog(this);
        loadDialog.setMessage("Please Wait...");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
               /* Intent orderindent = new Intent(getApplicationContext(),OrderIndentMainActivity.class);
                startActivity(orderindent);*/
                finish();
            }
        });
        tv_list_all = (TextView) findViewById(R.id.tv_list_all);
        toolbarTitle = (TextView) toolbar.findViewById(R.id.toolbar_title);
        loadMore = findViewById(R.id.load_more);

        toolbarTitle.setText("Order Indent");
        tv_list_all.setText("List of all Order Indent");
        listView = (RecyclerView) findViewById(R.id.listView);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
//                // Toast.makeText(getApplicationContext(), favouriteItem.get(i).get("strcustomers"), Toast.LENGTH_SHORT).show();
//
//            }
//        });

        requestType = getIntent().getIntExtra(REQUEST_TYPE, 0);
        company_id = getIntent().getStringExtra("company_id");
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(llm);

        // new Async_getall().execute();

        loadMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loadDialog.setCancelable(false);
                loadDialog.show();
                int prevCount = listView.getAdapter().getItemCount();
                int limitCount = listView.getAdapter().getItemCount() + 10;
                if (limitCount < favouriteItemFinal.size()) {
                    for (int i = prevCount; i < limitCount; i++) {
                        tempList.add(favouriteItemFinal.get(i));
                    }
                    adapter.updateResults(tempList);
                } else {
                    int remainingCount = favouriteItemFinal.size() - listView.getAdapter().getItemCount();
                    for (int i = prevCount; i < prevCount + remainingCount; i++) {
                        tempList.add(favouriteItemFinal.get(i));
                    }
                    adapter.updateResults(tempList);
                    loadMore.setVisibility(View.GONE);
                }
            }
        });
    }


    @Override
    protected void onResume() {
        super.onResume();

        progressDialog = new ProgressDialog(ViewSalesOrderBookingsActivity.this);
        progressDialog.setMessage("Please Wait ...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        if (Common.haveInternet(this)) {

            new Async_getNewOrUpdateserviceorders().execute();
        } else {
            new Async_getallcustomersoffline().execute();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            Intent home = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(home);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }


    private class Async_getallcustomersoffline extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
//            progressDialog = new ProgressDialog(ViewSalesOrderBookingsActivity.this);
//            progressDialog.setMessage("Please wait");
//            progressDialog.show();
        }

        protected String doInBackground(Void... params) {
            favouriteItem.clear();

            // String selectQuery  = "SELECT  " + KEY_TABLE_CROPS_CROP_MASTER_ID + ","+ KEY_TABLE_CROPS_CROP_NAME +  " FROM " + TABLE_COMPANY_DIVISION_CROPS + " AS CDC JOIN " + TABLE_CROPS + " AS CR ON CDC."+KEY_TABLE_COMPANY_DIVISION_CROPS_CROP_ID + " = CR."+ KEY_TABLE_CROPS_CROP_MASTER_ID + "  where " + KEY_TABLE_COMPANY_DIVISION_CROPS_COMPANY_ID + " = " + company_id + " and " + KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID + " = " + sel_division_id;
            //working query    // String selectQuery    = "SELECT  " + KEY_TABLE_CUSTOMER_NAME + ","+ KEY_TABLE_CUSTOMER_CODE+            " FROM " + TABLE_SERVICEORDER + " AS SO JOIN " + TABLE_CUSTOMERS + " AS C ON C."+KEY_TABLE_CUSTOMER_MASTER_ID + " = SO."+ KEY_TABLE_SERVICEORDER_CUSTOMER_ID +"  where SO." + KEY_TABLE_SERVICEORDER_USER_ID + " = " + userId  ;
            /*String selectQuery  = "SELECT DISTINCT " + KEY_TABLE_CUSTOMER_NAME + ","+ KEY_TABLE_CUSTOMER_CODE+","+ KEY_TABLE_CUSTOMER_DETAILS_INSIDE_BUCKET+","+ KEY_TABLE_CUSTOMER_DETAILS_OUTSIDE_BUCKET+ ",C."+ KEY_TABLE_CUSTOMER_MASTER_ID + ",SO."+ KEY_TABLE_SERVICEORDER_TOKEN_AMOUNT + ",CM."+ KEY_TABLE_COMPANIES_COMPANY_CODE + ",SD.advance_amount, SUM(SD.advance_amount) AS sumprice,division_name,dv.division_id FROM " + TABLE_SERVICEORDER
                    + " AS SO  JOIN " + TABLE_CUSTOMERS + " AS C ON C."+KEY_TABLE_CUSTOMER_MASTER_ID + " = SO."+ KEY_TABLE_SERVICEORDER_CUSTOMER_ID
                    + " LEFT JOIN "+TABLE_SERVICEORDERDETAILS + " AS SD ON SD."+ KEY_TABLE_SERVICEORDER_DETAIL_ORDER_ID + " = SO."+ KEY_TABLE_SERVICEORDER_ID
                    + "  LEFT JOIN "+TABLE_COMPANIES + " AS CM ON CM."+ KEY_TABLE_COMPANIES_MASTER_ID + " = SO."+ KEY_TABLE_SERVICEORDER_COMPANY_ID
                    + "  LEFT JOIN "+ TABLE_CUSTOMER_DETAILS + " AS CD ON CD."+ KEY_TABLE_CUSTOMER_DETAILS_MASTER_ID + " = C."+ KEY_TABLE_CUSTOMER_MASTER_ID
                    +"   LEFT JOIN division AS dv ON dv.division_id = SO.division_id"
                    +"  where SO." + KEY_TABLE_SERVICEORDER_USER_ID + " = " + userId +" AND SO."+KEY_TABLE_SERVICEORDER_ORDERTYPE+"="+requestType;
                   // +" GROUP BY SO.customer_id,SO.division_id";*/


            /*if("skjd"){
                String conQury=" and SO.user_id =" + userId;
            }
            if("sdfsdf") {
                String conQury=" and SO.user_id in (" +userId+")";
            }*/
            String selectQuery = "SELECT " +
                    KEY_TABLE_CUSTOMER_NAME + ", " + KEY_TABLE_CUSTOMER_CODE + "," +
                    " C.customer_id, C." + KEY_TABLE_CUSTOMER_DISTRICT + "," +
                    " SUM(SO.advance_amount), SUM(SOD.order_price) AS sumprice,dv.division_id,division_name,CM." + KEY_TABLE_COMPANIES_COMPANY_CODE + ",SO.approval_status" + ", SO.service_id , SO.ffmid , SO." + KEY_TABLE_SERVICEORDER_SAP_ID + ",SO." + KEY_TABLE_SERVICEORDER_ORDERDATE + ",SOA." + KEY_SOA_PENDING_BY +
                    " FROM service_order_details SOD" +
                    " LEFT JOIN service_order SO ON SO." + KEY_TABLE_SERVICEORDER_ID + " = SOD." + KEY_TABLE_SERVICEORDER_DETAIL_ORDER_ID +
                    " LEFT JOIN " + TABLE_SERVICE_ORDER_APPROVAL + " SOA ON SOA." + KEY_SOA_ORDER_ID + " = SO." + KEY_TABLE_SERVICEORDER_FFM_ID +
                    " LEFT JOIN customers AS C ON C.customer_id = SO.customer_id" +
                    " LEFT JOIN division AS dv ON dv.division_id = SO.division_id" +
                    " LEFT JOIN " + TABLE_COMPANIES + " AS CM ON CM." + KEY_TABLE_COMPANIES_MASTER_ID + " = SO." + KEY_TABLE_SERVICEORDER_COMPANY_ID +
                    " WHERE SO.user_id =" + userId +
                    " AND SO.order_type =1 AND CM." + KEY_TABLE_CUSTOMER_COMPANY_ID + " = " + company_id +
                    " GROUP BY SO.service_id  ORDER BY SO.service_id DESC";
            // " GROUP BY SO.customer_id,SO.division_id";

            Log.e("selectQuery", selectQuery);
            sdbw = db.getWritableDatabase();
            Cursor cursor = sdbw.rawQuery(selectQuery, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    /*if(cursor.getString(2)!= null && cursor.getString(3)!= null) {
                        int insidebucket = Integer.parseInt(cursor.getString(2));
                        int outsidebucket = Integer.parseInt(cursor.getString(3));
                        osa = insidebucket + outsidebucket;
                        Log.e("Values", cursor.getString(2) + ":" + cursor.getString(3) + ":OSA" + String.valueOf(osa) + ":customer id" + cursor.getString(4));
                    }*/
                    HashMap<String, String> map = new HashMap<String, String>();
                    // adding each child node to HashMap key => value
                    map.put("customer_name", cursor.getString(cursor.getColumnIndex(KEY_TABLE_CUSTOMER_NAME)));
                    map.put("customer_code", cursor.getString(cursor.getColumnIndex(KEY_TABLE_CUSTOMER_CODE)));
                    map.put("customer_id", cursor.getString(cursor.getColumnIndex(KEY_TABLE_CUSTOMER_MASTER_ID)));
                    map.put("Division", cursor.getString(cursor.getColumnIndex(KEY_TABLE_DIVISION_NAME)));
                    map.put("DivisionId", cursor.getString(cursor.getColumnIndex(KEY_TABLE_DIVISION_MASTER_ID)));
                    map.put("ABS", "");
                    map.put("Token_Amount", db.getSumOfOrderInAmount(userId, cursor.getString(cursor.getColumnIndex(KEY_TABLE_CUSTOMER_MASTER_ID)), cursor.getString(cursor.getColumnIndex(KEY_TABLE_DIVISION_MASTER_ID)), cursor.getString(cursor.getColumnIndex(KEY_TABLE_SERVICEORDER_ID))));
                    map.put("Crop name", "");
                    map.put("Product Name", "");
                    map.put("Order_price", cursor.getString(cursor.getColumnIndex("sumprice")));
                    map.put("Quantity", "");
                    map.put("Order Date", cursor.getString(cursor.getColumnIndex(KEY_TABLE_SERVICEORDER_ORDERDATE)));
                    map.put("company_name", cursor.getString(cursor.getColumnIndex(KEY_TABLE_DIVISION_NAME)));
                    map.put("approval_status", cursor.getString(cursor.getColumnIndex(KEY_TABLE_SERVICEORDER_APPROVAL_STATUS)));
                    map.put("service_id", cursor.getString(cursor.getColumnIndex(KEY_TABLE_SERVICEORDER_ID)));
                    map.put("ffm_id", cursor.getString(cursor.getColumnIndex(KEY_TABLE_SERVICEORDER_FFM_ID)));
                    map.put("order_id", cursor.getString(cursor.getColumnIndex(KEY_TABLE_SERVICEORDER_FFM_ID)));
                    map.put("sap_id", cursor.getString(cursor.getColumnIndex(KEY_TABLE_SERVICEORDER_SAP_ID)));
                    map.put("district", cursor.getString(cursor.getColumnIndex(KEY_TABLE_CUSTOMER_DISTRICT)));
                    map.put("pending_by", cursor.getString(cursor.getColumnIndex(KEY_SOA_PENDING_BY)));
                    favouriteItem.add(map);
                    String orders = String.valueOf(favouriteItem.size());
                } while (cursor.moveToNext());
            } else Log.d("LOG", "returned null!");

            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }

            if (favouriteItem.size() == 0) {
                tempList.clear();
//                listView.setEmptyView(findViewById(R.id.tv_alert));
                listView.setVisibility(View.GONE);
                findViewById(R.id.tv_alert).setVisibility(View.VISIBLE);
            } else {
                runOnUiThread(new Runnable() {

                    @Override
                    public void run() {
                        //   updateResults(favouriteItem);
                        listView.setVisibility(View.VISIBLE);
                        findViewById(R.id.tv_alert).setVisibility(View.GONE);
                        tempList.clear();
                        favouriteItemFinal.clear();
                        favouriteItemFinal.addAll(favouriteItem);
//                        for (int i = 0; i < 100; i++)
//                            favouriteItemFinal.addAll(favouriteItem);
                        int count = favouriteItemFinal.size();
                        if (favouriteItemFinal.size() > 10)
                            count = 10;
                        for (int i = 0; i < count; i++)
                            tempList.add(favouriteItemFinal.get(i));
                        adapter = new ItemfavitemAdapter(getApplicationContext(), tempList);
                        listView.setAdapter(adapter);
                        if (favouriteItemFinal.size() > 10)
                            loadMore.setVisibility(View.VISIBLE);
                        else
                            loadMore.setVisibility(View.GONE);
                    }
                });
            }
        }
    }

    class ItemfavitemAdapter extends RecyclerView.Adapter<ItemfavitemAdapter.ViewHolder> {

        Context context;
        ArrayList<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();


        public ItemfavitemAdapter(Context context, ArrayList<HashMap<String, String>> results) {
            this.context = context;
            this.results = results;

        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            ViewHolder viewHolder = new ViewHolder(getLayoutInflater().inflate(R.layout.row_viewbooking1, null));
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            if (role == Constants.Roles.ROLE_7 || role == Constants.Roles.ROLE_12 || role == Constants.Roles.ROLE_6) {
                holder.tv_accept.setVisibility(View.GONE);
                holder.tv_reject.setVisibility(View.GONE);
            } else {
                holder.tv_accept.setVisibility(View.GONE);
                holder.tv_reject.setVisibility(View.GONE);
            }
            //Glide.with(context).load(results.get(position).get("image")).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.pic);
            holder.distributor.setText(results.get(position).get("customer_name"));
            holder.division.setText(results.get(position).get("Division"));
            holder.orderNumber.setText(results.get(position).get("sap_id"));
            holder.orderDate.setText(results.get(position).get("Order Date").split(" ")[0]);
            holder.district.setText(results.get(position).get("district"));
//            holder.tv_bookeamount.setText("BA :" + Math.round(Float.parseFloat(results.get(position).get("Token_Amount"))));
            holder.ba.setText(String.valueOf(Math.round(Float.parseFloat(results.get(position).get("Token_Amount")))));


            if (results.get(position).get("approval_status").equalsIgnoreCase("1")) {
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
            }

            holder.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent customers = new Intent(getApplicationContext(), ViewSalesOrderCustomerDetailsActivity.class);
                    customers.putExtra("customer_name", results.get(position).get("customer_name"));
                    customers.putExtra("customer_code", results.get(position).get("customer_code"));
                    customers.putExtra("ABS", results.get(position).get("ABS"));
                    customers.putExtra("customer_id", results.get(position).get("customer_id"));
                    customers.putExtra("divison_id", results.get(position).get("DivisionId"));
                    customers.putExtra("TYPE", "mo");
                    customers.putExtra("service_id", results.get(position).get("service_id"));
                    customers.putExtra("order_id", results.get(position).get("ffm_id"));
                    customers.putExtra("approval_status", results.get(position).get("approval_status"));
                    customers.putExtra(REQUEST_TYPE, requestType);
                    startActivity(customers);
                }
            });
            final ViewHolder finalHolder = holder;
            holder.approvalInfo.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
//                    final PopupWindow popup = new PopupWindow(ViewSalesOrderBookingsActivity.this);
                    final AlertDialog popup = new AlertDialog.Builder(ViewSalesOrderBookingsActivity.this).create();
                    View dialogView = getLayoutInflater().inflate(R.layout.approval_layout, null);
//                    int displayWidth = getWindowManager().getDefaultDisplay().getWidth() - (getWindowManager().getDefaultDisplay().getWidth() / 6);
//                    int displayHeight = getWindowManager().getDefaultDisplay().getHeight();
//                    RelativeLayout.LayoutParams layoutParams = new RelativeLayout.LayoutParams(displayWidth, displayHeight);
//                    dialogView.setLayoutParams(layoutParams);
                    popup.setView(dialogView);
                    popup.show();
//                    popup.showAtLocation(finalHolder.approvalInfo, Gravity.CENTER, 0, 0);
                    List<ServiceOrderApproval> orderApprovals = db.getOrderApprovalsByOrderId(results.get(position).get("order_id"));

                    ImageView closeView = dialogView.findViewById(R.id.close_icon);
                    ListView approvalsRecycler = dialogView.findViewById(R.id.approvals_recycler);
                    TextView alertView = dialogView.findViewById(R.id.alert_view);
                    if (orderApprovals != null && orderApprovals.size() > 0) {
                        approvalsRecycler.setVisibility(View.VISIBLE);
                        alertView.setVisibility(View.GONE);
                        ApprovalsAdapter approvalsAdapter = new ApprovalsAdapter(ViewSalesOrderBookingsActivity.this, orderApprovals);
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

            @BindView(R.id.item_layout)
            RelativeLayout itemLayout;
            @BindView(R.id.distributor)
            TextView distributor;
            @BindView(R.id.district)
            TextView district;
            @BindView(R.id.order_date)
            TextView orderDate;
            @BindView(R.id.order_number)
            TextView orderNumber;
            @BindView(R.id.ba)
            TextView ba;
            @BindView(R.id.division)
            TextView division;
            @BindView(R.id.history_info)
            TextView approvalInfo;
            @BindView(R.id.tv_approvalstatus)
            TextView tv_approvalstatus;
            @BindView(R.id.tv_accept)
            TextView tv_accept;
            @BindView(R.id.tv_reject)
            ImageView tv_reject;
            @BindView(R.id.ll_accept_reject)
            LinearLayout ll_accept_reject;


//            public TextView itemname, company_code, tv_rbs_amount, tv_bookeamount, tv_approvalstatus, tv_accept, approvalInfo, distributor, ba, division, orderNumber, orderDate, district;
//            public ImageView tv_reject;
//            public Button btn_view;
//            public LinearLayout ll_accept_reject;


            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

        public void updateResults(ArrayList<HashMap<String, String>> results) {

           /* this.results = results;
            notifyDataSetChanged();*/
            if (results.size() == favouriteItemFinal.size())
                loadMore.setVisibility(View.GONE);
            adapter = new ItemfavitemAdapter(getApplicationContext(), results);
            listView.setAdapter(adapter);
            if (loadDialog != null && loadDialog.isShowing())
                loadDialog.dismiss();
        }

    }


    public class Async_getNewOrUpdateserviceorders extends AsyncTask<Void, Void, String> {

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
                if (latestRecordDate != null)
                    days = String.valueOf(Common.getNoOfdays(latestRecordDate));

                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                Request request = new Request.Builder()
                        .url(Common.getCompleteURLEVM(Constants.NEW_OR_UPDATED_RECORDS_SERVICE_ORDER, userId, team, company_id)+"&days="+days)
                        .get()
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();
                Common.Log.i("SERVICE ORDER URL " + Common.getCompleteURLEVM(Constants.NEW_OR_UPDATED_RECORDS_SERVICE_ORDER, userId, team, company_id)+"&days="+days);


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
                        String created_datetime = service_order.getString("created_datetime");
                        String updated_datetime = service_order.getString("updated_datetime");
                        String approval_comments = service_order.getString("approval_comments");
                        if (n > 0) {
                            sb.append(",");
                        }
                        sb.append(ffm_id);


                        String selectQuery = "SELECT * FROM " + TABLE_SERVICEORDER + " WHERE " + KEY_TABLE_SERVICEORDER_FFM_ID + " = '" + ffm_id + "'";


                        sdbw = db.getWritableDatabase();
                        Cursor cc = sdbw.rawQuery(selectQuery, null);
                        cc.getCount();
                        // looping through all rows and adding to list
//                        if (cc.getCount() == 0) {
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
//                        } else {

                        db.updateApprovalOrRejectStatus(user_id, approval_status, approval_comments, ffm_id, false);
//                        }


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
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //  Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                    }
                });

            }
            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
           /* if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }*/
            if (sb.toString() != null && sb.toString().length() > 0) {
                new Async_UpdateAprovalStatus().execute();
            } else {
                new Async_getallcustomersoffline().execute();
                Common.dismissProgressDialog(progressDialog);
            }

        }
    }


    private class Async_UpdateAprovalStatus extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* progressDialog = new ProgressDialog(PlanerOneActivity.this);
            progressDialog.setMessage("Updating Aproval Status");
            progressDialog.show();*/
        }

        protected String doInBackground(Void... params) {

            try {
                OkHttpClient client = new OkHttpClient();


                Response responses = null;


                RequestBody formBody = new FormEncodingBuilder()
                        .add("table", "service_order")
                        .add("field", "order_id")
                        .add("updated_ids", sb.toString())
                        .add("user_id", Common.getUserIdFromSP(getApplicationContext()))
                        .build();


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
                    System.out.println("!!!!!!!1ACKNOWLEDGE_TO_SERVER" + formBody.toString() + "\n" + jsonData);
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

            new Async_getallcustomersoffline().execute();
            Common.dismissProgressDialog(progressDialog);


        }
    }


}
