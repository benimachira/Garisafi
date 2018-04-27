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
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
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
import ke.co.wonderkid.garisafi.models.AllEnqModel;
import ke.co.wonderkid.garisafi.utils.Config;
import ke.co.wonderkid.garisafi.utils.VolleyAppSingleton;


public class EnquiriesAllActivity extends AppCompatActivity {


    DatabaseHelper dbHelper;
    int login_choice=0;
    String username;




    private String urlJsonArry =  "";
    RecyclerView recyclerView;
    ProgressDialog pDialog;
    DatabaseHelper db_helper;
    Context context;
    private  static  String user_id;
    private SwipeRefreshLayout mSwipeRefreshLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_all_enquiries);
        dbHelper=new DatabaseHelper(this);
        context=EnquiriesAllActivity.this;

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.all_enq_toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_material);
        ab.setDisplayHomeAsUpEnabled(true);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_advanced);

        // /You will setup the action bar with pull to refresh layout
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.container);

        mSwipeRefreshLayout.setColorScheme(R.color.blue,
                R.color.colorPrimary, R.color.mycontentcolorPrimary, R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e(getClass().getSimpleName(), "refresh");
               // new GetLinks().execute();
                select_all_enquiries();
            }
        });

        try {
            Cursor cus = dbHelper.select_user();
            if (cus.getCount() == 0) {
                user_id="0";

            }else  {
                while (cus.moveToNext()){
                    user_id=cus.getString(0);
                    username=cus.getString(1);

                }
            }
        } catch (SQLiteException se) {
            Log.e(getClass().getSimpleName(), "Open the database");
        }

        if(user_id.contains("0")) {
            Intent intent=new Intent(this,LoginChoice.class);
            startActivity(intent);
            finish();
            Toast.makeText(context,"Loging or sign up to view your enquiries",Toast.LENGTH_LONG).show();
        }else {
            select_all_enquiries();

        }

    }






    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.refresh_menu, menu);
        return true;
    }

    @Override
    public boolean onPrepareOptionsMenu(Menu menu) {


        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case android.R.id.home:
                onBackPressed();
                return true;

            case R.id.menu_search:
                select_all_enquiries();

                break;

        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
            super.onBackPressed();

    }

    @Override
    protected void onResume() {
        super.onResume();
        select_all_enquiries();
    }


    private void initRecyclerView(List<AllEnqModel> items) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(context,items));

    }

    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {

        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        List<AllEnqModel>  items;
        Context context;

        public static class ViewHolder extends RecyclerView.ViewHolder {

            public final View mView;
            public final ImageView mImageView,message;
            public final TextView mTextView_make,badge_notification;

            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.avatar);
                mTextView_make = (TextView) view.findViewById(R.id.tv_make);
                badge_notification= (TextView) view.findViewById(R.id.badge_notification);
                message= (ImageView) view.findViewById(R.id.message);

            }

            @Override
            public String toString() {
                return super.toString() + " '" + mTextView_make.getText();
            }
        }

        public SimpleStringRecyclerViewAdapter(Context context, List<AllEnqModel> items) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, false);
            this.items = items;
            this.context=context;


        }

        @Override
        public SimpleStringRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_all_enquiries, parent, false);
            view.setBackgroundResource(mBackground);
            return new SimpleStringRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final SimpleStringRecyclerViewAdapter.ViewHolder holder, final int position) {
            String make=items.get(position).getCar_make();
            String model=items.get(position).getCar_model();
            String message=items.get(position).getEnqrymsg();
            String unread_count=items.get(position).getUnread_count();
            String image_name=items.get(position).getDefault_image();
            int ad_owner=items.get(position).getAd_owner();
            final String original_enq_id=items.get(position).getOriginal_enq_id();
            String custom_url="http://www.garisafi.com/ads_photos/";


            final String teams= make+ "  "+model;



            holder.mTextView_make.setText(teams);
            if(unread_count.contains("null")) {
                holder.badge_notification.setVisibility(View.GONE);
                holder.message.setVisibility(View.GONE);

            }else if(Integer.parseInt(unread_count)<=0) {
                holder.badge_notification.setVisibility(View.GONE);
                holder.message.setVisibility(View.GONE);
            }else if(Integer.parseInt(unread_count)>0) {
                holder.badge_notification.setText(""+unread_count);
            }



            Glide.with(holder.mImageView.getContext())
                    .load(custom_url+image_name)
                    .error(R.drawable.grey)
                    .placeholder( R.drawable.grey)
                    .into(holder.mImageView);


            if(ad_owner==Integer.parseInt(user_id)) {
                //if the ad is mine show me all the buyers that have sent they enquiries
                

                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, EnquiriesBuyersActivity.class);
                        intent.putExtra("ad_id", items.get(position).getAd_id());
                        intent.putExtra("original_enq_id", original_enq_id);

                        context.startActivity(intent);
                    }
                });


            }else {
                //if the ad is not mine show me the chat
                holder.mView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(context, EnquiriesChatsActivity.class);
                        intent.putExtra("ad_id",items.get(position).getAd_id());
                        intent.putExtra("original_enq_id", original_enq_id);
                        intent.putExtra("seller_id",items.get(position).getFrom_id() );
                        intent.putExtra("buyer_id",items.get(position).getTo_id());
                        context.startActivity(intent);
                    }
                });
            }






        }


        @Override
        public int getItemCount() {
            return items.size();
        }
    }




    public void select_all_enquiries(){
        mSwipeRefreshLayout.setRefreshing(true);
        urlJsonArry =  new Config().GARISAFI_API+"/select_all_enquiries.php";
        final List<AllEnqModel> list = new ArrayList<>();
        String REQUEST_TAG = "All_ENQUIRIES";

        StringRequest free_req = new StringRequest(Request.Method.POST,urlJsonArry,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                      // Toast.makeText(context,user_id+"\n\n"+response,Toast.LENGTH_LONG).show();

                        try {
                            // Parsing json array response
                            // loop through each json object



                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject person = (JSONObject) jsonArray.get(i);
                                String tbl_ads_idtbl_ads = person.getString("ad_id");
                                String car_make = person.getString("car_make");
                                String car_model = person.getString("car_model");
                                String enqrymsg = person.getString("enqrymsg");
                                String original_enq_id = person.getString("idtbl_enquiries");
                                String default_image = person.getString("default_image");
                                String to_usrid = person.getString("to_usrid");
                                String from_usrid = person.getString("from_usrid");
                                String unread_count = person.getString("unread_count");

                                int ad_owner=person.getInt("ad_owner");


                                AllEnqModel all_eq_model = new AllEnqModel(car_make,car_model,enqrymsg,tbl_ads_idtbl_ads,
                                        original_enq_id,unread_count,default_image,ad_owner,from_usrid,to_usrid);
                                list.add(all_eq_model);
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
                if(mSwipeRefreshLayout.isRefreshing()){
                    // Notify swipeRefreshLayout that the refresh has finished
                    mSwipeRefreshLayout.setRefreshing(false);
                }

                String message = null;
                if (volleyError instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(context,""+message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                    Toast.makeText(context,""+message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(context,""+message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof ParseError) {
                    message = "Parsing error! Please try again after some time!!";
                    Toast.makeText(context,""+message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection!";
                    Toast.makeText(context,""+message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof TimeoutError) {
                    message = "Connection TimeOut! Please check your internet connection.";
                    Toast.makeText(context,""+message, Toast.LENGTH_LONG).show();
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
        VolleyAppSingleton.getInstance(context).addToRequestQueue(free_req, REQUEST_TAG);

    }

}
