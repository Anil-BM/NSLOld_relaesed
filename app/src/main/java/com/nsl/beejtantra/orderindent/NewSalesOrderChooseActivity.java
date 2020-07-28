package com.nsl.beejtantra.orderindent;


import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.graphics.Typeface;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.provider.Settings;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.nsl.beejtantra.Companies;
import com.nsl.beejtantra.Constants;
import com.nsl.beejtantra.Crops;
import com.nsl.beejtantra.CropsFragmentSalesorderActivity;
import com.nsl.beejtantra.Customers;
import com.nsl.beejtantra.DatabaseHandler;
import com.nsl.beejtantra.Divisions;
import com.nsl.beejtantra.MainActivity;
import com.nsl.beejtantra.R;
import com.nsl.beejtantra.SelectedCities;
import com.nsl.beejtantra.Users;
import com.nsl.beejtantra.adapters.CustomListAdapter;
import com.nsl.beejtantra.commonutils.Common;

import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;

import static com.nsl.beejtantra.DatabaseHandler.KEY_SEASON_LINE_ITEM_DIVISION_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_SEASON_LINE_ITEM_VALID_FROM;
import static com.nsl.beejtantra.DatabaseHandler.KEY_SEASON_LINE_ITEM_VALID_TO;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_COMPANIES_COMPANY_CODE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_COMPANIES_COMPANY_SAP_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_COMPANIES_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_COMPANIES_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CUSTOMER_CITY;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CUSTOMER_CODE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CUSTOMER_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CUSTOMER_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_DIVISION_CODE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_DIVISION_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_DIVISION_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USERS_FIRST_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USERS_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USERS_REPORTING_MANAGER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USERS_USER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USER_COMPANY_CUSTOMER_CUSTOMER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USER_COMPANY_CUSTOMER_STATUS;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USER_COMPANY_CUSTOMER_USER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USER_COMPANY_DIVISION_COMPANY_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USER_COMPANY_DIVISION_DIVISION_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USER_COMPANY_DIVISION_USER_ID;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_COMPANIES;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_CUSTOMERS;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_DIVISION;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_SEASON;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_SEASON_LINE_ITEMS;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_USERS;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_USER_COMPANY_CUSTOMER;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_USER_COMPANY_DIVISION;

/**
 * A simple {@link Fragment} subclass.
 */
public class NewSalesOrderChooseActivity extends AppCompatActivity {
    // JSON parser class

    ProgressDialog progressDialog;
    boolean  time_enabled=false;

    TextView tv_name, tv_code;
    Spinner spin_user,spin_season, spin_company, spin_customer, spin_division, spin_scheme;
    Button btn_savebooing;

    String sel_company_id, sel_customer_id, sel_product_id, sel_division_id, finalJson, company_id, customer_id, order_date, status, orderidfromserviceorder;

    private ArrayList<SelectedCities> organisations;
    ArrayList<String> adaptercity;

    private ArrayList<SelectedCities> arlist_products;
    ArrayList<String> adapter_products;

    private ArrayList<SelectedCities> arlist_crops;
    ArrayList<String> adapter_crops;

    private ArrayList<SelectedCities> arlist_schemes;
    ArrayList<String> adapter_schemes;
    private TextView tv_user, tv_company, tv_division, tv_customer,text_season;
    String jsonData, str_qty, str_amount, userId;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> cropids = new ArrayList<HashMap<String, String>>();

    DateFormat dateFormat, orderdateFormat;
    Date myDate;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";

