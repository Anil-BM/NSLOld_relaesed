package com.nsl.beejtantra;

import android.app.ProgressDialog;
import android.os.AsyncTask;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.nsl.beejtantra.commonutils.Common;
import com.nsl.beejtantra.network.RetrofitRequester;
import com.nsl.beejtantra.pojo.TargetActual;
import com.nsl.beejtantra.pojo.TargetActualResVo;
import com.twinkle94.monthyearpicker.picker.YearMonthPickerDialog;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TargetsActualsActivity extends AppCompatActivity {
    @BindView(R.id.targets_recycler)
    RecyclerView targetsRecycler;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.toolbar_title)
    TextView title;
    @BindView(R.id.month_year)
    TextView monthYear;
    @BindView(R.id.tv_no_records)
    TextView noRecords;
    private String customerId;
    private String serverDate;
    private SimpleDateFormat serverFormat, dateFormat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_targets_actuals);
        ButterKnife.bind(this);
        customerId = getIntent().getExtras().getString("customer_id");
        setSupportActionBar(toolbar);
        title.setText("TARGETS VS ACTUALS");
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
        targetsRecycler.setLayoutManager(llm);

        dateFormat = new SimpleDateFormat("MMMM yyyy");
        serverFormat = new SimpleDateFormat("MM yyyy");
        monthYear.setText(dateFormat.format(new Date()));
        serverDate = serverFormat.format(new Date());
        if (Common.haveInternet(this))
            callApi();
        else
            Toast.makeText(this, "Please Check the Internet Connection.", Toast.LENGTH_SHORT).show();

    }

    private void callApi() {
        final ProgressDialog progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Please Wait...");
        progressDialog.show();

        MyApplication.getInstance().getRetrofitAPI().getTargetActuals(customerId, serverDate.split(" ")[1], serverDate.split(" ")[0]).enqueue(new Callback<TargetActualResVo>() {
            @Override
            public void onResponse(Call<TargetActualResVo> call, Response<TargetActualResVo> response) {
                try {
                    TargetActualResVo targetActualResVo = response.body();
                    if (targetActualResVo != null) {
                        List<TargetActual> targetsvsactuals = targetActualResVo.targetsvsactuals;
                        if (targetsvsactuals != null && targetsvsactuals.size() > 0) {
                            targetsRecycler.setVisibility(View.VISIBLE);
                            noRecords.setVisibility(View.GONE);
                            TargetsAdapter targetsAdapter = new TargetsAdapter(targetsvsactuals);
                            targetsRecycler.setAdapter(targetsAdapter);
                        } else {
                            targetsRecycler.setVisibility(View.GONE);
                            noRecords.setVisibility(View.VISIBLE);
                        }
                    } else {
                        targetsRecycler.setVisibility(View.GONE);
                        noRecords.setVisibility(View.VISIBLE);
                    }
                    progressDialog.dismiss();
                } catch (Exception e) {
                    progressDialog.dismiss();
                }
            }

            @Override
            public void onFailure(Call<TargetActualResVo> call, Throwable t) {

            }
        });
    }


    class TargetsAdapter extends RecyclerView.Adapter<TargetsAdapter.TargetHolder> {
        private List<TargetActual> targetActuals;

        public TargetsAdapter(List<TargetActual> targetsvsactuals) {
            this.targetActuals = targetsvsactuals;
        }

        @NonNull
        @Override
        public TargetHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            TargetHolder targetHolder = new TargetHolder(getLayoutInflater().inflate(R.layout.row_targets, null));
            return targetHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull TargetHolder targetHolder, int pos) {
            targetHolder.tvProduct.setText(Common.isStringNull(targetActuals.get(pos).productName));
            targetHolder.tvActuals.setText(String.valueOf(targetActuals.get(pos).actualQuantity));
            targetHolder.tvTargets.setText(String.valueOf(targetActuals.get(pos).targetQuantity));
            targetHolder.tvSale.setText(String.valueOf(targetActuals.get(pos).salePercentage));

        }

        @Override
        public int getItemCount() {
            return targetActuals.size();
        }

        class TargetHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.tv_product)
            TextView tvProduct;
            @BindView(R.id.tv_targets)
            TextView tvTargets;
            @BindView(R.id.tv_actuals)
            TextView tvActuals;
            @BindView(R.id.tv_sale)
            TextView tvSale;

            public TargetHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_filter, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_filter) {
            YearMonthPickerDialog yearMonthPickerDialog = new YearMonthPickerDialog(this, new YearMonthPickerDialog.OnDateSetListener() {
                @Override
                public void onYearMonthSet(int year, int month) {
                    Calendar calendar = Calendar.getInstance();
                    calendar.set(Calendar.YEAR, year);
                    calendar.set(Calendar.MONTH, month);


                    monthYear.setText(dateFormat.format(calendar.getTime()));

                    serverDate = serverFormat.format(calendar.getTime());
                    if (Common.haveInternet(TargetsActualsActivity.this))
                        callApi();
                    else
                        Toast.makeText(TargetsActualsActivity.this, "Please Check the Internet Connection.", Toast.LENGTH_LONG).show();
                }
            });
            yearMonthPickerDialog.show();

            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
