package com.nsl.beejtantra;

import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Handler;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.Toolbar;

import com.nsl.beejtantra.adapters.CustomListAdapter;
import com.nsl.beejtantra.commonutils.Common;
import com.nsl.beejtantra.geotagging.GeoTaggingMapActivity;
import com.nsl.beejtantra.pojo.FarmerPojo;
import com.nsl.beejtantra.retailers.Retailer;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CUSTOMER_CITY;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CUSTOMER_CODE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CUSTOMER_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CUSTOMER_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CUSTOMER_REGION_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USER_COMPANY_CUSTOMER_CUSTOMER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USER_COMPANY_CUSTOMER_STATUS;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USER_COMPANY_CUSTOMER_USER_ID;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_CUSTOMERS;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_USER_COMPANY_CUSTOMER;

public class DistRetailerActivity extends AppCompatActivity {

    @BindView(R.id.spin_type)
    Spinner spinType;
    @BindView(R.id.auto_tv)
    AutoCompleteTextView autoTv;
    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar toolbar;
    List<String> types = new ArrayList<>();
    int type = 0;
    DatabaseHandler db;
    private boolean onItemClick = false;
    private List<SelectedCities> objects = new ArrayList<>();
    MyRunnable myRunnable;
    private Handler handler;
    private String selectedId = "0";
    private String selectedName;
    private int selectedIdPosition;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dist_retailer);
        ButterKnife.bind(this);
        db = new DatabaseHandler(this);
