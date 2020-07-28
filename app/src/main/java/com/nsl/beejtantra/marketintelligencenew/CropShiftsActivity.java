package com.nsl.beejtantra.marketintelligencenew;

import android.app.ProgressDialog;
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
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

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
import com.nsl.beejtantra.pojo.CommodityPrice;
import com.nsl.beejtantra.pojo.CommodityPriceReqVo;
import com.nsl.beejtantra.pojo.CropShift;
import com.nsl.beejtantra.pojo.CropShiftReqVo;
import com.nsl.beejtantra.pojo.District;
import com.nsl.beejtantra.pojo.FormResponse;
import com.nsl.beejtantra.pojo.FormResponseResVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.nsl.beejtantra.DatabaseHandler.KEY_CP_FFM_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_CP_PRIMARY_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_CROP_SHIFTS_FFMID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_CROP_SHIFTS_PRIMARY_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CROPS_CROP_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CROPS_CROP_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CROPS_DIVISION_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_DIVISION_CODE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_DIVISION_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_DIVISION_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USER_COMPANY_DIVISION_DIVISION_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USER_COMPANY_DIVISION_USER_ID;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_COMMODITY_PRICE;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_CROPS;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_CROP_SHIFTS;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_DIVISION;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_USER_COMPANY_DIVISION;

public class CropShiftsActivity extends AppCompatActivity implements RetrofitResponseListener {

