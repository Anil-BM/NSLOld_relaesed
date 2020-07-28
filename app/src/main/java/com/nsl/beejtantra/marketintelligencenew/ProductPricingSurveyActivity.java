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
import android.widget.LinearLayout;
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
import com.nsl.beejtantra.pojo.ProductPricingReqVo;
import com.nsl.beejtantra.pojo.ProductPricingSurvey;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import static com.nsl.beejtantra.DatabaseHandler.KEY_SURVEY_FFM_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_SURVEY_PRIMARY_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CROPS_CROP_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CROPS_CROP_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CROPS_DIVISION_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_DIVISION_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_DIVISION_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USER_COMPANY_DIVISION_DIVISION_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USER_COMPANY_DIVISION_USER_ID;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_CROPS;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_DIVISION;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_PRODUCT_PRICING_SURVEY;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_USER_COMPANY_DIVISION;

public class ProductPricingSurveyActivity extends AppCompatActivity implements RetrofitResponseListener {

    EditText et_ptaluka, et_pvillage, et_pcompetitor_company_name, et_pcompetitor_product_name, et_psegment, et_psale_quantity, et_p_pack_size, et_pdistributor_net_landing_price,
            et_pcompany_billing_price, et_pdistributor_billing_price_to_retailer, et_pfarmer_price, et_pmrp, et_plast_year_sale, et_pcurrent_year_sale, et_pnext_year_estimated_sale, et_duration, et_grainType;
    Spinner regionSpinner, spin_pdivision, spin_pcrop;
    AutoCompleteTextView et_pdistrict;
    LinearLayout paddyFieldsLayout, segmentLayout;
    TextView save_product_pricing, tv_next_year_estimate, tv_current_year_sale, tv_last_year_sale, tv_mrp, tv_farmar_price, tv_distributor_billing_price, tv_company_billing_price, tv_distribution_net_landing, tv_sale_quantity;
    ArrayList<String> divisionProductAdapter;
    ArrayList<String> cropProductadapter;
    List<SelectedCities> divisions = new ArrayList<>();
    List<SelectedCities> crops = new ArrayList<>();
    DatabaseHandler db;
    private List<SelectedCities> distObjs = new ArrayList<>();
    private String regionId = "0", districtId = "0";
    private boolean onDistrictItemClick;
    private MyRunnable1 myRunnable1;
    private Handler handler;
    private String divisionId, divisionName;
    private String cropId, cropName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.mi_activity_product_pricing_survey);
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

        et_pdistrict = findViewById(R.id.et_pdistrict);
        et_ptaluka = findViewById(R.id.et_ptaluka);
        et_pvillage = findViewById(R.id.et_pvillage);
        spin_pdivision = findViewById(R.id.spin_pdivision);
        spin_pcrop = findViewById(R.id.spin_pcrop);
        regionSpinner = findViewById(R.id.spin_region);
        et_pcompetitor_company_name = findViewById(R.id.et_pcompetitor_company_name);
        et_pcompetitor_product_name = findViewById(R.id.et_pcompetitor_product_name);
        et_psegment = findViewById(R.id.et_psegment);
        et_psale_quantity = findViewById(R.id.et_psale_quantity);
        et_p_pack_size = findViewById(R.id.et_p_pack_size);
        et_pdistributor_net_landing_price = findViewById(R.id.et_pdistributor_net_landing_price);
        et_pcompany_billing_price = findViewById(R.id.et_pcompany_billing_price);
        et_pdistributor_billing_price_to_retailer = findViewById(R.id.et_pdistributor_billing_price_to_retailer);
        et_pfarmer_price = findViewById(R.id.et_pfarmer_price);
        et_pmrp = findViewById(R.id.et_pmrp);
        et_plast_year_sale = findViewById(R.id.et_plast_year_sale);
        et_pcurrent_year_sale = findViewById(R.id.et_pcurrent_year_sale);
        et_pnext_year_estimated_sale = findViewById(R.id.et_pnext_year_estimated_sale);
        paddyFieldsLayout = findViewById(R.id.paddy_fields_layout);
        segmentLayout = findViewById(R.id.segment_layout);
        et_duration = findViewById(R.id.et_duration);
        et_grainType = findViewById(R.id.et_grain_type);
        save_product_pricing = findViewById(R.id.save_product_pricing);
        tv_next_year_estimate = findViewById(R.id.tv_next_year_estimate);
        tv_current_year_sale = findViewById(R.id.tv_current_year_sale);
        tv_last_year_sale = findViewById(R.id.tv_last_year_sale);
        tv_mrp = findViewById(R.id.tv_mrp);
        tv_farmar_price = findViewById(R.id.tv_farmar_price);
        tv_distributor_billing_price = findViewById(R.id.tv_distributor_billing_price);
        tv_company_billing_price = findViewById(R.id.tv_company_billing_price);
        tv_distribution_net_landing = findViewById(R.id.tv_distribution_net_landing);
        tv_sale_quantity = findViewById(R.id.tv_sale_quantity);
        setRegionsData();
        et_pdistrict.requestFocus();
        autoCompleteDistrictAdapter(new ArrayList<SelectedCities>());
        myRunnable1 = new MyRunnable1();
        divisionProductAdapter = new ArrayList<String>();
        divisionProductAdapter.add("Select Division");

        cropProductadapter = new ArrayList<>();
        cropProductadapter.add("Select Crop");

        String query = "SELECT DISTINCT ud." + KEY_TABLE_USER_COMPANY_DIVISION_DIVISION_ID + ",d." + KEY_TABLE_DIVISION_NAME + " FROM " + TABLE_USER_COMPANY_DIVISION + " ud INNER JOIN " + TABLE_DIVISION + " d ON ud." + KEY_TABLE_USER_COMPANY_DIVISION_DIVISION_ID + " = d." + KEY_TABLE_DIVISION_MASTER_ID + " WHERE ud." + KEY_TABLE_USER_COMPANY_DIVISION_USER_ID + " in (" + Common.getTeamFromSP(this) + ")";
        Common.Log.i("Query " + query);
        Cursor cursor = db.getWritableDatabase().rawQuery(query, null);


        if (cursor.moveToFirst()) {
            do {
                SelectedCities selectedCities = new SelectedCities();
                selectedCities.setCityId(cursor.getString(cursor.getColumnIndex(KEY_TABLE_USER_COMPANY_DIVISION_DIVISION_ID)));
                selectedCities.setCityName(cursor.getString(cursor.getColumnIndex(KEY_TABLE_DIVISION_NAME)));
                divisionProductAdapter.add(cursor.getString(cursor.getColumnIndex(KEY_TABLE_DIVISION_NAME)));
                divisions.add(selectedCities);
            } while (cursor.moveToNext());
        }


        spin_pdivision.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, R.id.customSpinnerItemTextView, divisionProductAdapter));

        spin_pdivision.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                spin_pcrop.setSelection(0);
                if (position > 0) {
                    divisionId = divisions.get(position - 1).getCityId();
                    if (divisionProductAdapter.get(position).equalsIgnoreCase("COTTON")) {
                        tv_sale_quantity.setText("Sale Quantity" + " " + "(Pkts)");
                        tv_distribution_net_landing.setText("Distributor Net Landing Price" + " " + "(Pkts)");
                        tv_company_billing_price.setText("Company Billing Price" + " " + "(Pkts)");
                        tv_distributor_billing_price.setText("Distributor Billing Price to Retailer" + " " + "(Pkts)");
                        tv_farmar_price.setText("Farmer Price" + " " + "(Pkts)");
                        tv_mrp.setText("MRP" + " " + "(Pkts)");
                        tv_last_year_sale.setText("Last Year Sale in Village" + " " + "(Pkts)");
                        tv_current_year_sale.setText("Current Year Sale in Village" + " " + "(Pkts)");
                        tv_next_year_estimate.setText("Next Year Estimated Sale in Village" + " " + "(Pkts)");

                    } else if (divisionProductAdapter.get(position).equalsIgnoreCase("FIELD CROPS")) {
                        tv_sale_quantity.setText("Sale Quantity" + " " + "(Quintals)");
                        tv_distribution_net_landing.setText("Distributor Net Landing Price" + " " + "(Kg)");
                        tv_company_billing_price.setText("Company Billing Price" + " " + "(Kg)");
                        tv_distributor_billing_price.setText("Distributor Billing Price to Retailer" + " " + "(Kg)");
                        tv_farmar_price.setText("Farmer Price" + " " + "(Kg)");
                        tv_mrp.setText("MRP" + " " + "(Kg)");
                        tv_last_year_sale.setText("Last Year Sale in Village" + " " + "(Quintals)");
                        tv_current_year_sale.setText("Current Year Sale in Village" + " " + "(Quintals)");
                        tv_next_year_estimate.setText("Next Year Estimated Sale in Village" + " " + "(Quintals)");

                    } else if (divisionProductAdapter.get(position).equalsIgnoreCase("VEGETABLES")) {
                        tv_sale_quantity.setText("Sale Quantity" + " " + "(Kgs)");
                        tv_distribution_net_landing.setText("Distributor Net Landing Price" + " " + "(Kg)");
                        tv_company_billing_price.setText("Company Billing Price" + " " + "(Kg)");
                        tv_distributor_billing_price.setText("Distributor Billing Price to Retailer" + " " + "(Kg)");
                        tv_farmar_price.setText("Farmer Price" + " " + "(Kg)");
                        tv_mrp.setText("MRP" + " " + "(Kg)");
                        tv_last_year_sale.setText("Last Year Sale in Village" + " " + "(Kgs)");
                        tv_current_year_sale.setText("Current Year Sale in Village" + " " + "(Kgs)");
                        tv_next_year_estimate.setText("Next Year Estimated Sale in Village" + " " + "(Kgs)");

                    }
                    divisionName = divisions.get(position - 1).getCityName();
                    String cropQuery = "SELECT DISTINCT cr." + KEY_TABLE_CROPS_CROP_ID + ",cr." + KEY_TABLE_CROPS_CROP_NAME + " FROM " + TABLE_CROPS + " cr WHERE cr." + KEY_TABLE_CROPS_DIVISION_ID + " = " + divisionId;
                    Common.Log.i("CROP QUERY " + cropQuery);
                    Cursor cropCursor = db.getWritableDatabase().rawQuery(cropQuery, null);
                    cropProductadapter.clear();
                    crops.clear();
                    cropProductadapter.add("Select Crop");
                    if (cropCursor.moveToFirst()) {
                        do {
                            SelectedCities crop = new SelectedCities();
                            crop.setCityId(cropCursor.getString(cropCursor.getColumnIndex(KEY_TABLE_CROPS_CROP_ID)));
                            crop.setCityName(cropCursor.getString(cropCursor.getColumnIndex(KEY_TABLE_CROPS_CROP_NAME)));
                            cropProductadapter.add(cropCursor.getString(cropCursor.getColumnIndex(KEY_TABLE_CROPS_CROP_NAME)));
                            crops.add(crop);
                        } while (cropCursor.moveToNext());
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spin_pcrop.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, R.id.customSpinnerItemTextView, cropProductadapter));

        spin_pcrop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    cropId = crops.get(position - 1).getCityId();
                    cropName = crops.get(position - 1).getCityName();
                    if (cropName.equalsIgnoreCase("paddy")) {
                        paddyFieldsLayout.setVisibility(View.VISIBLE);
                        segmentLayout.setVisibility(View.GONE);
                    } else {
                        paddyFieldsLayout.setVisibility(View.GONE);
                        segmentLayout.setVisibility(View.VISIBLE);
                    }
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        save_product_pricing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!Validated())
                    return;
                else {
                    ProductPricingSurvey productPricingSurvey = new ProductPricingSurvey();
                    productPricingSurvey.regionId = regionId;
                    productPricingSurvey.district = districtId;
                    productPricingSurvey.taluka = et_ptaluka.getText().toString().trim();
                    productPricingSurvey.village = et_pvillage.getText().toString().trim();
                    productPricingSurvey.divisionId = Integer.parseInt(divisionId);
                    productPricingSurvey.divisionName = divisionName;
                    productPricingSurvey.cropId = Integer.parseInt(cropId);
                    productPricingSurvey.cropName = cropName;
                    productPricingSurvey.competitorCompanyName = et_pcompetitor_company_name.getText().toString().trim();
                    productPricingSurvey.competitorProductName = et_pcompetitor_product_name.getText().toString().trim();
                    productPricingSurvey.segment = et_psegment.getText().toString().trim();
                    productPricingSurvey.saleQuantity = et_psale_quantity.getText().toString().trim();
                    productPricingSurvey.packSize = et_p_pack_size.getText().toString().trim();
                    productPricingSurvey.distributorNetLandingPrice = et_pdistributor_net_landing_price.getText().toString().trim();
                    productPricingSurvey.companyBillingPrice = et_pcompany_billing_price.getText().toString().trim();
                    productPricingSurvey.distributorBillingPriceToRetailer = et_pdistributor_billing_price_to_retailer.getText().toString().trim();
                    productPricingSurvey.farmerPrice = et_pfarmer_price.getText().toString().trim();
                    productPricingSurvey.mrp = et_pmrp.getText().toString().trim();
                    productPricingSurvey.lastYearSaleInVillage = et_plast_year_sale.getText().toString().trim();
                    productPricingSurvey.currentYearSaleInVillage = et_pcurrent_year_sale.getText().toString().trim();
                    productPricingSurvey.nextYearEstimatedSaleInVillage = et_pnext_year_estimated_sale.getText().toString().trim();
                    productPricingSurvey.userId = Integer.parseInt(Common.getUserIdFromSP(ProductPricingSurveyActivity.this));
                    db.insertProductPricingSurvey(productPricingSurvey);
                    List<ProductPricingSurvey> productPricingSurveys=db.getofflineProductPricingSurvey(Common.getUserIdFromSP(ProductPricingSurveyActivity.this));
                    ProductPricingReqVo productPricingReqVo =new ProductPricingReqVo();
                    productPricingReqVo.productPriceSurvey=productPricingSurveys;
                    if(Common.haveInternet(ProductPricingSurveyActivity.this))
                            new RetrofitRequester(ProductPricingSurveyActivity.this).sendRequest(Constants.URL_INSERTING_PRODUCT_SURVEY, productPricingReqVo, true);
                        else
                            finish();
                }

            }
        });

        et_pdistrict.addTextChangedListener(new TextWatcher() {
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
                    et_pdistrict.showDropDown();
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

    }

    public void autoCompleteDistrictAdapter(List<SelectedCities> list) {
        final CustomListAdapter adapter = new CustomListAdapter(this,
                R.layout.autocomplete_custom_layout, list);

        et_pdistrict.setAdapter(adapter);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                et_pdistrict.showDropDown();
            }
        }, 100);

        adapter.setOnItemClickListener(new CustomListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, SelectedCities item) {
                onDistrictItemClick = true;
                et_pdistrict.setText(item.getCityName().toUpperCase());
                et_pdistrict.setSelection(item.getCityName().length());
                districtId = item.getCityId();
                et_pdistrict.dismissDropDown();
                Common.hideSoftKeyboard(ProductPricingSurveyActivity.this);

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
                    et_pdistrict.setText("");
                    et_ptaluka.setText("");
                    et_pvillage.setText("");
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
            et_pdistrict.requestFocus();
            et_pdistrict.setError("Please Choose District");
            return false;
        }
        if (et_ptaluka.getText().toString().isEmpty()) {
            et_ptaluka.requestFocus();
            et_ptaluka.setError("Please Enter Taluka");
            return false;
        }
        if (et_pvillage.getText().toString().isEmpty()) {
            et_pvillage.requestFocus();
            et_pvillage.setError("Please Enter Village");
            return false;
        }
        if (spin_pdivision.getSelectedItemPosition() == 0) {
            Toast.makeText(getApplicationContext(), "Please Select Division", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (spin_pcrop.getSelectedItemPosition() == 0) {
            Toast.makeText(getApplicationContext(), "Please Select Crop", Toast.LENGTH_SHORT).show();
            return false;
        }

        if (et_pcompetitor_company_name.getText().toString().isEmpty()) {
            et_pcompetitor_company_name.requestFocus();
            et_pcompetitor_company_name.setError("Please Enter Competitor Name");
            return false;
        }

        if (et_pcompetitor_product_name.getText().toString().isEmpty()) {
            et_pcompetitor_product_name.requestFocus();
            et_pcompetitor_product_name.setError("Please Enter Competitor Product Name");
            return false;
        }

        if (et_psegment.getText().toString().isEmpty()) {
            et_psegment.requestFocus();
            et_psegment.setError("Please Enter Segment");
            return false;
        }

        if (et_psale_quantity.getText().toString().isEmpty()) {
            et_psale_quantity.requestFocus();
            et_psale_quantity.setError("Please Enter Sale Quantity");
            return false;
        }

        if (et_p_pack_size.getText().toString().isEmpty()) {
            et_p_pack_size.requestFocus();
            et_p_pack_size.setError("Please Enter Pack Size");
            return false;
        }

        if (et_pdistributor_net_landing_price.getText().toString().isEmpty()) {
            et_pdistributor_net_landing_price.requestFocus();
            et_pdistributor_net_landing_price.setError("Please Enter Distributor Net Landing Price");
            return false;
        }

        if (et_pcompany_billing_price.getText().toString().isEmpty()) {
            et_pcompany_billing_price.requestFocus();
            et_pcompany_billing_price.setError("Please Enter Company Billing");
            return false;
        }

        if (et_pdistributor_billing_price_to_retailer.getText().toString().isEmpty()) {
            et_pdistributor_billing_price_to_retailer.requestFocus();
            et_pdistributor_billing_price_to_retailer.setError("Please Enter Distributor Billing Price to Retailer");
            return false;
        }

        if (et_pfarmer_price.getText().toString().isEmpty()) {
            et_pfarmer_price.requestFocus();
            et_pfarmer_price.setError("Please Enter Farmer Price");
            return false;
        }

        if (et_pmrp.getText().toString().isEmpty()) {
            et_pmrp.requestFocus();
            et_pmrp.setError("Please Enter MRP");
            return false;
        }


        if (et_plast_year_sale.getText().toString().isEmpty()) {
            et_plast_year_sale.requestFocus();
            et_plast_year_sale.setError("Please Enter Last Year Sale in Village");
            return false;
        }

        if (et_pcurrent_year_sale.getText().toString().isEmpty()) {
            et_pcurrent_year_sale.requestFocus();
            et_pcurrent_year_sale.setError("Please Enter Current Year Sale in Village");
            return false;
        }

        if (et_pnext_year_estimated_sale.getText().toString().isEmpty()) {
            et_pnext_year_estimated_sale.requestFocus();
            et_pnext_year_estimated_sale.setError("Please Enter Next Year Estimated Sale in Village");
            return false;
        }

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
                    db.updateTable(KEY_SURVEY_FFM_ID,TABLE_PRODUCT_PRICING_SURVEY,KEY_SURVEY_PRIMARY_ID,formResponse.sqliteId, formResponse.ffmId);
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
                /*regionId="1";
                key="'%Ana%'";*/
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
                        if (!onDistrictItemClick && et_pdistrict.isEnabled()) {
                            districtId = "0";
                            autoCompleteDistrictAdapter(distObjs);
                        } else onDistrictItemClick = false;
                    }
                });
            }

        }
    }
}
