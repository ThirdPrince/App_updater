package com.p.appupdate.net;

import android.os.Handler;
import android.os.Looper;
import android.util.Log;

import com.p.appupdate.NetCallBack;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.List;
import java.util.concurrent.TimeUnit;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class OkHttpManager implements INetManager{

    private static final String TAG = "OkHttpManager";

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
        Call call =  okHttpClient.newCall(request);
        call.enqueue(new Callback() {
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
    public void download(String url,final File targetFile, final INetDownloadCallBack callBack,Object tag) {

        if(!targetFile.exists())
        {
            targetFile.getParentFile().mkdirs();
        }
        Request request = new Request.Builder().url(url).tag(tag).build();

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
            public void onResponse(Call call, Response response)throws IOException  {


                InputStream inputStream = null;
                OutputStream outputStream = null;
                try {
                    inputStream = response.body().byteStream();
                outputStream = new FileOutputStream(targetFile);

                final long contentLength = response.body().contentLength();
                int len = 0;
                int sum = 0;
                byte[] bytes = new byte[1024*12];
                while ((len = inputStream.read(bytes))!= -1 )
                {
                    sum += len;
                    outputStream.write(bytes,0,len);
                    outputStream.flush();
                    final int sumLen = sum ;
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.progress((int) ((sumLen * 1.0f/contentLength*100)) +"%" );
                        }
                    });

                }
                targetFile.setExecutable(true,false);
                targetFile.setReadable(true,false);
                targetFile.setWritable(true,false);
                mHandler.post(new Runnable() {
                    @Override
                    public void run() {
                        callBack.success(targetFile);
                    }
                });
                } catch (final Exception e) {
                    e.printStackTrace();
                    mHandler.post(new Runnable() {
                        @Override
                        public void run() {
                            callBack.failed(e);
                        }
                    });
                }finally {

                    if(inputStream != null)
                    {
                        inputStream.close();
                    }
                    if(outputStream != null)
                    {
                        outputStream.close();
                    }
                }


            }
        });
    }

    @Override
    public void cancel(Object tag) {
        List<Call> queuedCalls = okHttpClient.dispatcher().queuedCalls();
        for(Call call :queuedCalls)
        {
            if(tag.equals(call.request().tag()))
        {
                Log.e(TAG,"queuedCalls tag ::"+tag);
                call.cancel();
            }
        }

        List<Call> runningCalls = okHttpClient.dispatcher().runningCalls();
        for(Call call :runningCalls)
        {
            if(tag.equals(call.request().tag()))
            {
                Log.e(TAG,"runningCalls tag::"+tag);
                call.cancel();
            }
        }
        
    }
}