//        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                finish();
            }
        });
        types.add("Select");
        types.add("Distributor");
        types.add("Retailer");
        ArrayAdapter adapter = new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, R.id.customSpinnerItemTextView, types);
        spinType.setAdapter(adapter);

        spinType.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try {
                    autoTv.setText("");
                    autoTv.dismissDropDown();
                    type = i;
                    myRunnable = new MyRunnable();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        autoTv.addTextChangedListener(new TextWatcher() {
            public CharSequence beforeTextChangedS;

            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                beforeTextChangedS = s;

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                Log.d("afterTextChanged", "afterTextChanged" + s.length());
                if (difference(beforeTextChangedS, s)) {
                    autoTv.dismissDropDown();
                    return;
                }
                if (s.length() == 0) {
                    // auto_tv.setAdapter(adapter);
                    autoTv.showDropDown();
                } else {
                    if (handler != null) {
                        handler.removeCallbacks(myRunnable);
                    }
                    handler = new Handler();
                    myRunnable.setData(s.toString());
                    handler.postDelayed(myRunnable, 300);
                }
            }
        });

       /* autoTv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (autoTv.getText().length() == 0) {

                    autoTv.showDropDown();
                }
            }
        });*/
    }

    public void autoCompleteAdapter(List<SelectedCities> list) {
        final CustomListAdapter adapter = new CustomListAdapter(this,
                R.layout.autocomplete_custom_layout, list);

        autoTv.setAdapter(adapter);
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                autoTv.showDropDown();
            }
        }, 100);

        adapter.setOnItemClickListener(new CustomListAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position, SelectedCities item) {
                autoTv.setText(item.getCityName().toUpperCase());
                autoTv.setSelection(item.getCityName().length());
                autoTv.dismissDropDown();
                selectedId = item.getCityId();
                selectedIdPosition = position;
                selectedName = item.getCityName();
                Common.hideSoftKeyboard(DistRetailerActivity.this);
                //   Toast.makeText(getApplicationContext(), item.getCityName() + "\n" + item.getCityId(), Toast.LENGTH_SHORT).show();
                Log.d("onItemClick", item.getCityId());
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                autoTv.dismissDropDown();
                            }
                        });

                    }
                }, 100);


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
                if (type == 2) {
                    List<Retailer> lista = db.getAllRetailers(key);
                    Log.d("afterTextChanged", "list: " + lista.size());
                    objects.clear();
                    for (int i = 0; i < lista.size(); i++) {
                        SelectedCities object = new SelectedCities();
                        object.setCityId(String.valueOf(lista.get(i).get_ret_masterid()));
                        object.setCityName(lista.get(i).get_ret_name());
                        objects.add(object);
                    }
                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!onItemClick)
                                autoCompleteAdapter(objects);
                            else onItemClick = false;
                        }
                    });
                } else if (type == 1) {

                    runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (!onItemClick)
                                new AsyncUsers().execute();
                            else onItemClick = false;
                        }
                    });
                }
            }
        }
    }

    public class AsyncUsers extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected String doInBackground(String... params) {

            objects.clear();
            SelectedCities citiesz = new SelectedCities();
            citiesz.setCityId("0");
            citiesz.setCityName(Common.getStringResourceText(R.string.select_distributor));
            // System.out.println("city id is :" + cityId + "city name is :" + cityName);
            // arlist_users.add(citiesz);
            // adapter_users.add(Common.getStringResourceText(R.string.select_distributor));


            try {
                String selectQuery = "SELECT  " + "CR." + KEY_TABLE_CUSTOMER_MASTER_ID + "," + KEY_TABLE_CUSTOMER_NAME + "," + KEY_TABLE_CUSTOMER_REGION_ID + "," + KEY_TABLE_CUSTOMER_CODE + ",CR." + KEY_TABLE_CUSTOMER_CITY + " FROM " + TABLE_USER_COMPANY_CUSTOMER + " AS CDC JOIN " + TABLE_CUSTOMERS + " AS CR ON CDC." + KEY_TABLE_USER_COMPANY_CUSTOMER_CUSTOMER_ID + " = CR." + KEY_TABLE_CUSTOMER_MASTER_ID + "  where " + KEY_TABLE_USER_COMPANY_CUSTOMER_USER_ID + " in (" + Common.getTeamFromSP(DistRetailerActivity.this) + " ) and CR." + KEY_TABLE_USER_COMPANY_CUSTOMER_STATUS + " = 1" + " and CDC." + KEY_TABLE_USER_COMPANY_CUSTOMER_STATUS + " = 1 group by(CR." + KEY_TABLE_CUSTOMER_MASTER_ID + ")";


                //List<Company_division_crops> cdclist = db.getAllCompany_division_crops();

                SQLiteDatabase sdbw = db.getWritableDatabase();

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
                        objects.add(cities2);


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
            autoCompleteAdapter(objects);

        }
    }

    public boolean isCustomerLatLonExist(int id) {
        String latLng = db.getCustomerLatLngById(id);
        if (latLng == null || latLng.length() < 6) {
            return false;
        } else {
            return true;
        }
    }

    public boolean isRetailerLatLonExist(int id) {
        String latLng = db.getRetailerLatLngById(id);
        if (latLng == null || latLng.length() < 6) {
            return false;
        } else {
            return true;
        }
    }

    @OnClick(R.id.btn_submit)
    void submit() {
        if (spinType.getSelectedItemPosition() == 0) {
            Toast.makeText(this, "Please select distributor/retailer", Toast.LENGTH_SHORT).show();
            return;
        }

        if (selectedId != null && !selectedId.equalsIgnoreCase("0") && spinType.getSelectedItemPosition() == 1) {
            if (isCustomerLatLonExist(Integer.parseInt(selectedId))) {
                showMessageDialog();
                return;
            }
        }
        if (selectedId != null && !selectedId.equalsIgnoreCase("0") && spinType.getSelectedItemPosition() == 2) {
            if (isRetailerLatLonExist(Integer.parseInt(selectedId))) {
                showMessageDialog();
                return;
            }
        }
        if (Common.haveInternet(this)) {
            Intent intent = new Intent(DistRetailerActivity.this, GeoTaggingMapActivity.class);
            intent.putExtra("id", selectedId);
            intent.putExtra("name", selectedName);
            intent.putExtra("type", spinType.getSelectedItemPosition());
            startActivity(intent);
        } else {
            Toast.makeText(this, Common.INTERNET_UNABLEABLE, Toast.LENGTH_SHORT).show();
        }

    }

    private void showMessageDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        builder.setTitle("Alert !");
        builder.setMessage("Geotagging already done for " + spinType.getSelectedItem().toString() + "- " + selectedName);

              /*  builder.setPositiveButton("YES", new DialogInterface.OnClickListener() {

                    public void onClick(final DialogInterface dialog, int which) {

                        Intent intent = new Intent(DistRetailerActivity.this, GeoTaggingMapActivity.class);
                        intent.putExtra("id", selectedId);
                        intent.putExtra("name", selectedName);
                        intent.putExtra("type", spinType.getSelectedItemPosition());
                        startActivity(intent);

                    }
                });
*/
        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {

            @Override
            public void onClick(DialogInterface dialog, int which) {

                // Do nothing
                dialog.dismiss();
            }
        });

        AlertDialog alert = builder.create();
        alert.show();
    }

    public boolean difference(CharSequence ch, Editable editable) {
        try {
            if (ch != null && editable != null) {
                if (editable.length() - ch.length() > 1) {
                    return true;
                } else return false;
            } else
                return false;
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }


    }
}
