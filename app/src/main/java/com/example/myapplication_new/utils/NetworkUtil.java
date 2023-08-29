package com.example.myapplication_new.utils;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.util.Log;
import android.view.View;

import androidx.annotation.Nullable;

import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.Map;

public class NetworkUtil {
    public static final String TAG = "---NetworkUtil";

    /**
     * click to visit web text
     * be careful not to use in main UI Thread since network request will block it, resulting in app collapse
     * @param strUrlPath visited web site
     */
    public static String getHttpText(String strUrlPath){
        String res = null;
        //HttpURLConnection
        try {
            URL url = new URL(strUrlPath);
            HttpURLConnection connection = (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(5*1000);
            connection.setDoInput(true);
            connection.setRequestMethod("GET");
            connection.connect();
            //get requested data(stream)
            InputStream inputStream = connection.getInputStream();
            res = InputStreamUtils.parseIsToString(inputStream);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        return res;
    }
    /*
     * Function : 发送Post请求到服务器 Param : params请求体内容，encode编码格式
     */
    public static String submitPostData(String strUrlPath, Map<String, String> params) {
        byte[] data;
        String result = "";
        if (params!=null){
            String requestData = new JSONObject(params).toString();
            //LogUtil.e("origin requestData="+requestData);
/*            requestData ="{\"content\":\""+ RSAUtil.encryptByPublicKey(requestData)+"\"}";*/
            requestData ="{\"content\":\""+ requestData+"\"}";
            Log.e(TAG, "submitPostData: url = "+strUrlPath+" \n requestData= "+requestData );
/*            LogUtil.e("submitPostData: url="+strUrlPath + "\nrequestData="+requestData);*/
            data = requestData.getBytes();// 获得请求体
        }else{
            data = new byte[0];
/*            LogUtil.e("submitPostData: url="+strUrlPath + "\nrequestData=null");*/
            Log.e(TAG, "submitPostData: url = "+strUrlPath+" \n requestData=null " );
        }
        try {
            URL url = new URL(strUrlPath);
            HttpURLConnection httpURLConnection = (HttpURLConnection) url.openConnection();
            httpURLConnection.setConnectTimeout(20000); // 设置连接超时时间
            httpURLConnection.setDoInput(true); // 打开输入流，以便从服务器获取数据
            httpURLConnection.setDoOutput(true); // 打开输出流，以便向服务器提交数据
            httpURLConnection.setRequestMethod("POST"); // 设置以Post方式提交数据
            httpURLConnection.setUseCaches(false); // 使用Post方式不能使用缓存
            httpURLConnection.setRequestProperty("Content-Type", "text/plain");// 设置请求体的类型是文本类型
            httpURLConnection.setRequestProperty("Content-Length", String.valueOf(data.length));// 设置请求体的长度
            httpURLConnection.connect();

            OutputStream outputStream = httpURLConnection.getOutputStream();// 获得输出流，向服务器写入数据
            outputStream.write(data);
            outputStream.flush();
            outputStream.close();
            int response = httpURLConnection.getResponseCode(); // 获得服务器的响应码
            if (response == HttpURLConnection.HTTP_OK) {
/*                LogUtil.e("---NetworkUtil","response:" +response);*/
                Log.e(TAG, "response: "+response);
                InputStream inputStream = httpURLConnection.getInputStream();
/*                result = RSAUtil.decryptByPublicKey(dealResponseResult(inputStream)) ;*/ // TODO: 2023/8/15  
                //LogUtil.e("---NetworkUtil","result:" +response);
            }else{
                result = "error:response="+response+"; responseMessage="+httpURLConnection.getResponseMessage();
            }
        } catch (IOException e) {
            e.printStackTrace();
            result = "NetWorkUtil. exception:"+e.toString();
/*            LogUtil.e("---NetworkUtil","IOException:" +result);*/
            Log.e(TAG, "IOException: "+result );
        }
/*        LogUtil.e(result);*/
        Log.e(TAG, "submitPostData: result="+result);
        return result;
    }

    /*
     * Function : 封装请求体信息 Param : params请求体内容，encode编码格式
     */
    public static StringBuffer getRequestData(Map<String, String> params, String encode) {
        StringBuffer stringBuffer = new StringBuffer(); // 存储封装好的请求体信息
        try {
            for (Map.Entry<String, String> entry : params.entrySet()) {
                stringBuffer.append(entry.getKey()).append("=")
                        .append(URLEncoder.encode(entry.getValue(), encode))
                        .append("&");
            }
            stringBuffer.deleteCharAt(stringBuffer.length() - 1); // 删除最后的一个"&"
        } catch (Exception e) {
            e.printStackTrace();
        }
        return stringBuffer;
    }
    /*
     * Function : 处理服务器的响应结果（将输入流转化成字符串） Param : inputStream服务器的响应输入流
     */
    public static String dealResponseResult(InputStream inputStream) {
        String resultData; // 存储处理结果
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        byte[] data = new byte[1024];
        int len = 0;
        try {
            while ((len = inputStream.read(data)) != -1) {
                byteArrayOutputStream.write(data, 0, len);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        resultData = new String(byteArrayOutputStream.toByteArray());
/*        LogUtil.e(TAG, "dealResponseResult: resultData :" + resultData );*/
        Log.e(TAG, "dealResponseResult: resultData:"+ resultData  );
        return resultData;
    }


    /*
     * 获取网络上的图片
     * @Nullable 注解表示参数可以为null
     */
    public static @Nullable
    Drawable getHTTPImage(String urlStr, Resources resources){
        Bitmap bitmap = null;
        InputStream is = null;
        HttpURLConnection connection = null;
        try
        {
            URL url = new URL(urlStr);
            connection= (HttpURLConnection) url.openConnection();
            connection.setConnectTimeout(3*1000);
            connection.setDoInput(true);
            connection.connect();
            int responseCode = connection.getResponseCode();
            if (responseCode >= 300) {
                connection.disconnect();
                Log.e(TAG, "dz[httpURLConnectionGet 300]");
                return null;
            }
            //得到输入流
            is = connection.getInputStream();
            bitmap = BitmapFactory.decodeStream(is);
            bitmap = CommonUtil.getRoundedBitmap(resources,bitmap);
            is.close();
        } catch (Exception e)
        {
            e.printStackTrace();
        }finally {
            if (connection!=null) connection.disconnect();
        }

        return bitmap==null?null:new BitmapDrawable(resources,bitmap);
    }
}
