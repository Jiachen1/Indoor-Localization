package sjtu.iiot.posiiot_sensortest.Buffer;

/**
 * Created by sunjiachen on 16/8/14.
 */
public class Data {
    private static boolean Flag = true;
    private static StringBuffer BufferData = new StringBuffer();
    private static StringBuffer BufferProfile = new StringBuffer();

    public static StringBuffer getBufferData()
    {
        return BufferData;
    }

    public static void setBufferData(StringBuffer a)
    {
        BufferData = a;
    }

    public static StringBuffer getBufferProfile()
    {
        return BufferProfile;
    }

    public static void setBufferProfile(StringBuffer b)
    {
        BufferProfile = b;
    }
    public static boolean getFlag()
    {
        return Flag;
    }
    public static void setFlag(boolean c)
    {
        Flag = c;
    }
    public static void refreshBufferData ()
    {
        BufferData.delete(0,BufferData.length());
    }
    public static void refreshBufferProfile()
    {
        BufferProfile.delete(0,BufferProfile.length());
    }
}
