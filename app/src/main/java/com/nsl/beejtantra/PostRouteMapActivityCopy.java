package com.nsl.beejtantra;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Point;
import android.graphics.PorterDuff;
import android.graphics.drawable.Drawable;
import android.location.Location;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.PowerManager;
import android.os.SystemClock;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.chrisbanes.photoview.PhotoView;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.Projection;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.JointType;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.LatLngBounds;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.maps.model.RoundCap;
import com.nsl.beejtantra.commonutils.Common;
import com.nsl.beejtantra.oreo.Utils;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;

import org.joda.time.DateTime;
import org.joda.time.Hours;
import org.joda.time.Minutes;
import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.nsl.beejtantra.Constants.SharedPrefrancesKey.ROLE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_GEO_TRACKING_CHECK_IN_LAT_LONG;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_GEO_TRACKING_CHECK_IN_TIME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_GEO_TRACKING_CHECK_OUT_LAT_LONG;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_GEO_TRACKING_CHECK_OUT_TIME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_GEO_TRACKING_DISTANCE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_GEO_TRACKING_FFMID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_GEO_TRACKING_POLYLINE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_GEO_TRACKING_ROUTE_PATH_LAT_LONG;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_GEO_TRACKING_USER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_GEO_TRACKING_VERSION;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_GEO_TRACKING_VISIT_DATE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USERS_DESIGNATION;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USERS_EMAIL;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USERS_FIRST_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USERS_HEADQUARTER;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USERS_IMAGE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USERS_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USERS_MOBILE_NO;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USERS_REGION_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USERS_SAP_ID;
import static com.nsl.beejtantra.DatabaseHandler.METER_READING_CHECKIN_IMAGE;
import static com.nsl.beejtantra.DatabaseHandler.METER_READING_CHECKIN_TEXT;
import static com.nsl.beejtantra.DatabaseHandler.METER_READING_CHECKOUT_IMAGE;
import static com.nsl.beejtantra.DatabaseHandler.METER_READING_CHECKOUT_TEXT;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_GEO_TRACKING;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_USERS;
import static com.nsl.beejtantra.oreo.LocationUpdatesService.ACTION_BROADCAST_LOCATION_MAP;
import static com.nsl.beejtantra.oreo.LocationUpdatesService.EXTRA_LOCATION;

public class PostRouteMapActivityCopy extends AppCompatActivity implements GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener, LocationListener {

    private static final int REQUEST_CHECK_SETTINGS = 111;
    GoogleMap map;
    DatabaseHandler db;
    ArrayList<MarkerData> markerPoints;
    LatLng point;
    String userId, jsonData, datefromcalander, datefromrecords, str_address, str_name, str_userName;
    SQLiteDatabase sdbw, sdbr;
    SimpleDateFormat sdf;
    SharedPreferences sharedpreferences;

