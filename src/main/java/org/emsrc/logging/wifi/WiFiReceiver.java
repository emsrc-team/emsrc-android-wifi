package org.emsrc.logging.wifi;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.util.Log;

import org.emsrc.helper.HashFactory;

/**
 * COPIED FROM PHONESTUDY
 */
public class WiFiReceiver extends BroadcastReceiver {
    public static String TAG = "WiFiReceiver";

    //final AppDatabaseHelper appDatabaseHelper = new AppDatabaseHelper();
    private WifiState lastState = WifiState.UNKNOWN;

    @Override
    public void onReceive(Context context, Intent intent) {
        // check if logging should be running
//        if (!ParamsLogging.loggingActive){
//            return;
//        }

        long timestamp = System.currentTimeMillis();

        // TODO create log entry instance + save
//        UsageActivity lastUsageActivity = appDatabaseHelper.getLastUsageActivity(UsageActivityName.WIFI);
//        if (lastUsageActivity != null) {
//            lastState = WifiState.fromString(lastUsageActivity.getEvent());
//        }

        String intentType = intent.getAction();

        if (intentType.equals(WifiManager.WIFI_STATE_CHANGED_ACTION)) {
            //find out if Wifi was enabled or disabled
            int newWifiState = intent.getIntExtra(WifiManager.EXTRA_WIFI_STATE, WifiManager.WIFI_STATE_UNKNOWN);
            switch (newWifiState) {
                case WifiManager.WIFI_STATE_ENABLED:
                    if (lastState != WifiState.ENABLED_DISCONNECTED) {
                        Log.i(TAG, "Wifi was enabled.");
                        saveEntry(WifiState.ENABLED_DISCONNECTED, timestamp);
                    }
                    break;
                case WifiManager.WIFI_STATE_DISABLED:
                    if (lastState != WifiState.DISABLED) {
                        Log.i(TAG, "Wifi was disabled.");
                        saveEntry(WifiState.DISABLED, timestamp);
                    }
                    break;
                default:
                    //ignore cases ENABLING, DISABLING, UNKNOWN
                    Log.i(TAG, "Wifi is enabling, disabling or unknown - ignoring state change");
            }
        } else if (intentType.equals(WifiManager.NETWORK_STATE_CHANGED_ACTION)) {
            //find out if Wifi was connected or disconnected
            NetworkInfo info = intent.getParcelableExtra(WifiManager.EXTRA_NETWORK_INFO);
            NetworkInfo.DetailedState newState = info.getDetailedState();

            switch (newState) {
                case CONNECTED:
                    if (lastState != WifiState.ENABLED_CONNECTED) {
                        String ssid = getWifiName(context);
                        if(ssid == null ||  ssid.equalsIgnoreCase("<unknown ssid>")){
                            return;
                        }
                        Log.i(TAG, "Wifi Connection was established. SSID: " + ssid);
                        saveEntry(WifiState.ENABLED_CONNECTED, new HashFactory(context).getHash(ssid), timestamp);
                        break;
                    }
                case DISCONNECTED:
                    if (lastState != WifiState.ENABLED_DISCONNECTED) {
                        String ssid = getWifiName(context);
                        if(ssid == null ||  ssid.equalsIgnoreCase("<unknown ssid>")) {
                            return;
                        }
                        Log.i(TAG, "Wifi Connection was lost. SSID: " + ssid);
                        saveEntry(WifiState.ENABLED_DISCONNECTED, new HashFactory(context).getHash(ssid), timestamp);
                        break;
                    }
                default:
                    Log.i(TAG, "Ignoring too fine grained Wifi Connection State");
            }
        } else {
            Log.i(TAG, "Unknown intent type was received");
        }
    }

    public String getWifiName(Context context) {
        Log.d(TAG, "getWifiName()");

        WifiManager manager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        if (manager.isWifiEnabled()) {
            WifiInfo wifiInfo = manager.getConnectionInfo();
            if (wifiInfo != null) {
                NetworkInfo.DetailedState state = WifiInfo.getDetailedStateOf(wifiInfo.getSupplicantState());
                if (state == NetworkInfo.DetailedState.CONNECTED || state == NetworkInfo.DetailedState.OBTAINING_IPADDR) {
                    return wifiInfo.getSSID();
                }
            }
        }
        return null;
    }

    private void saveEntry(WifiState state, long timestamp) {
        // TODO
//        UsageActivity usageActivity = new UsageActivity().activityName(UsageActivityName.WIFI).timestamp(timestamp).event(state.toString());
//        usageActivity.insertIntoDB();
    }

    private void saveEntry(WifiState state, String ssid, long timestamp) {
        // TODO
//        UsageActivity usageActivity = new UsageActivity().activityName(UsageActivityName.WIFI).timestamp(timestamp).event(state.toString()).description(ssid);
//        usageActivity.insertIntoDB();
    }
}
