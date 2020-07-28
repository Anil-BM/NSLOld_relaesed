package com.nsl.beejtantra;


import android.app.Activity;
import android.app.ProgressDialog;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.InputFilter;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.SimpleAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.google.gson.Gson;
import com.nsl.beejtantra.adapters.CropAdapter;
import com.nsl.beejtantra.adapters.CustomListAdapter;
import com.nsl.beejtantra.adapters.CustomListAdapter1;
import com.nsl.beejtantra.adapters.CustomSpinnerAdapter;
import com.nsl.beejtantra.commonutils.Common;
import com.nsl.beejtantra.commonutils.NetworkChangeListenerActivity;
import com.nsl.beejtantra.enums.PlannerEnum;
import com.nsl.beejtantra.geotagging.GeoTaggingMapActivity;
import com.nsl.beejtantra.pojo.DistributorsRetailerPojo;
import com.nsl.beejtantra.pojo.District;
import com.nsl.beejtantra.pojo.FarmerPojo;
import com.nsl.beejtantra.retailers.Retailer;
import com.nsl.beejtantra.view.MultiSelectionSpinner;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

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
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_FARMER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_FEILD_AREA;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_FFM_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_GEO_TRACKING_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_GSTIN_NO;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_LOCATION_ADDRESS;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_LOCATION_DISTRICT;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_LOCATION_TALUKA;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_LOCATION_VILLAGE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_MOBILE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_PLAN_DATE_TIME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_PURPOSE_VISIT;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_PURPOSE_VISIT_IDS;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_REGION_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_RETAILER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_STATUS;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_TYPE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_UPDATED_BY;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_UPDATE_DATETIME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_VILLAGE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_VISIT_CROP_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_VISIT_CUSTOMER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_VISIT_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_VISIT_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_VISIT_PLAN_TYPE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_EMP_VISIT_USER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CUSTOMER_CITY;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CUSTOMER_CODE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CUSTOMER_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CUSTOMER_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CUSTOMER_REGION_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_FARMER_PHONE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_RETAILER_GSTIN_NO;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_RETAILER_MOBILE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USERS_FIRST_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USERS_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USER_COMPANY_CUSTOMER_CUSTOMER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USER_COMPANY_CUSTOMER_STATUS;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USER_COMPANY_CUSTOMER_USER_ID;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_CUSTOMERS;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_EMPLOYEE_VISIT_MANAGEMENT;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_FARMERS;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_RETAILER;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_USERS;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_USER_COMPANY_CUSTOMER;

/**
 * A simple {@link Fragment} subclass.
 */
public class PlanScheduleFormActivity extends NetworkChangeListenerActivity {
    // JSON parser class

    ProgressDialog progressDialog;


    TextView tv_code, spin_crop;
    View line, line1, cropLine;
    Spinner spin_visittypes, spin_user;
    EditText et_time;
    AutoCompleteTextView et_address, et_name, et_district;

    EditText et_fieldarea, et_taluka, et_village, et_gstin, et_mobile, validate_mobile;
    TextView tv_fieldarea, tv_taluka, tv_village, tv_gstin, tv_mobile, tv_region, tv_name, tv_district;
    ImageView edit_iv, new_iv;
    RelativeLayout fieldsLayout, addressLayout;
    LinearLayout fieldAreaLayout, gstinLayout;
    Button btn_savebooing/*, btn_new*/;

    String sel_crop_id, cusRegionId, str_name, str_address, str_district, str_taluka, str_village, str_mobile, str_gstin, str_fieldarea, sel_product_id, sel_scheme_id, str_time, company_id, order_date, str_date;
    String village = "", location_address, field_area, check_in_time, comments = "", str_sel_visittypeid = "", str_sel_purposeid;
    int sel_customer_id, type, geo_tracking_id, mobile, status = 1, customer_id;
    MultiSelectionSpinner spin_lanuagesknown;
    private ArrayList<SelectedCities> arlist_users;
    private ArrayList<Filminstitutes> languagesList;
    ArrayList<String> adapter_users;
    final Context context = this;
    private LinearLayout ll_select;
    int sqliteid, ffmid;

    String jsonData;
    String str_qty;
    String str_amount;
    String userId;
    String sel_userId;
    int role;
    String team;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> cropids = new ArrayList<HashMap<String, String>>();
    List<Crops> cropItemsList = new ArrayList<>();

    private ArrayList<Users> userses;
    ArrayList<String> adapterusers;

    DateFormat dateFormat, orderdateFormat;
    Date myDate;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    TextView tvInvisibleError, text_purpose, tvInvisibleError1;
    DatabaseHandler db;
    SQLiteDatabase sql, sdbw, sdbr;
    private TextView textView_start_date;
    TextView spin_purpose;
    RelativeLayout regionLayout;
    private String str_sel_purposetypes;
    private String userName;
    String retailerId = "0";
    int serverFlag = 0;
    String farmerId = "0";
    private List<PlannerEnum> visitersEnumList;
    private List<Crops> cropsList, searchedCrops;
    private String visiterIds;
    private ProgressDialog progressDialog1;
    private boolean isDirectCustomers;
    private AutoCompleteTextView auto_tv;
    String customername;
    private Retailer retailer;
    private FarmerPojo farmer;
    private Handler handler;
    private MyRunnable myRunnable;
    private MyRunnable1 myRunnable1;
    private boolean onItemClick, onDistrictItemClick;
    private Spinner regionSpinner;
    private String regionId = "";
    private int approvalStatus;
    private String cropId = "";
    private RelativeLayout cropLayout;
    StringBuffer cropNames;
    StringBuffer cropIds = new StringBuffer();
    StringBuffer fieldAreas = new StringBuffer();
    private CropAdapter cropAdapter;
    private String districtId = "0";
    private OptionsAdapter optionsAdapter;
    private ArrayList<PurposeVisit> purposeList;
    private List<String> checkedList = new ArrayList<>();
    private int beforeTextChangedLength;

    private String blockCharacterSet = "'.~#^|$%&*!()@-=;";

    private InputFilter filter = new InputFilter() {

        @Override
        public CharSequence filter(CharSequence source, int start, int end, Spanned dest, int dstart, int dend) {

            if (source != null && blockCharacterSet.contains(("" + source))) {
                return "";
            }
            return null;
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_plan_schedule);
        db = new DatabaseHandler(this);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                finish();
            }
        });

        type = Integer.parseInt(getIntent().getExtras().getString("type"));
        str_date = getIntent().getExtras().getString("startDate");


        userId = sharedpreferences.getString("userId", "");
        team = sharedpreferences.getString("team", "");
        role = sharedpreferences.getInt(Constants.SharedPrefrancesKey.ROLE, 0);
        spin_visittypes = (Spinner) findViewById(R.id.spin_visittype);
        spin_crop = (TextView) findViewById(R.id.spin_crop);
        cropLayout = (RelativeLayout) findViewById(R.id.crop_layout);
        spin_purpose = (TextView) findViewById(R.id.spin_purpose);
        text_purpose = findViewById(R.id.text_purpose);
        spin_user = (Spinner) findViewById(R.id.spin_user);
        ll_select = (LinearLayout) findViewById(R.id.ll_select);
        cropLine = findViewById(R.id.crop_line);
        //spin_customers = (Spinner) findViewById(R.id.spin_customers);
        tvInvisibleError = (TextView) findViewById(R.id.tvInvisibleError);
        tvInvisibleError1 = findViewById(R.id.tvInvisibleError1);
        et_time = (EditText) findViewById(R.id.et_time);
        regionLayout = (RelativeLayout) findViewById(R.id.region_layout);
        regionSpinner = (Spinner) findViewById(R.id.spin_region);
        et_name = (AutoCompleteTextView) findViewById(R.id.et_name);
//        btn_new = (Button) findViewById(R.id.btn_new);
        line = findViewById(R.id.line);
        line1 = findViewById(R.id.line1);
        et_address = (AutoCompleteTextView) findViewById(R.id.et_address);
        et_district = (AutoCompleteTextView) findViewById(R.id.et_district);
        et_taluka = (EditText) findViewById(R.id.et_taluka);
        et_village = (EditText) findViewById(R.id.et_village);
        et_gstin = (EditText) findViewById(R.id.et_gstin_no);
        et_mobile = (EditText) findViewById(R.id.et_moibile_no);
        validate_mobile = findViewById(R.id.validate_mobile);
        et_fieldarea = (EditText) findViewById(R.id.et_fieldarea);
        textView_start_date = (TextView) findViewById(R.id.start_date);
        fieldsLayout = findViewById(R.id.fields_layout);
        addressLayout = findViewById(R.id.address_layout);
        fieldAreaLayout = findViewById(R.id.field_area_layout);
        gstinLayout = findViewById(R.id.gstin_layout);
        tv_region = findViewById(R.id.region_tv);
        tv_name = findViewById(R.id.name_tv);
        tv_district = findViewById(R.id.district_tv);
        tv_taluka = findViewById(R.id.taluka_tv);
        tv_village = findViewById(R.id.village_tv);
        tv_gstin = findViewById(R.id.gstin_tv);
        tv_fieldarea = findViewById(R.id.fieldarea_tv);
        tv_mobile = findViewById(R.id.mobile_tv);
        edit_iv = findViewById(R.id.edit_icon);
        new_iv = findViewById(R.id.new_iv);

        et_name.setFilters(new InputFilter[]{filter});
        auto_tv = (AutoCompleteTextView) findViewById(R.id.auto_tv);
//        goneFields(View.GONE);
        Typeface typeface = Typeface.createFromAsset(getAssets(), Constants.CUSTOM_FONT_PATH_LIGHT);
        auto_tv.setTypeface(typeface);

//        btn_new.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                if (Common.haveInternet(PlanScheduleFormActivity.this)) {
//                    et_district.setText("");
//                    et_taluka.setText("");
//                    et_village.setText("");
//                    et_gstin.setText("");
//                    et_mobile.setText("");
//                    retailerId = "0";
////                    regionId = "";
//                    sel_userId = Common.getUserIdFromSP(PlanScheduleFormActivity.this);
//                    regionLayout.setVisibility(View.VISIBLE);
//                    if (str_sel_visittypeid.equalsIgnoreCase("2")) {
//                        auto_tv.setVisibility(View.VISIBLE);
////                        new AsyncUsers().execute();
//                        line.setVisibility(View.VISIBLE);
//                    } else {
//                        auto_tv.setVisibility(View.GONE);
//                        line.setVisibility(View.GONE);
//                    }
//
//                } else
//
//                {
//                    Toast.makeText(PlanScheduleFormActivity.this, "Please Turn On Internet Connection.", Toast.LENGTH_SHORT).show();
//                }
//            }
//        });

        textView_start_date.setText(str_date + "   >  SCHEDULE");
        if (role != Constants.Roles.ROLE_7) {
            sel_userId = getIntent().getExtras().getString("sel_userId");
            userName = getIntent().getExtras().getString("sel_userName");
            isDirectCustomers = getIntent().getBooleanExtra("isDirectCustomers", false);

            //textView_start_date.setText(userName+" > "+str_date+" > SCHEDULE");
            textView_start_date.setText(userName + ">" + str_date + " > SCHEDULE");
        }

        dateFormat = new

                SimpleDateFormat(Constants.DateFormat.COMMON_DATE_TIME_FORMAT);

        orderdateFormat = new

                SimpleDateFormat(Constants.DateFormat.COMMON_DATE_FORMAT);

        myDate = new

                Date();
        setplanvisittypeData();
//        new AsyncUsers().execute();


        et_address.setThreshold(1);
