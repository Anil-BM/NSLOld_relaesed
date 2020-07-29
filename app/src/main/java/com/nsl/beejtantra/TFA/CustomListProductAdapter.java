package com.nsl.beejtantra.TFA;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.nsl.beejtantra.Products;
import com.nsl.beejtantra.R;

import java.util.ArrayList;
import java.util.List;

public class CustomListProductAdapter extends ArrayAdapter {
    private List<Products> dataList;
    private Context mContext;
    private int itemLayout;

    private ListFilter listFilter = new ListFilter();
    private List<Products> dataListAllItems;
    private CustomListProductAdapter.OnItemClickListener mListener;




    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mListener = (OnItemClickListener) onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, Products item);
    }

    public CustomListProductAdapter(Context context, int resource, List<Products> storeDataLst) {
        super(context, resource, storeDataLst);
        dataList = storeDataLst;
        mContext = context;
        itemLayout = resource;
    }

    @Override
    public int getCount() {
        return dataList.size();
    }

    @Override
    public Products getItem(int position) {
    /*    Log.d("CustomListAdapter",
                dataList.get(position).getProductMasterId());*/
        return dataList.get(position);
    }

    @Override
    public View getView(final int position, View view, @NonNull ViewGroup parent) {

        if (view == null) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(itemLayout, parent, false);
        }

        TextView strName = (TextView) view.findViewById(R.id.textView);
        strName.setText(getItem(position).getProductName());
        strName.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mListener.onItemClick(v,position,getItem(position));
            }
        });
        return view;
    }

    @NonNull
    @Override
    public Filter getFilter() {
        Log.d("performFiltering", String.valueOf("getFilter()"));
        return listFilter;
    }
    public class ListFilter extends Filter {
        private Object lock = new Object();

        @Override
        protected FilterResults performFiltering(CharSequence prefix) {
            Log.d("performFiltering", String.valueOf(prefix));
            FilterResults results = new FilterResults();
            if (dataListAllItems == null) {
                synchronized (lock) {
                    dataListAllItems = new ArrayList<Products>(dataList);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                synchronized (lock) {
                    results.values = dataListAllItems;
                    results.count = dataListAllItems.size();
                }
            } else {
                final String searchStrLowerCase = prefix.toString().toLowerCase();

                ArrayList<Products> matchValues = new ArrayList<>();

                for (Products dataItem : dataListAllItems) {
                    if (dataItem.getProductName().toLowerCase().startsWith(searchStrLowerCase)) {
                        matchValues.add(dataItem);
                    }
                }

                results.values = matchValues;
                results.count = matchValues.size();
            }

            return results;
        }

        @Override
        protected void publishResults(CharSequence constraint, FilterResults results) {
            if (results.values != null) {
                dataList = (ArrayList<Products>)results.values;
            } else {
                dataList = null;
            }
            if (results.count > 0) {
                notifyDataSetChanged();
            } else {
                notifyDataSetInvalidated();
            }
        }

    }
}
