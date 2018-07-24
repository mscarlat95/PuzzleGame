package marius.scarlat.com.puzzlegame.network;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.util.Log;
import android.widget.Toast;

import java.io.IOException;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;

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

        /* Obtain player data */
        final String webServiceUrl = params[0];
        final String playerName = params[1];
        final String playerScore = params[2];

        URL url = null;
        HttpURLConnection urlConnection = null;

        try {
            /* Establish a new connection to the web server service */
            url = new URL(webServiceUrl);
            urlConnection = (HttpURLConnection) url.openConnection();
            urlConnection.setDoOutput(true);

            /* Add data */
            String data = URLEncoder.encode("name", "UTF-8") + "=" + URLEncoder.encode(playerName, "UTF-8");
            data += "&" + URLEncoder.encode("value", "UTF-8") + "=" + URLEncoder.encode(playerScore, "UTF-8");

            /* Publish */
            OutputStreamWriter wr = new OutputStreamWriter(urlConnection.getOutputStream());
            wr.write( data );
            wr.flush();

            /* Get result code */
            final String resultMessage = urlConnection.getResponseMessage();
            final int resultStatus = urlConnection.getResponseCode();

            Log.d(TAG, "doInBackground: Result Status " + resultStatus);
            Log.d(TAG, "doInBackground: Result Message " + resultMessage);

            return  resultStatus + ": " + resultMessage;

        } catch (IOException e) {
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

        /* Display UI message */
        if (result.equals(Constants.UNSET)) {
            Log.d(TAG, "onPostExecute: Score publishing didn't work");
        } else {
            Toast.makeText(context, result, Toast.LENGTH_SHORT).show();
        }
    }
}
