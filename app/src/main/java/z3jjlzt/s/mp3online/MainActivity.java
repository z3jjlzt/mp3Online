package z3jjlzt.s.mp3online;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.ProgressBar;

import com.z3jjlzt.utils.MyBaseAdapter;
import com.z3jjlzt.utils.ViewHolder;

import java.util.ArrayList;
import java.util.List;

import butterknife.Bind;
import butterknife.ButterKnife;
import okhttp3.OkHttpClient;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava.RxJavaCallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import rx.android.schedulers.AndroidSchedulers;
import rx.functions.Action1;
import rx.schedulers.Schedulers;
import z3jjlzt.s.utils.Constants;
import z3jjlzt.s.utils.HttpDownloader;
import z3jjlzt.s.utils.Iapi;
import z3jjlzt.s.z3jjlzt.s.beans.MusicBean;

public class MainActivity extends AppCompatActivity {
    @Bind(R.id.lv)
    ListView lv;

    @Bind(R.id.pb)
    ProgressBar progressBar;

    private List<MusicBean> musicBeanList = null;
    private HttpDownloader httpDownloader = null;

    private MyBaseAdapter<MusicBean> adapter = null;
    private OkHttpClient okHttpClient = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);

        init();
        getJson();

    }

    private void init() {
        //初始化变量
        okHttpClient = Constants.client;
        httpDownloader = HttpDownloader.getInstance();
        musicBeanList = new ArrayList<>();
        adapter = new MyBaseAdapter<MusicBean>(MainActivity.this, musicBeanList, R.layout.item) {
            @Override
            public void convert(ViewHolder viewHolder, MusicBean s, int i) {
                viewHolder.setText(R.id.tv_item, s.getName());
            }
        };
        //事件绑定
        lv.setAdapter(adapter);
        lv.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, final View view, final int position, long id) {
                httpDownloader.downFile(MainActivity.this, musicBeanList, position, progressBar);
            }
        });
    }


    private void getJson() {
        Retrofit retrofit = new Retrofit.Builder().baseUrl(Constants.HTTP_PATH)
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(RxJavaCallAdapterFactory.create())
                .client(okHttpClient)
                .build();
        Iapi iapi = retrofit.create(Iapi.class);

        Action1<List<MusicBean>> action1 = new Action1<List<MusicBean>>() {
            @Override
            public void call(List<MusicBean> list) {
                musicBeanList.addAll(list);
                adapter.notifyDataSetChanged();
            }
        };

        //rx开始
        iapi.getJson()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(action1);

    }

}