    //Define a request code to send to Google Play services
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    int hours;
    String latitude, longitude, str_distance, timeTaken;
    double duration;
    public static final String mypreference = "mypref";
    private TextView tv_distance, tv_name;
    private EditText et_date;
    private Spinner spin_company;
    private ArrayList<SelectedCities> organisations;
    ArrayList<String> adaptercity;
    private String team;
    private int role;
    private String userName;
    private String customerId;
    private LinearLayout ll_select;
    private Location myCurrentLocation;
    long distanceFinal = 0;
    private List<LatLng> decodedPolyLine = new ArrayList<>();
    private String distanceValueFinal = "0";
    private boolean noData = false;
    private MyReceiver myReceiver;
    private boolean isMarkerRotating = false;
    private Marker carMarker = null;
    private Timer timer = null;
    private Date datePickerDate = null;
    private DateTime checkInDate = null;
    private float distanceValueUpdated = 0;
    private PowerManager.WakeLock screenLock;
    private RelativeLayout buttom_sheet_layout;
    private BottomSheetBehavior<RelativeLayout> relativeLayoutBottomSheetBehavior;
    private boolean canShowButtomView = false;
    private String meterReadingCheckInTxt;
    private String meterReadingCheckInImg;
    private String meterReadingCheckoutTxt;
    private String meterReadingCheckoutImg;
    private TextView checkout_txt;
    private TextView checkin_txt;
    private TextView checkin_img;
    private TextView checkout_img;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_routemap);
        turnOnScreen(this, 0);
        db = new DatabaseHandler(this);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        userId = sharedpreferences.getString("userId", "");
        team = sharedpreferences.getString("team", "");
        role = sharedpreferences.getInt(ROLE, 0);

        sdf = new SimpleDateFormat(Constants.DateFormat.COMMON_DATE_FORMAT);
        tv_distance = (TextView) findViewById(R.id.tv_distance);
        tv_name = (TextView) findViewById(R.id.tv_name);
        et_date = (EditText) findViewById(R.id.et_date);
        spin_company = (Spinner) findViewById(R.id.spin_user);
        ll_select = (LinearLayout) findViewById(R.id.ll_select);
        buttom_sheet_layout = (RelativeLayout) findViewById(R.id.buttom_sheet_layout);
        checkout_txt = (TextView) findViewById(R.id.checkout_txt);
        checkin_txt = (TextView) findViewById(R.id.checkin_txt);
        checkin_img = (TextView) findViewById(R.id.checkin_img);
        checkout_img = (TextView) findViewById(R.id.checkout_img);
        relativeLayoutBottomSheetBehavior = BottomSheetBehavior.from(buttom_sheet_layout);
        tv_distance.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!canShowButtomView) {

                    return;
                }
                if (relativeLayoutBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                    relativeLayoutBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                } else {
                    relativeLayoutBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                }
            }
        });
        checkin_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (Common.haveInternet(PostRouteMapActivityCopy.this)) {
                        if (meterReadingCheckInImg != null && meterReadingCheckInImg.length() > 10)
                            ImagepopUp(null, meterReadingCheckInImg);
                        else
                            Common.customToast(PostRouteMapActivityCopy.this, "Image not available..");
                    } else {
                        Common.customToast(PostRouteMapActivityCopy.this, Common.INTERNET_UNABLEABLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        checkout_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if (Common.haveInternet(PostRouteMapActivityCopy.this)) {
                        if (meterReadingCheckoutImg != null && meterReadingCheckoutImg.length() > 10)
                            ImagepopUp(null, meterReadingCheckoutImg);
                        else
                            Common.customToast(PostRouteMapActivityCopy.this, "Image not available..");
                    } else {
                        Common.customToast(PostRouteMapActivityCopy.this, Common.INTERNET_UNABLEABLE);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });
        et_date.setVisibility(View.VISIBLE);
        if (role == Constants.Roles.ROLE_7) {
            ll_select.setVisibility(View.GONE);
            customerId = userId;
            Common.saveDataInSP(getApplicationContext(), Constants.CUSTOMER_ID_GEO, customerId);
        }

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        callapi();

        datefromcalander = PlanerMainActivity.curDate;

        if (datefromcalander == null) {
            Calendar c = Calendar.getInstance();
            System.out.println("Current time => " + c.getTime());

            SimpleDateFormat df = new SimpleDateFormat(Constants.DateFormat.COMMON_DATE_FORMAT);
            datefromcalander = df.format(c.getTime());
        }
        System.out.println("Current time => " + datefromcalander);
        et_date.setText(datefromcalander);
        // Initializing
        markerPoints = new ArrayList<MarkerData>();

        // map                = ((MapFragment) getFragmentManager().findFragmentById(R.id.map)).getMap();

        if (map == null) {
            ((MapFragment) getFragmentManager().findFragmentById(
                    R.id.map)).getMapAsync(new OnMapReadyCallback() {
                @Override
                public void onMapReady(GoogleMap googleMap) {

                    // check if map is created successfully or not
                    if (googleMap == null) {
                        return;
                        //Toast.makeText(getApplicationContext(), "Sorry! unable to create maps", Toast.LENGTH_SHORT).show();
                    }
                    map = googleMap;
                    map.setMapType(GoogleMap.MAP_TYPE_NORMAL);
                    if (ActivityCompat.checkSelfPermission(PostRouteMapActivityCopy.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(PostRouteMapActivityCopy.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                        return;
                    }
                    map.setMyLocationEnabled(false);
                    map.getUiSettings().setZoomControlsEnabled(true);
                    map.setPadding(0, 30, 30, 150);
                    //googleMap.getUiSettings().setRotateGesturesEnabled(true);
                    //googleMap.getUiSettings().setMyLocationButtonEnabled(true);
                    // googleMap.getUiSettings().setCompassEnabled(true);
                    // map.setMaxZoomPreference(15);
                    map.setOnMyLocationButtonClickListener(new GoogleMap.OnMyLocationButtonClickListener() {
                        @Override
                        public boolean onMyLocationButtonClick() {
                            /*if (myCurrentLocation == null) {
                                if (ActivityCompat.checkSelfPermission(PostRouteMapActivityCopy.this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(PostRouteMapActivityCopy.this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                                    return false;
                                }
                                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, PostRouteMapActivityCopy.this);
                            }
*/
                            displayGoogleLocationSettingPage(PostRouteMapActivityCopy.this, REQUEST_CHECK_SETTINGS);
                            return true;
                        }
                    });
                }
            });

            try {
                if (datefromcalander != null && datefromcalander.length() > 5 && customerId != null && !customerId.equalsIgnoreCase(""))
                    new Async_getalloffline().execute(datefromcalander);
            } catch (Exception e) {
                Log.d("Tag", e.toString());
            }

        }


        mGoogleApiClient = new GoogleApiClient.Builder(getApplicationContext())
                // The next two lines tell the new client that “this” current class will handle connection stuff
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build();

        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 3000)
                // 10 seconds, in milliseconds
                .setFastestInterval(1 * 2000); // 1 second, in milliseconds
        et_date.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentDate = Calendar.getInstance();
                final int mYear = mcurrentDate.get(Calendar.YEAR);
                final int mMonth = mcurrentDate.get(Calendar.MONTH);
                final int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog mDatePicker = new DatePickerDialog(PostRouteMapActivityCopy.this, new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                        int sel_month = selectedmonth + 1;
                        String sday = String.valueOf(selectedday);
                        String smonth = null;
                        if (sel_month < 10)
                            smonth = "0" + sel_month;
                        else
                            smonth = String.valueOf(sel_month);

                        if (selectedday < 10)
                            sday = "0" + selectedday;
                        else
                            sday = String.valueOf(selectedday);

                        Calendar selectedCal = Calendar.getInstance();
                        selectedCal.set(selectedyear, selectedmonth, selectedday);
                        long selectedMilli = selectedCal.getTimeInMillis();

                        datePickerDate = new Date(selectedMilli);
                        if (datePickerDate.after(new Date())) {

                            Toast.makeText(PostRouteMapActivityCopy.this, "Please select current or previous date", Toast.LENGTH_SHORT).show();
                        } else {
                            map.clear();
                            markerPoints.clear();
                            et_date.setText(selectedyear + "-" + smonth + "-" + sday);
                            tv_distance.setVisibility(View.VISIBLE);
                            datefromcalander = et_date.getText().toString();
                            try {
                                if (customerId != null && !customerId.equalsIgnoreCase(""))
                                    new Async_getalloffline().execute(datefromcalander);
                            } catch (Exception e) {
                                Log.d("Tag", e.toString());
                            }
                        }
                        /*if (datePickerDate.equals(new Date()) && customerId!=Common.getUserIdFromSP(PostRouteMapActivityCopy.this) && role!=Constants.Roles.ROLE_7){
                            registerReceiver();
                        }else {
                            unRegisterReceiver();
                        }*/

                    }
                }, mYear, mMonth, mDay);
                mDatePicker.getDatePicker().setMaxDate(System.currentTimeMillis());
                //mDatePicker.setTitle("Select date");
                mDatePicker.show();
            }
        });


        String selectQuery = "SELECT " + KEY_TABLE_USERS_FIRST_NAME + "," + KEY_TABLE_USERS_SAP_ID + "," + KEY_TABLE_USERS_MOBILE_NO + "," + KEY_TABLE_USERS_EMAIL + "," + KEY_TABLE_USERS_DESIGNATION + "," + KEY_TABLE_USERS_HEADQUARTER + "," + KEY_TABLE_USERS_REGION_ID + "," + KEY_TABLE_USERS_IMAGE + " FROM " + TABLE_USERS + "  WHERE " + KEY_TABLE_USERS_MASTER_ID + " = " + userId;
        //List<Company_division_crops> cdclist = db.getAllCompany_division_crops();
        Cursor cursor = null;
        try {
            sdbw = db.getWritableDatabase();
            cursor = sdbw.rawQuery(selectQuery, null);
            //System.out.println("cursor count "+cursor.getCount());
            if (cursor != null && cursor.moveToFirst()) {
                Log.e("-----", "fname : " + cursor.getString(0) + "lname : " + cursor.getString(1) + "mobile : " + cursor.getString(2) + "email : " + cursor.getString(3));
                str_userName = cursor.getString(0);
            }
            tv_name.setText(str_userName + " > " + datefromcalander);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Common.closeCursor(cursor);
            Common.closeDataBase(sdbw);
        }

