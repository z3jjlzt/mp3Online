package z3jjlzt.s.services;

import android.app.Service;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Handler;
import android.os.IBinder;
import android.os.Message;

import java.util.List;

import z3jjlzt.s.utils.Constants;

/**
 * Created by s on 2016/3/8.
 */
public class MusicPlayer extends Service {
    private int CMD;
    private static final String ACTION_UPDATE="z3jjlzt.s.services.UPDATE";

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        CMD=intent.getIntExtra("CMD",-1);
    switch (CMD){
        case Constants.CMD_INIT:

            break;
        case Constants.CMD_PLAY:
            break;
        case Constants.CMD_NEXT:
            break;
        case Constants.CMD_STOP:
            break;
    }
        return super.onStartCommand(intent, flags, startId);
    }
    class MusicUtil {
        private List<String> musiclist=null;
        private MediaPlayer mediaPlayer=null;
        private Context context;
        private int  index;
        private boolean isPlaying=false;
        public MusicUtil(List<String> musiclist, Context context){
            this.context=context;
            this.musiclist=musiclist;
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {

                }
            });
        }
        Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Intent intent = new Intent();
                intent.setAction(MusicPlayer.ACTION_UPDATE);
                context.sendBroadcast(intent);
                handler.sendEmptyMessageDelayed(1,500);
            }
        };
        public void init(){

        }
        public  void play(){
            mediaPlayer.start();
            isPlaying=true;
        }
        public  void next(){

        }

    }
}
