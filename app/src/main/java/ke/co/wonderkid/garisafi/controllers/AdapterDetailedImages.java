package ke.co.wonderkid.garisafi.controllers;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import ke.co.wonderkid.garisafi.R;
import me.relex.circleindicator.CircleIndicator;

public class AdapterDetailedImages extends PagerAdapter {

    private ArrayList<String> images;
    private LayoutInflater inflater;
    private Context context;
    Boolean active=false;



    public AdapterDetailedImages(Context context, ArrayList<String> images) {
        this.context = context;
        this.images=images;
        inflater = LayoutInflater.from(context);
    }

    @Override
    public void destroyItem(ViewGroup container, int position, Object object) {
        container.removeView((View) object);
    }

    @Override
    public int getCount() {

        return images.size();
    }

    @Override
    public Object instantiateItem(ViewGroup view, int position) {
        View myImageLayout = inflater.inflate(R.layout.slide, view, false);
        String custom_url="http://www.garisafi.com/ads_photos/";
        ImageView myImage = (ImageView) myImageLayout
                .findViewById(R.id.image);
        myImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
             //   Toast.makeText(context,"whoo", Toast.LENGTH_LONG).show();
              //  new CarDetails().show_dialog_detailed_photos();
                Intent i=new Intent(context,ExpandedImage.class);
                i.putStringArrayListExtra("chat_list", images);
                context.startActivity(i);
                ((Activity) context).overridePendingTransition(R.anim.scale_up, 0);


                //show_dialog_detailed_photos();
            }
        });

        String img=custom_url+images.get(position).toString();
        Glide.with(context)
                .load(img)
                .error(R.drawable.grey)
                .placeholder( R.drawable.grey)
                .animate(R.anim.fade_in)
                .into(myImage);




        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }
    public void show_dialog_detailed_photos() {

        Button btn_enquire,confirm_phone,submit_code,ok_btn;
        Spinner sp_report_ad;
        ViewPager viewPager;
        TextView tv_titles;

        ((Activity) context).getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        final android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(context);
        LayoutInflater inflater = ((Activity) context).getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_show_large_images, null);
        dialogBuilder.setView(dialogView);

        tv_titles = (TextView) dialogView.findViewById(R.id.tv_titles);


        viewPager = (ViewPager) dialogView.findViewById(R.id.viewPager_ad_images);
        viewPager.setAdapter(new AdapterEnlargedImages(context,images));
        CircleIndicator indicator = (CircleIndicator) dialogView.findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);

        dialogBuilder.setCancelable(true);
        final android.support.v7.app.AlertDialog b = dialogBuilder.create();
        b.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        tv_titles.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                b.dismiss();

            }
        });


            b.show();


    }

}