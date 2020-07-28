package com.nsl.beejtantra.marketintelligencenew;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.buffer.AbstractBuffer;
import com.nsl.beejtantra.Constants;
import com.nsl.beejtantra.Crops;
import com.nsl.beejtantra.Customers;
import com.nsl.beejtantra.DatabaseHandler;
import com.nsl.beejtantra.MultiSelectionSpinner;
import com.nsl.beejtantra.PurposeVisit;
import com.nsl.beejtantra.R;
import com.nsl.beejtantra.SelectedCities;
import com.nsl.beejtantra.adapters.CustomListAdapter;
import com.nsl.beejtantra.commonutils.Common;
import com.nsl.beejtantra.network.RetrofitRequester;
import com.nsl.beejtantra.network.RetrofitResponseListener;
import com.nsl.beejtantra.orderindent.NewSalesOrderChooseActivity;
import com.nsl.beejtantra.pojo.ChannelPreference;
import com.nsl.beejtantra.pojo.ChannelPreferenceReqVo;
import com.nsl.beejtantra.pojo.CropShift;
import com.nsl.beejtantra.pojo.CropShiftReqVo;
import com.nsl.beejtantra.pojo.FormResponse;
import com.nsl.beejtantra.pojo.FormResponseResVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.nsl.beejtantra.DatabaseHandler.KEY_CHANNEL_PREF_FFM_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_CHANNEL_PREF_PRIMARY_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_CROP_SHIFTS_FFMID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_CROP_SHIFTS_PRIMARY_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CROPS_CROP_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CROPS_CROP_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CROPS_DIVISION_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CUSTOMER_CITY;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CUSTOMER_CODE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CUSTOMER_DETAILS_DIVISION_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CUSTOMER_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CUSTOMER_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USER_COMPANY_CUSTOMER_CUSTOMER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USER_COMPANY_CUSTOMER_STATUS;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USER_COMPANY_CUSTOMER_USER_ID;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_CHANNEL_PREFERENCE;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_CROPS;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_CROP_SHIFTS;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_CUSTOMERS;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_CUSTOMER_DETAILS;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_USER_COMPANY_CUSTOMER;

public class ChannelPreferenceActivity extends AppCompatActivity implements RetrofitResponseListener {

