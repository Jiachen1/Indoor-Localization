package sjtu.iiot.posiiot.Activity;


import android.app.AlarmManager;
import android.app.AlertDialog;
import android.app.PendingIntent;
import android.bluetooth.BluetoothAdapter;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.ls.widgets.map.MapWidget;
import com.ls.widgets.map.model.MapObject;

import java.lang.reflect.Field;
import java.util.HashMap;

import sjtu.iiot.posiiot.Components.Communications.Communications;
import sjtu.iiot.posiiot.DataStructure.LogTagConstants;
import sjtu.iiot.posiiot.GlobalValue;
import sjtu.iiot.posiiot.Model.RoutingAlgorithm;
import sjtu.iiot.posiiot.Presenter.LocationPresenter;
import sjtu.iiot.posiiot.R;
import sjtu.iiot.posiiot.View.LocationView;

import static android.os.SystemClock.sleep;

/**
 * Created by TongXinyu on 16/7/8.
 */
public class MainActivity extends FragmentActivity implements OnClickListener,LocationView {


    private AlarmManager alarmManager;
    private PendingIntent pi1,pi2,pi3,pi4,pi5;
    private int CurMap = 1;
    private int MapMat[] = {1,1,1,2,2,0,0,0,0,0,0};
    private BluetoothAdapter adpter= BluetoothAdapter.getDefaultAdapter();
    private RoutingAlgorithm mRoutingAlgorithm;
    private int GetTrace[][] = null;
    private int Destination[] = {1,2,3,4,5,6,7};
    private LocationPresenter ILocationPresenter;
    private View v;
    private PopupWindow popupwindow, popupwindow2;
    private boolean flag = false;
    private boolean flag_outside = false;
    private ImageView Arrow_Icon;
    private ImageButton where_layout,setting;
    private LinearLayout layout;
    private RelativeLayout ShowTop;
    private Handler mHandler;
    private TextView mCurFloorTextView;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_map);
        mRoutingAlgorithm = new RoutingAlgorithm();
        layout = (LinearLayout) this.findViewById(R.id.LocationMap);
        Arrow_Icon = (ImageView) this.findViewById(R.id.CompassDir);
        where_layout = (ImageButton) this.findViewById(R.id.wheretogo);
        setting = (ImageButton) this.findViewById(R.id.setting);
        ShowTop = (RelativeLayout) this.findViewById(R.id.ShowTop);
        mCurFloorTextView = (TextView) this.findViewById(R.id.CurFloorTextView);


        where_layout.setOnClickListener(this);
        setting.setOnClickListener(this);
        GlobalValue.SetNavFlagFlag(false);
        GlobalValue.SetViewFlag(false);
        GlobalValue.SetCurPos(1);
        GlobalValue.SetDesNumber(0);

        Class<?> c = null;
        try {
            c = Class.forName("com.ls.widgets.map.utils.Resources");
            Object obj = c.newInstance();
            Field field = c.getDeclaredField("LOGO");
            field.setAccessible(true);
            field.set(obj, null);
        } catch (Exception e) {
            e.printStackTrace();
        }
        ILocationPresenter = new LocationPresenter(this, this.getApplicationContext(), Arrow_Icon,layout);
        ILocationPresenter.InitView("Map_1st_floor_revise", 1000, 1000, 0, 0);

        ILocationPresenter.InitLocationService();
        initBroadcast();

        mHandler=new Handler()
        {
            public void handleMessage(Message msg)
            {
                Log.e("MSG",msg.what + "woikap");
                switch(msg.what)
                {
                    case 1: {
                        ILocationPresenter.InitView("Map_1st_floor_revise", 1000, 1000, 0, 0);
                        mCurFloorTextView.setText("Current Floor : 1st");
                        break;
                    }
                    case 2: {
                        ILocationPresenter.InitView("Map_2nd_floor", 1000, 1000, 0, 0);
                        mCurFloorTextView.setText("Current Floor : 2nd");
                        break;
                    }
                    case 3: {
                        ILocationPresenter.InitView("Map_B1_floor", 1000, 1000, 0, 0);
                        mCurFloorTextView.setText("Current Floor : B1");
                        break;
                    }
                }
                super.handleMessage(msg);
            }
        };


        this.StartUIRefreshMap();
