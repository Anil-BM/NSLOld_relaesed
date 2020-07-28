
package com.nsl.beejtantra.advancebooking;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.nsl.beejtantra.Constants;
import com.nsl.beejtantra.DatabaseHandler;
import com.nsl.beejtantra.MainActivity;
import com.nsl.beejtantra.R;
import com.nsl.beejtantra.Schemes;
import com.nsl.beejtantra.ServiceOrderDetailMaster;
import com.nsl.beejtantra.Utility;
import com.nsl.beejtantra.adapters.ABSMultiViewTypeAdapter;
import com.nsl.beejtantra.commonutils.Common;
import com.nsl.beejtantra.orderindent.ViewSalesOrderCustomerDetailsActivity;
import com.nsl.beejtantra.pojo.GetWayDetailsByID;
import com.nsl.beejtantra.pojo.MyWayProduct;
import com.nsl.beejtantra.pojo.RejectionCommentVo;
import com.nsl.beejtantra.pojo.ServiceOrderApproval;
import com.nsl.beejtantra.pojo.ServiceOrderHistory;
import com.nsl.beejtantra.pojo.ViewModalAdvBookingListPojo;
import com.nsl.beejtantra.pojo.ViewModalAdvBookingPojo;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

import static com.nsl.beejtantra.DatabaseHandler.KEY_PRODUCT_BRAND_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_PRODUCT_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_PRODUCT_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_SCHEMES_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CUSTOMER_DETAILS_CREDIT_LIMIT;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CUSTOMER_DETAILS_INSIDE_BUCKET;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CUSTOMER_DETAILS_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CUSTOMER_DETAILS_OUTSIDE_BUCKET;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CUSTOMER_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_DIVISION_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_SCHEME_PRODUCTS_SLAB_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_SERVICEORDER_CUSTOMER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_SERVICEORDER_DETAIL_ADVANCEAMOUNT;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_SERVICEORDER_DETAIL_CROP_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_SERVICEORDER_DETAIL_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_SERVICEORDER_DETAIL_ORDERPRICE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_SERVICEORDER_DETAIL_ORDER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_SERVICEORDER_DETAIL_ORDER_QUANTITY;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_SERVICEORDER_DETAIL_PRODUCT_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_SERVICEORDER_DETAIL_QUANTITY;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_SERVICEORDER_DETAIL_SCHEME_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_SERVICEORDER_DIVISION_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_SERVICEORDER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_SERVICEORDER_ORDERDATE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_SERVICEORDER_ORDERTYPE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_SERVICEORDER_SLAB_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_SERVICEORDER_USER_ID;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_CUSTOMERS;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_CUSTOMER_DETAILS;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_PRODUCTS;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_SCHEMES;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_SERVICEORDER;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_SERVICEORDERDETAILS;
import static com.nsl.beejtantra.R.drawable.background_green;
import static com.nsl.beejtantra.R.drawable.background_orange;
import static com.nsl.beejtantra.orderindent.FragmentOrderIndent.REQUEST_TYPE;

/**
 * A simple {@link Fragment} subclass.
 */
public class ViewABSDetailsActivity extends AppCompatActivity {


    ProgressDialog progressDialog;

    FloatingActionButton fab;
    String jsonData, userId, customer_id, DivisionId;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();

    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    TextView tv_name, tv_code, tv_amount, tv_token_amount, tv_company_name;
    double sum_osa = 0;
    double osa;
    int j;
    DatabaseHandler db;
    SQLiteDatabase sdbw, sdbr;
    private int requestType;
    SimpleDateFormat dateFormat;
    SimpleDateFormat simpleDateFormat;
    private ArrayList<String> dateList = new ArrayList<>();
    List<ViewModalAdvBookingPojo> viewModalAdvBookingPojos = new ArrayList<>();
    ArrayList<ViewModalAdvBookingPojo> viewModalAdvBookingPojosFinal = new ArrayList<>();
    ViewModalAdvBookingListPojo viewModalAdvBookingListPojo = new ViewModalAdvBookingListPojo();

    private ViewModalAdvBookingPojo viewModalAdvBookingPojo;

