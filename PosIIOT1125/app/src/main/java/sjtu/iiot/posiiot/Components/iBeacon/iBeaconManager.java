package sjtu.iiot.posiiot.Components.iBeacon;

import java.util.HashMap;

import sjtu.iiot.posiiot.Components.Core.Module.Coordinate;

/**
 * Created by TongXinyu on 16/7/5.
 */
public class iBeaconManager {

    private static HashMap<String, iBeacon> LiBeacons;
    private static int miBeaconManagerID;

    public iBeaconManager(int iBeaconManagerID)
    {
        miBeaconManagerID = iBeaconManagerID;
        LiBeacons = new HashMap<String, iBeacon>();
    }

    public iBeacon Get_iBeacon(String mac)
    {
        if (LiBeacons.containsKey(mac))
            return LiBeacons.get(mac);
        else
            return null;
    }

    public void AddiBeacon(String mac, Coordinate BLElocation,int modify)
    {
        iBeacon tmp = new iBeacon(mac, BLElocation, modify);
        LiBeacons.put(mac,tmp);
    }

}