//        et_district.setThreshold(1);
//        et_taluka.setThreshold(1);
//        et_village.setThreshold(1);
//        et_gstin.setThreshold(1);
//        et_mobile.setThreshold(1);

        spin_crop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog dialog = new AlertDialog.Builder(PlanScheduleFormActivity.this).create();
                View popupView = getLayoutInflater().inflate(R.layout.crop_popup, null);
                dialog.setView(popupView);
                dialog.show();

                SearchView searchView = popupView.findViewById(R.id.search_view);
                RecyclerView cropRecycler = popupView.findViewById(R.id.crop_recycler);
                Button submit = popupView.findViewById(R.id.submit_popup);
                searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String s) {
                        if (s.length() > 0) {
                            searchedCrops = new ArrayList<>();
                            for (int i = 0; i < cropsList.size(); i++) {
                                if (cropsList.get(i)._crop_name.toLowerCase().contains(s.toLowerCase())) {
                                    searchedCrops.add(cropsList.get(i));
                                }
                            }
                            cropAdapter.updateResults(searchedCrops);
                        }
                        return false;
                    }

                    @Override
                    public boolean onQueryTextChange(String s) {
                        if (s.length() == 0)
                            cropAdapter.updateResults(cropsList);
                        return false;
                    }
                });

                cropAdapter = new CropAdapter(PlanScheduleFormActivity.this, cropsList) {
                    @Override
                    public void setCropItems(List<Crops> cropItems) {
                        super.setCropItems(cropItems);
                        cropItemsList.addAll(cropItems);
                        HashSet<Crops> cropsHashSet = new HashSet<>();
                        cropsHashSet.addAll(cropItemsList);
                        cropItemsList.clear();
                        cropItemsList.addAll(cropsHashSet);

                    }
                };
                LinearLayoutManager llm = new LinearLayoutManager(PlanScheduleFormActivity.this, LinearLayoutManager.VERTICAL, false);
                cropRecycler.setLayoutManager(llm);
                cropRecycler.setAdapter(cropAdapter);

                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        cropNames = new StringBuffer();
                        cropIds = new StringBuffer();
                        fieldAreas = new StringBuffer();
                        for (int i = 0; i < cropItemsList.size(); i++) {
                            if (cropItemsList.get(i).isChecked) {
                                if (cropItemsList.get(i).fieldArea == null || cropItemsList.get(i).fieldArea.length() == 0) {
                                    Toast.makeText(PlanScheduleFormActivity.this, "Selected Crop Field Area should not be Empty.", Toast.LENGTH_SHORT).show();
                                    return;
                                } else {
                                    cropNames.append(cropItemsList.get(i)._crop_name + " - " + cropItemsList.get(i).fieldArea + ",");
                                    cropIds.append(cropItemsList.get(i)._crop_masterid + ",");
                                    fieldAreas.append(cropItemsList.get(i).fieldArea + ",");
                                }


                            }
                        }
                        if (cropNames.toString().trim().length() > 0)
                            cropNames = new StringBuffer(cropNames.substring(0, cropNames.lastIndexOf(",")));
                        if (cropIds.toString().trim().length() > 0)
                            cropIds = new StringBuffer(cropIds.substring(0, cropIds.lastIndexOf(",")));
                        if (fieldAreas.toString().trim().length() > 0)
                            fieldAreas = new StringBuffer(fieldAreas.substring(0, fieldAreas.lastIndexOf(",")));
                        spin_crop.setText(cropNames.toString());
                        dialog.dismiss();
                    }
                });

            }
        });

        et_address.addTextChangedListener(new

                                                  TextWatcher() {

                                                      @Override
                                                      public void onTextChanged(CharSequence s, int start, int before, int count) {
//                PlacesTask placesTask = new PlacesTask();
//                placesTask.execute(s.toString());
                                                      }

                                                      @Override
                                                      public void beforeTextChanged(CharSequence s, int start, int count,
                                                                                    int after) {
                                                          // TODO Auto-generated method stub
                                                      }

                                                      @Override
                                                      public void afterTextChanged(Editable s) {
                                                          // TODO Auto-generated method stub
                                                      }
                                                  });


        et_name.addTextChangedListener(new

                                               TextWatcher() {
                                                   @Override
                                                   public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                                                   }

                                                   @Override
                                                   public void onTextChanged(CharSequence s, int start, int before, int count) {

                                                   }

                                                   @Override
                                                   public void afterTextChanged(Editable s) {
                                                       Log.d("afterTextChanged", "afterTextChanged" + s.length());
                                                       if (s.length() == 0) {
//                                                           btn_new.setVisibility(View.VISIBLE);
                                                           // auto_tv.setAdapter(adapter);
                                                           clearFields();
                                                           et_name.showDropDown();
                                                       } else {
                                                           if (handler != null) {
                                                               handler.removeCallbacks(myRunnable);
                                                           }
                                                           handler = new Handler();
                                                           if (myRunnable != null) {
                                                               myRunnable.setData(s.toString());
                                                               handler.postDelayed(myRunnable, 300);
                                                           }
                                                       }
                                                   }
                                               });

        et_name.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        et_name.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (et_name.getText().length() == 0) {
                    //   auto_tv.setAdapter(adapter);

                    if (handler != null) {
                        handler.removeCallbacks(myRunnable);
                    }
                    handler = new Handler();
                    if (myRunnable != null) {
                        myRunnable.setData("");
                        handler.postDelayed(myRunnable, 300);
                    }
//                    et_name.showDropDown();
                }
                return false;
            }
        });

        et_district.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() == 0) {
                    et_district.showDropDown();
                } else {
                    if (handler != null) {
                        handler.removeCallbacks(myRunnable1);
                    }
                    handler = new Handler();
                    if (myRunnable1 != null) {
                        myRunnable1.setData(s.toString());
                        handler.postDelayed(myRunnable1, 300);
                    }

                }
            }
        });

        et_district.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (et_district.getText().length() == 0) {
                    if (handler != null) {
                        handler.removeCallbacks(myRunnable1);
                    }
                    handler = new Handler();
                    if (myRunnable1 != null) {
                        myRunnable1.setData("");
                        handler.postDelayed(myRunnable1, 300);
                    }
//                    et_district.showDropDown();
                }
                return false;
            }
        });

        new_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                newLayout();

            }
        });
        validate_mobile.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                beforeTextChangedLength = charSequence.length();
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                if (editable == null || editable.length() < 4) {
                    return;
                }

                if (Common.haveInternet(PlanScheduleFormActivity.this)) {
                    if (editable.length() == 10 && beforeTextChangedLength == 9)
                        new Async_RetailerChecking(editable.toString(), str_sel_visittypeid).execute();
                    else {
                        clearFields();
                    }
                } else {
                    if (validate_mobile.getText().toString().trim().length() > 5) {
                        validate_mobile.setText("");
                        Toast.makeText(PlanScheduleFormActivity.this, Common.INTERNET_UNABLEABLE, Toast.LENGTH_SHORT).show();
                    } else if (editable.length() == 0) {
                        clearFields();
                    }

                }
            }
        });
        edit_iv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                et_name.setVisibility(View.VISIBLE);
                addressLayout.setVisibility(View.GONE);
                validate_mobile.setText("");
                validate_mobile.setVisibility(View.GONE);
                if (str_sel_visittypeid.equalsIgnoreCase("3"))
                    new_iv.setVisibility(View.VISIBLE);
                else
                    new_iv.setVisibility(View.GONE);
                et_name.setText("");
                clearFields();
            }
        });

        et_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentTime = Calendar.getInstance();
                int hour = mcurrentTime.get(Calendar.HOUR_OF_DAY);
                int minute = mcurrentTime.get(Calendar.MINUTE);
                TimePickerDialog mTimePicker;
                mTimePicker = new TimePickerDialog(PlanScheduleFormActivity.this, new TimePickerDialog.OnTimeSetListener() {
                    @Override
                    public void onTimeSet(TimePicker timePicker, int selectedHour, int selectedMinute) {
                        String formattedTime = "";
                        String sHour = "00";
                        String sMinute = "00";
                        // converting hour to tow digit if its between 0 to 9. (e.g. 7 to 07)
                        if (selectedHour < 10)
                            sHour = "0" + selectedHour;
                        else
                            sHour = String.valueOf(selectedHour);

                        if (selectedMinute < 10)
                            sMinute = "0" + selectedMinute;
                        else
                            sMinute = String.valueOf(selectedMinute);

                        et_time.setText(sHour + ":" + sMinute + ":00");
                    }
                }, hour, minute, true);
                // mTimePicker.setTitle("Select Time");
                mTimePicker.show();

            }
        });

        btn_savebooing = (Button)

                findViewById(R.id.btn_saveplan);
        btn_savebooing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                str_time = et_time.getText().toString().trim();
                str_address = et_address.getText().toString();
                if (retailer != null || farmer != null) {
                    str_district = tv_district.getText().toString().trim();
                    str_taluka = tv_taluka.getText().toString().trim();
                    str_name = tv_name.getText().toString().trim();
                    str_village = tv_village.getText().toString().trim();
                    str_gstin = tv_gstin.getText().toString().trim();
                    str_mobile = tv_mobile.getText().toString().trim();
                    str_fieldarea = tv_fieldarea.getText().toString().trim();
                } else {
                    str_district = et_district.getText().toString().trim();
                    str_taluka = et_taluka.getText().toString().trim();
                    str_name = et_name.getText().toString().trim();
                    str_village = et_village.getText().toString().trim();
                    str_gstin = et_gstin.getText().toString().trim();
                    str_mobile = validate_mobile.getText().toString().trim();
                    str_fieldarea = et_fieldarea.getText().toString().trim();
                }
                if (spin_visittypes.getSelectedItem().toString().trim().equalsIgnoreCase("Please select visit type")) {

                    // Toast.makeText(getActivity(), "Please select crop", Toast.LENGTH_SHORT).show();
                    tvInvisibleError.requestFocus();
                    tvInvisibleError.setError("Please select visit type");
                    return;

                }
                if (!str_sel_visittypeid.equalsIgnoreCase("3") && auto_tv.getText().toString().trim().length() == 0) {

                    // Toast.makeText(getActivity(), "Please select crop", Toast.LENGTH_SHORT).show();
                    tvInvisibleError.requestFocus();
                    tvInvisibleError.setError("Please select distributor");
                    return;

                }
                if (spin_purpose.getText().toString().trim().length() == 0) {
                    tvInvisibleError1.requestFocus();
                    tvInvisibleError1.setError("Please select purpose");
                    return;
                }
                if (str_sel_visittypeid.equalsIgnoreCase("2") || str_sel_visittypeid.equalsIgnoreCase("3")) {
                    if (str_sel_visittypeid.equalsIgnoreCase("3") && (cropIds.toString().length() == 0 || cropIds.toString().equalsIgnoreCase("0"))) {
                        spin_crop.requestFocus();
                        Toast.makeText(PlanScheduleFormActivity.this, "Please Select Crop", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    if (TextUtils.isEmpty(str_name) || str_name.length() > 0 && str_name.startsWith(" ")) {
                        et_name.requestFocus();
                        et_name.setError("Please enter name");
                        return;
                    }

                                   /* if (TextUtils.isEmpty(str_address) || str_address.length() > 0 && str_address.startsWith(" ")) {
                                        et_address.requestFocus();
                                        et_address.setError("Please enter address");

                                        return;
                                    }*/
                    if (fieldsLayout.getVisibility() == View.VISIBLE) {
                        if (districtId.equalsIgnoreCase("0") || TextUtils.isEmpty(str_district) || str_district.length() > 0 && str_district.startsWith(" ")) {
                            et_district.requestFocus();
                            et_district.setError("Please choose district from dropdown");
                            return;
                        }
                        if (TextUtils.isEmpty(str_taluka) || str_taluka.length() > 0 && str_taluka.startsWith(" ")) {
                            et_taluka.requestFocus();
                            et_taluka.setError("Please enter taluka");
                            return;
                        }

                        if (TextUtils.isEmpty(str_village) || str_village.length() > 0 && str_village.startsWith(" ")) {
                            et_village.requestFocus();
                            et_village.setError("Please enter village");
                            return;
                        }
                        if (str_sel_visittypeid.equalsIgnoreCase("2") && (/*str_gstin.trim().length() == 0 &&*/ str_mobile.trim().length() == 0)) {
//                                        et_gstin.requestFocus();
//                                        et_gstin.setError("Please enter gstin or mobile numer");
                            Toast.makeText(PlanScheduleFormActivity.this, "Please enter mobile number", Toast.LENGTH_SHORT).show();
                            return;
                        }
                        if (str_mobile.trim().length() > 0 && str_mobile.trim().length() < 10) {
                            validate_mobile.requestFocus();
                            validate_mobile.setError("Please Enter Valid Mobile Number");
                            return;
                        }
                        if (str_sel_visittypeid.equalsIgnoreCase("3") && (TextUtils.isEmpty(str_mobile) || str_mobile.length() > 0 && str_mobile.startsWith(" "))) {
                            validate_mobile.requestFocus();
                            validate_mobile.setError("Please enter mobile number");

                            return;
                        }
                        if (!str_sel_visittypeid.equalsIgnoreCase("2") &&
                                (str_fieldarea == null || str_fieldarea.length() == 0)) {
                            et_fieldarea.requestFocus();
                            et_fieldarea.setError("Please enter fieldarea");
                            return;
                        }
                        if (!str_sel_visittypeid.equalsIgnoreCase("1") && regionId.length() == 0) {
                            regionSpinner.requestFocus();
                            Toast.makeText(PlanScheduleFormActivity.this, "Please Choose Region", Toast.LENGTH_SHORT).show();
                            return;
                        }
                    } else {
                        if (addressLayout.getVisibility() == View.GONE && str_sel_visittypeid.equalsIgnoreCase("3")) {
                            Toast.makeText(PlanScheduleFormActivity.this, "please enter mobile number", Toast.LENGTH_SHORT).show();
                            newLayout();
                            return;
                        }

                        if (addressLayout.getVisibility() == View.GONE && str_sel_visittypeid.equalsIgnoreCase("2")) {
                            Toast.makeText(PlanScheduleFormActivity.this, "Retailer not exists, You can't Create Plan.", Toast.LENGTH_SHORT).show();
                            return;
                        }

                    }
                }


                if (TextUtils.isEmpty(str_time) || str_time.length() > 0 && str_time.startsWith(" ")) {
                    et_time.setError("Please select time");
                    et_time.requestFocus();
                    return;
                }
                final AlertDialog.Builder alert = new AlertDialog.Builder(PlanScheduleFormActivity.this);
                alert.setTitle("Alert");
                alert.setMessage("Are you sure want to create Planner? ");
                alert.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog,
                                                int which) {
                                dialog.dismiss();
                                Handler handler = Common.disableClickEvent(btn_savebooing, true);


//                                if (str_sel_visittypeid.equalsIgnoreCase("3") && farmerId.equalsIgnoreCase("0")) {
//                                    if (!str_mobile.isEmpty()) {
//                                        if (db.isAlreadyRecordExist(db.getWritableDatabase(), str_mobile, TABLE_FARMERS, KEY_TABLE_FARMER_PHONE) == 1) {
//                                            Toast.makeText(PlanScheduleFormActivity.this, "Farmer existed with this Mobile Number", Toast.LENGTH_SHORT).show();
//                                            return;
//                                        }
//
//                                    }
//                                }
//                                if (str_sel_visittypeid.equalsIgnoreCase("2") && retailerId.equalsIgnoreCase("0")) {
//                                    if (!str_gstin.isEmpty()) {
//                                        if (db.isAlreadyRecordExist(db.getWritableDatabase(), str_gstin, TABLE_RETAILER, KEY_TABLE_RETAILER_GSTIN_NO) == 1) {
//                                            Toast.makeText(PlanScheduleFormActivity.this, "Retailer existed with this GSTIN", Toast.LENGTH_SHORT).show();
//                                            return;
//                                        }
//
//                                    }
//
//                                    if (!str_mobile.isEmpty()) {
//                                        if (db.isAlreadyRecordExist(db.getWritableDatabase(), str_mobile, TABLE_RETAILER, KEY_TABLE_RETAILER_MOBILE) == 1) {
//                                            Toast.makeText(PlanScheduleFormActivity.this, "Retailer existed with this Mobile Number", Toast.LENGTH_SHORT).show();
//                                            return;
//                                        }
//
//                                    }
//                                }
                                Common.disableClickEvent(btn_savebooing, handler);
                                //new Async_submitadvancepayment().execute();
                                order_date = dateFormat.format(myDate);
                                Log.d("Insert: ", "Inserting event .." + PlanScheduleFormActivity.this.getApplicationContext().toString());
                                progressDialog1 = Common.showProgressDialog(PlanScheduleFormActivity.this);

                                // For Inserting order into service order table
                                Log.d("Insert: ", "Inserting event ..");
                                // db.addEVM(new Employe_visit_management_pojo("",userId,sel_customer_id, str_sel_visittypeid, str_sel_purposeid, str_date, str_name, mobile, village, str_address, str_fieldarea, check_in_time, comments, "1","",type,userId,userId,order_date,order_date,geo_tracking_id));
                                // custom dialog

                                if (spin_purpose.getText().toString().trim().length() > 0) {

                                    str_sel_purposetypes = spin_purpose.getText().toString().trim();

                                    Log.d("str_sel_purposetypes", str_sel_purposetypes.toString());
                                }
                                visiterIds = getVisiterIdByName(checkedList);
                                Log.d("str_sel_purposetypes", visiterIds);
                                if (visiterIds.contains("12"))
                                    comments = getOtherComments();
                                else
                                    comments = "";
                                approvalStatus = role == Constants.Roles.ROLE_5 ? 1 : 0;
                              /*  if (str_sel_visittypeid.equalsIgnoreCase("2") && retailerId.equalsIgnoreCase("0")) {

                                    Retailer retailer = new Retailer("", str_name, "", "", str_district, str_taluka, str_village, regionId, str_gstin, "", str_mobile, "", String.valueOf(sel_customer_id), "", "", "", "", "0");
                                    retailer.setID(db.addRetailers(retailer));
                                    if (Common.haveInternet(PlanScheduleFormActivity.this))
                                        new Async_InsertRetailer(retailer).execute();
                                    else {
                                        serverFlag = 0;
                                        retailerId = String.valueOf(retailer.getID());
                                        postPlannerData();
                                    }
                                } else*/
                                if (str_sel_visittypeid.equalsIgnoreCase("3") && farmerId.equalsIgnoreCase("0")) {
                                    if (Common.haveInternet(PlanScheduleFormActivity.this)) {
                                        FarmerPojo farmerPojo = new FarmerPojo("", str_name, str_mobile, "", str_district, str_taluka, str_village, regionId, str_fieldarea, "1", "0");
//                                        farmerPojo.setPrimaryId(db.addFarmer(farmerPojo));
                                        new Async_InsertFarmer(farmerPojo).execute();
                                    } else {
                                        Common.disableClickEvent(btn_savebooing, true);
                                        progressDialog1.dismiss();
                                        Toast.makeText(PlanScheduleFormActivity.this, Common.INTERNET_UNABLEABLE, Toast.LENGTH_SHORT).show();
                                        return;
//                                        serverFlag = 0;
//                                        farmerId = String.valueOf(farmerPojo.getPrimaryId());
//                                        postPlannerData();
                                    }
                                } else {
                                    serverFlag = 1;
                                    postPlannerData();
                                }


                            }
                        });
                alert.setNegativeButton("No", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
                alert.show();


            }
        });
        if (role == Constants.Roles.ROLE_7) {
            ll_select.setVisibility(View.GONE);


            new AsyncUsers().execute();
        } else if (role != Constants.Roles.ROLE_7) {

            callapi();
        }

        if (sel_userId != null && sel_userId.length() > 0 && userses != null) {

            for (Users users : userses) {
                if (users.getID() == Integer.parseInt(sel_userId)) {
                    int index = userses.indexOf(users);
                    spin_user.setSelection(index);
                    break;
                }
            }
        }
        // new Async_getalloffline().execute();

    }

    private void newLayout() {
        Common.hideKeyboard(PlanScheduleFormActivity.this);
        addressLayout.setVisibility(View.GONE);
        if (et_name.getText().toString().trim().length() == 0) {
            et_name.setError("Please enter name");
            et_name.requestFocus();
            return;
        }
        validate_mobile.setVisibility(View.VISIBLE);

    }

    private void clearFields() {
        retailer = null;
        farmer = null;
        et_district.setText("");
        et_taluka.setText("");
        et_village.setText("");
        et_gstin.setText("");
        et_mobile.setText("");
        retailerId = "0";
        farmerId = "0";
        if (str_sel_visittypeid.equalsIgnoreCase("3"))
            new_iv.setVisibility(View.VISIBLE);
        addressLayout.setVisibility(View.GONE);
        fieldsLayout.setVisibility(View.GONE);
    }

    @Override
    protected void onInternetConnected() {
        myRunnable = new MyRunnable();
        myRunnable1 = new MyRunnable1();
    }

    @Override
    protected void onInternetDisconnected() {
        myRunnable = new MyRunnable();
        myRunnable1 = new MyRunnable1();
        Common.disableClickEvent(btn_savebooing, true);
        if (progressDialog != null && progressDialog.isShowing())
            progressDialog.dismiss();
        if (progressDialog1 != null && progressDialog1.isShowing())
            progressDialog1.dismiss();
    }

    private boolean isRecordExists(String record) {
        List<Retailer> allRetailers = db.getAllRetailers();
        boolean exists = false;
        for (int i = 0; i < allRetailers.size(); i++) {
            if (allRetailers.get(i).get_ret_mobile().trim().equalsIgnoreCase(record.trim())) {
                exists = true;
                break;
            }
        }
        return exists;
    }

