package com.seawindsolution.meranews.Activities;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.NavigationView;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.ViewPager;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.PopupMenu;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.text.Spannable;
import android.text.SpannableString;
import android.view.Gravity;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.SubMenu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.seawindsolution.meranews.Fragments.NewsFragment;
import com.seawindsolution.meranews.Model.CategoryModel;
import com.seawindsolution.meranews.R;
import com.seawindsolution.meranews.Utils.Constants;
import com.seawindsolution.meranews.Utils.CustomTypefaceSpan;
import com.seawindsolution.meranews.Utils.DataPreference;
import com.seawindsolution.meranews.Utils.Functions;

import org.json.JSONException;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private ViewPager viewPager;
    private NavigationView navigationView;
    private ArrayList<CategoryModel> Cats = new ArrayList<>();
    DataPreference dp;
    TabLayout tabLayout;
    TabAdapter tabAdapter;
    Typeface typeface;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dp = new DataPreference(this);
        typeface = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/RobotoCondensed-Regular.ttf");
        /*init task*/
        Toolbar toolbar = findViewById(R.id.toolbar);
        tabLayout = findViewById(R.id.tabs);
        viewPager = findViewById(R.id.viewpager);
        navigationView = findViewById(R.id.nav_view);

        try {
            Cats.addAll(Functions.getCategories(dp.getData("category_list")));
        } catch (JSONException e) {
            e.printStackTrace();
        }

        setSupportActionBar(toolbar);
        toolbar.setLogo(ContextCompat.getDrawable(getApplicationContext(), R.drawable.ic_mera_news_logo));
        toolbar.setOnClickListener(v -> startActivity(new Intent(getApplicationContext(),HomeActivity.class)));

        for (int i = 0; i < Cats.size(); i++) {
            tabLayout.addTab(tabLayout.newTab().setText(Cats.get(i).getCat_name()));
        }

        tabAdapter = new TabAdapter(getSupportFragmentManager(), tabLayout.getTabCount());
        viewPager.setAdapter(tabAdapter);

        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));

        tabLayout.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            public void onTabReselected(TabLayout.Tab tab) {}
            public void onTabSelected(TabLayout.Tab tab) {viewPager.setCurrentItem(tab.getPosition());}
            public void onTabUnselected(TabLayout.Tab tab) {}
        });

        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();

        navigationView.setNavigationItemSelectedListener(this);
        viewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
                navigationView.getMenu().getItem(position).setChecked(true);
            }
            @Override
            public void onPageSelected(int position) {}
            @Override
            public void onPageScrollStateChanged(int state) {}
        });
        View headerview = navigationView.getHeaderView(0);
        headerview.setOnClickListener(v -> {
            drawer.closeDrawer(Gravity.START);
            startActivity(new Intent(getApplicationContext(), HomeActivity.class));
        });

        changeNavigationtitlefont();
        changeTabsFont();
    }
    class TabAdapter extends FragmentPagerAdapter {
        int mNumOfTabs;
        ;
        TabAdapter(FragmentManager manager,int NumOfTabs) {
            super(manager);
            this.mNumOfTabs = NumOfTabs;
        }
        @Override
        public Fragment getItem(int position) {
            Bundle bundle = new Bundle();
            NewsFragment tab1 = new NewsFragment();
            bundle.putString("id",Cats.get(position).getCat_id());
            bundle.putString("type",Cats.get(position).getCat_name());
            tab1.setArguments(bundle);
            return tab1;
        }
        @Override
        public int getCount() {
            return mNumOfTabs;
        }
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
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        int id = item.getItemId();
      /*  if (id == R.id.nav_home) {
            viewPager.setCurrentItem(0);
        }*/  if (id == R.id.nav_gujarat) {
            viewPager.setCurrentItem(0);
        } else if (id == R.id.nav_national) {
            viewPager.setCurrentItem(1);
        } else if (id == R.id.nav_international) {
            viewPager.setCurrentItem(2);
        } else if (id == R.id.nav_entertainment) {
            viewPager.setCurrentItem(3);
        } else if (id == R.id.nav_business) {
            viewPager.setCurrentItem(4);
        } else if (id == R.id.nav_sports) {
            viewPager.setCurrentItem(5);
        } else if (id == R.id.nav_health) {
            viewPager.setCurrentItem(6);
        } else if (id == R.id.nav_editorial) {
            viewPager.setCurrentItem(7);
        } else if (id == R.id.nav_exclusive) {
            viewPager.setCurrentItem(8);
        } else if (id == R.id.nav_automobile) {
            viewPager.setCurrentItem(9);
        }else if(id == R.id.nav_facebook){
            onBrowseClick(Constants.Facebook_url);
        }else if(id == R.id.nav_twitter){
            onBrowseClick(Constants.Twitter_url);
        }else if(id == R.id.nav_google){
            onBrowseClick(Constants.Google_url);
        }else if(id == R.id.nav_instagram){
            onBrowseClick(Constants.Instagram_url);
        }else if(id == R.id.nav_youtube){
            onBrowseClick(Constants.Youtube_url);
        }
        DrawerLayout drawer = findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    void showPopup(View v) {
        PopupMenu popup = new PopupMenu(this, v);
        MenuInflater inflater = popup.getMenuInflater();
        inflater.inflate(R.menu.custom_setting_menu, popup.getMenu());
        popup.show();
    }
    public void onBrowseClick(String url) {
        Uri uri = Uri.parse(url);
        Intent intent = new Intent(Intent.ACTION_VIEW, uri);
        if (intent.resolveActivity(getPackageManager()) != null)
            startActivity(intent);
    }
    private void applyFontToMenuItem(MenuItem mi) {
        SpannableString mNewTitle = new SpannableString(mi.getTitle());
        mNewTitle.setSpan(new CustomTypefaceSpan("" , typeface), 0 , mNewTitle.length(),  Spannable.SPAN_INCLUSIVE_INCLUSIVE);
        mi.setTitle(mNewTitle);
    }


    private void changeTabsFont() {
        ViewGroup vg = (ViewGroup) tabLayout.getChildAt(0);
        int tabsCount = vg.getChildCount();
        for (int j = 0; j < tabsCount; j++) {
            ViewGroup vgTab = (ViewGroup) vg.getChildAt(j);
            int tabChildsCount = vgTab.getChildCount();
            for (int i = 0; i < tabChildsCount; i++) {
                View tabViewChild = vgTab.getChildAt(i);
                if (tabViewChild instanceof TextView) {
                    ((TextView) tabViewChild).setTypeface(typeface);
                }
            }
        }
    }
    private void changeNavigationtitlefont(){
        Menu m = navigationView.getMenu();
        for (int i=0;i<m.size();i++) {
            MenuItem mi = m.getItem(i);
            SubMenu subMenu = mi.getSubMenu();
            if (subMenu!=null && subMenu.size() >0 ) {
                for (int j=0; j <subMenu.size();j++) {
                    MenuItem subMenuItem = subMenu.getItem(j);
                    applyFontToMenuItem(subMenuItem);
                }
            }
            applyFontToMenuItem(mi);
        }
    }
}