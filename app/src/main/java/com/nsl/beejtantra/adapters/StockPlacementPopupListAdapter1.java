package com.nsl.beejtantra.adapters;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nsl.beejtantra.R;
import com.nsl.beejtantra.pojo.StockDispatchLineItem;
import com.nsl.beejtantra.pojo.StockPlacementPopupPojo;

import java.util.List;

/**
 * Created by Jithu on 1/12/2016.
 */
public class StockPlacementPopupListAdapter1 extends RecyclerView.Adapter<StockPlacementPopupListAdapter1.ViewHolder> {
    Context context;
    List<StockDispatchLineItem> getStockPlacementPopupPojo;
    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position);
    }


    public StockPlacementPopupListAdapter1(Context context, List<StockDispatchLineItem> objects) {
        this.context = context;
        this.getStockPlacementPopupPojo = objects;
    }


    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int i) {

        View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.stock_placement_item, parent, false);
        ViewHolder vh = new ViewHolder(v);
        v.setTag(i);
        return vh;

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, int position) {


        holder.tvDate.setText(getStockPlacementPopupPojo.get(position).dispatchDate);
        holder.tvQuantity.setText(getStockPlacementPopupPojo.get(position).quantity);
        holder.tvOrderNo.setText(getStockPlacementPopupPojo.get(position).orderSapId);
        // holder.tvDate.setText(AlertsTitleEnum.getPNTitle(getStockPlacementPopupPojo.get(position).pnCode).getPnTitle());
        //  holder.image1.setBackgroundResource(AlertsTitleEnum.getPNTitle(getStockPlacementPopupPojo.get(position).pnCode).getDrawableSelected());


    }

    @Override
    public long getItemId(int i) {
        return getStockPlacementPopupPojo.size();
    }

    @Override
    public int getItemCount() {
        return getStockPlacementPopupPojo.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        TextView tvDate;
        TextView tvQuantity;
        TextView tvOrderNo;
//        ImageButton ivDelete;

        public ViewHolder(final View rowView) {
            super(rowView);

            tvDate = (TextView) rowView.findViewById(R.id.tv_date);
            tvQuantity = (TextView) rowView.findViewById(R.id.tv_qunatity);
            tvOrderNo = rowView.findViewById(R.id.tv_order_no);
//            ivDelete = (ImageButton) rowView.findViewById(R.id.imageButton3);
//
//            ivDelete.setOnClickListener(this);
            rowView.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, getPosition());
            }
        }
    }

}
