package com.nsl.beejtantra;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nsl.beejtantra.Constants;
import com.nsl.beejtantra.DatabaseHandler;
import com.nsl.beejtantra.R;
import com.nsl.beejtantra.TFA.Approvaldetails;
import com.nsl.beejtantra.TFA.BaseActivity;
import com.nsl.beejtantra.TFA.FieldActivity;
import com.nsl.beejtantra.Utility;
import com.nsl.beejtantra.pojo.RejectionCommentVo;
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
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.nsl.beejtantra.Constants.SharedPrefrancesKey.ROLE;
import static com.nsl.beejtantra.R.drawable.background_green;
import static com.nsl.beejtantra.R.drawable.background_orange;
import static com.nsl.beejtantra.R.drawable.background_yellow;

public class CompletedTfaActivities extends BaseActivity {
    ProgressDialog progressDialog;
    String total_expences_edit;
    @BindView(R.id.tv_activity)
    TextView tv_activity;
    @BindView(R.id.tv_place)
    TextView tv_place;
    @BindView(R.id.tv_crop)
    TextView tv_crop;
    @BindView(R.id.tv_formers1)
    TextView tv_formers1;
    @BindView(R.id.tv_act_formers1)
    TextView tv_act_formers1;
    @BindView(R.id.tv_act_geo)
    TextView tv_act_geo;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;
    @BindView(R.id.tv_adv)
    TextView tv_adv;

    @BindView(R.id.tv_approvalstatus)
    TextView tv_approvalstatus;
    @BindView(R.id.tv_accept)
    TextView tv_accept;
    @BindView(R.id.tv_reject)
    ImageView tv_reject;
    @BindView(R.id.edit_view)
    ImageView edit_view;
    @BindView(R.id.ll_accept_reject)
    LinearLayout ll_accept_reject;
    @BindView(R.id.rv)
    RecyclerView rv;
    ItemfavitemAdapter adapter;
    ArrayList<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> favouriteItem2 = new ArrayList<HashMap<String, String>>();
    DatabaseHandler db;
    SQLiteDatabase sdbw, sdbr;
    String jsonData,approvedby;
    String aprv_status = "0";
    ArrayList<HashMap<String, String>> favouriteItem;


