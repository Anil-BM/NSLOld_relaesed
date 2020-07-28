package com.nsl.beejtantra.network;

import android.app.Activity;
import android.app.Fragment;
import android.app.ProgressDialog;
import android.content.Context;
import android.support.v4.content.WakefulBroadcastReceiver;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.nsl.beejtantra.Constants;
import com.nsl.beejtantra.MyApplication;
import com.nsl.beejtantra.commonutils.Common;
import com.nsl.beejtantra.pojo.FormResponseResVo;

import org.apache.commons.cli.CommandLine;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.ResponseBody;
import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit2.Call;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

public class RetrofitRequester {
    private final RetrofitResponseListener retrofitResponseListener;
    private Activity activity;
    private Context context;
    private ProgressDialog progressDialog;

    public RetrofitRequester(RetrofitResponseListener retrofitResponseListener) {

        this.retrofitResponseListener = retrofitResponseListener;

        if (retrofitResponseListener instanceof Activity) {
            this.context = (Context) retrofitResponseListener;
            this.activity = (Activity) retrofitResponseListener;
        } else if (retrofitResponseListener instanceof Fragment) {
            this.context = ((Fragment) retrofitResponseListener).getActivity();
            this.activity = ((Fragment) retrofitResponseListener).getActivity();

        } else if (retrofitResponseListener instanceof android.support.v4.app.Fragment) {
            this.context = ((android.support.v4.app.Fragment) retrofitResponseListener).getActivity();
            this.activity = ((android.support.v4.app.Fragment) retrofitResponseListener).getActivity();

        } else if (retrofitResponseListener instanceof WakefulBroadcastReceiver) {
            this.context = (Context) retrofitResponseListener;
            this.activity = (Activity) retrofitResponseListener;

        }

    }

    public RetrofitRequester(Context context, RetrofitResponseListener retrofitResponseListener1) {

        this.retrofitResponseListener = retrofitResponseListener1;
        this.context = context;
    }

  /*  public void sendStringRequest(String requestName, HashMap<String, String> requestParams) {

        sendStringRequest(requestName, requestParams, -1);

    }
*/

/*
    public void sendStringRequest(String requestName, final HashMap<String, String> requestParams, final int requestId, boolean showProgressDialog) {


        if (requestId == Constants.RequestCodes.ONCREATE_REQUEST_CODE) {
            Common.showContentView((Activity) context, false);
        }

        if (!Common.haveInternet(context)) {

            Common.customToast(context, Constants.INTERNET_UNABLEABLE);

            return;

        }

        if (showProgressDialog) {

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialog = Common.showProgressDialog(context);
                }
            });


        }


        CommonRequestKey commonRequestKey = new CommonRequestKey();
        commonRequestKey.setRequesterid(String.valueOf(Common.getUserIdFromSP(context)));
        commonRequestKey.setRequestname(requestName);
        commonRequestKey.setRequestparameters(new Gson().toJson(requestParams));
        Common.Log.i("commonRequestKey.toString() : " + commonRequestKey.toString());

        Common.Log.i("? - API Request : "+new Gson().toJson(commonRequestKey));


        WAYALERTS.getInstance().getRetrofitAPI().callApiService(commonRequestKey, new Callback<Object>() {
            @Override
            public void success(Object object, Response response) {

                Common.Log.i("Inside Retrofit Success of " + getClass().getSimpleName());

                Common.Log.i("? - API Response : " + new Gson().toJson(object));

                Common.Log.i("response : " + response);

                retrofitResponseListener.onResponseSuccess(object, requestParams, requestId);

                if (requestId == Constants.RequestCodes.ONCREATE_REQUEST_CODE) {
                    Common.showContentView((Activity) context, true);
                }

                Common.dismissProgressDialog(progressDialog);

            }

            @Override
            public void failure(RetrofitError error) {

                error.printStackTrace();

                Common.Log.i("error.toString() : " + error.toString());

                Common.dismissProgressDialog(progressDialog);


            }

        });


    }
*/

/*

    public void sendStringRequest(String requestName, HashMap<String, String> requestParams, boolean showProgressDialog) {

        sendStringRequest(requestName, requestParams, -1, showProgressDialog);

    }


    public void sendStringRequest(String requestName, HashMap<String, String> requestParams, final int requestId) {
        sendStringRequest(requestName, requestParams, requestId, true);
    }
*/



