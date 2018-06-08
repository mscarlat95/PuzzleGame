package marius.scarlat.com.puzzlegame.network;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

import marius.scarlat.com.puzzlegame.general.Constants;

public class PublishScoreTask extends AsyncTask<    String,     /* Parameters Type */
                                                    Void,       /* Progress Type */
                                                    String> {   /* Result Type */

    private static final String TAG = "PublishScoreTask";

    private Context context;
    private ProgressDialog progressDialog;

    public PublishScoreTask(Context context) {
        this.context = context;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Publishing user score");
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {

        URL url = null;
        HttpURLConnection urlConnection = null;

        try {

            /* Obtain player data */
            final String playerName = params[1];
            final String playerScore = params[2];

            /* Establish a new connection to the web server service */
            url = new URL(params[0]);
            urlConnection = (HttpURLConnection) url.openConnection();

            /* Configure client */
            urlConnection.setRequestMethod("POST");
            urlConnection.setRequestProperty("Content-Type", "application/json;charset=UTF-8");
            urlConnection.setRequestProperty("Accept","application/json");
            urlConnection.setDoOutput(true);
            urlConnection.setDoInput(true);

            /* Add data */
            JSONObject jsonObj = new JSONObject();
            jsonObj.put("name", playerName);
            jsonObj.put("value", playerScore);
            Log.d(TAG, "doInBackground: Sending JSON Obj = " + jsonObj.toString());

            /* Publish */
            DataOutputStream outputStream = new DataOutputStream(urlConnection.getOutputStream());
            outputStream.writeBytes(jsonObj.toString() + "/");
            outputStream.flush();
            outputStream.close();

            /* Get result code */
            final String resultMessage = urlConnection.getResponseMessage();
            final int resultStatus = urlConnection.getResponseCode();

            Log.d(TAG, "doInBackground: Result Status " + resultStatus);
            Log.d(TAG, "doInBackground: Result Message " + resultMessage);

            return  resultMessage;

        } catch (IOException e) {
            e.printStackTrace();

        } catch (JSONException e) {
            e.printStackTrace();
        } finally {
            if (urlConnection != null) {
                urlConnection.disconnect();
            }
        }

        return Constants.UNSET;
    }


    @Override
    protected void onPostExecute(String result) {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        if (result.equals(Constants.UNSET)) {
            Log.d(TAG, "onPostExecute: Score publishing didn't work");
        }

    }
}
