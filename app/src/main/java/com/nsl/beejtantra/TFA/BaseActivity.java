package com.nsl.beejtantra.TFA;

import android.support.v7.app.AppCompatActivity;
import android.widget.TextView;
import android.widget.Toast;

public class BaseActivity extends AppCompatActivity {
    public String T(TextView tv) {
        return tv.getText().toString().trim();
    }
    public Integer TInt(String tv) {
        return Integer.valueOf(tv);
    }
    public void  Toast(String s) {
        Toast.makeText(getApplicationContext(),s ,Toast.LENGTH_LONG).show();
    }
}