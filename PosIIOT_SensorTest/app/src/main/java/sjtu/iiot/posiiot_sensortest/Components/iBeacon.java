package sjtu.iiot.posiiot_sensortest.Components;


/**
 * Created by TongXinyu on 16/7/5.
 */
public class iBeacon extends Object {

    private String mMac;
    private static int [] mRSSIArray;
    private static int CurrentPointer = 0;
    private static int size = 10;
    private final int mModify;
    private static byte[] mScanRecord;
    private static long mSystemTime;
    private final Coordinate mBLELocation;

    public iBeacon(String mac, Coordinate BLELocation) {
        mMac = mac;
        size = 10;
        mBLELocation = BLELocation;
        mModify = 0;
        mRSSIArray = new int[size];
        mRSSIArray[CurrentPointer] = -100;
        CurrentPointer = (CurrentPointer + 1)%size;
    }

    public iBeacon(String mac, Coordinate BLELocation, int modify) {
        mMac = mac;
        size = 10;
        mBLELocation = BLELocation;
        mModify = modify;
        mRSSIArray = new int[size];
        mRSSIArray[CurrentPointer] = -100;
        CurrentPointer = (CurrentPointer + 1)%size;
    }

    public int GetModify()
    {
        return mModify;
    }

    public Coordinate GetLocation()
    {
        return mBLELocation;
    }

    public void UpdataRSSI(int rssi)
    {
        mRSSIArray[CurrentPointer] = rssi;
        CurrentPointer = (CurrentPointer + 1)%size;
    }

    public int GetRSSI()
    {
        if (CurrentPointer == 0)
            return mRSSIArray[size-1];
        else
            return mRSSIArray[CurrentPointer-1];
    }

    public int[] iBeaconGetRSSIArray()
    {
        return mRSSIArray;
    }

    public String GetMac(){return mMac;}

}

