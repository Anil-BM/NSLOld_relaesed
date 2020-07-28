package com.nsl.beejtantra.oreo;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.location.Location;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
import com.mapbox.mapboxsdk.MapmyIndia;
import com.mapbox.mapboxsdk.annotations.Icon;
import com.mapbox.mapboxsdk.annotations.IconFactory;
import com.mapbox.mapboxsdk.annotations.Marker;
import com.mapbox.mapboxsdk.annotations.PolylineOptions;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mmi.services.account.MapmyIndiaAccountManager;
import com.nsl.beejtantra.Constants;
import com.nsl.beejtantra.DatabaseHandler;
import com.nsl.beejtantra.Geo_Tracking_POJO;
import com.nsl.beejtantra.GraphActivity;
import com.nsl.beejtantra.R;
import com.nsl.beejtantra.TFA.MarkerData2;
import com.nsl.beejtantra.commonutils.Common;
import com.squareup.picasso.Picasso;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USERS_DESIGNATION;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USERS_EMAIL;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USERS_FIRST_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USERS_HEADQUARTER;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USERS_IMAGE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USERS_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USERS_MOBILE_NO;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USERS_REGION_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USERS_SAP_ID;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_USERS;

public class GeoMeterReadingActivity extends AppCompatActivity implements com.mapbox.mapboxsdk.maps.OnMapReadyCallback {
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.start_value)
    TextView startValue;
    @BindView(R.id.end_value)
    TextView endValue;
    @BindView(R.id.personal_uses_value)
    TextView personalUsesValue;
    @BindView(R.id.odometer_distance)
    TextView odometerDistance;
    @BindView(R.id.gps_distance)
    TextView gpsDistance;
    @BindView(R.id.start_iv)
    ImageView startIv;
    @BindView(R.id.end_iv)
    ImageView endIv;
    @BindView(R.id.weekly)
    TextView weekly;
    @BindView(R.id.monthly)
    TextView monthly;
    @BindView(R.id.name)
    TextView name;
    @BindView(R.id.date_tv)
    TextView date_tv;
    DatabaseHandler db;
    //String url = "http://maps.googleapis.com/maps/api/staticmap?size=720x350&key=AIzaSyBxY-hkH-tCLLaP2ajbMUz9EnQYUn05TLI&path=color:red%7Cweight:4%7Cenc:&signature=W0YtSeTsdXVtYDPX_T5byDQvVqc=";
    String newurl;
    private Bitmap bitmapImg;
   // GoogleMap map;
    private MapView mapView;
    private Marker carMarker = null;
    //private List<LatLng> decodedPolyLine = new ArrayList<>();
    private List<com.mapbox.mapboxsdk.geometry.LatLng> decodedPolyLine = new ArrayList<>();
    private List<MarkerData2> markerPoints = new ArrayList<>();
    MapboxMap mapboxMap;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
    /*    MapmyIndiaAccountManager.getInstance().setRestAPIKey("5zf2txekry89tciw19sgmjpo7w133ioj");
        MapmyIndiaAccountManager.getInstance().setMapSDKKey("hgxmpb6gldoe2jb2r3upyje5rej6v72p");
        MapmyIndiaAccountManager.getInstance().setAtlasGrantType("client_credentials");
        MapmyIndiaAccountManager.getInstance().setAtlasClientId("qwj3TMxdzY7SIXZq8s3A4xDzY3LBjO3xAepnlJFBOjA_DQ7xzJWYtgfi1mKTFeTCLePMnWjzcGfP3PeOP6QozA==");
        MapmyIndiaAccountManager.getInstance().setAtlasClientSecret("NdJUAD9O1c0LyinGBY0q0A17p-U96zMmvmehrrw4OVI91FWsWwBD2VCd3HVpTBawIi_g0BxxNireuLAJZpwie4283oO0mRYf");
        MapmyIndia.getInstance(getApplicationContext());*/

             //development
        MapmyIndiaAccountManager.getInstance().setRestAPIKey("c2xjhz1dyc4rq6ssp48kdc7setcx6y6t");
        MapmyIndiaAccountManager.getInstance().setMapSDKKey("zfctfo6pdmh3r98gorzd3j6gotpts7mr");
        MapmyIndiaAccountManager.getInstance().setAtlasGrantType("client_credentials");
        MapmyIndiaAccountManager.getInstance().setAtlasClientId("GEKtDly5wD1flnsqJ07VncZorQfC9eNEF89pCB3gqcU4LbCLLR2ljBQDUNsZOuBQWekn7wN7CE0=");
        MapmyIndiaAccountManager.getInstance().setAtlasClientSecret("ebEc8GH231cvvK5eI8n3hx12rAee0CPZq-Qmx8HMTyWJbzqkl9gmNXfIc8gToKlIytraUhIdJ6cEd4jkM3I0jg==");
        MapmyIndia.getInstance(getApplicationContext());

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_meter_reading);
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

        mapView = findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);
       /* if (map == null) {
            ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {


                }
            });


        }*/
        String date = getIntent().getStringExtra("date");
        Log.d("date",date);
        String date1 = date == null || date.length() < 9 ? Common.getCurrentDateYYYYMMDD() : date;
        getUserName(date1);
        date_tv.setText(date1);
        if (!date1.equalsIgnoreCase(Common.getCurrentDateYYYYMMDD())){
            weekly.setClickable(false);
            weekly.setEnabled(false);
        }
        final Geo_Tracking_POJO geoTrackingDataByDate = db.getGeoTrackingDataByDate(date1,Common.getUserIdFromSP(this));
        if (geoTrackingDataByDate == null) {
            Toast.makeText(this, "Geo tracking data not available for "+date1+" Date.", Toast.LENGTH_SHORT).show();
            finish();
        } else {

            startValue.setText(Common.isStringNull(geoTrackingDataByDate.getMeter_reading_checkin_text()));
            endValue.setText(Common.isStringNull(geoTrackingDataByDate.getMeter_reading_checkout_text()));
            personalUsesValue.setText(Common.isStringNull(geoTrackingDataByDate.getPersonal_uses_km()));
            Picasso.with(this).load(geoTrackingDataByDate.getMeter_reading_checkin_image()).placeholder(R.drawable.odometer).error(R.drawable.odometer).into(startIv);
            Picasso.with(this).load(geoTrackingDataByDate.getMeter_reading_checkout_image()).placeholder(R.drawable.odometer).error(R.drawable.odometer).into(endIv);

            startIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imagePopup(startIv, geoTrackingDataByDate.getMeter_reading_checkin_image());
                }
            });

            endIv.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    imagePopup(endIv, geoTrackingDataByDate.getMeter_reading_checkout_image());
                }
            });
            double gpsDist = Common.isStringNull(geoTrackingDataByDate.getGeo_distance()).length() == 0 ? 0 : Math.floor(Double.parseDouble(geoTrackingDataByDate.getGeo_distance()));

            double checkInValue = Common.isStringNull(geoTrackingDataByDate.getMeter_reading_checkin_text()).length() == 0 ? 0 : Double.parseDouble(Common.isStringNull(geoTrackingDataByDate.getMeter_reading_checkin_text()));
            double checkOutValue = Common.isStringNull(geoTrackingDataByDate.getMeter_reading_checkout_text()).length() == 0 ? 0 : Double.parseDouble(Common.isStringNull(geoTrackingDataByDate.getMeter_reading_checkout_text()));
            double personalKm = Common.isStringNull(geoTrackingDataByDate.getPersonal_uses_km()).length() == 0 ? 0 : Double.parseDouble(Common.isStringNull(geoTrackingDataByDate.getPersonal_uses_km()));
            double odometerDist = Math.floor(checkOutValue) - Math.floor(checkInValue);
            if (checkInValue <= 0 || checkOutValue <= 0)
                odometerDistance.setText("0");
            else {
                odometerDist = odometerDist - personalKm;
                if (odometerDist <= 0)
                    odometerDist = 0;
                odometerDistance.setText(String.valueOf(odometerDist));
            }
            try {
                String routePath = geoTrackingDataByDate.getGeo_route_path_lat_lon();
                String checkInLatLon = geoTrackingDataByDate.get_Geo_check_in_lat_lon();
                String checkOutLatLon = geoTrackingDataByDate.getGeo_check_out_lat_lon();
//                String marker_me = "color:green|label:start|" + checkInLatLon.split(",")[0] + "," + checkInLatLon.split(",")[1];
//                String marker_dest = "color:red|label:end|" + checkOutLatLon.split(",")[0] + "," + checkOutLatLon.split(",")[1];
//                marker_me = URLEncoder.encode(marker_me, "UTF-8");
//                marker_dest = URLEncoder.encode(marker_dest, "UTF-8");
                if (geoTrackingDataByDate.getPolyline() != null && geoTrackingDataByDate.getPolyline().length() > 0) {
                    decodedPolyLine = Common.decodePolyLinemapmyindia(geoTrackingDataByDate.getPolyline());
                    if (gpsDist > 0) {
                        gpsDist = Math.floor(gpsDist / 1000);
                    }
                    gpsDistance.setText(String.valueOf(gpsDist));
//                    newurl = url + URLEncoder.encode(getLatLngs(latLngList), "UTF-8") + "&markers=" + marker_me + "&markers=" + marker_dest;
                } else {
                    String[] routeSplit = routePath.split(":");
                    Common.Log.i("GeoMeter Route " + routeSplit.length);
                    markerPoints.add(new MarkerData2(new LatLng(Double.parseDouble(checkInLatLon.split(",")[0]), Double.parseDouble(checkInLatLon.split(",")[1])), "", ""));
                    if (routeSplit != null && routeSplit.length > 0) {
                        for (int i = 0; i < routeSplit.length; i++) {
                            LatLng latLng = new LatLng(Double.parseDouble(routeSplit[i].split(",")[0]), Double.parseDouble(routeSplit[i].split(",")[1]));
                            markerPoints.add(new MarkerData2(latLng, "", ""));
                        }
                    }
//                    newurl = url + URLEncoder.encode(getLatLngs(latLngs), "UTF-8") + "&markers=" + marker_me + "&markers=" + marker_dest;
//                    Common.Log.i("Map URL " + newurl);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }



                /*    @SuppressLint("StaticFieldLeak") AsyncTask<Void, Void, Bitmap> setImageFromUrl = new AsyncTask<Void, Void, Bitmap>() {

                        @Override
                        protected Bitmap doInBackground(Void... params) {
                            Bitmap bmp = null;
                            HttpClient httpclient = new DefaultHttpClient();
                            HttpGet request = new HttpGet(newurl);

                            InputStream in = null;
                            try {
                                HttpResponse response = httpclient.execute(request);
                                in = response.getEntity().getContent();
                                bmp = BitmapFactory.decodeStream(in);
                                in.close();
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            return bmp;
                        }

                        protected void onPostExecute(Bitmap bmp) {
                            if (bmp != null) {
                                bitmapImg = bmp;
                                // tripDetails.get(position).bitmap=bmp;

                                mapIv.setImageBitmap(bmp);
//                        notifyItemChanged(holder.location.getVerticalScrollbarPosition());

                            }

                        }
                    };*/

            //  setImageFromUrl.execute();

        }


        weekly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent graphIntent = new Intent(getApplicationContext(), GraphActivity.class);
                graphIntent.putExtra("type", Constants.WEEKLY);
                startActivity(graphIntent);
            }
        });

        monthly.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent graphIntent = new Intent(getApplicationContext(), GraphActivity.class);
                graphIntent.putExtra("type", Constants.MONTHLY);
                startActivity(graphIntent);

            }
        });
    }

    private void drawMap() {

        mapboxMap.clear();
       // final ArrayList<LatLng> points = new ArrayList<>();
        //PolylineOptions lineOptions = new PolylineOptions();
        com.mapbox.mapboxsdk.annotations.PolylineOptions lineOptions = new com.mapbox.mapboxsdk.annotations.PolylineOptions();
        final ArrayList<LatLng> points = new ArrayList<>();
        float distanceValue = 0;
        if (decodedPolyLine == null || decodedPolyLine.size() < 1) {
            if (markerPoints != null && markerPoints.size() > 0) {
                try {
                    IconFactory iconFactory = IconFactory.getInstance(GeoMeterReadingActivity.this);
                    Icon icon= iconFactory.fromResource(R.drawable.mapbox_compass_icon);
                    mapboxMap.addMarker(new com.mapbox.mapboxsdk.annotations.MarkerOptions().position(new com.mapbox.mapboxsdk.geometry.LatLng(
                            markerPoints.get(0).latLng.getLatitude(), markerPoints.get(0).latLng.getLongitude())).icon(icon));
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
            if (markerPoints != null && markerPoints.size() > 1) {
                try {
                    IconFactory iconFactory2 = IconFactory.getInstance(GeoMeterReadingActivity.this);
                    Icon icon2= iconFactory2.fromResource(R.drawable.car_1);
                    carMarker=mapboxMap.addMarker(new com.mapbox.mapboxsdk.annotations.MarkerOptions().position(new com.mapbox.mapboxsdk.geometry.LatLng(
                            markerPoints.get(markerPoints.size() - 1).latLng.getLatitude(), markerPoints.get(markerPoints.size() - 1).latLng.getLongitude())).icon(icon2));
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            if (markerPoints != null && markerPoints.size() > 0) {
                for (int i = 0; i < markerPoints.size(); i++) {
                    try {
                        points.add(markerPoints.get(i).latLng);
                        if (i > 1) {
                            Location location1 = new Location("");
                            location1.setLatitude(markerPoints.get(i - 1).latLng.getLatitude());
                            location1.setLongitude(markerPoints.get(i - 1).latLng.getLongitude());

                            Location location2 = new Location("");
                            location2.setLatitude(markerPoints.get(i).latLng.getLatitude());
                            location2.setLongitude(markerPoints.get(i).latLng.getLongitude());
                            distanceValue = distanceValue + location1.distanceTo(location2);
                        }
                        Log.d("distanceValue", String.valueOf(distanceValue));
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
            }
            if (lineOptions != null && points != null && points.size() > 0) {
                // Adding all the points in the route to LineOptions
                lineOptions.addAll(points);
                lineOptions.width(10);
                lineOptions.color(Color.RED);
                mapboxMap.addPolyline(lineOptions);
                //callDirectionAPI(points);
                distanceValue = round(distanceValue / 1000, 2);
//                distanceValueUpdated = distanceValue;
//                if (timeTaken == null) {
//                    timeTaken = "0 Hr";
//                }
//                double travelAllowance = distanceValue * Double.parseDouble(Common.nullChecker(db.getTravelAllowance(Common.getUserIdFromSP(this))));
                // Common.Log.i("Travel Allowance: " + db.getTravelAllowance(customerId));
                gpsDistance.setText(String.valueOf(distanceValue));
//                tv_distance.setText(" " + distanceValue + "  KM   " + timeTaken + " INR " + Math.round(travelAllowance));
                Log.d("points", "P: " + points.toString());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        zoomRoute(mapboxMap, points);
                    }
                }, 1500);


            }
        } else {

            mapboxMap.addPolyline(new PolylineOptions().addAll(decodedPolyLine).color(Color.RED).width(5));
//            distanceValue = round(Float.parseFloat(Common.nullChecker(distanceValueFinal)) / 1000, 2);
//            double travelAllowance = distanceValue * Double.parseDouble(Common.nullChecker(db.getTravelAllowance(customerId)));
            // Common.Log.i("Travel Allowance: " + db.getTravelAllowance(customerId));
//            tv_distance.setText(" " + distanceValue + "  KM   " + timeTaken + " INR " + Math.round(travelAllowance));

            if (decodedPolyLine != null && decodedPolyLine.size() > 0) {

             /*   MarkerOptions options = new MarkerOptions();
                points.addAll(decodedPolyLine);
                options.position(decodedPolyLine.get(0));
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                mapboxMap.addMarker(options);*/


                IconFactory iconFactory3 = IconFactory.getInstance(GeoMeterReadingActivity.this);
                Icon icon3= iconFactory3.fromResource(R.drawable.mapbox_compass_icon);
                mapboxMap.addMarker(new com.mapbox.mapboxsdk.annotations.MarkerOptions().position(new com.mapbox.mapboxsdk.geometry.LatLng(decodedPolyLine.get(0))).icon(icon3));


            }

            if (decodedPolyLine != null && decodedPolyLine.size() > 1) {

               /* MarkerOptions options = new MarkerOptions();
                options.position(decodedPolyLine.get(decodedPolyLine.size() - 1));
                options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                mapboxMap.addMarker(options);*/
                IconFactory iconFactory4 = IconFactory.getInstance(GeoMeterReadingActivity.this);
                Icon icon4 = iconFactory4.fromResource(R.drawable.mapbox_compass_icon);
                mapboxMap.addMarker(new com.mapbox.mapboxsdk.annotations.MarkerOptions().position(new com.mapbox.mapboxsdk.geometry.LatLng(decodedPolyLine.get(decodedPolyLine.size() - 1))).icon(icon4));

                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        zoomRoute(mapboxMap, decodedPolyLine);
                    }
                }, 1500);


            }

        }

    }

    /*private String getLatLngs(List<LatLng> latLngList) {
        List<LatLng> newLatLngList = new ArrayList<>();
        for (int i = 0; i < latLngList.size(); i++) {
            Location location1 = new Location("GPS");
            Location location2 = new Location("GPS");

            location1.setLatitude(latLngList.get(i).getLatitude());
            location1.setLongitude(latLngList.get(i).getLongitude());
            if (i > 0 && latLngList.size() > 0) {
                location2.setLatitude(newLatLngList.get(newLatLngList.size() - 1).getLatitude());
                location2.setLongitude(newLatLngList.get(newLatLngList.size() - 1).getLongitude());
            }

            if (i == 0 || isDistanceGrater(location2, location1, 5000)) {
                newLatLngList.add(new LatLng(location1.getLatitude(), location1.getLongitude()));
            }

        }
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < newLatLngList.size(); i++) {
            buffer.append(newLatLngList.get(i) + ",");
        }
        Common.Log.i("Points List " + buffer.toString());
        String newP = Common.encode(newLatLngList);
        Common.Log.i("points " + newP);
        return newP;

    }*/

    private boolean isDistanceGrater(Location location1, Location location2, float metres) {
        float distance = location1.distanceTo(location2);
        if (distance > metres) {
            Log.d("distance", String.valueOf(distance));
            return true;
        }

        return false;
    }

    /*public void zoomRoute(MapboxMap googleMap, List<LatLng> lstLatLngRoute) {

        if (googleMap == null || lstLatLngRoute == null || lstLatLngRoute.isEmpty() || lstLatLngRoute.size() < 2)
            return;

        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (LatLng latLngPoint : lstLatLngRoute) {
            boundsBuilder.include(latLngPoint);
            Common.Log.i("Points " + latLngPoint);
        }

        int routePadding = 1;
        LatLngBounds latLngBounds = boundsBuilder.build();

        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding));
    }*/
    public void zoomRoute(MapboxMap mapboxMap, List<LatLng> lstLatLngRoute) {

        if (mapboxMap == null || lstLatLngRoute == null || lstLatLngRoute.isEmpty() || lstLatLngRoute.size() < 2)
            return;

        com.mapbox.mapboxsdk.geometry.LatLngBounds.Builder boundsBuilder = new com.mapbox.mapboxsdk.geometry.LatLngBounds.Builder();
        for (LatLng latLngPoint : lstLatLngRoute)
            boundsBuilder.include(latLngPoint);

        int routePadding = 50;
        com.mapbox.mapboxsdk.geometry.LatLngBounds latLngBounds = boundsBuilder.build();

        mapboxMap.moveCamera(com.mapbox.mapboxsdk.camera.CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding));
    }
    public void imagePopup(ImageView imageView, String path) {
        AlertDialog.Builder bill_1 = new AlertDialog.Builder(this);
        LayoutInflater factory = LayoutInflater.from(this);
        View newview = factory.inflate(R.layout.imageview, null, false);
        PhotoView photoView_1 = newview.findViewById(R.id.show_image);
        ImageView cancel_dialog = newview.findViewById(R.id.cancel_dialog);
        final ProgressBar progressBar = newview.findViewById(R.id.progressBar);
        // photoView_1.setImageDrawable(imageView.getDrawable());
        Picasso.with(GeoMeterReadingActivity.this).load(path).into(photoView_1, new com.squareup.picasso.Callback() {
            @Override
            public void onSuccess() {
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onError() {
                progressBar.setVisibility(View.GONE);

            }
        });
        bill_1.setView(newview);

        final AlertDialog alertDialog = bill_1.create();

        alertDialog.show();
        alertDialog.setCancelable(true);
        cancel_dialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.dismissAlertDialog(alertDialog);
            }
        });
    }

    public static float round(double value, int places) {
        if (places >= 0) {
            try {

                BigDecimal bd = new BigDecimal(value);
                bd = bd.setScale(places, RoundingMode.HALF_UP);
                return bd.floatValue();
            } catch (NumberFormatException e) {
                e.printStackTrace();
            }
        }
        return 0;
    }

    public void getUserName(String date1){
        try {
            String selectQuery = "SELECT " + KEY_TABLE_USERS_FIRST_NAME + "," + KEY_TABLE_USERS_SAP_ID + "," + KEY_TABLE_USERS_MOBILE_NO + "," + KEY_TABLE_USERS_EMAIL + "," + KEY_TABLE_USERS_DESIGNATION + "," + KEY_TABLE_USERS_HEADQUARTER + "," + KEY_TABLE_USERS_REGION_ID + "," + KEY_TABLE_USERS_IMAGE + " FROM " + TABLE_USERS + "  WHERE " + KEY_TABLE_USERS_MASTER_ID + " = " + Common.getUserIdFromSP(GeoMeterReadingActivity.this);
            SQLiteDatabase sdbw = db.getWritableDatabase();
            Cursor cursor = sdbw.rawQuery(selectQuery, null);
            System.out.println("cursor count "+selectQuery+"\n"+cursor.getCount());
            if (cursor.moveToFirst()) {
                //  Log.e("-----", "fname : " + cursor.getString(0) + "lname : " + cursor.getString(1) + "mobile : " + cursor.getString(2) + "email : " + cursor.getString(3));
                name.setText(""+Common.isStringNull(cursor.getString(0)));


            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        // check if map is created successfully or not
        this.mapboxMap=mapboxMap;
        if (mapboxMap == null) {
            return;
            //Toast.makeText(getApplicationContext(), "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
        }


        if (ActivityCompat.checkSelfPermission(GeoMeterReadingActivity.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(GeoMeterReadingActivity.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        //map.setMyLocationEnabled(false);
       /* mapboxMap.getUiSettings().setZoomControlsEnabled(false);
        mapboxMap.getUiSettings().setZoomGesturesEnabled(false);
        mapboxMap.setPadding(0, 30, 30, 150);*/
        //googleMap.getUiSettings().setRotateGesturesEnabled(true);
        //googleMap.getUiSettings().setMyLocationButtonEnabled(true);
        // googleMap.getUiSettings().setCompassEnabled(true);
        // map.setMaxZoomPreference(15);
        drawMap();
    }

    @Override
    public void onMapError(int i, String s) {

    }
}
