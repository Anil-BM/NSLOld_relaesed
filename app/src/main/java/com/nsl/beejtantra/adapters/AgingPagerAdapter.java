//package com.nsl.beejtantra.adapters;
//
//import android.os.Bundle;
//import android.support.annotation.NonNull;
//import android.support.annotation.Nullable;
//import android.support.v4.app.Fragment;
//import android.support.v4.app.FragmentManager;
//import android.support.v4.app.FragmentStatePagerAdapter;
//
//import com.nsl.beejtantra.distributors.DistibutorDetailsFragment;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//
///**
// * Created by SupraSoft on 1/30/2019.
// */
//
//public class AgingPagerAdapter extends FragmentStatePagerAdapter {
//    private ArrayList<HashMap<String, String>> favouriteItem;
//    String[] TITLES = new String[]{"Below 90 Days", "Above 90 Days"};
//
//    public AgingPagerAdapter(FragmentManager fm, ArrayList<HashMap<String, String>> favouriteItem) {
//        super(fm);
//        this.favouriteItem = favouriteItem;
//    }
//
//
//    @Override
//    public Fragment getItem(int i) {
//        DistibutorDetailsFragment distibutorDetailsFragment = new DistibutorDetailsFragment(favouriteItem, customer_id);
//        Bundle bundle = new Bundle();
//        bundle.putInt("pos", i);
//        distibutorDetailsFragment.setArguments(bundle);
//        return distibutorDetailsFragment;
//    }
//
//    @Override
//    public int getCount() {
//        return TITLES.length;
//    }
//
//    @Override
//    public int getItemPosition(@NonNull Object object) {
//        return POSITION_NONE;
//    }
//
//    @Nullable
//    @Override
//    public CharSequence getPageTitle(int position) {
//        return TITLES[position];
//    }
//}
