package com.nsl.beejtantra.TFA;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.nsl.beejtantra.Constants;
import com.nsl.beejtantra.DatabaseHandler;
import com.nsl.beejtantra.R;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ApprovalsAdapter extends BaseAdapter {
    private List<Approvaldetails> orderApprovals;
    Activity context;
    DatabaseHandler db;
    String userId,userName,team;
    int role;
    private ApprovalHolder approvalHolder;
    public static final String mypreference = "mypref";
    SharedPreferences sharedpreferences;
    int finalappraoval_status;
    int finalappraoval_status_reject;
    public ApprovalsAdapter(Activity context, List<Approvaldetails> orderApprovals) {
        this.orderApprovals = orderApprovals;
        this.context = context;
        db = new DatabaseHandler(context);
        sharedpreferences = context.getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        userId = sharedpreferences.getString("userId", "");
        team = sharedpreferences.getString("team", "");
        role = sharedpreferences.getInt(Constants.SharedPrefrancesKey.ROLE, 0);
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
        Approvaldetails serviceOrderApproval = orderApprovals.get(orderApprovals.size()-(i+1));

        if(i==0)
        {

            finalappraoval_status = serviceOrderApproval.approval_status2;
        }
        Toast.makeText(context ,String.valueOf(finalappraoval_status_reject), Toast.LENGTH_LONG).show();

         if(finalappraoval_status!=9&&finalappraoval_status<=4) {
             Toast.makeText(context ,"No", Toast.LENGTH_LONG).show();
             if (finalappraoval_status == 1)
             {
                 if (i == 0) // MO
                 {
                     approvalHolder.approvalStatus.setImageResource(R.drawable.pending_icon);
                     approvalHolder.approvalUserName.setText(serviceOrderApproval.pending_approval_name);//pendingby MO

                 }

             }
             if (finalappraoval_status == 2)//Mo
             {
                 if (i == 0) // MO
                 {
                     approvalHolder.approvalStatus.setImageResource(R.drawable.pending_icon);
                     approvalHolder.approvalUserName.setText(serviceOrderApproval.pending_approval_name);//pendingby AM

                 }
                 else { //fapps=5
                     approvalHolder.approvalStatus.setImageResource(R.drawable.approved_icon);
                     approvalHolder.approvalUserName.setText(serviceOrderApproval.pending_approval_name);  //approved by mo
                 }
             }
             if (finalappraoval_status == 3)
             {
                 if (i == 0) // AM
                 {
                     approvalHolder.approvalStatus.setImageResource(R.drawable.pending_icon);
                     approvalHolder.approvalUserName.setText(serviceOrderApproval.pending_approval_name);//pendingby Rm

                 } else {//fappst=6,5 am,mo
                     approvalHolder.approvalStatus.setImageResource(R.drawable.approved_icon);
                     approvalHolder.approvalUserName.setText(serviceOrderApproval.pending_approval_name);  //approved Am
                 }

             }
             if (finalappraoval_status == 4)
             {

                     approvalHolder.approvalStatus.setImageResource(R.drawable.approved_icon);
                     approvalHolder.approvalUserName.setText(serviceOrderApproval.pending_approval_name);
                  if(i==0)
                  {
                     approvalHolder.approvalStatus.setImageResource(R.drawable.approved_icon);
                     approvalHolder.approvalUserName.setText("Field Activity Approvals");
                 }

             }
         }


        if(finalappraoval_status!=9&&finalappraoval_status>=4) {
            Toast.makeText(context ,"No", Toast.LENGTH_LONG).show();
            if (finalappraoval_status == 5)
            {
                if (i == 0) // MO
                {
                    approvalHolder.approvalStatus.setImageResource(R.drawable.pending_icon);
                    approvalHolder.approvalUserName.setText(serviceOrderApproval.pending_approval_name);//pendingby MO

                }

            }
            if (finalappraoval_status == 6)//Mo
            {
                if (i == 0) // MO
                {
                    approvalHolder.approvalStatus.setImageResource(R.drawable.pending_icon);
                    approvalHolder.approvalUserName.setText(serviceOrderApproval.pending_approval_name);//pendingby AM

                }
                else { //fapps=5
                    approvalHolder.approvalStatus.setImageResource(R.drawable.approved_icon);
                    approvalHolder.approvalUserName.setText(serviceOrderApproval.pending_approval_name);  //approved by mo
                }
            }
            if (finalappraoval_status == 7)
            {
                if (i == 0) // AM
                {
                    approvalHolder.approvalStatus.setImageResource(R.drawable.pending_icon);
                    approvalHolder.approvalUserName.setText(serviceOrderApproval.pending_approval_name);//pendingby Rm

                } else {//fappst=6,5 am,mo
                    approvalHolder.approvalStatus.setImageResource(R.drawable.approved_icon);
                    approvalHolder.approvalUserName.setText(serviceOrderApproval.pending_approval_name);  //approved Am
                }

            }
            if (finalappraoval_status == 8)
            {
                approvalHolder.approvalStatus.setImageResource(R.drawable.approved_icon);
                approvalHolder.approvalUserName.setText(serviceOrderApproval.pending_approval_name);
                if(i==0)
                {
                    approvalHolder.approvalStatus.setImageResource(R.drawable.approved_icon);
                    approvalHolder.approvalUserName.setText("Field Activity Approvals");
                }

            }
        }



        if(finalappraoval_status==9)
        {
           // Toast.makeText(context ,serviceOrderApproval.approval_name, Toast.LENGTH_LONG).show();
            if(i==0)
            {
                approvalHolder.approvalStatus.setImageResource(R.drawable.rejected_icon);
                approvalHolder.approvalUserName.setText(serviceOrderApproval.approval_name);
            }

           else if (i!=orderApprovals.size()-1)
            {
                approvalHolder.approvalStatus.setImageResource(R.drawable.approved_icon);
                approvalHolder.approvalUserName.setText(serviceOrderApproval.approval_name);
            }
        }
        // List<ServiceOrderHistory> serviceOrderHistories = db.getOrderHistoryByApprovalId(serviceOrderApproval.orderApprovalId);
        /*if (serviceOrderHistories != null && serviceOrderHistories.size() > 0) {
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
        }*/
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

