package com.nsl.beejtantra.orderindent;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nsl.beejtantra.DatabaseHandler;
import com.nsl.beejtantra.R;
import com.nsl.beejtantra.commonutils.Common;
import com.nsl.beejtantra.pojo.ServiceOrderHistory;

import org.w3c.dom.Text;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class HistoryDetailsActivity extends AppCompatActivity {
    @BindView(R.id.listView)
    RecyclerView historyRecycler;
    @BindView(R.id.tv_alert)
    TextView tvAlert;
    @BindView(R.id.product)
    TextView product;
    private DatabaseHandler db;
    private List<ServiceOrderHistory> sohList;
    private String type;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_history_details);
        ButterKnife.bind(this);
        db = new DatabaseHandler(this);
        String order_detail_id = getIntent().getExtras().getString("order_detail_id", "");
        type = getIntent().getExtras().getString("type", "1");
        if (order_detail_id.length() > 0)
            sohList = db.getSOHByOrderDetailId(order_detail_id);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        TextView titleView = findViewById(R.id.toolbar_title);
        setSupportActionBar(toolbar);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //What to do on back clicked
                finish();
            }
        });

        if (sohList != null && sohList.size() > 0) {
            product.setText("PRODUCT : " + db.getProductNametByProductId(sohList.get(0).productId).toUpperCase());
            historyRecycler.setVisibility(View.VISIBLE);
            product.setVisibility(View.VISIBLE);
            tvAlert.setVisibility(View.GONE);
            HistoryAdapter historyAdapter = new HistoryAdapter();
            LinearLayoutManager llm = new LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false);
            historyRecycler.setLayoutManager(llm);
            historyRecycler.setAdapter(historyAdapter);
        } else {
            tvAlert.setVisibility(View.VISIBLE);
            historyRecycler.setVisibility(View.GONE);
            product.setVisibility(View.GONE);
        }
    }

    class HistoryAdapter extends RecyclerView.Adapter<HistoryAdapter.HistoryHolder> {

        public HistoryAdapter() {

        }

        @NonNull
        @Override
        public HistoryHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
            HistoryHolder historyHolder = new HistoryHolder(getLayoutInflater().inflate(R.layout.row_order_history, null));
            return historyHolder;
        }

        @Override
        public void onBindViewHolder(@NonNull HistoryHolder historyHolder, int pos) {
            if (pos == 0)
                historyHolder.headerLayout.setVisibility(View.VISIBLE);
            else
                historyHolder.headerLayout.setVisibility(View.GONE);
            ServiceOrderHistory soh = sohList.get(pos);
            int srNo = pos + 1;
            historyHolder.tvSrNo.setText(String.valueOf(srNo));
            historyHolder.tvDate.setText(soh.cDateTime.split(" ")[0]);
            historyHolder.tvProduct.setText(soh.orderQuantity);
            historyHolder.tvQuantity.setText(soh.quantity);
            historyHolder.tvRate.setText(soh.modifiedBy);

//            if (type.equalsIgnoreCase("1"))
//                historyHolder.tvAmount.setText(soh.orderPrice);
//            else
//                historyHolder.tvAmount.setText(soh.advanceAmount);
        }

        @Override
        public int getItemCount() {
            return sohList.size();
        }

        class HistoryHolder extends RecyclerView.ViewHolder {
            @BindView(R.id.header)
            RelativeLayout headerLayout;
            @BindView(R.id.tv_sr_no)
            TextView tvSrNo;
            @BindView(R.id.tv_date)
            TextView tvDate;
            @BindView(R.id.tv_product)
            TextView tvProduct;
            @BindView(R.id.tv_qunatity)
            TextView tvQuantity;
            @BindView(R.id.tv_rate)
            TextView tvRate;
//            @BindView(R.id.tv_amount)
//            TextView tvAmount;

            public HistoryHolder(@NonNull View itemView) {
                super(itemView);
                ButterKnife.bind(this, itemView);
            }
        }
    }
}
