package io.itit;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.View;
import android.widget.LinearLayout;

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

import butterknife.Bind;
import butterknife.ButterKnife;
import cn.trinea.android.common.util.ToastUtils;
import io.itit.http.HttpUtils;
import rx.android.schedulers.AndroidSchedulers;
import rx.schedulers.Schedulers;


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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        fab.setOnClickListener(view -> {
            HttpUtils.appApis.getRandom().subscribeOn(Schedulers.io()).observeOn
                    (AndroidSchedulers.mainThread()).subscribe(info -> {
                if (info.getItems().size() == 0) {
                    ToastUtils.show(getApplicationContext(), "获取文章失败!");
                } else {
                    Intent intent = new Intent(getApplicationContext(), WrapperActivity.class);
                    intent.putExtra("URL", HttpUtils.baseUrl + info.getItems().get(0).getId());
                    intent.putExtra("TITLE", info.getItems().get(0).getTitle());
                    intent.putExtra("ID",info.getItems().get(0).getId());
                    ITITApplication.displayedItem = info.getItems().get(0);
                    startActivity(intent);
                }
            }, error -> {
                Logger.e(error.getLocalizedMessage());
                ToastUtils.show(getApplicationContext(), "获取文章失败!");
            });
        });

        initDrawer(savedInstanceState);
        initFragments();

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
        SearchView searchView = (SearchView) menu.findItem(R.id.action_go_to_search).getActionView();
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

        final IProfile profile = new ProfileDrawerItem().withEmail("ITIT").withName("Lee_3do")
                .withIcon(R.drawable.ic_launcher);
        AccountHeader header = new AccountHeaderBuilder()
                .withSelectionListEnabledForSingleProfile(false).withActivity(this).addProfiles
                        (profile).withProfileImagesClickable(true).withProfileImagesVisible(true)
                .withSavedInstance(savedInstanceState).withOnAccountHeaderProfileImageListener
                        (new AccountHeader.OnAccountHeaderProfileImageListener() {
            @Override
            public boolean onProfileImageClick(View view, IProfile profile, boolean current) {
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
                        ("资讯"), item1, item2, item3, new DividerDrawerItem(), new
                        SecondaryDrawerItem().withName("生活"), item4).build();
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

}
