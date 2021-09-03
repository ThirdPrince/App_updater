package com.p.appupdate;

import androidx.appcompat.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.p.appupdate.Dialog.UpdateVersionShowDialog;
import com.p.appupdate.bean.DownloadBean;
import com.p.appupdate.updater.AppUpdater;
import com.p.appupdate.utils.AppUtils;

import org.json.JSONObject;

/**
 * 应用内升级APP
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Button getAppUpdate ,getApk ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        getAppUpdate = findViewById(R.id.get_update);
        getApk = findViewById(R.id.get_apk);
        getAppUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AppUpdater.getInstance().getNetManager().get(Constants.APP_UPDATE_URL, new NetCallBack() {
                    @Override
                    public void success(String response) {
                        Log.e(TAG, "rsp==" + response);
                        try {
                            JSONObject jsonObject = new JSONObject(response);
                            DownloadBean downLoadBean = DownloadBean.parse(jsonObject);

                            if (downLoadBean == null) {
                                Toast.makeText(MainActivity.this, "接口返回数据异常", Toast.LENGTH_LONG).show();
                                return;
                            }

                            // 检测是否需要弹窗

                            long versionCode = Long.parseLong(downLoadBean.versionCode);
                            if (versionCode > AppUtils.getVersionCode(MainActivity.this)) {
                                UpdateVersionShowDialog.show(MainActivity.this, downLoadBean);
                                return;
                            }
                        }catch (Exception e)
                        {
                            e.printStackTrace();
                        }
                    }



                    @Override
                    public void failed(Throwable throwable) {
                        throwable.printStackTrace();
                    }
                });
            }
        });

    }


}
