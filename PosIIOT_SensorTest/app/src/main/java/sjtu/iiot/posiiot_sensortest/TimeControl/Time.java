package sjtu.iiot.posiiot_sensortest.TimeControl;

import sjtu.iiot.posiiot_sensortest.Buffer.Data;
import sjtu.iiot.posiiot_sensortest.Kalman;

/**
 * Created by sunjiachen on 16/8/15.
 */
public class Time {
    private clockThread mclock = null;
    public static Time INSTANCE = null;

    private Time()
    {
        if (mclock == null)
        {
            mclock = new clockThread();
        }
    }

    public static Time getIn()
    {
        if (INSTANCE == null)
        {
            INSTANCE = new Time();
        }
        return INSTANCE;
    }
    public void start() {
        if (mclock == null)
            mclock = new clockThread();
            mclock.start();
    }
    public void stop()
    {
        if(mclock != null)
            mclock.interrupt();
            mclock = null;
    }
    public void Pause(){
        mclock.onThreadPause();
    }
    public void Restart(){
        mclock.onThreadResume();
    }
    private class clockThread extends Thread {
        private boolean isPause = false;
        private boolean isInterrupted = false;
        public synchronized void onThreadPause() {
            isPause = true;
        }
        public synchronized void onThreadResume() {
            isPause = false;
            this.notify();
        }
        public void interrupt()
        {
            isInterrupted = true;
            super.interrupt();
        }
        @Override
        public void run() {
            // TODO Auto-generated method stub
            while (!isInterrupted) {
                while (!isPause) {
                    try {
                        Thread.sleep(2000);
                        //Log.i("system", "I AM RUNNING" );
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }
    }
}