    TextView save_cropshifts, tv_cropshift_increase_decrease;
    EditText et_taluka, et_village, et_cropshift_segment, et_cropshift_previous_year_area, et_cropshift_current_year_area,
            et_cropshift_reason_crop_shifts, et_cropshift_previous_year_srr, et_cropshift_current_year_srr, et_cropshift_next_year_estimated_srr;
    Spinner regionSpinner, spin_cropshift_division, spin_cropshift_crop;
    AutoCompleteTextView et_district;
    ArrayList<String> divisionCropShiftAdapter;
    ArrayList<String> cropCropShiftadapter;
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
    private String sel_division_id;
    private String divisionname;
    private ArrayList<SelectedCities> arlist_crops;
    private String sel_crop_id;
    private String cropname;
    private double currentYear, prevYear;
    ArrayList<String> cropShiftList = new ArrayList<>();
    LinearLayout crop_shift_ll;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mi_activity_crop_shifts);
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

        et_district = findViewById(R.id.et_cropshift_district);
        et_taluka = findViewById(R.id.et_cropshift_taluka);
        et_village = findViewById(R.id.et_cropshift_village);
        regionSpinner = findViewById(R.id.spin_region);
        spin_cropshift_division = findViewById(R.id.spin_cropshift_division);
        spin_cropshift_crop = findViewById(R.id.spin_cropshift_crop);
        et_cropshift_segment = findViewById(R.id.et_cropshift_segment);
        et_cropshift_previous_year_area = findViewById(R.id.et_cropshift_previous_year_area);
        et_cropshift_current_year_area = findViewById(R.id.et_cropshift_current_year_area);
        tv_cropshift_increase_decrease = findViewById(R.id.tv_cropshift_increase_decrease);
        et_cropshift_reason_crop_shifts = findViewById(R.id.et_cropshift_reason_crop_shifts);
        et_cropshift_previous_year_srr = findViewById(R.id.et_cropshift_previous_year_srr);
        et_cropshift_current_year_srr = findViewById(R.id.et_cropshift_current_year_srr);
        et_cropshift_next_year_estimated_srr = findViewById(R.id.et_cropshift_next_year_estimated_srr);
        crop_shift_ll = findViewById(R.id.crop_shift_ll);
        save_cropshifts = findViewById(R.id.save_cropshifts);

        setRegionsData();
        et_district.requestFocus();
        autoCompleteDistrictAdapter(new ArrayList<SelectedCities>());
        myRunnable1 = new MyRunnable1();

        new AsyncDivisionsoffline().execute();
        cropShiftList.clear();
        cropShiftList.add("paddy");
        cropShiftList.add("bajra");
        cropShiftList.add("mustard");
        cropShiftList.add("wheat");

        et_cropshift_current_year_area.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    currentYear = Double.parseDouble(s.toString());
                } else {
                    currentYear = 0;
                }
                calculateIncreaseDecrease();
            }
        });
        et_cropshift_previous_year_area.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.toString().length() > 0) {
                    prevYear = Double.parseDouble(s.toString());
                } else {
                    prevYear = 0;
                }
                calculateIncreaseDecrease();
            }
        });
        spin_cropshift_crop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sel_crop_id = arlist_crops.get(position).getCityId();
                if (sel_crop_id.equalsIgnoreCase("0")) {

                } else {
                    cropname = arlist_crops.get(position).getCityName();
                }
                if ((cropname!=null && cropShiftList.contains(cropname.toLowerCase())) || (divisionname!=null && divisionname.equalsIgnoreCase("Vegetables"))) {
                    crop_shift_ll.setVisibility(View.VISIBLE);
                } else {
                    crop_shift_ll.setVisibility(View.GONE);
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
        save_cropshifts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Validated())
                    return;
            }
        });
    }

    private void calculateIncreaseDecrease() {
        if (currentYear > 0 && prevYear > 0) {
            double increaseDecreaseValue = ((currentYear - prevYear) / prevYear) * 100;
            tv_cropshift_increase_decrease.setText(String.valueOf(Common.round(increaseDecreaseValue, 3)));
        } else {
            tv_cropshift_increase_decrease.setText("");
        }
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
                Common.hideSoftKeyboard(CropShiftsActivity.this);

            }
        });

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
                    db.updateTable(KEY_CROP_SHIFTS_FFMID, TABLE_CROP_SHIFTS, KEY_CROP_SHIFTS_PRIMARY_ID, formResponse.sqliteId, formResponse.ffmId);
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
            progressDialog = new ProgressDialog(CropShiftsActivity.this);
            progressDialog.setMessage("Please wait ...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            arlist_divisions = new ArrayList<SelectedCities>();
            divisionCropShiftAdapter = new ArrayList<String>();
            arlist_divisions.clear();

            SelectedCities citieszzy = new SelectedCities();
            citieszzy.setCityId("0");
            citieszzy.setCityName("Select Division");
            // System.out.println("city id is :" + cityId + "city name is :" + cityName);
            arlist_divisions.add(citieszzy);
            divisionCropShiftAdapter.add("Select Division");

            try {

                String selectQuery = "SELECT  " + "CR." + KEY_TABLE_DIVISION_MASTER_ID + "," + KEY_TABLE_DIVISION_NAME + "," + KEY_TABLE_DIVISION_CODE + " FROM " + TABLE_USER_COMPANY_DIVISION + " AS CDC JOIN " + TABLE_DIVISION + " AS CR ON CDC." + KEY_TABLE_USER_COMPANY_DIVISION_DIVISION_ID + " = CR." + KEY_TABLE_DIVISION_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_DIVISION_USER_ID + " in ( " + db.getTeam(Common.getUserIdFromSP(CropShiftsActivity.this)) + " ) " + " group by(CR." + KEY_TABLE_DIVISION_MASTER_ID + ")";
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
                        divisionCropShiftAdapter.add(cursor.getString(1));

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

            spin_cropshift_division.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, R.id.customSpinnerItemTextView, divisionCropShiftAdapter));
            spin_cropshift_division.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    sel_division_id = arlist_divisions.get(position).getCityId();
                    if (sel_division_id.equalsIgnoreCase("0")) {

                    } else {
                        divisionname = arlist_divisions.get(position).getCityName();

                    }
                    if (divisionname!=null && divisionname.equalsIgnoreCase("Vegetables")) {
                        crop_shift_ll.setVisibility(View.VISIBLE);
                    } else {
                        crop_shift_ll.setVisibility(View.GONE);
                    }
                    arlist_crops = new ArrayList<SelectedCities>();
                    cropCropShiftadapter = new ArrayList<String>();
                    arlist_crops.clear();
                    SelectedCities citieszzy = new SelectedCities();
                    citieszzy.setCityId("0");
                    citieszzy.setCityName("Select Crop");
                    // System.out.println("city id is :" + cityId + "city name is :" + cityName);
                    arlist_crops.add(citieszzy);
                    cropCropShiftadapter.add("Select Crop");
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
                            cropCropShiftadapter.add(cursor.getString(cursor.getColumnIndex(KEY_TABLE_CROPS_CROP_NAME)));

                        } while (cursor.moveToNext());
                    }
                    spin_cropshift_crop.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, R.id.customSpinnerItemTextView, cropCropShiftadapter));
                    // do some stuff....
                }


                @Override
                public void onNothingSelected(AdapterView<?> parent) {

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
        if (spin_cropshift_division.getSelectedItemPosition() == 0) {
            Toast.makeText(getApplicationContext(), "Please Select Division", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (spin_cropshift_crop.getSelectedItemPosition() == 0) {
            Toast.makeText(getApplicationContext(), "Please Select Crop", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (et_cropshift_segment.getText().toString().isEmpty()) {
            et_cropshift_segment.requestFocus();
            et_cropshift_segment.setError("Please Enter Segment");
            return false;
        }

        if (et_cropshift_previous_year_area.getText().toString().isEmpty()) {
            et_cropshift_previous_year_area.requestFocus();
            et_cropshift_previous_year_area.setError("Please Enter Previous Year Area");
            return false;
        }

        if (et_cropshift_current_year_area.getText().toString().isEmpty()) {
            et_cropshift_current_year_area.requestFocus();
            et_cropshift_current_year_area.setError("Please Enter Current Year Area");
            return false;
        }

        if (tv_cropshift_increase_decrease.getText().toString().isEmpty()) {
            tv_cropshift_increase_decrease.requestFocus();
            tv_cropshift_increase_decrease.setError("Please Enter Increase / Decrease");
            return false;
        }

        if (et_cropshift_reason_crop_shifts.getText().toString().isEmpty()) {
            et_cropshift_reason_crop_shifts.requestFocus();
            et_cropshift_reason_crop_shifts.setError("Please Enter Reason for Crop Shift");
            return false;
        }
        if (crop_shift_ll.getVisibility() == View.VISIBLE) {
            if (et_cropshift_previous_year_srr.getText().toString().isEmpty()) {
                et_cropshift_previous_year_srr.requestFocus();
                et_cropshift_previous_year_srr.setError("Please Enter Previous Year SRR");
                return false;
            }

            if (et_cropshift_current_year_srr.getText().toString().isEmpty()) {
                et_cropshift_current_year_srr.requestFocus();
                et_cropshift_current_year_srr.setError("Please Enter Current Year SRR");
                return false;
            }

            if (et_cropshift_next_year_estimated_srr.getText().toString().isEmpty()) {
                et_cropshift_next_year_estimated_srr.requestFocus();
                et_cropshift_next_year_estimated_srr.setError("Please Enter Next Year Estimates SRR");
                return false;
            }
        }

        CropShift cropShift = new CropShift();
        cropShift.userId = Integer.parseInt(Common.getUserIdFromSP(this));
        cropShift.district = Integer.parseInt(districtId);
        cropShift.taluka = et_taluka.getText().toString().trim();
        cropShift.village = et_village.getText().toString().trim();
        cropShift.divisionId = sel_division_id;
        cropShift.divisionName = divisionname;
        cropShift.cropId = sel_crop_id;
        cropShift.cropName = cropname;
        cropShift.segment = et_cropshift_segment.getText().toString().trim();
        cropShift.previousYearAreaInAcres = et_cropshift_previous_year_area.getText().toString().trim();
        cropShift.currentYearAreaInAcres = et_cropshift_current_year_area.getText().toString().trim();
        cropShift.percentageIncreaseOrDecrease = tv_cropshift_increase_decrease.getText().toString().trim();
        cropShift.reasonForCropShift = et_cropshift_reason_crop_shifts.getText().toString().trim();
        cropShift.previousYearSrr = et_cropshift_previous_year_srr.getText().toString().trim();
        cropShift.currentYearSrr = et_cropshift_current_year_srr.getText().toString().trim();
        cropShift.nextYearEstimatedSrr = et_cropshift_next_year_estimated_srr.getText().toString().trim();
        db.insertCropShift(cropShift);
        List<CropShift> cropShifts = db.getOfflineCropShifts(Common.getUserIdFromSP(this));
        CropShiftReqVo cropShiftReqVo = new CropShiftReqVo();
        cropShiftReqVo.cropShifts = cropShifts;
        if (Common.haveInternet(CropShiftsActivity.this))
            new RetrofitRequester(this).sendRequest(Constants.URL_INSERTING_CROP_SHIFTS, cropShiftReqVo, true);
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
