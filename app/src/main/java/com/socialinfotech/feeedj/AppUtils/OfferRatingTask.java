package com.socialinfotech.feeedj.AppUtils;

import android.os.AsyncTask;
import android.util.Log;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;

import static com.socialinfotech.feeedj.AppUtils.Utility.openConnectionForUrl;

/**
 * Created by fi8er1 on 20/07/2017.
 */

public class OfferRatingTask extends AsyncTask<Void, Void, Void> {

    HttpURLConnection connection;
    public static int responseCode;
    String[] dataArray;

    public OfferRatingTask(String[] asyncArray) {
        super();
        dataArray = asyncArray;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        String url;
        try {
            url = "https://feeedz.co/api/offers/offerlikes/" + dataArray[0];
            connection = openConnectionForUrl(url, "POST", dataArray[2]);
            DataOutputStream out = new DataOutputStream(connection.getOutputStream());
            out.writeBytes("rating:" + dataArray[1]);
            out.flush();
            out.close();
            responseCode = connection.getResponseCode();
            Log.e("TESTING LIKES", "" + connection.getURL());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
        Log.e("responseRating", "" + responseCode);
    }

}
