package sjtu.iiot.posiiot_sensortest.FilePro;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;


import android.content.Context;
import android.os.Environment;


/**
 * Created by sunjiachen on 16/8/14.
 */
public class FileWrite{


    private static FileWrite INSTANCE = null;
    private static String File_PATH;
    private FileDumper mFileDumper = null;
    private StringBuffer Message = new StringBuffer();
    private String Filename = null;
    /**
     *
     * 初始化目录
     *
     * */
    public void init(Context context) {
        if (Environment.getExternalStorageState().equals(
                Environment.MEDIA_MOUNTED)) {// 优先保存到SD卡中
            File_PATH = Environment.getExternalStorageDirectory()
                    .getAbsolutePath() + File.separator + "sjtu";
        } else {// 如果SD卡不存在，就保存到本应用的目录下
            File_PATH = context.getFilesDir().getAbsolutePath()
                    + File.separator + "sjtu";
        }
        File file = new File(File_PATH);
        if (!file.exists()) {
            file.mkdirs();
        }
    }

    public static FileWrite getInstance(Context context,StringBuffer message, String filename) {
            INSTANCE = new FileWrite(context,message,filename);
        return INSTANCE;
    }

    private FileWrite(Context context,StringBuffer message,String filename) {
        init(context);
        Message = message;
        Filename = filename;
    }

    public void start() {
            mFileDumper = new FileDumper(Message, File_PATH, Filename);
            mFileDumper.run();
    }

    private class FileDumper {

        private FileOutputStream out = null;
        private StringBuffer message = new StringBuffer();
        public FileDumper(StringBuffer Message, String dir,String filename) {
            try {
                out = new FileOutputStream(new File(dir, filename
                        + MyDate.getDateEN() + ".txt"));
            } catch (FileNotFoundException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }
            message = Message;
        }

        public void run() {
            try {
                byte[] bytes = message.toString().getBytes();
                out.write(bytes);
                out.close();
            }
            catch (Exception e) {
                e.printStackTrace();
            }

        }

    }

}