/*Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
    @Override
    public void uncaughtException(Thread thread, Throwable throwable) {
        Common.writeFile(throwable.toString());
    }
});*/
        bottomSheetView();
    }

    private void bottomSheetView() {
        // relativeLayoutBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
        relativeLayoutBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
            @Override
            public void onStateChanged(@NonNull View bottomSheet, int newState) {
                switch (newState) {
                    case BottomSheetBehavior.STATE_HIDDEN:
                        Log.d("BottomSheetBehavior", "STATE_HIDDEN");
                        break;
                    case BottomSheetBehavior.STATE_EXPANDED: {
                        //  viewPager.beginFakeDrag();
                        //   btnBottomSheet.setText("Close Sheet");
                        Log.d("BottomSheetBehavior", "STATE_EXPANDED");
                    }
                    break;
                    case BottomSheetBehavior.STATE_COLLAPSED: {
                        // btnBottomSheet.setText("Expand Sheet");
                        Log.d("BottomSheetBehavior", "STATE_COLLAPSED");
                        //  viewPager.endFakeDrag();
                    }
                    break;
                    case BottomSheetBehavior.STATE_DRAGGING:
                        Log.d("BottomSheetBehavior", "STATE_DRAGGING");
                        // relativeLayoutBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                        break;
                    case BottomSheetBehavior.STATE_SETTLING:
                        Log.d("BottomSheetBehavior", "STATE_SETTLING");
                        break;
                }
            }

            @Override
            public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                Log.d("BottomSheetBehavior", "onSlide: " + slideOffset);
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

    @Override
    public void onLocationChanged(Location location) {
        //  myCurrentLocation = location;
        //handleNewLocation(location);
        Common.Log.i("Location: " + String.valueOf(location.getLatitude()));
    }


    @Override
    public void onConnected(@Nullable Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling

            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
        // LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
        if (location != null) {
            handleNewLocation(location, 15);
        }
    }

    private void handleNewLocation(Location location, float zoom) {
        if (location != null && map != null) {
            latitude = String.valueOf(location.getLatitude());
            longitude = String.valueOf(location.getLongitude());
            LatLng newLatLng = new LatLng(location.getLatitude(), location.getLongitude());
            CameraUpdate yourLocation = CameraUpdateFactory.newLatLngZoom(newLatLng, zoom);
            map.animateCamera(yourLocation);
        }
        // Marker marker = googleMap.addMarker(new MarkerOptions().position(new LatLng(lat, lng)).title("You are here!").icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED)));
        //  Toast.makeText(getActivity(),latitude + longitude,Toast.LENGTH_LONG).show();
        // System.out.println("Location  : " + latitude + "==" + longitude + "==");

    }

    @Override
    public void onConnectionSuspended(int i) {

    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
 /*
             * Google Play services can resolve some errors it detects.
             * If the error has a resolution, try sending an Intent to
             * start a Google Play services activity that can resolve
             * error.
             */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                    /*
                     * Thrown if Google Play services canceled the original
                     * PendingIntent
                     */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
                /*
                 * If no resolution is available, display a dialog to the
                 * user with the error.
                 */
            Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }


    @Override
    public void onBackPressed() {
        finish();
    }

    @Override
    protected void onDestroy() {
        if (mGoogleApiClient != null)
            mGoogleApiClient.disconnect();
        this.screenLock.release();
        super.onDestroy();
        finish();
    }

    @Override
    protected void onPause() {
        super.onPause();
//        LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient, PostRouteMapActivityCopy.this);
        if (myReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
            myReceiver = null;
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            Intent home = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(home);
            finish();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class Async_getalloffline extends AsyncTask<String, Void, String> {
        ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* progressDialog = new ProgressDialog(PostRouteMapActivityCopy.this);
            progressDialog.setMessage("Please wait ...");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();*/
            markerPoints.clear();
            decodedPolyLine.clear();
            distanceValueFinal = "0";
            carMarker = null;
            canShowButtomView = false;
        }

        protected String doInBackground(String... params) {

            Cursor cursor = null;
            try {

                String selectQuery = "SELECT DISTINCT "
                        + KEY_TABLE_GEO_TRACKING_CHECK_IN_LAT_LONG + ","
                        + KEY_TABLE_GEO_TRACKING_CHECK_OUT_LAT_LONG + ","
                        + KEY_TABLE_GEO_TRACKING_ROUTE_PATH_LAT_LONG + ","
                        + KEY_TABLE_GEO_TRACKING_VISIT_DATE + ","
                        + KEY_TABLE_GEO_TRACKING_CHECK_IN_TIME + ","
                        + KEY_TABLE_GEO_TRACKING_CHECK_OUT_TIME + ","
                        + KEY_TABLE_GEO_TRACKING_POLYLINE + ","
                        + KEY_TABLE_GEO_TRACKING_VERSION + ","
                        + KEY_TABLE_GEO_TRACKING_DISTANCE + ","
                        + METER_READING_CHECKIN_TEXT + ","
                        + METER_READING_CHECKIN_IMAGE + ","
                        + METER_READING_CHECKOUT_TEXT + ","
                        + METER_READING_CHECKOUT_IMAGE
                        + " FROM " + TABLE_GEO_TRACKING + " where " + KEY_TABLE_GEO_TRACKING_USER_ID + " = " + customerId + " and  visit_date like '" + params[0] + "%'";
                sdbw = db.getWritableDatabase();

                cursor = sdbw.rawQuery(selectQuery, null);

                System.out.println("selectQuery  " + selectQuery + "cursor count" + cursor.getCount());

                if (cursor != null && cursor.moveToFirst()) {
                    do {
                        datefromrecords = cursor.getString(3).substring(0, 10);
                        String routepath = cursor.getString(2);

                        try {
                            if (sdf.parse(datefromcalander).equals(sdf.parse(datefromrecords))) {
                                noData = false;

                                String str_checkIn = cursor.getString(4);
                                String str_checkOut = cursor.getString(5);
                                Log.e("XXXXXXX", "str_checkIn " + str_checkIn + " str_checkOut " + str_checkOut);
                                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

                                Date checkIn = null;
                                Date checkOut = null;
                                checkIn = format.parse(str_checkIn);
                                checkInDate = new DateTime(checkIn);
                                if (str_checkOut != null && !str_checkOut.equalsIgnoreCase("null") && !str_checkOut.equalsIgnoreCase("")) {
                                    checkOut = format.parse(str_checkOut);
                                    DateTime checkOutDate = new DateTime(checkOut);
                                    timeTaken = Hours.hoursBetween(checkInDate, checkOutDate).getHours() % 24 + " Hr " + Minutes.minutesBetween(checkInDate, checkOutDate).getMinutes() % 60 + " min ";

                                } else {
                                    DateTime checkOutDatTime = new DateTime(new Date(System.currentTimeMillis()));
                                    timeTaken = Hours.hoursBetween(checkInDate, checkOutDatTime).getHours() % 24 + " Hr " + Minutes.minutesBetween(checkInDate, checkOutDatTime).getMinutes() % 60 + " min ";
                                }
                                Log.e(" ++Dates++ ", datefromcalander + " & " + datefromrecords + "timeTaken;" + timeTaken);

                                if (cursor.getString(6) == null || cursor.getString(6).length() <= 4) {

                                    ArrayList<String> latlng = new ArrayList(Arrays.asList(routepath.split(":")));
                                    Common.Log.i("Post Route "+latlng.size());
                                    markerPoints.clear();
                                    String[] checkInLatLong = cursor.getString(0).split(",");
                                    point = new LatLng(Double.parseDouble(checkInLatLong[0]), Double.parseDouble(checkInLatLong[1]));
                                    markerPoints.add(new MarkerData(point, "", ""));

                                    if (latlng.size() > 0) {
                                        for (int i = 0; i < latlng.size(); i++) {
                                            String latlngvalues = latlng.get(i);
                                            String latlngList[] = latlngvalues.split(",");
                                            double lat = Double.parseDouble(latlngList[0]);
                                            double lng = Double.parseDouble(latlngList[1]);
                                            point = new LatLng(lat, lng);

                                            markerPoints.add(new MarkerData(point, "", ""));
                                        }
                                    }
                                    Log.v("latlng", "" + latlng.size());
                                    Log.v("markerpoints", "" + markerPoints.size());
                                } else {
                                    decodedPolyLine = Common.decodePolyLine(cursor.getString(6));
                                    distanceValueFinal = cursor.getString(8);

                                }
                                meterReadingCheckInTxt = cursor.getString(9);
                                meterReadingCheckInImg = cursor.getString(10);
                                meterReadingCheckoutTxt = cursor.getString(11);
                                meterReadingCheckoutImg = cursor.getString(12);
                                if (Common.isNumeric(meterReadingCheckInTxt)) {
                                    canShowButtomView = true;
                                }
                            } else {
                                noData = true;
                                resetData();
                                runOnUiThread(new Runnable() {
                                    @Override
                                    public void run() {
                                        Common.showAlertDialogNOData(PostRouteMapActivityCopy.this, "Warning!", "Check-in data not found.\n Please select another date.");
                                    }
                                });

                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    } while (cursor.moveToNext());

                } else {
                    noData = true;
                    resetData();
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Common.showAlertDialogNOData(PostRouteMapActivityCopy.this, "Warning!", "Check-in data not found.\n Please select another date.");
                        }
                    });

                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                Common.closeCursor(cursor);
                Common.closeDataBase(sdbw);
            }

            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
           /* if (progressDialog.isShowing()) {
                progressDialog.dismiss();
                progressDialog.hide();
            }*/
            addDataOnButtomView(canShowButtomView);
            if (map == null || noData) {
                return;
            }

            map.clear();
            final ArrayList<LatLng> points = new ArrayList<>();
            PolylineOptions lineOptions = new PolylineOptions();
            float distanceValue = 0;
            if (decodedPolyLine == null || decodedPolyLine.size() < 1) {
                if (markerPoints != null && markerPoints.size() > 0) {
                    try {
                        MarkerOptions options = new MarkerOptions();
                        options.position(markerPoints.get(0).latLng);
                        options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                        map.addMarker(options);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                }
                if (markerPoints != null && markerPoints.size() > 1) {
                    try {
                        MarkerOptions options = new MarkerOptions();
                        options.position(markerPoints.get(markerPoints.size() - 1).latLng);
                        options.icon(BitmapDescriptorFactory.fromResource(R.drawable.car_1));
                        carMarker = map.addMarker(options);
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
                                location1.setLatitude(markerPoints.get(i - 1).latLng.latitude);
                                location1.setLongitude(markerPoints.get(i - 1).latLng.longitude);

                                Location location2 = new Location("");
                                location2.setLatitude(markerPoints.get(i).latLng.latitude);
                                location2.setLongitude(markerPoints.get(i).latLng.longitude);
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
                    map.addPolyline(lineOptions);
                    //callDirectionAPI(points);
                    distanceValue = round(distanceValue / 1000, 2);
                    distanceValueUpdated = distanceValue;
                    if (timeTaken == null) {
                        timeTaken = "0 Hr";
                    }
                    double travelAllowance = distanceValue * Double.parseDouble(Common.nullChecker(db.getTravelAllowance(customerId)));
                    // Common.Log.i("Travel Allowance: " + db.getTravelAllowance(customerId));
                    tv_distance.setText(" " + distanceValue + "  KM   " + timeTaken + " INR " + Math.round(travelAllowance));
                    Log.d("points", "P: " + points.toString());
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            zoomRoute(map, points);
                        }
                    }, 1500);


                }
            } else {

                map.addPolyline(new PolylineOptions().addAll(decodedPolyLine).color(Color.RED).width(5));
                distanceValue = round(Float.parseFloat(Common.nullChecker(distanceValueFinal)) / 1000, 2);
                double travelAllowance = distanceValue * Double.parseDouble(Common.nullChecker(db.getTravelAllowance(customerId)));
                // Common.Log.i("Travel Allowance: " + db.getTravelAllowance(customerId));
                tv_distance.setText(" " + distanceValue + "  KM   " + timeTaken + " INR " + Math.round(travelAllowance));

                if (decodedPolyLine != null && decodedPolyLine.size() > 0) {

                    MarkerOptions options = new MarkerOptions();
                    options.position(decodedPolyLine.get(0));
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    map.addMarker(options);

                }
                if (decodedPolyLine != null && decodedPolyLine.size() > 1) {

                    MarkerOptions options = new MarkerOptions();
                    options.position(decodedPolyLine.get(decodedPolyLine.size() - 1));
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    map.addMarker(options);
                    new Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            zoomRoute(map, decodedPolyLine);
                        }
                    }, 1500);


                }

            }

        }
    }

    private void addDataOnButtomView(boolean show) {
        if (show) {
            checkout_txt.setText(" " + meterReadingCheckoutTxt);
            checkin_txt.setText(" " + meterReadingCheckInTxt);
            relativeLayoutBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    try {

                        relativeLayoutBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }, 5000);
        } else {
            checkout_txt.setText(" ");
            checkin_txt.setText(" ");
            relativeLayoutBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
        }

    }

    private class Async_getall_geotracking extends AsyncTask<Boolean, Void, String> {
        // ProgressDialog pDialog;
        long nmDays = 30;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

           /* pDialog = new ProgressDialog(PostRouteMapActivityCopy.this);
            pDialog.setMessage("Please wait ");
            pDialog.setCancelable(false);
            pDialog.show();*/
        }

        protected String doInBackground(Boolean... params) {

            try {
                OkHttpClient client = new OkHttpClient();
                if (!params[0]) {
                    String date = Common.getLastRecordFromGeoTracking(db);
                    if (date != null) {
                        nmDays = Common.numberOfDays(date) + 5;
                    } else {
                        nmDays = 60;
                    }
                } else {
                    nmDays = 1;
                }
                Log.d("param", params[0] + " days " + nmDays);
                Request request = new Request.Builder()
                        .url(Common.getCompleteURL(role, Constants.URL_MASTER_GEO_TRACKING, userId, team) + "&days=" + nmDays)
                        .get()
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();


                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {

                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        jsonData = response.body().string();
                        //System.out.println("!!!!!!!1 GEO_TRACKING" + jsonData);
                        if (jsonData != null) {
                            try {
                                sdbw = db.getWritableDatabase();
                                JSONArray companyarray = new JSONArray(jsonData);

                                for (int n = 0; n < companyarray.length(); n++) {
                                    JSONObject objinfo = companyarray.getJSONObject(n);

                                    String tracking_id = objinfo.getString("tracking_id");
                                    String visit_type = objinfo.getString("visit_type");
                                    String user_id = objinfo.getString("user_id");
                                    String check_in_lat_lon = objinfo.getString("check_in_lat_lon");
                                    String check_out_lat_lon = objinfo.getString("check_out_lat_lon");
                                    String route_path_lat_lon = objinfo.getString("route_path_lat_lon");
                                    String distance = objinfo.getString("distance");
                                    String visit_date = objinfo.getString("visit_date");
                                    String check_in_time = objinfo.getString("check_in_time");
                                    String check_out_time = objinfo.getString("check_out_time");
                                    String status = objinfo.getString("status");
                                    String created_datetime = objinfo.getString("created_datetime");
                                    String updated_datetime = objinfo.getString("updated_datetime");
                                    String polyline = objinfo.getString("polyline");
                                    String meter_reading_checkin_image = objinfo.getString("meter_reading_checkin_image");
                                    String meter_reading_checkin_text = objinfo.getString("meter_reading_checkin_text");
                                    String meter_reading_checkout_image = objinfo.getString("meter_reading_checkout_image");
                                    String meter_reading_checkout_text = objinfo.getString("meter_reading_checkout_text");
                                    String vehicle_type = objinfo.getString("vehicle_type");
                                    String personal_uses_km = objinfo.getString("personal_uses_km");
                                    String checkin_comment = objinfo.getString("checkin_comment");
                                    String pause = objinfo.getString("pause");
                                    String resume = objinfo.getString("resume");


                                    String selectQuery = "SELECT * FROM " + TABLE_GEO_TRACKING + " WHERE " + KEY_TABLE_GEO_TRACKING_FFMID + " = '" + tracking_id + "'";

                                    sdbw = db.getWritableDbIfClosed(sdbw);
                                    Cursor cc = sdbw.rawQuery(selectQuery, null);
                                    if (cc != null)
                                        cc.moveToFirst();
                                    // looping through all rows and adding to list
                                    if (cc.getCount() == 0) {
                                        //doesn't exists therefore insert record.
                                        db.addGeotracking(new Geo_Tracking_POJO(tracking_id, visit_type, user_id, check_in_lat_lon, check_out_lat_lon, route_path_lat_lon, distance, visit_date, check_in_time, check_out_time, status, tracking_id, created_datetime, updated_datetime, polyline,
                                                meter_reading_checkin_image,
                                                meter_reading_checkin_text,
                                                meter_reading_checkout_image,
                                                meter_reading_checkout_text,
                                                vehicle_type,
                                                personal_uses_km, checkin_comment, pause, resume));
                                    } else if (cc.getCount() != 0 && role != Constants.Roles.ROLE_7 && !Common.getUserIdFromSP(PostRouteMapActivityCopy.this).equalsIgnoreCase(user_id)) {
                                        String updatequerys = "UPDATE " + TABLE_GEO_TRACKING + " SET " + KEY_TABLE_GEO_TRACKING_POLYLINE + " ='" + polyline + "'," + KEY_TABLE_GEO_TRACKING_ROUTE_PATH_LAT_LONG + " = '" + route_path_lat_lon + "'," + KEY_TABLE_GEO_TRACKING_DISTANCE + " = '" + distance + "' WHERE " + KEY_TABLE_GEO_TRACKING_FFMID + " = " + tracking_id;
                                        sdbw.execSQL(updatequerys);
                                    }
                                    Common.closeCursor(cc);

                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                Common.closeDataBase(sdbw);
                            }

                        } else {
                            Log.e("ServiceHandler", "Couldn't get any data from the url");
                        }

                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }
            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (nmDays > 1)
                new Async_getGeotrackingGenratedByAdmin().execute();

        }
    }


    private class Async_getGeotrackingGenratedByAdmin extends AsyncTask<Void, Void, String> {
        ProgressDialog pDialog;
        String jsonData;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

         /*   pDialog = new ProgressDialog(PostRouteMapActivityCopy.this);
            pDialog.setMessage("Please wait ");
            pDialog.setCancelable(false);
            pDialog.show();*/
        }

        protected String doInBackground(Void... params) {

            try {
                OkHttpClient client = new OkHttpClient();
                Response responses = null;
                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

                Request request = new Request.Builder()
                        .url(Constants.URL_GEO_TRACKING_ADMIN + team)

                        .get()
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();


                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {

                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        jsonData = response.body().string();
                        System.out.println("!!!!!!!1 GEO_TRACKING BY ADMIN " + Constants.URL_GEO_TRACKING_ADMIN + team + "\n" + jsonData);

                        if (jsonData != null && jsonData != "") {
                            try {
                                sdbw = db.getWritableDatabase();
                                if (jsonData.startsWith("{")) {
                                    Common.Log.i("Start with {");
                                    return;
                                }
                                JSONArray companyarray = new JSONArray(jsonData);

                                for (int n = 0; n < companyarray.length(); n++) {
                                    JSONObject objinfo = companyarray.getJSONObject(n);

                                    String tracking_id = objinfo.getString("tracking_id");
                                    String visit_type = objinfo.getString("visit_type");
                                    String user_id = objinfo.getString("user_id");
                                    String check_in_lat_lon = objinfo.getString("check_in_lat_lon");
                                    String check_out_lat_lon = objinfo.getString("check_out_lat_lon");
                                    String route_path_lat_lon = objinfo.getString("route_path_lat_lon");
                                    String distance = objinfo.getString("distance");
                                    String visit_date = objinfo.getString("visit_date");
                                    String check_in_time = objinfo.getString("check_in_time");
                                    String check_out_time = objinfo.getString("check_out_time");
                                    String status = objinfo.getString("status");
                                    String created_datetime = objinfo.getString("created_datetime");
                                    String updated_datetime = objinfo.getString("updated_datetime");
                                    String polyline = objinfo.getString("polyline");
                                    String meter_reading_checkin_image = objinfo.getString("meter_reading_checkin_image");
                                    String meter_reading_checkin_text = objinfo.getString("meter_reading_checkin_text");
                                    String meter_reading_checkout_image = objinfo.getString("meter_reading_checkout_image");
                                    String meter_reading_checkout_text = objinfo.getString("meter_reading_checkout_text");
                                    String vehicle_type = objinfo.getString("vehicle_type");
                                    String personal_uses_km = objinfo.getString("personal_uses_km");
                                    String checkin_comment = objinfo.getString("checkin_comment");
                                    String pause = objinfo.getString("pause");
                                    String resume = objinfo.getString("resume");

                                    String selectQuery = "SELECT * FROM " + TABLE_GEO_TRACKING + " WHERE " + KEY_TABLE_GEO_TRACKING_FFMID + " = '" + tracking_id + "'";

                                    sdbw = db.getWritableDbIfClosed(sdbw);
                                    Cursor cc = sdbw.rawQuery(selectQuery, null);
                                    if (cc != null)
                                        cc.moveToFirst();
                                    if (cc.getCount() == 0) {
                                        //doesn't exists therefore insert record.
                                        db.addGeotracking(new Geo_Tracking_POJO(tracking_id, visit_type, user_id, check_in_lat_lon, check_out_lat_lon, route_path_lat_lon, distance, visit_date, check_in_time, check_out_time, status, tracking_id, created_datetime, updated_datetime, polyline,
                                                meter_reading_checkin_image,
                                                meter_reading_checkin_text,
                                                meter_reading_checkout_image,
                                                meter_reading_checkout_text,
                                                vehicle_type,
                                                personal_uses_km, checkin_comment, pause, resume));
                                    } else {
                                        String updatequerys = "UPDATE " + TABLE_GEO_TRACKING + " SET " + KEY_TABLE_GEO_TRACKING_POLYLINE + " ='" + polyline + "'," + KEY_TABLE_GEO_TRACKING_ROUTE_PATH_LAT_LONG + " = '" + route_path_lat_lon + "'," + KEY_TABLE_GEO_TRACKING_DISTANCE + " = '" + distance + "' WHERE " + KEY_TABLE_GEO_TRACKING_FFMID + " = " + tracking_id;
                                        sdbw.execSQL(updatequerys);
                                    }
                                    Common.closeCursor(cc);
                                }


                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                Common.closeDataBase(sdbw);
                            }

                        } else {
                            Log.e("ServiceHandler", "Couldn't get any data from the url");
                        }

                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }
            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

        }
    }

    @Override
    public void onResume() {
        super.onResume();

        mGoogleApiClient.connect();
       /* if (sharedpreferences.getString("Geo_tracing", "") != null && sharedpreferences.getString("Geo_tracing", "").equalsIgnoreCase("true")) {

        } else {*/

        if (Common.haveInternet(this)) {
            new Async_getall_geotracking().execute(false);
        }
        //  }

        if (datefromcalander != null && datefromcalander.length() > 5 && customerId != null && !customerId.equalsIgnoreCase("")) {
            new Async_getalloffline().execute(datefromcalander);
        }
        myReceiver = new MyReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver,
                new IntentFilter(ACTION_BROADCAST_LOCATION_MAP));


    }

    private void callapi() {
        organisations = new ArrayList<SelectedCities>();
        adaptercity = new ArrayList<String>();
        organisations.clear();
        SelectedCities citiez = new SelectedCities();
        citiez.setCityId("0");
        citiez.setCityName("Select User");
        // System.out.println("city id is :" + cityId + "city name is :" + cityName);
        organisations.add(citiez);
        adaptercity.add("Select User");
        Cursor cursor = null;
        try {

            String selectQuery = "SELECT " + KEY_TABLE_USERS_FIRST_NAME + "," + KEY_TABLE_USERS_MASTER_ID + " FROM " + TABLE_USERS + "  WHERE  user_id in (" + team + ")";
            sdbw = db.getWritableDatabase();
            System.out.println("selectQuery " + selectQuery);
            cursor = sdbw.rawQuery(selectQuery, null);
            //System.out.println("cursor count "+cursor.getCount());
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    Users users = new Users();

                    users.setUserMasterID(cursor.getString(1));
                    users.setUser_first_name(cursor.getString(0));

                    SelectedCities cities2 = new SelectedCities();
                    cities2.setCityId(cursor.getString(1));
                    cities2.setCityName(cursor.getString(0));
                    // System.out.println("city id is :" + cityId + "city name is :" + cityName);
                    organisations.add(cities2);
                    adaptercity.add(cursor.getString(0));

                } while (cursor.moveToNext());
            }

            // do some stuff....
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Common.closeCursor(cursor);
            Common.closeDataBase(sdbw);
        }

        spin_company.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, R.id.customSpinnerItemTextView, adaptercity));

        spin_company.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                customerId = organisations.get(position).getCityId();
                userName = organisations.get(position).getCityName();
                Common.saveDataInSP(getApplicationContext(), Constants.CUSTOMER_ID_GEO, customerId);
                if (spin_company.getSelectedItemPosition() == 0) {
                    resetData();
                    return;
                }
                if (datefromcalander != null && datefromcalander.length() > 5 && !customerId.equalsIgnoreCase("0")) {
                    new Async_getalloffline().execute(datefromcalander);
                }
                /*if (datePickerDate!=null && datePickerDate.equals(new Date()) && customerId!=Common.getUserIdFromSP(PostRouteMapActivityCopy.this) && role!=Constants.Roles.ROLE_7){
                    registerReceiver();
                }else {
                    unRegisterReceiver();
                }*/
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        if (getIntent().getBooleanExtra("map_from_planner", false)) {
            for (int i = 0; i < organisations.size(); i++) {
                if (organisations.get(i).getCityId().equalsIgnoreCase(getIntent().getStringExtra("emp_id"))) {
                    spin_company.setSelection(i, true);
                    break;
                }
            }

        }
    }


    private void rotateMarker(final Marker marker, final float toRotation) {
        if (!isMarkerRotating) {
            final Handler handler = new Handler();
            final long start = SystemClock.uptimeMillis();
            final float startRotation = marker.getRotation();
            final long duration = 1000;

            final Interpolator interpolator = new LinearInterpolator();

            handler.post(new Runnable() {
                @Override
                public void run() {
                    isMarkerRotating = true;

                    long elapsed = SystemClock.uptimeMillis() - start;
                    float t = interpolator.getInterpolation((float) elapsed / duration);

                    float rot = t * toRotation + (1 - t) * startRotation;

                    marker.setRotation(-rot > 180 ? rot / 2 : rot);
                    if (t < 1.0) {
                        // Post again 16ms later.
                        handler.postDelayed(this, 16);
                    } else {
                        isMarkerRotating = false;
                    }
                }
            });
        }
    }


    private void animateMarker(GoogleMap googleMap, final Marker marker, final LatLng toPosition,
                               final boolean hideMarker) {
        final Handler handler = new Handler();
        final long start = SystemClock.uptimeMillis();
        Projection proj = googleMap.getProjection();
        Point startPoint = proj.toScreenLocation(marker.getPosition());
        final LatLng startLatLng = proj.fromScreenLocation(startPoint);
        final long duration = 6000;
        final Interpolator interpolator = new LinearInterpolator();
        handler.post(new Runnable() {
            @Override
            public void run() {
                long elapsed = SystemClock.uptimeMillis() - start;
                float t = interpolator.getInterpolation((float) elapsed
                        / duration);
                double lng = t * toPosition.longitude + (1 - t)
                        * startLatLng.longitude;
                double lat = t * toPosition.latitude + (1 - t)
                        * startLatLng.latitude;
                marker.setPosition(new LatLng(lat, lng));
                if (t < 1.0) {
                    // Post again 16ms later.
                    handler.postDelayed(this, 16);
                } else {
                    if (hideMarker) {
                        marker.setVisible(false);
                    } else {
                        marker.setVisible(true);
                    }
                }
            }
        });
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

                                if (myCurrentLocation != null) {
                                    handleNewLocation(myCurrentLocation, 15);
                                }
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
                       /* try {
                            Thread.sleep(500);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }*/
                        if (myCurrentLocation != null) {
                            handleNewLocation(myCurrentLocation, 15);
                        }
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

    private void resetData() {
        if (map != null) {
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    map.clear();
                    tv_distance.setText(" 0 KM  0 Hr 0 min  INR 0");
                }
            });

        }
    }

    public void zoomRoute(GoogleMap googleMap, List<LatLng> lstLatLngRoute) {

        if (googleMap == null || lstLatLngRoute == null || lstLatLngRoute.isEmpty() || lstLatLngRoute.size() < 2)
            return;

        LatLngBounds.Builder boundsBuilder = new LatLngBounds.Builder();
        for (LatLng latLngPoint : lstLatLngRoute)
            boundsBuilder.include(latLngPoint);

        int routePadding = 50;
        LatLngBounds latLngBounds = boundsBuilder.build();

        googleMap.moveCamera(CameraUpdateFactory.newLatLngBounds(latLngBounds, routePadding));
    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Location location = intent.getParcelableExtra(EXTRA_LOCATION);
            //   int role1 = intent.getParcelableExtra(ROLE);
            Log.d("MyReceiver", "MyReceiver Map");
            if (location != null) {
                myCurrentLocation = location;

                Toast.makeText(context, Utils.getLocationText(location),
                        Toast.LENGTH_SHORT).show();
                if (carMarker != null && map != null) {

                    moveMarker(carMarker, carMarker.getPosition(), new LatLng(location.getLatitude(), location.getLongitude()));
                    handleNewLocation(location, map.getCameraPosition().zoom);
                    Location location1 = new Location("");
                    location1.setLatitude(carMarker.getPosition().latitude);
                    location1.setLongitude(carMarker.getPosition().longitude);

                    float distance = location1.distanceTo(location);
                    float distance1 = round(distance / 1000, 2);
                    distanceValueUpdated = distanceValueUpdated + distance1;
                    double travelAllowance = distanceValueUpdated * Double.parseDouble(Common.nullChecker(db.getTravelAllowance(customerId)));
                    DateTime checkOutDatTime = new DateTime(new Date(System.currentTimeMillis()));
                    if (checkInDate != null)
                        timeTaken = Hours.hoursBetween(checkInDate, checkOutDatTime).getHours() % 24 + " Hr " + Minutes.minutesBetween(checkInDate, checkOutDatTime).getMinutes() % 60 + " min ";
                    tv_distance.setText(" " + distanceValueUpdated + "  KM   " + timeTaken + " INR " + Math.round(travelAllowance));

                }
            }
        }
    }

    private void moveMarker(Marker marker, LatLng previousLatLng, LatLng currentLatLng) {

        Location previousLocation = new Location("");
        previousLocation.setLatitude(previousLatLng.latitude);
        previousLocation.setLongitude(previousLatLng.longitude);

        Location currentLocation = new Location("");
        currentLocation.setLatitude(currentLatLng.latitude);
        currentLocation.setLongitude(currentLatLng.longitude);

        float rotationValue = previousLocation.bearingTo(currentLocation);
        if (previousLocation.distanceTo(currentLocation) > 10) {
            rotateMarker(marker, rotationValue);

            animateMarker(map, marker, currentLatLng, false);

            drawPolylineWithArrowEndcap(PostRouteMapActivityCopy.this, map, previousLatLng, currentLatLng);
        }


    }

    class MyTimerTask extends TimerTask {
        int count = 0;

        @Override
        public void run() {
            count++;
            if (count < 12) {
               /* final PowerManager pm = (PowerManager) getSystemService(Context.POWER_SERVICE);
                final PowerManager.WakeLock wl_cpu = pm.newWakeLock(PowerManager.PARTIAL_WAKE_LOCK, "MyCpuLock");*/

                Calendar calendar = Calendar.getInstance();
                SimpleDateFormat simpleDateFormat =
                        new SimpleDateFormat("dd:MMMM:yyyy HH:mm:ss a");
                final String strDate = simpleDateFormat.format(calendar.getTime());

                if (Common.haveInternet(getApplicationContext())) {
                    new Async_getall_geotracking().execute(true);
                }

                Log.d("timer", strDate);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //   locationF=null;
                       /* if (timer != null && locationF != null) {
                            //  if (locationF.getAccuracy() != 0.0 && locationF.getAccuracy() < 100) {
                            timer.cancel();
                            timer = null;
                            if (isSignedIn) {
                                CheckoutAlarmReceiver.cancelAlarm(PlanerOneActivity.this);
                                checkOutFunction();


                            } else {
                                CheckoutAlarmReceiver.setAlarm(false, PlanerOneActivity.this);
                                checkAndProcess();
                            }
                            //   }
                        }*/
                    }
                });


            } else {
/*
                if (timer != null) {
                    Common.dismissProgressDialog(progressDialogInOut);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            Toast.makeText(PlanerOneActivity.this, "Unable to deduct location+\nPlease restart your GPS", Toast.LENGTH_SHORT).show();

                        }
                    });
                    timer.cancel();
                    timer = null;
                }
*/

            }

        }
    }

    public Polyline drawPolylineWithArrowEndcap(Context context,
                                                GoogleMap googleMap,
                                                LatLng fromLatLng,
                                                LatLng toLatLng) {

        int arrowColor = Color.RED; // change this if you want another color (Color.BLUE)
        int lineColor = Color.RED;

        //  BitmapDescriptor endCapIcon = getEndCapIcon(context,arrowColor);

        // have googleMap create the line with the arrow endcap
        // NOTE:  the API will rotate the arrow image in the direction of the line
        Polyline polyline = googleMap.addPolyline(new PolylineOptions()
                .geodesic(true)
                .color(lineColor)
                .width(8)
                .startCap(new RoundCap())
                // .endCap(new CustomCap(endCapIcon,8))
                .jointType(JointType.ROUND)
                .add(fromLatLng, toLatLng));

        return polyline;
    }

    public BitmapDescriptor getEndCapIcon(Context context, int color) {

        // mipmap icon - white arrow, pointing up, with point at center of image
        // you will want to create:  mdpi=24x24, hdpi=36x36, xhdpi=48x48, xxhdpi=72x72, xxxhdpi=96x96
        Drawable drawable = ContextCompat.getDrawable(context, R.drawable.car_1);

        // set the bounds to the whole image (may not be necessary ...)
        drawable.setBounds(0, 0, drawable.getIntrinsicWidth(), drawable.getIntrinsicHeight());

        // overlay (multiply) your color over the white icon
        drawable.setColorFilter(color, PorterDuff.Mode.MULTIPLY);

        // create a bitmap from the drawable
        android.graphics.Bitmap bitmap = android.graphics.Bitmap.createBitmap(drawable.getIntrinsicWidth(),
                drawable.getIntrinsicHeight(), Bitmap.Config.ARGB_8888);

        // render the bitmap on a blank canvas
        Canvas canvas = new Canvas(bitmap);
        drawable.draw(canvas);

        // create a BitmapDescriptor from the new bitmap
        return BitmapDescriptorFactory.fromBitmap(bitmap);
    }

    public static void broadcastLocation(Context context, Location location) {
        // Notify anyone listening for broadcasts about the new location.
        Intent intent = new Intent(ACTION_BROADCAST_LOCATION_MAP);
        intent.putExtra(EXTRA_LOCATION, location);
        LocalBroadcastManager.getInstance(context).sendBroadcast(intent);

    }


    private class Async_UpdateUI extends AsyncTask<String, Void, String> {
        //  ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* progressDialog = new ProgressDialog(PostRouteMapActivityCopy.this);
            progressDialog.setMessage("Please wait ...");
            progressDialog.setCancelable(false);
            progressDialog.setCanceledOnTouchOutside(false);
            progressDialog.show();
            markerPoints.clear();
            decodedPolyLine.clear();
            distanceValueFinal="0";
            carMarker=null;*/
        }

        protected String doInBackground(String... params) {

            Cursor cursor = null;
            try {

                String selectQuery = "SELECT DISTINCT "
                        + KEY_TABLE_GEO_TRACKING_CHECK_IN_LAT_LONG + ","
                        + KEY_TABLE_GEO_TRACKING_CHECK_OUT_LAT_LONG + ","
                        + KEY_TABLE_GEO_TRACKING_ROUTE_PATH_LAT_LONG + ","
                        + KEY_TABLE_GEO_TRACKING_VISIT_DATE + ","
                        + KEY_TABLE_GEO_TRACKING_CHECK_IN_TIME + ","
                        + KEY_TABLE_GEO_TRACKING_CHECK_OUT_TIME + ","
                        + KEY_TABLE_GEO_TRACKING_POLYLINE + ","
                        + KEY_TABLE_GEO_TRACKING_VERSION + ","
                        + KEY_TABLE_GEO_TRACKING_DISTANCE
                        + " FROM " + TABLE_GEO_TRACKING + " where " + KEY_TABLE_GEO_TRACKING_USER_ID + " = " + params[0] + " and  visit_date like '" + params[1] + "%'";
                sdbw = db.getWritableDatabase();

                cursor = sdbw.rawQuery(selectQuery, null);

                System.out.println("selectQuery  " + selectQuery + "cursor count" + cursor.getCount());

                if (cursor.moveToFirst()) {
                    do {
                        datefromrecords = cursor.getString(3).substring(0, 10);
                        String routepath = cursor.getString(2);

                        try {
                            if (sdf.parse(datefromcalander).equals(sdf.parse(datefromrecords))) {
                                noData = false;

                                String str_checkIn = cursor.getString(4);
                                String str_checkOut = cursor.getString(5);
                                Log.e("XXXXXXX", "str_checkIn " + str_checkIn + " str_checkOut " + str_checkOut);
                                SimpleDateFormat format = new SimpleDateFormat("HH:mm:ss");

                                Date checkIn = null;
                                Date checkOut = null;
                                checkIn = format.parse(str_checkIn);
                                checkInDate = new DateTime(checkIn);
                                if (str_checkOut != null && !str_checkOut.equalsIgnoreCase("null") && !str_checkOut.equalsIgnoreCase("")) {
                                    checkOut = format.parse(str_checkOut);
                                    DateTime checkOutDate = new DateTime(checkOut);
                                    timeTaken = Hours.hoursBetween(checkInDate, checkOutDate).getHours() % 24 + " Hr " + Minutes.minutesBetween(checkInDate, checkOutDate).getMinutes() % 60 + " min ";

                                } else {
                                    DateTime checkOutDatTime = new DateTime(new Date(System.currentTimeMillis()));
                                    timeTaken = Hours.hoursBetween(checkInDate, checkOutDatTime).getHours() % 24 + " Hr " + Minutes.minutesBetween(checkInDate, checkOutDatTime).getMinutes() % 60 + " min ";
                                }
                                Log.e(" ++Dates++ ", datefromcalander + " & " + datefromrecords + "timeTaken;" + timeTaken);

                                if (cursor.getString(6) == null || cursor.getString(6).length() <= 4) {

                                    ArrayList<String> latlng = new ArrayList(Arrays.asList(routepath.split(":")));
                                    markerPoints.clear();
                                    String[] checkInLatLong = cursor.getString(0).split(",");
                                    point = new LatLng(Double.parseDouble(checkInLatLong[0]), Double.parseDouble(checkInLatLong[1]));
                                    markerPoints.add(new MarkerData(point, "", ""));

                                    if (latlng.size() > 0) {
                                        for (int i = 0; i < latlng.size(); i++) {
                                            String latlngvalues = latlng.get(i);
                                            String latlngList[] = latlngvalues.split(",");
                                            double lat = Double.parseDouble(latlngList[0]);
                                            double lng = Double.parseDouble(latlngList[1]);
                                            point = new LatLng(lat, lng);

                                            markerPoints.add(new MarkerData(point, "", ""));
                                        }
                                    }
                                    Log.v("latlng", "" + latlng.size());
                                    Log.v("markerpoints", "" + markerPoints.size());
                                } else {
                                    decodedPolyLine = Common.decodePolyLine(cursor.getString(6));
                                    distanceValueFinal = cursor.getString(8);

                                }

                            } else {
                                noData = true;
                                resetData();
                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    } while (cursor.moveToNext());

                } else {
                    noData = true;
                    resetData();
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                Common.closeCursor(cursor);
                Common.closeDataBase(sdbw);
            }

            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            if (map == null || noData) {
                return;
            }
            //map.clear();
            ArrayList<LatLng> points = new ArrayList<>();
            PolylineOptions lineOptions = new PolylineOptions();
            float distanceValue = 0;
            if (decodedPolyLine == null || decodedPolyLine.size() < 1) {
            /*    if (markerPoints != null && markerPoints.size() > 0) {

                    MarkerOptions options = new MarkerOptions();
                    options.position(markerPoints.get(0).latLng);
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    map.addMarker(options);

                }
                if (markerPoints != null && markerPoints.size() > 1) {

                    MarkerOptions options = new MarkerOptions();
                    options.position(markerPoints.get(markerPoints.size() - 1).latLng);
                    options.icon(BitmapDescriptorFactory.fromResource(R.drawable.car_1));
                    carMarker = map.addMarker(options);
                }*/


                for (int i = 0; i < markerPoints.size(); i++) {


                    points.add(markerPoints.get(i).latLng);


                    if (i > 1) {
                        Location location1 = new Location("");
                        location1.setLatitude(markerPoints.get(i - 1).latLng.latitude);
                        location1.setLongitude(markerPoints.get(i - 1).latLng.longitude);

                        Location location2 = new Location("");
                        location2.setLatitude(markerPoints.get(i).latLng.latitude);
                        location2.setLongitude(markerPoints.get(i).latLng.longitude);
                        distanceValue = distanceValue + location1.distanceTo(location2);

                    }
                    Log.d("distanceValue", String.valueOf(distanceValue));

                }
                if (lineOptions != null && points != null && points.size() > 0) {
                    // Adding all the points in the route to LineOptions
                    /*lineOptions.addAll(points);
                    lineOptions.width(10);
                    lineOptions.color(Color.RED);
                    map.addPolyline(lineOptions);*/
                    //callDirectionAPI(points);
                    distanceValue = round(distanceValue / 1000, 2);
                    if (timeTaken == null) {
                        timeTaken = "0 Hr";
                    }

                    double travelAllowance = distanceValue * Double.parseDouble(Common.nullChecker(db.getTravelAllowance(customerId)));
                    // Common.Log.i("Travel Allowance: " + db.getTravelAllowance(customerId));
                    tv_distance.setText(" " + distanceValue + "  KM   " + timeTaken + " INR " + Math.round(travelAllowance));
                    //   zoomRoute(map, points);
                    Location location2 = new Location("");
                    location2.setLatitude(points.get(points.size() - 1).latitude);
                    location2.setLongitude(points.get(points.size() - 1).longitude);

                    broadcastLocation(PostRouteMapActivityCopy.this, location2);

                }
            } /*else {

                map.addPolyline(new PolylineOptions().addAll(decodedPolyLine).color(Color.RED).width(5));
                distanceValue = round(Float.parseFloat(Common.nullChecker(distanceValueFinal)) / 1000, 2);
                double travelAllowance = distanceValue * Double.parseDouble(Common.nullChecker(db.getTravelAllowance(customerId)));
                // Common.Log.i("Travel Allowance: " + db.getTravelAllowance(customerId));
                tv_distance.setText(" " + distanceValue + "  KM   " + timeTaken + " INR " + Math.round(travelAllowance));

                if (decodedPolyLine != null && decodedPolyLine.size() > 0) {

                    MarkerOptions options = new MarkerOptions();
                    options.position(decodedPolyLine.get(0));
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_GREEN));
                    map.addMarker(options);

                }
                if (decodedPolyLine != null && decodedPolyLine.size() > 1) {

                    MarkerOptions options = new MarkerOptions();
                    options.position(decodedPolyLine.get(decodedPolyLine.size() - 1));
                    options.icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_RED));
                    map.addMarker(options);
                }
                zoomRoute(map, decodedPolyLine);
            }
*/
        }
    }

    public void registerReceiver() {
        myReceiver = new MyReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver,
                new IntentFilter(ACTION_BROADCAST_LOCATION_MAP));

        if (role != Constants.Roles.ROLE_7) {
            timer = new Timer();
            MyTimerTask myTimerTask = new MyTimerTask();
            timer.schedule(myTimerTask, 5000, 15000);
        }
    }

    public void unRegisterReceiver() {
        if (myReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
            myReceiver = null;
        }
        if (timer != null) {
            timer.cancel();
            timer = null;
        }

    }

    private void turnOnScreen(Context context, long time) {

        if ((context.getSystemService(POWER_SERVICE)) != null) {
            this.screenLock = ((PowerManager) context.getSystemService(POWER_SERVICE)).newWakeLock(
                    PowerManager.SCREEN_BRIGHT_WAKE_LOCK | PowerManager.ACQUIRE_CAUSES_WAKEUP, "TAG");
            this.screenLock.acquire();


            //  screenLock.release();
        }
    }

    public void ImagepopUp(ImageView imageView, String path) {
        AlertDialog.Builder bill_1 = new AlertDialog.Builder(this);
        LayoutInflater factory = LayoutInflater.from(this);
        View newview = factory.inflate(R.layout.imageview, null, false);
        PhotoView photoView_1 = newview.findViewById(R.id.show_image);
        ImageView cancel_dialog = newview.findViewById(R.id.cancel_dialog);
        final ProgressBar progressBar = newview.findViewById(R.id.progressBar);
        // photoView_1.setImageDrawable(imageView.getDrawable());
        Picasso.with(PostRouteMapActivityCopy.this).load(path).into(photoView_1, new com.squareup.picasso.Callback() {
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
}
