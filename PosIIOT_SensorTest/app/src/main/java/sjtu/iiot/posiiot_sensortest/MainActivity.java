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
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import sjtu.iiot.posiiot_sensortest.Fragment.*;
import sjtu.iiot.posiiot_sensortest.TimeControl.Time;


public class MainActivity extends FragmentActivity implements OnClickListener {
    private FragmentManager fragmentManager = getSupportFragmentManager();
    private FragmentTransaction transaction = null;

    private Fragment TestFragment;

    PowerManager powerManager = null;
    PowerManager.WakeLock wakeLock = null;

    private TextView Phone_ID;
    private Button bt_Start;

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
        // ImageView和TetxView置为绿色，页面随之跳转

        switch (v.getId()) {
            case R.id.StartButton:
                initFragment();
                break;
            default:
                break;
        }
    }

    private void initFragment() {
        // 由于是引用了V4包下的Fragment，所以这里的管理器要用getSupportFragmentManager获取
        //  fragmentManager = getSupportFragmentManager();
        // 开启事务
        transaction = fragmentManager.beginTransaction();
        // 隐藏所有Fragment
        hideFragment(transaction);
                if (TestFragment == null) {
                    TestFragment = new TestFragment();
                    transaction.add(R.id.fl_content, TestFragment);
                } else {
                    transaction.show(TestFragment);
                }
        // 提交事务
        transaction.commit();
    }
    /* 隐藏Fragment */
    private void hideFragment(FragmentTransaction transaction) {
        if (TestFragment != null) {
            transaction.hide(TestFragment);
        }
    }

    private void initEvent() {
        /* 设置按钮监听 */
        bt_Start.setOnClickListener(this);
    }


    private void initView() {
        //Button
        this.bt_Start = (Button) findViewById(R.id.StartButton);
    }

}