//    private void goneFields(int visibility) {
//        et_district.setVisibility(visibility);
//        et_taluka.setVisibility(visibility);
//        et_village.setVisibility(visibility);
//        et_gstin.setVisibility(visibility);
//        et_mobile.setVisibility(visibility);
//        et_fieldarea.setVisibility(View.GONE);
//        regionLayout.setVisibility(visibility);
//        /*if (str_sel_visittypeid.equalsIgnoreCase("2")) {
//            et_fieldarea.setVisibility(View.GONE);
//        } else*/
//        if (str_sel_visittypeid.equalsIgnoreCase("3")) {
//            et_gstin.setVisibility(View.GONE);
//        }
//    }

    private void postPlannerData() {
        if (role == Constants.Roles.ROLE_7) {
            db.addEVM(new Employe_visit_management_pojo("", type, geo_tracking_id, Integer.parseInt(userId), sel_customer_id,
                    str_sel_visittypeid, str_sel_purposetypes, visiterIds, str_date + " " + str_time, str_name, retailerId, farmerId, mobile, village, str_address, cropIds.toString(), regionId, str_district, str_taluka, str_village, str_gstin, fieldAreas.toString(), check_in_time,
                    comments, 1, approvalStatus, "", "", "", "", "", userId, userId, order_date, order_date, 0, serverFlag));
        } else if (role != Constants.Roles.ROLE_7) {
            db.addEVM(new Employe_visit_management_pojo("", type, geo_tracking_id, Integer.parseInt(sel_userId), sel_customer_id,
                    str_sel_visittypeid, str_sel_purposetypes, visiterIds, str_date + " " + str_time, str_name, retailerId, farmerId, mobile, village, str_address, cropIds.toString(), regionId, str_district, str_taluka, str_village, str_gstin, fieldAreas.toString(), check_in_time,
                    comments, 1, approvalStatus, "", "", "", "", "", userId, userId, order_date, order_date, 0, serverFlag));
        }

        if (Common.haveInternet(getApplicationContext())) {
            updateplanner();
        } else {
            Common.dismissProgressDialog(progressDialog1);

            Intent planer = new Intent();
            planer.putExtra("date", str_date);
            planer.putExtra("i", "1");
            setResult(Activity.RESULT_OK, planer);

            finish();
        }
    }

    private void setRegionsData() {
        List<Regions> regions = new ArrayList<>();
        if (str_sel_visittypeid.equalsIgnoreCase("2")) {
            regions.add(db.getRegionById(cusRegionId));
        } else if (str_sel_visittypeid.equalsIgnoreCase("3")) {
            regions = db.getRegionsByUserId(Common.getUserIdFromSP(this));
        }
        if (regions != null && regions.size() > 0) {
            final List<SpinnerModel1> spinnerModel1s = new ArrayList<>();
            for (int i = 0; i < regions.size(); i++) {
                SpinnerModel1 spinnerModel1 = new SpinnerModel1();
                spinnerModel1.id = regions.get(i).getRegionMasterId();
                spinnerModel1.name = regions.get(i).getRegionName();
                spinnerModel1s.add(spinnerModel1);
            }
            final CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(getApplication(), R.layout.spinner_item, spinnerModel1s);
            regionSpinner.setAdapter(adapter);
            regionSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    regionId = spinnerModel1s.get(i).id;
                    districtId = "0";
                    et_district.setText("");
                    et_taluka.setText("");
                    et_village.setText("");
                    et_fieldarea.setText("");
//                    et_district.setText("");
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });
        }
    }

    public void autoCompleteRetailerAdapter(List<SelectedCities> list) {
        final CustomListAdapter1 adapter = new CustomListAdapter1(this,
                R.layout.autocomplete_custom_layout1, list);

        et_name.setAdapter(adapter);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                et_name.showDropDown();
            }
        }, 100);

        adapter.setOnItemClickListener(new CustomListAdapter1.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, SelectedCities item) {
                onItemClick = true;
//                btn_new.setVisibility(View.GONE);
//                regionLayout.setVisibility(View.GONE);
                Common.Log.i("SELECTED ITEM " + item.getCityName().toUpperCase());
                fieldsLayout.setVisibility(View.GONE);
                et_name.setVisibility(View.GONE);
                addressLayout.setVisibility(View.VISIBLE);
                validate_mobile.setText("");
                validate_mobile.setVisibility(View.GONE);
                new_iv.setVisibility(View.GONE);
                if (str_sel_visittypeid.equalsIgnoreCase("2")) {
                    retailer = db.getRetailer(item.getCityId());
                    if (retailer != null) {
                        fieldAreaLayout.setVisibility(View.GONE);
                        gstinLayout.setVisibility(View.VISIBLE);
                        regionId = retailer.get_ret_region_id();
                        tv_region.setText(Common.isStringNull(db.getRegionById(regionId).getRegionName()));
                        tv_district.setText(retailer.get_ret_district().toUpperCase());
                        tv_name.setText(retailer.get_ret_name().toUpperCase());
                        tv_taluka.setText(retailer.get_ret_taluka().toUpperCase());
                        tv_village.setText(retailer.get_ret_village().toUpperCase());
                        tv_gstin.setText(retailer.get_ret_gstin_no().toUpperCase());
                        tv_mobile.setText(retailer.get_ret_mobile().toUpperCase());
                    }
                    retailerId = item.getRetailerId();
                } else if (str_sel_visittypeid.equalsIgnoreCase("3")) {
                    farmer = db.getFarmer(item.getCityId());
                    if (farmer != null) {
                        fieldAreaLayout.setVisibility(View.VISIBLE);
                        gstinLayout.setVisibility(View.GONE);
                        regionId = farmer.getRegionId();
                        tv_region.setText(Common.isStringNull(db.getRegionById(regionId).getRegionName()));
                        tv_district.setText(farmer.getLocationDistrict().toUpperCase());
                        tv_taluka.setText(farmer.getLocationTaluka().toUpperCase());
                        tv_name.setText(farmer.getFarmerName().toUpperCase());
                        tv_village.setText(farmer.getLocationVillage().toUpperCase());
                        tv_fieldarea.setText(farmer.getTotalLandHolding().toUpperCase());
                        tv_mobile.setText(farmer.getMobile().toUpperCase());
                    }
                    farmerId = item.getCityId();
                }
//                if (item.getCityName().equalsIgnoreCase("New Retailer") || item.getCityName().equalsIgnoreCase("New Farmer")) {
//                    enableFields(true);
//                } else {
                et_name.setText(item.getCityName().toUpperCase());
                et_name.setSelection(item.getCityName().length());
//                    enableFields(false);
//                }
                et_name.dismissDropDown();
                Common.hideSoftKeyboard(PlanScheduleFormActivity.this);
                //    Toast.makeText(getApplicationContext(),item.getCityName()+"\n"+item.getCityId(),Toast.LENGTH_SHORT).show();
//                Log.d("onItemClick", retailer.get_ret_masterid());

//                sel_customer_id = item.getCityId();
//                tv_customer.setTextColor(getResources().getColor(R.color.colorPrimary));
//                customername = item.getCityName();
//                SharedPreferences.Editor editor = sharedpreferences.edit();
//                editor.putString("customer_id", sel_customer_id);
//                editor.putString("customer_name", item.getCityName());
//                editor.commit();

            }
        });

    }

