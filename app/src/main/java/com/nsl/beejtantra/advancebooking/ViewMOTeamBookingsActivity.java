package com.nsl.beejtantra.advancebooking;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nsl.beejtantra.Constants;
import com.nsl.beejtantra.DatabaseHandler;
import com.nsl.beejtantra.MainActivity;
import com.nsl.beejtantra.R;
import com.nsl.beejtantra.SelectedCities;
import com.nsl.beejtantra.ServiceOrderDetailMaster;
import com.nsl.beejtantra.ServiceOrderMaster;
import com.nsl.beejtantra.Users;
import com.nsl.beejtantra.Utility;
import com.nsl.beejtantra.commonutils.Common;
import com.nsl.beejtantra.pojo.RejectionCommentVo;
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
import java.util.Calendar;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_COMPANIES_COMPANY_CODE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_COMPANIES_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CUSTOMER_CODE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CUSTOMER_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_SERVICEORDER_COMPANY_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_SERVICEORDER_DETAIL_ORDER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_SERVICEORDER_FFM_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_SERVICEORDER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USERS_FIRST_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USERS_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_COMPANIES;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_EMPLOYEE_VISIT_MANAGEMENT;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_SERVICEORDER;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_USERS;
import static com.nsl.beejtantra.R.drawable.background_green;
import static com.nsl.beejtantra.R.drawable.background_orange;
import static com.nsl.beejtantra.orderindent.FragmentOrderIndent.REQUEST_TYPE;


/**
 * A simple {@link Fragment} subclass.
 */
public class ViewMOTeamBookingsActivity extends AppCompatActivity {
    // JSON parser class
    private JSONObject JSONObj;
    private JSONArray JSONArr = null;
    ProgressDialog progressDialog;
    private RecyclerView listView;
    private ItemfavitemAdapter adapter;
    DatabaseHandler db;
    int osa;
    SQLiteDatabase sdbw, sdbr;
    Spinner spin_user;
    String jsonData;
    String userId, selUserId;
    String team;
    int role;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    int requestType;

