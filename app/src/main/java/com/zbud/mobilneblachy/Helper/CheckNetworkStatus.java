package com.zbud.mobilneblachy.Helper;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;


public class CheckNetworkStatus {

    public static boolean isNetworkAvailable(Context context) {
        String ssid;
        WifiManager wifiMgr = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        ssid = wifiInfo.getSSID().substring(1,wifiInfo.getSSID().length()-1);

        for (String networkName : Config.NETWORK_NAMES) {
            if ((ssid.equals(networkName))) {
                return true;
            }
        }
        //return false;
        return true; // tylko dla emulatora
    }
}