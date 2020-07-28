package com.nsl.beejtantra.adapters;

import android.app.Activity;
import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;

import com.nsl.beejtantra.ButtonPlus;
import com.nsl.beejtantra.ComplaintNewFormActivity;
import com.nsl.beejtantra.R;
import com.nsl.beejtantra.commonutils.CircularImageViewWithBoarder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by User on 12/14/2018.
 */

public class ComplaintImagesAdapter extends RecyclerView.Adapter<ComplaintImagesAdapter.MyViewHolder> {


    private OnItemClickListener mListener;
    private Activity activity;

    public ComplaintImagesAdapter(Activity activity) {
        this.activity = activity;
    }


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, View viewItem, int position);
    }


    @NonNull
    @Override
    public MyViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(activity).inflate(R.layout.add_imageview, null);
        MyViewHolder viewHolder = new MyViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyViewHolder myViewHolder, int i) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.ivImage)
        CircularImageViewWithBoarder ivImage;
        @BindView(R.id.btndelete)
        Button btndelete;

        public MyViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
            ivImage.setOnClickListener(this);
            btndelete.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                mListener.onItemClick(v, itemView, getPosition());
            }
        }
    }
}
