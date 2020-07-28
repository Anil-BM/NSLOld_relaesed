package com.nsl.beejtantra.retailers;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.nsl.beejtantra.CapitalizeFirstLetter;
import com.nsl.beejtantra.Constants;
import com.nsl.beejtantra.DatabaseHandler;
import com.nsl.beejtantra.R;
import com.nsl.beejtantra.SelectedCities;
import com.nsl.beejtantra.adapters.CustomListAdapter;
import com.nsl.beejtantra.commonutils.Common;
import com.nsl.beejtantra.pojo.DistributorsRetailerPojo;
import com.nsl.beejtantra.pojo.District;
import com.squareup.okhttp.FormEncodingBuilder;
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

import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CUSTOMER_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CUSTOMER_REGION_ID;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_CUSTOMERS;


/**
 * Created by admin on 2/22/2017.
 */
public class FragmentRetailer1 extends Fragment {
    DatabaseHandler db;
    Button submit, cancel;
    ProgressDialog progressDialog;
    private static SQLiteDatabase sql, sdbr, sdbw;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    EditText mobile, retailer_name, et_taluka, et_village, et_gstin;
    AutoCompleteTextView et_district;
    String jsonData, sqliteid, ffmid, checkuid;

    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    private EditText ettinno1;
    DistributorsRetailerPojo distributorsRetailerPojo = new DistributorsRetailerPojo();
    private ProgressDialog progressDialog1;
    private String company_id;
    private String division_id;
    private String crop_id;
    private String customer_id;
    private Activity activity;
    private String regionId;
    private boolean onDistrictItemClick;
    private MyRunnable1 myRunnable1;
    private Handler handler;

    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        super.onCreate(savedInstanceState);
        db = new DatabaseHandler(getActivity());

       /* FragmentFeedback fb = new FragmentFeedback();
        fb.checkConnection();*/
        View view = inflater.inflate(R.layout.fragment_retailer1, container, false);
        sharedpreferences = getActivity().getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        checkuid = sharedpreferences.getString("userId", "");
        retailer_name = (EditText) view.findViewById(R.id.et_name);
        mobile = (EditText) view.findViewById(R.id.et_moibile_no);
        et_taluka = view.findViewById(R.id.et_taluka);
        et_village = view.findViewById(R.id.et_village);
        et_gstin = view.findViewById(R.id.et_gstin_no);
        et_district = view.findViewById(R.id.et_district);
        submit = (Button) view.findViewById(R.id.btnSubmit);
        cancel = (Button) view.findViewById(R.id.btncancel);
        ettinno1 = (EditText) view.findViewById(R.id.ettinno1);

