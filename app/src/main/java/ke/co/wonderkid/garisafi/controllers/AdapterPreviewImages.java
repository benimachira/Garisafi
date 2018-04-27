package ke.co.wonderkid.garisafi.controllers;

import android.content.Context;
import android.net.Uri;
import android.support.v4.view.PagerAdapter;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;

import java.io.File;
import java.util.ArrayList;

import ke.co.wonderkid.garisafi.R;

public class AdapterPreviewImages extends PagerAdapter {

    private ArrayList<String> images;
    private LayoutInflater inflater;
    private Context context;

    public AdapterPreviewImages(Context context, ArrayList<String> images) {
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

        Uri uri=Uri.parse(new File(images.get(position).toString()).toString());
       // Toast.makeText(context, "this"+uri,Toast.LENGTH_LONG).show();

        ImageView myImage = (ImageView) myImageLayout
                .findViewById(R.id.image);

        File file = new File(images.get(position).toString());
        Uri imageUri = Uri.fromFile(file);
        Glide.with(context)
                .load(imageUri)
                .error(R.drawable.grey)
                .into(myImage);

        Glide.with(context)
                .load(imageUri)
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
}