package com.p.appupdate.net;

import com.p.appupdate.NetCallBack;

import java.io.File;

/**
 * 获取更新信息
 */
public interface INetManager {

    void get(String url, NetCallBack callBack);
    void download(String url, File targetFile,INetDownloadCallBack callBack,Object tag);
    void cancel(Object tag);
}
