package com.nsl.beejtantra.FarmerCoupans;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationManager;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nsl.beejtantra.DatabaseHandler;
import com.nsl.beejtantra.R;
import com.nsl.beejtantra.TFA.ActivityIndent;
import com.nsl.beejtantra.commonutils.Common;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Activityfarmercoupon extends AppCompatActivity {
    FloatingActionButton fab;
    EditText et_geotag;
    ListView listView;
    DatabaseHandler db;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";

    ItemfavitemAdapter adapter;
    String User_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activityfarmercoupon);

        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        User_id           = sharedpreferences.getString("userId", "");

        listView = (ListView) findViewById(R.id.listView);

        db = new DatabaseHandler(Activityfarmercoupon.this);






        adapter= new ItemfavitemAdapter(getApplicationContext(),db.getcoupans2(User_id));
        // Assign adapter to ListView
        listView.setAdapter(adapter);



        fab = (FloatingActionButton) findViewById(R.id.flaot);


        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                if (Common.haveInternet(Activityfarmercoupon.this)) {
                    // GeoTaggingMapActivityforFarmerCoupans
                    Intent it=new Intent(Activityfarmercoupon.this, CoupanForm.class);
                    startActivity(it);


                }

                 else {


                    LocationManager lm = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
                    boolean gps_enabled = false;
                    boolean network_enabled = false;

                    try {
                        gps_enabled = lm.isProviderEnabled(LocationManager.GPS_PROVIDER);
                    } catch (Exception ex) {
                    }

                    try {
                        network_enabled = lm.isProviderEnabled(LocationManager.NETWORK_PROVIDER);
                    } catch (Exception ex) {
                    }

                    if (!gps_enabled && !network_enabled) {
                        Toast.makeText(getApplicationContext(),"Please Enable Location",Toast.LENGTH_LONG).show();
                        startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
                    }
                    else {
                        Intent it=new Intent(Activityfarmercoupon.this, CoupanForm.class);
                        startActivity(it);
                    }


                }


            }
        });
    }


    class ItemfavitemAdapter extends BaseAdapter {

        Context context;
        ArrayList<ModelforCoupansList> results = new ArrayList<ModelforCoupansList>();




        public ItemfavitemAdapter(Context applicationContext, List<ModelforCoupansList> getcoupans2) {
            this.context = applicationContext;
            this.results = (ArrayList<ModelforCoupansList>) getcoupans2;
        }

        @Override
        public int getCount() {
            return results.size();
        }

        @Override
        public Object getItem(int position) {
            return results;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ItemfavitemAdapter.ViewHolder holder = new ItemfavitemAdapter.ViewHolder();
            if (convertView == null) {



                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.row_demandgeneration, parent, false);
                holder.event_name = (TextView) convertView.findViewById(R.id.tv_event);
                //holder.tv_status       = (TextView) convertView.findViewById(R.id.status);
                holder.tv_crop = (TextView) convertView.findViewById(R.id.tv_crop);
                holder.tv_date = (TextView) convertView.findViewById(R.id.tv_date);
                holder.tv_address = (TextView) convertView.findViewById(R.id.tv_address);
                holder.tv_district= (TextView) convertView.findViewById(R.id.tv_district);
                holder.tv_farmers= (TextView) convertView.findViewById(R.id.tv_farmers);
                holder.tv_coupon= (TextView) convertView.findViewById(R.id.tv_coupon);
                convertView.setTag(holder);

            }
            else {
                holder = (ItemfavitemAdapter.ViewHolder) convertView.getTag();
            }





            holder.event_name.setText( results.get(position).getCoupan());
            holder.tv_date.setText(results.get(position).getDate());
            holder.tv_crop.setText    ("farmer name   : "+results.get(position).getName());
            holder.tv_district.setText("Farmer Mobile: "          +results.get(position).getMobile());
            holder.tv_address.setText ("Address           : "          +results.get(position).getVillage()+" , "+results.get(position).getThaluka());
            holder.tv_coupon.setText (results.get(position).getCoupontype());
            //cropName = crops.get(position - 1).getCityName();

            holder.tv_farmers.setVisibility(View.GONE);



            return convertView;
        }




        public class ViewHolder {
            public TextView event_name, tv_date, tv_crop, tv_address,tv_district,tv_farmers,tv_coupon;


        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        /*Intent it=new Intent(Activityfarmercoupon.this, CoupanForm.class);
        startActivity(it);*/
    }
}
