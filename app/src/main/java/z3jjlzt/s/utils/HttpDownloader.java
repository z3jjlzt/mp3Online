package z3jjlzt.s.utils;

/**
 * Created by s on 2016/3/6.
 */

import android.content.Context;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import rx.Observable;
import rx.Subscriber;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action0;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import z3jjlzt.s.z3jjlzt.s.beans.MusicBean;

public class HttpDownloader {

    private static HttpDownloader httpDownloader = null;

    private HttpDownloader() {

    }

    /**
     * @return 单例模式
     */
    public static HttpDownloader getInstance() {
        if (httpDownloader == null) {
            synchronized (HttpDownloader.class) {
                if (httpDownloader == null)
                    httpDownloader = new HttpDownloader();
            }
        }
        return httpDownloader;
    }

    public void downFile(final Context context, final List<MusicBean> musicBeanList, final int position, final ProgressBar progressBar) {
        final String subpath = musicBeanList.get(position).getLocation();
        final File fileName = new File(Constants.SD_PATH, musicBeanList.get(position).getName() + ".mp3");
        if (!fileName.exists()) {

            //创建被观测者
            Observable<Integer> observable = Observable.create(new Observable.OnSubscribe<Integer>() {
                @Override
                public void call(Subscriber<? super Integer> subscriber) {
                    httpDownloader.okRequest(Constants.HTTP_PATH + subpath, fileName, subscriber);
                }
            });
            //初始化进度条
            Action0 initPb = new Action0() {
                @Override
                public void call() {
                    progressBar.setVisibility(View.VISIBLE);
                }
            };
            //更新进度条
            Action1<Integer> updatePb = new Action1<Integer>() {
                @Override
                public void call(Integer integer) {
                    progressBar.setProgress(integer);
                    Log.e("sb", integer + "%");
                }
            };
            //错误处理
            Action1<Throwable> onError = new Action1<Throwable>() {
                @Override
                public void call(Throwable throwable) {
                    Toast.makeText(context, "error", Toast.LENGTH_SHORT).show();
                }
            };
            //下载完成
            Action0 onCompleted = new Action0() {
                @Override
                public void call() {
                    progressBar.setVisibility(View.GONE);
                }
            };
            observable.subscribeOn(Schedulers.io())
                    .doOnSubscribe(initPb)//指定发生在下一个subscribeOn所在的线程
                    .subscribeOn(AndroidSchedulers.mainThread())
                    .observeOn(AndroidSchedulers.mainThread())
                    .subscribe(updatePb, onError, onCompleted);
        } else {
            Toast.makeText(context, "文件存在！！", Toast.LENGTH_SHORT).show();
        }
    }

    public void okRequest(String urlStr, final File file, final Subscriber<? super Integer> subscriber) {
        Request request = new Request.Builder().url(urlStr).build();
        OkHttpClient client = Constants.client;
        client.newCall(request).enqueue(new Callback() {//enqueue 异步加载
            @Override
            public void onFailure(Call call, IOException e) {

            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                int len;
                byte[] buf = new byte[2048];
                InputStream inputStream = response.body().byteStream();
                //可以在这里自定义路径
                FileOutputStream fileOutputStream = new FileOutputStream(file);
                long total = response.body().contentLength();
                int finished = 0;
                Log.e("sb", total + "");
                long time = System.currentTimeMillis();
                while ((len = inputStream.read(buf)) != -1) {
                    fileOutputStream.write(buf, 0, len);
                    finished += len;
                    if (System.currentTimeMillis() - time > 1000 || finished == total) {//每隔一秒发送
                        int progress = (int) (finished * 100.0f / total);
                        subscriber.onNext(progress);
                        time = System.currentTimeMillis();
                    }
                }
                subscriber.onCompleted();
                fileOutputStream.flush();
                fileOutputStream.close();
                inputStream.close();

            }
        });
    }
}