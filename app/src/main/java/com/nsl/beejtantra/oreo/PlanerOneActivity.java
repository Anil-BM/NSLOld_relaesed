package com.nsl.beejtantra.oreo;


import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.app.job.JobInfo;
import android.app.job.JobScheduler;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.IntentSender;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.location.Location;
import android.location.LocationManager;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.IBinder;
import android.preference.PreferenceManager;
import android.provider.MediaStore;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v4.content.res.ResourcesCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.Toolbar;
import android.text.Html;
import android.text.InputType;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RadioButton;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.common.api.Status;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.maps.model.LatLng;
import com.leinardi.android.speeddial.SpeedDialActionItem;
import com.leinardi.android.speeddial.SpeedDialView;
import com.nsl.beejtantra.Constants;
import com.nsl.beejtantra.DatabaseHandler;
import com.nsl.beejtantra.Geo_Tracking_POJO;
import com.nsl.beejtantra.MainActivity;
import com.nsl.beejtantra.MarshMallowPermission;
import com.nsl.beejtantra.MyApplication;
import com.nsl.beejtantra.PlanerDetailActivity;
import com.nsl.beejtantra.PlanerMainActivity;
import com.nsl.beejtantra.PlanerTypeActivity;
import com.nsl.beejtantra.R;
import com.nsl.beejtantra.SelectedCities;
import com.nsl.beejtantra.TFA.Main2Activity;
import com.nsl.beejtantra.commonutils.BitmapFile;
import com.nsl.beejtantra.commonutils.CircleImageView;
import com.nsl.beejtantra.commonutils.Common;
import com.nsl.beejtantra.commonutils.NetworkChangeListenerActivity;
import com.nsl.beejtantra.directionparser.AbstractRouting;
import com.nsl.beejtantra.directionparser.Route;
import com.nsl.beejtantra.directionparser.RouteException;
import com.nsl.beejtantra.directionparser.Routing;
import com.nsl.beejtantra.directionparser.RoutingListener;
import com.nsl.beejtantra.scheduler.LocationProviderChanged;
import com.squareup.okhttp.Callback;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.MultipartBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;
import com.squareup.picasso.Picasso;
import com.whygraphics.multilineradiogroup.MultiLineRadioGroup;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_APPROVAL_STATUS;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_CHECK_IN_TIME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_COMMENTS;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_CONCERN_PERSON_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_CREATED_BY;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_CREATED_DATETIME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_EVENT_END_DATE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_EVENT_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_EVENT_PARTICIPANTS;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_EVENT_PURPOSE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_EVENT_VENUE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_FEILD_AREA;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_FFM_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_GEO_TRACKING_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_LOCATION_ADDRESS;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_LOCATION_DISTRICT;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_LOCATION_TALUKA;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_LOCATION_VILLAGE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_MOBILE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_PLAN_DATE_TIME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_PURPOSE_VISIT;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_PURPOSE_VISIT_IDS;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_STATUS;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_TYPE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_UPDATED_BY;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_UPDATE_DATETIME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_VILLAGE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_VISIT_CUSTOMER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_VISIT_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_VISIT_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_VISIT_PLAN_TYPE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_VISIT_USER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CUSTOMER_ADDRESS;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CUSTOMER_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CUSTOMER_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_GEO_TRACKING_CHECK_OUT_LAT_LONG;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_GEO_TRACKING_CHECK_OUT_TIME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_GEO_TRACKING_CREATED_DATETIME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_GEO_TRACKING_DISTANCE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_GEO_TRACKING_FFMID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_GEO_TRACKING_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_GEO_TRACKING_POLYLINE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_GEO_TRACKING_ROUTE_PATH_LAT_LONG;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_GEO_TRACKING_UPDATED_STATUS;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_GEO_TRACKING_USER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USERS_FIRST_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USERS_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USER_COMPANY_CUSTOMER_STATUS;
import static com.nsl.beejtantra.DatabaseHandler.METER_READING_CHECKOUT_IMAGE;
import static com.nsl.beejtantra.DatabaseHandler.METER_READING_CHECKOUT_TEXT;
import static com.nsl.beejtantra.DatabaseHandler.PAUSE;
import static com.nsl.beejtantra.DatabaseHandler.PERSONAL_USES_KM;
import static com.nsl.beejtantra.DatabaseHandler.RESUME;
import static com.nsl.beejtantra.DatabaseHandler.SYNC_STATUS;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_CUSTOMERS;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_EMPLOYEE_VISIT_MANAGEMENT;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_GEO_TRACKING;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_USERS;
import static com.nsl.beejtantra.commonutils.Common.isStringNull;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlanerOneActivity extends NetworkChangeListenerActivity implements SharedPreferences.OnSharedPreferenceChangeListener, RoutingListener {


    public static final String mypreference = "mypref";
    private static final int REQUEST_CHECK_CAMERA = 100;
    private static final int REQUEST_CHECK_SETTINGS = 111;
    private static final int REQUEST_MAP_CLICKED = 222;
    private static final int PLANNER_ONE_ACTIVITY = 333;
    private static float DISTANCE_FOR_DIRECTION_CALL = 700;
    private static String API_KEY = "AIzaSyAUI7iUTr4Zfrc4ZKYbjm64OWW3F5x0mB0";
    public static boolean isSignedIn = false;
    LocationManager locationManager;
    String checkinlatlong, visit_date, trackingid, check_in_time, check_out_time, status, ffmidsqlite, ffmid, str_distance;

    boolean mBound = false;
    String i;
    ProgressDialog progressDialog;
    String jsonData, userId, companyinfo, chkevm, sel_company_id, userName;
    int role;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    SharedPreferences sharedpreferences;
    TextView tv_date, tv_noresults;
    DatabaseHandler db;
    SQLiteDatabase sdbw, sdbr;
    // FloatingActionButton fab;
    DateFormat dateFormat;
    SimpleDateFormat sdf;
    Date myDate;
    RelativeLayout checkinlayout;
    DateFormat timeFormat, currenttimeFormat;
    Date mytime;
    Date checkout, currenttime;
    String datefromcalander, dateString, eventdate, datefromrecords, cname, caddress;
    boolean iScheckedInClicked = false;
    ArrayList<String> adaptercity;
    private ListView listView;
    private ItemfavitemAdapter adapter;
    private String tid, team;
    private int approval_status;
    private LinearLayout ll_select;
    private Spinner selectDistributor;
    private ArrayList<SelectedCities> organisations;
    private String emp_visit_id, sql_id, event_approval_status;
    private ImageButton btn_add;
    private String strDate;
    private boolean isNoUsers = false;
    private boolean isDirectCustomers = false;
    private LocationUpdatesService mService;
    private ProgressDialog progressDialogInOut = null;
    int snapRoadCallsSuccess = 0;
    int snapRoadCallsFailure = 0;
    int snapRoadCallsAll = 0;
    int directionServiceCallsSuccess = 0;
    int directionServiceCallsFailure = 0;
    int directionServiceCallsAll = 0;
    /**
     * Defines callbacks for service binding, passed to bindService()
     */
    // Monitors the state of the connection to the service.
    private final ServiceConnection mServiceConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            LocationUpdatesService.LocalBinder binder = (LocationUpdatesService.LocalBinder) service;
            mService = binder.getService();
            mBound = true;
            Log.d("onServiceConnected", "onServiceConnected..");
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {
            mService = null;
            mBound = false;
            Log.d("onServiceConnected", "onServiceDisconnected..");
        }
    };
    private MyReceiver myReceiver;
    private Location locationF;
    private Timer timer;
    private String ffmIDTrackingID;
    String routePath = null;
    float distance = 0;
    List<String> listForSnapRoad = new ArrayList<>();
    List<LatLng> finalLatLngList = new ArrayList<>();
    private int MAX_POINT = 95;
    private int updatePosition = 0;
    private int newListSize = 0;
    private int versionCode = 0;
    boolean isDrawPolyline = false;
    private List<Location> locationRoadSnaps = new ArrayList<>();
    private List<List<LatLng>> listRoadSnap = new ArrayList<>();
    private String onRouteFailureError;
    public static int mJobId = 11;
    private List<LatLng> polyLinrTest = new ArrayList<>();
    //  private RelativeLayout rl_resume;
    //  private RelativeLayout rl_pause;
    //  private CoordinatorLayout myCoordinatorLayoutPause;
    private LocationUpdatesService.ActionClick actionClick;
    /*  private RapidFloatingActionLayout rfaLayout;
      private RapidFloatingActionButton rfaButton;
      private RapidFloatingActionHelper rfabHelper;*/
    private String stringRoutePath;
    private float ACCURACY_SETTING = 15;

    private static final int CAMERA_REQUEST_START = 1001;
    private File outputFileKm;
    boolean bool_1 = true;
    private ContentValues values;
    private Uri imageUri;
    private Bitmap thumbnailUtils;
    private BitmapFile mBitmapFile;
    private CircleImageView starting_kms_img;
    private AlertDialog alertDialog;
    private EditText et_meter_reading;
    private EditText et_comment;
    private TextView tv_remove_image;
    private TextView submit;
    private TextView tv_hint;
    private TextView tv_comment_hint;
    private String checkInComment = null;
    private String meter_reading_checkin_text = "0";
    private double previousReading = 0;
    private boolean displayToast = true;
    private TextView tv_previous_reading;
    private boolean isPlannerDataPushed = false;
    private CloseProgressReceiver closeProgressReceiver;
    private ProgressDialog pauseProgress;
    private SpeedDialView mSpeedDialView;
    private Toast mToast;
    private ArrayList<SpeedDialActionItem> items;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plannerone);


        /*else*/
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//            if (ActivityCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
//                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
//
//            }
//        }

Log.d("checking status", String.valueOf(isSignedIn));
        MarshMallowPermission marshMallowPermission = new MarshMallowPermission(this);
        if (!marshMallowPermission.checkPermissionForCamera()) {
            marshMallowPermission.requestPermissionForCamera();
        }
        if (!marshMallowPermission.checkPermissionForExternalStorage()) {
            marshMallowPermission.requestPermissionForExternalStorage();
        }
        try {
            PackageInfo pInfo = PlanerOneActivity.this.getPackageManager().getPackageInfo(getPackageName(), 0);
            versionCode = pInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        isSignedIn=false;
        Log.d("isChecked", String.valueOf(isSignedIn));
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        userId = sharedpreferences.getString("userId", "");
        team = sharedpreferences.getString("team", "");
        role = sharedpreferences.getInt(Constants.SharedPrefrancesKey.ROLE, 0);
        chkevm = sharedpreferences.getString("evm", "");
        companyinfo = sharedpreferences.getString("companyinfo", "");
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        ll_select = (LinearLayout) findViewById(R.id.ll_select);
        i = getIntent().getStringExtra("i");
        selectDistributor = (Spinner) findViewById(R.id.spin_user);
        btn_add = (ImageButton) findViewById(R.id.btn_add);

        if (i.equalsIgnoreCase("1")) {
            datefromcalander = getIntent().getStringExtra("date");
        } else {
            datefromcalander = PlanerMainActivity.curDate;
        }


        db = new DatabaseHandler(this);
        long date = System.currentTimeMillis();
        sdf = new SimpleDateFormat(Constants.DateFormat.COMMON_DATE_FORMAT);
        dateString = sdf.format(date);
        SimpleDateFormat sdfs = new SimpleDateFormat(Constants.DateFormat.COMMON_DATE_TIME_FORMAT);
        eventdate = sdfs.format(date);
        dateFormat = new SimpleDateFormat(Constants.DateFormat.COMMON_DATE_TIME_FORMAT);
        myDate = new Date();
        timeFormat = new SimpleDateFormat("HH:mm:ss");
        currenttimeFormat = new SimpleDateFormat("h:mm:ss aa");
        mytime = new Date();
        checkout = new Date();
        listView = (ListView) findViewById(R.id.listView);
        tv_date = (TextView) findViewById(R.id.tv_date);
        tv_noresults = (TextView) findViewById(R.id.tv_noresults);
        mSpeedDialView = findViewById(R.id.speedDial);
        mSpeedDialView.getMainFab().setBackgroundTintList(getResources().getColorStateList(R.color.green));
        /*rl_resume = (RelativeLayout) findViewById(R.id.rl_resume);
        rl_pause = (RelativeLayout) findViewById(R.id.rl_pause);
        myCoordinatorLayoutPause = (CoordinatorLayout) findViewById(R.id.myCoordinatorLayout1);
*/
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_date.setText(datefromcalander);
//        Log.e("date",datefromcalander);
        iScheckedInClicked = false;

        snackBar();
        febButtonStatus();

        if (role == Constants.Roles.ROLE_7) {
            ll_select.setVisibility(View.GONE);

            new Async_getalloffline().execute();

        } else if (role != Constants.Roles.ROLE_7) {


            new Async_getRMDirectDistributor().execute();
        }

        btn_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isNoUsers) {
                    Intent one = new Intent(getApplicationContext(), PlanerTypeActivity.class);
                    one.putExtra("date", datefromcalander);
                    one.putExtra("selecteduser", userName);
                    one.putExtra("selecteduserid", userId);
                    one.putExtra("isDirectCustomers", isDirectCustomers);


                    startActivityForResult(one, PLANNER_ONE_ACTIVITY);
                } else {
                    Toast.makeText(getApplicationContext(), "You can't create plan. Please Map distributor/Mo", Toast.LENGTH_SHORT).show();
                }

            }
        });

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                // Toast.makeText(getApplicationContext(), favouriteItem.get(i).get("strcustomers"), Toast.LENGTH_SHORT).show();
                Intent customers = new Intent(getApplicationContext(), PlanerDetailActivity.class);
                customers.putExtra("date", datefromcalander);
                customers.putExtra("name", favouriteItem.get(i).get("event_name"));
                customers.putExtra("event_purpose", favouriteItem.get(i).get("event_purpose"));
                customers.putExtra("address", favouriteItem.get(i).get("address"));
                customers.putExtra("location_district", favouriteItem.get(i).get("location_district"));
                customers.putExtra("location_taluka", favouriteItem.get(i).get("location_taluka"));
                customers.putExtra("location_village", favouriteItem.get(i).get("location_village"));
                customers.putExtra("event_datetime", favouriteItem.get(i).get("event_datetime"));
                customers.putExtra("event_id", favouriteItem.get(i).get("event_id"));
                customers.putExtra("event_status", favouriteItem.get(i).get("event_status"));
                customers.putExtra("selected_user_id", userId);
                startActivity(customers);
                // Toast.makeText(getApplicationContext(),favouriteItem.get(i).get("master_id"),Toast.LENGTH_LONG).show();
            }
        });
/*
        myCoordinatorLayoutPause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("cooo","0");
                if (LocationUpdatesService.isPaused(PlanerOneActivity.this)){
                    rl_pause.setVisibility(View.VISIBLE);
                    rl_resume.setVisibility(View.GONE);
                    LocationUpdatesService.savePauseStatusSP(PlanerOneActivity.this,false);

                    Intent intentResume = new Intent();
                    intentResume.setAction("com.nsl.beejtantra.resume.location");
                    (PlanerOneActivity.this).sendBroadcast(intentResume);

                    Log.d("cooo","1");

                }else {
                    Log.d("cooo","2");
                    rl_pause.setVisibility(View.GONE);
                    rl_resume.setVisibility(View.VISIBLE);
                    LocationUpdatesService.savePauseStatusSP(PlanerOneActivity.this,true);
                    Intent intentResume = new Intent();
                    intentResume.setAction("com.nsl.beejtantra.resume.location");
                    (PlanerOneActivity.this).sendBroadcast(intentResume);
                }
            }
        });
*/
/*
        rl_resume.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rl_pause.setVisibility(View.VISIBLE);
                rl_resume.setVisibility(View.GONE);

                Intent intentResume = new Intent();
                intentResume.setAction("com.nsl.beejtantra.resume.location");
                (PlanerOneActivity.this).sendBroadcast(intentResume);

                Log.d("cooo","1");
            }
        });
        rl_pause.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("cooo","2");
                rl_pause.setVisibility(View.GONE);
                rl_resume.setVisibility(View.VISIBLE);
                Intent intentResume = new Intent();
                intentResume.setAction("com.nsl.beejtantra.pause.location");
                (PlanerOneActivity.this).sendBroadcast(intentResume);
            }
        });*/
        if (Common.haveInternet(this)) {
            new Aysc_PlanerScheduleAndPush().execute();
            new Aysc_PlanerEventAndPush().execute();
            isPlannerDataPushed = true;
        }

    }

    @Override
    protected void onInternetConnected() {

    }

    @Override
    protected void onInternetDisconnected() {

    }

    private void febButtonStatus() {
        String[] checkinStatus = db.getCheckinStatus(dateString, userId);
        Log.d("dateString", dateString + "\n datefromcalander :" + datefromcalander + "checkinStatus:" + checkinStatus);
        if (dateString == null || datefromcalander == null) {
            finish();
            return;

        }
        if (datefromcalander.equalsIgnoreCase(dateString)) {
            //  rfaLayout.setVisibility(View.VISIBLE);
            mSpeedDialView.setVisibility(View.GONE);
            if (checkinStatus == null) {
                mSpeedDialView.setVisibility(View.VISIBLE);
                rapidFloating(1, 0);

            } else if (checkinStatus != null && (checkinStatus[0] != ("") || checkinStatus[0] != ("null") || checkinStatus[0] != null) && (checkinStatus[1] == null || checkinStatus[1].equalsIgnoreCase("") || checkinStatus[1].equalsIgnoreCase("null"))) {
                isSignedIn = true;
                mSpeedDialView.setVisibility(View.VISIBLE);
                rfaButtonColor(R.color.colorAccent);
                if (LocationUpdatesService.isPaused(this)) {
                    rapidFloating(2, 2);
                } else {
                    rapidFloating(2, 1);
                }
            } else if (checkinStatus != null && (!checkinStatus[0].equalsIgnoreCase("") || !checkinStatus[0].equalsIgnoreCase("null") || !checkinStatus[0].equalsIgnoreCase(null)) && (checkinStatus[1] != null || !checkinStatus[1].equalsIgnoreCase("") || !checkinStatus[1].equalsIgnoreCase("null"))) {
                rapidFloating(1, 0);
                // rfaLayout.setVisibility(View.GONE);
                mSpeedDialView.setVisibility(View.GONE);

            }

        } else {
            rapidFloating(1, 0);
            mSpeedDialView.setVisibility(View.GONE);
        }

    }

    private void rfaButtonColor(int color) {
        //mSpeedDialView.getMainFab().setBackgroundColor(0xFFFF0000);
        mSpeedDialView.getMainFab().setBackgroundTintList(getResources().getColorStateList(color));
    }


