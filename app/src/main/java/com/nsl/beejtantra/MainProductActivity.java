package com.nsl.beejtantra;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteDatabase;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;

import com.nsl.beejtantra.complaints.ComplaintsprodallActivity;
import com.nsl.beejtantra.complaints.FragmentComplaintsProducts;

import java.util.ArrayList;
import java.util.HashMap;

public class MainProductActivity extends AppCompatActivity {
    FragmentManager fm;
    FragmentTransaction ft;
    String jsonData;

    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    SharedPreferences sharedpreferences;
    public static final String mypreference = "mypref";
    ProgressDialog progressDialog;
    private SQLiteDatabase odb;
    DatabaseHandler db;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main_product);
        db = new DatabaseHandler(this);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        //   getSupportActionBar().setTitle("Complaints");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newfedback = new Intent(getApplicationContext(), ComplaintsprodallActivity.class);
                startActivity(newfedback);
                finish();
            }
        });

        fm = getSupportFragmentManager();
        FragmentComplaintsProducts fragmentComplaintsProducts = new FragmentComplaintsProducts(db);
        Bundle b = new Bundle();
        b.putString("company_id", getIntent().getStringExtra("company_id"));
        b.putString("division_id", getIntent().getStringExtra("division_id"));
        fragmentComplaintsProducts.setArguments(b);
        ft = fm.beginTransaction().add(R.id.content_frame, fragmentComplaintsProducts);
        ft.commit();

    }

}
