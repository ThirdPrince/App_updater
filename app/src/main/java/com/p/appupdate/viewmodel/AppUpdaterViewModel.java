package com.p.appupdate.viewmodel;

import android.util.Log;
import android.widget.Toast;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.p.appupdate.Constants;
import com.p.appupdate.NetCallBack;
import com.p.appupdate.bean.DownloadBean;
import com.p.appupdate.updater.AppUpdater;
import org.json.JSONObject;

/**
 * @author dhl
 * @version V1.0
 * @Title: appUpdater viewModel
 * @Package $
 * @Description: $(用一句话描述)
 * @date $
 */
public class AppUpdaterViewModel extends ViewModel {

    private MutableLiveData<DownloadBean> isUpdater;

    public LiveData<DownloadBean> getIsUpdater() {
        if (isUpdater == null) {
            isUpdater = new MutableLiveData();
        }
        checkAppUpdater();
        return isUpdater;
    }

    private void checkAppUpdater() {
        AppUpdater.getInstance().getNetManager().get(Constants.APP_UPDATE_URL, new NetCallBack() {
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
