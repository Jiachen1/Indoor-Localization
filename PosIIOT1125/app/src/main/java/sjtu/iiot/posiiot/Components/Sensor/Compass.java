package sjtu.iiot.posiiot.Components.Sensor;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;

import sjtu.iiot.posiiot.GlobalValue;

/**
 * Created by TongXinyu on 16/7/6.
 */
public class Compass implements SensorEventListener {

    private SensorManager sensorManager;
    private Sensor gsensor;
    private Sensor msensor;
    private float ComR[] = new float[9];
    private float ComI[] = new float[9];
    private float[] mGravity = new float[3];
    private float[] mGeomagnetic = new float[3];
    private float orientation[]= new float[3];
    public float x = 0f;

    public Compass(Context context) {
        sensorManager = (SensorManager) context
                .getSystemService(Context.SENSOR_SERVICE);
        gsensor = sensorManager.getDefaultSensor(Sensor.TYPE_ACCELEROMETER);
        msensor = sensorManager.getDefaultSensor(Sensor.TYPE_MAGNETIC_FIELD);
        start();
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

                if (Math.abs(Math.toDegrees(orientation[2])) < 30)
                    GlobalValue.SetDetectorFlag(true);
                else
                    GlobalValue.SetDetectorFlag(false);


                /*if (Math.abs(Math.abs(Math.toDegrees(orientation[2])) - 90) < 20)
                    GlobalValue.SetDetectorZFlag(true);
                else
                    GlobalValue.SetDetectorZFlag(false);
                */
                //Log.e("KKKK",Math.toDegrees(orientation[0]) + "," + Math.toDegrees(orientation[1]) + "," + Math.toDegrees(orientation[2]));

                x = (((float) Math.toDegrees(orientation[0]) + GlobalValue.GetCompassOff()) % 360);
                GlobalValue.SetCompass(x + 120);
                //Log.i(LogTagConstants.Compass_INFO_TAG, "x (deg): " + x);
            }
        }
    }
}
