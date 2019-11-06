package com.socialinfotech.feeedj.AppUtils;

import android.os.AsyncTask;
import android.util.Log;

import java.io.IOException;
import java.net.HttpURLConnection;

import static com.socialinfotech.feeedj.AppUtils.Utility.openConnectionForUrl;

/**
 * Created by fi8er1 on 19/07/2017.
 */

public class OfferReportingTask extends AsyncTask<Void, Void, Void> {

    private HttpURLConnection connection;
    private static int responseCode;
    private int[] dataArray;
    private String url = null;

    public OfferReportingTask(int[] asyncArray) {
        super();
        dataArray = asyncArray;
    }

    @Override
    protected Void doInBackground(Void... voids) {
        try {
            if (dataArray[0] == 0) {
                url = "https://feeedz.co/api/offers/OfferViews/" + dataArray[1];
            } else if (dataArray[0] == 1) {
                url = "https://feeedz.co/api/offers/OfferImages/" + dataArray[1];
            } else if (dataArray[0] == 2) {
                url = "https://feeedz.co/api/offers/OfferCall/" + dataArray[1];
            } else if (dataArray[0] == 3) {
                url = "https://feeedz.co/api/offers/OfferLocation/" + dataArray[1];
            }
            connection = openConnectionForUrl(url, "POST", null);
            responseCode = connection.getResponseCode();
            Log.i("responseMessage", "" + connection.getResponseMessage());
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    protected void onPostExecute(Void aVoid) {
        super.onPostExecute(aVoid);
//        try {
//            Log.e("responseReport", "" + responseCode + " " + connection.getResponseMessage() + "   " + url);
//        } catch (IOException e) {
//            e.printStackTrace();
//        }
    }
}