package com.nsl.beejtantra.marketintelligencenew;

import android.app.ProgressDialog;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nsl.beejtantra.Constants;
import com.nsl.beejtantra.DatabaseHandler;
import com.nsl.beejtantra.R;
import com.nsl.beejtantra.Regions;
import com.nsl.beejtantra.SelectedCities;
import com.nsl.beejtantra.SpinnerModel1;
import com.nsl.beejtantra.adapters.CustomListAdapter;
import com.nsl.beejtantra.adapters.CustomSpinnerAdapter;
import com.nsl.beejtantra.commonutils.Common;
import com.nsl.beejtantra.network.RetrofitRequester;
import com.nsl.beejtantra.network.RetrofitResponseListener;
import com.nsl.beejtantra.pojo.CompetitorStrength;
import com.nsl.beejtantra.pojo.CompetitorStrengthReqVo;
import com.nsl.beejtantra.pojo.District;
import com.nsl.beejtantra.pojo.FormResponse;
import com.nsl.beejtantra.pojo.FormResponseResVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.nsl.beejtantra.DatabaseHandler.KEY_COMPETITOR_STRENGTH_FFM_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_COMPETITOR_STRENGTH_PRIMARY_ID;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_COMPETITOR_STRENGTH;

public class CompetitorStrengthActivity extends AppCompatActivity implements RetrofitResponseListener {

