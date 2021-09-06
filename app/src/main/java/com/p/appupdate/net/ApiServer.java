package com.p.appupdate.net;

import com.p.appupdate.Constants;
import com.p.appupdate.bean.DownloadBean;

import java.util.Map;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

/**
 * @author dhL
 * @version V1.0
 * @Title:
 * @Package $
 * @Description: 获取app 应用更新
 * @date $
 */
public interface ApiServer {

    @GET(Constants.APP_UPDATE_URL)
    Call<DownloadBean> getAppUpdate();
}
