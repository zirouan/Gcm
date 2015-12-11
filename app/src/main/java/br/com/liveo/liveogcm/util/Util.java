package br.com.liveo.liveogcm.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;

import java.nio.channels.NoConnectionPendingException;

/**
 * Created by Rudsonlive on 02/10/15.
 */
public class Util {

    public static final String REFRESH_DATA_INTENT = "REFRESH_DATA_INTENT";

    public static boolean checkConnection(Context context) throws NoConnectionPendingException {

        ConnectivityManager connectivity = (ConnectivityManager) context
                .getSystemService(Context.CONNECTIVITY_SERVICE);

        if (connectivity == null) {
            return false;
        } else {

            NetworkInfo info = connectivity.getActiveNetworkInfo();
            if (info != null) {
                if (info.getState() == NetworkInfo.State.CONNECTED) {
                    return true;
                }
            }
        }
        return false;
    }
}