        company_id = getArguments().getString("company_id");
        division_id = getArguments().getString("division_id");
        crop_id = getArguments().getString("crop_id");
        customer_id = getArguments().getString("customer_id");
        CapitalizeFirstLetter capital = new CapitalizeFirstLetter();
        retailer_name.addTextChangedListener(capital.capitalise(retailer_name));
        setRegionsData();

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getActivity().finish();
            }
        });

        et_district.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    et_district.showDropDown();
                } else {
                    if (handler != null) {
                        handler.removeCallbacks(myRunnable1);
                    }
                    handler = new Handler();
                    myRunnable1.setData(s.toString());
                    handler.postDelayed(myRunnable1, 300);

                }
            }
        });

        et_district.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_district.getText().length() == 0) {
                    //   auto_tv.setAdapter(adapter);
                    et_district.showDropDown();
                }
            }
        });

        return view;
    }

    private void setRegionsData() {
        String query = "SELECT " + KEY_TABLE_CUSTOMER_REGION_ID + " FROM " + TABLE_CUSTOMERS + " WHERE " + KEY_TABLE_CUSTOMER_MASTER_ID + " = " + customer_id;
        Cursor cursor = db.getWritableDatabase().rawQuery(query, null);
        if (cursor.moveToFirst()) {
            regionId = cursor.getString(cursor.getColumnIndex(KEY_TABLE_CUSTOMER_REGION_ID));
        }
        cursor.close();
        autoCompleteDistrictAdapter(new ArrayList<SelectedCities>());
        myRunnable1 = new MyRunnable1();
    }

    public void autoCompleteDistrictAdapter(List<SelectedCities> list) {
        final CustomListAdapter adapter = new CustomListAdapter(getActivity(),
                R.layout.autocomplete_custom_layout, list);

        et_district.setAdapter(adapter);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                et_district.showDropDown();
            }
        }, 100);

        adapter.setOnItemClickListener(new CustomListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, SelectedCities item) {
                onDistrictItemClick = true;
                et_district.setText(item.getCityName().toUpperCase());
                et_district.setSelection(item.getCityName().length());

                et_district.dismissDropDown();
                Common.hideSoftKeyboard(getActivity());
                //    Toast.makeText(getApplicationContext(),item.getCityName()+"\n"+item.getCityId(),Toast.LENGTH_SHORT).show();
//                Log.d("onItemClick", retailer.get_ret_masterid());

//                sel_customer_id = item.getCityId();
//                tv_customer.setTextColor(getResources().getColor(R.color.colorPrimary));
//                customername = item.getCityName();
//                SharedPreferences.Editor editor = sharedpreferences.edit();
//                editor.putString("customer_id", sel_customer_id);
//                editor.putString("customer_name", item.getCityName());
//                editor.commit();

            }
        });

    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        this.activity = activity;
    }

    class MyRunnable1 implements Runnable {
        private String text;

        public void setData(String text) {
            this.text = text;
        }

        @Override
        public void run() {
            if (this != null) {
                String key = "'%" + text + "%'";
                List<District> districts = db.getDistrictsByRegionID(key, regionId);
                final List<SelectedCities> distObjs = new ArrayList<>();
                if (districts != null && districts.size() > 0) {
                    for (int i = 0; i < districts.size(); i++) {
                        SelectedCities obj = new SelectedCities();
                        obj.setCityId(districts.get(i).districtId);
                        obj.setCityName(districts.get(i).districtName);
                        distObjs.add(obj);
                    }
                }
                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!onDistrictItemClick && et_district.isEnabled())
                            autoCompleteDistrictAdapter(distObjs);
                        else onDistrictItemClick = false;
                    }
                });

            }
        }
    }

    private void validate() {
        Handler handler = Common.disableClickEvent(submit, true);
        String retailerName = retailer_name.getText().toString().trim();
        String mobileno = mobile.getText().toString().trim();
        String gstin = et_gstin.getText().toString().trim();
        String district = et_district.getText().toString().trim();
        String taluka = et_taluka.getText().toString().trim();
        String village = et_village.getText().toString().trim();


        if (TextUtils.isEmpty(retailerName)) {
            retailer_name.setError("Please enter Retailer Proprietor Ship Name");
            retailer_name.requestFocus();
            return;
        }
            if (TextUtils.isEmpty(mobileno)) {
                mobile.requestFocus();
                Toast.makeText(getActivity(), "Please Enter Mobile Number", Toast.LENGTH_SHORT).show();
                return;
            }
        if ((mobileno.length() > 0 && mobileno.length() != 10) || mobileno.startsWith(" ") || !Common.isValidMobileNumber(mobileno)) {
            mobile.setError("Please enter valid mobile number");
            mobile.requestFocus();
            return;
        }
        if (TextUtils.isEmpty(district)) {
            et_district.requestFocus();
            et_district.setError("Please Enter District");
            return;
        }
        if (TextUtils.isEmpty(taluka)) {
            et_taluka.requestFocus();
            et_taluka.setError("Please Enter Taluka");
            return;
        }
        if (TextUtils.isEmpty(village)) {
            et_village.requestFocus();
            et_village.setError("Please Enter Village");
            return;
        }
        Common.disableClickEvent(submit, handler);

        Retailer retailer = new Retailer("", retailerName, "", "", district, taluka, village, regionId, gstin, "", mobileno, "", customer_id, "", "", "", "", "0");
        retailer.setID(db.addRetailers(retailer));
        if (Common.haveInternet(getActivity()))
            new Async_InsertRetailer(retailer).execute();
        else
            getActivity().finish();

    }

    public class Async_InsertRetailer extends AsyncTask<String, String, String> {
        private Retailer retailer;

        public Async_InsertRetailer(Retailer retailer) {
            this.retailer = retailer;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(getActivity());
            progressDialog.setMessage("Please wait ...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {

                OkHttpClient client = new OkHttpClient();
                 /*For passing parameters*/

                RequestBody formBody = new FormEncodingBuilder()
                        .add("mobile_id", String.valueOf(retailer.getID()))
                        .add("retailer_name", retailer.get_ret_name())
                        .add("retailer_tin_no", retailer.get_ret_tin_no())
                        .add("location_district", retailer.get_ret_district())
                        .add("location_taluka", retailer.get_ret_taluka())
                        .add("location_village", retailer.get_ret_village())
                        .add("retailer_gstin_no", retailer.get_ret_gstin_no())
                        .add("region_id", retailer.get_ret_region_id())
                        .add("mobile", retailer.get_ret_mobile())
                        .add("phone", retailer.get_ret_phone())
                        .add("email_id", retailer.get_email())
                        .add("distributor_id", retailer.get_ret_dist_id())
                        .add("status", retailer.get_ret_status())
                        .add("ffmid", retailer.get_ffmid())

//                        .add("customer_id", checkuid)
                        .build();

                Response responses = null;

//                System.out.println("---- retailer data -----" + params[0] + params[1] + params[2] + params[3] + params[4] + params[5] + params[6] + params[7]);

                /*MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType,
                        "type=check_in_lat_lon&visit_type=1&user_id=7&latlon=17.4411%2C78.3911&visit_date=2016-12-05%2C18%3A27%3A30&check_in_time=18%3A27%3A30&tracking_id=0");*/
                Request request = new Request.Builder()
                        .url(Constants.URL_INSERTING_RETAILER)
                        .post(formBody)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();


                try {
                    responses = client.newCall(request).execute();
                    jsonData = responses.body().string();
                    System.out.println("!!!!!!!1 InsertRetailer" + jsonData);

                } catch (IOException e) {
                    e.printStackTrace();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (progressDialog.isShowing())
                progressDialog.dismiss();


            if (jsonData != null) {
                JSONArray jsonarray;
                try {
                    JSONObject jsonobject = new JSONObject(jsonData);
                    System.out.println("!!!!!!!1 postexecuteInsertRetailer" + jsonData);

                    String status = jsonobject.getString("status");
                    if (status.equalsIgnoreCase("success")) {
                        //  Toast.makeText(getActivity(), "Feed back inserted sucessfully", Toast.LENGTH_SHORT).show();
//                        sqliteid = jsonobject.getString("sqlite");
                        retailer.setID(Integer.parseInt(jsonobject.getString("mobile_id")));
                        retailer.set_ffmid(jsonobject.getString("ffm_id"));
                        retailer.set_ret_masterid(jsonobject.getString("retailer_id"));
                        String retailerId = retailer.get_ret_masterid();
                        db.addRetailers(retailer);
                        DistributorsRetailerPojo distributorsRetailerPojo = new DistributorsRetailerPojo();
                        distributorsRetailerPojo.distributorId = customer_id;
                        distributorsRetailerPojo.retailerId = String.valueOf(db.getSqlPrimaryKeyByFFMID(retailerId));
                        db.insertDistributorRetailers(distributorsRetailerPojo);

                        Toast.makeText(getActivity(), "Retailer Inserted Successfully.", Toast.LENGTH_SHORT).show();
                        getActivity().finish();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                // Toast.makeText(getApplicationContext(),"No data found",Toast.LENGTH_LONG).show();
            }

//            finish();

        }
    }


}

