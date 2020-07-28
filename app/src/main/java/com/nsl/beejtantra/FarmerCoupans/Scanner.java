package com.nsl.beejtantra.FarmerCoupans;

import android.content.Intent;
import android.content.SharedPreferences;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.CoordinatorLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.zxing.integration.android.IntentIntegrator;
import com.google.zxing.integration.android.IntentResult;
import com.nsl.beejtantra.AnyOrientationCaptureActivity;
import com.nsl.beejtantra.DatabaseHandler;
import com.nsl.beejtantra.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

import retrofit2.Retrofit;

public class Scanner extends AppCompatActivity {
    String scanned_number = "";
    ProgressBar progressBar;
    int SECOND_ACTIVITY_REQUEST_CODE_MANUAL_SCANNER=12345;
    int iptcondition = 0;
    ImageView scanwarning, scanner_back;
    TextView scan_error, scanned_number_txt, variety_code_txt, Crop_txt, lot_no1_txt, lot_no2_txt, Qty1_txt, Qty2_txt, valid_up_to_txt1, valid_up_to_txt2, pointsearn, fabscan;

    CheckBox check;
    CardView lotcard, varietycode_card;
    int quantity_in_check;
    Button confirmbutton;
    Retrofit retrofit;
    CoordinatorLayout coordinatorLayout, parent_scanner;
    List<String> lotnumber = new ArrayList<String>();
    String varietycode;
    String percentage1, percentage2;
    AppBarLayout mAppBarLayout;
    List<Long> inserted_ids;
    List<String> valid_up_to = new ArrayList<String>();
    String user_details_id = "";
    List<String> quantity = new ArrayList<String>();
    String verify = "";
    List<String> reedempoints_list = new ArrayList<String>();
    SharedPreferences sharedPreferences;
    int distrubutor_percentage, retailer_percentage;
    List<String> percentage_list = new ArrayList<String>();
    String timeStamp = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(Calendar.getInstance().getTime());
    String reedem_points_total;
    String region_id = "", user_type = "";
    List<String> reedem_master_id = new ArrayList<String>();
    List<String> user_details_master_id = new ArrayList<String>();
    int reedem_points;
    String id;
    String syncstatus = "0";
    long seconds;
    int check_txt = 0;
    AppBarLayout appBarLayout;
    DatabaseHandler db;
    String farmer_coupon_id;
    LinearLayout ll;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_scanner);



        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(AnyOrientationCaptureActivity.class);
        integrator.setPrompt("Scan");
        integrator.setOrientationLocked(false);
        integrator.setBeepEnabled(false);
        integrator.setTimeout(10000);
        integrator.initiateScan();

        db = new DatabaseHandler(this);
        ll = (LinearLayout) findViewById(R.id.laoding);
        scanwarning = (ImageView) findViewById(R.id.img_warning);

        scan_error = (TextView) findViewById(R.id.scan_error);
        progressBar = (ProgressBar) findViewById(R.id.progressBar_in_scanner);

        scanwarning.setVisibility(View.GONE);
        scan_error.setVisibility(View.GONE);


       // fabscan = (TextView) findViewById(R.id.fab_scan);

    }








    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

       /* if (requestCode == SECOND_ACTIVITY_REQUEST_CODE_MANUAL_SCANNER && resultCode == RESULT_OK && data != null) {
            ll.setVisibility(View.GONE);
            progressBar.setVisibility(View.VISIBLE);

            String returnString = data.getStringExtra("keyName");
            farmer_coupon_id = data.getStringExtra("farmer_coupon_id");

            Intent intent = new Intent();
            intent.putExtra("keyName", returnString);
            intent.putExtra("farmer_coupon_id", farmer_coupon_id);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            setResult(RESULT_OK, intent);
        }*/



       // else {


            IntentResult result = IntentIntegrator.parseActivityResult(resultCode, data);


            if (result.getContents() == null && resultCode != 1) {

                execute_manually();
                Log.d("notdetails", "empty");


            } else {

                if (resultCode == 1) {

                    scanned_number = data.getStringExtra("result_manual");
                    execute(scanned_number);


                } else {
                    scanned_number = result.getContents();


                    execute(scanned_number);
                }
            }
        }
       // progressBar.setVisibility(View.GONE);
    //}

    private void execute_manually() {

        scanwarning.setVisibility(View.VISIBLE);
        scan_error.setVisibility(View.VISIBLE);
        scan_error.setText("Invalid coupon");

        Toast.makeText(getApplicationContext(),"Invalid coupon",Toast.LENGTH_LONG).show();

    }
    private void execute(String scanned_number)
    {
        if (!scanned_number.isEmpty())
        {
            String farmer_coupon_id=db.getcoupanschk(scanned_number).get(0);
          //  Toast.makeText(getApplicationContext(),"hhjghjg"+farmer_coupon_id,Toast.LENGTH_LONG).show();
            if(!farmer_coupon_id.equals("0")&&!farmer_coupon_id.equals("Scanned")&&!farmer_coupon_id.equals("")&&!farmer_coupon_id.equals("Copan not exists in database"))
            {
                scanwarning.setVisibility(View.GONE);
                scan_error.setVisibility(View.GONE);

                Intent intent = new Intent();
                intent.putExtra("keyName", scanned_number);
                intent.putExtra("farmer_coupon_id", farmer_coupon_id);
                intent.putExtra("farmer_coupon_type", db.getcoupanschk(scanned_number).get(1));
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                setResult(RESULT_OK, intent);
                finish();
                Toast.makeText(getApplicationContext(),"Successfully scanned please fill remaining fields",Toast.LENGTH_LONG).show();
            }
            else if(farmer_coupon_id.equals("Scanned"))
            {
                scanwarning.setVisibility(View.VISIBLE);
                scan_error.setVisibility(View.VISIBLE);
                scan_error.setText("coupon Already Scanned");
                Toast.makeText(getApplicationContext(),"coupon Already Scanned",Toast.LENGTH_LONG).show();
            }
            else if(farmer_coupon_id.equals("Copan not exists in database"))
            {
                scanwarning.setVisibility(View.VISIBLE);
                scan_error.setVisibility(View.VISIBLE);
                scan_error.setText("Invalid coupon");
                Toast.makeText(getApplicationContext(),"Invalid coupon",Toast.LENGTH_LONG).show();
            }
            else if(farmer_coupon_id.equals(""))
            {
                scanwarning.setVisibility(View.VISIBLE);
                scan_error.setVisibility(View.VISIBLE);
                scan_error.setText("Invalid coupon!");
                Toast.makeText(getApplicationContext(),"Invalid coupon!",Toast.LENGTH_LONG).show();
            }

        }
        else
        {
            scanwarning.setVisibility(View.VISIBLE);
            scan_error.setVisibility(View.VISIBLE);
            scan_error.setText("Invalid coupon");
        }
    }

    public void scan_again(View view) {
        IntentIntegrator integrator = new IntentIntegrator(this);
        integrator.setCaptureActivity(AnyOrientationCaptureActivity.class);
        integrator.setPrompt("Scan");
        integrator.setTimeout(10000);
        integrator.setOrientationLocked(false);
        integrator.setBeepEnabled(false);
        integrator.initiateScan();
    }
    public void scan_manually(View view) {
        scanwarning.setVisibility(View.VISIBLE);
        scan_error.setVisibility(View.VISIBLE);
        scan_error.setText("Invalid coupon");

        Toast.makeText(getApplicationContext(),"Invalid coupon",Toast.LENGTH_LONG).show();
    }
}

