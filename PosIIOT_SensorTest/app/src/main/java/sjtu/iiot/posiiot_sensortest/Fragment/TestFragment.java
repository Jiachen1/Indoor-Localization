package sjtu.iiot.posiiot_sensortest.Fragment;

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
import sjtu.iiot.posiiot_sensortest.Components.OnValueChangeListener;
import sjtu.iiot.posiiot_sensortest.Components.StepTest;
import sjtu.iiot.posiiot_sensortest.FilePro.FileWrite;
import sjtu.iiot.posiiot_sensortest.R;
import sjtu.iiot.posiiot_sensortest.TimeControl.Time;

/**
 * Created by TongXinyu on 16/8/12.
 * Modified by Sunjiachen on 16/8/14.
 */
public class TestFragment extends Fragment{

    private Button StartButton;
    private OnValueChangeListener mOnValueChangeListener;
    private StepTest mStepTest = null;
    private View v;
    private Context mContext;
    private boolean Testing = false;
    private boolean Tested = false;
    private boolean Reset = false;
    private TextView Compass_Value;
    private TextView Accel_Value;

    DecimalFormat fnum = new DecimalFormat("##0.000");
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        v = inflater.inflate(R.layout.test_setting, container, false);
        mContext = this.getContext();
        Log.d("System_Debug", "Enter Test Fragment!" );
        StartButton = (Button) v.findViewById(R.id.StartButton);
        StartButton.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View view) {
                if (!Testing && Tested) {

                    new AlertDialog.Builder(getActivity())
                            .setTitle("确认" )
                            .setMessage("重新测试最新的测量结果将丢失，如需保存，请点击取消后选择生成日志" )
                            .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    // TODO Auto-generated method stub
                                    Data.refreshBufferData();
                                    Log.d("System_Debug", "Start Test!" );
                                    Testing = true;
                                    StartButton.setText("停止测试" );
                                    mStepTest = new StepTest(mContext, mOnValueChangeListener);
                                    mStepTest.start();
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
                        StartButton.setText("停止测试" );
                        mStepTest = new StepTest(mContext, mOnValueChangeListener);
                        mStepTest.start();
                        Time.getIn().Restart();
                    } else {
                        Testing = false;
                        Tested = true;
                        StartButton.setText("重新开始" );
                        mStepTest.stop();
                        Time.getIn().Pause();
                    }
                }

            }
        });

        Compass_Value = (TextView)v.findViewById(R.id.Compass_Value);
        Accel_Value = (TextView)v.findViewById(R.id.Accel_Value);
        mOnValueChangeListener = new OnValueChangeListener() {
            @Override
            public void OnValueChange(ListenerType Type, int DataSize, float[] data) {
                switch (Type)
                {
                    case Compass:
                        Compass_Value.setText("G-X:" + fnum.format(data[0]) + " / G-Y:" + fnum.format(data[1]) + " / G-Z:" + fnum.format(data[2]));
                        if(Data.getFlag() == true) {
                            Data.setBufferData(Data.getBufferData().append("1 " + fnum.format(data[0]) + " " + fnum.format(data[1]) + " " + fnum.format(data[2]) + "\n" ));
                            Data.setFlag(false);
                        }
                            break;
                    case Acceleration:
                        Accel_Value.setText("A-X:" + fnum.format(data[0]) + " / A-Y:" + fnum.format(data[1]) + " / A-Z:" + fnum.format(data[2]));
                        if(Data.getFlag() == true) {
                            Data.setBufferData(Data.getBufferData().append("2 " + fnum.format(data[0]) + " " + fnum.format(data[1]) + " " + fnum.format(data[2]) + "\n" ));
                            Data.setFlag(false);
                        }
                            break;
                    case BLE:
                        break;
                    case WiFi:
                        break;
                    default:
                        break;
                }
            }
        };
        return v;
    }
}
