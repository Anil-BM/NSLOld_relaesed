package com.nsl.beejtantra.TFA;

import android.app.DatePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.nsl.beejtantra.Constants;
import com.nsl.beejtantra.DatabaseHandler;
import com.nsl.beejtantra.R;
import com.nsl.beejtantra.SelectedCities;
import com.nsl.beejtantra.Utility;
import com.nsl.beejtantra.adapters.CustomListAdapter;
import com.nsl.beejtantra.commonutils.Common;
import com.nsl.beejtantra.pojo.District;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import static com.nsl.beejtantra.DatabaseHandler.KEY_REGION__MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CROPS_CROP_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CROPS_CROP_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CROPS_DIVISION_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_DIVISION_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_DIVISION_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USERS_HEADQUARTER;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USERS_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_CROPS;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_DIVISION;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_USERS;

public class ActivityIndentForm extends AppCompatActivity implements View.OnClickListener {
    String formattedDate,region,regionId,User_id;
    String status;
    CheckBox ch_terms;
    private SimpleDateFormat dateFormatter,dateFormatter22;
    private DatePickerDialog fromDatePickerDialog;
    EditText et_date,et_address,et_nooffarmers;
    AutoCompleteTextView et_district;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    String districtId, divisionId, divisionName,cropId,cropName;;
    DatabaseHandler db;
    SQLiteDatabase sdbw,sdbr;
    boolean check;
    String jsonData;
    List<SelectedCities> distObjs = new ArrayList<>();
    MyRunnable1 myRunnable1;
    List<SelectedCities> divisions = new ArrayList<>();
    List<SelectedCities> crops = new ArrayList<>();
    List<String> activities = new ArrayList<>();
    private boolean onDistrictItemClick;
    private Handler handler;
    Spinner spin_division,spin_crop,sp_activity ;
    ArrayList<String> divisionMarketAdapter;
    ArrayList<String> cropMarketadapter;
    Button btn_appt;
    String strDate;
    @Override
    protected void onStart() {
        super.onStart();
        Log.d("lifecycle","onStart invoked2");
    }
    @Override
    protected void onResume() {
        super.onResume();
        Log.d("lifecycle","onResume invoked2");
    }
    @Override
    protected void onPause() {
        super.onPause();
        Log.d("lifecycle","onPause invoked2");
    }
    @Override
    protected void onStop() {
        super.onStop();
        Log.d("lifecycle","onStop invoked2");
    }
    @Override
    protected void onRestart() {
        super.onRestart();
        Log.d("lifecycle","onRestart invoked2");
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        Log.d("lifecycle","onDestroy invoked2");
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_indent_form);

