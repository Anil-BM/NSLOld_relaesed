package com.nsl.beejtantra.complaints;


import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.nsl.beejtantra.Companies;
import com.nsl.beejtantra.Crops;
import com.nsl.beejtantra.Customers;
import com.nsl.beejtantra.DatabaseHandler;
import com.nsl.beejtantra.Divisions;
import com.nsl.beejtantra.MainActivity;
import com.nsl.beejtantra.MainRegulatoryActivity;
import com.nsl.beejtantra.Products_Pojo;
import com.nsl.beejtantra.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.nsl.beejtantra.DatabaseHandler.KEY_PRODUCT_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_PRODUCT_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_COMPANIES_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_COMPANIES_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CROPS_CROP_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CROPS_CROP_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CUSTOMER_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CUSTOMER_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_DIVISION_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_DIVISION_NAME;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_COMPANIES;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_CROPS;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_CUSTOMERS;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_DIVISION;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_PRODUCTS;


public class ComplaintsregallActivity extends AppCompatActivity {

    private static final int FROM_COMPLAINTS_REGALL_ACTIVITY = 4;
    private JSONObject JSONObj;
    private JSONArray JSONArr=null;
    ProgressDialog progressDialog;
    DatabaseHandler db;
    SQLiteDatabase sdbw,sdbr;
       int i=0,sum_osa=0,flag=0;
    private ListView listView;
    private ItemfavitemAdapter adapter;
    String customer_id,comp_id,status1;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    String color,fdbk,ffmidsqlite;
    int status;
    TextView empty;
    Button refresh;
    String jsonData, userId, id, payment_type, user,company_id, division_id,created_datetime, updated_datetime, ffmid;
    String aging1=null,aging2 = null,companyname,divisionname,cropname,productname,distname;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    TextView tv_name,tv_code,tv_total_osa_amt;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.all_complaint_reg);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        userId            = sharedpreferences.getString("userId", "");


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ComplaintsregallActivity.this,Complaintsselectactivity.class);
                startActivity(i);
                finish();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent newfedback = new Intent(getApplicationContext(),MainRegulatoryActivity.class);
                startActivityForResult(newfedback,FROM_COMPLAINTS_REGALL_ACTIVITY);

            }
        });
        refresh = (Button)findViewById(R.id.refresh);
        refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(ComplaintsregallActivity.this,ComplaintsregallActivity.class);
                startActivity(i);
                finish();
            }
        });

        db = new DatabaseHandler(ComplaintsregallActivity.this);

      /*  FragmentFeedback fragm = new FragmentFeedback();
        fragm.checkConnection();*/
        listView = (ListView) findViewById(R.id.listView);
        adapter = new ItemfavitemAdapter(ComplaintsregallActivity.this, favouriteItem);
        //adapter1 = new ItemfavitemAdapter(ComplaintsallActivity.this, favouriteItem1);
        listView.setAdapter(adapter);
        listView.setEmptyView(findViewById(R.id.empty));
        empty = (TextView)findViewById(R.id.empty);
       //db.deleteComplaints();
        try {


                Log.d("Reading: ", "Reading all complaints from server..");
                List<Complaints> complaints = db.getAllComplaintreg(userId);

                for (Complaints cm : complaints) {
                    String log = "Id: " + cm.getID() +"Userid: "+cm.get_user_id()+ " ,company: " + cm.getCompanyId() + " ,div: " + cm.get_division_id() + ",crop:" + cm.getCropid() + " ,product: " + cm.getProductid() + ",mkt_lot:" +
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

                    String selectQuerys = "SELECT  " + KEY_TABLE_COMPANIES_NAME  +" FROM " + TABLE_COMPANIES + " WHERE " + KEY_TABLE_COMPANIES_MASTER_ID + " = " + comp_id;
                    sdbw = db.getWritableDatabase();
                    Log.e("company query",selectQuerys);
                    Cursor cc = sdbw.rawQuery(selectQuerys, null);
//System.out.println("cursor count "+cursor.getCount());
                    if (cc != null && cc.moveToFirst()) {
                        Companies companies = new Companies();
                        companyname = cc.getString(0);
                        companies.setCompanycode(companyname);
                        //The 0 is the column index, we only have 1 column, so the index is 0
                        Log.e("COmpany",companyname);
                    }
                    selectQuerys = "SELECT  " + KEY_TABLE_DIVISION_NAME  +" FROM " + TABLE_DIVISION + " WHERE " + KEY_TABLE_DIVISION_MASTER_ID + " = " + div_id;
                    sdbw = db.getWritableDatabase();
                    Log.e("div query",selectQuerys);
                     cc = sdbw.rawQuery(selectQuerys, null);
//System.out.println("cursor count "+cursor.getCount());
                    if (cc != null && cc.moveToFirst()) {
                        Divisions companies = new Divisions();
                        divisionname = cc.getString(0);
                        companies.setDivName(divisionname);
                        //The 0 is the column index, we only have 1 column, so the index is 0
                        Log.e("div",divisionname);
                    }

                    selectQuerys = "SELECT  " + KEY_TABLE_CROPS_CROP_NAME  +" FROM " + TABLE_CROPS + " WHERE " + KEY_TABLE_CROPS_CROP_MASTER_ID + " = " + crop_id;
                    sdbw = db.getWritableDatabase();
                    Log.e("crop query",selectQuerys);
                    cc = sdbw.rawQuery(selectQuerys, null);
//System.out.println("cursor count "+cursor.getCount());
                    if (cc != null && cc.moveToFirst()) {
                        Crops companies = new Crops();
                        cropname = cc.getString(0);
                        companies.setCropName(cropname);
                        //The 0 is the column index, we only have 1 column, so the index is 0
                        Log.e("crop",cropname);
                    }

                    selectQuerys = "SELECT  " + KEY_PRODUCT_NAME  +" FROM " + TABLE_PRODUCTS + " WHERE " + KEY_PRODUCT_MASTER_ID + " = " + prod_id;
                    sdbw = db.getWritableDatabase();
                    Log.e("product query",selectQuerys);
                    cc = sdbw.rawQuery(selectQuerys, null);
//System.out.println("cursor count "+cursor.getCount());
                    if (cc != null && cc.moveToFirst()) {
                        Products_Pojo companies = new Products_Pojo();
                        productname = cc.getString(0);
                        companies.setProductName(productname);
                        //The 0 is the column index, we only have 1 column, so the index is 0
                        Log.e("product",productname);
                    }

                    selectQuerys = "SELECT  " + KEY_TABLE_CUSTOMER_NAME  +" FROM " + TABLE_CUSTOMERS + " WHERE " + KEY_TABLE_CUSTOMER_MASTER_ID + " = " + dist_id;
                    sdbw = db.getWritableDatabase();
                    Log.e("distributor query",selectQuerys);
                    cc = sdbw.rawQuery(selectQuerys, null);
//System.out.println("cursor count "+cursor.getCount());
                    if (cc != null && cc.moveToFirst()) {
                        Customers companies = new Customers();
                        distname = cc.getString(0);
                        companies.setCusName(distname);
                        //The 0 is the column index, we only have 1 column, so the index is 0
                        Log.e("distributor",distname);
                    }

                    HashMap<String, String> map = new HashMap<String, String>();
                    map.put("Id", String.valueOf(fb.getID()));
                    map.put("user_id", String.valueOf(fb.get_user_id()));
                    map.put("comp_name",companyname);
                    map.put("div_name",divisionname);
                    map.put("crop_name",cropname);
                    map.put("prod_name",productname);
                    map.put("dist_name",distname);
                    map.put("mkt_lot",fb.get_marketing_lot_number());
                    map.put("reg_type",fb.get_regulatory_type());
                    map.put("samp_date",fb.get_sampling_date());
                    map.put("samp_place",fb.get_place_sampling());
                    map.put("retailer", fb.get_retailer_name());
                    map.put("samp_officer",fb.get_sampling_officer_name());
                    map.put("samp_officer_contact",fb.get_sampling_officer_contact());
                    map.put("comments", fb.get_comments());
                    map.put("status", String.valueOf(fb.get_status()));
                    map.put("remarks", fb.get_remarks());
                    map.put("complaint_type", fb.get_complaint_type());
                    map.put("image", fb.get_image());




                    String comp_type = fb.get_complaint_type();

                    if(comp_type.equalsIgnoreCase("Regulatory"))

                    favouriteItem.add(map);

                }

                adapter = new ItemfavitemAdapter(ComplaintsregallActivity.this, favouriteItem);
                //adapter1 = new ItemfavitemAdapter(ComplaintsallActivity.this, favouriteItem1);
                listView.setAdapter(adapter);




        } catch (Exception e) {
            Log.d("Tag", e.toString());
        }


        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent booking = new Intent(getApplicationContext(),AllregulatoryActivity.class);


                booking.putExtra("details","REGULATORY");

                booking.putExtra("comp_name",favouriteItem.get(i).get("comp_name"));
                booking.putExtra("div_name",favouriteItem.get(i).get("div_name"));
                booking.putExtra("crop_name",favouriteItem.get(i).get("crop_name"));
                booking.putExtra("prod_name",favouriteItem.get(i).get("prod_name"));
                booking.putExtra("dist_name",favouriteItem.get(i).get("dist_name"));

                booking.putExtra("mkt_lot",favouriteItem.get(i).get("mkt_lot"));
                booking.putExtra("reg_type",favouriteItem.get(i).get("reg_type"));
                booking.putExtra("samp_date",favouriteItem.get(i).get("samp_date"));
                booking.putExtra("samp_place",favouriteItem.get(i).get("samp_place"));

                booking.putExtra("retailer",favouriteItem.get(i).get("retailer"));
                booking.putExtra("samp_officer",favouriteItem.get(i).get("samp_officer"));
                booking.putExtra("samp_officer_contact",favouriteItem.get(i).get("samp_officer_contact"));
                booking.putExtra("comments",favouriteItem.get(i).get("comments"));
                booking.putExtra("status",favouriteItem.get(i).get("status"));
                booking.putExtra("remarks",favouriteItem.get(i).get("remarks"));
                booking.putExtra("image",favouriteItem.get(i).get("image"));

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
            Intent home = new Intent(getApplicationContext(),MainActivity.class);
            startActivity(home);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    class ItemfavitemAdapter extends BaseAdapter {

        Context context;
        ArrayList<HashMap<String,String>> results = new ArrayList<HashMap<String,String>>();


        public ItemfavitemAdapter(ComplaintsregallActivity pd, ArrayList<HashMap<String, String>> results) {
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
            if(convertView == null)
            {
                sharedpreferences = context.getSharedPreferences(mypreference, Context.MODE_PRIVATE);

                LayoutInflater inflater     = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView                 = inflater.inflate(R.layout.row_complaints, parent, false);
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.tv_place = (TextView) convertView.findViewById(R.id.tv_place);
                holder.tv_comptype = (TextView) convertView.findViewById(R.id.tv_complainttype);
                holder.tv_billno = (TextView) convertView.findViewById(R.id.tv_billno);
                holder.tv_retailer = (TextView) convertView.findViewById(R.id.tv_retailername);
                holder.status_icon = (ImageView)convertView.findViewById(R.id.status_icon);
                holder.tv_status_value = (TextView)convertView.findViewById(R.id.statusval);

                convertView.setTag(holder);
            }
            else
            {
                holder = (ViewHolder)convertView.getTag();
            }
            //Glide.with(context).load(results.get(position).get("image")).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.pic);
            holder.tv_comptype.setText(results.get(position).get("comments"));
            holder.tv_status_value.setText(results.get(position).get("status"));
            status1 = results.get(position).get("status");
            holder.tv_retailer.setText(results.get(position).get("retailer"));
            holder.tv_place.setVisibility(View.GONE);
            status = Integer.parseInt(status1);
            if(status==1){
                holder.status_icon.setImageResource(R.drawable.icon4);
            }
            else if(status==2){
                holder.status_icon.setImageResource(R.drawable.icon5);
            }
            else if(status==3){
                holder.status_icon.setImageResource(R.drawable.icon6);
            }
            fdbk = results.get(position).get("comments");
            int len= fdbk.length();
            if(len > 60){
                holder.tv_comptype.setText(fdbk.substring(0,60)+"...");
            }
            else
                holder.tv_comptype.setText(fdbk);
            holder.tv_place.setText(fdbk);  //full complaint

            return convertView;
        }


        public class ViewHolder
        {
            public TextView tv_name, tv_place, tv_comptype, tv_billno, tv_retailer;
            public ImageView img;
            public ImageView status_icon;
            public TextView tv_status_value;

        }
        public void updateResults(ArrayList<HashMap<String, String>> results) {

            this.results = results;
            notifyDataSetChanged();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        switch (requestCode){
            case FROM_COMPLAINTS_REGALL_ACTIVITY:
                switch (resultCode){
                    case Activity.RESULT_OK:
                        this.recreate();
                        break;
                }

                break;
        }
    }

}



