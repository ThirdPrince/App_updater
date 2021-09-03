package com.p.appupdate.Dialog;

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;
import androidx.fragment.app.FragmentActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.p.appupdate.R;
import com.p.appupdate.bean.DownloadBean;
import com.p.appupdate.net.INetDownloadCallBack;
import com.p.appupdate.updater.AppUpdater;
import com.p.appupdate.utils.AppUtils;

import java.io.File;

/**
 * 弹窗
 */
public class UpdateVersionShowDialog extends DialogFragment {

    private static final String TAG = "UpdateVersionShowDialog";

    private  DownloadBean downloadBean ;

    private TextView title ,content,update;

    public static void show(FragmentActivity fragmentActivity, DownloadBean downloadBean)
    {
        Bundle bundle = new Bundle();
        bundle.putSerializable("download_bean",downloadBean);
        UpdateVersionShowDialog dialog = new UpdateVersionShowDialog();
        dialog.setArguments(bundle);
        dialog.show(fragmentActivity.getSupportFragmentManager(),TAG);
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Bundle bundle = getArguments();
        downloadBean = (DownloadBean) bundle.getSerializable("download_bean");
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.dialog_fragment,container,false);
        initView(view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        //setCancelable(false);
        initEvent();
    }

    private void initView(View view )
    {
        title = view.findViewById(R.id.title);
        content = view.findViewById(R.id.content);
        update = view.findViewById(R.id.update);
        title.setText(downloadBean.title);
        content.setText(downloadBean.content);

    }
    private void initEvent()
    {
        update.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                File targetFile = new File(getActivity().getCacheDir(),"update.apk");
                AppUpdater.getInstance().getNetManager().download(downloadBean.url, targetFile, new INetDownloadCallBack() {
                    @Override
                    public void success(File apkFile) {
                        Log.e(TAG,"apkFile ::"+apkFile.getPath());
                        dismiss();
                        AppUtils.installApk(getActivity(),apkFile.getPath());
                    }

                    @Override
                    public void failed(Throwable throwable) {
                        throwable.printStackTrace();
                    }

                    @Override
                    public void progress(final String progress) {
                        Log.e(TAG,"progress ::"+progress);
                        getActivity().runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                update.setText(progress);
                            }
                        });

                    }
                },UpdateVersionShowDialog.this);
            }
        });
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        AppUpdater.getInstance().getNetManager().cancel(UpdateVersionShowDialog.this);
    }
}

