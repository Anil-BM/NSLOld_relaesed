package com.nsl.beejtantra.adapters;

import android.content.Context;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.nsl.beejtantra.R;
import com.nsl.beejtantra.product_catalogue.CatalogueCropsPojo;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by User on 6/14/2018.
 */
public class InfoAdapter extends RecyclerView.Adapter<InfoAdapter.MyViewHolder> {

    Context context;
   List<CatalogueCropsPojo> infoPojos;


    private OnItemClickListener mListener;


    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mListener=onItemClickListener;
    }

    public interface OnItemClickListener{
        void onItemClick(View view, TextView textView, int position);
    }


    public InfoAdapter(Context context, List<CatalogueCropsPojo> homePagePojo) {

        this.context = context;
        this.infoPojos=  homePagePojo;
    }




    @Override
    public MyViewHolder  onCreateViewHolder(ViewGroup parent, int viewType) {

       View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.info_row, parent, false);
      //  Common.Log.i("Widht:"+view.getWidth()+", Hight: "+view.getHeight());
        GridLayoutManager.LayoutParams lp = (GridLayoutManager.LayoutParams) view.getLayoutParams();
        lp.height = parent.getMeasuredWidth() / 2;
        lp.width = parent.getMeasuredWidth() / 2;
        view.setLayoutParams(lp);
        MyViewHolder myViewHolder = new MyViewHolder(view);
        return myViewHolder;
    }

    @Override
    public void onBindViewHolder(MyViewHolder holder, final int position) {
        if (infoPojos.get(position).imgURI!=null)
            Picasso.with(context).load(new File(infoPojos.get(position).imgURI)).into(holder.images);
        else
            holder.images.setImageDrawable(context.getResources().getDrawable(R.mipmap.ic_maize));
            holder.name.setText(infoPojos.get(position).cropName);
         //  Common.Log.i(", Hight: "+Common.getViewWidth(holder.menu_id));



    }




    @Override
    public int getItemCount()
    {
        return infoPojos.size();
    }


    public class MyViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.text)
        TextView name;
        @BindView(R.id.image)
        ImageView images;

        @BindView(R.id.menu_id)
        RelativeLayout menu_id;


        public MyViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);

            menu_id.setOnClickListener(this);
            images.setOnClickListener(this);
            name.setOnClickListener(this);

        }

        @Override
        public void onClick(View v) {
            if (mListener != null){
                mListener.onItemClick(v,name,getPosition());
            }
        }
    }
}