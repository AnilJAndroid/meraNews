package com.seawindsolution.meranews.Fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.Toast;

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

import okhttp3.ResponseBody;
import retrofit2.Callback;
import retrofit2.Response;

public class NewsFragment extends Fragment {

    RecyclerView news_list;
    NewsAdapterNew newsAdapter;
    ProgressBar Loading;

    boolean loading = false;

    int page = 1;
    String id,type;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View root = inflater.inflate(R.layout.home, container, false);
        news_list = root.findViewById(R.id.news_list);
        Loading = root.findViewById(R.id.progressbar);

        id = getArguments().getString("id");
        type = getArguments().getString("type");

//        final LinearLayoutManager gm = new LinearLayoutManager(getActivity());
        PreLoadingLinearLayoutManager gm = new PreLoadingLinearLayoutManager(getActivity());
        newsAdapter = new NewsAdapterNew(getActivity(),type, new ArrayList<>()) {
            @Override
            public void load() {
                if(!loading){
                    page++;
                    doBacground();
                }
            }
        };
        news_list.setLayoutManager(gm);
        news_list.addItemDecoration(new DividerItemDecoration(getResources()));
        news_list.setAdapter(newsAdapter);
        doBacground();
        return root;
    }

    void doBacground(){
        if (Functions.isConnected(getActivity())) {
            loading = true;
            getNews();
        } else {
            Toast.makeText(getActivity(), R.string.txt_internet_connection, Toast.LENGTH_SHORT).show();
        }
    }

    void getNews(){
        if(page==1)
            Loading.setVisibility(View.VISIBLE);
        else
            newsAdapter.updateFooter(true);
        Log.d("WebAPI page : ", String.valueOf(page));
       retrofit2.Call<ResponseBody> News = WebService.getWebService().getStory_Category(page,id);
       News.enqueue(new Callback<ResponseBody>() {
           @Override
           public void onResponse(retrofit2.Call<ResponseBody> call, Response<ResponseBody> response) {
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
                   Loading.setVisibility(View.GONE);
               }
           }
           @Override
           public void onFailure(retrofit2.Call<ResponseBody> call, Throwable t) {
               Loading.setVisibility(View.GONE);
               newsAdapter.updateFooter(false);
           }
       });
    }
}
