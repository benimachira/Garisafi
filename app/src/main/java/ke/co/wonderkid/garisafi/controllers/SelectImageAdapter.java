package ke.co.wonderkid.garisafi.controllers;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

import ke.co.wonderkid.garisafi.R;
import ke.co.wonderkid.garisafi.models.SelectImageModel;

/**
 * Created by Oclemy on 8/4/2016 for ProgrammingWizards Channel and http://www.camposha.com.
 */
public class SelectImageAdapter extends BaseAdapter {

    Context c;
    ArrayList<SelectImageModel> spacecrafts;
    SelectImageAdapter adapter;
    public  ArrayList<String> SELECT_IMAGE_PATHS;

    public SelectImageAdapter(Context c, ArrayList<SelectImageModel> spacecrafts,ArrayList<String> SELECT_IMAGE_PATHS) {
        this.c = c;
        this.spacecrafts = spacecrafts;
        this.SELECT_IMAGE_PATHS=SELECT_IMAGE_PATHS;
        adapter=this;
    }

    @Override
    public int getCount() {
        return spacecrafts.size();
    }

    @Override
    public Object getItem(int i) {
        return spacecrafts.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(final int i, View view, ViewGroup viewGroup) {
        if (view == null) {
            //INFLATE CUSTOM LAYOUT
            view = LayoutInflater.from(c).inflate(R.layout.model, viewGroup, false);
        }

        final SelectImageModel spacecraft_item = (SelectImageModel) this.getItem(i);


        ImageView cancel_btn = (ImageView) view.findViewById(R.id.cancel_btn);
        ImageView img = (ImageView) view.findViewById(R.id.spacecraftImg);

        Glide.with(c)
                .load(spacecraft_item.getUri())
                .placeholder(R.drawable.placeholder)
                .into(img);

//        //BIND DATA
//        Picasso.with(c).load(spacecraft_item.getUri()).placeholder(R.drawable.placeholder).into(img);

        //VIEW ITEM CLICK
        img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


//               // SELECT_IMAGE_PATHS=new SellMyCarActivity().SELECT_IMAGE_PATHS;
//               // SELECT_IMAGE_PATHS.notify();
//               // synchronized(SELECT_IMAGE_PATHS){
//                    SELECT_IMAGE_PATHS.notifyAll();
//                }
//                SELECT_IMAGE_PATHS.remove(spacecraft_item);

                SELECT_IMAGE_PATHS.remove(SELECT_IMAGE_PATHS.get(i));
                spacecrafts.remove(spacecraft_item);
                adapter.notifyDataSetChanged();



            }
        });

        //VIEW ITEM CLICK
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //   Toast.makeText(c, s.getName(), Toast.LENGTH_SHORT).show();
            }
        });


        view.setOnLongClickListener(new View.OnLongClickListener() {

            @Override
            public boolean onLongClick(View v) {
                //your stuff
                Toast.makeText(c, spacecraft_item.getName(), Toast.LENGTH_SHORT).show();
                return true;
            }
        });
        return view;
    }
}
