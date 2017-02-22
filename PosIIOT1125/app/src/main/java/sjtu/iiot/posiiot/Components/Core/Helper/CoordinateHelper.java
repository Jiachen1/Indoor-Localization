package sjtu.iiot.posiiot.Components.Core.Helper;

import sjtu.iiot.posiiot.Components.Core.Module.Coordinate;

/**
 * Created by TongXinyu on 16/7/5.
 */
public class CoordinateHelper {

        public static Coordinate Init(int MapID,int Location_X,int Location_Y)
        {
            Coordinate tmp = new Coordinate(MapID,Location_X,Location_Y);
            return tmp;
        }

        public static Coordinate Init(String LocationString)
        {
            Coordinate tmp = new Coordinate(LocationString);
            return tmp;
        }
}
