package services;


import android.util.Log;

import com.fasterxml.jackson.databind.ObjectMapper;

import java.io.DataOutputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import config.AppConfig;
import content.GCMContent;

/**
 * Created by enclaveit on 1/25/16.
 */
public class APICallingService {

    public void sendPostMessage(String apiKey, GCMContent content) {
        HttpURLConnection urlConnection = null;

        try {

            URL url = new URL(AppConfig.GCM_API);

            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.getDoOutput();
            urlConnection.setConnectTimeout(5000);
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json");
            urlConnection.setRequestProperty("Authorization", "key=" + apiKey);
            urlConnection.connect();
            DataOutputStream dataOutputStream = new DataOutputStream(urlConnection.getOutputStream());
            Log.i("CONTENT", content.toString());
            ObjectMapper mapper = new ObjectMapper();
            mapper.writeValue(dataOutputStream, content);
            dataOutputStream.flush();
            dataOutputStream.close();

            int responseCode = urlConnection.getResponseCode();
            Log.i("Request Status", "This is response status from server: " + responseCode);
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            urlConnection.disconnect();
        }
    }


}
