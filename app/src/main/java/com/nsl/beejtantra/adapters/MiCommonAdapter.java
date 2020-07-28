package com.nsl.beejtantra.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nsl.beejtantra.Constants;
import com.nsl.beejtantra.R;
import com.nsl.beejtantra.commonutils.Common;
import com.nsl.beejtantra.marketintelligencenew.AdapterModel;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SupraSoft on 2/19/2019.
 */

public class MiCommonAdapter extends BaseAdapter {
    private Activity context;
    private List<AdapterModel> adapterModels;
    MiHolder miHolder;
    private int type;

    public MiCommonAdapter(Activity context, List<AdapterModel> adapterModelList, int type) {
        this.context = context;
        this.adapterModels = adapterModelList;
        this.type = type;
    }

    @Override
    public int getCount() {
        return adapterModels.size();
    }

    @Override
    public Object getItem(int i) {
        return adapterModels.get(i);
    }

    @Override
    public long getItemId(int i) {
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        View convertView = context.getLayoutInflater().inflate(R.layout.mi_list_row, null);
        if (convertView.getTag() == null) {
            miHolder = new MiHolder(convertView);
            convertView.setTag(miHolder);
        }
        if (type == Constants.ViewTypes.COMPETITOR_CHANNEL || type == Constants.ViewTypes.COMPETITOR_STRENGTH) {
            miHolder.bottomLayout.setVisibility(View.GONE);
            miHolder.villageLabel.setText("District");
            miHolder.divisionLabel.setText("Territory");
            miHolder.tvVillage.setText(Common.isStringNull(adapterModels.get(i).district));
            miHolder.tvDivision.setText(Common.isStringNull(adapterModels.get(i).territory));
        } else if (type == Constants.ViewTypes.CHANNEL_PREFERENCE) {
            miHolder.bottomLayout.setVisibility(View.GONE);
            miHolder.villageLabel.setText("Distributor");
            miHolder.divisionLabel.setText("Crop");
            miHolder.tvVillage.setText(Common.isStringNull(adapterModels.get(i).distributor));
            miHolder.tvDivision.setText(Common.isStringNull(adapterModels.get(i).crop));
        } else {
            miHolder.bottomLayout.setVisibility(View.VISIBLE);
            miHolder.tvVillage.setText(Common.isStringNull(adapterModels.get(i).village));
            miHolder.tvCrop.setText(Common.isStringNull(adapterModels.get(i).crop));
            miHolder.tvDivision.setText(Common.isStringNull(adapterModels.get(i).division));
        }

        return convertView;
    }

    class MiHolder {
        @BindView(R.id.bottom_layout)
        LinearLayout bottomLayout;
        @BindView(R.id.village_label)
        TextView villageLabel;
        @BindView(R.id.crop_label)
        TextView cropLabel;
        @BindView(R.id.division_lable)
        TextView divisionLabel;
        @BindView(R.id.village)
        TextView tvVillage;
        @BindView(R.id.division)
        TextView tvDivision;
        @BindView(R.id.crop)
        TextView tvCrop;


        public MiHolder(View convertView) {
            ButterKnife.bind(this, convertView);
        }
    }

}
