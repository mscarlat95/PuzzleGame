package marius.scarlat.com.puzzlegame.general;


import android.util.Log;

public class ConvertTime {

    private static final String TAG = "ConvertTime";

    public static double encodeTime(long millis) {
        Log.d(TAG, "encodeTime: Method was invoked!");

        if (millis < 0) {
            Log.d(TAG, "encodeTime: Time is undefined");
            return -1.0;
        }

        double result = ((double) millis / 1000 / 3600);

        return result;
//        return Double.valueOf(String.format("%.3f", result));
    }

    public static String decodeTime(double value) {
        Log.d(TAG, "decodeTime: Method was invoked!");

        if (value < 0) {
            Log.d(TAG, "decodeTime: Time is undefined");
            return Constants.UNSET;
        }

        int milliseconds = (int) ((value * 3600) * 1000);
        int seconds = (int) (milliseconds / 1000) % 60 ;
        int minutes = (int) ((milliseconds / (1000*60)) % 60);

        return String.format("%d:%d", minutes, seconds);
    }
}
