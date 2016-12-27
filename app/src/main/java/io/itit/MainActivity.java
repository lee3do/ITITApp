package io.itit;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.mikepenz.google_material_typeface_library.GoogleMaterial;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.orhanobut.logger.Logger;
import com.tencent.mm.sdk.modelmsg.SendAuth;
import com.tencent.mm.sdk.openapi.IWXAPI;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.util.UUID;

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.trinea.android.common.util.StringUtils;
import cn.trinea.android.common.util.ToastUtils;
import io.itit.db.DBHelper;
import io.itit.db.Data;
import io.itit.event.WxLoginEvent;
import io.itit.http.HttpUtils;
import io.itit.ui.Utils;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;

import static io.itit.ITITApplication.uuid;


public class MainActivity extends AppCompatActivity {
    @Bind(R.id.toolbar)
    Toolbar toolbar;
    @Bind(R.id.fab)
    FloatingActionButton fab;
    @Bind(R.id.content)
    LinearLayout content;
    MainActivityFragment recommendFragment;
    MainActivityFragment newsFragment;
    MainActivityFragment favFragment;
    AccountHeader header;
    MaterialDialog weixinLoginDialog;
    IProfile profile;
    boolean hasLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
        //透明导航栏
        getWindow().addFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_NAVIGATION);
        setSupportActionBar(toolbar);
        fab.setOnClickListener(view -> HttpUtils.appApis.getRandom().subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread()).subscribe(info -> {
            if (info.getItems().size() == 0) {
                ToastUtils.show(getApplicationContext(), "获取文章失败!");
            } else {
                Intent intent = new Intent(getApplicationContext(), WrapperActivity.class);
                intent.putExtra("URL", HttpUtils.baseUrl + info.getItems().get(0).getId());
                intent.putExtra("TITLE", info.getItems().get(0).getTitle());
                intent.putExtra("ID", info.getItems().get(0).getId());
                ITITApplication.displayedItem = info.getItems().get(0);
                startActivity(intent);
            }
        }, error -> {
            Logger.e(error.getLocalizedMessage());
            ToastUtils.show(getApplicationContext(), "获取文章失败!");
        }));

        initDrawer(savedInstanceState);
        initFragments();
        EventBus.getDefault().register(this);
    }

    private void initFragments() {
        recommendFragment = new MainActivityFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("POS", 1);
        recommendFragment.setArguments(bundle);

        newsFragment = new MainActivityFragment();
        Bundle bundle2 = new Bundle();
        bundle2.putInt("POS", 2);
        newsFragment.setArguments(bundle2);

        favFragment = new MainActivityFragment();
        Bundle bundle3 = new Bundle();
        bundle3.putInt("POS", 3);
        favFragment.setArguments(bundle3);


        getSupportFragmentManager().beginTransaction().replace(R.id.content, recommendFragment)
                .commit();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.main, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.action_go_to_search)
                .getActionView();
        SearchableInfo info = searchManager.getSearchableInfo(getComponentName());
        searchView.setSearchableInfo(info);


        searchView.setIconifiedByDefault(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });

        return true;
    }

    private void initDrawer(Bundle savedInstanceState) {
        profile = new ProfileDrawerItem().withEmail("ITIT");
        Data user = DBHelper.getDataByKey("NAME");

        if (user != null) {
            profile.withName(user.getValue());
            Data headUrl = DBHelper.getDataByKey("HEAD");
            if (headUrl != null && !StringUtils.isEmpty(headUrl.getValue())) {
                profile.withIcon(Uri.parse(headUrl.getValue()));
            } else {
                profile.withIcon(R.drawable.ic_launcher);
            }
            hasLogin = true;
        } else {
            ((ProfileDrawerItem) profile).withName(uuid).withIcon(R.drawable.ic_launcher);
        }

        header = new AccountHeaderBuilder().withSelectionListEnabledForSingleProfile(false)
                .withActivity(this).addProfiles(profile).withProfileImagesClickable(true)
                .withProfileImagesVisible(true).withSavedInstance(savedInstanceState)
                .withOnAccountHeaderProfileImageListener(new AccountHeader
                        .OnAccountHeaderProfileImageListener() {
            @Override
            public boolean onProfileImageClick(View view, IProfile profile, boolean current) {
                if (!hasLogin) {
                    weixinLoginDialog = Utils.generateWaitingDialog("微信登录中,请稍候", MainActivity.this);
                    weixinLoginDialog.setCanceledOnTouchOutside(true);
                    weixinLoginDialog.setCancelable(true);
                    SendAuth.Req req = new SendAuth.Req();
                    req.scope = "snsapi_userinfo";
                    req.state = "ITIT";
                    IWXAPI api = ITITApplication.msgApi;
                    api.sendReq(req);
                } else {
                    new MaterialDialog.Builder(MainActivity.this).theme(Theme.LIGHT).title
                            ("确定登出吗?").positiveText("确定").negativeText("取消").onPositive((dialog,
                                                                                         which) -> {
                        DBHelper.deleteKey("USER");
                        DBHelper.deleteKey("NAME");
                        DBHelper.deleteKey("HEAD");
                        uuid = UUID.randomUUID().toString();
                        ((ProfileDrawerItem) profile).withName(uuid).withIcon(R.drawable
                                .ic_launcher);
                        runOnUiThread(() -> header.updateProfile(profile));
                        hasLogin = false;
                        dialog.dismiss();
                    }).onNegative((dialog, which) -> dialog.dismiss()).show();
                }

                return true;
            }

            @Override
            public boolean onProfileImageLongClick(View view, IProfile profile, boolean current) {
                return false;
            }
        }).withTextColor(Color.BLACK).withCompactStyle(true).build();

        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withName("推荐").withIcon(GoogleMaterial
                .Icon.gmd_android).withIdentifier(0);

        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withName("最新").withIcon(GoogleMaterial
                .Icon.gmd_new_releases).withIdentifier(1);
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withName("收藏").withIcon(GoogleMaterial
                .Icon.gmd_favorite).withIdentifier(2);

        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withName("天气").withIcon(GoogleMaterial
                .Icon.gmd_wb_sunny).withIdentifier(3);


        Drawer drawer = new DrawerBuilder().withActivity(this).withToolbar(toolbar)
                .withAccountHeader(header).addDrawerItems(new SecondaryDrawerItem().withName
                        ("资讯"), item1, item2, new DividerDrawerItem(), new SecondaryDrawerItem()
                        .withName("个人"), item3).build();
        drawer.setSelection(0);
        drawer.setOnDrawerItemClickListener((view, position, drawerItem) -> {
            drawer.closeDrawer();
            switch ((int) drawerItem.getIdentifier()) {
                case 0:
                    getSupportFragmentManager().beginTransaction().replace(R.id.content,
                            recommendFragment).commit();
                    break;
                case 1:
                    getSupportFragmentManager().beginTransaction().replace(R.id.content,
                            newsFragment).commit();
                    break;
                case 2:
                    getSupportFragmentManager().beginTransaction().replace(R.id.content,
                            favFragment).commit();
                    break;
                case 3:
                    break;
                default:
                    break;
            }
            return true;
        });
    }

    @Subscribe
    public void onEvent(WxLoginEvent event) {
        if (weixinLoginDialog != null) {
            weixinLoginDialog.dismiss();
        }
        if (event.isSuccess) {
            Data user = DBHelper.getDataByKey("NAME");
            if (user == null) {
            } else {
                profile.withName(user.getValue());
                Data headUrl = DBHelper.getDataByKey("HEAD");
                if (headUrl != null && !StringUtils.isEmpty(headUrl.getValue())) {
                    profile.withIcon(Uri.parse(headUrl.getValue()));
                }
            }
            runOnUiThread(() -> header.updateProfile(profile));
            hasLogin = true;
        } else {
            Logger.e("Wx login error:" + event.code);
            ToastUtils.show(getApplicationContext(), "登录失败!");
            hasLogin = false;
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onStop() {
        super.onStop();
        if (weixinLoginDialog != null) {
            weixinLoginDialog.dismiss();
        }
    }
}