/*
    @Override
    public void onRFACItemLabelClick(int position, RFACLabelItem item) {
        try {
            if (favouriteItem == null || favouriteItem.size() == 0) {
                Toast.makeText(PlanerOneActivity.this, "Please add plan", Toast.LENGTH_SHORT).show();
                rfabHelper.toggleContent();
                return;

            }
            //  Toast.makeText(this, "clicked label: " + position+" "+item.getLabel(), Toast.LENGTH_SHORT).show();
            if (item.getLabel().equalsIgnoreCase(Constants.CHECK_IN) || item.getLabel().equalsIgnoreCase(Constants.CHECKOUT)) {
                startKM(item.getLabel());
            } else
                confirmationDailog(item.getLabel());
            rfabHelper.toggleContent();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    public void onRFACItemIconClick(int position, RFACLabelItem item) {
        try {
            if (favouriteItem == null || favouriteItem.size() == 0) {
                Toast.makeText(PlanerOneActivity.this, "Please add plan", Toast.LENGTH_SHORT).show();
                rfabHelper.toggleContent();
                return;

            }
            // Toast.makeText(this, "clicked icon: " + position+" "+item.getLabel(), Toast.LENGTH_SHORT).show();
            if (item.getLabel().equalsIgnoreCase(Constants.CHECK_IN) || item.getLabel().equalsIgnoreCase(Constants.CHECKOUT)) {
                startKM(item.getLabel());
            } else
                confirmationDailog(item.getLabel());
            rfabHelper.toggleContent();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }*/

    private void checkAndProcess() {

        LocationUpdatesService.saveResumeSP(this, "");
        LocationUpdatesService.savePauseSP(this, "");
        double latitude = locationF.getLatitude();
        double longitude = locationF.getLongitude();
        Log.d("getLongitude", "" + latitude);
        if (latitude == 0.0 && longitude == 0.0) {
            Toast.makeText(getApplicationContext(), "Location not found..", Toast.LENGTH_SHORT).show();
            Common.dismissProgressDialog(progressDialogInOut);
            return;
        }
        if (Common.haveInternet(this) && !isPlannerDataPushed) {
            new Aysc_PlanerScheduleAndPush().execute();
            new Aysc_PlanerEventAndPush().execute();
        }

        rfaButtonColor(R.color.colorAccent);
        rapidFloating(2, 1);
        isSignedIn = true;
        check_in_time = timeFormat.format(mytime);
        //  Log.e("Check_in_time",check_in_time);

        visit_date = dateFormat.format(myDate);
//
        // check if GPS enabled
        int a = Common.haveInternet(this) ? 1 : 0;
        checkinlatlong = String.valueOf(latitude) + "," + String.valueOf(longitude) + "," + locationF.getAccuracy() + "," + a + "," + Common.currentTimeFromMS(System.currentTimeMillis()) + ",0";
        // Toast.makeText(getApplicationContext(), String.valueOf(latitude) + String.valueOf(longitude), Toast.LENGTH_SHORT).show();

        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                Common.dismissProgressDialog(progressDialogInOut);
                if (Common.haveInternet(getApplicationContext())) {

                    new Async_Checkin().execute();

                } else {
                    Toast.makeText(getApplicationContext(), Common.INTERNET_UNABLEABLE, Toast.LENGTH_SHORT).show();

                }


            }
        }, 200);

    }

    private void callapi(int i) {
        SQLiteDatabase sdbw = null;
        Cursor cursor = null;
        organisations = new ArrayList<SelectedCities>();
        adaptercity = new ArrayList<String>();
        organisations.clear();
        SelectedCities citiez = new SelectedCities();
        citiez.setCityId("0");
        citiez.setCityName("Select User");
        // System.out.println("city id is :" + cityId + "city name is :" + cityName);
        organisations.add(citiez);
        adaptercity.add("Select User");
        try {
            String selectQuery;
//            if (i == 0) {
//                String[] newTeam = team.split(",");
//                StringBuilder sb = new StringBuilder();
//                for (String a : newTeam) {
//                    if (!Common.getUserIdFromSP(getApplicationContext()).equalsIgnoreCase(a)) {
//                        if (sb.toString().length() > 0) {
//                            sb.append(",");
//                        }
//
//                        sb.append(a);
//                    }
//                }
//                String teamnew = sb.toString();
//                selectQuery = "SELECT " + KEY_TABLE_USERS_FIRST_NAME + "," + KEY_TABLE_USERS_MASTER_ID + " FROM " + TABLE_USERS + "  WHERE " + db.KEY_TABLE_USERS_STATUS + " = 1 and  user_id in (" + teamnew + ")";
//            } else {

            selectQuery = "SELECT " + KEY_TABLE_USERS_FIRST_NAME + "," + KEY_TABLE_USERS_MASTER_ID + " FROM " + TABLE_USERS + "  WHERE " + db.KEY_TABLE_USERS_STATUS + " = 1 and  user_id in (" + team + ")";

//            }
            //List<Company_division_crops> cdclist = db.getAllCompany_division_crops();

            sdbw = db.getWritableDatabase();

            cursor = sdbw.rawQuery(selectQuery, null);
            System.out.println("cursor count " + "\n selectQuery" + selectQuery);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                   /* Users users = new Users();

                    users.setUserMasterID(cursor.getString(1));
                    users.setUser_first_name(cursor.getString(0));*/

                    SelectedCities cities2 = new SelectedCities();
                    cities2.setCityId(cursor.getString(1));
                    cities2.setCityName(cursor.getString(0));
                    // System.out.println("city id is :" + cityId + "city name is :" + cityName);
                    organisations.add(cities2);
                    if (String.valueOf(cities2.getCityId()).equalsIgnoreCase(Common.getUserIdFromSP(this)))
                        adaptercity.add("SELF");
                    else
                        adaptercity.add(cursor.getString(0));

                } while (cursor.moveToNext());
            } else {
                isNoUsers = true;
            }

            // do some stuff....
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Common.closeCursor(cursor);
            Common.closeDataBase(sdbw);
        }

        selectDistributor.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, R.id.customSpinnerItemTextView, adaptercity));
        selectDistributor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                userId = organisations.get(position).getCityId();
                userName = organisations.get(position).getCityName();
                if (userId.equalsIgnoreCase("0")) {
                    favouriteItem = new ArrayList<HashMap<String, String>>();
                    adapter = new ItemfavitemAdapter(getApplicationContext(), favouriteItem);
                    Log.e("adapter ", String.valueOf(adapter.getCount()));
                    listView.setAdapter(adapter);
                } else {
                    ll_select.setVisibility(View.VISIBLE);
                    Log.e("selected user ====", userName + " Id " + userId);
                    new Async_getalloffline().execute();

                }


                if (Common.getUserIdFromSP(getApplicationContext()).equalsIgnoreCase(userId)) {
                    febButtonStatus();

                } else {

                    mSpeedDialView.setVisibility(View.GONE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    /*private void checkin() {
        try {
            if (Utility.isNetworkAvailable(PlanerOneActivity.this, Constants.isShowNetworkToast)) {  // connected to the internet

            } else {
                Toast.makeText(getApplicationContext(), "No Internet Connection", Toast.LENGTH_SHORT).show();
            }

        } catch (Exception e) {
            Log.d("Tag", e.toString());
        }
    }*/


    private void showAproveAlertToUser(String id) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to approve this plan ?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        try {
                            ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                            NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

                            if (netInfo != null) {  // connected to the internet
                                if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                                    // connected to wifi
                                    // Toast.makeText(getActivity(), netInfo.getTypeName(), Toast.LENGTH_SHORT).show();

                                    new Async_UpdateAprovalStatus().execute();
                                } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                                    // connected to the mobile provider's data plan

                                    new Async_UpdateAprovalStatus().execute();
                                }
                            } else {
                                Toast.makeText(PlanerOneActivity.this, "No Internet Connection ! \n Please Try again", Toast.LENGTH_SHORT).show();
                            }

                        } catch (Exception e) {
                            Log.d("Tag", e.toString());
                        }
                        dialog.cancel();
                    }
                });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private void showRejectAlertToUser(String id) {

        AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to reject this plan ?");
        alertDialogBuilder.setPositiveButton("Yes",
                new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int id) {

                        if (Common.haveInternet(getApplicationContext())) {  // connected to the internet

                            new Async_UpdateAprovalStatus().execute();

                        } else {
                            Toast.makeText(PlanerOneActivity.this, "No Internet Connection ! \n Please Try again", Toast.LENGTH_SHORT).show();
                        }

                        //dialog.cancel();
                    }
                });
        alertDialogBuilder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.dismiss();
            }
        });
        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_planner, menu);
        MenuItem item_checkin = menu.findItem(R.id.action_checkin);
        MenuItem item_checkout = menu.findItem(R.id.action_checkout);
        item_checkin.setVisible(false);
        item_checkout.setVisible(false);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem menu) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = menu.getItemId();


        //noinspection SimplifiableIfStatement
        if (id == R.id.action_home) {
            Intent home = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(home);
            finish();
            return true;
        }

        if (id == R.id.action_map) {
            displayGoogleLocationSettingPage(PlanerOneActivity.this, REQUEST_MAP_CLICKED);

            return true;
        }

        if (id == R.id.action_meter) {
            if (!isFuturedate(datefromcalander)) {
                Intent meter = new Intent(getApplication(), GeoMeterReadingActivity.class);
                meter.putExtra("date", datefromcalander);
                startActivity(meter);
            } else {
                Toast.makeText(PlanerOneActivity.this, "You can't select future dates", Toast.LENGTH_LONG);

            }

        }

        if (id == R.id.action_checkin) {


        }
        if (id == R.id.action_checkout) {

        }

        return super.onOptionsItemSelected(menu);
    }

    private boolean isFuturedate(String datefromcalander) {
        try {
            Date date1 = sdf.parse(datefromcalander);
            if (date1.after(new Date())) {
                return true;
            }
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return false;
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
                                fabButtonFunctionality();
                               /* fab.setEnabled(true);
                                fab.setClickable(true);*/
                                break;
                            case REQUEST_MAP_CLICKED:
                                // if (!LocationUpdatesService.isCheckedIn(activity)) {
                                Intent map = new Intent(getApplicationContext(), Main2Activity.class);
                                map.putExtra("emp_id", userId);
                                map.putExtra("map_from_planner", true);
                                startActivity(map);
                                /*} else {
                                    Toast.makeText(activity, "Please use Route map after checkout.", Toast.LENGTH_LONG).show();
                                }*/
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
                       /* fab.setEnabled(true);
                        fab.setClickable(true);*/

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
                        fabButtonFunctionality();
                        /*fab.setEnabled(true);
                        fab.setClickable(true);*/
                        Log.d("onActivityResult", "OnresultAxtivity4");
                        break;
                    case Activity.RESULT_CANCELED:
                       /* fab.setEnabled(true);
                        fab.setClickable(true);*/
                        // The user was asked to change settings, but chose not to
                        // finish();

                        break;

                    default:
                        break;
                }
                break;

            case REQUEST_MAP_CLICKED:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        Intent map = new Intent(getApplicationContext(), Main2Activity.class);
                        startActivity(map);
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

            case PLANNER_ONE_ACTIVITY:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        new Async_getalloffline().execute();
                        Log.d("onActivityResult", "PLANNER_ONE_ACTIVITY");
                        break;
                    case Activity.RESULT_CANCELED:
                        // The user was asked to change settings, but chose not to
                        // finish();

                        break;

                    default:
                        break;
                }
                break;

            case CAMERA_REQUEST_START:
                switch (resultCode) {
                    case Activity.RESULT_OK:
                        try {
                            thumbnailUtils = MediaStore.Images.Media.getBitmap(
                                    getContentResolver(), imageUri);
                            String partFilename = currentDateFormat();
                            storeCameraPhotoInSDCard(thumbnailUtils, partFilename);
                            // CompressImageQuality compressImageQuality = new CompressImageQuality(this);
                            //mBitmapFile = compressImageQuality.compressImage(thumbnailUtils, String.valueOf(outputFileKm));
                            //Log.d("file size: 2  ", "" + (int) mBitmapFile.getBitmap().getByteCount());
                            //Bitmap mBitmap = mBitmapFile.getBitmap();
                            /*if (mBitmap != null) {
                              //  new File(imageUri.getPath()).delete();
                                bool_1 = false;
                                starting_kms_img.setImageBitmap(mBitmap);
                            }*/
                            if (outputFileKm != null) {
                                Picasso.with(this).load(outputFileKm).error(R.drawable.camera_ic).into(starting_kms_img);
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                        break;
                }
                break;


        }
    }

    private String currentDateFormat() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
        String currentTimeStamp = dateFormat.format(new Date());
        return currentTimeStamp;
    }

    private void storeCameraPhotoInSDCard(Bitmap bitmap, String currentDate) {
        outputFileKm = new File(Environment.getExternalStorageDirectory(), System.currentTimeMillis() + ".jpg");
        try {
            Log.d("file size: ", "" + bitmap.getByteCount());

            FileOutputStream fileOutputStream = new FileOutputStream(outputFileKm);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 20, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            Log.d("file size: ", "" + (int) outputFileKm.length());
            if (outputFileKm.length() > 100 * 1024)
                compressBitmap(outputFileKm, 4, 50);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
            Log.d("file size: ", "" + bitmap.getByteCount()+"1");
        } catch (IOException e) {
            e.printStackTrace();
            Log.d("file size: ", "" + bitmap.getByteCount());
        } catch (OutOfMemoryError outOfMemoryError) {
            outOfMemoryError.printStackTrace();
            Log.d("file size: ", "" + bitmap.getByteCount()+"2");
        }
    }

    public void compressBitmap(File file, int sampleSize, int quality) {
        try {
            //  File fileLocation = new File(Environment.getExternalStorageDirectory(), "photo_" + "sasa12" + ".jpg");
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inSampleSize = sampleSize;
            FileInputStream inputStream = new FileInputStream(file);

            Bitmap selectedBitmap = BitmapFactory.decodeStream(inputStream, null, options);
            inputStream.close();

            FileOutputStream outputStream = new FileOutputStream(outputFileKm.getPath());
            selectedBitmap.compress(Bitmap.CompressFormat.JPEG, quality, outputStream);
            outputStream.close();
            long lengthInKb = outputFileKm.length() / 1024; //in kb
            if (lengthInKb > 300) {
                compressBitmap(file, (sampleSize * 2), (quality / 2));
            }

            selectedBitmap.recycle();
            Log.d("fllflf", selectedBitmap.getByteCount() + "\n" + outputFileKm.length());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void fabButtonFunctionality() {


        if (favouriteItem == null || favouriteItem.size() == 0) {
            Toast.makeText(PlanerOneActivity.this, "Please add plan", Toast.LENGTH_SHORT).show();
            Common.dismissProgressDialog(progressDialogInOut);
            return;

        }


        if (mService != null && locationF == null) {
            mService.requestLocationUpdates(5);
        }

        //   countDownTimer = this.createTimer();
        timer = new Timer();
        MyTimerTask myTimerTask = new MyTimerTask();
        timer.schedule(myTimerTask, 10, 5000);

    }


    private class Async_getalloffline extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* progressDialog = new ProgressDialog(PlanerOneActivity.this);
            progressDialog.setMessage("Please wait \n data loading from offline");
            progressDialog.show();*/
            favouriteItem.clear();
        }

        protected String doInBackground(Void... params) {


            try {

                String selectQuery = "SELECT DISTINCT "
                        + KEY_EMP_CONCERN_PERSON_NAME + ","
                        + KEY_EMP_VISIT_PLAN_TYPE + ","
                        + KEY_EMP_STATUS + ","
                        + KEY_EMP_PLAN_DATE_TIME + ","
                        + KEY_EMP_PURPOSE_VISIT + ","
                        + KEY_EMP_TYPE + ","
                        + KEY_EMP_GEO_TRACKING_ID + ","
                        + KEY_EMP_VISIT_USER_ID + ","
                        + KEY_EMP_VISIT_CUSTOMER_ID + ","
                        + KEY_EMP_MOBILE + ","
                        + KEY_EMP_VILLAGE + ","
                        + KEY_EMP_LOCATION_ADDRESS + ","
                        + KEY_EMP_LOCATION_DISTRICT + ","
                        + KEY_EMP_LOCATION_TALUKA + ","
                        + KEY_EMP_LOCATION_VILLAGE + ","
                        + KEY_EMP_FEILD_AREA + ","
                        + KEY_EMP_CHECK_IN_TIME + ","
                        + KEY_EMP_COMMENTS + ","
                        + KEY_EMP_APPROVAL_STATUS + ","
                        + KEY_EMP_EVENT_END_DATE + ","
                        + KEY_EMP_EVENT_PURPOSE + ","
                        + KEY_EMP_VISIT_MASTER_ID + ","
                        + KEY_EMP_EVENT_VENUE + ","
                        + KEY_EMP_EVENT_PARTICIPANTS + ","
                        + KEY_EMP_FFM_ID + ","
                        + KEY_EMP_CREATED_BY + ","
                        + KEY_EMP_UPDATED_BY + ","
                        + KEY_EMP_CREATED_DATETIME + ","
                        + KEY_EMP_UPDATE_DATETIME + ","
                        + KEY_EMP_EVENT_NAME + ","
                        + KEY_EMP_VISIT_ID
                        + " FROM " + TABLE_EMPLOYEE_VISIT_MANAGEMENT + "  where " + KEY_EMP_VISIT_USER_ID + " = " + userId;

                sdbw = db.getWritableDatabase();

                Cursor cursor = sdbw.rawQuery(selectQuery, null);
                System.out.println("selectQuery  " + selectQuery);

                if (cursor.moveToFirst()) {
                    do {
                        datefromrecords = cursor.getString(3).substring(0, 10);

                        //Toast.makeText(getApplicationContext(), datefromrecords, Toast.LENGTH_SHORT).show();

                        HashMap<String, String> map = new HashMap<String, String>();
                        try {
                            if (sdf.parse(datefromcalander).equals(sdf.parse(datefromrecords))) {
                                // If two dates are equal.

                                Log.e("++Dates++", datefromcalander + "&" + datefromrecords);
                                // System.out.println("+++++++++++" + cursor.getString(0)+ cursor.getString(1)+ cursor.getString(2)+ cursor.getString(3));
                                String planvisittype = cursor.getString(1);
                                String emptype = cursor.getString(5);
                                if (planvisittype.equalsIgnoreCase("1")) {
                                    String cid = cursor.getString(8);


                                    String selectcQuery = "SELECT  " + KEY_TABLE_CUSTOMER_NAME + "," + KEY_TABLE_CUSTOMER_ADDRESS + " FROM " + TABLE_CUSTOMERS + "  where " + KEY_TABLE_CUSTOMER_MASTER_ID + " = " + cid;
                                    // String selectQuery = "SELECT  " + "CR." + KEY_TABLE_CUSTOMER_MASTER_ID + ","+ KEY_TABLE_CUSTOMER_NAME+ ","+ KEY_TABLE_CUSTOMER_CODE + " FROM " + TABLE_USER_COMPANY_CUSTOMER+ " AS CDC JOIN " + TABLE_CUSTOMERS + " AS CR ON CDC."+KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID + " = CR."+ KEY_TABLE_CUSTOMER_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_DIVISION_USER_ID + " = " + userId + " group by(CR." + KEY_TABLE_COMPANIES_MASTER_ID + ")" ;
                                    sdbw = db.getWritableDatabase();

                                    Cursor ccursor = sdbw.rawQuery(selectcQuery, null);
                                    System.out.println("ccursor count " + ccursor.getCount());

                                    if (ccursor.moveToFirst()) {
                                        do {
                                            cname = ccursor.getString(0);
                                            caddress = ccursor.getString(1);

                                        } while (ccursor.moveToNext());
                                    } else {
                                        Log.e("no plans", "no plans");
                                    }

                                    map.put("event_name", cname);
                                    map.put("address", caddress);
                                    map.put("event_status", cursor.getString(2));
                                    map.put("location_district", cursor.getString(12));
                                    map.put("location_taluka", cursor.getString(13));
                                    map.put("location_village", cursor.getString(14));
                                    map.put("event_approval_status", cursor.getString(18));
                                    map.put("event_customer_id", cursor.getString(8));
                                    map.put("event_datetime", cursor.getString(3));
                                    map.put("event_purpose", cursor.getString(4));
                                    map.put("event_id", cursor.getString(30));
                                    map.put("emp_visit_id", cursor.getString(24));

                                } else if (emptype.equalsIgnoreCase("2")) {
                                    Log.e("Type : ", "Event");

                                    map.put("event_name", cursor.getString(29));
                                    map.put("address", cursor.getString(22));
                                    map.put("event_status", cursor.getString(2));
                                    map.put("event_approval_status", cursor.getString(18));
                                    map.put("event_customer_id", cursor.getString(8));
                                    map.put("event_datetime", cursor.getString(3));
                                    map.put("event_purpose", cursor.getString(20));
                                    map.put("event_id", cursor.getString(30));
                                    map.put("emp_visit_id", cursor.getString(24));
                                } else {
                                    // adding each child node to HashMap key => value
                                    map.put("event_name", cursor.getString(0));
                                    map.put("address", cursor.getString(11));
                                    map.put("location_district", cursor.getString(12));
                                    map.put("location_taluka", cursor.getString(13));
                                    map.put("location_village", cursor.getString(14));
                                    map.put("event_status", cursor.getString(2));
                                    map.put("event_customer_id", cursor.getString(8));
                                    map.put("event_datetime", cursor.getString(3));
                                    map.put("event_purpose", cursor.getString(4));
                                    map.put("event_id", cursor.getString(30));
                                    map.put("event_approval_status", cursor.getString(18));
                                    map.put("emp_visit_id", cursor.getString(24));
                                }

                                favouriteItem.add(map);


                               /* Log.e("---inserted values --", "event_name :" + cursor.getString(0)
                                        + "\n visit_plaanetype" + cursor.getString(1)
                                        + "\n event_status" + cursor.getString(2)
                                        + "\n event_datetime" + cursor.getString(3)
                                        + "\n event_purpose" + cursor.getString(4)
                                        + "\n type :" + cursor.getString(5)
                                        + "\n geoid :" + cursor.getString(6)
                                        + "\n userid :" + cursor.getString(7)
                                        + "\n event_customer_id" + cursor.getString(8)
                                        + "\n mobile :" + cursor.getString(9)
                                        + "\n village :" + cursor.getString(10)
                                        + "\n location address :" + cursor.getString(11)
                                        + "\n field area :" + cursor.getString(12)
                                        + "\n checkin :" + cursor.getString(13)
                                        + "\n comments" + cursor.getString(14)
                                        + "\n approval status" + cursor.getString(15)
                                        + "\n Event date" + cursor.getString(16)
                                        + "\n Event purpose" + cursor.getString(17)
                                        + "\n masterid :" + cursor.getString(18)
                                        + "\n event venue :" + cursor.getString(19)
                                        + "\n event participents :" + cursor.getString(20)
                                        + "\n ffmid :" + cursor.getString(21)
                                        + "\n createdby :" + cursor.getString(22)
                                        + "\n updatedby :" + cursor.getString(23)

                                        + "\n cdatetime :" + cursor.getString(24)
                                        + "\n udatetime :" + cursor.getString(25)
                                        + "\n eventname :" + cursor.getString(26)
                                        + "\n event id :" + cursor.getString(27));
*/
                            } else {

                            }
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }


                    } while (cursor.moveToNext());


                } else {
                    Log.e("no plans", "no plans");
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            adapter = new ItemfavitemAdapter(getApplicationContext(), favouriteItem);
            Log.e("adapter ", String.valueOf(adapter.getCount()));
            listView.setAdapter(adapter);


        }
    }

    class ItemfavitemAdapter extends BaseAdapter {

        Context context;
        ArrayList<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();


        public ItemfavitemAdapter(Context context, ArrayList<HashMap<String, String>> results) {
            this.context = context;
            this.results = results;

        }

        @Override
        public int getCount() {
            return results.size();
        }

        @Override
        public Object getItem(int position) {
            return results;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ViewHolder holder = new ViewHolder();
            if (convertView == null) {

                sharedpreferences = context.getSharedPreferences(mypreference, Context.MODE_PRIVATE);

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.row_planner, parent, false);
                holder.event_name = (TextView) convertView.findViewById(R.id.tv_customer);
                //holder.tv_status       = (TextView) convertView.findViewById(R.id.status);
                holder.event_purpose = (TextView) convertView.findViewById(R.id.tv_purpose);
                holder.event_status = (TextView) convertView.findViewById(R.id.tv_status);
                holder.tv_approvalstatus = (TextView) convertView.findViewById(R.id.tv_approvalstatus);
                holder.tv_accept = (TextView) convertView.findViewById(R.id.tv_accept);
                holder.tv_reject = (ImageView) convertView.findViewById(R.id.tv_reject);
                convertView.setTag(holder);

            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            if (userId.equalsIgnoreCase(Common.getUserIdFromSP(getApplicationContext()))) {
                holder.tv_accept.setVisibility(View.GONE);
                holder.tv_reject.setVisibility(View.GONE);
            } else if (role == Constants.Roles.ROLE_7 || role == Constants.Roles.ROLE_12) {
//                holder.tv_accept.setVisibility(View.GONE);
//                holder.tv_reject.setVisibility(View.GONE);
            } else {
                /*if (results.get(position).get("event_approval_status").equalsIgnoreCase("1")) {
                    holder.tv_accept.setVisibility(View.GONE);
                    holder.tv_reject.setVisibility(View.GONE);
                } else if (results.get(position).get("event_approval_status").equalsIgnoreCase("2")) {
                    holder.tv_accept.setVisibility(View.GONE);
                    holder.tv_reject.setVisibility(View.GONE);
                } else {
                    holder.tv_accept.setVisibility(View.VISIBLE);
                    holder.tv_reject.setVisibility(View.VISIBLE);
                }*/
            }
            //Glide.with(context).load(results.get(position).get("image")).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.pic);
            holder.event_name.setText(results.get(position).get("event_name"));
            if (results.get(position).get("event_purpose").equalsIgnoreCase("1")) {
                holder.event_purpose.setText(Html.fromHtml("Payment Collection "));
            } else if (results.get(position).get("event_purpose").equalsIgnoreCase("6")) {
                holder.event_purpose.setText(Html.fromHtml("ABS"));
            } else if (results.get(position).get("event_purpose").equalsIgnoreCase("5")) {
                holder.event_purpose.setText(Html.fromHtml("Sales order"));
            } else if (results.get(position).get("event_purpose").equalsIgnoreCase("4")) {
                holder.event_purpose.setText(Html.fromHtml("Stock Information"));
            } else if (results.get(position).get("event_purpose").equalsIgnoreCase("7")) {
                holder.event_purpose.setText(Html.fromHtml("POG"));
            } else if (results.get(position).get("event_purpose").equalsIgnoreCase("3")) {
                holder.event_purpose.setText(Html.fromHtml("Feedback"));
            } else if (results.get(position).get("event_purpose").equalsIgnoreCase("2")) {
                holder.event_purpose.setText(Html.fromHtml("Complaint"));
            } else {
                holder.event_purpose.setText(Html.fromHtml(results.get(position).get("event_purpose")));
            }
            holder.event_status.setText(results.get(position).get("event_status") + " U " + userId);

            if (results.get(position).get("event_status").equalsIgnoreCase("1")) {
                holder.event_status.setText("Open");
                holder.event_status.setTextColor(getResources().getColor(R.color.colorPrimary));
            } else if (results.get(position).get("event_status").equalsIgnoreCase("2")) {
                holder.event_status.setText("Deviation");
                holder.event_status.setTextColor(getResources().getColor(R.color.tabselectedcolor));
            } else if (results.get(position).get("event_status").equalsIgnoreCase("3")) {
                holder.event_status.setText("In Progress");
                holder.event_status.setTextColor(getResources().getColor(R.color.tabselectedcolor));
            } else if (results.get(position).get("event_status").equalsIgnoreCase("4")) {
                holder.event_status.setText("Completed");
                holder.event_status.setTextColor(getResources().getColor(R.color.green));
            } else {

            }
            if (results.get(position).get("event_approval_status").equalsIgnoreCase("1")) {
                holder.tv_approvalstatus.setText("Approved");
                holder.tv_approvalstatus.setTextColor(getResources().getColor(R.color.white));
                holder.tv_approvalstatus.setBackgroundResource(R.drawable.background_green);
                holder.tv_accept.setVisibility(View.GONE);
                holder.tv_reject.setVisibility(View.GONE);
            } else if (results.get(position).get("event_approval_status").equalsIgnoreCase("2")) {
                holder.tv_approvalstatus.setText("Rejected");
                holder.tv_approvalstatus.setTextColor(getResources().getColor(R.color.white));
                holder.tv_approvalstatus.setBackgroundResource(R.drawable.background_orange);
                holder.tv_accept.setVisibility(View.GONE);
                holder.tv_reject.setVisibility(View.GONE);
            } else {
                holder.tv_approvalstatus.setText("Pending");
                holder.tv_approvalstatus.setTextColor(getResources().getColor(R.color.black));
                holder.tv_approvalstatus.setBackgroundResource(R.drawable.background_yellow);
                if (userId.equalsIgnoreCase(Common.getUserIdFromSP(getApplicationContext()))) {
                    holder.tv_accept.setVisibility(View.GONE);
                    holder.tv_reject.setVisibility(View.GONE);
                } else {
                    holder.tv_accept.setVisibility(View.VISIBLE);
                    holder.tv_reject.setVisibility(View.VISIBLE);
                }
            }

            /*PlannerApproval pa = db.getPAByUserId(Common.getUserIdFromSP(PlanerOneActivity.this), results.get(position).get("emp_visit_id"));
            if (pa == null) {
                holder.tv_accept.setVisibility(View.GONE);
                holder.tv_reject.setVisibility(View.GONE);
            } else if (pa != null && ((pa.plannerStatus.length() > 0 && !pa.plannerStatus.equalsIgnoreCase("0")) || pa.assignedUserId.equalsIgnoreCase(String.valueOf(Common.getUserIdFromSP(PlanerOneActivity.this))) || pa.createdBy.equalsIgnoreCase(String.valueOf(Common.getUserIdFromSP(PlanerOneActivity.this))))) {
                holder.tv_accept.setVisibility(View.GONE);
                holder.tv_reject.setVisibility(View.GONE);
            }*/
            holder.tv_accept.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        PlanerOneActivity.this.emp_visit_id = results.get(position).get("emp_visit_id");
                        sql_id = results.get(position).get("event_id");
                        event_approval_status = "1";

                        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

                        if (netInfo != null) {  // connected to the internet
                            if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                                // connected to wifi
                                // Toast.makeText(getActivity(), netInfo.getTypeName(), Toast.LENGTH_SHORT).show();
                                showAproveAlertToUser(results.get(position).get("event_id"));
                            } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                                // connected to the mobile provider's data plan
                                showAproveAlertToUser(results.get(position).get("event_id"));

                            }
                        } else {
                            Toast.makeText(context, "No Internet Conection ! \n Please try again   ", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        Log.d("Tag", e.toString());
                    }


                }
            });
            holder.tv_reject.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    PlanerOneActivity.this.emp_visit_id = results.get(position).get("emp_visit_id");
                    sql_id = results.get(position).get("event_id");
                    event_approval_status = "2";

                    try {
                        ConnectivityManager conMgr = (ConnectivityManager) getApplicationContext().getSystemService(Context.CONNECTIVITY_SERVICE);
                        NetworkInfo netInfo = conMgr.getActiveNetworkInfo();

                        if (netInfo != null) {  // connected to the internet
                            if (netInfo.getType() == ConnectivityManager.TYPE_WIFI) {
                                // connected to wifi
                                // Toast.makeText(getActivity(), netInfo.getTypeName(), Toast.LENGTH_SHORT).show();
                                showRejectAlertToUser(results.get(position).get("event_id"));
                            } else if (netInfo.getType() == ConnectivityManager.TYPE_MOBILE) {
                                // connected to the mobile provider's data plan
                                showRejectAlertToUser(results.get(position).get("event_id"));

                            }
                        } else {
                            Toast.makeText(context, "No Internet Conection ! \n Please try again   ", Toast.LENGTH_SHORT).show();
                        }

                    } catch (Exception e) {
                        Log.d("Tag", e.toString());
                    }

                }
            });


            Log.e("++ event status ++", results.get(position).get("event_status"));


            return convertView;
        }

        public void updateResults(ArrayList<HashMap<String, String>> results) {

            this.results = results;
            notifyDataSetChanged();
            Log.e("sssssssss", String.valueOf(results.size()));
            if (results.size() == 0) {
                tv_noresults.setVisibility(View.VISIBLE);
                listView.setVisibility(View.GONE);
            } else {
                tv_noresults.setVisibility(View.GONE);
                listView.setVisibility(View.VISIBLE);
            }

        }

        public class ViewHolder {
            public TextView event_name, event_purpose, event_status, tv_approvalstatus, tv_accept;
            public ImageView tv_reject;

        }
    }


    private class Async_Checkin extends AsyncTask<Void, Void, String> {
        final MediaType MEDIA_TYPE = MediaType.parse("image/jpg");

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PlanerOneActivity.this);
            progressDialog.setMessage("Submitting CheckIn");
            progressDialog.show();
        }

        protected String doInBackground(Void... params) {
            check_in_time = timeFormat.format(mytime);
            //  Log.e("Check_in_time",check_in_time);

            visit_date = dateFormat.format(myDate);

            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdtime = new SimpleDateFormat("HH:mm:ss");
            String strDate = sdtime.format(c.getTime());
            Log.e("Check_in_time", strDate);

            try {
                OkHttpClient client = new OkHttpClient();
                /*For passing parameters*/
                MultipartBuilder multipartBuilder = new MultipartBuilder();
                multipartBuilder.type(MultipartBuilder.FORM);
                multipartBuilder.addFormDataPart("type", "check_in_lat_lon");
                multipartBuilder.addFormDataPart("visit_type", "1");
                multipartBuilder.addFormDataPart("user_id", userId);
                multipartBuilder.addFormDataPart("latlon", checkinlatlong);
                multipartBuilder.addFormDataPart("visit_date", visit_date);
                multipartBuilder.addFormDataPart("check_in_time", check_in_time);
                multipartBuilder.addFormDataPart("app_version", String.valueOf(versionCode));
                multipartBuilder.addFormDataPart("installed_apps", Common.getPackageName(PlanerOneActivity.this));
                if (starting_kms_img.getVisibility() == View.VISIBLE) {
                    if (outputFileKm != null)
                        multipartBuilder.addFormDataPart("meter_reading_checkin_image", outputFileKm.getAbsolutePath(), RequestBody.create(MEDIA_TYPE, outputFileKm));
                    multipartBuilder.addFormDataPart("meter_reading_checkin_text", meter_reading_checkin_text);

                }
                multipartBuilder.addFormDataPart("vehicle_type", Common.getDataFromSP(PlanerOneActivity.this, "VEHICLE_TYPE"));
                if (checkInComment != null) {
                    multipartBuilder.addFormDataPart("checkin_comment", checkInComment);

                }

                RequestBody formBody = multipartBuilder.build();
              /*  RequestBody formBody = new FormEncodingBuilder()
                        .add("type", "check_in_lat_lon")
                        .add("visit_type", "1")
                        .add("user_id", userId)
                        .add("latlon", checkinlatlong)
                        .add("visit_date", visit_date)
                        .add("check_in_time", check_in_time)
                        .add("app_version", String.valueOf(versionCode))
                        .add("installed_apps", Common.getPackageName(PlanerOneActivity.this))
                        .add("meter_reading_checkin_image", Common.getPackageName(PlanerOneActivity.this))
                        .add("meter_reading_checkin_text", Common.getPackageName(PlanerOneActivity.this))
                        .add("vehicle_type", Common.getPackageName(PlanerOneActivity.this))

                        .build();*/

                Response responses = null;
                System.out.println("!!!!!!!1" + Common.bodyToString(formBody));

              /*  MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType,
                        "type=" + "check_in_lat_lon" + "&visit_type=" + "1" + "&user_id=" + userId + "&latlon=" + checkinlatlong + "&visit_date=" + visit_date + "&check_in_time=" + check_in_time);
           */
                Request request = new Request.Builder()
                        .url(Constants.URL_NSL_MAIN + Constants.URL_CHECKINOUT)
                        .post(formBody)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        //.addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();


                client.newCall(request).enqueue(new Callback() {
                    public int recordExits = 0;

                    @Override
                    public void onFailure(Request request, IOException e) {
                        Log.d("Error: :", e.toString());
                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        jsonData = response.body().string();
                        Log.d("JSON:", jsonData);
                        //deleteFile();

                        if (jsonData != null) {
                            JSONArray jsonarray;
                            try {
                                Log.d("JSON:", jsonData);
                                JSONObject jsonobject = new JSONObject(jsonData);
                                status = jsonobject.getString("status");
                                if (status.equalsIgnoreCase("success")) {

                                    trackingid = jsonobject.getString("tracking_id");
                                    String meter_reading_checkin_image = jsonobject.getString("meter_reading_checkin_image");
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.putString("tracking_id", trackingid);
                                    editor.putString("checkinlatlong", checkinlatlong);
                                    editor.putString("checkin", "true");
                                    editor.commit();
                                    Cursor cc = null;
                                    try {
                                        String selectQuerys = "SELECT  " + KEY_TABLE_GEO_TRACKING_ID + "," + KEY_TABLE_GEO_TRACKING_ROUTE_PATH_LAT_LONG + "," + KEY_TABLE_GEO_TRACKING_FFMID + "," + PAUSE + "," + RESUME + " FROM " + TABLE_GEO_TRACKING + " where " + KEY_TABLE_GEO_TRACKING_USER_ID + " = " + userId + " and  visit_date like '" + datefromcalander + "%'" + " ORDER BY " + KEY_TABLE_GEO_TRACKING_ID + " DESC LIMIT 1 ";
                                        sdbw = db.getWritableDatabase();

                                        cc = sdbw.rawQuery(selectQuerys, null);
                                        if (cc != null && cc.moveToFirst()) {
                                            recordExits = cc.getCount();
                                        }

                                        if (recordExits == 0)
                                            db.addGeotracking(new Geo_Tracking_POJO(trackingid, "", userId, checkinlatlong, "", checkinlatlong, str_distance, visit_date, check_in_time, null, "", trackingid, visit_date, visit_date, null,
                                                    meter_reading_checkin_image,
                                                    meter_reading_checkin_text,
                                                    null,
                                                    null,
                                                    Common.getDataFromSP(PlanerOneActivity.this, "VEHICLE_TYPE"),
                                                    null, checkInComment, null, null));

                                    } catch (Exception e) {
                                        e.printStackTrace();
                                    } finally {
                                        Common.closeCursor(cc);
                                        Common.closeDataBase(sdbw);
                                    }
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            rfaButtonColor(R.color.colorAccent);
                                            rapidFloating(2, 1);
                                            isSignedIn = true;

                                        }
                                    });


                                    AlarmReceiver.setAlarm(false, PlanerOneActivity.this);
                                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
                                        ComponentName componentName = new ComponentName(PlanerOneActivity.this, JobScheduleService.class);
                                        final JobInfo jobInfo = new JobInfo.Builder(mJobId, componentName)
                                                .setRequiredNetworkType(JobInfo.NETWORK_TYPE_ANY)
                                                .build();

                                        JobScheduler jobScheduler = (JobScheduler) getSystemService(
                                                Context.JOB_SCHEDULER_SERVICE);
                                        jobScheduler.schedule(jobInfo);
                                    }
                                    LocationProviderChanged.saveGpsStatus(PlanerOneActivity.this, "");

                                    deleteFile();
                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            }

                        } else {
                            Log.e("ServiceHandler", "Couldn't get any data from the url");
                            Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
                        }
                        Common.dismissProgressDialog(progressDialog);

                    }
                });


                //   System.out.println("!!!!!!!1" + jsonData);


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

    private void deleteFile() {
        try {
            if (outputFileKm != null && outputFileKm.exists()) {
                outputFileKm.delete();
                outputFileKm = null;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        outputFileKm = null;
    }

    private class Async_UpdateAprovalStatus extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PlanerOneActivity.this);
            progressDialog.setMessage("Updating Approval Status");
            progressDialog.show();
        }

        protected String doInBackground(Void... params) {
            check_in_time = timeFormat.format(mytime);
            //  Log.e("Check_in_time",check_in_time);

            visit_date = dateFormat.format(myDate);

            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdtime = new SimpleDateFormat("HH:mm:ss");
            String strDate = sdtime.format(c.getTime());
            Log.e("Check_in_time", strDate);

            try {
                OkHttpClient client = new OkHttpClient();


                Response responses = null;


                RequestBody formBody = new FormEncodingBuilder()
                        .add("table", "employee_visit_management")
                        .add("primary_key", "emp_visit_id")
                        .add("primary_value", emp_visit_id)
                        .add("sql_id", sql_id)
                        .add("approval_status", event_approval_status)
                        .add("updated_by", sharedpreferences.getString("userId", ""))
                        .build();

                Common.Log.i("form data " + formBody.toString());


                /*MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType,
                        "type=check_in_lat_lon&visit_type=1&user_id=7&latlon=17.4411%2C78.3911&visit_date=2016-12-05%2C18%3A27%3A30&check_in_time=18%3A27%3A30&tracking_id=0");*/
                Request request = new Request.Builder()
                        .url(Constants.URL_TABLE_UPDATE)
                        .post(formBody)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();


                try {
                    responses = client.newCall(request).execute();
                    jsonData = responses.body().string();
                    System.out.println("!!!!!!!1 approve" + jsonData);
                } catch (IOException e) {
                    e.printStackTrace();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Common.dismissProgressDialog(progressDialog);

            if (jsonData != null) {
                JSONArray jsonarray;
                try {
                    JSONObject jsonobject = new JSONObject(jsonData);
                    status = jsonobject.getString("status");
                    if (status.equalsIgnoreCase("success")) {
                        String approval_status = jsonobject.getString("approval_status");
                        sdbw = db.getWritableDatabase();
                        String updatequerys = "UPDATE " + TABLE_EMPLOYEE_VISIT_MANAGEMENT + " SET " + KEY_EMP_APPROVAL_STATUS + " = '" + approval_status + "' WHERE " + KEY_EMP_VISIT_ID + " = " + sql_id;
                        sdbw.execSQL(updatequerys);

                       /* JSONObject planner_approval = jsonobject.getJSONObject("planner_approval");
                        PlannerApproval plannerApproval = new PlannerApproval();
                        plannerApproval.plannerApprovalId = planner_approval.getString("planner_approval_id");
                        plannerApproval.empVisitId = planner_approval.getString("emp_visit_id");
                        plannerApproval.assignedUserId = planner_approval.getString("assigned_user_id");
                        plannerApproval.userId = planner_approval.getString("user_id");
                        plannerApproval.plannerStatus = planner_approval.getString("planner_status");
                        plannerApproval.syncStatus = planner_approval.getString("sync_status");
                        plannerApproval.createdBy = planner_approval.getString("created_by");
                        plannerApproval.modifiedBy = planner_approval.getString("modified_by");
                        plannerApproval.cDateTime = planner_approval.getString("created_date_time");
                        plannerApproval.mDateTime = planner_approval.getString("modified_date_time");
                        db.insertPlannerApproval(plannerApproval);
                        db.updatePA(Common.getUserIdFromSP(PlanerOneActivity.this), event_approval_status, plannerApproval.empVisitId);*/
                        new Async_getalloffline().execute();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    Common.closeDataBase(sdbw);
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                //  Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
            }


        }
    }

    private class Async_Checkout extends AsyncTask<String, Void, String> {
        String polyLine;
        String distanceFinal;
        final MediaType MEDIA_TYPE = MediaType.parse("image/jpg");

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            //progressDialog = new ProgressDialog(PlanerOneActivity.this);
            // progressDialog.setMessage("Submitting Check out");
            // progressDialog.show();
        }

        protected String doInBackground(String... params) {


            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdftime = new SimpleDateFormat("HH:mm:ss");
            final String strDate = sdftime.format(c.getTime());
            Log.e("Check_out_time", strDate);

            try {
                polyLine = params[0];
                distanceFinal = params[1];
                OkHttpClient client = new OkHttpClient();
                /*For passing parameters*/
                MultipartBuilder multipartBuilder = new MultipartBuilder();
                multipartBuilder.type(MultipartBuilder.FORM);


                multipartBuilder.addFormDataPart("type", "check_out_lat_lon");
                multipartBuilder.addFormDataPart("latlon", checkinlatlong);
                multipartBuilder.addFormDataPart("check_out_time", strDate);
                multipartBuilder.addFormDataPart("tracking_id", ffmIDTrackingID);
                multipartBuilder.addFormDataPart("user_id", userId);
                //  multipartBuilder.addFormDataPart("distance", params[1]);
              /*  multipartBuilder.addFormDataPart("route_snap", String.valueOf(snapRoadCallsSuccess));
                multipartBuilder.addFormDataPart("route_snap_all", String.valueOf(snapRoadCallsAll));
                multipartBuilder.addFormDataPart("route_snap_failure", String.valueOf(snapRoadCallsFailure));
                multipartBuilder.addFormDataPart("google_direction", String.valueOf(directionServiceCallsSuccess));
                multipartBuilder.addFormDataPart("google_direction_all", String.valueOf(directionServiceCallsAll));
                multipartBuilder.addFormDataPart("google_direction_failure", String.valueOf(directionServiceCallsFailure));*/
                multipartBuilder.addFormDataPart("pause", Common.isStringNull(params[2]));
                multipartBuilder.addFormDataPart("resume", Common.isStringNull(params[3]));
                multipartBuilder.addFormDataPart("route_path_lat_lon", stringRoutePath);
                // multipartBuilder.addFormDataPart("polyline", params[0]);
                multipartBuilder.addFormDataPart("check_out_by", "0");
                if (starting_kms_img.getVisibility() == View.VISIBLE) {
                    if (outputFileKm != null)
                        multipartBuilder.addFormDataPart("meter_reading_checkout_image", outputFileKm.getAbsolutePath(), RequestBody.create(MEDIA_TYPE, outputFileKm));
                    multipartBuilder.addFormDataPart("meter_reading_checkout_text", meter_reading_checkin_text);

                }

                if (checkInComment != null) {
                    if (tv_comment_hint.getText().toString() != null && tv_comment_hint.getText().toString().trim().equalsIgnoreCase("Personal Uses KM")) {
                        multipartBuilder.addFormDataPart("personal_uses_km", checkInComment);
                    } else {

                        multipartBuilder.addFormDataPart("checkin_comment", checkInComment);
                    }

                }
                RequestBody formBody = multipartBuilder.build();
                Response responses = null;

                Log.d("body", "body:  " + Common.bodyToString(formBody));
                Log.d("body", "route path:  " + stringRoutePath);

                /*MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType,
                        "type=check_in_lat_lon&visit_type=1&user_id=7&latlon=17.4411%2C78.3911&visit_date=2016-12-05%2C18%3A27%3A30&check_in_time=18%3A27%3A30&tracking_id=0");*/
                Request request = new Request.Builder()
                        .url(Constants.URL_NSL_MAIN + Constants.URL_CHECKINOUT)
                        .post(formBody)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        // .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();

                client.newCall(request).enqueue(new Callback() {
                    @Override
                    public void onFailure(Request request, IOException e) {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            mService.cancelAlarm();
                        }
                        mService.removeLocationUpdates();
                        Common.dismissProgressDialog(progressDialogInOut);

                    }

                    @Override
                    public void onResponse(Response response) throws IOException {
                        jsonData = response.body().string();
                        System.out.println("!!!!!!!1 checkout" + jsonData);
                        if (jsonData != null) {
                            try {
                                //deleteFile();
                                // {"status":"success","msg":"successfully! done","tracking_id":"11723"}
                                JSONObject jsonObject = new JSONObject(jsonData);
                                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                                    String polyline = jsonObject.getString("polyline");
                                    String distance = jsonObject.getString("distance");
                                    String meter_reading_checkout_image = jsonObject.getString("meter_reading_checkout_image");
                                    String tracking_id = jsonObject.getString("tracking_id");
                                    String personal_uses_km = jsonObject.getString("personal_uses_km");
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.putString("tracking_id", "");
                                    editor.putString("checkinlatlong", "");
                                    editor.putString("checkin", "false");
                                    editor.commit();
                                    isSignedIn = false;

                                    sdbw = db.getWritableDatabase();
                                    String updatequerys = "UPDATE " + TABLE_EMPLOYEE_VISIT_MANAGEMENT + " SET " + KEY_EMP_STATUS + " = '" + "4" + "' WHERE " + KEY_EMP_VISIT_USER_ID + " = " + userId + " and " + KEY_EMP_PLAN_DATE_TIME + " like '" + datefromcalander + "%'";
                                    sdbw.execSQL(updatequerys);

                                    String updatequery1 = "UPDATE " + TABLE_GEO_TRACKING + " SET " + KEY_TABLE_GEO_TRACKING_UPDATED_STATUS + " = 1 ," + KEY_TABLE_GEO_TRACKING_CHECK_OUT_LAT_LONG + " = '" + checkinlatlong + "'," + KEY_TABLE_GEO_TRACKING_CHECK_OUT_TIME + " = '" + strDate + "'," + KEY_TABLE_GEO_TRACKING_POLYLINE + " = '" + polyline + "'," + KEY_TABLE_GEO_TRACKING_DISTANCE + " = '" + distance + "'," + METER_READING_CHECKOUT_IMAGE + " = '" + meter_reading_checkout_image + "'," + PERSONAL_USES_KM + " = '" + personal_uses_km + "'," + METER_READING_CHECKOUT_TEXT + " = '" + meter_reading_checkin_text + "' WHERE " + KEY_TABLE_GEO_TRACKING_ID + " = " + tid;

                                    sdbw.execSQL(updatequery1);
                                    runOnUiThread(new Runnable() {
                                        @Override
                                        public void run() {
                                            mSpeedDialView.setVisibility(View.GONE);
                                        }
                                    });

                                    new AsyncCheckoutAcknowledge(PlanerOneActivity.this, tracking_id).execute();

                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                Common.closeDataBase(sdbw);
                            }
                        }

                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
                            mService.cancelAlarm();
                        }
                        mService.removeLocationUpdates();
                        Common.dismissProgressDialog(progressDialogInOut);

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


           /* Intent intent1 = new Intent(PlanerOneActivity.this, LocationService.class);
            stopService(intent1);
            Log.d("onDestroy", "onDestroy 2.......................");*/


        }
    }


    private class Async_getRMDirectDistributor extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
           /* progressDialog = new ProgressDialog(PlanerOneActivity.this);
            progressDialog.setMessage("Please wait");
            progressDialog.show();*/
        }

        protected String doInBackground(Void... params) {


            sdbw = db.getWritableDatabase();

            String selectQuery1 = "SELECT DISTINCT (CR.customer_id)," +
                    "CR.customer_name, " +
                    "CR.customer_code, " +
                    "C.company_id, " +
                    "C.company_code" +
                    " FROM user_company_customer AS CDC JOIN customers AS CR ON CDC.customer_id = CR.customer_id " +
                    "JOIN companies AS C ON C.company_id = CDC.company_id WHERE CR." + KEY_TABLE_USER_COMPANY_CUSTOMER_STATUS + " = 1" + " and CDC." + KEY_TABLE_USER_COMPANY_CUSTOMER_STATUS + " = 1 and user_id =" + Common.getUserIdFromSP(PlanerOneActivity.this);


            Log.e("Distributor selectQuery", selectQuery1);
            Cursor cursor1 = null;
            try {
                cursor1 = sdbw.rawQuery(selectQuery1, null);

                if (cursor1 != null && cursor1.getCount() > 0) {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            // fab.setVisibility(View.VISIBLE);
                            callapi(1);
                            isDirectCustomers = true;

                            febButtonStatus();

                        }
                    });

                } else {
                    callapi(0);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                Common.closeCursor(cursor1);
                Common.closeDataBase(sdbw);
            }

            // do some stuff....

            Common.Log.i(favouriteItem.toString());

            return jsonData;
        }

        protected void onPostExecute(String s) {
            super.onPostExecute(s);


        }
    }

    private boolean checkPermissions() {
        return PackageManager.PERMISSION_GRANTED == ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION);
    }

    @Override
    protected void onStart() {
        super.onStart();

        PreferenceManager.getDefaultSharedPreferences(this)
                .registerOnSharedPreferenceChangeListener(this);
        if (checkPermissions()) {
            bindService(new Intent(this, LocationUpdatesService.class), mServiceConnection,
                    Context.BIND_AUTO_CREATE);
        }
        if (Common.haveInternet(this)) {
            String[] ids = isCheckedIn(this);
            if (ids != null && ids.length > 3 && ids[0] != null && ids[0].length() > 8) {
                new Async_Routepath(this, ids[0], ids[1], ids[2], ids[3]).execute();
            }
        }
    }


    @Override
    protected void onResume() {
        super.onResume();
        myReceiver = new MyReceiver();
        LocalBroadcastManager.getInstance(this).registerReceiver(myReceiver,
                new IntentFilter(LocationUpdatesService.ACTION_BROADCAST));

        actionClick = new LocationUpdatesService.ActionClick();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction("com.nsl.beejtantra.pause.location1");
        intentFilter.addAction("com.nsl.beejtantra.resume.location1");
        this.registerReceiver(actionClick, intentFilter);
        displayToast = true;
    }

    @Override
    protected void onPause() {
        super.onPause();
        if (myReceiver != null) {
            LocalBroadcastManager.getInstance(this).unregisterReceiver(myReceiver);
            myReceiver = null;
        }
        Common.dismissProgressDialog(progressDialog);
        progressDialog = null;
        Common.dismissProgressDialog(progressDialogInOut);
        progressDialogInOut = null;
        unregisterReceiver(actionClick);
        displayToast = false;
    }

    @Override
    protected void onStop() {
        if (mBound) {
            // Unbind from the service. This signals to the service that this activity is no longer
            // in the foreground, and the service can respond by promoting itself to a foreground
            // service.
            unbindService(mServiceConnection);
            mBound = false;
        }
        PreferenceManager.getDefaultSharedPreferences(this)
                .unregisterOnSharedPreferenceChangeListener(this);
        super.onStop();
    }

    private class MyReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Location location = intent.getParcelableExtra(LocationUpdatesService.EXTRA_LOCATION);
            if (location != null) {
                locationF = location;
                if (displayToast)
                    Toast.makeText(context, Utils.getLocationText(location),
                            Toast.LENGTH_SHORT).show();
            }
        }
    }

    private class CloseProgressReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.d("CloseProgressReceiver", "CloseProgressReceiver..");
            boolean status = intent.getBooleanExtra(LocationUpdatesService.EXTRA_LOCATION_SAVED, false);
            boolean pasued = intent.getBooleanExtra(LocationUpdatesService.EXTRA_LOCATION_PAUSED, false);
            Common.dismissProgressDialog(pauseProgress);
            if (status) {
                Toast.makeText(context, "Location updated",
                        Toast.LENGTH_SHORT).show();
                if (pasued)
                    rapidFloating(2, 2);
                else rapidFloating(2, 1);
            } else {
                Toast.makeText(context, "Location not found.\n Please try again",
                        Toast.LENGTH_SHORT).show();
            }
            if (closeProgressReceiver != null) {
                LocalBroadcastManager.getInstance(context).unregisterReceiver(closeProgressReceiver);
                myReceiver = null;
            }
        }
    }

    @Override
    public void onSharedPreferenceChanged(SharedPreferences sharedPreferences, String s) {
        // Update the buttons state depending on whether location updates are being requested.
        if (s.equals(Utils.KEY_REQUESTING_LOCATION_UPDATES)) {
            //  setButtonsState(sharedPreferences.getBoolean(Utils.KEY_REQUESTING_LOCATION_UPDATES,
            //          false));
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

                Log.d("timer", strDate);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        //   locationF=null;
                        if (timer != null && locationF != null) {
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
                        }
                    }
                });


            } else {
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

            }

        }
    }


    public void checkOutFunction() {
        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdftime = new SimpleDateFormat("HH:mm:ss");
        strDate = sdftime.format(c.getTime());
        Log.e("Check_out_time", strDate);
        visit_date = dateFormat.format(myDate);
        double latitude = locationF.getLatitude();
        double longitude = locationF.getLongitude();
        int a = Common.haveInternet(this) ? 1 : 0;

        checkinlatlong = String.valueOf(latitude) + "," + String.valueOf(longitude) + "," + locationF.getAccuracy() + "," + a + "," + Common.currentTimeFromMS(System.currentTimeMillis());

        Toast.makeText(getApplicationContext(), checkinlatlong, Toast.LENGTH_LONG).show();
        String selectQuerys = "SELECT  " + KEY_TABLE_GEO_TRACKING_ID + "," + KEY_TABLE_GEO_TRACKING_ROUTE_PATH_LAT_LONG + "," + KEY_TABLE_GEO_TRACKING_FFMID + "," + PAUSE + "," + RESUME + " FROM " + TABLE_GEO_TRACKING + " where " + KEY_TABLE_GEO_TRACKING_USER_ID + " = " + userId + " and  visit_date like '" + datefromcalander + "%'" + " ORDER BY " + KEY_TABLE_GEO_TRACKING_ID + " DESC LIMIT 1 ";
        sdbw = db.getWritableDatabase();

        Cursor cc = sdbw.rawQuery(selectQuerys, null);
        System.out.println("cursor count " + selectQuerys);

        if (cc != null && cc.moveToFirst()) {
            ffmIDTrackingID = cc.getString(2);
            tid = String.valueOf(cc.getLong(0)); //The 0 is the column index, we only have 1 column, so the index is 0
            String pauseP = cc.getString(3); //The 0 is the column index, we only have 1 column, so the index is 0
            String resumeP = cc.getString(4); //The 0 is the column index, we only have 1 column, so the index is 0

            Log.e("++ checkout lastId ++", ffmIDTrackingID);

            stringRoutePath = cc.getString(1) + ":" + checkinlatlong;

            //  stringRoutePath = "23.2745823,77.3698906,4.551,1,19-27-24:23.2741552,77.3705778,9.102,1,19-27-38:23.2747185,77.3711825,6.068,1,19-27-52:23.2753108,77.3717187,13.653,1,19-28-07:23.2759512,77.3721397,4.551,1,19-28-17:23.2767163,77.3724288,7.585,1,19-28-30:23.2774168,77.37274,7.585,1,19-28-43:23.2779029,77.3733293,10.619,1,19-28-54:23.2784608,77.373993,7.585,1,19-29-08:23.2790608,77.3746576,13.653,1,19-29-21:23.2797895,77.3745348,11.871,1,19-29-45:23.280539,77.3746082,18.204,1,19-30-01:23.2813289,77.3748156,16.687,1,19-30-20:23.2812582,77.3752041,9.212,1,19-37-52";
            if (stringRoutePath != null) {
                if (Common.haveInternet(PlanerOneActivity.this)) {
                    new Async_Checkout().execute("NA", "0", pauseP, resumeP);
                } else {
                    Toast.makeText(PlanerOneActivity.this, "No Internet Connection ! \n Please Try again", Toast.LENGTH_SHORT).show();
                }
                ArrayList<String> latlng = new ArrayList(Arrays.asList(stringRoutePath.split(":")));
/*
                if (latlng != null && latlng.size() > 0) {

                    if (latlng.size() > 1) {

                        prepairDataForRoadSnap(latlng);
                    } else {
                        listForSnapRoad.add(constructURL(latlng, null));
                        Common.dismissDialog(progressDialogInOut);
                        if (listForSnapRoad.size() > 0)
                            new AsyncRoadSnap().execute(listForSnapRoad.get(0));
                    }

                }*/


            }

        }

    }


    public void prepaireDataDrawPloyline(List<LatLng> allPolylinePoints) {
        if (allPolylinePoints != null)
            Log.d("new List:", " " + allPolylinePoints.size());

        StringBuilder stringBuilder = new StringBuilder();
        distance = 0;
        if (allPolylinePoints != null && allPolylinePoints.size() > 0) {

            for (int i = 0; i < allPolylinePoints.size() - 1; i++) {
                Location location1 = new Location("");
                location1.setLatitude(allPolylinePoints.get(i).latitude);
                location1.setLongitude(allPolylinePoints.get(i).longitude);

                Location location2 = new Location("");
                location2.setLatitude(allPolylinePoints.get(i + 1).latitude);
                location2.setLongitude(allPolylinePoints.get(i + 1).longitude);
                distance += location1.distanceTo(location2);
                stringBuilder.append(allPolylinePoints.get(i).latitude + " " + allPolylinePoints.get(i).longitude + "\n");
                if (i == allPolylinePoints.size() - 2)
                    stringBuilder.append(allPolylinePoints.get(i + 1).latitude + " " + allPolylinePoints.get(i + 1).longitude + "\n");
            }
        }
        //   Common.writeFile(stringBuilder.toString());
        if (Common.haveInternet(PlanerOneActivity.this)) {
            new Async_Checkout().execute(Common.encode(finalLatLngList), String.valueOf(distance), "Paused", "Resumed");
        } else {
            Toast.makeText(PlanerOneActivity.this, "No Internet Connection ! \n Please Try again", Toast.LENGTH_SHORT).show();
        }

    }

    private class AsyncRoadSnap extends AsyncTask<String, Void, String> {
        ProgressDialog pDialog;
        String jsonData = null;
        private String pointstr;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            pDialog = new ProgressDialog(PlanerOneActivity.this);
            pDialog.setMessage("Please wait ");
            pDialog.setCancelable(false);
            if (!((Activity) PlanerOneActivity.this).isFinishing()) {
                try {

                    //show dialog
                    //  pDialog.show();
                } catch (WindowManager.BadTokenException e) {
                    e.printStackTrace();
                }
            }
        }

        protected String doInBackground(String... params) {


            try {
                OkHttpClient client = new OkHttpClient();
                Response responses = null;
                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

                Request request = new Request.Builder()
                        .url("https://roads.googleapis.com/v1/snapToRoads?key=" + API_KEY + "&interpolate=true&path=" + URLEncoder.encode(params[0], "UTF-8"))
                        .get()
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();

                //   try {
                snapRoadCallsAll++;
                responses = client.newCall(request).execute();
                jsonData = responses.body().string();
                System.out.println("!!!!!!!1 GEO_TRACKING" + "https://roads.googleapis.com/v1/snapToRoads?key=" + API_KEY + "&interpolate=true&path=" + URLEncoder.encode(params[0], "UTF-8"));
                /*} catch (IOException e) {
                    e.printStackTrace();
                    snapRoadCallsFailure++;
                }*/


            } catch (Exception e) {
                e.printStackTrace();
                snapRoadCallsFailure++;

            }

            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (pDialog.isShowing()) {
                pDialog.dismiss();
            }

            //  Log.d("adcd", new Gson().toJson(jsonData));
            try {
                List<LatLng> latLngs = new ArrayList<>();
                JSONObject jsonObject = new JSONObject(jsonData);
                JSONArray snappedPoints = jsonObject.getJSONArray("snappedPoints");
                Log.d("snappedPoints", String.valueOf(snappedPoints.length()));
                if (listRoadSnap != null && listRoadSnap.size() != 0)
                    listRoadSnap.remove(0);
                snapRoadCallsSuccess++;

                for (int i = 0; i < snappedPoints.length(); i++) {
                    JSONObject location = snappedPoints.getJSONObject(i).getJSONObject("location");
                    double latitude = location.getDouble("latitude");
                    double longitude = location.getDouble("longitude");
                    finalLatLngList.add(new LatLng(latitude, longitude));
                    pointstr += latitude + "," + longitude + "|";


                }

                Log.d("listsize", String.valueOf(finalLatLngList.size()));
               /* for (int i = 0; i < finalLatLngList.size(); i++) {
                    Common.Log.i("s: " + finalLatLngList.get(i).latitude + "," + finalLatLngList.get(i).longitude);
                }
*/
                //   Log.d("points ", pointstr);
                hitGoogleServices(listRoadSnap);
                //  mMap.addPolyline(new PolylineOptions().addAll(latLngs).color(Color.BLUE).width(2));
                //   mMap.animateCamera(CameraUpdateFactory.newLatLngBounds(bounds.build(), 0));
            } catch (JSONException e) {
                e.printStackTrace();
                snapRoadCallsFailure++;
                if (listRoadSnap != null && listRoadSnap.size() != 0) {
                    finalLatLngList.addAll(listRoadSnap.get(0));
                    listRoadSnap.remove(0);
                }
                hitGoogleServices(listRoadSnap);
            }


        }
    }

    private void hitGoogleServices(List<List<LatLng>> listRoadSnapHits) {
        if (!Common.haveInternet(this)) {
            Toast.makeText(this, Common.INTERNET_UNABLEABLE, Toast.LENGTH_LONG).show();
            locationRoadSnaps.clear();
            listRoadSnap.clear();
            listForSnapRoad.clear();
            finalLatLngList.clear(  );
            return;
        }

        if (listRoadSnapHits.size() > 0) {
            // listRoadSnap.remove(0);
            for (int i = 0; i < listRoadSnapHits.size(); i++) {
                if (listRoadSnapHits.get(i).size() < 5) {
                    finalLatLngList.addAll(listRoadSnapHits.get(i));

                    listRoadSnapHits.remove(i);
                    i--;
                    continue;
                }

                break;
            }
            if (listRoadSnapHits.size() > 0) {
                String url = constructURL(listRoadSnapHits.get(0));
                // listRoadSnap.remove(0);
                new AsyncRoadSnap().execute(url);


            }
        }
        if (listRoadSnapHits.size() == 0) {
            isDrawPolyline = true;
            for (int i = 0; i < finalLatLngList.size() - 1; i++) {
                Location startLocation = new Location("");
                startLocation.setLatitude(finalLatLngList.get(i).latitude);
                startLocation.setLongitude(finalLatLngList.get(i).longitude);

                Location endLocation = new Location("");
                endLocation.setLatitude(finalLatLngList.get(i + 1).latitude);
                endLocation.setLongitude(finalLatLngList.get(i + 1).longitude);

               /* Location location3 = new Location("");
                location3.setLatitude(finalLatLngList.get(i + 2).latitude);
                location3.setLongitude(finalLatLngList.get(i + 2).longitude);*/

                if (isDistanceGrater(startLocation, endLocation, DISTANCE_FOR_DIRECTION_CALL)) {
                    List<LatLng> list = new ArrayList<>();
                    list.add(new LatLng(startLocation.getLatitude(), startLocation.getLongitude()));
                    list.add(new LatLng(endLocation.getLatitude(), endLocation.getLongitude()));
                    updatePosition = i;
                    isDrawPolyline = false;
                    AbstractRouting.TravelMode travelMode = Routing.TravelMode.WALKING;
                    if (isDistanceGrater(startLocation, endLocation, 2000)) {
                        travelMode = Routing.TravelMode.DRIVING;
                    }

                    callDirectionAPI(list, travelMode);
                    break;

                }

            }
            if (isDrawPolyline)
                prepaireDataDrawPloyline(finalLatLngList);

        }
    }

    public String constructURL(List<String> list, String s) {
        StringBuilder sb = new StringBuilder();

        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (i != 0) {
                    sb.append("|");
                }
                String[] splitPoints = list.get(i).split(",");
                sb.append(splitPoints[0] + "," + splitPoints[1]);
            }
        }
        Log.d("URL Points:", sb.toString());
        return sb.toString();
    }

    public String constructURL(List<LatLng> list) {
        StringBuilder sb = new StringBuilder();

        if (list != null && list.size() > 0) {
            for (int i = 0; i < list.size(); i++) {
                if (i != 0) {
                    sb.append("|");
                }

                sb.append(list.get(i).latitude + "," + list.get(i).longitude);
            }
        }
        Log.d("URL Points:", sb.toString());
        return sb.toString();
    }

    public void callDirectionAPI(List<LatLng> latLngs, AbstractRouting.TravelMode travelMode) {
        Log.d("latLngs:", latLngs.toString());
        Routing routing = new Routing.Builder()
                .travelMode(travelMode)
                .withListener(this)
                .waypoints(latLngs)
                .alternativeRoutes(false)
                .key(API_KEY)
                .build();
        routing.execute();
        directionServiceCallsAll++;
    }


    @Override
    public void onRoutingFailure(RouteException e) {
        Toast.makeText(getApplicationContext(), "Somthing went wrong..", Toast.LENGTH_SHORT).show();
        onRouteFailureError = e.toString();
        directionServiceCallsFailure++;
        prepaireDataDrawPloyline(finalLatLngList);
        //  Common.dismissProgressDialog(progressDialogInOut);
    }

    @Override
    public void onRoutingStart() {

    }

    @Override
    public void onRoutingSuccess(List<Route> route, int shortestRouteIndex) {
        directionServiceCallsSuccess++;
        if (directionServiceCallsAll == 1) {
            for (int i = 0; i < finalLatLngList.size(); i++) {
                Common.Log.i("s: " + finalLatLngList.get(i).latitude + "," + finalLatLngList.get(i).longitude);
            }
        }

       /* System.out.print("Yes 2:  " + route.get(0).getPolyline() + "\n");
        System.out.print("Yes 2.1:  " + Common.encode(route.get(0).getPoints()));*/
        //  polyLinrTest.clear();
        isDrawPolyline = true;
        if (route != null && route.size() != 0) {
            Log.d("onRoutingSuccess", "length " + route.get(0).getPoints().size());
            finalLatLngList.addAll(updatePosition + 1, route.get(0).getPoints());

            updatePosition = (updatePosition + 1 + route.get(0).getPoints().size());

            for (int i = updatePosition; i < finalLatLngList.size() - 1; i++) {
                Location startLocation = new Location("");
                startLocation.setLatitude(finalLatLngList.get(i).latitude);
                startLocation.setLongitude(finalLatLngList.get(i).longitude);

                Location endLocation = new Location("");
                endLocation.setLatitude(finalLatLngList.get(i + 1).latitude);
                endLocation.setLongitude(finalLatLngList.get(i + 1).longitude);

                if (isDistanceGrater(startLocation, endLocation, DISTANCE_FOR_DIRECTION_CALL)) {
                    List<LatLng> list = new ArrayList<>();
                    list.add(new LatLng(startLocation.getLatitude(), startLocation.getLongitude()));
                    list.add(new LatLng(endLocation.getLatitude(), endLocation.getLongitude()));
                    updatePosition = i;
                    isDrawPolyline = false;
                    AbstractRouting.TravelMode travelMode = Routing.TravelMode.WALKING;
                    if (isDistanceGrater(startLocation, endLocation, 2000)) {
                        travelMode = Routing.TravelMode.DRIVING;
                    }
                    callDirectionAPI(list, travelMode);
                    break;

                }

            }


        }
        if (isDrawPolyline)
            prepaireDataDrawPloyline(finalLatLngList);


    }


    @Override
    public void onRoutingCancelled() {

    }

    /*private void prepairListOfNewData(ArrayList<String> latlng) {
        ArrayList<String> latLngList = new ArrayList<>();
        final List<List<String>> listList = new ArrayList<>();

        Log.d("split", "length-1->  " + latlng.size());
        newListSize = (int) Math.ceil(latlng.size() / MAX_POINT);
        Log.d("prepairData", "prepairData: " + newListSize);
        for (int i = 0; i < latlng.size(); i++) {

            String[] pointsStrArry = latlng.get(i).split(",");
            if (pointsStrArry.length > 2 && Float.parseFloat(pointsStrArry[3]) > 500) {
                continue;
            }
            latLngList.add(pointsStrArry[0] + "," + pointsStrArry[1]);
            if (isCount90(i) && i != 0) {
                Log.d("prepairData", "prepairData: " + i);
                listList.add(latLngList);
                latLngList = new ArrayList<>();
                String[] pointsStrArry1 = latlng.get(i - 1).split(",");
                latLngList.add(pointsStrArry1[0] + "," + pointsStrArry1[1]);
            }
            if (!isCount90(i) && i == latlng.size() - 1) {
                listList.add(latLngList);
            }

        }
        Log.d("split", "length-2->  " + listList.toString());
        for (int j = 0; j < listList.size(); j++) {
            listForSnapRoad.add(constructURL(listList.get(j), null));
        }
        Common.dismissDialog(progressDialogInOut);
        if (listForSnapRoad.size() > 0) {
            new AsyncRoadSnap().execute(listForSnapRoad.get(0));
        }

    }*/

    public boolean isCount23(int position) {
        return position % 23 == 0;

    }

    public boolean isCount90(int position) {
        return position % MAX_POINT == 0;

    }

    private boolean isDistanceGrater(Location location1, Location location2, float metres) {
        float distance = location1.distanceTo(location2);
        return distance > metres;


    }

