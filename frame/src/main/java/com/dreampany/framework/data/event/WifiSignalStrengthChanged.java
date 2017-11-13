package com.dreampany.framework.data.event;

import android.content.Context;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;

import java.util.List;

public final class WifiSignalStrengthChanged  extends NetworkEvent {
 // private static final String MESSAGE = "WifiSignalStrengthChanged";
  private Context context;

  public WifiSignalStrengthChanged(Context context) {
      super(null);
    this.context = context;
  }

  public List<ScanResult> getWifiScanResults() {
    WifiManager wifiManager = (WifiManager) context.getApplicationContext().getSystemService(Context.WIFI_SERVICE);
    return wifiManager.getScanResults();
  }
}