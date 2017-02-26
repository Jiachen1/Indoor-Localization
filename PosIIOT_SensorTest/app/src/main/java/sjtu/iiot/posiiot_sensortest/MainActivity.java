package sjtu.iiot.posiiot_sensortest;
/*
Modified by Sunjiachen on 8/14/2016
 */

import android.os.PowerManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.LinearLayout;
import android.widget.TextView;


import java.util.Date;

import sjtu.iiot.posiiot_sensortest.Buffer.Data;
import sjtu.iiot.posiiot_sensortest.FilePro.FileWrite;
import sjtu.iiot.posiiot_sensortest.FilePro.MyDate;
import sjtu.iiot.posiiot_sensortest.Fragment.*;
import sjtu.iiot.posiiot_sensortest.TimeControl.Time;


public class MainActivity extends FragmentActivity implements OnClickListener {
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragmentTransaction transaction = null;
    private LinearLayout ll_step;
    private LinearLayout ll_ble;
    private LinearLayout ll_test;
    private LinearLayout ll_wifi;

    private Fragment StepFragment;
    private Fragment BLEFragment;
    private Fragment TestFragment;
    private Fragment WiFiFragment;

    PowerManager powerManager = null;
    PowerManager.WakeLock wakeLock = null;


    private TextView Phone_ID;
    private TextView tv_step;
    private TextView tv_ble;
    private TextView tv_test;
    private TextView tv_wifi;
    private CheckBox cb_step;
    private CheckBox cb_ble;
    private CheckBox cb_wifi;
    private Button bt_log;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Phone_ID = (TextView) findViewById(R.id.Phone_Version);
        Phone_ID.setText("手机型号：" + android.os.Build.MODEL + "\n安卓版本："
                + android.os.Build.VERSION.RELEASE);

        powerManager = (PowerManager) this.getSystemService(this.POWER_SERVICE);
        wakeLock = this.powerManager.newWakeLock(PowerManager.FULL_WAKE_LOCK, "My Lock");

