package com.nsl.beejtantra.complaints;


import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.Animation;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;

import com.nsl.beejtantra.Companies;
import com.nsl.beejtantra.DatabaseHandler;
import com.nsl.beejtantra.Divisions;
import com.nsl.beejtantra.MainActivity;
import com.nsl.beejtantra.R;
import com.nsl.beejtantra.SelectedCities;
import com.nsl.beejtantra.commonutils.Common;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_COMPANIES_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_COMPANIES_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_DIVISION_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_DIVISION_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USER_COMPANY_CUSTOMER_USER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USER_COMPANY_DIVISION_COMPANY_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USER_COMPANY_DIVISION_DIVISION_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USER_COMPANY_DIVISION_USER_ID;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_COMPANIES;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_DIVISION;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_USER_COMPANY_CUSTOMER;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_USER_COMPANY_DIVISION;

/**
 * A simple {@link Fragment} subclass.
 */
public class Complaintsselectactivity extends AppCompatActivity {
    // JSON parser class
    @BindView(R.id.spinCompany)
    Spinner spin_company;
    @BindView(R.id.spinDivision)
    Spinner spin_division;
    @BindView(R.id.tvInvisibleError1)
    TextView tvError1;
    @BindView(R.id.tvInvisibleError2)
    TextView tvError2;
    @BindView(R.id.go_to_complaints)
    Button goToComplaints;
    private List<SelectedCities> arlist_companies;
    private List<String> adapter_companies;
    private SQLiteDatabase sql;
    private DatabaseHandler db;
    private String sel_comp_id = "0";
    private List<SelectedCities> arlist_divisions;
    private List<String> adapter_divisions;
    private String sel_division_id = "0";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_complaintsselectactivity);
        ButterKnife.bind(this);
        db = new DatabaseHandler(this);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        new AsyncCompaniesoffline().execute();

        goToComplaints.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (sel_comp_id.equalsIgnoreCase("0")) {
                    tvError1.requestFocus();
                    tvError1.setError("Please Select Company");
                    return;
                }
                if (sel_division_id.equalsIgnoreCase("0")) {
                    tvError2.requestFocus();
                    tvError2.setError("Please Select Division");
                    return;
                }
                Intent reg = new Intent(Complaintsselectactivity.this, ComplaintsprodallActivity.class);
                reg.putExtra("company_id", sel_comp_id);
                reg.putExtra("division_id", sel_division_id);
                startActivity(reg);
            }
        });

    }

    public class AsyncCompaniesoffline extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        @Override
        protected String doInBackground(String... params) {

            arlist_companies = new ArrayList<SelectedCities>();
            adapter_companies = new ArrayList<String>();


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


                String selectQuery = "SELECT  " + "CR." + KEY_TABLE_COMPANIES_MASTER_ID + "," + KEY_TABLE_COMPANIES_NAME + " FROM " + TABLE_USER_COMPANY_CUSTOMER + " AS CDC JOIN " + TABLE_COMPANIES + " AS CR ON CDC." + KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID + " = CR." + KEY_TABLE_COMPANIES_MASTER_ID + "  where CDC." + KEY_TABLE_USER_COMPANY_CUSTOMER_USER_ID + " IN (" + Common.getTeamFromSP(Complaintsselectactivity.this) + ") group by(CR." + KEY_TABLE_COMPANIES_MASTER_ID + ")";

                System.out.println(selectQuery);
                // String selectQuery = "SELECT  " + KEY_TABLE_COMPANIES_MASTER_ID + ","+ KEY_TABLE_COMPANIES_NAME + " FROM " + TABLE_USER_COMPANY_CUSTOMER + " AS C JOIN " + TABLE_COMPANIES + " AS UCC ON UCC."+KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID + " = C."+ KEY_TABLE_COMPANIES_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_CUSTOMER_COMPANY_ID + " = " + sel_comp_id + " and " + KEY_TABLE_USER_COMPANY_CUSTOMER_USER_ID + " = " + checkuid;


                // List<Company_division_crops> cdclist = db.getAllCompany_division_crops();
                //  List<User_Company_Customer> cdclist = db.getAllUser_Company_Customer();
                sql = db.getWritableDatabase();

                Cursor cursor = sql.rawQuery(selectQuery, null);
                //System.out.println("cursor count "+cursor.getCount());
                if (cursor.moveToFirst()) {
                    do {
                        companies = new SelectedCities();

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

                  /*for (int l=0; l<=cropids.size();l++){
                      sdbr =db.getReadableDatabase();

                      Cursor cursors = sdbr.query(TABLE_CROPS, new String[] { KEY_TABLE_CROPS_CROP_ID,
                                      KEY_TABLE_CROPS_CROP_MASTER_ID,KEY_TABLE_CROPS_CROP_NAME }, KEY_TABLE_CROPS_CROP_MASTER_ID + "=?",
                              new String[] { String.valueOf(cropids.get(l).get("crop_id")) }, null, null, null, null);
                      if (cursors != null)
                          cursors.moveToFirst();

                      Crops crops = new Crops(Integer.parseInt(cursors.getString(0)),cursors.getString(1), cursors.getString(2), cursors.getString(3), cursors.getString(4), cursors.getString(5), cursors.getString(6),cursors.getString(7));
                      // return contact
                      Log.v("Cropidis xyz",cursors.getString(1)+cursors.getString(2));
                  }*/


            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            // adapter.updateResults(arrayList);
            spin_company.setAdapter(new ArrayAdapter<String>(Complaintsselectactivity.this, R.layout.spinner_item, R.id.customSpinnerItemTextView, adapter_companies));
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
                String selectQuery = "SELECT  " + "CR." + KEY_TABLE_DIVISION_MASTER_ID + "," + KEY_TABLE_DIVISION_NAME + " FROM " + TABLE_USER_COMPANY_DIVISION + " AS CDC JOIN " + TABLE_DIVISION + " AS CR ON CDC." + KEY_TABLE_USER_COMPANY_DIVISION_DIVISION_ID + " = CR." + KEY_TABLE_DIVISION_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_DIVISION_COMPANY_ID + " = " + sel_comp_id + " and " + KEY_TABLE_USER_COMPANY_DIVISION_USER_ID + " = " + Common.getUserIdFromSP(Complaintsselectactivity.this) + " group by(CR." + KEY_TABLE_DIVISION_MASTER_ID + ")";
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
                        divisions = new SelectedCities();

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

                  /*for (int l=0; l<=cropids.size();l++){
                      sdbr =db.getReadableDatabase();

                      Cursor cursors = sdbr.query(TABLE_CROPS, new String[] { KEY_TABLE_CROPS_CROP_ID,
                                      KEY_TABLE_CROPS_CROP_MASTER_ID,KEY_TABLE_CROPS_CROP_NAME }, KEY_TABLE_CROPS_CROP_MASTER_ID + "=?",
                              new String[] { String.valueOf(cropids.get(l).get("crop_id")) }, null, null, null, null);
                      if (cursors != null)
                          cursors.moveToFirst();

                      Crops crops = new Crops(Integer.parseInt(cursors.getString(0)),cursors.getString(1), cursors.getString(2), cursors.getString(3), cursors.getString(4), cursors.getString(5), cursors.getString(6),cursors.getString(7));
                      // return contact
                      Log.v("Cropidis xyz",cursors.getString(1)+cursors.getString(2));
                  }*/


            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            // adapter.updateResults(arrayList);
            spin_division.setAdapter(new ArrayAdapter<String>(Complaintsselectactivity.this, R.layout.spinner_item, R.id.customSpinnerItemTextView, adapter_divisions));
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


}






