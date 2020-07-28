package com.nsl.beejtantra.adapters;

import android.content.Context;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Filter;
import android.widget.TextView;

import com.nsl.beejtantra.R;
import com.nsl.beejtantra.SelectedCities;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by suprasoft on 4/9/2018.
 */

public class CustomListAdapter extends ArrayAdapter {

    private List<SelectedCities> dataList;
    private Context mContext;
    private int itemLayout;

    private ListFilter listFilter = new ListFilter();
    private List<SelectedCities> dataListAllItems;
    private OnItemClickListener mListener;

    public void setOnItemClickListener(OnItemClickListener onItemClickListener) {
        mListener = onItemClickListener;
    }

    public interface OnItemClickListener {
        void onItemClick(View view, int position, SelectedCities item);
    }



    public CustomListAdapter(Context context, int resource, List<SelectedCities> storeDataLst) {
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
    public SelectedCities getItem(int position) {
        Log.d("CustomListAdapter",
                dataList.get(position).getCityId());
        return dataList.get(position);
    }

    @Override
    public View getView(final int position, View view, @NonNull ViewGroup parent) {

        if (view == null) {
            view = LayoutInflater.from(parent.getContext())
                    .inflate(itemLayout, parent, false);
        }

        TextView strName = (TextView) view.findViewById(R.id.textView);
        strName.setText(getItem(position).getCityName());
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
                    dataListAllItems = new ArrayList<SelectedCities>(dataList);
                }
            }

            if (prefix == null || prefix.length() == 0) {
                synchronized (lock) {
                    results.values = dataListAllItems;
                    results.count = dataListAllItems.size();
                }
            } else {
                final String searchStrLowerCase = prefix.toString().toLowerCase();

                ArrayList<SelectedCities> matchValues = new ArrayList<>();

                for (SelectedCities dataItem : dataListAllItems) {
                    if (dataItem.getCityName().toLowerCase().startsWith(searchStrLowerCase)) {
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
                dataList = (ArrayList<SelectedCities>)results.values;
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