        Log.d("lifecycle","onCreate invoked2");

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
        btn_appt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(getApplicationContext(),districtId,Toast.LENGTH_SHORT).show();
                if(et_district.getText().toString().equals("")||districtId==null||districtId.equals("0"))
                {
                    Toast.makeText(getApplicationContext(),"Select District",Toast.LENGTH_SHORT).show();
                }
                else if(spin_division.getSelectedItemPosition()==0)
                {
                    Toast.makeText(getApplicationContext(),"Select DIvision",Toast.LENGTH_SHORT).show();
                }
                else if(sp_activity.getSelectedItemPosition()==0)
                {
                    Toast.makeText(getApplicationContext(),"Select Activity/Event",Toast.LENGTH_SHORT).show();
                }
                else if(spin_crop.getSelectedItemPosition()==0)
                {
                    Toast.makeText(getApplicationContext(),"Select Crop",Toast.LENGTH_SHORT).show();
                }
                else if(et_date.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Select Date",Toast.LENGTH_SHORT).show();
                }
                else if(et_address.getText().toString().equals(""))
                {
                    Toast.makeText(getApplicationContext(),"Enter Address",Toast.LENGTH_SHORT).show();
                }
                else if(et_nooffarmers.getText().toString().equals("")||Integer.valueOf(et_nooffarmers.getText().toString())==0)
                {
                    Toast.makeText(getApplicationContext(),"Enter No Of Farmers",Toast.LENGTH_SHORT).show();
                }
                else if(!check)
                {
                    Toast.makeText(getApplicationContext(),"Please indicate that you accept the Items.",Toast.LENGTH_SHORT).show();
                }
                else{
                    Calendar c = Calendar.getInstance();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                     strDate = sdf.format(c.getTime());

                    if (Utility.isNetworkAvailable(ActivityIndentForm.this, Constants.isShowNetworkToast)) {
                        // connected to the internet
                        Demandgeneration_add demandgeneration_add=new Demandgeneration_add(User_id,districtId
                                ,divisionId,cropId,
                                sp_activity.getSelectedItem().toString()
                                ,et_address.getText().toString(),et_date.getText().toString(),et_nooffarmers.getText().toString()
                                ,"0",strDate,strDate);
                         status=db.adddemangeneration(demandgeneration_add,"local");
                        if(!status.equals("0"))
                        {
                            new Async_postdemandgeneration().execute();
                        }
                        else if(status.equals("0"))
                        {
                            Toast.makeText(getApplicationContext(),"Data Already Exists On This Date and Event",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Something Went Wrong",Toast.LENGTH_SHORT).show();
                        }



                    } else {
                        Demandgeneration_add demandgeneration_add=new Demandgeneration_add(User_id,districtId
                                ,divisionId,cropId,
                                sp_activity.getSelectedItem().toString()
                                ,et_address.getText().toString(),et_date.getText().toString(),et_nooffarmers.getText().toString()
                                ,"0",strDate,strDate);
                         status=db.adddemangeneration(demandgeneration_add,"local");

                        if(!status.equals("0"))
                        {
                            Toast.makeText(getApplicationContext(),"Created Successfully",Toast.LENGTH_SHORT).show();
                            Intent it=new Intent(ActivityIndentForm.this,ActivityIndent.class);
                            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(it);
                        }
                        else if(status.equals("0")){
                            Toast.makeText(getApplicationContext(),"Already Exists On This Date and Event",Toast.LENGTH_SHORT).show();
                        }
                        else{
                            Toast.makeText(getApplicationContext(),"Something Went Wrong",Toast.LENGTH_SHORT).show();
                        }
                    }





                }

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

                    String cropQuery = "SELECT DISTINCT cr." + KEY_TABLE_CROPS_CROP_ID + ",cr." + KEY_TABLE_CROPS_CROP_NAME + " FROM " + TABLE_CROPS + " cr WHERE cr." + KEY_TABLE_CROPS_DIVISION_ID + " = " + divisionId;
                    Common.Log.i("CROP QUERY " + cropQuery);
                    Cursor cropCursor = db.getWritableDatabase().rawQuery(cropQuery, null);
                    cropMarketadapter.clear();
                    crops.clear();
                    cropMarketadapter.add("Select Crop");
                    if (cropCursor.moveToFirst()) {
                        do {
                            SelectedCities crop = new SelectedCities();
                            crop.setCityId(cropCursor.getString(cropCursor.getColumnIndex(KEY_TABLE_CROPS_CROP_ID)));
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
                    cropName = crops.get(position - 1).getCityName();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        ch_terms.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener()
        {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked)
            {
                if ( isChecked )
                {
                    check=true;
                }
                else {
                    check=false;
                }

            }
        });


    }






