package sjtu.iiot.posiiot.Components.iBeacon;

import android.bluetooth.BluetoothDevice;

/**
 * Created by TongXinyu on 16/7/5.
 */
public interface iBeaconScannerUiCallbacks {
    //Find required iBeacon device
    public void uiDeviceFound(final BluetoothDevice device, iBeacon Beacon);
    //Filter irrelevant Bluetooth device
    public void uiDeviceFilter(final BluetoothDevice device, int rssi);


    /* define Null Adapter class for that interface */
    public static class Null implements iBeaconScannerUiCallbacks {
        @Override
        public void uiDeviceFound(final BluetoothDevice device, iBeacon Beacon) {}
        public void uiDeviceFilter(final BluetoothDevice device, int rssi){}
    }
}
