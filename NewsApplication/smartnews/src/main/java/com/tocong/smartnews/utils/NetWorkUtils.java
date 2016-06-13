package com.tocong.smartnews.utils;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;

/**
 * Created by tocong on 2016/6/13.
 */
public class NetWorkUtils {

    public static String getMethod(String url) throws IOException {
        BufferedReader bufferedReader = null;
        String result = "";
        String line;
        try {
            URL urlAddress = new URL(url);
            HttpURLConnection conn = (HttpURLConnection) urlAddress.openConnection();
            conn.setConnectTimeout(5 * 1000);
            conn.setReadTimeout(5*1000);
            conn.setRequestMethod("GET");
            conn.connect();
            if (conn.getResponseCode() == 200) {
                bufferedReader = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                while ((line = bufferedReader.readLine()) != null) {
                    result += "\n" + line;

                }


            }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (bufferedReader != null) {
                bufferedReader.close();
            }
        }
        return result;
    }
}