    EditText et_company1_name, et_company1_turnover, et_company2_name, et_company2_turnover, et_company3_name, et_company3_turnover, et_company4_name, et_company4_turnover, et_company5_name, et_company5_turnover,
            et_company6_name, et_company6_turnover, et_company7_name, et_company7_turnover, et_company8_name, et_company8_turnover, et_company9_name, et_company9_turnover, et_company10_name, et_company10_turnover;
    AutoCompleteTextView et_distributor;
    TextView save_channel_preferences;
    TextView spin_crops_dealing;
    private ProgressDialog progressDialog;
    private ArrayList<SelectedCities> arlist_customers;
    private ArrayList<String> adapter_customers;
    DatabaseHandler db;
    SQLiteDatabase sdbw;
    private String sel_customer_id;
    //private ArrayList<SelectedCities> cropItems;
    //private ArrayList<String> cropNames;
    private OptionsAdapter optionsAdapter;
    private ArrayList<PurposeVisit> purposeList=new ArrayList<>();
    private List<String> checkedList = new ArrayList<>();
    private String comments="";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mi_activity_channel_preference);
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
        et_distributor = findViewById(R.id.et_distributor);
        spin_crops_dealing = findViewById(R.id.spin_crops_dealing);
        et_company1_name = findViewById(R.id.et_company1_name);
        et_company1_turnover = findViewById(R.id.et_company1_turnover);
        et_company2_name = findViewById(R.id.et_company2_name);
        et_company2_turnover = findViewById(R.id.et_company2_turnover);
        et_company3_name = findViewById(R.id.et_company3_name);
        et_company3_turnover = findViewById(R.id.et_company3_turnover);
        et_company4_name = findViewById(R.id.et_company4_name);
        et_company4_turnover = findViewById(R.id.et_company4_turnover);
        et_company5_name = findViewById(R.id.et_company5_name);
        et_company5_turnover = findViewById(R.id.et_company5_turnover);
        et_company6_name = findViewById(R.id.et_company6_name);
        et_company6_turnover = findViewById(R.id.et_company6_turnover);
        et_company7_name = findViewById(R.id.et_company7_name);
        et_company7_turnover = findViewById(R.id.et_company7_turnover);
        et_company8_name = findViewById(R.id.et_company8_name);
        et_company8_turnover = findViewById(R.id.et_company8_turnover);
        et_company9_name = findViewById(R.id.et_company9_name);
        et_company9_turnover = findViewById(R.id.et_company9_turnover);
        et_company10_name = findViewById(R.id.et_company10_name);
        et_company10_turnover = findViewById(R.id.et_company10_turnover);
        save_channel_preferences = findViewById(R.id.save_channel_preferences);
        new AsyncCustomersoffline().execute();
        save_channel_preferences.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isValidated())
                    return;
            }
        });
        et_distributor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    // auto_tv.setAdapter(adapter);
                    et_distributor.showDropDown();
                }
            }
        });

        et_distributor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_distributor.getText().length() == 0) {
                    //   auto_tv.setAdapter(adapter);
                    et_distributor.showDropDown();
                }
            }
        });

        spin_crops_dealing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spin_crops_dealing.getText().toString().trim().length() > 0) {
                    String[] split = spin_crops_dealing.getText().toString().trim().split(",");
                    checkedList.clear();
                    for (int i = 0; i < split.length; i++) {
                        checkedList.add(split[i]);
                    }
                }
                checkboxPopup();
            }
        });
    }

    @Override
    public void onResponseSuccess(ArrayList<Object> object, Map<String, String> requestParams, int requestId) {

    }

    @Override
    public void onFormResponseSuccess(String url, FormResponseResVo formResponseResVo, ProgressDialog dialog) {
        try {
            List<FormResponse> formResponseList = formResponseResVo.formResponse;
            if (formResponseList != null && formResponseList.size() > 0) {
                for (int i = 0; i < formResponseList.size(); i++) {
                    FormResponse formResponse = formResponseList.get(i);
                    db.updateTable(KEY_CHANNEL_PREF_FFM_ID, TABLE_CHANNEL_PREFERENCE, KEY_CHANNEL_PREF_PRIMARY_ID, formResponse.sqliteId, formResponse.ffmId);
                }
                Common.dismissProgressDialog(dialog);
                Toast.makeText(this, formResponseResVo.msg, Toast.LENGTH_SHORT).show();
                finish();
            } else {
                Common.dismissProgressDialog(dialog);
                Toast.makeText(this, "Response Empty", Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {
            Common.dismissProgressDialog(dialog);
            Common.Log.i("RESPONSE EXCEPTION " + e.getMessage());
            Toast.makeText(this, e.getMessage(), Toast.LENGTH_LONG).show();
        }
    }

    public class AsyncCustomersoffline extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(ChannelPreferenceActivity.this);
            progressDialog.setMessage("Please wait ...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            arlist_customers = new ArrayList<SelectedCities>();
            adapter_customers = new ArrayList<String>();
            arlist_customers.clear();

            SelectedCities citiesz = new SelectedCities();
            citiesz.setCityId("0");
            citiesz.setCityName(Common.getStringResourceText(R.string.select_distributor));
            // System.out.println("city id is :" + cityId + "city name is :" + cityName);
//            arlist_crops.add(citiesz);
//            adapter_crops.add(Common.getStringResourceText(R.string.select_distributor));
            try {
                List<Crops> cdcList = new ArrayList<>();

                String selectQuery = "SELECT  " + "CR." + KEY_TABLE_CUSTOMER_MASTER_ID + "," + KEY_TABLE_CUSTOMER_NAME + "," + KEY_TABLE_CUSTOMER_CODE + ",CR." + KEY_TABLE_CUSTOMER_CITY + " FROM " + TABLE_USER_COMPANY_CUSTOMER + " AS CDC JOIN " + TABLE_CUSTOMERS + " AS CR ON CDC." + KEY_TABLE_USER_COMPANY_CUSTOMER_CUSTOMER_ID + " = CR." + KEY_TABLE_CUSTOMER_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_CUSTOMER_USER_ID + " in ( " + db.getTeam(Common.getUserIdFromSP(ChannelPreferenceActivity.this)) + " ) and CR." + KEY_TABLE_USER_COMPANY_CUSTOMER_STATUS + " = 1" + " and CDC." + KEY_TABLE_USER_COMPANY_CUSTOMER_STATUS + " = 1 group by(CR." + KEY_TABLE_CUSTOMER_MASTER_ID + ")";
                // String selectQuery = "SELECT  " + "CR." + KEY_TABLE_CUSTOMER_MASTER_ID + ","+ KEY_TABLE_CUSTOMER_NAME+ ","+ KEY_TABLE_CUSTOMER_CODE + " FROM " + TABLE_USER_COMPANY_CUSTOMER+ " AS CDC JOIN " + TABLE_CUSTOMERS + " AS CR ON CDC."+KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID + " = CR."+ KEY_TABLE_CUSTOMER_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_DIVISION_USER_ID + " = " + userId + " group by(CR." + KEY_TABLE_COMPANIES_MASTER_ID + ")" ;
                sdbw = db.getWritableDatabase();

                Cursor cursor = sdbw.rawQuery(selectQuery, null);
                //System.out.println("cursor count "+cursor.getCount());
                if (cursor.moveToFirst()) {
                    do {
                        Customers customers = new Customers();

                        customers.setCusMasterID(cursor.getString(0));
                        customers.setCusName(cursor.getString(1));
                        customers.setCuscode(cursor.getString(2));


                        String city = cursor.getString(3) == null || cursor.getString(3).equalsIgnoreCase("null") ? "" : cursor.getString(3);
                        SelectedCities cities2 = new SelectedCities();
                        cities2.setCityId(cursor.getString(0));
                        cities2.setCityName(cursor.getString(1) + " -" + city);
                        // System.out.println("city id is :" + cityId + "city name is :" + cityName);
                        arlist_customers.add(cities2);
                        adapter_customers.add(cursor.getString(1) + " -" + city);
                        System.out.println("+++++++++++" + cursor.getString(0) + cursor.getString(1) + cursor.getString(2));


                    } while (cursor.moveToNext());
                }

                // do some stuff....
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
            autoCompleteAdapter(arlist_customers);
            /*spin_customer.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, R.id.customSpinnerItemTextView, adapter_crops));
            spin_customer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    sel_customer_id = arlist_crops.get(position).getCityId();
                    //listView.setVisibility(View.INVISIBLE);
                    //
                    //Toast.makeText(getActivity(), categorytypeIdis, Toast.LENGTH_SHORT).show();
                    // Toast.makeText(getApplicationContext(), "apicalled" ,Toast.LENGTH_SHORT).show();
                    if (sel_customer_id.equalsIgnoreCase("0")) {

                    } else {
                        customername = arlist_crops.get(position).getCityName();
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("customer_id", sel_customer_id);
                        editor.putString("customer_name", arlist_crops.get(position).getCityName());
                        editor.commit();
                    }
                }


                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });*/
        }
    }

    public void autoCompleteAdapter(List<SelectedCities> list) {
        final CustomListAdapter adapter = new CustomListAdapter(this,
                R.layout.autocomplete_custom_layout, list);

        et_distributor.setAdapter(adapter);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                et_distributor.showDropDown();
            }
        }, 100);

        adapter.setOnItemClickListener(new CustomListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, SelectedCities item) {
                et_distributor.setText(item.getCityName().toUpperCase());
                et_distributor.setSelection(item.getCityName().length());
                et_distributor.dismissDropDown();
                Common.hideSoftKeyboard(ChannelPreferenceActivity.this);
                //    Toast.makeText(getApplicationContext(),item.getCityName()+"\n"+item.getCityId(),Toast.LENGTH_SHORT).show();
                Log.d("onItemClick", item.getCityId());

                sel_customer_id = item.getCityId();

                String query = "SELECT CR." + KEY_TABLE_CROPS_CROP_MASTER_ID + ",CR." + KEY_TABLE_CROPS_CROP_NAME + " FROM " + TABLE_CROPS + " AS CR JOIN " + TABLE_CUSTOMER_DETAILS + " AS CD ON CR." + KEY_TABLE_CROPS_DIVISION_ID + " = CD." + KEY_TABLE_CUSTOMER_DETAILS_DIVISION_ID + " WHERE CD." + KEY_TABLE_CUSTOMER_MASTER_ID + " = " + sel_customer_id;
                Common.Log.i("query " + query);
                sdbw = db.getWritableDatabase();
                Cursor cursor = sdbw.rawQuery(query, null);
                //cropItems = new ArrayList<>();
               // cropNames = new ArrayList<>();
                purposeList.clear();
                if (cursor != null && cursor.moveToFirst()) {
                    do {
                     /*   SelectedCities cropItem = new SelectedCities();
                        cropItem.setCityId(cursor.getString(0));
                        cropItem.setCityName(cursor.getString(1));*/
                     //   cropItems.add(cropItem);
                     //   cropNames.add(cursor.getString(1));

                        PurposeVisit purposeVisit = new PurposeVisit();
                        purposeVisit.id = cursor.getString(0);
                        purposeVisit.title = cursor.getString(1);
                        purposeVisit.isChecked = false;
                        purposeList.add(purposeVisit);
                    } while (cursor.moveToNext());

                }
               // spin_crops_dealing.setItems(cropNames);

              /*  tv_customer.setTextColor(getResources().getColor(R.color.colorPrimary));
                customername = item.getCityName() ==null ? "":item.getCityName().trim();
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("customer_id", sel_customer_id);
                editor.putString("customer_name", customername);
                editor.commit();*/

            }
        });
        et_distributor.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("afterTextChanged", "afterTextChanged" + s.length());
                if (s.length() == 0) {
                    // auto_tv.setAdapter(adapter);
                    et_distributor.showDropDown();
                }

            }
        });

        et_distributor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_distributor.getText().length() == 0) {
                    //   auto_tv.setAdapter(adapter);
                    et_distributor.showDropDown();
                }
            }
        });
    }

    private boolean isValidated() {
        if (sel_customer_id==null ||sel_customer_id.equalsIgnoreCase("")|| sel_customer_id.equalsIgnoreCase("0")){
            Toast.makeText(this,"Please select distributor",Toast.LENGTH_LONG).show();
            return false;
        }
        if (spin_crops_dealing.getText().toString().isEmpty()){
           Toast.makeText(this,"Please select crop dealing with",Toast.LENGTH_LONG).show();
           return false;
        }
        if (et_company1_name.getText().toString().isEmpty()) {
            et_company1_name.requestFocus();
            et_company1_name.setError("Please Enter Company 1 Name");
            return false;
        }

        if (et_company1_turnover.getText().toString().isEmpty()) {
            et_company1_turnover.requestFocus();
            et_company1_turnover.setError("Please Enter Company 1 Turnover");
            return false;
        }

        if (et_company2_name.getText().toString().isEmpty()) {
            et_company2_name.requestFocus();
            et_company2_name.setError("Please Enter Company 2 Name");
            return false;
        }

        if (et_company2_turnover.getText().toString().isEmpty()) {
            et_company2_turnover.requestFocus();
            et_company2_turnover.setError("Please Enter Company 2 Turnover");
            return false;
        }

        if (et_company3_name.getText().toString().isEmpty()) {
            et_company3_name.requestFocus();
            et_company3_name.setError("Please Enter Company 3 Name");
            return false;
        }

        if (et_company3_turnover.getText().toString().isEmpty()) {
            et_company3_turnover.requestFocus();
            et_company3_turnover.setError("Please Enter Company 3 Turnover");
            return false;
        }

        if (et_company4_name.getText().toString().isEmpty()) {
            et_company4_name.requestFocus();
            et_company4_name.setError("Please Enter Company 4 Name");
            return false;
        }

        if (et_company4_turnover.getText().toString().isEmpty()) {
            et_company4_turnover.requestFocus();
            et_company4_turnover.setError("Please Enter Company 4 Turnover");
            return false;
        }

        if (et_company5_name.getText().toString().isEmpty()) {
            et_company5_name.requestFocus();
            et_company5_name.setError("Please Enter Company 5 Name");
            return false;
        }

        if (et_company5_turnover.getText().toString().isEmpty()) {
            et_company5_turnover.requestFocus();
            et_company5_turnover.setError("Please Enter Company 5 Turnover");
            return false;
        }
       // List<Integer> selectedIndices = spin_crops_dealing.getSelectedIndices();
        StringBuffer cropIds = new StringBuffer();
        StringBuffer cropNames = new StringBuffer();
        for (int i = 0; i < purposeList.size(); i++) {
            if (purposeList.get(i).isChecked) {
              //  SelectedCities selectedCities = cropItems.get(selectedIndices.get(i));
                cropIds.append(purposeList.get(i).id + ",");
                cropNames.append(purposeList.get(i).title + ",");
            }
        }
        ChannelPreference cp = new ChannelPreference();
        cp.userId = Integer.parseInt(Common.getUserIdFromSP(this));
        cp.distributorId = sel_customer_id;
        cp.cropName = cropNames.substring(0, cropNames.lastIndexOf(","));
        cp.cropId = cropIds.substring(0, cropIds.lastIndexOf(","));
        cp.companyName1 = et_company1_name.getText().toString().trim();
        cp.company1Turnover=et_company1_turnover.getText().toString().trim();
        cp.companyName2 = et_company2_name.getText().toString().trim();
        cp.company2Turnover=et_company2_turnover.getText().toString().trim();
        cp.companyName3 = et_company3_name.getText().toString().trim();
        cp.company3Turnover=et_company3_turnover.getText().toString().trim();
        cp.companyName4 = et_company4_name.getText().toString().trim();
        cp.company4Turnover=et_company4_turnover.getText().toString().trim();
        cp.companyName5 = et_company5_name.getText().toString().trim();
        cp.company5Turnover=et_company5_turnover.getText().toString().trim();
        cp.companyName6 = et_company6_name.getText().toString().trim();
        cp.company6Turnover=et_company6_turnover.getText().toString().trim();
        cp.companyName7 = et_company7_name.getText().toString().trim();
        cp.company7Turnover=et_company7_turnover.getText().toString().trim();
        cp.companyName8 = et_company8_name.getText().toString().trim();
        cp.company8Turnover=et_company8_turnover.getText().toString().trim();
        cp.companyName9 = et_company9_name.getText().toString().trim();
        cp.company9Turnover=et_company9_turnover.getText().toString().trim();
        cp.companyName10 = et_company10_name.getText().toString().trim();
        cp.company10Turnover=et_company10_turnover.getText().toString().trim();
        db.insertChannelPreference(cp);

        List<ChannelPreference> channelPreferences = db.getOfflineChannelPreferences(Common.getUserIdFromSP(this));
        ChannelPreferenceReqVo channelPreferenceReqVo = new ChannelPreferenceReqVo();
        channelPreferenceReqVo.channelPreference = channelPreferences;
        if (Common.haveInternet(ChannelPreferenceActivity.this))
            new RetrofitRequester(this).sendRequest(Constants.URL_INSERTING_CHANNEL_PREFERENCE, channelPreferenceReqVo, true);
        else
            finish();
        return true;
    }


    private void checkboxPopup() {
        final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(this).create();
        View dialogView = getLayoutInflater().inflate(R.layout.check_box_recyclerview, null);
        alertDialog.setView(dialogView);
        alertDialog.show();
        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        final StringBuffer sb = new StringBuffer();
        ListView optionsView = dialogView.findViewById(R.id.options_list);
        TextView submit = dialogView.findViewById(R.id.options_submit);
        TextView cancel = dialogView.findViewById(R.id.options_cancel);
        if (purposeList != null) {
            setDataToOptions();
            optionsAdapter = new OptionsAdapter(this);
            optionsView.setAdapter(optionsAdapter);
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (int i = 0; i < purposeList.size(); i++) {
                    purposeList.get(i).isChecked = false;
                }
                setDataToOptions();
                alertDialog.dismiss();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkedList.clear();
                sb.setLength(0);
                for (int i = 0; i < purposeList.size(); i++) {
                    if (purposeList.get(i).isChecked && purposeList.get(i).title.equalsIgnoreCase("Other") && getOtherComments().length() == 0) {
                        Toast.makeText(ChannelPreferenceActivity.this, "Please Enter Other", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (purposeList.get(i).isChecked) {
                        checkedList.add(purposeList.get(i).title);
                        sb.append(purposeList.get(i).title + ",");
                    }
                    if (!purposeList.get(i).isChecked && purposeList.get(i).title.equalsIgnoreCase("Other")) {
                        setOtherComments("");
                    }
                }
//                sb.setLength(0);
//                associateContactId.setLength(0);
//                opportunityAssociateContacts.clear();
//                for (int i = 0; i < associateContacts.size(); i++) {
//                    if (i > 0 && associateContacts.get(i).aBoolean == true) {
//                        OpportunityAssociateContact opportunityAssociateContact = new OpportunityAssociateContact();
//                        opportunityAssociateContact.id = associateContacts.get(i).contactId;
//                        opportunityAssociateContact.name = associateContacts.get(i).name;
//                        opportunityAssociateContacts.add(opportunityAssociateContact);
//                        associateContactId.append(associateContacts.get(i).contactId + ",");
//                        sb.append(associateContacts.get(i).name + ",");
//                    }
//                }
                if (sb.toString().length() > 0) {
                    spin_crops_dealing.setText(Common.isStringNull(sb.toString().substring(0, sb.lastIndexOf(","))));
                } else {
                    Toast.makeText(ChannelPreferenceActivity.this, "Please Select Purpose", Toast.LENGTH_SHORT).show();
                    return;
                }
                alertDialog.dismiss();
            }
        });
    }

    public class OptionsAdapter extends BaseAdapter {
        Activity context;

        StringBuffer buffer = new StringBuffer();

        public OptionsAdapter(Activity context) {
            this.context = context;

        }


        @Override
        public int getCount() {
            return purposeList.size();
        }

        @Override
        public Object getItem(int i) {
            return purposeList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int position, View view, ViewGroup viewGroup) {
            view = context.getLayoutInflater().inflate(R.layout.check_box_item, null);
            CheckBox optionBox = view.findViewById(R.id.chk_product);
            final EditText otherEt = view.findViewById(R.id.other_et);

            optionBox.setText(purposeList.get(position).title);
            optionBox.setChecked(purposeList.get(position).isChecked);
            if (purposeList.get(position).title.equalsIgnoreCase("Other") && purposeList.get(position).isChecked) {
                otherEt.setVisibility(View.VISIBLE);
                otherEt.setText(getOtherComments());
            } else {
                otherEt.setVisibility(View.GONE);
            }

            optionBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    purposeList.get(position).isChecked = b;
                    if (purposeList.get(position).title.equalsIgnoreCase("Other")) {
                        if (purposeList.get(position).isChecked)
                            otherEt.setVisibility(View.VISIBLE);
                        else {
                            otherEt.setVisibility(View.GONE);
                        }
                    } else {
                        otherEt.setVisibility(View.GONE);
                    }
                    notifyDataSetChanged();
                }
            });

            otherEt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    setOtherComments(editable.toString().trim());
                }
            });
            return view;
        }

    }

    private void setDataToOptions() {
        if (checkedList != null) {
            for (int i = 0; i < checkedList.size(); i++) {
                for (int j = 0; j < purposeList.size(); j++) {
                    if (checkedList.get(i).equalsIgnoreCase(purposeList.get(j).title)) {
                        purposeList.get(j).isChecked = true;
                    }
                }
            }
        }
    }

    private void setOtherComments(String trim) {
        this.comments = trim;
    }

    private String getOtherComments() {
        return comments;
    }
}
