/*
 * Copyright (C) 2015 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ke.co.wonderkid.garisafi.controllers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.AppCompatDelegate;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.bumptech.glide.Glide;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ke.co.wonderkid.garisafi.R;
import ke.co.wonderkid.garisafi.models.MyAdsModel;
import ke.co.wonderkid.garisafi.utils.Config;
import ke.co.wonderkid.garisafi.utils.VolleyAppSingleton;

import static ke.co.wonderkid.garisafi.utils.UniversalMethods.doubleToStringNoDecimal;
import static ke.co.wonderkid.garisafi.utils.UniversalMethods.formarted_time;


public class MyAdsActivity extends AppCompatActivity {
    DatabaseHelper dbHelper;
    private String urlJsonArry =  "";
    RecyclerView recyclerView;
    ProgressDialog pDialog;
    DatabaseHelper db_helper;
    String user_id;
    Context context;
    FloatingActionButton float_btn;
    List<String>  sold_car_list ;
    FloatingActionButton upload_car_fab;
    private SwipeRefreshLayout mSwipeRefreshLayout;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_ads_activity);
        context=MyAdsActivity.this;
        dbHelper=new DatabaseHelper(context);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.my_ads_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_advanced);
        upload_car_fab=(FloatingActionButton) findViewById(R.id.fab_sell_car);


        // /You will setup the action bar with pull to refresh layout
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.container);

        mSwipeRefreshLayout.setColorScheme(R.color.blue,
                R.color.colorPrimary, R.color.mycontentcolorPrimary, R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e(getClass().getSimpleName(), "refresh");
                // new GetLinks().execute();
                select_car_ads_normal();
            }
        });


        pDialog = new ProgressDialog(this);
        db_helper=new DatabaseHelper(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        try {
            Cursor cus = dbHelper.select_user();
            if (cus.getCount() == 0) {
                user_id="0";

            }else  {
                while (cus.moveToNext()){
                    user_id=cus.getString(0);

                }
            }
        } catch (SQLiteException se) {
            Log.e(getClass().getSimpleName(), "Open the database");
        }
        if(user_id.contains("0")) {
            Intent intent=new Intent(this,LoginChoice.class);
            startActivity(intent);
            finish();
            Toast.makeText(context,"Loging or sign up to view your ads",Toast.LENGTH_LONG).show();
        }else {

            select_car_ads_normal();
        }

        upload_car_fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent=new Intent(context,SellMyCarActivity.class);
                startActivity(intent);
            }
        });


    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tools_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {
        switch (AppCompatDelegate.getDefaultNightMode()) {
        }
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                return true;
            case R.id.menu_search:
                Intent intent=new Intent(this,Search_Activity.class);
                startActivity(intent);
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    @Override
    public void onBackPressed() {

            super.onBackPressed();

    }





    private void initRecyclerView(List<MyAdsModel> items) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(MyAdsActivity.this,items));

    }

    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {

        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        List<MyAdsModel>  items;
        Context context;
        DatabaseHelper Helper;
        List<String>  sold_car_list ;
        int saved_is_sold;
        Boolean is_sold=false;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public String mBoundString;

            public final View mView;
            public final ImageView mImageView;
            public final TextView tv_make_model,tv_mark_as_sold,tv_year,install_nego_tv,tv_likes,tv_views;
            TextView tv_location,tv_price,tv_unread_count,tv_flagged,date_tv;
            LinearLayout badge_layout1;
            LinearLayout linear_preview,edit_ad_layout,linear_notification,linear_share;

            CheckBox checkBox_mark_as_sold;



            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.car_image);
                tv_make_model = (TextView) view.findViewById(R.id.tv_make);
                tv_price = (TextView) view.findViewById(R.id.tv_price);
                tv_mark_as_sold = (TextView) view.findViewById(R.id.tv_mark_as_sold);
                tv_year= (TextView) view.findViewById(R.id.tv_year);
                install_nego_tv= (TextView) view.findViewById(R.id.install_nego_tv);
                tv_unread_count= (TextView) view.findViewById(R.id.tv_unread_count);
                tv_flagged= (TextView) view.findViewById(R.id.tv_flagged);
//                badge_layout1= (LinearLayout) view.findViewById(R.id.badge_id_layout);
                tv_flagged= (TextView) view.findViewById(R.id.tv_flagged);
                date_tv= (TextView) view.findViewById(R.id.date_tv);
                tv_likes= (TextView) view.findViewById(R.id.tv_likes);
                tv_views= (TextView) view.findViewById(R.id.tv_views);

                linear_preview=(LinearLayout) view.findViewById(R.id.linear_preview);
                edit_ad_layout=(LinearLayout) view.findViewById(R.id.edit_ad_layout);
                linear_notification=(LinearLayout) view.findViewById(R.id.linear_notification);
                linear_share=(LinearLayout) view.findViewById(R.id.linear_share);
              checkBox_mark_as_sold=(CheckBox) view.findViewById(R.id.checkbox_mark_as_sold);


            }

            @Override
            public String toString() {
                return super.toString() + " '" + tv_make_model.getText();
            }
        }

        public SimpleStringRecyclerViewAdapter(Context context, List<MyAdsModel> items) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, false);
            this.items = items;
            this.context=context;
            Helper=new DatabaseHelper(this.context);


        }

        @Override
        public SimpleStringRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.my_ads_activity_content, parent, false);
            view.setBackgroundResource(mBackground);
            return new SimpleStringRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final SimpleStringRecyclerViewAdapter.ViewHolder holder, final int position) {
            String make=items.get(position).getCar_make();
            String model=items.get(position).getCar_model();
            String year=items.get(position).getYear();
            String location=items.get(position).getLocation();
            String date_posted=items.get(position).getUpload_date();
            final String car_condition=Integer.toString(items.get(position).getCar_condition());
            final String contact_mode=Integer.toString(items.get(position).getContact_mode());
            final String negotiable=Integer.toString(items.get(position).getNegotiable());
            final String mechanic_allowed=Integer.toString(items.get(position).getMechanic_allowed());
            final String car_history=Integer.toString(items.get(position).getCar_history());
            final String test_drive=Integer.toString(items.get(position).getTest_drive());
            final String installments=items.get(position).getInstallment_status();
            final String price=items.get(position).getPrice();
            final String mileage=items.get(position).getMileage();
            final int flagged_status=items.get(position).getFlagged_status();


            String likes=items.get(position).getLikes().trim();
            String ad_views=items.get(position).getAd_views().trim();
            String unread_enq=items.get(position).getUnread_count().trim();


            String car_image1=items.get(position).getPhoto1();
            final int ad_id=items.get(position).getAd_id();
            final String ad_user_id=items.get(position).getAd_user_id();
            final String original_id=items.get(position).getIdtbl_enquiries();
            String custom_url="http://www.garisafi.com/ads_photos/";

            final String teams= make+ " "+model;



            holder.tv_make_model.setText(teams+" ");
            holder.tv_year.setText("( "+year+" )");
            String edited_price=price.split("\\.", 2)[0];
            holder.tv_price.setText("Ksh "+doubleToStringNoDecimal(Double.parseDouble(edited_price))+"  ");
            holder.install_nego_tv.setText("Negotiable - installments allowed");
            holder.date_tv.setText("Posted "+formarted_time(date_posted));

            if(Integer.parseInt(unread_enq)>0) {
                holder.tv_unread_count.setText("" + unread_enq);
            }

            Log.d("whaaat",""+items.size());

            Log.d("wppppp","ad "+ad_id+" likes "+likes+" unread "+unread_enq);


            if(Integer.parseInt(ad_views)==0){
                holder.tv_views.setText("No views");
            }else if(Integer.parseInt(ad_views)==1){
                holder.tv_views.setText(""+ad_views+" view");
            }else if(Integer.parseInt(ad_views)>1){
                holder.tv_views.setText(""+ad_views+" views");
            }else {
                holder.tv_views.setText("No views");

            }

            if(Integer.parseInt(likes)==1){
                holder.tv_likes.setText(""+likes+" like");
            }else if(Integer.parseInt(likes)>1){
                holder.tv_likes.setText(""+likes+" likes");
            }




            try {
                is_sold=false;
                Cursor c=Helper.is_car_sold(ad_id,ad_user_id);
              if(c.getCount()>0){
                  is_sold=true;
              }
                c.close();

            }catch (SQLiteException e){

            }


            if(is_sold){
                holder.checkBox_mark_as_sold.setChecked(true);
              //  holder.tv_mark_as_sold.setTextColor(context.getResources().getColor(R.color.pakistan_green));
                holder.tv_mark_as_sold.setText("sold");
            }

            if(flagged_status==1) {
                holder.tv_flagged.setVisibility(View.VISIBLE);
            }else {
                holder.tv_flagged.setVisibility(View.GONE);
            }

            Glide.with(holder.mImageView.getContext())
                    .load(custom_url+car_image1)
                    .error(R.drawable.grey)
                    .placeholder( R.drawable.grey)
                    .into(holder.mImageView);

            holder.linear_preview.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,CarDetails.class);
                    intent.putExtra("ad_id", ad_id);
                    intent.putExtra("ad_user_id", ad_user_id);

                    context.startActivity(intent);

                }
            });

            holder.edit_ad_layout.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,EditMyAdActivity.class);
                    intent.putExtra("ad_id", ad_id);
                    intent.putExtra("car_condition", car_condition);
                    intent.putExtra("exterior_color", contact_mode);
                    intent.putExtra("negotiable", negotiable);
                    intent.putExtra("mechanic_allowed", mechanic_allowed);
                    intent.putExtra("car_history", car_history);
                    intent.putExtra("test_drive", test_drive);
                    intent.putExtra("installments", installments);
                    intent.putExtra("price", price.split("\\.", 2)[0]);
                    intent.putExtra("mileage", mileage);

                    context.startActivity(intent);


                }
            });

            holder.linear_notification.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    Intent intent=new Intent(context,EnquiriesAllActivity.class);
                    intent.putExtra("ad_id",Integer.toString(ad_id));
                    intent.putExtra("original_enq_id",original_id);
                    context.startActivity(intent);
                  Toast.makeText(context, "six "+ad_id+" "+original_id, Toast.LENGTH_LONG).show();

                }
            });

            holder.linear_share.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    share_this_car();

                }
            });


            holder.checkBox_mark_as_sold.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {


                    if (isChecked) {

                            new MarkAsSold(context,Integer.toString(ad_id),ad_user_id,1);

                           // holder.tv_mark_as_sold.setTextColor(context.getResources().getColor(R.color.colorPrimary));
                            holder.tv_mark_as_sold.setText("sold");

                    } else {
                        //not checked
                            new MarkAsSold(context,Integer.toString(ad_id),ad_user_id,0);

                           // holder.checkBox_mark_as_sold.setTextColor(context.getResources().getColor(R.color.font_color));
                            holder.tv_mark_as_sold.setText("mark as sold");

                    }





                }
            });

        }

        public  void share_this_car(){

            try {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                sharingIntent.setType("text/plain");
                String shareBody = "Toyota prado 2017\nwww.garisafi.com";
                sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Toyota prado 2017");
                sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
                context.startActivity(Intent.createChooser(sharingIntent, "Share this car via"));

            }catch (Exception e){
                Toast.makeText(context,"An error occured while trying to share this car",Toast.LENGTH_LONG).show();

            }
        }



        @Override
        public int getItemCount() {
            return items.size();
        }
    }



    public void select_car_ads_normal(){
        mSwipeRefreshLayout.setRefreshing(true);
        final List<MyAdsModel> list = new ArrayList<>();
        String REQUEST_TAG = "MY_ADS";
        urlJsonArry =  new Config().GARISAFI_API+"/select_my_ads.php";

        StringRequest free_req = new StringRequest(Request.Method.POST,urlJsonArry,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Parsing json array response
                            // loop through each json object

                        //    Toast.makeText(MyAdsActivity.this,""+response,Toast.LENGTH_LONG).show();

                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject person = (JSONObject) jsonArray.get(i);
                                int ad_id = person.getInt("ad_id");
                                String car_make = person.getString("car_make");
                                String car_model = person.getString("car_model");
                                String year = person.getString("car_year");
                                String location = person.getString("location");
                                String price = person.getString("price");
                                String photo1 = person.getString("default_image");
                                String ad_user_id = person.getString("tbl_usrac_idtbl_usrac");
                                String unread_count = person.getString("unread_count");
                                String idtbl_enquiries = person.getString("idtbl_enquiries");
                                int flagged_status = person.getInt("flagged_status");

                                int car_condition = person.getInt("tbl_carconditions_idtbl_carconditions");
                                int contact_mode = person.getInt("tbl_contactmode_idtbl_contactmode");
                                String mileage = person.getString("mileage");
                                String installment_status = person.getString("installments");
                                int negotiable = person.getInt("negotiable");
                                int mechanic_allowed = person.getInt("mechanic_allowed");
                                int car_history = person.getInt("car_history");
                                int test_drive = person.getInt("test_drive");
                                String upload_date = person.getString("upload_date");
                                String likes = person.getString("likes");
                                String views = person.getString("ad_views");

                                Log.d("meeeeee","ad "+ad_id+" likes "+likes+" views"+views);


                                MyAdsModel adsModel = new MyAdsModel(car_make,car_model,year, location, price, installment_status,
                                        photo1,ad_id,ad_user_id,unread_count,idtbl_enquiries,car_condition,contact_mode,mileage,negotiable,
                                        mechanic_allowed,car_history,test_drive,flagged_status,upload_date,likes,views);

                                list.add(adsModel);
                            }

                            initRecyclerView(list);



                        } catch (JSONException e) {
                            e.printStackTrace();

                        }catch (SQLiteException e){

                        }
                        if(mSwipeRefreshLayout.isRefreshing()){
                            // Notify swipeRefreshLayout that the refresh has finished
                            mSwipeRefreshLayout.setRefreshing(false);
                        }



                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String message = null;
                if (volleyError instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(MyAdsActivity.this,""+message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                    Toast.makeText(MyAdsActivity.this,""+message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(MyAdsActivity.this,""+message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                    Toast.makeText(MyAdsActivity.this,""+message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(MyAdsActivity.this,""+message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                    Toast.makeText(MyAdsActivity.this,""+message, Toast.LENGTH_LONG).show();
                }

                if(mSwipeRefreshLayout.isRefreshing()){
                    // Notify swipeRefreshLayout that the refresh has finished
                    mSwipeRefreshLayout.setRefreshing(false);
                }


            }

        }){

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", user_id);


                return params;
            }

        };

        // Adding String request to request queue
        VolleyAppSingleton.getInstance(MyAdsActivity.this).addToRequestQueue(free_req, REQUEST_TAG);

    }

}
