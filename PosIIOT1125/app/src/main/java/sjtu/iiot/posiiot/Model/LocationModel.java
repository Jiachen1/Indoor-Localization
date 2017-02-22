package sjtu.iiot.posiiot.Model;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.util.Log;
import com.ls.widgets.map.MapWidget;
import com.ls.widgets.map.model.MapObject;

import java.util.HashMap;
import java.util.NavigableMap;

import sjtu.iiot.posiiot.Components.Communications.Communications;
import sjtu.iiot.posiiot.Components.Core.Helper.CoordinateHelper;
import sjtu.iiot.posiiot.Components.Core.Module.Coordinate;
import sjtu.iiot.posiiot.Components.Sensor.StepCalculater;
import sjtu.iiot.posiiot.Components.iBeacon.*;
import sjtu.iiot.posiiot.DataBase.HistoryLocDB;
import sjtu.iiot.posiiot.DataBase.LocUpdateUiCallBacks;
import sjtu.iiot.posiiot.DataStructure.MapInfo;
import sjtu.iiot.posiiot.DataStructure.ObjectInfo;
import sjtu.iiot.posiiot.DataStructure.LogTagConstants;
import sjtu.iiot.posiiot.GlobalValue;

import static java.lang.Math.PI;
import static java.lang.Math.abs;
import static java.lang.Math.asin;
import static java.lang.Math.atan;
import static java.lang.Math.sqrt;

/**
 * Created by TongXinyu on 16/7/6.
 */
public class LocationModel {
    private Context IContext;
    private HashMap<String,MapInfo> MapSet;
    private HashMap<String,MapWidget> MapIdOnToMapWidget;
    public static HashMap<String,ObjectInfo> ObjectSet;
    private MapWidget IMapWidget;
    private float Communication_X;
    private float Communication_Y;

    private StepCalculater IStepCalculater;


    private iBeaconScanner mIBeaconScanner;
    private iBeaconManager mIBeaconManager;
    private int CurManagerId = 1;
    private LocalizationManager mLocalizationManager;
    private iBeaconScannerUiCallbacks mIBeaconScannerUiCallbacks;
    private LocalizationAlgorithm mLocalizationAlgorithm;
    private HistoryLocDB mHistoryLocDB;

    private String UserID="mayintong";
    static public Communications mCommunications;


    private int[][] Interface_x = {
            {-1,580,860,0,450,0,0,0,0,0,0},
            {580,-1,0,0,0,0,0,0,0,0,0},
            {860,0,-1,0,0,0,0,0,0,0,0},
            {0,0,0,-1,100,0,0,0,0,0,0},
            {450,0,0,100,-1,0,0,0,0,0,0},
            {0,0,0,0,0,-1,0,0,0,0,0},
            {0,0,0,0,0,0,-1,0,0,0,0},
            {0,0,0,0,0,0,0,-1,0,0,0},
            {0,0,0,0,0,0,0,0,-1,0,0},
            {0,0,0,0,0,0,0,0,0,-1,0},
            {0,0,0,0,0,0,0,0,0,0,-1}
    };

    private int[][] Interface_y = {
            {-1,350,1000,0,0,0,0,0,0,0,0},
            {350,-1,0,0,0,0,0,0,0,0,0},
            {1000,0,-1,0,0,0,0,0,0,0,0},
            {0,0,0,-1,800,0,0,0,0,0,0},
            {500,0,0,800,-1,0,0,0,0,0,0},
            {0,0,0,0,0,-1,0,0,0,0,0},
            {0,0,0,0,0,0,-1,0,0,0,0},
            {0,0,0,0,0,0,0,-1,0,0,0},
            {0,0,0,0,0,0,0,0,-1,0,0},
            {0,0,0,0,0,0,0,0,0,-1,0},
            {0,0,0,0,0,0,0,0,0,0,-1}
    };

    public LocationModel(Context mContext){
        IContext=mContext;
        MapSet=new HashMap<String,MapInfo>();
        MapIdOnToMapWidget=new HashMap<String,MapWidget>();
        ObjectSet=new HashMap<String,ObjectInfo>();
        mCommunications=new Communications(mContext);
    }

