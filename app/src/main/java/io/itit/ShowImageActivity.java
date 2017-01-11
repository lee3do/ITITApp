package io.itit;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.assist.ImageScaleType;

import java.util.ArrayList;

import io.itit.widget.TouchImageView;

public class ShowImageActivity extends FragmentActivity {

    ArrayList<TouchImageView> images = new ArrayList<>();
    String[] imagesUrls;
    ViewPager pager;
    DisplayImageOptions mOptions;
    int index = 0;

    @Override
    protected void onNewIntent(Intent intent) {

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_image);
        pager = (ViewPager) findViewById(R.id.viewpager);
        imagesUrls = getIntent().getExtras().getStringArray("URL");
        index = getIntent().getIntExtra("INDEX",0);
        mOptions = new DisplayImageOptions.Builder().showImageOnLoading(R.drawable.loading)
                .showImageForEmptyUri(R.drawable.loading).showImageOnFail(R.drawable.loading)
                .imageScaleType(ImageScaleType.EXACTLY).considerExifParams(true).cacheOnDisk
                        (true).cacheInMemory(true).build();
        initView();
    }


    private void initView() {
        for (int i = 0; i < imagesUrls.length; i++) {
            images.add(new TouchImageView(getApplicationContext()));
        }
        pager.setAdapter(new MyPagerAdapter());
        pager.setCurrentItem(index);
    }

    class MyPagerAdapter extends PagerAdapter {

        @Override
        public boolean isViewFromObject(View arg0, Object arg1) {

            return arg0 == arg1;
        }

        @Override
        public int getCount() {
            return images.size();
        }

        @Override
        public void destroyItem(ViewGroup container, int position, Object object) {
            container.removeView(images.get(position));

        }

        @Override
        public int getItemPosition(Object object) {

            return super.getItemPosition(object);
        }


        @Override
        public Object instantiateItem(ViewGroup container, int position) {
            ImageView imageView = images.get(position);
            container.addView(imageView);
            ImageLoader.getInstance().displayImage(imagesUrls[position], imageView, mOptions);
            //imageView.setOnClickListener(l -> finish());
            return images.get(position);
        }

    }

}