    private ArrayList<SelectedCities> organisations;
    ArrayList<String> userList;

    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    private TextView tv_list_all, tv_alert;
    private LinearLayout lv_content;
    String comment;
    boolean isApproval;
    private String insertComment;
    private String rejectionCommentVoJson;
    private TextView toolbar_title;
    private StringBuilder sb = new StringBuilder();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.viewmobookings);
        Common.Log.i("Screen " + getLocalClassName());
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        userId = Common.getUserIdFromSP(this);
        selUserId = getIntent().getExtras().getString("user_id", null);
        team = sharedpreferences.getString("team", "");
        role = sharedpreferences.getInt(Constants.SharedPrefrancesKey.ROLE, 0);

        Log.d("team ", team + "\n role" + role);
        db = new DatabaseHandler(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                finish();
            }
        });
        tv_list_all = (TextView) findViewById(R.id.tv_list_all);
        tv_alert = (TextView) findViewById(R.id.tv_alert);
        toolbar_title = (TextView) findViewById(R.id.toolbar_title);
        lv_content = (LinearLayout) findViewById(R.id.lv_content);
        spin_user = (Spinner) findViewById(R.id.spin_user);
        callapi();
        listView = (RecyclerView) findViewById(R.id.listView);
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(llm);
        adapter = new ItemfavitemAdapter(getApplicationContext(), favouriteItem);
        listView.setAdapter(adapter);
        requestType = getIntent().getIntExtra(REQUEST_TYPE, 0);


        if (requestType == 1) {
            toolbar_title.setText("Order Indent");
            tv_list_all.setText("List of all Order Indent");
        }

        // new Async_getall().execute();


    }

    @Override
    protected void onResume() {
        super.onResume();
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        if (Common.haveInternet(this)) {

            new Async_getNewOrUpdateserviceorders().execute();
        } else {

            callapi();
        }

    }

    private void callapi() {
        organisations = new ArrayList<SelectedCities>();
        userList = new ArrayList<String>();
        organisations.clear();
        SelectedCities citiez = new SelectedCities();
        citiez.setCityId("0");
        citiez.setCityName("Select User");
        // System.out.println("city id is :" + cityId + "city name is :" + cityName);
        organisations.add(citiez);
        userList.add("Select User");
        try {

            String selectQuery = "SELECT " + KEY_TABLE_USERS_FIRST_NAME + "," + KEY_TABLE_USERS_MASTER_ID + " FROM " + TABLE_USERS + "  WHERE  " + db.KEY_TABLE_USERS_STATUS + " = 1 and  user_id in (" + team + ")";
            //List<Company_division_crops> cdclist = db.getAllCompany_division_crops();
            Log.d("selectQuery", selectQuery);
            sdbw = db.getWritableDatabase();

            Cursor cursor = sdbw.rawQuery(selectQuery, null);
            //System.out.println("cursor count "+cursor.getCount());
            if (cursor.moveToFirst()) {
                do {
                    Users users = new Users();

                    users.setUserMasterID(cursor.getString(1));
                    users.setUser_first_name(cursor.getString(0));

                    SelectedCities cities2 = new SelectedCities();
                    cities2.setCityId(cursor.getString(1));
                    cities2.setCityName(cursor.getString(0));
                    // System.out.println("city id is :" + cityId + "city name is :" + cityName);
                    organisations.add(cities2);
                    if (Common.getUserIdFromSP(this).equalsIgnoreCase(cities2.getCityId()))
                        userList.add("SELF");
                    else
                        userList.add(cursor.getString(0));

                } while (cursor.moveToNext());
            }

            // do some stuff....
        } catch (Exception e) {
            e.printStackTrace();
        }

        spin_user.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, R.id.customSpinnerItemTextView, userList));
        spin_user.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userId = organisations.get(position).getCityId();

                if (userId.equalsIgnoreCase("0")) {

                } else {
                    new Async_getallcustomersoffline().execute();
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        if (selUserId != null) {
            for (int i = 0; i < organisations.size(); i++) {
                if (selUserId.equalsIgnoreCase(organisations.get(i).getCityId())) {
                    spin_user.setSelection(i);
                    break;
                }
            }
        }
        Common.dismissProgressDialog(progressDialog);
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
            home.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
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
                    " C.customer_id," +
                    " SO.advance_amount, SUM(SOD.advance_amount) AS sumprice,dv.division_id,division_name,CM." + KEY_TABLE_COMPANIES_COMPANY_CODE + ",SO.approval_status" +
                    ",SO.ffmid, SO.service_id" +
                    " FROM service_order_details SOD" +
                    " JOIN service_order SO ON SO." + KEY_TABLE_SERVICEORDER_ID + " = SOD." + KEY_TABLE_SERVICEORDER_DETAIL_ORDER_ID +
                    " JOIN customers AS C ON C.customer_id = SO.customer_id" +
                    " JOIN division AS dv ON dv.division_id = SO.division_id" +
                    " LEFT JOIN " + TABLE_COMPANIES + " AS CM ON CM." + KEY_TABLE_COMPANIES_MASTER_ID + " = SO." + KEY_TABLE_SERVICEORDER_COMPANY_ID +
                    " WHERE SO.user_id =" + userId +
                    " AND SO.order_type =" + requestType +
                    " GROUP BY SO.customer_id";

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
                    map.put("customer_name", cursor.getString(0));
                    map.put("customer_code", cursor.getString(1));
                    map.put("customer_id", cursor.getString(2));
                    map.put("Division", cursor.getString(6));
                    map.put("DivisionId", cursor.getString(5));
                    map.put("ABS", "");
                    map.put("Token_Amount", db.getTokenAmount(cursor.getString(2), cursor.getString(5), Calendar.getInstance().get(Calendar.YEAR), "1"));
                    map.put("Crop name", "");
                    map.put("Product Name", "");
                    map.put("Order_price", cursor.getString(4));
                    map.put("Quantity", "");
                    map.put("Order Date", "");
                    map.put("company_name", cursor.getString(6));
                    map.put("approval_status", cursor.getString(8));
                    map.put("order_id", cursor.getString(9));
                    map.put("service_id", cursor.getString(10));
                    favouriteItem.add(map);
                    String orders = String.valueOf(favouriteItem.size());
                } while (cursor.moveToNext());
            } else Log.d("LOG", "returned null!");

            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Common.dismissProgressDialog(progressDialog);


            if (favouriteItem.size() == 0) {
                tv_alert.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);

            } else {
                tv_alert.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
                adapter.updateResults(favouriteItem);

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
            ViewHolder viewHolder = new ViewHolder(getLayoutInflater().inflate(R.layout.row_viewbooking, null));
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull ViewHolder holder, final int position) {
            holder.itemname.setText(results.get(position).get("customer_name"));
            holder.company_code.setText(""/*results.get(position).get("Division")*/);
            holder.tv_rbs_amount.setText("TA :" + results.get(position).get("Token_Amount"));
            holder.tv_bookeamount.setText("BA :" + results.get(position).get("Order_price"));

            holder.tv_approvalstatus.setVisibility(View.GONE);
            holder.tv_accept.setVisibility(View.GONE);
            holder.tv_reject.setVisibility(View.GONE);
            //  holder.ll_accept_reject.setVisibility(View.GONE);

           /* if (results.get(position).get("approval_status").equalsIgnoreCase("1")) {
                holder.tv_approvalstatus.setText("Approved");
                holder.tv_accept.setVisibility(View.GONE);
                holder.tv_reject.setVisibility(View.VISIBLE);
                holder.tv_approvalstatus.setTextColor(getResources().getColor(R.color.white));
            } else if (results.get(position).get("approval_status").equalsIgnoreCase("2")) {
                holder.tv_reject.setVisibility(View.GONE);
                holder.tv_accept.setVisibility(View.VISIBLE);
                holder.tv_approvalstatus.setText("Rejected");
                holder.tv_approvalstatus.setTextColor(getResources().getColor(R.color.white));
            } else {
                holder.tv_accept.setVisibility(View.VISIBLE);
                holder.tv_reject.setVisibility(View.VISIBLE);
                holder.tv_approvalstatus.setText("Pending");
                holder.tv_approvalstatus.setTextColor(getResources().getColor(R.color.tabselectedcolor));
            }*/
            holder.itemLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent customers = new Intent(getApplicationContext(), ViewMOTeamBookingsApprovalActivity.class);
                    customers.putExtra("customer_name", results.get(position).get("customer_name"));
                    customers.putExtra("customer_code", results.get(position).get("customer_code"));
                    customers.putExtra("ABS", results.get(position).get("ABS"));
                    customers.putExtra("customer_id", results.get(position).get("customer_id"));
                    customers.putExtra("Token_Amount", results.get(position).get("Token_Amount"));
                    customers.putExtra("company_name", results.get(position).get("company_name"));
                    customers.putExtra("DivisionId", results.get(position).get("DivisionId"));
                    customers.putExtra(REQUEST_TYPE, requestType);
                    customers.putExtra("TYPE", "rm");
                    customers.putExtra("userId", userId);
                    customers.putExtra("service_id", results.get(position).get("service_id"));
                    customers.putExtra("order_id", results.get(position).get("ffm_id"));
                    customers.putExtra("approval_status", results.get(position).get("approval_status"));
                    startActivity(customers);
                }
            });
            if (results.get(position).get("approval_status").equalsIgnoreCase("1")) {
                holder.tv_approvalstatus.setText("Approved");
                holder.tv_accept.setVisibility(View.GONE);
                holder.tv_reject.setVisibility(View.GONE);
                holder.tv_approvalstatus.setTextColor(getResources().getColor(R.color.white));
                holder.tv_approvalstatus.setBackgroundResource(background_green);
                //  holder.tv_approvalstatus.setTextColor(getResources().getColor(R.color.green));
            } else if (results.get(position).get("approval_status").equalsIgnoreCase("2")) {
                holder.tv_reject.setVisibility(View.GONE);
                holder.tv_accept.setVisibility(View.GONE);
                holder.tv_approvalstatus.setText("Rejected");
                holder.tv_approvalstatus.setTextColor(getResources().getColor(R.color.white));
                holder.tv_approvalstatus.setBackgroundResource(background_orange);
            } else {
                holder.tv_accept.setVisibility(View.GONE);
                holder.tv_reject.setVisibility(View.GONE);
                holder.tv_approvalstatus.setText("Pending");
                holder.tv_approvalstatus.setBackgroundResource(background_orange);
                holder.tv_approvalstatus.setTextColor(getResources().getColor(R.color.black));
                //  holder.tv_approvalstatus.setTextColor(getResources().getColor(R.color.tabselectedcolor));
            }

            holder.tv_accept.setTag(position);
            holder.tv_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isApproval = true;
                    int position = (int) v.getTag();
                    String order_id = results.get(position).get("order_id");
                    if (Utility.isNetworkAvailable(ViewMOTeamBookingsActivity.this, true)) {
                        String requestBody = "approval_status=1&updated_by=" + Common.getUserIdFromSP(getApplicationContext()) + "&order_id=" + order_id;
                        new ApproveOrRejectAsyncTask().execute(requestBody, v);
                    } else {
                        //  db.updateApprovalOrRejectStatus(Common.getUserIdFromSP(getApplicationContext()),"1","",order_id,true);
                    }
                }
            });

            holder.tv_reject.setTag(position);
            holder.tv_reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    isApproval = false;
                    int position = (int) v.getTag();

                    showCommentsDialog(results.get(position).get("order_id"), v);

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

            @BindView(R.id.tv_company)
            TextView itemname;
            @BindView(R.id.tv_company_code)
            TextView company_code;
            @BindView(R.id.tv_company_coded)
            TextView tv_rbs_amount;
            @BindView(R.id.tv_bookeamount)
            TextView tv_bookeamount;
            @BindView(R.id.tv_approvalstatus)
            TextView tv_approvalstatus;
            @BindView(R.id.tv_accept)
            TextView tv_accept;
            @BindView(R.id.tv_reject)
            ImageView tv_reject;
            @BindView(R.id.view_comments)
            TextView btn_view;
            @BindView(R.id.ll_accept_reject)
            LinearLayout ll_accept_reject;
            @BindView(R.id.item_layout)
            RelativeLayout itemLayout;


            public ViewHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

        public void updateResults(ArrayList<HashMap<String, String>> results) {

            this.results = results;
            notifyDataSetChanged();
        }
    }

    private void showCommentsDialog(final String order_id, final View button_view) {

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Comment");
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_reject_dialog, null);
        alert.setView(dialogView);

        final EditText editText = (EditText) dialogView.findViewById(R.id.edit_comments);
        final Button buttonOk = (Button) dialogView.findViewById(R.id.btn_ok);
        final Button buttonCancel = (Button) dialogView.findViewById(R.id.btn_cancel);
        final AlertDialog alertDialog = alert.show();
        final RejectionCommentVo rejectionCommentVo = new RejectionCommentVo();

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                comment = editText.getText().toString();
                if (Utility.isNetworkAvailable(ViewMOTeamBookingsActivity.this, true)) {
                   /* rejectionCommentVo.comments = comment;
                    rejectionCommentVo.commentsGivenBy = Common.getUserIdFromSP(getApplicationContext());
                    rejectionCommentVo.commentsGivenById = Common.getUserIdFromSP(getApplicationContext());
                    rejectionCommentVo.createdDate = Common.toDateStr(System.currentTimeMillis(), Constants.DateFormat.COMMON_DATE_TIME_FORMAT);
                    rejectionCommentVoJson = new Gson().toJson(rejectionCommentVo);

                    Log.d("rejected comment;", rejectionCommentVoJson);*/


                    String requestBody = "approval_status=2&updated_by=" + Common.getUserIdFromSP(getApplicationContext()) + "&comments=" + comment + "&order_id=" + order_id;
                    new ApproveOrRejectAsyncTask().execute(requestBody, button_view);
                } else {

                    // db.updateApprovalOrRejectStatus(Common.getUserIdFromSP(getApplicationContext()),"2",comment,order_id,true);
                }
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                if (Utility.isNetworkAvailable(ViewMOTeamBookingsActivity.this, true)) {
                    String requestBody = "approval_status=2&updated_by=" + Common.getUserIdFromSP(getApplicationContext()) + "&order_id=" + order_id;
                    new ApproveOrRejectAsyncTask().execute(requestBody, button_view);
                } else {
                    //  db.updateApprovalOrRejectStatus(Common.getUserIdFromSP(getApplicationContext()),"2","",order_id,true);
                }
            }
        });


    }

    private class ApproveOrRejectAsyncTask extends AsyncTask<Object, Void, String> {
        ImageView imageView;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ViewMOTeamBookingsActivity.this);
            progressDialog.setMessage("Please wait ...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        protected String doInBackground(Object... params) {


            try {
                //  Object fdsfds = new Gson().fromJson(params[0].toString(), Object.class);
                // Log.d("fdsfds",fdsfds.toString());
                OkHttpClient client = new OkHttpClient();
                Response responses = null;
                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType, (String) params[0]);
                imageView = (ImageView) params[1];
                Request request = new Request.Builder()
                        .url(Constants.URL_ORDER_APPROVAL)
                        .post(body)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();
                try {
                    responses = client.newCall(request).execute();
                    jsonData = responses.body().string();
                    System.out.println("!!!!!!!1 request" + request.toString() + " response: \n" + jsonData);
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

            if (progressDialog.isShowing())
                progressDialog.dismiss();


            if (jsonData != null) {
                try {

                    JSONObject responseObject = new JSONObject(jsonData);
                    if (responseObject.get("status").equals("success")) {
                        showDialog((String) responseObject.get("msg"));
                        String comments = responseObject.getString("comments");

                        imageView.setVisibility(View.GONE);
                        /*ServiceOrderMaster rejectCommentData = db.getApproveRejectCommentData((String) responseObject.get("ffm_id"));
                        if (rejectCommentData != null) {
                            String approvalComments = rejectCommentData.get_approval_comments();
                            JSONArray jsonArray = new JSONArray(approvalComments);

                            jsonArray.put(rejectionCommentVoJson);
                            insertComment =jsonArray.toString();
                                    Log.d("rejected comment;", jsonArray.toString());
                        } else {


                            JSONArray jsonArray = new JSONArray();
                            jsonArray.put(rejectionCommentVoJson);
                            insertComment = jsonArray.toString();
                            Log.d("rejected comment;", jsonArray.toString());

                        }*/
                        db.updateApprovalOrRejectStatus(Common.getUserIdFromSP(getApplicationContext()), isApproval ? "1" : "2", comments, (String) responseObject.get("ffm_id"), false);

                        new Async_getallcustomersoffline().execute();

                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                //  Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
            }

            Log.d("Reading: ", "Reading all scheme products..");


        }
    }

    public void showDialog(String message) {
        AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Info");
        alert.setMessage(message);
        alert.setPositiveButton("Ok", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        alert.show();
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
Common.Log.i("BOOKINGS URL "+Common.getCompleteURLEVM(Constants.NEW_OR_UPDATED_RECORDS_SERVICE_ORDER, Common.getUserIdFromSP(getApplicationContext()), team)+"&days="+days);
                Request request = new Request.Builder()
                        .url(Common.getCompleteURLEVM(Constants.NEW_OR_UPDATED_RECORDS_SERVICE_ORDER, Common.getUserIdFromSP(getApplicationContext()), team)+"&days="+days)
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
                        Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
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
                if (progressDialog.isShowing()) {
                    progressDialog.dismiss();
                }
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
                    System.out.println("!!!!!!!1ACKNOWLEDGE_TO_SERVER" + formBody.toString() + "\n" + jsonData + "\n ids: " + sb.toString() + " userId " + userId);
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
