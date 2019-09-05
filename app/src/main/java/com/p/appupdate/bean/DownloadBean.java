package com.p.appupdate.bean;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.Serializable;

/**
 * 更新信息 bean
 */
public class DownloadBean implements Serializable {

    public String title ;
    public String content ;
    public String url  ;
    public  String md5 ;
    public  String versionCode ;

    public  static DownloadBean  parse(JSONObject jsonObject)
    {
        DownloadBean downloadBean = null;
        try {
            String title = jsonObject.getString("title");
            String content = jsonObject.getString("content");
            String url = jsonObject.getString("url");
            String  md5 = jsonObject.getString("md5");
            String  versionCode = jsonObject.getString("versionCode");
            downloadBean = new DownloadBean();
            downloadBean.url = url ;
            downloadBean.title = title;
            downloadBean.md5 = md5 ;
            downloadBean.content = content ;
            downloadBean.versionCode = versionCode ;

            return downloadBean ;
        } catch (JSONException e) {
            e.printStackTrace();
        }

        return null ;
    }
}
