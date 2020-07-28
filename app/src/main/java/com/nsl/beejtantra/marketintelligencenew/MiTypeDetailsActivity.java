package com.nsl.beejtantra.marketintelligencenew;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nsl.beejtantra.Constants;
import com.nsl.beejtantra.DatabaseHandler;
import com.nsl.beejtantra.R;
import com.nsl.beejtantra.commonutils.Common;
import com.nsl.beejtantra.pojo.DetailObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MiTypeDetailsActivity extends AppCompatActivity {
    @BindView(R.id.toolbar_title)
    TextView title;
    @BindView(R.id.toolbar)
    Toolbar toolbar;
    @BindView(R.id.fields_layout)
    LinearLayout fieldsLayout;
    private int type, id;
    private DatabaseHandler db;
    private List<DetailObject> itemDetails = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mi_type_details);
        ButterKnife.bind(this);
        db = new DatabaseHandler(this);
        toolbar.setNavigationIcon(getResources().getDrawable(R.drawable.back));
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        type = getIntent().getExtras().getInt("type", 0);
        id = Integer.parseInt(getIntent().getExtras().getString("id", "0"));
        switch (type) {
            case Constants.ViewTypes.MARKET_POTENTIAL:
                itemDetails = db.getMpItemDetailsById(id);

                break;
            case Constants.ViewTypes.CHANNEL_PREFERENCE:
                itemDetails=db.getChannelPreferenceItemDetailsById(id);
                break;
            case Constants.ViewTypes.COMMODITY_PRICE:
                itemDetails = db.getCpItemDetailsById(id);
                break;
            case Constants.ViewTypes.COMPETITOR_CHANNEL:
                itemDetails = db.getCcItemDetailsById(id);
                break;
            case Constants.ViewTypes.COMPETITOR_STRENGTH:
                itemDetails = db.getCsItemDetailsById(id);
                break;
            case Constants.ViewTypes.CROP_SHIFTS:
                itemDetails = db.getCropShiftItemDetailsById(id);
                break;
            case Constants.ViewTypes.PRODUCT_PRICING:
                itemDetails = db.getPpsItemDetailsById(id);
                break;
            default:
                break;
        }
        for (int i = 0; i < itemDetails.size(); i++) {
            View itemView = getLayoutInflater().inflate(R.layout.form_detail_row, null);
            ItemHolder itemHolder = new ItemHolder(itemView);
            itemHolder.label.setText(getLabel(itemDetails.get(i).label));
            itemHolder.value.setText(Common.isStringNull(itemDetails.get(i).value));
        }
    }

    private String getLabel(String label) {
        String[] split = label.split("_");
        StringBuffer stringBuffer = new StringBuffer();
        for (int i = 0; i < split.length; i++) {
            if (!split[i].equalsIgnoreCase("id"))
                stringBuffer.append(split[i].substring(0, 1).toUpperCase() + split[i].substring(1, split[i].length()) + " ");
        }
        return stringBuffer.toString();
    }

    class ItemHolder {
        @BindView(R.id.label)
        TextView label;
        @BindView(R.id.value)
        TextView value;

        public ItemHolder(View itemView) {
            ButterKnife.bind(this, itemView);
            fieldsLayout.addView(itemView);
        }
    }
}
