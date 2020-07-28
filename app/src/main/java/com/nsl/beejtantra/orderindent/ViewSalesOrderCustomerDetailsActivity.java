
package com.nsl.beejtantra.orderindent;


import android.annotation.SuppressLint;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Gravity;
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
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nsl.beejtantra.Constants;
import com.nsl.beejtantra.DatabaseHandler;
import com.nsl.beejtantra.MainActivity;
import com.nsl.beejtantra.PaymentCollectionPaymentTypeActivity;
import com.nsl.beejtantra.Payment_collection_detailsActivity;
import com.nsl.beejtantra.R;
import com.nsl.beejtantra.ServiceOrderDetailMaster;
import com.nsl.beejtantra.Utility;
import com.nsl.beejtantra.advancebooking.NewAdvancebokingChooseActivity;
import com.nsl.beejtantra.commonutils.Common;
import com.nsl.beejtantra.pojo.RejectionCommentVo;
import com.nsl.beejtantra.pojo.ServiceOrderApproval;
import com.nsl.beejtantra.pojo.ServiceOrderHistory;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import butterknife.OnClick;

import static com.nsl.beejtantra.DatabaseHandler.KEY_PRODUCT_BRAND_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_PRODUCT_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_PRODUCT_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_SCHEMES_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CUSTOMER_DETAILS_CREDIT_LIMIT;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CUSTOMER_DETAILS_INSIDE_BUCKET;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CUSTOMER_DETAILS_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CUSTOMER_DETAILS_OUTSIDE_BUCKET;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CUSTOMER_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_DIVISION_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_DIVISION_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_SCHEME_PRODUCTS_SLAB_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_SERVICEORDER_CUSTOMER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_SERVICEORDER_DETAIL_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_SERVICEORDER_DETAIL_ORDERPRICE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_SERVICEORDER_DETAIL_ORDER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_SERVICEORDER_DETAIL_ORDER_QUANTITY;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_SERVICEORDER_DETAIL_PRODUCT_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_SERVICEORDER_DETAIL_QUANTITY;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_SERVICEORDER_DETAIL_SCHEME_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_SERVICEORDER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_SERVICEORDER_ORDERDATE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_SERVICEORDER_ORDERTYPE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_SERVICEORDER_USER_ID;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_CUSTOMERS;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_CUSTOMER_DETAILS;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_DIVISION;
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
public class ViewSalesOrderCustomerDetailsActivity extends AppCompatActivity {

    private ProgressDialog progressDialog;
    private RecyclerView listView;
    private ItemfavitemAdapter adapter;
    String aging1 = null, aging2 = null;
    String jsonData, userId, customer_id, divison_id;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();
    ArrayList<String> array_comments = new ArrayList<String>();

    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    TextView tv_name, tv_code, tv_amount, tv_credit, tv_osa, tv_division, tv_company_name;
    ImageView iv_ageing;
    DatabaseHandler db;
    SQLiteDatabase sdbw, sdbr;
    private int requestType, j = 0, i = 0;

