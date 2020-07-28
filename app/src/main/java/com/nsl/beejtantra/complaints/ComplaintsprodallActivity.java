package com.nsl.beejtantra.complaints;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nsl.beejtantra.Companies;
import com.nsl.beejtantra.Constants;
import com.nsl.beejtantra.Crops;
import com.nsl.beejtantra.Customers;
import com.nsl.beejtantra.DatabaseHandler;
import com.nsl.beejtantra.Divisions;
import com.nsl.beejtantra.MainActivity;
import com.nsl.beejtantra.MainProductActivity;
import com.nsl.beejtantra.Products_Pojo;
import com.nsl.beejtantra.R;
import com.nsl.beejtantra.Utility;
import com.nsl.beejtantra.commonutils.Common;
import com.nsl.beejtantra.network.BasicAuthInterceptor;
import com.nsl.beejtantra.network.RetrofitAPI;
import com.nsl.beejtantra.pojo.ComplaintReq;
import com.nsl.beejtantra.pojo.RejectionCommentVo;
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
import java.util.concurrent.TimeUnit;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.nsl.beejtantra.DatabaseHandler.KEY_PRODUCT_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_PRODUCT_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_COMPANIES_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_COMPANIES_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_COMPLAINTS_COMMENTS;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_COMPLAINTS_FFMID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_COMPLAINTS_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_COMPLAINTS_IMAGE_UPLOAD;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_COMPLAINTS_REMARKS;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_COMPLAINTS_STAGES;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_COMPLAINTS_STATUS;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CROPS_CROP_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CROPS_CROP_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CUSTOMER_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CUSTOMER_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_DIVISION_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_DIVISION_NAME;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_COMPANIES;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_COMPLAINT;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_CROPS;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_CUSTOMERS;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_DIVISION;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_PRODUCTS;


public class ComplaintsprodallActivity extends AppCompatActivity {

    private static final int FROM_COMPLAINTS_PRODUCT_ACTIVITY = 5;
    private JSONObject JSONObj;
    private JSONArray JSONArr = null;
    ProgressDialog progressDialog;
    DatabaseHandler db;
    SQLiteDatabase sdbw, sdbr;
    int i = 0, sum_osa = 0, flag = 0;
    private ListView listView;
    private ItemfavitemAdapter adapter;
    String customer_id, comp_id, status1;

