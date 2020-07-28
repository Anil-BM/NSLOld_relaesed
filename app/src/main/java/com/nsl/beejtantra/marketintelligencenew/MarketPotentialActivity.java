package com.nsl.beejtantra.marketintelligencenew;

import android.app.ProgressDialog;
import android.database.Cursor;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nsl.beejtantra.Constants;
import com.nsl.beejtantra.DatabaseHandler;
import com.nsl.beejtantra.PlanScheduleFormActivity;
import com.nsl.beejtantra.R;
import com.nsl.beejtantra.Regions;
import com.nsl.beejtantra.SelectedCities;
import com.nsl.beejtantra.SpinnerModel;
import com.nsl.beejtantra.SpinnerModel1;
import com.nsl.beejtantra.adapters.CustomListAdapter;
import com.nsl.beejtantra.adapters.CustomSpinnerAdapter;
import com.nsl.beejtantra.commonutils.Common;
import com.nsl.beejtantra.network.RetrofitRequester;
import com.nsl.beejtantra.network.RetrofitResponseListener;
import com.nsl.beejtantra.pojo.District;
import com.nsl.beejtantra.pojo.FormResponse;
import com.nsl.beejtantra.pojo.FormResponseResVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.nsl.beejtantra.DatabaseHandler.KEY_MARKET_POTENTIAL_FFM_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_MARKET_POTENTIAL_PRIMARY_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CROPS_CROP_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CROPS_CROP_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CROPS_DIVISION_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_DIVISION_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_DIVISION_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USER_COMPANY_DIVISION_DIVISION_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USER_COMPANY_DIVISION_USER_ID;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_CROPS;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_DIVISION;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_MARKET_POTENTIAL;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_USER_COMPANY_DIVISION;

public class MarketPotentialActivity extends AppCompatActivity implements RetrofitResponseListener {