    private ArrayList<MyWayProduct> pProductArrayList;
    private ArrayList<MyWayProduct.SubCategory> pSubItemArrayList;
    private List<String> arrayList;
    private List<GetWayDetailsByID> allFnfListSqlite12 = new ArrayList<>();
    private boolean isFirstViewClick;
    private boolean isSecondViewClick;
    private ScrollView mLinearListView2;
    private int sr_no = 0;
    private RecyclerView recyclerView;
    private String serviceId;
    private TextView tv_approvalstatus;
    private TextView tv_accept;
    private ImageView tv_reject;
    private LinearLayout ll_accept_reject;
    private String approval_status;
    private int approval_status_send;
    private int role;
    private ServiceOrderApproval soa;
    private ServiceOrderHistory serviceOrderHistory;
    private String order_id;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_advancebooking_customer_details_test);
        db = new DatabaseHandler(this);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        userId = sharedpreferences.getString("userId", "");
        role = sharedpreferences.getInt(Constants.SharedPrefrancesKey.ROLE, 0);

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
        fab = (FloatingActionButton) findViewById(R.id.fab);
        if (getIntent().getStringExtra("TYPE").equalsIgnoreCase("rm")) {
            userId = getIntent().getStringExtra("userId");
            fab.setVisibility(View.INVISIBLE);
        }

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotohome = new Intent(getApplicationContext(), NewAdvancebokingChooseActivity.class);
                gotohome.putExtra("selection", "distributor");
                gotohome.putExtra("customer_id", customer_id);
                startActivity(gotohome);
                finish();
            }
        });


        dateFormat = new SimpleDateFormat(Constants.DateFormat.COMMON_DATE_FORMAT);
        simpleDateFormat = new SimpleDateFormat("dd MMMM yyyy");
        //company_name
        tv_name = (TextView) findViewById(R.id.tv_name);
        tv_company_name = (TextView) findViewById(R.id.tv_company_name);
        tv_code = (TextView) findViewById(R.id.tv_code);
        tv_amount = (TextView) findViewById(R.id.tv_amount);
        tv_token_amount = (TextView) findViewById(R.id.tv_token_amount);
        recyclerView = (RecyclerView) findViewById(R.id.recycler_view);
        tv_approvalstatus = (TextView) findViewById(R.id.tv_approvalstatus);
        tv_accept = (TextView) findViewById(R.id.tv_accept);
        tv_reject = (ImageView) findViewById(R.id.tv_reject);
        //btn_view            = (Button) findViewById(R.id.view_comments);
        ll_accept_reject = (LinearLayout) findViewById(R.id.ll_accept_reject);

        customer_id = getIntent().getStringExtra("customer_id");
        DivisionId = getIntent().getStringExtra("DivisionId");
        order_id = getIntent().getStringExtra("order_id");
        serviceId = getIntent().getStringExtra("service_id");
        approval_status = getIntent().getStringExtra("approval_status");
        soa = db.getSOAByUserId(Common.getUserIdFromSP(this), order_id);
        if (soa == null) {
            ll_accept_reject.setVisibility(View.GONE);
//            approval_status = getIntent().getStringExtra("approval_status");
        } else {
            approval_status = soa.orderStatus;
            if (soa.assignedUserId.equalsIgnoreCase(Common.getUserIdFromSP(this)) /*|| soa.createdBy.equalsIgnoreCase(Common.getUserIdFromSP(this))*/)
                ll_accept_reject.setVisibility(View.GONE);
            else
                ll_accept_reject.setVisibility(View.VISIBLE);
        }

        tv_name.setText(getIntent().getStringExtra("customer_name"));
        tv_code.setText("(" + getIntent().getStringExtra("customer_code") + ")");
        tv_token_amount.setText("" + getIntent().getStringExtra("Token_Amount"));
        tv_company_name.setText("" + getIntent().getStringExtra("company_name"));


        requestType = getIntent().getIntExtra(REQUEST_TYPE, 0);

        new Async_getallcustomersoffline().execute();


        if (requestType == 1) {
            toolbar.setTitle("Order Indent");
        }

        setButtonview(approval_status);
