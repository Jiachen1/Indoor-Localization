package sjtu.iiot.posiiot.Components.Sensor;

/**
 * Created by TongXinyu on 16/7/7.
 */
public interface StepListener {

    public void step(long timeNS);

    /* define Null Adapter class for that interface */
    public static class Null implements StepListener  {
        @Override
        public void step(long timeNS){};
    }

}
