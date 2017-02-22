package sjtu.iiot.posiiot.Model;

import sjtu.iiot.posiiot.Components.iBeacon.*;

/**
 * Created by TongXinyu on 16/7/5.
 */
public class LocalizationManager {

    private static LocalizationState mLocalizationState = LocalizationState.ClOSE;
    private static iBeacon miBeacon;
    private static iBeaconManager miBeaconManager;

    public LocalizationManager ()
    {

    }

    public void SetiBeaconManager(iBeaconManager iBeacon_Manager) { miBeaconManager = iBeacon_Manager;}

    public void SetLocalizationState(LocalizationState state)
    {
        mLocalizationState = state;
    }

    protected iBeaconManager GetiBeaconManager()
    {
        return miBeaconManager;
    }

    protected iBeacon GetiBeacon()
    {
        return miBeacon;
    }

    public LocalizationState GetLocalizationState()
    {
        return mLocalizationState;
    }

    public static enum LocalizationState
    {
        ClOSE,
        ONLINE_WIFI,
        ONLINE_WIFI_BLE,
        ONLINE_WIFI_BLE_DEADROCKING,
        OFFLINE_BLE,
        OFFLINE_BLE_DEADROCKING;
    }
}
