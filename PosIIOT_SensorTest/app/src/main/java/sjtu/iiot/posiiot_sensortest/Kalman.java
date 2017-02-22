package sjtu.iiot.posiiot_sensortest;

import sjtu.iiot.posiiot_sensortest.Buffer.Data;

/**
 * Created by sunjiachen on 16/11/14.
 */
public class Kalman {
    private int i;
    private void Filtering(){
        X_predict(i) = X_optimal() + Control(i-1);
        P1(i) = P(i-1) + Q;
        Kg=P1/P1+R;
        X=X1+Kg*(shijia-X1);

    }
}
