package com.nsl.beejtantra;


import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nsl.beejtantra.commonutils.Common;
import com.nsl.beejtantra.feedback.MainFeedbackActivity;
import com.nsl.beejtantra.scheduler.DownloadService;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;

import static com.nsl.beejtantra.Constants.BASE_URL_SHECHES_IMAGE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_SCHEMES_FILE_LOCATION;
import static com.nsl.beejtantra.DatabaseHandler.KEY_SCHEMES_MASTER_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_SCHEMES_SAP_CODE;
import static com.nsl.beejtantra.DatabaseHandler.KEY_SCHEMES_STATUS;
import static com.nsl.beejtantra.DatabaseHandler.KEY_SCHEMES_TITLE;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_SCHEMES;


public class SchemesActivity extends AppCompatActivity {

    private JSONObject JSONObj;
    private JSONArray JSONArr = null;
    ProgressDialog progressDialog;
    DatabaseHandler db;
    SQLiteDatabase sdbw, sdbr;
    int i = 0, sum_osa = 0;
    private ListView listView;
    private ItemfavitemAdapter adapter;
    String customer_id, comp_id;
    String jsonData, userId;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    String color;
    String aging1 = null, aging2 = null;
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    TextView tv_name, tv_code, tv_total_osa_amt;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schemes);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        userId = sharedpreferences.getString("userId", "");
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent newfedback = new Intent(getApplicationContext(), MainFeedbackActivity.class);
                startActivity(newfedback);

            }
        });
        db = new DatabaseHandler(this);

        listView = (ListView) findViewById(R.id.listView);
        final ArrayList<HashMap<String, String>> feedList = new ArrayList<HashMap<String, String>>();

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> adapterView, View view, int i, long l) {
                /*TextView divtext=(TextView)findViewById(R.id.tv_div);
                String div_value;
                div_value= divtext.getText().toString();*/
                Log.d("clicked", "clicked..1.." + view.getId());
                if (view.getId() == R.id.ll_click) {
                    Intent schemedetails = new Intent(getApplicationContext(), SchemesDetailsActivity.class);
                    schemedetails.putExtra("name", favouriteItem.get(i).get("name"));
                    schemedetails.putExtra("scheme_id", favouriteItem.get(i).get("scheme_id"));
                    schemedetails.putExtra("status", favouriteItem.get(i).get("status"));
                    startActivity(schemedetails);
                } else if (view.getId() == R.id.image_download) {
                    Log.d("clicked", "clicked..");

                }

            }
        });
        progressDialog = new ProgressDialog(SchemesActivity.this);
        progressDialog.setMessage("Please wait");
        progressDialog.show();

        adapter = new ItemfavitemAdapter(SchemesActivity.this, favouriteItem);
        listView.setAdapter(adapter);
        if (Common.haveInternet(getApplicationContext())) {
            new Async_getallschemes().execute();
        } else {
            new Async_getalloffline().execute();

        }


    }

    private class Async_getalloffline extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

        }

        protected String doInBackground(Void... params) {
            favouriteItem.clear();
            try {


                String selectQuery = "SELECT  " + KEY_SCHEMES_MASTER_ID + "," + KEY_SCHEMES_TITLE + "," + KEY_SCHEMES_SAP_CODE + "," + KEY_SCHEMES_STATUS + "," + KEY_SCHEMES_FILE_LOCATION + " FROM " + TABLE_SCHEMES;
                sdbw = db.getWritableDatabase();
                Log.e("query", selectQuery);
                Cursor cursor = sdbw.rawQuery(selectQuery, null);


                if (cursor.moveToFirst()) {
                    do {
                        HashMap<String, String> map = new HashMap<String, String>();
                        map.put("sr", "");
                        map.put("name", cursor.getString(1));
                        map.put("sapcode", "");
                        map.put("value", "");
                        map.put("unit", "");
                        map.put("scheme_id", cursor.getString(0));
                        map.put("status", cursor.getString(3));
                        map.put("file_name", cursor.getString(4));

                        favouriteItem.add(map);


                    } while (cursor.moveToNext());
                }

                //  }


            } catch (Exception e) {
                e.printStackTrace();
            }

            return jsonData;

        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            if (progressDialog.isShowing()) {
                progressDialog.dismiss();
            }
            adapter.updateResults(favouriteItem);
            // tv_total_osa_amt.setText(String.valueOf(sum_osa));
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

    class ItemfavitemAdapter extends BaseAdapter {

        Context context;
        ArrayList<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();


        public ItemfavitemAdapter(SchemesActivity pd, ArrayList<HashMap<String, String>> results) {
            this.context = pd;
            this.results = results;

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
            ViewHolder holder = new ViewHolder();
            if (convertView == null) {
                sharedpreferences = context.getSharedPreferences(mypreference, Context.MODE_PRIVATE);

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.row_schemes, parent, false);
                holder.tv_name = (TextView) convertView.findViewById(R.id.tv_name);
                holder.ll_click = (LinearLayout) convertView.findViewById(R.id.ll_click);
                holder.image_download = (ImageView) convertView.findViewById(R.id.image_download);
                //  holder.tv_aging1             = (TextView)convertView.findViewById(R.id.tv_aging1);

                convertView.setTag(holder);
            } else {
                holder = (ViewHolder) convertView.getTag();
            }
            //Glide.with(context).load(results.get(position).get("image")).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.pic);
            holder.tv_name.setText(results.get(position).get("name"));

            holder.ll_click.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent schemedetails = new Intent(getApplicationContext(), SchemesDetailsActivity.class);
                    schemedetails.putExtra("name", results.get(position).get("name"));
                    schemedetails.putExtra("scheme_id", results.get(position).get("scheme_id"));
                    schemedetails.putExtra("status", results.get(position).get("status"));
                    startActivity(schemedetails);
                }
            });
            holder.image_download.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.d("clicked", "clicked..");
                    if (Common.haveInternet(SchemesActivity.this)) {
                        if (results.get(position).get("file_name") != null && !results.get(position).get("file_name").equalsIgnoreCase(" ")) {
                            try {
                                mProgressDialog = new ProgressDialog(SchemesActivity.this);
                                mProgressDialog.setProgressStyle(ProgressDialog.STYLE_HORIZONTAL);
                                mProgressDialog.setCancelable(false);
                                mProgressDialog.show();
                                Intent intent = new Intent(getApplicationContext(), DownloadService.class);
                                intent.putExtra("url", BASE_URL_SHECHES_IMAGE + results.get(position).get("file_name"));
                                intent.putExtra("file_name", results.get(position).get("file_name"));
                                intent.putExtra("receiver", new DownloadReceiver(new Handler()));
                                startService(intent);
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                        } else {
                            Toast.makeText(getApplicationContext(), "File not found.", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Toast.makeText(getApplicationContext(), "Check Internet Connection..", Toast.LENGTH_SHORT).show();
                    }
                }
            });


            return convertView;
        }


        public class ViewHolder {
            public TextView tv_name, tv_place, tv_feedback;
            public ImageView img, image_download;
            LinearLayout ll_click;

        }

        public void updateResults(ArrayList<HashMap<String, String>> results) {

            this.results = results;
            notifyDataSetChanged();
        }
    }

    private class DownloadReceiver extends ResultReceiver {
        public DownloadReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, Bundle resultData) {
            super.onReceiveResult(resultCode, resultData);
            if (resultCode == DownloadService.UPDATE_PROGRESS) {
                int progress = resultData.getInt("progress");
                mProgressDialog.setProgress(progress);
                if (progress == 100) {
                    mProgressDialog.dismiss();

                    File file = new File(Environment.getExternalStorageDirectory() + "/NSL/", resultData.getString("file_name"));
                    Uri path = Uri.fromFile(file);
                    Intent pdfOpenintent = new Intent(Intent.ACTION_VIEW);
                    pdfOpenintent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    pdfOpenintent.setDataAndType(path, "application/pdf");
                    try {
                        startActivity(pdfOpenintent);
                    } catch (ActivityNotFoundException e) {

                    }
                }
            }
        }
    }

    private class Async_getallschemes extends AsyncTask<Void, Void, String> {

        @Override
        protected void onPreExecute() {
            super.onPreExecute();

            // bar.setVisibility(View.VISIBLE);
        }

        protected String doInBackground(Void... params) {

            //odb.delete(TABLE_DIVISION,null,null);

            try {
                OkHttpClient client = new OkHttpClient();


                Response responses = null;


                MediaType mediaType = MediaType.parse("application/x-www-form-urlencoded");
                Common.Log.i("SCHEME URL " + Constants.URL_NSL_MAIN + Constants.URL_MASTER_SCHEMES + userId);
                Request request = new Request.Builder()
                        .url(Constants.URL_NSL_MAIN + Constants.URL_MASTER_SCHEMES + Common.getTeamFromSP(SchemesActivity.this))
                        .get()
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();


                try {
                    responses = client.newCall(request).execute();
                    jsonData = responses.body().string();
                    System.out.println("!!!!!!!1" + jsonData);
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
            /// bar.setVisibility(View.GONE);

            if (jsonData != null) {
                try {

                    JSONObject jsonObject = new JSONObject(jsonData);
                    if (jsonObject != null && jsonObject.has("error")) {
                        //db.deleteDataByTableName(new String []{"schemes","scheme_products"});
                    }

                    JSONArray companyarray = jsonObject.getJSONArray("scheme_information");
                    if (companyarray != null && companyarray.length() > 0) {
                        //  db.deleteDataByTableName(new String []{"schemes","scheme_products"});
                    }

                    for (int n = 0; n < companyarray.length(); n++) {

                        JSONObject objinfo = companyarray.getJSONObject(n);
                        JSONObject schemeDataObj = objinfo.getJSONObject("scheme_data");

                        String scheme_id = schemeDataObj.getString("scheme_id");
                        String scheme_title = schemeDataObj.getString("scheme_title");
                        String product_sap_code = schemeDataObj.getString("scheme_sap_code");
                        String file_location = schemeDataObj.getString("file_location");
                        String status = schemeDataObj.getString("status");

                        db.addSchemes(new Schemes(scheme_id, scheme_title, product_sap_code, file_location, status));

                        JSONArray schemeProductDetailsArray = objinfo.getJSONArray("scheme_product_details");

                        for (int j = 0; j < schemeProductDetailsArray.length(); j++) {
                            JSONObject schemeProductDetailsObjinfo = schemeProductDetailsArray.getJSONObject(j);

                            String scheme_id1 = schemeProductDetailsObjinfo.getString("scheme_id");
                            String product_id = schemeProductDetailsObjinfo.getString("product_id");
                            String scheme_price = schemeProductDetailsObjinfo.getString("price_per_packet");
                            String region_id = schemeProductDetailsObjinfo.getString("region_id");
                            String company_id = schemeProductDetailsObjinfo.getString("company_id");
                            String crop_id = schemeProductDetailsObjinfo.getString("crop_id");
                            String slab_id = schemeProductDetailsObjinfo.getString("slab_id");
                            String booking_incentive = schemeProductDetailsObjinfo.getString("booking_incentive");
                            String valid_from = schemeProductDetailsObjinfo.getString("valid_from");
                            String valid_to = schemeProductDetailsObjinfo.getString("valid_to");
                            String booking_year = schemeProductDetailsObjinfo.getString("booking_year");
                            String season_code = schemeProductDetailsObjinfo.getString("season_code");
                            String extenstion_date = schemeProductDetailsObjinfo.getString("extenstion_date");


                            // Log.d("Insert: ", "Inserting Divisions ..");
                            db.addScheme_Products(new Scheme_Products(scheme_id1, product_id, scheme_price, region_id, company_id, crop_id, slab_id, booking_incentive, valid_from, valid_to, booking_year, season_code, extenstion_date));

                        }


                    }

                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    new Async_getalloffline().execute();
                }

            } else {
                Log.e("ServiceHandler", "Couldn't get any data from the url");
                Toast.makeText(getApplicationContext(), "No data found", Toast.LENGTH_LONG).show();
            }


        }


    }
}



