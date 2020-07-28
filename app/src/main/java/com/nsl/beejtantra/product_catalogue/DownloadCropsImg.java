package com.nsl.beejtantra.product_catalogue;

import android.app.Service;
import android.content.ContentValues;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Handler;
import android.os.IBinder;
import android.os.Looper;
import android.os.StrictMode;
import android.support.annotation.Nullable;
import android.util.Log;

import com.nsl.beejtantra.Constants;
import com.nsl.beejtantra.DatabaseHandler;
import com.nsl.beejtantra.network.RetrofitAPI;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.TimeUnit;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

import okhttp3.HttpUrl;
import okhttp3.OkHttpClient;
import okhttp3.ResponseBody;
import okio.BufferedSink;
import okio.Okio;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;
import rx.functions.Func1;
import rx.schedulers.Schedulers;

import static com.nsl.beejtantra.commonutils.Common.startServices;

/**
 * Created by suprasoft on 6/26/2018.
 */

public class DownloadCropsImg extends Service {

    String TAG = "DownloadCropsImg";
    private int taskCount = 0;
    private DatabaseHandler db;
    public  String IMAGE_PATH;
    Handler handler;
    Runnable runnable;
    public DownloadCropsImg() {
        super();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        IMAGE_PATH="/data/data/" + getPackageName() + "/crop_imgs";
        db = new DatabaseHandler(this);
        handler=new Handler(Looper.getMainLooper());
        runnable=new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "runnable...1");
                try{
                stopSelf();
                startServices(DownloadCropProductImg.class,getApplicationContext());
                }catch (Exception e){
                    e.printStackTrace();
                }
            }
        };
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        List<CatalogueCropsPojo> list = db.getCatalogueCrops(db.IMG_URI + " is " + null+" and "+ db.CROP_IMG_PATH +" is not "+null);
        Log.d("DownloadCropsImg", "DownloadCropsImg....list size " + list.size());

        if (list!=null &&  list.size()>0) {
            downloadZipFileRx(list.get(0).cropImgPath, list.get(0));

        }
        if (list!=null && list.size()==0){
            try{
            stopSelf();
            startServices(DownloadCropProductImg.class,getApplicationContext());
            }catch (Exception e){
                e.printStackTrace();
            }
        }


    }

    private void downloadZipFileRx(String url, CatalogueCropsPojo catalogueCropsPojo) {
        // https://github.com/GameJs/gamejs/archive/master.zip
        // AtomicGameEngine/AtomicGameEngine/archive/master.zip
        RetrofitAPI downloadService = createService(RetrofitAPI.class,  Constants.B_URL_PRODUCT_CATALOGUE);
        downloadService.downloadFileByUrlRx(url)
                .flatMap(processResponse(catalogueCropsPojo))
                .flatMap(updateRecordsInDb())
                //.flatMap(unpackZip())
                .subscribeOn(Schedulers.io())
                // .observeOn(AndroidSchedulers.mainThread())
                .subscribe(handleResult());

    }

    private Func1<Response<ResponseBody>, Observable<CatalogueCropsPojo>> processResponse(final CatalogueCropsPojo catalogueCropsPojo) {
        return new Func1<Response<ResponseBody>, Observable<CatalogueCropsPojo>>() {
            @Override
            public Observable<CatalogueCropsPojo> call(Response<ResponseBody> responseBodyResponse) {
                return saveToDiskRx(responseBodyResponse, catalogueCropsPojo);
            }
        };
    }

    private Observable<CatalogueCropsPojo> saveToDiskRx(final Response<ResponseBody> response, final CatalogueCropsPojo catalogueCropsPojo) {
        return Observable.create(new Observable.OnSubscribe<CatalogueCropsPojo>() {
            @Override
            public void call(Subscriber<? super CatalogueCropsPojo> subscriber) {
                try {
                    String header = response.headers().get("Content-Disposition");
                    HttpUrl url = response.raw().request().url();
                    String filename = url.toString().substring(url.toString().lastIndexOf("/") );
                    //String filename = header.replace("attachment; filename=", "");

                    File file = new File(IMAGE_PATH);
                    if (!file.exists())
                        file.mkdir();
                    File destinationFile = new File(IMAGE_PATH + filename);

                    BufferedSink bufferedSink = Okio.buffer(Okio.sink(destinationFile));
                    if (response.body() !=null)
                    bufferedSink.writeAll(response.body().source());
                    bufferedSink.close();
                    // CatalogueCropsPojo catalogueCropsPojo = new CatalogueCropsPojo();
                    if (response.body() !=null) {
                        catalogueCropsPojo.imgURI = destinationFile.getAbsolutePath();
                        catalogueCropsPojo.file = destinationFile;
                    }else {
                        catalogueCropsPojo.imgURI = null;
                        catalogueCropsPojo.cropImgPath=null;
                        catalogueCropsPojo.file = destinationFile;
                    }
                    subscriber.onNext(catalogueCropsPojo);
                    subscriber.onCompleted();
                } catch (IOException e) {
                    stopSelf();
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        });
    }

    private Func1<CatalogueCropsPojo, Observable<CatalogueCropsPojo>> updateRecordsInDb() {
        return new Func1<CatalogueCropsPojo, Observable<CatalogueCropsPojo>>() {
            @Override
            public Observable<CatalogueCropsPojo> call(CatalogueCropsPojo catalogueCropsPojo) {

                ContentValues contentValues = new ContentValues();
                contentValues.put(db.IMG_URI, catalogueCropsPojo.imgURI);
                if (catalogueCropsPojo.cropImgPath==null)
                    contentValues.put(db.CROP_IMG_PATH, catalogueCropsPojo.cropImgPath);
                int id = db.updateTable(db.TABLE_CATALOGUE_CROPS, contentValues, db.SERVER_PK, String.valueOf(catalogueCropsPojo.serverPk));
                Log.d("Anil", "Anil...Ji..." + catalogueCropsPojo.file.getAbsolutePath() + id + "\n" + catalogueCropsPojo.serverPk);

                return Observable.just(catalogueCropsPojo);
            }
        };
    }

    private Func1<File, Observable<File>> unpackZip() {
        return new Func1<File, Observable<File>>() {
            @Override
            public Observable<File> call(File file) {
                InputStream is;
                ZipInputStream zis;
                String parentFolder;
                String filename;
                try {
                    parentFolder = file.getParentFile().getPath();

                    is = new FileInputStream(file.getAbsolutePath());
                    zis = new ZipInputStream(new BufferedInputStream(is));
                    ZipEntry ze;
                    byte[] buffer = new byte[1024];
                    int count;

                    while ((ze = zis.getNextEntry()) != null) {
                        filename = ze.getName();

                        if (ze.isDirectory()) {
                            File fmd = new File(parentFolder + "/" + filename);
                            fmd.mkdirs();
                            continue;
                        }

                        FileOutputStream fout = new FileOutputStream(parentFolder + "/" + filename);

                        while ((count = zis.read(buffer)) != -1) {
                            fout.write(buffer, 0, count);
                        }

                        fout.close();
                        zis.closeEntry();
                    }

                    zis.close();

                } catch (IOException e) {
                    e.printStackTrace();
                }


                File extractedFile = new File(file.getAbsolutePath().replace(".zip", ""));
                if (!file.delete()) Log.d("unpackZip", "Failed to deleted the zip file.");
                return Observable.just(extractedFile);
            }
        };
    }

    private Observer<CatalogueCropsPojo> handleResult() {
        return new Observer<CatalogueCropsPojo>() {
            @Override
            public void onCompleted() {
                taskCount++;
                Log.d(TAG, "onCompleted" + taskCount);
                fetchdata();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Log.d(TAG, "Error " + e.getMessage());
            }

            @Override
            public void onNext(CatalogueCropsPojo file) {
                Log.d(TAG, "File downloaded and extracted to " + file.serverPk + "S URL: " + file.cropImgPath);
            }
        };
    }

    public <T> T createService(Class<T> serviceClass, String baseUrl) {
        OkHttpClient client = new OkHttpClient.Builder()
                .connectTimeout(5, TimeUnit.MINUTES)
                .writeTimeout(5, TimeUnit.MINUTES)
                .readTimeout(5, TimeUnit.MINUTES)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create()).build();
        return retrofit.create(serviceClass);
    }

    private void downloadZipFile() {
        RetrofitAPI downloadService = createService(RetrofitAPI.class, "https://github.com/");
        Call<ResponseBody> call = downloadService.downloadFileByUrl("gameplay3d/GamePlay/archive/master.zip");

        call.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, final Response<ResponseBody> response) {
                if (response.isSuccessful()) {
                    Log.d(TAG, "Got the body for the file");

                    new AsyncTask<Void, Long, Void>() {
                        @Override
                        protected Void doInBackground(Void... voids) {
                            saveToDisk(response.body(), "gameplay3d.zip");
                            return null;
                        }
                    }.execute();

                } else {
                    Log.d(TAG, "Connection failed " + response.errorBody());
                }
            }

            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                t.printStackTrace();
                Log.e(TAG, t.getMessage());
            }
        });

    }

    private void saveToDisk(ResponseBody body, String filename) {
        try {
            File file = new File(IMAGE_PATH);
            if (!file.exists())
                file.mkdir();
            File destinationFile = new File(IMAGE_PATH + filename);

            InputStream is = null;
            OutputStream os = null;

            try {
                long filesize = body.contentLength();
                Log.d(TAG, "File Size=" + filesize);
                is = body.byteStream();
                os = new FileOutputStream(destinationFile);

                byte data[] = new byte[4096];
                int count;
                int progress = 0;
                while ((count = is.read(data)) != -1) {
                    os.write(data, 0, count);
                    progress += count;
                    Log.d(TAG, "Progress: " + progress + "/" + filesize + " >>>> " + (float) progress / filesize);
                }

                os.flush();

                Log.d(TAG, "File saved successfully!");
                return;
            } catch (IOException e) {
                e.printStackTrace();
                Log.d(TAG, "Failed to save the file!");
                return;
            } finally {
                if (is != null) is.close();
                if (os != null) os.close();
            }
        } catch (IOException e) {
            e.printStackTrace();
            Log.d(TAG, "Failed to save the file!");
            return;
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "Service destroyed onDestroy");

    }

    public void fetchdata() {
        Observable.fromCallable(new Callable<List<CatalogueCropsPojo>>() {
            @Override
            public List<CatalogueCropsPojo> call() throws Exception {
                return new DatabaseHandler(DownloadCropsImg.this).getCatalogueCrops(db.IMG_URI + " is " + null+" and "+ db.CROP_IMG_PATH +" is not "+null);
            }
        }).subscribeOn(Schedulers.io()).subscribe(ad());

    }

    private Observer<? super List<CatalogueCropsPojo>> ad() {
        return new Observer<List<CatalogueCropsPojo>>() {
            @Override
            public void onCompleted() {
               // Log.d("Anil", "Crops list size onCompleted");

            }

            @Override
            public void onError(Throwable e) {
                Log.d("Anil", e.toString());

            }

            @Override
            public void onNext(List<CatalogueCropsPojo> crops) {
                Log.d("Anil", "crops list size" + crops.size());
                if (crops!=null &&  crops.size()>0) {
                    downloadZipFileRx(crops.get(0).cropImgPath, crops.get(0));

                }
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable,1000*60*2);
                if (crops!=null && crops.size()==0){
                    try{
                    handler.removeCallbacks(runnable);
                    stopSelf();
                    startServices(DownloadCropProductImg.class,getApplicationContext());
                    }catch (Exception e){
                        e.printStackTrace();
                    }
                }
            }
        };
    }


}