    private void init() {
        btn_appt=(Button)findViewById(R.id.btn_appt);
        sp_activity = (Spinner) findViewById(R.id.sp_activity);
        spin_division = (Spinner) findViewById(R.id.sp_division);
        spin_crop= (Spinner) findViewById(R.id.sp_crop);
        et_district = (AutoCompleteTextView) findViewById(R.id.et_district);

        et_date=(EditText)findViewById(R.id.et_date);
        et_nooffarmers=(EditText)findViewById(R.id.et_noofformaers);
        et_date.setText(getIntent().getStringExtra("dateselected"));


        et_address = (EditText) findViewById(R.id.et_village);


        ch_terms = (CheckBox) findViewById( R.id.ch_terms );


        et_date.setOnClickListener(this);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        User_id           = sharedpreferences.getString("userId", "");
        //Toast.makeText(getApplicationContext(),regionId,Toast.LENGTH_SHORT).show();
        db = new DatabaseHandler(this);

        autoCompleteDistrictAdapter(new ArrayList<SelectedCities>());
        myRunnable1 = new ActivityIndentForm.MyRunnable1();

        activities.add("Select The Activity/Event");
        activities.add("Field visit");
        activities.add("Field day");
        activities.add("Mega field day");
        activities.add("Picking efficiency");
        activities.add("Yield estimation");
        activities.add("Crop cutting experiment");
        activities.add("Farmer Sanman");
        activities.add("Villege level meetings");
        activities.add("Group meeting");
        activities.add("Mega farmer meeting");
        activities.add("Jeep campaign");
        activities.add("Dealer meet");
        activities.add("Folk activity");
        activities.add("Exhibition");
        activities.add("Door to door campaign");
        activities.add("other DG activity");
        sp_activity.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, R.id.customSpinnerItemTextView, activities));

        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/SEGOEWP-LIGHT.TTF");
        et_district.setTypeface(custom_font);
        et_date.setTypeface(custom_font);
        et_address.setTypeface(custom_font);
        et_district.setTypeface(custom_font);
        et_nooffarmers.setTypeface(custom_font);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

    private void calinit() {
        Date c = Calendar.getInstance().getTime();
        System.out.println("Current time => " + c);

        SimpleDateFormat df = new SimpleDateFormat("dd-MM-yyyy");
        formattedDate= df.format(c);


        dateFormatter = new SimpleDateFormat("yyyy-MM-dd", Locale.US);
        dateFormatter22 = new SimpleDateFormat("dd-MM-yyyy", Locale.US);
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

    private void getregion() {
        String selectQuery = "SELECT "+KEY_TABLE_USERS_HEADQUARTER+","+KEY_REGION__MASTER_ID+ " FROM " + TABLE_USERS + "  WHERE " + KEY_TABLE_USERS_MASTER_ID + " = " + User_id;
        //List<Company_division_crops> cdclist = db.getAllCompany_division_crops();

        sdbw = db.getWritableDatabase();

        Cursor cursor = sdbw.rawQuery(selectQuery, null);
        //System.out.println("cursor count "+cursor.getCount());
        if (cursor.moveToFirst()) {
            do {

                region=cursor.getString(0);
                regionId=cursor.getString(1);


            } while (cursor.moveToNext());
        }
    }

    private class Async_postdemandgeneration extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(Void... params) {


            try {
                OkHttpClient client = new OkHttpClient();
                /*For passing parameters*/

                Response responses = null;

                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");


                RequestBody body = RequestBody.create(mediaType, "user_id=" + User_id+ "&district_id="
                        +districtId + "&division_id=" +divisionId
                        + "&crop_id=" +cropId+ "&activity_event=" + sp_activity.getSelectedItem().toString()
                        + "&no_of_farmers=" + et_nooffarmers.getText().toString() + "&date_of_event=" + et_date.getText().toString()
                        + "&address=" + et_address.getText().toString() + "&status=" + "" +
                        "&created_datetime=" + strDate+"&dg_id=" + status );
                Request request = new Request.Builder()
                        .url(Constants.BASE_URL+Constants.POST_DEMANDGENERATION)
                        .post(body)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();

                try {
                    responses = client.newCall(request).execute();

                    if (responses.isSuccessful()) {

                        Log.v("okhttp", "s");

                    } else {

                        Log.v("okhttp", "N");


                    }

                    jsonData = responses.body().string();
                    System.out.println("!!!!!!!Formone" + jsonData);


                    Log.d("suneel_pp", jsonData);


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
            //pb.setVisibility(View.INVISIBLE);
           //
            try {
                JSONObject jsonobject = new JSONObject(s);

                Toast.makeText(getApplicationContext(),jsonobject.getString("msg"),Toast.LENGTH_SHORT).show();
                if(jsonobject.getString("Status").equals("Success"))
                {
                    String ss=db.update_demandgeneration(User_id,status);
                    if(ss.equals("Sucess")) {
                        Intent it = new Intent(ActivityIndentForm.this, ActivityIndent.class);
                        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(it);
                    }
                   /* else {
                        Toast.makeText(getApplicationContext(),"Details Are Updates To Server But Not Stored In Your Phone",Toast.LENGTH_SHORT).show();
                    }*/

                }
                else {
                    Toast.makeText(getApplicationContext(),jsonobject.getString("msg"),Toast.LENGTH_SHORT).show();
                }
                /*if(jsonobject.getString("Status").equals("Invalid"))
                {
                    Intent it=new Intent(ActivityIndentForm.this,ActivityIndent.class);
                    startActivity(it);
                }*/
            } catch (JSONException e) {
                e.printStackTrace();
            }


        }
        }

    }