    String remarks, crop_id, product_id, marketing_lot_number, complaint_type, farmer_name, contact_no, complaint_area_acres, soil_type, others, purchased_quantity, complain_quantity;
    String bill_number, purchase_date, retailer_name, distributor, mandal, village, image_upload, regulatory_type, sampling_date, place_sampling, sampling_officer_name, sampling_officer_contact, comments;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    String color, fdbk, ffmidsqlite;
    int status;
    TextView empty;
    Button refresh;
    String jsonData, user, userId, id, payment_type, company_id, division_id, created_datetime, updated_datetime, ffmid;
    String aging1 = null, aging2 = null, companyname, divisionname, cropname, productname, distname;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    TextView tv_name, tv_code, tv_total_osa_amt;
    String companyId, divisionId;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_complaints_prod);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        userId = sharedpreferences.getString("userId", "");
        companyId = getIntent().getStringExtra("company_id");
        divisionId = getIntent().getStringExtra("division_id");

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent newfedback = new Intent(getApplicationContext(), MainProductActivity.class);
                newfedback.putExtra("company_id", companyId);
                newfedback.putExtra("division_id", divisionId);
                startActivityForResult(newfedback, FROM_COMPLAINTS_PRODUCT_ACTIVITY);

            }
        });
        refresh = (Button) findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ComplaintsprodallActivity.this, ComplaintsprodallActivity.class);
                startActivity(i);
            }
        });

        db = new DatabaseHandler(ComplaintsprodallActivity.this);

      /*  FragmentFeedback fragm = new FragmentFeedback();
        fragm.checkConnection();*/
        listView = (ListView) findViewById(R.id.listViewproducts);
        adapter = new ItemfavitemAdapter(ComplaintsprodallActivity.this, favouriteItem);
        //adapter1 = new ItemfavitemAdapter(ComplaintsallActivity.this, favouriteItem1);
        listView.setAdapter(adapter);
        listView.setEmptyView(findViewById(R.id.empty));
        empty = (TextView) findViewById(R.id.empty);
        // db.deleteComplaints();

        if (Common.haveInternet(this))
            new Async_getServerComplaints().execute();
        else
            new Async_getAllComplaints().execute();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent booking = new Intent(getApplicationContext(), AllproductsActivity.class);

                booking.putExtra("details", "PRODUCT");

                booking.putExtra("comp_name", favouriteItem.get(i).get("comp_name"));
                booking.putExtra("div_name", favouriteItem.get(i).get("div_name"));
                booking.putExtra("crop_name", favouriteItem.get(i).get("crop_name"));
                booking.putExtra("prod_name", favouriteItem.get(i).get("prod_name"));
                booking.putExtra("dist_name", favouriteItem.get(i).get("dist_name"));

                booking.putExtra("mkt_lot", favouriteItem.get(i).get("mkt_lot"));
                booking.putExtra("farmer_name", favouriteItem.get(i).get("farmer_name"));
                booking.putExtra("contact", favouriteItem.get(i).get("contact"));
                booking.putExtra("compl_area", favouriteItem.get(i).get("compl_area"));

                booking.putExtra("soil_type", favouriteItem.get(i).get("soil_type"));
                booking.putExtra("others", favouriteItem.get(i).get("others"));
                booking.putExtra("purchase_qty", favouriteItem.get(i).get("purchase_qty"));
                booking.putExtra("complaint_qty", favouriteItem.get(i).get("complaint_qty"));
                booking.putExtra("purchase_date", favouriteItem.get(i).get("purchase_date"));
                booking.putExtra("billno", favouriteItem.get(i).get("billno"));
                booking.putExtra("retailer", favouriteItem.get(i).get("retailer"));

                booking.putExtra("mandal", favouriteItem.get(i).get("mandal"));
                booking.putExtra("village", favouriteItem.get(i).get("village"));
                booking.putExtra("comments", favouriteItem.get(i).get("comments"));
                booking.putExtra("status", favouriteItem.get(i).get("status"));
                booking.putExtra("remarks", favouriteItem.get(i).get("remarks"));
                booking.putExtra("complaint_type", favouriteItem.get(i).get("complaint_type"));
                booking.putExtra("image", favouriteItem.get(i).get("image"));
                booking.putExtra("comp_per", favouriteItem.get(i).get("comp_per"));
                booking.putExtra("comp_rem", favouriteItem.get(i).get("comp_rem"));
                booking.putExtra("zone", favouriteItem.get(i).get("zone"));
                booking.putExtra("dealer", favouriteItem.get(i).get("dealer"));
                booking.putExtra("region", favouriteItem.get(i).get("region"));
                booking.putExtra("per_lot", favouriteItem.get(i).get("per_lot"));
                booking.putExtra("state", favouriteItem.get(i).get("state"));
                booking.putExtra("date_of_sowing", favouriteItem.get(i).get("date_of_sowing"));
                booking.putExtra("insp_date", favouriteItem.get(i).get("insp_date"));
                booking.putExtra("date_of_comp", favouriteItem.get(i).get("date_of_comp"));
                booking.putExtra("district", favouriteItem.get(i).get("district"));

                startActivity(booking);


            }
        });

        // new Async_getalloffline().execute();


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

    class ItemfavitemAdapter extends BaseAdapter {

        Context context;
        ArrayList<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();


        public ItemfavitemAdapter(ComplaintsprodallActivity pd, ArrayList<HashMap<String, String>> results) {
            this.context = pd;
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
            ViewHolder holder = new ViewHolder();
            if (convertView == null) {
                sharedpreferences = context.getSharedPreferences(mypreference, Context.MODE_PRIVATE);

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.row_complaints, parent, false);
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tv_place = (TextView) convertView.findViewById(R.id.tv_place);
                holder.tv_comptype = (TextView) convertView.findViewById(R.id.tv_complainttype);
                holder.tv_billno = (TextView) convertView.findViewById(R.id.tv_billno);
                holder.tv_retailer = (TextView) convertView.findViewById(R.id.tv_retailername);//farmer here
                holder.status_icon = (ImageView) convertView.findViewById(R.id.status_icon);
                holder.tv_status_value = (TextView) convertView.findViewById(R.id.statusval);
                holder.tv_status = convertView.findViewById(R.id.tv_status);
                holder.tv_stage = convertView.findViewById(R.id.tv_stage);
                holder.tv_date = convertView.findViewById(R.id.tv_date);
                holder.tv_comments = convertView.findViewById(R.id.tv_comments);
                holder.tv_stage_label = convertView.findViewById(R.id.stage_label);
                holder.ivReject = convertView.findViewById(R.id.tv_reject);
                holder.tvAccept = convertView.findViewById(R.id.tv_accept);
                holder.tvApprovalStatus = convertView.findViewById(R.id.tv_approvalstatus);
                holder.acceptRejectLayout = convertView.findViewById(R.id.ll_accept_reject);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            //Glide.with(context).load(results.get(position).get("image")).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.pic);
            int role = sharedpreferences.getInt(Constants.SharedPrefrancesKey.ROLE, 0);
            int complaintStatus = Integer.parseInt(Common.isStringNulltoInt(results.get(position).get("status")));
            if (role == Constants.Roles.ROLE_5 && complaintStatus == Constants.ComplaintStatus.OPEN)
                holder.acceptRejectLayout.setVisibility(View.VISIBLE);
            else
                holder.acceptRejectLayout.setVisibility(View.GONE);
            holder.tv_comptype.setText(results.get(position).get("complaint_type"));
            holder.tv_status_value.setText(results.get(position).get("status"));
            status1 = results.get(position).get("status");
            holder.tv_retailer.setText(results.get(position).get("farmer_name"));
            holder.tv_date.setText(Common.setDateFormateOnTxt(results.get(position).get("date_of_comp")));
            holder.tv_comments.setText(results.get(position).get("comments"));
            holder.tv_status.setText(Common.getStatus(Integer.parseInt(Common.isStringNulltoInt(results.get(position).get("status")))));
            if (Common.getStages(Integer.parseInt(Common.isStringNulltoInt(results.get(position).get("stages")))).equalsIgnoreCase("")) {
                holder.tv_stage_label.setVisibility(View.GONE);
                holder.tv_stage.setVisibility(View.GONE);
            } else {
                holder.tv_stage.setText(Common.getStages(Integer.parseInt(Common.isStringNulltoInt(results.get(position).get("stages")))));
                holder.tv_stage_label.setVisibility(View.VISIBLE);
                holder.tv_stage.setVisibility(View.VISIBLE);
            }
            holder.tv_place.setVisibility(View.GONE);
            status = Integer.parseInt(status1);
            if (status == 1) {
                holder.status_icon.setImageResource(R.drawable.icon4);
            } else if (status == 2) {
                holder.status_icon.setImageResource(R.drawable.icon5);
            } else if (status == 3) {
                holder.status_icon.setImageResource(R.drawable.icon6);
            }
            fdbk = results.get(position).get("comments");
            int len = fdbk.length();
            if (len > 60) {
                holder.tv_comments.setText(fdbk.substring(0, 60) + "...");
            } else
                holder.tv_comments.setText(fdbk);
            holder.tv_place.setText(fdbk);  //full complaint

            holder.tvAccept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showCommentsDialog(1, results.get(position).get("ffm_id"));
                }
            });

            holder.ivReject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showCommentsDialog(2, results.get(position).get("ffm_id"));
                }
            });

            return convertView;
        }


        public class ViewHolder {
            public TextView tv_name, tv_place, tv_comptype, tv_billno, tv_retailer, tv_status, tv_stage_label, tv_comments, tv_date, tv_stage, tvAccept, tvApprovalStatus;
            public ImageView img, ivReject;
            public ImageView status_icon;
            public TextView tv_status_value;
            public LinearLayout acceptRejectLayout;

        }

        public void updateResults(ArrayList<HashMap<String, String>> results) {

            this.results = results;
            notifyDataSetChanged();
        }
    }

    private void showCommentsDialog(final int reqStatus, final String ffm_id) {

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
                String comment = editText.getText().toString().trim();
                if (Utility.isNetworkAvailable(ComplaintsprodallActivity.this, true)) {
                    List<Complaints> complaints = db.getAllComplaintsproducts(Common.getTeamFromSP(ComplaintsprodallActivity.this));
                    Log.e("list size", String.valueOf(complaints.size()));
                    if (comment.length() == 0) {
                        Toast.makeText(ComplaintsprodallActivity.this, "Please Enter Comments", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (complaints.size() > 0) {
                        prepareComplainprodOfflineDataAndPush(complaints, comment, ffm_id, reqStatus);
                    } else {

                        doApproveOrReject(comment, ffm_id, reqStatus);

                    }
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

    private void doApproveOrReject(String comment, String ffm_id, int status) {
        JSONObject mainObject = new JSONObject();
        try {
            mainObject.put("ffm_id", ffm_id);
            mainObject.put("approval_status", String.valueOf(status));
            mainObject.put("user_id", Common.getUserIdFromSP(getApplicationContext()));
            mainObject.put("comments", comment);
            Common.Log.i("COMPLAINT APPROVE " + mainObject.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
        new ApproveOrRejectAsyncTask().execute(mainObject.toString());
    }

    private class ApproveOrRejectAsyncTask extends AsyncTask<Object, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ComplaintsprodallActivity.this);
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
                Request request = new Request.Builder()
                        .url(Constants.URL_COMPLAINT_APPROVAL)
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
                    Common.Log.i("COMPLAINT APPROVE RESPONSE " + jsonData);
                    if (responseObject.get("status").equals("success")) {
                        // showDialog((String) responseObject.get("msg"));
                        String comments = responseObject.getString("comments");
                        int complaint_status = responseObject.getInt("complaint_status");
                        int stages = responseObject.getInt("stages");
                        String ffm_id = responseObject.getString("ffm_id");
                        String query = "UPDATE " + TABLE_COMPLAINT + " SET " + KEY_TABLE_COMPLAINTS_STATUS + " = " + complaint_status + " , " + KEY_TABLE_COMPLAINTS_STAGES + " = " + stages + " , " + KEY_TABLE_COMPLAINTS_COMMENTS + " = '" + comments + "' , " + KEY_TABLE_COMPLAINTS_REMARKS + " = '" + comments + "' WHERE " + KEY_TABLE_COMPLAINTS_FFMID + " = '" + ffm_id + "'";
                        Common.Log.i("UPDATE APPROVE QUERY " + query);
                        db.getWritableDatabase().execSQL(query);
                        new Async_getAllComplaints().execute();
                    } else {
                        Toast.makeText(ComplaintsprodallActivity.this, responseObject.getString("msg"), Toast.LENGTH_SHORT).show();
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

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode) {
            case FROM_COMPLAINTS_PRODUCT_ACTIVITY:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        this.recreate();
                        break;
                }

                break;
        }
    }

    private void prepareComplainprodOfflineDataAndPush(List<Complaints> complaints, String comment, String ffm_id, int reqStatus) {

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
                complaintReq.distributor = String.valueOf(cm.get_distributor());
                complaintReq.mandal = cm.get_mandal();
                complaintReq.village = cm.get_village();
                complaintReq.comments = cm.get_comments();
                complaintReq.userId = String.valueOf(cm.get_user_id());
                complaintReq.complaintType = cm.get_complaint_type();
                complaintReq.complaintPercentage = cm.getComplaintPercentage();
                complaintReq.complaintRemarks = cm.getComplaintRemarks();
                complaintReq.zone = cm.getZone();
                complaintReq.region = cm.getRegion();
                complaintReq.dealer = cm.getDealer();
                complaintReq.performanceType = cm.getPerformanceLot();
                complaintReq.state = cm.getState();
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
                sendImage(complaintReq, files);
            }
            doApproveOrReject(comment, ffm_id, reqStatus);
        }
        //pushFeedBackThread();

    }

    private void sendImage(ComplaintReq complaintReq, List<File> files) {
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


        Call<ResponseBody> responseBodyCall = apiInterface.uploadProfile("complaint", fileParts, complaintReq);
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

                                String sqliteid = jsonobject.getString("sqlite");
                                String ffmid = jsonobject.getString("ffm_id");
                                String image_url = jsonobject.getString("image_url");
                                int complaintStatus = jsonobject.getInt("status");
                                int stages = jsonobject.getInt("stages");
                                Log.e("sqlite id", sqliteid);
                                Log.e("ffmid", ffmid);
                                SQLiteDatabase sql = db.getWritableDatabase();
                                // updatecomplaints
                                String updatequery = "UPDATE " + TABLE_COMPLAINT + " SET " + KEY_TABLE_COMPLAINTS_FFMID + " = " + ffmid + " , " + KEY_TABLE_COMPLAINTS_STATUS + " = " + complaintStatus + " , " + KEY_TABLE_COMPLAINTS_STAGES + " = " + stages + " , " + KEY_TABLE_COMPLAINTS_IMAGE_UPLOAD + " = '" + image_url + "' WHERE " + KEY_TABLE_COMPLAINTS_ID + " = " + sqliteid;
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

    class Async_getServerComplaints extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(Void... params) {


            try {
                OkHttpClient client = new OkHttpClient();


                Response responses = null;


                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

                Request request = new Request.Builder()
                        .url(Constants.URL_NSL_MAIN + Constants.URL_MASTER_COMPLAINTS + "&team=" + Common.getTeamFromSP(ComplaintsprodallActivity.this) + "&days=60")
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
                        System.out.println(companyarray.length());
                        Log.e("Length complaint", String.valueOf(companyarray.length()));
                        db.deleteDataByTableName(db.TABLE_COMPLAINT);
                        for (int n = 0; n < companyarray.length(); n++) {
                            JSONObject objinfo = companyarray.getJSONObject(n);
                            if (!objinfo.has("error")) {


                                Complaints cmpreg = new Complaints();
                                cmpreg._complaint_id = Integer.parseInt(objinfo.getString("complaint_id"));
                                cmpreg._user_id = Integer.parseInt(objinfo.getString("user_id"));
                                cmpreg._company_id = Integer.parseInt(objinfo.getString("company_id"));
                                cmpreg._division_id = Integer.parseInt(objinfo.getString("division_id"));
                                cmpreg._crop_id = Integer.parseInt(objinfo.getString("crop_id"));
                                cmpreg._product_id = Integer.parseInt(objinfo.getString("product_id"));
                                cmpreg._marketing_lot_number = objinfo.getString("marketing_lot_number");
                                cmpreg._complaint_type = objinfo.getString("complaint_type");
                                cmpreg._farmer_name = objinfo.getString("farmer_name");
                                cmpreg._contact_no = objinfo.getString("contact_no");
                                cmpreg._complaint_area_acres = objinfo.getString("complaint_area_acres");
                                cmpreg._soil_type = objinfo.getString("soil_type");
                                cmpreg._others = objinfo.getString("others");
                                cmpreg._purchased_quantity = objinfo.getString("purchased_quantity");
                                cmpreg._complaint_quantity = objinfo.getString("complaint_quantity");
                                cmpreg._purchase_date = objinfo.getString("purchase_date");
                                cmpreg._bill_number = objinfo.getString("bill_number");
                                cmpreg._retailer_name = objinfo.getString("retailer_name");
                                cmpreg.retailerId = objinfo.getString("retailer_id");
                                cmpreg._distributor = Integer.parseInt(objinfo.getString("distributor"));
//                                cmpreg._mandal = objinfo.getString("mandal");
//                                cmpreg._village = objinfo.getString("village");
                                cmpreg._image = objinfo.getString("image");
                                cmpreg._regulatory_type = objinfo.getString("regulatory_type");
                                cmpreg._sampling_date = objinfo.getString("sampling_date");
                                cmpreg._place_sampling = objinfo.getString("place_sampling");
                                cmpreg._sampling_officer_name = objinfo.getString("sampling_officer_name");
                                cmpreg._sampling_officer_contact = objinfo.getString("sampling_officer_contact");
                                cmpreg._comments = objinfo.getString("comments");
                                cmpreg._status = Integer.parseInt(objinfo.getString("status"));
                                cmpreg._remarks = objinfo.getString("remarks");
                                cmpreg._created_datetime = objinfo.getString("created_datetime");
                                cmpreg._updated_datetime = objinfo.getString("updated_datetime");
                                cmpreg._ffmid = objinfo.getString("complaint_id");
                                cmpreg.complaintPercentage = objinfo.getString("complaint_percentage");
                                cmpreg.complaintRemarks = objinfo.getString("complaint_remarks");
//                                cmpreg.zone = objinfo.getString("zone");
//                                cmpreg.region = objinfo.getString("region");
//                                cmpreg.dealer = objinfo.getString("dealer");
                                cmpreg.performanceLot = objinfo.getString("performance_on_other_fields");
//                                cmpreg.state = objinfo.getString("state");
                                cmpreg.dateOfSowing = objinfo.getString("date_of_sowing");
                                cmpreg.inspectedDate = objinfo.getString("inspected_date");
                                cmpreg.dateOfComplaint = objinfo.getString("date_of_complaint");
//                                cmpreg.district = objinfo.getString("district");
                                cmpreg.stages = objinfo.getInt("stages");

                                Log.d("Insert: ", "Inserting complaints ..");
//                                db.addComplaint(new Complaints(cmpreg._user_id, cmpreg._company_id, cmpreg._division_id, cmpreg._crop_id, cmpreg._product_id, cmpreg._marketing_lot_number, cmpreg._complaint_type, cmpreg._farmer_name,
//                                        cmpreg._contact_no, cmpreg._complaint_area_acres, cmpreg._soil_type, cmpreg._others, cmpreg._purchased_quantity, cmpreg._complaint_quantity, cmpreg._purchase_date, cmpreg._bill_number,
//                                        cmpreg._retailer_name, cmpreg._distributor, cmpreg._mandal, cmpreg._village, cmpreg._image, cmpreg._regulatory_type, cmpreg._sampling_date, cmpreg._place_sampling, cmpreg._sampling_officer_name,
//                                        cmpreg._sampling_officer_contact, cmpreg._comments, cmpreg._status, cmpreg._remarks, cmpreg._created_datetime, cmpreg._updated_datetime, cmpreg._ffmid));

                                db.addComplaint(cmpreg);

                            }
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } else {
                    Log.e("ServiceHandler", "Couldn't get any data from the url");
                    //Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                    // db.deleteComplaints();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            new Async_getAllComplaints().execute();
           /* Common.dismissDialog(dataSyncingDialog);
            Intent main = new Intent(LoginActivity.this, MainActivity.class);
            startActivity(main);
            finish();*/


        }

    }

    class Async_getAllComplaints extends AsyncTask<Void, Void, Void> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ComplaintsprodallActivity.this);
            progressDialog.setMessage("Please wait ...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected Void doInBackground(Void... voids) {
            try {
                favouriteItem.clear();

                Log.d("Reading: ", "Reading all complaints from server..");
                List<Complaints> complaints = db.getAllComplaints(Common.getTeamFromSP(ComplaintsprodallActivity.this), companyId, divisionId);

                for (Complaints cm : complaints) {
                    String log = "Id: " + cm.getID() + "Userid: " + cm.get_user_id() + " ,company: " + cm.getCompanyId() + " ,div: " + cm.get_division_id() + ",crop:" + cm.getCropid() + " ,product: " + cm.getProductid() + ",mkt_lot:" +
                            cm.get_marketing_lot_number() + ",comptype" + cm.get_complaint_type() + ",farmer:" + cm.get_farmer_name() + ",contact" + cm.get_contact_no() + ",complarea" + cm.get_complaint_area_acres()
                            + ",soiltype" + cm.get_soil_type() + ",others" + cm.get_others() + ",purqty" + cm.get_purchased_quantity() + ",cmpqty" + cm.get_complaint_quantity() + ",purdate" + cm.get_purchase_date() + ",billnum" + cm.get_bill_number()
                            + ",retlrname" + cm.get_retailer_name() + ",distributor" + cm.get_distributor() + ",mandal" + cm.get_mandal() + ",village" + cm.get_village() + "image:" + cm.get_image() + ",regtype" + cm.get_regulatory_type() + ",samplingdate" + cm.get_sampling_date()
                            + ",samplingplace" + cm.get_place_sampling() + ",ofcrname" + cm.get_sampling_officer_name() + ",ofcrcontact" + cm.get_sampling_officer_contact() + ",comments" + cm.get_comments() + ",status" + cm.get_status() +
                            ",createddatetime" + cm.get_created_datetime() + ",updateddatetime" + cm.get_updated_datetime() + ",ffmid " + cm.get_ffmid();
                    Log.e("complaintsafterupdate: ", log);


                }
                for (Complaints fb : complaints) {

                    int comp_id = fb.getCompanyId();
                    int div_id = fb.get_division_id();
                    int crop_id = fb.getCropid();
                    int prod_id = fb.getProductid();
                    int dist_id = fb.get_distributor();

                    String selectQuerys = "SELECT  " + KEY_TABLE_COMPANIES_NAME + " FROM " + TABLE_COMPANIES + " WHERE " + KEY_TABLE_COMPANIES_MASTER_ID + " = " + comp_id;
                    sdbw = db.getWritableDatabase();
                    Log.e("company query", selectQuerys);
                    Cursor cc = sdbw.rawQuery(selectQuerys, null);
//System.out.println("cursor count "+cursor.getCount());
                    if (cc != null && cc.moveToFirst()) {
                        Companies companies = new Companies();
                        companyname = cc.getString(0);
                        companies.setCompanycode(companyname);
                        //The 0 is the column index, we only have 1 column, so the index is 0
                        Log.e("COmpany", companyname);
                    }
                    selectQuerys = "SELECT  " + KEY_TABLE_DIVISION_NAME + " FROM " + TABLE_DIVISION + " WHERE " + KEY_TABLE_DIVISION_MASTER_ID + " = " + div_id;
                    sdbw = db.getWritableDatabase();
                    Log.e("div query", selectQuerys);
                    cc = sdbw.rawQuery(selectQuerys, null);
//System.out.println("cursor count "+cursor.getCount());
                    if (cc != null && cc.moveToFirst()) {
                        Divisions companies = new Divisions();
                        divisionname = cc.getString(0);
                        companies.setDivName(divisionname);
                        //The 0 is the column index, we only have 1 column, so the index is 0
                        Log.e("div", divisionname);
                    }

                    selectQuerys = "SELECT  " + KEY_TABLE_CROPS_CROP_NAME + " FROM " + TABLE_CROPS + " WHERE " + KEY_TABLE_CROPS_CROP_MASTER_ID + " = " + crop_id;
                    sdbw = db.getWritableDatabase();
                    Log.e("crop query", selectQuerys);
                    cc = sdbw.rawQuery(selectQuerys, null);
//System.out.println("cursor count "+cursor.getCount());
                    if (cc != null && cc.moveToFirst()) {
                        Crops companies = new Crops();
                        cropname = cc.getString(0);
                        companies.setCropName(cropname);
                        //The 0 is the column index, we only have 1 column, so the index is 0
                        Log.e("crop", cropname);
                    }

                    selectQuerys = "SELECT  " + KEY_PRODUCT_NAME + " FROM " + TABLE_PRODUCTS + " WHERE " + KEY_PRODUCT_MASTER_ID + " = " + prod_id;
                    sdbw = db.getWritableDatabase();
                    Log.e("product query", selectQuerys);
                    cc = sdbw.rawQuery(selectQuerys, null);
//System.out.println("cursor count "+cursor.getCount());
                    if (cc != null && cc.moveToFirst()) {
                        Products_Pojo companies = new Products_Pojo();
                        productname = cc.getString(0);
                        companies.setProductName(productname);
                        //The 0 is the column index, we only have 1 column, so the index is 0
                        Log.e("product", productname);
                    }

                    selectQuerys = "SELECT  " + KEY_TABLE_CUSTOMER_NAME + " FROM " + TABLE_CUSTOMERS + " WHERE " + KEY_TABLE_CUSTOMER_MASTER_ID + " = " + dist_id;
                    sdbw = db.getWritableDatabase();
                    Log.e("distributor query", selectQuerys);
                    cc = sdbw.rawQuery(selectQuerys, null);
//System.out.println("cursor count "+cursor.getCount());
                    if (cc != null && cc.moveToFirst()) {
                        Customers companies = new Customers();
                        distname = cc.getString(0);
                        companies.setCusName(distname);
                        //The 0 is the column index, we only have 1 column, so the index is 0
                        Log.e("distributor", distname);
                    }

                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("Id", String.valueOf(fb.getID()));
                    map.put("user_id", String.valueOf(fb.get_user_id()));
                    map.put("comp_name", companyname);
                    map.put("div_name", divisionname);
                    map.put("crop_name", cropname);
                    map.put("prod_name", productname);
                    map.put("dist_name", distname);
                    map.put("mkt_lot", fb.get_marketing_lot_number());
                    map.put("farmer_name", fb.get_farmer_name());
                    map.put("contact", fb.get_contact_no());
                    map.put("compl_area", fb.get_complaint_area_acres());
                    map.put("soil_type", fb.get_soil_type());
                    map.put("others", fb.get_others());
                    map.put("purchase_qty", fb.get_purchased_quantity());
                    map.put("complaint_qty", fb.get_complaint_quantity());
                    map.put("purchase_date", String.valueOf(fb.get_purchase_date()));
                    map.put("billno", fb.get_bill_number());
                    map.put("retailer", fb.get_retailer_name());
                    map.put("mandal", fb.get_mandal());
                    map.put("village", fb.get_village());
                    map.put("comments", fb.get_comments());
                    map.put("status", String.valueOf(fb.get_status()));
                    map.put("remarks", fb.get_remarks());
                    map.put("complaint_type", fb.get_complaint_type());
                    map.put("image", fb.get_image());
                    map.put("comp_per", fb.getComplaintPercentage());
                    map.put("comp_rem", fb.getComplaintRemarks());
                    map.put("zone", fb.getZone());
                    map.put("dealer", fb.getDealer());
                    map.put("region", fb.getRegion());
                    map.put("per_lot", fb.getPerformanceLot());
                    map.put("state", fb.getState());
                    map.put("date_of_sowing", fb.getDateOfSowing());
                    map.put("insp_date", fb.getInspectedDate());
                    map.put("date_of_comp", fb.getDateOfComplaint());
                    map.put("district", fb.getDistrict());
                    map.put("stages", String.valueOf(fb.getStages()));
                    map.put("ffm_id", fb.get_ffmid());

//                String comp_type = fb.get_complaint_type();
//                if (comp_type.equalsIgnoreCase("product")) {
                    favouriteItem.add(map);
//                }

                }


            } catch (Exception e) {
                Log.d("Tag", e.toString());
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
            if (progressDialog != null)
                progressDialog.dismiss();
            adapter = new ItemfavitemAdapter(ComplaintsprodallActivity.this, favouriteItem);
            //adapter1 = new ItemfavitemAdapter(ComplaintsallActivity.this, favouriteItem1);
            listView.setAdapter(adapter);
        }
    }

}



