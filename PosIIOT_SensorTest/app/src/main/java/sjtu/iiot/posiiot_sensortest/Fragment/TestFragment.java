package sjtu.iiot.posiiot_sensortest.Fragment;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.view.View.OnClickListener;
import android.widget.TextView;

import junit.framework.Test;
import android.app.AlertDialog;
import java.text.DecimalFormat;

import sjtu.iiot.posiiot_sensortest.Buffer.Data;
import sjtu.iiot.posiiot_sensortest.Components.CoordinateHelper;
import sjtu.iiot.posiiot_sensortest.Components.OnValueChangeListener;
import sjtu.iiot.posiiot_sensortest.Components.iBeaconManager;
import sjtu.iiot.posiiot_sensortest.Components.iBeaconScanner;
import sjtu.iiot.posiiot_sensortest.FilePro.MyDate;
import sjtu.iiot.posiiot_sensortest.R;
import sjtu.iiot.posiiot_sensortest.TimeControl.Time;

/**
 * Created by TongXinyu on 16/8/12.
 * Modified by Sunjiachen on 16/8/14.
 */
public class TestFragment extends Fragment{

    private Button ReStartButton;
    private OnValueChangeListener mOnValueChangeListener;
    //private StepTest mStepTest = null;
    private View v;

    private iBeaconScanner mIBeaconScanner;
    private iBeaconManager mIBeaconManager;

    private Context mContext;
    private boolean Testing =false;
    private boolean Tested = false;

    DecimalFormat fnum = new DecimalFormat("##0.000");
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.test_setting, container, false);
        mContext = this.getContext();
        Log.d("System_Debug", "Enter Test Fragment!" );

        mIBeaconManager = new iBeaconManager(1);
        /* Test iBeacon */
        mIBeaconManager.AddiBeacon("20:C3:8F:D1:75:6B", CoordinateHelper.Init(1, 670, 500),0);        //50A
        mIBeaconManager.AddiBeacon("20:C3:8F:D1:E7:1B", CoordinateHelper.Init(1, 670, 950),0);      //46a
        mIBeaconManager.AddiBeacon("84:EB:18:58:A4:56", CoordinateHelper.Init(1, 50, 950),0);       //197b
        mIBeaconManager.AddiBeacon("20:C3:8F:D1:75:2F", CoordinateHelper.Init(1, 50, 500),0);      //24a
        mIBeaconManager.AddiBeacon("84:EB:18:58:A4:6A", CoordinateHelper.Init(1, 50, 50),0);      //24a
        mIBeaconManager.AddiBeacon("20:C3:8F:D1:E7:4F", CoordinateHelper.Init(1, 670, 50), 0);      //24a

        ReStartButton = (Button) v.findViewById(R.id.ReStartButton);
        ReStartButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Testing && Tested) {

                    new AlertDialog.Builder(getActivity())
                            .setTitle("确认" )
                            .setMessage("重新测试最新的测量结果将丢失" )
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub
                                    Data.refreshBufferBlE();
                                    Log.d("System_Debug", "Start Test!" );
                                    Testing = true;
                                    ReStartButton.setText("停止测试" );
                                    Time.getIn().Restart();
                                }
                            })
                            .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub
                                }
                            })
                            .show();

                }

                else {
                    if (!Testing) {
                        Log.d("System_Debug", "Start Test!" );
                        Testing = true;
                        ReStartButton.setText("停止测试" );
                        mIBeaconScanner=new iBeaconScanner(mContext,mOnValueChangeListener);
                        //Check whether BT is OK
                        if(mIBeaconScanner.checkBleHardwareAvailable() == false) {
                            Log.e("System", "No BT");
                        }
                        // initialize BleWrapper object
                        mIBeaconScanner.initialize();
                        mIBeaconScanner.SetManager(mIBeaconManager);
                        mIBeaconScanner.beginScanning();
                        Time.getIn().Restart();
                    } else {
                        Testing = false;
                        Tested = true;
                        ReStartButton.setText("重新开始" );
                        Time.getIn().Pause();
                    }
                }

            }
        });

        mOnValueChangeListener = new OnValueChangeListener() {
            @Override
            public void OnValueChange(ListenerType Type, int DataSize, float[] data,String mac) {
                switch (Type)
                {
                    case Compass:
                        break;
                    case Acceleration:
                        break;
                    case BLE:
                        Data.setBufferBlE(Data.getBufferBLE().append(mac + " " + data[0] + " " + MyDate.getDateEN()+"\n"));
                        break;
                    default:
                        break;
                }
            }
        };
        return v;
    }
}
