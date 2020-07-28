package com.nsl.beejtantra.network;


import android.app.ProgressDialog;

import com.nsl.beejtantra.pojo.FormResponseResVo;

import java.util.ArrayList;
import java.util.Map;

public interface RetrofitResponseListener {


    void onResponseSuccess(ArrayList<Object> object, Map<String, String> requestParams, int requestId);

    void onFormResponseSuccess(String url,FormResponseResVo formResponseResVo, ProgressDialog dialog);

}
