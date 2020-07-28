package com.nsl.beejtantra.FarmerCoupans;

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
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.mapbox.mapboxsdk.MapmyIndia;
import com.mapbox.mapboxsdk.camera.CameraUpdate;
import com.mapbox.mapboxsdk.camera.CameraUpdateFactory;
import com.mapbox.mapboxsdk.geometry.LatLng;
import com.mapbox.mapboxsdk.maps.MapView;
import com.mapbox.mapboxsdk.maps.MapboxMap;
import com.mapbox.mapboxsdk.maps.OnMapReadyCallback;
import com.mmi.services.account.MapmyIndiaAccountManager;
import com.nsl.beejtantra.Constants;
import com.nsl.beejtantra.DatabaseHandler;
import com.nsl.beejtantra.MyApplication;
import com.nsl.beejtantra.R;
import com.nsl.beejtantra.commonutils.Common;
import com.nsl.beejtantra.geotagging.GeoTaggingMapActivity;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class GeoTaggingMapActivityforFarmerCoupans extends AppCompatActivity implements LocationListener, OnMapReadyCallback,
        GoogleMap.OnMapLoadedCallback, GoogleMap.OnCameraMoveListener, MapboxMap.OnCameraChangeListener {

    @BindView(R.id.btn_calculate_area)
    Button btn_btnCalculateArea;
    @BindView(R.id.tv_area)
    TextView tvArea;
    @BindView(R.id.toolbar_title)
    TextView toolbarTitle;
    @BindView(R.id.toolbar)
    Toolbar toolbar;


    private static final int REQUEST_CHECK_SETTINGS = 111;
    ArrayList<LatLng> polyline = new ArrayList<>();
    private GoogleMap mGoogleMap;
    boolean isEstimationStarted = false;
    private float ACCURACY = 60;
    private boolean onlyOnce = true;
    private String id;
    private int type;
    private String selectedLatLng;
    private String name;
    private LatLng latLng;
    private MapView mapView;
    MapboxMap mapboxMap;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
       /* MapmyIndiaAccountManager.getInstance().setRestAPIKey("5zf2txekry89tciw19sgmjpo7w133ioj");
        MapmyIndiaAccountManager.getInstance().setMapSDKKey("hgxmpb6gldoe2jb2r3upyje5rej6v72p");
        MapmyIndiaAccountManager.getInstance().setAtlasGrantType("client_credentials");
        MapmyIndiaAccountManager.getInstance().setAtlasClientId("qwj3TMxdzY7SIXZq8s3A4xDzY3LBjO3xAepnlJFBOjA_DQ7xzJWYtgfi1mKTFeTCLePMnWjzcGfP3PeOP6QozA==");
        MapmyIndiaAccountManager.getInstance().setAtlasClientSecret("NdJUAD9O1c0LyinGBY0q0A17p-U96zMmvmehrrw4OVI91FWsWwBD2VCd3HVpTBawIi_g0BxxNireuLAJZpwie4283oO0mRYf");
        MapmyIndia.getInstance(getApplicationContext());*/
        // development
        MapmyIndiaAccountManager.getInstance().setRestAPIKey("c2xjhz1dyc4rq6ssp48kdc7setcx6y6t");
        MapmyIndiaAccountManager.getInstance().setMapSDKKey("zfctfo6pdmh3r98gorzd3j6gotpts7mr");
        MapmyIndiaAccountManager.getInstance().setAtlasGrantType("client_credentials");
        MapmyIndiaAccountManager.getInstance().setAtlasClientId("GEKtDly5wD1flnsqJ07VncZorQfC9eNEF89pCB3gqcU4LbCLLR2ljBQDUNsZOuBQWekn7wN7CE0=");
        MapmyIndiaAccountManager.getInstance().setAtlasClientSecret("ebEc8GH231cvvK5eI8n3hx12rAee0CPZq-Qmx8HMTyWJbzqkl9gmNXfIc8gToKlIytraUhIdJ6cEd4jkM3I0jg==");
        MapmyIndia.getInstance(getApplicationContext());
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_geo_tagging_map_activityfor_farmer_coupans);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        /*SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        if (mapFragment == null) {
            mapFragment = SupportMapFragment.newInstance();
            getSupportFragmentManager().beginTransaction().replace(R.id.map, mapFragment).commit();
        }
        mapFragment.getMapAsync(this);*/




        mapView = findViewById(R.id.map);
        mapView.onCreate(savedInstanceState);
        mapView.getMapAsync(this);



        id = getIntent().getStringExtra("id");
        type = getIntent().getIntExtra("type",0);
        name = getIntent().getStringExtra("name");
        toolbarTitle.setText(""+name);
        toolbarTitle.setAllCaps(true);
        Log.d("id ",id+" | "+type+" | "+name);

    }

    @Override
    protected void onStart() {
        super.onStart();
        displayGoogleLocationSettingPage(this, REQUEST_CHECK_SETTINGS);


    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        stopLocationUpdates();
    }

    @OnClick(R.id.btn_calculate_area)
    void setBtn_btnCalculateArea() {

        if (latLng != null && Common.haveInternet(this)) {
            Intent intent = new Intent();
            intent.putExtra("lat", String.valueOf(latLng.getLatitude()));
            intent.putExtra("long", String.valueOf(latLng.getLongitude()));
            setResult(RESULT_OK, intent);
            finish();


        }else {
            Toast.makeText(this,Common.INTERNET_UNABLEABLE,Toast.LENGTH_SHORT).show();

        }

    }

    @Override
    public void onLocationChanged(Location location) {

        if (location != null) {
            if (mapboxMap != null && onlyOnce) {
                myCurrentLocationCamera(mapboxMap, location);
                onlyOnce = false;
            }
           /* if (isEstimationStarted && location.getAccuracy() < ACCURACY) {

                polyline.add(new LatLng(location.getLatitude(), location.getLongitude()));
                addMarkerOnMap(new LatLng(location.getLatitude(), location.getLongitude()), getResources().getDrawable(R.drawable.dot_red));
                if (polyline.size() > 1) {
                    ArrayList<LatLng> latLngs = new ArrayList<>();
                    latLngs.add(polyline.get(polyline.size() - 2));
                    latLngs.add(polyline.get(polyline.size() - 1));
                    addPolyline(latLngs);
                }
            }*/
        }

    }


    protected void stopLocationUpdates() {
        LocationServices.FusedLocationApi.removeLocationUpdates(
                MyApplication.getInstance().getGoogleApiClient(), this);
        Log.d("startLocationUpdates", "Location update stopped .......................");
    }

    protected void startLocationUpdates() {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return;
        }
        PendingResult<Status> pendingResult = LocationServices.FusedLocationApi.requestLocationUpdates(
                MyApplication.getInstance().getGoogleApiClient(), MyApplication.getInstance().getLocationRequest(), this);
        Log.d("startLocationUpdates", "Location update started ..............: ");
    }


    public void displayGoogleLocationSettingPage(final Activity activity, final int requestCode) {

        LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder()
                .addLocationRequest(MyApplication.getInstance().getLocationRequest())
                .setAlwaysShow(true);

        PendingResult<LocationSettingsResult> result =
                LocationServices.SettingsApi.checkLocationSettings(MyApplication.getInstance().getGoogleApiClient(), builder.build());
        result.setResultCallback(new ResultCallback<LocationSettingsResult>() {
            @Override
            public void onResult(LocationSettingsResult result) {
                final Status status = result.getStatus();
                // final LocationSettingsStates s= result.getLocationSettingsStates();
                switch (status.getStatusCode()) {
                    case LocationSettingsStatusCodes.SUCCESS:
                        // All location settings are satisfied. The client can initialize location
                        // requests here.
                        //  Common.myCurrentLocationCamera(currentLocation, gMap);

                        switch (requestCode) {
                            case REQUEST_CHECK_SETTINGS:
                                startLocationUpdates();
                                //calculateArea();
                                break;

                        }

                        break;
                    case LocationSettingsStatusCodes.RESOLUTION_REQUIRED:
                        // Location settings are not satisfied. But could be fixed by showing the user
                        // a dialog.
                        //   Toast.makeText(activity, "RESOLUTION_REQUIRED", Toast.LENGTH_SHORT).show();
                        try {
                            // Show the dialog by calling startResolutionForResult(),
                            // and check the result in onActivityResult().
                            status.startResolutionForResult(activity, requestCode);
                        } catch (IntentSender.SendIntentException e) {
                            // Ignore the error.
                        }
                        break;
                    case LocationSettingsStatusCodes.SETTINGS_CHANGE_UNAVAILABLE:
                        // Location settings are not satisfied. However, we have no way to fix the
                        // settings so we won't show the dialog.

                        break;
                }
            }
        });

    }


    @Override
    public void onActivityResult(final int requestCode, int resultCode, Intent data) {
        // final LocationSettingsStates states = LocationSettingsStates.fromIntent(data);
        Log.d("onActivityResult", "OnresultAxtivity1");
        switch (requestCode) {
            case REQUEST_CHECK_SETTINGS:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        startLocationUpdates();
                        // calculateArea();
                        Log.d("onActivityResult", "OnresultAxtivity4");
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        finish();

                        break;

                    default:
                        break;
                }
                break;


        }
    }


    /*public void calculateArea() {

        Log.d("isEstimationStarted", String.valueOf(isEstimationStarted));
        if (!isEstimationStarted) {
            if (MyApplication.getInstance().getGoogleApiClient() != null && Common.haveInternet(this)) {
                mGoogleMap.clear();
                polyline.clear();
                tvArea.setText("Area:" + 0 + " m");
                Common.getDefaultSP(this).edit().putString("latlng_field_estimation", null).commit();
                Common.getDefaultSP(this).edit().putString("area_in_hectare", null).commit();
                startLocationUpdates();
                isEstimationStarted = true;
                btn_btnCalculateArea.setText(Common.getStringResourceText(R.string.stop));
            } else {
                Toast.makeText(this, Common.INTERNET_UNABLEABLE, Toast.LENGTH_SHORT).show();

            }
        } else {
            isEstimationStarted = false;
            stopLocationUpdates();
            btn_btnCalculateArea.setText(Common.getStringResourceText(R.string.start));
            double area = 0;
            if (polyline != null && polyline.size() > 0) {
                polyline.add(polyline.get(0));
                area = SphericalUtil.computeArea(polyline);
                // double areaInHectare = area / 10000;
                Gson gson = new Gson();
                String json = gson.toJson(polyline);
                Common.getDefaultSP(this).edit().putString("latlng_field_estimation", json).commit();
                Common.getDefaultSP(this).edit().putString("area_in_hectare", String.valueOf(area)).commit();
                // Log.d("isEstimationStarted", "Area:" +area+"\n"+ Math.round(areaInHectare*10000)/10000 +" H");
                tvArea.setText("Area:" + area + " m");
            }
            if (getIntent().hasExtra("yield")) {
                Intent intent = new Intent();
                intent.putExtra("area", area);
                this.setResult(Activity.RESULT_OK, intent);

                finish();

            }


        }
    }*/

    @Override
    public void onMapLoaded() {

    }



    public void myCurrentLocationCamera(MapboxMap mapboxMap, Location location) {
        Log.d("lastknownLocation", location.toString());
        if (location != null && mapboxMap != null) {
            CameraUpdate center = CameraUpdateFactory.newLatLng(new LatLng(location.getLatitude(), location.getLongitude()));
            CameraUpdate zoom = CameraUpdateFactory.zoomTo(Constants.ZOOM_LEVEL);
            mapboxMap.moveCamera(center);
            mapboxMap.animateCamera(zoom);
            Log.d("lastknownLocation", location.toString() + " " + mapboxMap.getCameraPosition());

        }
    }

 /*   public void addMarkerOnMap(LatLng latLng, Drawable drawableId) {
        MarkerOptions options = new MarkerOptions();
        options.position(latLng);
        options.icon(getMarkerIconFromDrawable(drawableId));
        mGoogleMap.addMarker(options);
    }*/

    private BitmapDescriptor getMarkerIconFromDrawable(Drawable drawable) {
        Canvas canvas = new Canvas();
        Bitmap bitmap = Bitmap.createBitmap(drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);
        canvas.setBitmap(bitmap);
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());
        drawable.draw(canvas);
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

 /*   void addPolyline(ArrayList<LatLng> latLngs) {
        PolylineOptions lineOptions = new PolylineOptions();
        lineOptions.addAll(latLngs);
        lineOptions.width(10);
        lineOptions.color(Color.RED);
        mGoogleMap.addPolyline(lineOptions);
    }*/

    @Override
    public void onCameraMove() {

    }

  /*  @Override
    public void onCameraChange(CameraPosition cameraPosition) {
        Log.d("onCameraChange"," "+cameraPosition.toString());
        if (cameraPosition != null) {
            try {
                latLng = cameraPosition.target;
                String addresss = Common.getAddressString(this, cameraPosition.target.latitude, cameraPosition.target.longitude);
                tvArea.setText(addresss);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }*/

    @Override
    public void onMapReady(MapboxMap mapboxMap) {
        this.mapboxMap = mapboxMap;
        //mGoogleMap.setMapType(GoogleMap.MAP_TYPE_NORMAL);
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

            return;
        }
        // mapboxMap.setMyLocationEnabled(true);
        mapboxMap.getUiSettings().setZoomControlsEnabled(true);
        mapboxMap.setPadding(0, 30, 30, 100);
        mapboxMap.setOnCameraChangeListener(this);
    }

    @Override
    public void onMapError(int i, String s) {

    }

    @Override
    public void onCameraChange(com.mapbox.mapboxsdk.camera.CameraPosition cameraPosition) {
        Log.d("onCameraChange"," "+cameraPosition.toString());
        if (cameraPosition != null) {
            try {
                latLng = cameraPosition.target;
                String addresss = Common.getAddressString(this, cameraPosition.target.getLatitude(), cameraPosition.target.getLongitude());
                tvArea.setText(addresss);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    public class Async_RetailerChecking extends AsyncTask<String, String, String> {
        private final int type;
        private final String latLng;
        private String id;
        private ProgressDialog progressDialog;
        private String jsonData;

        public Async_RetailerChecking(String id, int type, String latLng) {
            this.id = id;
            this.type = type;
            this.latLng = latLng;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(GeoTaggingMapActivityforFarmerCoupans.this);
            progressDialog.setMessage("Please wait ...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {

                OkHttpClient client = new OkHttpClient();
                /*For passing parameters*/

                RequestBody formBody = new FormEncodingBuilder()
                        .add("user_id", Common.getUserIdFromSP(GeoTaggingMapActivityforFarmerCoupans.this))
                        .add("distributor_id", id)
                        .add("type", String.valueOf(type))
                        .add("lat_lon", latLng)

                        .build();

                Response responses = null;
                Request request = new Request.Builder()
                        .url(Constants.URL_DISTRIBUTOR_TAGGING)
                        .post(formBody)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();


                try {
                    responses = client.newCall(request).execute();
                    jsonData = responses.body().string();
                    System.out.println("!!!!!!!1 URL_DISTRIBUTOR_TAGGING" + jsonData);

                } catch (IOException e) {
                    e.printStackTrace();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (progressDialog.isShowing())
                progressDialog.dismiss();


            if (jsonData != null) {
                JSONArray jsonarray;
                try {
                    JSONObject jsonobject = new JSONObject(jsonData);
                    System.out.println("!!!!!!!1 postexecuteInsertRetailer" + jsonData);

                    String status = jsonobject.getString("status");
                    if (status.equalsIgnoreCase("success")) {
                        if (type==1) {
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(DatabaseHandler.KEY_TABLE_CUSTOMER_LAT_LNG, latLng);
                            new DatabaseHandler(GeoTaggingMapActivityforFarmerCoupans.this).updateTable(DatabaseHandler.TABLE_CUSTOMERS, contentValues, DatabaseHandler.KEY_TABLE_CUSTOMER_MASTER_ID, id);
                        }else if (type==2){
                            ContentValues contentValues = new ContentValues();
                            contentValues.put(DatabaseHandler.KEY_TABLE_CUSTOMER_LAT_LNG, latLng);
                            new DatabaseHandler(GeoTaggingMapActivityforFarmerCoupans.this).updateTable(DatabaseHandler.TABLE_RETAILER, contentValues, DatabaseHandler.KEY_TABLE_RETAILER_MASTER_ID, id);

                        }

                        AlertDialog.Builder builder = new AlertDialog.Builder(GeoTaggingMapActivityforFarmerCoupans.this);

                        builder.setTitle("Alert !");
                        builder.setMessage("Geotagging done successfully..");

                        builder.setNegativeButton("Exit", new DialogInterface.OnClickListener() {

                            @Override
                            public void onClick(DialogInterface dialog, int which) {

                                // Do nothing
                                dialog.dismiss();
                                finish();
                            }
                        });

                        AlertDialog alert = builder.create();
                        alert.setCancelable(false);
                        alert.show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                // Toast.makeText(getApplicationContext(),"No data found",Toast.LENGTH_LONG).show();
            }

//            finish();

        }
    }

}
