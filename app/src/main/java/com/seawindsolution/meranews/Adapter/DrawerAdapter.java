package com.seawindsolution.meranews.Adapter;

import android.content.Context;
import android.graphics.Typeface;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.seawindsolution.meranews.Model.CategoryModel;
import com.seawindsolution.meranews.R;

import java.util.List;

public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.MyViewHolder> {
    private Context mContext;
    private List<CategoryModel> categoryModels;
    private Typeface typeface;

    class MyViewHolder extends RecyclerView.ViewHolder {
        TextView title;
        ImageView icon;

        MyViewHolder(View view) {
            super(view);
            icon = view.findViewById(R.id.icon);
            title = view.findViewById(R.id.title);
        }
    }
    public DrawerAdapter(Context mContext, List<CategoryModel> category_list) {
        this.categoryModels = category_list;
        this.mContext = mContext;
        typeface = Typeface.createFromAsset(mContext.getAssets(), "fonts/roboto-light.ttf");
    }

    public CategoryModel getModels(int pos){
        return this.categoryModels.get(pos);
    }
    @Override
    public MyViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.layout_cardview_item, parent, false);
        return new MyViewHolder(itemView);
    }
    @Override
    public void onBindViewHolder(final MyViewHolder holder, int position) {
        final CategoryModel model = categoryModels.get(position);
        holder.title.setText(model.getCat_name());
//        holder.icon.setImageDrawable(typeface);
        /*Glide.with(mContext).load(model.getImage()).into(holder.imageCardView);
        holder.iv_favorite.setImageResource(model.isFavorite()?R.drawable.ic_bookmark_on:R.drawable.ic_bookmark_off);*/

     /*   holder.iv_favorite.setOnClickListener(v -> {
            model.setFavorite(!model.isFavorite());
            notifyDataSetChanged();
            Toast.makeText(mContext, model.isFavorite()?"Added in Favorite!":"Removed from Favorite!", Toast.LENGTH_SHORT).show();
        });*/

     /*   holder.commonCardView.setOnClickListener(v -> {
            Intent intent = new Intent(mContext, DetailActivity.class);
            intent.putExtra("Name", model.getTitle());
            intent.putExtra("Image", model.getImage());
            mContext.startActivity(intent);
        });*/
//        holder.iv_share.setOnClickListener(v -> Util.ShareContent(mContext,model.getTitle()));
    }
    @Override
    public int getItemCount() {
        return categoryModels.size();
    }
}