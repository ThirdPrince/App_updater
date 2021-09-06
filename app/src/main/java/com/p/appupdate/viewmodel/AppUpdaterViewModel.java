package com.p.appupdate.viewmodel;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.p.appupdate.Constants;
import com.p.appupdate.NetCallBack;
import com.p.appupdate.bean.DownloadBean;
import com.p.appupdate.net.ApiServer;
import com.p.appupdate.updater.AppUpdater;
import org.json.JSONObject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * @author dhl
 * @version V1.0
 * @Title: appUpdater viewModel
 * @Package $
 * @Description: $(用一句话描述)
 * @date $
 */
public class AppUpdaterViewModel extends ViewModel {

    private static final String TAG = "AppUpdaterViewModel";

    private MutableLiveData<DownloadBean> isUpdater;

    public LiveData<DownloadBean> getIsUpdater() {
        if (isUpdater == null) {
            isUpdater = new MutableLiveData();
        }
        checkAppUpdater();
        return isUpdater;
    }

    private void checkAppUpdater() {

       // useRetrofit();
        useOkhttp();
    }

    /**
     * retrofit
     */
    private void useRetrofit(){
        Retrofit retrofit= new Retrofit.Builder()
                .baseUrl(Constants.BASE_URL)
                .addConverterFactory(GsonConverterFactory.create()) //设置数据解析器
                .build();

        ApiServer retrofitApi = retrofit.create(ApiServer.class);
        Call<DownloadBean> userCall=retrofitApi.getAppUpdate();
        userCall.enqueue(new Callback<DownloadBean>() {
            @Override
            public void onResponse(Call<DownloadBean> call, Response<DownloadBean> response) {
                Log.e(TAG,"response = "+response.toString());
                isUpdater.postValue(response.body());
            }

            @Override
            public void onFailure(Call<DownloadBean> call, Throwable t) {

            }
        });


    }

    /**
     * okhttp
     */
    private void useOkhttp(){
        AppUpdater.getInstance().getNetManager().get(Constants.BASE_URL+Constants.APP_UPDATE_URL, new NetCallBack() {
            @Override
            public void success(String response) {
                try {
                    JSONObject jsonObject = new JSONObject(response);
                    DownloadBean downLoadBean = DownloadBean.parse(jsonObject);
                    isUpdater.postValue(downLoadBean);

                } catch (Exception e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void failed(Throwable throwable) {
                throwable.printStackTrace();
            }
        });

    }

}
