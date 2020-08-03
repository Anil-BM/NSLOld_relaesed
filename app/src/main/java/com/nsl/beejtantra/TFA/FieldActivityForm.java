package com.nsl.beejtantra.TFA;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.ComponentName;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.location.Location;
import android.location.LocationListener;
import android.os.AsyncTask;
import android.os.Environment;
import android.os.IBinder;
import android.provider.MediaStore;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.gun0912.tedpermission.PermissionListener;
import com.gun0912.tedpermission.TedPermission;
import com.nsl.beejtantra.Constants;
import com.nsl.beejtantra.DatabaseHandler;
import com.nsl.beejtantra.FarmerCoupans.CoupanForm;
import com.nsl.beejtantra.GPSTracker;
import com.nsl.beejtantra.R;
import com.nsl.beejtantra.SelectedCities;
import com.nsl.beejtantra.TFA.support.ActivityPlanner2;
import com.nsl.beejtantra.commonutils.Common;
import com.squareup.okhttp.MediaType;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CROPS_CROP_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CROPS_CROP_NAME;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_CROPS_DIVISION_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_DIVISION_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_DIVISION_NAME;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_CROPS;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_DIVISION;

public class FieldActivityForm extends BaseActivity implements LocationListener {
    /*in crops method query param should be dynamic*/
    ArrayList<String> cropMarketadapter;
    int SECOND_ACTIVITY_REQUEST_CODE = 12;
    ArrayList<String> divisionMarketAdapter;
    List<SelectedCities> divisions = new ArrayList<>();
    String districtId, divisionId, divisionName;
    List<SelectedCities> crops = new ArrayList<>();
    List<byte[]> images_list = new ArrayList<byte[]>();
    DatabaseHandler db;
    private int REQUEST_CAMERA = 0, SELECT_FILE = 1;
    Spinner spin_crop;
    private String base64String1 = null;
    private String base64String2 = null;
    private String base64String3 = null;
    private String base64String4 = null;
    private String base64String5 = null;
    private String base64String6 = null;

    String jsonData,result = "NO IMAGE",userChoosenTask;
    TextView tv_image,tv_activity,tv_place,tv_crop,tv_date;
    ImageView iv_edit;
    long ch;
    ProgressDialog progressDialog;

    @BindView(R.id.et_ownername)
    EditText et_ownername;

    @BindView(R.id.et_ownerpnno)
    EditText et_ownerpnno;

    @BindView(R.id.et_totalnooffarmers)
    EditText et_totalnooffarmers;

    @BindView(R.id.et_non_used_farmers)
    EditText et_non_used_farmers;

    @BindView(R.id.et_nooffarmers)
    EditText et_nooffarmers;

    @BindView(R.id.et_estimationperhead)
    EditText et_estimationperhead;

    @BindView(R.id.et_totalexpences)
    EditText et_totalexpences;

    @BindView(R.id.et_geotag)
    EditText et_geotag;

    @BindView(R.id.btn_appt)
    Button btn_appt;

    @BindView(R.id.ivImage1)
    ImageView ivImage1;
    @BindView(R.id.ivImage2)
    ImageView ivImage2;
    @BindView(R.id.ivImage3)
    ImageView ivImage3;
    @BindView(R.id.ivImage4)
    ImageView ivImage4;
    @BindView(R.id.ivImage5)
    ImageView ivImage5;
    @BindView(R.id.ivImage6)
    ImageView ivImage6;



