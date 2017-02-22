package sjtu.iiot.posiiot.View;

import com.ls.widgets.map.MapWidget;
import com.ls.widgets.map.model.MapObject;

/**
 * Created by TongXinyu on 16/7/5.
 */
public interface LocationView {
    void MoveMapObjectTo(MapObject IMapObject,int x,int y);
    void DrawMapObject(MapWidget IMapWidget,int LayerIndex,MapObject IMapObject);
    void DrawMapWidget(MapWidget IMapWidget);
    void RemoveMapWidget();
}