    /*public void sendMultiPartRequest(String requestName, final HashMap<String, String> requestParams, HashMap<String, TypedFile> filesHashMap)
    {

        if (!Common.haveInternet(context))
        {

            Common.customToast(context, Constants.INTERNET_UNABLEABLE);

            return;

        }


        progressDialog = Common.showProgressDialog(context);




        CommonRequestKey commonRequestKey = new CommonRequestKey();
        commonRequestKey.setRequesterid(String.valueOf(Common.getUserIdFromSP(context)));
        commonRequestKey.setRequestname(requestName);
        commonRequestKey.setRequestparameters(new Gson().toJson(requestParams));

        Common.Log.i("? - Mupltipart Request : "+new Gson().toJson(commonRequestKey));




        WAYALERTS.getInstance().getRetrofitAPI().uploadImages( "1", commonRequestKey,filesHashMap, new Callback<ApiResponseVo>() {
            @Override
            public void success(ApiResponseVo apiResponseVo, Response response)
            {
                Common.Log.i("Inside Retrofit Success of " + getClass().getSimpleName());

                Common.Log.i("? - Mupltipart Response : " + new Gson().toJson(apiResponseVo));

                Common.Log.i("response : " + response);

                retrofitResponseListener.onResponseSuccess(apiResponseVo, requestParams, -1);

                Common.dismissProgressDialog(progressDialog);

            }

            @Override
            public void failure(RetrofitError error) {
                error.printStackTrace();

                Common.Log.i("error.toString() : " + error.toString());

                Common.dismissProgressDialog(progressDialog);

            }
        });





    }*/

