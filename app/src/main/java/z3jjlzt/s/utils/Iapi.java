package z3jjlzt.s.utils;

import java.util.List;

import okhttp3.Response;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import rx.Observable;
import z3jjlzt.s.z3jjlzt.s.beans.MusicBean;

/**
 * Created by s on 2016/3/10.
 */
public interface Iapi {
    @GET("music.json")
    Call<List<MusicBean>> geturl();
    @GET("music.json")
    Observable<List<MusicBean>> getJson();
    @GET("/{name}")
    Observable<Response> down_File(@Path("name")String name);
}
