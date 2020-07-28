package com.nsl.beejtantra.product_catalogue;

import android.app.ProgressDialog;
import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.IBinder;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.util.Log;

import com.nsl.beejtantra.Constants;
import com.nsl.beejtantra.DatabaseHandler;
import com.nsl.beejtantra.MyApplication;
import com.nsl.beejtantra.commonutils.Common;
import com.nsl.beejtantra.network.RetrofitResponseListener;
import com.nsl.beejtantra.pojo.FormResponseResVo;
import com.nsl.beejtantra.scheduler.SchedulingService;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import rx.Observer;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

/**
 * Created by suprasoft on 6/20/2018.
 */

public class CatalogueIntentService extends Service implements RetrofitResponseListener {
    public static final String mypreference = "mypref";
    private static final String TAG = SchedulingService.class.getName();
    public SharedPreferences sharedpreferences;
    DatabaseHandler db;
    ArrayList<HashMap<String, String>> favouriteItem = new ArrayList<HashMap<String, String>>();
    String userId, team;
    int role;
    Context mContext;
    private int p=0;


    @Override
    public void onCreate() {
        super.onCreate();
        db = new DatabaseHandler(this);
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        sharedpreferences = getSharedPreferences(mypreference, Context.MODE_PRIVATE);
        this.mContext = getApplicationContext();

        userId = sharedpreferences.getString("userId", "");
        role = sharedpreferences.getInt(Constants.SharedPrefrancesKey.ROLE, 0);
        team = sharedpreferences.getString("team", "");
        Common.Log.i("CatalogueIntentService  onCreate..");
/*
        CatalogueListVo catalogueListVo=new CatalogueListVo();
        List<CatalogueCropsPojo> catalogueCropsPojos=new ArrayList<>();
        for (int i=0; i<100; i++){
            CatalogueCropsPojo catalogueCropsPojo=new CatalogueCropsPojo();
            catalogueCropsPojo.serverPk=i+1;

            if (i%3==0)
                catalogueCropsPojo.cropImgPath="tracking_images/20180511_090841.png";
            else if (i%2==0)
            catalogueCropsPojo.cropImgPath="tracking_images/20180324_144925.png";
           else
                catalogueCropsPojo.cropImgPath="tracking_images/1528791522370.jpg";
            catalogueCropsPojo.cropName="Test "+i;
            catalogueCropsPojo.dateTime= String.valueOf(System.currentTimeMillis());
            catalogueCropsPojo.status=1;
            catalogueCropsPojo.version="1";
            for (int j= 0; j<10; j++){
                ++p;
                CatalogueCropsProductsPojo catalogueCropsProductsPojo=new CatalogueCropsProductsPojo();
                catalogueCropsProductsPojo.cropRelationId=i+1;
                catalogueCropsProductsPojo.dateTime=String.valueOf(System.currentTimeMillis());
                catalogueCropsProductsPojo.productImg="tracking_images/20180324_144925.png";
                if (j==1 || j==5)
                catalogueCropsProductsPojo.productImg="tracking_images/1528791522370.jpg";

                catalogueCropsProductsPojo.description="<!DOCTYPE html>\n" +
                        "<html lang=\"en\">\n" +
                        "  <head>\n" +
                        "    <meta charset=\"utf-8\">\n" +
                        "    <meta http-equiv=\"X-UA-Compatible\" content=\"IE=edge\">\n" +
                        "    <meta name=\"viewport\" content=\"width=device-width, initial-scale=1\">\n" +
                        "    <!-- The above 3 meta tags *must* come first in the head; any other head content must come *after* these tags -->\n" +
                        "    <title>NSL</title>\n" +
                        "    <link href=\"file:///android_asset/css/base.css\" type=\"text/css\" rel=\"stylesheet\">\n" +
                        "    <link href=\"file:///android_asset/css/bootstrap.css\" type=\"text/css\" rel=\"stylesheet\">\n" +
                        "    <link href=\"file:///android_asset/css/bootstrap-select.css\" type=\"text/css\" rel=\"stylesheet\">\n" +
                        "    <link href=\"file:///android_asset/css/font-awesome.css\" type=\"text/css\" rel=\"stylesheet\">\n" +
                        "    <link href=\"https://fonts.googleapis.com/css?family=Roboto:100,100i,300,300i,400,400i,500,500i,700,700i,900,900i\" rel=\"stylesheet\"> \n" +
                        "  </head>\n" +
                        "  <body>\n" +
                        "<div class=\"main_wrapper\">\n" +
                        "    <!-- Content Wrapper. Contains page content -->\n" +
                        "  <div class=\"container\">\n" +
                        "    <!-- Main content -->\n" +
                        "    <section class=\"content\">\n" +
                        "    <div class=\"col-sm-12\">\n" +
                        "    <div class=\"row catelogo_header\">\n" +
                        "    <div class=\"col-sm-12  text-center\">\n" +
                        "    <h2 class=\"title_cat\">Mallika - <span>NCS 207 BG II</span></h2>\n" +
                        "\t<a href=\"#\" onclick=\"window.history.go(-1)\"><i class=\"fa fa-angle-left\"></i></a>\n" +
                        "    </div>\n" +
                        "    </div>\n" +
                        "    </div>\n" +
                        "    <!--<div class=\"col-sm-4  text-center\">\n" +
                        "    <img src=\"images/mallikansc207bg11.png\" class=\"img-responsive\" alt=\"mallikansc207bg11\">\n" +
                        "    </div>\n" +
                        "\t-->\n" +
                        "        <div class=\"col-sm-8\">\n" +
                        "\t <div class=\"table-responsive\">\n" +
                        "    <table class=\"table table-bordered\">\n" +
                        "    <thead>\n" +
                        "      <tr>\n" +
                        "        <th  colspan=\"2\">Morphological Characters</th>\n" +
                        "      </tr>\n" +
                        "    </thead>\n" +
                        "    <tbody>\n" +
                        "      <tr>\n" +
                        "        <td>Hybrid Character:</td>\n" +
                        "        <td>Mallika BG II(NCS-207 BG II)</td>\n" +
                        "      </tr>\n" +
                        "      <tr>\n" +
                        "        <td>Plant Height:</td>\n" +
                        "        <td>Very tall</td>\n" +
                        "      </tr>\n" +
                        "      <tr>\n" +
                        "        <td>Reaction to Major Pests:</td>\n" +
                        "        <td>Resistant to American\n" +
                        "Bollworm,<br/> Pink Bollworm,<br/>\n" +
                        "Spotted Bollworm and\n" +
                        "Spodoptera.<br/> Tolerant to\n" +
                        "sucking pests,<br/> Jassids and\n" +
                        "Whitefly</td>\n" +
                        "      </tr>\n" +
                        "      <tr>\n" +
                        "        <td>Boll Shape & Size:</td>\n" +
                        "        <td>Ovate and very large</td>\n" +
                        "      </tr>  \n" +
                        "       <tr>\n" +
                        "        <td>Quality of Produce: </td>\n" +
                        "        <td>Ginning%: 35 - 36<br/>\n" +
                        "Staple length: > 32.5 mm<br/>\n" +
                        "Strength: 21 - 24 g/tex<br/>\n" +
                        "Micronaire: 3.0 - 3.9</td>\n" +
                        "      </tr>\n" +
                        "       <tr>\n" +
                        "        <td  colspan=\"2\">\n" +
                        "        <h2>Special Features/USPs:</h2>\n" +
                        "        <ul>\n" +
                        "        <li>Best Hybrid suitable for various soil types</li>\n" +
                        "        <li>Big bolls and higher boll weight</li>\n" +
                        "        <li>Sucking pests tolerant</li>\n" +
                        "        <li>Easy picking and Good rejuvenation ability.</li>\n" +
                        "        </ul>\n" +
                        "        </td>\n" +
                        "      </tr>\n" +
                        "    </tbody>\n" +
                        "  </table></div></div>\n" +
                        "    </section>\n" +
                        "  </div>\n" +
                        "  <!-- End- Content Wrapper. Contains page content -->\n" +
                        "</div>\n" +
                        "  </body>\n" +
                        "  <script src=\"file:///android_asset/js/jquery.js\" type=\"text/javascript\"></script>\n" +
                        "  <script src=\"file:///android_asset/js/bootstrap.js\" type=\"text/javascript\"></script>\n" +
                        "  <script src=\"file:///android_asset/js/bootstrap-select.js\" type=\"text/javascript\"></script>\n" +
                        "</html>";
                catalogueCropsProductsPojo.productName="Product "+i+ " "+j;
                catalogueCropsProductsPojo.serverPk=p;
                catalogueCropsProductsPojo.version="1";
                catalogueCropsProductsPojo.status=1;
                catalogueCropsPojo.catalogueCropsProductsList.add(catalogueCropsProductsPojo);

            }
            catalogueCropsPojos.add(catalogueCropsPojo);
        }
        catalogueListVo.catalogueCropsList=catalogueCropsPojos;
        db.addCatalogues(catalogueCropsPojos);
        saveToDisk(new Gson().toJson(catalogueListVo),"jsonFile");
      Log.d("Data",new Gson().toJson(catalogueListVo));*/
    }

