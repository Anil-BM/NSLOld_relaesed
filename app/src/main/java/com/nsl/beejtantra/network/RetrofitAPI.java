package com.nsl.beejtantra.network;


import com.nsl.beejtantra.RetroResponses.resmain_farmer_coupons_detail;
import com.nsl.beejtantra.TFA.DemandgenerationGetResults;
import com.nsl.beejtantra.TFA.res.res_tfa_activities_masterd;
import com.nsl.beejtantra.pojo.FormResponseResVo;
import com.nsl.beejtantra.pojo.GodownResVo;
import com.nsl.beejtantra.pojo.RetailerStockReqVo;
import com.nsl.beejtantra.pojo.RetailerStockResVo;
import com.nsl.beejtantra.pojo.SeasonResVo;
import com.nsl.beejtantra.pojo.StockDispatchResp;
import com.nsl.beejtantra.pojo.TargetActualResVo;
import com.nsl.beejtantra.product_catalogue.CatalogueListVo;

import java.util.ArrayList;
import java.util.Map;

import okhttp3.MultipartBody;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;
import retrofit2.http.Streaming;
import retrofit2.http.Url;
import rx.Observable;


public interface RetrofitAPI {
    /*@POST("/requestprocessor/requestProcessing")
    void callApiService(@Body CommonRequestKey login, Callback<Object> response);

    @Multipart
    @POST("/api?api_token=tatx123")
    void upload(@Part("image") TypedFile file,
                @Part("multipart") String multipart,
                @Part("requestparameters") CommonRequestKey requestparameters,
                Callback<RestResponse> response);


    @Multipart
    @POST("/api?api_token=tatx123")
    void uploadImages(
            @Part("multipart") String multipart,
            @Part("requestparameters") CommonRequestKey requestparameters,
            @PartMap Map<String, TypedFile> files,
            Callback<ApiResponseVo> response);
*/

    /*@GET("/needtosync")
    void callGetAPIS(@FieldMap Map<String, String> params,Callback<Object> response);
*/
    @retrofit2.http.GET("details")
    Call<ArrayList<Object>> callGetAPIS(@retrofit2.http.QueryMap Map<String, String> params);

    @retrofit2.http.Headers({"Authorization: Basic cmVzdDpzZWVkc0BhZG1pbg==", "Cache-Control: no-cache"})
    @POST("/details")
    Call<ArrayList<Object>> acknowledgeToServerAPIS(@retrofit2.http.QueryMap Map<String, String> params);

    @retrofit2.http.Headers({"Authorization: Basic cmVzdDpzZWVkc0BhZG1pbg==", "Cache-Control: no-cache"})
    @POST("/login")
    Call<Object> login(@retrofit2.http.Field("username") String username, @retrofit2.http.Field("password") String password, @retrofit2.http.Field("fcm_id") String fcm_id);

    @Streaming
    @retrofit2.http.Headers({"Authorization: Basic cmVzdDpzZWVkc0BhZG1pbg==", "Cache-Control: no-cache"})
    @retrofit2.http.GET
    Call<CatalogueListVo> callGetCatalogue(@Url String url1,@retrofit2.http.QueryMap Map<String, String> params);

    // Regular Retrofit 2 GET request
    @Streaming
    @retrofit2.http.GET
    Call<ResponseBody> downloadFileByUrl(@Url String fileUrl);

    @retrofit2.http.Headers({"Authorization: Basic cmVzdDpzZWVkc0BhZG1pbg==", "Cache-Control: no-cache"})
    @Multipart
    @POST("/complaint")
    Call<ResponseBody> uploadFile(@Part MultipartBody.Part[] file,
                                  @Part("requestpara") Object object);


    // Retrofit 2 GET request for rxjava
    @Streaming
    @retrofit2.http.GET
    Observable<Response<ResponseBody>> downloadFileByUrlRx(@Url String fileUrl);

    @Multipart
    @POST
    Call<ResponseBody> uploadProfile(@Url String path,
                                     @Part MultipartBody.Part[] file,
                                     @Part("requestpara") Object object);

    @POST
    Call<FormResponseResVo> postFormData(@Url String path, @Body Object object);

    @retrofit2.http.GET("targets_vs_actuvals?")
    Call<TargetActualResVo> getTargetActuals(@Query("customer_id") String customerId, @Query("year") String year, @Query("month") String month);

    @retrofit2.http.GET("godown_stock")
    Call<GodownResVo> getGodowns(@Query("user_regions") String userRegions, @Query("company_id") String companyId);

    @retrofit2.http.GET("season_mapping")
    Call<SeasonResVo> getSeasons();

    @retrofit2.http.GET("stock_dispatch")
    Call<StockDispatchResp> getStockDispatch(@Query("team") String team);

    @POST("retailer_stock")
    Call<RetailerStockResVo> postRetailerStockSupply(@Body RetailerStockReqVo retailerStockReqVo);


    @retrofit2.http.GET("dg_monitoring_user_data?")
    Call<DemandgenerationGetResults> getlist(@Query("user_id") String user_id);

    @retrofit2.http.GET("farmer_coupons_detail?")
    Call<resmain_farmer_coupons_detail> getcoupans(@Query("user_id") String user_id);

   /* @retrofit2.http.GET("farmer_coupon_data?")
    Call<resmain_farmer_coupons_detail> getcoupans(@Query("user_id") String user_id);*/

    //@retrofit2.http.GET("sto_plan_master?")
    @retrofit2.http.GET("tfa_activity_master?")
    Call<res_tfa_activities_masterd> gettfaactivitylist(@Query("name") String username);
}