//    private void enableFields(boolean b) {
//        goneFields(View.VISIBLE);
//        if (b) {
//            et_district.setText("");
//            et_taluka.setText("");
//            et_fieldarea.setText("");
//            et_village.setText("");
//            et_gstin.setText("");
//            et_mobile.setText("");
//        }
//        et_district.setEnabled(b);
//        et_taluka.setEnabled(b);
//        et_village.setEnabled(b);
//        et_gstin.setEnabled(b);
//        et_mobile.setEnabled(b);
//        et_fieldarea.setEnabled(b);
//        regionSpinner.setEnabled(b);
//    }

    public void autoCompleteDistrictAdapter(List<SelectedCities> list) {
        final CustomListAdapter adapter = new CustomListAdapter(this,
                R.layout.autocomplete_custom_layout, list);

        et_district.setAdapter(adapter);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                et_district.showDropDown();
            }
        }, 100);

        adapter.setOnItemClickListener(new CustomListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, SelectedCities item) {
                onDistrictItemClick = true;
                et_district.setText(item.getCityName().toUpperCase());
                et_district.setSelection(item.getCityName().length());
                districtId = item.getCityId();
                et_district.dismissDropDown();
                Common.hideSoftKeyboard(PlanScheduleFormActivity.this);
                //    Toast.makeText(getApplicationContext(),item.getCityName()+"\n"+item.getCityId(),Toast.LENGTH_SHORT).show();
//                Log.d("onItemClick", retailer.get_ret_masterid());

//                sel_customer_id = item.getCityId();
//                tv_customer.setTextColor(getResources().getColor(R.color.colorPrimary));
//                customername = item.getCityName();
//                SharedPreferences.Editor editor = sharedpreferences.edit();
//                editor.putString("customer_id", sel_customer_id);
//                editor.putString("customer_name", item.getCityName());
//                editor.commit();

            }
        });

    }

    private void checkboxPopup() {
        final android.app.AlertDialog alertDialog = new android.app.AlertDialog.Builder(this).create();
        View dialogView = getLayoutInflater().inflate(R.layout.check_box_recyclerview, null);
        alertDialog.setView(dialogView);
        alertDialog.show();
        alertDialog.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_NOT_FOCUSABLE | WindowManager.LayoutParams.FLAG_ALT_FOCUSABLE_IM);
        alertDialog.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_VISIBLE);
        final StringBuffer sb = new StringBuffer();
        ListView optionsView = dialogView.findViewById(R.id.options_list);
        TextView submit = dialogView.findViewById(R.id.options_submit);
        TextView cancel = dialogView.findViewById(R.id.options_cancel);
        if (purposeList != null) {
            setDataToOptions();
            optionsAdapter = new OptionsAdapter(this);
            optionsView.setAdapter(optionsAdapter);
        }

        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                for (int i = 0; i < purposeList.size(); i++) {
                    purposeList.get(i).isChecked = false;
                }
                setDataToOptions();
                alertDialog.dismiss();
            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                checkedList.clear();
                sb.setLength(0);
                for (int i = 0; i < purposeList.size(); i++) {
                    if (purposeList.get(i).isChecked && purposeList.get(i).title.equalsIgnoreCase("Other") && getOtherComments().length() == 0) {
                        Toast.makeText(PlanScheduleFormActivity.this, "Please Enter Other", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    if (purposeList.get(i).isChecked) {
                        checkedList.add(purposeList.get(i).title);
                        sb.append(purposeList.get(i).title + ",");
                    }
                    if (!purposeList.get(i).isChecked && purposeList.get(i).title.equalsIgnoreCase("Other")) {
                        setOtherComments("");
                    }
                }
//                sb.setLength(0);
//                associateContactId.setLength(0);
//                opportunityAssociateContacts.clear();
//                for (int i = 0; i < associateContacts.size(); i++) {
//                    if (i > 0 && associateContacts.get(i).aBoolean == true) {
//                        OpportunityAssociateContact opportunityAssociateContact = new OpportunityAssociateContact();
//                        opportunityAssociateContact.id = associateContacts.get(i).contactId;
//                        opportunityAssociateContact.name = associateContacts.get(i).name;
//                        opportunityAssociateContacts.add(opportunityAssociateContact);
//                        associateContactId.append(associateContacts.get(i).contactId + ",");
//                        sb.append(associateContacts.get(i).name + ",");
//                    }
//                }
                if (sb.toString().length() > 0) {
                    spin_purpose.setText(Common.isStringNull(sb.toString().substring(0, sb.lastIndexOf(","))));
                } else {
                    Toast.makeText(PlanScheduleFormActivity.this, "Please Select Purpose", Toast.LENGTH_SHORT).show();
                    return;
                }
                alertDialog.dismiss();
            }
        });
    }

    private void setDataToOptions() {
        if (checkedList != null) {
            for (int i = 0; i < checkedList.size(); i++) {
                for (int j = 0; j < purposeList.size(); j++) {
                    if (checkedList.get(i).equalsIgnoreCase(purposeList.get(j).title)) {
                        purposeList.get(j).isChecked = true;
                    }
                }
            }
        }
    }

    private void callapi() {
        userses = new ArrayList<Users>();
        adapterusers = new ArrayList<String>();
        userses.clear();
        Users user = new Users();
        /*user.setID(0);
        user.setUser_first_name("Selef");
        // System.out.println("city id is :" + cityId + "city name is :" + cityName);
        userses.add(user);
        adapterusers.add("Self");*/
        try {

            String selectQuery;
//            if (!isDirectCustomers) {
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
//                selectQuery = "SELECT " + KEY_TABLE_USERS_FIRST_NAME + "," + KEY_TABLE_USERS_MASTER_ID + " FROM " + TABLE_USERS + "  WHERE " + db.KEY_TABLE_USERS_STATUS + " = 1 and user_id in (" + team + ")";
//            } else {

            selectQuery = "SELECT " + KEY_TABLE_USERS_FIRST_NAME + "," + KEY_TABLE_USERS_MASTER_ID + " FROM " + TABLE_USERS + "  WHERE " + db.KEY_TABLE_USERS_STATUS + " = 1 and user_id in (" + team + ")";

//            }


            //  String selectQuery = "SELECT " + KEY_TABLE_USERS_FIRST_NAME + "," + KEY_TABLE_USERS_MASTER_ID + " FROM " + TABLE_USERS + "  WHERE  user_id in (" + userId + "," + team + ")";
            //List<Company_division_crops> cdclist = db.getAllCompany_division_crops();

            sdbw = db.getWritableDatabase();

            Cursor cursor = sdbw.rawQuery(selectQuery, null);
            //System.out.println("cursor count "+cursor.getCount());
            if (cursor.moveToFirst()) {
                do {
                   /* Users users = new Users();

                    users.setUserMasterID(cursor.getString(1));
                    users.setUser_first_name(cursor.getString(0));*/

                    Users userDEtail = new Users();
                    userDEtail.setID(Integer.parseInt(cursor.getString(1)));
                    userDEtail.setUser_first_name(cursor.getString(0));
                    // System.out.println("city id is :" + cityId + "city name is :" + cityName);
                    userses.add(userDEtail);
                    if (String.valueOf(userDEtail.getID()).equalsIgnoreCase(Common.getUserIdFromSP(this)))
                        adapterusers.add("SELF");
                    else
                        adapterusers.add(cursor.getString(0));

                } while (cursor.moveToNext());
            }

            // do some stuff....
        } catch (Exception e) {
            e.printStackTrace();
        }

        spin_user.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, R.id.customSpinnerItemTextView, adapterusers));
        spin_user.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {

            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                sel_userId = String.valueOf(userses.get(position).getID());
                Log.e("user spinner id", sel_userId);
                tvInvisibleError.setError(null);
                if (userId.equalsIgnoreCase("0")) /*{*/
                    sel_userId = Common.getUserIdFromSP(PlanScheduleFormActivity.this);
//                } else {
                new AsyncUsers().execute();
                ll_select.setVisibility(View.VISIBLE);
//                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



       /* tv_name.setText(getIntent().getStringExtra("customer_name"));
        tv_code.setText(getIntent().getStringExtra("customer_code"));*/


    }

    private void updateplanner() {
        String selectQuery = null;
        if (role == Constants.Roles.ROLE_7) {
            selectQuery = "SELECT DISTINCT "
                    + KEY_EMP_CONCERN_PERSON_NAME + ","
                    + KEY_EMP_RETAILER_ID + ","
                    + KEY_EMP_FARMER_ID + ","
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
                    + KEY_EMP_VISIT_CROP_ID + ","
                    + KEY_EMP_REGION_ID + ","
                    + KEY_EMP_LOCATION_DISTRICT + ","
                    + KEY_EMP_LOCATION_TALUKA + ","
                    + KEY_EMP_LOCATION_VILLAGE + ","
                    + KEY_EMP_GSTIN_NO + ","
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
                    + KEY_EMP_VISIT_ID + ","
                    + KEY_EMP_PURPOSE_VISIT_IDS
                    + " FROM " + TABLE_EMPLOYEE_VISIT_MANAGEMENT + "  where " + KEY_EMP_VISIT_USER_ID + " = " + userId + " and " + KEY_EMP_FFM_ID + " = 0" + " and " + KEY_EMP_TYPE + "= 1";
        } else if (role != Constants.Roles.ROLE_7) {
            selectQuery = "SELECT DISTINCT "
                    + KEY_EMP_CONCERN_PERSON_NAME + ","
                    + KEY_EMP_RETAILER_ID + ","
                    + KEY_EMP_FARMER_ID + ","
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
                    + KEY_EMP_VISIT_CROP_ID + ","
                    + KEY_EMP_REGION_ID + ","
                    + KEY_EMP_LOCATION_DISTRICT + ","
                    + KEY_EMP_LOCATION_TALUKA + ","
                    + KEY_EMP_LOCATION_VILLAGE + ","
                    + KEY_EMP_GSTIN_NO + ","
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
                    + KEY_EMP_VISIT_ID + ","
                    + KEY_EMP_PURPOSE_VISIT_IDS
                    + " FROM " + TABLE_EMPLOYEE_VISIT_MANAGEMENT + "  where " + KEY_EMP_CREATED_BY + " = " + userId + " and " + KEY_EMP_FFM_ID + " = 0" + " and " + KEY_EMP_TYPE + "= 1";
        }


        Log.e("selectQuery", selectQuery);
        sdbw = db.getWritableDatabase();


        Cursor cursor = sdbw.rawQuery(selectQuery, null);
        if (cursor != null && cursor.moveToFirst()) {

            Log.e(" +++ Values +++ ", "type :" + cursor.getString(5) + " : " + cursor.getString(6) + " : " + cursor.getString(21) + ": checkintime" + cursor.getString(13) + ": sqlite id" + cursor.getString(27) + " 28: " + cursor.getString(28));

            if (Common.haveInternet(getApplicationContext())) {
                new Async_UpdatePlaner(cursor).execute();
            }

            //while (cursor.moveToNext());
        } else Log.d("LOG", "returned null!");


    }

    private void setplanvisittypeData() {

        final ArrayList<Visittypes> visittypeList = new ArrayList<>();

        visittypeList.add(new Visittypes(" -- Select -- ", "Please Select Visit Type"));
        visittypeList.add(new Visittypes("1", "Distributor"));
        visittypeList.add(new Visittypes("2", "Retailer"));
        visittypeList.add(new Visittypes("3", "Farmer"));


//        final ArrayList<Visittypes> purposeList = new ArrayList<>();
//        //Add countries
//        purposeList.add(new Visittypes("0", " Select Purpose "));
//       /* purposeList.add(new Visittypes("1", "Payment Collection"));
//        purposeList.add(new Visittypes("6", "ABS"));
//        purposeList.add(new Visittypes("5", "Sales order"));
//        purposeList.add(new Visittypes("4", "Stock Information"));*/
//
//        purposeList.add(new Visittypes("1", "Payment Collection"));
//        purposeList.add(new Visittypes("6", "Marketing"));
//        purposeList.add(new Visittypes("5", "Sales order"));
//        purposeList.add(new Visittypes("4", "Stock Supply"));
//        purposeList.add(new Visittypes("4", "Market Intelligence"));


        languagesList = new ArrayList<>();
        //Add countries
        //languagesList.add(new Filminstitutes(" -- Select -- ", " -- Select -- "));
        languagesList.add(new Filminstitutes(" -- Select -- ", " Select Purpose "));
        languagesList.add(new Filminstitutes("1", "Payment Collection"));
        languagesList.add(new Filminstitutes("6", "ABS"));
        languagesList.add(new Filminstitutes("5", "Sales order"));
        languagesList.add(new Filminstitutes("4", "Stock Information"));


//        final ArrayList<Visittypes> visittyperetailerpurposeList = new ArrayList<>();
//        //Add countries
//        visittyperetailerpurposeList.add(new Visittypes("0", " Select Purpose "));
//        visittyperetailerpurposeList.add(new Visittypes("7", "POG"));
//
//        final ArrayList<Visittypes> visittypefarmerpurposeList = new ArrayList<>();
//        //Add countries
//        visittypefarmerpurposeList.add(new Visittypes(" -- Select -- ", " Select Purpose "));
//        visittypefarmerpurposeList.add(new Visittypes("3", "Feedback"));
//        visittypefarmerpurposeList.add(new Visittypes("2", "Complaint"));


        //fill data in spinner
       /* ArrayAdapter<Filminstitutes> adapter = new ArrayAdapter<Filminstitutes>(getApplicationContext(), android.R.layout.simple_spinner_dropdown_item, countryList);
        spin_filminstitute.setAdapter(adapter);*/

        final ArrayAdapter adapter = new ArrayAdapter<Visittypes>(getApplicationContext(), R.layout.spinner_item, R.id.customSpinnerItemTextView, visittypeList);
        spin_visittypes.setAdapter(adapter);
        spin_visittypes.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                tvInvisibleError.setError(null);
                Visittypes country = (Visittypes) parent.getSelectedItem();

                ///Toast.makeText(getApplicationContext(), "Institute id: "+country.getId()+",  Institute Name : "+country.getName(), Toast.LENGTH_SHORT).show();
                str_sel_visittypeid = country.getId();
                if (str_sel_visittypeid.equalsIgnoreCase("1") || str_sel_visittypeid.equalsIgnoreCase("2") || str_sel_visittypeid.equalsIgnoreCase("3")) {
                    visitersEnumList = PlannerEnum.getListByVisitTypeID(Integer.parseInt(str_sel_visittypeid));
                    Log.d("visiterList", visitersEnumList.toString());
                    purposeList = new ArrayList<>();
                    purposeList.clear();
                    for (PlannerEnum visitersEnum : visitersEnumList) {
                        PurposeVisit purposeVisit = new PurposeVisit();
                        purposeVisit.title = visitersEnum.getTitle();
                        purposeVisit.isChecked = false;
                        purposeList.add(purposeVisit);

                    }
                    if (purposeList != null && purposeList.size() > 0) {
                        text_purpose.setVisibility(View.VISIBLE);
                        spin_purpose.setVisibility(View.VISIBLE);
//                        new ArrayAdapter(getApplicationContext(), R.layout.spinner_item, R.id.customSpinnerItemTextView, purposeList.toArray(new String[0]));
//                        spin_purpose.setItems(purposeList);
                    } else {
                        text_purpose.setVisibility(View.GONE);
                        spin_purpose.setVisibility(View.GONE);
                    }
                } else {
                    text_purpose.setVisibility(View.GONE);
                    spin_purpose.setVisibility(View.GONE);
                }
                Common.hideKeyboard(PlanScheduleFormActivity.this);
                fieldsLayout.setVisibility(View.GONE);
                addressLayout.setVisibility(View.GONE);
                validate_mobile.setVisibility(View.GONE);
                if (str_sel_visittypeid.equalsIgnoreCase("1")) {
                    Common.Log.i("type 1");
                    auto_tv.setVisibility(View.VISIBLE);
                    line.setVisibility(View.VISIBLE);
                    et_name.setVisibility(View.GONE);
                    new_iv.setVisibility(View.GONE);
                    line1.setVisibility(View.GONE);
                    et_address.setVisibility(View.GONE);
//                    btn_new.setVisibility(View.GONE);
                    cropLayout.setVisibility(View.GONE);
                    regionLayout.setVisibility(View.GONE);
                    cropLine.setVisibility(View.GONE);
//                    goneFields(View.GONE);
                } else if (str_sel_visittypeid.equalsIgnoreCase("2")) {
                    Common.Log.i("type 2");
                    et_name.setText("");
//                    goneFields(View.GONE);
                    auto_tv.setVisibility(View.VISIBLE);
                    line.setVisibility(View.VISIBLE);
                    et_name.setVisibility(View.VISIBLE);
                    new_iv.setVisibility(View.GONE);
                    line1.setVisibility(View.VISIBLE);
                    et_address.setVisibility(View.GONE);
//                    enableFields(true);
                    et_fieldarea.setVisibility(View.GONE);
//                    btn_new.setVisibility(View.VISIBLE);
                    cropLayout.setVisibility(View.GONE);
                    cropLine.setVisibility(View.GONE);
//                    List<Retailer> allRetailers = db.getAllRetailers();
//                    if (allRetailers != null && allRetailers.size() > 0) {
//                        List<SelectedCities> selectedCities = new ArrayList<>();
//                        for (int i = 0; i < allRetailers.size(); i++) {
//                            SelectedCities object = new SelectedCities();
//                            object.setCityId(allRetailers.get(i).get_ret_masterid());
//                            object.setCityName(allRetailers.get(i).get_ret_name());
//                            object.setCustomerCode(allRetailers.get(i).get_ret_region_id());
//                            selectedCities.add(object);
//                        }
                    autoCompleteDistrictAdapter(new ArrayList<SelectedCities>());
                    autoCompleteRetailerAdapter(new ArrayList<SelectedCities>());

                    myRunnable = new MyRunnable();
                    myRunnable1 = new MyRunnable1();
//                    }
                } else if (str_sel_visittypeid.equalsIgnoreCase("3")) {
                    Common.Log.i("type 3");
                    et_name.setText("");
                    auto_tv.setVisibility(View.GONE);
                    line.setVisibility(View.GONE);
//                    goneFields(View.GONE);
//                    btn_new.setVisibility(View.VISIBLE);
                    et_name.setVisibility(View.VISIBLE);
                    new_iv.setVisibility(View.VISIBLE);
                    line1.setVisibility(View.VISIBLE);
                    et_address.setVisibility(View.GONE);
//                    enableFields(true);
                    et_gstin.setVisibility(View.GONE);

                    cropLayout.setVisibility(View.VISIBLE);
                    cropLine.setVisibility(View.VISIBLE);
                    setCropData();
                    setRegionsData();
                    autoCompleteDistrictAdapter(new ArrayList<SelectedCities>());
                    autoCompleteRetailerAdapter(new ArrayList<SelectedCities>());
                    myRunnable = new MyRunnable();
                    myRunnable1 = new MyRunnable1();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });

        spin_purpose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (spin_purpose.getText().toString().trim().length() > 0) {
                    String[] split = spin_purpose.getText().toString().trim().split(",");
                    checkedList.clear();
                    for (int i = 0; i < split.length; i++) {
                        checkedList.add(split[i]);
                    }
                }
                checkboxPopup();
            }
        });
       /* spin_purpose.setListener(new MultiSelectionSpinner.OnMultipleItemsSelectedListener() {
            @Override
            public void selectedIndices(List<Integer> indices) {
                Log.d("selectedStrings", indices.toString() );
            }

            @Override
            public void selectedStrings(List<String> strings) {
                Log.d("selectedStrings", strings.toString() );
            }
        });
        spin_purpose.setListener(new MultiSelectionSpinner.OnMultipleItemsSelectedListener() {
            @Override
            public void selectedIndices(List<Integer> indices) {

                StringBuffer buffer = new StringBuffer();
                Log.d("selectedStrings", indices.toString() );

               *//* for(Integer index : indices){
                    Filminstitutes country = (Filminstitutes) languagesList.get(index);
                    str_sel_languagesknown = country.getId();

                    if(!str_sel_languagesknown.equals(" -- Select -- "))
                        buffer.append(str_sel_languagesknown);

                    edtv_langvalidate.setError(null);

                    //if the value is not the last element of the list
                    //then append the comma(,) as well
                    if (!index.equals(indices.size()-1)){
                        buffer.append(",");
                    }
                }
                str_sel_languagesknown = buffer.toString();*//*
            }

            @Override
            public void selectedStrings(List<String> strings) {
                visiterIds = getVisiterIdByName(strings);
                Log.d("selectedStrings", strings.toString() + "\n" + getVisiterIdByName(strings));

            }
        });
*/

    }

    private void setCropData() {
        cropsList = db.getAllCrops();
        if (cropsList != null && cropsList.size() > 0) {
            final List<SpinnerModel1> spinnerModel1s = new ArrayList<>();
            for (int i = 0; i < cropsList.size(); i++) {
                SpinnerModel1 spinnerModel1 = new SpinnerModel1();
                spinnerModel1.id = cropsList.get(i)._crop_masterid;
                spinnerModel1.name = cropsList.get(i)._crop_name;
                spinnerModel1s.add(spinnerModel1);
            }





           /* final CustomSpinnerAdapter adapter = new CustomSpinnerAdapter(getApplication(), R.layout.spinner_item, spinnerModel1s);
            spin_crop.setAdapter(adapter);
            spin_crop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                    cropId = spinnerModel1s.get(i).id;
                }

                @Override
                public void onNothingSelected(AdapterView<?> adapterView) {

                }
            });*/
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

    public class AsyncUsers extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PlanScheduleFormActivity.this);
            progressDialog.setMessage("Please wait ...All USers");
            progressDialog.setCancelable(true);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            arlist_users = new ArrayList<SelectedCities>();
            adapter_users = new ArrayList<String>();
            arlist_users.clear();

            SelectedCities citiesz = new SelectedCities();
            citiesz.setCityId("0");
            citiesz.setCityName(Common.getStringResourceText(R.string.select_distributor));
            // System.out.println("city id is :" + cityId + "city name is :" + cityName);
            // arlist_users.add(citiesz);
            // adapter_users.add(Common.getStringResourceText(R.string.select_distributor));


            try {
                String selectQuery = null;
                if (role == Constants.Roles.ROLE_7) {
//                    if (userId.equalsIgnoreCase(Common.getUserIdFromSP(context)))
                    selectQuery = "SELECT  " + "CR." + KEY_TABLE_CUSTOMER_MASTER_ID + "," + KEY_TABLE_CUSTOMER_NAME + "," + KEY_TABLE_CUSTOMER_REGION_ID + "," + KEY_TABLE_CUSTOMER_CODE + ",CR." + KEY_TABLE_CUSTOMER_CITY + " FROM " + TABLE_USER_COMPANY_CUSTOMER + " AS CDC JOIN " + TABLE_CUSTOMERS + " AS CR ON CDC." + KEY_TABLE_USER_COMPANY_CUSTOMER_CUSTOMER_ID + " = CR." + KEY_TABLE_CUSTOMER_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_CUSTOMER_USER_ID + " in (" + Common.getTeamFromSP(context) + " ) and CR." + KEY_TABLE_USER_COMPANY_CUSTOMER_STATUS + " = 1" + " and CDC." + KEY_TABLE_USER_COMPANY_CUSTOMER_STATUS + " = 1 group by(CR." + KEY_TABLE_CUSTOMER_MASTER_ID + ")";
//                    else
//                        selectQuery = "SELECT  " + "CR." + KEY_TABLE_CUSTOMER_MASTER_ID + "," + KEY_TABLE_CUSTOMER_NAME + "," + KEY_TABLE_CUSTOMER_CODE + ",CR." + KEY_TABLE_CUSTOMER_CITY + " FROM " + TABLE_USER_COMPANY_CUSTOMER + " AS CDC JOIN " + TABLE_CUSTOMERS + " AS CR ON CDC." + KEY_TABLE_USER_COMPANY_CUSTOMER_CUSTOMER_ID + " = CR." + KEY_TABLE_CUSTOMER_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_CUSTOMER_USER_ID + " = " + userId + " and CR." + KEY_TABLE_USER_COMPANY_CUSTOMER_STATUS + " = 1" + " and CDC." + KEY_TABLE_USER_COMPANY_CUSTOMER_STATUS + " = 1 group by(CR." + KEY_TABLE_CUSTOMER_MASTER_ID + ")";

                }
                if (role != Constants.Roles.ROLE_7) {
//                    if (sel_userId.equalsIgnoreCase(Common.getUserIdFromSP(context)))
                    selectQuery = "SELECT  " + "CR." + KEY_TABLE_CUSTOMER_MASTER_ID + "," + KEY_TABLE_CUSTOMER_NAME + "," + KEY_TABLE_CUSTOMER_REGION_ID + "," + KEY_TABLE_CUSTOMER_CODE + ",CR." + KEY_TABLE_CUSTOMER_CITY + " FROM " + TABLE_USER_COMPANY_CUSTOMER + " AS CDC JOIN " + TABLE_CUSTOMERS + " AS CR ON CDC." + KEY_TABLE_USER_COMPANY_CUSTOMER_CUSTOMER_ID + " = CR." + KEY_TABLE_CUSTOMER_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_CUSTOMER_USER_ID + " in (" + db.getTeam(sel_userId) + " ) and CR." + KEY_TABLE_USER_COMPANY_CUSTOMER_STATUS + " = 1" + " and CDC." + KEY_TABLE_USER_COMPANY_CUSTOMER_STATUS + " = 1 group by(CR." + KEY_TABLE_CUSTOMER_MASTER_ID + ")";
//                    else
//                        selectQuery = "SELECT  " + "CR." + KEY_TABLE_CUSTOMER_MASTER_ID + "," + KEY_TABLE_CUSTOMER_NAME + "," + KEY_TABLE_CUSTOMER_CODE + ",CR." + KEY_TABLE_CUSTOMER_CITY + " FROM " + TABLE_USER_COMPANY_CUSTOMER + " AS CDC JOIN " + TABLE_CUSTOMERS + " AS CR ON CDC." + KEY_TABLE_USER_COMPANY_CUSTOMER_CUSTOMER_ID + " = CR." + KEY_TABLE_CUSTOMER_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_CUSTOMER_USER_ID + " = " + sel_userId + " and CR." + KEY_TABLE_USER_COMPANY_CUSTOMER_STATUS + " = 1" + " and CDC." + KEY_TABLE_USER_COMPANY_CUSTOMER_STATUS + " = 1 group by(CR." + KEY_TABLE_CUSTOMER_MASTER_ID + ")";
                }


                //List<Company_division_crops> cdclist = db.getAllCompany_division_crops();

                sdbw = db.getWritableDatabase();

                Cursor cursor = sdbw.rawQuery(selectQuery, null);
                //System.out.println("cursor count "+cursor.getCount());
                if (cursor.moveToFirst()) {
                    do {
                        Customers customers = new Customers();
                        customers.setCusMasterID(cursor.getString(0));
                        customers.setCusName(cursor.getString(1));
                        customers.setCusregion_Id(cursor.getString(cursor.getColumnIndex(KEY_TABLE_CUSTOMER_REGION_ID)));
                        // customers.setUser_last_name(cursor.getString(2));
                        String city = cursor.getString(3) == null || cursor.getString(3).equalsIgnoreCase("null") ? "" : cursor.getString(3);

                        SelectedCities cities2 = new SelectedCities();
                        cities2.setCityId(cursor.getString(0));
                        cities2.setCityName(cursor.getString(1) + " -" + city);
                        cities2.setRegionId(cursor.getString(cursor.getColumnIndex(KEY_TABLE_CUSTOMER_REGION_ID)));
                        // System.out.println("city id is :" + cityId + "city name is :" + cityName);
                        arlist_users.add(cities2);
                        adapter_users.add(cursor.getString(1) + " -" + city);


                    } while (cursor.moveToNext());
                }
            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (progressDialog != null || progressDialog.isShowing())
                progressDialog.dismiss();

            // adapter.updateResults(arrayList);
            autoCompleteAdapter(arlist_users);
            /*spin_customers.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, R.id.customSpinnerItemTextView, adapter_users));
            spin_customers.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    sel_customer_id = Integer.parseInt(arlist_users.get(position).getCityId());
                    //listView.setVisibility(View.INVISIBLE);
                    //
                    //Toast.makeText(getActivity(), categorytypeIdis, Toast.LENGTH_SHORT).show();
                    // Toast.makeText(getApplicationContext(), "apicalled" ,Toast.LENGTH_SHORT).show();

                }


                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });*/
        }
    }

    public class Async_RetailerChecking extends AsyncTask<String, String, String> {
        private final String type;
        private String mobileNumber;

        public Async_RetailerChecking(String mobileNumber, String type) {
            this.mobileNumber = mobileNumber;
            this.type = type;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PlanScheduleFormActivity.this);
            progressDialog.setMessage("Please wait ... Retailers");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {

                OkHttpClient client = new OkHttpClient();
                /*For passing parameters*/

                RequestBody formBody = new FormEncodingBuilder()
                        /* .add("mobile", String.valueOf(farmer.getPrimaryId()))
                         .add("farmer_name", farmer.getFarmerName())
                         .add("location_district", farmer.getLocationDistrict())
                         .add("location_taluka", farmer.getLocationTaluka())
                         .add("location_village", farmer.getLocationVillage())
                         .add("region_id", farmer.getRegionId())*/
                        .add("mobile", mobileNumber)
                        .add("type", type)

//                        .add("customer_id", checkuid)
                        .build();

                Response responses = null;

//                System.out.println("---- retailer data -----" + params[0] + params[1] + params[2] + params[3] + params[4] + params[5] + params[6] + params[7]);

                /*MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType,
                        "type=check_in_lat_lon&visit_type=1&user_id=7&latlon=17.4411%2C78.3911&visit_date=2016-12-05%2C18%3A27%3A30&check_in_time=18%3A27%3A30&tracking_id=0");*/
                Request request = new Request.Builder()
                        .url(Constants.URL_RETAILER_EXIST)
                        .post(formBody)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();


                try {
                    responses = client.newCall(request).execute();
                    jsonData = responses.body().string();
                    System.out.println("!!!!!!!1 InsertRetailer" + jsonData);

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
                Common.hideKeyboard(PlanScheduleFormActivity.this);
                JSONArray jsonarray;
                try {
                    JSONObject jsonobject = new JSONObject(jsonData);
                    System.out.println("!!!!!!!1 postexecuteInsertRetailer" + jsonData);

                    String status = jsonobject.getString("status");
                    if (status.equalsIgnoreCase("success")) {
                        JSONObject result = jsonobject.getJSONObject("result");
                        farmer = new FarmerPojo();
                        farmer.setFarmerId(result.getString("farmer_id"));
                        farmer.setFarmerName(result.getString("farmer_name"));
                        farmer.setMobile(result.getString("mobile"));
                        farmer.setRegionId(result.getString("region_id"));
                        farmer.setLocationDistrict(result.getString("location_district"));
                        farmer.setLocationTaluka(result.getString("location_taluka"));
                        farmer.setLocationVillage(result.getString("location_village"));
                        farmer.setTotalLandHolding(result.getString("total_land_holding"));
                        farmer.setStatus(result.getString("status"));
                        fieldsLayout.setVisibility(View.GONE);
                        addressLayout.setVisibility(View.VISIBLE);
                        fieldAreaLayout.setVisibility(View.VISIBLE);
                        gstinLayout.setVisibility(View.GONE);
                        regionId = farmer.getRegionId();
                        tv_region.setText(Common.isStringNull(db.getRegionById(regionId).getRegionName()));
                        tv_district.setText(farmer.getLocationDistrict().toUpperCase());
                        tv_taluka.setText(farmer.getLocationTaluka().toUpperCase());
                        tv_name.setText(farmer.getFarmerName().toUpperCase());
                        tv_village.setText(farmer.getLocationVillage().toUpperCase());
                        tv_fieldarea.setText(farmer.getTotalLandHolding().toUpperCase());
                        tv_mobile.setText(farmer.getMobile().toUpperCase());
                        farmerId = farmer.getFarmerId();
                        validate_mobile.setVisibility(View.GONE);
                        et_name.setVisibility(View.GONE);
                        new_iv.setVisibility(View.GONE);
                    } else {
                        fieldsLayout.setVisibility(View.VISIBLE);
                        if (str_sel_visittypeid.equalsIgnoreCase("3")) {
                            et_fieldarea.setVisibility(View.VISIBLE);
                            et_gstin.setVisibility(View.GONE);
//                            et_mobile.setText(validate_mobile.getText().toString().trim());
                            et_mobile.setVisibility(View.GONE);
                        }
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

    public class Async_UpdatePlaner extends AsyncTask<Void, Void, String> {
        Cursor cursor;

        public Async_UpdatePlaner(Cursor cursor) {
            this.cursor = cursor;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            /*progressDialog = new ProgressDialog(PlanScheduleFormActivity.this);
            progressDialog.setMessage("Please wait ...");
            progressDialog.setCancelable(false);
            progressDialog.show();*/
            //Toast.makeText(getApplicationContext(),"Updateing Advance Bookings",Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(Void... params) {

            try {
                OkHttpClient client = new OkHttpClient();
                /*For passing parameters*/
                RequestBody formBody = new FormEncodingBuilder()

                        .add("type", cursor.getString(cursor.getColumnIndex(KEY_EMP_TYPE)))
                        .add("geo_tracking_id", cursor.getString(cursor.getColumnIndex(KEY_EMP_GEO_TRACKING_ID)))
                        .add("user_id", cursor.getString(cursor.getColumnIndex(KEY_EMP_VISIT_USER_ID)))
                        .add("customer_id", cursor.getString(cursor.getColumnIndex(KEY_EMP_VISIT_CUSTOMER_ID)))
                        .add("visit_plan_type", cursor.getString(cursor.getColumnIndex(KEY_EMP_VISIT_PLAN_TYPE)))
                        .add("purpose_visit", cursor.getString(cursor.getColumnIndex(KEY_EMP_PURPOSE_VISIT_IDS)))
                        .add("plan_date_time", cursor.getString(cursor.getColumnIndex(KEY_EMP_PLAN_DATE_TIME)))
                        .add("concern_person_name", cursor.getString(cursor.getColumnIndex(KEY_EMP_CONCERN_PERSON_NAME)))
                        .add("retailer_id", cursor.getString(cursor.getColumnIndex(KEY_EMP_RETAILER_ID)))
                        .add("farmer_id", cursor.getString(cursor.getColumnIndex(KEY_EMP_FARMER_ID)))
                        .add("mobile", cursor.getString(cursor.getColumnIndex(KEY_EMP_MOBILE)))
                        .add("village", cursor.getString(cursor.getColumnIndex(KEY_EMP_VILLAGE)))
                        .add("location_address", cursor.getString(cursor.getColumnIndex(KEY_EMP_LOCATION_ADDRESS)))
                        .add("crop_id", cursor.getString(cursor.getColumnIndex(KEY_EMP_VISIT_CROP_ID)))
                        .add("region_id", cursor.getString(cursor.getColumnIndex(KEY_EMP_REGION_ID)))
                        .add("location_district", cursor.getString(cursor.getColumnIndex(KEY_EMP_LOCATION_DISTRICT)))
                        .add("location_taluka", cursor.getString(cursor.getColumnIndex(KEY_EMP_LOCATION_TALUKA)))
                        .add("location_village", cursor.getString(cursor.getColumnIndex(KEY_EMP_LOCATION_VILLAGE)))
                        .add("retailer_gstin_no", cursor.getString(cursor.getColumnIndex(KEY_EMP_GSTIN_NO)))
                        .add("field_area", cursor.getString(cursor.getColumnIndex(KEY_EMP_FEILD_AREA)))
                        .add("id", cursor.getString(cursor.getColumnIndex(KEY_EMP_VISIT_ID)))
                        .add("comments", cursor.getString(cursor.getColumnIndex(KEY_EMP_COMMENTS)))
                        .add("status", cursor.getString(cursor.getColumnIndex(KEY_EMP_STATUS)))
                        .add("approval_status", cursor.getString(cursor.getColumnIndex(KEY_EMP_APPROVAL_STATUS)))
                        .add("event_name", cursor.getString(cursor.getColumnIndex(KEY_EMP_EVENT_NAME)))
                        .add("event_end_date", cursor.getString(cursor.getColumnIndex(KEY_EMP_EVENT_END_DATE)))
                        .add("event_purpose", cursor.getString(cursor.getColumnIndex(KEY_EMP_EVENT_PURPOSE)))
                        .add("event_venue", cursor.getString(cursor.getColumnIndex(KEY_EMP_EVENT_VENUE)))
                        .add("event_participants", cursor.getString(cursor.getColumnIndex(KEY_EMP_EVENT_PARTICIPANTS)))
                        .add("created_by", cursor.getString(cursor.getColumnIndex(KEY_EMP_CREATED_BY)))
                        .add("updated_by", cursor.getString(cursor.getColumnIndex(KEY_EMP_UPDATED_BY)))
                        .build();

                Response responses = null;
                Common.Log.i("Form Data " + new Gson().toJson(formBody));

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


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Common.dismissProgressDialog(progressDialog1);

            if (jsonData != null) {
                JSONArray jsonarray;
                try {
                    JSONObject jsonobject = new JSONObject(jsonData);
                    String status = jsonobject.getString("status");
                    if (status.equalsIgnoreCase("success")) {


                        sqliteid = jsonobject.getInt("sqlite");
                        ffmid = jsonobject.getInt("ffm_id");
                        approvalStatus = jsonobject.getInt("approval_status");

                        //   Toast.makeText(getActivity(), "sqlite id and ffm id " + sqliteid + " , " + ffmid, Toast.LENGTH_SHORT).show();

                        sql = db.getWritableDatabase();
                        // updatecomplaints
                        String updatequery = "UPDATE " + TABLE_EMPLOYEE_VISIT_MANAGEMENT + " SET " + KEY_EMP_FFM_ID + " = " + ffmid + "," + KEY_EMP_APPROVAL_STATUS + " = " + approvalStatus + " WHERE " + KEY_EMP_VISIT_ID + " = " + sqliteid;
                        sql.execSQL(updatequery);

                        /*JSONObject planner_approval = jsonobject.getJSONObject("planner_approval");
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
                        db.insertPlannerApproval(plannerApproval);*/

                        Intent planer = new Intent();
                        planer.putExtra("date", str_date);
                        planer.putExtra("i", "1");
                        setResult(Activity.RESULT_OK, planer);
                        finish();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                    Intent planer = new Intent();
                    planer.putExtra("date", str_date);
                    planer.putExtra("i", "1");
                    setResult(Activity.RESULT_OK, planer);
                    finish();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                // Toast.makeText(getApplicationContext(),"No data found",Toast.LENGTH_LONG).show();
            }

        }
    }

    public class Async_InsertRetailer extends AsyncTask<String, String, String> {
        private Retailer retailer;

        public Async_InsertRetailer(Retailer retailer) {
            this.retailer = retailer;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PlanScheduleFormActivity.this);
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
                        .add("mobile_id", String.valueOf(retailer.getID()))
                        .add("retailer_name", retailer.get_ret_name())
                        .add("retailer_tin_no", retailer.get_ret_tin_no())
                        .add("location_district", retailer.get_ret_district())
                        .add("location_taluka", retailer.get_ret_taluka())
                        .add("location_village", retailer.get_ret_village())
                        .add("retailer_gstin_no", retailer.get_ret_gstin_no())
                        .add("region_id", retailer.get_ret_region_id())
                        .add("mobile", retailer.get_ret_mobile())
                        .add("phone", retailer.get_ret_phone())
                        .add("email_id", retailer.get_email())
                        .add("distributor_id", retailer.get_ret_dist_id())
                        .add("status", retailer.get_ret_status())
                        .add("ffmid", retailer.get_ffmid())

//                        .add("customer_id", checkuid)
                        .build();

                Response responses = null;

//                System.out.println("---- retailer data -----" + params[0] + params[1] + params[2] + params[3] + params[4] + params[5] + params[6] + params[7]);

                /*MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType,
                        "type=check_in_lat_lon&visit_type=1&user_id=7&latlon=17.4411%2C78.3911&visit_date=2016-12-05%2C18%3A27%3A30&check_in_time=18%3A27%3A30&tracking_id=0");*/
                Request request = new Request.Builder()
                        .url(Constants.URL_INSERTING_RETAILER)
                        .post(formBody)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();


                try {
                    responses = client.newCall(request).execute();
                    jsonData = responses.body().string();
                    System.out.println("!!!!!!!1 InsertRetailer" + jsonData);

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
                        //  Toast.makeText(getActivity(), "Feed back inserted sucessfully", Toast.LENGTH_SHORT).show();
//                        sqliteid = jsonobject.getString("sqlite");
                        String exists = jsonobject.getString("exists");
                        if (exists.equalsIgnoreCase("0")) {
                            retailer.setID(Integer.parseInt(jsonobject.getString("mobile_id")));
                            retailer.set_ffmid(jsonobject.getString("ffm_id"));
                            retailer.set_ret_masterid(jsonobject.getString("retailer_id"));
                            retailerId = retailer.get_ret_masterid();
                            db.addRetailers(retailer);
                            DistributorsRetailerPojo distributorsRetailerPojo = new DistributorsRetailerPojo();
                            distributorsRetailerPojo.distributorId = String.valueOf(sel_customer_id);
                            distributorsRetailerPojo.retailerId = String.valueOf(db.getSqlPrimaryKeyByFFMID(retailerId));
                            db.insertDistributorRetailers(distributorsRetailerPojo);
//                        jsonobject.getString("id");
//                        //   Toast.makeText(getActivity(), "sqlite id and ffm id " + sqliteid + " , " + ffmid, Toast.LENGTH_SHORT).show();
////                        Log.e("sqlite id", sqliteid);
////                        Log.e("ffmid", ffmid);
//                        sdbw = db.getWritableDatabase();
//                        // updateFeedback(Feedback feedback);
//                        String updatequery = "UPDATE " + TABLE_RETAILER + " SET " + KEY_TABLE_RETAILER_FFMID + " = " + ffmid + " WHERE " + KEY_TABLE_RETAILER_PRIMARY_ID + " = " + retailer.getID();
//                        sdbw.execSQL(updatequery);
//                        System.out.println(updatequery);
                            postPlannerData();
                        } else {
                            db.deleteRetailer(Integer.parseInt(jsonobject.getString("mobile_id")));
                            Common.dismissProgressDialog(progressDialog1);
                            Toast.makeText(PlanScheduleFormActivity.this, "Retailer existed with this Mobile Number", Toast.LENGTH_SHORT).show();
                        }
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

    public class Async_InsertFarmer extends AsyncTask<String, String, String> {
        private FarmerPojo farmer;

        public Async_InsertFarmer(FarmerPojo farmer) {
            this.farmer = farmer;
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(PlanScheduleFormActivity.this);
            progressDialog.setMessage("Please wait ... Farmer");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {

                OkHttpClient client = new OkHttpClient();
                /*For passing parameters*/

                RequestBody formBody = new FormEncodingBuilder()
//                        .add("farmer_primary_id", String.valueOf(farmer.getPrimaryId()))
                        .add("farmer_name", farmer.getFarmerName())
                        .add("location_district", farmer.getLocationDistrict())
                        .add("location_taluka", farmer.getLocationTaluka())
                        .add("location_village", farmer.getLocationVillage())
                        .add("region_id", farmer.getRegionId())
                        .add("mobile", farmer.getMobile())
                        .add("total_land_holding", farmer.getTotalLandHolding())

//                        .add("customer_id", checkuid)
                        .build();

                Response responses = null;

//                System.out.println("---- retailer data -----" + params[0] + params[1] + params[2] + params[3] + params[4] + params[5] + params[6] + params[7]);

                /*MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType,
                        "type=check_in_lat_lon&visit_type=1&user_id=7&latlon=17.4411%2C78.3911&visit_date=2016-12-05%2C18%3A27%3A30&check_in_time=18%3A27%3A30&tracking_id=0");*/
                Request request = new Request.Builder()
                        .url(Constants.URL_INSERTING_FARMER)
                        .post(formBody)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();


                try {
                    responses = client.newCall(request).execute();
                    jsonData = responses.body().string();
                    System.out.println("!!!!!!!1 InsertRetailer" + jsonData);

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
                        //  Toast.makeText(getActivity(), "Feed back inserted sucessfully", Toast.LENGTH_SHORT).show();
//                        sqliteid = jsonobject.getString("sqlite");
//                        String exists = jsonobject.getString("exists");
//                        if (exists.equalsIgnoreCase("0")) {
//                            farmer.setPrimaryId(Integer.parseInt(jsonobject.getString("mobile_id")));
                        farmer.setFfmId(jsonobject.getString("ffm_id"));
                        farmer.setFarmerId(jsonobject.getString("farmer_id"));
                        farmerId = farmer.getFarmerId();
                        db.addFarmer(farmer);
//                        jsonobject.getString("id");
//                        //   Toast.makeText(getActivity(), "sqlite id and ffm id " + sqliteid + " , " + ffmid, Toast.LENGTH_SHORT).show();
////                        Log.e("sqlite id", sqliteid);
////                        Log.e("ffmid", ffmid);
//                        sdbw = db.getWritableDatabase();
//                        // updateFeedback(Feedback feedback);
//                        String updatequery = "UPDATE " + TABLE_RETAILER + " SET " + KEY_TABLE_RETAILER_FFMID + " = " + ffmid + " WHERE " + KEY_TABLE_RETAILER_PRIMARY_ID + " = " + retailer.getID();
//                        sdbw.execSQL(updatequery);
//                        System.out.println(updatequery);
                        postPlannerData();
//                        } else {
//                            db.deleteFarmer(Integer.parseInt(jsonobject.getString("mobile_id")));
//                            Common.dismissProgressDialog(progressDialog1);
//                            Toast.makeText(PlanScheduleFormActivity.this, "Farmer existed with this Mobile Number", Toast.LENGTH_SHORT).show();

//                        }

                    } else {
                        Common.disableClickEvent(btn_savebooing, true);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                    Common.disableClickEvent(btn_savebooing, true);
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                // Toast.makeText(getApplicationContext(),"No data found",Toast.LENGTH_LONG).show();
            }

//            finish();

        }
    }

    /**
     * A method to download json data from url
     */
    private String downloadUrl(String strUrl) throws IOException {
        String data = "";
        InputStream iStream = null;
        HttpURLConnection urlConnection = null;
        try {
            URL url = new URL(strUrl);

            // Creating an http connection to communicate with url
            urlConnection = (HttpURLConnection) url.openConnection();

            // Connecting to url
            urlConnection.connect();

            // Reading data from url
            iStream = urlConnection.getInputStream();

            BufferedReader br = new BufferedReader(new InputStreamReader(iStream));

            StringBuffer sb = new StringBuffer();

            String line = "";
            while ((line = br.readLine()) != null) {
                sb.append(line);
            }

            data = sb.toString();

            br.close();

        } catch (Exception e) {
            Log.d("Exceptionadinl", e.toString());
        } finally {
            iStream.close();
            urlConnection.disconnect();
        }
        return data;
    }

    // Fetches all places from GooglePlaces AutoComplete Web Service
    private class PlacesTask extends AsyncTask<String, Void, String> {

        @Override
        protected String doInBackground(String... place) {
            // For storing data from web service
            String data = "";

            // Obtain browser key from https://code.google.com/apis/console
            String key = "key=AIzaSyBxY-hkH-tCLLaP2ajbMUz9EnQYUn05TLI";

            String input = "";

            try {
                input = "input=" + URLEncoder.encode(place[0], "utf-8");
            } catch (UnsupportedEncodingException e1) {
                e1.printStackTrace();
            }

            // place type to be searched
            String types = "types=geocode";

            // Sensor enabled
            String sensor = "sensor=false";

            // Building the parameters to the web service
            String parameters = input + "&" + types + "&" + sensor + "&" + key;

            // Output format
            String output = "json";

            // Building the url to the web service
            String url = "https://maps.googleapis.com/maps/api/place/autocomplete/" + output + "?" + parameters;

            try {
                // Fetching the data from we service
                data = downloadUrl(url);
            } catch (Exception e) {
                Log.d("Background Task", e.toString());
            }
            return data;
        }

        @Override
        protected void onPostExecute(String result) {
            super.onPostExecute(result);

            // Creating ParserTask
            ParserTask parserTask = new ParserTask();

            // Starting Parsing the JSON string returned by Web Service
            parserTask.execute(result);
        }
    }

    /**
     * A class to parse the Google Places in JSON format
     */
    private class ParserTask extends AsyncTask<String, Integer, List<HashMap<String, String>>> {

        JSONObject jObject;

        @Override
        protected List<HashMap<String, String>> doInBackground(String... jsonData) {

            List<HashMap<String, String>> places = null;

            PlaceJSONParser placeJsonParser = new PlaceJSONParser();

            try {
                jObject = new JSONObject(jsonData[0]);

                // Getting the parsed data as a List construct
                places = placeJsonParser.parse(jObject);

            } catch (Exception e) {
                Log.d("Exception", e.toString());
            }
            return places;
        }

        @Override
        protected void onPostExecute(List<HashMap<String, String>> result) {

            String[] from = new String[]{"description"};
            int[] to = new int[]{android.R.id.text1};

            // Creating a SimpleAdapter for the AutoCompleteTextView
            SimpleAdapter adapter = new SimpleAdapter(getBaseContext(), result, android.R.layout.simple_list_item_1, from, to);

            // Setting the adapter
            et_address.setAdapter(adapter);

            et_address.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                }
            });

        }
    }

    public String convertlistToString(List<String> experienceList) {
        //The string builder used to construct the string
        StringBuilder commaSepValueBuilder = new StringBuilder();
        String experience_key = "";
        //Looping through the list
        for (int i = 0; i < experienceList.size(); i++) {
            //append the value into the builder
            commaSepValueBuilder.append(experienceList.get(i));

            //if the value is not the last element of the list
            //then append the comma(,) as well
            if (i != experienceList.size() - 1) {
                commaSepValueBuilder.append(",");
            }
        }
        return commaSepValueBuilder.toString();
    }


    public static String getVisiterIdByName(List<String> names) {
        ArrayList<String> list = new ArrayList();
        list.clear();
        for (int i = 0; i < names.size(); i++) {
            for (PlannerEnum plannerEnum : PlannerEnum.values()) {
                String visitTitle = plannerEnum.getTitle();

                if (names.get(i).equalsIgnoreCase(visitTitle)) {
                    list.add(String.valueOf(plannerEnum.getRoleId()));

                }


            }


        }

        String joined = TextUtils.join(",", list);
        return joined;
    }

    public void autoCompleteAdapter(List<SelectedCities> list) {
        final CustomListAdapter adapter = new CustomListAdapter(this,
                R.layout.autocomplete_custom_layout, list);

        auto_tv.setAdapter(adapter);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                auto_tv.showDropDown();
            }
        }, 100);

        adapter.setOnItemClickListener(new CustomListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, SelectedCities item) {
                auto_tv.setText(item.getCityName().toUpperCase());
                auto_tv.setSelection(item.getCityName().length());
                auto_tv.dismissDropDown();
                Common.hideSoftKeyboard(PlanScheduleFormActivity.this);
                //    Toast.makeText(getApplicationContext(),item.getCityName()+"\n"+item.getCityId(),Toast.LENGTH_SHORT).show();
                Log.d("onItemClick", item.getCityId());

                sel_customer_id = Integer.parseInt(item.getCityId());
                customername = item.getCityName();
                cusRegionId = item.getRegionId();
                setRegionsData();

            }
        });
        auto_tv.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("afterTextChanged", "afterTextChanged" + s.length());
                if (s.length() == 0) {
                    // auto_tv.setAdapter(adapter);
                    auto_tv.showDropDown();
                }

            }
        });

        auto_tv.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (auto_tv.getText().length() == 0) {
                    //   auto_tv.setAdapter(adapter);
                    auto_tv.showDropDown();
                }
                return false;
            }
        });

        auto_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
    }

    class MyRunnable implements Runnable {
        private String text;

        public void setData(String text) {
            this.text = text;
        }

        @Override
        public void run() {
            if (this != null) {
                String key = "'%" + text + "%'";
//                SelectedCities newObj = new SelectedCities();
//                newObj.setExistData(false);
//                newObj.setCityId("0");
                if (str_sel_visittypeid.equalsIgnoreCase("2")) {
                    List<Retailer> lista = db.getAllRetailers(key, String.valueOf(sel_customer_id));
                    Log.d("afterTextChanged", "list: " + lista.size());
                    final List<SelectedCities> objects = new ArrayList<>();
                    for (int i = 0; i < lista.size(); i++) {
                        SelectedCities object = new SelectedCities();
                        object.setCityId(String.valueOf(lista.get(i).getID()));
                        object.setCityName(lista.get(i).get_ret_name());
                        object.setDistrict(lista.get(i).get_ret_district());
                        object.setTaluka(lista.get(i).get_ret_taluka());
                        object.setMobile(lista.get(i).get_ret_mobile());
                        object.setRetailerId(lista.get(i).get_ffmid());
                        objects.add(object);
                    }
//                    newObj.setCityName("New Retailer");
//                    if (Common.haveInternet(PlanScheduleFormActivity.this))
//                        objects.add(newObj);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!onItemClick)
                                autoCompleteRetailerAdapter(objects);
                            else onItemClick = false;
                        }
                    });
                } else if (str_sel_visittypeid.equalsIgnoreCase("3")) {
                    List<FarmerPojo> lista = db.getAllFarmers(key);
                    Log.d("afterTextChanged", "list: " + lista.size());
                    final List<SelectedCities> objects = new ArrayList<>();
                    for (int i = 0; i < lista.size(); i++) {
                        SelectedCities object = new SelectedCities();
                        object.setCityId(lista.get(i).getFarmerId());
                        object.setCityName(lista.get(i).getFarmerName());
                        object.setDistrict(lista.get(i).getLocationDistrict());
                        object.setTaluka(lista.get(i).getLocationTaluka());
                        object.setMobile(lista.get(i).getMobile());
                        objects.add(object);
                    }
//                    newObj.setCityName("New Farmer");
//                    if (Common.haveInternet(PlanScheduleFormActivity.this))
//                        objects.add(newObj);
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!onItemClick)
                                autoCompleteRetailerAdapter(objects);
                            else onItemClick = false;
                        }
                    });
                }
            }
        }
    }

    class MyRunnable1 implements Runnable {
        private String text;

        public void setData(String text) {
            this.text = text;
        }

        @Override
        public void run() {
            if (this != null) {
                String key = "'%" + text + "%'";
                if (str_sel_visittypeid.equalsIgnoreCase("2") || str_sel_visittypeid.equalsIgnoreCase("3")) {
                    List<District> districts = db.getDistrictsByRegionID(key, regionId);
                    final List<SelectedCities> distObjs = new ArrayList<>();
                    if (districts != null && districts.size() > 0) {
                        for (int i = 0; i < districts.size(); i++) {
                            SelectedCities obj = new SelectedCities();
                            obj.setCityId(districts.get(i).districtId);
                            obj.setCityName(districts.get(i).districtName);
                            distObjs.add(obj);
                        }
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!onDistrictItemClick && et_district.isEnabled()) {
                                districtId = "0";
                                autoCompleteDistrictAdapter(distObjs);
                            } else onDistrictItemClick = false;
                        }
                    });
                }

            }
        }
    }

    public class OptionsAdapter extends BaseAdapter {
        Activity context;

        StringBuffer buffer = new StringBuffer();

        public OptionsAdapter(Activity context) {
            this.context = context;

        }


        @Override
        public int getCount() {
            return purposeList.size();
        }

        @Override
        public Object getItem(int i) {
            return purposeList.get(i);
        }

        @Override
        public long getItemId(int i) {
            return i;
        }

        @Override
        public View getView(final int position, View view, ViewGroup viewGroup) {
            view = context.getLayoutInflater().inflate(R.layout.check_box_item, null);
            CheckBox optionBox = view.findViewById(R.id.chk_product);
            final EditText otherEt = view.findViewById(R.id.other_et);

            optionBox.setText(purposeList.get(position).title);
            optionBox.setChecked(purposeList.get(position).isChecked);
            if (purposeList.get(position).title.equalsIgnoreCase("Other") && purposeList.get(position).isChecked) {
                otherEt.setVisibility(View.VISIBLE);
                otherEt.setText(getOtherComments());
            } else {
                otherEt.setVisibility(View.GONE);
            }

            optionBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                    purposeList.get(position).isChecked = b;
                    if (purposeList.get(position).title.equalsIgnoreCase("Other")) {
                        if (purposeList.get(position).isChecked)
                            otherEt.setVisibility(View.VISIBLE);
                        else {
                            otherEt.setVisibility(View.GONE);
                        }
                    } else {
                        otherEt.setVisibility(View.GONE);
                    }
                    notifyDataSetChanged();
                }
            });

            otherEt.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                }

                @Override
                public void afterTextChanged(Editable editable) {
                    setOtherComments(editable.toString().trim());
                }
            });
            return view;
        }

    }

    private void setOtherComments(String trim) {
        this.comments = trim;
    }

    private String getOtherComments() {
        return comments;
    }
}