//    @Override
//    public void onNeedPermission(String permission, int requestCode) {
//        Log.d("onNeedPermission", "onNeedPermission..");
//        checkWriteExternalStoragePermission(this, permission, requestCode);
//
//    }
//
//    @Override
//    public void onPermissionPreviouslyDenied(String permission, int requestCode) {
//        Log.d("onNeedPermission", "onPermissionPreviouslyDenied..");
//        checkWriteExternalStoragePermission(this, permission, requestCode);
//    }
//
//    @Override
//    public void onPermissionDisabled(String permission, int requestCode) {
//        Log.d("onNeedPermission", "onPermissionDisabled..");
//    }
//
//    @Override
//    public void onPermissionGranted(String permission, int requestCode) {
//        Log.d("onNeedPermission", "onPermissionGranted..");
//        if (requestCode == GPS_REQUEST_CODE) {
//            bindService(new Intent(this, LocationUpdatesService.class), mServiceConnection,
//                    Context.BIND_AUTO_CREATE);
//            PermissionUtil.checkPermission(this, this, Manifest.permission.WRITE_EXTERNAL_STORAGE, this, WRITE_REQUEST_CODE);
//        }
//
//
//    }

    @Override
    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        Common.Log.i("Permisiion.... " + requestCode + " \n " + permissions.length + " \n" + grantResults);
