package sjtu.iiot.posiiot.Presenter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Matrix;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.ls.widgets.map.MapWidget;
import com.ls.widgets.map.config.MapGraphicsConfig;

import sjtu.iiot.posiiot.GlobalValue;
import sjtu.iiot.posiiot.Model.LocationModel;
import sjtu.iiot.posiiot.R;
import sjtu.iiot.posiiot.View.LocationView;

/**
 * Created by TongXinyu on 16/7/5.
 */
public class LocationPresenter {
    LocationView ILocationView;
    LocationModel ILocationModel;
    MapWidget MapOnShow;
    Handler IHandler;
    Context IContext;
    private LinearLayout mLayoutRotation;
    private int State_Number = 0;
    private ImageView Arrow_Icon;
    private boolean StartUIFlag = false;

    public  LocationPresenter(LocationView mLocationView, Context mContext, ImageView Arr, LinearLayout layoutRotation){
        IContext=mContext;
        Arrow_Icon = Arr;
        mLayoutRotation = layoutRotation;
        this.ILocationView=mLocationView;
        ILocationModel=new LocationModel(mContext);
        IHandler=new Handler()
        {
            public void handleMessage(Message msg)
            {
                switch(msg.what)
                {
                    case 0: {
                        float Compass_Valus = GlobalValue.GetCompass();
                        //mLayoutRotation.setRotation(360 - Compass_Valus - 20);

                        /*if (Compass_Valus < 45 || Compass_Valus > 315)
                            ILocationModel.GetObjectInstance("user").setDrawable(IContext.getResources().getDrawable(R.drawable.arrow_1));
                        else if (Compass_Valus > 45 && Compass_Valus < 135)
                            ILocationModel.GetObjectInstance("user").setDrawable(IContext.getResources().getDrawable(R.drawable.arrow_2));
                        else if (Compass_Valus > 135 && Compass_Valus < 225)
                            ILocationModel.GetObjectInstance("user").setDrawable(IContext.getResources().getDrawable(R.drawable.arrow_3));
                        else if (Compass_Valus > 225 && Compass_Valus < 315)
                            ILocationModel.GetObjectInstance("user").setDrawable(IContext.getResources().getDrawable(R.drawable.arrow_4));
                        Arrow_Icon.setRotation(Compass_Valus);*/
                        ILocationModel.GetObjectInstance("user").setDrawable(RotationObject(R.drawable.location_icon,Compass_Valus));
                        if (State_Number == 0)
                        {
                            ILocationModel.GetObjectInstance("arrow_show").setDrawable(IContext.getResources().getDrawable(R.drawable.nullpic));
                        }

                        if (State_Number == 5) {
                            ILocationModel.GetObjectInstance("arrow_show").setDrawable(RotationObject(R.drawable.arrow_show,-GlobalValue.GetDirectionFlag()));
                        }

                        if (State_Number == 10)
                        {
                            State_Number = -1;
                        }
                        State_Number++;
                        ShowMapObject("user");

                        if (GlobalValue.GetViewFlag()) {
                            ILocationModel.GetObjectInstance("arrow_des").setDrawable(IContext.getResources().getDrawable(R.drawable.arrow_des));
                            ShowMapObject("arrow_des");
                            ShowMapObject("arrow_show");
                        }
                        else
                        {
                            ILocationModel.GetObjectInstance("arrow_des").setDrawable(IContext.getResources().getDrawable(R.drawable.nullpic));
                            ILocationModel.GetObjectInstance("arrow_show").setDrawable(IContext.getResources().getDrawable(R.drawable.nullpic));
                            ShowMapObject("arrow_des");
                            ShowMapObject("arrow_show");
                        }
                        break;
                    }
                }
                super.handleMessage(msg);
            }
        };
    }

