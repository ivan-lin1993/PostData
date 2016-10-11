package com.ivan.postdata;

import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.os.AsyncTask;
import android.util.Base64;
import android.util.Log;

import java.io.BufferedReader;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

/**
 * Created by Ivan Lin on 2016/10/10.
 */

public class PostData extends AsyncTask<String,Void,Void> {
    private AsyncResponse delegate = null;
    private Context thisContext;

    private ProgressDialog pDialog;
    private HttpURLConnection conn;
    private String param;
    private String result;
    private String loadstr = null;

    public interface AsyncResponse {
        void processFinish(String output);
    }

    public PostData(Context c, AsyncResponse delegate) {
        thisContext = c;
        this.delegate = delegate;
        param = "";
        result = null;
    }

    public PostData(Context c, String _loadstr, AsyncResponse delegate) {
        thisContext = c;
        this.delegate = delegate;
        param = "";
        result = null;
        loadstr = _loadstr;
    }

    @Override
    protected void onPreExecute() {
        super.onPreExecute();
        if (loadstr != null) {
            pDialog = new ProgressDialog(thisContext);
            pDialog.setMessage(loadstr);
            pDialog.setIndeterminate(false);
            pDialog.setCancelable(false);
            pDialog.show();
        }
    }

    @Override
    protected void onPostExecute(Void parm) {
        super.onPostExecute(parm);
        if (loadstr != null) pDialog.dismiss();
        Log.e("result", result);
        delegate.processFinish(result);
    }

    @Override
    protected Void doInBackground(String... params) {
        for (String urlstr : params) {
            try {
//Create connection
                URL url = new URL(urlstr);
                conn = (HttpURLConnection) url.openConnection();
                conn.setRequestMethod("POST");
                conn.setRequestProperty("Content-Type",
                        "application/x-www-form-urlencoded");
                conn.setRequestProperty("Content-Length", "" +
                        Integer.toString(param.getBytes().length));
                conn.setRequestProperty("Content-Language", "UTF-8");
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                conn.setRequestProperty("X-Requested-With", "XMLHttpRequest");
                conn.setUseCaches(false);
                conn.setDoInput(true);
                conn.setDoOutput(true);
//Send request
                DataOutputStream wr = new DataOutputStream(
                        conn.getOutputStream());
                wr.writeBytes(param);
                wr.flush();
                wr.close();
//Get Response
                try {
                    BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream(), "UTF-8"));
                    result = rd.readLine();
                    rd.close();
                } catch (Exception e) {
                    result = "連線逾時....";
                }
            } catch (Exception e) {
                e.printStackTrace();
                result = "連線逾時....";
                return null;
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }
            }
        }
        return null;
    }

    static public void addParms(String param, String key, String value) {
        String t_value = null;
        try {
            t_value = URLEncoder.encode(value, "UTF-8");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
        if (param != "") {
            param += "&";
        }
        param += (key + "=" + t_value);
    }

    static public void addParms(String param,String key, int value) {
        int t_value;
        t_value = value;
        if (param != "") {
            param += "&";
        }
        param += (key + "=" + t_value);
    }

    public String Image2String(Bitmap bmp) {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodeImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodeImage;
    }

    public String getParam(){
        return param;
    }
    public void ClearParam(){
        param="";
    }
}
