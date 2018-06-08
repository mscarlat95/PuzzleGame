package marius.scarlat.com.puzzlegame.network;


import android.content.Context;
import android.net.ConnectivityManager;
import android.util.Log;
import android.widget.Toast;

public class NetworkConnection {
    private static final String TAG = "NetworkConnection";

    public static boolean isAvailable(Context context) {
        Log.d(TAG, "isAvailable: Method was invoked!");

        ConnectivityManager connectivityManager = (ConnectivityManager) context
                                                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivityManager.getActiveNetworkInfo() != null) {
            Log.d(TAG, "Network is available");
            return true;
        }

        Log.d(TAG, "Network is not available");
        Toast.makeText(context, "You must connect to the internet!", Toast.LENGTH_SHORT).show();
        return false;
    }

}
