package com.nsl.beejtantra.FarmerCoupans;

import android.Manifest;

import android.content.ComponentName;
import android.content.ContentValues;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.IBinder;
import android.provider.Settings;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nsl.beejtantra.Constants;
import com.nsl.beejtantra.Coupans_add;
import com.nsl.beejtantra.DatabaseHandler;
import com.nsl.beejtantra.GPSTracker;
import com.nsl.beejtantra.LoginActivity;
import com.nsl.beejtantra.MyApplication;
import com.nsl.beejtantra.PlanerDetailActivity;
import com.nsl.beejtantra.R;
import com.nsl.beejtantra.RetroResponses.resmain_farmer_coupons_detail;
import com.nsl.beejtantra.TFA.ActivityIndent;
import com.nsl.beejtantra.TFA.ActivityIndentForm;
import com.nsl.beejtantra.TFA.Demandgeneration_add;
import com.nsl.beejtantra.Utility;
import com.nsl.beejtantra.commonutils.Common;
import com.nsl.beejtantra.geotagging.GeoTaggingMapActivity;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;

public class CoupanForm extends AppCompatActivity implements LocationListener{
    EditText et_geotag, et_name, et_pnno, et_village, et_adhar, et_thaluka;
    CardView scan;
    TextView tv_scan, tv_code, tv_coupan_type;
    int SECOND_ACTIVITY_REQUEST_CODE = 12;
    int MAP_ACTIVITY_REQUEST_CODE = 21;
    DatabaseHandler db;
    private String status;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    String User_id;
    Button btn_appt;
    boolean isGPSEnabled = false;
    String farmer_coupon_id = "", farmer_coupon_type = "";
    String jsonData;
    LocationManager locationManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_coupan_form);


        db = new DatabaseHandler(CoupanForm.this);
        scan = (CardView) findViewById(R.id.scan1);
        tv_scan = (TextView) findViewById(R.id.scan_to_earn1);
        tv_coupan_type = (TextView) findViewById(R.id.coupan_type);
        tv_coupan_type.setText("");
        tv_code = (TextView) findViewById(R.id.code);
        et_geotag = (EditText) findViewById(R.id.et_geotag);
        et_name = (EditText) findViewById(R.id.et_ownername);
        et_pnno = (EditText) findViewById(R.id.et_ownerpnno);
        et_village = (EditText) findViewById(R.id.et_ownervillage);
        et_thaluka = (EditText) findViewById(R.id.et_ownerthaluka);
        et_adhar = (EditText) findViewById(R.id.et_owneradhar);
        btn_appt = (Button) findViewById(R.id.btn_appt);


        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        User_id = sharedpreferences.getString("userId", "");

        //  getcoupans("775");
        if (getIntent().getStringExtra("keyName") != null) {
            String returnString = getIntent().getStringExtra("keyName");
            farmer_coupon_id = getIntent().getStringExtra("farmer_coupon_id");
            farmer_coupon_type = getIntent().getStringExtra("farmer_coupon_type");

            tv_code.setVisibility(View.VISIBLE);
            tv_scan.setVisibility(View.GONE);
            tv_code.setText(returnString);
            tv_coupan_type.setText(farmer_coupon_type);
        }

        scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             /*   Intent intent = new Intent(CoupanForm.this, Scanner.class);
                startActivity(intent);*/

                Intent intent = new Intent(CoupanForm.this, Scanner.class);
                startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE);
            }
        });


        btn_appt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (tv_code.getText().toString().equals("Coupan") || tv_code.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please scan coupon", Toast.LENGTH_LONG).show();
                } else if (et_name.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter name", Toast.LENGTH_LONG).show();
                } else if (et_pnno.getText().toString().equals("") || et_pnno.getText().toString().length() < 10) {
                    Toast.makeText(getApplicationContext(), "Please enter mobile number", Toast.LENGTH_LONG).show();
                } else if (et_thaluka.getText().toString().equals("")) {
                    Toast.makeText(getApplicationContext(), "Please enter thaluka", Toast.LENGTH_LONG).show();
                }
             else if (et_village.getText().toString().equals("")) {
                Toast.makeText(getApplicationContext(), "Please enter village", Toast.LENGTH_LONG).show();
            }else if (et_adhar.getText().toString().equals("") || et_adhar.getText().toString().length() < 12) {
                    Toast.makeText(getApplicationContext(), "Please enter adhar number", Toast.LENGTH_LONG).show();
                } else if (et_geotag.getText().toString().equals("") || et_geotag.getText().toString().length() < 12) {
                    Toast.makeText(getApplicationContext(), "Please tag your location", Toast.LENGTH_LONG).show();
                } else if (farmer_coupon_id.equals("")) {
                    Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_LONG).show();
                } else {


                    // Toast.makeText(getApplicationContext(),farmer_coupon_id,Toast.LENGTH_LONG).show();
                    if (Utility.isNetworkAvailable(CoupanForm.this, Constants.isShowNetworkToast)) {

                        String ss = db.update_COUPANS(tv_code.getText().toString(), User_id, et_name.getText().toString(), et_pnno.getText().toString(), et_adhar.getText().toString(), "", et_geotag.getText().toString(), et_village.getText().toString(), et_thaluka.getText().toString());
                        //  Toast.makeText(getApplicationContext(),User_id,Toast.LENGTH_SHORT).show();
                        if (ss.equals("Sucess")) {
                            new Async_postcoupans().execute();

                        } else {
                            Toast.makeText(getApplicationContext(), "Something went wrong", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        String ss = db.update_COUPANS(tv_code.getText().toString(), User_id, et_name.getText().toString(), et_pnno.getText().toString(), et_adhar.getText().toString(), "", et_geotag.getText().toString(), et_village.getText().toString(), et_thaluka.getText().toString());
                        Toast.makeText(getApplicationContext(), ss, Toast.LENGTH_SHORT).show();
                        if (ss.equals("Sucess")) {
                            Intent it = new Intent(CoupanForm.this, Activityfarmercoupon.class);
                            it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                            startActivity(it);

                        } else {
                            Toast.makeText(getApplicationContext(), "Something Went Wrong", Toast.LENGTH_SHORT).show();
                        }
                    }

                }


            }
        });
        et_geotag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                Intent intent = new Intent(CoupanForm.this, GeoTaggingMapActivityforFarmerCoupans.class);
                startActivityForResult(intent, MAP_ACTIVITY_REQUEST_CODE);




            }
        });
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == SECOND_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            String returnString = data.getStringExtra("keyName");
             farmer_coupon_id = data.getStringExtra("farmer_coupon_id");
            farmer_coupon_type=data.getStringExtra("farmer_coupon_type");
            tv_coupan_type.setText(farmer_coupon_type);
            tv_code.setVisibility(View.VISIBLE);
            tv_scan.setVisibility(View.GONE);
            tv_code.setText(returnString);
        }

        if (requestCode == MAP_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {
            String returnlat = data.getStringExtra("lat");
            String returnlong = data.getStringExtra("long");
            et_geotag.setText(returnlat+" , "+returnlong);
        }
    }

    @Override
    public void onProviderDisabled(String provider) {
        Log.d("Latitude","disable");
    }

    @Override
    public void onProviderEnabled(String provider) {
        Log.d("Latitude","enable");
    }
    @Override
    public void onLocationChanged(Location location) {
        et_geotag.setText( location.getLatitude() + " , " + location.getLongitude());
    }
    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {
        Log.d("Latitude","status");
    }


    private class Async_postcoupans extends AsyncTask<Void, Void, String> {

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


                RequestBody body = RequestBody.create(mediaType, "user_id=" + User_id+ "&farmer_name="
                        +et_name.getText().toString() + "&farmer_mobile=" +et_pnno.getText().toString()
                        + "&farmer_adhar_no=" +et_adhar.getText().toString()+ "&location=" + et_geotag.getText().toString()
                        + "&address=" +"" + "&farmer_coupon_id=" + farmer_coupon_id+ "&village=" +et_village.getText().toString()+ "&thaluka=" + et_thaluka.getText().toString());
                Request request = new Request.Builder()
                        .url(Constants.BASE_URL+Constants.POST_FARMERCOUPAN_DATA)
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
                if(jsonobject.getString("status").equals("success"))
                {
                    String ss=db.update_Coupan_Server_Status(User_id,farmer_coupon_id);
                    if(ss.equals("Sucess")) {
                        Intent it = new Intent(CoupanForm.this, Activityfarmercoupon.class);
                        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(it);
                    }
                   /* else {
                        Toast.makeText(getApplicationContext(),"Details Are Updates To Server But Not Stored In Your Phone as sync status 1",Toast.LENGTH_SHORT).show();
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


    private GPSTracker gpsService;
    private boolean mBound;
    private ServiceConnection mConnection = new ServiceConnection() {

        @Override
        public void onServiceConnected(ComponentName className,
                                       IBinder service) {
            // We've bound to LocalService, cast the IBinder and get LocalService instance
            GPSTracker.GpsBinder binder = (GPSTracker.GpsBinder) service;
            gpsService = binder.getService();

            if (gpsService.canGetLocation) {
                double latitude = gpsService.getLatitude();
                double longitude = gpsService.getLongitude();
                String checkinlatlong = String.valueOf(latitude) + "," + String.valueOf(longitude);
                Log.d("checkinlatlong11  ", checkinlatlong);
                // gpsService.stopUsingGPS();;

                et_geotag.setText(checkinlatlong);
            }

            if (!gpsService.canGetLocation) {
                // gpsService.getLocation();
                if (!gpsService.canGetLocation)
                    gpsService.showSettingsAlert(CoupanForm.this);
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };


    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(CoupanForm.this, GPSTracker.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }

    /*@Override
    protected void onResume() {
        super.onResume();
        Log.d("kdf","dkml");
       // unbindService(mConnection);
        Intent intent = new Intent(CoupanForm.this, GPSTracker.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }
*/
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
    }
}

