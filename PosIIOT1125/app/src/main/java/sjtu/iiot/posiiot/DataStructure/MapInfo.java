package sjtu.iiot.posiiot.DataStructure;

import com.ls.widgets.map.MapWidget;

import java.util.HashMap;

/**
 * Created by TongXinyu on 16/7/7.
 */
public class MapInfo {
    private MapWidget IMapWidget;
    private HashMap<String,Integer> ILayerSet;
    private HashMap<String,Integer> IObejectSet;
    public MapInfo(MapWidget mMapWidget){
        IMapWidget=mMapWidget;
        ILayerSet=new HashMap<String,Integer>();
        IObejectSet=new HashMap<String,Integer>();
    }
    public MapInfo(MapWidget mMapWidget,HashMap<String,Integer> mLayerSet){
        IMapWidget=mMapWidget;
        ILayerSet=mLayerSet;
        IObejectSet=new HashMap<String,Integer>();
    }
    public int GetLayerSetSize(){
        return ILayerSet.size();
    }
    public int GetLayerIndex(String LayerIndex){
        return ILayerSet.get(LayerIndex);
    }
    public void AddNewLayer(String mLayer,int LayerIndex){
        ILayerSet.put(mLayer,LayerIndex);
        IMapWidget.createLayer(LayerIndex);
    }
}
