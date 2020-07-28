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

/**
 * Created by suprasoft on 6/26/2018.
 */

public class DownloadCropProductImg extends Service {

    String TAG = "DownloadCropProductImg";
    private int taskCount = 0;
    private DatabaseHandler db;
    public  String IMAGE_PATH;
    private Handler handler;
    private Runnable runnable;

    public DownloadCropProductImg() {
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
        IMAGE_PATH="/data/data/" + getPackageName() + "/prod_imgs";
        StrictMode.ThreadPolicy policy = new StrictMode.ThreadPolicy.Builder()
                .permitAll().build();
        StrictMode.setThreadPolicy(policy);
        db = new DatabaseHandler(this);
        handler=new Handler(Looper.getMainLooper());
        runnable=new Runnable() {
            @Override
            public void run() {
                Log.d(TAG, "runnable...2");
                stopSelf();
            }
        };
        List<CatalogueCropsProductsPojo> list = db.getCatalogueCropProductsImgNull();
        Log.d("DownloadCropsProductImg", "list size " + list.size());

      if (list!=null &&list.size()>0) {
          downloadZipFileRx(list.get(0).productImg, list.get(0));

        }else{
          stopSelf();
      }

    }

    private void downloadZipFileRx(String url, CatalogueCropsProductsPojo catalogueCropsProductsPojo) {
        // https://github.com/GameJs/gamejs/archive/master.zip
        // AtomicGameEngine/AtomicGameEngine/archive/master.zip
        RetrofitAPI downloadService = createService(RetrofitAPI.class, Constants.B_URL_PRODUCT_CATALOGUE);
        downloadService.downloadFileByUrlRx(url)
                .flatMap(processResponse(catalogueCropsProductsPojo))
                .flatMap(updateRecordsInDb())

                //.flatMap(unpackZip())
                .subscribeOn(Schedulers.io())
                // .observeOn(AndroidSchedulers.mainThread())
                .subscribe(handleResult());

    }

    private Func1<Response<ResponseBody>, Observable<CatalogueCropsProductsPojo>> processResponse(final CatalogueCropsProductsPojo catalogueCropsProductsPojo) {
        return new Func1<Response<ResponseBody>, Observable<CatalogueCropsProductsPojo>>() {
            @Override
            public Observable<CatalogueCropsProductsPojo> call(Response<ResponseBody> responseBodyResponse) {
                return saveToDiskRx(responseBodyResponse, catalogueCropsProductsPojo);
            }
        };
    }

    private Observable<CatalogueCropsProductsPojo> saveToDiskRx(final Response<ResponseBody> response, final CatalogueCropsProductsPojo catalogueCropsProductsPojo) {
        return Observable.create(new Observable.OnSubscribe<CatalogueCropsProductsPojo>() {
            @Override
            public void call(Subscriber<? super CatalogueCropsProductsPojo> subscriber) {
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
                    if (response.body()!=null)
                    bufferedSink.writeAll(response.body().source());
                    bufferedSink.close();
                    if (response.body() !=null) {
                        catalogueCropsProductsPojo.imgURI = destinationFile.getAbsolutePath();
                    }else {
                        catalogueCropsProductsPojo.imgURI = null;
                        catalogueCropsProductsPojo.productImg=null;
                    }
                    subscriber.onNext(catalogueCropsProductsPojo);
                    subscriber.onCompleted();
                } catch (IOException e) {
                    stopSelf();
                    e.printStackTrace();
                    subscriber.onError(e);
                }
            }
        });
    }

    private Func1<CatalogueCropsProductsPojo, Observable<CatalogueCropsProductsPojo>> updateRecordsInDb() {
        return new Func1<CatalogueCropsProductsPojo, Observable<CatalogueCropsProductsPojo>>() {
            @Override
            public Observable<CatalogueCropsProductsPojo> call(CatalogueCropsProductsPojo file) {

                ContentValues contentValues = new ContentValues();
                contentValues.put(db.IMG_URI, file.imgURI);
                if (file.productImg==null)
                    contentValues.put(db.PRODUCT_IMG, file.productImg);
                int id = db.updateTable(db.TABLE_CATALOGUE_CROPS_PRODUCTS, contentValues, db.SERVER_PK, String.valueOf(file.serverPk));
             //   Log.d("Anil", "Anil...Ji..." + file.imgURI + id + "\n" + file.serverPk);

                return Observable.just(file);
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

    private Observer<CatalogueCropsProductsPojo> handleResult() {
        return new Observer<CatalogueCropsProductsPojo>() {
            @Override
            public void onCompleted() {
                taskCount++;
          //      Log.d(TAG, "onCompleted" + taskCount);
                fetchdata();
            }

            @Override
            public void onError(Throwable e) {
                e.printStackTrace();
                Log.d(TAG, "Error " + e.getMessage());
            }

            @Override
            public void onNext(CatalogueCropsProductsPojo file) {
                Log.d(TAG, "File downloaded and extracted to " + file.serverPk + "S URL: " + file.productImg);
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
        Observable.fromCallable(new Callable<List<CatalogueCropsProductsPojo>>() {
            @Override
            public List<CatalogueCropsProductsPojo> call() throws Exception {

                return new DatabaseHandler(DownloadCropProductImg.this).getCatalogueCropProductsImgNull();
            }
        }).subscribeOn(Schedulers.io()).subscribe(ad());

    }

    private Observer<? super List<CatalogueCropsProductsPojo>> ad() {
        return new Observer<List<CatalogueCropsProductsPojo>>() {
            @Override
            public void onCompleted() {
             //   Log.d("Anil", "crops list size onCompleted");
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable,1000*2*60);
            }

            @Override
            public void onError(Throwable e) {
                Log.d("Anil", e.toString());

            }

            @Override
            public void onNext(List<CatalogueCropsProductsPojo> crops) {
                Log.d("Anil", "Product list size" + crops.size());
                if (crops!=null &&crops.size()>0) {
                    downloadZipFileRx(crops.get(0).productImg, crops.get(0));

                }
                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable,1000*60*2);
                if (crops!=null && crops.size()==0){
                    handler.removeCallbacks(runnable);
                    stopSelf();
                }

            }
        };
    }


}
