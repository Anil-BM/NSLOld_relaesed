package com.nsl.beejtantra.adapters;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nsl.beejtantra.R;
import com.nsl.beejtantra.orderindent.HistoryDetailsActivity;
import com.nsl.beejtantra.orderindent.ViewSalesOrderCustomerDetailsActivity;
import com.nsl.beejtantra.pojo.ViewModalAdvBookingPojo;

import java.util.ArrayList;

public class ABSMultiViewTypeAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private int visibility;
    private String approvalStatus;
    private ArrayList<ViewModalAdvBookingPojo> viewModalAdvBookingPojos;
    Activity mContext;
    int total_types;
    MediaPlayer mPlayer;
    private boolean fabStateVolume = false;
    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }

    public class TypeHeaderViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView mSubItemName;


        public TypeHeaderViewHolder(View itemView) {
            super(itemView);

            this.mSubItemName = (TextView) itemView
                    .findViewById(R.id.textViewTitle);


            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }
    }

    public class TypeDataViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {


        TextView itemname, tv_schemedetails, tv_rbs_amount, tv_orderdate, itemcount, tvQunatity/*, tvOrderQuantity, tvRate, historyInfo*/;
        //        EditText et_qty;
        ImageView editView;
        LinearLayout heading;


        public TypeDataViewHolder(View itemView) {
            super(itemView);
            this.itemname = (TextView) itemView.findViewById(R.id.tv_product);
            this.tv_schemedetails = (TextView) itemView.findViewById(R.id.tv_schemedetails);
            this.tv_rbs_amount = (TextView) itemView.findViewById(R.id.tv_amount);
            this.tv_orderdate = (TextView) itemView.findViewById(R.id.tv_orderdate);
            this.itemcount = (TextView) itemView.findViewById(R.id.tv_sr_no);
            this.tvQunatity = (TextView) itemView.findViewById(R.id.tv_qunatity);
            this.editView = itemView.findViewById(R.id.edit_view);
          /*  this.tvOrderQuantity = (TextView) itemView.findViewById(R.id.order_qty);
            this.tvRate = (TextView) itemView.findViewById(R.id.tv_rate);
            this.et_qty = itemView.findViewById(R.id.et_qty);
            this.historyInfo = itemView.findViewById(R.id.history_info);*/


        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }
    }


    public ABSMultiViewTypeAdapter(ArrayList<ViewModalAdvBookingPojo> viewModalAdvBookingPojos, Activity context, String approvalStatus, int visibility) {
        this.viewModalAdvBookingPojos = viewModalAdvBookingPojos;
        this.mContext = context;
        this.approvalStatus = approvalStatus;
        this.visibility = visibility;
        total_types = viewModalAdvBookingPojos.size();

    }


    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {

        View view;
        switch (viewType) {
            case 0:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.row_second, parent, false);
                return new TypeHeaderViewHolder(view);
            case 1:
                view = LayoutInflater.from(parent.getContext()).inflate(R.layout.fnf_item, parent, false);
                return new TypeDataViewHolder(view);

        }
        return null;


    }


    @Override
    public int getItemViewType(int position) {

        switch (viewModalAdvBookingPojos.get(position).type) {
            case 0:
                return 0;
            case 1:
                return 1;
            default:
                return -1;
        }


    }

    @Override
    public void onBindViewHolder(final RecyclerView.ViewHolder holder, final int listPosition) {

        final ViewModalAdvBookingPojo object = viewModalAdvBookingPojos.get(listPosition);

        if (object != null) {
            switch (object.type) {
                case 0:
                    ((TypeHeaderViewHolder) holder).mSubItemName.setText(object.orderDate);

                    break;
                case 1:

                    ((TypeDataViewHolder) holder).itemname.setText(object.customerName);
                    ((TypeDataViewHolder) holder).tv_rbs_amount.setText(object.aBS);
                    ((TypeDataViewHolder) holder).tvQunatity.setText(object.quantity);
                  /*  ((TypeDataViewHolder) holder).tvOrderQuantity.setText(object.orderQuantity);
                    ((TypeDataViewHolder) holder).et_qty.setText(object.quantity);
                    ((TypeDataViewHolder) holder).tvRate.setText(object.rate);*/
                    ((TypeDataViewHolder) holder).tv_schemedetails.setText(object.slabId);
                    ((TypeDataViewHolder) holder).itemcount.setText(String.valueOf(listPosition));

                    if (visibility == View.VISIBLE && approvalStatus != null && approvalStatus.equalsIgnoreCase("0")) {
                        ((TypeDataViewHolder) holder).editView.setVisibility(View.VISIBLE);
                    } else {
                        ((TypeDataViewHolder) holder).editView.setVisibility(View.GONE);

                    }

                   /* ((TypeDataViewHolder) holder).historyInfo.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent historyIntent = new Intent(mContext, HistoryDetailsActivity.class);
                            historyIntent.putExtra("order_detail_id", viewModalAdvBookingPojos.get(listPosition).serviceOrderDetailId);
                            historyIntent.putExtra("type","2");
                            mContext.startActivity(historyIntent);
                        }
                    });*/
                  /*  ((TypeDataViewHolder) holder).et_qty.addTextChangedListener(new TextWatcher() {
                        @Override
                        public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                        }

                        @Override
                        public void afterTextChanged(Editable editable) {
                            if (editable.toString().trim().length() > 0) {
                                int quantity = Integer.parseInt(editable.toString().trim());
                                if (quantity >= 0) {
                                    double total = quantity * Double.parseDouble(viewModalAdvBookingPojos.get(listPosition).rate);
                                    viewModalAdvBookingPojos.get(listPosition).quantity = String.valueOf(quantity);
                                    viewModalAdvBookingPojos.get(listPosition).aBS = String.valueOf(total);
                                }
                            } else {
                                viewModalAdvBookingPojos.get(listPosition).quantity = "0";
                                viewModalAdvBookingPojos.get(listPosition).aBS = "0";
                            }
                            ((TypeDataViewHolder) holder).tv_rbs_amount.setText(viewModalAdvBookingPojos.get(listPosition).aBS);
                            updatedObjects(viewModalAdvBookingPojos);
                        }
                    });*/
                    ((TypeDataViewHolder) holder).editView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            final AlertDialog popup = new AlertDialog.Builder(mContext).create();
                            View dialogView = mContext.getLayoutInflater().inflate(R.layout.product_layout, null);
                            popup.setView(dialogView);
                            popup.show();

                            ImageView closeView = dialogView.findViewById(R.id.close_view);
                            TextView productView = dialogView.findViewById(R.id.product_name);
                            EditText quantityEt = dialogView.findViewById(R.id.et_quantity);
                            TextView unitPrice = dialogView.findViewById(R.id.unit_price);
                            final TextView amountView = dialogView.findViewById(R.id.amount);
                            productView.setText(object.customerName);
                            amountView.setText(object.aBS);
                            quantityEt.setText(object.quantity);
                            quantityEt.setSelection(object.quantity.length());
                            unitPrice.setText(object.rate);

                            closeView.setOnClickListener(new View.OnClickListener() {
                                @Override
                                public void onClick(View view) {
                                    popup.dismiss();
                                }
                            });
                            quantityEt.addTextChangedListener(new TextWatcher() {
                                @Override
                                public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                }

                                @Override
                                public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                                }

                                @Override
                                public void afterTextChanged(Editable editable) {
                                    if (editable.toString().trim().length() > 0) {
                                        int quantity = Integer.parseInt(editable.toString().trim());
                                        if (quantity >= 0) {
                                            double total = quantity * Double.parseDouble(viewModalAdvBookingPojos.get(listPosition).rate);
                                            viewModalAdvBookingPojos.get(listPosition).quantity = String.valueOf(quantity);
                                            viewModalAdvBookingPojos.get(listPosition).aBS = String.valueOf(total);
                                            amountView.setText(String.valueOf(total));

                                        }
                                    } else {
                                        viewModalAdvBookingPojos.get(listPosition).quantity = "0";
                                        viewModalAdvBookingPojos.get(listPosition).aBS = "0";

                                    }
                                    ((TypeDataViewHolder) holder).tvQunatity.setText(viewModalAdvBookingPojos.get(listPosition).quantity);
                                    ((TypeDataViewHolder) holder).tv_rbs_amount.setText(viewModalAdvBookingPojos.get(listPosition).aBS);
                                    updatedObjects(viewModalAdvBookingPojos);

                                }
                            });

                        }
                    });

                    break;

            }
        }

    }

    public void updatedObjects(ArrayList<ViewModalAdvBookingPojo> viewModalAdvBookingPojos) {
    }

    @Override
    public int getItemCount() {
        return viewModalAdvBookingPojos.size();
    }


}