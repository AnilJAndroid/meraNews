package com.seawindsolution.meranews.Activities;

import android.animation.Animator;
import android.content.Intent;
import android.graphics.Point;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.view.Display;
import android.view.ViewAnimationUtils;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.seawindsolution.meranews.R;
import com.seawindsolution.meranews.Utils.DataPreference;
import com.seawindsolution.meranews.Utils.Functions;
import com.seawindsolution.meranews.Web.WebService;

import org.w3c.dom.Text;

import java.io.IOException;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class SplashActivity extends AppCompatActivity {

    DataPreference dp;
    CardView card_gujarati,card_english;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.splash_screen);

        Typeface typeface = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/RobotoCondensed-Regular.ttf");

        dp = new DataPreference(this);

        card_gujarati = findViewById(R.id.card_gujarati);
        card_english = findViewById(R.id.card_english);
        card_gujarati.setOnClickListener(v -> goto_home());
        card_english.setOnClickListener(v -> goto_home());

        ((TextView)findViewById(R.id.tvEng)).setTypeface(typeface);
        ((TextView)findViewById(R.id.tvGuj)).setTypeface(typeface);

//      new Handler().postDelayed(this::doAnimation, 500);
        doAnimation();
        GetData();

    }

    void doAnimation(){
        Display mdisp = getWindowManager().getDefaultDisplay();
        Point mdispSize = new Point();
        mdisp.getSize(mdispSize);
        int maxX = mdispSize.x;
        int maxY = mdispSize.y;

        LinearLayout shape = findViewById(R.id.circle);
        if(android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.LOLLIPOP){
            shape.post(() -> {
                Animator circularReveal = ViewAnimationUtils.createCircularReveal(
                        shape,
                        maxX,
                        maxY,
                        0,
                        (float) Math.hypot(shape.getWidth(), shape.getHeight()));
                circularReveal.setInterpolator(new AccelerateDecelerateInterpolator());
                circularReveal.setDuration(1000);
                circularReveal.start();
            });
        }else {
            Animation logoMoveAnimation = AnimationUtils.loadAnimation(this, android.R.anim.fade_in);
            shape.startAnimation(logoMoveAnimation);
        }
    }

    private void GetData(){
        Call<ResponseBody> menus = WebService.getWebService().getMenuList();
        menus.enqueue(new Callback<ResponseBody>() {
            @Override
            public void onResponse(Call<ResponseBody> call, Response<ResponseBody> response) {
                if(response.isSuccessful()){
                    try {
                        String strRes = response.body().string();
                        dp.saveData("category_list", strRes);
                    } catch (IOException e) {e.printStackTrace();}
                }
            }
            @Override
            public void onFailure(Call<ResponseBody> call, Throwable t) {}
        });
    }
    void goto_home(){
        if(Functions.isConnected(SplashActivity.this)){
            Intent i = new Intent(getApplicationContext(), MainActivity.class);
            startActivity(i);
        }else {
            Toast.makeText(SplashActivity.this, R.string.txt_internet_connection, Toast.LENGTH_SHORT).show();
        }
    }
}
