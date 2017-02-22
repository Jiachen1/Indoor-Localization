package sjtu.iiot.posiiot.DataStructure;

import com.ls.widgets.map.MapWidget;
import com.ls.widgets.map.model.MapObject;

/**
 * Created by TongXinyu on 16/7/7.
 */
public class ObjectInfo {
    private int LayerIndex;
    private MapObject IMapObject;
    private MapWidget IMapWidget;
    private int LocX;
    private int LocY;
    private boolean IsDrawn;
    public ObjectInfo(int mLayerIndex,MapObject mMapObject,MapWidget mMapWidget){
        LayerIndex=mLayerIndex;
        IMapObject=mMapObject;
        IMapWidget= mMapWidget;
        IsDrawn=false;
    }
    public void SetLoc(int x,int y){
        LocX=x;
        LocY=y;
    }
    public int  GetLocX(){
        return LocX;
    }
    public int  GetLocY(){
        return LocY;
    }
    public int GetLayerIndex(){
        return LayerIndex;
    }
    public MapWidget GetMapWidget(){
        return IMapWidget;
    }
    public  MapObject GetMapObject(){
        return IMapObject;
    }
    public boolean GetIsDrawnFlag(){
        return IsDrawn;
    }
    public void SetIsDrawnFlag(boolean flag){
        IsDrawn=flag;
    }
}
