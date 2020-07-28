package com.nsl.beejtantra.products;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.nsl.beejtantra.R;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by sowmy on 6/14/2018.
 */

public class ProductCatelogAdapter extends RecyclerView.Adapter<ProductCatelogAdapter.ViewHolder> {
    Context context;
    ArrayList<ProductCatelogPojo> productCatelogPojos;

    public ProductCatelogAdapter(Context context , ArrayList<ProductCatelogPojo> productCatelogPojos) {
        this.context = context;
        this.productCatelogPojos = productCatelogPojos;
    }

    @Override
    public ProductCatelogAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.product_catelog_list,null);
        ViewHolder holder =new ViewHolder(view);
        return holder;
    }

    @Override
    public void onBindViewHolder(ProductCatelogAdapter.ViewHolder holder, int position) {
        holder.hybrid_name.setText(productCatelogPojos.get(position).getHybride_name());
        holder.hybrid_description.setText(productCatelogPojos.get(position).getHybrid_description());
    }

    @Override
    public int getItemCount() {
        return productCatelogPojos.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.hybrid_name)
        TextView hybrid_name;
        @BindView(R.id.hybrid_desciption)
        TextView hybrid_description;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }
}
