package z3jjlzt.s.utils;

import android.os.Environment;

import okhttp3.OkHttpClient;

/**
 * Created by s on 2016/3/8.
 */
public class Constants {
    public static String HTTP_PATH="http://45.62.110.91/";
    public static String SD_PATH= Environment.getExternalStorageDirectory()+"/";

    public static OkHttpClient client = new OkHttpClient();

    public final static int  CMD_INIT=0;
    public final static int  CMD_PLAY=1;
    public final static int  CMD_NEXT=2;
    public final static int  CMD_STOP=3;



}
