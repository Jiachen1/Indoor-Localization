package sjtu.iiot.posiiot.DataBase;

import sjtu.iiot.posiiot.Components.Core.Module.Coordinate;

/**
 * Created by TongXinyu on 16/7/5.
 */
public interface LocUpdateUiCallBacks {
    public void uiLocationPresenter(final Coordinate mCoordinate);

    /* define Null Adapter class for that interface */
    public static class Null implements LocUpdateUiCallBacks {
        @Override
        //Offline Localization
        public void uiLocationPresenter(final Coordinate mCoordinate){};
    }
}