    /**
     * Start localization service
     */
    public void StartLocalizationService(){
        /* Set the event when update the position */


        mHistoryLocDB = new HistoryLocDB(new LocUpdateUiCallBacks() {
            @Override
            public void uiLocationPresenter(Coordinate LBSLocation) {
                int x = LBSLocation.ParseLocationX();
                int y = LBSLocation.ParseLocationY();
                int des_Locx,des_Locy;

                if (GlobalValue.GetNavFlagFlag()) {
                    des_Locx = Interface_x[GlobalValue.GetTraceMat()[GlobalValue.GetTraceNumber()][0]-1][GlobalValue.GetTraceMat()[GlobalValue.GetTraceNumber()][1]-1];
                    des_Locy = Interface_y[GlobalValue.GetTraceMat()[GlobalValue.GetTraceNumber()][0]-1][GlobalValue.GetTraceMat()[GlobalValue.GetTraceNumber()][1]-1];
                    GlobalValue.SetViewFlag(true);
                }
                else
                {
                    des_Locx = 0;
                    des_Locy = 0;
                    GlobalValue.SetViewFlag(false);
                }


                if ((x - des_Locx)*(x - des_Locx) + (y - des_Locy)*(y - des_Locy) < 22500)
                {
                    GlobalValue.SetCurPos(GlobalValue.GetTraceMat()[GlobalValue.GetTraceNumber()][1]);
                    if ((GlobalValue.GetTraceMat()[GlobalValue.GetTraceNumber()][1]) == GlobalValue.GetDesNumber()) {
                        GlobalValue.SetNavFlagFlag(false);
                        GlobalValue.SetViewFlag(false);
                    }
                    else
                    {
                        GlobalValue.AddTraceNumber();
                    }

                }
                else if (GlobalValue.GetCurPos() == 1 && x >200 && x < 550 && y < 100 )
                {
                    GlobalValue.SetCurPos(5);
                    Log.e("Here","Here");
                    if (GlobalValue.GetNavFlagFlag())
                    {
                        if ((GlobalValue.GetTraceMat()[GlobalValue.GetTraceNumber()][1]) == GlobalValue.GetDesNumber()) {
                            GlobalValue.SetNavFlagFlag(false);
                            GlobalValue.SetViewFlag(false);
                        } else {
                            GlobalValue.AddTraceNumber();
                        }
                    }
                }
                /*else if (GlobalValue.GetCurPos() == 1 && x >100 && x < 300 && y > 900 )
                {
                    GlobalValue.SetCurPos(8);
                    if (GlobalValue.GetNavFlagFlag())
                    {
                        if ((GlobalValue.GetTraceMat()[GlobalValue.GetTraceNumber()][1]) == GlobalValue.GetDesNumber()) {
                            GlobalValue.SetNavFlagFlag(false);
                            GlobalValue.SetViewFlag(false);
                        } else {
                            GlobalValue.AddTraceNumber();
                        }
                    }
                }
*/

                ObjectSet.get("user").SetLoc(x,y);
                ObjectSet.get("arrow_des").SetLoc(des_Locx,des_Locy);
                ObjectSet.get("arrow_show").SetLoc(x,y);
                double x_diretion = ((double) (des_Locx - x))/(sqrt((x-des_Locx)*(x-des_Locx) + (y-des_Locy)*(y-des_Locy)));
                double y_diretion = ((double) (y - des_Locy))/(sqrt((x-des_Locx)*(x-des_Locx) + (y-des_Locy)*(y-des_Locy)));

                if (abs(x_diretion - 0) > 0.01)
                {
                    double angle = atan(y_diretion/x_diretion);
                    if (x_diretion < 0) {
                       angle = PI + angle;
                    }

                   GlobalValue.SetDirectionFlag((int)(angle*180/PI));

                }

            }
        });

        mHistoryLocDB.SetUserLocation(CoordinateHelper.Init(1,300,900));



        /* Start the step calculater */
        IStepCalculater=new StepCalculater(IContext);
        IStepCalculater.SetLocDB(mHistoryLocDB);
        IStepCalculater.startstep();


        mIBeaconManager = new iBeaconManager(1);




        //floor 1

        mIBeaconManager.AddiBeacon("20:91:48:53:4F:C1", CoordinateHelper.Init(1, 583, 665),0);
        mIBeaconManager.AddiBeacon("20:91:48:53:4A:4D", CoordinateHelper.Init(1, 583, 368),0);
        mIBeaconManager.AddiBeacon("20:91:48:53:2D:06", CoordinateHelper.Init(1, 659, 716),0);
        mIBeaconManager.AddiBeacon("20:91:48:53:33:75", CoordinateHelper.Init(1, 773, 958),0);

        mIBeaconManager.AddiBeacon("20:91:48:53:48:C8", CoordinateHelper.Init(1, 443, 489),0);

        mIBeaconManager.AddiBeacon("20:91:48:53:26:84", CoordinateHelper.Init(1, 196, 479),3);
        mIBeaconManager.AddiBeacon("20:91:48:53:25:B7", CoordinateHelper.Init(1, 106, 744),0);
        mIBeaconManager.AddiBeacon("20:91:48:53:49:3A", CoordinateHelper.Init(1, 239, 996),0);
        mIBeaconManager.AddiBeacon("20:91:48:53:49:36", CoordinateHelper.Init(1, 889, 948),0);

        /* Switch */
        mIBeaconManager.AddiBeacon("A4:D5:78:0B:AF:D8", CoordinateHelper.Init(2, 446, 10),0);
        mIBeaconManager.AddiBeacon("20:91:48:53:51:46", CoordinateHelper.Init(2, 258, 10),0);

        //floor 2

        mIBeaconManager.AddiBeacon("20:91:48:53:49:02", CoordinateHelper.Init(3, 850, 1100),0);

        mIBeaconManager.AddiBeacon("20:91:48:52:3B:B6", CoordinateHelper.Init(3, 850, 700),0);
        mIBeaconManager.AddiBeacon("20:91:48:52:41:0F", CoordinateHelper.Init(3, 650, 750),0);
        mIBeaconManager.AddiBeacon("20:91:48:52:3B:D4", CoordinateHelper.Init(3, 450, 750),0);

        mIBeaconManager.AddiBeacon("20:91:48:53:22:59", CoordinateHelper.Init(3, 50, 750),0);
        mIBeaconManager.AddiBeacon("20:91:48:53:51:4F", CoordinateHelper.Init(3, 50, 300),3);


        /* Set the localization manager */
        mLocalizationManager = new LocalizationManager();
        mLocalizationManager.SetiBeaconManager(mIBeaconManager);

        /* Set the type of localization */
        mLocalizationManager.SetLocalizationState(LocalizationManager.LocalizationState.OFFLINE_BLE_DEADROCKING);


        mLocalizationAlgorithm = new LocalizationAlgorithm(this.IContext,mLocalizationManager, new LocalizationAlgorithmUiCallback() {
            @Override
            public void uiLocalizationOnline(Object object) {
                //Insert Online Communication Code
            }
        });

        mIBeaconScanner=new iBeaconScanner(IContext,new iBeaconScannerUiCallbacks() {
            @Override
            public void uiDeviceFound(BluetoothDevice device, iBeacon Beacon) {
                /*-------------------------------- Localization Algorithm --------------------------------*/
                /* You can also replace the algorithm using your algorithm */
                Log.e("CurMap","CurMap " + GlobalValue.GetCurPos());
                mLocalizationAlgorithm.Localization(Beacon,mHistoryLocDB);
                if (Beacon.GetRSSI() > -75) {
                    if (mIBeaconManager.Get_iBeacon(device.getAddress()).GetLocation().ParseMapID() == 1 && CurManagerId != 1) {
                        GlobalValue.SetCurPos(1);
                        CurManagerId = 1;
                    }

                    if (mIBeaconManager.Get_iBeacon(device.getAddress()).GetLocation().ParseMapID() == 3 && CurManagerId != 2) {
                        GlobalValue.SetCurPos(5);
                        CurManagerId = 2;
                    }
                }


                /*---------------------------------------- END ----------------------------------------*/
            }

            @Override
            public void uiDeviceFilter(BluetoothDevice device, int rssi) {
                /*-------------------------------- Localization Algorithm --------------------------------*/
                /*if (rssi > -80) {
                    if (mIBeaconManager_1st.Get_iBeacon(device.getAddress()) != null) {
                        GlobalValue.SetCurPos(1);
                        mIBeaconScanner.SetManager(mIBeaconManager_1st);
                        CurManagerId = 1;
                    }

                    if (mIBeaconManager_Switch_1and2.Get_iBeacon(device.getAddress()) != null) {
                        if (CurManagerId == 1) {
                            GlobalValue.SetCurPos(5);
                            CurManagerId = 2;
                        }
                        if (CurManagerId == 2) {
                            GlobalValue.SetCurPos(1);
                            CurManagerId = 1;
                        }
                    }
                    if (mIBeaconManager_2nd.Get_iBeacon(device.getAddress()) != null) {
                        GlobalValue.SetCurPos(4);
                        mIBeaconScanner.SetManager(mIBeaconManager_2nd);
                        CurManagerId = 1;
                    }
                }*/
                /* How to deal with iBeacon not in IBeaconManager*/
                /*---------------------------------------- END ----------------------------------------*/
            }
        });


        //Check whether BT is OK
        if(mIBeaconScanner.checkBleHardwareAvailable() == false) {
            Log.e("System", "No BT");
        }
        // initialize BleWrapper object
        mIBeaconScanner.initialize();
        mIBeaconScanner.SetManager(mIBeaconManager);
        mIBeaconScanner.beginScanning();

        Thread Modify = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                Looper.prepare();
                mLocalizationAlgorithm.ModifyAlgorithm(mHistoryLocDB);
            }
        });
        Modify.start();


        /* Communicate with server and Update the position */
        //CommunicateWithServer();
    }

    /**
     * You can change the localization type
     * @param LBSType type of localization type, which can be set as ClOSE, OFFLINE_BLE, OFFLINE_BLE_DEADROCKING, ONLINE_WIFI, ONLINE_WIFI_BLE, ONLINE_WIFI_BLE_DEADROCKING
     */

    public void SetLocalizationType(LocalizationManager.LocalizationState LBSType)
    {
        mLocalizationManager.SetLocalizationState(LBSType);
    }

    /**
     * You can using different groups of iBeacon to achieve localization. When we enter into a new map, we can download the BLE information
     * of new map and add them into a new beacon manager
     * @param beaconManager
     */
    public void SetManager(iBeaconManager beaconManager)
    {
        mLocalizationManager.SetiBeaconManager(beaconManager);
    }

    public MapWidget CreateNewMap(String MapID){
        IMapWidget=new MapWidget(IContext,MapID);

        MapIdOnToMapWidget.put(MapID,IMapWidget);
        MapSet.put(MapID,new MapInfo(IMapWidget));
        return IMapWidget;
    }

    public int CreateLayer(String MapID,String LayerLabel){
        int LayerIndex=MapSet.get(MapID).GetLayerSetSize()+1;
        MapSet.get(MapID).AddNewLayer(LayerLabel, LayerIndex);
        return LayerIndex;
    }

    public MapObject CreateMapObject(String ObjectID,MapWidget IMapWidget,int LayerIndex,Drawable IDrawable, int PixelX, int PixelY, int PivotX, int PivotY) {
        MapObject IMapObject= new MapObject(LayerIndex,
                IDrawable,
                PixelX, PixelY, // Coordinate in pixels
                PivotX, PivotY, // Pivot point
                true, // Touchable
                true);
        ObjectSet.put(ObjectID, new ObjectInfo(LayerIndex, IMapObject, IMapWidget));
        return IMapObject;
    }

    public MapWidget GetMapWidgetById(String MapID){
        return MapIdOnToMapWidget.get(MapID);
    }

    public MapObject GetObjectInstance(String ObjectID){
        return ObjectSet.get(ObjectID).GetMapObject();
    }

    public int GetLayerIndexOfObject(String ObjectID){
        return ObjectSet.get(ObjectID).GetLayerIndex();
    }

    public MapWidget GetMapWidgetOfObject(String ObjectID){
        return ObjectSet.get(ObjectID).GetMapWidget();
    }

    public boolean GetMapObjectIsDrawnFlag(String ObjectID){
        return ObjectSet.get(ObjectID).GetIsDrawnFlag();
    }

    public void SetMapObjectIsDrawnFlag(String ObjectID,boolean flag){
        ObjectSet.get(ObjectID).SetIsDrawnFlag(flag);
    }

    public int GetMapLayerIndex(String MapID,String Label){
        return MapSet.get(MapID).GetLayerIndex(Label);

    }

    private void CommunicateWithServer(){
        new Thread(){
            @Override
            public void run(){
                while(true){
                    try {
                        Thread.sleep(1000);
                        /* Update proportion of X and Y */
                        Communication_X = ((float) mHistoryLocDB.GetUserLocation().ParseLocationX()) / IMapWidget.getOriginalMapWidth();
                        Communication_Y = ((float) mHistoryLocDB.GetUserLocation().ParseLocationY()) / IMapWidget.getOriginalMapHeight();
                        //Log.i(UserID,UserID);
                        mCommunications.UploadUserLocation(UserID, //mHistoryLocDB.GetUserLocation().ParseLocationX()
                                Communication_X, //mHistoryLocDB.GetUserLocation().ParseLocationY()
                                1 - Communication_Y);
                        //Log.i(LogTagConstants.Localization_INFO_Tag, "Communication: " + IMapWidget.getMapWidth() + " : " + IMapWidget.getMapHeight());
                        Log.i(LogTagConstants.Localization_INFO_Tag,"Communication:" + Communication_X + " : " + (1 - Communication_Y));
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }
}