    EditText et_taluka, et_village, et_kharifcrop, et_rabicrop, et_summercrop, et_seed_usage, et_nsl_sale, et_top_company1_name, et_company1_qty, et_top_company2_name, et_company2_qty, et_top_company3_name, et_company3_qty, et_top_company4_name, et_company4_qty, et_top_company5_name, et_company5_qty;
    AutoCompleteTextView et_district;
    Spinner regionSpinner, spin_division, spin_crop;
    TextView tv_total_potential, tv_total_market_potential_volume, save_market_potential, tv_company1_qty, tv_company2_qty, tv_company3_qty, tv_company4_qty, tv_company5_qty, tv_nsl_sale, tv_total_market, tv_seed_usage;
    ArrayList<String> divisionMarketAdapter;
    ArrayList<String> cropMarketadapter;
    DatabaseHandler db;
    List<SelectedCities> divisions = new ArrayList<>();
    List<SelectedCities> crops = new ArrayList<>();
    private String divisionId, cropId;
    double kharif = 0, rabi = 0, summer = 0, totalPotential = 0;
    private String divisionName, cropName;
    private String regionId = "0";
    private String districtId = "0";
    private boolean onDistrictItemClick;
    private MyRunnable1 myRunnable1;
    private Handler handler;
    List<SelectedCities> distObjs = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mi_activity_market_potential);
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

        et_district = findViewById(R.id.et_district);
        et_taluka = findViewById(R.id.et_taluka);
        et_village = findViewById(R.id.et_village);
        spin_division = findViewById(R.id.spin_division);
        spin_crop = findViewById(R.id.spin_crop);
        et_kharifcrop = findViewById(R.id.et_kharifcrop);
        et_rabicrop = findViewById(R.id.et_rabicrop);
        et_summercrop = findViewById(R.id.et_summercrop);
        tv_total_potential = findViewById(R.id.tv_total_potential);
        et_seed_usage = findViewById(R.id.et_seed_usage);
        regionSpinner = findViewById(R.id.spin_region);
        tv_total_market_potential_volume = findViewById(R.id.tv_total_market_potential_volume);
        et_nsl_sale = findViewById(R.id.et_nsl_sale);
        et_top_company1_name = findViewById(R.id.et_top_company1_name);
        et_company1_qty = findViewById(R.id.et_company1_qty);
        et_top_company2_name = findViewById(R.id.et_top_company2_name);
        et_company2_qty = findViewById(R.id.et_company2_qty);
        et_top_company3_name = findViewById(R.id.et_top_company3_name);
        et_company3_qty = findViewById(R.id.et_company3_qty);
        et_top_company4_name = findViewById(R.id.et_top_company4_name);
        et_company4_qty = findViewById(R.id.et_company4_qty);
        et_top_company5_name = findViewById(R.id.et_top_company5_name);
        et_company5_qty = findViewById(R.id.et_company5_qty);
        save_market_potential = findViewById(R.id.save_market_potential);
        tv_company1_qty = findViewById(R.id.tv_company1_qty);
        tv_company2_qty = findViewById(R.id.tv_company2_qty);
        tv_company3_qty = findViewById(R.id.tv_company3_qty);
        tv_company4_qty = findViewById(R.id.tv_company4_qty);
        tv_company5_qty = findViewById(R.id.tv_company5_qty);
        tv_nsl_sale = findViewById(R.id.tv_nsl_sale);
        tv_total_market = findViewById(R.id.tv_total_market);
        tv_seed_usage = findViewById(R.id.tv_seed_usage);
        setRegionsData();
        et_district.requestFocus();
        autoCompleteDistrictAdapter(new ArrayList<SelectedCities>());
        myRunnable1 = new MyRunnable1();
        divisionMarketAdapter = new ArrayList<String>();
        divisionMarketAdapter.add("Select Division");

        cropMarketadapter = new ArrayList<>();
        cropMarketadapter.add("Select Crop");
        String query = "SELECT DISTINCT ud." + KEY_TABLE_USER_COMPANY_DIVISION_DIVISION_ID + ",d." + KEY_TABLE_DIVISION_NAME + " FROM " + TABLE_USER_COMPANY_DIVISION + " ud INNER JOIN " + TABLE_DIVISION + " d ON ud." + KEY_TABLE_USER_COMPANY_DIVISION_DIVISION_ID + " = d." + KEY_TABLE_DIVISION_MASTER_ID + " WHERE ud." + KEY_TABLE_USER_COMPANY_DIVISION_USER_ID + " in (" + Common.getTeamFromSP(this) + ")";
        Common.Log.i("Query " + query);
        Cursor cursor = db.getWritableDatabase().rawQuery(query, null);


        if (cursor.moveToFirst()) {
            do {
                SelectedCities selectedCities = new SelectedCities();
                selectedCities.setCityId(cursor.getString(cursor.getColumnIndex(KEY_TABLE_USER_COMPANY_DIVISION_DIVISION_ID)));
                selectedCities.setCityName(cursor.getString(cursor.getColumnIndex(KEY_TABLE_DIVISION_NAME)));
                divisionMarketAdapter.add(cursor.getString(cursor.getColumnIndex(KEY_TABLE_DIVISION_NAME)));
                divisions.add(selectedCities);
            } while (cursor.moveToNext());
        }


        spin_division.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, R.id.customSpinnerItemTextView, divisionMarketAdapter));

        spin_division.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    divisionId = divisions.get(position - 1).getCityId();

                    if (divisionMarketAdapter.get(position).equalsIgnoreCase("FIELD CROPS")) {
                        tv_seed_usage.setText("Seed Usage Quantity/ Acre" + " " + "(Kgs)");
                        tv_total_market.setText("Total Market Potential Volume" + " " + "(Quintals)");
                        tv_nsl_sale.setText("NSL Sale" + " " + "(Quintals)");
                        tv_company1_qty.setText("Company 1 Qty" + " " + "(Quintals)");
                        tv_company2_qty.setText("Company 2 Qty" + " " + "(Quintals)");
                        tv_company3_qty.setText("Company 3 Qty" + " " + "(Quintals)");
                        tv_company4_qty.setText("Company 4 Qty" + " " + "(Quintals)");
                        tv_company5_qty.setText("Company 5 Qty" + " " + "(Quintals)");

                    } else if (divisionMarketAdapter.get(position).equalsIgnoreCase("COTTON")) {
                        tv_seed_usage.setText("Seed Usage Quantity/ Acre" + " " + "(Pkts)");
                        tv_total_market.setText("Total Market Potential Volume" + " " + "(Pkts)");
                        tv_nsl_sale.setText("NSL Sale" + " " + "(Pkts)");
                        tv_company1_qty.setText("Company 1 Qty" + " " + "(Pkts)");
                        tv_company2_qty.setText("Company 2 Qty" + " " + "(Pkts)");
                        tv_company3_qty.setText("Company 3 Qty" + " " + "(Pkts)");
                        tv_company4_qty.setText("Company 4 Qty" + " " + "(Pkts)");
                        tv_company5_qty.setText("Company 5 Qty" + " " + "(Pkts)");

                    } else if (divisionMarketAdapter.get(position).equalsIgnoreCase("VEGETABLES")) {
                        tv_seed_usage.setText("Seed Usage Quantity/ Acre" + " " + "(gms)");
                        tv_total_market.setText("Total Market Potential Volume" + " " + "(Kgs)");
                        tv_nsl_sale.setText("NSL Sale" + " " + "(Kgs)");
                        tv_company1_qty.setText("Company 1 Qty" + " " + "(Kgs)");
                        tv_company2_qty.setText("Company 2 Qty" + " " + "(Kgs)");
                        tv_company3_qty.setText("Company 3 Qty" + " " + "(Kgs)");
                        tv_company4_qty.setText("Company 4 Qty" + " " + "(Kgs)");
                        tv_company5_qty.setText("Company 5 Qty" + " " + "(Kgs)");

                    }
                    divisionName = divisions.get(position - 1).getCityName();
                    calculateMarketPotentialVolume();
                    String cropQuery = "SELECT DISTINCT cr." + KEY_TABLE_CROPS_CROP_ID + ",cr." + KEY_TABLE_CROPS_CROP_NAME + " FROM " + TABLE_CROPS + " cr WHERE cr." + KEY_TABLE_CROPS_DIVISION_ID + " = " + divisionId;
                    Common.Log.i("CROP QUERY " + cropQuery);
                    Cursor cropCursor = db.getWritableDatabase().rawQuery(cropQuery, null);
                    cropMarketadapter.clear();
                    crops.clear();
                    cropMarketadapter.add("Select Crop");
                    if (cropCursor.moveToFirst()) {
                        do {
                            SelectedCities crop = new SelectedCities();
                            crop.setCityId(cropCursor.getString(cropCursor.getColumnIndex(KEY_TABLE_CROPS_CROP_ID)));
                            crop.setCityName(cropCursor.getString(cropCursor.getColumnIndex(KEY_TABLE_CROPS_CROP_NAME)));
                            cropMarketadapter.add(cropCursor.getString(cropCursor.getColumnIndex(KEY_TABLE_CROPS_CROP_NAME)));
                            crops.add(crop);
                        } while (cropCursor.moveToNext());
                    }
                    spin_crop.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, R.id.customSpinnerItemTextView, cropMarketadapter));


                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spin_crop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    cropId = crops.get(position - 1).getCityId();
                    cropName = crops.get(position - 1).getCityName();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        save_market_potential.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isValidated())
                    return;
            }
        });

        et_kharifcrop.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String kharifStr = editable.toString().trim();
                if (kharifStr.length() > 0) {
                    kharif = Double.parseDouble(kharifStr);
                } else {
                    kharif = 0;
                }
                totalPotential = kharif + rabi + summer;
                tv_total_potential.setText(String.valueOf(Common.round(totalPotential,3)));
                calculateMarketPotentialVolume();
            }
        });

        et_rabicrop.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String rabiStr = editable.toString().trim();
                if (rabiStr.length() > 0) {
                    rabi = Double.parseDouble(rabiStr);
                } else {
                    rabi = 0;
                }
                totalPotential = kharif + rabi + summer;
                tv_total_potential.setText(String.valueOf(Common.round(totalPotential,3)));
                calculateMarketPotentialVolume();
            }
        });

        et_seed_usage.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                calculateMarketPotentialVolume();
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
                districtId = "0";
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

        /*et_district.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (et_district.getText().length() == 0) {
                    //   auto_tv.setAdapter(adapter);
                    et_district.showDropDown();
                }
            }
        });*/

        et_summercrop.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                String summerStr = editable.toString().trim();
                if (summerStr.length() > 0) {
                    summer = Double.parseDouble(summerStr);
                } else {
                    summer = 0;
                }
                totalPotential = kharif + rabi + summer;
                tv_total_potential.setText(String.valueOf(Common.round(totalPotential,3)));
                calculateMarketPotentialVolume();
            }
        });
    }

    public void autoCompleteDistrictAdapter(List<SelectedCities> list) {
        final CustomListAdapter adapter = new CustomListAdapter(this,
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
                districtId = item.getCityId();
                et_district.dismissDropDown();
                Common.hideSoftKeyboard(MarketPotentialActivity.this);

            }
        });

    }

    private boolean isValidated() {
        if (regionId.equalsIgnoreCase("0")) {
            Toast.makeText(this, "Please Select Region", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (districtId.equalsIgnoreCase("0")) {
            et_district.requestFocus();
            et_district.setError("Please Choose District");
            return false;
        }
        if (et_taluka.getText().toString().isEmpty()) {
            et_taluka.requestFocus();
            et_taluka.setError("Please Enter Taluka");
            return false;
        }
        if (et_village.getText().toString().isEmpty()) {
            et_village.requestFocus();
            et_village.setError("Please Enter Village");
            return false;
        }
        if (spin_division.getSelectedItemPosition() == 0) {
            Toast.makeText(getApplicationContext(), "Please Select Division", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (spin_crop.getSelectedItemPosition() == 0) {
            Toast.makeText(getApplicationContext(), "Please Select Crop", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (et_kharifcrop.getText().toString().isEmpty()) {
            et_kharifcrop.requestFocus();
            et_kharifcrop.setError("Please Enter Kharif Crop Acerage in Village");
            return false;
        }
        if (et_rabicrop.getText().toString().isEmpty()) {
            et_rabicrop.requestFocus();
            et_rabicrop.setError("Please Enter Rabi Crop Acerage in Village");
            return false;
        }

        if (et_summercrop.getText().toString().isEmpty()) {
            et_summercrop.requestFocus();
            et_summercrop.setError("Please Enter Summer Crop Acerage in Village");
            return false;
        }
        if (tv_total_potential.getText().toString().isEmpty()) {
            tv_total_potential.requestFocus();
            tv_total_potential.setError("Total Potential Acerage Should not be Empty");
            return false;
        }
        if (et_seed_usage.getText().toString().isEmpty()) {
            et_seed_usage.requestFocus();
            et_seed_usage.setError("Please Enter Seed usage Quantity");
            return false;
        }
        if (tv_total_market_potential_volume.getText().toString().isEmpty()) {
            tv_total_market_potential_volume.requestFocus();
            tv_total_market_potential_volume.setError("Total Market Potential Volume should not be Empty");
            return false;
        }
        if (et_nsl_sale.getText().toString().isEmpty()) {
            et_nsl_sale.requestFocus();
            et_nsl_sale.setError("Please enter Nsl Sale");
            return false;
        }
        if (et_top_company1_name.getText().toString().isEmpty()) {
            et_top_company1_name.requestFocus();
            et_top_company1_name.setError("Please Enter Top Company 1 Name");
            return false;
        }
        if (et_company1_qty.getText().toString().isEmpty()) {
            et_company1_qty.requestFocus();
            et_company1_qty.setError("Please Enter Company 1 Qty");
            return false;
        }
        if (et_top_company2_name.getText().toString().isEmpty()) {
            et_top_company2_name.requestFocus();
            et_top_company2_name.setError("Please Enter Top Company 2 Name");
            return false;
        }
        if (et_company2_qty.getText().toString().isEmpty()) {
            et_company2_qty.requestFocus();
            et_company2_qty.setError("Please Enter Company 2 Qty");
            return false;
        }
        if (et_top_company3_name.getText().toString().isEmpty()) {
            et_top_company3_name.requestFocus();
            et_top_company3_name.setError("Please Enter Top Company 3 Name");
            return false;
        }
        if (et_company3_qty.getText().toString().isEmpty()) {
            et_company3_qty.requestFocus();
            et_company3_qty.setError("Please Enter Company 3 Qty");
            return false;
        }
        MarketPotential mp = new MarketPotential();
        mp.userId = Integer.parseInt(Common.getUserIdFromSP(MarketPotentialActivity.this));
        mp.regionId = regionId;
        mp.district = districtId;
        mp.taluka = et_taluka.getText().toString().trim();
        mp.village = et_village.getText().toString().trim();
        mp.divisionId = Integer.parseInt(divisionId);
        mp.cropId = Integer.parseInt(cropId);
        mp.kharifCropAcreage = et_kharifcrop.getText().toString().trim();
        mp.rabiCropAcreage = et_rabicrop.getText().toString().trim();
        mp.summerCropAcreage = et_summercrop.getText().toString().trim();
        mp.totalPotentialAcreage = tv_total_potential.getText().toString().trim();
        mp.seedUsageQuanity = et_seed_usage.getText().toString().trim();
        mp.totalMarketPotentialVolume = tv_total_market_potential_volume.getText().toString().trim();
        mp.nslSale = et_nsl_sale.getText().toString().trim();
        mp.topCompanyName1 = et_top_company1_name.getText().toString().trim();
        mp.company1Qty = et_company1_qty.getText().toString().trim();
        mp.topCompanyName2 = et_top_company2_name.getText().toString().trim();
        mp.company2Qty = et_company2_qty.getText().toString().trim();
        mp.topCompanyName3 = et_top_company3_name.getText().toString().trim();
        mp.company3Qty = et_company3_qty.getText().toString().trim();
        mp.topCompanyName4 = et_top_company4_name.getText().toString().trim();
        mp.company4Qty = et_company4_qty.getText().toString().trim();
        mp.topCompanyName5 = et_top_company5_name.getText().toString().trim();
        mp.company5Qty = et_company5_qty.getText().toString().trim();
        mp.cropName = cropName;
        mp.divisionName = divisionName;
        db.insertMarketPotential(mp);

        List<MarketPotential> marketPotentials = db.getofflineMarketPotentials(Common.getTeamFromSP(MarketPotentialActivity.this));
        MarketPotentialReqVo marketPotentialReqVo = new MarketPotentialReqVo();
        marketPotentialReqVo.marketPotential = marketPotentials;
        if (Common.haveInternet(MarketPotentialActivity.this))
            new RetrofitRequester(this).sendRequest(Constants.URL_INSERTING_MARKET_POTENTIAL, marketPotentialReqVo, true);
        else
            finish();
        return true;
    }

    private void setRegionsData() {
        List<Regions> regions = db.getRegionsByUserId(Common.getUserIdFromSP(this));
        if (regions != null && regions.size() > 0) {
            final List<SpinnerModel1> spinnerModel1s = new ArrayList<>();
            SpinnerModel1 selectModel = new SpinnerModel1();
            selectModel.id = "0";
            selectModel.name = "Select Region";
            spinnerModel1s.add(selectModel);
            for (int i = 0; i < regions.size(); i++) {
                SpinnerModel1 spinnerModel1 = new SpinnerModel1();
                spinnerModel1.id = regions.get(i).getRegionMasterId();
                spinnerModel1.name = regions.get(i).getRegionName();
                spinnerModel1s.add(spinnerModel1);
            }
            final CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(getApplication(), R.layout.spinner_item, spinnerModel1s);
            regionSpinner.setAdapter(adapter);
            regionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    distObjs.clear();
                    regionId = spinnerModel1s.get(i).id;
                    districtId = "0";
                    et_district.setText("");
                    et_taluka.setText("");
                    et_village.setText("");
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
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
                    db.updateTable(KEY_MARKET_POTENTIAL_FFM_ID, TABLE_MARKET_POTENTIAL, KEY_MARKET_POTENTIAL_PRIMARY_ID, formResponse.sqliteId, formResponse.ffmId);
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

    public void calculateMarketPotentialVolume() {
        if (et_seed_usage.getText().toString().trim().length() > 0 && divisionName != null) {
            double result = 0;
            if (divisionName.equalsIgnoreCase("COTTON")) {
                result = totalPotential * Double.parseDouble(et_seed_usage.getText().toString().trim());
            } else if (divisionName.equalsIgnoreCase("FIELD CROPS")) {
                result = (totalPotential * Double.parseDouble(et_seed_usage.getText().toString().trim())) / 100;
            } else if (divisionName.equalsIgnoreCase("VEGETABLES")) {
                result = (totalPotential * Double.parseDouble(et_seed_usage.getText().toString().trim())) / 1000;
            }
            tv_total_market_potential_volume.setText(String.valueOf(Common.round(result,3)));
        }
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
                final List<District> districts = db.getDistrictsByRegionID(key, regionId);
                distObjs.clear();
                if (districts != null && districts.size() > 0) {
                    for (int i = 0; i < districts.size(); i++) {
                        SelectedCities obj = new SelectedCities();
                        obj.setCityId(districts.get(i).districtId);
                        obj.setCityName(districts.get(i).districtName);
                        distObjs.add(obj);
                    }
                }
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        if (!onDistrictItemClick && et_district.isEnabled()) {
                            districtId = "0";
                            autoCompleteDistrictAdapter(distObjs);
                        } else onDistrictItemClick = false;
                    }
                });
            }

        }
    }
}
