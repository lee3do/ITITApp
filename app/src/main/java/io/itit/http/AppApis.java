package io.itit.http;

import io.itit.domain.Item;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Url;
import rx.Observable;

public interface AppApis {
    @GET("srv/feed/random/user/")
    Observable<Item> getRandom();

    @GET("srv/feed/rank/user/{index}")
    Observable<Item> getRecommends(@Path("index") int index);

    @GET("srv/feed/new/user/{index}")
    Observable<Item> getNews(@Path("index") int index);

    @GET("srv/feed/like/{index}")
    Observable<Item> getLikes(@Path("index") int index);

    @GET()
    Call<ResponseBody> httpGet(@Url String url);
}