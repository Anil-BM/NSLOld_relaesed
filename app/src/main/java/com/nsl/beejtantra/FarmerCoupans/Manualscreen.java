package com.nsl.beejtantra.FarmerCoupans;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.design.widget.TextInputEditText;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.nsl.beejtantra.DatabaseHandler;
import com.nsl.beejtantra.R;

public class Manualscreen extends AppCompatActivity {
    String get_text_manual;
    DatabaseHandler db;
    TextInputEditText inpput_text_manual;
    Button btn_submit;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_manualscreen);
        inpput_text_manual = (TextInputEditText) findViewById(R.id.scan_manual_edt);
        btn_submit = (Button) findViewById(R.id.btn_submit);

        db = new DatabaseHandler(this);

        AlertDialog.Builder builder = new AlertDialog.Builder(Manualscreen.this);

        builder.setMessage("Unable to scan QR code.Please Enter  number manually");


        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                dialogInterface.dismiss();

            }
        });


        builder.setNegativeButton("Cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {


                Intent intent = new Intent(Manualscreen.this, CoupanForm.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);

                startActivity(intent);

                finish();
            }
        });


        builder.setCancelable(false);

        builder.show();


    }

    public void submit_manual(View view) {

        get_text_manual = inpput_text_manual.getText().toString();


        if (!get_text_manual.isEmpty()) {


            if (get_text_manual.length() == 16) {


                if (!get_text_manual.isEmpty())
                {
                    String farmer_coupon_id=db.getcoupanschk(get_text_manual).get(0);
                    if(!farmer_coupon_id.equals("0")&&!farmer_coupon_id.equals("Scanned")&&!farmer_coupon_id.equals("")&&!farmer_coupon_id.equals("Copan not exists in database"))
                    {
                        Intent intent = new Intent(Manualscreen.this,CoupanForm.class);
                        intent.putExtra("keyName", get_text_manual);
                        intent.putExtra("farmer_coupon_id", farmer_coupon_id);
                        intent.putExtra("farmer_coupon_type", db.getcoupanschk(get_text_manual).get(1));
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();
                        Toast.makeText(getApplicationContext(),"Successfully scanned please fill remaining fields",Toast.LENGTH_LONG).show();
                    }
                    else if(farmer_coupon_id.equals("Scanned")){
                        /*Intent intent = new Intent(Manualscreen.this,CoupanForm.class);
                        intent.putExtra("keyName", get_text_manual);
                        intent.putExtra("farmer_coupon_id", farmer_coupon_id);
                        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(intent);
                        finish();*/
                        Toast.makeText(getApplicationContext(),"coupon Already Scanned",Toast.LENGTH_LONG).show();
                    }
                    else if(farmer_coupon_id.equals("Copan not exists in database")){
                        Toast.makeText(getApplicationContext(),"Invalid coupon",Toast.LENGTH_LONG).show();
                    }
                    else if(farmer_coupon_id.equals("")){
                        Toast.makeText(getApplicationContext(),"Invalid coupon!",Toast.LENGTH_LONG).show();
                    }

                }


            } else {


                Toast.makeText(Manualscreen.this, "Please Enter Valid Serial Number", Toast.LENGTH_SHORT).show();

            }


        } else {


            Toast.makeText(Manualscreen.this, "Please Enter Serial Number", Toast.LENGTH_SHORT).show();


        }


    }

    @Override
    public void onBackPressed() {


        Intent intent = new Intent(Manualscreen.this, CoupanForm.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        finish();


    }


    public void finish_btn(View view) {


        Intent intent = new Intent(Manualscreen.this, CoupanForm.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(intent);

        finish();


    }
}