//        if (requestCode == 100 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {
//
//        } else {
//            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
//                requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
//            }
//
//        }
        switch (requestCode) {

//
//            case GPS_REQUEST_CODE:
//                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                    bindService(new Intent(this, LocationUpdatesService.class), mServiceConnection,
//                            Context.BIND_AUTO_CREATE);
//                    Toast.makeText(this, "Permission Granted", Toast.LENGTH_SHORT).show();
//                } else {
//                    Toast.makeText(this, "Permission Denied", Toast.LENGTH_SHORT).show();
//                    finish();
//                }
//                PermissionUtil.checkPermission(this, this, Manifest.permission.WRITE_EXTERNAL_STORAGE, this, WRITE_REQUEST_CODE);
//
//                break;
            case REQUEST_CHECK_CAMERA:
                if (requestCode == 100 && (grantResults[0] == PackageManager.PERMISSION_GRANTED)) {

                } else {
                    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.KITKAT) {
                        requestPermissions(new String[]{Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.WRITE_EXTERNAL_STORAGE}, 100);
                    }

                }
                break;
            // other 'case' lines to check for other
            // permissions this app might request
        }
    }

    private void prepairDataForRoadSnap(ArrayList<String> latlng) {

        for (int i = 0; i < latlng.size(); i++) {
            String[] pointsStrArry = latlng.get(i).split(",");
            Location location = new Location("");
            if (pointsStrArry != null && pointsStrArry.length >= 2) {
                if (pointsStrArry.length > 2)
                    location.setAccuracy(Float.parseFloat(pointsStrArry[2]));
                location.setLatitude(Double.parseDouble(pointsStrArry[0]));
                location.setLongitude(Double.parseDouble(pointsStrArry[1]));
                locationRoadSnaps.add(location);
            }

        }
        ArrayList<LatLng> latLngList = new ArrayList<>();
        listRoadSnap = new ArrayList<>();

        Log.d("split", "length-1->  " + latlng.size());
        // newListSize = (int) Math.ceil(latlng.size() / MAX_POINT);
        //  Log.d("prepairData", "prepairData: " + newListSize);
        for (int i = 0; i < locationRoadSnaps.size(); i++) {

            if (locationRoadSnaps.get(i).getAccuracy() > ACCURACY_SETTING && i > 0 && i != locationRoadSnaps.size() - 1) {
                if (!isDistanceGrater(locationRoadSnaps.get(i - 1), locationRoadSnaps.get(i), 6 * 1000) && locationRoadSnaps.get(i).getAccuracy() < 100)
                    continue;
            }
            latLngList.add(new LatLng(locationRoadSnaps.get(i).getLatitude(), locationRoadSnaps.get(i).getLongitude()));
            if (i != 0) {
                if (isCount90(latLngList.size())) {
                    Log.d("prepairData", "prepairData: " + i);
                    listRoadSnap.add(latLngList);
                    latLngList = new ArrayList<>();
                    latLngList.add(new LatLng(locationRoadSnaps.get(i - 1).getLatitude(), locationRoadSnaps.get(i - 1).getLongitude()));
                } else if (isDistanceGrater(locationRoadSnaps.get(i - 1), locationRoadSnaps.get(i), 400)) {
                    Log.d("prepairData", "prepairData: else " + i);
                    latLngList.remove(latLngList.size() - 1);
                    if (latLngList.size() > 0)
                        listRoadSnap.add(latLngList);
                    latLngList = new ArrayList<>();
                    latLngList.add(new LatLng(locationRoadSnaps.get(i).getLatitude(), locationRoadSnaps.get(i).getLongitude()));
                }
            }
            if (!isCount90(i) && i == locationRoadSnaps.size() - 1) {
                listRoadSnap.add(latLngList);
            }

        }
        Log.d("split", "length-2->  " + listRoadSnap.toString());
        Log.d("split", "length-2->  " + listRoadSnap.get(listRoadSnap.size() - 1));

        hitGoogleServices(listRoadSnap);
    }

    public void snackBar() {
        CoordinatorLayout coordinatorLayout = (CoordinatorLayout) findViewById(R.id.myCoordinatorLayout);
        // Create the Snackbar
        final Snackbar snackbar = Snackbar.make(coordinatorLayout, "", 60000);
// Get the Snackbar's layout view
        Snackbar.SnackbarLayout layout = (Snackbar.SnackbarLayout) snackbar.getView();
        layout.setBackgroundColor(getResources().getColor(R.color.transparent_50P));
// Hide the text
        TextView textView = (TextView) layout.findViewById(android.support.design.R.id.snackbar_text);
        textView.setVisibility(View.INVISIBLE);

// Inflate our custom view
        LayoutInflater li = LayoutInflater.from(getApplicationContext());
        View snackView = li.inflate(R.layout.my_snackbar, null);
        snackView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                snackbar.dismiss();
            }
        });
