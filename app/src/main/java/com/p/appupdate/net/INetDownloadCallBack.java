package com.p.appupdate.net;

import java.io.File;

/**
 * 下载文件接口
 */
public interface INetDownloadCallBack {

    void success(File apkFile);
    void failed(Throwable throwable);
    void progress(String progress);
}
