package com.seawindsolution.meranews.Utils;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.text.Html;
import android.text.Spanned;
import android.text.format.DateUtils;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorTreeAdapter;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.seawindsolution.meranews.Model.CategoryModel;
import com.seawindsolution.meranews.Model.NewsModel;
import com.seawindsolution.meranews.R;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Type;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.TimeZone;

/**
 * Created by ${Vrund} on 4/28/2017.
 */
public class Functions {


    @SuppressWarnings("UnusedParameters")
    public static boolean isConnected(Context context) {
        System.out.println("executeCommand");
        Runtime runtime = Runtime.getRuntime();
        try {
            Process mIpAddrProcess = runtime
                    .exec("/system/bin/ping -c 1 8.8.8.8");
            int mExitValue = mIpAddrProcess.waitFor();
            System.out.println(" mExitValue " + mExitValue);
            return mExitValue == 0;
        } catch (InterruptedException | IOException ignore) {
            ignore.printStackTrace();
            System.out.println(" Exception:" + ignore);
        }
        return false;
    }


    public static boolean appInstalledOrNot(Context context, String paramString) {
        PackageManager localPackageManager = context.getPackageManager();
        try {
            localPackageManager.getPackageInfo(paramString, 1);
            return true;
        } catch (PackageManager.NameNotFoundException ignored) {
        }
        return false;
    }


    public static float DpToPixel(float dp, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        return dp * ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
    }

    public static void setListViewHeightBasedOnChildren(Context context, ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            // pre-condition
            return;
        }

        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);
            listItem.measure(0, 0);
            totalHeight += listItem.getMeasuredHeight();
        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = (int) (totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1)) + DpToPixel(24, context));
        listView.setLayoutParams(params);
        listView.requestLayout();
    }

    public static CharSequence formatDate(String date1) {
        @SuppressLint("SimpleDateFormat")
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
        Date date = null;
        try {
            date = dateFormat.parse(date1);
        } catch (ParseException e) {
            Log.e("Error", e.toString());
            e.printStackTrace();
        }
        Calendar cal = Calendar.getInstance();
        TimeZone tz = cal.getTimeZone();
        TimeZone tz1 = TimeZone.getTimeZone("Asia/Kolkata");
        long time1 = 0;
        if (date != null) {
            if (tz.inDaylightTime(date)) {
                time1 = date.getTime() + tz.getRawOffset() - tz1.getRawOffset() + tz.getDSTSavings();
            } else {
                time1 = date.getTime() + tz.getRawOffset() - tz1.getRawOffset();
            }
        }
        long current = System.currentTimeMillis();
        CharSequence time = DateUtils.getRelativeTimeSpanString(time1, current,
                DateUtils.MINUTE_IN_MILLIS);
        if (time.equals("0 minutes ago")) {
            return "few seconds ago";
        } else {
            return time;
        }
    }

    @SuppressWarnings("deprecation")
    public static Spanned fromHtml(String html) {
        Spanned result;
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N) {
            result = Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY);
        } else {
            result = Html.fromHtml(html);
        }
        return result;
    }

    public static int getImageResourceByName(Context context, String aString) {
        String packageName = context.getPackageName();
        int resId = context.getResources().getIdentifier(aString.trim(), "drawable", packageName);
        if (resId == 0)
            return R.drawable.ic_dummy;
        else
            return resId;
    }

    public static ArrayList<CategoryModel> getCategories(String response) throws JSONException {
        ArrayList<CategoryModel> data = new ArrayList<>();
        JSONObject jsonObject = new JSONObject(response);
        JSONObject innerJsonObject = jsonObject.getJSONObject("response");
        JSONArray jsonArray = innerJsonObject.optJSONArray("data");
        for (int i = 0; i < jsonArray.length(); i++) {
            JSONObject vp = jsonArray.optJSONObject(i);
            CategoryModel cm = new CategoryModel();
            cm.setCat_id(vp.optString(Constants.CAT_ID));
            cm.setCat_name(vp.optString(Constants.CAT_NAME));
            data.add(cm);
        }
        return data;
    }

    public static ArrayList<NewsModel> getNews(JSONObject jsonObject) throws JSONException {
        ArrayList<NewsModel> data;
        Gson gson = new Gson();
        JSONObject innerObject = jsonObject.getJSONObject("response");
        JSONArray jsonArray = innerObject.optJSONArray("data");
        Type listType = new TypeToken<List<NewsModel>>(){}.getType();
        data = gson.fromJson(jsonArray.toString(),listType);
        return data;
    }

    public static void ShareContent(Context c,String str){
        String content = "https://meranews.in/"+str;
        Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
        sharingIntent.setType("text/plain");
        sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, content);
        sharingIntent.setFlags(Intent.FLAG_ACTIVITY_MULTIPLE_TASK);
        c.startActivity(Intent.createChooser(sharingIntent, "Share Via"));
    }
    public static String getDesc(String value){
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.N)
            return Html.fromHtml(value,Html.FROM_HTML_MODE_LEGACY).toString();
         else
            return Html.fromHtml(value).toString();

    }
    public static String getDate(String date){
            /*2018-05-23 09:00:00*/
        SimpleDateFormat input = new SimpleDateFormat("yyyy/MM/dd ");
        SimpleDateFormat output = new SimpleDateFormat("dd MMM yyyy");
        try {
            Date oneWayTripDate = input.parse(date);
            return output.format(oneWayTripDate);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return date;
    }
}