    ArrayList<HashMap<String, String>> favouriteItem;
    private int imageViewId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_field_form);

        ButterKnife.bind(this);

        favouriteItem = new ArrayList<HashMap<String, String>>();
        favouriteItem = (ArrayList<HashMap<String, String>>) getIntent().getSerializableExtra("favouriteItem");
        initialise();

        Toast(favouriteItem.get(0).get("tfa_master_id"));
        iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tv_edit();
            }
        });
        et_geotag.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(FieldActivityForm.this, TfaLocationActivity.class);
                startActivityForResult(intent, SECOND_ACTIVITY_REQUEST_CODE);
            }
        });

        ivImage1.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                selectImage();
                imageViewId = ivImage1.getId();
            }
        });

        ivImage2.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                selectImage();
                imageViewId = ivImage2.getId();
            }
        });

        ivImage3.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                selectImage();
                imageViewId = ivImage3.getId();
            }
        });

        ivImage4.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                selectImage();
                imageViewId = ivImage4.getId();
            }
        });

        ivImage5.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                selectImage();
                imageViewId = ivImage5.getId();
            }
        });

        ivImage6.setOnClickListener(new View.OnClickListener()
        {
            @Override
            public void onClick(View v) {
                selectImage();
                imageViewId = ivImage6.getId();
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
                Integer v2 = Integer.parseInt(!et_non_used_farmers.getText().toString().isEmpty() ?
                        et_non_used_farmers.getText().toString() : "0");
                Integer value = v1+ v2;
                et_totalnooffarmers.setText(value.toString());
                et_estimationperhead.setText("");
            }
        });
        et_non_used_farmers.addTextChangedListener(new TextWatcher() {
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

                Integer v2 = Integer.parseInt(!et_non_used_farmers.getText().toString().isEmpty() ?
                        et_non_used_farmers.getText().toString() : "0");
                Integer value = v1 + v2;
                et_totalnooffarmers.setText(value.toString());
                et_estimationperhead.setText("");
            }
        });

        et_totalexpences.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    Integer v1 = Integer.parseInt(!et_totalnooffarmers.getText().toString().isEmpty() ?
                            et_totalnooffarmers.getText().toString() : "0");

                    Integer v2 = Integer.parseInt(!et_totalexpences.getText().toString().isEmpty() ?
                            et_totalexpences.getText().toString() : "0");
                    if(v1!=0&&v2!=0)
                        et_estimationperhead.setText(String.valueOf(Double.valueOf(Double.valueOf(v2) / Double.valueOf(v1))));
                }
                catch (Exception e)
                {
                    Toast(e.toString());
                }
              /*  if(et_nooffarmers.getText().toString()!=null&&!et_nooffarmers.getText().toString().equals(""))
                {
                    if(et_non_used_farmers.getText().toString()!=null&&!et_non_used_farmers.getText().toString().equals(""))
                    {
                        et_estimationperhead.setText(String.valueOf(
                                Integer.valueOf(et_totalexpences.getText().toString())/Integer.valueOf(et_nooffarmers.getText().toString())));
                    }
                }*/
            }
        });


    }

    @Override
    protected void onStart() {
        super.onStart();
        Intent intent = new Intent(FieldActivityForm.this, GPSTracker.class);
        bindService(intent, mConnection, Context.BIND_AUTO_CREATE);
    }
    @Override
    protected void onResume() {
        super.onResume();
        if(getIntent().getStringExtra("lat")!=null)
        {
            et_geotag.setText(getIntent().getStringExtra("lat")+" ,"+getIntent().getStringExtra("lng"));
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mConnection);
    }

    private void initialise() {
        db = new DatabaseHandler(this);
        iv_edit=(ImageView) findViewById(R.id.edit_view);
        tv_activity=(TextView)findViewById(R.id.tv_activity);
        tv_place=(TextView)findViewById(R.id.tv_place);
        tv_crop=(TextView)findViewById(R.id.tv_crop);
        tv_date=(TextView)findViewById(R.id.tv_date);
        tv_image=(TextView)findViewById(R.id.tv_img);



        Typeface custom_font = Typeface.createFromAsset(getAssets(),  "fonts/SEGOEWP-LIGHT.TTF");
        et_ownername.setTypeface(custom_font);
        et_ownerpnno.setTypeface(custom_font);
        et_nooffarmers.setTypeface(custom_font);
        et_totalnooffarmers.setTypeface(custom_font);
        et_non_used_farmers.setTypeface(custom_font);
        et_estimationperhead.setTypeface(custom_font);
        et_totalexpences.setTypeface(custom_font);
        tv_activity.setTypeface(custom_font);
        tv_place.setTypeface(custom_font);
        tv_crop.setTypeface(custom_font);
        tv_date.setTypeface(custom_font);
        tv_image.setTypeface(custom_font);
        et_geotag.setTypeface(custom_font);

        tv_activity.setText("Type  : "+(favouriteItem.get(0).get("tfa_title")));
        tv_place.setText("Place : "+(favouriteItem.get(0).get("village")+" ,"+
                favouriteItem.get(0).get("taluka")+" ,"+favouriteItem.get(0).get("district_name")));
        tv_crop.setText("Crop  :"+(favouriteItem.get(0).get("tfa_title")));
        tv_date.setText("date  :"+(favouriteItem.get(0).get("activity_date")));

    }

    public void tv_edit() {
        // TODO submit data to server...

        final AlertDialog dialog = new AlertDialog.Builder(FieldActivityForm.this).create();
        View popupView = getLayoutInflater().inflate(R.layout.fieldactivityeditfields_popup, null);
        dialog.setView(popupView);
        dialog.show();

        spin_crop = popupView.findViewById(R.id.sp_crop);
        // Button submit = popupView.findViewById(R.id.submit_popup);









        /*submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });*/
        Crops();
    }

    private void Crops() {


        divisionMarketAdapter = new ArrayList<String>();
        divisionMarketAdapter.add("Select Division");

        String query = "SELECT "+ KEY_TABLE_DIVISION_ID +","+ KEY_TABLE_DIVISION_NAME + " FROM " + TABLE_DIVISION+ " where "+ KEY_TABLE_DIVISION_NAME+"='Field Crops'";
       /* String query = "SELECT DISTINCT ud." + KEY_TABLE_USER_COMPANY_DIVISION_DIVISION_ID + ",d." +
                KEY_TABLE_DIVISION_NAME + " FROM " + TABLE_USER_COMPANY_DIVISION + " ud INNER JOIN " +
                TABLE_DIVISION + " d ON ud." + KEY_TABLE_USER_COMPANY_DIVISION_DIVISION_ID + " = d." +
                KEY_TABLE_DIVISION_MASTER_ID + " WHERE ud." +
                KEY_TABLE_USER_COMPANY_DIVISION_USER_ID + " in (" + Common.getTeamFromSP(this) + ")";*/
        Common.Log.i("Query " + query);
        Cursor cursor = db.getWritableDatabase().rawQuery(query, null);


        if (cursor.moveToFirst()) {
            do {
                Toast.makeText(getApplicationContext(),cursor.getString(cursor.getColumnIndex(KEY_TABLE_DIVISION_ID)),Toast.LENGTH_LONG).show();
                SelectedCities selectedCities = new SelectedCities();
                selectedCities.setCityId(cursor.getString(cursor.getColumnIndex(KEY_TABLE_DIVISION_ID)));
                selectedCities.setCityName(cursor.getString(cursor.getColumnIndex(KEY_TABLE_DIVISION_NAME)));
                divisionMarketAdapter.add(cursor.getString(cursor.getColumnIndex(KEY_TABLE_DIVISION_NAME)));
                divisions.add(selectedCities);
                cropMarketadapter = new ArrayList<>();
                cropMarketadapter.add("Select Crop");
                spin_crop.setAdapter(new ArrayAdapter<String>(getApplicationContext(), R.layout.spinner_item, R.id.customSpinnerItemTextView, cropMarketadapter));

                divisionId = divisions.get(1 - 1).getCityId();
                divisionName = divisions.get(1 - 1).getCityName();

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
            } while (cursor.moveToNext());
        }
    }
    public void submitact(View v) throws JSONException
    {

        if(et_ownername.getText().toString().equals("")||et_ownername.getText().toString()==null)
        {
            Toast("Enter owner name");
        }
        else if(et_ownerpnno.getText().toString().equals("")||et_ownerpnno.getText().toString()==null)
        {
            Toast("Enter owner number");
        }
        else if(et_non_used_farmers.getText().toString().equals("")||et_non_used_farmers.getText().toString()==null)
        {
            Toast("Enter no of non used farmers");
        }
        else if(et_nooffarmers.getText().toString().equals("")||et_nooffarmers.getText().toString()==null)
        {
            Toast("Enter no of used farmers ");
        }
        else if(et_totalnooffarmers.getText().toString().equals("")||et_totalnooffarmers.getText().toString()==null)
        {
            Toast("Enter total no of farmers");
        }
        else if(et_estimationperhead.getText().toString().equals("")||et_estimationperhead.getText().toString()==null)
        {
            Toast("Enter estimation per head");
        }
        else if(et_totalexpences.getText().toString().equals("")||et_estimationperhead.getText().toString()==null)
        {
            Toast("Enter totalexpences");
        }
        else  if(Integer.valueOf(et_totalexpences.getText().toString())<Integer.valueOf(et_totalnooffarmers.getText().toString()))
        {Toast("Enter expences should be greater than attended people");}
        else if(images_list.size()==0||images_list.size()==1)
        {
            Toast("Please select atleast two images");
        }
        else
        {
            Calendar c = Calendar.getInstance();
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            //local anil
            /*ActivityPlanner2 act=new ActivityPlanner2(Integer.valueOf(favouriteItem.get(0).get("tfa_list_id")),
                    Integer.valueOf(favouriteItem.get(0).get("tfa_master_id")),Integer.valueOf(et_nooffarmers.getText().toString()),
                    Integer.valueOf(et_non_used_farmers.getText().toString()),
                    Integer.valueOf(et_totalnooffarmers.getText().toString()),Integer.valueOf(et_estimationperhead.getText().toString()),
                    Integer.valueOf(et_totalexpences.getText().toString()),
                    "435",et_ownerpnno.getText().toString(),et_ownername.getText().toString(),
                    5);

            //int tfa_list_id, int tfa_activity_master_id, int used_farmers, int non_used_farmers, int actual_no_farmers, int actual_estimation_per_head, int actual_total_expences, String location_lat_lang, String owner_number, String owner_name, int approval_status
            ch=db.addPlannerActivity2(act,"local");
            Long l= new Long(ch);
            int i=l.intValue();
            long ch1=0;
            Toast.makeText(getApplicationContext(),et_ownername.getText().toString(),Toast.LENGTH_LONG).show();
            T(et_ownername);
            Log.d("ch",String.valueOf(ch));*/

            /*if(ch>0)
            {*///local anil
                if (Common.haveInternet(getApplicationContext()))
                {
                    Calendar c1 = Calendar.getInstance();
                    SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd");
                    JSONObject mainobj = new JSONObject();

                   /* {"tfa_list_id":16,"actual_total_expences":5555,"actual_estimation_per_head":53.932038834951456,
                            "used_farmers":48,"non_used_farmers":55,"actual_no_farmers":103,"location_lat_lang":"5555",
                            "owner_number":"255","owner_name":"tt","approval_status":5,"total_expences":5555,
                            "village_list_array1":[{"tfa_list_id":16,"approval_status":"5","approval_comment":"Pending FA",
                            "approved_user_id":"861","status":"1","created_datetime":"2020-07-31"}],
                        "village_list_array":[{"tfa_list_id":16,"approval_status":"5","approval_comment":"Pending FA",
                            "approved_user_id":"861","status":"1","created_datetime":"2020-07-31"}]}*/


                    try {
                        mainobj.put("tfa_list_id",Integer.valueOf(favouriteItem.get(0).get("tfa_list_id")));
                        mainobj.put("actual_total_expences",Integer.valueOf(et_totalexpences.getText().toString()));
                        mainobj.put("actual_estimation_per_head",Double.valueOf(et_estimationperhead.getText().toString()));
                        mainobj.put("used_farmers",Integer.valueOf(et_nooffarmers.getText().toString()));
                        mainobj.put("non_used_farmers",Integer.valueOf(et_non_used_farmers.getText().toString()));
                        mainobj.put("actual_no_farmers",Integer.valueOf(et_totalnooffarmers.getText().toString()));
                    }
                  catch (NumberFormatException e)
                  {
                      Toast(e.toString());
                  }

                    mainobj.put("tfa_activity_master_id",favouriteItem.get(0).get("tfa_activity_master_id"));

                    mainobj.put("location_lat_lang",et_geotag.getText().toString());
                    mainobj.put("owner_number",et_ownerpnno.getText().toString());
                    mainobj.put("owner_name",et_ownername.getText().toString());
                    mainobj.put("approval_status",5);
                    mainobj.put("image_1",base64String1);
                    mainobj.put("image_2",base64String2);
                    mainobj.put("image_3",base64String3);
                    mainobj.put("image_4",base64String4);
                    mainobj.put("image_5",base64String5);
                    mainobj.put("image_6",base64String6);



                    JSONArray village_list_array1 = new JSONArray();
                    JSONObject villages_obj1 = new JSONObject();
                    try {
                        villages_obj1.put("tfa_approval_id", favouriteItem.get(0).get("tfa_approval_id"));
                        villages_obj1.put("tfa_list_id", Integer.valueOf(favouriteItem.get(0).get("tfa_list_id")));
                        mainobj.put("total_expences", Integer.valueOf(favouriteItem.get(0).get("total_expences")));
                    }
                    catch (NumberFormatException e)
                    {
                        Toast(e.toString());
                    }


                    villages_obj1.put("approval_status", "5");

                    villages_obj1.put("approval_comment", "Pending FA");
                    villages_obj1.put("approved_user_id",getIntent().getStringExtra("selectedid"));
                    villages_obj1.put("status", "1");
                    villages_obj1.put("created_datetime",sdf1.format(c1.getTime()));
                    village_list_array1.put(villages_obj1);
                    mainobj.put("village_list_array1", village_list_array1);


                    mainobj.put("village_list_array", village_list_array1);
                    Log.d("mainobj", (mainobj.toString()));
                    new Async_UpdateFieldActivityForm().execute(mainobj.toString());
                }
                else
                {
                   /* Intent it = new Intent(FieldActivityForm.this, FieldActivity.class);
                    it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(it);*/
                   Toast("Please make sure that internet on your mobile");
                }

          /*  }
            else {
                Toast("Something went wrong");//local anil

            }*/
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

    public class Async_UpdateFieldActivityForm extends AsyncTask<String, String, String> {
        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = new ProgressDialog(FieldActivityForm.this);
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
                        .url(Constants.URL_TFA_LIST_DATA_SECOND)
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
                        Intent it = new Intent(FieldActivityForm.this, FieldActivity.class);
                        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                        startActivity(it);
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

    private void selectImage() {
        final CharSequence[] items = {"Take Photo", "Choose from Gallery",
                "Cancel"};
        AlertDialog.Builder builder = new AlertDialog.Builder(FieldActivityForm.this);
        // builder.setTitle("Add Photo!");
        builder.setItems(items, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int item) {

                if (items[item].equals("Take Photo")) {
                    userChoosenTask = "Take Photo";
                    if (ActivityCompat.checkSelfPermission(FieldActivityForm.this, Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED) {

                        cameraIntent();

                    } else {
                        PermissionListener permissionlistener = new PermissionListener() {
                            @Override
                            public void onPermissionGranted() {


                                Toast.makeText(FieldActivityForm.this, "Permission Granted", Toast.LENGTH_SHORT).show();


                            }

                            @Override
                            public void onPermissionDenied(List<String> deniedPermissions) {
                                Toast.makeText(FieldActivityForm.this, "Permission Denied", Toast.LENGTH_SHORT).show();

                            }


                        };


                        TedPermission.with(FieldActivityForm.this)
                                .setPermissionListener(permissionlistener)
                                .setDeniedMessage("If you reject permission,you can not use this service\n\nPlease turn on permissions at [Setting] > [Permission]")
                                .setPermissions(Manifest.permission.CAMERA)
                                .check();


                    }


                } else if (items[item].equals("Choose from Gallery")) {
                    userChoosenTask = "Choose from Gallery";
                    galleryIntent();
                } else if (items[item].equals("Cancel")) {
                    dialog.dismiss();
                }
            }
        });
        builder.show();
    }
    private void galleryIntent() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);//
        startActivityForResult(Intent.createChooser(intent, "Select File"), SELECT_FILE);
    }

    private void cameraIntent() {
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, REQUEST_CAMERA);
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == SELECT_FILE)
                onSelectFromGalleryResult(data);
            else if (requestCode == REQUEST_CAMERA)
                onCaptureImageResult(data);
        }
        if (requestCode == SECOND_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK && data != null) {

            if(data.getStringExtra("lat")!=null)
            {
                et_geotag.setText(data.getStringExtra("lat")+" ,"+data.getStringExtra("lng"));
            }
        }
    }
    private void onSelectFromGalleryResult(Intent data) {
        Bitmap bm = null;
        if (data != null) {
            try {
                //
                //   bm = (Bitmap) data.getExtras().get("data");
                bm = MediaStore.Images.Media.getBitmap(getApplicationContext().getContentResolver(), data.getData());
                ByteArrayOutputStream bytes = new ByteArrayOutputStream();
                bm.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
                byte[] byteArray = bytes.toByteArray();


                images_list.add(byteArray);

                result = Base64.encodeToString(byteArray, Base64.DEFAULT);
                // Toast.makeText(getActivity(),"base64 successfull "+ result,Toast.LENGTH_LONG).show();
            } catch (IOException e) {
                e.printStackTrace();
            }

        }
//        fm.setVisibility(View.VISIBLE);
//        ivImage.setImageBitmap(bm);

        switch (imageViewId) {
            case R.id.ivImage1:
                ivImage1.setImageBitmap(bm);
                base64String1 = result;

                break;
            case R.id.ivImage2:
                ivImage2.setImageBitmap(bm);
                base64String2 = result;
                break;
            case R.id.ivImage3:
                ivImage3.setImageBitmap(bm);
                base64String3 = result;
                break;
            case R.id.ivImage4:
                ivImage4.setImageBitmap(bm);
                base64String4 = result;

                break;
            case R.id.ivImage5:
                ivImage5.setImageBitmap(bm);
                base64String5 = result;
                break;
            case R.id.ivImage6:
                ivImage6.setImageBitmap(bm);
                base64String5 = result;

                break;
        }



    }
    private void onCaptureImageResult(Intent data) {
        Bitmap thumbnail = (Bitmap) data.getExtras().get("data");
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        thumbnail.compress(Bitmap.CompressFormat.JPEG, 50, bytes);
        byte[] byteArray = bytes.toByteArray();
        images_list.add(byteArray);
        result = Base64.encodeToString(byteArray, Base64.DEFAULT);
        //Toast.makeText(getActivity(),"base64 successfull "+ result,Toast.LENGTH_LONG).show();
        File destination = new File(Environment.getExternalStorageDirectory(),
                System.currentTimeMillis() + ".jpg");
        //  String result = Base64.encodeToString(destination, Base64.DEFAULT);
        FileOutputStream fo;
        try {
            destination.createNewFile();
            fo = new FileOutputStream(destination);
            fo.write(bytes.toByteArray());
            fo.close();
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        switch (imageViewId) {
            case R.id.ivImage1:
                ivImage1.setImageBitmap(thumbnail);
                base64String1 = result;
                images_list.add(byteArray);
                break;
            case R.id.ivImage2:
                ivImage2.setImageBitmap(thumbnail);
                base64String2 = result;
                images_list.add(byteArray);
                break;
            case R.id.ivImage3:
                ivImage3.setImageBitmap(thumbnail);
                base64String3 = result;
                images_list.add(byteArray);

            case R.id.ivImage4:
                ivImage4.setImageBitmap(thumbnail);
                base64String4 = result;
                images_list.add(byteArray);
                break;
            case R.id.ivImage5:
                ivImage5.setImageBitmap(thumbnail);
                base64String5 = result;
                images_list.add(byteArray);
                break;
            case R.id.ivImage6:
                ivImage6.setImageBitmap(thumbnail);
                base64String6 = result;
                images_list.add(byteArray);

                break;
        }
//        fm.setVisibility(View.VISIBLE);
//        ivImage.setImageBitmap(thumbnail);

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
                    gpsService.showSettingsAlert(FieldActivityForm.this);
            }

        }

        @Override
        public void onServiceDisconnected(ComponentName arg0) {
            mBound = false;
        }
    };
    public void onBackPressed() {
        super.onBackPressed();
        Intent it=new Intent(FieldActivityForm.this, FieldActivity.class);
        it.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(it);
    }
}
