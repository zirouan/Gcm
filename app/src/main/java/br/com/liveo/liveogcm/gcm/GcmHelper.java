package br.com.liveo.liveogcm.gcm;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;

import java.io.IOException;

import br.com.liveo.liveogcm.R;
import br.com.liveo.liveogcm.util.Constant;
import retrofit.Call;
import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Retrofit;


public class GcmHelper {

    public static boolean googlePlayServicesIsAvailable(Activity activity) {

        int resultCode = GooglePlayServicesUtil.isGooglePlayServicesAvailable(activity);

        if (resultCode != ConnectionResult.SUCCESS) {
            if (GooglePlayServicesUtil.isUserRecoverableError(resultCode)) {

                GooglePlayServicesUtil.getErrorDialog(
                        resultCode, activity, Constant.REQUEST_CODE_GOOGLEPLAY).show();
                return true;
            }
            return false;
        }
        return true;
    }

    public static void register(Context ctx, TheRegisterDevice listener){
        String regId = readRegistrationId(ctx);

        try {
            if ("".equals(regId)) {
                GoogleCloudMessaging gcm = GoogleCloudMessaging.getInstance(ctx);
                registerBackground(ctx, gcm, listener);

            } else {
                listener.toRegister(regId, false);
            }
        }catch (Exception e){
            e.getStackTrace();
        }
    }

    private static String readRegistrationId(Context context) {

        final SharedPreferences prefs =  getGCMPreferences(context);
        String registrationId = prefs.getString(Constant.PROP_REG_ID, "");
        int registeredVersion = prefs.getInt(Constant.PROP_APP_VERSION, Integer.MIN_VALUE);
        int currentVersion = getAppVersion(context);

        if ("".equals(registrationId) || registeredVersion != currentVersion) {
            return "";
        }
        return registrationId;
    }

    private static void saveRegistrationId(Context context, String regId) {

        final SharedPreferences prefs = getGCMPreferences(context);

        int appVersion = getAppVersion(context);

        SharedPreferences.Editor editor = prefs.edit();
        editor.putString(Constant.PROP_REG_ID, regId);
        editor.putInt(Constant.PROP_APP_VERSION, appVersion);
        editor.apply();
    }

    private static void registerBackground(final Context context,
                                              final GoogleCloudMessaging gcm,
                                              final TheRegisterDevice listener) {

        new AsyncTask<Void, Void, String>() {
            @Override
            protected String doInBackground(Void... params) {

                String regid = null;
                try {
                    regid = gcm.register(context.getString(R.string.sender_id));
                } catch (IOException ex) {
                    ex.printStackTrace();
                }
                return regid;
            }

            @Override
            protected void onPostExecute(String key) {
                try {
                    toSendRegistrationIdServer(context, key, listener);
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }.execute();
    }

    private static void toSendRegistrationIdServer(final Context context, final String key,
                                                   final TheRegisterDevice listener) throws IOException {

        Retrofit retrofit = new Retrofit.Builder()
                .baseUrl(context.getString(R.string.sender_url))
                .addConverterFactory(GsonConverterFactory.create())
                .build();

        GcmRetrofit gcmRetrofit = retrofit.create(GcmRetrofit.class);
        Call<String> venuesCall = gcmRetrofit.toSendRegistration("registrar", key);
        venuesCall.enqueue(new Callback<String>() {

            @Override
            public void onResponse(retrofit.Response<String> response, Retrofit retrofit) {

                if (response != null){
                    saveRegistrationId(context, key);
                    listener.toRegister(key, true);
                }
            }

            @Override
            public void onFailure(Throwable t) {
            }
        });
    }

    private static SharedPreferences getGCMPreferences(Context context) {
        return context.getSharedPreferences(Constant.GCM, Context.MODE_PRIVATE);
    }

    private static int getAppVersion(Context context) {
        try {
            PackageManager pm = context.getPackageManager();
            PackageInfo packageInfo = pm.getPackageInfo(
                    context.getPackageName(), 0);
            return packageInfo.versionCode;

        } catch (PackageManager.NameNotFoundException e) {
            throw new RuntimeException(e);
        }
    }

    public interface TheRegisterDevice{
        void toRegister(String regId, boolean inBackground);
    }
}

