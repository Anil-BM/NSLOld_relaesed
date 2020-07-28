package com.nsl.beejtantra.orderindent;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nsl.beejtantra.DatabaseHandler;
import com.nsl.beejtantra.R;
import com.nsl.beejtantra.commonutils.Common;
import com.nsl.beejtantra.pojo.ServiceOrderApproval;
import com.nsl.beejtantra.pojo.ServiceOrderHistory;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SupraSoft on 3/5/2019.
 */

public class ApprovalsAdapter extends BaseAdapter {
    private List<ServiceOrderApproval> orderApprovals;
    Activity context;
    DatabaseHandler db;
    private ApprovalHolder approvalHolder;

    public ApprovalsAdapter(Activity context, List<ServiceOrderApproval> orderApprovals) {
        this.orderApprovals = orderApprovals;
        this.context = context;
        db = new DatabaseHandler(context);
    }

   /* @NonNull
    @Override
    public ApprovalHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ApprovalHolder approvalHolder = new ApprovalHolder(context.getLayoutInflater().inflate(R.layout.row_approval, null));
        return approvalHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ApprovalHolder approvalHolder, int i) {
        ServiceOrderApproval serviceOrderApproval = orderApprovals.get(i);
        if (serviceOrderApproval.orderStatus.equalsIgnoreCase("0")) {
            approvalHolder.approvalStatus.setImageResource(R.drawable.pending_icon);
        } else if (serviceOrderApproval.orderStatus.equalsIgnoreCase("1")) {
            approvalHolder.approvalStatus.setImageResource(R.drawable.approved_icon);
        } else if (serviceOrderApproval.orderStatus.equalsIgnoreCase("2")) {
            approvalHolder.approvalStatus.setImageResource(R.drawable.rejected_icon);
        }
        List<ServiceOrderHistory> serviceOrderHistories = db.getOrderHistoryByApprovalId(serviceOrderApproval.orderApprovalId);
        if (serviceOrderHistories != null && serviceOrderHistories.size() > 0) {
            approvalHolder.approvalUserName.setText(Common.isStringNull(serviceOrderHistories.get(0).modifiedBy));
            ProductAdapter productAdapter = new ProductAdapter(context, serviceOrderHistories);
            LinearLayoutManager llm = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
            approvalHolder.productRecycler.setLayoutManager(llm);
            approvalHolder.productRecycler.setAdapter(productAdapter);
        } else {
            approvalHolder.approvalUserName.setText(Common.isStringNull(serviceOrderApproval.pendingBy));
        }
    }

    @Override
    public int getItemCount() {
        return orderApprovals.size();
    }*/

    @Override
    public int getCount() {
        return orderApprovals.size();
    }

    @Override
    public Object getItem(int i) {
        return orderApprovals.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = context.getLayoutInflater().inflate(R.layout.row_approval, null);
            approvalHolder = new ApprovalHolder(view);
            view.setTag(approvalHolder);
        } else {
            approvalHolder = (ApprovalHolder) view.getTag();
        }
        ServiceOrderApproval serviceOrderApproval = orderApprovals.get(i);
        if (serviceOrderApproval.orderStatus.equalsIgnoreCase("0")) {
            approvalHolder.approvalStatus.setImageResource(R.drawable.pending_icon);
        } else if (serviceOrderApproval.orderStatus.equalsIgnoreCase("1")) {
            approvalHolder.approvalStatus.setImageResource(R.drawable.approved_icon);
        } else if (serviceOrderApproval.orderStatus.equalsIgnoreCase("2")) {
            approvalHolder.approvalStatus.setImageResource(R.drawable.rejected_icon);
        }
        List<ServiceOrderHistory> serviceOrderHistories = db.getOrderHistoryByApprovalId(serviceOrderApproval.orderApprovalId);
        if (serviceOrderHistories != null && serviceOrderHistories.size() > 0) {
            approvalHolder.approvalUserName.setText(Common.isStringNull(serviceOrderHistories.get(0).modifiedBy));
            approvalHolder.productRecycler.setVisibility(View.VISIBLE);
            ProductAdapter productAdapter = new ProductAdapter(context, serviceOrderHistories);
//                LinearLayoutManager llm = new LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false);
//                approvalHolder.productRecycler.setLayoutManager(llm);
            approvalHolder.productRecycler.setAdapter(productAdapter);
            Common.Log.i("Products");
            Common.Log.i("--------");
            Common.setListViewHeightBasedOnChildren(approvalHolder.productRecycler);
        } else {
            approvalHolder.productRecycler.setVisibility(View.GONE);
            approvalHolder.approvalUserName.setText(Common.isStringNull(serviceOrderApproval.pendingBy));
        }
        return view;
    }

    class ApprovalHolder /*extends RecyclerView.ViewHolder*/ {
        @BindView(R.id.approval_user_name)
        TextView approvalUserName;
        @BindView(R.id.approval_status)
        ImageView approvalStatus;
        @BindView(R.id.product_recycler)
        ListView productRecycler;

        public ApprovalHolder(@NonNull View itemView) {
            ButterKnife.bind(this, itemView);
        }
    }


}
