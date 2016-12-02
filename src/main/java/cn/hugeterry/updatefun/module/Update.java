package cn.hugeterry.updatefun.module;

import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

import cn.hugeterry.updatefun.config.DownloadKey;
import cn.hugeterry.updatefun.config.UpdateKey;
import cn.hugeterry.updatefun.utils.Tools;

/**
 * Created by hugeterry(http://hugeterry.cn)
 * Date: 16/7/13 14:38
 */
public class Update extends Thread {

    private String mUpdateurl;
    public Update(String updateUrl){
        this.mUpdateurl = updateUrl;
    }

    private String result;
//    private String url = "http://api.fir.im/apps/latest/" + UpdateKey.APP_ID
//            + "?api_token=" + UpdateKey.API_TOKEN;

    public void run() {
        try {
            URL httpUrl = new URL(mUpdateurl);

            HttpURLConnection conn = (HttpURLConnection) httpUrl
                    .openConnection();
            conn.setRequestMethod("GET");
            conn.setReadTimeout(3000);

            if (conn.getResponseCode() == 200) {
                BufferedReader reader = new BufferedReader(new InputStreamReader(
                        conn.getInputStream()));
                StringBuffer sb = new StringBuffer();
                String str;

                while ((str = reader.readLine()) != null) {
                    sb.append(str);
                }
                result = new String(sb.toString().getBytes(), "utf-8");

                Log.i("OK","update:"+result);
                interpretingData(result);
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void interpretingData(String result) {
        try {
//            JSONObject object = new JSONObject(result);
//
//            Log.i("OK",object.toString());
//
//            DownloadKey.changeLog = object.getString("changelog");
//            DownloadKey.version = object.optInt("version");
//            DownloadKey.apkUrl = object.getString("installUrl");
//            DownloadKey.build = object.getString("build");
//            DownloadKey.versionName = object.getString("versionShort");
//            Log.i("UpdateFun TAG",
//                    String.format("ChangeLog:%s, Version:%s, ApkDownloadUrl:%s",
//                            DownloadKey.changeLog, DownloadKey.version, DownloadKey.apkUrl));



            try {
                JSONObject jobj = Tools.dealData(result);
                if (jobj == null) {
                    Log.i("OK","null");
                } else {
                    if ("Y".equals(jobj.optString("rtnType"))) {
                        JSONArray ja = new JSONArray(jobj.optString("result"));
                        JSONObject jo = (JSONObject) ja.get(0);
                        int versionCode = jo.optInt("version_num");
                        String apkUrl  = jo.optString("url");
                        DownloadKey.version = versionCode;
                        DownloadKey.apkUrl = apkUrl;
                    }
                }
            } catch (Exception e) {
					e.printStackTrace();
            }





        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
