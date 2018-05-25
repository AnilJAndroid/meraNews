package com.seawindsolution.meranews.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.seawindsolution.meranews.Activities.DetailActivity;
import com.seawindsolution.meranews.Listner.OnLoadMoreListener;
import com.seawindsolution.meranews.Model.NewsModel;
import com.seawindsolution.meranews.R;

import java.util.ArrayList;
import java.util.List;

public class DataAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    private List<NewsModel> data;
    private Context context;
    private Typeface typeface,typeface_normal;
    private RequestOptions requestOptions;
    private LayoutInflater mInflater;
    private boolean withFooter;
    private String type;

    private final int FOOTER = 0;
    private final int ITEM = 1;

    private int visibleThreshold = 2;
    private int lastVisibleItem, totalItemCount;
    private boolean loading;
    private OnLoadMoreListener onLoadMoreListener;


    public DataAdapter(Context context, String type, List<NewsModel> data,RecyclerView recyclerView) {
        this.data = data;
        this.context = context;
        this.type = type;
        this.mInflater = LayoutInflater.from(context);
        typeface = Typeface.createFromAsset(context.getAssets(), "fonts/RobotoCondensed-Regular.ttf");
        typeface_normal = Typeface.createFromAsset(context.getAssets(), "fonts/RobotoCondensed-Light.ttf");
        requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_mera_news_placeholder)
                .error(R.drawable.ic_mera_news_placeholder)
                .centerCrop();
        setHasStableIds(true);
        if (recyclerView.getLayoutManager() instanceof LinearLayoutManager) {
            final LinearLayoutManager linearLayoutManager = (LinearLayoutManager) recyclerView.getLayoutManager();
            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    totalItemCount = linearLayoutManager.getItemCount();
                    lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition();
                    if (!loading && totalItemCount <= (lastVisibleItem + visibleThreshold)) {
                        if (onLoadMoreListener != null) {
                            onLoadMoreListener.onLoadMore();
                        }
                        loading = true;
                    }
                }
            });
        }
        }
    public void addModels(ArrayList<NewsModel> list) {
        this.data.addAll(list);
        this.notifyDataSetChanged();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM) {
            View kek = mInflater.inflate(R.layout.layout_cardview_item, parent,false);
            return new MainHolder(kek);
        } else{
            View kek = mInflater.inflate(R.layout.loading_footer, parent,false);
            return new FooterHolder(kek);
        }
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (holder instanceof MainHolder) {
            MainHolder vh = (MainHolder) holder;
            NewsModel model = data.get(position);
            vh.titleCardView.setText(model.getTitle());
            vh.titleCardView.setTypeface(typeface);
            Glide.with(context).load(model.getTw_image()).apply(requestOptions).into(vh.imageCardView);
            vh.iv_favorite.setImageResource(model.isFavorite()?R.drawable.ic_bookmark_on:R.drawable.ic_bookmark_off);
            vh.iv_favorite.setOnClickListener(v -> {
                model.setFavorite(!model.isFavorite());
                notifyDataSetChanged();
            });
            vh.txt_publish.setTypeface(typeface_normal);
            vh.txt_publish.setText(model.getPublished_on());
            vh.commonCardView.setOnClickListener(v -> {
                Intent intent = new Intent(context, DetailActivity.class);
                intent.putExtra("single_new", model);
                intent.putExtra("type",type);
                context.startActivity(intent);
            });
        } else if (holder instanceof FooterHolder) {
            ((FooterHolder) holder).loading.setIndeterminate(true);
        }
    }
    private class FooterHolder extends RecyclerView.ViewHolder {
        ProgressBar loading;
        FooterHolder(View itemView) {
            super(itemView);
            loading = itemView.findViewById(R.id.progressbar);
        }
    }
    private class MainHolder extends RecyclerView.ViewHolder {
        TextView titleCardView,txt_publish;
        ImageView imageCardView;
        ImageView iv_favorite;
        private CardView commonCardView;
        MainHolder(View view) {
            super(view);
            titleCardView = view.findViewById(R.id.titleCardView);
            imageCardView = view.findViewById(R.id.imageCardView);
            commonCardView = view.findViewById(R.id.commonCardView);
            iv_favorite = view.findViewById(R.id.iv_favorite);
            txt_publish = view.findViewById(R.id.txt_publish);
        }
    }
    @Override
    public int getItemViewType(int position) {
        return (position==data.size()-1) ? FOOTER : ITEM;
    }
    public void setLoaded() {
        loading = false;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemCount() {
        return data.size();
    }
    public void setOnLoadMoreListener(OnLoadMoreListener onLoadMoreListener) {
        this.onLoadMoreListener = onLoadMoreListener;
    }
}
