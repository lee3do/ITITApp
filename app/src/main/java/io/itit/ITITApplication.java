package io.itit;

import android.app.Application;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.widget.ImageView;

import com.mikepenz.materialdrawer.util.AbstractDrawerImageLoader;
import com.mikepenz.materialdrawer.util.DrawerImageLoader;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.orhanobut.logger.Logger;
import com.tencent.mm.sdk.openapi.IWXAPI;
import com.tencent.mm.sdk.openapi.WXAPIFactory;
import com.umeng.socialize.PlatformConfig;

import io.itit.db.DBHelper;
import io.itit.domain.Item;

/**
 * Created by Lee_3do on 16/7/30.
 */
public class ITITApplication extends Application {
    public static IWXAPI msgApi;
    public static Context CONTEXT;
    public static String uuid= "" ;
    public static Item.ItemsBean displayedItem;
    public static final String APP_ID = "wxd2452b98bd35e114";
    public static final String WECHAT_KEY = "778f0c68964b4869618fb0833afe30c4";

    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(context)
                .threadPriority(Thread.NORM_PRIORITY - 2).
                memoryCacheSize(2 * 1024 * 1024)//设置内存缓存的大小
                .diskCacheSize(50 * 1024 * 1024). //设置磁盘缓存大小 50M   .
                discCacheFileNameGenerator(new Md5FileNameGenerator()).tasksProcessingOrder
                        (QueueProcessingType.LIFO).writeDebugLogs().build();
        ImageLoader.getInstance().init(config);

        DrawerImageLoader.init(new AbstractDrawerImageLoader() {
            @Override
            public void set(ImageView imageView, Uri uri, Drawable placeholder) {
                imageView.setScaleType(ImageView.ScaleType.CENTER_CROP);
                DisplayImageOptions options = new DisplayImageOptions.Builder()
                        .cacheInMemory(true)
                        .cacheOnDisk(true)
                        .build();
                ImageLoader.getInstance().displayImage(uri.toString(), imageView, options);
            }
        });
    }

    @Override
    public void onCreate() {
        super.onCreate();
        Logger.init("ITIT");
        initImageLoader(getApplicationContext());
        initWechat();
        DBHelper.init(getApplicationContext());
        CONTEXT = this;
    }



    protected void initWechat() {
        msgApi = WXAPIFactory.createWXAPI(getApplicationContext(), APP_ID, true);
        // 将该app注册到微信
        msgApi.registerApp(APP_ID);

        PlatformConfig.setWeixin(APP_ID, WECHAT_KEY);
        final IWXAPI msgApi = WXAPIFactory.createWXAPI(getApplicationContext(), null);
        // 将该app注册到微信
        msgApi.registerApp(APP_ID);
    }

}
