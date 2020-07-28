package com.nsl.beejtantra.fieldestimation;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nsl.beejtantra.Constants;
import com.nsl.beejtantra.Crops;
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
import butterknife.OnClick;


public class MaizeActivity extends AppCompatActivity implements RetrofitResponseListener {

    //    @BindView(R.id.ed1)
//    EditText ed1;
//    @BindView(R.id.ed2)
//    EditText ed2;
//    @BindView(R.id.ed3)
//    EditText ed3;
    @BindView(R.id.spinCrop)
    Spinner spinCrop;
    @BindView(R.id.spinProduct)
    Spinner spinProduct;
    @BindView(R.id.button_cotton)
    Button btn;
    @BindView(R.id.result)
    TextView result;
    @BindView(R.id.et_area_in_meters)
    EditText etAreaInMeters;
    @BindView(R.id.et_yield_in_meters)
    EditText etYieldInMeters;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView title;
    List<SelectedCities> adapterProducts = new ArrayList<>();
    List<SelectedCities> adapterCrops = new ArrayList<>();
    List<String> productValues = new ArrayList<>();
    List<String> cropValues = new ArrayList<>();
    private DatabaseHandler db;
    double toAcre = 0.00024711;
    double calcValue = 4046.86;
    int type = 0;
    private double resultValue;
    private String productId = "0", cropId = "0";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.maize);
        ButterKnife.bind(this);
        type = getIntent().getExtras().getInt("type", 0);
        setSupportActionBar(toolbar);
        db = new DatabaseHandler(this);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        if (type == 0) {
            title.setText("Field Crops");
            btn.setText("Estimated Yield in Quintals");
        } else {
            title.setText("Vegetables");
            btn.setText("Estimated Yield in Kgs");
        }
        productValues.add("Select Product");
        cropValues.add("Select Crop");
        SelectedCities selectedCities = new SelectedCities();
        selectedCities.setCityId("0");
        selectedCities.setCityName("Select Product");
        adapterProducts.add(selectedCities);
        SelectedCities crop = new SelectedCities();
        crop.setCityId("0");
        crop.setCityName("Select Crop");
        adapterCrops.add(crop);
        List<Products_Pojo> productsList = db.getProductsByDivisionId(2);
        List<Crops> cropsList = db.getCropsByDivisionId(2);
        for (int i = 0; i < productsList.size(); i++) {
            productValues.add(productsList.get(i).getProductName());
            SelectedCities selectedCities1 = new SelectedCities();
            selectedCities1.setCityId(productsList.get(i).getProductMasterId());
            selectedCities1.setCityName(productsList.get(i).getProductName());
            adapterProducts.add(selectedCities1);
        }
        spinProduct.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner_item, R.id.customSpinnerItemTextView, productValues));
        for (int i = 0; i < cropsList.size(); i++) {
            cropValues.add(cropsList.get(i).getCropName());
            SelectedCities cropItem = new SelectedCities();
            cropItem.setCityId(cropsList.get(i).getCropMasterId());
            cropItem.setCityName(cropsList.get(i).getCropName());
            adapterCrops.add(cropItem);
        }

        spinCrop.setAdapter(new ArrayAdapter<String>(this, R.layout.spinner_item, R.id.customSpinnerItemTextView, cropValues));
        onTextChanged(etAreaInMeters);
        onTextChanged(etYieldInMeters);
        spinProduct.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                productId = adapterProducts.get(i).getCityId();
                btn.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        spinCrop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                cropId = adapterCrops.get(i).getCityId();
                btn.setVisibility(View.VISIBLE);
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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

    @OnClick(R.id.button_cotton)
    void get() {
//        String plot_cob_weight = ed1.getText().toString();
//        String cob_grains_weight = ed2.getText().toString();
//        String total_weight = ed3.getText().toString();
//         double Yield_in_quintals;
//         String plot_area=etAreaInMeters.getText().toString();;
//        if (ed1.getText().toString().length()>0 && ed2.getText().toString().length()>0 && ed3.getText().toString().length()>0 && plot_area.length()>0) {
//
//            Yield_in_quintals = (Double.parseDouble(plot_cob_weight) * (( Double.parseDouble(cob_grains_weight) / Double.parseDouble(total_weight)) * 100)*10000 / Double.parseDouble(plot_area))/100;
//            result.setText("" + String.valueOf(Yield_in_quintals));
//        }else {
//            Toast.makeText(getApplicationContext(),"Please enter field",Toast.LENGTH_SHORT).show();
//        }

        Common.hideKeyboard(this);
        String areaAcre = etAreaInMeters.getText().toString().trim();
        String yieldMetre = etYieldInMeters.getText().toString().trim();
        if (cropId.equalsIgnoreCase("0")) {
            Toast.makeText(this, "Please Select Crop", Toast.LENGTH_SHORT).show();
            return;
        }
        if (productId.equalsIgnoreCase("0")) {
            Toast.makeText(this, "Please Select Product", Toast.LENGTH_SHORT).show();
            return;
        }

        if (areaAcre.length() > 0 && yieldMetre.length() > 0) {
            YieldReqVo yieldReqVo = new YieldReqVo();
            if (type == 0) {
                yieldReqVo.divisionId = "Field Crops";
                resultValue = Double.parseDouble(yieldMetre) * calcValue * Double.parseDouble(areaAcre) / 100;
            } else {
                yieldReqVo.divisionId = "Vegetables";
                resultValue = Double.parseDouble(yieldMetre) * calcValue * Double.parseDouble(areaAcre);
            }
            result.setText("" + resultValue);
            yieldReqVo.productId = productId;
            yieldReqVo.cropId = cropId;
            yieldReqVo.area = areaAcre;
            yieldReqVo.yield = yieldMetre;
            yieldReqVo.result = String.valueOf(resultValue);
            yieldReqVo.createdBy = Common.getUserIdFromSP(this);
            btn.setVisibility(View.GONE);
            if (Common.haveInternet(this))
                new RetrofitRequester(this).sendRequest(Constants.URL_INSERTING_YIELD_ESTIMATION, yieldReqVo, true);
            else
                db.insertYE(yieldReqVo);

        } else {
            Toast.makeText(getApplicationContext(), "Please enter field", Toast.LENGTH_SHORT).show();
        }

    }

    @OnClick(R.id.tv_go_to_map)
    void tvGoToMap() {
        Intent intent = new Intent(this, FieldEstimationActivity.class);
        intent.putExtra("yield", true);
        startActivityForResult(intent, 10);
    }


    @Override
    public void onActivityResult(final int requestCode, int resultCode, Intent data) {
        // final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        Log.d("onActivityResult", "OnresultAxtivity1");
        switch (requestCode) {
            case 10:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        double area = data.getDoubleExtra("area", 0);
                        area = area * toAcre;
                        etAreaInMeters.setText("" + area);
                        Log.d("onActivityResult", "OnresultAxtivity4");
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        // finish();

                        break;

                    default:
                        break;
                }
                break;


        }
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
