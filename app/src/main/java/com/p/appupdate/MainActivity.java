package com.p.appupdate;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModel;
import androidx.lifecycle.ViewModelProvider;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.p.appupdate.Dialog.UpdateVersionShowDialog;
import com.p.appupdate.bean.DownloadBean;
import com.p.appupdate.updater.AppUpdater;
import com.p.appupdate.utils.AppUtils;
import com.p.appupdate.viewmodel.AppUpdaterViewModel;

import org.json.JSONObject;

/**
 * 应用内升级APP
 */
public class MainActivity extends AppCompatActivity {

    private static final String TAG = "MainActivity";

    private Button getAppUpdate ,getApk ;

    private AppUpdaterViewModel appUpdaterViewModel ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        appUpdaterViewModel = new ViewModelProvider(this, new ViewModelProvider.NewInstanceFactory()).get(AppUpdaterViewModel.class);
        getAppUpdate = findViewById(R.id.get_update);
        getApk = findViewById(R.id.get_apk);

        getAppUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                appUpdaterViewModel.getIsUpdater().observe(MainActivity.this, new Observer<DownloadBean>() {
                    @Override
                    public void onChanged(DownloadBean bean) {
                        if (bean == null) {
                            Toast.makeText(MainActivity.this, "接口返回数据异常", Toast.LENGTH_LONG).show();
                            return;
                        }
                        long versionCode = Long.parseLong(bean.versionCode);
                        if (versionCode > AppUtils.getVersionCode(MainActivity.this)) {
                            UpdateVersionShowDialog.show(MainActivity.this, bean);
                        }
                    }
                });

            }
        });

    }


}