    @Override
    public void onStart(Intent intent, int startId) {
        super.onStart(intent, startId);
        Common.Log.i("CatalogueIntentService  onHandleIntent..");
        final Map<String, String> map = new HashMap<String, String>();
        map.put("user_id",Common.getUserIdFromSP(this));
       //  new RetrofitRequester(this).callGetApi(map);


           sendGetRequest(map);


    }



    @Override
    public void onResponseSuccess(ArrayList<Object> object, Map<String, String> requestParams, int requestId) {

        Common.Log.i("CatalogueIntentService  onResponseSuccess..");

    }

    @Override
    public void onFormResponseSuccess(String url,FormResponseResVo formResponseResVo, ProgressDialog dialog) {

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Common.Log.i("CatalogueIntentService  onDestroy..");
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    public void sendGetRequest(final Map<String, String> map) {

        if (!Common.haveInternet(this)) {
            this.stopSelf();
            // Common.customToast(this, Constants.INTERNET_UNABLEABLE);

            return;

        }

        MyApplication.getInstance().getRetrofitAPI().callGetCatalogue("crops_catalogue",map).enqueue(new Callback<CatalogueListVo>() {
            @Override
            public void onResponse(Call<CatalogueListVo> call, Response<CatalogueListVo> response) {
                if(response.body()!=null){
                    CatalogueListVo o = response.body();
                    db.addCatalogues(o.catalogueCropsList);

                    new android.os.Handler().postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            try{
                                stopSelf();
                                startService(new Intent(CatalogueIntentService.this, DownloadCropsImg.class));
                            }catch (Exception e){
                                e.printStackTrace();
                            }
                        }
                    }, 5000);
                }
            }

            @Override
            public void onFailure(Call<CatalogueListVo> call, Throwable t) {

            }
        });


