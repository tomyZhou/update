package cn.hugeterry.updatefun.module;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import java.lang.ref.WeakReference;

import cn.hugeterry.updatefun.config.DownloadKey;
import cn.hugeterry.updatefun.utils.GetAppInfo;
import cn.hugeterry.updatefun.view.UpdateDialog;

/**
 * Created by hugeterry(http://hugeterry.cn)
 * Date: 16/8/19 10:44
 */
public class HandleUpdateResult implements Runnable {

    private int version = 0;
    private Up_handler up_handler;
    private static UpdateListener mUpdateListener;
    private static String mUpdateUrl;

    public HandleUpdateResult(Context context,String mUpdateUrl,UpdateListener listener) {
        up_handler = new Up_handler(context);
        this.version = GetAppInfo.getAppVersionCode(context);
        this.mUpdateListener = listener;
        this.mUpdateUrl = mUpdateUrl;
    }

    private static class Up_handler extends Handler {
        WeakReference<Context> mActivityReference;

        public Up_handler(Context context) {
            mActivityReference = new WeakReference<>(context);
        }

        @Override
        public void handleMessage(Message msg) {
            final Context context = mActivityReference.get();
            if (context != null) {
                switch (msg.arg1) {
                    case 1:
                        showNoticeDialog(context);

                        break;
                    case 2:
                        if (DownloadKey.ISManual) {
                            DownloadKey.LoadManual = false;
                            Toast.makeText(context, "网络不畅通", Toast.LENGTH_LONG).show();
                        }
                        break;
                    case 3:
                        if (DownloadKey.ISManual) {
                            DownloadKey.LoadManual = false;
                            Toast.makeText(context, "版本已是最新", Toast.LENGTH_LONG).show();
                        }
                        break;
                    default:
                        break;
                }
            }
        }
    }

    @Override
    public void run() {
        // 检测更新
        Update update = new Update(mUpdateUrl);
        update.start();

        Message msg = new Message();
        try {
            update.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

//        Log.i("OK","在线版本："+DownloadKey.version);
//        Log.i("OK","本地版本："+version);

        if (DownloadKey.apkUrl.equals("")) {
            Log.i("UpdateFun TAG", "获取的应用信息为空，不更新，请确认网络是否畅通或者应用ID及API_TOKEN是否正确");
            mUpdateListener.OnUpdateListener(2);
            msg.arg1 = 2;
            up_handler.sendMessage(msg);
        } else if (DownloadKey.version> version) {
            Log.i("UpdateFun TAG", "需更新版本"+"在线版本："+DownloadKey.version+"本地版本："+version);
            mUpdateListener.OnUpdateListener(1);
            msg.arg1 = 1;
            up_handler.sendMessage(msg);
        } else {
            Log.i("UpdateFun TAG", "版本已是最新");
            mUpdateListener.OnUpdateListener(3);
            msg.arg1 = 3;
            up_handler.sendMessage(msg);
        }
    }

    public static void showNoticeDialog(Context context) {
        Intent intent = new Intent();
        intent.setClass(context, UpdateDialog.class);
        ((Activity) context).startActivityForResult(intent, 100);
    }

}
