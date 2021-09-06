package com.p.appupdate.updater;

import com.p.appupdate.net.INetManager;
import com.p.appupdate.net.OkHttpManager;

public class AppUpdater {
    private static AppUpdater mInstance;

    private INetManager mNetManager = new OkHttpManager();

    public void setNetManager(INetManager mNetManager) {
        this.mNetManager = mNetManager;
    }

    public INetManager getNetManager() {
        return mNetManager;
    }

    private AppUpdater() {

    }

    public static AppUpdater getInstance() {
        if (mInstance == null) {
            synchronized (AppUpdater.class) {
                if (mInstance == null) {
                    mInstance = new AppUpdater();
                }
            }
        }

        return mInstance;
    }

}
