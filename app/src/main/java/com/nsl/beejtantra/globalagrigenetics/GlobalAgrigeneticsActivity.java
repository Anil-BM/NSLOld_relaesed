package com.nsl.beejtantra.globalagrigenetics;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.GridView;
import android.widget.SimpleAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.gson.Gson;
import com.google.maps.android.SphericalUtil;
import com.nsl.beejtantra.Constants;
import com.nsl.beejtantra.DatabaseHandler;
import com.nsl.beejtantra.DistRetailerActivity;
import com.nsl.beejtantra.GodownActivity;
import com.nsl.beejtantra.MainDailyDiaryActivity;
import com.nsl.beejtantra.MyApplication;
import com.nsl.beejtantra.PaymentActivity;
import com.nsl.beejtantra.PlanerMainActivity;
import com.nsl.beejtantra.PostRouteMapActivityCopy;
import com.nsl.beejtantra.R;
import com.nsl.beejtantra.SchemesActivity;
import com.nsl.beejtantra.advancebooking.AdvanceBookingMainActivity;
import com.nsl.beejtantra.commonutils.Common;
import com.nsl.beejtantra.complaints.Complaintsselectactivity;
import com.nsl.beejtantra.distributors.DIstributorsListActivity;
import com.nsl.beejtantra.feedback.FeedbackallActivity;
import com.nsl.beejtantra.fieldestimation.FieldEstimationMainAcivity;
import com.nsl.beejtantra.marketintelligencenew.MarketIntelligenceAcivityCopy;
import com.nsl.beejtantra.orderindent.OrderIndentMainActivity;
import com.nsl.beejtantra.pojo.Menu;
import com.nsl.beejtantra.product_catalogue.ProductActivity1;
import com.nsl.beejtantra.product_catalogue.Product_Catlogue;
import com.nsl.beejtantra.stockmovement.NewStockMovementChooseActivity;
import com.nsl.beejtantra.stockreturns.NewStockReturnsChooseActivity;
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

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.nsl.beejtantra.FragmentCategories.getFlag;


/**
 * Created by sys on 6/8/2017.
 */

public class GlobalAgrigeneticsActivity extends AppCompatActivity {

    @BindView(R.id.gridView)
    GridView gridView;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_global_agrigenetics);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        toolbarTitle.setText("GLOBAL AGRIGENETICS");
        toolbarTitle.setAllCaps(true);

        // Each row in the list stores country name, currency and flag
        final List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();
        HashMap<String, String> hm = new HashMap<String, String>();
        hm.put("txt", Constants.Names.ORDER_INDENT);
        hm.put("flag", String.valueOf(R.drawable.g_orderindent));
        aList.add(hm);

        HashMap<String, String> hm1 = new HashMap<String, String>();
        hm1.put("txt", Constants.Names.PAYMENT_COLLECTIONS);
        hm1.put("flag", String.valueOf(R.drawable.g_paymentcollection));
        aList.add(hm1);

        HashMap<String, String> hm2 = new HashMap<String, String>();
        hm2.put("txt", Constants.Names.PRODUCT_CATALOGUE);
        hm2.put("flag", String.valueOf(R.drawable.g_productcataloge));
        aList.add(hm2);

        HashMap<String, String> hm3 = new HashMap<String, String>();
        hm3.put("txt", Constants.Names.GLOBAL_REWARDS);
        hm3.put("flag", String.valueOf(R.drawable.global_rewards));
        aList.add(hm3);


        // Keys used in Hashmap
        String[] from = {"txt", "flag"};

        // Ids of views in listview_layout
        int[] to = {R.id.tv_gridview, R.id.iv_gridView};

        // Instantiating an adapter to store each items
        // R.layout.listview_layout defines the layout of each item
        SimpleAdapter adapter = new SimpleAdapter(GlobalAgrigeneticsActivity.this, aList, R.layout.gridview_row, from, to);

        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                view.setSelected(true);
                String name = aList.get(i).get("txt");
                if (name.equalsIgnoreCase(Constants.Names.ORDER_INDENT)) {

                    Intent orderindent = new Intent(GlobalAgrigeneticsActivity.this, OrderIndentMainActivity.class);
                    //orderindent.putExtra(Constants.Names.GLOBAL_AGRIGENETICS,Constants.Names.GLOBAL_AGRIGENETICS);
                    Common.saveDataInSP(GlobalAgrigeneticsActivity.this, Constants.Names.GLOBAL_AGRIGENETICS, true);
                    startActivity(orderindent);
                } else if (name.equalsIgnoreCase(Constants.Names.PAYMENT_COLLECTIONS)) {

                    Intent payment = new Intent(GlobalAgrigeneticsActivity.this, PaymentActivity.class);
                    Common.saveDataInSP(GlobalAgrigeneticsActivity.this, Constants.Names.GLOBAL_AGRIGENETICS, true);
                    startActivity(payment);
                    // MainActivity.showExceedingAlert(getContext(),aList.get(i).get("txt")+"\n"+ Common.getStringResourceText(R.string.will_be_live_soon));

                } else if (name.equalsIgnoreCase(Constants.Names.PRODUCT_CATALOGUE)) {

                    Intent payment = new Intent(GlobalAgrigeneticsActivity.this, Product_Catlogue.class);
                    startActivity(payment);
                    // MainActivity.showExceedingAlert(getContext(),aList.get(i).get("txt")+"\n"+ Common.getStringResourceText(R.string.will_be_live_soon));

                } else if (name.equalsIgnoreCase(Constants.Names.GLOBAL_REWARDS)) {
                    Toast.makeText(GlobalAgrigeneticsActivity.this, "This section is under development", Toast.LENGTH_SHORT).show();
                    Intent payment = new Intent(GlobalAgrigeneticsActivity.this, PaymentActivity.class);
                    // startActivity(payment);
                    // MainActivity.showExceedingAlert(getContext(),aList.get(i).get("txt")+"\n"+ Common.getStringResourceText(R.string.will_be_live_soon));

                }


            }
        });

    }

    @Override
    protected void onStart() {
        super.onStart();


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }


}
