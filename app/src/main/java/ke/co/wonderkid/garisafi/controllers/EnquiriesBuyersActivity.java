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
import ke.co.wonderkid.garisafi.models.BuyersEnqModel;
import ke.co.wonderkid.garisafi.utils.Config;
import ke.co.wonderkid.garisafi.utils.VolleyAppSingleton;



public class EnquiriesBuyersActivity extends AppCompatActivity {

    DatabaseHelper dbHelper;


    private String urlJsonArry =  "";
    RecyclerView recyclerView;
    ProgressDialog pDialog;
    DatabaseHelper db_helper;
    Context context;
    String user_id;
    String ad_id;
    static String original_enq_id;
    private SwipeRefreshLayout mSwipeRefreshLayout;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_buyers_enquiries);
        dbHelper=new DatabaseHelper(this);
        context=EnquiriesBuyersActivity.this;

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.buyers_enq_toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_material);
        ab.setDisplayHomeAsUpEnabled(true);

        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_advanced);


        // /You will setup the action bar with pull to refresh layout
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.container_buyers);

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



        pDialog = new ProgressDialog(this);
        db_helper=new DatabaseHelper(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        ad_id=getIntent().getExtras().getString("ad_id");
        original_enq_id =getIntent().getExtras().getString("original_enq_id");


        try {
            Cursor cus = dbHelper.select_user();
            if (cus.getCount() == 0) {
                user_id="";

            }else  {
                while (cus.moveToNext()){
                    user_id=cus.getString(0);

                }
            }
        } catch (SQLiteException se) {
            Log.e(getClass().getSimpleName(), "Open the database");
        }


        select_all_enquiries();




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





    private void initRecyclerView(List<BuyersEnqModel> items) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(context,items));

    }

    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {

        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        List<BuyersEnqModel>  items;
        Context context;

        public static class ViewHolder extends RecyclerView.ViewHolder {

            public final View mView;
            public final ImageView mImageView,message;
            public final TextView tv_buyer_name,tv_sms_count;


            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.avatar);
                tv_buyer_name = (TextView) view.findViewById(R.id.tv_buyer_name);
                tv_sms_count = (TextView) view.findViewById(R.id.tv_sms_count);
                message= (ImageView) view.findViewById(R.id.message);

            }

        }

        public SimpleStringRecyclerViewAdapter(Context context, List<BuyersEnqModel> items) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, false);
            this.items = items;
            this.context=context;


        }

        @Override
        public SimpleStringRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_buyers_enquiries, parent, false);
            view.setBackgroundResource(mBackground);
            return new SimpleStringRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final SimpleStringRecyclerViewAdapter.ViewHolder holder, final int position) {
            String buyer_name=items.get(position).getByrname();
            String buyer_phone=items.get(position).getByr_phno();
            String message_count=items.get(position).getMessage_count();
            Toast.makeText(context,"seller "+items.get(position).getSeller_id()+" buyer "+items.get(position).getBuyer_id(),Toast.LENGTH_LONG).show();
            holder.tv_buyer_name.setText(buyer_name);



            if(message_count.contains("null")) {
                holder.tv_sms_count.setVisibility(View.GONE);
                holder.message.setVisibility(View.GONE);

            }else if(Integer.parseInt(message_count)<=0) {
                holder.tv_sms_count.setVisibility(View.GONE);
                holder.message.setVisibility(View.GONE);
            }else if(Integer.parseInt(message_count)>0) {
                holder.tv_sms_count.setText(""+message_count);
            }


            Glide.with(holder.mImageView.getContext())
                    .load("")
                    .error(R.drawable.grey)
                    .placeholder( R.drawable.grey)
                    .into(holder.mImageView);

            holder.mView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,EnquiriesChatsActivity.class);

                    intent.putExtra("ad_id",items.get(position).getAd_id());
                    intent.putExtra("original_enq_id", original_enq_id);
                    intent.putExtra("seller_id",items.get(position).getSeller_id() );
                    intent.putExtra("buyer_id",items.get(position).getBuyer_id());

                    context.startActivity(intent);
                }
            });







        }


        @Override
        public int getItemCount() {
            return items.size();
        }
    }





    public void select_all_enquiries(){
        mSwipeRefreshLayout.setRefreshing(true);
        urlJsonArry =  new Config().GARISAFI_API+"/select_enquiry_buyers.php";
        final List<BuyersEnqModel> list = new ArrayList<>();
        String REQUEST_TAG = "BUYER_ENQUIRIES";

        StringRequest free_req = new StringRequest(Request.Method.POST,urlJsonArry,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

             //          Toast.makeText(context,""+response,Toast.LENGTH_LONG).show();

                        try {
                            // Parsing json array response
                            // loop through each json object



                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject person = (JSONObject) jsonArray.get(i);
                                String ad_id = person.getString("tbl_ads_idtbl_ads");
                                int tbl_enquiries_idtbl_enquiries= person.getInt("tbl_enquiries_idtbl_enquiries");
                                String byrname = person.getString("byrname");
                                String byr_phno = person.getString("byr_phno");
                                String message_count = person.getString("message_count");
                                String from_usrid = person.getString("from_usrid");
                                String to_usrid = person.getString("to_usrid");

                                if(Integer.parseInt(from_usrid)==Integer.parseInt(user_id) ){

                                }else {

                                    BuyersEnqModel buyer_enq_model = new BuyersEnqModel(byrname, byr_phno, message_count, ad_id,
                                            from_usrid, to_usrid);
                                    list.add(buyer_enq_model);
                                }
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


                if(mSwipeRefreshLayout.isRefreshing()){
                    // Notify swipeRefreshLayout that the refresh has finished
                    mSwipeRefreshLayout.setRefreshing(false);
                }

            }

        }){

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("ad_id", ad_id);
                params.put("user_id", user_id);


                return params;
            }

        };

        // Adding String request to request queue
        VolleyAppSingleton.getInstance(context).addToRequestQueue(free_req, REQUEST_TAG);

    }

}
