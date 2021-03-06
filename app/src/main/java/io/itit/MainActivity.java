package io.itit;

import android.animation.Animator;
import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.AppBarLayout;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import com.afollestad.materialdialogs.MaterialDialog;
import com.afollestad.materialdialogs.Theme;
import com.mikepenz.materialdrawer.AccountHeader;
import com.mikepenz.materialdrawer.AccountHeaderBuilder;
import com.mikepenz.materialdrawer.Drawer;
import com.mikepenz.materialdrawer.DrawerBuilder;
import com.mikepenz.materialdrawer.model.DividerDrawerItem;
import com.mikepenz.materialdrawer.model.PrimaryDrawerItem;
import com.mikepenz.materialdrawer.model.ProfileDrawerItem;
import com.mikepenz.materialdrawer.model.SecondaryDrawerItem;
import com.mikepenz.materialdrawer.model.interfaces.IProfile;
import com.nostra13.universalimageloader.core.ImageLoader;
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
import de.hdodenhof.circleimageview.CircleImageView;
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
    @Bind(R.id.search)
    ImageView search;
    @Bind(R.id.profile_image)
    CircleImageView profileImage;
    Drawer drawer;
    @Bind(R.id.big_profile_image)
    CircleImageView bigProfileImage;
    @Bind(R.id.show_more)
    ImageView showMore;
    @Bind(R.id.search_bar)
    RelativeLayout searchBar;
    @Bind(R.id.appbar)
    AppBarLayout appbar;

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
        profileImage.setOnClickListener(v -> profileClick());
        showMore.setOnClickListener(v -> {
            if (drawer.isDrawerOpen()) {
                drawer.closeDrawer();
            } else {
                drawer.openDrawer();
            }
        });
        searchBar.setOnClickListener(v -> {
            startActivity(new Intent(MainActivity.this, SearchActivity.class));
        });
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

    private void initDrawer(Bundle savedInstanceState) {
        profile = new ProfileDrawerItem().withEmail("ITIT");
        Data user = DBHelper.getDataByKey("NAME");

        if (user != null) {
            profile.withName(user.getValue());
            Data headUrl = DBHelper.getDataByKey("HEAD");
            if (headUrl != null && !StringUtils.isEmpty(headUrl.getValue())) {
                profile.withIcon(Uri.parse(headUrl.getValue()));
                ImageLoader.getInstance().displayImage(headUrl.getValue(), profileImage);
            } else {
                profile.withIcon(R.drawable.boy);
            }
            hasLogin = true;
        } else {
            ((ProfileDrawerItem) profile).withName("游客").withIcon(R.drawable.boy);
        }

        header = new AccountHeaderBuilder().withSelectionListEnabledForSingleProfile(false)
                .withActivity(this).addProfiles(profile).withProfileImagesClickable(true)
                .withProfileImagesVisible(true).withSavedInstance(savedInstanceState)
                .withOnAccountHeaderProfileImageListener(new AccountHeader
                        .OnAccountHeaderProfileImageListener() {
            @Override
            public boolean onProfileImageClick(View view, IProfile profile, boolean current) {
                profileClick();
                return true;
            }

            @Override
            public boolean onProfileImageLongClick(View view, IProfile profile, boolean current) {
                return false;
            }
        }).withTextColor(Color.BLACK).withCompactStyle(true).build();

        PrimaryDrawerItem item1 = new PrimaryDrawerItem().withName("推荐").withIcon(R.drawable
                .tuijian).withIdentifier(0);

        PrimaryDrawerItem item2 = new PrimaryDrawerItem().withName("最新").withIcon(R.drawable
                .zuixin).withIdentifier(1);
        PrimaryDrawerItem item3 = new PrimaryDrawerItem().withName("收藏").withIcon(R.drawable
                .shoucang).withIdentifier(2);

        PrimaryDrawerItem item4 = new PrimaryDrawerItem().withName("关于").withIcon(R.drawable
                .guanyu).withIdentifier(3).withSelectable(false);

        PrimaryDrawerItem item5 = new PrimaryDrawerItem().withName("退出登录").withIcon(R.drawable
                .exit).withIdentifier(4).withSelectable(false);

        PrimaryDrawerItem item6 = new PrimaryDrawerItem().withName("清除缓存").withIcon(R.drawable
                .clear).withIdentifier(5).withSelectable(false);


        drawer = new DrawerBuilder().withActionBarDrawerToggle(false).withActivity(this)
                .withAccountHeader(header).addDrawerItems(new SecondaryDrawerItem().withName
                        ("资讯"), item1, item2, new DividerDrawerItem(), new SecondaryDrawerItem()
                        .withName("个人"), item3, new DividerDrawerItem(), new SecondaryDrawerItem
                        ().withName("系统"), item6, item4, item5).build();
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
                    startActivity(new Intent(MainActivity.this, AboutActivity.class));
                    break;
                case 4:
                    new MaterialDialog.Builder(MainActivity.this).theme(Theme.LIGHT).title
                            ("确定登出吗?").positiveText("确定").negativeText("取消").onPositive((dialog,
                                                                                         which) -> {
                        DBHelper.deleteKey("USER");
                        DBHelper.deleteKey("NAME");
                        DBHelper.deleteKey("HEAD");
                        uuid = UUID.randomUUID().toString();
                        ((ProfileDrawerItem) profile).withName("游客").withIcon(R.drawable.boy);
                        runOnUiThread(() -> header.updateProfile(profile));
                        hasLogin = false;

                        profileImage.setImageResource(R.drawable.boy);
                        dialog.dismiss();
                    }).onNegative((dialog, which) -> dialog.dismiss()).show();
                    break;
                case 5:
                    new MaterialDialog.Builder(MainActivity.this).theme(Theme.LIGHT).title
                            ("确定清除缓存吗?").positiveText("确定").negativeText("取消").onPositive(
                                    (dialog, which) -> {
                        ImageLoader.getInstance().clearDiskCache();
                        ImageLoader.getInstance().clearMemoryCache();
                        Utils.clearWebViewCache(getApplicationContext());
                        ToastUtils.show(getApplicationContext(), "缓存清除成功！");
                        dialog.dismiss();
                    }).onNegative((dialog, which) -> dialog.dismiss()).show();
                    break;
                default:
                    break;
            }
            return true;
        });
    }

    private void profileClick() {
        if (!hasLogin) {
            drawer.closeDrawer();
            weixinLoginDialog = Utils.generateWaitingDialog("微信登录中,请稍候", MainActivity.this);
            weixinLoginDialog.setCanceledOnTouchOutside(true);
            weixinLoginDialog.setCancelable(true);
            SendAuth.Req req = new SendAuth.Req();
            req.scope = "snsapi_userinfo";
            req.state = "ITIT";
            IWXAPI api = ITITApplication.msgApi;
            api.sendReq(req);
        } else {
            ToastUtils.show(this, "陪君醉笑三千场，不诉离殇");
        }
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
                    runOnUiThread(() -> {
                        setAnimation(headUrl);
                    });

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

    private void setAnimation(Data headUrl) {
        bigProfileImage.bringToFront();
        bigProfileImage.requestLayout();
        bigProfileImage.setVisibility(View.VISIBLE);
        ImageLoader.getInstance().displayImage(headUrl.getValue(), bigProfileImage);

        float toX = profileImage.getX();
        float fromX = bigProfileImage.getX();
        float toY = profileImage.getY();
        float fromY = bigProfileImage.getY();

        ObjectAnimator animatorX = ObjectAnimator.ofFloat(bigProfileImage, "translationX", 0, toX
                - fromX - 180);

        ObjectAnimator animatorY = ObjectAnimator.ofFloat(bigProfileImage, "translationY", 0, toY
                - fromY - 50);

        ObjectAnimator scaleX = ObjectAnimator.ofFloat(bigProfileImage, "scaleX", 1f, 0.3f);
        ObjectAnimator scaleY = ObjectAnimator.ofFloat(bigProfileImage, "scaleY", 1f, 0.3f);


        AnimatorSet set3 = new AnimatorSet();
        set3.play(animatorY).with(animatorX).with(scaleX).with(scaleY);
        set3.setDuration(2000);
        set3.start();

        set3.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                bigProfileImage.setVisibility(View.GONE);
                bigProfileImage.setX(fromX);
                bigProfileImage.setY(fromY);
                ImageLoader.getInstance().displayImage(headUrl.getValue(), profileImage);
                ObjectAnimator animator = ObjectAnimator.ofFloat(profileImage, "translationX", 0,
                        -20, 0, 20, 0);
                animator.setDuration(1000);
                animator.start();
            }

            @Override
            public void onAnimationCancel(Animator animation) {
                bigProfileImage.setVisibility(View.GONE);
                ImageLoader.getInstance().displayImage(headUrl.getValue(), profileImage);
            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
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
