package com.seawindsolution.meranews.Activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.google.gson.Gson;
import com.seawindsolution.meranews.Adapter.HomeAdapter;
import com.seawindsolution.meranews.Adapter.NewsAdapterNew;
import com.seawindsolution.meranews.Model.NewsModel;
import com.seawindsolution.meranews.R;
import com.seawindsolution.meranews.Utils.DividerItemDecoration;
import com.seawindsolution.meranews.Utils.Functions;
import com.seawindsolution.meranews.Utils.PreLoadingLinearLayoutManager;
import com.seawindsolution.meranews.Web.WebService;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class HomeActivity extends AppCompatActivity {

    private RecyclerView top_news_recyclerview;
    private NestedScrollView nested_scrollview;
    private TextView main_title,title_top_new;
    ImageView main_news_image;
    private RequestOptions requestOptions;
    private HomeAdapter newsAdapter;
    boolean loading = false;
    int page = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        android.support.v7.widget.Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        /*solution for pagination recycleview in nestedscrollview*/
        nested_scrollview = findViewById(R.id.nested_scrollview);
        nested_scrollview.getViewTreeObserver().addOnScrollChangedListener(() -> {
            View view = nested_scrollview.getChildAt(nested_scrollview.getChildCount() - 1);
            int diff = (view.getBottom() - (nested_scrollview.getHeight() + nested_scrollview.getScrollY()));
            if (diff==0) {
                page++;
                getTop_Stories();
            }
        });


        PreLoadingLinearLayoutManager gm = new PreLoadingLinearLayoutManager(getApplicationContext());
        final LinearLayoutManager linearLayoutManager = new LinearLayoutManager(getApplicationContext());
        top_news_recyclerview = findViewById(R.id.top_news_recyclerview);
        top_news_recyclerview.setNestedScrollingEnabled(false);

        RecyclerView top_stories_recyclerview = findViewById(R.id.top_stories_recyclerview);
        top_stories_recyclerview.setNestedScrollingEnabled(false);
        main_title = findViewById(R.id.main_title);
        title_top_new = findViewById(R.id.title_top_new);
        main_news_image = findViewById(R.id.main_news_image);
        requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_mera_news_placeholder)
                .error(R.drawable.ic_mera_news_placeholder)
                .centerCrop();

        top_news_recyclerview.addItemDecoration(new DividerItemDecoration(getResources()));
        top_news_recyclerview.setLayoutManager(gm);
        top_stories_recyclerview.addItemDecoration(new DividerItemDecoration(getResources()));
        top_stories_recyclerview.setLayoutManager(linearLayoutManager);
        newsAdapter = new HomeAdapter(getApplicationContext(),"Home",new ArrayList<>());
        top_stories_recyclerview.setAdapter(newsAdapter);

        if(Functions.isConnected(getApplicationContext())){
            getCoverStory();
            get_top_news();
            getTop_Stories();
        }
        else
            Toast.makeText(getApplicationContext(), R.string.txt_internet_connection, Toast.LENGTH_SHORT).show();
    }

    void getCoverStory(){
        Call<ResponseBody> coverstory = WebService.getWebService().getCoverStory();
        coverstory.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    try {
                        String strRes = response.body().string();
                        JSONObject jsonObject = new JSONObject(strRes);
                        if (jsonObject.optString("success").equals("true")) {
                            JSONObject data = jsonObject.getJSONObject("response").getJSONObject("data");
                            NewsModel model = new Gson().fromJson(String.valueOf(data),NewsModel.class);
                            main_title.setText(model.getTitle());
                            Glide.with(getApplicationContext()).load(model.getTw_image()).apply(requestOptions).into(main_news_image);
                            main_news_image.setOnClickListener(v -> {
                                Intent intent = new Intent(getApplicationContext(), DetailActivity.class);
                                intent.putExtra("single_new", model);
                                intent.putExtra("type","Home");
                                startActivity(intent);
                            });
                        }
                    } catch (JSONException|IOException e) {e.printStackTrace();}
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {}
        });
    }
    void get_top_news(){
        Call<ResponseBody> top_news = WebService.getWebService().getTop_story();
        top_news.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    try {
                        String strRes = response.body().string();
                        JSONObject jsonObject = new JSONObject(strRes);
                        if (jsonObject.optString("success").equals("true")) {
                            ArrayList<NewsModel> models = new ArrayList<>();
                            JSONObject data = jsonObject.getJSONObject("response").getJSONObject("data");
                            for (Iterator<String> iter = data.keys();iter.hasNext();){
                                String key = iter.next();
                                JSONObject object = data.getJSONObject(key);
                                NewsModel model = new Gson().fromJson(String.valueOf(object),NewsModel.class);
                                models.add(model);
                            }
                            NewsAdapterNew adapterNew = new NewsAdapterNew(HomeActivity.this,"Home",models) {
                                @Override
                                public void load() {}
                            };
                            title_top_new.setVisibility(View.VISIBLE);
                            top_news_recyclerview.setAdapter(adapterNew);

                        }
                    } catch (JSONException|IOException e) {
                        e.printStackTrace();
                    }
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {}
        });
    }
    void getTop_Stories(){
        loading = true;
        newsAdapter.updateFooter(true);
        Call<ResponseBody> top_stories = WebService.getWebService().getTopStories(page);
        top_stories.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    loading = false;
                    ArrayList<NewsModel> temp = new ArrayList<>();
                    try {
                        String strRes = response.body().string();
                        JSONObject jsonObject = new JSONObject(strRes);
                        if (jsonObject.optString("success").equals("true")) {
                            temp.addAll(Functions.getNews(jsonObject));
                            newsAdapter.addModels(temp);
                        }
                    } catch (IOException|JSONException e) {
                        e.printStackTrace();
                    }
                    newsAdapter.updateFooter(false);
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {
                newsAdapter.updateFooter(false);
                top_news_recyclerview.setNestedScrollingEnabled(false);
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        menu.clear();
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(final MenuItem item) {
        switch (item.getItemId()) {
            case R.id.search:
                SearchManager manager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
                SearchView search = (SearchView)item.getActionView();
                search.setSearchableInfo(manager.getSearchableInfo(getComponentName()));
                search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
                    @Override
                    public boolean onQueryTextSubmit(String query) {
                        search.onActionViewCollapsed();
                        /*Intent i = new Intent(getApplicationContext(),SearchActivity.class);
                        i.putExtra("key",query);
                        startActivity(i);*/
                        return false;
                    }
                    @Override
                    public boolean onQueryTextChange(String query) {return true;}
                });
                return true;
            case R.id.action_settings:
                showPopup(findViewById(R.id.action_settings));
                return false;
            case android.R.id.home:
                onBackPressed();
                return false;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.custom_setting_menu, popup.getMenu());
        popup.show();
    }
}
