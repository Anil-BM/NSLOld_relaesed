package com.nsl.beejtantra;

import android.graphics.Typeface;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IndexAxisValueFormatter;
import com.github.mikephil.charting.interfaces.datasets.IBarDataSet;
import com.nsl.beejtantra.commonutils.Common;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GraphActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView toolbar_title;
    DatabaseHandler db;
    int type;

    @BindView(R.id.chart1)
    BarChart barChart;
    private Typeface mTfLight;
    private List<String> labels = new ArrayList<>();
    private int days;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graph);
        ButterKnife.bind(this);
        type = getIntent().getExtras().getInt("type", 0);
        db = new DatabaseHandler(this);
        mTfLight = Typeface.createFromAsset(getAssets(), Constants.CUSTOM_FONT_PATH_LIGHT);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        String text = type == Constants.MONTHLY ? "Monthly Graph" : "Weekly Graph";
        toolbar_title.setText(text);
        List<String> recordDates = getSpecifiedDates();

//        List<Geo_Tracking_POJO> geoRecords = db.getGeoTrackingDataForDays(recordDate);


//        float randomMultiplier = mSeekBarY.getProgress() * 100000f;

//        for (int i = 0; i < geoRecords.size(); i++) {
//            gpsDistance.add(new BarEntry(i, (float) (Math.floor(Double.parseDouble(geoRecords.get(i).getGeo_distance())) / 1000)));
//            double checkInValue = Common.isStringNull(geoRecords.get(i).getMeter_reading_checkin_text()).length() == 0 ? 0 : Double.parseDouble(Common.isStringNull(geoRecords.get(i).getMeter_reading_checkin_text()));
//            double checkOutValue = Common.isStringNull(geoRecords.get(i).getMeter_reading_checkout_text()).length() == 0 ? 0 : Double.parseDouble(Common.isStringNull(geoRecords.get(i).getMeter_reading_checkout_text()));
//            float odometerDist = (float) (Math.floor(checkOutValue) - Math.floor(checkInValue));
//            odometerDistance.add(new BarEntry(i, odometerDist));
//        }

        List<BarEntry> gps = new ArrayList<BarEntry>();
        List<BarEntry> odometer = new ArrayList<BarEntry>();


        for (int i = 0; i < recordDates.size(); i++) {
//            gps.add(new BarEntry(i, 0.4f));
            if (recordDates.size() - 1 == i)
                labels.add("");
            else
                labels.add(recordDates.get(i));
            Geo_Tracking_POJO geoRecord = db.getGeoTrackingDataByDate(recordDates.get(i), Common.getUserIdFromSP(this));
            if (geoRecord == null) {
                gps.add(new BarEntry(i, 0));
                odometer.add(new BarEntry(i, 0));
            } else {
                gps.add(new BarEntry(i, (float) (Math.floor(Double.parseDouble(Common.isStringNull(geoRecord.getGeo_distance()).length() == 0 ? "0" : Common.isStringNull(geoRecord.getGeo_distance()))) / 1000)));
                double checkInValue = Common.isStringNull(geoRecord.getMeter_reading_checkin_text()).length() == 0 ? 0 : Double.parseDouble(Common.isStringNull(geoRecord.getMeter_reading_checkin_text()));
                double checkOutValue = Common.isStringNull(geoRecord.getMeter_reading_checkout_text()).length() == 0 ? 0 : Double.parseDouble(Common.isStringNull(geoRecord.getMeter_reading_checkout_text()));
                float odometerDist = (float) (Math.floor(checkOutValue) - Math.floor(checkInValue));
                if (odometerDist <= 0)
                    odometerDist = 0;

                odometer.add(new BarEntry(i, odometerDist));
            }
        }

        barChart.setDrawBarShadow(false);
        barChart.setDrawValueAboveBar(true);
//        barChart.setDescription("");
        barChart.setDescription(null);
        barChart.setMaxVisibleValueCount(50);
        barChart.setPinchZoom(true);
        barChart.setDrawGridBackground(false);

        XAxis xl = barChart.getXAxis();
        xl.setGranularity(1f);
        xl.setCenterAxisLabels(true);
        xl.setPosition(XAxis.XAxisPosition.BOTTOM);
        xl.setLabelRotationAngle(90f);
        IndexAxisValueFormatter indexAxisValueFormatter = new IndexAxisValueFormatter();
        String[] lablesArray = new String[labels.size()];
        for (int i = 0; i < labels.size(); i++) {
            lablesArray[i] = labels.get(i);

        }
        indexAxisValueFormatter.setValues(lablesArray);
        xl.setValueFormatter(indexAxisValueFormatter);
      /*  xl.setValueFormatter(new AxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return String.valueOf((int) value);
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });*/

        YAxis leftAxis = barChart.getAxisLeft();
       /* leftAxis.setValueFormatter(new AxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return String.valueOf((int) value);
            }

            @Override
            public int getDecimalDigits() {
                return 0;
            }
        });*/
        leftAxis.setDrawGridLines(false);
        leftAxis.setSpaceTop(30f);
        leftAxis.setAxisMinValue(0f); // this replaces setStartAtZero(true
        barChart.getAxisRight().setEnabled(false);

        //data
        float groupSpace = 0.04f;
        float barSpace = 0.02f; // x2 dataset
        float barWidth = 0.46f; // x2 dataset
        // (0.46 + 0.02) * 2 + 0.04 = 1.00 -> interval per "group"

//        int startYear = 1980;
//        int endYear = 1985;


//        for (int i = startYear; i < endYear; i++) {
//            odometer.add(new BarEntry(i, 0.7f));
//        }


        BarDataSet set1, set2;

        if (barChart.getData() != null && barChart.getData().getDataSetCount() > 0) {
            set1 = (BarDataSet) barChart.getData().getDataSetByIndex(0);
            set2 = (BarDataSet) barChart.getData().getDataSetByIndex(1);
            set2.setValues(gps);
            set1.setValues(odometer);
            barChart.getData().notifyDataChanged();
            barChart.notifyDataSetChanged();
        } else {
            // create 2 datasets with different types
            set2 = new BarDataSet(gps, "GPS");
            set2.setColor(getResources().getColor(R.color.colorPrimary));
            set1 = new BarDataSet(odometer, "Odometer");
            set1.setColor(getResources().getColor(R.color.odometer_color));

            ArrayList<IBarDataSet> dataSets = new ArrayList<IBarDataSet>();
            dataSets.add(set1);
            dataSets.add(set2);

            BarData data = new BarData(dataSets);
            barChart.setData(data);
        }


        barChart.getBarData().setBarWidth(barWidth);
        barChart.getXAxis().setAxisMinValue(0);
        barChart.groupBars(0, groupSpace, barSpace);
        barChart.invalidate();


    }

    private List<String> getSpecifiedDates() {
        long daysInMillis = 1000 * 60 * 60 * 24;
        days = 7;
        if (type == 2) {
            try {
                days = Integer.parseInt(Common.getCurrentDateDD());
            }catch (Exception e){
                e.printStackTrace();
            }
        }
        List<String> dates = new ArrayList<>();
        for (int i = days; i >= 0; i--) {
            long longDate = System.currentTimeMillis() - (i * daysInMillis);
            Date d = new Date(longDate);
            dates.add(Common.getSpecifiedDate(d));
        }
        Date fDate = new Date(2 * System.currentTimeMillis());
        dates.add(Common.getSpecifiedDate(fDate));
        return dates;

    }
}
