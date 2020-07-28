package com.nsl.beejtantra.fieldestimation;

import android.app.ProgressDialog;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nsl.beejtantra.Constants;
import com.nsl.beejtantra.DatabaseHandler;
import com.nsl.beejtantra.Products_Pojo;
import com.nsl.beejtantra.R;
import com.nsl.beejtantra.SelectedCities;
import com.nsl.beejtantra.commonutils.Common;
import com.nsl.beejtantra.network.RetrofitRequester;
import com.nsl.beejtantra.network.RetrofitResponseListener;
import com.nsl.beejtantra.pojo.FormResponseResVo;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;


public class Cotton_new extends AppCompatActivity implements RetrofitResponseListener {

    EditText ed1, ed2, ed3, ed4;
    Button btn;
    TextView result;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.spinCompany)
    Spinner spinProducts;
    DatabaseHandler db;
    List<SelectedCities> adapterProducts = new ArrayList<>();
    List<String> adapterValues = new ArrayList<>();
    int calcValue = 43560;
    int lakh = 100000;
    private String productId = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.cotton);
        ButterKnife.bind(this);
        db = new DatabaseHandler(this);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        ed1 = (EditText) findViewById(R.id.ed1);
        ed2 = (EditText) findViewById(R.id.ed2);
        ed3 = (EditText) findViewById(R.id.ed3);
        ed4 = (EditText) findViewById(R.id.ed4);
        btn = (Button) findViewById(R.id.button_cotton);
        result = (TextView) findViewById(R.id.result);
        adapterValues.add("Select Product");
        SelectedCities selectedCities = new SelectedCities();
        selectedCities.setCityId("0");
        selectedCities.setCityName("Select Product");
        adapterProducts.add(selectedCities);
        final List<Products_Pojo> productsList = db.getProductsByDivisionId(1);
        for (int i = 0; i < productsList.size(); i++) {
            adapterValues.add(productsList.get(i).getProductName());
            SelectedCities selectedCities1 = new SelectedCities();
            selectedCities1.setCityId(productsList.get(i).getProductMasterId());
            selectedCities1.setCityName(productsList.get(i).getProductName());
            adapterProducts.add(selectedCities1);
        }
        onTextChanged(ed1);
        onTextChanged(ed2);
        onTextChanged(ed3);
        onTextChanged(ed4);
        spinProducts.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner_item, R.id.customSpinnerItemTextView, adapterValues));
        spinProducts.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                productId = adapterProducts.get(i).getCityId();
                btn.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        btn.setOnClickListener(new View.OnClickListener() {
            public double Yield_per_hector_in_grams;

            @Override
            public void onClick(View v) {
                btn.setFocusable(true);
                Common.hideKeyboard(Cotton_new.this);
                String bolls_plant = ed1.getText().toString();
                String weight_plant = ed2.getText().toString();
                String row_distnce = ed3.getText().toString();
                String plant_distance = ed4.getText().toString();

                if (productId.equalsIgnoreCase("0")) {
                    Toast.makeText(Cotton_new.this, "Please Select Product", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (ed1.getText().toString().length() > 0 && ed2.getText().toString().length() > 0 && ed3.getText().toString().length() > 0 && ed4.getText().toString().length() > 0) {
//                    Yield_per_hector_in_grams = (Double.parseDouble(bolls_plant) * Double.parseDouble(weight_plant)) * (10000 / (Double.parseDouble(row_distnce) * Double.parseDouble(plant_distance)));
                    Yield_per_hector_in_grams = (calcValue / Double.parseDouble(row_distnce) * Double.parseDouble(plant_distance)) * Integer.parseInt(bolls_plant) * Double.parseDouble(weight_plant) / lakh;
                    result.setText("" + String.valueOf(Yield_per_hector_in_grams));
                    YieldReqVo yieldReqVo = new YieldReqVo();
                    yieldReqVo.productId = productId;
                    yieldReqVo.divisionId = "Cotton";
                    yieldReqVo.result = String.valueOf(Yield_per_hector_in_grams);
                    yieldReqVo.avgNoOfBallsPlant = ed1.getText().toString().trim();
                    yieldReqVo.avgBallWeight = ed2.getText().toString().trim();
                    yieldReqVo.rowToRowDistance = ed3.getText().toString().trim();
                    yieldReqVo.plantToPlantDistance = ed4.getText().toString().trim();
                    yieldReqVo.createdBy = Common.getUserIdFromSP(Cotton_new.this);
                    btn.setVisibility(View.GONE);
                    if (Common.haveInternet(Cotton_new.this))
                        new RetrofitRequester(Cotton_new.this).sendRequest(Constants.URL_INSERTING_YIELD_ESTIMATION, yieldReqVo, true);
                    else
                        db.insertYE(yieldReqVo);

                } else {
                    Toast.makeText(getApplicationContext(), "Please enter field", Toast.LENGTH_SHORT).show();
                }
            }
        });


    }

    private void onTextChanged(EditText et) {
        et.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                btn.setVisibility(View.VISIBLE);
            }
        });
    }

    @Override
    public void onResponseSuccess(ArrayList<Object> object, Map<String, String> requestParams, int requestId) {

    }

    @Override
    public void onFormResponseSuccess(String url, FormResponseResVo formResponseResVo, ProgressDialog dialog) {
        try {
            if (formResponseResVo != null) {
                Toast.makeText(this, formResponseResVo.msg, Toast.LENGTH_SHORT).show();
            }
        } catch (Exception e) {

        }
        Common.dismissProgressDialog(dialog);
    }
}
