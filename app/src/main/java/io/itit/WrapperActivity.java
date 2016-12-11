package io.itit;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.webkit.WebSettings;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.Toast;

import com.orhanobut.logger.Logger;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.shareboard.ShareBoardConfig;

import butterknife.Bind;
import butterknife.ButterKnife;
import in.srain.cube.views.ptr.PtrClassicFrameLayout;
import in.srain.cube.views.ptr.PtrDefaultHandler;
import in.srain.cube.views.ptr.PtrFrameLayout;
import in.srain.cube.views.ptr.PtrHandler;
import in.srain.cube.views.ptr.header.StoreHouseHeader;
import io.itit.http.HttpUtils;
import me.imid.swipebacklayout.lib.app.SwipeBackActivity;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static io.itit.http.HttpUtils.iconUrl;

public class WrapperActivity extends SwipeBackActivity {
    String url = "";
    String urlFirst = "";
    String title = "";
    int id;
    boolean isLiked = false;
    boolean isReadOri = false;
    MenuItem favMenu;
    MenuItem readOriMenu;

    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.webView)
    WebView webView;
    @Bind(R.id.rotate_header_web_view_frame)
    PtrClassicFrameLayout rotateHeaderWebViewFrame;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_wrapper);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        if (getIntent() != null) {
            url = getIntent().getStringExtra("URL");
            urlFirst = url;
            title = getIntent().getStringExtra("TITLE");
            id = getIntent().getIntExtra("ID", 0);
        }
        initWebview();
        initPullToRefresh();
        webView.loadUrl(url);
        toolbar.setTitle(title);

        HttpUtils.appApis.isFav(ITITApplication.uuid, id).subscribeOn(Schedulers.io()).observeOn
                (AndroidSchedulers.mainThread()).subscribe(info -> {
            isLiked = info.isIsLike();
            runOnUiThread(()->{
                Logger.d("this page is liked?"+isLiked);
                if (isLiked) {
                    favMenu.setIcon(R.drawable.ic_like);
                } else {
                    favMenu.setIcon(R.drawable.ic_unlike);
                }
            });
        }, error -> {
            Logger.e(error.getLocalizedMessage());
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case R.id.action_fav:
                if (isLiked) {
                    HttpUtils.appApis.unFav(ITITApplication.uuid, id).subscribeOn(Schedulers.io()
                    ).observeOn(AndroidSchedulers.mainThread()).subscribe(info -> {
                        favMenu.setIcon(R.drawable.ic_unlike);
                        isLiked = !isLiked;
                    }, error -> {
                        Logger.e(error.toString());
                    });

                } else {
                    HttpUtils.appApis.fav(ITITApplication.uuid, id).subscribeOn(Schedulers.io())
                            .observeOn(AndroidSchedulers.mainThread()).subscribe(info -> {
                        favMenu.setIcon(R.drawable.ic_like);
                        isLiked = !isLiked;
                    }, error -> {
                        Logger.e(error.toString());
                    });
                }
                return true;
            case R.id.action_share:
                share(title, ITITApplication.displayedItem.getDesc(), url, iconUrl +
                        ITITApplication.displayedItem.getProviderId());
                return true;
            case R.id.read_orign:
                isReadOri = !isReadOri;
                if (isReadOri) {
                    webView.loadUrl(ITITApplication.displayedItem.getUrl());
                    readOriMenu.setIcon(R.drawable.read_empty);
                } else {
                    webView.loadUrl(urlFirst);
                    readOriMenu.setIcon(R.drawable.read_full);
                }
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);

    }

    public void share(String title, String text, String url, String imageUrl) {
        final SHARE_MEDIA[] displaylist = new SHARE_MEDIA[]{SHARE_MEDIA.WEIXIN, SHARE_MEDIA
                .WEIXIN_CIRCLE};
        ShareBoardConfig config = new ShareBoardConfig();
        config.setShareboardPostion(ShareBoardConfig.SHAREBOARD_POSITION_CENTER);
        config.setMenuItemBackgroundShape(ShareBoardConfig.BG_SHAPE_CIRCULAR);
        config.setCancelButtonVisibility(true);

        UMImage image = new UMImage(this, imageUrl);
        new ShareAction(this).setDisplayList(displaylist).withText(text).withTargetUrl(url)
                .withMedia(image).withTitle(title).setListenerList(new UMShareListener() {
            @Override
            public void onResult(SHARE_MEDIA platform) {
                Toast.makeText(getApplicationContext(), platform + " 分享成功啦", Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onError(SHARE_MEDIA platform, Throwable t) {
                Toast.makeText(getApplicationContext(), platform + " 分享失败啦", Toast.LENGTH_SHORT)
                        .show();
            }

            @Override
            public void onCancel(SHARE_MEDIA platform) {
                Toast.makeText(getApplicationContext(), platform + " 分享取消了", Toast.LENGTH_SHORT)
                        .show();
            }
        }).open(config);
    }

    private void initPullToRefresh() {
        StoreHouseHeader header = new StoreHouseHeader(this);
        header.initWithString("ITIT");
        rotateHeaderWebViewFrame.setHeaderView(header);
        rotateHeaderWebViewFrame.addPtrUIHandler(header);

        rotateHeaderWebViewFrame.disableWhenHorizontalMove(true);

        rotateHeaderWebViewFrame.setPtrHandler(new PtrHandler() {
            @Override
            public boolean checkCanDoRefresh(PtrFrameLayout frame, View content, View header) {
                boolean canRefresh = PtrDefaultHandler.checkContentCanBePulledDown(frame,
                        content, header);
                return canRefresh;
            }

            @Override
            public void onRefreshBegin(PtrFrameLayout frame) {
                webView.loadUrl(url);
                rotateHeaderWebViewFrame.postDelayed(() -> rotateHeaderWebViewFrame
                        .refreshComplete(), 10000);//默认10s关闭
            }
        });
    }

    private void initWebview() {
        if (webView == null) return;
        WebSettings ws = webView.getSettings();
        ws.setJavaScriptEnabled(true);
        // 设置 缓存模式
        ws.setAppCacheEnabled(true);
        String appCacheDir = webView.getContext().getDir("cache", Context.MODE_PRIVATE).getPath();
        ws.setAppCachePath(appCacheDir);
        ws.setCacheMode(WebSettings.LOAD_DEFAULT);
        ws.setRenderPriority(WebSettings.RenderPriority.HIGH);
        ws.setLoadsImagesAutomatically(true);
        ws.setSavePassword(false);
        ws.setSaveFormData(false);

        ws.setAllowFileAccess(true);
        // 开启 DOM storage API 功能
        ws.setDomStorageEnabled(true);
        ws.setDatabaseEnabled(true);
        String ua = ws.getUserAgentString();
        ws.setUserAgentString(ua + "itit");
        ws.setJavaScriptCanOpenWindowsAutomatically(true);
        webView.setWebViewClient(client);
    }

    WebViewClient client = new WebViewClient() {
        public boolean shouldOverrideUrlLoading(WebView view, String url) {
            if (url.startsWith("mailto:") || url.startsWith("tel:")) {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
                startActivity(intent);
                return true;
            }
            view.loadUrl(url);
            return true;
        }

        @Override
        public void onPageFinished(WebView view, String url) {
            super.onPageFinished(view, url);
            WrapperActivity.this.url = url;
            if (rotateHeaderWebViewFrame != null) {
                rotateHeaderWebViewFrame.refreshComplete();
            }
        }

        @Override
        public void onReceivedError(WebView view, int errorCode, String description, String
                failingUrl) {
            super.onReceivedError(view, errorCode, description, failingUrl);
            view.loadUrl("file:///android_asset/webroot/error.html");
        }

        @Override
        public void onPageStarted(WebView view, String url, Bitmap favicon) {
            super.onPageStarted(view, url, favicon);
        }
    };

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.content_menu, menu);
        favMenu = menu.getItem(0);
        readOriMenu = menu.getItem(1);
        if (isLiked) {
            favMenu.setIcon(R.drawable.ic_like);
        } else {
            favMenu.setIcon(R.drawable.ic_unlike);
        }
        return true;
    }


    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
    }

}
