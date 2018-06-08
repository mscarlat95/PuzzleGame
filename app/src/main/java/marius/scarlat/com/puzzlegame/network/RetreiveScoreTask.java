package marius.scarlat.com.puzzlegame.network;


import android.app.ProgressDialog;
import android.content.Context;
import android.os.AsyncTask;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;

import marius.scarlat.com.puzzlegame.general.Constants;
import marius.scarlat.com.puzzlegame.views_adapters.ScoreRecyclerViewAdapter;

public class RetreiveScoreTask extends AsyncTask<   String,     /* Parameters Type */
                                                    Void,       /* Progress Type */
                                                    String> {   /* Result Type */

    private static final String TAG = "RetreiveScoreTask";

    private ArrayList<String> users = new ArrayList<>();
    private ArrayList<Double> scores = new ArrayList<>();

    private Context context;
    private ScoreRecyclerViewAdapter adapter;
    private RecyclerView recyclerView;
    private ProgressDialog progressDialog;

    public RetreiveScoreTask(Context context, RecyclerView recyclerView, ScoreRecyclerViewAdapter adapter) {
        this.context = context;
        this.recyclerView = recyclerView;
        this.adapter = adapter;
    }

    @Override
    protected void onPreExecute() {
        progressDialog = new ProgressDialog(context);
        progressDialog.setMessage("Retrieve scores from server");
        progressDialog.show();
    }

    @Override
    protected String doInBackground(String... params) {

        String result = "";
        URL url = null;
        HttpURLConnection urlConnection = null;

        try {
            /* Establish a new connection to the web server service */
            url = new URL(params[0]);
            urlConnection = (HttpURLConnection) url.openConnection();

            InputStream inputStream = urlConnection.getInputStream();
            InputStreamReader reader = new InputStreamReader(inputStream);

            /* Retreive data from the input streamer */
            int data = reader.read();
            while (data != -1) {
                char current = (char) data;

                result += current;
                data = reader.read();
            }

            /* Result will be processed in the onPostExecute method */
            Log.d(TAG, "Received from server: " + result);
            return result;

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
    protected void onPostExecute(String s) {
        if (progressDialog.isShowing()) {
            progressDialog.dismiss();
        }

        if (s.equals(Constants.UNSET)) {
            Toast.makeText(context, "Unable to retrieve score", Toast.LENGTH_SHORT).show();
            return;
        }

        /* Extract information from JSON data */
        parseScoreInformation(s);

        /* Update UI */
        displayScores();
    }

    private void parseScoreInformation(String scoreInformation) {
        JSONArray resultArr = null;

        try {
            if (! new JSONObject(scoreInformation).isNull("error")) {
                Log.d(TAG, "parseScoreInformation: JSON result has errors");
                return;
            }

            resultArr = new JSONObject(scoreInformation).getJSONArray("result");
            for (int i = 0; i < resultArr.length(); ++i) {
                JSONObject item = resultArr.getJSONObject(i);

                users.add(item.getString("name"));
                scores.add(item.getDouble("value"));
            }

//            Log.d(TAG, "Received users: " + users.toString());
//            Log.d(TAG, "Received scores: " + scores.toString());

        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void displayScores() {
        adapter.setUsers(users);
        adapter.setScores(scores);

        recyclerView.setLayoutManager(new LinearLayoutManager(context));
        recyclerView.setAdapter(adapter);
    }

}
