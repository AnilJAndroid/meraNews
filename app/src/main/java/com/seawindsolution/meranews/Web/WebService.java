package com.seawindsolution.meranews.Web;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by admin on 25-Apr-18.
 */

public class WebService {
    static final String API_URL_MAIN = "http://www.meranews.in/backend/mobileapi/";
    public static Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(API_URL_MAIN)
            .build();
    public static MNService getWebService(){
        return retrofit.create(MNService.class);
    }
    public interface MNService {
        @GET("get-menu-items")
        Call<ResponseBody> getMenuList();

        @GET("get-story-by-category")
        Call<ResponseBody> getStory_Category(@Query("page") int page, @Query("cat_id") String cat_id);

        @GET("get-top-stories")
        Call<ResponseBody> getTop_story();

        @GET("get-cover-story")
        Call<ResponseBody> getCoverStory();

        @GET("get-general-story")
        Call<ResponseBody> getTopStories(@Query("page") int page);
    }


}