    public static String companyname, customername, companycode, divisionname;
    DatabaseHandler db;
    SQLiteDatabase sdbw, sdbr;
    private AutoCompleteTextView auto_tv;
    private ArrayList<Users> users;
    private ArrayList<String> adapterusers;
    String sel_userId;
    private String team;
    private int role;
    LinearLayout ll_line;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newsalesorderchoose);

        db = new DatabaseHandler(this);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        userId = sharedpreferences.getString("userId", "");
        team = sharedpreferences.getString("team", "");
        role = sharedpreferences.getInt(Constants.SharedPrefrancesKey.ROLE, 0);
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

        tv_user = (TextView) findViewById(R.id.text_user);
        tv_company = (TextView) findViewById(R.id.text_company);
        tv_customer = (TextView) findViewById(R.id.text_customer);
        tv_division = (TextView) findViewById(R.id.text_division);
        text_season = (TextView) findViewById(R.id.text_season);
        ll_line=(LinearLayout) findViewById(R.id.ll_line);
        text_season.setVisibility(View.VISIBLE);
        ll_line.setVisibility(View.VISIBLE);

        spin_user = (Spinner) findViewById(R.id.spin_user);
        spin_season = (Spinner) findViewById(R.id.spin_season);
        spin_company = (Spinner) findViewById(R.id.spin_company);
        spin_customer = (Spinner) findViewById(R.id.spin_customer);
        spin_division = (Spinner) findViewById(R.id.spin_division);
        spin_season.setVisibility(View.VISIBLE);
        ArrayList<String> al_seasons=new ArrayList<>();
        al_seasons.add("Select Season");
        al_seasons.add("KHARIF"+"-"+ Calendar.getInstance().get(Calendar.YEAR));
        al_seasons.add("RABI"+"-"+ Calendar.getInstance().get(Calendar.YEAR));
        al_seasons.add("SPRING"+"-"+ Calendar.getInstance().get(Calendar.YEAR));
        al_seasons.add("VEGETABLES"+"-"+ Calendar.getInstance().get(Calendar.YEAR));
        al_seasons.add("SUMMER"+"-"+ Calendar.getInstance().get(Calendar.YEAR));
        spin_season.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, R.id.customSpinnerItemTextView, al_seasons));
        auto_tv = (AutoCompleteTextView) findViewById(R.id.auto_tv);

        Typeface typeface = Typeface.createFromAsset(getAssets(), Constants.CUSTOM_FONT_PATH_LIGHT);
        auto_tv.setTypeface(typeface);

        callapi();
        if (getIntent().getExtras() != null && getIntent().getExtras().getString("selection") != null && getIntent().getExtras().getString("selection", "defaultKey").equalsIgnoreCase("distributor")) {
            tv_customer.setVisibility(View.GONE);
            spin_customer.setVisibility(View.GONE);
        }

        btn_savebooing = (Button) findViewById(R.id.btn_savebooking);
        btn_savebooing.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String season = null;
                if (getIntent().getExtras() != null &&
                        getIntent().getExtras().getString("selection") != null
                        && getIntent().getExtras().getString("selection", "defaultKey").equalsIgnoreCase("distributor"))
                {
                    if (sel_company_id == null || sel_company_id == "0" || sel_division_id == null || sel_division_id == "0"||spin_season.getSelectedItemPosition()==0) {
                        Toast.makeText(NewSalesOrderChooseActivity.this, "Please select Company, Division,Season and go to booking ", Toast.LENGTH_SHORT).show();
                    } /*else if (!isValidDivision()) {
                        Toast.makeText(NewSalesOrderChooseActivity.this, "Selected Division is not existed in the Active Season.", Toast.LENGTH_SHORT).show();
                    }*/ else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            try {
                                if (Settings.Global.getInt(getContentResolver(), Settings.Global.AUTO_TIME) == 1)
                                {


                                    Log.v("suneel_time_enabled", "time_enabled");
                                    time_enabled=true;

                                } else {

                                    time_enabled=false;
                                    startActivity(new Intent(Settings.ACTION_DATE_SETTINGS));


                                }
                            } catch (Settings.SettingNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                        if(time_enabled==true)
                        {
                            if(spin_season.getSelectedItemPosition()==1)
                                season="KHARIF"+"-"+ Calendar.getInstance().get(Calendar.YEAR);
                            if(spin_season.getSelectedItemPosition()==2)
                                season="RABI"+"-"+ Calendar.getInstance().get(Calendar.YEAR);
                            if(spin_season.getSelectedItemPosition()==3)
                                season="SPRING"+"-"+ Calendar.getInstance().get(Calendar.YEAR);
                            if(spin_season.getSelectedItemPosition()==4)
                                season="VEGETABLES"+"-"+ Calendar.getInstance().get(Calendar.YEAR);
                            if(spin_season.getSelectedItemPosition()==5)
                                season="SUMMER"+"-"+ Calendar.getInstance().get(Calendar.YEAR);

                            Intent cropsproducts = new Intent(getApplicationContext(), CropsFragmentSalesorderActivity.class);
                            cropsproducts.putExtra("sel_user_id", sel_userId);
                            cropsproducts.putExtra("season", season);
                            startActivity(cropsproducts);

                            finish();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Please enable auto date time option",Toast.LENGTH_LONG).show();
                        }

                    }
                }
                else
                {
                    if (sel_company_id == null || sel_company_id == "0" || sel_customer_id == null || sel_customer_id == "0" || sel_division_id == null || sel_division_id == "0"||spin_season.getSelectedItemPosition()==0 || !auto_tv.getText().toString().trim().equalsIgnoreCase(customername)) {
                        Toast.makeText(NewSalesOrderChooseActivity.this, "Please select Company, Customer, Season and Division and go to booking ", Toast.LENGTH_SHORT).show();
                    }/* else if (!isValidDivision()) {
                        Toast.makeText(NewSalesOrderChooseActivity.this, "Selected Division is not existed in the Active Season.", Toast.LENGTH_SHORT).show();
                    }*/ else {
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                            try {
                                if (Settings.Global.getInt(getContentResolver(), Settings.Global.AUTO_TIME) == 1)
                                {


                                    Log.v("suneel_time_enabled", "time_enabled");
                                    time_enabled=true;

                                } else {

                                    time_enabled=false;
                                    startActivity(new Intent(Settings.ACTION_DATE_SETTINGS));


                                }
                            } catch (Settings.SettingNotFoundException e) {
                                e.printStackTrace();
                            }
                        }
                        if(time_enabled==true)
                        {
                            if(spin_season.getSelectedItemPosition()==1)
                                season="KHARIF"+"-"+ Calendar.getInstance().get(Calendar.YEAR);
                            if(spin_season.getSelectedItemPosition()==2)
                                season="RABI"+"-"+ Calendar.getInstance().get(Calendar.YEAR);
                            if(spin_season.getSelectedItemPosition()==3)
                                season="SPRING"+"-"+ Calendar.getInstance().get(Calendar.YEAR);
                            if(spin_season.getSelectedItemPosition()==4)
                                season="VEGETABLES"+"-"+ Calendar.getInstance().get(Calendar.YEAR);
                            if(spin_season.getSelectedItemPosition()==5)
                                season="SUMMER"+"-"+ Calendar.getInstance().get(Calendar.YEAR);

                            Intent cropsproducts = new Intent(getApplicationContext(), CropsFragmentSalesorderActivity.class);
                            cropsproducts.putExtra("sel_user_id", sel_userId);
                            cropsproducts.putExtra("season", season);
                            startActivity(cropsproducts);
                            finish();
                        }
                        else {
                            Toast.makeText(getApplicationContext(),"Please enable auto date time option",Toast.LENGTH_LONG).show();
                        }
                    }
                }
                /*Intent cropsproducts = new Intent(getApplicationContext(), CropsFragmentAdvancebookingActivity.class);
                startActivity(cropsproducts);*/
            }
        });