    private float so, sum_so, sum_osa;
    private String str_division_name, so_company_code;
    double str_climit;
    private String service_id;
    private TextView tv_approvalstatus;
    private TextView tv_accept;
    private ImageView tv_reject;
    private Button btn_view;
    private LinearLayout ll_accept_reject;
    private String order_id;
    private String approval_status;
    private int approval_status_send;
    private int role;
    private ServiceOrderApproval soa;
    private ServiceOrderHistory serviceOrderHistory;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_salesorder_customer_details);

        Toast.makeText(getApplicationContext(), "Please enter comment", Toast.LENGTH_LONG).show();

        db = new DatabaseHandler(this);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        userId = sharedpreferences.getString("userId", "");
        role = sharedpreferences.getInt(Constants.SharedPrefrancesKey.ROLE, 0);
        so_company_code = sharedpreferences.getString("so_company_code", "");

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
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent gotohome = new Intent(getApplicationContext(), NewAdvancebokingChooseActivity.class);

                gotohome.putExtra("customer_id", customer_id);
                startActivity(gotohome);
            }
        });

        tv_name = (TextView) findViewById(R.id.tv_customer_name);
        tv_company_name = (TextView) findViewById(R.id.tv_company_name);

        tv_division = (TextView) findViewById(R.id.tv_division_name);
        tv_code = (TextView) findViewById(R.id.tv_code);
        tv_amount = (TextView) findViewById(R.id.tv_amount);
        tv_credit = (TextView) findViewById(R.id.tv_credit);
        tv_osa = (TextView) findViewById(R.id.tv_osa);
        iv_ageing = (ImageView) findViewById(R.id.iv_ageing);
        tv_approvalstatus = (TextView) findViewById(R.id.tv_approvalstatus);
        tv_accept = (TextView) findViewById(R.id.tv_accept);
        tv_reject = (ImageView) findViewById(R.id.tv_reject);
        //btn_view            = (Button) findViewById(R.id.view_comments);
        ll_accept_reject = (LinearLayout) findViewById(R.id.ll_accept_reject);

        customer_id = getIntent().getStringExtra("customer_id");
        tv_name.setText(getIntent().getStringExtra("customer_name"));
        tv_company_name.setText(so_company_code);
        // tv_code.setText("(" + getIntent().getStringExtra("customer_code") + ")");
        divison_id = getIntent().getStringExtra("divison_id");
        service_id = getIntent().getStringExtra("service_id");
        order_id = getIntent().getStringExtra("order_id");
        approval_status = getIntent().getStringExtra("approval_status");
        soa = db.getSOAByUserId(Common.getUserIdFromSP(this), order_id);
        if (soa == null) {
            ll_accept_reject.setVisibility(View.GONE);
//            approval_status = getIntent().getStringExtra("approval_status");
        } else {
            approval_status = soa.orderStatus;
            if (soa.assignedUserId.equalsIgnoreCase(Common.getUserIdFromSP(this))/* || soa.createdBy.equalsIgnoreCase(Common.getUserIdFromSP(this))*/)
                ll_accept_reject.setVisibility(View.GONE);
            else
                ll_accept_reject.setVisibility(View.VISIBLE);
        }
        if (getIntent().getStringExtra("TYPE").equalsIgnoreCase("rm")) {
            userId = getIntent().getStringExtra("userId");
        }

        listView = (RecyclerView) findViewById(R.id.listView);
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        listView.setLayoutManager(llm);
        adapter = new ItemfavitemAdapter(getApplicationContext(), favouriteItem);
        listView.setAdapter(adapter);
        new Async_getallcustomersoffline().execute();
        new Async_getalloffline().execute();
        requestType = getIntent().getIntExtra(REQUEST_TYPE, 0);

        setButtonview(approval_status,service_id);
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

    private void setButtonview(String approval_status,String order_id) {
        if (approval_status != null) {
            if (approval_status.equalsIgnoreCase("1")) {
                try {
                    JSONObject jsonObject=new JSONObject(db.getorderintentstatus(order_id));
                    tv_approvalstatus.setText("Approved"+" ("+jsonObject.getString("comments")+")");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
                try {
                    JSONObject jsonObject=new JSONObject(db.getorderintentstatus(order_id));
                    tv_approvalstatus.setText("Rejected"+" ("+jsonObject.getString("comments")+")");
                } catch (JSONException e) {
                    e.printStackTrace();
                }

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
           /* progressDialog = new ProgressDialog(ViewAdvancebokingCustomerDetailsActivity.this);
            progressDialog.setMessage("Please wait");
            progressDialog.show();*/

        }

        protected String doInBackground(Void... params) {

            favouriteItem.clear();
            j = 0;

            // String selectQuery  = "SELECT  " + KEY_TABLE_CROPS_CROP_MASTER_ID + ","+ KEY_TABLE_CROPS_CROP_NAME +  " FROM " + TABLE_COMPANY_DIVISION_CROPS + " AS CDC JOIN " + TABLE_CROPS + " AS CR ON CDC."+KEY_TABLE_COMPANY_DIVISION_CROPS_CROP_ID + " = CR."+ KEY_TABLE_CROPS_CROP_MASTER_ID + "  where " + KEY_TABLE_COMPANY_DIVISION_CROPS_COMPANY_ID + " = " + company_id + " and " + KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID + " = " + sel_division_id;
            //working query    // String selectQuery    = "SELECT  " + KEY_TABLE_CUSTOMER_NAME + ","+ KEY_TABLE_CUSTOMER_CODE+            " FROM " + TABLE_SERVICEORDER + " AS SO JOIN " + TABLE_CUSTOMERS + " AS C ON C."+KEY_TABLE_CUSTOMER_MASTER_ID + " = SO."+ KEY_TABLE_SERVICEORDER_CUSTOMER_ID +"  where SO." + KEY_TABLE_SERVICEORDER_USER_ID + " = " + userId  ;
            String selectQuery = " SELECT " + KEY_PRODUCT_BRAND_NAME
                    + ",SD." + KEY_TABLE_SCHEME_PRODUCTS_SLAB_ID
                    + ",SD." + KEY_TABLE_SERVICEORDER_DETAIL_ORDERPRICE
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
                    + KEY_TABLE_SERVICEORDER_ORDERTYPE + "=1 AND SO.division_id=" + divison_id + " AND SO.service_id=" + service_id + " group by " + KEY_TABLE_SERVICEORDER_ORDERDATE + "," + KEY_PRODUCT_NAME;
            Log.e("selectQuery123", selectQuery);
            sdbw = db.getWritableDatabase();

            Cursor cursor = sdbw.rawQuery(selectQuery, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {

                    if (cursor.getString(2) == null || cursor.getString(2).equalsIgnoreCase("") || cursor.getString(2).equalsIgnoreCase("null")) {
                        int str_count = cursor.getCount();
                    } else {
                        j++;
                        so = Float.parseFloat(cursor.getString(2));
                        Log.e("Values", cursor.getString(0) + " : " + cursor.getString(1) + " : OSA" + String.valueOf(so) + " : customer id" + cursor.getString(3) + " : qty" + cursor.getString(9));
                        sum_so = sum_so + so;
                        String inside_value = cursor.getString(5);
                        String outside_value = cursor.getString(6);
                        float inside = (inside_value != null && !inside_value.equals("null")) ? Float.parseFloat(cursor.getString(5)) : 0;
                        float outside = (outside_value != null && !outside_value.equals("null")) ? Float.parseFloat(cursor.getString(6)) : 0;
                        float total_osa = inside + outside;
                        sum_osa = sum_osa + total_osa;

                        str_division_name = cursor.getString(7);

                        Double creditLimt = Double.parseDouble(cursor.getString(4));
                        str_climit = creditLimt - (sum_osa);

                        HashMap<String, String> map = new HashMap<String, String>();
                        // adding each child node to HashMap key => value
                        map.put("product_id", cursor.getString(cursor.getColumnIndex(KEY_TABLE_SERVICEORDER_DETAIL_PRODUCT_ID)));
                        map.put("service_order_detail_id", cursor.getString(cursor.getColumnIndex(KEY_TABLE_SERVICEORDER_DETAIL_MASTER_ID)));
                        map.put("customer_name", cursor.getString(0));
                        map.put("customer_code", cursor.getString(1));
                        map.put("customer_id", cursor.getString(3));
                        map.put("Division", "");
                        map.put("ABS", String.valueOf(so));
                        map.put("Crop name", "");
                        map.put("Product Name", "");
                        map.put("Order price", "");
                        map.put("Quantity", cursor.getString(9));
                        map.put("order_quantity", cursor.getString(cursor.getColumnIndex(KEY_TABLE_SERVICEORDER_DETAIL_ORDER_QUANTITY)));
                        map.put("OrderDate", cursor.getString(8));
                        map.put("count", String.valueOf(j));
                        map.put("price_per_unit", db.getPricePerUnitByProductId(cursor.getString(10), Common.getDefaultSP(getApplicationContext()).getString("region_id", "0")));
                        favouriteItem.add(map);
                        if (outside > 0) {
                            aging1 = ">";

                            aging2 = "90";
                            //color= "#ff0000";

                        } else {
                            aging1 = "<";
                            aging2 = "90";
                            // color="#008000";
                        }
                    }


                    String orders = String.valueOf(favouriteItem.size());
                } while (cursor.moveToNext());

            } else Log.d("LOG", "returned null!");


            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            tv_amount.setText("" + String.valueOf(sum_so));
            tv_credit.setText(String.valueOf(str_climit));
            if (sum_osa > 0) {
                tv_osa.setText(String.valueOf(sum_osa));
            } else {
                tv_osa.setText("0");
            }
            tv_division.setText("" + str_division_name);
            if (aging1 != null && aging1.equalsIgnoreCase("<")) {
                iv_ageing.setBackground(getResources().getDrawable(R.drawable.less));
            } else if (aging1 != null && aging1.equalsIgnoreCase(">")) {
                iv_ageing.setBackground(getResources().getDrawable(R.drawable.greater));
            }
            adapter.updateResults(favouriteItem);
        }
    }

    /*class ItemfavitemAdapter extends BaseAdapter {

        Context context;
        ArrayList<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();


        public ItemfavitemAdapter(Context context, ArrayList<HashMap<String, String>> results) {
            this.context = context;
            this.results = results;

        }

        @Override
        public int getCount() {
            return results.size();
        }

        @Override
        public Object getItem(int position) {
            return results;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ItemfavitemAdapter.ViewHolder holder = new ItemfavitemAdapter.ViewHolder();
            if (convertView == null) {
                sharedpreferences = context.getSharedPreferences(mypreference, Context.MODE_PRIVATE);
                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.listheader_salesorder, parent, false);
                holder.itemname = (TextView) convertView.findViewById(R.id.tv_product);
                holder.company_code = (TextView) convertView.findViewById(R.id.tv_schemedetails);
                holder.scheme = (TextView) convertView.findViewById(R.id.tv_scheme);
                holder.tv_rbs_amount = (TextView) convertView.findViewById(R.id.tv_amount);
                holder.tv_itemcount = (TextView) convertView.findViewById(R.id.tv_sr_no);
                holder.tv_orderdate = (TextView) convertView.findViewById(R.id.textViewTitle);
                holder.rl_date = (RelativeLayout) convertView.findViewById(R.id.header);
                holder.tv_qunatity = (TextView) convertView.findViewById(R.id.tv_qunatity);
                holder.et_quantity = (EditText) convertView.findViewById(R.id.et_qty);
                holder.tv_rate = (TextView) convertView.findViewById(R.id.tv_rate);
                convertView.setTag(holder);
            } else {
                holder = (ItemfavitemAdapter.ViewHolder) convertView.getTag();
            }
            //Glide.with(context).load(results.get(position).get("image")).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.pic);
            holder.itemname.setText(results.get(position).get("customer_name"));
            holder.company_code.setText(results.get(position).get("customer_code"));
            holder.tv_rbs_amount.setText(results.get(position).get("ABS"));
            holder.tv_qunatity.setText(results.get(position).get("Quantity"));
            holder.et_quantity.setText(results.get(position).get("Quantity"));
            holder.tv_rate.setText(results.get(position).get("price_per_unit"));

            String dateString = results.get(position).get("OrderDate");

            if (dateString != null && dateString.indexOf(" ") > -1) {
                dateString = dateString.split(" ")[0];
            }
            if (approval_status.equalsIgnoreCase("0")) {
                holder.tv_qunatity.setVisibility(View.GONE);
                holder.et_quantity.setVisibility(View.VISIBLE);
            } else {
                holder.tv_qunatity.setVisibility(View.VISIBLE);
                holder.et_quantity.setVisibility(View.GONE);
            }

            if (position == 0) {
                holder.rl_date.setVisibility(View.VISIBLE);
                holder.tv_orderdate.setText("S# DETAILS (" + dateString + ")");
            } else if (position < results.size() - 1 && dateString != null) {
                String prevDateString = results.get(position - 1).get("OrderDate");
                if (prevDateString != null && prevDateString.indexOf(" ") > -1) {
                    prevDateString = prevDateString.split(" ")[0];
                }
                if (!dateString.equals(prevDateString)) {
                    holder.rl_date.setVisibility(View.VISIBLE);
                    holder.tv_orderdate.setText("S# DETAILS (" + dateString + ")");
                } else {
                    holder.rl_date.setVisibility(View.GONE);
                }
            } else if (position == results.size() - 1 && dateString != null) {
                String prevDateString = results.get(position - 1).get("OrderDate");
                if (prevDateString != null && prevDateString.indexOf(" ") > -1) {
                    prevDateString = prevDateString.split(" ")[0];
                }
                if (!dateString.equals(prevDateString)) {
                    holder.rl_date.setVisibility(View.VISIBLE);
                    holder.tv_orderdate.setText("S# DETAILS (" + dateString + ")");
                } else {
                    holder.rl_date.setVisibility(View.GONE);
                }
            }

            holder.tv_itemcount.setText(results.get(position).get("count") + ".");
            holder.company_code.setVisibility(View.GONE);
            holder.scheme.setVisibility(View.GONE);
            return convertView;
        }


        public class ViewHolder {
            public TextView itemname, company_code, tv_rbs_amount, scheme, tv_itemcount, tv_orderdate, tv_qunatity, et_quantity, tv_rate;
            public RelativeLayout rl_date;

        }

        public void updateResults(ArrayList<HashMap<String, String>> results) {

            this.results = results;
            notifyDataSetChanged();
        }
    }*/


    class ItemfavitemAdapter extends RecyclerView.Adapter<ItemfavitemAdapter.ViewHolder> {

        Context context;


        public ItemfavitemAdapter(Context context, ArrayList<HashMap<String, String>> tResults) {
            this.context = context;
            results = tResults;

        }


        @NonNull
        @Override
        public ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            ViewHolder viewHolder = new ViewHolder(getLayoutInflater().inflate(R.layout.listheader_salesorder, null));
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull final ViewHolder holder, @SuppressLint("RecyclerView") final int position) {
            final double pricePerUnit = Double.parseDouble(results.get(position).get("price_per_unit"));
            holder.itemname.setText(results.get(position).get("customer_name"));
//            holder.company_code.setText(results.get(position).get("customer_code"));
            holder.tv_rbs_amount.setText(results.get(position).get("ABS"));
//            holder.order_qty.setText(results.get(position).get("order_quantity"));
            holder.tv_qunatity.setText(results.get(position).get("Quantity"));
//            holder.et_quantity.setText(results.get(position).get("Quantity"));
//            holder.tv_rate.setText(results.get(position).get("price_per_unit"));
            results.get(position).put("edit_flag", "0");

            String dateString = results.get(position).get("OrderDate");

            if (dateString != null && dateString.indexOf(" ") > -1) {
                dateString = dateString.split(" ")[0];
            }

            if (ll_accept_reject.getVisibility() == View.VISIBLE && approval_status != null && approval_status.equalsIgnoreCase("0")) {
                holder.editView.setVisibility(View.VISIBLE);
            } else {
                holder.editView.setVisibility(View.INVISIBLE);


            }
//            holder.historyInfo.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    Intent historyIntent = new Intent(ViewSalesOrderCustomerDetailsActivity.this, HistoryDetailsActivity.class);
//                    historyIntent.putExtra("order_detail_id", results.get(position).get("service_order_detail_id"));
//                    historyIntent.putExtra("type", "1");
//                    startActivity(historyIntent);
//                }
//            });
           /* holder.et_quantity.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    if (editable.toString().trim().length() > 0) {
                        int quantity = Integer.parseInt(editable.toString().trim());
                        if (quantity >= 0) {
                            double total = quantity * pricePerUnit;
                            holder.tv_rbs_amount.setText(String.valueOf(total));
                            results.get(position).put("edit_flag", "1");
                            results.get(position).put("Quantity", String.valueOf(quantity));
                            results.get(position).put("ABS", String.valueOf(total));
                        }
                    } else {
                        holder.tv_rbs_amount.setText("0");
                        results.get(position).put("Quantity", String.valueOf(0));
                        results.get(position).put("ABS", String.valueOf(0));
                    }
                    sum_so = 0;
                    for (int i = 0; i < results.size(); i++) {
                        sum_so += Double.parseDouble(results.get(i).get("ABS"));
                    }
                    tv_amount.setText(String.valueOf(sum_so));
                }
            });*/


            holder.editView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    final AlertDialog popup = new AlertDialog.Builder(ViewSalesOrderCustomerDetailsActivity.this).create();
                    View dialogView = getLayoutInflater().inflate(R.layout.product_layout, null);
                    popup.setView(dialogView);
                    popup.show();

                    ImageView closeView = dialogView.findViewById(R.id.close_view);
                    TextView productView = dialogView.findViewById(R.id.product_name);
                    EditText quantityEt = dialogView.findViewById(R.id.et_quantity);
                    TextView unitPrice = dialogView.findViewById(R.id.unit_price);
                    final TextView amountView = dialogView.findViewById(R.id.amount);
                    productView.setText(results.get(position).get("customer_name"));
                    amountView.setText(results.get(position).get("ABS"));
                    quantityEt.setText(results.get(position).get("Quantity"));
                    quantityEt.setSelection(results.get(position).get("Quantity").length());
                    unitPrice.setText(results.get(position).get("price_per_unit"));

                    closeView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            popup.dismiss();
                        }
                    });
                    quantityEt.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            if (editable.toString().trim().length() > 0) {
                                int quantity = Integer.parseInt(editable.toString().trim());
                                if (quantity >= 0) {
                                    double total = quantity * pricePerUnit;
                                    holder.tv_rbs_amount.setText(String.valueOf(total));
                                    amountView.setText(String.valueOf(total));
                                    holder.tv_qunatity.setText(String.valueOf(quantity));
                                    results.get(position).put("edit_flag", "1");
                                    results.get(position).put("Quantity", String.valueOf(quantity));
                                    results.get(position).put("ABS", String.valueOf(total));
                                }
                            } else {
                                holder.tv_rbs_amount.setText("0");
                                amountView.setText("0");
                                holder.tv_qunatity.setText(String.valueOf(0));
                                results.get(position).put("Quantity", String.valueOf(0));
                                results.get(position).put("ABS", String.valueOf(0));
                            }
                            sum_so = 0;
                            for (int i = 0; i < results.size(); i++) {
                                sum_so += Double.parseDouble(results.get(i).get("ABS"));
                            }
                            tv_amount.setText(String.valueOf(sum_so));

                        }
                    });

                }
            });
            if (position == 0) {
                holder.rl_date.setVisibility(View.VISIBLE);
                holder.tv_orderdate.setText(dateString);
            } else if (position < results.size() - 1 && dateString != null) {
                String prevDateString = results.get(position - 1).get("OrderDate");
                if (prevDateString != null && prevDateString.indexOf(" ") > -1) {
                    prevDateString = prevDateString.split(" ")[0];
                }
                if (!dateString.equals(prevDateString)) {
                    holder.rl_date.setVisibility(View.VISIBLE);
                    holder.tv_orderdate.setText(dateString);
                } else {
                    holder.rl_date.setVisibility(View.GONE);
                }
            } else if (position == results.size() - 1 && dateString != null) {
                String prevDateString = results.get(position - 1).get("OrderDate");
                if (prevDateString != null && prevDateString.indexOf(" ") > -1) {
                    prevDateString = prevDateString.split(" ")[0];
                }
                if (!dateString.equals(prevDateString)) {
                    holder.rl_date.setVisibility(View.VISIBLE);
                    holder.tv_orderdate.setText(dateString);
                } else {
                    holder.rl_date.setVisibility(View.GONE);
                }
            }

            holder.tv_itemcount.setText(results.get(position).get("count") + ".");
