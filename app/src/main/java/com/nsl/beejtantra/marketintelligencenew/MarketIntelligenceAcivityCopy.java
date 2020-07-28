package com.nsl.beejtantra.marketintelligencenew;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.GridView;
import android.widget.SimpleAdapter;

import com.nsl.beejtantra.R;
import com.nsl.beejtantra.marketintelligence.CommodityPriceAllActivity;
import com.nsl.beejtantra.marketintelligence.CropShiftAllActivity;
import com.nsl.beejtantra.marketintelligence.PricingSurveyAllActivity;
import com.nsl.beejtantra.marketintelligence.ProductSurveyAllActivity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class MarketIntelligenceAcivityCopy extends AppCompatActivity {

    private GridView gridView;
    String[] Name = {"Market Potential", "Product & Pricing Survey", "Competitor Channel", "Competitor Strength", "Channel Preference", "Commodity Price", "Crop Shifts"};//
    int[] flags = new int[]{
            R.drawable.market_potential,
            R.drawable.product_pricing,
            R.drawable.competitor_channel,
            R.drawable.competitor_strength, R.drawable.channels_preference, R.drawable.commodity_price, R.drawable.crop_shifts
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_market_intelligence_acivity);


        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                finish();
            }
        });

        gridView = (GridView) findViewById(R.id.gridView);
        List<HashMap<String, String>> aList = new ArrayList<HashMap<String, String>>();

        for (int i = 0; i < 7; i++) {
            HashMap<String, String> hm = new HashMap<String, String>();
            hm.put("txt", Name[i]);
            hm.put("flag", Integer.toString(flags[i]));
            aList.add(hm);
        }

        // Keys used in Hashmap
        String[] from = {"txt", "flag"};

        // Ids of views in listview_layout
        int[] to = {R.id.tv_type, R.id.imageView2};
        SimpleAdapter adapter = new SimpleAdapter(getApplicationContext(), aList, R.layout.row_market_intelligence_selection, from, to);
        gridView.setAdapter(adapter);
        gridView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                view.setSelected(true);
                Intent listIntent = new Intent(MarketIntelligenceAcivityCopy.this, MiTypeListActivity.class);
                listIntent.putExtra("type", i);
                startActivity(listIntent);
            }
        });
    }
}
