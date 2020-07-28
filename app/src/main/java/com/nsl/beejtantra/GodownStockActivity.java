package com.nsl.beejtantra;

import android.content.Intent;
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
import com.nsl.beejtantra.pojo.GodownStock;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class GodownStockActivity extends AppCompatActivity {
    @BindView(R.id.toolbar)
    android.support.v7.widget.Toolbar toolbar;
    @BindView(R.id.search_view)
    SearchView searchView;
    @BindView(R.id.toolbar_title)
    TextView titleView;
    @BindView(R.id.godowns_recycler)
    RecyclerView godownsRecycler;
    private DatabaseHandler db;
    List<GodownStock> godownStocks, searchedGodownStocks = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_godown);
        ButterKnife.bind(this);
        titleView.setText("Godown Stock");
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

        godownStocks = db.getGodownStocksByID(getIntent().getExtras().getString("godown_id"));
        if (godownStocks != null & godownStocks.size() > 0)
            setListOnAdapter(godownStocks);


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                if (s.length() > 0) {
                    searchedGodownStocks.clear();
                    for (int i = 0; i < godownStocks.size(); i++) {
                        if (godownStocks.get(i).productName.toLowerCase().contains(s.toLowerCase())) {
                            searchedGodownStocks.add(godownStocks.get(i));
                        }
                    }
                    setListOnAdapter(searchedGodownStocks);
                }
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                if (s.length() == 0) {
                    setListOnAdapter(godownStocks);
                }
                return false;
            }
        });


    }

    private void setListOnAdapter(List<GodownStock> godownStocks) {
        GodownStockAdapter godownStockAdapter = new GodownStockAdapter(godownStocks);
        godownsRecycler.setAdapter(godownStockAdapter);
    }

    class GodownStockAdapter extends RecyclerView.Adapter<GodownStockAdapter.GodownStockHolder> {

        List<GodownStock> godowns;

        public GodownStockAdapter(List<GodownStock> godowns) {
            this.godowns = godowns;
        }

        @NonNull
        @Override
        public GodownStockHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            GodownStockHolder godownHolder = new GodownStockHolder(getLayoutInflater().inflate(R.layout.godown_row1, null));
            return godownHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull GodownStockHolder godownHolder, int i) {
            final GodownStock godown = godowns.get(i);
            godownHolder.code.setText(Common.isStringNull(godown.quantity));
            godownHolder.company.setText(Common.isStringNull(godown.divisionName));
            godownHolder.goDownName.setText(Common.isStringNull(godown.productName));
            godownHolder.regionName.setText(Common.isStringNull(godown.cropName));


        }

        @Override
        public int getItemCount() {
            return godowns.size();
        }

        class GodownStockHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.godown_name)
            TextView goDownName;
            @BindView(R.id.code)
            TextView code;
            @BindView(R.id.company_name)
            TextView company;
            @BindView(R.id.region_name)
            TextView regionName;


            public GodownStockHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
