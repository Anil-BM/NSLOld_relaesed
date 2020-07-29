package com.nsl.beejtantra.TFA;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.analytics.ecommerce.Product;
import com.nsl.beejtantra.Constants;
import com.nsl.beejtantra.Crops;
import com.nsl.beejtantra.CropsFragmentSalesorderActivity;
import com.nsl.beejtantra.DatabaseHandler;
import com.nsl.beejtantra.NewCropsFragmentSalesOrderList;
import com.nsl.beejtantra.PlanScheduleFormActivity;
import com.nsl.beejtantra.Products;
import com.nsl.beejtantra.R;
import com.nsl.beejtantra.SelectedCities;
import com.nsl.beejtantra.Users;
import com.nsl.beejtantra.adapters.CropAdapter;
import com.nsl.beejtantra.adapters.CustomListAdapter;
import com.nsl.beejtantra.commonutils.Common;
import com.nsl.beejtantra.marketintelligencenew.MarketPotentialActivity;
import com.nsl.beejtantra.orderindent.ViewSalesOrderCompanyActivity;
import com.nsl.beejtantra.pojo.District;
import com.nsl.beejtantra.pojo.ServiceOrderApproval;
import com.nsl.beejtantra.pojo.ServiceOrderHistory;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.nsl.beejtantra.DatabaseHandler.KEY_REGION__MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CROPS_CROP_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CROPS_CROP_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CROPS_CROP_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CROPS_DIVISION_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_DIVISION_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_DIVISION_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_DIVISION_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_SERVICEORDER_FFM_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_SERVICEORDER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_SERVICEORDER_SAP_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USERS_DESIGNATION;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USERS_EMAIL;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USERS_FIRST_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USERS_HEADQUARTER;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USERS_IMAGE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USERS_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USERS_MOBILE_NO;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USERS_REGION_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USERS_SAP_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USER_COMPANY_DIVISION_DIVISION_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USER_COMPANY_DIVISION_USER_ID;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_CROPS;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_DIVISION;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_SERVICEORDER;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_USERS;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_USER_COMPANY_DIVISION;
import static com.nsl.beejtantra.advancebooking.CropsFragmentAdvancebookingActivity.toJson;
import static com.nsl.beejtantra.orderindent.FragmentOrderIndent.REQUEST_TYPE;

public class ActivityIndentForm extends BaseActivity implements View.OnClickListener {
    String formattedDate,region,regionId,User_id,seluserId;
    private SimpleDateFormat dateFormatter,dateFormatter22;
    private DatePickerDialog fromDatePickerDialog;
    MyAdapter mAdapter;
    ProgressDialog progressDialog;
    boolean isenabled;
    int role;
    EditText et_date,et_villages_list,et_nooffarmers,et_estimatedperhead,et_totalexp,et_village,et_adv,
            et_taluka,et_noofvillages;
    TextView et_refresh;
    AutoCompleteTextView et_district,et_product;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    ArrayList<Long> longsss;
    String districtId, divisionId="", divisionName,cropId="",ProductMasterId,approval_name,approval_pnno,approval_mail,approval_role;
    DatabaseHandler db;

    ArrayList<Integer> rsize= new ArrayList<>();
    SQLiteDatabase sdbw,sdbr;
    long ch;
    List<SelectedCities> distObjs = new ArrayList<>();
    List<Products> ProductObjs = new ArrayList<>();
    MyRunnable1 myRunnable1;
    MyRunnable2 myRunnable2;
    MyRunnable3 myRunnable3;
    String jsonData;
    List<SelectedCities> divisions = new ArrayList<>();
    List<SelectedCities> crops = new ArrayList<>();
    List<String> activities = new ArrayList<>();
    private boolean onDistrictItemClick,onProductItemClick;
    private Handler handler;
    Spinner  spin_division,spin_crop,sp_activity ;
    ArrayList<String> divisionMarketAdapter;
    ArrayList<String> cropMarketadapter;

    Button submit,cnt_submit,btn_appt;
    Typeface custom_font,custom_font2;
    ArrayList<Integer> remove= new ArrayList<>();
    ArrayList<String> al_localids_vill= new ArrayList<>();



