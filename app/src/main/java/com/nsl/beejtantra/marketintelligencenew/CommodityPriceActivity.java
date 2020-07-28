package com.nsl.beejtantra.marketintelligencenew;

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
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.matrix.Vector3;
import com.nsl.beejtantra.Constants;
import com.nsl.beejtantra.DatabaseHandler;
import com.nsl.beejtantra.Divisions;
import com.nsl.beejtantra.R;
import com.nsl.beejtantra.Regions;
import com.nsl.beejtantra.SelectedCities;
import com.nsl.beejtantra.SpinnerModel1;
import com.nsl.beejtantra.adapters.CustomListAdapter;
import com.nsl.beejtantra.adapters.CustomSpinnerAdapter;
import com.nsl.beejtantra.commonutils.Common;
import com.nsl.beejtantra.network.RetrofitRequester;
import com.nsl.beejtantra.network.RetrofitResponseListener;
import com.nsl.beejtantra.orderindent.NewSalesOrderChooseActivity;
import com.nsl.beejtantra.pojo.CommodityPrice;
import com.nsl.beejtantra.pojo.CommodityPriceReqVo;
import com.nsl.beejtantra.pojo.District;
import com.nsl.beejtantra.pojo.FormResponse;
import com.nsl.beejtantra.pojo.FormResponseResVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.nsl.beejtantra.DatabaseHandler.KEY_COMP_CHANNEL_FFM_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_COMP_CHANNEL_PRIMARY_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_CP_FFM_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_CP_PRIMARY_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CROPS_CROP_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CROPS_CROP_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CROPS_DIVISION_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_DIVISION_CODE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_DIVISION_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_DIVISION_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USER_COMPANY_DIVISION_COMPANY_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USER_COMPANY_DIVISION_DIVISION_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USER_COMPANY_DIVISION_USER_ID;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_COMMODITY_PRICE;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_COMPETITOR_CHANNEL;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_CROPS;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_DIVISION;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_USER_COMPANY_DIVISION;

public class CommodityPriceActivity extends AppCompatActivity implements RetrofitResponseListener {

    TextView save_commodity_price, tv_apmc_mandi_price;
    EditText et_taluka, et_village, et_commodityprice_segment, et_commodityprice_apmc_mandiprice, et_commodityprice_dealer, et_commodityprice_purchase;
    Spinner regionSpinner, spin_commodityprice_division, spin_commodityprice_crop;
    ArrayList<String> divisionCommodityAdapter;
    ArrayList<String> cropCommodityAdapter;
    AutoCompleteTextView et_district;
    private String regionId = "0";
    private String districtId = "0";
    private boolean onDistrictItemClick;
    private MyRunnable1 myRunnable1;
    private Handler handler;
    List<SelectedCities> distObjs = new ArrayList<>();
    DatabaseHandler db;
    private ProgressDialog progressDialog;
    private ArrayList<SelectedCities> arlist_divisions;
    private SQLiteDatabase sdbw;
    private String sel_division_id, sel_crop_id;
    private String divisionname, cropname;
    private List<SelectedCities> arlist_crops;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mi_activity_commodity_price);
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

        et_district = findViewById(R.id.et_commodityprice_district);
        et_taluka = findViewById(R.id.et_commodityprice_taluka);
        et_village = findViewById(R.id.et_commodityprice_village);
        spin_commodityprice_division = findViewById(R.id.spin_commodityprice_division);
        regionSpinner = findViewById(R.id.spin_region);
        spin_commodityprice_crop = findViewById(R.id.spin_commodityprice_crop);
        et_commodityprice_segment = findViewById(R.id.et_commodityprice_segment);
        et_commodityprice_apmc_mandiprice = findViewById(R.id.et_commodityprice_apmc_mandiprice);
        et_commodityprice_dealer = findViewById(R.id.et_commodityprice_dealer);
        et_commodityprice_purchase = findViewById(R.id.et_commodityprice_purchase);
        tv_apmc_mandi_price = findViewById(R.id.tv_apmc_mandi_price);
        save_commodity_price = findViewById(R.id.save_commodity_price);

        setRegionsData();
        et_district.requestFocus();
        autoCompleteDistrictAdapter(new ArrayList<SelectedCities>());
        myRunnable1 = new MyRunnable1();
        new AsyncDivisionsoffline().execute();

