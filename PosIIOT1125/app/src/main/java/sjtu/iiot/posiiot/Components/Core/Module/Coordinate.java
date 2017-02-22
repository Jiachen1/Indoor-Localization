package sjtu.iiot.posiiot.Components.Core.Module;

import java.text.ParsePosition;
/**
 * Created by TongXinyu on 16/7/5.
 */
public class Coordinate {

    private final String mLocationString;
    private final int mMapID;
    private final int mCoordinate_X;
    private final int mCoordinate_Y;
    private final int mCoordinate_Z = 0;
    private static int mUserType;

    public Coordinate(int MapID,int Location_X,int Location_Y)
    {
        mMapID = MapID;
        mCoordinate_X = Location_X;
        mCoordinate_Y = Location_Y;
        mLocationString = mMapID + "," + mCoordinate_X + "," + mCoordinate_Y+ ",";
    }

    public Coordinate(String LocationString)
    {
        mLocationString = LocationString;
        mMapID = Integer.parseInt(LocationString.split(",")[0]);
        mCoordinate_X = Integer.parseInt(LocationString.split(",")[1]);
        mCoordinate_Y = Integer.parseInt(LocationString.split(",")[2]);
        mUserType = Integer.parseInt(LocationString.split(",")[3]);
    }

    public int ParseLocationX()
    {
        return mCoordinate_X;
    }

    public int ParseLocationY()
    {
        return mCoordinate_Y;
    }

    public int ParseMapID()
    {
        return mMapID;
    }

    public int ParseUserType()
    {
        return mUserType;
    }

    public String ParseString() {return mLocationString;}



}

