package com.nsl.beejtantra.complaints;


import android.app.Activity;
import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.MediaStore;
import android.support.v4.app.Fragment;
import android.support.v4.content.CursorLoader;
import android.support.v4.view.ViewPager;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.MimeTypeMap;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.components.ComponentBase;
import com.google.gson.Gson;
import com.nsl.beejtantra.CapitalizeFirstLetter;
import com.nsl.beejtantra.Companies;
import com.nsl.beejtantra.Constants;
import com.nsl.beejtantra.Crops;
import com.nsl.beejtantra.Customers;
import com.nsl.beejtantra.DatabaseHandler;
import com.nsl.beejtantra.Divisions;
import com.nsl.beejtantra.MyApplication;
import com.nsl.beejtantra.PlanScheduleFormActivity;
import com.nsl.beejtantra.adapters.CustomListAdapter;
import com.nsl.beejtantra.adapters.CustomListAdapter1;
import com.nsl.beejtantra.commonutils.CompressImageQuality;
import com.nsl.beejtantra.marketintelligence.ProductSurveyAllActivity;
import com.nsl.beejtantra.Products;
import com.nsl.beejtantra.R;
import com.nsl.beejtantra.SelectedCities;
import com.nsl.beejtantra.Utility;
import com.nsl.beejtantra.commonutils.Common;
import com.nsl.beejtantra.network.BasicAuthInterceptor;
import com.nsl.beejtantra.network.RetrofitAPI;
import com.nsl.beejtantra.orderindent.NewSalesOrderChooseActivity;
import com.nsl.beejtantra.pojo.ComplaintReq;
import com.nsl.beejtantra.pojo.FarmerPojo;
import com.nsl.beejtantra.retailers.Retailer;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import static com.nsl.beejtantra.DatabaseHandler.KEY_PRODUCTS_COMPANY_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_PRODUCTS_DIVISION_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_PRODUCT_CROP_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_PRODUCT_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_PRODUCT_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_REGION_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_REGION_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_REGION__MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_COMPANIES_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_COMPANIES_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_COMPANY_DIVISION_CROPS_COMPANY_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_COMPANY_DIVISION_CROPS_CROP_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_COMPLAINTS_FFMID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_COMPLAINTS_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_COMPLAINTS_IMAGE_UPLOAD;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_COMPLAINTS_STAGES;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_COMPLAINTS_STATUS;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CROPS_CROP_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CROPS_CROP_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CUSTOMER_DISTRICT;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CUSTOMER_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CUSTOMER_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CUSTOMER_REGION_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CUSTOMER_STATE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_DIVISION_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_DIVISION_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USER_COMPANY_CUSTOMER_CUSTOMER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USER_COMPANY_CUSTOMER_STATUS;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USER_COMPANY_CUSTOMER_USER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USER_COMPANY_DIVISION_COMPANY_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USER_COMPANY_DIVISION_DIVISION_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USER_COMPANY_DIVISION_USER_ID;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_COMPANIES;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_COMPANY_DIVISION_CROPS;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_COMPLAINT;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_CROPS;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_CUSTOMERS;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_DIVISION;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_PRODUCTS;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_REGION;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_USER_COMPANY_CUSTOMER;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_USER_COMPANY_DIVISION;

/**
 * A simple {@link Fragment} subclass.
 */
public class FragmentComplaintsProducts extends Fragment {
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    private Button btnSelect, btnremove;
    private ImageView ivImage, ivImage1, ivImage2/*, ivImage4, ivImage5, ivImage6*/;
    private String userChoosenTask, result = "NO IMAGE";
    private static int currentPage = 0;
    private static int NUM_PAGES = 0;
    Spinner /*spin_company, spin_division,*/ spin_crop;
    Spinner spin_product;
    Spinner spin_regulatory;
    AutoCompleteTextView spin_distributor;
    Spinner spin_soil;
    String sel_comp_id, sel_division_id, sel_crop_id, sel_product_id, sel_distributor_id = "0";
    ProgressDialog progressDialog;
    private static final String HOME_URL = "http://www.mehndiqueens.com/indianbeauty/api/user/images";
    ViewPager mViewPager;
    String sqliteid, ffmid;
    DatabaseHandler db;
    String jsonData;
    //  private SQLiteDatabase sqldb;
    private static SQLiteDatabase sql, sdbr;
    FrameLayout fm;
    private ArrayList<SelectedCities> arlist_companies, arlist_divisions, arlist_crops, arlist_products, arlist_distributor;

    ArrayList<String> adapter_companies, adapter_divisions, adapter_crops, adapter_products, adapter_distributors;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    CapitalizeFirstLetter capital = new CapitalizeFirstLetter();
    LinearLayout ll_regulatory, ll_products;
    Spinner spin_complaint_type, performance_spinner;
    private String[] stringqone = {"Select Regulatory Type", "Sample Drawn", "Legal Metorology"};
    private String[] soil_type = {"Select Irrigation type", "Irrigated", "Rainfed"};
    private String[] COMPLAINT_TYPE = {"Select Complaint Type", "Germination%", "Offtype%", "Poor yields", "Packing Issue", "Others (Specify)"};
    private String[] PERFORMANCE_TYPE = {"Select", "Good", "Not Good"};
    String checkuid;
    LinearLayout complaintPerLayout, complaintRemLayout, complaintOthersLayout;

    EditText phone, complaintPer, dateOfComplaint, /*district, region, state, zone,*/
            dateOfSowing, inspectedDate, complaintRem, mkt_lot_num, others, contact, /*mandal,*/  /*vlg,*/
            bill, farmername, purdate, purqty, cmplqty, retailer, distributor, ofcer_name, comments, /*dealer,*/
            cmpltarea;
    TextView /*tvInvisibleError1, tvInvisibleError2,*/ tvInvisibleError3, tvInvisibleError4, tvInvisibleError5, tvInvisibleError6, tvInvisibleError11, tvInvisibleError12;
    Button submit, cancel;
    AutoCompleteTextView rtlr;
    private int imageViewId;
    private String base64String1;
    private String base64String2;
    private String base64String3;
    private String base64String4;
    private String base64String5;
    private String base64String6;
    private DatePickerDialog fromDatePickerDialog;
    //private SliderLayout imageSlider;
    SharedPreferences sharedpreferences;
    private SimpleDateFormat dateFormatter;
    public static final String mypreference = "mypref";
    String regexStr;
    private String pd;
    private Bitmap thumbnailUtils;
    private Uri imageUri;
    private ContentValues values;
    private List<File> files = new ArrayList<>();
    private List<String> filePaths = new ArrayList<>();
    private String dc;
    private String ds;
    private String inspectDate;
    private boolean onItemClick;
    private MyRunnable myRunnable;
    private Handler handler;
    private String retailerId = "0";
    private String customername;

    public FragmentComplaintsProducts() {
    }

    public FragmentComplaintsProducts(DatabaseHandler db) {
        this.db = db;
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        db = new DatabaseHandler(getActivity());
        sel_comp_id = getArguments().getString("company_id");
        sel_division_id = getArguments().getString("division_id");
        View view = inflater.inflate(R.layout.fragment_complaintsproducts, container, false);
        //db.deleteComplaints();
        fm = (FrameLayout) view.findViewById(R.id.frame);
        cancel = (Button) view.findViewById(R.id.btncancel);

        btnremove = (Button) view.findViewById(R.id.btndelete);

        ivImage = (ImageView) view.findViewById(R.id.ivImage);
        ivImage1 = (ImageView) view.findViewById(R.id.ivImage1);
        ivImage2 = (ImageView) view.findViewById(R.id.ivImage2);
       /* ivImage4 = (ImageView) view.findViewById(R.inspectDate.ivImage4);
        ivImage5 = (ImageView) view.findViewById(R.inspectDate.ivImage5);
        ivImage6 = (ImageView) view.findViewById(R.inspectDate.ivImage6);*/

        ivImage.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectImage();
                imageViewId = ivImage.getId();
            }
        });

        ivImage1.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                selectImage();
                imageViewId = ivImage1.getId();
            }
        });
        ivImage2.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                imageViewId = ivImage2.getId();
                selectImage();
            }
        });
       /* ivImage4.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                imageViewId = ivImage4.getId();
                selectImage();
            }
        });
        ivImage5.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                imageViewId = ivImage5.getId();
                selectImage();
            }
        });

        ivImage6.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                imageViewId = ivImage6.getId();
                selectImage();
            }
        });*/
        btnremove.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ivImage.setImageDrawable(null);
                fm.setVisibility(View.GONE);
                btnremove.setVisibility(View.GONE);
                result = "NO IMAGE";
            }
        });
//        tvInvisibleError1 = (TextView) view.findViewById(R.id.tvInvisibleError1);
//        tvInvisibleError2 = (TextView) view.findViewById(R.id.tvInvisibleError2);
        tvInvisibleError3 = (TextView) view.findViewById(R.id.tvInvisibleError3);
        tvInvisibleError4 = (TextView) view.findViewById(R.id.tvInvisibleError4);
        tvInvisibleError5 = (TextView) view.findViewById(R.id.tvInvisibleError5);
        tvInvisibleError6 = (TextView) view.findViewById(R.id.tvInvisibleError6);
        tvInvisibleError11 = view.findViewById(R.id.tvInvisibleError11);
        tvInvisibleError12 = view.findViewById(R.id.tvInvisibleError12);

