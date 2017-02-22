package sjtu.iiot.posiiot.Components.Communications;

/**
 * Created by shawn on 3/30/15.
 */

public interface BLIObserver {
    public void onBLUpdate(int notificationType, String args);
}
