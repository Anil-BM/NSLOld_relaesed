package com.nsl.beejtantra.view;

import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.support.annotation.NonNull;
import android.support.design.widget.TabLayout;
import android.support.v4.view.PagerAdapter;
import android.support.v7.widget.AppCompatTextView;
import android.text.Html;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.ViewGroup;

/**
 * Created by SupraSoft on 11/17/2017.
 */

public class CustomTabLayout extends TabLayout {
    public CustomTabLayout(Context context) {
        super(context);
    }

    public CustomTabLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public CustomTabLayout(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @Override
    public void setTabsFromPagerAdapter(@NonNull PagerAdapter adapter) {
        Typeface typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/SEGOEWP-SEMIBOLD.TTF");

        this.removeAllTabs();

        ViewGroup slidingTabStrip = (ViewGroup) getChildAt(0);

        for (int i = 0, count = adapter.getCount(); i < count; i++) {
            Tab tab = this.newTab();
            String text = "<small>" + adapter.getPageTitle(i).toString() + "</small>";
            this.addTab(tab.setText(Html.fromHtml(text)));
            AppCompatTextView view = (AppCompatTextView) ((ViewGroup) slidingTabStrip.getChildAt(i)).getChildAt(1);
//           view.setTextSize(TypedValue.COMPLEX_UNIT_DIP,14);
//           view.getAutoSizeMinTextSize()
//            view.setShadowLayer(
//                    1.5f, // radius
//                    5.0f, // dx
//                    5.0f, // dy
//                    Color.parseColor("#FF3D803D") // shadow color
//            );
            view.setTypeface(typeface, Typeface.NORMAL);

        }
    }
}