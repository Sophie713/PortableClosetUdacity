package com.sophie.miller.portablecloset.utils;

import android.os.AsyncTask;

import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;

/**
 * posted when user adds a new item
 */
public class ExampleAsyncTask extends AsyncTask<String, String, String> {

    private String doInBackgroundResponse = "" ;

    @Override
    protected String doInBackground(String... params) {
        try {
            URL url = new URL("https://www.google.com/");
            URLConnection con = url.openConnection();
            HttpURLConnection http = (HttpURLConnection) con;
            http.setRequestMethod("POST"); // PUT is another valid option
            http.setDoOutput(true);
            doInBackgroundResponse = "Success";
        } catch (Exception e) {
            e.printStackTrace();
            doInBackgroundResponse = e.getMessage();
        }
        return doInBackgroundResponse;
    }


    @Override
    protected void onPostExecute(String result) {
        Notifications.log(doInBackgroundResponse);
    }
}