/*
        Thread SwitchMap = new Thread(new Runnable()
        {
            @Override
            public void run()
            {
                sleep(1000);
                Log.d("lll","popo " + MapMat[GlobalValue.GetCurPos()-1]);
                if (CurMap != MapMat[GlobalValue.GetCurPos()-1])
                {
                    CurMap = MapMat[GlobalValue.GetCurPos()-1];
                    if (CurMap == 1)
                    {
                        ILocationPresenter.InitView("Map_1st_floor", 1000, 1000, 0, 0);
                        ILocationPresenter.InitLocationService();
                    }

                    if (CurMap == 2 || CurMap == 0)
                    {
                        ILocationPresenter.InitView("Map_2nd_floor", 1000, 1000, 0, 0);
                        ILocationPresenter.InitLocationService();
                    }
                }
            }
        });

        SwitchMap.start();*/
        if (!adpter.isEnabled()) {
            new AlertDialog.Builder(this)
                    .setTitle("Bluetooth Ensure")
                    .setMessage("PosIIOT System needs user open bluetooth")
                    .setPositiveButton("Open", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                            adpter.enable();
                        }
                    })
                    .setNegativeButton("Cancel(lose accuracy)", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // TODO Auto-generated method stub
                        }
                    })
                    .show();
        }
    }

    public void StartUIRefreshMap(){
        new Thread(){
            @Override
            public void run(){
                while(true){
                    try {
                        Thread.sleep(1000);
                        Message mes=new Message();

                        if (CurMap != MapMat[GlobalValue.GetCurPos()-1])
                        {
                            CurMap = MapMat[GlobalValue.GetCurPos()-1];

                            if (CurMap == 0)
                            {
                                mes.what=3;
                            }
                            if (CurMap == 1)
                            {
                                mes.what=1;
                            }

                            if (CurMap == 2)
                            {
                                mes.what=2;
                            }
                        }
                        mHandler.sendMessage(mes);
                    } catch (InterruptedException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }
                }
            }
        }.start();
    }

    @Override
    public void onClick(View v) {
        View view_show = (View)this.findViewById(R.id.ShowTop);
        switch (v.getId()) {
            case R.id.wheretogo:
                if (popupwindow != null && popupwindow.isShowing()) {
                    //wheretogo.setBackgroundColor(0x66BFBFBF);
                    popupwindow.dismiss();
                    popupwindow = null;
                } else {
                    //wheretogo.setBackgroundColor(0xff404040);
                    initmPopupWindowView();
                    popupwindow.showAtLocation(view_show, Gravity.TOP| Gravity.CENTER_HORIZONTAL, 0, where_layout.getHeight() + 40);
                    //popupwindow.showAsDropDown(v,0,0);
                }
                break;
            case R.id.setting:
                if (popupwindow2 != null && popupwindow2.isShowing()) {
                    //wheretogo.setBackgroundColor(0x66BFBFBF);
                    popupwindow2.dismiss();
                    popupwindow2 = null;
                } else {
                    //wheretogo.setBackgroundColor(0xff404040);
                    initmPopupWindowView2();
                    Log.d("sys", "onClick: iam here");
                    popupwindow2.showAtLocation(view_show, Gravity.BOTTOM | Gravity.CENTER_HORIZONTAL, 0, setting.getHeight());
                    //popupwindow.showAsDropDown(v, 0, 0);
                }
                break;
            case R.id.TimeButton2:
                GetTrace = mRoutingAlgorithm.getTrace(GlobalValue.GetCurPos(),1);
                GlobalValue.SetNavFlagFlag(true);
                GlobalValue.SetTraceMat(GetTrace);
                GlobalValue.SetDesNumber(1);
                Log.d("Des","Des LOC = " + Destination[0]);
                popupwindow2.dismiss();
                popupwindow2 = null;
                break;
            case R.id.TimeButton3:
                GetTrace = mRoutingAlgorithm.getTrace(GlobalValue.GetCurPos(),1);
                GlobalValue.SetNavFlagFlag(true);
                GlobalValue.SetTraceMat(GetTrace);
                GlobalValue.SetDesNumber(1);
                Log.d("Des","Des LOC = " + Destination[1]);
                popupwindow2.dismiss();
                popupwindow2 = null;
                break;
            case R.id.TimeButton4:
                GetTrace = mRoutingAlgorithm.getTrace(GlobalValue.GetCurPos(),10);
                GlobalValue.SetNavFlagFlag(true);
                GlobalValue.SetTraceMat(GetTrace);
                GlobalValue.SetDesNumber(10);
                Log.d("Des","Des LOC = " + Destination[2]);
                popupwindow2.dismiss();
                popupwindow2 = null;
                break;
            case R.id.TimeButton5:
                Log.d("Des","Des LOC = " + Destination[3]);
                popupwindow2.dismiss();
                popupwindow2 = null;
                break;
            case R.id.TimeButton6:
                Log.d("Des","Des LOC = " + Destination[4]);
                popupwindow2.dismiss();
                popupwindow2 = null;
                break;
            /*case R.id.TimeButton7:
                Log.d("Des","Des LOC = " + Destination[5]);
                popupwindow2.dismiss();
                popupwindow2 = null;
                break;
            case R.id.TimeButton8:
                Log.d("Des","Des LOC = " + Destination[6]);
                popupwindow2.dismiss();
                popupwindow2 = null;
                break;
*/
            case R.id.PosButton2:
                GetTrace = mRoutingAlgorithm.getTrace(GlobalValue.GetCurPos(),4);
                GlobalValue.SetNavFlagFlag(true);
                GlobalValue.SetTraceMat(GetTrace);
                GlobalValue.SetDesNumber(4);
                Log.d("Des","Des LOC /= " + GetTrace[0][0] + GetTrace[0][1]);
                popupwindow.dismiss();
                popupwindow = null;
                break;
            case R.id.PosButton3:
                Log.d("Des","Des LOC = " + Destination[1]);
                popupwindow.dismiss();
                popupwindow = null;
                break;
            case R.id.PosButton4:
                Log.d("Des","Des LOC = " + Destination[2]);
                popupwindow.dismiss();
                popupwindow = null;
                break;
            case R.id.PosButton5:
                Log.d("Des","Des LOC = " + Destination[3]);
                popupwindow.dismiss();
                popupwindow = null;
                break;
            case R.id.PosButton6:
                popupwindow.dismiss();
                popupwindow = null;
                break;
            case R.id.PosButton7:
                GetTrace = mRoutingAlgorithm.getTrace(GlobalValue.GetCurPos(),2);
                GlobalValue.SetNavFlagFlag(true);
                GlobalValue.SetTraceMat(GetTrace);
                GlobalValue.SetDesNumber(2);
                Log.d("Des","Des LOC /= " + GetTrace[0][0] + GetTrace[0][1]);
                popupwindow.dismiss();
                popupwindow = null;
                break;
            case R.id.PosButton8:
                GetTrace = mRoutingAlgorithm.getTrace(GlobalValue.GetCurPos(),5);
                GlobalValue.SetNavFlagFlag(true);
                GlobalValue.SetTraceMat(GetTrace);
                GlobalValue.SetDesNumber(5);
                Log.d("Des","Des LOC /= " + GetTrace[0][0] + GetTrace[0][1]);
                popupwindow.dismiss();
                popupwindow = null;
                break;
            default:
                break;
        }
    }

    @Override
    public void MoveMapObjectTo(MapObject IMapObject, int x, int y) {
        IMapObject.moveTo(x, y);
    }

    @Override
    public void DrawMapObject(MapWidget IMapWidget, int LayerIndex, MapObject IMapObject) {
        ILocationPresenter.GetMapOnShow().getLayerById(LayerIndex).addMapObject(IMapObject);
    }

    @Override
    public void DrawMapWidget(MapWidget IMapWidget) {
        Log.d(LogTagConstants.Draw_DEBUG_Tag, "Draw Map Widget");
        layout.addView(IMapWidget);
    }

    @Override
    public void RemoveMapWidget() {
        layout.removeAllViewsInLayout();
    }

    public void initmPopupWindowView() {

        // // 获取自定义布局文件pop.xml的视图
        View customView = getLayoutInflater().inflate(R.layout.pop,
                null, false);
        // 创建PopupWindow实例
        popupwindow = new PopupWindow(customView, AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT);
        popupwindow.setOutsideTouchable(true);
        popupwindow.setBackgroundDrawable(new ColorDrawable(0x00000000));
        // 设置动画效果 [R.style.AnimationFade 是自己事先定义好的]
        //popupwindow.setAnimationStyle(R.style.add_new_style);
        // 自定义view添加触摸事件
        customView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (popupwindow != null && popupwindow.isShowing()) {
                    popupwindow.dismiss();
                    popupwindow = null;
                    return true;
                }
                return false;
            }
        });
        popupwindow.setTouchInterceptor(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    popupwindow.dismiss();
                    popupwindow = null;
                    return true;
                }
                return false;
            }
        });
        /** 在这里可以实现自定义视图的功能 */
        Button Locbut2 = (Button) customView.findViewById(R.id.PosButton2);
        Button Locbut3 = (Button) customView.findViewById(R.id.PosButton3);
        Button Locbut4 = (Button) customView.findViewById(R.id.PosButton4);
        Button Locbut5 = (Button) customView.findViewById(R.id.PosButton5);
        Button Locbut6 = (Button) customView.findViewById(R.id.PosButton6);
        Button Locbut7 = (Button) customView.findViewById(R.id.PosButton7);
        Button Locbut8 = (Button) customView.findViewById(R.id.PosButton8);

        Locbut2.setOnClickListener(this);
        Locbut3.setOnClickListener(this);
        Locbut4.setOnClickListener(this);
        Locbut5.setOnClickListener(this);
        Locbut6.setOnClickListener(this);
        Locbut7.setOnClickListener(this);
        Locbut8.setOnClickListener(this);

    }

    public void initmPopupWindowView2() {

        // // 获取自定义布局文件pop.xml的视图
        View customView = getLayoutInflater().inflate(R.layout.pop2,
                null, false);
        // 创建PopupWindow实例
        popupwindow2 = new PopupWindow(customView, AbsListView.LayoutParams.WRAP_CONTENT, AbsListView.LayoutParams.WRAP_CONTENT);
        popupwindow2.setOutsideTouchable(true);
        popupwindow2.setBackgroundDrawable(new ColorDrawable(0x00000000));
        // 设置动画效果 [R.style.AnimationFade 是自己事先定义好的]
        //popupwindow.setAnimationStyle(R.style.add_new_style);
        // 自定义view添加触摸事件
        customView.setOnTouchListener(new View.OnTouchListener() {

            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (popupwindow2 != null && popupwindow2.isShowing()) {
                    popupwindow2.dismiss();
                    popupwindow2 = null;
                    return true;
                }
                return false;
            }
        });
        popupwindow2.setTouchInterceptor(new View.OnTouchListener() {
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_OUTSIDE) {
                    popupwindow2.dismiss();
                    popupwindow2 = null;
                    return true;
                }
                return false;
            }
        });
        /** 在这里可以实现自定义视图的功能 */
        Button Timebut2 = (Button) customView.findViewById(R.id.TimeButton2);
        Button Timebut3 = (Button) customView.findViewById(R.id.TimeButton3);
        Button Timebut4 = (Button) customView.findViewById(R.id.TimeButton4);
        Button Timebut5 = (Button) customView.findViewById(R.id.TimeButton5);
        Button Timebut6 = (Button) customView.findViewById(R.id.TimeButton6);
        //Button Timebut7 = (Button) customView.findViewById(R.id.TimeButton7);
        //Button Timebut8 = (Button) customView.findViewById(R.id.TimeButton8);

        Timebut2.setOnClickListener(this);
        Timebut3.setOnClickListener(this);
        Timebut4.setOnClickListener(this);
        Timebut5.setOnClickListener(this);
        Timebut6.setOnClickListener(this);
        //Timebut7.setOnClickListener(this);
        //Timebut8.setOnClickListener(this);
    }

    public void initBroadcast()
    {
        Intent intent = new Intent("1");
        intent.putExtra("msg",1);
       /* pi1 = PendingIntent.getBroadcast(this, 0, intent, 0);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()
                , pi1);

        Intent intent1 = new Intent("2");
        intent1.putExtra("msg", 2);
        pi1 = PendingIntent.getBroadcast(this, 0, intent1, 0);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis() + 100000
                , pi1);

        Intent intent2 = new Intent("3");
        intent2.putExtra("msg",3);
        pi3 = PendingIntent.getBroadcast(this, 0, intent2, 0);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+200000
                ,  pi3);

        Intent intent3 = new Intent("4");
        intent3.putExtra("msg",4);
        pi4 = PendingIntent.getBroadcast(this, 0, intent3, 0);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+300000
                ,  pi4);

        Intent intent4 = new Intent("5");
        intent4.putExtra("msg",5);
        pi5 = PendingIntent.getBroadcast(this, 0, intent4, 0);
        alarmManager = (AlarmManager) getSystemService(ALARM_SERVICE);
        alarmManager.set(AlarmManager.RTC_WAKEUP, System.currentTimeMillis()+400000
                ,  pi5);*/
    }


}
