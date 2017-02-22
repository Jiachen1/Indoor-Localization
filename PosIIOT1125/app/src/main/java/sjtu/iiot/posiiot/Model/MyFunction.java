package sjtu.iiot.posiiot.Model;

/**
 * Created by TongXinyu on 16/7/12.
 */
public class MyFunction {


    public MyFunction()
    {

    }

    public int[] ModifyAlg(int SumWeight,int SumX,int SumY,int UserLocX,int UserLocY)
    {
        return ModifyAlgorithm(SumWeight,SumX,SumY,UserLocX,UserLocY);
    }

    private int[] ModifyAlgorithm(int SumWeight,int SumX,int SumY,int UserLocX,int UserLocY) {
        int [] tmp = new int[2];
        if (SumWeight > 2000) {
            UserLocX = (int) (UserLocX * 0.2 + SumX / SumWeight * 0.8);
            UserLocY = (int) (UserLocY * 0.2 + SumY / SumWeight * 0.8);
        } else if (SumWeight >= 400) {
            UserLocX = (int) ((UserLocX * (2500.0 - SumWeight) + SumX) / 2500.0);
            UserLocY = (int) ((UserLocY * (2500.0 - SumWeight) + SumY) / 2500.0);
        }

        tmp[0] = UserLocX;
        tmp[1] = UserLocY;
        return tmp;
    }

    public int[] OBDAlg(int SumValue,int BLE_LocX,int BLE_LocY,int User_LocX,int User_LocY)
    {
        return OBDAlgorithm(SumValue,BLE_LocX,BLE_LocY,User_LocX,User_LocY);
    }

    private int[] OBDAlgorithm(int SumValue,int BLE_LocX,int BLE_LocY,int User_LocX,int User_LocY)
    {
        int [] tmp = new int[2];
        if (SumValue >= 700.0) {
            User_LocX = (int) (0.3 * User_LocX + 0.7 * Math.abs(BLE_LocX));
            User_LocY = (int) (0.3 * User_LocY + 0.7 * Math.abs(BLE_LocY));
        } else {
            User_LocX = (int) (User_LocX * (1 - SumValue / 1000.0) + BLE_LocX * SumValue / 1000.0);
            User_LocY = (int) (User_LocY * (1 - SumValue / 1000.0) + BLE_LocY * SumValue / 1000.0);
        }

        tmp[0] = User_LocX;
        tmp[1] = User_LocY;

        return tmp;
    }



}
