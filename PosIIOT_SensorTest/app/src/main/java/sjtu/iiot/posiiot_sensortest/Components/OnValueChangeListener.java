package sjtu.iiot.posiiot_sensortest.Components;

import java.util.Objects;

/**
 * Created by TongXinyu on 16/8/12.
 */
public interface OnValueChangeListener {

    void OnValueChange(final ListenerType Type, final int DataSize, final float[] data,String Mac);

    /* define Null Adapter class for that interface */
    public static class Null implements OnValueChangeListener{
        @Override
        public void OnValueChange(final ListenerType Type, final int DataSize, final float[] data,String Mac) {}
    }

    public static enum ListenerType
    {
        Compass,
        Acceleration,
        BLE,
    }

}