        // 初始化控件
        initView();
        // 初始化按钮事件
        initEvent();
        // 初始化并设置当前Fragment
        initFragment(0);
        //initEventOnAddress();
        Time.getIn().start();
    }
    @Override
    protected void onResume() {
        super.onResume();
        wakeLock.acquire();
    }

    @Override
    protected void onPause() {
        super.onPause();
        wakeLock.release();
    }


    @Override
    public void onClick(View v) {
        /* 在每次点击后将所有的按钮(TextView)颜色改为灰色，然后根据点击着色 */
        restartBotton();
        // ImageView和TetxView置为绿色，页面随之跳转
        switch (v.getId()) {
            case R.id.ll_step:
                if (cb_step.isChecked() == true) {
                    tv_step.setTextColor(0xff000000);
                    initFragment(0);
                }
                break;
            case R.id.ll_ble:
                if (cb_ble.isChecked() == true) {
                    tv_ble.setTextColor(0xff000000);
                    initFragment(1);
                }
                break;
            case R.id.ll_test:
                tv_test.setTextColor(0xff000000);
                initFragment(2);
                break;
            case R.id.ll_wifi:
                if (cb_wifi.isChecked() == true) {
                    tv_wifi.setTextColor(0xff000000);
                    initFragment(3);
                }
                break;
            case R.id.LogButton:
                Time.getIn().Pause();
                FileWrite.getInstance(this, Data.getBufferData(), "DATA").start();
                Setprofile();
                FileWrite.getInstance(this, Data.getBufferProfile(), "PRO").start();
                Data.refreshBufferData();
                Data.refreshBufferProfile();
                transaction = fragmentManager.beginTransaction();
                transaction.remove(TestFragment);
                TestFragment = new TestFragment();
                transaction.add(R.id.fl_content, TestFragment);
                transaction.commit();
                break;
            default:
                break;
        }
    }

    private void restartBotton() {
        /* TextView置为白色 */
        tv_step.setTextColor(0xffffffff);
        tv_ble.setTextColor(0xffffffff);
        tv_test.setTextColor(0xffffffff);
        tv_wifi.setTextColor(0xffffffff);
    }

    private void initFragment(int index) {
        // 由于是引用了V4包下的Fragment，所以这里的管理器要用getSupportFragmentManager获取
      //  fragmentManager = getSupportFragmentManager();
        // 开启事务
        transaction = fragmentManager.beginTransaction();
        // 隐藏所有Fragment
        hideFragment(transaction);
        switch (index) {
            case 0:
                if (StepFragment == null) {
                    StepFragment = new StepFragment();
                    transaction.add(R.id.fl_content, StepFragment);
                } else {
                    transaction.show(StepFragment);
                }
                break;
            case 1:
                if (BLEFragment == null) {
                    BLEFragment = new BLEFragment();
                    transaction.add(R.id.fl_content, BLEFragment);
                } else {
                    transaction.show(BLEFragment);
                }
                break;
            case 2:
                if (TestFragment == null) {
                    TestFragment = new TestFragment();
                    transaction.add(R.id.fl_content, TestFragment);
                } else {
                    transaction.show(TestFragment);
                }
                break;
            case 3:
                if (WiFiFragment == null) {
                    WiFiFragment = new WiFiFragment();
                    transaction.add(R.id.fl_content, WiFiFragment);
                } else {
                    transaction.show(WiFiFragment);
                }
                break;
            default:
                break;
        }
        // 提交事务
        transaction.commit();
    }
    private void addFragment(FragmentTransaction transaction)
    {
        transaction.add(R.id.fl_content, StepFragment);
        transaction.add(R.id.fl_content, BLEFragment);
        transaction.add(R.id.fl_content, WiFiFragment);

    }
    /* 隐藏Fragment */
    private void hideFragment(FragmentTransaction transaction) {
        if (StepFragment != null) {
            transaction.hide(StepFragment);
        }
        if (BLEFragment != null) {
            transaction.hide(BLEFragment);
        }
        if (TestFragment != null) {
            transaction.hide(TestFragment);
        }
        if (WiFiFragment != null) {
            transaction.hide(WiFiFragment);
        }
    }

    private void initEvent() {
        /* 设置按钮监听 */
        ll_step.setOnClickListener(this);
        ll_ble.setOnClickListener(this);
        ll_test.setOnClickListener(this);
        ll_wifi.setOnClickListener(this);
        bt_log.setOnClickListener(this);
    }


    private void initView() {
        // 菜单2个Linearlayout
        this.ll_step = (LinearLayout) findViewById(R.id.ll_step);
        this.ll_ble = (LinearLayout) findViewById(R.id.ll_ble);
        this.ll_test = (LinearLayout) findViewById(R.id.ll_test);
        this.ll_wifi = (LinearLayout) findViewById(R.id.ll_wifi);

        // 菜单2个菜单标题
        this.tv_step = (TextView) findViewById(R.id.tv_step);
        this.tv_ble = (TextView) findViewById(R.id.tv_ble);
        this.tv_test = (TextView) findViewById(R.id.tv_test);
        this.tv_wifi = (TextView) findViewById(R.id.tv_wifi);

        // checkbox
        this.cb_wifi = (CheckBox) findViewById(R.id.cb_wifi);
        this.cb_ble = (CheckBox) findViewById(R.id.cb_ble);
        this.cb_step = (CheckBox) findViewById(R.id.cb_step);

        //Button
        this.bt_log = (Button) findViewById(R.id.LogButton);
        tv_step.setTextColor(0xff000000);
    }

    private void Setprofile() {
        Data.setBufferProfile(Data.getBufferProfile().append("1 " + android.os.Build.MODEL + "\n" + "2 " + android.os.Build.VERSION.RELEASE
                + "\n"));
        if (cb_ble.isChecked() == true)
            Data.setBufferProfile(Data.getBufferProfile().append("3 1\n"));
        else
            Data.setBufferProfile(Data.getBufferProfile().append("3 0\n"));
        if (cb_step.isChecked() == true)
            Data.setBufferProfile(Data.getBufferProfile().append("4 1\n"));
        else
            Data.setBufferProfile(Data.getBufferProfile().append("4 0\n"));
        if (cb_wifi.isChecked() == true)
            Data.setBufferProfile(Data.getBufferProfile().append("5 1\n"));
        else
            Data.setBufferProfile(Data.getBufferProfile().append("5 0\n"));
        Data.setBufferProfile(Data.getBufferProfile().append(MyDate.getDateEN()));

    }
}