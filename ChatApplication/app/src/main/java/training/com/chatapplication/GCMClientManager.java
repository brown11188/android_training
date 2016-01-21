package training.com.chatapplication;

/**
 * Created by enclaveit on 1/21/16.
 */
import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.AsyncTask;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.GoogleApiAvailability;
import com.google.android.gms.common.GooglePlayServicesUtil;
import com.google.android.gms.gcm.GoogleCloudMessaging;
import com.google.android.gms.iid.InstanceID;

import java.io.IOException;

public class GCMClientManager {
    // Constants
    public static final String TAG = "GCMClientManager";
    public static final String EXTRA_MESSAGE = "message";
    public static final String PROPERTY_REG_ID = "registration_id";
    private static final String PROPERTY_APP_VERSION = "appVersion";
    private final static int PLAY_SERVICES_RESOLUTION_REQUEST = 9000;

    private GoogleCloudMessaging gcm;
    private String regId;
    private String projectNumber;
    private Activity activity;

    public GCMClientManager(Activity activity, String projectNumber) {
        this.activity = activity;
        this.projectNumber = projectNumber;
        this.gcm = GoogleCloudMessaging.getInstance(activity);
    }

    private static int getAppVersion(Context context) {
        try {
            PackageInfo packageInfo = context.getPackageManager()
                    .getPackageInfo(context.getPackageName(), 0);
            return packageInfo.versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            // should never happen
            throw new RuntimeException("Could not get package name: " + e);
        }
    }


    public void registerIfNeeded(final RegistrationCompletedHandler handler) {
        if(checkPlayServices()) {
            regId = getRegistrationId(getContext());
            if(regId.isEmpty()) {
                registerInBackground(handler);
            } else {
                Log.d(TAG, "if needed" + regId );
                handler.onSuccess(regId, false);
            }
        } else {
            Log.i(TAG, "No valid Google Play Services APK found! ");
        }
    }

    private void registerInBackground(final RegistrationCompletedHandler handler) {
        new AsyncTask<Void, Void, String>() {

            @Override
            protected String doInBackground(Void... params) {
                try {
                if(gcm == null) {
                    gcm = GoogleCloudMessaging.getInstance(getContext());
                }
                    InstanceID instanceID = InstanceID.getInstance(getContext());
                    regId = instanceID.getToken(projectNumber, GoogleCloudMessaging.INSTANCE_ID_SCOPE, null);
                    Log.i(TAG, "background" + regId);
                } catch (IOException e) {
                    handler.onFailure("Error :" + e.getMessage());
                }
                return regId;
            }

            @Override
            protected void onPostExecute(String regId) {
                if(regId != null) {
                    handler.onSuccess(regId, true);
                }

            }
        }.execute(null, null, null);
    }

    private String getRegistrationId(Context context) {
        final SharedPreferences prefs = getGCMPreferences(context);
        String registrationId = prefs.getString(PROPERTY_REG_ID,"");
        if(registrationId.isEmpty()){
            Log.i(TAG, "Registration not found");
            return "";
        }
        int registeredVersion = prefs.getInt(PROPERTY_APP_VERSION, Integer.MIN_VALUE);
        Log.i("APP VERSION", registeredVersion + "");
        int currentVersion = getAppVersion(context);
        if (registeredVersion != currentVersion) {
            Log.i(TAG, "App version changed.");
            return "";
        }
        return registrationId;
    }

    private SharedPreferences getGCMPreferences(Context context) {
        return getContext().getSharedPreferences(context.getPackageName(),Context.MODE_PRIVATE);
    }

    private Context getContext() {
        return activity;
    }

    private Activity getActivity() {
        return activity;
    }

    private boolean checkPlayServices() {
        int resultCode = GoogleApiAvailability.getInstance().isGooglePlayServicesAvailable(getContext());
        if(resultCode != ConnectionResult.SUCCESS){
            if(GoogleApiAvailability.getInstance().isUserResolvableError(resultCode)){
                GoogleApiAvailability.getInstance().getErrorDialog(getActivity(), resultCode , PLAY_SERVICES_RESOLUTION_REQUEST).show();
            } else {
                Log.i(TAG, "This device is not supported Google Play Services");
            }
            return false;
        }
        return true;
    }

    public static abstract class RegistrationCompletedHandler {
        public abstract void onSuccess(String registrationId, boolean isNewRegistration);
        public void onFailure(String ex){
            Log.e(TAG, ex);
        }
    }
}
