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
import ke.co.wonderkid.garisafi.models.AdsModel;
import ke.co.wonderkid.garisafi.utils.Config;
import ke.co.wonderkid.garisafi.utils.VolleyAppSingleton;

import static ke.co.wonderkid.garisafi.utils.UniversalMethods.doubleToStringNoDecimal;

public class MyLikesActivity extends AppCompatActivity {
    DatabaseHelper dbHelper;
    private String urlJsonArry =  "";
    RecyclerView recyclerView;
    ProgressDialog pDialog;
    DatabaseHelper db_helper;
    String user_id;
    Context context;
    FloatingActionButton float_btn;
    List<String>  sold_car_list ;
    JSONObject jsonObject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.my_likes_activity);
        context=MyLikesActivity.this;
        dbHelper=new DatabaseHelper(context);


        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.my_ads_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_advanced);
        //float_btn=(FloatingActionButton) findViewById(R.id.fab);


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
            Toast.makeText(context,"Loging or sign up to view your liked vehicles",Toast.LENGTH_LONG).show();
        }else {

            try {
                jsonObject = new JSONObject();
                Cursor c=dbHelper.select_liked_cars(user_id);
                while (c.moveToNext()) {

                    String ad_id=c.getString(0);
                    jsonObject.put("params_" + c.getPosition(),ad_id);

                }
                c.close();


            }catch (SQLiteException e){

            } catch (JSONException e) {
                e.printStackTrace();
            }


            select_car_ads_normal();
        }

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





    private void initRecyclerView(List<AdsModel> items) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(MyLikesActivity.this,items));

    }

    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {

        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        List<AdsModel>  items;
        Context context;
        DatabaseHelper Helper;
        List<String>  sold_car_list ;
        int saved_is_sold;
        Boolean is_sold=false;

        public static class ViewHolder extends RecyclerView.ViewHolder {
            public String mBoundString;

            public final View mView;
            public final ImageView mImageView;
            public final TextView mTextView;
            TextView tv_year,tv_location,tv_installments,tv_price,tv_unread_count;
            LinearLayout linearLayout_parent;




            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.avatar);
                mTextView = (TextView) view.findViewById(R.id.tv_make);
                tv_year = (TextView) view.findViewById(R.id.tv_year);
                tv_location = (TextView) view.findViewById(R.id.tv_location);
                tv_price = (TextView) view.findViewById(R.id.tv_price);
                tv_installments = (TextView) view.findViewById(R.id.tv_installments);
                linearLayout_parent=(LinearLayout) view.findViewById(R.id.liner_layout_parent);



            }

            @Override
            public String toString() {
                return super.toString() + " '" + mTextView.getText();
            }
        }

        public SimpleStringRecyclerViewAdapter(Context context, List<AdsModel> items) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, false);
            this.items = items;
            this.context=context;
            Helper=new DatabaseHelper(this.context);


        }

        @Override
        public SimpleStringRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_my_liked_ads, parent, false);
            view.setBackgroundResource(mBackground);
            return new SimpleStringRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final SimpleStringRecyclerViewAdapter.ViewHolder holder, final int position) {
            String make=items.get(position).getCar_make();
            String model=items.get(position).getCar_model();
            String year=items.get(position).getYear();
            String location=items.get(position).getLocation();
            String price=items.get(position).getPrice();
            String installments=items.get(position).getInstallment_status();




            String car_image1=items.get(position).getPhoto1();
            final int ad_id=items.get(position).getAd_id();
            final String ad_user_id=items.get(position).getAd_user_id();
            String custom_url="http://www.garisafi.com/ads_photos/";

            final String teams= make+ "  "+model;



            holder.mTextView.setText(teams);
            holder.tv_year.setText(year);
            holder.tv_location.setText(""+location);

            String edited_price=price.split("\\.", 2)[0];
            holder.tv_price.setText("Ksh "+doubleToStringNoDecimal(Double.parseDouble(edited_price)));

            if(Integer.parseInt(installments)>0){
                holder.tv_installments.setVisibility(View.VISIBLE);
                holder.tv_installments.setText("Installments allowed");
            }else {
                holder.tv_installments.setText("");
            }


            Glide.with(holder.mImageView.getContext())
                    .load(custom_url+car_image1)
                    .error(R.drawable.grey)
                    .placeholder( R.drawable.grey)
                    .into(holder.mImageView);

            holder.linearLayout_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,CarDetails.class);
                    intent.putExtra("ad_id", ad_id);
                    intent.putExtra("ad_user_id", ad_user_id);

                    context.startActivity(intent);

                }
            });

        }


        @Override
        public int getItemCount() {
            return items.size();
        }
    }


    public void select_car_ads_normal(){
        pDialog.show();
        urlJsonArry =  new Config().GARISAFI_API+"/select_liked_ads.php";
        final List<AdsModel> list = new ArrayList<>();
        String REQUEST_TAG = "MY_ADS";

        StringRequest free_req = new StringRequest(Request.Method.POST,urlJsonArry,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Parsing json array response
                            // loop through each json object

                          // Toast.makeText(MyLikesActivity.this,""+response,Toast.LENGTH_LONG).show();

                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject person = (JSONObject) jsonArray.get(i);
                                int ad_id = person.getInt("idtbl_ads");
                                String car_make = person.getString("car_make");
                                String car_model = person.getString("car_model");
                                String year = person.getString("car_year");
                                String location = person.getString("location");
                                String price = person.getString("price");
                                String installment_status = person.getString("installments");
                                String photo1 = person.getString("default_image");
                                String ad_user_id = person.getString("tbl_usrac_idtbl_usrac");

                                AdsModel adsModel = new AdsModel(car_make,car_model,year, location, price, installment_status,
                                        photo1,ad_id,ad_user_id);
                                list.add(adsModel);
                            }

                            initRecyclerView(list);



                        } catch (JSONException e) {
                            e.printStackTrace();

                        }catch (SQLiteException e){

                        }
                        pDialog.dismiss();


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String message = null;
                if (volleyError instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(MyLikesActivity.this,""+message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                    Toast.makeText(MyLikesActivity.this,""+message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(MyLikesActivity.this,""+message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                    Toast.makeText(MyLikesActivity.this,""+message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(MyLikesActivity.this,""+message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                    Toast.makeText(MyLikesActivity.this,""+message, Toast.LENGTH_LONG).show();
                }
                pDialog.dismiss();

            }

        }){

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("params",jsonObject.toString());

                return params;
            }

        };

        // Adding String request to request queue
        VolleyAppSingleton.getInstance(MyLikesActivity.this).addToRequestQueue(free_req, REQUEST_TAG);

    }

}
