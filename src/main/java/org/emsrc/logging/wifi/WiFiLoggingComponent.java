package org.emsrc.logging.wifi;

import android.content.Context;
import android.content.IntentFilter;
import android.util.Log;

import org.emsrc.common.loggingcomponent.ILoggingComponent;

public class WiFiLoggingComponent implements ILoggingComponent {

    private static final String TAG = "WiFiLoggingController";

    private WiFiReceiver wiFiReceiver;

    @Override
    public void startLoggingComponent(Context context) {
        // register receiver etc.
        IntentFilter wifiFilter = new IntentFilter("android.net.wifi.WIFI_STATE_CHANGED");
        wifiFilter.addAction("android.net.wifi.STATE_CHANGE");
        wiFiReceiver = new WiFiReceiver();
        context.getApplicationContext().registerReceiver(wiFiReceiver, wifiFilter);

        Log.i(TAG, "logging component started.");
    }

    @Override
    public void stopLoggingComponent(Context context) {
        if (wiFiReceiver != null) {
            context.getApplicationContext().unregisterReceiver(wiFiReceiver);
        }

        Log.i(TAG,"logging component stopped.");
    }
}
