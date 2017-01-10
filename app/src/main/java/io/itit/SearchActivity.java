package io.itit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;

import butterknife.Bind;
import butterknife.ButterKnife;
import io.itit.widget.ItitSearchView;

public class SearchActivity extends AppCompatActivity {

    @Bind(R.id.search)
    ItitSearchView searchView;
    @Bind(R.id.content)
    LinearLayout content;
    @Bind(R.id.activity_search)
    RelativeLayout activitySearch;
    @Bind(R.id.toolbar)
    Toolbar toolbar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        ButterKnife.bind(this);

        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


        MainActivityFragment searchFragment = new MainActivityFragment();
        Bundle bundle = new Bundle();
        bundle.putInt("POS", 100);
        searchFragment.setArguments(bundle);
        getSupportFragmentManager().beginTransaction().replace(R.id.content, searchFragment)
                .commit();

        searchView.setQueryHint("搜索文章");
        searchView.setOnQueryTextListener(query -> {
            searchView.clearFocus();
            searchFragment.setSearcheArgs(query);
            return true;
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
