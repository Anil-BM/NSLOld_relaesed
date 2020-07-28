package com.nsl.beejtantra.orderindent;


import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.design.widget.CoordinatorLayout;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.nsl.beejtantra.Constants;
import com.nsl.beejtantra.CropsFragmentSalesorderActivity;
import com.nsl.beejtantra.Customer_Details;
import com.nsl.beejtantra.Customers;
import com.nsl.beejtantra.DatabaseHandler;
import com.nsl.beejtantra.R;
import com.nsl.beejtantra.SchemesActivity;
import com.nsl.beejtantra.commonutils.Common;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentOrderIndent extends Fragment {
    public static final String REQUEST_TYPE = "request_type";
    // JSON parser class
    private JSONObject JSONObj;
    private JSONArray JSONArr=null;
    ProgressDialog progressDialog;

    DatabaseHandler db;
    SQLiteDatabase sdbw, sdbr;
    private ListView listView;
    private ItemfavitemAdapter adapter;

    String jsonData;
    String userId;
    String checkdivisions;
    int role;
    String customerscount;
    String team;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    String[] Name={"View all Order Indents ","New Order Indent"};
    String[] Names = {"View My Order Indents","My Team Order Indents","New Order Indent"};
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    boolean k=false;
    private BroadcastReceiver mMessageReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {

            new Async_getallcustomers().execute();

        }
    };
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment

        View view          = inflater.inflate(R.layout.fragment_orderindent, container, false);
        progressDialog = new ProgressDialog(getActivity());
        LocalBroadcastManager.getInstance(getActivity()).registerReceiver(mMessageReceiver,
                new IntentFilter("custom-message"));
        db = new DatabaseHandler(getActivity());
        try {
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please wait ...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            ConnectivityManager conMgr = (ConnectivityManager) getActivity()
                    .getSystemService(Context.CONNECTIVITY_SERVICE);
            NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
            if (netInfo != null)
            {  // connected to the internet
                if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {

                    new Async_getallcustomers().execute();
                } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {

                    new Async_getallcustomers().execute();
                }
            }
            else
            {
                progressDialog.dismiss();
                Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            progressDialog.dismiss();
            Log.d("Tag", e.toString());
            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
        }





        sharedpreferences  = getActivity().getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        userId             = sharedpreferences.getString("userId", "");
        team               = sharedpreferences.getString("team", "");
        role      = sharedpreferences.getInt(Constants.SharedPrefrancesKey.ROLE, 0);
        customerscount     = sharedpreferences.getString("customerscount", "");

        final CoordinatorLayout coordinatorLayout = (CoordinatorLayout) view.findViewById(R.id.coordinatorLayout);
        listView = (ListView) view.findViewById(R.id.listView);
        RelativeLayout rl = (RelativeLayout)view.findViewById(R.id.rl_schemes);
        rl.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent schemes = new Intent(getActivity(),SchemesActivity.class);
                startActivity(schemes);
            }
        });
        if(role==Constants.Roles.ROLE_7 ) {

            List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

            for (int i = 0; i < 2; i++) {
                HashMap<String, String> hm = new HashMap<String, String>();
                hm.put("txt", Name[i]);

                aList.add(hm);
            }

            // Keys used in Hashmap
            String[] from = {"txt"};

            // Ids of views in listview_layout
            int[] to = {R.id.tv_type};

            // Instantiating an adapter to store each items
            // R.layout.listview_layout defines the layout of each item
            SimpleAdapter adapter = new SimpleAdapter(getActivity(), aList, R.layout.row_advancebooking, from, to);
            listView.setAdapter(adapter);
            listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                    view.setSelected(true);

                    try {
                        ConnectivityManager conMgr = (ConnectivityManager) getActivity()
                                .getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
                        if (netInfo != null) {  // connected to the internet
                            if (i == 1&&k==true) {
                                Intent newbooking = new Intent(getActivity(), NewSalesOrderChooseActivity.class);
                                newbooking.putExtra("selection", "sales");
                                startActivity(newbooking);
                            } else {
                                if(k==true) {
                                    Intent viewbooking = new Intent(getActivity(), ViewSalesOrderCompanyActivity.class);
                                    viewbooking.putExtra(REQUEST_TYPE, 1);
                                    startActivity(viewbooking);
                                }
                            }
                        } else {
                            Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                        }
                    } catch (Exception e) {
                        Log.d("Tag", e.toString());
                        Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                    }



                }
            });

        } else {

            {

                // Each row in the list stores country name, currency and flag
                List<HashMap<String,String>> aList = new ArrayList<HashMap<String,String>>();

                for(int i=0;i<3;i++)
                {
                    HashMap<String, String> hm = new HashMap<String,String>();
                    hm.put("txt",  Names[i]);
                    aList.add(hm);
                }

                // Keys used in Hashmap
                String[] from = { "txt" };

                // Ids of views in listview_layout
                int[] to = { R.id.tv_type};

                // Instantiating an adapter to store each items
                // R.layout.listview_layout defines the layout of each item
                SimpleAdapter adapter = new SimpleAdapter(getActivity(), aList, R.layout.row_advancebooking, from, to);
                listView.setAdapter(adapter);


                listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                        view.setSelected(true);

                        try {
                            ConnectivityManager conMgr = (ConnectivityManager) getActivity()
                                    .getSystemService(Context.CONNECTIVITY_SERVICE);
                            NetworkInfo netInfo = conMgr.getActiveNetworkInfo();
                            if (netInfo != null) {  // connected to the internet
                                if (i==0&&k==true){
//
                                    Intent viewbooking = new Intent(getActivity(),ViewSalesOrderCompanyActivity.class);
                                    viewbooking.putExtra(REQUEST_TYPE, 1);
                                    startActivity(viewbooking);
                                    // Toast.makeText(getActivity(), ""+i, Toast.LENGTH_SHORT).show();

                                }
                                else if (i==1){
                                    if(k==true) {
                                        Intent newbooking = new Intent(getActivity(), ViewMOTeamOrderIndentActivity.class);
                                        newbooking.putExtra(REQUEST_TYPE, 1);
                                        startActivity(newbooking);
                                    }
                                    // Toast.makeText(getActivity(), ""+i, Toast.LENGTH_SHORT).show();
                                }

                                else if (i==2) {

                                    if (customerscount.equalsIgnoreCase("1")) {
                                        if(k==true) {
                                            Intent viewbooking = new Intent(getActivity(), NewSalesOrderChooseActivity.class);
                                            viewbooking.putExtra("selection", "sales");
                                            startActivity(viewbooking);
                                        }
                                    }
                                    else{
                                        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(getActivity());
                                        alertDialogBuilder.setMessage("Sorry ! You Don't have customers to do this operation")
                                                .setCancelable(false)
                                                .setPositiveButton("Ok",
                                                        new DialogInterface.OnClickListener() {
                                                            public void onClick(DialogInterface dialog, int id) {

                                                                dialog.cancel();
                                                            }
                                                        });

                                        AlertDialog alert = alertDialogBuilder.create();
                                        alert.show();
                                    }

                                    // Toast.makeText(getActivity(), ""+i, Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_SHORT).show();
                            }
                        } catch (Exception e) {
                            Log.d("Tag", e.toString());
                            Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                        }




                    }
                });
            }
        }
        return view;
    }

    class ItemfavitemAdapter extends BaseAdapter {

        Context context;
        ArrayList<HashMap<String,String>> results = new ArrayList<HashMap<String,String>>();


        public ItemfavitemAdapter(Context context,ArrayList<HashMap<String, String>> results) {
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
            ViewHolder holder = new ViewHolder();
            if(convertView == null)
            {
                sharedpreferences = context.getSharedPreferences(mypreference, Context.MODE_PRIVATE);

                LayoutInflater inflater     = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView                 = inflater.inflate(R.layout.row_advancebooking, parent, false);
                holder.itemname             = (TextView)convertView.findViewById(R.id.tv_company);
                convertView.setTag(holder);
            }
            else
            {
                holder = (ViewHolder)convertView.getTag();
            }
           //Glide.with(context).load(results.get(position).get("image")).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.pic);
            holder.itemname.setText(results.get(position).get("name"+"-"+"company_code"));

            return convertView;
        }


        public class ViewHolder
        {
            public TextView  itemname;

        }
        public void updateResults(ArrayList<HashMap<String, String>> results) {

            this.results = results;
            notifyDataSetChanged();
        }
    }

    public class Async_getallcustomers extends AsyncTask<Void, Void, String> {


        private String jsonData;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog.setCancelable(false);
            progressDialog.show();
            //bar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(Void... params) {

            progressDialog.setMessage("Please wait ...");
              //odb.delete(TABLE_DIVISION,null,null);


            try {
                OkHttpClient client = new OkHttpClient();


                Response responses = null;


                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

                Request request = new Request.Builder()
                        .url(Common.getCompleteURL(role, Constants.GET_CUSTOMERS_AND_CUSTOMER_DETAILS, userId, team))
                        .get()
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();

                try {
                    responses = client.newCall(request).execute();
                    jsonData = responses.body().string();
                    System.out.println("!!!!!!!1" + Common.getCompleteURL(role, Constants.GET_CUSTOMERS_AND_CUSTOMER_DETAILS, userId, team) + "\n" + jsonData);
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
                    db.deleteDataByTableName(new String[]{"customers", "customer_details"});

                    JSONObject jsonObject = new JSONObject(jsonData);


                    JSONArray companyarray = jsonObject.getJSONArray("customer_information");

                    for (int n = 0; n < companyarray.length(); n++) {


                        JSONObject jsonObjectCstomerData = companyarray.getJSONObject(n);

                        JSONObject objinfo = jsonObjectCstomerData.getJSONObject("customer_data");

                        String customer_id = objinfo.getString("customer_id");
                        String customer_name = objinfo.getString("customer_name");
                        String customer_code = objinfo.getString("customer_code");
                        String address = objinfo.getString("address");
                        String street = objinfo.getString("street");
                        String company_id = objinfo.getString("company_id");
                        String city = objinfo.getString("city");
                        String region_id = objinfo.getString("region_id");
                        String country = objinfo.getString("country");
                        String telephone = objinfo.getString("telephone");
                        String created_datetime = objinfo.getString("created_datetime");
                        String updated_datetime = objinfo.getString("updated_datetime");
                        String status = objinfo.getString("status");
                        String password = objinfo.getString("password");
                        String email = objinfo.getString("email");
                        String state = objinfo.getString("state");
                        String district = objinfo.getString("district");
                        String lat_lon = objinfo.getString("lat_lon");


                        Log.d("Insert: ", "Inserting Customers ..");
                        db.addCustomers(new Customers(customer_id,
                                customer_name,
                                customer_code,
                                address,
                                street,
                                city,
                                country,
                                region_id,
                                telephone,
                                company_id,
                                status,
                                created_datetime,
                                updated_datetime, password, email, state, district, lat_lon));

                        Log.d("Inserted: ", "Inserted Customers ..");
                        if (jsonObjectCstomerData.has("customer_data_details")) {
                            JSONArray jsonArrayCustomerDetails = jsonObjectCstomerData.getJSONArray("customer_data_details");

                            for (int i = 0; i < jsonArrayCustomerDetails.length(); i++) {
                                JSONObject jsonObjectData = jsonArrayCustomerDetails.getJSONObject(i);

                                String customer_id1 = jsonObjectData.getString("customer_id");
                                String divsion_id = jsonObjectData.getString("divsion_id");
                                String credit_limit = jsonObjectData.getString("credit_limit");
                                String inside_bucket = jsonObjectData.getString("inside_bucket");
                                String outside_bucket = jsonObjectData.getString("outside_bucket");
                                String status1 = jsonObjectData.getString("status");
                                String created_datetime1 = jsonObjectData.getString("created_datetime");
                                String updated_datetime1 = jsonObjectData.getString("updated_datetime");
                                //String credit_balance = jsonObjectData.getString("credit_balance");
                                String credit_balance = "0";

                                Log.d("Insert: ", "Inserting Customer details ..");
                                db.addCustomer_details(new Customer_Details(customer_id1, divsion_id, credit_limit, inside_bucket, outside_bucket, status1, created_datetime1, updated_datetime1, credit_balance));

                            }
                        }
                     k=true;
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }
                progressDialog.dismiss();
                k=true;
            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                Toast.makeText(getActivity(), "No Internet Connection", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
                k=true;
            }
            /*Log.d("Reading: ", "Reading all customers..");

            List<Customers> customers = db.getAllCustomers();

            for (Customers cus : customers) {
                String log = "Id: "+cus.getID() + " \n customers master ID"+ cus.getCusMasterId()+" , \n customersname: " + cus.getCusName() + "\n cus code "+cus.getCuscode()+" \n Address : "+cus.getCusaddress()+" \n Street : "+cus.getCusstreet()+  "\n cus city & country "+cus.getCus_city() +":"+ cus.getCuscountry()+ " \n C region id"+ cus.getCusregion_Id()+ " \n C telephone"+ cus.getCustelephone()+" \n C C date"+ cus.getCuscdatetime()+" \n C company Id"+ cus.getCuscompany_Id()+" \n C status"+ cus.getCusstatus()+" \n Cus cdate"+cus.getCuscdatetime()+" \n Cus udate"+cus.getCusudatetime();
                // Writing Contacts to log
                Log.e("Customers: ", log);

            }*/

            progressDialog.dismiss();
        }

    }


}
