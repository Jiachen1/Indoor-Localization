package sjtu.iiot.posiiot.DataBase;

import sjtu.iiot.posiiot.Components.Core.Module.Coordinate;

/**
 * Created by TongXinyu on 16/7/5.
 */
public class HistoryLocDB{

    private int mCurrent = 0;
    private int mSize = 10;
    private Coordinate[] UserLocation = new Coordinate[10];
    private LocUpdateUiCallBacks mUiCallback = null;

    public HistoryLocDB(LocUpdateUiCallBacks UiCallback,int size) {
        mSize = size;
        UserLocation = new Coordinate[mSize];
        UserLocation[mCurrent] = null;
        mCurrent = (mCurrent + 1)%mSize;
        mUiCallback = UiCallback;
    }

    public HistoryLocDB(LocUpdateUiCallBacks UiCallback)
    {
        mUiCallback = UiCallback;
    }

    //private LBS_Location[] FriendsLocation;

    public Coordinate GetUserLocation()
    {
        if (mCurrent == 0)
            return UserLocation[mSize-1];
        else
            return UserLocation[mCurrent-1];
    }

    public void SetUserLocation(Coordinate Coordinate)
    {

        int x = Coordinate.ParseLocationX();
        int y = Coordinate.ParseLocationY();
        if (Coordinate.ParseLocationX() > 5500 && Coordinate.ParseLocationX() < 9500)
            if (Coordinate.ParseLocationY() > 5500 && Coordinate.ParseLocationY() <= 7500)
                y = 5500;
            else if (Coordinate.ParseLocationY() > 7500 && Coordinate.ParseLocationY() < 9500)
                y = 9500;

        if (Coordinate.ParseLocationY() > 6500 && Coordinate.ParseLocationY() < 8500)
            if (Coordinate.ParseLocationX() > 5500 && Coordinate.ParseLocationX() <= 7500)
                x = 5500;
            else if (Coordinate.ParseLocationX() > 7500 && Coordinate.ParseLocationX() < 9500)
                x = 9500;
        Coordinate Coor = new Coordinate(1,x,y);

        UserLocation[mCurrent] = Coor;
        mCurrent = (mCurrent + 1)%mSize;

        mUiCallback.uiLocationPresenter(Coor);
    }
}