//        sel_comp_id = "0";
//        spin_division = (Spinner) view.findViewById(R.id.spinDivision);
//        spin_company = (Spinner) view.findViewById(R.id.spinCompany);
        spin_crop = (Spinner) view.findViewById(R.id.spinCrop);
        spin_product = (Spinner) view.findViewById(R.id.spinProduct);
        spin_complaint_type = view.findViewById(R.id.complaint_type_spinner);
        // spin_distributor = (Spinner) view.findViewById(R.id.spinDistributor);
        spin_distributor = (AutoCompleteTextView) view.findViewById(R.id.spinDistributor);
        Typeface typeface = Typeface.createFromAsset(getActivity().getAssets(), Constants.CUSTOM_FONT_PATH_LIGHT);
        spin_distributor.setTypeface(typeface);

        complaintPerLayout = view.findViewById(R.id.complaint_per_layout);
        complaintRemLayout = view.findViewById(R.id.complaint_rem_layout);
        complaintOthersLayout = view.findViewById(R.id.complaint_others_layout);
        complaintPer = view.findViewById(R.id.complaint_per_et);
        complaintRem = view.findViewById(R.id.complaint_rem_et);
        dateOfComplaint = view.findViewById(R.id.date_of_complaint);
        /*dealer = view.findViewById(R.id.dealer_et);
        district = view.findViewById(R.id.etDistrict);
        region = view.findViewById(R.id.etRegion);
        state = view.findViewById(R.id.etState);
        zone = view.findViewById(R.id.etZone);*/
        dateOfSowing = view.findViewById(R.id.etSowing);
        performance_spinner = view.findViewById(R.id.performance_spinner);
        inspectedDate = view.findViewById(R.id.etInspectedDate);
        spin_soil = (Spinner) view.findViewById(R.id.spinSoilType);
        cmpltarea = (EditText) view.findViewById(R.id.etComplaintArea);
        mkt_lot_num = (EditText) view.findViewById(R.id.etMarketingLotNumber);
        others = (EditText) view.findViewById(R.id.etOthers);
        farmername = (EditText) view.findViewById(R.id.etFarmerName);
        contact = (EditText) view.findViewById(R.id.etContact);
        purqty = (EditText) view.findViewById(R.id.etPurchaseQuantity);
        cmplqty = (EditText) view.findViewById(R.id.etComplaintQuantity);
        purdate = (EditText) view.findViewById(R.id.etPurchaseDate);
        comments = (EditText) view.findViewById(R.id.etComments);
        purdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentDate = Calendar.getInstance();
                final int mYear = mcurrentDate.get(Calendar.YEAR);
                final int mMonth = mcurrentDate.get(Calendar.MONTH);
                final int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                        int selmon = selectedmonth + 1;
                        pd = selectedyear + "-" + selmon + "-" + selectedday + " 00:00:00";
                        purdate.setText(Common.setDateFormateOnTxt(selectedyear + "-" + selmon + "-" + selectedday));
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select date");
                mDatePicker.show();

            }
        });
        dateOfComplaint.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentDate = Calendar.getInstance();
                final int mYear = mcurrentDate.get(Calendar.YEAR);
                final int mMonth = mcurrentDate.get(Calendar.MONTH);
                final int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                        int selmon = selectedmonth + 1;
                        dc = selectedyear + "-" + selmon + "-" + selectedday + " 00:00:00";
                        dateOfComplaint.setText(Common.setDateFormateOnTxt(selectedyear + "-" + selmon + "-" + selectedday));
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select date");
                mDatePicker.show();

            }
        });
        inspectedDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentDate = Calendar.getInstance();
                final int mYear = mcurrentDate.get(Calendar.YEAR);
                final int mMonth = mcurrentDate.get(Calendar.MONTH);
                final int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                        int selmon = selectedmonth + 1;
                        inspectDate = selectedyear + "-" + selmon + "-" + selectedday + " 00:00:00";
                        inspectedDate.setText(Common.setDateFormateOnTxt(selectedyear + "-" + selmon + "-" + selectedday));
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select date");
                mDatePicker.show();

            }
        });
        dateOfSowing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Calendar mcurrentDate = Calendar.getInstance();
                final int mYear = mcurrentDate.get(Calendar.YEAR);
                final int mMonth = mcurrentDate.get(Calendar.MONTH);
                final int mDay = mcurrentDate.get(Calendar.DAY_OF_MONTH);


                DatePickerDialog mDatePicker = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
                    public void onDateSet(DatePicker datepicker, int selectedyear, int selectedmonth, int selectedday) {
                        // TODO Auto-generated method stub
                        int selmon = selectedmonth + 1;
                        ds = selectedyear + "-" + selmon + "-" + selectedday + " 00:00:00";
                        dateOfSowing.setText(Common.setDateFormateOnTxt(selectedyear + "-" + selmon + "-" + selectedday));
                    }
                }, mYear, mMonth, mDay);
                mDatePicker.setTitle("Select date");
                mDatePicker.show();

            }
        });
        bill = (EditText) view.findViewById(R.id.etBillnumber);
        rtlr = (AutoCompleteTextView) view.findViewById(R.id.etRetailer);
//        mandal = (EditText) view.findViewById(R.id.etMandal);
//        vlg = (EditText) view.findViewById(R.id.etVillage);
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent viewall = new Intent(getActivity(), ComplaintsprodallActivity.class);
                startActivity(viewall);
            }
        });
        submit = (Button) view.findViewById(R.id.btnSubmit);
        sharedpreferences = getActivity().getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        checkuid = sharedpreferences.getString("userId", "");
        ArrayAdapter NoCoreAdapter = new ArrayAdapter(getActivity(),
                R.layout.spinner_item, R.id.customSpinnerItemTextView, stringqone);

        ArrayAdapter soiltype = new ArrayAdapter(getActivity(),
                R.layout.spinner_item, R.id.customSpinnerItemTextView, soil_type);
        spin_soil.setAdapter(soiltype);

        final ArrayAdapter complaintTypeAdapter = new ArrayAdapter(getActivity(), R.layout.spinner_item, R.id.customSpinnerItemTextView, COMPLAINT_TYPE);
        spin_complaint_type.setAdapter(complaintTypeAdapter);
        ArrayAdapter performanceAdapter = new ArrayAdapter(getActivity(), R.layout.spinner_item, R.id.customSpinnerItemTextView, PERFORMANCE_TYPE);
        performance_spinner.setAdapter(performanceAdapter);

//        new AsyncCompaniesoffline().execute();
        new AsyncCropsoffline().execute();

        mkt_lot_num.addTextChangedListener(capital.capitalise(mkt_lot_num));
        others.addTextChangedListener(capital.capitalise(others));
        farmername.addTextChangedListener(capital.capitalise(farmername));
        contact.addTextChangedListener(capital.capitalise(contact));
        purqty.addTextChangedListener(capital.capitalise(purqty));
        cmplqty.addTextChangedListener(capital.capitalise(cmplqty));
        purdate.addTextChangedListener(capital.capitalise(purdate));
        comments.addTextChangedListener(capital.capitalise(comments));
        bill.addTextChangedListener(capital.capitalise(bill));
        rtlr.addTextChangedListener(capital.capitalise(rtlr));
//        mandal.addTextChangedListener(capital.capitalise(mandal));
//        vlg.addTextChangedListener(capital.capitalise(vlg));
        complaintPer.addTextChangedListener(capital.capitalise(complaintPer));
        complaintRem.addTextChangedListener(capital.capitalise(complaintRem));
        dateOfComplaint.addTextChangedListener(capital.capitalise(dateOfComplaint));
