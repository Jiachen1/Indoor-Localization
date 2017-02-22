package sjtu.iiot.posiiot.Model;

import sjtu.iiot.posiiot.Components.Core.Module.Coordinate;

/**
 * Created by TongXinyu on 16/7/5.
 */
public interface LocalizationAlgorithmUiCallback {
    //Online Localization
    public void uiLocalizationOnline(final Object object);
    //Offline Localization
    //public Coordinate uiLocalizationOffline(final Object object);


    /* define Null Adapter class for that interface */
    public static class Null implements LocalizationAlgorithmUiCallback  {
        @Override
        //Online Localization
        public void uiLocalizationOnline(final Object object){};
        //public Coordinate uiLocalizationOffline(final Object object){return null;};
    }

}
