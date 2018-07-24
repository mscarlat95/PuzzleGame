package marius.scarlat.com.puzzlegame.general;


import android.util.Log;

public class ConvertTime {

    private static final String TAG = "ConvertTime";

    public static double encodeTime(long millis) {
        Log.d(TAG, "encodeTime: Method was invoked!");

        int seconds = (int) (millis / 1000) % 60 ;
        int minutes = (int) ((millis / (1000*60)) % 60);

        return Double.parseDouble(minutes + "." + seconds);
    }

    public static String decodeTime(double value) {
        Log.d(TAG, "decodeTime: Method was invoked!");

        if (value < 0) {
            Log.d(TAG, "decodeTime: Time is undefined");
            return Constants.UNSET;
        }

        int minutes = (int) value;
        int seconds = (int) ((value - minutes) * 100);

        return String.format("%d:%d", minutes, seconds);
    }
}