    TextView save_competitor_strength;
    EditText et_cterritory, et_competitor_companyname, et_business_covering_villagese, et_farmer_clubs, et_demo_plots, et_temporary_fas_counter_boys,
            et_permanent_fa, et_company_staff, et_fds_couducted, et_mfds_conducted, et_newproduct_minikit_trail_plots, et_workshops_conducted, et_product_solds;
    AutoCompleteTextView et_cdistrict;
    Spinner regionSpinner;
    private List<SelectedCities> distObjs = new ArrayList<>();
    private boolean onDistrictItemClick;
    private String districtId = "0", regionId = "0";
    private MyRunnable1 myRunnable1;
    DatabaseHandler db;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mi_activity_competitor_strength);
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


        et_cdistrict = findViewById(R.id.et_cdistrict);
        et_cterritory = findViewById(R.id.et_cterritory);
        regionSpinner = findViewById(R.id.spin_region);
        et_competitor_companyname = findViewById(R.id.et_competitor_companyname);
        et_business_covering_villagese = findViewById(R.id.et_business_covering_villagese);
        et_product_solds = findViewById(R.id.tv_product_solds);
        et_farmer_clubs = findViewById(R.id.et_farmer_clubs);
        et_demo_plots = findViewById(R.id.et_demo_plots);
        et_temporary_fas_counter_boys = findViewById(R.id.et_temporary_fas_counter_boys);
        et_permanent_fa = findViewById(R.id.et_permanent_fa);
        et_company_staff = findViewById(R.id.et_company_staff);
        et_fds_couducted = findViewById(R.id.et_fds_couducted);
        et_mfds_conducted = findViewById(R.id.et_mfds_conducted);
        et_newproduct_minikit_trail_plots = findViewById(R.id.et_newproduct_minikit_trail_plots);
        et_workshops_conducted = findViewById(R.id.et_workshops_conducted);
        save_competitor_strength = findViewById(R.id.save_competitor_strength);

        setRegionsData();
        autoCompleteDistrictAdapter(new ArrayList<SelectedCities>());
        myRunnable1 = new MyRunnable1();
        et_cdistrict.addTextChangedListener(new TextWatcher() {
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
                    et_cdistrict.showDropDown();
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
        save_competitor_strength.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isValidated())
                    return;
            }
        });
    }

    public void autoCompleteDistrictAdapter(List<SelectedCities> list) {
        final CustomListAdapter adapter = new CustomListAdapter(this,
                R.layout.autocomplete_custom_layout, list);

        et_cdistrict.setAdapter(adapter);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                et_cdistrict.showDropDown();
            }
        }, 100);

        adapter.setOnItemClickListener(new CustomListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, SelectedCities item) {
                onDistrictItemClick = true;
                et_cdistrict.setText(item.getCityName().toUpperCase());
                et_cdistrict.setSelection(item.getCityName().length());
                districtId = item.getCityId();
                et_cdistrict.dismissDropDown();
                Common.hideSoftKeyboard(CompetitorStrengthActivity.this);

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
                    et_cdistrict.setText("");
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
    }

    private boolean isValidated() {
        if (regionId.equalsIgnoreCase("0")) {
            Toast.makeText(this, "Please Select Region", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (districtId.equalsIgnoreCase("0")) {
            et_cdistrict.requestFocus();
            et_cdistrict.setError("Please Choose District");
            return false;
        }
        if (et_cterritory.getText().toString().isEmpty()) {
            et_cterritory.requestFocus();
            et_cterritory.setError("Please Enter Territory");
            return false;
        }
        if (et_competitor_companyname.getText().toString().isEmpty()) {
            et_competitor_companyname.requestFocus();
            et_competitor_companyname.setError("Please Enter Competitor Company Name");
            return false;
        }
        if (et_business_covering_villagese.getText().toString().isEmpty()) {
            et_business_covering_villagese.requestFocus();
            et_business_covering_villagese.setError("Please Enter Business Covering Villages");
            return false;
        }
        if (et_farmer_clubs.getText().toString().isEmpty()) {
            et_farmer_clubs.requestFocus();
            et_farmer_clubs.setError("Please Enter No. of Farmer Clubs");
            return false;
        }
        if (et_demo_plots.getText().toString().isEmpty()) {
            et_demo_plots.requestFocus();
            et_demo_plots.setError("Please Enter No. of Demo Plots");
            return false;
        }
        if (et_temporary_fas_counter_boys.getText().toString().isEmpty()) {
            et_temporary_fas_counter_boys.requestFocus();
            et_temporary_fas_counter_boys.setError("Please Enter No.of Temporary FAs/ Counter Boys");
            return false;
        }
        if (et_permanent_fa.getText().toString().isEmpty()) {
            et_permanent_fa.requestFocus();
            et_permanent_fa.setError("Please Enter No. of Permanent FA");
            return false;
        }
        if (et_company_staff.getText().toString().isEmpty()) {
            et_company_staff.requestFocus();
            et_company_staff.setError("Please Enter No. of Company Staff");
            return false;
        }

        if (et_fds_couducted.getText().toString().isEmpty()) {
            et_fds_couducted.requestFocus();
            et_fds_couducted.setError("Please Enter No. of FDs Conducted");
            return false;
        }
        if (et_mfds_conducted.getText().toString().isEmpty()) {
            et_mfds_conducted.requestFocus();
            et_mfds_conducted.setError("Please Enter No. of MFDs Conducted");
            return false;
        }
        if (et_newproduct_minikit_trail_plots.getText().toString().isEmpty()) {
            et_newproduct_minikit_trail_plots.requestFocus();
            et_newproduct_minikit_trail_plots.setError("Please Enter No. of New Product Minikit Trail Plots");
            return false;
        }
        if (et_workshops_conducted.getText().toString().isEmpty()) {
            et_workshops_conducted.requestFocus();
            et_workshops_conducted.setError("Please Enter No. of Workshop Conducted");
            return false;
        }

        CompetitorStrength cs = new CompetitorStrength();
        cs.userId = Integer.parseInt(Common.getUserIdFromSP(this));
        cs.district = districtId;
        cs.territory = et_cterritory.getText().toString().trim();
        cs.competitorCompanyName = et_competitor_companyname.getText().toString().trim();
        cs.businessCoveringVillages = et_business_covering_villagese.getText().toString().trim();
        cs.noOfProductsSold = et_product_solds.getText().toString().trim();
        cs.noOfFarmerClubs = et_farmer_clubs.getText().toString().trim();
        cs.noOfDemoPlots = et_demo_plots.getText().toString().trim();
        cs.noOfTemporaryFasCounterBoys = et_temporary_fas_counter_boys.getText().toString().trim();
        cs.noOfPermanentFa = et_permanent_fa.getText().toString().trim();
        cs.noOfCompanyStaff = et_company_staff.getText().toString().trim();
        cs.noOfFdsConducted = et_fds_couducted.getText().toString().trim();
        cs.noOfMfdsConducted = et_mfds_conducted.getText().toString().trim();
        cs.noOfNewProductMinikitTrailPlots = et_newproduct_minikit_trail_plots.getText().toString().trim();
        cs.noOfWorkshopsConducted = et_workshops_conducted.getText().toString().trim();
        db.insertCompetitorStrength(cs);
        List<CompetitorStrength> competitorStrengths = db.getOfflineCompetitorStrengths(Common.getUserIdFromSP(this));
        CompetitorStrengthReqVo competitorStrengthReqVo = new CompetitorStrengthReqVo();
        competitorStrengthReqVo.competitorStrength = competitorStrengths;
        if (Common.haveInternet(CompetitorStrengthActivity.this))
            new RetrofitRequester(this).sendRequest(Constants.URL_INSERTING_COMPETITOR_STRENGTH, competitorStrengthReqVo, true);
        else
            finish();
        return true;

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
                    db.updateTable(KEY_COMPETITOR_STRENGTH_FFM_ID, TABLE_COMPETITOR_STRENGTH, KEY_COMPETITOR_STRENGTH_PRIMARY_ID, formResponse.sqliteId, formResponse.ffmId);
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
                        if (!onDistrictItemClick && et_cdistrict.isEnabled()) {
                            districtId = "0";
                            autoCompleteDistrictAdapter(distObjs);
                        } else onDistrictItemClick = false;
                    }
                });
            }

        }
    }
}