    public void InitView(String MapID,int UserLocX,int UserLocY,int UserPivotX,int UserPivotY){
        ILocationView.RemoveMapWidget();
        MapWidget IMapWidget=ILocationModel.CreateNewMap(MapID);
        IMapWidget.setMinZoomLevel(11);
        IMapWidget.setMaxZoomLevel(11);
        ILocationModel.CreateLayer(MapID, "Users");

        ILocationModel.CreateMapObject("user", IMapWidget, ILocationModel.GetMapLayerIndex(MapID, "Users"),
                IContext.getResources().getDrawable(R.drawable.location_icon), UserLocX, UserLocY, UserPivotX, UserPivotY);

        ILocationModel.CreateMapObject("arrow_des", IMapWidget, ILocationModel.GetMapLayerIndex(MapID, "Users"),
                IContext.getResources().getDrawable(R.drawable.arrow_des), UserLocX, UserLocY, UserPivotX, UserPivotY);

        ILocationModel.CreateMapObject("arrow_show", IMapWidget, ILocationModel.GetMapLayerIndex(MapID, "Users"),
                IContext.getResources().getDrawable(R.drawable.arrow_show), UserLocX, UserLocY, UserPivotX, UserPivotY);

        ILocationView.DrawMapWidget(IMapWidget);
        IMapWidget.zoomIn();
        MapOnShow=IMapWidget;

        if (!StartUIFlag) {
            this.StartUIRefreshThread();
            StartUIFlag = true;
        }
    }

    public BitmapDrawable RotationObject(int IDNumber, float ang) {

        // 加载需要操作的图片，这里是eoeAndroid的logo图片
        Bitmap bitmapOrg = BitmapFactory.decodeResource(IContext.getResources(), IDNumber);
        //获取这个图片的宽和高
        int width = bitmapOrg.getWidth();
        int height = bitmapOrg.getHeight();


        /*定义预转换成的图片的宽度和高度
        int newWidth = 600;
        int newHeight =600;

        //计算缩放率，新尺寸除原始尺寸
        float scaleWidth = ((float) newWidth) / width;
        float scaleHeight = ((float) newHeight) / height;




        // 缩放图片动作
        matrix.postScale(scaleWidth, scaleHeight);*/

        // 创建操作图片用的matrix对象
        Matrix matrix = new Matrix();

        //旋转图片 动作
        matrix.postRotate(ang);

        // 创建新的图片
        Bitmap resizedBitmap = Bitmap.createBitmap(bitmapOrg, 0, 0,
                width, height, matrix, true);

        //将上面创建的Bitmap转换成Drawable对象，使得其可以使用在ImageView, ImageButton中
        BitmapDrawable bmd = new BitmapDrawable(resizedBitmap);
        return bmd;
    }

    public void InitLocationService(){
        ILocationModel.StartLocalizationService();
    }

    public MapWidget GetMapOnShow(){
        return MapOnShow;
    }

    public void ShowMapObject(String ObjectID){

        if(ILocationModel.GetMapObjectIsDrawnFlag(ObjectID)==true){
            ILocationView.MoveMapObjectTo(ILocationModel.GetObjectInstance(ObjectID),ILocationModel.ObjectSet.get(ObjectID).GetLocX(),ILocationModel.ObjectSet.get(ObjectID).GetLocY());
            MapOnShow.scrollMapTo(ILocationModel.ObjectSet.get(ObjectID).GetLocX(),ILocationModel.ObjectSet.get(ObjectID).GetLocY());

        }
        else{
            ILocationView.DrawMapObject(ILocationModel.GetMapWidgetOfObject(ObjectID), ILocationModel.GetLayerIndexOfObject(ObjectID), ILocationModel.GetObjectInstance(ObjectID));
            ILocationModel.SetMapObjectIsDrawnFlag(ObjectID,true);
        }
    }

    public void StartUIRefreshThread(){
        new Thread(){
            @Override
            public void run(){
                while(true){
                    try {
                        Thread.sleep(100);
                        Message message=new Message();
                        message.what=0;
                        IHandler.sendMessage(message);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

}