//        cropCommodityAdapter = new ArrayList<>();
//        cropCommodityAdapter.add("Select Crop");


        spin_commodityprice_crop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sel_crop_id = arlist_crops.get(position).getCityId();
                if (sel_crop_id.equalsIgnoreCase("0")) {

                } else {
                    cropname = arlist_crops.get(position).getCityName();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

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

        save_commodity_price.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Validated())
                    return;
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
                Common.hideSoftKeyboard(CommodityPriceActivity.this);

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
                    db.updateTable(KEY_CP_FFM_ID, TABLE_COMMODITY_PRICE, KEY_CP_PRIMARY_ID, formResponse.sqliteId, formResponse.ffmId);
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

    public class AsyncDivisionsoffline extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(CommodityPriceActivity.this);
            progressDialog.setMessage("Please wait ...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            arlist_divisions = new ArrayList<SelectedCities>();
            divisionCommodityAdapter = new ArrayList<String>();
            arlist_divisions.clear();

            SelectedCities citieszzy = new SelectedCities();
            citieszzy.setCityId("0");
            citieszzy.setCityName("Select Division");
            // System.out.println("city id is :" + cityId + "city name is :" + cityName);
            arlist_divisions.add(citieszzy);
            divisionCommodityAdapter.add("Select Division");

            try {

                String selectQuery = "SELECT  " + "CR." + KEY_TABLE_DIVISION_MASTER_ID + "," + KEY_TABLE_DIVISION_NAME + "," + KEY_TABLE_DIVISION_CODE + " FROM " + TABLE_USER_COMPANY_DIVISION + " AS CDC JOIN " + TABLE_DIVISION + " AS CR ON CDC." + KEY_TABLE_USER_COMPANY_DIVISION_DIVISION_ID + " = CR." + KEY_TABLE_DIVISION_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_DIVISION_USER_ID + " in ( " + db.getTeam(Common.getUserIdFromSP(CommodityPriceActivity.this)) + " ) " + " group by(CR." + KEY_TABLE_DIVISION_MASTER_ID + ")";
//                String selectQuery = "SELECT  " + "CR." + KEY_TABLE_DIVISION_MASTER_ID + "," + KEY_TABLE_DIVISION_NAME + "," + KEY_TABLE_DIVISION_CODE + " FROM " + TABLE_USER_COMPANY_DIVISION + " AS CDC JOIN " + TABLE_DIVISION + " AS CR ON CDC." + KEY_TABLE_USER_COMPANY_DIVISION_DIVISION_ID + " = CR." + KEY_TABLE_DIVISION_MASTER_ID + " JOIN " + TABLE_SEASON_LINE_ITEMS + " AS SE ON CDC." + KEY_TABLE_USER_COMPANY_DIVISION_DIVISION_ID + " = SE." + KEY_SEASON_LINE_ITEM_DIVISION_ID + "  where " + KEY_TABLE_USER_COMPANY_DIVISION_USER_ID + " in ( " + db.getTeam(sel_userId) + " ) and " + KEY_TABLE_USER_COMPANY_DIVISION_COMPANY_ID + " = " + sel_company_id + " group by(CR." + KEY_TABLE_DIVISION_MASTER_ID + ")";
                Log.e("DIVISIONS QUERY", selectQuery);
                sdbw = db.getWritableDatabase();

                Cursor cursor = sdbw.rawQuery(selectQuery, null);
                //System.out.println("cursor count "+cursor.getCount());
                if (cursor.moveToFirst()) {
                    do {

                        Divisions divisions = new Divisions();
                        divisions.setDivMasterID(cursor.getString(0));
                        divisions.setDivName(cursor.getString(1));

                        SelectedCities cities3 = new SelectedCities();
                        cities3.setCityId(cursor.getString(0));
                        cities3.setCityName(cursor.getString(1));

                        arlist_divisions.add(cities3);
                        divisionCommodityAdapter.add(cursor.getString(1));

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

            spin_commodityprice_division.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, R.id.customSpinnerItemTextView, divisionCommodityAdapter));
            spin_commodityprice_division.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    sel_division_id = arlist_divisions.get(position).getCityId();
                    if (sel_division_id.equalsIgnoreCase("0")) {

                    } else {
                        divisionname = arlist_divisions.get(position).getCityName();
                        if (divisionCommodityAdapter.get(position).equalsIgnoreCase("COTTON")) {
                            tv_apmc_mandi_price.setText("APMC Mandi Price" + " " + "Quintals");

                        } else if (divisionCommodityAdapter.get(position).equalsIgnoreCase("FIELD CROPS")) {
                            tv_apmc_mandi_price.setText("APMC Mandi Price" + " " + "Quintals");

                        } else if (divisionCommodityAdapter.get(position).equalsIgnoreCase("VEGETABLES")) {
                            tv_apmc_mandi_price.setText("APMC Mandi Price" + " " + "Kgs");
                        }

                    }
                    arlist_crops = new ArrayList<SelectedCities>();
                    cropCommodityAdapter = new ArrayList<String>();
                    arlist_crops.clear();
                    SelectedCities citieszzy = new SelectedCities();
                    citieszzy.setCityId("0");
                    citieszzy.setCityName("Select Crop");
                    // System.out.println("city id is :" + cityId + "city name is :" + cityName);
                    arlist_crops.add(citieszzy);
                    cropCommodityAdapter.add("Select Crop");
                    String query = "SELECT * FROM " + TABLE_CROPS + " WHERE " + KEY_TABLE_CROPS_DIVISION_ID + " = " + sel_division_id;
                    sdbw = db.getWritableDatabase();

                    Cursor cursor = sdbw.rawQuery(query, null);
                    //System.out.println("cursor count "+cursor.getCount());
                    if (cursor.moveToFirst()) {
                        do {


                            SelectedCities cities3 = new SelectedCities();
                            cities3.setCityId(cursor.getString(cursor.getColumnIndex(KEY_TABLE_CROPS_CROP_MASTER_ID)));
                            cities3.setCityName(cursor.getString(cursor.getColumnIndex(KEY_TABLE_CROPS_CROP_NAME)));

                            arlist_crops.add(cities3);
                            cropCommodityAdapter.add(cursor.getString(cursor.getColumnIndex(KEY_TABLE_CROPS_CROP_NAME)));

                        } while (cursor.moveToNext());
                    }
                    spin_commodityprice_crop.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, R.id.customSpinnerItemTextView, cropCommodityAdapter));
                    // do some stuff....
                }


                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
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

    private boolean Validated() {
        if (regionId.equalsIgnoreCase("0")) {
            Toast.makeText(this, "Please Select Region", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (districtId.equalsIgnoreCase("0")) {
            et_district.requestFocus();
            et_district.setError("Please Choose District");
//            isFilled =false;
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
        if (spin_commodityprice_division.getSelectedItemPosition() == 0) {
            Toast.makeText(getApplicationContext(), "Please Select Division", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (spin_commodityprice_crop.getSelectedItemPosition() == 0) {
            Toast.makeText(getApplicationContext(), "Please Select Crop", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (et_commodityprice_segment.getText().toString().isEmpty()) {
            et_commodityprice_segment.requestFocus();
            et_commodityprice_segment.setError("Please Enter Segment");
            return false;
        }

        if (et_commodityprice_dealer.getText().toString().isEmpty()) {
            et_commodityprice_dealer.requestFocus();
            et_commodityprice_dealer.setError("Please Enter Commodity Dealer / Commission Agent Price");
            return false;
        }

        CommodityPrice commodityPrice = new CommodityPrice();
        commodityPrice.userId = Integer.parseInt(Common.getUserIdFromSP(this));
        commodityPrice.regionId = Integer.parseInt(regionId);
        commodityPrice.district = Integer.parseInt(districtId);
        commodityPrice.taluka = et_taluka.getText().toString().trim();
        commodityPrice.village = et_village.getText().toString().trim();
        commodityPrice.divisionId = sel_division_id;
        commodityPrice.divisionName = divisionname;
        commodityPrice.cropId = sel_crop_id;
        commodityPrice.cropName = cropname;
        commodityPrice.segment = et_commodityprice_segment.getText().toString().trim();
        commodityPrice.apmcMandiPrice = et_commodityprice_apmc_mandiprice.getText().toString().trim();
        commodityPrice.commodityDealerCommissionAgentPrice = et_commodityprice_dealer.getText().toString().trim();
        commodityPrice.purchasePriceByIndustry = et_commodityprice_purchase.getText().toString().trim();
        db.insertCommodityPrice(commodityPrice);

        List<CommodityPrice> commodityPrices = db.getOfflineCommodityPrices(Common.getUserIdFromSP(this));
        CommodityPriceReqVo commodityPriceReqVo = new CommodityPriceReqVo();
        commodityPriceReqVo.commodityPrice = commodityPrices;
        if (Common.haveInternet(CommodityPriceActivity.this))
            new RetrofitRequester(this).sendRequest(Constants.URL_INSERTING_COMMODITY_PRICE, commodityPriceReqVo, true);
        else
            finish();
        return true;
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
