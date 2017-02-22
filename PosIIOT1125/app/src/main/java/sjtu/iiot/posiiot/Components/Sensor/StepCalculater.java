package sjtu.iiot.posiiot.Components.Sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;

import sjtu.iiot.posiiot.Components.Core.Helper.CoordinateHelper;
import sjtu.iiot.posiiot.DataBase.HistoryLocDB;
import sjtu.iiot.posiiot.GlobalValue;

/**
 * Created by TongXinyu on 16/7/6.
 */
public class StepCalculater implements Runnable, SensorEventListener,StepListener{
    /** Called when the activity is first created. */

    //设置LOG标签
    private boolean doWrite = false;
    private SensorManager sm;

    private boolean StepCalculateFlag;
    public int stepcounter,spacecounter;
    public Compass mMyCompass;

    private HistoryLocDB mLocDB = null;
    private boolean instep;
    private int tmp_x;
    private int tmp_y;
    public int flag=0;
    public int step_south;
    public int step_north;
    public int step_east;
    public int step_west;


    public int Step_Distance_X=20;
    public int Step_Distance_Y=20;



    int anglebias=45;
    public int mapid=1;
    public float GrayThreshold=0;


    private StepDetector simpleStepDetector;

    public StepCalculater(Context context) {
        //创建一个SensorManager来获取系统的传感器服务
        sm = (SensorManager)context.getSystemService(Context.SENSOR_SERVICE);
        /*
         * 最常用的一个方法 注册事件
         * 参数1 ：SensorEventListener监听器
         * 参数2 ：Sensor 一个服务可能有多个Sensor实现，此处调用getDefaultSensor获取默认的Sensor
         * 参数3 ：模式 可选数据变化的刷新频率
         * */
        //  注册加速度传感器
        sm.registerListener(this, sm.getDefaultSensor(Sensor.TYPE_ACCELEROMETER), SensorManager.SENSOR_DELAY_FASTEST);//.SENSOR_DELAY_NORMAL);
        mMyCompass=new Compass(context);
        StepCalculateFlag=false;
        stepcounter=0;
        spacecounter=0;
        instep=false;

        simpleStepDetector = new StepDetector();
        simpleStepDetector.registerListener(this);
    }

    public void SetLocDB(HistoryLocDB LocDB)
    {
        mLocDB = LocDB;
    }

    public void startstep(){
        stepcounter=0;
        spacecounter=0;
        instep=false;
        StepCalculateFlag=true;
    }
    public void stopstep(){
        StepCalculateFlag = false;
    }

    public void onAccuracyChanged(Sensor sensor, int accuracy) {
        //ACT.setText("onAccuracyChanged被触发");
    }

    public void onSensorChanged(SensorEvent event) {

        if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
            simpleStepDetector.updateAccel(
                    event.timestamp, event.values[0], event.values[1], event.values[2]);
        }
    }


    @Override
    public  void step(long timeNs){

        //int angle_off = -45;
        float angle = GlobalValue.GetCompass();
        if (angle>360)
            angle-=360;
        if (angle<0)
            angle+=360;

        instep=false;
        flag=0;
        Log.i("step", "true");
        if(angle < 45 || angle > 315)
        {
            step_east++;
            //if (mLocDB.GetUserLocation().ParseLocationY() - Step_Distance_Y > 50) {
                mLocDB.SetUserLocation(CoordinateHelper.Init(mLocDB.GetUserLocation().ParseMapID(),
                        mLocDB.GetUserLocation().ParseLocationX(), mLocDB.GetUserLocation().ParseLocationY() - Step_Distance_Y));
            //}
        }

        else if(angle > 45 && angle < 135)
        {
            step_north++;
            //if (mLocDB.GetUserLocation().ParseLocationX() + Step_Distance_X < 650) {
                mLocDB.SetUserLocation(CoordinateHelper.Init(mLocDB.GetUserLocation().ParseMapID(),
                        mLocDB.GetUserLocation().ParseLocationX() + Step_Distance_X, mLocDB.GetUserLocation().ParseLocationY()));
            //}
        }

        else if(angle > 135 && angle < 225)
        {
            step_west++;
            //if (mLocDB.GetUserLocation().ParseLocationY() + Step_Distance_Y < 950 ) {
                mLocDB.SetUserLocation(CoordinateHelper.Init(mLocDB.GetUserLocation().ParseMapID(),
                        mLocDB.GetUserLocation().ParseLocationX(), mLocDB.GetUserLocation().ParseLocationY() + Step_Distance_Y));
            //}
        }

        else if(angle > 225 && angle < 315)
        {
            step_south++;
            //if (mLocDB.GetUserLocation().ParseLocationX()  - Step_Distance_X > 30) {
                mLocDB.SetUserLocation(CoordinateHelper.Init(mLocDB.GetUserLocation().ParseMapID(),
                        mLocDB.GetUserLocation().ParseLocationX() - Step_Distance_X, mLocDB.GetUserLocation().ParseLocationY()));
           // }
        }
        flag=1;
    }

    public void run() {
        // TODO Auto-generated method stub

    }
}
