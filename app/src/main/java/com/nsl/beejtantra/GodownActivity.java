package com.nsl.beejtantra;

import android.app.ProgressDialog;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nsl.beejtantra.commonutils.Common;
import com.nsl.beejtantra.pojo.Godown;
import com.nsl.beejtantra.pojo.GodownResVo;
import com.nsl.beejtantra.pojo.GodownStock;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USER_COMPANY_DIVISION_COMPANY_ID;
import static com.nsl.beejtantra.DatabaseHandler.KEY_TABLE_USER_COMPANY_DIVISION_USER_ID;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_GODOWN;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_GODOWN_STOCK;
import static com.nsl.beejtantra.DatabaseHandler.TABLE_USER_COMPANY_DIVISION;

public class GodownActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar toolbar;
    @BindView(R.id.search_view)
    SearchView searchView;
    @BindView(R.id.toolbar_title)
    TextView titleView;
    @BindView(R.id.godowns_recycler)
    RecyclerView godownsRecycler;
    private DatabaseHandler db;
    String userRegions = "", companies = "";
    List<Godown> godowns, searchedGodowns = new ArrayList<>();
    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_godown);
        ButterKnife.bind(this);
        db = new DatabaseHandler(this);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        searchView.setOnSearchClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                titleView.setVisibility(View.GONE);
            }
        });
        searchView.setOnCloseListener(new android.support.v7.widget.SearchView.OnCloseListener() {
            @Override
            public boolean onClose() {
                titleView.setVisibility(View.VISIBLE);
                return false;
            }
        });

        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        godownsRecycler.setLayoutManager(llm);

        if (Common.haveInternet(GodownActivity.this)) {
            progressDialog = new ProgressDialog(GodownActivity.this);
            progressDialog.setMessage("Please Wait...");
            progressDialog.setCancelable(false);
            progressDialog.show();
            setQueryData();
            if (userRegions.length() > 0 && companies.length() > 0) {

                MyApplication.getInstance().getRetrofitAPI().getGodowns(userRegions, companies).enqueue(new Callback<GodownResVo>() {
                    @Override
                    public void onResponse(Call<GodownResVo> call, Response<GodownResVo> response) {
                        try {
                            GodownResVo godownResVo = response.body();
                            if (godownResVo != null) {
                                db.deleteDataByTableName(TABLE_GODOWN);
                                db.deleteDataByTableName(TABLE_GODOWN_STOCK);
                                List<Godown> godowns = godownResVo.godown;
                                if (godowns != null & godowns.size() > 0) {
                                    for (int i = 0; i < godowns.size(); i++) {
                                        db.insertGodown(godowns.get(i));
                                        List<GodownStock> godownStocks = godowns.get(i).godownStock;
                                        if (godownStocks != null & godownStocks.size() > 0) {
                                            for (int j = 0; j < godownStocks.size(); j++)
                                                db.insertGodownStock(godownStocks.get(j));
                                        }
                                    }
                                }

                                setDataToUI();

                            }
                        } catch (Exception e) {
                            if (progressDialog != null)
                                Common.dismissProgressDialog(progressDialog);
                        }

                    }

                    @Override
                    public void onFailure(Call<GodownResVo> call, Throwable t) {
                        if (progressDialog != null)
                            Common.dismissProgressDialog(progressDialog);
                    }
                });

            }
        } else {
            setDataToUI();
        }

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (s.length() > 0) {
                    searchedGodowns.clear();
                    for (int i = 0; i < godowns.size(); i++) {
                        if (godowns.get(i).godownName.toLowerCase().contains(s.toLowerCase())) {
                            searchedGodowns.add(godowns.get(i));
                        }
                    }
                    setListOnAdapter(searchedGodowns);

                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.length() == 0) {
                    if (godowns != null)
                        setListOnAdapter(godowns);
                }
                return false;
            }
        });
    }

    private void setDataToUI() {
        godowns = db.getAllGodowns();
        if (godowns != null && godowns.size() > 0) {
            setListOnAdapter(godowns);
        }
        if (progressDialog != null)
            Common.dismissProgressDialog(progressDialog);
    }


    private void setListOnAdapter(List<Godown> godowns) {
        GodownAdapter godownAdapter = new GodownAdapter(godowns);
        godownsRecycler.setAdapter(godownAdapter);

    }

    private void setQueryData() {
        List<Regions> regions = db.getRegionsByUserId(Common.getUserIdFromSP(this));
        StringBuffer regionBuffer = new StringBuffer();
        StringBuffer companyBuffer = new StringBuffer();

        for (int i = 0; i < regions.size(); i++) {
            regionBuffer.append(regions.get(i)._region_master_id + ",");
        }
        if (regionBuffer.length() > 0)
            userRegions = regionBuffer.substring(0, regionBuffer.lastIndexOf(",")).toString().trim();

        String query = "SELECT DISTINCT " + KEY_TABLE_USER_COMPANY_DIVISION_COMPANY_ID + " FROM " + TABLE_USER_COMPANY_DIVISION + " WHERE " + KEY_TABLE_USER_COMPANY_DIVISION_USER_ID + " = " + Common.getUserIdFromSP(this);
        Cursor cursor = db.getWritableDatabase().rawQuery(query, null);
        if (cursor.moveToFirst()) {
            do {
                companyBuffer.append(cursor.getString(cursor.getColumnIndex(KEY_TABLE_USER_COMPANY_DIVISION_COMPANY_ID)) + ",");
            } while (cursor.moveToNext());
            cursor.close();
        }
        if (companyBuffer.length() > 0)
            companies = companyBuffer.substring(0, companyBuffer.lastIndexOf(",")).toString().trim();
    }

    class GodownAdapter extends RecyclerView.Adapter<GodownAdapter.GodownHolder> {

        List<Godown> godowns;

        public GodownAdapter(List<Godown> godowns) {
            this.godowns = godowns;
        }

        @NonNull
        @Override
        public GodownHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            GodownHolder godownHolder = new GodownHolder(getLayoutInflater().inflate(R.layout.godown_row, null));
            return godownHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull GodownHolder godownHolder, int i) {
            final Godown godown = godowns.get(i);
            godownHolder.code.setText(Common.isStringNull(godown.godownCode));
            godownHolder.company.setText(Common.isStringNull(godown.companyName));
            godownHolder.goDownName.setText(Common.isStringNull(godown.godownName));
            godownHolder.regionName.setText(Common.isStringNull(godown.regionName));

            godownHolder.rowLayout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent stockIntent = new Intent(GodownActivity.this, GodownStockActivity.class);
                    stockIntent.putExtra("godown_id", godown.godownId);
                    startActivity(stockIntent);
                }
            });

        }

        @Override
        public int getItemCount() {
            return godowns.size();
        }

        class GodownHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.godown_name)
            TextView goDownName;
            @BindView(R.id.code)
            TextView code;
            @BindView(R.id.company_name)
            TextView company;
            @BindView(R.id.region_name)
            TextView regionName;
            @BindView(R.id.ll_horizontal)
            LinearLayout rowLayout;

            public GodownHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
