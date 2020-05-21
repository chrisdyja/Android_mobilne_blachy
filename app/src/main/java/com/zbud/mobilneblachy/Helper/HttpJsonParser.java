package com.zbud.mobilneblachy.Helper;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.ProtocolException;
import java.net.URL;
import java.util.HashMap;

import org.json.JSONException;
import org.json.JSONObject;

import android.net.Uri;
import android.util.Log;

public class HttpJsonParser {
    static InputStream inputStream = null;
    static JSONObject jsonObject = null;
    static String json = "";
    static HttpURLConnection urlConnection = null;

    public JSONObject makeHttpRequest(String url, String method,
                                      HashMap<String, String> params) {
        try {
            Uri.Builder builder = new Uri.Builder();
            URL urlObject;
            String encodedParams = "";

            if (params != null) {
                for (HashMap.Entry<String, String> entry : params.entrySet()) {
                    builder.appendQueryParameter(entry.getKey(), entry.getValue());
                }
            }
            if (builder.build().getEncodedQuery() != null) {
                encodedParams = builder.build().getEncodedQuery();
            }
            if("GET".equals(method)) {
                url = url + "?" + encodedParams;
                urlObject = new URL(url);
                urlConnection = (HttpURLConnection) urlObject.openConnection();
                urlConnection.setRequestMethod(method);
            } else {
                urlObject = new URL(url);
                urlConnection = (HttpURLConnection) urlObject.openConnection();
                urlConnection.setRequestMethod(method);
                urlConnection.setDoInput(true);
                urlConnection.getOutputStream().write(encodedParams.getBytes());
            }

            urlConnection.connect();
            inputStream = urlConnection.getInputStream();
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
            StringBuilder stringBuilder = new StringBuilder();
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line + "\n");
            }
            inputStream.close();
            json = stringBuilder.toString();
            Log.i("PHP-ERROR == ", "================================================");
            Log.i("PHP-ERROR", json);
            Log.i("PHP-ERROR === ", "===============================================");
            jsonObject = new JSONObject(json);

        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        } catch (ProtocolException e){
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            Log.e("JSON Parser", "Error parsing data " + e.toString());
        } catch (Exception e) {
            Log.e("Exception", "Error parsing data " + e.toString());
        }
        return jsonObject;
    }
}

