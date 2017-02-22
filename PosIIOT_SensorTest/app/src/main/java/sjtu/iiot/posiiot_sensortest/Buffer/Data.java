package sjtu.iiot.posiiot_sensortest.Buffer;

/**
 * Created by sunjiachen on 16/8/14.
 */
public class Data {

    private static StringBuffer BufferBLE = new StringBuffer();
    public static void setBufferBlE(StringBuffer d) {BufferBLE = d;}
    public static StringBuffer getBufferBLE() {return BufferBLE;}
    public static void refreshBufferBlE()
    {
        BufferBLE.delete(0,BufferBLE.length());
    }
}