    public void sendRequestofdemandgeneration(final String path, final Object obj, boolean showProgressDialog) {

        if (!Common.haveInternet(context)) {

            Common.customToast(context, Constants.INTERNET_UNABLEABLE);

            return;

        }
        /*if (showProgressDialog) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialog = Common.showProgressDialog(context);
                    progressDialog.setCancelable(false);
                }
            });
        }*/
        Common.Log.i("request Anil" + new Gson().toJson(obj));
        MyApplication.getInstance().getRetrofitAPI().postFormData(path, obj).enqueue(new retrofit2.Callback<FormResponseResVo>() {
            @Override
            public void onResponse(Call<FormResponseResVo> call, retrofit2.Response<FormResponseResVo> response) {
                try {
                    Common.Log.i("Response: " + new Gson().toJson(response.body()));
                    if (response.body() == null) {
                        Common.dismissProgressDialog(progressDialog);
                        Toast.makeText(context, "Intenal Server Error Anil", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else {

                    }
                    retrofitResponseListener.onFormResponseSuccess(path, response.body(), progressDialog);
                    // Common.Log.i("Response: absajk");
                } catch (Exception e) {
                    Common.Log.i("exception Anil" + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<FormResponseResVo> call, Throwable t) {
                Common.Log.i("retrofit Anil" + t.toString());
                Common.dismissProgressDialog(progressDialog);
            }
        });

    }








    public void sendRequestoffarmercoupon(final String path, final Object obj, boolean showProgressDialog) {

        if (!Common.haveInternet(context)) {

            Common.customToast(context, Constants.INTERNET_UNABLEABLE);

            return;

        }

        Common.Log.i("request Anil2" + new Gson().toJson(obj));
        MyApplication.getInstance().getRetrofitAPI().postFormData(path, obj).enqueue(new retrofit2.Callback<FormResponseResVo>() {
            @Override
            public void onResponse(Call<FormResponseResVo> call, retrofit2.Response<FormResponseResVo> response) {
                try {
                    Common.Log.i("Response2: " + new Gson().toJson(response.body()));
                    if (response.body() == null) {
                        Common.dismissProgressDialog(progressDialog);
                        Toast.makeText(context, "Intenal Server Error Anil2", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    else {

                    }
                    retrofitResponseListener.onFormResponseSuccess(path, response.body(), progressDialog);
                    // Common.Log.i("Response: absajk");
                } catch (Exception e) {
                    Common.Log.i("exception Anil2" + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<FormResponseResVo> call, Throwable t) {
                Common.Log.i("retrofit Anil2" + t.toString());
                Common.dismissProgressDialog(progressDialog);
            }
        });

    }


















    public void sendRequest(final String path, final Object obj, boolean showProgressDialog) {

        if (!Common.haveInternet(context)) {

            Common.customToast(context, Constants.INTERNET_UNABLEABLE);

            return;

        }
        if (showProgressDialog) {
            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialog = Common.showProgressDialog(context);
                    progressDialog.setCancelable(false);
                }
            });
        }
        Common.Log.i("request " + new Gson().toJson(obj));
        MyApplication.getInstance().getRetrofitAPI().postFormData(path, obj).enqueue(new retrofit2.Callback<FormResponseResVo>() {
            @Override
            public void onResponse(Call<FormResponseResVo> call, retrofit2.Response<FormResponseResVo> response) {
                try {
                    Common.Log.i("Response: " + new Gson().toJson(response.body()));
                    if (response.body() == null) {
                        Common.dismissProgressDialog(progressDialog);
                        Toast.makeText(context, "Intenal Server Error", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    retrofitResponseListener.onFormResponseSuccess(path, response.body(), progressDialog);
                    // Common.Log.i("Response: absajk");
                } catch (Exception e) {
                    Common.Log.i("exception " + e.getMessage());
                }
            }

            @Override
            public void onFailure(Call<FormResponseResVo> call, Throwable t) {
                Common.Log.i("retrofit " + t.toString());
                Common.dismissProgressDialog(progressDialog);
            }
        });

    }


    public void callGetApi(final Map<String, String> map) {

        MyApplication.getInstance().getRetrofitAPI().callGetAPIS(map).enqueue(new retrofit2.Callback<ArrayList<Object>>() {
            @Override
            public void onResponse(Call<ArrayList<Object>> call, retrofit2.Response<ArrayList<Object>> response) {
                if (response.body() != null) {
                    Log.d("retrofit", response.body().toString());
                    retrofitResponseListener.onResponseSuccess(response.body(), map, -1);
                }
            }

            @Override
            public void onFailure(Call<ArrayList<Object>> call, Throwable t) {
                Log.d("retrofit", t.toString());
            }
        });
    }


/*    public void callPostLoginApi() {

        MyApplication.getInstance().getRetrofitAPI().login("9581440514", "password", "password12121212", new Callback<Object>() {
            @Override
            public void success(Object o, Response response) {
                Log.d("retrofit", o.toString());

                // retrofitResponseListener.onResponseSuccess(o,map,-1);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.d("retrofit", error.toString());
            }
        });
    }*/

    public void sendGetRequest() {
        sendGetRequest(null, true);
    }

    public void sendGetRequest(final Map<String, String> map, boolean showProgressDialog) {

        if (!Common.haveInternet(context)) {

            Common.customToast(context, Constants.INTERNET_UNABLEABLE);

            return;

        }


        if (showProgressDialog) {

            activity.runOnUiThread(new Runnable() {
                @Override
                public void run() {
                    progressDialog = Common.showProgressDialog(context);
                    progressDialog.setCancelable(false);
                }
            });


        }

       /* final ApiRequestController apiRequestController = new ApiRequestController();
        apiRequestController.requesterid = Common.getUserIdFromSP(context);
        //  apiRequestController.requesterid =36;
        apiRequestController.requestname = requestName;
        apiRequestController.requestparameters = obj;*/

        //   Common.Log.i("request :"+ new Gson().toJson(apiRequestController));

//        MyApplication.getInstance().getRetrofitAPI().callGetCatalogue(map).subscribeOn(Schedulers.newThread())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<Object>() {
//                    @Override
//                    public void onCompleted() {
//                        Common.dismissProgressDialog(progressDialog);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        Common.dismissProgressDialog(progressDialog);
//                        Toast.makeText(context, "Internal Server Error!", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onNext(Object o) {
//
//                    }
//                });


    }
}
