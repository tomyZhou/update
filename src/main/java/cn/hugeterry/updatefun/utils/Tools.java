package cn.hugeterry.updatefun.utils;

import android.app.Activity;
import android.content.Intent;
import android.view.Gravity;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.Random;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Tools {
    private long lastClickTime;
    private String key = "tdddes01";
    //	private String key ="jlwdes";
    public final static int TOAST_OF_WARING = 1;
    public final static int TOAST_OF_ERROR = 2;
    public final static int TOAST_OF_SUCCESS = 0;

    public Tools() {

    }




    // 跳转到下一画面,关闭上一画面
    public static void JumpToNextActivity(Activity mActivity, Class<?> cls) {
        Intent intent01 = new Intent(mActivity, cls);
        mActivity.startActivity(intent01);
        mActivity.finish();
    }

    // 跳转到下一画面，不关闭上一画面
    public static void JumpToNextActivityNot(Activity mActivity, Class<?> cls) {
        Intent intent01 = new Intent(mActivity, cls);
        mActivity.startActivity(intent01);
    }

    // 不允许连续点击
    public boolean isFastDoubleClick() {
        long time = System.currentTimeMillis();
        long timeD = time - lastClickTime;
        if (0 < timeD && timeD < 1000) {
            return true;
        }
        lastClickTime = time;
        return false;
    }



    // 去除json字符串两端的“[” 与 “]”
    public static String SubString(String data) {
        return data.substring(1, data.length() - 1);
    }

    // 绘制HTML
    public String drawHtml(String body) {
        StringBuilder data = new StringBuilder("<html><body>");
        data.append(body);
        data.append("</body></html>");
        return data.toString();
    }

    // 连接后台返回数据处理
    public static JSONObject dealData(String content) {
        if (content == null || "".equals(content)) {
            return null;
        } else {
            try {
                content.getBytes();
                JSONObject jobj = new JSONObject(content);
//				String result = decrypt(jobj.optString("value"));
                String result = SubString(jobj.optString("value"));
                jobj = new JSONObject(result);

                return jobj;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    public JSONObject dealData2(String content) {
        if (content == null || "".equals(content)) {
            return null;
        } else {
            try {
                content.getBytes();
                JSONObject jobj = new JSONObject(content);
//				String result = decrypt(jobj.optString("value"));
                String result = SubString(jobj.optString("result"));
                jobj = new JSONObject(result);

                return jobj;
            } catch (JSONException e) {
                e.printStackTrace();
                return null;
            }
        }
    }

    // 金额数据显示处理
    public Double formatPrice(Double price) {
        /*
		 * 第一种方法
		 */
        DecimalFormat df = new DecimalFormat("######0.00");
        price = Double.parseDouble(df.format(price));
        return price;
    }

    //递归删除文件和文件夹
    public void DeleteFile(File file) {
        if (file.exists() == false) {
            return;
        } else {
            if (file.isFile()) {
                file.delete();
                return;
            }
            if (file.isDirectory()) {
                File[] childFile = file.listFiles();
                if (childFile == null || childFile.length == 0) {
                    file.delete();
                    return;
                }
                for (File f : childFile) {
                    DeleteFile(f);
                }
                file.delete();
            }
        }
    }

    public static String unicodeStr(String data) {
        String result = "";
        try {

            result = URLEncoder.encode(data);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return result;
    }

    public String strtoDdate(String time) {

        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        String dateString = formatter.format(time);
        return dateString;
    }

    public String getEcsalt() {
        Random r=new Random();
        int tag[]={0,0,0,0,0,0,0,0,0,0};
        String number="";
        int temp=0;
        while(number.length()!=4){
            temp=r.nextInt(10);//随机获取0~9的数字
            if(tag[temp]==0){
                number+=temp;
                tag[temp]=1;
            }
        }
        return number;
    }

    /**
     * 转换获取html  src   img
     *
     * @param imgString
     * @return
     */
    public static String filterImgSrc(String imgString) {
        String patternStr = "<\\s*img\\s*(?:[^>]*)src\\s*=\\s*([^>]+)";
        Pattern pattern = Pattern.compile(patternStr);
        Matcher matcher = pattern.matcher(imgString);
        String returnString = null;
        while (matcher.find()) {
            String group = matcher.group(1);
            if (group == null) {
                continue;
            }
            // 这里可能还需要更复杂的判断,用以处理src="...."内的一些转义符
            if (group.startsWith("'")) {
                returnString = group.substring(1, group.indexOf("'", 1));
            } else if (group.startsWith("\"")) {
                returnString = group.substring(1, group.indexOf("\"", 1));
            } else {
                returnString = group.split("\\s")[0];
            }
        }
        return returnString;
    }

    //校验数字 ^(([1-9]+)|([0-9]+\.[0-9]{1,2}))$
    public static boolean checkType(String str) {

        Pattern p = null;
        Matcher m = null;
        boolean b = false;
        p = Pattern.compile("^([1-9]+)|^([1-9]{1})([0-9]+)|^([0-9]/d*)/.(/d+)$");
//		p = Pattern.compile("^(0|[1-9]/d*)|(([1-9]+)|([0-9]+\\.[0-9]{1,2}))$");
        m = p.matcher(str);
        b = m.matches();
        return b;
    }

}

