package com.nsl.beejtantra;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nsl.beejtantra.commonutils.Common;
import com.squareup.okhttp.FormEncodingBuilder;
import com.squareup.okhttp.OkHttpClient;
import com.squareup.okhttp.Request;
import com.squareup.okhttp.RequestBody;
import com.squareup.okhttp.Response;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import butterknife.BindView;
import butterknife.ButterKnife;


public class ChangePassword extends AppCompatActivity {

    EditText etOldPassword, etNewPassword, etConfirmpassword;
    Button btn;
    TextView result;
    @BindView(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.change_password);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (getIntent().hasExtra("is_default")) {
                    Toast.makeText(ChangePassword.this, "Sorry you can't exit! Please change password", Toast.LENGTH_SHORT).show();
                    return;
                }
                finish();
            }
        });
        etOldPassword = (EditText) findViewById(R.id.et_old_password);
        etNewPassword = (EditText) findViewById(R.id.et_new_password);
        etConfirmpassword = (EditText) findViewById(R.id.et_confirmpassword);

        btn = (Button) findViewById(R.id.button_cotton);


        btn.setOnClickListener(new View.OnClickListener() {
            public double Yield_per_hector_in_grams;

            @Override
            public void onClick(View v) {
                String bolls_plant = etOldPassword.getText().toString();
                String weight_plant = etNewPassword.getText().toString();
                String row_distnce = etConfirmpassword.getText().toString();

                if (etNewPassword.getText().toString().trim().length() == 0 && etConfirmpassword.getText().toString().trim().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please enter new password & confirm password", Toast.LENGTH_SHORT).show();
                    return;

                }
                if (etNewPassword.getText().toString().trim().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please enter new password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (etNewPassword.getText().toString().trim().length() < 8) {
                    Toast.makeText(getApplicationContext(), "Password Should contain minimum of 8 characters", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (etNewPassword.getText().toString().trim().length() > 20) {
                    Toast.makeText(getApplicationContext(), "Password Should not exceed more than 20 characters", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!isValidPassword(etNewPassword.getText().toString().trim())) {
                    Toast.makeText(getApplicationContext(), "Password Should contain atleast one Special character, Uppercase letter , Lowercase letter and Nummeric Value.", Toast.LENGTH_SHORT).show();
                    return;
                }

                if (etConfirmpassword.getText().toString().trim().length() == 0) {
                    Toast.makeText(getApplicationContext(), "Please enter confirm password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (!etConfirmpassword.getText().toString().trim().equalsIgnoreCase(etNewPassword.getText().toString().trim())) {
                    Toast.makeText(getApplicationContext(), "Mismatch new password & confirm password", Toast.LENGTH_SHORT).show();
                    return;
                }
                if (Common.haveInternet(ChangePassword.this))
                    new Async_ChangePassword().execute(etNewPassword.getText().toString());
                else
                    Toast.makeText(getApplicationContext(), "Please Turn on Internet Connection.", Toast.LENGTH_LONG).show();
            }
        });

    }

    public class Async_ChangePassword extends AsyncTask<String, String, String> {
        private String jsonData;
        private ProgressDialog progressDialog;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressDialog = Common.showProgressDialog(ChangePassword.this);

        }

        @Override
        protected String doInBackground(String... params) {

            try {

                OkHttpClient client = new OkHttpClient();
                 /*For passing parameters*/
                RequestBody formBody = new FormEncodingBuilder()
                        .add("password", params[0])
                        .add("user_id", Common.getUserIdFromSP(ChangePassword.this))

                        .build();

                Response responses = null;
                Request request = new Request.Builder()
                        .url(Constants.CHANGE_PASSWORD)
                        .post(formBody)
                        .addHeader("authorization", "Basic cmVzdDpzZWVkc0BhZG1pbg==")
                        .addHeader("content-type", "application/x-www-form-urlencoded")
                        .addHeader("cache-control", "no-cache")
                        .build();


                try {
                    responses = client.newCall(request).execute();
                    jsonData = responses.body().string();
                    System.out.println("!!!!!!!1 InsertComplaintsregulatory" + jsonData);

                } catch (IOException e) {
                    e.printStackTrace();
                }


            } catch (Exception e) {
                e.printStackTrace();
            }


            return null;
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            Common.dismissProgressDialog(progressDialog);

            if (jsonData != null) {
                JSONArray jsonarray;
                try {
                    JSONObject jsonobject = new JSONObject(jsonData);
                    String status = jsonobject.getString("status");
                    if (status.equalsIgnoreCase("success")) {
                        Common.saveDataInSP(ChangePassword.this, Constants.SharedPrefrancesKey.IS_DEFAULT_PASSWORD, false);
                        Toast.makeText(getApplicationContext(), "Password changed successfully", Toast.LENGTH_SHORT).show();
                        ChangePassword.this.finish();
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

    @Override
    public void onBackPressed() {
        if (getIntent().hasExtra("is_default")) {
            Toast.makeText(ChangePassword.this, "Sorry you can't exit! Please change password", Toast.LENGTH_SHORT).show();
            return;
        }
        super.onBackPressed();
    }


    public boolean isValidPassword(final String password) {

        Pattern pattern;
        Matcher matcher;

        final String PASSWORD_PATTERN = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#$%])(?=\\S+$).{4,}$";

        pattern = Pattern.compile(PASSWORD_PATTERN);
        matcher = pattern.matcher(password);

        return matcher.matches();

    }

}


