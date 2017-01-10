package io.itit;

import android.app.SearchManager;
import android.app.SearchableInfo;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.SearchView;

import butterknife.Bind;
import butterknife.ButterKnife;

public class SearchActivity extends AppCompatActivity {

    @Bind(R.id.search)
    SearchView searchView;
    @Bind(R.id.content)
    LinearLayout content;
    @Bind(R.id.activity_search)
    RelativeLayout activitySearch;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        MainActivityFragment searchFragment = new MainActivityFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("POS", 100);
        searchFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.content, searchFragment)
                .commit();

        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchableInfo info = searchManager.getSearchableInfo(getComponentName());
        searchView.setSearchableInfo(info);

        searchView.setIconified(true);
        searchView.setIconifiedByDefault(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                searchFragment.setSearcheArgs(query);
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                return true;
            }
        });
    }
}
