package com.seawindsolution.meranews.Adapter;

import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.request.RequestOptions;
import com.seawindsolution.meranews.Activities.DetailActivity;
import com.seawindsolution.meranews.Model.NewsModel;
import com.seawindsolution.meranews.R;

import java.util.ArrayList;

public abstract class NewsAdapterNew extends RecyclerView.Adapter<RecyclerView.ViewHolder> {

    private ArrayList<NewsModel> data;
    private Context context;
    private Typeface typeface,typeface_normal;
    private RequestOptions requestOptions;
    private LayoutInflater mInflater;
    private boolean withFooter;
    private String type;
    private final int FOOTER = 0;
    private final int ITEM = 1;
    private android.os.Handler handler;

    public abstract void load();

    public NewsAdapterNew(Context context, String type, ArrayList<NewsModel> data) {
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
        handler = new android.os.Handler();
    }
    public void addModels(ArrayList<NewsModel> list) {
        this.data.addAll(list);
        this.notifyDataSetChanged();
    }
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == ITEM) {
            View kek = mInflater.inflate(R.layout.layout_cardview_item, null);
            return new MainHolder(kek);
        } else{
            View kek = mInflater.inflate(R.layout.loading_footer, null);
            return new FooterHolder(kek);
        }
    }
    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (data.size()>0 && (  position >= getItemCount() - 1)) {
            load();
        }
        if (getItemViewType(position) == ITEM) {
            MainHolder vh = (MainHolder) holder;
            NewsModel model = data.get(position);
            vh.titleCardView.setText(model.getTitle());
            vh.titleCardView.setTypeface(typeface);
//          Glide.with(context).load(model.getTw_image()).apply(requestOptions).into(vh.imageCardView);
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
        } else if (getItemViewType(position) == FOOTER) {
            FooterHolder vh = (FooterHolder) holder;
            vh.loading.setVisibility(withFooter?View.VISIBLE:View.GONE);
        }
    }
    private class FooterHolder extends RecyclerView.ViewHolder {
        LinearLayout loading;
        FooterHolder(View itemView) {
            super(itemView);
            loading = itemView.findViewById(R.id.loading);
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
         if (withFooter && position == getItemCount() - 1)
            return FOOTER;
        return ITEM;
    }
    @Override
    public long getItemId(int position) {
        return position;
    }
    @Override
    public int getItemCount() {
        int itemCount = data.size();
        if (withFooter)
            itemCount++;
        return itemCount;
    }
    public void updateFooter(boolean conn) {
        handler.post(() -> {
            withFooter = conn;
            notifyDataSetChanged();
        });
    }
}
