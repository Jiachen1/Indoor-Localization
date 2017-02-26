package sjtu.iiot.posiiot_sensortest.Fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import sjtu.iiot.posiiot_sensortest.R;

/**
 * Created by TongXinyu on 16/8/12.
 */
public class StepFragment extends Fragment {
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.step_setting, container, false);
    }
}