    String ar_cs[];
    String ar_vill[];
    String ar_p[];
    Map.Entry<ArrayList,ArrayList> entry;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indent_form);



        init();

        calinit();

        getregion();





        et_district.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                districtId = "0";
                if (s.length() == 0) {
                    et_district.showDropDown();
                } else {
                    if (handler != null) {
                        handler.removeCallbacks(myRunnable1);
                    }

                    handler = new Handler();
                    myRunnable1.setData(s.toString());
                    handler.postDelayed(myRunnable1, 300);

                }
            }
        });
        et_product.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                ProductMasterId = "0";
                if (cropId.equals("") || divisionId.equals("")) {
                    Toast("Please select division and crop");
                } else {
                    if (s.length() == 0) {
                        et_product.showDropDown();
                    } else {
                        if (handler != null) {
                            handler.removeCallbacks(myRunnable2);
                        }

                        handler = new Handler();
                        myRunnable2.setData2(s.toString());
                        handler.postDelayed(myRunnable2, 300);

                    }
                }
            }
        });
        et_nooffarmers.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Integer v1 = Integer.parseInt(!et_nooffarmers.getText().toString().isEmpty() ?
                        et_nooffarmers.getText().toString() : "0");
                Integer v2 = Integer.parseInt(!et_estimatedperhead.getText().toString().isEmpty() ?
                        et_estimatedperhead.getText().toString() : "0");
                Integer value = v1 * v2;
                et_totalexp.setText(value.toString());
            }
        });
        et_estimatedperhead.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Integer v1 = Integer.parseInt(!et_nooffarmers.getText().toString().isEmpty() ?
                        et_nooffarmers.getText().toString() : "0");

                Integer v2 = Integer.parseInt(!et_estimatedperhead.getText().toString().isEmpty() ?
                        et_estimatedperhead.getText().toString() : "0");
                Integer value = v1 * v2;
                et_totalexp.setText(value.toString());
            }
        });









        et_villages_list.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                final AlertDialog dialog = new AlertDialog.Builder(ActivityIndentForm.this).create();
                View popupView = getLayoutInflater().inflate(R.layout.villageslist_popup, null);
                dialog.setView(popupView);
                dialog.show();
                isenabled=true;




                RecyclerView cropRecycler = popupView.findViewById(R.id.crop_recycler);
                et_refresh = (TextView)popupView. findViewById(R.id.et_refresh);
                et_noofvillages = (EditText) popupView. findViewById(R.id.et_noofvillages);
                submit = popupView.findViewById(R.id.submit_popup);
                cnt_submit= (Button) popupView.findViewById(R.id.bt_cnt);

                custom_font2 = Typeface.createFromAsset(getAssets(),  "fonts/fontawesome-webfont.ttf");
                et_refresh.setTypeface(custom_font2);
                et_refresh.setText("\uf01e");

                et_noofvillages.setFocusable(true);
                et_noofvillages.setFocusableInTouchMode(true);
                et_noofvillages.setClickable(true);

                LinearLayoutManager llm = new LinearLayoutManager(ActivityIndentForm.this, LinearLayoutManager.VERTICAL, false);
                cropRecycler.setLayoutManager(llm);





                et_refresh.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        /*et_noofvillages.setEnabled(true);
                        et_noofvillages.setFocusable(true);
                        et_noofvillages.requestFocus();
                        et_noofvillages.setText("");*/
                        isenabled=true;
                        et_noofvillages.setFocusable(true);
                        et_noofvillages.setFocusableInTouchMode(true);
                        et_noofvillages.setClickable(true);
                        //isProhibitEditPassword= false;

                        et_noofvillages.setEnabled(true);
                        et_noofvillages.setFocusable(true);
                        et_noofvillages.requestFocus();
                        et_noofvillages.setText("");
                    }
                });
                submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {


                        JSONArray village_list_array_tmp=new JSONArray();

                        for(int i1=0;i1<ar_vill.length;i1++)
                        {
                            JSONObject villages_obj_tmp = new JSONObject();
                            try
                            {
                                if(ar_vill!=null && ar_vill.length>0&&ar_cs!=null && ar_cs.length>0&&ar_p!=null && ar_p.length>0)
                                {
                                    villages_obj_tmp.put("village_name", ar_vill[i1]);
                                    villages_obj_tmp.put("current_sal", ar_cs[i1]);
                                    villages_obj_tmp.put("potential", ar_p[i1]);
                                }
                                else
                                {
                                    Toast("Enter all details");
                                }
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }
                            village_list_array_tmp.put(villages_obj_tmp);


                        }
                        et_villages_list.setText(village_list_array_tmp.toString());
                        dialog.dismiss();
                    }





                });
                cnt_submit.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {

                        if (!T(et_noofvillages).equals("") || T(et_noofvillages) == null) {

                            et_noofvillages.setEnabled(false);
                            et_noofvillages.setFocusable(false);

                            int k = Integer.valueOf(et_noofvillages.getText().toString());

                            rsize.clear();
                            for (int i = 0; i < k; i++) {
                                rsize.add(i);
                            }
                            //etdata=new String[Integer.valueOf(et_noofvillages.getText().toString())];

                            mAdapter = new MyAdapter(ActivityIndentForm.this, rsize);

                            if (isenabled == true) {
                                cropRecycler.setAdapter(mAdapter);
                            }
                            //Toast("Enter villages count");
                            isenabled = false;
                        } else {
                            Toast("Enter villages count");
                        }
                    }


                });

            }
        });



        divisionMarketAdapter = new ArrayList<String>();
        divisionMarketAdapter.add("Select Division");

        String query = "SELECT "+ KEY_TABLE_DIVISION_ID +","+ KEY_TABLE_DIVISION_NAME + " FROM " + TABLE_DIVISION;
       /* String query = "SELECT DISTINCT ud." + KEY_TABLE_USER_COMPANY_DIVISION_DIVISION_ID + ",d." +
                KEY_TABLE_DIVISION_NAME + " FROM " + TABLE_USER_COMPANY_DIVISION + " ud INNER JOIN " +
                TABLE_DIVISION + " d ON ud." + KEY_TABLE_USER_COMPANY_DIVISION_DIVISION_ID + " = d." +
                KEY_TABLE_DIVISION_MASTER_ID + " WHERE ud." +
                KEY_TABLE_USER_COMPANY_DIVISION_USER_ID + " in (" + Common.getTeamFromSP(this) + ")";*/
        Common.Log.i("Query " + query);
        Cursor cursor = db.getWritableDatabase().rawQuery(query, null);


        if (cursor.moveToFirst()) {
            do {
                SelectedCities selectedCities = new SelectedCities();
                selectedCities.setCityId(cursor.getString(cursor.getColumnIndex(KEY_TABLE_DIVISION_ID)));
                selectedCities.setCityName(cursor.getString(cursor.getColumnIndex(KEY_TABLE_DIVISION_NAME)));
                divisionMarketAdapter.add(cursor.getString(cursor.getColumnIndex(KEY_TABLE_DIVISION_NAME)));
                divisions.add(selectedCities);
            } while (cursor.moveToNext());
        }
        spin_division.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, R.id.customSpinnerItemTextView, divisionMarketAdapter));









        cropMarketadapter = new ArrayList<>();
        cropMarketadapter.add("Select Crop");
        spin_crop.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, R.id.customSpinnerItemTextView, cropMarketadapter));
        spin_division.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    divisionId = divisions.get(position - 1).getCityId();
                    divisionName = divisions.get(position - 1).getCityName();

                    String cropQuery = "SELECT DISTINCT cr." + KEY_TABLE_CROPS_CROP_MASTER_ID + ",cr." + KEY_TABLE_CROPS_CROP_NAME + " FROM " + TABLE_CROPS + " cr WHERE cr." + KEY_TABLE_CROPS_DIVISION_ID + " = " + divisionId;
                    Common.Log.i("CROP QUERY " + cropQuery);
                    Cursor cropCursor = db.getWritableDatabase().rawQuery(cropQuery, null);
                    cropMarketadapter.clear();
                    crops.clear();
                    cropMarketadapter.add("Select Crop");
                    if (cropCursor.moveToFirst()) {
                        do {
                            SelectedCities crop = new SelectedCities();
                            crop.setCityId(cropCursor.getString(cropCursor.getColumnIndex(KEY_TABLE_CROPS_CROP_MASTER_ID)));
                            crop.setCityName(cropCursor.getString(cropCursor.getColumnIndex(KEY_TABLE_CROPS_CROP_NAME)));
                            cropMarketadapter.add(cropCursor.getString(cropCursor.getColumnIndex(KEY_TABLE_CROPS_CROP_NAME)));
                            crops.add(crop);
                        } while (cropCursor.moveToNext());
                    }
                    spin_crop.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, R.id.customSpinnerItemTextView, cropMarketadapter));


                }
                else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spin_crop.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if (position > 0) {
                    cropId = crops.get(position - 1).getCityId();
                }
                else {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

    }

    private void calculate(final Integer  v) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            et_totalexp.setText(String.valueOf(v));
                        }
                    });
                }
                catch (Exception e) {
                    //print the error here
                }
            }
        }).start();

    }




    private void init() {
        db = new DatabaseHandler(this);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);

        role = sharedpreferences.getInt(Constants.SharedPrefrancesKey.ROLE, 0);


        isenabled=true;
        sp_activity = (Spinner) findViewById(R.id.sp_activity);
        spin_division = (Spinner) findViewById(R.id.sp_division);
        spin_crop= (Spinner) findViewById(R.id.sp_crop);
        btn_appt= (Button) findViewById(R.id.btn_appt);

        et_district = (AutoCompleteTextView) findViewById(R.id.et_district);
        et_product = (AutoCompleteTextView) findViewById(R.id.et_product);
        et_nooffarmers = (EditText) findViewById(R.id.et_nooffarmers);
        et_estimatedperhead = (EditText) findViewById(R.id.et_estimationperhead);
        et_totalexp = (EditText) findViewById(R.id.et_totalexpences);
        et_date=(EditText)findViewById(R.id.et_date);
        et_date.setText(getIntent().getStringExtra("dateselected"));
        et_villages_list=(EditText)findViewById(R.id.et_listofvillages);
        et_taluka = (EditText) findViewById(R.id.et_taluka);
        et_village = (EditText) findViewById(R.id.et_village);
        et_adv = (EditText) findViewById(R.id.et_adv);

        et_date.setOnClickListener(this);

        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        User_id           = sharedpreferences.getString("userId", "");
        seluserId=getIntent().getStringExtra("idselected");
        //Toast.makeText(getApplicationContext(),regionId,Toast.LENGTH_SHORT).show();
        db = new DatabaseHandler(this);

        autoCompleteDistrictAdapter(new ArrayList<SelectedCities>());
        autoCompleteProductAdapter(new ArrayList<Products>());
        myRunnable1 = new ActivityIndentForm.MyRunnable1();

        myRunnable2 = new MyRunnable2();
        myRunnable3 = new MyRunnable3();


        entry = db.getactvitiestfa().entrySet().iterator().next();
        sp_activity.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, R.id.customSpinnerItemTextView, entry.getKey() ));

        custom_font = Typeface.createFromAsset(getAssets(),  "fonts/SEGOEWP-LIGHT.TTF");
        et_estimatedperhead.setTypeface(custom_font);
        et_district.setTypeface(custom_font);
        et_nooffarmers.setTypeface(custom_font);
        et_totalexp.setTypeface(custom_font);
        et_date.setTypeface(custom_font);
        et_villages_list.setTypeface(custom_font);
        et_village.setTypeface(custom_font);
        et_taluka.setTypeface(custom_font);
        et_district.setTypeface(custom_font);
        et_product.setTypeface(custom_font);
        et_adv.setTypeface(custom_font);
    }

    private void calinit() {
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
        formattedDate= df.format(c);


        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);

        setDateTimeField();
    }
    private void setDateTimeField() {



        Calendar newCalendar = Calendar.getInstance();

        fromDatePickerDialog = new DatePickerDialog(this, new DatePickerDialog.OnDateSetListener() {

            public void onDateSet(DatePicker view, int year, int monthOfYear, int dayOfMonth) {
                Calendar newDate = Calendar.getInstance();
                newDate.set(year, monthOfYear, dayOfMonth-1);

                Calendar newDate2 = Calendar.getInstance();
                newDate2.set(year, monthOfYear, dayOfMonth);

                et_date.setText(dateFormatter.format(newDate2.getTime()));


            }

        },newCalendar.get(Calendar.YEAR), newCalendar.get(Calendar.MONTH), newCalendar.get(Calendar.DAY_OF_MONTH));


    }

    @Override
    public void onClick(View view) {
        if(view.getId()==R.id.et_date)
        {
            fromDatePickerDialog.getDatePicker().setMinDate(System.currentTimeMillis() - 1000);

            fromDatePickerDialog.show();

        }
    }
    public void autoCompleteProductAdapter(List<Products> list) {
        final CustomListProductAdapter adapter = new CustomListProductAdapter(this,
                R.layout.autocomplete_custom_layout, list);
//            Log.d("listtostring",list.get(0)._product_name);

        et_product.setAdapter(adapter);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                et_product.showDropDown();
            }
        }, 100);





        adapter.setOnItemClickListener(new CustomListProductAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, Products item) {
                onProductItemClick = true;
                et_product.setText(item.getProductName().toUpperCase());
                et_product.setSelection(item.getProductName().length());
                ProductMasterId = item.getProductMasterId();
                et_product.dismissDropDown();
                Common.hideSoftKeyboard(ActivityIndentForm.this);

            }
        });

    }
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
                Common.hideSoftKeyboard(ActivityIndentForm.this);

            }
        });

    }



    class MyRunnable1 implements Runnable {
        private String text;

        public void setData(String text) {
            this.text = text;
        }

        @Override
        public void run() {

            String key = "'%" + text + "%'";
            final List<District> districts = db.getDistrictsByRegionID(key, regionId);
            distObjs.clear();
            if (districts != null && districts.size() > 0) {
                for (int i = 0; i < districts.size(); i++) {
                    SelectedCities obj = new SelectedCities();
                    obj.setCityId(districts.get(i).districtId);
                    obj.setCityName(districts.get(i).districtName);
                    // Toast.makeText(getApplicationContext(),districts.get(i).districtName,Toast.LENGTH_SHORT).show();
                    distObjs.add(obj);
                }
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!onDistrictItemClick && et_district.isEnabled()) {
                        //   districtId = "0";
                        autoCompleteDistrictAdapter(distObjs);
                    } else onDistrictItemClick = false;
                }
            });


        }
    }
    class MyRunnable2 implements Runnable {
        private String text;

        public void setData2(String text) {
            this.text = text;
        }

        @Override
        public void run() {

            String key = "'%" + text + "%'";
            final List<Products> productslist = db.getProductByDevisionIdandCropId(key, divisionId,cropId);
            Log.d("productslist",productslist.toString());
            ProductObjs.clear();
            if (productslist != null && productslist.size() > 0) {
                for (int i = 0; i < productslist.size(); i++) {
                    Products obj = new Products();
                    obj.setProductMasterID(productslist.get(i)._product_masterid);
                    obj.setProductName(productslist.get(i)._product_name);
                    // Toast.makeText(getApplicationContext(),productslist.get(i)._product_name,Toast.LENGTH_SHORT).show();
                    ProductObjs.add(obj);
                }
            }
            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    if (!onProductItemClick && et_product.isEnabled()) {
                        //   districtId = "0";
                        autoCompleteProductAdapter(ProductObjs);
                    } else onProductItemClick = false;
                }
            });


        }
    }
    class MyRunnable3 implements Runnable {
        private String text;

        public void setData(String text) {
            this.text = text;
        }

        @Override
        public void run() {
            //text = "   " + T(et_estimatedperhead);  //it will execute after some time because of handler

            runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    et_nooffarmers.setText(text);
                }
            });
        }
    }
    private void getregion() {
        String selectQuery = "SELECT " + KEY_TABLE_USERS_HEADQUARTER + "," + KEY_REGION__MASTER_ID + " FROM " + TABLE_USERS + "  WHERE " + KEY_TABLE_USERS_MASTER_ID + " = " + User_id;
        //List<Company_division_crops> cdclist = db.getAllCompany_division_crops();

        sdbw = db.getWritableDatabase();

        Cursor cursor = sdbw.rawQuery(selectQuery, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor.moveToFirst()) {
            do {

                region = cursor.getString(0);
                regionId = cursor.getString(1);


            } while (cursor.moveToNext());
        }
    }

    public void submitact(View v) throws JSONException {
        Handler handlerbtn = Common.disableClickEvent(btn_appt, true);
        if (T(et_district).equals("") || districtId.equals("0")) {
            Toast("Please select district");
            et_district.requestFocus();
        } else if (spin_division.getSelectedItemPosition() == 0) {
            Toast("Please select division");
        } else if (spin_crop.getSelectedItemPosition() == 0) {
            Toast("Please select crop");
        } else if (T(et_product).equals("") || ProductMasterId.equals("0")) {
            Toast("Please select product");
            et_product.requestFocus();
        }  else if (T(et_taluka).equals("") || T(et_taluka) == null) {
            Toast("Please enter taluka");
            et_taluka.requestFocus();
        } else if (T(et_village).equals("") || T(et_village) == null) {
            Toast("Please enter village");
            et_village.requestFocus();
        }
        else if (T(et_villages_list).equals("") || T(et_villages_list) == null)
        {
            Toast("Please enter villages list");
            et_villages_list.requestFocus();
        }
        else if (T(et_nooffarmers).equals("") || T(et_nooffarmers) == null) {
            Toast("Please enter number of farmers");
            et_nooffarmers.requestFocus();
        } else if (T(et_estimatedperhead).equals("") || T(et_estimatedperhead) == null) {
            Toast("Please enter estimation per head");
            et_estimatedperhead.requestFocus();
        } else if (T(et_adv).equals("") || T(et_adv) == null) {
            Toast("Please enter advance required");
            et_adv.requestFocus();
        }
        else {

            getuserdetails();
            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Log.d("hi",seluserId+"  "+User_id);//it is loggin id
            int apprv_sts2 = 1;
            String cmts="Pending";
            String apprv_by=seluserId;
            if(role == Constants.Roles.ROLE_17)
            {
                apprv_sts2 = 1;
                cmts="Pending";
                apprv_by=seluserId;

            }
            if(role == Constants.Roles.ROLE_7)
            {
                apprv_sts2=2;
                cmts= "Approved By MO";
                apprv_by=User_id;

            }
               /* if(role == Constants.Roles.ROLE_6)
                {
                    apprv_sts2=2;
                    cmts= "Approved By AM";
                    apprv_by=0;
                }
                if(role == Constants.Roles.ROLE_5)
                {
                    apprv_sts2=3;
                    cmts= "Approved By RM";
                    apprv_by=0;
                }*/
            if(apprv_by.equals("")||apprv_by==null)
            {
                apprv_by="0";
            }
            ActivityPlanner act=
                    new ActivityPlanner(
                            Integer.valueOf(districtId),
                            Integer.valueOf(divisionId),
                            Integer.valueOf(cropId),
                            Integer.valueOf(ProductMasterId),
                            sp_activity.getSelectedItemPosition()+1,
                            Integer.valueOf(T(et_nooffarmers)),
                            Integer.valueOf(T(et_estimatedperhead)),
                            Integer.valueOf(T(et_totalexp)),
                            Integer.valueOf(T(et_adv)),
                            Integer.valueOf(seluserId),
                            Integer.valueOf(User_id),
                            1,
                            et_date.getText().toString(),
                            sdf.format(c.getTime()),
                            sdf.format(c.getTime()),
                            et_taluka.getText().toString(),
                            et_village.getText().toString(),
                            et_village.getText().toString(),
                            "",
                            apprv_sts2,
                            cmts,
                            Integer.valueOf(apprv_by),
                            "",approval_name,approval_role,approval_mail,approval_pnno);
            longsss=db.addPlannerActivity_Form(act,"local");

            ch=longsss.get(0);
            if(ch==-10)
            {
                Toast("Activity already exists with same product ,day ,activity and user");
                return;
            }
            if(ch<=0)
            {
                Toast("Record not inserted");
                return;
            }
            Long l= new Long(ch);
            int i=l.intValue();
            long ch1=0;

            //Long l= new Long(ch);
            for(int JK=0;JK<ar_vill.length;JK++) {
                //   int m=l.intValue();
                Village_list village_list = new Village_list(i, ar_cs[JK], ar_p[JK], 1, 0, ar_vill[JK], sdf.format(c.getTime()));
                ch1=db.addVillageList(village_list,"local");
                Long l1= new Long(ch1);
                int i1=l1.intValue();
                Log.d("ch1",String.valueOf(i1));
                Common.disableClickEvent(btn_appt, handlerbtn);
            }


            if(ch>0&&ch1>0)
            {
                if (Common.haveInternet(getApplicationContext()))
                {



                    Calendar c1 = Calendar.getInstance();
                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                    JSONObject mainobj = new JSONObject();
                    mainobj.put("tfa_list_id",ch);
                    mainobj.put("district_id",districtId);
                    mainobj.put("division_id",divisionId);
                    mainobj.put("crop_id",cropId);
                    mainobj.put("product_id",ProductMasterId);

                    mainobj.put("tfa_activity_master_id",entry.getValue().get(sp_activity.getSelectedItemPosition()));
                    mainobj.put("activity_date",T(et_date));
                    mainobj.put("taluka",T(et_taluka));
                    mainobj.put("village",T(et_village));
                    mainobj.put("no_of_farmers",T(et_nooffarmers));

                    mainobj.put("estimation_per_head",T(et_estimatedperhead));
                    mainobj.put("total_expences",T(et_totalexp));
                    mainobj.put("advance_required",T(et_adv));
                    mainobj.put("conducting_place",T(et_village));
                    mainobj.put("user_id",seluserId);

                    mainobj.put("created_user_id",User_id);
                    mainobj.put("status",1);
                    mainobj.put("sync_status",0);
                    mainobj.put("created_datetime",sdf1.format(c1.getTime()));


                    JSONArray village_list_array = new JSONArray();
                    JSONArray village_list_array1 = new JSONArray();



                    JSONObject villages_obj1 = new JSONObject();
                    villages_obj1.put("tfa_approval_id", longsss.get(1));//not required
                    villages_obj1.put("tfa_list_id", ch);
                    villages_obj1.put("approval_status", apprv_sts2);
                    villages_obj1.put("approval_comment", cmts);
                    villages_obj1.put("approved_user_id", apprv_by);

                    villages_obj1.put("approved_user_name", approval_name);
                    villages_obj1.put("approved_user_role", approval_role);
                    villages_obj1.put("approved_user_email", approval_mail);
                    villages_obj1.put("approved_user_mobile", approval_pnno);

                    villages_obj1.put("status", "1");
                    villages_obj1.put("sync_status", "0");
                    villages_obj1.put("created_datetime",sdf1.format(c1.getTime()));
                    village_list_array1.put(villages_obj1);
                    mainobj.put("village_list_array1", village_list_array1);

                    for(int JK=0;JK<ar_vill.length;JK++) {
                        {
                            JSONObject villages_obj = new JSONObject();
                            try {
                                int ch11;
                                Long l2 = new Long(ch1);
                                int i2 = l2.intValue();
                                ch11 = i2 - (ar_vill.length - (JK + 1));
                                al_localids_vill.add(String.valueOf(ch11));
                                Log.d("ch11", String.valueOf(ch11));
                                villages_obj.put("tfa_village_id", String.valueOf(ch11));
                                villages_obj.put("tfa_list_id", ch);
                                villages_obj.put("village_name", ar_vill[JK]);
                                villages_obj.put("current_sal", ar_cs[JK]);
                                villages_obj.put("potential", ar_p[JK]);
                                villages_obj.put("status", "1");
                                villages_obj.put("sync_status", "0");
                                villages_obj.put("created_datetime", sdf1.format(c1.getTime()));
                                village_list_array.put(villages_obj);
                            } catch (JSONException e) {
                                e.printStackTrace();
                            }



                        }


                    }
                    mainobj.put("village_list_array", village_list_array);
                    Log.d("mainobj", (mainobj.toString()));
                    Common.disableClickEvent(btn_appt, handlerbtn);
                    new Async_UpdateActivityForm().execute(mainobj.toString());
                }
                else
                {
                    Intent it = new Intent(ActivityIndentForm.this, ActivityIndent.class);
                    it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(it);
                }

            }
            else {
                Toast("Something went wrong");

            }
        }
    }

    private void getuserdetails() {
        String selectQuery = "SELECT "+ KEY_TABLE_USERS_FIRST_NAME +","+ KEY_TABLE_USERS_MOBILE_NO + ","+KEY_TABLE_USERS_EMAIL +","+KEY_TABLE_USERS_DESIGNATION +" FROM " + TABLE_USERS + "  WHERE " + KEY_TABLE_USERS_MASTER_ID + " = " + User_id;
        //List<Company_division_crops> cdclist = db.getAllCompany_division_crops();

        sdbw = db.getWritableDatabase();

        Cursor cursor = sdbw.rawQuery(selectQuery, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor.moveToFirst()) {
            do {
                Users users = new Users();

                Log.e("-----","fname : "+cursor.getString(0)+"lname : "+cursor.getString(1)+"mobile : "+cursor.getString(2)+"email : "+cursor.getString(3));
                approval_name=cursor.getString(0);
                approval_pnno=cursor.getString(1);
                approval_mail=cursor.getString(2);
                approval_role=cursor.getString(3);



            } while (cursor.moveToNext());
        }
    }

    public class MyAdapter extends RecyclerView.Adapter<MyAdapter.MyViewHolder> {
        private ArrayList<Integer> mDataset=new ArrayList<>();
        private Activity context;

        public  class MyViewHolder extends RecyclerView.ViewHolder {
            // each data item is just a string in this case

            @BindView(R.id.et_villagename1)
            EditText et_villagename1;
            @BindView(R.id.et_currentvillagesale1)
            EditText et_currentvillagesale1;

            @BindView(R.id.et_villagepotential1)
            EditText et_villagepotential1;
            @BindView(R.id.et_addall)
            EditText et_addall;
            @BindView(R.id.ll_rl)
            RelativeLayout ll_rl;
            VillageTextWatcher villageTextWatcher;
            CurrentsaleTextWatcher currentsaleTextWatcher;
            PotentialTextWatcher potentialTextWatcher;
            public MyViewHolder(View v,VillageTextWatcher villageTextWatcher,CurrentsaleTextWatcher currentsaleTextWatcher,PotentialTextWatcher potentialTextWatcher) {
                super(v);
                ButterKnife.bind(this,itemView);
                this.villageTextWatcher = villageTextWatcher;
                this.et_villagename1.addTextChangedListener(villageTextWatcher);
                this.currentsaleTextWatcher = currentsaleTextWatcher;
                this.et_currentvillagesale1.addTextChangedListener(currentsaleTextWatcher);
                this.potentialTextWatcher = potentialTextWatcher;
                this.et_villagepotential1.addTextChangedListener(potentialTextWatcher);

            }


        }
        public MyAdapter(Activity context, ArrayList myDataset) {
            mDataset = myDataset;
            this.context=context;
        }
        @Override
        public MyAdapter.MyViewHolder onCreateViewHolder(ViewGroup parent,
                                                         int viewType)
        {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.my_text_view, parent, false);
            return new MyViewHolder(v, new VillageTextWatcher(),new CurrentsaleTextWatcher(),new PotentialTextWatcher());

            //MyViewHolder vh = new MyViewHolder(context.getLayoutInflater().inflate(R.layout.my_text_view,  new VillageTextWatcher(),new CurrentsaleTextWatcher(),new PotentialTextWatcher()));
            //return vh;

        }
        @Override
        public void onBindViewHolder(MyViewHolder holder, @SuppressLint("RecyclerView") int position)
        {
            holder.potentialTextWatcher.updatePosition(holder.getAdapterPosition(), holder.et_villagepotential1);
            holder.currentsaleTextWatcher.updatePosition(holder.getAdapterPosition(), holder.et_currentvillagesale1);
            holder.villageTextWatcher.updatePosition(holder.getAdapterPosition(), holder.et_villagename1);
            ar_cs=new String[mDataset.size()];
            ar_vill=new String[mDataset.size()];
            ar_p=new String[mDataset.size()];


            if(position==mDataset.size()-1)
            {
                holder.et_addall.setVisibility(View.GONE);
            }
            else
            {
                holder.et_addall.setVisibility(View.GONE);
            }
            holder.et_addall.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    remove.add(position);
                }
            });

            holder.et_villagepotential1.setTag(position);

            holder.et_villagepotential1.setTag(position);
            holder.et_currentvillagesale1.setTag(position);
        }
        public int getItemCount()
        {
            return Integer.valueOf(mDataset.size());
        }

        private class AmountTextWatcher implements TextWatcher {

            int position;
            EditText amountEditText;

            public void updatePosition(int position, EditText amountEditText) {
                this.position = position;
                this.amountEditText = amountEditText;
            }

            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        }

    }
    public class Async_UpdateActivityForm extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            progressDialog = new ProgressDialog(ActivityIndentForm.this);
            progressDialog.setMessage("Please wait ...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            //Toast.makeText(getApplicationContext(),"Updateing Advance Bookings",Toast.LENGTH_LONG).show();
        }

        @Override
        protected String doInBackground(String... params) {

            try {

                OkHttpClient client = new OkHttpClient();

                MediaType mediaType = MediaType.parse("application/octet-stream");
                RequestBody body = RequestBody.create(mediaType, params[0]);
                Request request = new Request.Builder()
                        .url(Constants.URL_TFA_LIST_DATA)
                        .post(body)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        // .addHeader("postman-token", "6bb3394a-3eaa-d4ae-cd37-0257afbdf3db")
                        .build();

                Response response = null;
                //client.newCall(request).execute();

                try {
                    response = client.newCall(request).execute();
                    jsonData = response.body().string();
                    System.out.println("!!!!!!!1Sales Oreder" + jsonData);
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
            if (progressDialog.isShowing())
                progressDialog.dismiss();
            Log.e("anil", jsonData);
            if (jsonData != null) {
                try {
                    JSONObject obj=new JSONObject(jsonData);
                    if(obj.getString("Status").equals("Success"))
                    {

                        Long l= new Long(ch);

                        String id=obj.getString("InsertedListServerData") ;
                        String InsertedApprovalServerData=obj.getString("InsertedApprovalServerData") ;
                        JSONArray array_formactivity_id=obj.getJSONArray("InsertedVillServerData") ;
                        String currentString = obj.getString("InsertedUserData");
                        String[] separated = currentString.split("::::");
                        String name=separated[0];
                        String role=separated[1];


                        String sss= db.update_ids_tfaformactivity(id,array_formactivity_id,String.valueOf(l.intValue()),
                                al_localids_vill,String.valueOf(longsss.get(0)),InsertedApprovalServerData,name,role);

                        if(sss.equals("sucess"))
                        {
                            Intent it = new Intent(ActivityIndentForm.this, ActivityIndent.class);
                            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(it);
                        }


                    }
                    else if(obj.getString("Status").equals("error"))
                    {

                        Toast(obj.getString("Status"));
                    }
                    else
                    {
                        Toast("Something went wrong!!");
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

    private class VillageTextWatcher implements TextWatcher {
        int position;
        EditText et_village;


        public void updatePosition(int position, EditText et_village) {
            this.position = position;
            this.et_village = et_village;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            ar_vill[position]=et_village.getText().toString();
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }
    private class CurrentsaleTextWatcher implements TextWatcher {
        int position;
        EditText et_cs;


        public void updatePosition(int position, EditText et_cs) {
            this.position = position;
            this.et_cs = et_cs;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            ar_cs[position]=et_cs.getText().toString();
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }
    private class PotentialTextWatcher implements TextWatcher {
        int position;
        EditText et_p;


        public void updatePosition(int position, EditText et_p) {
            this.position = position;
            this.et_p = et_p;
        }

        @Override
        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

        }

        @Override
        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            ar_p[position]=et_p.getText().toString();
        }

        @Override
        public void afterTextChanged(Editable editable) {

        }
    }
}
