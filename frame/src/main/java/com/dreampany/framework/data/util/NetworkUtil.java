package com.dreampany.framework.data.util;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.provider.Settings;

import com.dreampany.framework.data.enums.ApState;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.math.BigInteger;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.Socket;
import java.net.UnknownHostException;
import java.nio.ByteOrder;

/**
 * Created by air on 6/27/17.
 */

public final class NetworkUtil {
    private NetworkUtil() {
    }

    public static final String DEFAULT_PING_HOST = "www.google.com";
    public static final int DEFAULT_PING_PORT = 80;
    public static final int DEFAULT_PING_TIMEOUT_IN_MS = 2000;

    public static boolean isApEnabledV1(Context context) {
        try {
            WifiManager manager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

            Method method = manager.getClass().getMethod("isWifiApEnabled");
            return (boolean) method.invoke(manager);

        } catch (NoSuchMethodException e) {
            LogKit.verbose("wifi ap state failure " + e.toString());
            return false;
        } catch (InvocationTargetException e) {
            LogKit.verbose("wifi ap state failure " + e.toString());
            return false;
        } catch (IllegalAccessException e) {
            LogKit.verbose("wifi ap state failure " + e.toString());
            return false;
        }
    }

    public static boolean isApEnabled(Context context) {
        ApState state = getApState(context);
        return state == ApState.WIFI_AP_STATE_ENABLED;
    }

    public static ApState getApState(int apState) {
        if (apState >= 10) {
            apState = apState - 10;
        }
        return ApState.class.getEnumConstants()[apState];
    }

    public static ApState getApState(Context context) {
        try {
            WifiManager manager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);

            Method method = manager.getClass().getMethod("getWifiApState");
            int apState = ((Integer) method.invoke(manager));
            if (apState >= 10) {
                apState = apState - 10;
            }
            return ApState.class.getEnumConstants()[apState];
        } catch (NoSuchMethodException e) {
            LogKit.verbose("wifi ap state failure " + e.toString());
            return ApState.WIFI_AP_STATE_FAILED;
        } catch (InvocationTargetException e) {
            LogKit.verbose("wifi ap state failure " + e.toString());
            return ApState.WIFI_AP_STATE_FAILED;
        } catch (IllegalAccessException e) {
            LogKit.verbose("wifi ap state failure " + e.toString());
            return ApState.WIFI_AP_STATE_FAILED;
        }
    }

    public static boolean hasInternet() {
        try {
            return hasInternet(DEFAULT_PING_HOST, DEFAULT_PING_PORT,DEFAULT_PING_TIMEOUT_IN_MS);
        } catch (IOException e) {
            return false;
        }
    }

    public static boolean hasInternet(String host, int port, int timeout) throws IOException {
        Socket socket = new Socket();
        socket.connect(new InetSocketAddress(host, port), timeout);
        return socket.isConnected();
    }

    public static boolean isConnectedToWifi(Context context) {
        ConnectivityManager connectivityManager =
                (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();

        if (networkInfo != null) {
            return networkInfo.getType() == ConnectivityManager.TYPE_WIFI;
        }

        return false;
    }

    public static boolean hasNetwork(Context context) {
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo activeNetwork = cm.getActiveNetworkInfo();
        boolean isConnected = activeNetwork != null && activeNetwork.isConnected();
        return isConnected;
    }

/*    public static InetAddress getWifiAddress(Context context) {
        WifiManager manager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = manager.getConnectionInfo();
        if (wifiInfo == null) {
            return null;
        }

        String host = Formatter.formatIpAddress(wifiInfo.getIpAddress());
        try {
            return InetAddress.getByName(host);
        } catch (UnknownHostException ex) {
            return null;
        }
    }*/

    public static InetAddress getWifiAddress(Context context) {
        WifiManager manager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = manager.getConnectionInfo();
        if (wifiInfo == null) {
            return null;
        }

        int ipAddress = wifiInfo.getIpAddress();

        if (ByteOrder.LITTLE_ENDIAN.equals(ByteOrder.nativeOrder())) {
            ipAddress = Integer.reverseBytes(ipAddress);
        }

        byte[] address = BigInteger.valueOf(ipAddress).toByteArray();
        try {
            return InetAddress.getByAddress(address);
        } catch (UnknownHostException ex) {
            return null;
        }
    }

    public static InetAddress getApAddress(Context context) {
        WifiManager manager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcpInfo = manager.getDhcpInfo();
        if (dhcpInfo == null) {
            return null;
        }

        int ipAddress = dhcpInfo.serverAddress;

        if (ByteOrder.LITTLE_ENDIAN.equals(ByteOrder.nativeOrder())) {
            ipAddress = Integer.reverseBytes(ipAddress);
        }

        byte[] address = BigInteger.valueOf(ipAddress).toByteArray();
        try {
            return InetAddress.getByAddress(address);
        } catch (UnknownHostException ex) {
            return null;
        }
    }

    public static byte[] macAddressToByteArray(String macString) {
        String[] mac = macString.split("[:\\s-]");
        byte[] macAddress = new byte[6];
        for (int i = 0; i < mac.length; i++) {
            macAddress[i] = Integer.decode("0x" + mac[i]).byteValue();
        }
        return macAddress;
    }

    public static boolean isMobileDataEnabled(Context context) {
        return android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.JELLY_BEAN_MR1 ?
                Settings.Global.getInt(context.getContentResolver(), "mobile_data", 1) == 1 :
                Settings.Secure.getInt(context.getContentResolver(), "mobile_data", 1) == 1;
    }
}
