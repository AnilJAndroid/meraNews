package com.seawindsolution.meranews.Activities;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.request.RequestOptions;
import com.seawindsolution.meranews.Model.NewsModel;
import com.seawindsolution.meranews.R;
import com.seawindsolution.meranews.Utils.Functions;

public class DetailActivity extends AppCompatActivity {

    private TextView titleTextView, descriptionTextView;
    private ImageView nameImageView;
    private RequestOptions requestOptions;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detail);

        Typeface typeface = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/RobotoCondensed-Regular.ttf");
        Typeface typeface_normal = Typeface.createFromAsset(getApplicationContext().getAssets(), "fonts/RobotoCondensed-Light.ttf");

        requestOptions = new RequestOptions()
                .placeholder(R.drawable.ic_mera_news_placeholder)
                .error(R.drawable.ic_mera_news_placeholder)
                .centerCrop();

        Toolbar toolbar = findViewById(R.id.toolbar);
        boolean is_vidio = getIntent().getBooleanExtra("is_video",false);

        if(is_vidio){
            ImageView ib_videos = findViewById(R.id.ib_videos);
            ib_videos.setVisibility(View.VISIBLE);
        }
        String type = getIntent().getStringExtra("type");
        toolbar.setTitle(type);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);


       NewsModel model = (NewsModel) getIntent().getSerializableExtra("single_new");

        titleTextView = findViewById(R.id.titleTextView);
        descriptionTextView = findViewById(R.id.descriptionTextView);
        nameImageView = findViewById(R.id.nameImageView);

        titleTextView.setText(model.getTitle());
        titleTextView.setTypeface(typeface);
        descriptionTextView.setText(Functions.getDesc(model.getContent()));
        Glide.with(getApplicationContext()).load(model.getTw_image()).apply(requestOptions).into(nameImageView);

        descriptionTextView.setTypeface(typeface);
        descriptionTextView.setTypeface(typeface_normal);
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.custom_details_menu, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                finish();
                return true;
            case R.id.btn_share:
                String str = "Is this a 'Sarkar' or a Circus?' : Jignesh Mevani";
                String content = "https://meranews.in/"+str;
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, content);
                sharingIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(Intent.createChooser(sharingIntent, "Share Via"));
//              Util.ShareContent(getApplicationContext(),content);
                return true;
            case R.id.btn_bookmark:
                Toast.makeText(this, "Added in Favorite!", Toast.LENGTH_SHORT).show();
                item.setIcon(ContextCompat.getDrawable(getApplicationContext(),R.drawable.ic_bookmark_on_black));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }
}