// Configure the view
  /*  ImageView imageView = (ImageView) snackView.findViewById(R.id.image);
    imageView.setImageBitmap(image);
    TextView textViewTop = (TextView) snackView.findViewById(R.id.text);
    textViewTop.setText(text);
    textViewTop.setTextColor(Color.WHITE);*/

//If the view is not covering the whole snackbar layout, add this line
        layout.setPadding(0, 0, 0, 0);

// Add the view to the Snackbar's layout
        layout.addView(snackView, 0);
// Show the Snackbar
        snackbar.show();
    }

    private void confirmationDailog(final String itemName) {

        final AlertDialog.Builder alertDialogBuilder = new AlertDialog.Builder(this);
        alertDialogBuilder.setMessage("Are you sure you want to " + itemName + " ?")
                .setCancelable(false)
                .setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {

                                switch (itemName) {
                                    case Constants.CHECK_IN:
                                       /* if (Common.haveInternet(PlanerOneActivity.this)) {
                                            locationRoadSnaps.clear();
                                            listRoadSnap.clear();
                                            listForSnapRoad.clear();
                                            finalLatLngList.clear();
//                                            snapRoadCallsSuccess = 0;
//                                            directionServiceCallsSuccess = 0;
                                            progressDialogInOut = new ProgressDialog(PlanerOneActivity.this);
                                            progressDialogInOut.setMessage("Please wait...");
                                            progressDialogInOut.show();
                                            progressDialogInOut.setCancelable(false);

                                            // Common.disableClickEvent(fab, true);
                                            displayGoogleLocationSettingPage(PlanerOneActivity.this, REQUEST_CHECK_SETTINGS);
                                        } else {
                                            Toast.makeText(PlanerOneActivity.this, "No Internet Connection ! \n Please Try again", Toast.LENGTH_SHORT).show();
                                        }*/
                                        break;

                                    case Constants.CHECKOUT:
                                       /* if (Common.haveInternet(PlanerOneActivity.this)) {
                                            progressDialogInOut = new ProgressDialog(PlanerOneActivity.this);
                                            progressDialogInOut.setMessage("Please wait...");
                                            progressDialogInOut.show();
                                            progressDialogInOut.setCancelable(false);
                                            new GetApiKey().execute();
                                        } else {
                                            Toast.makeText(PlanerOneActivity.this, "No Internet Connection ! \n Please Try again", Toast.LENGTH_SHORT).show();
                                        }*/
                                        break;

                                    case "Pause":
                                        pauseProgress = Common.showProgressDialog(PlanerOneActivity.this);
                                        closeProgressReceiver = new CloseProgressReceiver();
                                        LocalBroadcastManager.getInstance(PlanerOneActivity.this).registerReceiver(closeProgressReceiver,
                                                new IntentFilter(LocationUpdatesService.ACTION_BROADCAST_PAUSED_LOCATION_SAVED));
                                        Intent intentPause = new Intent();
                                        intentPause.setAction("com.nsl.beejtantra.pause.location1");
                                        (PlanerOneActivity.this).sendBroadcast(intentPause);
                                        break;
                                    case "Resume":
                                        pauseProgress = Common.showProgressDialog(PlanerOneActivity.this);
                                        closeProgressReceiver = new CloseProgressReceiver();
                                        LocalBroadcastManager.getInstance(PlanerOneActivity.this).registerReceiver(closeProgressReceiver,
                                                new IntentFilter(LocationUpdatesService.ACTION_BROADCAST_PAUSED_LOCATION_SAVED));

                                        Intent intentResume = new Intent();
                                        intentResume.setAction("com.nsl.beejtantra.resume.location1");
                                        (PlanerOneActivity.this).sendBroadcast(intentResume);

                                        break;

                                }

                            }
                        }).setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });

        AlertDialog alert = alertDialogBuilder.create();
        alert.show();
    }

    private class GetApiKey extends AsyncTask<Void, Void, String> {
        String jsonData;
        private String latestVersion;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            //bar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(Void... params) {

            //odb.delete(TABLE_DIVISION,null,null);

            try {
                OkHttpClient client = new OkHttpClient();


                Response responses = null;


                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");

                Request request = new Request.Builder()
                        .url(Constants.URL_NSL_MAIN + Constants.API_CURRENT_KEY)
                        .get()
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();

                //     try {

                responses = client.newCall(request).execute();
                jsonData = responses.body().string();
                System.out.println("!!!!!!!1" + jsonData);
                ////  } catch (IOException e) {
                //        e.printStackTrace();
                //     }


            } catch (Exception e) {
                e.printStackTrace();
                API_KEY = "AIzaSyAUI7iUTr4Zfrc4ZKYbjm64OWW3F5x0mB0";
                ACCURACY_SETTING = 15;
                DISTANCE_FOR_DIRECTION_CALL = 700;
            }
            if (jsonData != null) {
                try {

                    JSONObject object = new JSONObject(jsonData);
                    JSONObject objApp_version = object.getJSONObject("api_key");
                    API_KEY = objApp_version.getString("api_name");
                    ACCURACY_SETTING = Float.parseFloat(objApp_version.getString("accuracy"));
                    DISTANCE_FOR_DIRECTION_CALL = Float.parseFloat(objApp_version.getString("direction_distance_setting"));


                } catch (Exception e) {
                    API_KEY = "AIzaSyAUI7iUTr4Zfrc4ZKYbjm64OWW3F5x0mB0";
                    ACCURACY_SETTING = 15;
                    DISTANCE_FOR_DIRECTION_CALL = 700;
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                //  Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
            }
            return jsonData;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            locationRoadSnaps.clear();
            listRoadSnap.clear();
            listForSnapRoad.clear();
            finalLatLngList.clear();
           /* snapRoadCallsSuccess = 0;
            snapRoadCallsAll = 0;
            snapRoadCallsFailure = 0;
            directionServiceCallsSuccess = 0;
            directionServiceCallsAll = 0;
            directionServiceCallsFailure = 0;*/

            if (Common.haveInternet(PlanerOneActivity.this)) {
                // Common.disableClickEvent(fab, true);
                displayGoogleLocationSettingPage(PlanerOneActivity.this, REQUEST_CHECK_SETTINGS);
            } else {
                Toast.makeText(PlanerOneActivity.this, "No Internet Connection ! \n Please Try again", Toast.LENGTH_SHORT).show();
            }
        }
    }


    class Aysc_PlanerScheduleAndPush extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            preparePlanerScheduleAndPush();
            return null;
        }
    }

    class Aysc_PlanerEventAndPush extends AsyncTask<Void, Void, Void> {
        @Override
        protected Void doInBackground(Void... voids) {
            preparePlanerEventAndPush();
            return null;
        }

    }

    private void preparePlanerScheduleAndPush() {
        String jsonData = null;
        String selectQuery = "SELECT DISTINCT "
                + KEY_EMP_CONCERN_PERSON_NAME + ","
                + KEY_EMP_VISIT_PLAN_TYPE + ","
                + KEY_EMP_STATUS + ","
                + KEY_EMP_PLAN_DATE_TIME + ","
                + KEY_EMP_PURPOSE_VISIT_IDS + ","
                + KEY_EMP_TYPE + ","
                + KEY_EMP_GEO_TRACKING_ID + ","
                + KEY_EMP_VISIT_USER_ID + ","
                + KEY_EMP_VISIT_CUSTOMER_ID + ","
                + KEY_EMP_MOBILE + ","
                + KEY_EMP_VILLAGE + ","
                + KEY_EMP_LOCATION_ADDRESS + ","
                + KEY_EMP_FEILD_AREA + ","
                + KEY_EMP_CHECK_IN_TIME + ","
                + KEY_EMP_COMMENTS + ","
                + KEY_EMP_APPROVAL_STATUS + ","
                + KEY_EMP_EVENT_END_DATE + ","
                + KEY_EMP_EVENT_PURPOSE + ","
                + KEY_EMP_VISIT_MASTER_ID + ","
                + KEY_EMP_EVENT_VENUE + ","
                + KEY_EMP_EVENT_PARTICIPANTS + ","
                + KEY_EMP_FFM_ID + ","
                + KEY_EMP_CREATED_BY + ","
                + KEY_EMP_UPDATED_BY + ","
                + KEY_EMP_CREATED_DATETIME + ","
                + KEY_EMP_UPDATE_DATETIME + ","
                + KEY_EMP_EVENT_NAME + ","
                + KEY_EMP_VISIT_ID
                + " FROM " + TABLE_EMPLOYEE_VISIT_MANAGEMENT + "  where " + KEY_EMP_VISIT_USER_ID + " in (" + team + ") and " + KEY_EMP_FFM_ID + " = 0" + " and " + KEY_EMP_TYPE + "= 1";


        Log.e("selectQueryPlaner", selectQuery);
        sdbw = db.getWritableDatabase();
        Cursor cursor = null;
        try {


            cursor = sdbw.rawQuery(selectQuery, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {

                    Log.e(" +++ Values +++ ", "type :" + cursor.getString(5) + " : " + cursor.getString(6) + " : " + cursor.getString(21) + ": checkintime" + cursor.getString(13) + ": sqlite id" + cursor.getString(27) + "approvalstatus" + cursor.getString(16));

                    try {

                        OkHttpClient client = new OkHttpClient();
                        /*For passing parameters*/
                        RequestBody formBody = new FormEncodingBuilder()
                                .add("user_id", cursor.getString(7))
                                .add("type", cursor.getString(5))
                                .add("geo_tracking_id", cursor.getString(6))
                                .add("customer_id", cursor.getString(8))
                                .add("visit_plan_type", cursor.getString(1))
                                .add("purpose_visit", cursor.getString(4))
                                .add("plan_date_time", cursor.getString(3))
                                .add("concern_person_name", cursor.getString(0))
                                .add("mobile", cursor.getString(9))
                                .add("village", cursor.getString(10))
                                .add("location_address", cursor.getString(11))
                                .add("field_area", cursor.getString(12))
                                .add("id", cursor.getString(27))
                                .add("comments", cursor.getString(14))
                                .add("status", cursor.getString(15))
                                .add("event_name", cursor.getString(26))
                                .add("event_end_date", cursor.getString(16))
                                .add("event_purpose", cursor.getString(17))
                                .add("event_venue", cursor.getString(19))
                                .add("event_participants", cursor.getString(20))
                                .add("created_by", cursor.getString(22))
                                .add("updated_by", cursor.getString(23))
                                .build();

                        Response responses = null;


                /*MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType,
                        "type=check_in_lat_lon&visit_type=1&user_id=7&latlon=17.4411%2C78.3911&visit_date=2016-12-05%2C18%3A27%3A30&check_in_time=18%3A27%3A30&tracking_id=0");*/
                        Request request = new Request.Builder()
                                .url(Constants.URL_INSERTING_EMP_VISIT_MANAGEMENT)
                                .post(formBody)
                                .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                                .addHeader("content-type", "application/x-www-form-urlencoded")
                                .addHeader("cache-control", "no-cache")
                                .build();


                        try {
                            responses = client.newCall(request).execute();
                            jsonData = responses.body().string();
                            System.out.println("!!!!!!!1 Planner inserting" + jsonData);
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    if (jsonData != null) {
                        JSONArray jsonarray;
                        try {
                            JSONObject jsonobject = new JSONObject(jsonData);
                            String status = jsonobject.getString("status");
                            if (status.equalsIgnoreCase("success")) {


                                String sqliteid = jsonobject.getString("sqlite");
                                String ffmid = jsonobject.getString("ffm_id");

                                //   Toast.makeText(getActivity(), "sqlite id and ffm id " + sqliteid + " , " + ffmid, Toast.LENGTH_SHORT).show();

                                SQLiteDatabase sql = db.getWritableDatabase();
                                // updatecomplaints
                                String updatequery = "UPDATE " + TABLE_EMPLOYEE_VISIT_MANAGEMENT + " SET " + KEY_EMP_FFM_ID + " = " + ffmid + " WHERE " + KEY_EMP_VISIT_ID + " = " + sqliteid;
                                sql.execSQL(updatequery);

                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        Log.e("ServiceHandler", "Couldn't get any data from the url");
                        // Toast.makeText(getApplicationContext(),"No data found",Toast.LENGTH_LONG).show();
                    }


                } while (cursor.moveToNext());
            } else {
                Log.d("LOG", "returned null!");

            }

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Common.closeCursor(cursor);
        }
    }


    private void preparePlanerEventAndPush() {
        String jsonData = null;
        String selectQuery = "SELECT DISTINCT "
                + KEY_EMP_CONCERN_PERSON_NAME + ","
                + KEY_EMP_VISIT_PLAN_TYPE + ","
                + KEY_EMP_STATUS + ","
                + KEY_EMP_PLAN_DATE_TIME + ","
                + KEY_EMP_PURPOSE_VISIT_IDS + ","
                + KEY_EMP_TYPE + ","
                + KEY_EMP_GEO_TRACKING_ID + ","
                + KEY_EMP_VISIT_USER_ID + ","
                + KEY_EMP_VISIT_CUSTOMER_ID + ","
                + KEY_EMP_MOBILE + ","
                + KEY_EMP_VILLAGE + ","
                + KEY_EMP_LOCATION_ADDRESS + ","
                + KEY_EMP_FEILD_AREA + ","
                + KEY_EMP_CHECK_IN_TIME + ","
                + KEY_EMP_COMMENTS + ","
                + KEY_EMP_APPROVAL_STATUS + ","
                + KEY_EMP_EVENT_END_DATE + ","
                + KEY_EMP_EVENT_PURPOSE + ","
                + KEY_EMP_VISIT_MASTER_ID + ","
                + KEY_EMP_EVENT_VENUE + ","
                + KEY_EMP_EVENT_PARTICIPANTS + ","
                + KEY_EMP_FFM_ID + ","
                + KEY_EMP_CREATED_BY + ","
                + KEY_EMP_UPDATED_BY + ","
                + KEY_EMP_CREATED_DATETIME + ","
                + KEY_EMP_UPDATE_DATETIME + ","
                + KEY_EMP_EVENT_NAME + ","
                + KEY_EMP_VISIT_ID
                + " FROM " + TABLE_EMPLOYEE_VISIT_MANAGEMENT + "  where " + KEY_EMP_VISIT_USER_ID + "=" + sharedpreferences.getString("userId", null) + " and " + KEY_EMP_FFM_ID + "=0" + " and " + KEY_EMP_TYPE + "=2";


        Log.e("selectQuery", selectQuery);
        Cursor cursor = null;
        try {
            sdbw = db.getWritableDatabase();
            cursor = sdbw.rawQuery(selectQuery, null);
            if (cursor != null && cursor.moveToFirst()) {
                do {
                    //Log.e(" +++ Values +++ ", "type :" + cursor.getString(5)+"concern person name :" + cursor.getString(0) + " : " + cursor.getString(6) + " : " + cursor.getString(21) + ": checkintime" + cursor.getString(13) + ": sqlite id" + cursor.getString(27));
                    Log.e(" +++ Values +++ ", "concern person name :" + cursor.getString(0) + "type :" + cursor.getString(5));

                    try {


                        OkHttpClient client = new OkHttpClient();
                        /*For passing parameters*/
                        RequestBody formBody = new FormEncodingBuilder()
                                .add("user_id", cursor.getString(7))
                                .add("type", cursor.getString(5))
                                .add("geo_tracking_id", cursor.getString(6))
                                .add("customer_id", cursor.getString(8))
                                .add("visit_plan_type", cursor.getString(1))
                                .add("purpose_visit", cursor.getString(4))
                                .add("plan_date_time", cursor.getString(3))
                                .add("concern_person_name", "" + cursor.getString(0))
                                .add("mobile", cursor.getString(9))
                                .add("village", cursor.getString(10))
                                .add("location_address", cursor.getString(11))
                                .add("field_area", cursor.getString(12))
                                .add("id", cursor.getString(27))
                                .add("comments", cursor.getString(14))
                                .add("status", cursor.getString(2))
                                .add("approval_status", cursor.getString(15))
                                .add("event_name", cursor.getString(26))
                                .add("event_end_date", cursor.getString(16))
                                .add("event_purpose", cursor.getString(17))
                                .add("event_venue", cursor.getString(19))
                                .add("event_participants", cursor.getString(20))
                                .add("created_by", cursor.getString(21))
                                .add("updated_by", cursor.getString(22))
                                .build();

                        Response responses = null;


                /*MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType,
                        "type=check_in_lat_lon&visit_type=1&user_id=7&latlon=17.4411%2C78.3911&visit_date=2016-12-05%2C18%3A27%3A30&check_in_time=18%3A27%3A30&tracking_id=0");*/
                        Request request = new Request.Builder()
                                .url(Constants.URL_INSERTING_EMP_VISIT_MANAGEMENT)
                                .post(formBody)
                                .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                                .addHeader("content-type", "application/x-www-form-urlencoded")
                                .addHeader("cache-control", "no-cache")
                                .build();


                        try {
                            responses = client.newCall(request).execute();
                            jsonData = responses.body().string();
                            System.out.println("!!!!!!!1 Planner inserting" + jsonData);

                            if (jsonData != null) {
                                JSONArray jsonarray;
                                try {
                                    JSONObject jsonobject = new JSONObject(jsonData);
                                    String status = jsonobject.getString("status");
                                    if (status.equalsIgnoreCase("success")) {


                                        int sqliteid = jsonobject.getInt("sqlite");
                                        int ffmid = jsonobject.getInt("ffm_id");

                                        //   Toast.makeText(getActivity(), "sqlite id and ffm id " + sqliteid + " , " + ffmid, Toast.LENGTH_SHORT).show();

                                        SQLiteDatabase sql = db.getWritableDatabase();
                                        // updatecomplaints
                                        String updatequery = "UPDATE " + TABLE_EMPLOYEE_VISIT_MANAGEMENT + " SET " + KEY_EMP_FFM_ID + " = " + ffmid + " WHERE " + KEY_EMP_VISIT_ID + " = " + sqliteid;
                                        sql.execSQL(updatequery);

                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                                }

                            } else {
                                Log.e("ServiceHandler", "Couldn't get any data from the url");
                                // Toast.makeText(getApplicationContext(),"No data found",Toast.LENGTH_LONG).show();
                            }
                        } catch (IOException e) {
                            e.printStackTrace();
                        }


                    } catch (Exception e) {
                        e.printStackTrace();
                    }


                } while (cursor.moveToNext());
            } else Log.d("LOG", "returned null!");
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            Common.closeCursor(cursor);
        }
        //  pushPlanerScheduleThread();
    }

/*public static void setAutoCheckOutAlarm(Context context){
    Log.d("LOG", "setted alarm");
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.HOUR_OF_DAY, 11);
    calendar.set(Calendar.MINUTE, 58);
    calendar.set(Calendar.SECOND, 0);
    Intent intent1 = new Intent(context, AlarmReceiver.class);
    intent1.setAction("com.nsl.intent.action.ALARM");
    PendingIntent pendingIntent = PendingIntent.getBroadcast(context, 0,intent1, PendingIntent.FLAG_UPDATE_CURRENT);
    AlarmManager am = (AlarmManager) context.getSystemService(context.ALARM_SERVICE);
    am.setRepeating(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), AlarmManager.INTERVAL_DAY, pendingIntent);
}*/


    public void startKM(final String label) {
       // Toast.makeText(getApplicationContext(),"njk",Toast.LENGTH_LONG).show();
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        LayoutInflater inflater = LayoutInflater.from(this);
        final View dialogView = inflater.inflate(R.layout.checkin_out_popup, null);
        builder.setView(dialogView);
        alertDialog = builder.create();
        alertDialog.setCancelable(false);
        alertDialog.show();
        TextView banner = dialogView.findViewById(R.id.banner_title);
        starting_kms_img = dialogView.findViewById(R.id.image);
        ImageView image_close = dialogView.findViewById(R.id.image_close);
        et_meter_reading = (EditText) dialogView.findViewById(R.id.et_meter_reading);
        et_comment = (EditText) dialogView.findViewById(R.id.et_comment);
        submit = (TextView) dialogView.findViewById(R.id.submit);
        tv_remove_image = (TextView) dialogView.findViewById(R.id.tv_remove_image);
        tv_hint = (TextView) dialogView.findViewById(R.id.tv_hint);
        tv_comment_hint = (TextView) dialogView.findViewById(R.id.tv_comment_hint);
        tv_previous_reading = (TextView) dialogView.findViewById(R.id.tv_previous_reading);
        final LinearLayout ll_otherviews = (LinearLayout) dialogView.findViewById(R.id.ll_otherviews);
        banner.setText(label);

//        String vehicle_type = Common.getDataFromSP(PlanerOneActivity.this, "VEHICLE_TYPE");
        Geo_Tracking_POJO trackingDataByDate = db.getGeoTrackingDataByDate(dateFormat.format(myDate), Common.getUserIdFromSP(PlanerOneActivity.this));
        String vehicle_type = null;
        if (trackingDataByDate == null)
            trackingDataByDate = db.getLastGeoRecord(Common.getUserIdFromSP(PlanerOneActivity.this));
        if (trackingDataByDate != null)
            vehicle_type = trackingDataByDate.getVehicle_type();


        try {
            if (vehicle_type == null) {
                ll_otherviews.setVisibility(View.GONE);
            } else {
                Common.saveDataInSP(PlanerOneActivity.this, "VEHICLE_TYPE", vehicle_type);
                ll_otherviews.setVisibility(View.VISIBLE);
            }

            previousReading = db.getPreviousMeterReading(vehicle_type, Common.getUserIdFromSP(PlanerOneActivity.this));
            tv_previous_reading.setText("Previous reading: " + Math.round(previousReading));
        } catch (Exception e) {
            e.printStackTrace();
        }
        updateView(vehicle_type, isSignedIn);
        MultiLineRadioGroup mMultiLineRadioGroup = (MultiLineRadioGroup) dialogView.findViewById(R.id.main_activity_multi_line_radio_group);
        mMultiLineRadioGroup.addButtons("2 Wheeler", "BUS", "4 Wheeler", "Car Hire", "With Other Person", "Other");
        mMultiLineRadioGroup.check(vehicle_type);
        if (isSignedIn && vehicle_type != null) {
            Common.Log.i("Count " + mMultiLineRadioGroup.getChildCount() + " R " + mMultiLineRadioGroup.getRadioButtonCount());
            for (int i = 0; i < mMultiLineRadioGroup.getRadioButtonCount(); i++) {
                mMultiLineRadioGroup.getRadioButtonAt(i).setEnabled(false);
                mMultiLineRadioGroup.getRadioButtonAt(i).setClickable(false);
            }
            // et_comment.setEnabled(false);
        }
        mMultiLineRadioGroup.setOnCheckedChangeListener(new MultiLineRadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(ViewGroup group, RadioButton button) {
                ll_otherviews.setVisibility(View.VISIBLE);
                Toast.makeText(PlanerOneActivity.this,
                        button.getText() + " was clicked",
                        Toast.LENGTH_SHORT).show();
                Common.saveDataInSP(PlanerOneActivity.this, "VEHICLE_TYPE", String.valueOf(button.getText()));
                try {
                    previousReading = db.getPreviousMeterReading(button.getText().toString(), Common.getUserIdFromSP(PlanerOneActivity.this));
                    tv_previous_reading.setText("Previous reading: " + Math.round(previousReading));
                } catch (Exception e) {
                    e.printStackTrace();
                }
                updateView(String.valueOf(button.getText()), isSignedIn);
            }


        });

        tv_remove_image.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (imageUri != null) {
                    new File(imageUri.getPath()).delete();
                    bool_1 = true;
                }
                deleteFile();
                bool_1 = true;
                Picasso.with(PlanerOneActivity.this).load(R.drawable.camera_ic).into(starting_kms_img);
            }
        });
        image_close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.dismissAlertDialog(alertDialog);
            }
        });
        starting_kms_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (PermissionUtil.shouldAskPermission()) {
                    if (checkSelfPermission(Manifest.permission.CAMERA)
                            != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(PlanerOneActivity.this, "Please Allow Camera permission", Toast.LENGTH_SHORT).show();
                        requestPermissions(new String[]{Manifest.permission.CAMERA},
                                REQUEST_CHECK_CAMERA);
                        return;
                    } else if (checkSelfPermission(Manifest.permission.WRITE_EXTERNAL_STORAGE)
                            != PackageManager.PERMISSION_GRANTED) {
                        Toast.makeText(PlanerOneActivity.this, "Please Allow storage permission", Toast.LENGTH_SHORT).show();
                        requestPermissions(new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE},
                                WRITE_REQUEST_CODE);
                        return;
                    }
                }
                if (bool_1) {
                    try {
                        values = new ContentValues();
                        values.put(MediaStore.Images.Media.TITLE, "Planer One");
                        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
                        imageUri = getContentResolver().insert(
                                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
                        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
                        startActivityForResult(intent, CAMERA_REQUEST_START);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                } else {
                    Toast.makeText(PlanerOneActivity.this, "You have already taken picture", Toast.LENGTH_LONG).show();
                }

            }
        });
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Common.haveInternet(PlanerOneActivity.this)) {
                    Toast.makeText(PlanerOneActivity.this, "No Internet Connection ! \n Please Try again", Toast.LENGTH_SHORT).show();
                    return;
                }

                String vehicle_type = Common.getDataFromSP(PlanerOneActivity.this, "VEHICLE_TYPE");
                Log.d("vehicle_type", ": " + vehicle_type);
                if (vehicle_type != null) {

                   /* if (et_comment.getVisibility() == View.VISIBLE && (et_comment.getText().toString() == null || et_comment.getText().toString().length() < 3)) {
                        Toast.makeText(PlanerOneActivity.this, "Please enter comment", Toast.LENGTH_SHORT).show();
                        return;
                    } else*/
                    if (et_meter_reading.getVisibility() == View.VISIBLE && (et_meter_reading.getText().toString() == null || et_meter_reading.getText().toString().trim().length() < 1)) {
                        Toast.makeText(PlanerOneActivity.this, "Please enter meter reading", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (et_meter_reading.getVisibility() == View.VISIBLE && (!Common.isNumeric(et_meter_reading.getText().toString().trim()) || (Double.parseDouble(et_meter_reading.getText().toString().trim()) - previousReading < 0))) {
                        Toast.makeText(PlanerOneActivity.this, "Meter reading should not be less than previous reading", Toast.LENGTH_LONG).show();
                        return;
                    } else if (et_comment.getVisibility() == View.VISIBLE && vehicle_type.equalsIgnoreCase("With Other Person") && (et_comment.getText() == null || et_comment.getText().toString() == null || et_comment.getText().toString().trim() == null || et_comment.getText().toString().trim().length() < 1)) {
                        Toast.makeText(PlanerOneActivity.this, "Please enter Car Owner Employee ID.", Toast.LENGTH_SHORT).show();
                        return;
                    } else if (starting_kms_img.getVisibility() == View.VISIBLE && (outputFileKm == null || outputFileKm.length() == 0)) {
                        Toast.makeText(PlanerOneActivity.this, "Please upload image", Toast.LENGTH_SHORT).show();
                        return;
                    } else {
                        Toast.makeText(PlanerOneActivity.this, "Success", Toast.LENGTH_SHORT).show();
                        meter_reading_checkin_text = et_meter_reading.getText().toString().trim();
                        checkInComment = et_comment.getText().toString().trim();
                        if (label.equalsIgnoreCase(Constants.CHECK_IN)) {
                            Toast.makeText(getApplicationContext(),"2",Toast.LENGTH_LONG).show();
                            locationRoadSnaps.clear();
                            listRoadSnap.clear();
                            listForSnapRoad.clear();
                            finalLatLngList.clear();
//                                            snapRoadCallsSuccess = 0;
//                                            directionServiceCallsSuccess = 0;
                            progressDialogInOut = new ProgressDialog(PlanerOneActivity.this);
                            progressDialogInOut.setMessage("Please wait...");
                            progressDialogInOut.show();
                            progressDialogInOut.setCancelable(false);

                            // Common.disableClickEvent(fab, true);
                            displayGoogleLocationSettingPage(PlanerOneActivity.this, REQUEST_CHECK_SETTINGS);

                        } else {
                            Toast.makeText(getApplicationContext(),"1",Toast.LENGTH_LONG).show();
                            progressDialogInOut = new ProgressDialog(PlanerOneActivity.this);
                            progressDialogInOut.setMessage("Please wait...");
                            progressDialogInOut.show();
                            progressDialogInOut.setCancelable(false);
                            new GetApiKey().execute();

                        }
                        Common.dismissAlertDialog(alertDialog);
                    }
                } else {
                    Toast.makeText(PlanerOneActivity.this, "Please select vehicle type", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    public void updateView(String type, boolean checkInStatus) {
        if (!checkInStatus) {
            if (type != null && type.equalsIgnoreCase("2 Wheeler")) {
                et_comment.setVisibility(View.GONE);
                tv_comment_hint.setVisibility(View.GONE);
                et_meter_reading.setVisibility(View.VISIBLE);
                tv_remove_image.setVisibility(View.VISIBLE);
                starting_kms_img.setVisibility(View.VISIBLE);
                tv_previous_reading.setVisibility(View.VISIBLE);
                tv_hint.setVisibility(View.VISIBLE);
                et_comment.setVisibility(View.GONE);
            } else if (type != null && type.equalsIgnoreCase("BUS")) {
                et_comment.setVisibility(View.VISIBLE);
                et_meter_reading.setVisibility(View.GONE);
                tv_remove_image.setVisibility(View.GONE);
                starting_kms_img.setVisibility(View.GONE);
                tv_previous_reading.setVisibility(View.GONE);
                tv_hint.setVisibility(View.GONE);
                tv_comment_hint.setText("Comment ");
                et_comment.setHint("Comment");
                et_comment.setInputType(InputType.TYPE_CLASS_TEXT);
                tv_comment_hint.setVisibility(View.VISIBLE);
            } else if (type != null && type.equalsIgnoreCase("4 Wheeler")) {
                et_comment.setVisibility(View.GONE);
                tv_comment_hint.setVisibility(View.GONE);
                et_meter_reading.setVisibility(View.VISIBLE);
                tv_remove_image.setVisibility(View.VISIBLE);
                starting_kms_img.setVisibility(View.VISIBLE);
                tv_previous_reading.setVisibility(View.VISIBLE);
                tv_hint.setVisibility(View.VISIBLE);
            } else if (type != null && type.equalsIgnoreCase("Car Hire")) {
                et_comment.setVisibility(View.VISIBLE);
                et_meter_reading.setVisibility(View.GONE);
                tv_remove_image.setVisibility(View.GONE);
                starting_kms_img.setVisibility(View.GONE);
                tv_previous_reading.setVisibility(View.GONE);
                tv_hint.setVisibility(View.GONE);
                tv_comment_hint.setText("Comment ");
                et_comment.setHint("Comment");
                et_comment.setInputType(InputType.TYPE_CLASS_TEXT);
                tv_comment_hint.setVisibility(View.VISIBLE);
            } else if (type != null && type.equalsIgnoreCase("With Other Person")) {
                et_comment.setVisibility(View.VISIBLE);
                et_meter_reading.setVisibility(View.GONE);
                tv_remove_image.setVisibility(View.GONE);
                starting_kms_img.setVisibility(View.GONE);
                tv_previous_reading.setVisibility(View.GONE);
                tv_hint.setVisibility(View.GONE);
                tv_comment_hint.setText("Employee ID ");
                et_comment.setHint("*Vehicle Owner Employee ID");
                et_comment.setInputType(InputType.TYPE_CLASS_NUMBER);
                tv_comment_hint.setVisibility(View.VISIBLE);
            } else if (type != null && type.equalsIgnoreCase("Other")) {
                et_comment.setVisibility(View.VISIBLE);
                et_meter_reading.setVisibility(View.GONE);
                tv_remove_image.setVisibility(View.GONE);
                starting_kms_img.setVisibility(View.GONE);
                tv_hint.setVisibility(View.GONE);
                tv_comment_hint.setText("Comment ");
                et_comment.setHint("Comment");
                et_comment.setInputType(InputType.TYPE_CLASS_TEXT);
                tv_comment_hint.setVisibility(View.VISIBLE);
                tv_previous_reading.setVisibility(View.GONE);
            }
        } else {
            if (type != null && type.equalsIgnoreCase("2 Wheeler")) {

                et_meter_reading.setVisibility(View.VISIBLE);
                tv_remove_image.setVisibility(View.VISIBLE);
                starting_kms_img.setVisibility(View.VISIBLE);
                tv_previous_reading.setVisibility(View.VISIBLE);
                tv_hint.setVisibility(View.VISIBLE);
                et_comment.setVisibility(View.GONE);
                tv_comment_hint.setText("Personal Uses KM ");
                et_comment.setHint("Personal Uses KM");
                et_comment.setInputType(InputType.TYPE_CLASS_NUMBER);
                tv_comment_hint.setVisibility(View.VISIBLE);
                et_comment.setVisibility(View.VISIBLE);
            } else if (type != null && type.equalsIgnoreCase("BUS")) {
                et_comment.setVisibility(View.VISIBLE);
                et_meter_reading.setVisibility(View.GONE);
                tv_remove_image.setVisibility(View.GONE);
                starting_kms_img.setVisibility(View.GONE);
                tv_previous_reading.setVisibility(View.GONE);
                tv_hint.setVisibility(View.GONE);
                tv_comment_hint.setText("Comment ");
                et_comment.setHint("Comment");
                et_comment.setInputType(InputType.TYPE_CLASS_TEXT);
                tv_comment_hint.setVisibility(View.VISIBLE);
            } else if (type != null && type.equalsIgnoreCase("4 Wheeler")) {

                tv_comment_hint.setVisibility(View.GONE);
                et_meter_reading.setVisibility(View.VISIBLE);
                tv_remove_image.setVisibility(View.VISIBLE);
                starting_kms_img.setVisibility(View.VISIBLE);
                tv_previous_reading.setVisibility(View.VISIBLE);
                tv_hint.setVisibility(View.VISIBLE);
                tv_comment_hint.setText("Personal Uses KM ");
                et_comment.setHint("Personal Uses KM");
                et_comment.setInputType(InputType.TYPE_CLASS_NUMBER);
                tv_comment_hint.setVisibility(View.VISIBLE);
                et_comment.setVisibility(View.VISIBLE);
            } else if (type != null && type.equalsIgnoreCase("Car Hire")) {
                et_comment.setVisibility(View.VISIBLE);
                et_meter_reading.setVisibility(View.GONE);
                tv_remove_image.setVisibility(View.GONE);
                starting_kms_img.setVisibility(View.GONE);
                tv_previous_reading.setVisibility(View.GONE);
                tv_hint.setVisibility(View.GONE);
                tv_comment_hint.setText("Comment ");
                et_comment.setHint("Comment");
                et_comment.setInputType(InputType.TYPE_CLASS_TEXT);
                tv_comment_hint.setVisibility(View.VISIBLE);
            } else if (type != null && type.equalsIgnoreCase("With Other Person")) {
                et_comment.setVisibility(View.GONE);
                et_meter_reading.setVisibility(View.GONE);
                tv_remove_image.setVisibility(View.GONE);
                starting_kms_img.setVisibility(View.GONE);
                tv_previous_reading.setVisibility(View.GONE);
                tv_hint.setVisibility(View.GONE);
               /* tv_comment_hint.setText("Employee ID ");
                et_comment.setHint("Vehicle Owner Employee ID");
                et_comment.setInputType(InputType.TYPE_CLASS_TEXT);*/
                tv_comment_hint.setVisibility(View.GONE);
            } else if (type != null && type.equalsIgnoreCase("Other")) {
                et_comment.setVisibility(View.VISIBLE);
                et_meter_reading.setVisibility(View.GONE);
                tv_remove_image.setVisibility(View.GONE);
                starting_kms_img.setVisibility(View.GONE);
                tv_previous_reading.setVisibility(View.GONE);
                tv_hint.setVisibility(View.GONE);
                tv_comment_hint.setText("Comment ");
                et_comment.setHint("Comment");
                et_comment.setInputType(InputType.TYPE_CLASS_TEXT);
                tv_comment_hint.setVisibility(View.VISIBLE);
            }
        }

    }

    public static class Async_Routepath extends AsyncTask<Void, Void, String> {

        private final Context context;
        private final String trackingId;
        private final String pause;
        private final String resume;
        private String path;
        private String jsonData;

        public Async_Routepath(Context context, String id, String path, String pause, String resume) {
            this.context = context;
            this.trackingId = id;
            this.path = path;
            this.pause = pause;
            this.resume = resume;
        }

        protected String doInBackground(Void... params) {

            try {

                OkHttpClient client = new OkHttpClient();
                /*For passing parameters*/
                RequestBody formBody = new FormEncodingBuilder()
                        .add("latlon", isStringNull(path))
                        .add("tracking_id", trackingId)
                        .add("pause", isStringNull(pause))
                        .add("resume", isStringNull(resume))
                        .build();

                Response responses = null;

                /*MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType,
                        "type=check_in_lat_lon&visit_type=1&user_id=7&latlon=17.4411%2C78.3911&visit_date=2016-12-05%2C18%3A27%3A30&check_in_time=18%3A27%3A30&tracking_id=0");*/
                Request request = new Request.Builder()
                        .url(Constants.URL_NSL_MAIN + Constants.URL_ROUTEPATH_UPDATE_INTERVAL)
                        .post(formBody)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();


                try {
                    responses = client.newCall(request).execute();
                    jsonData = responses.body().string();
                    System.out.println("!!!!!!!1 Planner : " + jsonData + " a  " + path.length());
                    if (jsonData != null) {
                        try {
                            JSONObject jsonObject = new JSONObject(jsonData);
                            if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                                ContentValues contentValues = new ContentValues();
                                contentValues.put(SYNC_STATUS, 1);
                                new DatabaseHandler(context).updateTable(TABLE_GEO_TRACKING, contentValues, KEY_TABLE_GEO_TRACKING_FFMID, jsonObject.getString("tracking_id"));

                            }
                        } catch (Exception e) {
                            e.printStackTrace();
                        }
                    }

                } catch (IOException e) {
                    e.printStackTrace();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return jsonData;
        }


    }

    public class AsyncCheckoutAcknowledge extends AsyncTask<Void, Void, String> {

        private final Context context;
        private final String trackingId;
        private String path;
        private String jsonData;

        public AsyncCheckoutAcknowledge(Context context, String id) {
            this.context = context;
            this.trackingId = id;
            // this.path = path;
        }

        protected String doInBackground(Void... params) {

            try {

                OkHttpClient client = new OkHttpClient();
                /*For passing parameters*/
                RequestBody formBody = new FormEncodingBuilder()
                        //.add("latlon", path)
                        .add("tracking_id", trackingId)
                        .build();

                Response responses = null;
                Log.d("AsyncCheckout", "AsyncCheckoutAcknowledge..");
                /*MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType,
                        "type=check_in_lat_lon&visit_type=1&user_id=7&latlon=17.4411%2C78.3911&visit_date=2016-12-05%2C18%3A27%3A30&check_in_time=18%3A27%3A30&tracking_id=0");*/
                Request request = new Request.Builder()
                        .url(Constants.URL_NSL_MAIN + Constants.URL_GEO_POLYLINE)
                        .post(formBody)
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
                        System.out.println("!!!!!!!1 URL_GEO_POLYLINE : " + jsonData);

                        if (jsonData != null) {
                            try {
                                //deleteFile();
                                // {"status":"success","msg":"successfully! done","tracking_id":"11723"}
                                JSONObject jsonObject = new JSONObject(jsonData);
                                if (jsonObject.getString("status").equalsIgnoreCase("success")) {
                                    String polyline = jsonObject.getString("polyline");
                                    String distance = jsonObject.getString("distance");
                                    String meter_reading_checkout_image = jsonObject.getString("meter_reading_checkout_image");
                                    String tracking_id = jsonObject.getString("tracking_id");
                                    String personal_uses_km = jsonObject.getString("personal_uses_km");
                                    SharedPreferences.Editor editor = sharedpreferences.edit();
                                    editor.putString("tracking_id", "");
                                    editor.putString("checkinlatlong", "");
                                    editor.putString("checkin", "false");
                                    editor.commit();
                                    isSignedIn = false;

                                    sdbw = db.getWritableDatabase();
                                    String updatequerys = "UPDATE " + TABLE_EMPLOYEE_VISIT_MANAGEMENT + " SET " + KEY_EMP_STATUS + " = '" + "4" + "' WHERE " + KEY_EMP_VISIT_USER_ID + " = " + userId + " and " + KEY_EMP_PLAN_DATE_TIME + " like '" + datefromcalander + "%'";
                                    sdbw.execSQL(updatequerys);

                                    String updatequery1 = "UPDATE " + TABLE_GEO_TRACKING + " SET " + KEY_TABLE_GEO_TRACKING_UPDATED_STATUS + " = 1 ," + KEY_TABLE_GEO_TRACKING_CHECK_OUT_LAT_LONG + " = '" + checkinlatlong + "'," + KEY_TABLE_GEO_TRACKING_CHECK_OUT_TIME + " = '" + strDate + "'," + KEY_TABLE_GEO_TRACKING_POLYLINE + " = '" + polyline + "'," + KEY_TABLE_GEO_TRACKING_DISTANCE + " = '" + distance + "'," + METER_READING_CHECKOUT_IMAGE + " = '" + meter_reading_checkout_image + "'," + PERSONAL_USES_KM + " = '" + personal_uses_km + "' WHERE " + KEY_TABLE_GEO_TRACKING_ID + " = " + tid;

                                    sdbw.execSQL(updatequery1);


                                }

                            } catch (Exception e) {
                                e.printStackTrace();
                            } finally {
                                if (sdbw.isOpen())
                                    sdbw.close();
                            }
                        }

                    }
                });


            } catch (Exception e) {
                e.printStackTrace();
            }

            return jsonData;
        }


    }

    public static String[] isCheckedIn(Context context) {
        String[] id = new String[4];

        Calendar c = Calendar.getInstance();
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String strDate = sdf.format(c.getTime());
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat(Constants.DateFormat.COMMON_DATE_FORMAT);
        String datefromcalander = df.format(c.getTime());
        Log.d("datesee",datefromcalander);
        String selectQueryss = "SELECT  " + KEY_TABLE_GEO_TRACKING_ID + "," + KEY_TABLE_GEO_TRACKING_ROUTE_PATH_LAT_LONG + "," + KEY_TABLE_GEO_TRACKING_FFMID + "," + KEY_TABLE_GEO_TRACKING_CHECK_OUT_TIME + "," + KEY_TABLE_GEO_TRACKING_CREATED_DATETIME + "," + PAUSE + "," + RESUME + " FROM " + TABLE_GEO_TRACKING + " where " + " visit_date like '" + datefromcalander + "%' and user_id ='" + Common.getUserIdFromSP(context) + "'" + " ORDER BY " + KEY_TABLE_GEO_TRACKING_ID + " DESC LIMIT 1 ";
        SQLiteDatabase sdbw1 = new DatabaseHandler(context).getWritableDatabase();
        try {

            Cursor ccc = sdbw1.rawQuery(selectQueryss, null);
            System.out.println("cursor count " + ccc.getCount() + "\n" + selectQueryss);
            if (ccc != null && ccc.moveToFirst()) {
                if ((ccc.getString(3) == null || ccc.getString(3).equalsIgnoreCase("") || ccc.getString(3).equalsIgnoreCase("null")) && (ccc.getString(4) != null && ccc.getString(4).length() > 5)) {
                    id[0] = ccc.getString(2);
                    id[1] = ccc.getString(1);
                    id[2] = ccc.getString(5);
                    id[3] = ccc.getString(6);
                    return id;
                }
            }
        } catch (Exception e) {

        } finally {
            if (sdbw1.isOpen())
                sdbw1.close();
            Common.Log.i("DB Closed: " + "finally called");
        }
        return null;
    }

    /*private void initSpeedDial(boolean addActionItems) {
        mSpeedDialView = findViewById(R.id.speedDial);

        if (addActionItems) {
            mSpeedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id.fab_no_label, R.drawable
                    .ic_link_white_24dp)
                    .create());

            Drawable drawable = AppCompatResources.getDrawable(MainActivity.this, R.drawable.ic_custom_color);
            FabWithLabelView fabWithLabelView = mSpeedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id
                    .fab_custom_color, drawable)
                    .setFabImageTintColor(ResourcesCompat.getColor(getResources(), R.color.inbox_primary, getTheme()))
                    .setLabel(R.string.label_custom_color)
                    .setLabelColor(Color.WHITE)
                    .setLabelBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.inbox_primary,
                            getTheme()))
                    .create());
            if (fabWithLabelView != null) {
                fabWithLabelView.setSpeedDialActionItem(fabWithLabelView.getSpeedDialActionItemBuilder()
                        .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.material_white_1000,
                                getTheme()))
                        .create());
            }

            mSpeedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id.fab_long_label, R.drawable
                    .ic_lorem_ipsum)
                    .setLabel("Lorem ipsum dolor sit amet, consectetur adipiscing elit, sed do eiusmod tempor " +
                            "incididunt ut labore et dolore magna aliqua.")
                    .create());

            drawable = AppCompatResources.getDrawable(MainActivity.this, R.drawable.ic_add_white_24dp);
            mSpeedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id.fab_add_action, drawable)
                    .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.material_green_500,
                            getTheme()))
                    .setLabel(R.string.label_add_action)
                    .setLabelBackgroundColor(Color.TRANSPARENT)
                    .create());

            mSpeedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id.fab_custom_theme, R.drawable
                    .ic_theme_white_24dp)
                    .setLabel(getString(R.string.label_custom_theme))
                    .setTheme(R.style.AppTheme_Purple)
                    .create());

        }
    }*/

    public void rapidFloating(int checkInStatus, int pauseResumeStatus) {
        /*RapidFloatingActionContentLabelList rfaContent = new RapidFloatingActionContentLabelList(this);
        rfaContent.setOnRapidFloatingActionContentLabelListListener(this);*/
        if (items != null && items.size() > 0) {
            for (int i = 0; i < items.size(); i++)
                mSpeedDialView.removeActionItem(items.get(i));
        }

        items = new ArrayList<>();
        items.clear();

        if (pauseResumeStatus == 1) {
           /* items.add(new RFACLabelItem<Integer>()
                    .setLabel("Pause")
                    // .setResId(R.drawable.ic_pause1)
                    .setDrawable(getResources().getDrawable(R.drawable.ic_pause1))
                    .setIconNormalColor(0xffd84315)
                    .setIconPressedColor(0xffbf360c)
                    .setLabelColor(0xFFFF0000)
                    .setLabelSizeSp(getResources().getDimensionPixelSize(R.dimen._8sdp))
                    .setWrapper(0)

            );*/
            items.add(new SpeedDialActionItem.Builder(R.id.fab_pause_action, R.drawable.ic_pause1)
                    .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.green,
                            getTheme()))
                    .setLabel("Pause")
                    .setTheme(R.style.AppTheme_Purple)
                    .setLabelBackgroundColor(Color.TRANSPARENT)
                    .create());
        } else if (pauseResumeStatus == 2) {
          /*  items.add(new RFACLabelItem<Integer>()
                            .setLabel("Resume")
//                        .setResId(R.mipmap.ico_test_c)
                            .setDrawable(getResources().getDrawable(R.drawable.ic_resume1))
                            .setIconNormalColor(0xff056f00)
                            .setIconPressedColor(0xff3e2723)
                            .setLabelColor(0xff056f00)
                            .setLabelSizeSp(getResources().getDimensionPixelSize(R.dimen._8sdp))
//                        .setLabelSizeSp(14)
//                        .setLabelBackgroundDrawable(RFABShape.generateCornerShapeDrawable(0xaa000000, RFABTextUtil.dip2px(this, 4)))
                            .setWrapper(0)
            );*/
            items.add(new SpeedDialActionItem.Builder(R.id.fab_resume_action, R.drawable.ic_resume1)
                    .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent,
                            getTheme()))
                    .setLabel("Resume")
                    .setLabelBackgroundColor(Color.TRANSPARENT)
                    .create());

        }
        if (checkInStatus == 1) {
           /* items.add(new RFACLabelItem<Integer>()
                    .setLabel(Constants.CHECK_IN)
                    .setDrawable(getResources().getDrawable(R.drawable.ic_checkin))
                    .setIconNormalColor(0xff056f00)
                    .setIconPressedColor(0xff0d5302)
                    .setLabelColor(0xff056f00)
                    .setLabelSizeSp(getResources().getDimensionPixelSize(R.dimen._8sdp))
                    .setWrapper(1)
            );*/

            items.add(new SpeedDialActionItem.Builder(R.id.fab_checkin, R.drawable.ic_checkin)
                    .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.green,
                            getTheme()))
                    .setLabel(Constants.CHECK_IN)
                    .setLabelBackgroundColor(Color.TRANSPARENT)
                    .create());

        } else if (checkInStatus == 2) {
           /* items.add(new RFACLabelItem<Integer>()
                    .setLabel(Constants.CHECKOUT)
                    .setDrawable(getResources().getDrawable(R.drawable.ic_checkout))
                    .setIconNormalColor(0xFFFF0000)
                    .setIconPressedColor(0xff0d5302)
                    .setLabelColor(0xFFFF0000)
                    .setLabelSizeSp(getResources().getDimensionPixelSize(R.dimen._8sdp))
                    .setWrapper(1)
            );*/

            items.add(new SpeedDialActionItem.Builder(R.id.fab_checkout, R.drawable.ic_checkout)
                    .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.colorAccent,
                            getTheme()))
                    .setLabel(Constants.CHECKOUT)
                    .setLabelBackgroundColor(Color.TRANSPARENT)
                    .create());
        }

        mSpeedDialView.addAllActionItems(items);

        //Set main action clicklistener.
        mSpeedDialView.setOnChangeListener(new SpeedDialView.OnChangeListener() {
            @Override
            public boolean onMainActionSelected() {
                showToast("Main action clicked!");
                return false; // True to keep the Speed Dial open
            }

            @Override
            public void onToggleChanged(boolean isOpen) {
                Log.d("Main", "Speed dial toggle state changed. Open = " + isOpen);
                String[] checked = isCheckedIn(PlanerOneActivity.this);
                if (checked != null && checked.length > 0 && checked[0].length() > 4) {
                    rfaButtonColor(R.color.colorAccent);
                } else {
                    rfaButtonColor(R.color.green);
                }

            }
        });

        mSpeedDialView.setOnActionSelectedListener(new SpeedDialView.OnActionSelectedListener() {
            @Override
            public boolean onActionSelected(SpeedDialActionItem actionItem) {
                try {
                    if (favouriteItem == null || favouriteItem.size() == 0) {
                        Toast.makeText(PlanerOneActivity.this, "Please add plan", Toast.LENGTH_SHORT).show();
                        mSpeedDialView.close();
                        return false;

                    }
                    //  Toast.makeText(this, "clicked label: " + position+" "+item.getLabel(), Toast.LENGTH_SHORT).show();
                    if (actionItem.getLabel(PlanerOneActivity.this).equalsIgnoreCase(Constants.CHECK_IN) || actionItem.getLabel(PlanerOneActivity.this).equalsIgnoreCase(Constants.CHECKOUT)) {
                        startKM(actionItem.getLabel(PlanerOneActivity.this));
                    } else
                        confirmationDailog(actionItem.getLabel(PlanerOneActivity.this));
                    mSpeedDialView.close();
                } catch (Exception e) {
                    e.printStackTrace();
                }

/*
                switch (actionItem.getId()) {
                    case R.id.fab_checkin:
                        showToast("No label action clicked!\nClosing with animation");
                        mSpeedDialView.close(); // To close the Speed Dial with animation
                        return true; // false will close it without animation
                    case R.id.fab_checkout:
                        showToast(actionItem.getLabel(PlanerOneActivity.this) + " clicked!\nClosing without animation.");
                        break;
                    case R.id.fab_pause_action:
                        showToast(actionItem.getLabel(PlanerOneActivity.this) + " clicked!\nClosing without animation.");
                        return false; // closes without animation (same as mSpeedDialView.close(false); return false;)
                    case R.id.fab_resume_action:
                        showToast(actionItem.getLabel(PlanerOneActivity.this) + " clicked!");
                        break;
                   */
/* case R.id.fab_add_action:
                        mSpeedDialView.addActionItem(new SpeedDialActionItem.Builder(R.id.fab_replace_action,
                                R.drawable.ic_replace_white_24dp)
                                .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color
                                                .material_orange_500,
                                        getTheme()))
                                .setLabel(getString(R.string.label_replace_action))
                                .create(), ADD_ACTION_POSITION);
                        break;
                    case R.id.fab_replace_action:
                        mSpeedDialView.replaceActionItem(new SpeedDialActionItem.Builder(R.id
                                .fab_remove_action,
                                R.drawable.ic_delete_white_24dp)
                                .setLabel(getString(R.string.label_remove_action))
                                .setFabBackgroundColor(ResourcesCompat.getColor(getResources(), R.color.inbox_accent,
                                        getTheme()))
                                .create(), ADD_ACTION_POSITION);
                        break;
                    case R.id.fab_remove_action:
                        mSpeedDialView.removeActionItemById(R.id.fab_remove_action);
                        break;*//*

                    default:
                        break;
                }
*/
                return true; // To keep the Speed Dial open
            }
        });

/*
        rfaContent
                .setItems(items)
                .setIconShadowRadius(RFABTextUtil.dip2px(this, 5))
                // .setIconShadowColor(0xff888888)
                .setIconShadowDy(RFABTextUtil.dip2px(this, 5))
        ;

        rfabHelper = new RapidFloatingActionHelper(
                this,
                rfaLayout,
                rfaButton,
                rfaContent
        ).build();*/
    }


    protected void showToast(String text) {
        if (mToast != null) {
            mToast.cancel();
        }
        mToast = Toast.makeText(getApplicationContext(), text, Toast.LENGTH_LONG);
        mToast.show();
    }
}
