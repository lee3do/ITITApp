package io.itit.ui;

import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.DisplayImageOptions;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.display.FadeInBitmapDisplayer;
import com.nostra13.universalimageloader.core.listener.ImageLoadingListener;
import com.nostra13.universalimageloader.core.listener.SimpleImageLoadingListener;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collections;
import java.util.Date;
import java.util.LinkedList;
import java.util.List;

import cn.trinea.android.common.util.StringUtils;
import io.itit.ITITApplication;
import io.itit.R;
import io.itit.WrapperActivity;
import io.itit.domain.Item;
import io.itit.http.HttpUtils;

import static io.itit.http.HttpUtils.iconUrl;


public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.CardViewHolder> {
    private List<Item.ItemsBean> newsList;
    Context mContext;

    private ImageLoader imageLoader = ImageLoader.getInstance();
    private DisplayImageOptions options = new DisplayImageOptions.Builder().showImageOnLoading(R
            .drawable.noimage).showImageOnFail(R.drawable.noimage).showImageForEmptyUri(R
            .drawable.lks_for_blank_url).cacheInMemory(true).cacheOnDisk(true).considerExifParams
            (true).build();
    private ImageLoadingListener animateFirstListener = new AnimateFirstDisplayListener();

    public NewsAdapter(List<Item.ItemsBean> newsList, Context context) {
        this.newsList = newsList;
        this.mContext = context;
        setHasStableIds(true);
    }

    public void setNewsList(List<Item.ItemsBean> newsList, boolean reCreate) {
        if (reCreate) {
            this.newsList = newsList;
        } else {
            this.newsList.addAll(newsList);
        }
    }

    public void updateNewsList(List<Item.ItemsBean> newsList, boolean reCreate) {
        setNewsList(newsList, reCreate);
        notifyDataSetChanged();
    }

    @Override
    public CardViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        final Context context = parent.getContext();

        View itemView = LayoutInflater.from(context).inflate(R.layout.news_list_item, parent,
                false);

        return new CardViewHolder(itemView, new CardViewHolder.ClickResponseListener() {
            @Override
            public void onWholeClick(int position) {
                browse(context, position);
            }

            @Override
            public void onOverflowClick(View v, int position) {

            }
        });
    }

    @Override
    public void onBindViewHolder(CardViewHolder holder, int position) {
        Item.ItemsBean item = newsList.get(position);
        imageLoader.displayImage(iconUrl + item.getProviderId(), holder.newsImage, options,
                animateFirstListener);

        holder.title.setText(item.getTitle());
        DateFormat df = SimpleDateFormat.getDateInstance();
        holder.time.setText(df.format(new Date(item.getFetchDate())));
        holder.authorText.setText(item.getAuthor());
        holder.readNumText.setText(item.getViewCount() + "");
        if (StringUtils.isEmpty(item.getDesc())) {
            holder.content.setVisibility(View.GONE);
        } else {
            holder.content.setVisibility(View.VISIBLE);
            holder.content.setText(item.getDesc());
        }
    }

    @Override
    public int getItemCount() {
        return newsList == null ? 0 : newsList.size();
    }

    @Override
    public long getItemId(int position) {
        return newsList.get(position).hashCode();
    }

    private void browse(Context context, int position) {
        Item.ItemsBean item = newsList.get(position);
        Intent intent = new Intent(mContext, WrapperActivity.class);
        intent.putExtra("URL", HttpUtils.baseUrl + item.getId());
        intent.putExtra("TITLE", item.getTitle());
        intent.putExtra("ID",item.getId());
        ITITApplication.displayedItem = item;
        context.startActivity(intent);
    }

    public static class CardViewHolder extends RecyclerView.ViewHolder implements View
            .OnClickListener {
        public ImageView newsImage;
        public TextView title;
        public TextView time;
        public TextView content;
        public ImageView overflow;
        public TextView authorText;
        public TextView readNumText;

        private ClickResponseListener mClickResponseListener;

        public CardViewHolder(View v, ClickResponseListener clickResponseListener) {
            super(v);

            this.mClickResponseListener = clickResponseListener;

            newsImage = (ImageView) v.findViewById(R.id.thumbnail_image);
            title = (TextView) v.findViewById(R.id.title);
            content = (TextView) v.findViewById(R.id.content);
            overflow = (ImageView) v.findViewById(R.id.card_share_overflow);
            authorText = (TextView) v.findViewById(R.id.author);
            readNumText = (TextView) v.findViewById(R.id.read_num);
            time = (TextView) v.findViewById(R.id.time);

            v.setOnClickListener(this);
            authorText.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (v == authorText) {
                mClickResponseListener.onOverflowClick(v, getAdapterPosition());
            } else {
                mClickResponseListener.onWholeClick(getAdapterPosition());
            }
        }

        public interface ClickResponseListener {
            void onWholeClick(int position);

            void onOverflowClick(View v, int position);
        }
    }

    private static class AnimateFirstDisplayListener extends SimpleImageLoadingListener {
        static final List<String> displayedImages = Collections.synchronizedList(new LinkedList<>
                ());

        @Override
        public void onLoadingComplete(String imageUri, View view, Bitmap loadedImage) {
            if (loadedImage != null) {
                ImageView imageView = (ImageView) view;
                boolean firstDisplay = !displayedImages.contains(imageUri);
                if (firstDisplay) {
                    FadeInBitmapDisplayer.animate(imageView, 500);
                    displayedImages.add(imageUri);
                }
            }
        }
    }
}
