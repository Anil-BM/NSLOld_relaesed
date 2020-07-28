package com.nsl.beejtantra.orderindent;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.nsl.beejtantra.DatabaseHandler;
import com.nsl.beejtantra.R;
import com.nsl.beejtantra.commonutils.Common;
import com.nsl.beejtantra.pojo.ServiceOrderHistory;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SupraSoft on 3/5/2019.
 */

public class ProductAdapter extends BaseAdapter {
    private Activity context;
    List<ServiceOrderHistory> orderHistories;
    DatabaseHandler db;
    private ProductHolder productHolder;

    public ProductAdapter(Activity context, List<ServiceOrderHistory> serviceOrderHistories) {
        db = new DatabaseHandler(context);
        this.context = context;
        this.orderHistories = serviceOrderHistories;
    }

  /*  @NonNull
    @Override
    public ProductHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        ProductHolder productHolder = new ProductHolder(context.getLayoutInflater().inflate(R.layout.prouduct_row, null));
        return productHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull ProductHolder productHolder, int i) {
        productHolder.product.setText(Common.isStringNull(db.getProductNametByProductId(orderHistories.get(i).productId).toUpperCase()));
        productHolder.oQty.setText(Common.isStringNull(orderHistories.get(i).orderQuantity));
        productHolder.aQty.setText(Common.isStringNull(orderHistories.get(i).quantity));

    }

    @Override
    public int getItemCount() {
        return orderHistories.size();
    }*/

    @Override
    public int getCount() {
        return orderHistories.size();
    }

    @Override
    public Object getItem(int i) {
        return orderHistories.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            view = context.getLayoutInflater().inflate(R.layout.prouduct_row, null);
            productHolder = new ProductHolder(view);
            view.setTag(productHolder);
        }else {
            productHolder = (ProductHolder) view.getTag();
        }
        productHolder.product.setText(Common.isStringNull(db.getProductNametByProductId(orderHistories.get(i).productId).toUpperCase()));
        productHolder.oQty.setText(Common.isStringNull(orderHistories.get(i).orderQuantity));
        productHolder.aQty.setText(Common.isStringNull(orderHistories.get(i).quantity));
        return view;
    }

    class ProductHolder /*extends RecyclerView.ViewHolder*/ {
        @BindView(R.id.product)
        TextView product;
        @BindView(R.id.o_qty)
        TextView oQty;
        @BindView(R.id.a_qty)
        TextView aQty;

        public ProductHolder(@NonNull View itemView) {
//            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }
}
