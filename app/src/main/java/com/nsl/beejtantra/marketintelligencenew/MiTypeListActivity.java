package com.nsl.beejtantra.marketintelligencenew;

import android.content.Intent;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nsl.beejtantra.Constants;
import com.nsl.beejtantra.DatabaseHandler;
import com.nsl.beejtantra.R;
import com.nsl.beejtantra.adapters.MiCommonAdapter;
import com.nsl.beejtantra.commonutils.Common;
import com.nsl.beejtantra.marketintelligence.MainCommodityPriceActivity;
import com.nsl.beejtantra.marketintelligence.MarketIntelligenceAcivity;
import com.nsl.beejtantra.pojo.ChannelPreference;
import com.nsl.beejtantra.pojo.CommodityPrice;
import com.nsl.beejtantra.pojo.CompetitorStrength;
import com.nsl.beejtantra.pojo.CropShift;
import com.nsl.beejtantra.pojo.ProductPricingSurvey;

import java.nio.channels.Channel;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MiTypeListActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.listView)
    ListView listView;
    @BindView(R.id.empty)
    TextView empty;
    @BindView(R.id.fab)
    FloatingActionButton fab;

    private int type;
    DatabaseHandler db;
    List<AdapterModel> adapterModels = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_type_list);
        ButterKnife.bind(this);
        type = getIntent().getExtras().getInt("type", 0);
        db = new DatabaseHandler(this);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent entryIntent = null;
                switch (type) {
                    case Constants.ViewTypes.MARKET_POTENTIAL:
                        entryIntent = new Intent(getApplicationContext(), MarketPotentialActivity.class);
                        break;
                    case Constants.ViewTypes.CHANNEL_PREFERENCE:
                        entryIntent = new Intent(getApplicationContext(), ChannelPreferenceActivity.class);
                        break;
                    case Constants.ViewTypes.COMMODITY_PRICE:
                        entryIntent = new Intent(getApplicationContext(), CommodityPriceActivity.class);
                        break;
                    case Constants.ViewTypes.COMPETITOR_CHANNEL:
                        entryIntent = new Intent(getApplicationContext(), CompetitorChannelActivity.class);
                        break;
                    case Constants.ViewTypes.COMPETITOR_STRENGTH:
                        entryIntent = new Intent(getApplicationContext(), CompetitorStrengthActivity.class);
                        break;
                    case Constants.ViewTypes.CROP_SHIFTS:
                        entryIntent = new Intent(getApplicationContext(), CropShiftsActivity.class);
                        break;
                    case Constants.ViewTypes.PRODUCT_PRICING:
                        entryIntent = new Intent(getApplicationContext(), ProductPricingSurveyActivity.class);
                        break;

                }
                startActivity(entryIntent);
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        switch (type) {
            case Constants.ViewTypes.MARKET_POTENTIAL:
                toolbarTitle.setText("Market Potential");
                adapterModels.clear();
                List<MarketPotential> marketPotentials = db.getAllMarketPotentials(Common.getTeamFromSP(this));
                if (marketPotentials != null && marketPotentials.size() > 0) {
                    for (int i = 0; i < marketPotentials.size(); i++) {
                        MarketPotential marketPotential = marketPotentials.get(i);
                        Common.Log.i("Market Potential " + marketPotential.userId);
                        AdapterModel adapterModel = new AdapterModel();
                        adapterModel.id = String.valueOf(marketPotential.id);
                        adapterModel.crop = marketPotential.cropName;
                        adapterModel.division = marketPotential.divisionName;
                        adapterModel.village = marketPotential.village;
                        adapterModels.add(adapterModel);
                    }

                }
                break;
            case Constants.ViewTypes.CHANNEL_PREFERENCE:
                toolbarTitle.setText("Channel Preference");
                adapterModels.clear();
                List<ChannelPreference> channelPreferences = db.getAllChannelPreferences(Common.getTeamFromSP(this));
                if (channelPreferences != null && channelPreferences.size() > 0) {
                    for (int i = 0; i < channelPreferences.size(); i++) {
                        ChannelPreference cp = channelPreferences.get(i);
                        Common.Log.i("Market Potential " + cp.userId);
                        AdapterModel adapterModel = new AdapterModel();
                        adapterModel.id = String.valueOf(cp.id);
                        adapterModel.crop=cp.cropName;
                        adapterModel.distributor=db.getDistributorNameById(cp.distributorId);
                        adapterModels.add(adapterModel);
                    }

                }
                break;
            case Constants.ViewTypes.COMMODITY_PRICE:
                toolbarTitle.setText("Commodity Price");
                adapterModels.clear();
                List<CommodityPrice> commodityPriceList = db.getAllCommodityPrices(Common.getTeamFromSP(this));
                if (commodityPriceList != null && commodityPriceList.size() > 0) {
                    for (int i = 0; i < commodityPriceList.size(); i++) {
                        CommodityPrice commodityPrice = commodityPriceList.get(i);
                        Common.Log.i("Market Potential " + commodityPrice.userId);
                        AdapterModel adapterModel = new AdapterModel();
                        adapterModel.id = String.valueOf(commodityPrice.id);
                        adapterModel.crop = commodityPrice.cropName;
                        adapterModel.division = commodityPrice.divisionName;
                        adapterModel.village = commodityPrice.village;
                        adapterModels.add(adapterModel);
                    }
                }
                break;
            case Constants.ViewTypes.COMPETITOR_CHANNEL:
                toolbarTitle.setText("Competitor Channel");
                adapterModels.clear();
                List<CompetitorChannel> competitorChannels = db.getAllCompetitorChannels(Common.getTeamFromSP(this));
                if (competitorChannels != null && competitorChannels.size() > 0) {
                    for (int i = 0; i < competitorChannels.size(); i++) {
                        CompetitorChannel competitorChannel = competitorChannels.get(i);
                        Common.Log.i("Market Potential " + competitorChannel.userId);
                        AdapterModel adapterModel = new AdapterModel();
                        adapterModel.id = String.valueOf(competitorChannel.id);
                        adapterModel.district = db.getDistrictNameById(competitorChannel.district);
                        adapterModel.territory = competitorChannel.territory;
//                        adapterModel.crop = competitorChannel.cropName;
//                        adapterModel.division = competitorChannel.divisionName;
//                        adapterModel.village = competitorChannel.village;
                        adapterModels.add(adapterModel);
                    }

                }
                break;
            case Constants.ViewTypes.COMPETITOR_STRENGTH:
                toolbarTitle.setText("Competitor Strength");
                adapterModels.clear();
                List<CompetitorStrength> competitorStrengths = db.getAllCompetitorStrengths(Common.getTeamFromSP(this));
                if (competitorStrengths != null && competitorStrengths.size() > 0) {
                    for (int i = 0; i < competitorStrengths.size(); i++) {
                        CompetitorStrength competitorStrength = competitorStrengths.get(i);
                        Common.Log.i("Market Potential " + competitorStrength.userId);
                        AdapterModel adapterModel = new AdapterModel();
                        adapterModel.id = String.valueOf(competitorStrength.id);
                        adapterModel.district = db.getDistrictNameById(competitorStrength.district);
                        adapterModel.territory = competitorStrength.territory;
//                        adapterModel.crop = competitorChannel.cropName;
//                        adapterModel.division = competitorChannel.divisionName;
//                        adapterModel.village = competitorChannel.village;
                        adapterModels.add(adapterModel);
                    }

                }
                break;
            case Constants.ViewTypes.CROP_SHIFTS:
                toolbarTitle.setText("Crop Shifts");
                adapterModels.clear();
                List<CropShift> cropShifts = db.getAllCropShifts(Common.getTeamFromSP(this));
                if (cropShifts != null && cropShifts.size() > 0) {
                    for (int i = 0; i < cropShifts.size(); i++) {
                        CropShift pps = cropShifts.get(i);
                        AdapterModel adapterModel = new AdapterModel();
                        adapterModel.id = String.valueOf(pps.id);
                        adapterModel.crop = pps.cropName;
                        adapterModel.division = pps.divisionName;
                        adapterModel.village = pps.village;
                        adapterModels.add(adapterModel);
                    }

                }
                break;
            case Constants.ViewTypes.PRODUCT_PRICING:
                toolbarTitle.setText("Product & Pricing Survey");
                adapterModels.clear();
                List<ProductPricingSurvey> productPricingSurveys = db.getAllProductPricingSurvey(Common.getTeamFromSP(this));
                if (productPricingSurveys != null && productPricingSurveys.size() > 0) {
                    for (int i = 0; i < productPricingSurveys.size(); i++) {
                        ProductPricingSurvey pps = productPricingSurveys.get(i);
                        AdapterModel adapterModel = new AdapterModel();
                        adapterModel.id = String.valueOf(pps.id);
                        adapterModel.crop = pps.cropName;
                        adapterModel.division = pps.divisionName;
                        adapterModel.village = pps.village;
                        adapterModels.add(adapterModel);
                    }

                }
                break;

        }

        MiCommonAdapter miCommonAdapter = new MiCommonAdapter(this, adapterModels, type);
        listView.setAdapter(miCommonAdapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                Intent detailsIntent = new Intent(MiTypeListActivity.this, MiTypeDetailsActivity.class);
                detailsIntent.putExtra("type", type);
                detailsIntent.putExtra("id", adapterModels.get(i).id);
                startActivity(detailsIntent);

            }
        });
    }
}