    private String approval_status,order_id;;
    int approval_status_send;
    String list_id,team,userId;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    private int role;
    SimpleDateFormat sdf;
    Calendar c;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_completed_tfa_activities);
        ButterKnife.bind(this);


        db = new DatabaseHandler(this);
        db = new DatabaseHandler(this);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        userId = sharedpreferences.getString("userId", "");
        team = sharedpreferences.getString("team", "");
        role = sharedpreferences.getInt(ROLE, 0);

        // Toast(String.valueOf(role));
        if(role== Constants.Roles.ROLE_5||role==Constants.Roles.ROLE_6||role==Constants.Roles.ROLE_7)
        {
            ll_accept_reject.setVisibility(View.VISIBLE);
        }
        else
        {
            ll_accept_reject.setVisibility(View.GONE);
        }
        edit_view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showCommentsDialog2();
            }
        });
        c = Calendar.getInstance();
        sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");


        favouriteItem = new ArrayList<HashMap<String, String>>();
        favouriteItem = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("favouriteItem");
        toolbar_title.setText("Field Activity Details "  +":"+favouriteItem.get(0).get("activity_date")+"");
        tv_activity.setText("Type  : "+(favouriteItem.get(0).get("tfa_title")));

        tv_crop.setText("Crop  "+(favouriteItem.get(0).get("tfa_title")));

        tv_adv.setText("Advance taken:"+(favouriteItem.get(0).get("advance_required")+"( Rs)"));
        total_expences_edit=favouriteItem.get(0).get("total_expences");



        tv_formers1.setText("Count : "+(favouriteItem.get(0).get("no_of_farmers")+" farmers ("+favouriteItem.get(0).get("total_expences")+" Rs)"));
        tv_act_formers1.setText("Count : "+(favouriteItem.get(0).get("one_farmers")+" farmers ("+favouriteItem.get(0).get("two_cost")+" Rs)"));
        tv_act_geo.setText("Place : "+(favouriteItem.get(0).get("village")+" ,"+
                favouriteItem.get(0).get("taluka")+" ,"+favouriteItem.get(0).get("district_name")));
        double latitude = 0;
        double longitude = 0;
        try {
            String[] latlong = favouriteItem.get(0).get("three_latlng").split(",");
            latitude = Double.parseDouble(latlong[0]);
            longitude = Double.parseDouble(latlong[1]);
            getloc(latitude,longitude);
        }
        catch (Exception e)
        {
            tv_place.setText("Geo Location: "+"geoloaction failled");
        }
        finally {
            try {
                getloc(latitude,longitude);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        LinearLayoutManager llm = new LinearLayoutManager(CompletedTfaActivities.this, LinearLayoutManager.VERTICAL, false);
        rv.setLayoutManager(llm);



        new Async_getalltfalistvillages().execute();
        approval_status=favouriteItem.get(0).get("approval_status");
        Toast(approval_status);
        setButtonview(approval_status);

        tv_accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showCommentsDialog(favouriteItem.get(0).get("tfa_list_id"), tv_accept, 1);
            }
        });
        tv_reject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                showCommentsDialog(favouriteItem.get(0).get("tfa_list_id"), tv_accept, 2);
            }
        });

        if(tv_place.getText().toString().equals("place"))
        {
            tv_place.setText("Geo Location:  "+(favouriteItem.get(0).get("village")+" ,"+
                    favouriteItem.get(0).get("taluka")+" ,"+favouriteItem.get(0).get("district_name")));
        }
    }

    private void setButtonview(String approval_status) {
        if (approval_status != null) {
            if (Integer.valueOf(approval_status)>4) {
                if(Constants.Roles.ROLE_7==role&&Integer.valueOf(approval_status)==5&&Integer.valueOf(approval_status)!=9)
                {
                    tv_accept.setVisibility(View.VISIBLE);
                    tv_reject.setVisibility(View.VISIBLE);
                    tv_approvalstatus.setText("Pending");
                    edit_view.setVisibility(View.VISIBLE);
                    tv_approvalstatus.setBackgroundResource(background_yellow);
                    tv_approvalstatus.setTextColor(getResources().getColor(R.color.black));
                }
                else if(Constants.Roles.ROLE_7==role&&Integer.valueOf(approval_status)>=6&&Integer.valueOf(approval_status)!=9)
                {
                    tv_approvalstatus.setText("Approved");
                    tv_approvalstatus.setVisibility(View.VISIBLE);
                    tv_accept.setVisibility(View.GONE);
                    tv_reject.setVisibility(View.GONE);
                    edit_view.setVisibility(View.GONE);
                    tv_approvalstatus.setTextColor(getResources().getColor(R.color.white));
                    tv_approvalstatus.setBackgroundResource(background_green);
                }
                if(Constants.Roles.ROLE_6==role&&Integer.valueOf(approval_status)==6&&Integer.valueOf(approval_status)!=9)
                {
                    tv_accept.setVisibility(View.VISIBLE);
                    tv_reject.setVisibility(View.VISIBLE);
                    tv_approvalstatus.setText("Pending");
                    tv_approvalstatus.setBackgroundResource(background_yellow);
                    tv_approvalstatus.setTextColor(getResources().getColor(R.color.black));
                }
                else if(Constants.Roles.ROLE_6==role&&Integer.valueOf(approval_status)==5&&Integer.valueOf(approval_status)!=9)
                {
                    tv_accept.setVisibility(View.GONE);
                    tv_reject.setVisibility(View.GONE);
                    tv_approvalstatus.setText("Pending");
                    edit_view.setVisibility(View.VISIBLE);
                    tv_approvalstatus.setBackgroundResource(background_yellow);
                    tv_approvalstatus.setTextColor(getResources().getColor(R.color.black));
                }
                else if(Constants.Roles.ROLE_6==role&&Integer.valueOf(approval_status)>=7&&Integer.valueOf(approval_status)!=9)
                {
                    tv_approvalstatus.setText("Approved");
                    tv_approvalstatus.setVisibility(View.VISIBLE);
                    tv_accept.setVisibility(View.GONE);
                    tv_reject.setVisibility(View.GONE);
                    edit_view.setVisibility(View.GONE);
                    tv_approvalstatus.setTextColor(getResources().getColor(R.color.white));
                    tv_approvalstatus.setBackgroundResource(background_green);
                }


                if(Constants.Roles.ROLE_5==role&&Integer.valueOf(approval_status)==7&&Integer.valueOf(approval_status)!=9)
                {
                    tv_accept.setVisibility(View.VISIBLE);
                    tv_reject.setVisibility(View.VISIBLE);
                    tv_approvalstatus.setText("Pending");
                    edit_view.setVisibility(View.VISIBLE);
                    tv_approvalstatus.setBackgroundResource(background_yellow);
                    tv_approvalstatus.setTextColor(getResources().getColor(R.color.black));
                }
                else if(Constants.Roles.ROLE_5==role&&Integer.valueOf(approval_status)<7&&Integer.valueOf(approval_status)!=9)
                {
                    tv_accept.setVisibility(View.GONE);
                    tv_reject.setVisibility(View.GONE);
                    tv_approvalstatus.setText("Pending");
                    edit_view.setVisibility(View.VISIBLE);
                    tv_approvalstatus.setBackgroundResource(background_yellow);
                    tv_approvalstatus.setTextColor(getResources().getColor(R.color.black));
                }
                else if(Constants.Roles.ROLE_5==role&&Integer.valueOf(approval_status)>=8&&Integer.valueOf(approval_status)!=9)
                {
                    tv_approvalstatus.setText("Approved");
                    tv_approvalstatus.setVisibility(View.VISIBLE);
                    tv_accept.setVisibility(View.GONE);
                    tv_reject.setVisibility(View.GONE);
                    edit_view.setVisibility(View.GONE);
                    tv_approvalstatus.setTextColor(getResources().getColor(R.color.white));
                    tv_approvalstatus.setBackgroundResource(background_green);
                }



                if (Integer.valueOf(approval_status)==9)
                {
                    tv_reject.setVisibility(View.GONE);
                    tv_accept.setVisibility(View.GONE);
                    tv_approvalstatus.setVisibility(View.VISIBLE);
                    tv_approvalstatus.setText("Rejected");
                    edit_view.setVisibility(View.GONE);
                    tv_approvalstatus.setTextColor(getResources().getColor(R.color.white));
                    tv_approvalstatus.setBackgroundResource(background_orange);
                }
                if (role==19)
                {

                    edit_view.setVisibility(View.GONE);

                }
            }
         /*   else if (Integer.valueOf(approval_status)>2) {
                // try {
                //JSONObject jsonObject=new JSONObject(db.getorderintentstatus(order_id));
                tv_approvalstatus.setText("Approved");
                // } catch (JSONException e) {
                //   e.printStackTrace();
                // }

                tv_approvalstatus.setVisibility(View.VISIBLE);
                tv_accept.setVisibility(View.GONE);
                tv_reject.setVisibility(View.GONE);
                tv_approvalstatus.setTextColor(getResources().getColor(R.color.white));
                tv_approvalstatus.setBackgroundResource(background_green);
            }*/
        }
        //  tv_approvalstatus.setTextColor(getResources().getColor(R.color.tabselectedcolor));
    }



    class ItemfavitemAdapter extends RecyclerView.Adapter<ItemfavitemAdapter.ViewHolder> {

        Context context;


        public ItemfavitemAdapter(Context context, ArrayList<HashMap<String, String>> tResults) {
            this.context = context;
            results = tResults;

        }


        @NonNull
        @Override
        public ItemfavitemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            ItemfavitemAdapter.ViewHolder viewHolder = new ItemfavitemAdapter.ViewHolder(getLayoutInflater().inflate(R.layout.row_villagelist, null));
            return viewHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull final ItemfavitemAdapter.ViewHolder holder, final int position) {

            holder.tv_vill.setText(results.get(position).get("village_name"));
            holder.tv_cs.setText(results.get(position).get("cs"));
            holder.tv_p.setText(results.get(position).get("p"));
            holder.tv_sr_no.setText(String.valueOf(position));

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
            public TextView tv_cs, tv_vill, /*order_qty,*/
                    tv_p, tv_rbs_amount, scheme, tv_sr_no, tv_orderdate, tv_qunatity/*, et_quantity, tv_rate*/;
            public RelativeLayout rl_date;
            public ImageView editView;

            public ViewHolder(View convertView) {
                super(convertView);
                tv_vill = (TextView) convertView.findViewById(R.id.tv_vill);
                tv_cs = (TextView) convertView.findViewById(R.id.tv_cs);
                tv_p = (TextView) convertView.findViewById(R.id.tv_p);
                tv_sr_no = (TextView) convertView.findViewById(R.id.tv_sr_no);
            }
        }


    }





    /*    class ItemfavitemAdapter extends RecyclerView.Adapter<ItemfavitemAdapter.ViewHolder>
        {

            Context context;
           ArrayList<HashMap<String, String>> results2 = new ArrayList<HashMap<String, String>>();


            public ItemfavitemAdapter(Context context, ArrayList<HashMap<String, String>> results) {
                this.context = context;
                this.results2 = results;

            }

            @NonNull
            @Override
            public ItemfavitemAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {

                ViewHolder viewHolder = new ViewHolder(getLayoutInflater().inflate(R.layout.row_villagelist, null));


                return viewHolder;
            }

            @Override
            public void onBindViewHolder(@NonNull ItemfavitemAdapter.ViewHolder viewHolder, int i) {
                viewHolder.tv_vill.setText(results2.get(i).get("village_name"));
                viewHolder.tv_cs.setText(results2.get(i).get("cs"));
                viewHolder.tv_p.setText(results2.get(i).get("p"));
            }



            @Override
            public long getItemId(int position) {
                return position;
            }

            @Override
            public int getItemCount() {
                return results2.size();
            }


            public class ViewHolder extends RecyclerView.ViewHolder {


                @BindView(R.id.tv_vill)
                TextView tv_vill;
                @BindView(R.id.tv_cs)
                TextView tv_cs;
                @BindView(R.id.tv_p)
                TextView tv_p;

                public ViewHolder(@NonNull View itemView) {
                    super(itemView);
                    ButterKnife.bind(this, itemView);
                }
            }

            public void updateResults(ArrayList<HashMap<String, String>> results) {
               *//* if (results.size() == favouriteItemFinal.size())
                loadMore.setVisibility(View.GONE);
            this.results = results;
            notifyDataSetChanged();
            if (loadDialog != null && loadDialog.isShowing())
                loadDialog.dismiss();*//*
        } //Anil
    }*/
    private class Async_getalltfalistvillages extends AsyncTask<Void, Void, String>
    {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            favouriteItem2.clear();
        }
        @Override
        protected String doInBackground(Void... voids) {
            try {
                String selectQuery = "select * from tfa_village_list where tfa_list_id="+favouriteItem.get(0).get("tfa_list_id");

                sdbw = db.getWritableDatabase();



                /* */

                Log.d("plans", selectQuery);
                Cursor cursor = sdbw.rawQuery(selectQuery, null);
                if (cursor.moveToFirst()) {
                    do {
                        Log.d("plans", "no plans11");
                        // list_dates.add(cursor.getString(0));
                        HashMap<String,String> map=new HashMap();

                        map.put("village_name",cursor.getString(2));
                        map.put("cs",cursor.getString(3));
                        map.put("p",cursor.getString(4));
                        //  map.put("crop_id",cursor.getString(3)); imp anil;

                        favouriteItem2.add(map);

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
            Log.d("plans", favouriteItem2.toString());


            adapter = new ItemfavitemAdapter(getApplicationContext(), favouriteItem2);
            rv.setAdapter(adapter);

            //adapter.updateResults(favouriteItem);
        }
    }
    private void showCommentsDialog(final String order_id, final View button_view, final int i) {

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Reject Comment");
        if(i==1)
        {
            alert.setTitle("Advance Approval");
        }



        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_reject_dialog, null);
        alert.setView(dialogView);

        final EditText editText = (EditText) dialogView.findViewById(R.id.edit_comments);
        editText.setHint("You can edit advance amount here now advence requisted is "
                +favouriteItem.get(0).get("advance_required")+"( Rs)");
        final Button buttonOk = (Button) dialogView.findViewById(R.id.btn_ok);
        final Button buttonCancel = (Button) dialogView.findViewById(R.id.btn_cancel);
        final AlertDialog alertDialog = alert.show();
        final RejectionCommentVo rejectionCommentVo = new RejectionCommentVo();

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
                String comment = editText.getText().toString();
                JSONObject mainObject = new JSONObject();
                JSONArray apprv_array = new JSONArray();
                JSONObject apprv_obj = new JSONObject();
                try {
                    mainObject.put("tfa_list_id",favouriteItem.get(0).get("tfa_list_id"));


                    if(i==1)
                    {
                        if (role == Constants.Roles.ROLE_7) {
                            aprv_status = "6";
                            approvedby="Approved By MO FA";
                        } else if (role == Constants.Roles.ROLE_6) {
                            aprv_status = "7";
                            approvedby="Approved By AM FA";
                        } else if (role == Constants.Roles.ROLE_5) {
                            aprv_status = "8";
                            approvedby="Approved By RM FA";
                        }
                    }
                    else
                    {
                        aprv_status = "9";
                        if (role == Constants.Roles.ROLE_7) {
                            approvedby="Rejected By MO FA";
                        } else if (role == Constants.Roles.ROLE_6) {
                            approvedby="Rejected By AM FA";
                        } else if (role == Constants.Roles.ROLE_5) {
                            approvedby="Rejected By RM FA";
                        }

                    }
                    // Log.d("kl",aprv_status);
                    approval_status_send=Integer.valueOf(aprv_status);
                    mainObject.put("approval_status",aprv_status);
                    mainObject.put("approval_comments","");
                    mainObject.put("approved_by",userId);
                    mainObject.put("approved_date",sdf.format(c.getTime()));
                    apprv_obj.put("tfa_list_id",favouriteItem.get(0).get("tfa_list_id"));

                    if(total_expences_edit.equals("")||total_expences_edit==null)
                    {
                        mainObject.put("total_expences",favouriteItem.get(0).get("total_expences"));
                    }
                    else
                    {
                        mainObject.put("total_expences",total_expences_edit);
                    }


                    apprv_obj.put("approval_status",aprv_status);
                    apprv_obj.put("approval_comment","");
                    apprv_obj.put("approved_user_id",userId);
                    apprv_obj.put("status",0);
                    apprv_obj.put("created_datetime",sdf.format(c.getTime()));

                    apprv_array.put(apprv_obj);
                    mainObject.put("village_list_array1",apprv_array);

                } catch (JSONException e) {
                    e.printStackTrace();
                }


                if (Utility.isNetworkAvailable(CompletedTfaActivities.this, true))
                {
                    Log.d("plans",mainObject.toString());
                    new ApproveOrRejectAsyncTask().execute(mainObject.toString(), button_view);
                }
                else
                {

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
    private class ApproveOrRejectAsyncTask extends AsyncTask<Object, Void, String> {
        View imageView;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CompletedTfaActivities.this);
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
                        .url(Constants.URL_TFA_ORDER_APPROVAL)
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

            JSONObject responseObject;
            if (jsonData != null) {
                try {

                    responseObject = new JSONObject(jsonData);
                    if (responseObject.get("Status").equals("Success")) {
                        String currentString = responseObject.getString("InsertedUserData");
                        String[] separated = currentString.split("::::");
                        String name=separated[0];
                        String role=separated[1];

                        Approvaldetails aprvdet=new Approvaldetails(Integer.valueOf(favouriteItem.get(0).get("tfa_list_id")),
                                Integer.valueOf((Integer) responseObject.get("InsertedApprovalServerData")),Integer.valueOf(aprv_status), "",
                                userId,sdf.format(c.getTime()),userId,"1",sdf.format(c.getTime()),sdf.format(c.getTime()),
                                "1",name,role);

                        String sts =db.updatetfa_approvalbyamrmmo(aprvdet,"local");
                        Log.d("plans",sts);
                        if(sts.equals("sucess"))
                        {
                            setButtonview(String.valueOf(approval_status_send));
                        }


                    }
                    else
                    {
                        Toast((String) responseObject.get("msg"));
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
    private void showCommentsDialog2() {

        final AlertDialog.Builder alert = new AlertDialog.Builder(this);
        alert.setTitle("Edit Total Expences:"+" "+favouriteItem.get(0).get("total_expences"));




        LayoutInflater inflater = this.getLayoutInflater();
        View dialogView = inflater.inflate(R.layout.custom_reject_dialog, null);
        alert.setView(dialogView);

        final EditText editText = (EditText) dialogView.findViewById(R.id.edit_comments);
        editText.setText(favouriteItem.get(0).get("total_expences"));
        final Button buttonOk = (Button) dialogView.findViewById(R.id.btn_ok);
        final Button buttonCancel = (Button) dialogView.findViewById(R.id.btn_cancel);
        final AlertDialog alertDialog = alert.show();
        final RejectionCommentVo rejectionCommentVo = new RejectionCommentVo();

        buttonOk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(!editText.getText().toString().equals("")||editText.getText().toString()!=null)
                {
                    tv_formers1.setText("Count      : "+(favouriteItem.get(0).get("no_of_farmers")+" farmers ("+editText.getText().toString()+" Rs)"));
                    total_expences_edit=editText.getText().toString();
                }
                alertDialog.dismiss();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              alertDialog.dismiss();



            }
        });


    }

    private void getloc(double latitude, double longitude) throws IOException {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        addresses = geocoder.getFromLocation(latitude, longitude, 1);
        try {
            // Here 1 represent max location result to returned, by documents it recommended 1 to 5
            String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()

        } catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            e.printStackTrace();
        }
        catch (Exception | Error exception) {
            // Output unexpected Exceptions/Errors.
            exception.printStackTrace();
        }
       
    }
    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent it=new Intent(CompletedTfaActivities.this, FieldActivity.class);
        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(it);
    }
}
