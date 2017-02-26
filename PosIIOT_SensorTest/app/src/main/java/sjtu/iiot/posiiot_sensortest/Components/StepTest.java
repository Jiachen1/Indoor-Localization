package sjtu.iiot.posiiot_sensortest.Components;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.util.Log;
import sjtu.iiot.posiiot_sensortest.Components.OnValueChangeListener.ListenerType;
/**
 * Created by TongXinyu on 16/8/12.
 */
public class StepTest implements SensorEventListener {

    private SensorManager sensorManager;
    private OnValueChangeListener mListener;
    private Sensor gsensor;
    private Sensor msensor;
    private float ComR[] = new float[9];
    private float ComI[] = new float[9];
    private float[] mGravity = new float[3];
    private float[] mGeomagnetic = new float[3];
    private float orientation[] = new float[3];
    private float Accel[] = new float[3];

    public StepTest(Context context,OnValueChangeListener Listener) {
        sensorManager = (SensorManager) context
                .getSystemService(Context.SENSOR_SERVICE);
        mListener = Listener;
        gsensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        msensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
    }

    public void start() {
        sensorManager.registerListener(this,gsensor,SensorManager.SENSOR_DELAY_FASTEST);
        sensorManager.registerListener(this,msensor,SensorManager.SENSOR_DELAY_FASTEST);
    }

    public void stop() {
        sensorManager.unregisterListener(this);
    }

    @Override
    public void onAccuracyChanged(Sensor sensor, int accuracy)
    {

    }

    @Override
    public void onSensorChanged(SensorEvent event) {
        final float alpha = 0.97f;

        synchronized (this) {
            if (event.sensor.getType() == Sensor.TYPE_ACCELEROMETER) {
                mGravity[0] = alpha * mGravity[0] + (1 - alpha)
                        * event.values[0];
                mGravity[1] = alpha * mGravity[1] + (1 - alpha)
                        * event.values[1];
                mGravity[2] = alpha * mGravity[2] + (1 - alpha)
                        * event.values[2];
                Accel[0] = event.values[0] - mGravity[0];
                Accel[1] = event.values[1] - mGravity[1];
                Accel[2] = event.values[2] - mGravity[2];

                mListener.OnValueChange(ListenerType.Acceleration,3,Accel);
            }


            if (event.sensor.getType() == Sensor.TYPE_MAGNETIC_FIELD) {

                mGeomagnetic[0] = alpha * mGeomagnetic[0] + (1 - alpha)
                        * event.values[0];
                mGeomagnetic[1] = alpha * mGeomagnetic[1] + (1 - alpha)
                        * event.values[1];
                mGeomagnetic[2] = alpha * mGeomagnetic[2] + (1 - alpha)
                        * event.values[2];
            }


            if (SensorManager.getRotationMatrix(ComR,ComI,mGravity,mGeomagnetic))
            {
                SensorManager.getOrientation(ComR, orientation);
                mListener.OnValueChange(ListenerType.Compass,3,orientation);
            }
        }
    }
}
