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
import com.nsl.beejtantra.pojo.District;
import com.nsl.beejtantra.pojo.FormResponse;
import com.nsl.beejtantra.pojo.FormResponseResVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.nsl.beejtantra.DatabaseHandler.KEY_COMP_CHANNEL_FFM_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_COMP_CHANNEL_PRIMARY_ID;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_COMPETITOR_CHANNEL;

public class CompetitorChannelActivity extends AppCompatActivity implements RetrofitResponseListener {

    EditText et_cterritory, et_ctotal_no_of_distributors, et_ctotal_no_of_retailers, et_cno_of_nsl_villages, et_no_of_nsl_distributors,
            et_no_of_nsl_retailers, et_name_of_competitor_company1, et_no_of_distributors_company1, et_no_of_retailers_company1, et_name_competitor_company2, et_no_of_distributors_company2, et_no_of_retailers_company2,
            et_name_competitor_company3, tv_total_no_of_villages_territory, et_no_of_distributors_company3, et_no_of_retailers_company3, et_name_competitor_company4, et_no_of_distributors_company4, et_no_of_retailers_company4, et_name_competitor_company5, et_no_of_distributors_company5, et_no_of_retailers_company5;
    AutoCompleteTextView et_cdistrict;
    TextView tv_market_penetration_company1, tv_market_penetration_company2, tv_market_penetration_company3, tv_market_penetration_company4, tv_market_penetration_company5, save_competitor_channel;
    Spinner regionSpinner;
    private List<SelectedCities> distObjs = new ArrayList<>();
    private boolean onDistrictItemClick;
    private String districtId = "0", regionId = "0";
    private MyRunnable1 myRunnable1;
    DatabaseHandler db;
    private Handler handler;
    double totalNoOfDistributors = 0, totalNoOfRetailers = 0, noOfDistributors1 = 0, noOfRetailers1 = 0, noOfDistributors2 = 0, noOfRetailers2 = 0, noOfDistributors3 = 0, noOfRetailers3 = 0, noOfDistributors4 = 0, noOfRetailers4 = 0, noOfDistributors5 = 0, noOfRetailers5 = 0;
    private static final int ALL = 0;
    private static final int ONE = 1;
    private static final int TWO = 2;
    private static final int THREE = 3;
    private static final int FOUR = 4;
    private static final int FIVE = 5;


    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mi_activity_competitor_channel);
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
        tv_total_no_of_villages_territory = findViewById(R.id.tv_total_no_of_villages_territory);
        et_ctotal_no_of_distributors = findViewById(R.id.et_ctotal_no_of_distributors);
        et_ctotal_no_of_retailers = findViewById(R.id.et_ctotal_no_of_retailers);
        et_cno_of_nsl_villages = findViewById(R.id.et_cno_of_nsl_villages);
        et_no_of_nsl_distributors = findViewById(R.id.et_no_of_nsl_distributors);
        et_no_of_nsl_retailers = findViewById(R.id.et_no_of_nsl_retailers);

        et_name_of_competitor_company1 = findViewById(R.id.et_name_of_competitor_company1);
        et_no_of_distributors_company1 = findViewById(R.id.et_no_of_distributors_company1);
        et_no_of_retailers_company1 = findViewById(R.id.et_no_of_retailers_company1);
        tv_market_penetration_company1 = findViewById(R.id.tv_market_penetration_company1);

        et_name_competitor_company2 = findViewById(R.id.et_name_competitor_company2);
        et_no_of_distributors_company2 = findViewById(R.id.et_no_of_distributors_company2);
        et_no_of_retailers_company2 = findViewById(R.id.et_no_of_retailers_company2);
        tv_market_penetration_company2 = findViewById(R.id.tv_market_penetration_company2);

        et_name_competitor_company3 = findViewById(R.id.et_name_competitor_company3);
        et_no_of_distributors_company3 = findViewById(R.id.et_no_of_distributors_company3);
        et_no_of_retailers_company3 = findViewById(R.id.et_no_of_retailers_company3);
        tv_market_penetration_company3 = findViewById(R.id.tv_market_penetration_company3);

        et_name_competitor_company4 = findViewById(R.id.et_name_competitor_company4);
        et_no_of_distributors_company4 = findViewById(R.id.et_no_of_distributors_company4);
        et_no_of_retailers_company4 = findViewById(R.id.et_no_of_retailers_company4);
        tv_market_penetration_company4 = findViewById(R.id.tv_market_penetration_company4);

        et_name_competitor_company5 = findViewById(R.id.et_name_competitor_company5);
        et_no_of_distributors_company5 = findViewById(R.id.et_no_of_distributors_company5);
        et_no_of_retailers_company5 = findViewById(R.id.et_no_of_retailers_company5);
        tv_market_penetration_company5 = findViewById(R.id.tv_market_penetration_company5);

        save_competitor_channel = findViewById(R.id.save_competitor_channel);

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
        save_competitor_channel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isValidated())
                    return;

            }
        });

        et_ctotal_no_of_distributors.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().length() > 0) {
                    totalNoOfDistributors = Double.parseDouble(editable.toString().trim());
                    validateMarketPenetration(ALL);
                }
            }
        });

        et_ctotal_no_of_retailers.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable.toString().trim().length() > 0) {
                    totalNoOfRetailers = Double.parseDouble(editable.toString().trim());
                    validateMarketPenetration(ALL);
                }
            }
        });

        et_no_of_distributors_company1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (isFieldsFilled() && editable.toString().trim().length() > 0) {
                    noOfDistributors1 = Double.parseDouble(editable.toString().trim());
                    validateMarketPenetration(ONE);
                }

            }
        });
        et_no_of_retailers_company1.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (isFieldsFilled() && editable.toString().trim().length() > 0) {
                    noOfRetailers1 = Double.parseDouble(editable.toString().trim());
                    validateMarketPenetration(ONE);
                }
            }
        });
        et_no_of_distributors_company2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (isFieldsFilled() && editable.toString().trim().length() > 0) {
                    noOfDistributors2 = Double.parseDouble(editable.toString().trim());
                    validateMarketPenetration(TWO);
                }

            }
        });
        et_no_of_retailers_company2.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (isFieldsFilled() && editable.toString().trim().length() > 0) {
                    noOfRetailers2 = Double.parseDouble(editable.toString().trim());
                    validateMarketPenetration(TWO);
                }
            }
        });
        et_no_of_distributors_company3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (isFieldsFilled() && editable.toString().trim().length() > 0) {
                    noOfDistributors3 = Double.parseDouble(editable.toString().trim());
                    validateMarketPenetration(THREE);
                }

            }
        });
        et_no_of_retailers_company3.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (isFieldsFilled() && editable.toString().trim().length() > 0) {
                    noOfRetailers3 = Double.parseDouble(editable.toString().trim());
                    validateMarketPenetration(THREE);
                }
            }
        });

        et_no_of_distributors_company4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (isFieldsFilled() && editable.toString().trim().length() > 0) {
                    noOfDistributors4 = Double.parseDouble(editable.toString().trim());
                    validateMarketPenetration(FOUR);
                }

            }
        });
        et_no_of_retailers_company4.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (isFieldsFilled() && editable.toString().trim().length() > 0) {
                    noOfRetailers4 = Double.parseDouble(editable.toString().trim());
                    validateMarketPenetration(FOUR);
                }
            }
        });
        et_no_of_distributors_company5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (isFieldsFilled() && editable.toString().trim().length() > 0) {
                    noOfDistributors5 = Double.parseDouble(editable.toString().trim());
                    validateMarketPenetration(FIVE);
                }

            }
        });
        et_no_of_retailers_company5.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (isFieldsFilled() && editable.toString().trim().length() > 0) {
                    noOfRetailers5 = Double.parseDouble(editable.toString().trim());
                    validateMarketPenetration(FIVE);
                }
            }
        });
    }

    private void validateMarketPenetration(int type) {
        switch (type) {
            case ALL:
                if (noOfRetailers1 > 0 && noOfRetailers1 > 0)
                    calculateMarketPenetration(ONE, noOfDistributors1, noOfRetailers1);
                if (noOfRetailers2 > 0 && noOfRetailers2 > 0)
                    calculateMarketPenetration(TWO, noOfDistributors2, noOfRetailers2);
                if (noOfRetailers3 > 0 && noOfRetailers3 > 0)
                    calculateMarketPenetration(THREE, noOfDistributors3, noOfRetailers3);
                if (noOfRetailers4 > 0 && noOfRetailers4 > 0)
                    calculateMarketPenetration(FOUR, noOfDistributors4, noOfRetailers4);
                if (noOfRetailers5 > 0 && noOfRetailers5 > 0)
                    calculateMarketPenetration(FIVE, noOfDistributors5, noOfRetailers5);
                break;
            case ONE:
                if (noOfDistributors1 == 0) {
//                    et_no_of_distributors_company1.setError("please enter no of distributors1");
//                    et_no_of_distributors_company1.requestFocus();
                    return;
                }
                if (noOfRetailers1 == 0) {
//                    et_no_of_retailers_company1.setError("please enter no of retailers1");
//                    et_no_of_retailers_company1.requestFocus();
                    return;
                }
                calculateMarketPenetration(type, noOfDistributors1, noOfRetailers1);
                break;
            case TWO:
                if (noOfDistributors2 == 0) {
//                    et_no_of_distributors_company2.setError("please enter no of distributors1");
//                    et_no_of_distributors_company2.requestFocus();
                    return;
                }
                if (noOfRetailers2 == 0) {
//                    et_no_of_retailers_company2.setError("please enter no of retailers1");
//                    et_no_of_retailers_company2.requestFocus();
                    return;
                }
                calculateMarketPenetration(type, noOfDistributors2, noOfRetailers2);
                break;
            case THREE:
                if (noOfDistributors3 == 0) {
//                    et_no_of_distributors_company3.setError("please enter no of distributors1");
//                    et_no_of_distributors_company3.requestFocus();
                    return;
                }
                if (noOfRetailers3 == 0) {
//                    et_no_of_retailers_company3.setError("please enter no of retailers1");
//                    et_no_of_retailers_company3.requestFocus();
                    return;
                }
                calculateMarketPenetration(type, noOfDistributors3, noOfRetailers3);
                break;
            case FOUR:
                if (noOfDistributors4 == 0) {
//                    et_no_of_distributors_company4.setError("please enter no of distributors1");
//                    et_no_of_distributors_company4.requestFocus();
                    return;
                }
                if (noOfRetailers4 == 0) {
//                    et_no_of_retailers_company4.setError("please enter no of retailers1");
//                    et_no_of_retailers_company4.requestFocus();
                    return;
                }
                calculateMarketPenetration(type, noOfDistributors4, noOfRetailers4);
                break;
            case FIVE:
                if (noOfDistributors5 == 0) {
//                    et_no_of_distributors_company5.setError("please enter no of distributors1");
//                    et_no_of_distributors_company5.requestFocus();
                    return;
                }
                if (noOfRetailers5 == 0) {
//                    et_no_of_retailers_company5.setError("please enter no of retailers1");
//                    et_no_of_retailers_company5.requestFocus();
                    return;
                }
                calculateMarketPenetration(type, noOfDistributors5, noOfRetailers5);
                break;
        }
    }

    private void calculateMarketPenetration(int type, double distributors, double retailers) {
        double result = ((distributors + retailers) / (totalNoOfDistributors + totalNoOfRetailers) * 100);
        switch (type) {
            case ONE:
                tv_market_penetration_company1.setText(String.valueOf(Common.round(result,3)));
                break;
            case TWO:
                tv_market_penetration_company2.setText(String.valueOf(Common.round(result,3)));
                break;
            case THREE:
                tv_market_penetration_company3.setText(String.valueOf(Common.round(result,3)));
                break;
            case FOUR:
                tv_market_penetration_company4.setText(String.valueOf(Common.round(result,3)));
                break;
            case FIVE:
                tv_market_penetration_company5.setText(String.valueOf(Common.round(result,3)));
                break;

        }


    }

    private boolean isFieldsFilled() {
        if (totalNoOfDistributors == 0) {
            et_ctotal_no_of_retailers.setError("please enter total no of distributors");
            et_ctotal_no_of_retailers.requestFocus();
            return false;
        }
        if (totalNoOfRetailers == 0) {
            et_ctotal_no_of_retailers.setError("please enter total no of retailers");
            et_ctotal_no_of_retailers.requestFocus();
            return false;
        }
        return true;
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
                Common.hideSoftKeyboard(CompetitorChannelActivity.this);

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

        if (et_ctotal_no_of_distributors.getText().toString().isEmpty()) {
            et_ctotal_no_of_distributors.requestFocus();
            et_ctotal_no_of_distributors.setError("Please Enter Total no.of Distributors");
            return false;
        }

        if (et_ctotal_no_of_retailers.getText().toString().isEmpty()) {
            et_ctotal_no_of_retailers.requestFocus();
            et_ctotal_no_of_retailers.setError("Please Enter Total no.of Retailers ");
            return false;
        }

        if (et_no_of_nsl_distributors.getText().toString().isEmpty()) {
            et_no_of_nsl_distributors.requestFocus();
            et_no_of_nsl_distributors.setError("Please Enter No.of NSL Distributors");
            return false;
        }

        if (et_no_of_nsl_retailers.getText().toString().isEmpty()) {
            et_no_of_nsl_retailers.requestFocus();
            et_no_of_nsl_retailers.setError("Please Enter No.of NSL Retailers");
            return false;
        }

        if (et_name_of_competitor_company1.getText().toString().isEmpty()) {
            et_name_of_competitor_company1.requestFocus();
            et_name_of_competitor_company1.setError("Please Enter Name of Competitor Company 1");
            return false;
        }

        if (et_no_of_distributors_company1.getText().toString().isEmpty()) {
            et_no_of_distributors_company1.requestFocus();
            et_no_of_distributors_company1.setError("Please Enter No.of Distributors Company 1");
            return false;
        }

        if (et_no_of_retailers_company1.getText().toString().isEmpty()) {
            et_no_of_retailers_company1.requestFocus();
            et_no_of_retailers_company1.setError("Please Enter No.of Retailers Company 1");
            return false;
        }

        if (et_name_competitor_company2.getText().toString().isEmpty()) {
            et_name_competitor_company2.requestFocus();
            et_name_competitor_company2.setError("Please Enter Name of Competitor Company 2");
            return false;
        }

        if (et_no_of_distributors_company2.getText().toString().isEmpty()) {
            et_no_of_distributors_company2.requestFocus();
            et_no_of_distributors_company2.setError("Please Enter No.of Distributors Company 2");
            return false;
        }

        if (et_no_of_retailers_company2.getText().toString().isEmpty()) {
            et_no_of_retailers_company2.requestFocus();
            et_no_of_retailers_company2.setError("Please Enter No.of Retailers Company 2");
            return false;
        }

        if (et_name_competitor_company3.getText().toString().isEmpty()) {
            et_name_competitor_company3.requestFocus();
            et_name_competitor_company3.setError("Please Enter Name of Competitor Company 3");
            return false;
        }

        if (et_no_of_distributors_company3.getText().toString().isEmpty()) {
            et_no_of_distributors_company3.requestFocus();
            et_no_of_distributors_company3.setError("Please Enter No.of Distributors Company 3");
            return false;
        }

        if (et_no_of_retailers_company3.getText().toString().isEmpty()) {
            et_no_of_retailers_company3.requestFocus();
            et_no_of_retailers_company3.setError("Please Enter No.of Retailers Company 3");
            return false;
        }

        CompetitorChannel cc = new CompetitorChannel();
        cc.userId = Integer.parseInt(Common.getUserIdFromSP(this));
        cc.regionId = regionId;
        cc.district = districtId;
        cc.competitorCompanyName1 = et_name_of_competitor_company1.getText().toString().trim();
        cc.competitorCompanyName2 = et_name_competitor_company2.getText().toString().trim();
        cc.competitorCompanyName3 = et_name_competitor_company3.getText().toString().trim();
        cc.competitorCompanyName4 = et_name_competitor_company4.getText().toString().trim();
        cc.competitorCompanyName5 = et_name_competitor_company5.getText().toString().trim();
        cc.marketPenetration1 = tv_market_penetration_company1.getText().toString().trim();
        cc.marketPenetration2 = tv_market_penetration_company2.getText().toString().trim();
        cc.marketPenetration3 = tv_market_penetration_company3.getText().toString().trim();
        cc.marketPenetration4 = tv_market_penetration_company4.getText().toString().trim();
        cc.marketPenetration5 = tv_market_penetration_company5.getText().toString().trim();
        cc.noOfDistributors1 = et_no_of_distributors_company1.getText().toString().trim();
        cc.noOfDistributors2 = et_no_of_distributors_company2.getText().toString().trim();
        cc.noOfDistributors3 = et_no_of_distributors_company3.getText().toString().trim();
        cc.noOfDistributors4 = et_no_of_distributors_company4.getText().toString().trim();
        cc.noOfDistributors5 = et_no_of_distributors_company5.getText().toString().trim();
        cc.noOfNslDistributors = et_no_of_nsl_distributors.getText().toString().trim();
        cc.noOfNslRetailers = et_no_of_nsl_retailers.getText().toString().trim();
        cc.noOfNslVillages = et_cno_of_nsl_villages.getText().toString().trim();
        cc.noOfRetailers1 = et_no_of_retailers_company1.getText().toString().trim();
        cc.noOfRetailers2 = et_no_of_retailers_company2.getText().toString().trim();
        cc.noOfRetailers3 = et_no_of_retailers_company3.getText().toString().trim();
        cc.noOfRetailers4 = et_no_of_retailers_company4.getText().toString().trim();
        cc.noOfRetailers5 = et_no_of_retailers_company5.getText().toString().trim();
        cc.territory = et_cterritory.getText().toString().trim();
        cc.totalNoOfVillages = tv_total_no_of_villages_territory.getText().toString().trim();
        cc.totalNoOfRetailers = et_ctotal_no_of_retailers.getText().toString().trim();
        cc.totalNoOfDistributors = et_ctotal_no_of_distributors.getText().toString().trim();
        db.insertCompetitorChannel(cc);
        List<CompetitorChannel> competitorChannels = db.getOfflineCompetitorChannels(Common.getUserIdFromSP(this));
        CompetitorChannelReqVo competitorChannelReqVo = new CompetitorChannelReqVo();
        competitorChannelReqVo.competitorChannel = competitorChannels;
        if (Common.haveInternet(CompetitorChannelActivity.this))
            new RetrofitRequester(this).sendRequest(Constants.URL_INSERTING_COMPETITOR_CHANNEL, competitorChannelReqVo, true);
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
                    db.updateTable(KEY_COMP_CHANNEL_FFM_ID, TABLE_COMPETITOR_CHANNEL, KEY_COMP_CHANNEL_PRIMARY_ID, formResponse.sqliteId, formResponse.ffmId);
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