//        MyApplication.getInstance().getRetrofitAPI().callGetCatalogue(map).subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<CatalogueListVo>() {
//                    @Override
//                    public void onCompleted() {
//                        Common.Log.i("completed");
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//
//                        Common.Log.i("onError; "+e.toString());
//                        // Toast.makeText(context, "Internal Server Error!", Toast.LENGTH_SHORT).show();
//                    }
//
//                    @Override
//                    public void onNext(CatalogueListVo o) {
//                       /* List<VersionControlVo> versionControlVoList = new ArrayList<>();
//                       // ArrayList<Object> versionControlVoList1 = (ArrayList<Object>) o;
//                        ArrayList as = Common.getSpecificDataObject(o, ArrayList.class);
//                      //  versionControlVoList=(List<VersionControlVo>)as;
//                        for (Object objectt : as) {
//
//                            VersionControlVo versionControlListVo = Common.getSpecificDataObject(objectt, VersionControlVo.class);
//                            versionControlVoList.add(versionControlListVo);
//
//
//                        }
//
//                      //  if (db.getVersionControlList().size() == 0) {*/
//
//                            db.addCatalogues(o.catalogueCropsList);
//
//                        new android.os.Handler().postDelayed(new Runnable() {
//                            @Override
//                            public void run() {
//                                try{
//                                stopSelf();
//                                startService(new Intent(CatalogueIntentService.this, DownloadCropsImg.class));
//                            }catch (Exception e){
//                                e.printStackTrace();
//                            }
//                        }
//                        }, 5000);
//
//
//                       // }
//                        Common.Log.i("onNext"+o.catalogueCropsList.size());
//                    }
//                });

    }

    private void saveToDisk(String body, String filename) {
        try {
            new File("/data/data/" + getPackageName() + "/jsond").mkdir();
            File destinationFile = new File("/data/data/" + getPackageName() + "/jsond/" + filename);

            InputStream is = null;
            OutputStream os = null;

            try {
                FileWriter fileWriter = new FileWriter(destinationFile);
                fileWriter.write(body);
                fileWriter.flush();
                fileWriter.close();

                Log.d(TAG, "File saved successfully!");
                return;
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Failed to save the file!");
                return;
            } finally {

            }
        } catch (Exception e) {
            e.printStackTrace();
            Log.d(TAG, "Failed to save the file!");
            return;
        }
    }
}