//        if (role != Constants.Roles.ROLE_5) {
//            tv_accept.setVisibility(View.GONE);
//            tv_reject.setVisibility(View.GONE);
//        }
        tv_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                approval_status_send = 1;
                showCommentsDialog(order_id, tv_accept, 1);
            }
        });
        tv_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                approval_status_send = 2;
                showCommentsDialog(order_id, tv_accept, 2);
            }
        });
    }

    private void setButtonview(String approval_status) {
        if (approval_status != null) {
            if (approval_status.equalsIgnoreCase("1")) {
                tv_approvalstatus.setText("Approved");
                tv_approvalstatus.setVisibility(View.VISIBLE);
                tv_accept.setVisibility(View.GONE);
                tv_reject.setVisibility(View.GONE);
                tv_approvalstatus.setTextColor(getResources().getColor(R.color.white));
                tv_approvalstatus.setBackgroundResource(background_green);
                //  tv_approvalstatus.setTextColor(getResources().getColor(R.color.green));
            } else if (approval_status.equalsIgnoreCase("2")) {
                tv_reject.setVisibility(View.GONE);
                tv_accept.setVisibility(View.GONE);
                tv_approvalstatus.setVisibility(View.VISIBLE);
                tv_approvalstatus.setText("Rejected");
                tv_approvalstatus.setTextColor(getResources().getColor(R.color.white));
                tv_approvalstatus.setBackgroundResource(background_orange);
            } else {

                if (soa == null) {
                    tv_accept.setVisibility(View.VISIBLE);
                    tv_reject.setVisibility(View.VISIBLE);
                    tv_approvalstatus.setText("Pending");
                    tv_approvalstatus.setBackgroundResource(background_orange);
                    tv_approvalstatus.setTextColor(getResources().getColor(R.color.black));
                } else {
                    tv_approvalstatus.setVisibility(View.GONE);
                    ll_accept_reject.setVisibility(View.VISIBLE);
                    tv_accept.setVisibility(View.VISIBLE);
                    tv_reject.setVisibility(View.VISIBLE);
                }
            }
            //  tv_approvalstatus.setTextColor(getResources().getColor(R.color.tabselectedcolor));
        }
    }

    private void showCommentsDialog(final String order_id, final View button_view, final int i) {

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Transport Name");
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
                String comment = editText.getText().toString();
                if (Utility.isNetworkAvailable(ViewABSDetailsActivity.this, true)) {
                    JSONObject mainObject = new JSONObject();
                    try {
                        mainObject.put("approval_status", String.valueOf(i));
                        mainObject.put("updated_by", Common.getUserIdFromSP(getApplicationContext()));
                        mainObject.put("comments", comment);
                        mainObject.put("order_id", order_id);
                        JSONArray itemsArray = new JSONArray();
                        for (int i = 0; i < favouriteItem.size(); i++) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("order_id", order_id);
                            jsonObject.put("service_order_detail_id", favouriteItem.get(i).get(KEY_TABLE_SERVICEORDER_DETAIL_MASTER_ID));
                            jsonObject.put("product_id", favouriteItem.get(i).get(KEY_TABLE_SERVICEORDER_DETAIL_PRODUCT_ID));
                            String quantity = favouriteItem.get(i).get("Quantity");
                            jsonObject.put("quantity", quantity.trim().length() == 0 ? "0" : quantity.trim());
                            jsonObject.put("order_price", "");
                            jsonObject.put("advance_amount", favouriteItem.get(i).get("ABS"));
                            jsonObject.put("edit_flag", favouriteItem.get(i).get("edit_flag"));
                            itemsArray.put(jsonObject);
                        }
                        mainObject.put("service_order_history", itemsArray);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    Common.Log.i("REQUEST BODY " + mainObject.toString());
//                    String requestBody = "approval_status=" + i + "&updated_by=" + Common.getUserIdFromSP(getApplicationContext()) + "&comments=" + comment + "&order_id=" + order_id;
                    new ApproveOrRejectAsyncTask().execute(mainObject.toString(), button_view);
                } else {

                    // db.updateApprovalOrRejectStatus(Common.getUserIdFromSP(getApplicationContext()),"2",comment,order_id,true);
                }
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
//                if (Utility.isNetworkAvailable(ViewSalesOrderCustomerDetailsActivity.this, true)) {
//                    String requestBody = "approval_status=" + i + "&updated_by=" + Common.getUserIdFromSP(getApplicationContext()) + "&order_id=" + order_id;
//                    new ApproveOrRejectAsyncTask().execute(requestBody, button_view);
//                } else {
//                    //  db.updateApprovalOrRejectStatus(Common.getUserIdFromSP(getApplicationContext()),"2","",order_id,true);
//                }
            }
        });


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

    private class ApproveOrRejectAsyncTask extends AsyncTask<Object, Void, String> {
        View imageView;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ViewABSDetailsActivity.this);
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
                Common.Log.i("Request APPROVE" + new Gson().toJson(body.toString()));
                imageView = (View) params[1];
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
                        // showDialog((String) responseObject.get("msg"));
                        String comments = responseObject.getString("comments");
                        imageView.setVisibility(View.GONE);
                        JSONArray historyArray = responseObject.getJSONArray("service_order_history");
                        JSONArray service_order_details = responseObject.getJSONArray("service_order_details");
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
                            soh.orderApprovalId=historyObj.getString("service_order_approval_id");
                            db.insertServiceOrderHistory(soh);
                        }
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


                            db.addServiceorderdetails(new ServiceOrderDetailMaster(ffmID, service_order_details_order_id, service_order_details_crop_id, scheme_id,
                                    product_id, quantity, order_price,
                                    service_order_details_advance_amount, service_order_details_status, service_order_details_created_datetime, service_order_details_updated_datetime, ffmID, slab_id, orderQuantity));
                        }

                        JSONObject order_approval = responseObject.getJSONObject("order_approval");
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
                        String actualStatus = responseObject.getString("actual_approval_status");
                        db.updateApprovalOrRejectStatus(Common.getUserIdFromSP(getApplicationContext()), actualStatus, comments, (String) responseObject.get("ffm_id"), false);
                        db.updateSOA(Common.getUserIdFromSP(getApplicationContext()), String.valueOf(approval_status_send), order_id);
                        soa = db.getSOAByUserId(Common.getUserIdFromSP(getApplicationContext()), order_id);
                        approval_status = soa.orderStatus;
                        //   new Async_getallcustomersoffline().execute();
                        setButtonview(String.valueOf(approval_status_send));
                        setListOnAdapter(viewModalAdvBookingPojosFinal, recyclerView);
