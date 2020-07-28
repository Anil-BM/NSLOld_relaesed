package com.nsl.beejtantra.distributors;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.nsl.beejtantra.R;
import com.nsl.beejtantra.TargetsActualsActivity;

import java.util.ArrayList;
import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SupraSoft on 1/30/2019.
 */

public class DistibutorDetailsFragment extends Fragment {
    private String customerId;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> below90 = new ArrayList<HashMap<String, String>>();
    ArrayList<HashMap<String, String>> above90 = new ArrayList<HashMap<String, String>>();
    public static final String mypreference = "mypref";
    int pos = 0;
    @BindView(R.id.listView1)
    ListView listView;
    @BindView(R.id.tv_alert)
    TextView tvAlert;
    @BindView(R.id.go_to_targets)
    Button goToTargets;
    private ItemfavitemAdapter adapter;
//    private ArrayList<HashMap<String, String>> items = new ArrayList<>();

    public DistibutorDetailsFragment(ArrayList<HashMap<String, String>> favouriteItem, String customer_id) {
        this.favouriteItem = favouriteItem;
        this.customerId = customer_id;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = getLayoutInflater().inflate(R.layout.fragment_distributor_details, null);
        ButterKnife.bind(this, rootView);
//        pos = getArguments().getInt("pos", 0);
//        if (pos == 0) {
//        for (int j = 0; j < favouriteItem.size(); j++) {
//            if (favouriteItem.get(j).get("Aging1").equalsIgnoreCase("<")) {
//                below90.add(favouriteItem.get(j));
//
//            } else {
//                above90.add(favouriteItem.get(j));
//            }
//        }
//        } else {
//            for (int j = 0; j < favouriteItem.size(); j++) {
//                if (favouriteItem.get(j).get("Aging1").equalsIgnoreCase(">"))
//                    items.add(favouriteItem.get(j));
//            }
//        }
       /* if (below90.size() > 0) {

            adapter = new ItemfavitemAdapter(getActivity(), below90);
            listView.setAdapter(adapter);
        } else {
            tvAlert.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }*/
        if (favouriteItem.size() > 0) {
            adapter = new ItemfavitemAdapter(getActivity(), favouriteItem);
            listView.setAdapter(adapter);
        } else {
            tvAlert.setVisibility(View.VISIBLE);
            listView.setVisibility(View.GONE);
        }

        goToTargets.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent targetsIntent = new Intent(getActivity(), TargetsActualsActivity.class);
                targetsIntent.putExtra("customer_id", customerId);
                startActivity(targetsIntent);

            }
        });
        return rootView;
    }

    class ItemfavitemAdapter extends BaseAdapter {

        Activity context;
        ArrayList<HashMap<String, String>> results = new ArrayList<HashMap<String, String>>();


        public ItemfavitemAdapter(Activity pd, ArrayList<HashMap<String, String>> results) {
            this.context = pd;
            this.results = results;

        }

        @Override
        public int getCount() {
            return results.size();
        }

        @Override
        public Object getItem(int position) {
            return results;
        }

        @Override
        public long getItemId(int position) {
            return 0;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            ItemfavitemAdapter.ViewHolder holder = new ItemfavitemAdapter.ViewHolder();
            if (convertView == null) {
//                sharedpreferences = context.getSharedPreferences(mypreference, Context.MODE_PRIVATE);

                LayoutInflater inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
                convertView = inflater.inflate(R.layout.row_distributor_details, parent, false);
                holder.tv_sr = (TextView) convertView.findViewById(R.id.tv_sr);
                holder.tv_division = (TextView) convertView.findViewById(R.id.tv_div);
                holder.tv_osa = (TextView) convertView.findViewById(R.id.tv_osa);
                holder.tv_credit_limit = (TextView) convertView.findViewById(R.id.tv_credit_limit);
                holder.headerLayout = convertView.findViewById(R.id.header_layout);

                //  holder.tv_aging1             = (TextView)convertView.findViewById(R.id.tv_aging1);
                holder.img = (ImageView) convertView.findViewById(R.id.img);
//                if (aging1 != null && aging1.equals("<")) {
//                    holder.img.setImageResource(R.drawable.less);
//                } else if (aging1 != null && aging1.equals(">")) {
//                    holder.img.setImageResource(R.drawable.greater);
//                }
                holder.tv_aging2 = (TextView) convertView.findViewById(R.id.tv_aging2);
                convertView.setTag(holder);
            } else {
                holder = (ItemfavitemAdapter.ViewHolder) convertView.getTag();
            }
            //Glide.with(context).load(results.get(position).get("image")).diskCacheStrategy(DiskCacheStrategy.ALL).into(holder.pic);
            //holder.tv_sr.setText(results.get(position).get("sr"));
            holder.tv_division.setText(results.get(position).get("division"));
            holder.tv_osa.setText(results.get(position).get("inside_bucket"));
            //holder.tv_aging1.setText(results.get(position).get("Aging1"));
            holder.tv_aging2.setText(results.get(position).get("outside_bucket"));
            holder.tv_credit_limit.setText(results.get(position).get("credit_limit"));
            if (position == 0)
                holder.headerLayout.setVisibility(View.VISIBLE);
            else
                holder.headerLayout.setVisibility(View.GONE);


            return convertView;
        }


        public class ViewHolder {
            public LinearLayout headerLayout;
            public TextView tv_sr, tv_division, tv_osa, tv_aging2, tv_credit_limit;
            public ImageView img;

        }

        public void updateResults(ArrayList<HashMap<String, String>> results) {

            this.results = results;
            notifyDataSetChanged();
        }
    }
}
