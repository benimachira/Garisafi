package ke.co.wonderkid.garisafi.controllers;

import android.content.Context;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.drawable.GlideDrawable;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.Target;

import java.util.ArrayList;

import ke.co.wonderkid.garisafi.R;

public class AdapterEnlargedImages extends PagerAdapter {

    private ArrayList<String> images;
    private LayoutInflater inflater;
    private Context context;




    public AdapterEnlargedImages(Context context, ArrayList<String> images) {
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
        View myImageLayout = inflater.inflate(R.layout.expanded_images_fragment, view, false);
        String custom_url="http://www.garisafi.com/ads_photos/";
        final ProgressBar progressBar = (ProgressBar) myImageLayout
                .findViewById(R.id.progress);

        ImageView myImage = (ImageView) myImageLayout
                .findViewById(R.id.expanded_image);

        myImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
              //  new CarDetails().show_dialog_detailed_photos();

            }
        });

//        Glide.with(context)
//                .load(custom_url+images.get(position).toString())
//                .error(R.drawable.grey)
//                .placeholder( R.drawable.grey)
//                .into(myImage);

        String img=custom_url+images.get(position).toString();


        Glide.with(context)
        .load(img)
                .listener(new RequestListener<String, GlideDrawable>() {
                    @Override
                    public boolean onException(Exception e, String model, Target<GlideDrawable> target, boolean isFirstResource) {
                        return false;
                    }

                    @Override
                    public boolean onResourceReady(GlideDrawable resource, String model, Target<GlideDrawable> target, boolean isFromMemoryCache, boolean isFirstResource) {
                        progressBar.setVisibility(View.GONE);
                        return false;
                    }
                })
                .into(myImage);

//        Glide.with(context)
//                .load(img)
//
//                .error(R.drawable.grey)
//                .placeholder( R.color.brown)
//                .animate(R.anim.fade_in)
//                .into(myImage);

        view.addView(myImageLayout, 0);
        return myImageLayout;
    }

    @Override
    public boolean isViewFromObject(View view, Object object) {
        return view.equals(object);
    }




}