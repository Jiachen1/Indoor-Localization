package sjtu.iiot.posiiot_sensortest.Components;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.Context;
import android.content.pm.PackageManager;
import android.os.Handler;
import android.util.Log;
import sjtu.iiot.posiiot_sensortest.Components.OnValueChangeListener.ListenerType;

/**
 * Created by TongXinyu on 16/7/5.
 */
public class iBeaconScanner {

    private static final int SCAN_INTERVAL_MS =500000;

    private Context mParent = null;
    private BluetoothManager mBluetoothManager = null;
    private BluetoothAdapter mBluetoothAdapter = null;
    private static iBeaconManager miBeaconManager;
    private Handler scanHandler = new Handler();
    private boolean isScanning = false;
    /* callback object through which we are returning results to the caller */
    private OnValueChangeListener mUiCallback = null;
    /* define NULL object for UI callbacks */
    private static final OnValueChangeListener NULL_CALLBACK = new OnValueChangeListener.Null();


    /* creates BleWrapper object, set its parent activity and callback object */
    public iBeaconScanner(Context parent, OnValueChangeListener callback) {
        this.mParent = parent;
        mUiCallback = callback;
        if(mUiCallback == null) mUiCallback = NULL_CALLBACK;
    }

    public void SetManager(iBeaconManager LiBeaconManager)
    {
        miBeaconManager = LiBeaconManager;
    }

    /* run test and check if this device has BT and BLE hardware available */
    public boolean checkBleHardwareAvailable() {
        // First check general Bluetooth Hardware:
        // get BluetoothManager...
        final BluetoothManager manager = (BluetoothManager) mParent.getSystemService(Context.BLUETOOTH_SERVICE);
        if(manager == null) return false;
        // .. and then get adapter from manager
        final BluetoothAdapter adapter = manager.getAdapter();
        if(adapter == null) return false;

        // and then check if BT LE is also available
        boolean hasBle = mParent.getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE);
        return hasBle;
    }


    /* before any action check if BT is turned ON and enabled for us
     * call this in onResume to be always sure that BT is ON when Your
     * application is put into the foreground */
    public boolean isBtEnabled() {
        final BluetoothManager manager = (BluetoothManager) mParent.getSystemService(Context.BLUETOOTH_SERVICE);
        if(manager == null) return false;

        final BluetoothAdapter adapter = manager.getAdapter();
        if(adapter == null) return false;

        return adapter.isEnabled();
    }


    public void beginScanning() {
        scanHandler.post(scanRunnable);
    }

    private Runnable scanRunnable = new Runnable() {
        @Override
        public void run() {
            BluetoothAdapter adapter = BluetoothAdapter.getDefaultAdapter();

            if (isScanning) {
                adapter.stopLeScan(leScanCallback);
            } else if (!adapter.startLeScan(leScanCallback)) {
                // an error occurred during startLeScan
            }

            isScanning = !isScanning;

            scanHandler.postDelayed(this, SCAN_INTERVAL_MS);
        }
    };

    private BluetoothAdapter.LeScanCallback leScanCallback = new BluetoothAdapter.LeScanCallback() {
        @Override
        public void onLeScan(BluetoothDevice device, int rssi, byte[] scanRecord) {
            iBeacon tmp = miBeaconManager.Get_iBeacon(device.getAddress());
            float res[] = new float[1];
            //Log.i("BLE", device.getAddress());
            if (tmp != null)
            {
                res[0] = rssi;
                mUiCallback.OnValueChange(ListenerType.BLE,1,res,device.getAddress());
            }
            else
            {
                /*res[0] = rssi;
                mUiCallback.OnValueChange(ListenerType.BLE,1,res,device.getAddress());*/
            }
        }
    };

    /* initialize BLE and get BT Manager & Adapter */
    public boolean initialize() {
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager) mParent.getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                return false;
            }
        }

        if(mBluetoothAdapter == null) mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            return false;
        }
        return true;
    }
}
