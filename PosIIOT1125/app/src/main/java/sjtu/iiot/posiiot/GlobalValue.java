package sjtu.iiot.posiiot;

import android.util.Log;

/**
 * Created by TongXinyu on 16/7/9.
 */
public class GlobalValue {

    private static float CompassValue = 0f;
    private static float CompassOff = 0f;
    private static int RSSIValue = 78;
    private static boolean DetectorFlag = true;
    private static boolean NavFlagFlag = true;
    private static boolean ViewFlag = true;
    private static int diretion = 0;
    private static int TraceMat[][] = null;
    private static int CurPos;
    private static int DesNumber;
    private static int TraceNumber;


    public static void SetDesNumber(int tmp)
    {
        TraceNumber = 0;
        DesNumber = tmp;
    }

    public static int GetDesNumber()
    {
        return DesNumber;
    }

    public static int GetTraceNumber()
    {
        return TraceNumber;
    }

    public static void AddTraceNumber()
    {
        TraceNumber++;
    }


    public static void SetCurPos(int tmp)
    {
        CurPos = tmp;
    }
    public static int GetCurPos()
    {
        return CurPos;
    }


    public static void SetTraceMat(int tmp[][])
    {
        TraceMat = tmp;
    }

    public static int[][] GetTraceMat()
    {
        return TraceMat;
    }

    public static void SetViewFlag(boolean tmp)
    {
        ViewFlag = tmp;
    }

    public static boolean GetViewFlag()
    {
        return ViewFlag;
    }

    public static void SetCompass(float tmp)
    {
        CompassValue = tmp;
    }

    public static float GetCompass()
    {
        return CompassValue;
    }

    public static void SetCompassOff(float tmp)
    {
        CompassOff = tmp;
    }

    public static float GetCompassOff()
    {
        return CompassOff;
    }

    public static int GetRSSIValue()
    {
        return RSSIValue;
    }

    public static void SetRSSIValue(int rssi)
    {
        RSSIValue = rssi;
    }

    public static void SetDetectorFlag(boolean tmp) {DetectorFlag = tmp;}

    public static boolean GetDetectorFlag(){return DetectorFlag;}

    public static void SetNavFlagFlag(boolean tmp) {NavFlagFlag = tmp;}

    public static boolean GetNavFlagFlag(){return NavFlagFlag;}

    public static void SetDirectionFlag(int tmp) {diretion = tmp;}

    public static int GetDirectionFlag(){return diretion;}

}
