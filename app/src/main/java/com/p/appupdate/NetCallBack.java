package com.p.appupdate;

public interface NetCallBack {

    void success(String response);
    void failed(Throwable throwable);

}
