package ke.co.wonderkid.garisafi.controllers;

import android.os.Bundle;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import java.util.ArrayList;

import ke.co.wonderkid.garisafi.R;
import me.relex.circleindicator.CircleIndicator;

/**
 * Created by beni on 4/3/18.
 */

public class ExpandedImage extends AppCompatActivity {
    ViewPager viewPager;
    private ArrayList<String> images;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.expanded_image);
        images=getIntent().getExtras().getStringArrayList("chat_list");

        viewPager = (ViewPager) findViewById(R.id.viewPager_ad_images);
        viewPager.setAdapter(new AdapterEnlargedImages(this,images));
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);


        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.sell_my_car_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
                overridePendingTransition(0, R.anim.scale_down);
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        overridePendingTransition(0, R.anim.scale_down);
    }
}