//            holder.company_code.setVisibility(View.GONE);
//            holder.scheme.setVisibility(View.GONE);
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public int getItemCount() {
            return results.size();
        }


        class ViewHolder extends RecyclerView.ViewHolder {
            public TextView historyInfo, itemname, /*order_qty,*/
                    company_code, tv_rbs_amount, scheme, tv_itemcount, tv_orderdate, tv_qunatity/*, et_quantity, tv_rate*/;
            public RelativeLayout rl_date;
            public ImageView editView;

            public ViewHolder(View convertView) {
                super(convertView);
                itemname = (TextView) convertView.findViewById(R.id.tv_product);
//                historyInfo = convertView.findViewById(R.id.history_info);
//                company_code = (TextView) convertView.findViewById(R.id.tv_schemedetails);
//                scheme = (TextView) convertView.findViewById(R.id.tv_scheme);
                tv_rbs_amount = (TextView) convertView.findViewById(R.id.tv_amount);
                tv_itemcount = (TextView) convertView.findViewById(R.id.tv_sr_no);
                tv_orderdate = (TextView) convertView.findViewById(R.id.textViewTitle);
                rl_date = (RelativeLayout) convertView.findViewById(R.id.header);
                editView = convertView.findViewById(R.id.edit_view);
//                order_qty = convertView.findViewById(R.id.order_qty);
                tv_qunatity = (TextView) convertView.findViewById(R.id.tv_qunatity);
//                et_quantity = (EditText) convertView.findViewById(R.id.et_qty);
//                tv_rate = (TextView) convertView.findViewById(R.id.tv_rate);
            }
        }

        public void updateResults(ArrayList<HashMap<String, String>> tResults) {

            results = tResults;
            notifyDataSetChanged();
        }
    }

    class Async_getalloffline extends AsyncTask<Void, Void, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ViewSalesOrderCustomerDetailsActivity.this);
            progressDialog.setMessage("Please wait");
            progressDialog.show();
        }

        protected String doInBackground(Void... params) {


            try {

                String selectQuery = "SELECT D." + KEY_TABLE_DIVISION_MASTER_ID + "," + KEY_TABLE_DIVISION_NAME + "," + KEY_TABLE_CUSTOMER_DETAILS_INSIDE_BUCKET + "," + KEY_TABLE_CUSTOMER_DETAILS_OUTSIDE_BUCKET + "," + KEY_TABLE_CUSTOMER_DETAILS_CREDIT_LIMIT + " FROM " + TABLE_CUSTOMERS + " AS C LEFT JOIN " + TABLE_CUSTOMER_DETAILS + " AS CD ON CD." + KEY_TABLE_CUSTOMER_DETAILS_MASTER_ID + " = C." + KEY_TABLE_CUSTOMER_MASTER_ID + " LEFT JOIN " + TABLE_DIVISION + " AS D ON  D." + KEY_TABLE_DIVISION_MASTER_ID + " = CD.Division_id WHERE C." + KEY_TABLE_CUSTOMER_MASTER_ID + " = " + sharedpreferences.getString("customer_id", null) + " and " + "D." + KEY_TABLE_DIVISION_MASTER_ID + " = " + divison_id + " group by C." + KEY_TABLE_CUSTOMER_MASTER_ID + ",D." + KEY_TABLE_DIVISION_MASTER_ID;
                System.out.println(selectQuery);


                // String selectQuery = "SELECT  " + "CR." + KEY_TABLE_CUSTOMER_MASTER_ID + ","+ KEY_TABLE_CUSTOMER_NAME+ ","+ KEY_TABLE_CUSTOMER_CODE + " FROM " + TABLE_USER_COMPANY_CUSTOMER+ " AS CDC JOIN " + TABLE_CUSTOMERS + " AS CR ON CDC."+KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID + " = CR."+ KEY_TABLE_CUSTOMER_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_DIVISION_USER_ID + " = " + userId + " group by(CR." + KEY_TABLE_COMPANIES_MASTER_ID + ")" ;
                sdbw = db.getWritableDatabase();

                Cursor cursor = sdbw.rawQuery(selectQuery, null);
                //System.out.println("cursor count "+cursor.getCount());
                if (cursor.moveToFirst()) {
                    do {
                        i = i + 1;
                        /*Customers customers = new Customers();

                        customers.setCusMasterID(cursor.getString(0));
                        customers.setCusName(cursor.getString(1));
                        customers.setCuscode(cursor.getString(2));*/

                        System.out.println("******----" + cursor.getString(0) + cursor.getString(1) + cursor.getString(2) + cursor.getString(3));
                        Log.d("div id", cursor.getString(0));
                        Log.d("div name ", cursor.getString(1));
                        Log.d("inside bucket", cursor.getString(2));
                        Log.d("outside bucket", cursor.getString(3));

                        float inside = Float.parseFloat(cursor.getString(2));
                        float outside = Float.parseFloat(cursor.getString(3));
                        sum_osa = inside + outside;
                        if (outside > 0) {
                            aging1 = ">";

                            aging2 = "90";
                            //color= "#ff0000";

                        } else if (outside == 0) {
                            aging1 = "<";
                            aging2 = "90";
                            // color="#008000";
                        }

                    } while (cursor.moveToNext());
                    //
                }

                // do some stuff....
            } catch (Exception e) {
                e.printStackTrace();
            }

            return jsonData;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            //adapter.updateResults(favouriteItem);
            //  tv_osa.setText(String.valueOf(sum_osa));

           /* if (aging1 != null && aging1.equalsIgnoreCase("<")) {
                iv_ageing.setBackground(getResources().getDrawable(R.drawable.less));
            } else if (aging1 != null && aging1.equalsIgnoreCase(">")) {
                iv_ageing.setBackground(getResources().getDrawable(R.drawable.greater));
            }*/
        }
    }

    private void showCommentsDialog(final String order_id, final View button_view, final int i) {

        /*if (Double.valueOf(tv_osa.getText().toString()) > 0&&i==1) {
            open();
        }
          else {*/
        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Transport Name");
        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_reject_dialog_withdropdown, null);
        alert.setView(dialogView);

        final Spinner editText = (Spinner) dialogView.findViewById(R.id.edit_comments);
        final RelativeLayout edit_comments_ll = (RelativeLayout) dialogView.findViewById(R.id.edit_comments_ll);
        final EditText editText2 = (EditText) dialogView.findViewById(R.id.edit_comments2);
        final View view = (View) dialogView.findViewById(R.id.vieww);
        array_comments.clear();
        array_comments.add("select comment");
        new Async_getallcomments().execute();
        progressDialog = new ProgressDialog(ViewSalesOrderCustomerDetailsActivity.this);
        progressDialog.setMessage("Please wait ...");
        progressDialog.setCancelable(false);
        progressDialog.show();
        editText.setAdapter(new ArrayAdapter<String>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, array_comments));
        if (i == 1) {
            editText.setVisibility(View.GONE);
            edit_comments_ll.setVisibility(View.GONE);
            view.setVisibility(View.GONE);
            editText2.setVisibility(View.VISIBLE);
        }
        if (i == 2) {
            editText.setVisibility(View.VISIBLE);
            edit_comments_ll.setVisibility(View.VISIBLE);
            view.setVisibility(View.VISIBLE);
            editText2.setVisibility(View.GONE);
        }

        editText.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (editText.getSelectedItem().toString().equals("Others")) {
                    editText2.setVisibility(View.VISIBLE);

                } else {
                    editText2.setVisibility(View.GONE);
                }

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
            }
        });


        final Button buttonOk = (Button) dialogView.findViewById(R.id.btn_ok);
        final Button buttonCancel = (Button) dialogView.findViewById(R.id.btn_cancel);
        final AlertDialog alertDialog = alert.show();
        final RejectionCommentVo rejectionCommentVo = new RejectionCommentVo();
        // array_comments.add("uaidfaj");

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String comment = null;
                String check = "no";
                if (i == 1) {
                    if (!editText2.getText().toString().equals("") && editText2.getText().toString() != null) {
                        comment = editText2.getText().toString();
                        check = "yes";
                        alertDialog.dismiss();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please enter comment", Toast.LENGTH_LONG).show();

                    }
                }
                if (i == 2) {
                    if (editText.getSelectedItem().toString().equals("Others")) {
                        if (!editText2.getText().toString().equals("") && editText2.getText().toString() != null) {
                            comment = editText2.getText().toString();
                            check = "yes";
                            alertDialog.dismiss();
                        } else {
                            Toast.makeText(getApplicationContext(), "Please enter comment", Toast.LENGTH_LONG).show();

                        }
                    } else if (!editText.getSelectedItem().toString().equals("select comment")) {
                        comment = editText.getSelectedItem().toString();
                        check = "yes";
                        alertDialog.dismiss();
                    } else {
                        Toast.makeText(getApplicationContext(), "Please enter comment", Toast.LENGTH_LONG).show();

                    }
                }

                if (Utility.isNetworkAvailable(ViewSalesOrderCustomerDetailsActivity.this, true) && check.equals("yes")) {
                    JSONObject mainObject = new JSONObject();
                    try {
                        mainObject.put("approval_status", String.valueOf(i));
                        mainObject.put("updated_by", Common.getUserIdFromSP(getApplicationContext()));
                        mainObject.put("comments", comment);
                        mainObject.put("order_id", order_id);
                        JSONArray itemsArray = new JSONArray();
                        for (int i = 0; i < results.size(); i++) {
                            JSONObject jsonObject = new JSONObject();
                            jsonObject.put("order_id", order_id);
                            jsonObject.put("service_order_detail_id", results.get(i).get(KEY_TABLE_SERVICEORDER_DETAIL_MASTER_ID));
                            jsonObject.put("product_id", results.get(i).get(KEY_TABLE_SERVICEORDER_DETAIL_PRODUCT_ID));
                            String quantity = results.get(i).get("Quantity");
                            jsonObject.put("quantity", quantity.trim().length() == 0 ? "0" : quantity.trim());
                            jsonObject.put("order_price", results.get(i).get("ABS"));
                            jsonObject.put("advance_amount", "");
                            jsonObject.put("edit_flag", results.get(i).get("edit_flag"));
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
                    Toast.makeText(getApplicationContext(), "Please enter comment", Toast.LENGTH_LONG).show();
                    if (array_comments.size() == 1 && i == 2) {
                        Toast.makeText(getApplicationContext(), "Please check internet connection speed", Toast.LENGTH_LONG).show();
                    }
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


    //}


    }


    private class ApproveOrRejectAsyncTask extends AsyncTask<Object, Void, String> {
        View imageView;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ViewSalesOrderCustomerDetailsActivity.this);
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
                            soh.orderApprovalId = historyObj.getString("service_order_approval_id");
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
                        setButtonview(String.valueOf(approval_status_send),service_id);
                        adapter = new ItemfavitemAdapter(getApplicationContext(), favouriteItem);
                        listView.setAdapter(adapter);
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

    private class Async_getallcomments extends AsyncTask<Void, Void, String> {

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
                        .url(Constants.URL_NSL_MAIN + Constants.URL_MASTER_ORDERINTENT_APPROVE_REJECT_CMTS)
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
                    for (int n = 0; n < companyarray.length(); n++) {
                        JSONObject objinfo = companyarray.getJSONObject(n);
                        //  String region_id = objinfo.getString("region_id");
                        //  String region_name = objinfo.getString("region_name");
                        // String region_code = objinfo.getString("region_code");
                        String comment_name = objinfo.getString("comment_name");
                        array_comments.add(comment_name);
                        // Log.d("Insert: ", "Inserting Regions ..");
                        //db.addRegions(new Regions(region_id, region_name, region_code, status));
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
            }

           /* Log.d("Reading: ", "Reading all Regions..");

            List<Regions> regions = db.getAllRegions();

            for (Regions regions1 : regions) {
                String log = "Id: "+regions1.getID()+ "Region master ID"+regions1.getRegionMasterId()+" ,Name: " + regions1.getRegionName()+ " ,region code: " + regions1.getRegioncode() + regions1.getRegionstatus();
                // Writing Contacts to log
                Log.e("Regions: ", log);

            }*/
            try {
                ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext()
                        .getSystemService(Context.CONNECTIVITY_SERVICE);
                NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
                if (netInfo != null) {  // connected to the internet
                    if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                        // connected to wifi
                        // Toast.makeText(getActivity(), netInfo.getTypeName(), Toast.LENGTH_SHORT).show();
                        // new LoginActivity.Async_getallDistricts().execute();
                    } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                        // connected to the mobile provider's data plan
                        //new LoginActivity.Async_getallDistricts().execute();
                    }
                } else {
                    Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                }
            } catch (Exception e) {
                Log.d("Tag", e.toString());
            }
            progressDialog.dismiss();
        }
        }
    public void open(){
        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(ViewSalesOrderCustomerDetailsActivity.this);

        // set title
        alertDialogBuilder.setTitle("Outstanding of "+tv_osa.getText().toString());

        // set dialog message
        alertDialogBuilder
                .setMessage("Your con't approve this order because of outstanding amount, please reject this order and check customer outstanding!")
                .setCancelable(false)
                .setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {
                    // finish();


                    }
                });


        // create alert dialog
        AlertDialog alertDialog = alertDialogBuilder.create();

        // show it
        alertDialog.show();

    }
}