//        new AsyncGetofflineCompanies().execute();

    }

//    private boolean isValidDivision() {
//        String query = "SELECT " + KEY_SEASON_LINE_ITEM_VALID_FROM + "," + KEY_SEASON_LINE_ITEM_VALID_TO + " FROM " + TABLE_SEASON_LINE_ITEMS + " WHERE " + KEY_SEASON_LINE_ITEM_DIVISION_ID + " = " + sel_division_id + " AND " + KEY_SEASON_LINE_ITEM_VALID_FROM + " <= '" + Common.getCurrentDateYYYYMMDD() + "' AND " + KEY_SEASON_LINE_ITEM_VALID_TO + " >= '" + Common.getCurrentDateYYYYMMDD()+"'";
//        Cursor cursor = db.getWritableDatabase().rawQuery(query, null);
//        return cursor.getCount() > 0 ? true : false;
//    }

    private void callapi() {
        users = new ArrayList<Users>();
        adapterusers = new ArrayList<String>();
        users.clear();
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
//                selectQuery = "SELECT " + KEY_TABLE_USERS_FIRST_NAME + "," + KEY_TABLE_USERS_MASTER_ID + " FROM " + TABLE_USERS + "  WHERE " + db.KEY_TABLE_USERS_STATUS + " = 1 and user_id in (" + teamnew + ")";
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
                    users.add(userDEtail);
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
                sel_userId = String.valueOf(users.get(position).getID());
                tv_user.setTextColor(getResources().getColor(R.color.colorPrimary));
                if ((sel_userId.equalsIgnoreCase(Common.getUserIdFromSP(NewSalesOrderChooseActivity.this)) && role == Constants.Roles.ROLE_4))
                    btn_savebooing.setVisibility(View.GONE);
                else {
                    btn_savebooing.setVisibility(View.VISIBLE);
                    new AsyncGetofflineCompanies().execute();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



       /* tv_name.setText(getIntent().getStringExtra("customer_name"));
        tv_code.setText(getIntent().getStringExtra("customer_code"));*/


    }

    public class AsyncGetofflineCompanies extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(NewSalesOrderChooseActivity.this);
            progressDialog.setMessage("Please wait ...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            organisations = new ArrayList<SelectedCities>();
            adaptercity = new ArrayList<String>();
            organisations.clear();
            SelectedCities citiez = new SelectedCities();
            citiez.setCityId("0");
            citiez.setCityName("Select Company");
            // System.out.println("city id is :" + cityId + "city name is :" + cityName);
            organisations.add(citiez);
            adaptercity.add("Select Company");
            try {
                List<Crops> cdcList = new ArrayList<>();


//                String selectQuery = "SELECT  " + "CR." + KEY_TABLE_COMPANIES_MASTER_ID + "," + KEY_TABLE_COMPANIES_NAME + "," + KEY_TABLE_COMPANIES_COMPANY_CODE + "," + KEY_TABLE_COMPANIES_COMPANY_SAP_ID + " FROM " + TABLE_USER_COMPANY_DIVISION + " AS CDC JOIN " + TABLE_COMPANIES + " AS CR ON CDC." + KEY_TABLE_USER_COMPANY_DIVISION_COMPANY_ID + " = CR." + KEY_TABLE_COMPANIES_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_DIVISION_USER_ID + " = " + sel_userId + " group by(CR." + KEY_TABLE_COMPANIES_MASTER_ID + ")";
//                if(sel_userId.equalsIgnoreCase(Common.getUserIdFromSP(NewSalesOrderChooseActivity.this)))
                String selectQuery = "SELECT  " + "CR." + KEY_TABLE_COMPANIES_MASTER_ID + "," + KEY_TABLE_COMPANIES_NAME + "," + KEY_TABLE_COMPANIES_COMPANY_CODE + "," + KEY_TABLE_COMPANIES_COMPANY_SAP_ID + " FROM " + TABLE_USER_COMPANY_DIVISION + " AS CDC JOIN " + TABLE_COMPANIES + " AS CR ON CDC." + KEY_TABLE_USER_COMPANY_DIVISION_COMPANY_ID + " = CR." + KEY_TABLE_COMPANIES_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_DIVISION_USER_ID + " in ( " + db.getTeam(sel_userId) + " ) group by(CR." + KEY_TABLE_COMPANIES_MASTER_ID + ")";
                Common.Log.i("Querrry " + selectQuery);
                sdbw = db.getWritableDatabase();

                Cursor cursor = sdbw.rawQuery(selectQuery, null);
                //System.out.println("cursor count "+cursor.getCount());
                if (cursor.moveToFirst()) {
                    do {
                        Companies companies = new Companies();

                        companies.setCompanyMasterId(cursor.getString(0));
                        companies.setCompanyName(cursor.getString(1));
                        companies.setCompanycode(cursor.getString(3));
                        companies.setCompanysapid(cursor.getString(2));

                        SelectedCities cities2 = new SelectedCities();
                        cities2.setCityId(cursor.getString(0));
                        cities2.setCityName(cursor.getString(1));
                        // System.out.println("city id is :" + cityId + "city name is :" + cityName);
                        organisations.add(cities2);
                        adaptercity.add(cursor.getString(1));

                    } while (cursor.moveToNext());
                }

                // do some stuff....
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
            // adapter.updateResults(arrayList);
            spin_company.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, R.id.customSpinnerItemTextView, adaptercity));
            spin_company.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    sel_company_id = organisations.get(position).getCityId();
                    //listView.setVisibility(View.INVISIBLE);
                    //
                    //Toast.makeText(getActivity(), categorytypeIdis, Toast.LENGTH_SHORT).show();
                    // Toast.makeText(getApplicationContext(), "apicalled" ,Toast.LENGTH_SHORT).show();
                    if (sel_company_id.equalsIgnoreCase("0")) {

                    } else {
                        companycode = organisations.get(position).getCityName();
                        companyname = organisations.get(position).getCityName();
                        String selectQuerys = "SELECT  " + KEY_TABLE_COMPANIES_COMPANY_CODE + " FROM " + TABLE_COMPANIES + " WHERE " + KEY_TABLE_COMPANIES_MASTER_ID + " = " + sel_company_id;
                        sdbw = db.getWritableDatabase();

                        Cursor cc = sdbw.rawQuery(selectQuerys, null);
                        //System.out.println("cursor count "+cursor.getCount());
                        if (cc != null && cc.moveToFirst()) {
                            Companies companies = new Companies();
                            companycode = cc.getString(0);
                            companies.setCompanycode(companycode);
                            //The 0 is the column index, we only have 1 column, so the index is 0

                        }


                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("company_id", sel_company_id);
                        editor.commit();
                        new AsyncDivisionsoffline().execute();
                    }

                }

                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
            if (Common.getBooleanDataFromSP(getApplicationContext(), Constants.Names.GLOBAL_AGRIGENETICS)) {
                spin_company.setSelection(adaptercity.indexOf("Global"));
                spin_company.setEnabled(false);
            }else {
                spin_company.setSelection(adaptercity.indexOf("Nuziveedu Seeds Ltd"));
            }
        }
    }

    public class AsyncCustomersoffline extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(NewSalesOrderChooseActivity.this);
            progressDialog.setMessage("Please wait ...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            arlist_crops = new ArrayList<SelectedCities>();
            adapter_crops = new ArrayList<String>();
            arlist_crops.clear();

            SelectedCities citiesz = new SelectedCities();
            citiesz.setCityId("0");
            citiesz.setCityName(Common.getStringResourceText(R.string.select_distributor));
            // System.out.println("city id is :" + cityId + "city name is :" + cityName);
//            arlist_crops.add(citiesz);
//            adapter_crops.add(Common.getStringResourceText(R.string.select_distributor));
            try {
                List<Crops> cdcList = new ArrayList<>();

                String selectQuery = "SELECT  " + "CR." + KEY_TABLE_CUSTOMER_MASTER_ID + "," + KEY_TABLE_CUSTOMER_NAME + "," + KEY_TABLE_CUSTOMER_CODE + ",CR." + KEY_TABLE_CUSTOMER_CITY + " FROM " + TABLE_USER_COMPANY_CUSTOMER + " AS CDC JOIN " + TABLE_CUSTOMERS + " AS CR ON CDC." + KEY_TABLE_USER_COMPANY_CUSTOMER_CUSTOMER_ID + " = CR." + KEY_TABLE_CUSTOMER_MASTER_ID + "  where " + "CDC." + KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID + " = " + sel_company_id + " and " + KEY_TABLE_USER_COMPANY_CUSTOMER_USER_ID + " in ( " + db.getTeam(sel_userId) + " ) and CR." + KEY_TABLE_USER_COMPANY_CUSTOMER_STATUS + " = 1" + " and CDC." + KEY_TABLE_USER_COMPANY_CUSTOMER_STATUS + " = 1 group by(CR." + KEY_TABLE_CUSTOMER_MASTER_ID + ")";
                // String selectQuery = "SELECT  " + "CR." + KEY_TABLE_CUSTOMER_MASTER_ID + ","+ KEY_TABLE_CUSTOMER_NAME+ ","+ KEY_TABLE_CUSTOMER_CODE + " FROM " + TABLE_USER_COMPANY_CUSTOMER+ " AS CDC JOIN " + TABLE_CUSTOMERS + " AS CR ON CDC."+KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID + " = CR."+ KEY_TABLE_CUSTOMER_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_DIVISION_USER_ID + " = " + userId + " group by(CR." + KEY_TABLE_COMPANIES_MASTER_ID + ")" ;
                sdbw = db.getWritableDatabase();

                Cursor cursor = sdbw.rawQuery(selectQuery, null);
                //System.out.println("cursor count "+cursor.getCount());
                if (cursor.moveToFirst()) {
                    do {
                        Customers customers = new Customers();

                        customers.setCusMasterID(cursor.getString(0));
                        customers.setCusName(cursor.getString(1));
                        customers.setCuscode(cursor.getString(2));


                        String city = cursor.getString(3) == null || cursor.getString(3).equalsIgnoreCase("null") ? "" : cursor.getString(3);
                        SelectedCities cities2 = new SelectedCities();
                        cities2.setCityId(cursor.getString(0));
                        cities2.setCityName(cursor.getString(1) + " -" + city);
                        // System.out.println("city id is :" + cityId + "city name is :" + cityName);
                        arlist_crops.add(cities2);
                        adapter_crops.add(cursor.getString(1) + " -" + city);
                        System.out.println("+++++++++++" + cursor.getString(0) + cursor.getString(1) + cursor.getString(2));


                    } while (cursor.moveToNext());
                }

                // do some stuff....
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
            autoCompleteAdapter(arlist_crops);
            /*spin_customer.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, R.id.customSpinnerItemTextView, adapter_crops));
            spin_customer.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    sel_customer_id = arlist_crops.get(position).getCityId();
                    //listView.setVisibility(View.INVISIBLE);
                    //
                    //Toast.makeText(getActivity(), categorytypeIdis, Toast.LENGTH_SHORT).show();
                    // Toast.makeText(getApplicationContext(), "apicalled" ,Toast.LENGTH_SHORT).show();
                    if (sel_customer_id.equalsIgnoreCase("0")) {

                    } else {
                        customername = arlist_crops.get(position).getCityName();
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("customer_id", sel_customer_id);
                        editor.putString("customer_name", arlist_crops.get(position).getCityName());
                        editor.commit();
                    }
                }


                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });*/
        }
    }

    public class AsyncDivisionsoffline extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(NewSalesOrderChooseActivity.this);
            progressDialog.setMessage("Please wait ...");
            progressDialog.setCancelable(false);
            progressDialog.show();
        }

        @Override
        protected String doInBackground(String... params) {

            arlist_schemes = new ArrayList<SelectedCities>();
            adapter_schemes = new ArrayList<String>();
            arlist_schemes.clear();

            SelectedCities citieszzy = new SelectedCities();
            citieszzy.setCityId("0");
            citieszzy.setCityName("Select Division");
            // System.out.println("city id is :" + cityId + "city name is :" + cityName);
            arlist_schemes.add(citieszzy);
            adapter_schemes.add("Select Division");

            try {

                String selectQuery = "SELECT  " + "CR." + KEY_TABLE_DIVISION_MASTER_ID + "," + KEY_TABLE_DIVISION_NAME + "," + KEY_TABLE_DIVISION_CODE + " FROM " + TABLE_USER_COMPANY_DIVISION + " AS CDC JOIN " + TABLE_DIVISION + " AS CR ON CDC." + KEY_TABLE_USER_COMPANY_DIVISION_DIVISION_ID + " = CR." + KEY_TABLE_DIVISION_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_DIVISION_USER_ID + " in ( " + db.getTeam(sel_userId) + " ) and " + KEY_TABLE_USER_COMPANY_DIVISION_COMPANY_ID + " = " + sel_company_id + " group by(CR." + KEY_TABLE_DIVISION_MASTER_ID + ")";
//                String selectQuery = "SELECT  " + "CR." + KEY_TABLE_DIVISION_MASTER_ID + "," + KEY_TABLE_DIVISION_NAME + "," + KEY_TABLE_DIVISION_CODE + " FROM " + TABLE_USER_COMPANY_DIVISION + " AS CDC JOIN " + TABLE_DIVISION + " AS CR ON CDC." + KEY_TABLE_USER_COMPANY_DIVISION_DIVISION_ID + " = CR." + KEY_TABLE_DIVISION_MASTER_ID + " JOIN " + TABLE_SEASON_LINE_ITEMS + " AS SE ON CDC." + KEY_TABLE_USER_COMPANY_DIVISION_DIVISION_ID + " = SE." + KEY_SEASON_LINE_ITEM_DIVISION_ID + "  where " + KEY_TABLE_USER_COMPANY_DIVISION_USER_ID + " in ( " + db.getTeam(sel_userId) + " ) and " + KEY_TABLE_USER_COMPANY_DIVISION_COMPANY_ID + " = " + sel_company_id + " group by(CR." + KEY_TABLE_DIVISION_MASTER_ID + ")";
                Log.e("DIVISIONS QUERY", selectQuery);
                sdbw = db.getWritableDatabase();

                Cursor cursor = sdbw.rawQuery(selectQuery, null);
                //System.out.println("cursor count "+cursor.getCount());
                if (cursor.moveToFirst()) {
                    do {

                        Divisions divisions = new Divisions();
                        divisions.setDivMasterID(cursor.getString(0));
                        divisions.setDivName(cursor.getString(1));

                        SelectedCities cities3 = new SelectedCities();
                        cities3.setCityId(cursor.getString(0));
                        cities3.setCityName(cursor.getString(1));

                        arlist_schemes.add(cities3);
                        adapter_schemes.add(cursor.getString(1));

                    } while (cursor.moveToNext());
                }

                // do some stuff....
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

            spin_division.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, R.id.customSpinnerItemTextView, adapter_schemes));
            spin_division.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
                @Override
                public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                    sel_division_id = arlist_schemes.get(position).getCityId();
                    if (sel_division_id.equalsIgnoreCase("0")) {

                    } else {
                        divisionname = arlist_schemes.get(position).getCityName();
                        SharedPreferences.Editor editor = sharedpreferences.edit();
                        editor.putString("division_id", sel_division_id);
                        editor.commit();
                        new AsyncCustomersoffline().execute();
                    }

                }


                @Override
                public void onNothingSelected(AdapterView<?> parent) {

                }
            });
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
                Common.hideSoftKeyboard(NewSalesOrderChooseActivity.this);
                //    Toast.makeText(getApplicationContext(),item.getCityName()+"\n"+item.getCityId(),Toast.LENGTH_SHORT).show();
                Log.d("onItemClick", item.getCityId());

                sel_customer_id = item.getCityId();
                tv_customer.setTextColor(getResources().getColor(R.color.colorPrimary));
                customername = item.getCityName() ==null ? "":item.getCityName().trim();
                SharedPreferences.Editor editor = sharedpreferences.edit();
                editor.putString("customer_id", sel_customer_id);
                editor.putString("customer_name", customername);
                editor.commit();

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

        auto_tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (auto_tv.getText().length() == 0) {
                    //   auto_tv.setAdapter(adapter);
                    auto_tv.showDropDown();
                }
            }
        });
    }

}