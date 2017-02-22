package sjtu.iiot.posiiot.Model;

import android.content.Context;
import android.util.Log;

import sjtu.iiot.posiiot.Components.Core.Module.Coordinate;
import sjtu.iiot.posiiot.Components.iBeacon.*;
import sjtu.iiot.posiiot.DataBase.HistoryLocDB;
import sjtu.iiot.posiiot.DataStructure.LogTagConstants;
import sjtu.iiot.posiiot.GlobalValue;
import static android.os.SystemClock.sleep;

/**
 * Created by TongXinyu on 16/7/5.
 */
public class LocalizationAlgorithm {
    LocalizationManager mLocalizationManager = null;
    private Context mParent = null;
    private LocalizationAlgorithmUiCallback mUiCallback = null;
    private iBeaconManager miBeaconManager = null;
    private MyFunction MF_tmp = null;
    private int[] MF_Result = new int[2];
    private static final String DEBUG_TAG = "LocalizationAlgorithm";
    private static int SumWeight = 0;
    private static int SumX = 0;
    private static int SumY = 0;
    private static int[] tmp = new int[2];

    public LocalizationAlgorithm(Context parent, LocalizationManager Localization_Manager, LocalizationAlgorithmUiCallback UiCallback)
    {
        this.mParent = parent;
        mLocalizationManager = Localization_Manager;
        mUiCallback = UiCallback;
        MF_tmp =  new MyFunction();
    }

    public void ModifyAlgorithm(HistoryLocDB mHistoryLocDB)
    {
        int UserLocX,UserLocY;
        while (true) {
            sleep(4000);

            UserLocX = mHistoryLocDB.GetUserLocation().ParseLocationX();
            UserLocY = mHistoryLocDB.GetUserLocation().ParseLocationY();

            MF_Result = MF_tmp.ModifyAlg(SumWeight,SumX,SumY,UserLocX,UserLocY);
            UserLocX = MF_Result[0];
            UserLocY = MF_Result[1];

            /*if (UserLocX > 1200 && UserLocX < 1850)
            {
                if (UserLocY > 1500 && UserLocY < 1600)
                    UserLocY = 1500;
                else if (UserLocY >= 1600 && UserLocY < 1700)
                    UserLocY = 1700;

                else if (UserLocY > 1800 && UserLocY < 1900)
                    UserLocY = 1800;
                else if (UserLocY >= 1900 && UserLocY < 2000)
                    UserLocY = 2000;

                else if (UserLocY >= 2150 && UserLocY < 2200)
                    UserLocY = 2150;
                else if (UserLocY >= 2200)
                    UserLocY = 2300;
            }*/
            mHistoryLocDB.SetUserLocation(new Coordinate(1,UserLocX,UserLocY));
            SumWeight = 0;
            SumX = 0;
            SumY = 0;
        }
    }

    private void Localization_OBD(Object object,HistoryLocDB locationDB) {
        iBeacon miBeacon = (iBeacon)object;
        int rssi = miBeacon.GetRSSI() + miBeacon.GetModify();

        int BLE_LocX = mLocalizationManager.GetiBeaconManager().Get_iBeacon(miBeacon.GetMac()).GetLocation().ParseLocationX();
        int BLE_LocY = mLocalizationManager.GetiBeaconManager().Get_iBeacon(miBeacon.GetMac()).GetLocation().ParseLocationY();
        int User_LocX = locationDB.GetUserLocation().ParseLocationX();
        int User_LocY = locationDB.GetUserLocation().ParseLocationY();
        int SumValue = (rssi + GlobalValue.GetRSSIValue())*(rssi + GlobalValue.GetRSSIValue());
        if (rssi >= -GlobalValue.GetRSSIValue()) {

            MF_Result = MF_tmp.OBDAlg(SumValue,BLE_LocX,BLE_LocY,User_LocX,User_LocY);
            User_LocX = MF_Result[0];
            User_LocY = MF_Result[1];

            /*if (User_LocX > 1200 && User_LocX < 1850)
            {
                if (User_LocY > 1500 && User_LocY < 1600)
                    User_LocY = 1500;
                else if (User_LocY >= 1600 && User_LocY < 1700)
                    User_LocY = 1700;

                else if (User_LocY > 1800 && User_LocY < 1900)
                    User_LocY = 1800;
                else if (User_LocY >= 1900 && User_LocY < 2000)
                    User_LocY = 2000;

                else if (User_LocY >= 2150 && User_LocY < 2200)
                    User_LocY = 2150;
                else if (User_LocY >= 2200)
                    User_LocY = 2300;
            }*/

            locationDB.SetUserLocation(new Coordinate(1,User_LocX,User_LocY));
            Log.i(LogTagConstants.Localization_INFO_Tag, "BLEResult:" + miBeacon.GetMac() + " : " + rssi);
            Log.i(LogTagConstants.Localization_INFO_Tag, "Position:" + User_LocX + "," + User_LocY);
        }
        Log.i(LogTagConstants.Localization_INFO_Tag,"RSSIValue" + GlobalValue.GetRSSIValue());

        /* Modify Location */
        SumValue = (rssi + GlobalValue.GetRSSIValue()+12)*(rssi + GlobalValue.GetRSSIValue()+12);
        if (rssi >= -GlobalValue.GetRSSIValue()-12)
        {
                SumWeight += SumValue;
                SumX += Math.abs(BLE_LocX) * SumValue;
                SumY += Math.abs(BLE_LocY) * SumValue;
        }
    }

    public void Localization(Object obj,HistoryLocDB locationDB)
    {
        switch (mLocalizationManager.GetLocalizationState())
        {
            case ClOSE:
                Log.d(DEBUG_TAG, " : CLOSE");
                break;
            case OFFLINE_BLE:
                Log.d(DEBUG_TAG," : OFFLINE_BLE");
                break;
            case OFFLINE_BLE_DEADROCKING:
                Log.d(DEBUG_TAG," : OFFLINE_BLE_DEADROCKING");
                Localization_OBD(obj,locationDB);
                break;
            case ONLINE_WIFI:
                Log.d(DEBUG_TAG," : ONLINE_WIFI");
                break;
            case ONLINE_WIFI_BLE:
                Log.d(DEBUG_TAG," : ONLINE_WIFI_BLE");
                break;
            case ONLINE_WIFI_BLE_DEADROCKING:
                Log.d(DEBUG_TAG," : ONLINE_WIFI_BLE_DEADROCKING");
                break;
            default:
                Log.d(DEBUG_TAG," : ERROR");
                break;
        }
    }
}
