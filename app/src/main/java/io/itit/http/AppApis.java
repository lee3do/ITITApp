package io.itit.http;

import io.itit.domain.Empty;
import io.itit.domain.Fav;
import io.itit.domain.Item;
import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.POST;
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

    @GET("srv/feed/like/{uuid}/{index}")
    Observable<Item> getLikes(@Path("uuid") String uuid, @Path("index") int index);

    @POST("srv/feed/device/{id}")
    Call<ResponseBody> register(@Path("id") String id);

    /*
    public static final String IS_LIKE_URL = "http://itit.io:7001/srv/feed/islikeit/";
	public static final String LIKE_IT_URL = "http://itit.io:7001/srv/feed/likeit/";
	public static final String UNLIKE_IT_URL="http://itit.io:7001/srv/feed/unlikeit/";
	public static final String SEARCH_URL = "http://itit.io:7001/srv/feed/search/user/";
     */

    @GET("srv/feed/likeit/{uuid}/{id}")
    Observable<Empty> fav(@Path("uuid") String uuid, @Path("id") int id);

    @GET("srv/feed/unlikeit/{uuid}/{id}")
    Observable<Empty> unFav(@Path("uuid") String uuid,@Path("id") int id);


    @GET("srv/feed/islikeit/{uuid}/{id}")
    Observable<Fav> isFav(@Path("uuid") String uuid, @Path("id") int id);


    @GET()
    Call<ResponseBody> httpGet(@Url String url);
}