package com.p.appupdate.net;

import android.os.Handler;
import android.os.Looper;

import com.p.appupdate.NetCallBack;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpManager implements INetManager{

    private static OkHttpClient okHttpClient ;
    private static Handler mHandler ;
    static
    {
        OkHttpClient.Builder  builder = new OkHttpClient.Builder();
        builder.connectTimeout(14, TimeUnit.SECONDS);
        okHttpClient = builder.build();
        mHandler = new Handler(Looper.getMainLooper());
    }
    @Override
    public void get(String url, final NetCallBack callBack) {
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, final IOException e) {
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(callBack != null)
                        {
                            callBack.failed(e);
                        }
                    }
                });

            }

            @Override
            public void onResponse(Call call,  Response response) throws IOException {
                final String rsp = response.body().string();
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        if(callBack != null)
                        {
                            callBack.success(rsp);
                        }
                    }
                });

            }
        });

    }

    @Override
    public void download(String url,final File targetFile, final INetDownloadCallBack callBack) {

        if(!targetFile.exists())
        {
            targetFile.getParentFile().mkdirs();
        }
        Request request = new Request.Builder().url(url).build();
        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                if(callBack != null)
                {
                    callBack.failed(e);
                }
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {

                InputStream inputStream = response.body().byteStream();
                OutputStream outputStream = new FileOutputStream(targetFile);
                long contentLength = response.body().contentLength();
                int len = 0;
                int sum = 0;
                byte[] bytes = new byte[1024*8];
                while ((len = inputStream.read(bytes))!= -1 )
                {
                    sum += len;
                    outputStream.write(bytes,0,len);
                    callBack.progress((int) ((sum * 1.0f/contentLength)*100) +"%" );
                }
                //targetFile.setExecutable(true,false);
                callBack.success(targetFile);


            }
        });
    }
}