//                        new Async_getallcustomersoffline().execute();
//                        adapter.updateResults(results);
//                        adapter.notifyDataSetChanged();
//                        Intent intent = new Intent();
                        //  setResult(RESULT_OK,intent);
                        //  finish();

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


    private class Async_getallcustomersoffline extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* progressDialog = new ProgressDialog(ViewABSDetailsActivity.this);
            progressDialog.setMessage("Please wait");
            progressDialog.show();*/
        }

        protected String doInBackground(Void... params) {
            favouriteItem.clear();
            j = 0;

            // String selectQuery  = "SELECT  " + KEY_TABLE_CROPS_CROP_MASTER_ID + ","+ KEY_TABLE_CROPS_CROP_NAME +  " FROM " + TABLE_COMPANY_DIVISION_CROPS + " AS CDC JOIN " + TABLE_CROPS + " AS CR ON CDC."+KEY_TABLE_COMPANY_DIVISION_CROPS_CROP_ID + " = CR."+ KEY_TABLE_CROPS_CROP_MASTER_ID + "  where " + KEY_TABLE_COMPANY_DIVISION_CROPS_COMPANY_ID + " = " + company_id + " and " + KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID + " = " + sel_division_id;
            //working query    // String selectQuery    = "SELECT  " + KEY_TABLE_CUSTOMER_NAME + ","+ KEY_TABLE_CUSTOMER_CODE+            " FROM " + TABLE_SERVICEORDER + " AS SO JOIN " + TABLE_CUSTOMERS + " AS C ON C."+KEY_TABLE_CUSTOMER_MASTER_ID + " = SO."+ KEY_TABLE_SERVICEORDER_CUSTOMER_ID +"  where SO." + KEY_TABLE_SERVICEORDER_USER_ID + " = " + userId  ;
//            String selectQuery = " SELECT DISTINCT " + KEY_PRODUCT_BRAND_NAME + ",SD." + KEY_TABLE_SCHEME_PRODUCTS_SLAB_ID + ",SD." + KEY_TABLE_SERVICEORDER_DETAIL_ADVANCEAMOUNT + ",C." + KEY_TABLE_CUSTOMER_MASTER_ID + ",SO." + KEY_TABLE_SERVICEORDER_ORDERDATE + ",SD." + KEY_TABLE_SERVICEORDER_DETAIL_QUANTITY + ",SD." + KEY_TABLE_SERVICEORDER_DETAIL_PRODUCT_ID + ",SD." + KEY_TABLE_SERVICEORDER_DETAIL_SCHEME_ID + ",SD." + KEY_TABLE_SERVICEORDER_DETAIL_CROP_ID
//                    + " FROM " + TABLE_SERVICEORDER + " AS SO JOIN " + TABLE_CUSTOMERS + " AS C ON C." + KEY_TABLE_CUSTOMER_MASTER_ID + " = SO." + KEY_TABLE_SERVICEORDER_CUSTOMER_ID +
//                    " LEFT JOIN " + TABLE_SERVICEORDERDETAILS + " AS SD ON SD." + KEY_TABLE_SERVICEORDER_DETAIL_ORDER_ID + " = SO." + KEY_TABLE_SERVICEORDER_ID +
//                    " LEFT JOIN " + TABLE_SCHEMES + " AS SCH ON SD." + KEY_TABLE_SERVICEORDER_DETAIL_SCHEME_ID + " = SCH." + KEY_SCHEMES_MASTER_ID +
//                    " LEFT JOIN " + TABLE_PRODUCTS + " AS PRO ON SD." + KEY_TABLE_SERVICEORDER_DETAIL_PRODUCT_ID + " = PRO." + KEY_PRODUCT_MASTER_ID +
//                    " LEFT JOIN scheme_products AS sp ON sp.scheme_id = SCH.scheme_id" +
//                    "  where SD.order_detail_id is not null AND " + " SO." + KEY_TABLE_SERVICEORDER_USER_ID + " = " + userId + " AND  C." + KEY_TABLE_CUSTOMER_MASTER_ID + " = " + customer_id + " AND SO." + KEY_TABLE_SERVICEORDER_ORDERTYPE + "=2" + " AND SO." + KEY_TABLE_SERVICEORDER_DIVISION_ID + "=" + DivisionId + " AND SD." + KEY_TABLE_SERVICEORDER_DETAIL_ORDER_ID + " = " + serviceId + " group by SD.order_detail_id,DATE(SD.created_datetime)";

            String selectQuery = " SELECT " + KEY_PRODUCT_BRAND_NAME
                    + ",SD." + KEY_TABLE_SCHEME_PRODUCTS_SLAB_ID
                    + ",SD." + KEY_TABLE_SERVICEORDER_DETAIL_ADVANCEAMOUNT
                    + ",C." + KEY_TABLE_CUSTOMER_MASTER_ID
                    + ",CD." + KEY_TABLE_CUSTOMER_DETAILS_CREDIT_LIMIT
                    + ",CD." + KEY_TABLE_CUSTOMER_DETAILS_INSIDE_BUCKET
                    + ",CD." + KEY_TABLE_CUSTOMER_DETAILS_OUTSIDE_BUCKET
                    + ",dv." + KEY_TABLE_DIVISION_NAME
                    + ",SO." + KEY_TABLE_SERVICEORDER_ORDERDATE
                    + ",SD." + KEY_TABLE_SERVICEORDER_DETAIL_QUANTITY
                    + ",SD." + KEY_TABLE_SERVICEORDER_DETAIL_PRODUCT_ID
                    + ",SD." + KEY_TABLE_SERVICEORDER_DETAIL_MASTER_ID
                    + ",SD." + KEY_TABLE_SERVICEORDER_DETAIL_ORDER_QUANTITY
                    + ",SD." + KEY_TABLE_SERVICEORDER_DETAIL_SCHEME_ID
                    + " FROM " + TABLE_SERVICEORDER
                    + " AS SO JOIN " + TABLE_CUSTOMERS + " AS C ON C." + KEY_TABLE_CUSTOMER_MASTER_ID + " = SO." + KEY_TABLE_SERVICEORDER_CUSTOMER_ID
                    + " LEFT JOIN " + TABLE_SERVICEORDERDETAILS + " AS SD ON SD."
                    + KEY_TABLE_SERVICEORDER_DETAIL_ORDER_ID + " = SO." + KEY_TABLE_SERVICEORDER_ID
                    + " LEFT JOIN " + TABLE_CUSTOMER_DETAILS + " AS CD ON CD." + KEY_TABLE_CUSTOMER_DETAILS_MASTER_ID + " = C." + KEY_TABLE_CUSTOMER_MASTER_ID
                    + " LEFT JOIN " + TABLE_SCHEMES + " AS SCH ON SD." + KEY_TABLE_SERVICEORDER_DETAIL_SCHEME_ID + " = SCH." + KEY_SCHEMES_MASTER_ID
                    + " LEFT JOIN " + TABLE_PRODUCTS + " AS PRO ON SD." + KEY_TABLE_SERVICEORDER_DETAIL_PRODUCT_ID + " = PRO." + KEY_PRODUCT_MASTER_ID
                    + " LEFT JOIN division AS dv ON dv.division_id = SO.division_id"
                    + " LEFT JOIN scheme_products AS sp ON sp.scheme_id = SCH.scheme_id" +
                    "  where SO." + KEY_TABLE_SERVICEORDER_USER_ID + " = " + userId
                    + " AND  SO." + KEY_TABLE_SERVICEORDER_CUSTOMER_ID + " = " + customer_id + " AND SO."
                    + KEY_TABLE_SERVICEORDER_ORDERTYPE + "=2 AND SO.division_id=" + DivisionId + " AND SO.service_id=" + serviceId + " group by " + KEY_TABLE_SERVICEORDER_ORDERDATE + "," + KEY_PRODUCT_NAME;
            sdbw = db.getWritableDatabase();

            Cursor cursor = sdbw.rawQuery(selectQuery, null);
            Log.e("selectQuery", selectQuery);
            if (cursor != null && cursor.moveToFirst()) {
                do {


                    if (cursor.getString(2) == null || cursor.getString(2).equalsIgnoreCase("") || cursor.getString(2).equalsIgnoreCase("null")) {

                    } else {

                        j = j + 1;
                        osa = Double.parseDouble(cursor.getString(2));
                        Log.e("Values", cursor.getString(0) + " : " + cursor.getString(1) + " : OSA" + String.valueOf(osa) + " : customer id" + cursor.getString(3));
                        sum_osa = sum_osa + osa;
                        HashMap<String, String> map = new HashMap<String, String>();
                        // adding each child node to HashMap key => value
                        map.put("product_id", cursor.getString(cursor.getColumnIndex(KEY_TABLE_SERVICEORDER_DETAIL_PRODUCT_ID)));
                        map.put("service_order_detail_id", cursor.getString(cursor.getColumnIndex(KEY_TABLE_SERVICEORDER_DETAIL_MASTER_ID)));
                        map.put("customer_name", cursor.getString(0));
                        map.put("customer_code", cursor.getString(1));
                        map.put("customer_id", cursor.getString(3));
                        map.put("Division", "");
                        map.put("ABS", String.valueOf(osa));
                        map.put("Crop name", "");
                        map.put("Product Name", "");
                        map.put("Order price", "");
                        map.put("Quantity", cursor.getString(9));
                        map.put("OrderDate", cursor.getString(8));
                        map.put("itemcount", "" + j);
                        map.put("price_per_unit", db.getPricePerUnitByProductId( cursor.getString(cursor.getColumnIndex(KEY_TABLE_SERVICEORDER_DETAIL_PRODUCT_ID)), Common.getDefaultSP(getApplicationContext()).getString("region_id", "0")));
                        favouriteItem.add(map);


                        String schemeId = cursor.getString(cursor.getColumnIndex(KEY_TABLE_SERVICEORDER_DETAIL_SCHEME_ID));
                        String pricePerUnit = "0";
                        Log.d("schemeId: ", schemeId);
                        if (schemeId == null || schemeId.equalsIgnoreCase("0") || schemeId.equalsIgnoreCase(" ")) {
                            pricePerUnit = db.getPricePerUnitByProductId(cursor.getString(cursor.getColumnIndex(KEY_TABLE_SERVICEORDER_DETAIL_PRODUCT_ID)),Common.getDefaultSP(getApplicationContext()).getString("region_id", "0"));
                        } else {
                            List<Schemes> schemesByProducId = db.getSchemesByProducId(cursor.getString(cursor.getColumnIndex(KEY_TABLE_SERVICEORDER_DETAIL_PRODUCT_ID)), cursor.getString(cursor.getColumnIndex(KEY_TABLE_SCHEME_PRODUCTS_SLAB_ID)), schemeId);
                            pricePerUnit = schemesByProducId.size() > 0 ? schemesByProducId.get(0).getScheme_value() : "0";
                        }

                        dateList.add(Common.dateformate(cursor.getString(8).split(" ")[0]));
                        // final ArrayList<Schemes>  adapter_schemes = (ArrayList<Schemes>) db.getSchemesByProducId(cursor.getString(6));


                        ViewModalAdvBookingPojo viewModalAdvBookingPojo = new ViewModalAdvBookingPojo();

                        viewModalAdvBookingPojo.customerName = cursor.getString(0);
                        viewModalAdvBookingPojo.customerCode = cursor.getString(1);
                        viewModalAdvBookingPojo.customerId = cursor.getString(3);
                        viewModalAdvBookingPojo.orderDate = Common.dateformate(cursor.getString(8).split(" ")[0]);
                        viewModalAdvBookingPojo.aBS = String.valueOf(osa);
                        viewModalAdvBookingPojo.orderQuantity = cursor.getString(cursor.getColumnIndex(KEY_TABLE_SERVICEORDER_DETAIL_ORDER_QUANTITY));
                        viewModalAdvBookingPojo.quantity = cursor.getString(9);
                        viewModalAdvBookingPojo.rate = pricePerUnit;
                        viewModalAdvBookingPojo.slabId = cursor.getString(1).equalsIgnoreCase(null) || cursor.getString(1).equalsIgnoreCase("") || cursor.getString(1).equalsIgnoreCase("null") ? "--NA--" : cursor.getString(1);
                        viewModalAdvBookingPojo.serviceOrderDetailId = cursor.getString(cursor.getColumnIndex(KEY_TABLE_SERVICEORDER_DETAIL_MASTER_ID));
                        viewModalAdvBookingPojos.add(viewModalAdvBookingPojo);


                        GetWayDetailsByID getWayDetailsByID = new GetWayDetailsByID();
                        getWayDetailsByID.setCustomer_name(cursor.getString(0));
                        getWayDetailsByID.setCustomer_code(cursor.getString(1));
                        getWayDetailsByID.setCustomer_id(cursor.getString(3));
                        getWayDetailsByID.setOrderdate(Common.dateformate(cursor.getString(4).split(" ")[0]));
                        getWayDetailsByID.setAbs(String.valueOf(osa));
                        getWayDetailsByID.setQuantity(cursor.getString(9));
                        getWayDetailsByID.setRate(db.getPricePerUnitByProductId(cursor.getString(cursor.getColumnIndex(KEY_TABLE_SERVICEORDER_DETAIL_PRODUCT_ID))));

                        allFnfListSqlite12.add(getWayDetailsByID);
                    }


                    String orders = String.valueOf(favouriteItem.size());
                } while (cursor.moveToNext());

            } else Log.d("LOG", "returned null!");


            HashSet hs = new HashSet();
            hs.addAll(dateList);
            dateList.clear();
            dateList.addAll(hs);

            for (j = 0; j < dateList.size(); j++) {
                ViewModalAdvBookingPojo viewModalAdvBookingPojo = new ViewModalAdvBookingPojo();
                viewModalAdvBookingPojo.orderDate = dateList.get(j);
                viewModalAdvBookingPojo.type = 0;
                viewModalAdvBookingPojosFinal.add(viewModalAdvBookingPojo);


                for (int i = 0; i < viewModalAdvBookingPojos.size(); i++) {
                    if (dateList.get(j).equalsIgnoreCase(viewModalAdvBookingPojos.get(i).orderDate)) {
                        ViewModalAdvBookingPojo viewModalAdvBookingPojo1 = viewModalAdvBookingPojos.get(i);
                        viewModalAdvBookingPojo1.type = 1;
                        viewModalAdvBookingPojosFinal.add(viewModalAdvBookingPojo1);
                    }

                }
            }


            Log.d("LOG", "returned null!" + dateList.toString() + "\n" + viewModalAdvBookingPojos.toString());
            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            tv_amount.setText("" + String.valueOf(sum_osa));
            /*if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }*/
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    setListOnAdapter(viewModalAdvBookingPojosFinal, recyclerView);
                }
            });

        }
    }


    public ABSMultiViewTypeAdapter setListOnAdapter(final ArrayList<ViewModalAdvBookingPojo> viewModalAdvBookingPojos, RecyclerView recyclerView) {
        Log.d("viewModal: ", viewModalAdvBookingPojos.toString());
        GridLayoutManager gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView.setLayoutManager(gridLayoutManager);
        ABSMultiViewTypeAdapter absMultiViewTypeAdapter = null;

        if (viewModalAdvBookingPojos.size() != 0) {
            recyclerView.setVisibility(View.VISIBLE);
            //recordnotfnd.setVisibility(View.GONE);
            absMultiViewTypeAdapter = new ABSMultiViewTypeAdapter(viewModalAdvBookingPojos, this, approval_status,ll_accept_reject.getVisibility()) {
                @Override
                public void updatedObjects(ArrayList<ViewModalAdvBookingPojo> viewModalAdvBookingPojos) {
                    super.updatedObjects(viewModalAdvBookingPojos);
                    viewModalAdvBookingPojosFinal = viewModalAdvBookingPojos;
                    for (int i = 0; i < favouriteItem.size(); i++) {
                        for (int k = 0; k < viewModalAdvBookingPojos.size(); k++) {
                            if (favouriteItem.get(i).get("service_order_detail_id").equalsIgnoreCase(viewModalAdvBookingPojos.get(k).serviceOrderDetailId)) {
                                favouriteItem.get(i).put("edit_flag", "1");
                                favouriteItem.get(i).put("Quantity", viewModalAdvBookingPojos.get(k).quantity);
                                favouriteItem.get(i).put("ABS", viewModalAdvBookingPojos.get(k).aBS);
                                sum_osa = 0;
                                for (int j = 0; j < favouriteItem.size(); j++) {
                                    sum_osa += Double.parseDouble(favouriteItem.get(j).get("ABS"));
                                }

                            }
                        }
                    }
                    tv_amount.setText(String.valueOf(sum_osa));

                }
            };
            recyclerView.setAdapter(absMultiViewTypeAdapter);
            absMultiViewTypeAdapter.setOnItemClickListener(new ABSMultiViewTypeAdapter.OnItemClickListener() {

                @Override
                public void onItemClick(View view, int position) {


                }
            });
            absMultiViewTypeAdapter.notifyDataSetChanged();

        } else if (viewModalAdvBookingPojos == null || viewModalAdvBookingPojos.size() == 0) {

            // recordnotfnd.setVisibility(View.VISIBLE);
            recyclerView.setVisibility(View.GONE);
        }

        return absMultiViewTypeAdapter;
    }


}