//        dealer.addTextChangedListener(capital.capitalise(dealer));
//        district.addTextChangedListener(capital.capitalise(district));
//        region.addTextChangedListener(capital.capitalise(region));
//        state.addTextChangedListener(capital.capitalise(state));
//        zone.addTextChangedListener(capital.capitalise(zone));
        dateOfSowing.addTextChangedListener(capital.capitalise(dateOfSowing));
        inspectedDate.addTextChangedListener(capital.capitalise(inspectedDate));

        spin_complaint_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                if (i == 0) {
                    complaintPerLayout.setVisibility(View.GONE);
                    complaintRemLayout.setVisibility(View.GONE);
                    complaintOthersLayout.setVisibility(View.GONE);
                } else if (i < 3 && i > 0) {
                    complaintPerLayout.setVisibility(View.VISIBLE);
                    complaintRemLayout.setVisibility(View.GONE);
                    complaintOthersLayout.setVisibility(View.GONE);
                } else if (i > 2 && i < 5) {
                    complaintRemLayout.setVisibility(View.VISIBLE);
                    complaintPerLayout.setVisibility(View.GONE);
                    complaintOthersLayout.setVisibility(View.GONE);
                } else if (i == 5) {
                    complaintRemLayout.setVisibility(View.GONE);
                    complaintPerLayout.setVisibility(View.GONE);
                    complaintOthersLayout.setVisibility(View.VISIBLE);
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validate();
            }
        });


        rtlr.addTextChangedListener(new

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
                                                    if (s.toString().trim().length() == 0) {
//                                                           btn_new.setVisibility(View.VISIBLE);
                                                        // auto_tv.setAdapter(adapter);
                                                        retailer = null;
                                                        retailerId = "0";

                                                    } else {
                                                        if (handler != null) {
                                                            handler.removeCallbacks(myRunnable);
                                                        }

                                                        if (myRunnable != null) {
                                                            handler = new Handler();
                                                            myRunnable.setData(s.toString().trim());
                                                            handler.postDelayed(myRunnable, 300);
                                                        }
                                                    }
                                                }
                                            });

        rtlr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (rtlr.getText().length() == 0) {
                    //   auto_tv.setAdapter(adapter);
                    rtlr.showDropDown();
                }
            }
        });
        return view;
    }

    public void autoCompleteRetailerAdapter(List<SelectedCities> list) {
        final CustomListAdapter1 adapter = new CustomListAdapter1(getActivity(),
                R.layout.autocomplete_custom_layout1, list);

        rtlr.setAdapter(adapter);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rtlr.showDropDown();
            }
        }, 100);

        adapter.setOnItemClickListener(new CustomListAdapter1.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, SelectedCities item) {
                onItemClick = true;
//                btn_new.setVisibility(View.GONE);
//                regionLayout.setVisibility(View.GONE);
                retailerId = item.getCityId();
                rtlr.setText(item.getCityName().toUpperCase());
                rtlr.setSelection(item.getCityName().length());
                Common.Log.i("SELECTED ITEM " + item.getCityName().toUpperCase());
                rtlr.dismissDropDown();
                Common.hideSoftKeyboard(getActivity());
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


    @Override
    public void onResume() {
        super.onResume();

    }


    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case Utility.MY_PERMISSIONS_REQUEST_READ_EXTERNAL_STORAGE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    if (userChoosenTask.equals("Take Photo"))
                        cameraIntent();
                    else if (userChoosenTask.equals("Choose from Library"))
                        galleryIntent();
                } else {
                    //code for deny
                }
                break;
        }
    }

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Gallery",
                "Cancel"};

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        // builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {
                boolean result = Utility.checkPermission(getActivity());

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (result)
                        cameraIntent();

                } else if (items[item].equals("Choose from Gallery")) {
                    userChoosenTask = "Choose from Gallery";
                    if (result)
                        galleryIntent();

                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }

    private void galleryIntent() {
        values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "complaint");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Gallery");
        imageUri = getActivity().getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent galleryintent = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(galleryintent, SELECT_FILE);


        /*Intent intent = new Intent();
        intent.setType("image*//*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);*/
    }

    private void cameraIntent() {
        values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE, "complaint");
        values.put(MediaStore.Images.Media.DESCRIPTION, "From your Camera");
        imageUri = getActivity().getContentResolver().insert(
                MediaStore.Images.Media.EXTERNAL_CONTENT_URI, values);
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        intent.putExtra(MediaStore.EXTRA_OUTPUT, imageUri);
        startActivityForResult(intent, REQUEST_CAMERA);

       /* Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);*/
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
    }

    private void storeCameraPhotoInSDCard(Bitmap bitmap, String currentDate) {
        File file = new File("/data/data/" + getActivity().getPackageName() + "/complaint_images");
        if (!file.exists())
            file.mkdir();
        File outputFile = new File("/data/data/" + getActivity().getPackageName() + "/complaint_images/" + "photo_" + currentDate + ".jpg");
        try {
            FileOutputStream fileOutputStream = new FileOutputStream(outputFile);
            bitmap.compress(Bitmap.CompressFormat.JPEG, 25, fileOutputStream);
            fileOutputStream.flush();
            fileOutputStream.close();
            CompressImageQuality compressImageQuality = new CompressImageQuality(getActivity());
            compressImageQuality.compressImage(String.valueOf(outputFile));
            files.add(outputFile);
            filePaths.add(outputFile.getAbsolutePath());
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Bitmap getImageFileFromSDCard(String filename) {
        Bitmap bitmap = null;
        File imageFile = new File("/data/data/" + getActivity().getPackageName() + "/complaint_images/" + filename);
        try {
            FileInputStream fis = new FileInputStream(imageFile);
            bitmap = BitmapFactory.decodeStream(fis);
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        return bitmap;
    }

    private void onCaptureImageResult(Intent data) {

        try {
            thumbnailUtils = MediaStore.Images.Media.getBitmap(
                    getActivity().getContentResolver(), imageUri);


            String partFilename = currentDateFormat();
            storeCameraPhotoInSDCard(thumbnailUtils, partFilename);

            // display the image from SD Card to ImageView Control
            String storeFilename = "photo_" + partFilename + ".jpg";
            Bitmap mBitmap = getImageFileFromSDCard(storeFilename);
            if (mBitmap != null) {
                Common.Log.i("mBitmap1 : " + mBitmap.getByteCount());
                switch (imageViewId) {
                    case R.id.ivImage:
                        ivImage.setImageBitmap(mBitmap);
                        break;
                    case R.id.ivImage1:
                        ivImage1.setImageBitmap(mBitmap);
                        break;
                    case R.id.ivImage2:
                        ivImage2.setImageBitmap(mBitmap);
                        break;

            /*case R.inspectDate.ivImage4:
                ivImage4.setImageBitmap(thumbnail);
                base64String4 = result;

                break;
            case R.inspectDate.ivImage5:
                ivImage5.setImageBitmap(thumbnail);
                base64String5 = result;
                break;
            case R.inspectDate.ivImage6:
                ivImage6.setImageBitmap(thumbnail);
                base64String6 = result;
                break;*/


                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }

/*
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 50, bytes);

        byte[] byteArray = bytes.toByteArray();
        result = Base64.encodeToString(byteArray, Base64.DEFAULT);

        //Toast.makeText(getActivity(),"base64 successfull "+ result,Toast.LENGTH_LONG).show();
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        //  String result = Base64.encodeToString(destination, Base64.DEFAULT);
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }*/
      /*  switch (imageViewId) {
            case R.inspectDate.ivImage:
                ivImage.setImageBitmap(mBitmap);
                base64String1 = result;
                break;
            case R.inspectDate.ivImage1:
                ivImage1.setImageBitmap(mBitmap);
                base64String2 = result;

                break;
            case R.inspectDate.ivImage2:
                ivImage2.setImageBitmap(mBitmap);
                base64String3 = result;

                break;

            case R.inspectDate.ivImage4:
                ivImage4.setImageBitmap(thumbnail);
                base64String4 = result;

                break;
            case R.inspectDate.ivImage5:
                ivImage5.setImageBitmap(thumbnail);
                base64String5 = result;
                break;
            case R.inspectDate.ivImage6:
                ivImage6.setImageBitmap(thumbnail);
                base64String6 = result;
                break;


        }*/
        // fm.setVisibility(View.VISIBLE);

        btnremove.setVisibility(View.GONE);
    }

    private String currentDateFormat() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyyMMdd_HH_mm_ss");
        String currentTimeStamp = dateFormat.format(new Date());
        return currentTimeStamp;
    }


    public String getRealPathFromURI(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};

        //This method was deprecated in API level 11
        //Cursor cursor = managedQuery(contentUri, proj, null, null, null);

        CursorLoader cursorLoader = new CursorLoader(
                getActivity(),
                contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        int column_index =
                cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
        cursor.moveToFirst();
        return cursor.getString(column_index);
    }

    private void onSelectFromGalleryResult(Intent data) {

        Bitmap bm = null;
        if (data != null) {

            Uri selectedimage = data.getData();
//            selectedImagePath = selectedimage.getEncodedPath();

            String convertedPath = getRealPathFromURI(selectedimage);

            //Uri convert back again from path
            Uri uriFromPath = Uri.fromFile(new File(convertedPath));
            try {
                thumbnailUtils = BitmapFactory.decodeStream(
                        getActivity().getContentResolver().openInputStream(uriFromPath));
                String partFilename = currentDateFormat();
                storeCameraPhotoInSDCard(thumbnailUtils, partFilename);

                // display the image from SD Card to ImageView Control
                String storeFilename = "photo_" + partFilename + ".jpg";
                Bitmap mBitmap = getImageFileFromSDCard(storeFilename);
                if (mBitmap != null) {
                    switch (imageViewId) {
                        case R.id.ivImage:
                            ivImage.setImageBitmap(mBitmap);
                            break;
                        case R.id.ivImage1:
                            ivImage1.setImageBitmap(mBitmap);

                            break;
                        case R.id.ivImage2:
                            ivImage2.setImageBitmap(mBitmap);

                            break;

                    }

                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }








          /*  try {
                //
                //   bm = (Bitmap) data.getExtras().get("data");
                bm = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), data.getData());
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                byte[] byteArray = bytes.toByteArray();
                result = Base64.encodeToString(byteArray, Base64.DEFAULT);
                // Toast.makeText(getActivity(),"base64 successfull "+ result,Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
        switch (imageViewId) {
            case R.inspectDate.ivImage:
                ivImage.setImageBitmap(bm);
                base64String1 = result;
                break;
            case R.inspectDate.ivImage1:
                ivImage1.setImageBitmap(bm);
                base64String2 = result;

                break;
            case R.inspectDate.ivImage2:
                ivImage2.setImageBitmap(bm);
                base64String3 = result;

                break;

            case R.inspectDate.ivImage4:
                ivImage4.setImageBitmap(bm);
                base64String4 = result;

                break;
            case R.inspectDate.ivImage5:
                ivImage5.setImageBitmap(bm);
                base64String5 = result;
                break;
            case R.inspectDate.ivImage6:
                ivImage6.setImageBitmap(bm);
                base64String6 = result;
                break;

*/
        }

        btnremove.setVisibility(View.GONE);
    }

    private void validate() {
        Handler handler = Common.disableClickEvent(submit, true);

        String fid = "1";
        /*if (spin_company.getSelectedItem().toString().trim().equalsIgnoreCase("Select Company")) {
            tvInvisibleError1.requestFocus();
            tvInvisibleError1.setError("Please select company");
            return;

        }
        if (spin_division.getSelectedItem().toString().trim().equalsIgnoreCase("Select Division")) {
            tvInvisibleError2.requestFocus();
            tvInvisibleError2.setError("Please select division");
            return;
        }*/
        if (spin_crop.getSelectedItem().toString().trim().equalsIgnoreCase("Select Crop")) {
            tvInvisibleError3.requestFocus();
            tvInvisibleError3.setError("Please select crop");
            return;
        }
        if (spin_product.getSelectedItem().toString().trim().equalsIgnoreCase("Select Product Name") || spin_product.getSelectedItem() == " ") {
            tvInvisibleError4.requestFocus();
            tvInvisibleError4.setError("Please select product");
            return;
        }

        String mkt_lot = mkt_lot_num.getText().toString();
        if (TextUtils.isEmpty(mkt_lot) || mkt_lot.length() > 0 && mkt_lot.startsWith(" ")) {
            mkt_lot_num.requestFocus();
            mkt_lot_num.setError("Please enter marketing lot number");
            return;
        }
        String fn = farmername.getText().toString();
        if (TextUtils.isEmpty(fn) || fn.length() > 0 && fn.startsWith(" ")) {
            farmername.requestFocus();
            farmername.setError("Please enter farmer name");
            return;
        }
        String cnt = contact.getText().toString();
        if (TextUtils.isEmpty(cnt) || cnt.length() > 0 && cnt.startsWith(" ")) {
            contact.requestFocus();
            contact.setError("Please enter mobile number");
            return;
        }
        if (cnt.length() > 0 && cnt.length() < 10 || cnt.startsWith(" ")) {
            contact.requestFocus();
            contact.setError("Please enter valid mobile number");
            return;
        }
        String cmparea = cmpltarea.getText().toString();
        if (TextUtils.isEmpty(cmparea) || cmparea.length() > 0 && cmparea.startsWith(" ")) {
            cmpltarea.requestFocus();
            cmpltarea.setError("Please enter complaint area");
            return;
        }
        String soil = spin_soil.getSelectedItem().toString();
        if (spin_soil.getSelectedItem().toString().trim().equalsIgnoreCase("Select soil type")) {
            tvInvisibleError5.requestFocus();
            tvInvisibleError5.setError("Please select soil type");
            return;

        }


        String pqty = purqty.getText().toString();
        if (TextUtils.isEmpty(pqty) || pqty.length() > 0 && pqty.startsWith(" ")) {
            purqty.requestFocus();
            purqty.setError("Please enter purchase packets in ea");
            return;
        }

        String cqty = cmplqty.getText().toString();
        if (TextUtils.isEmpty(cqty) || cqty.length() > 0 && cqty.startsWith(" ")) {
            cmplqty.requestFocus();
            cmplqty.setError("Please enter complaint packets in ea");
            return;
        }
        // String pd = purdate.getText().toString();
        if (TextUtils.isEmpty(purdate.getText().toString()) || purdate.getText().toString().length() > 0 && purdate.getText().toString().startsWith(" ")) {
            purdate.requestFocus();
            purdate.setError("Please enter purchase date");
            return;
        }
        String bn = bill.getText().toString();
        if (TextUtils.isEmpty(bn) || bn.length() > 0 && bn.startsWith(" ")) {
            bill.requestFocus();
            bill.setError("Please enter bill number");
            return;
        }
        String rn = rtlr.getText().toString();
        if (retailerId.equalsIgnoreCase("0")) {
            rtlr.requestFocus();
            rtlr.setText("");
            rtlr.setError("Please Choose Retailer");
            return;
        }
       /* if (TextUtils.isEmpty(rn) || rn.length() > 0 && rn.startsWith(" ")) {
            rtlr.requestFocus();
            rtlr.setError("Please enter retailer name");
            return;
        }*/

        if (sel_distributor_id == null || sel_distributor_id == "0") {
            tvInvisibleError6.requestFocus();
            tvInvisibleError6.setError("Please select distributor");
            return;
        }
        /*String mn = mandal.getText().toString();
        if (TextUtils.isEmpty(mn) || mn.length() > 0 && mn.startsWith(" ")) {
            mandal.requestFocus();
            mandal.setError("Please enter mandal");
            return;
        }
        String vl = vlg.getText().toString();
        if (TextUtils.isEmpty(vl) || vl.length() > 0 && vl.startsWith(" ")) {
            vlg.requestFocus();
            vlg.setError("Please enter village");
            return;
        }*/

        String complaintType = spin_complaint_type.getSelectedItem().toString().trim();
        String complaintPercentage = complaintPer.getText().toString().trim();
        String complaintRemarks = complaintRem.getText().toString().trim();
        String ot = others.getText().toString().trim();
        if (complaintType.equalsIgnoreCase("Select Complaint Type")) {
            tvInvisibleError11.requestFocus();
            tvInvisibleError11.setError("Please select complaint type");
            return;
        } else if (complaintType.equalsIgnoreCase(COMPLAINT_TYPE[1]) || complaintType.equalsIgnoreCase(COMPLAINT_TYPE[2])) {

            if (TextUtils.isEmpty(complaintPercentage)) {
                complaintPer.requestFocus();
                complaintPer.setError("Please enter complaint %");
                return;
            }
        } else if (complaintType.equalsIgnoreCase(COMPLAINT_TYPE[3]) || complaintType.equalsIgnoreCase(COMPLAINT_TYPE[4])) {

            if (TextUtils.isEmpty(complaintRemarks)) {
                complaintRem.requestFocus();
                complaintRem.setError("Please enter complaint remarks");
                return;
            }
        } else if (complaintType.equalsIgnoreCase(COMPLAINT_TYPE[5])) {

            if (TextUtils.isEmpty(ot) || ot.length() > 0 && ot.startsWith(" ")) {
                others.requestFocus();
                others.setError("Please enter others");
                return;
            }
        }
        /*String districtText = district.getText().toString().trim();
        if (TextUtils.isEmpty(districtText)) {
            district.requestFocus();
            district.setError("please enter district");
            return;
        }
        String regionText = region.getText().toString().trim();
        if (TextUtils.isEmpty(regionText)) {
            region.requestFocus();
            region.setError("please enter region");
            return;
        }
        String stateText = state.getText().toString().trim();
        if (TextUtils.isEmpty(stateText)) {
            state.requestFocus();
            state.setError("please enter state");
            return;
        }
        String zoneText = zone.getText().toString().trim();
        if (TextUtils.isEmpty(zoneText)) {
            zone.requestFocus();
            zone.setError("please enter zone");
            return;
        }*/
        String performaceType = performance_spinner.getSelectedItem().toString().trim();
        if (performaceType.equalsIgnoreCase("Select")) {
            tvInvisibleError12.requestFocus();
            tvInvisibleError12.setError("Please select performance");
            return;
        }

        String inspectedDt = inspectedDate.getText().toString().trim();
        if (TextUtils.isEmpty(inspectedDate.getText().toString().trim())) {
            inspectedDate.requestFocus();
            inspectedDate.setError("please enter district");
            return;
        }

        if (files.size() == 0 || filePaths.size() == 0) {
            ivImage.requestFocus();
            Toast.makeText(getActivity(), "Please Choose atleast one Image", Toast.LENGTH_SHORT).show();
            return;
        }
        String cmts = comments.getText().toString();
        if (TextUtils.isEmpty(cmts) || cmts.length() > 0 && cmts.startsWith(" ")) {
            comments.requestFocus();
            comments.setError("Please enter complaint");
            return;
        } else {
            Common.disableClickEvent(submit, handler);
            progressDialog = Common.showProgressDialog(getActivity());

            Double pq = Double.valueOf(purqty.getText().toString());
            Double cq = Double.valueOf(cmplqty.getText().toString());

            int comp = Integer.parseInt(sel_comp_id);
            int div = Integer.parseInt(sel_division_id);
            int crop = Integer.parseInt(sel_crop_id);

            //String dist =customername ;
            ///  String dl = deal.getText().toString();

            int product = Integer.parseInt(sel_product_id);
//        String regtype = spin_regulatory.getSelectedItem().toString();

            Double cmpa = Double.valueOf(cmpltarea.getText().toString());
            ArrayList<String> objects = new ArrayList<>();
            StringBuilder stringBuilder = new StringBuilder();
            if (base64String1 != null) {
                objects.add(base64String1);
            }
            if (base64String2 != null) {
                objects.add(base64String2);
            }
            if (base64String3 != null) {
                objects.add(base64String3);
            }
            if (base64String4 != null) {
                objects.add(base64String4);
            }
            if (base64String5 != null) {
                objects.add(base64String5);
            }
            if (base64String6 != null) {
                objects.add(base64String6);
            }

            JSONArray jsonArray = new JSONArray();
            for (int i = 0; i < objects.size(); i++) {
                try {
                    jsonArray.put(new JSONObject().put("image_64", objects.get(i)));
                } catch (JSONException e) {
                    e.printStackTrace();
                }


            }

            //   String allImagesBase64 = stringBuilder.toString();
            Log.e("imges", jsonArray.toString());

            int fdid = Integer.parseInt(fid);
            //String ivImage="xyz.jpg";
            String nothing = null;
            int nothingint = 0;
            double nothingdouble = 0;
            String cmpa1 = cmpa.toString();
            String pq1 = pq.toString();
            String cq1 = cq.toString();
            StringBuffer pathBuffer = new StringBuffer();
            for (int i = 0; i < filePaths.size(); i++) {
                pathBuffer.append(filePaths.get(i) + ",");
            }
            String imagePath = pathBuffer.substring(0, pathBuffer.lastIndexOf(",")).toString();
            //  db.addComplaint(new Complaints(comp, div, crop, 1, mkt_lot, nothing, fn, cnt, cmpa1, soil, ot, pq1, cq1, pd, bn, rn, nothingint, mn, vl, result, nothing, nothing, nothing, nothing, nothing, nothing, nothingint, nothing, nothing));
            db.addComplaint(new Complaints(fdid, Integer.parseInt(checkuid), comp, div, Integer.parseInt(sel_crop_id), product, mkt_lot, complaintType, fn, cnt, cmpa1, soil, ot, pq1, cq1, pd, bn, rn, Integer.parseInt(sel_distributor_id), "", "", imagePath, nothing, nothing, nothing, nothing, nothing, cmts, nothingint, nothing, nothing, nothing, null, complaintPercentage, complaintRemarks, dc, "", "", "", "", "", ds, performaceType, inspectDate, retailerId));

            HashMap<String, String> map = new HashMap<String, String>();
            map.put("sr", String.valueOf(fdid));
            map.put("user_id", checkuid);
            //  map.put("name", "");
            //  map.put("area", fb.get_complaint_area_acres());
            map.put("type", "Product");
            map.put("farmer", fn);
            map.put("retailer", rn);
            map.put("comments", cmts);
            //  map.put("status", String.valueOf(fb.get_status()));
            //    map.put("complaint_type",fb.get_complaint_type());

            favouriteItem.add(map);


            // Toast.makeText(getActivity(),"Inserted successfully...",Toast.LENGTH_SHORT).show();
        }
        if (Common.haveInternet(getActivity())) {
            insertToService();
        } else {
            Common.dismissProgressDialog(progressDialog);
            getActivity().setResult(Activity.RESULT_OK);
//            Intent complaints = new Intent(getActivity(), ProductSurveyAllActivity.class);
//            startActivity(complaints);
            getActivity().finish();
            //   Toast.makeText(getContext(),Common.INTERNET_UNABLEABLE,Toast.LENGTH_SHORT).show();
        }
    }

    private void insertToService() {
        Log.e("Reading: ", "Reading all Complaints..");

        List<Complaints> complaints = db.getAllComplaintsproducts(Common.getTeamFromSP(getActivity()));
        Log.e("list size", String.valueOf(complaints.size()));

        if (complaints.size() == 0) {
            Log.e("no data found", "no data");
            Common.dismissProgressDialog(progressDialog);
        }

        for (Complaints cm : complaints) {
            String log = "Id: " + cm.getID() + " ,company: " + cm.getCompanyId() + " ,div: " + cm.get_division_id() + ",crop:" + cm.getCropid() + " ,product: " + cm.getProductid() + ",mkt_lot:" +
                    cm.get_marketing_lot_number() + ",comptype" + cm.get_complaint_type() + ",farmer:" + cm.get_farmer_name() + ",contact" + cm.get_contact_no() + ",complarea" + cm.get_complaint_area_acres()
                    + ",soiltype" + cm.get_soil_type() + ",others" + cm.get_others() + ",purqty" + cm.get_purchased_quantity() + ",cmpqty" + cm.get_complaint_quantity() + ",purdate" + cm.get_purchase_date() + ",billnum" + cm.get_bill_number()
                    + ",retlrname" + cm.get_retailer_name() + ",distributor" + cm.get_distributor() + ",mandal" + cm.get_mandal() + ",village" + cm.get_village() + "image:" + cm.get_image() + ",regtype" + cm.get_regulatory_type() + ",samplingdate" + cm.get_sampling_date()
                    + ",samplingplace" + cm.get_place_sampling() + ",ofcrname" + cm.get_sampling_officer_name() + ",ofcrcontact" + cm.get_sampling_officer_contact() + ",comments" + cm.get_comments() + ",status" + cm.get_status() + ",remarks" + cm.get_remarks() +
                    ",createddatetime" + cm.get_created_datetime() + ",updateddatetime" + cm.get_updated_datetime() + ",ffmid " + cm.get_ffmid();
            Log.e("complaints: ", log);

            String imageStr1 = "";
            String imageStr2 = "";
            String imageStr3 = "";
            String imageStr4 = "";
            String imageStr5 = "";
            String imageStr6 = "";
            try {
                JSONArray jsonArray = new JSONArray(cm.get_image());

                //  String[] imagesDb = fb.getImage().split(",nsl,");
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String image = jsonObject.getString("image_64");

                    if (i == 0) {
                        imageStr1 = image;
                    }
                    if (i == 1) {
                        imageStr2 = image;
                    }
                    if (i == 2) {
                        imageStr3 = image;
                    }
                    if (i == 3) {
                        imageStr4 = image;
                    }
                    if (i == 4) {
                        imageStr5 = image;
                    }
                    if (i == 5) {
                        imageStr6 = image;
                    }

                }

            } catch (JSONException e) {
                e.printStackTrace();
            }

            ComplaintReq complaintReq = new ComplaintReq();
            complaintReq.id = String.valueOf(cm.getID());
            complaintReq.companyId = String.valueOf(cm.getCompanyId());
            complaintReq.divisionId = String.valueOf(cm.get_division_id());
            complaintReq.cropId = sel_crop_id;
            complaintReq.productId = String.valueOf(cm.getProductid());
            complaintReq.marketingLotNumber = cm.get_marketing_lot_number();
            complaintReq.others = cm.get_others();
            complaintReq.farmerName = cm.get_farmer_name();
            complaintReq.contactNo = cm.get_contact_no();
            complaintReq.complaintAreaAcres = cm.get_complaint_area_acres();
            complaintReq.soilType = cm.get_soil_type();
            complaintReq.purchasedQuantity = cm.get_purchased_quantity();
            complaintReq.complaintQuantity = cm.get_complaint_quantity();
            complaintReq.purchaseDate = cm.get_purchase_date();
            complaintReq.billNumber = cm.get_bill_number();
            complaintReq.retailerName = cm.get_retailer_name();
            complaintReq.retailerId = cm.getRetailerId();
            complaintReq.distributor = String.valueOf(cm.get_distributor());
//            complaintReq.mandal = cm.get_mandal();
//            complaintReq.village = cm.get_village();
            complaintReq.comments = cm.get_comments();
            complaintReq.userId = String.valueOf(cm.get_user_id());
            complaintReq.complaintType = cm.get_complaint_type();
            complaintReq.complaintPercentage = cm.getComplaintPercentage();
            complaintReq.complaintRemarks = cm.getComplaintRemarks();
//            complaintReq.zone = cm.getZone();
//            complaintReq.region = cm.getRegion();
//            complaintReq.dealer = cm.getDealer();
            complaintReq.performanceType = cm.getPerformanceLot();
//            complaintReq.state = cm.getState();
            complaintReq.dateOfSowing = cm.getDateOfSowing();
            complaintReq.dateOfComplaint = cm.getDateOfComplaint();
            complaintReq.inspectedDate = cm.getInspectedDate();
            Common.Log.i("complaint request " + new Gson().toJson(complaintReq));
            sendImage(complaintReq);

//            new Async_InsertComplaintsprod().execute(String.valueOf(cm.getID()), String.valueOf(cm.getCompanyId()), String.valueOf(cm.get_division_id()), sel_crop_id, String.valueOf(cm.getProductid()), cm.get_marketing_lot_number(), cm.get_others(), cm.get_farmer_name(),
//                    cm.get_contact_no(), cm.get_complaint_area_acres(), cm.get_soil_type(), cm.get_purchased_quantity(), cm.get_complaint_quantity(), cm.get_purchase_date(), cm.get_bill_number(), cm.get_retailer_name(), String.valueOf(cm.get_distributor()), cm.get_mandal(),
//                    cm.get_village(), cm.get_comments(), String.valueOf(cm.get_user_id()), imageStr1, imageStr2, imageStr3, imageStr4, imageStr5, imageStr6);
        }


    }

    private void sendImage(ComplaintReq obj) {

        List<MultipartBody.Part> muPartList = new ArrayList<>();
        if (files != null) {
            for (int i = 0; i < files.size(); i++) {
                muPartList.add(prepareFilePart("image[]", Uri.fromFile(files.get(i)), files.get(i)));
            }

        }
        MultipartBody.Part[] fileParts = muPartList.toArray(new MultipartBody.Part[muPartList.size()]);
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor();
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        okhttp3.OkHttpClient client = new okhttp3.OkHttpClient.Builder().addInterceptor(interceptor)
                .addInterceptor(new BasicAuthInterceptor("rest", "seeds@admin"))
                .connectTimeout(Constants.CONNECTION_TIME_OUT, TimeUnit.SECONDS)
                .writeTimeout(Constants.WRITE_TIME_OUT, TimeUnit.SECONDS)
                .readTimeout(Constants.READ_TIME_OUT, TimeUnit.SECONDS)
                .build();

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(Constants.URL_NSL_MAIN)
                .addConverterFactory(GsonConverterFactory.create())
                .client(client)
                .build();
        RetrofitAPI apiInterface = retrofit.create(RetrofitAPI.class);

        apiInterface.uploadProfile("complaint", fileParts, obj).enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, retrofit2.Response<ResponseBody> response) {
                try {
                    String jsonData = response.body().string();
                    Common.Log.i("COMPLAINT JSON " + jsonData);
                    Common.dismissProgressDialog(progressDialog);
                    if (jsonData != null) {
                        JSONArray jsonarray;
                        try {
                            JSONObject jsonobject = new JSONObject(jsonData);
                            String status = jsonobject.getString("status");
                            if (status.equalsIgnoreCase("success")) {

                                sqliteid = jsonobject.getString("sqlite");
                                ffmid = jsonobject.getString("ffm_id");
                                String image_url = jsonobject.getString("image_url");
                                int complaintStatus = jsonobject.getInt("complaint_status");
                                int stages = jsonobject.getInt("stages");


                                Log.e("sqlite inspectDate", sqliteid);
                                Log.e("ffmid", ffmid);
                                sql = db.getWritableDatabase();
                                // updatecomplaints
                                String updatequery = "UPDATE " + TABLE_COMPLAINT + " SET " + KEY_TABLE_COMPLAINTS_FFMID + " = " + ffmid + " , " + KEY_TABLE_COMPLAINTS_STATUS + " = " + complaintStatus + " , " + KEY_TABLE_COMPLAINTS_STAGES + " = " + stages + " , " + KEY_TABLE_COMPLAINTS_IMAGE_UPLOAD + " = '" + image_url + "' WHERE " + KEY_TABLE_COMPLAINTS_ID + " = " + sqliteid;
                                sql.execSQL(updatequery);
                                System.out.println(updatequery);
//                                List<Complaints> complaints = db.getAllComplaints(checkuid);
//
//                                for (Complaints cm : complaints) {
//                                    String log = "Id: " + cm.getID() + " ,company: " + cm.getCompanyId() + " ,div: " + cm.get_division_id() + ",crop:" + cm.getCropid() + " ,product: " + cm.getProductid() + ",mkt_lot:" +
//                                            cm.get_marketing_lot_number() + ",comptype" + cm.get_complaint_type() + ",farmer:" + cm.get_farmer_name() + ",contact" + cm.get_contact_no() + ",complarea" + cm.get_complaint_area_acres()
//                                            + ",soiltype" + cm.get_soil_type() + ",others" + cm.get_others() + ",purqty" + cm.get_purchased_quantity() + ",cmpqty" + cm.get_complaint_quantity() + ",purdate" + cm.get_purchase_date() + ",billnum" + cm.get_bill_number()
//                                            + ",retlrname" + cm.get_retailer_name() + ",distributor" + cm.get_distributor() + ",mandal" + cm.get_mandal() + ",village" + cm.get_village() + "image:" + cm.get_image() + ",regtype" + cm.get_regulatory_type() + ",samplingdate" + cm.get_sampling_date()
//                                            + ",samplingplace" + cm.get_place_sampling() + ",ofcrname" + cm.get_sampling_officer_name() + ",ofcrcontact" + cm.get_sampling_officer_contact() + ",comments" + cm.get_comments() + ",status" + cm.get_status() +
//                                            ",createddatetime" + cm.get_created_datetime() + ",updateddatetime" + cm.get_updated_datetime() + ",ffmid " + cm.get_ffmid();
//                                    Log.e("complaintsafterupdate: ", log);
//
//
//                                }

                                android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(getActivity());
                                alert.setTitle("Success");
                                alert.setMessage("Complaints Product inserted successfully");
                                alert.setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog,
                                                                int which) {

                                                getActivity().setResult(Activity.RESULT_OK);
//                                                Intent complaints = new Intent(getActivity(), ProductSurveyAllActivity.class);
//                                                startActivity(complaints);
                                                getActivity().finish();
                                            }
                                        });
                                alert.show();
                            }

                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    } else {
                        Log.e("ServiceHandler", "Couldn't get any data from the url");
                        // Toast.makeText(getApplicationContext(),"No data found",Toast.LENGTH_LONG).show();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {

            }
        });






        /*Call<okhttp3.ResponseBody> responseBodyCall = MyApplication.getInstance().getRetrofitAPI().uploadProfile("complaint",fileParts, obj);
        responseBodyCall.enqueue(new Callback<okhttp3.ResponseBody>() {
            @Override
            public void onResponse(Call<okhttp3.ResponseBody> call, retrofit2.Response<okhttp3.ResponseBody> response) {
                try {
                    String jsonData = response.body().string();
                    Common.dismissProgressDialog(progressDialog);
                    if (jsonData != null) {
                        JSONArray jsonarray;
                        try {
                            JSONObject jsonobject = new JSONObject(jsonData);
                            String status = jsonobject.getString("status");
                            if (status.equalsIgnoreCase("success")) {

                                sqliteid = jsonobject.getString("sqlite");
                                ffmid = jsonobject.getString("ffm_id");
                                String image_url = jsonobject.getString("image_url");
                                //          Toast.makeText(getActivity(), "sqlite inspectDate and ffmid " + sqliteid + " , " + ffmid, Toast.LENGTH_SHORT).show();
                                Log.e("sqlite inspectDate", sqliteid);
                                Log.e("ffmid", ffmid);
                                sql = db.getWritableDatabase();
                                // updatecomplaints
                                String updatequery = "UPDATE " + TABLE_COMPLAINT + " SET " + KEY_TABLE_COMPLAINTS_FFMID + " = " + ffmid + " , " + KEY_TABLE_COMPLAINTS_IMAGE_UPLOAD + " = " + image_url + " WHERE " + KEY_TABLE_COMPLAINTS_ID + " = " + sqliteid;
                                sql.execSQL(updatequery);
                                System.out.println(updatequery);
//                                List<Complaints> complaints = db.getAllComplaints(checkuid);
//
//                                for (Complaints cm : complaints) {
//                                    String log = "Id: " + cm.getID() + " ,company: " + cm.getCompanyId() + " ,div: " + cm.get_division_id() + ",crop:" + cm.getCropid() + " ,product: " + cm.getProductid() + ",mkt_lot:" +
//                                            cm.get_marketing_lot_number() + ",comptype" + cm.get_complaint_type() + ",farmer:" + cm.get_farmer_name() + ",contact" + cm.get_contact_no() + ",complarea" + cm.get_complaint_area_acres()
//                                            + ",soiltype" + cm.get_soil_type() + ",others" + cm.get_others() + ",purqty" + cm.get_purchased_quantity() + ",cmpqty" + cm.get_complaint_quantity() + ",purdate" + cm.get_purchase_date() + ",billnum" + cm.get_bill_number()
//                                            + ",retlrname" + cm.get_retailer_name() + ",distributor" + cm.get_distributor() + ",mandal" + cm.get_mandal() + ",village" + cm.get_village() + "image:" + cm.get_image() + ",regtype" + cm.get_regulatory_type() + ",samplingdate" + cm.get_sampling_date()
//                                            + ",samplingplace" + cm.get_place_sampling() + ",ofcrname" + cm.get_sampling_officer_name() + ",ofcrcontact" + cm.get_sampling_officer_contact() + ",comments" + cm.get_comments() + ",status" + cm.get_status() +
//                                            ",createddatetime" + cm.get_created_datetime() + ",updateddatetime" + cm.get_updated_datetime() + ",ffmid " + cm.get_ffmid();
//                                    Log.e("complaintsafterupdate: ", log);
//
//
//                                }

                                android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(getActivity());
                                alert.setTitle("Success");
                                alert.setMessage("Complaints Product inserted successfully");
                                alert.setPositiveButton("OK",
                                        new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialog,
                                                                int which) {

                                                getActivity().setResult(Activity.RESULT_OK);
                                                Intent complaints = new Intent(getActivity(), ProductSurveyAllActivity.class);
                                                startActivity(complaints);
                                                getActivity().finish();
                                            }
                                        });
                                alert.show();
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
            }f

            @Override
            public void onFailure(Call<okhttp3.ResponseBody> call, Throwable t) {
            }
        });*/

    }

    private MultipartBody.Part prepareFilePart(String file_i, Uri uri, File file) {
        okhttp3.RequestBody requestFile =
                okhttp3.RequestBody.create(MediaType.parse(getMimeType(getActivity(), uri)), file);
        // MultipartBody.Part is used to send also the actual file name
        return MultipartBody.Part.createFormData(file_i, file.getName(), requestFile);
    }

    private String getMimeType(Activity context, Uri uri) {
        String extension;

        //Check uri format to avoid null
        if (uri.getScheme().equals(ContentResolver.SCHEME_CONTENT)) {
            //If scheme is a content
            final MimeTypeMap mime = MimeTypeMap.getSingleton();
            extension = mime.getExtensionFromMimeType(context.getContentResolver().getType(uri));
        } else {
            //If scheme is a File
            //This will replace white spaces with %20 and also other special characters. This will avoid returning null values on file name with spaces and special characters.
            extension = MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(new File(uri.getPath())).toString());

        }

        return extension;
    }

    public class Async_InsertComplaintsprod extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                OkHttpClient client = new OkHttpClient();
                /*For passing parameters*/
                RequestBody formBody = new FormEncodingBuilder()
                        .add("inspectDate", params[0])
                        .add("company_id", params[1])
                        .add("division_id", params[2])
                        .add("complaint_type", "product")
                        .add("crop_id", params[3])
                        .add("product_id", params[4])
                        .add("marketing_lot_number", params[5])
                        .add("others", params[6])
                        .add("farmer_name", params[7])
                        .add("contact_no", params[8])
                        .add("complaint_area_acres", params[9])
                        .add("soil_type", params[10])
                        .add("purchased_quantity", params[11])
                        .add("complaint_quantity", params[12])
                        .add("purchase_date", params[13])
                        .add("bill_number", params[14])
                        .add("retailer_name", params[15])
                        .add("distributor", params[16])
                        .add("mandal", params[17])
                        .add("village", params[18])
                        .add("comments", params[19])
                        .add("user_id", params[20])
                        .add("image_1", params[21])
                        .add("image_2", params[22])
                        .add("image_3", params[23])
                        .add("image_4", params[24])
                        .add("image_5", params[25])
                        .add("image_6", params[26])
                        .build();

                Response responses = null;
                Common.Log.i("Complaint JSON " + new Gson().toJson(formBody.toString()));

                System.out.println("---- Complaints products data -----" + params[1] + params[2] + params[3] + params[4] + params[5] + params[6] + params[7] + params[8]
                        + params[9] + params[10] + params[11] + params[12] + params[13] + params[14] + params[15] + params[16] + params[17] + params[18] + params[19] + params[20] + params[21]);

                /*MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                RequestBody body = RequestBody.create(mediaType,
                        "type=check_in_lat_lon&visit_type=1&user_id=7&latlon=17.4411%2C78.3911&visit_date=2016-12-05%2C18%3A27%3A30&check_in_time=18%3A27%3A30&tracking_id=0");*/
                Request request = new Request.Builder()
                        .url(Constants.URL_INSERTING_COMPLAINTS)
                        .post(formBody)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();


                try {
                    responses = client.newCall(request).execute();
                    jsonData = responses.body().string();
                    System.out.println("!!!!!!!1 InsertComplaintsproduct" + jsonData);

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
            Common.dismissProgressDialog(progressDialog);

            if (jsonData != null) {
                JSONArray jsonarray;
                try {
                    JSONObject jsonobject = new JSONObject(jsonData);
                    String status = jsonobject.getString("status");
                    if (status.equalsIgnoreCase("success")) {

                        sqliteid = jsonobject.getString("sqlite");
                        ffmid = jsonobject.getString("ffm_id");
                        String image_url = jsonobject.getString("image_url");
                        //          Toast.makeText(getActivity(), "sqlite inspectDate and ffmid " + sqliteid + " , " + ffmid, Toast.LENGTH_SHORT).show();
                        Log.e("sqlite inspectDate", sqliteid);
                        Log.e("ffmid", ffmid);
                        sql = db.getWritableDatabase();
                        // updatecomplaints
                        String updatequery = "UPDATE " + TABLE_COMPLAINT + " SET " + KEY_TABLE_COMPLAINTS_FFMID + " = " + ffmid + " WHERE " + KEY_TABLE_COMPLAINTS_ID + " = " + sqliteid;
                        sql.execSQL(updatequery);
                        System.out.println(updatequery);
                        List<Complaints> complaints = db.getAllComplaints(Common.getTeamFromSP(getActivity()));

                        for (Complaints cm : complaints) {
                            String log = "Id: " + cm.getID() + " ,company: " + cm.getCompanyId() + " ,div: " + cm.get_division_id() + ",crop:" + cm.getCropid() + " ,product: " + cm.getProductid() + ",mkt_lot:" +
                                    cm.get_marketing_lot_number() + ",comptype" + cm.get_complaint_type() + ",farmer:" + cm.get_farmer_name() + ",contact" + cm.get_contact_no() + ",complarea" + cm.get_complaint_area_acres()
                                    + ",soiltype" + cm.get_soil_type() + ",others" + cm.get_others() + ",purqty" + cm.get_purchased_quantity() + ",cmpqty" + cm.get_complaint_quantity() + ",purdate" + cm.get_purchase_date() + ",billnum" + cm.get_bill_number()
                                    + ",retlrname" + cm.get_retailer_name() + ",distributor" + cm.get_distributor() + ",mandal" + cm.get_mandal() + ",village" + cm.get_village() + "image:" + cm.get_image() + ",regtype" + cm.get_regulatory_type() + ",samplingdate" + cm.get_sampling_date()
                                    + ",samplingplace" + cm.get_place_sampling() + ",ofcrname" + cm.get_sampling_officer_name() + ",ofcrcontact" + cm.get_sampling_officer_contact() + ",comments" + cm.get_comments() + ",status" + cm.get_status() +
                                    ",createddatetime" + cm.get_created_datetime() + ",updateddatetime" + cm.get_updated_datetime() + ",ffmid " + cm.get_ffmid();
                            Log.e("complaintsafterupdate: ", log);


                        }

                        android.support.v7.app.AlertDialog.Builder alert = new android.support.v7.app.AlertDialog.Builder(getActivity());
                        alert.setTitle("Success");
                        alert.setMessage("Complaints Product inserted successfully");
                        alert.setPositiveButton("OK",
                                new DialogInterface.OnClickListener() {
                                    @Override
                                    public void onClick(DialogInterface dialog,
                                                        int which) {

                                        getActivity().setResult(Activity.RESULT_OK);
                                        Intent complaints = new Intent(getActivity(), ProductSurveyAllActivity.class);
                                        startActivity(complaints);
                                        getActivity().finish();
                                    }
                                });
                        alert.show();
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                // Toast.makeText(getApplicationContext(),"No data found",Toast.LENGTH_LONG).show();
            }

        }
    }


   /* public class AsyncCompaniesoffline extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            arlist_companies = new ArrayList<SelectedCities>();
            adapter_companies = new ArrayList<String>();
            arlist_companies.clear();

            SelectedCities companies = new SelectedCities();
            companies.setCityId("0");
            companies.setCityName("Select Company");
            //    System.out.println("city inspectDate is :" + cityId + "city name is :" + cityName);
            arlist_companies.add(companies);
            adapter_companies.add("Select Company");


            try {

                List<Companies> compList = new ArrayList<>();

                //String selectQuery = "SELECT  * FROM " + TABLE_COMPANY_DIVISION_CROPS + " where " + KEY_TABLE_COMPANY_DIVISION_CROPS_COMPANY_ID + " = " + company_id + " and " + KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID + " = " + sel_division_id;
                //String selectQuery = "SELECT  * FROM " + TABLE_COMPANY_DIVISION_CROPS;
                // String selectQuery= " SELECT "  CR.company_id,name FROM companies  AS CDC inner JOIN user_company_customer AS CR ON CDC.company_id = CR.company_id  where"" + user_id + "= 7" + " group by(" + CR.company_id + ")";


                String selectQuery = "SELECT  " + "CR." + KEY_TABLE_COMPANIES_MASTER_ID + "," + KEY_TABLE_COMPANIES_NAME + " FROM " + TABLE_USER_COMPANY_CUSTOMER + " AS CDC JOIN " + TABLE_COMPANIES + " AS CR ON CDC." + KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID + " = CR." + KEY_TABLE_COMPANIES_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_CUSTOMER_USER_ID + " = " + checkuid + " group by(CR." + KEY_TABLE_COMPANIES_MASTER_ID + ")";

                System.out.println(selectQuery);
                // String selectQuery = "SELECT  " + KEY_TABLE_COMPANIES_MASTER_ID + ","+ KEY_TABLE_COMPANIES_NAME + " FROM " + TABLE_USER_COMPANY_CUSTOMER + " AS C JOIN " + TABLE_COMPANIES + " AS UCC ON UCC."+KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID + " = C."+ KEY_TABLE_COMPANIES_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID + " = " + sel_comp_id + " and " + KEY_TABLE_USER_COMPANY_CUSTOMER_USER_ID + " = " + checkuid;


                // List<Company_division_crops> cdclist = db.getAllCompany_division_crops();
                //  List<User_Company_Customer> cdclist = db.getAllUser_Company_Customer();
                sql = db.getWritableDatabase();

                Cursor cursor = sql.rawQuery(selectQuery, null);
                //System.out.println("cursor count "+cursor.getCount());
                if (cursor.moveToFirst()) {
                    do {
                        Companies company = new Companies();

                        companies.setCityId(cursor.getString(0));

                        companies.setCityName(cursor.getString(1));

                        SelectedCities cities2 = new SelectedCities();
                        cities2.setCityId(cursor.getString(0));
                        cities2.setCityName(cursor.getString(1));
                        // System.out.println("city inspectDate is :" + cityId + "city name is :" + cityName);
                        arlist_companies.add(cities2);
                        adapter_companies.add(cursor.getString(1));
                        System.out.println(cursor.getString(1));


                    } while (cursor.moveToNext());
                }

                  *//*for (int l=0; l<=cropids.size();l++){
                      sdbr =db.getReadableDatabase();

                      Cursor cursors = sdbr.query(TABLE_CROPS, new String[] { KEY_TABLE_CROPS_CROP_ID,
                                      KEY_TABLE_CROPS_CROP_MASTER_ID,KEY_TABLE_CROPS_CROP_NAME }, KEY_TABLE_CROPS_CROP_MASTER_ID + "=?",
                              new String[] { String.valueOf(cropids.get(l).get("crop_id")) }, null, null, null, null);
                      if (cursors != null)
                          cursors.moveToFirst();

                      Crops crops = new Crops(Integer.parseInt(cursors.getString(0)),cursors.getString(1), cursors.getString(2), cursors.getString(3), cursors.getString(4), cursors.getString(5), cursors.getString(6),cursors.getString(7));
                      // return contact
                      Log.v("Cropidis xyz",cursors.getString(1)+cursors.getString(2));
                  }*//*


            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            // adapter.updateResults(arrayList);
            spin_company.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, R.id.customSpinnerItemTextView, adapter_companies));
            spin_company.setSelection(adapter_companies.indexOf("Nuziveedu Seeds Ltd"));
            // ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.activity_list_item,android.R.inspectDate.text1,adapter_companies);
            //   dataAdapter
            //        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //  spin_company.setAdapter(dataAdapter);
            spin_company.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    sel_comp_id = arlist_companies.get(position).getCityId();
                    // sel_comp_id = arlist_companies.get(position).toString();
                    //listView.setVisibility(View.INVISIBLE);
                    //
                    //Toast.makeText(getActivity(), categorytypeIdis, Toast.LENGTH_SHORT).show();
                    // Toast.makeText(getApplicationContext(), "apicalled" ,Toast.LENGTH_SHORT).show();
                    if (sel_comp_id.equalsIgnoreCase("0")) {
                        sel_comp_id = "0";

                    } else {
                        spin_division.setVisibility(View.VISIBLE);
                        new AsyncDivisionsoffline().execute();
                    }
                }


                @Override
                public void onNothingSelected(AdapterView<?> parent) {
                    sel_comp_id = "0";
                }
            });
        }
    }

    public class AsyncDivisionsoffline extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            arlist_divisions = new ArrayList<SelectedCities>();
            adapter_divisions = new ArrayList<String>();
            arlist_divisions.clear();

            SelectedCities divisions = new SelectedCities();
            divisions.setCityId("0");
            divisions.setCityName("Select Division");
            //    System.out.println("city inspectDate is :" + cityId + "city name is :" + cityName);
            arlist_divisions.add(divisions);
            adapter_divisions.add("Select Division");


            try {

                List<Divisions> divList = new ArrayList<>();

                //String selectQuery = "SELECT  * FROM " + TABLE_COMPANY_DIVISION_CROPS + " where " + KEY_TABLE_COMPANY_DIVISION_CROPS_COMPANY_ID + " = " + company_id + " and " + KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID + " = " + sel_division_id;
                //String selectQuery = "SELECT  * FROM " + TABLE_COMPANY_DIVISION_CROPS;
                // String selectQuery= " SELECT "  CR.company_id,name FROM companies  AS CDC inner JOIN user_company_customer AS CR ON CDC.company_id = CR.company_id  where"" + user_id + "= 7" + " group by(" + CR.company_id + ")";


                //String selectQuery = "SELECT  " +"CR."+ KEY_TABLE_COMPANIES_MASTER_ID + ","+ KEY_TABLE_COMPANIES_NAME + " FROM " + TABLE_USER_COMPANY_CUSTOMER + " AS CDC JOIN " + TABLE_COMPANIES + " AS CR ON CDC."+KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID + " = CR."+ KEY_TABLE_COMPANIES_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_CUSTOMER_USER_ID + " = " + checkuid+" group by(CR."+ KEY_TABLE_COMPANIES_MASTER_ID+")";
                String selectQuery = "SELECT  " + "CR." + KEY_TABLE_DIVISION_MASTER_ID + "," + KEY_TABLE_DIVISION_NAME + " FROM " + TABLE_USER_COMPANY_DIVISION + " AS CDC JOIN " + TABLE_DIVISION + " AS CR ON CDC." + KEY_TABLE_USER_COMPANY_DIVISION_DIVISION_ID + " = CR." + KEY_TABLE_DIVISION_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_DIVISION_COMPANY_ID + " = " + sel_comp_id + " and " + KEY_TABLE_USER_COMPANY_DIVISION_USER_ID + " = " + checkuid + " group by(CR." + KEY_TABLE_DIVISION_MASTER_ID + ")";
                //String selectQuery = "SELECT  " + KEY_TABLE_CROPS_CROP_MASTER_ID + ","+ KEY_TABLE_CROPS_CROP_NAME + " FROM " + TABLE_COMPANY_DIVISION_CROPS + " AS CDC JOIN " + TABLE_CROPS + " AS CR ON CDC."+KEY_TABLE_COMPANY_DIVISION_CROPS_CROP_ID + " = CR."+ KEY_TABLE_CROPS_CROP_MASTER_ID + "  where " + KEY_TABLE_COMPANY_DIVISION_CROPS_COMPANY_ID + " = " + company_id + " and " + KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID + " = " + sel_division_id;
                System.out.println(selectQuery);

                // String selectQuery = "SELECT  " + KEY_TABLE_COMPANIES_MASTER_ID + ","+ KEY_TABLE_COMPANIES_NAME + " FROM " + TABLE_USER_COMPANY_CUSTOMER + " AS C JOIN " + TABLE_COMPANIES + " AS UCC ON UCC."+KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID + " = C."+ KEY_TABLE_COMPANIES_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID + " = " + sel_comp_id + " and " + KEY_TABLE_USER_COMPANY_CUSTOMER_USER_ID + " = " + checkuid;


                // List<Company_division_crops> cdclist = db.getAllCompany_division_crops();
                //  List<User_Company_Customer> cdclist = db.getAllUser_Company_Customer();
                sql = db.getWritableDatabase();

                Cursor cursor = sql.rawQuery(selectQuery, null);
                //System.out.println("cursor count "+cursor.getCount());
                if (cursor.moveToFirst()) {
                    do {
                        Divisions Division = new Divisions();

                        divisions.setCityId(cursor.getString(0));

                        divisions.setCityName(cursor.getString(1));

                        SelectedCities cities2 = new SelectedCities();
                        cities2.setCityId(cursor.getString(0));
                        cities2.setCityName(cursor.getString(1));
                        // System.out.println("city inspectDate is :" + cityId + "city name is :" + cityName);
                        arlist_divisions.add(cities2);
                        adapter_divisions.add(cursor.getString(1));
                        System.out.println(cursor.getString(1));


                    } while (cursor.moveToNext());
                }

                  *//*for (int l=0; l<=cropids.size();l++){
                      sdbr =db.getReadableDatabase();

                      Cursor cursors = sdbr.query(TABLE_CROPS, new String[] { KEY_TABLE_CROPS_CROP_ID,
                                      KEY_TABLE_CROPS_CROP_MASTER_ID,KEY_TABLE_CROPS_CROP_NAME }, KEY_TABLE_CROPS_CROP_MASTER_ID + "=?",
                              new String[] { String.valueOf(cropids.get(l).get("crop_id")) }, null, null, null, null);
                      if (cursors != null)
                          cursors.moveToFirst();

                      Crops crops = new Crops(Integer.parseInt(cursors.getString(0)),cursors.getString(1), cursors.getString(2), cursors.getString(3), cursors.getString(4), cursors.getString(5), cursors.getString(6),cursors.getString(7));
                      // return contact
                      Log.v("Cropidis xyz",cursors.getString(1)+cursors.getString(2));
                  }*//*


            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            // adapter.updateResults(arrayList);
            spin_division.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, R.id.customSpinnerItemTextView, adapter_divisions));
            // ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.activity_list_item,android.R.inspectDate.text1,adapter_companies);
            //   dataAdapter
            //        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //  spin_company.setAdapter(dataAdapter);
            spin_division.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    sel_division_id = arlist_divisions.get(position).getCityId();
                    // sel_comp_id = arlist_companies.get(position).toString();
                    //listView.setVisibility(View.INVISIBLE);
                    //
                    //  Toast.makeText(getActivity(), "Companyid" + sel_comp_id + "divisionid" + sel_division_id, Toast.LENGTH_SHORT).show();
                    // Toast.makeText(getApplicationContext(), "apicalled" ,Toast.LENGTH_SHORT).show();
                    if (sel_division_id.equalsIgnoreCase("0")) {

                    } else {
                        new AsyncCropsoffline().execute();
                    }
                }


                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }*/

    public class AsyncCropsoffline extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            arlist_crops = new ArrayList<SelectedCities>();
            adapter_crops = new ArrayList<String>();
            arlist_crops.clear();

            SelectedCities crops = new SelectedCities();
            crops.setCityId("0");
            crops.setCityName("Select Crop");
            //    System.out.println("city inspectDate is :" + cityId + "city name is :" + cityName);
            arlist_crops.add(crops);
            adapter_crops.add("Select Crop");


            try {

                List<Crops> cropList = new ArrayList<>();

                //String selectQuery = "SELECT  * FROM " + TABLE_COMPANY_DIVISION_CROPS + " where " + KEY_TABLE_COMPANY_DIVISION_CROPS_COMPANY_ID + " = " + company_id + " and " + KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID + " = " + sel_division_id;
                //String selectQuery = "SELECT  * FROM " + TABLE_COMPANY_DIVISION_CROPS;
                // String selectQuery= " SELECT "  CR.company_id,name FROM companies  AS CDC inner JOIN user_company_customer AS CR ON CDC.company_id = CR.company_id  where"" + user_id + "= 7" + " group by(" + CR.company_id + ")";


                //String selectQuery = "SELECT  " +"CR."+ KEY_TABLE_COMPANIES_MASTER_ID + ","+ KEY_TABLE_COMPANIES_NAME + " FROM " + TABLE_USER_COMPANY_CUSTOMER + " AS CDC JOIN " + TABLE_COMPANIES + " AS CR ON CDC."+KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID + " = CR."+ KEY_TABLE_COMPANIES_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_CUSTOMER_USER_ID + " = " + checkuid+" group by(CR."+ KEY_TABLE_COMPANIES_MASTER_ID+")";
                String selectQuery = "SELECT  CR." + KEY_TABLE_CROPS_CROP_MASTER_ID + "," + KEY_TABLE_CROPS_CROP_NAME + " FROM " + TABLE_COMPANY_DIVISION_CROPS + " AS CDC JOIN " + TABLE_CROPS + " AS CR ON CDC." + KEY_TABLE_COMPANY_DIVISION_CROPS_CROP_ID + " = CR." + KEY_TABLE_CROPS_CROP_MASTER_ID + "  where " + KEY_TABLE_COMPANY_DIVISION_CROPS_COMPANY_ID + " = " + sel_comp_id + " and " + KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID + " = " + sel_division_id + " group by(CR." + KEY_TABLE_CROPS_CROP_MASTER_ID + ")";                //String selectQuery = "SELECT  " + KEY_TABLE_CROPS_CROP_MASTER_ID + ","+ KEY_TABLE_CROPS_CROP_NAME + " FROM " + TABLE_COMPANY_DIVISION_CROPS + " AS CDC JOIN " + TABLE_CROPS + " AS CR ON CDC."+KEY_TABLE_COMPANY_DIVISION_CROPS_CROP_ID + " = CR."+ KEY_TABLE_CROPS_CROP_MASTER_ID + "  where " + KEY_TABLE_COMPANY_DIVISION_CROPS_COMPANY_ID + " = " + company_id + " and " + KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID + " = " + sel_division_id;
                System.out.println(selectQuery);

                // String selectQuery = "SELECT  " + KEY_TABLE_COMPANIES_MASTER_ID + ","+ KEY_TABLE_COMPANIES_NAME + " FROM " + TABLE_USER_COMPANY_CUSTOMER + " AS C JOIN " + TABLE_COMPANIES + " AS UCC ON UCC."+KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID + " = C."+ KEY_TABLE_COMPANIES_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID + " = " + sel_comp_id + " and " + KEY_TABLE_USER_COMPANY_CUSTOMER_USER_ID + " = " + checkuid;


                // List<Company_division_crops> cdclist = db.getAllCompany_division_crops();
                //  List<User_Company_Customer> cdclist = db.getAllUser_Company_Customer();
                sql = db.getWritableDatabase();

                Cursor cursor = sql.rawQuery(selectQuery, null);
                //System.out.println("cursor count "+cursor.getCount());
                if (cursor.moveToFirst()) {
                    do {
                        Crops Crop = new Crops();

                        crops.setCityId(cursor.getString(0));

                        crops.setCityName(cursor.getString(1));

                        SelectedCities cities2 = new SelectedCities();
                        cities2.setCityId(cursor.getString(0));
                        cities2.setCityName(cursor.getString(1));
                        // System.out.println("city inspectDate is :" + cityId + "city name is :" + cityName);
                        arlist_crops.add(cities2);
                        adapter_crops.add(cursor.getString(1));
                        System.out.println(cursor.getString(1));


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

            // adapter.updateResults(arrayList);
            spin_crop.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, R.id.customSpinnerItemTextView, adapter_crops));
            // ArrayAdapter<String> dataAdapter = new ArrayAdapter<String>(getActivity(),android.R.layout.activity_list_item,android.R.inspectDate.text1,adapter_companies);
            //   dataAdapter
            //        .setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            //  spin_company.setAdapter(dataAdapter);
            spin_crop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    sel_crop_id = arlist_crops.get(position).getCityId();
                    // sel_comp_id = arlist_companies.get(position).toString();
                    //listView.setVisibility(View.INVISIBLE);
                    //
                    //     Toast.makeText(getActivity(), "Divisionid" + sel_division_id + "cropid" + sel_crop_id, Toast.LENGTH_SHORT).show();
                    // Toast.makeText(getApplicationContext(), "apicalled" ,Toast.LENGTH_SHORT).show();
                    if (sel_crop_id.equalsIgnoreCase("0")) {

                    } else {
                        new AsyncProductsoffline().execute();
                    }
                }


                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    public class AsyncProductsoffline extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            arlist_products = new ArrayList<SelectedCities>();
            adapter_products = new ArrayList<String>();
            arlist_products.clear();

            SelectedCities products = new SelectedCities();
            products.setCityId("0");
            products.setCityName("Select Product Name");
            //    System.out.println("city inspectDate is :" + cityId + "city name is :" + cityName);
            arlist_products.add(products);
            adapter_products.add("Select Product Name");


            try {

                List<Products> prodList = new ArrayList<>();

                //String selectQuery = "SELECT  * FROM " + TABLE_COMPANY_DIVISION_CROPS + " where " + KEY_TABLE_COMPANY_DIVISION_CROPS_COMPANY_ID + " = " + company_id + " and " + KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID + " = " + sel_division_id;
                //String selectQuery = "SELECT  * FROM " + TABLE_COMPANY_DIVISION_CROPS;
                // String selectQuery= " SELECT "  CR.company_id,name FROM companies  AS CDC inner JOIN user_company_customer AS CR ON CDC.company_id = CR.company_id  where"" + user_id + "= 7" + " group by(" + CR.company_id + ")";


                //String selectQuery = "SELECT  " +"CR."+ KEY_TABLE_COMPANIES_MASTER_ID + ","+ KEY_TABLE_COMPANIES_NAME + " FROM " + TABLE_USER_COMPANY_CUSTOMER + " AS CDC JOIN " + TABLE_COMPANIES + " AS CR ON CDC."+KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID + " = CR."+ KEY_TABLE_COMPANIES_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_CUSTOMER_USER_ID + " = " + checkuid+" group by(CR."+ KEY_TABLE_COMPANIES_MASTER_ID+")";
                String selectQuery = "SELECT  " + KEY_PRODUCT_MASTER_ID + "," + KEY_PRODUCT_NAME + " FROM " + TABLE_PRODUCTS + "  where " + KEY_PRODUCTS_COMPANY_ID + " = " + sel_comp_id + " and " + KEY_PRODUCTS_DIVISION_ID + " = " + sel_division_id + " and " + KEY_PRODUCT_CROP_ID + " = " + sel_crop_id;
                System.out.println(selectQuery);

                // String selectQuery = "SELECT  " + KEY_TABLE_COMPANIES_MASTER_ID + ","+ KEY_TABLE_COMPANIES_NAME + " FROM " + TABLE_USER_COMPANY_CUSTOMER + " AS C JOIN " + TABLE_COMPANIES + " AS UCC ON UCC."+KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID + " = C."+ KEY_TABLE_COMPANIES_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID + " = " + sel_comp_id + " and " + KEY_TABLE_USER_COMPANY_CUSTOMER_USER_ID + " = " + checkuid;


                // List<Company_division_crops> cdclist = db.getAllCompany_division_crops();
                //  List<User_Company_Customer> cdclist = db.getAllUser_Company_Customer();
                sql = db.getWritableDatabase();

                Cursor cursor = sql.rawQuery(selectQuery, null);
                //System.out.println("cursor count "+cursor.getCount());
                if (cursor.moveToFirst()) {
                    do {
                        Products Product = new Products();

                        products.setCityId(cursor.getString(0));

                        products.setCityName(cursor.getString(1));

                        SelectedCities cities2 = new SelectedCities();
                        cities2.setCityId(cursor.getString(0));
                        cities2.setCityName(cursor.getString(1));
                        // System.out.println("city inspectDate is :" + cityId + "city name is :" + cityName);
                        arlist_products.add(cities2);
                        adapter_products.add(cursor.getString(1));
                        System.out.println(cursor.getString(1));


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

            spin_product.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, R.id.customSpinnerItemTextView, adapter_products));

            spin_product.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    sel_product_id = arlist_products.get(position).getCityId();

                    if (sel_product_id.equalsIgnoreCase("0")) {

                    } else {
                        new AsyncDistributorsoffline().execute();
                    }

                }


                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
        }
    }

    public class AsyncDistributorsoffline extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            arlist_distributor = new ArrayList<SelectedCities>();
            adapter_distributors = new ArrayList<String>();
            arlist_distributor.clear();

           /* SelectedCities distributor = new SelectedCities();
            distributor.setCityId("0");
            distributor.setCityName("Select Distributor");
            //    System.out.println("city inspectDate is :" + cityId + "city name is :" + cityName);
            arlist_distributor.add(distributor);
            adapter_distributors.add("Select Distributor");*/


            try {

                List<Customers> customersList = new ArrayList<>();

                //String selectQuery = "SELECT  * FROM " + TABLE_COMPANY_DIVISION_CROPS + " where " + KEY_TABLE_COMPANY_DIVISION_CROPS_COMPANY_ID + " = " + company_id + " and " + KEY_TABLE_COMPANY_DIVISION_CROPS_DIVISION_ID + " = " + sel_division_id;
                //String selectQuery = "SELECT  * FROM " + TABLE_COMPANY_DIVISION_CROPS;
                // String selectQuery= " SELECT "  CR.company_id,name FROM companies  AS CDC inner JOIN user_company_customer AS CR ON CDC.company_id = CR.company_id  where"" + user_id + "= 7" + " group by(" + CR.company_id + ")";


                //String selectQuery = "SELECT  " +"CR."+ KEY_TABLE_COMPANIES_MASTER_ID + ","+ KEY_TABLE_COMPANIES_NAME + " FROM " + TABLE_USER_COMPANY_CUSTOMER + " AS CDC JOIN " + TABLE_COMPANIES + " AS CR ON CDC."+KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID + " = CR."+ KEY_TABLE_COMPANIES_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_CUSTOMER_USER_ID + " = " + checkuid+" group by(CR."+ KEY_TABLE_COMPANIES_MASTER_ID+")";
                // String selectQuery = "SELECT  " + KEY_PRODUCT_MASTER_ID + "," + KEY_PRODUCT_NAME + " FROM " + TABLE_PRODUCTS + "  where " + KEY_PRODUCTS_COMPANY_ID + " = " + sel_comp_id + " and " + KEY_PRODUCTS_DIVISION_ID + " = " + sel_division_id + " and " + KEY_PRODUCT_CROP_ID + " = " + sel_crop_id;
                String selectQuery = "SELECT  " + "CR." + KEY_TABLE_CUSTOMER_MASTER_ID + "," + KEY_TABLE_CUSTOMER_NAME + " FROM " + TABLE_USER_COMPANY_CUSTOMER + " AS CDC JOIN " + TABLE_CUSTOMERS + " AS CR ON CDC." + KEY_TABLE_USER_COMPANY_CUSTOMER_CUSTOMER_ID + " = CR." + KEY_TABLE_CUSTOMER_MASTER_ID + "  where CDC." + KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID + " = " + sel_comp_id + " and " + KEY_TABLE_USER_COMPANY_CUSTOMER_USER_ID + " IN (" + Common.getTeamFromSP(getActivity()) + ") and CR." + KEY_TABLE_USER_COMPANY_CUSTOMER_STATUS + " = 1" + " and CDC." + KEY_TABLE_USER_COMPANY_CUSTOMER_STATUS + " = 1 group by(CR." + KEY_TABLE_CUSTOMER_MASTER_ID + ")";

                System.out.println(selectQuery);

                // String selectQuery = "SELECT  " + KEY_TABLE_COMPANIES_MASTER_ID + ","+ KEY_TABLE_COMPANIES_NAME + " FROM " + TABLE_USER_COMPANY_CUSTOMER + " AS C JOIN " + TABLE_COMPANIES + " AS UCC ON UCC."+KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID + " = C."+ KEY_TABLE_COMPANIES_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID + " = " + sel_comp_id + " and " + KEY_TABLE_USER_COMPANY_CUSTOMER_USER_ID + " = " + checkuid;


                // List<Company_division_crops> cdclist = db.getAllCompany_division_crops();
                //  List<User_Company_Customer> cdclist = db.getAllUser_Company_Customer();
                sql = db.getWritableDatabase();

                Cursor cursor = sql.rawQuery(selectQuery, null);
                //System.out.println("cursor count "+cursor.getCount());
                if (cursor.moveToFirst()) {
                    do {

                        SelectedCities cities2 = new SelectedCities();

                        cities2.setCityId(cursor.getString(0));
                        cities2.setCityName(cursor.getString(1));
                        String cityId = cities2.getCityId();
                        String cityName = cities2.getCityName();
                        System.out.println("city inspectDate is :" + cityId + "city name is :" + cityName);
                        arlist_distributor.add(cities2);
                        adapter_distributors.add(cursor.getString(1));
                        System.out.println(cursor.getString(1));


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
            autoCompleteAdapter(arlist_distributor);
           /* spin_distributor.setAdapter(new ArrayAdapter<String>(getActivity(), R.layout.spinner_item, R.id.customSpinnerItemTextView, adapter_distributors));

            spin_distributor.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    sel_distributor_id = arlist_distributor.get(position).getCityId();

                    if (sel_distributor_id.equalsIgnoreCase("0")) {

                    } else {
                        autoCompleteRetailerAdapter(new ArrayList<SelectedCities>());

                        myRunnable = new MyRunnable();

//                        String query = "SELECT C." + KEY_TABLE_CUSTOMER_STATE + ",C." + KEY_TABLE_CUSTOMER_DISTRICT + ",C." + KEY_TABLE_CUSTOMER_REGION_ID + ",R." + KEY_REGION__MASTER_ID + ",R." + KEY_REGION_NAME + " FROM " + TABLE_CUSTOMERS + " C LEFT JOIN " + TABLE_REGION + " R ON (C." + KEY_TABLE_CUSTOMER_REGION_ID + " = R." + KEY_REGION__MASTER_ID + ") WHERE C." + KEY_TABLE_USER_COMPANY_CUSTOMER_CUSTOMER_ID + "=" + sel_distributor_id;
//                        Common.Log.i("Query " + query);
//                        Cursor cursor = db.getWritableDatabase().rawQuery(query, null);
//                        Common.Log.i("Cursor Size " + cursor.getCount());
//                        if (cursor != null && cursor.getCount() > 0 && cursor.moveToFirst()) {
//                            String stateTxt = Common.isStringNull(cursor.getString(cursor.getColumnIndex(KEY_TABLE_CUSTOMER_STATE)));
//                            String districtTxt = Common.isStringNull(cursor.getString(cursor.getColumnIndex(KEY_TABLE_CUSTOMER_DISTRICT)));
//                            String regionTxt = Common.isStringNull(cursor.getString(cursor.getColumnIndex(KEY_REGION_NAME)));
//                            Common.Log.i("State " + stateTxt);
//                            Common.Log.i("District " + districtTxt);
//                            Common.Log.i("Region " + regionTxt);
////                            state.setText(stateTxt);
////                            district.setText(districtTxt);
////                            region.setText(regionTxt);
//                            cursor.close();
//                        }
                    }

                }


                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });*/
        }
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
                SelectedCities newObj = new SelectedCities();
                newObj.setExistData(false);
                newObj.setCityId("0");
                List<Retailer> lista = db.getAllRetailers(key, String.valueOf(sel_distributor_id));
                Log.d("afterTextChanged", "list: " + lista.size());
                final List<SelectedCities> objects = new ArrayList<>();
                for (int i = 0; i < lista.size(); i++) {
                    SelectedCities object = new SelectedCities();
                    object.setCityId(String.valueOf(lista.get(i).get_ffmid()));
                    object.setCityName(lista.get(i).get_ret_name());
                    object.setDistrict(lista.get(i).get_ret_district());
                    object.setTaluka(lista.get(i).get_ret_taluka());
                    object.setMobile(lista.get(i).get_ret_mobile());
                    objects.add(object);
                }
                getActivity().runOnUiThread(new Runnable() {
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


    public void autoCompleteAdapter(List<SelectedCities> list) {
        final CustomListAdapter adapter = new CustomListAdapter(getContext(),
                R.layout.autocomplete_custom_layout, list);

        spin_distributor.setAdapter(adapter);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                //  spin_distributor.showDropDown();
            }
        }, 100);

        adapter.setOnItemClickListener(new CustomListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, SelectedCities item) {
                spin_distributor.setText(item.getCityName().toUpperCase());
                spin_distributor.setSelection(item.getCityName().length());
                spin_distributor.dismissDropDown();
                Common.hideSoftKeyboard(getActivity());
                //    Toast.makeText(getApplicationContext(),item.getCityName()+"\n"+item.getCityId(),Toast.LENGTH_SHORT).show();
                Log.d("onItemClick", item.getCityId());

                sel_distributor_id = item.getCityId();
                //   tv_customer.setTextColor(getResources().getColor(R.color.colorPrimary));
                customername = item.getCityName();
                autoCompleteRetailerAdapter(new ArrayList<SelectedCities>());

                myRunnable = new MyRunnable();


            }
        });
        spin_distributor.addTextChangedListener(new TextWatcher() {
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
                    // spin_distributor.setAdapter(adapter);
                    spin_distributor.showDropDown();
                }

            }
        });

     /*   spin_distributor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (spin_distributor.getText().length() == 0) {
                    //   spin_distributor.setAdapter(adapter);
                    spin_distributor.showDropDown();
                }
            }
        });*/
        spin_distributor.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View view, MotionEvent motionEvent) {
                if (spin_distributor.getText().length() == 0) {
                    //   spin_distributor.setAdapter(adapter);
                    spin_distributor.showDropDown();
                }
                return false;
            }
        });
    }
}



