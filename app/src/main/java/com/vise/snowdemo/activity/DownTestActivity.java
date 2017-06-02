package com.vise.snowdemo.activity;

import android.Manifest;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.vise.log.ViseLog;
import com.vise.snowdemo.R;
import com.vise.utils.view.DialogUtil;
import com.vise.xsnow.net.ViseNet;
import com.vise.xsnow.net.callback.ACallback;
import com.vise.xsnow.net.callback.UCallback;
import com.vise.xsnow.net.mode.DownProgress;
import com.vise.xsnow.permission.OnPermissionCallback;
import com.vise.xsnow.permission.PermissionManager;
import com.vise.xsnow.ui.BaseActivity;

import java.io.File;

/**
 * @Description: 下载展示
 * @author: <a href="http://www.xiaoyaoyou1212.com">DAWI</a>
 * @date: 17/1/18 23:06.
 */
public class DownTestActivity extends BaseActivity {

    private Button mDownload_btn;
    private ProgressBar mDownload_progress;
    private TextView mDownload_progress_desc;
    private Button mUpload_btn;
    private ProgressBar mUpload_progress;
    private TextView mUpload_progress_desc;

    private String saveName = "weixin.apk";

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_down_test);
    }

    @Override
    protected void initView() {
        mDownload_btn = F(R.id.download_btn);
        mDownload_progress = F(R.id.download_progress);
        mDownload_progress_desc = F(R.id.download_progress_desc);
        mUpload_btn = F(R.id.upload_btn);
        mUpload_progress = F(R.id.upload_progress);
        mUpload_progress_desc = F(R.id.upload_progress_desc);
    }

    @Override
    protected void bindEvent() {
        C(mDownload_btn);
        C(mUpload_btn);
    }

    @Override
    protected void initData() {
        mUpload_btn.setClickable(false);
        PermissionManager.instance().with(this).request(new OnPermissionCallback() {
            @Override
            public void onRequestAllow(String permissionName) {
                DialogUtil.showTips(mContext, "权限控制", "已经授权！\n" + permissionName);
            }

            @Override
            public void onRequestRefuse(String permissionName) {
                DialogUtil.showTips(mContext, "权限控制", "拒绝授权，提示请求许可理由！\n" + permissionName);
            }

            @Override
            public void onRequestNoAsk(String permissionName) {
                DialogUtil.showTips(mContext, "权限控制", "拒绝授权，不在提醒！\n" + permissionName);
            }
        }, Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE);
    }

    @Override
    protected void processClick(View view) {
        switch (view.getId()) {
            case R.id.download_btn:
                download();
                break;
            case R.id.upload_btn:
                upload();
                break;
        }
    }

    private void upload() {
        ViseNet.UPLOAD(new UCallback() {
            @Override
            public void onProgress(long currentLength, long totalLength, float percent) {

            }

            @Override
            public void onFail(int errCode, String errMsg) {

            }
        }).addFile("", new File("")).suffixUrl("").request(mContext, new ACallback<Object>() {
            @Override
            public void onSuccess(Object data) {

            }

            @Override
            public void onFail(int errCode, String errMsg) {

            }
        });
    }

    private void download() {
        mDownload_btn.setClickable(false);
        ViseNet.DOWNLOAD()
                .baseUrl("http://dldir1.qq.com/")
                .suffixUrl("weixin/android/weixin6330android920.apk")
                .setFileName(saveName)
                .request(mContext, new ACallback<DownProgress>() {
                    @Override
                    public void onSuccess(DownProgress downProgress) {
                        if (downProgress == null) {
                            return;
                        }
                        ViseLog.i("down progress currentLength:" + downProgress.getDownloadSize() + ",totalLength:" + downProgress.getTotalSize());
                        mDownload_progress.setProgress((int) (downProgress.getDownloadSize() * 100 / downProgress.getTotalSize()));
                        mDownload_progress_desc.setText(downProgress.getPercent());
                        if (downProgress.isDownComplete()) {
                            mDownload_btn.setClickable(true);
                        }
                    }

                    @Override
                    public void onFail(int errCode, String errMsg) {
                        ViseLog.i("down errorCode:" + errCode + ",errorMsg:" + errMsg);
                        mDownload_btn.setClickable(true);
                    }
                });
    }
}
