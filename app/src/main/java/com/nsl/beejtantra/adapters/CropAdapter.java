package com.nsl.beejtantra.adapters;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.nsl.beejtantra.CropItem;
import com.nsl.beejtantra.Crops;
import com.nsl.beejtantra.R;
import com.nsl.beejtantra.commonutils.Common;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by SupraSoft on 2/22/2019.
 */

public class CropAdapter extends RecyclerView.Adapter<CropAdapter.CropHolder> {
    private Activity context;
    private List<Crops> cropsList;

    public CropAdapter(Activity context, List<Crops> cropsList) {
        this.context = context;
        this.cropsList = cropsList;
    }

    @NonNull
    @Override
    public CropHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        CropHolder cropHolder = new CropHolder(context.getLayoutInflater().inflate(R.layout.crop_item, null));
        return cropHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull final CropHolder cropHolder, final int pos) {
        final int position = pos;
        final Crops crops = cropsList.get(position);
        final String cropName = crops.getCropName();
        cropHolder.cropCheckBox.setText(cropName);
        cropHolder.cropCheckBox.setOnCheckedChangeListener(null);
        cropHolder.cropCheckBox.setChecked(crops.isChecked);
        cropHolder.etFieldArea.setEnabled(crops.isChecked);
//        cropHolder.etFieldArea.setFocusable(crops.isChecked);
        cropHolder.etFieldArea.setCursorVisible(crops.isChecked);
//        cropHolder.etFieldArea.setFocusableInTouchMode(crops.isChecked);
        cropHolder.cropCheckBox.setTag(position);
        cropHolder.etFieldArea.setTag(position);
        if (crops.isChecked) {
            cropHolder.etFieldArea.setText(Common.isStringNull(crops.fieldArea));
        } else {
            cropHolder.etFieldArea.setText("");
        }

        cropHolder.cropCheckBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                int itemPos = (int) cropHolder.cropCheckBox.getTag();
                cropHolder.etFieldArea.setEnabled(b);
                cropHolder.etFieldArea.setFocusable(b);
                cropHolder.etFieldArea.setCursorVisible(b);
                cropHolder.etFieldArea.setFocusableInTouchMode(b);
                cropsList.get(itemPos).isChecked = b;
                setCropItems(cropsList);

            }
        });
        cropHolder.etFieldArea.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void afterTextChanged(Editable editable) {
                int itemPos = (int) cropHolder.etFieldArea.getTag();
                if (editable.toString().trim().length() > 0) {
                    cropsList.get(itemPos).fieldArea = editable.toString().trim();
                }
                setCropItems(cropsList);
            }
        });


    }

    public void updateResults(List<Crops> searchedCrops) {
        this.cropsList = searchedCrops;
        notifyDataSetChanged();
    }

    @Override
    public int getItemCount() {
        return cropsList.size();
    }

    class CropHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.chk_crop)
        CheckBox cropCheckBox;
        @BindView(R.id.et_fieldarea)
        EditText etFieldArea;

        public CropHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }
    }

    public void setCropItems(List<Crops> cropItems) {

    }
}
