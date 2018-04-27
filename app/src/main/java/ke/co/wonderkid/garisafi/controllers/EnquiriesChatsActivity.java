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
import android.content.res.Configuration;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v4.content.ContextCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
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

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ke.co.wonderkid.garisafi.R;
import ke.co.wonderkid.garisafi.models.ChatsEnqModel;
import ke.co.wonderkid.garisafi.utils.Config;
import ke.co.wonderkid.garisafi.utils.VolleyAppSingleton;

import static ke.co.wonderkid.garisafi.controllers.MainActivity.notification_count;



public class EnquiriesChatsActivity extends AppCompatActivity {



    private String urlJsonArry =  "";
    RecyclerView recyclerView;
    ProgressDialog pDialog;
    DatabaseHelper db_helper;
    Context context;
    String user_id,ad_id,buyer_id,reply_message,original_enq_id,seller_id;
    EditText ed_reply;
    boolean logged_in=false;
    List<ChatsEnqModel> chat_list=null;
    String time_now;
    public LinearLayoutManager linearLayoutManager;
    private SwipeRefreshLayout mSwipeRefreshLayout;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chats_enquiries);
        db_helper=new DatabaseHelper(this);
        context=EnquiriesChatsActivity.this;

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.chats_enq_toolbar);
        setSupportActionBar(toolbar);

        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.abc_ic_ab_back_material);
        ab.setDisplayHomeAsUpEnabled(true);


        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_advanced);
        ed_reply= (EditText) findViewById(R.id.ed_reply);

        linearLayoutManager= new LinearLayoutManager(context);


        // /You will setup the action bar with pull to refresh layout
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.container_chats);

        mSwipeRefreshLayout.setColorScheme(R.color.blue,
                R.color.colorPrimary, R.color.mycontentcolorPrimary, R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                select_chat_enquiries();
            }
        });

        ad_id=getIntent().getExtras().getString("ad_id");
        buyer_id=getIntent().getExtras().getString("buyer_id");
        original_enq_id=getIntent().getExtras().getString("original_enq_id");
        seller_id=getIntent().getExtras().getString("seller_id");

        Toast.makeText(this,"ad\n"+ad_id+" buyer id\n"+buyer_id+" seller id\n"+seller_id,Toast.LENGTH_LONG).show();
        fecth_user_details();
        select_chat_enquiries();

//        ed_reply.setOnFocusChangeListener(new View.OnFocusChangeListener() {
//            @Override
//            public void onFocusChange(View view, boolean hasFocus) {
//
//
//
//            }
//        });




    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);


        // Checks whether a hardware keyboard is available
        if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_NO) {
            if(recyclerView!=null) {
                recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
            }
        } else if (newConfig.hardKeyboardHidden == Configuration.HARDKEYBOARDHIDDEN_YES) {
            if(recyclerView!=null) {
                recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount() - 1);
            }
        }
    }


    public  void  fecth_user_details(){

        try {
            Cursor cus = db_helper.select_user();
            if (cus.getCount() == 0) {

                user_id="0";
                logged_in=false;
            }else  {
                while (cus.moveToNext()){
                    user_id=cus.getString(0);
//                    local_phone_no=cus.getString(1);
//                    local_user_name=cus.getString(2);
//                    local_email_address=cus.getString(3);

                }
                logged_in=true;

            }
        } catch (SQLiteException se) {

            Log.e(getClass().getSimpleName(), "Open the database"+se.getMessage().toString());
        }


    }

    public void  reply_message(View view){
        reply_message=ed_reply.getText().toString();
        if (TextUtils.isEmpty(reply_message)) {
            ed_reply.setError("Please enter a reply first");
            return;

        }



        send_reply();


    }




// this be the toolbar section of and activit.. We override the three methods for menu functions

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
                select_chat_enquiries();

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
       // select_chat_enquiries();
    }





    private void initRecyclerView(List<ChatsEnqModel> items) {
        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(context,items,user_id,0));
        recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount() - 1);

    }

    private void udateRecyclerView(List<ChatsEnqModel> items,int action) {

        recyclerView.setLayoutManager(linearLayoutManager);
        recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(context,items,user_id,action));
        recyclerView.smoothScrollToPosition(recyclerView.getAdapter().getItemCount() - 1);

    }

    public static class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {

     //   private final TypedValue mTypedValue = new TypedValue();
     //   private int mBackground;
        List<ChatsEnqModel>  items;
        Context context;
        String user_id;
        int action;

        public static class ViewHolder extends RecyclerView.ViewHolder {

            public final View mView;
            public final TextView tv_sender,tv_message;
            LinearLayout linear_parent, linear_sent;


            public ViewHolder(View view) {
                super(view);
                mView = view;
                tv_sender = (TextView) view.findViewById(R.id.tv_sender);
                tv_message = (TextView) view.findViewById(R.id.tv_chat_message);
                linear_parent=(LinearLayout) view.findViewById(R.id.linear_parent);
                linear_sent=(LinearLayout) view.findViewById(R.id.linear_sent);

            }

        }

        public SimpleStringRecyclerViewAdapter(Context context, List<ChatsEnqModel> items,String user_id,Integer action) {
           // context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, false);
            this.items = items;
            this.context=context;
            this.user_id=user_id;
            this.action=action;


        }

        @Override
        public SimpleStringRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_chats_enquiries, parent, false);
           // view.setBackgroundResource(mBackground);
            return new SimpleStringRecyclerViewAdapter.ViewHolder(view);
        }

        @Override
        public void onBindViewHolder(final SimpleStringRecyclerViewAdapter.ViewHolder holder, final int position) {
            String message_sent=items.get(position).getEnqrymsg();
            String from_id=items.get(position).getFrom_id().toString();
            String buyer_name=items.get(position).getByrname().toString();




            if(from_id.trim().contentEquals(user_id)) {
                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(10,10,100,10);
                holder.tv_message.setLayoutParams(params);
                holder.linear_parent.setGravity(Gravity.LEFT );
                holder.tv_sender.setText("Me");

                final int sdk = android.os.Build.VERSION.SDK_INT;
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    holder.tv_message.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.my_sms_bg_me) );
                } else {
                    holder.tv_message.setBackground(ContextCompat.getDrawable(context, R.drawable.my_sms_bg_me));
                }

                if (position == items.size() - 1) {

                    if(action==1){
                        holder.linear_sent.setGravity(Gravity.LEFT);
                        holder.linear_sent.setVisibility(View.VISIBLE);
                    }
                }





            }else {

                LinearLayout.LayoutParams params = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT, LinearLayout.LayoutParams.WRAP_CONTENT);
                params.setMargins(100,10,10,10);
                holder.tv_message.setLayoutParams(params);
                holder.linear_parent.setGravity(Gravity.RIGHT );
                holder.tv_sender.setText(""+buyer_name);

                final int sdk = android.os.Build.VERSION.SDK_INT;
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    holder.tv_message.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.my_sms_bg_them) );
                } else {
                    holder.tv_message.setBackground(ContextCompat.getDrawable(context, R.drawable.my_sms_bg_them));
                }

            }
            holder.tv_message.setText(""+message_sent);





        }


        @Override
        public int getItemCount() {
            return items.size();
        }
    }





    public void select_chat_enquiries(){

     mSwipeRefreshLayout.setRefreshing(true);
     chat_list = new ArrayList<>();
         Toast.makeText(context,"from "+buyer_id+" to "+seller_id+" my id "+user_id,Toast.LENGTH_LONG).show();



        urlJsonArry =  new Config().GARISAFI_API+"/select_enquiry_chats.php";
        String REQUEST_TAG = "CHAT_ENQUIRIES";


        StringRequest free_req = new StringRequest(Request.Method.POST,urlJsonArry,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                      //  Toast.makeText(context,""+response,Toast.LENGTH_LONG).show();

                        if(response.contentEquals("NO")){

                        }else {

                            try {
                                // Parsing json array response
                                // loop through each json object


                                JSONArray jsonArray = new JSONArray(response);
                                for (int i = 0; i < jsonArray.length(); i++) {

                                    JSONObject person = (JSONObject) jsonArray.get(i);
                                    // int ad_id = person.getInt("idtbl_ads");

                                    String to_usrid = person.getString("to_usrid");
                                    String from_usrid = person.getString("from_usrid");
                                    String enqrydate = person.getString("enqrydate");
                                    String enqrymsg = person.getString("enqrymsg");
                                    String offer_price = person.getString("offer_price");
                                    String byrname = person.getString("byrname");


//                                String price = person.getString("price");
//                                String installment_status = person.getString("installments");
//                                String photo1 = person.getString("default_image");

                                    ChatsEnqModel chats_enq_model = new ChatsEnqModel(to_usrid, from_usrid,
                                            enqrydate, enqrymsg, offer_price, byrname);
                                    chat_list.add(chats_enq_model);
                                }

                                notification_count="0";


                                initRecyclerView(chat_list);

                            } catch (JSONException e) {
                                e.printStackTrace();

                            } catch (SQLiteException e) {

                            }
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
                params.put("from_id", buyer_id);
                params.put("to_id", seller_id);

                params.put("seller_id", seller_id);
                params.put("my_id", user_id);

                return params;
            }

        };

        // Adding String request to request queue
        VolleyAppSingleton.getInstance(context).addToRequestQueue(free_req, REQUEST_TAG);

    }

    public void send_reply(){
        mSwipeRefreshLayout.setRefreshing(true);

        urlJsonArry =  new Config().GARISAFI_API+"/reply_to_enquiry.php";
        String REQUEST_TAG = "SEND_REPLY";
        final String to_id;

        if(user_id.contentEquals(buyer_id)){
            to_id=seller_id;
        }else {
            to_id= buyer_id;
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        time_now = sdf.format(new Date());


        StringRequest free_req = new StringRequest(Request.Method.POST,urlJsonArry,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {


                        if(response.contentEquals("1")){
                            ed_reply.setText("");
                            try {
                                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                                imm.hideSoftInputFromWindow(getCurrentFocus().getWindowToken(), 0);
                            } catch (Exception e) {
                                Log.e("error",e.getMessage().toString());
                            }
                            ChatsEnqModel chats_enq_model = new ChatsEnqModel(to_id,user_id,
                                    time_now,reply_message,"0","");
                            chat_list.add(chats_enq_model);
                            udateRecyclerView(chat_list,1);
                        }else if(response.contentEquals("2")){
                            Toast.makeText(context,"could not send message",Toast.LENGTH_LONG).show();


                        }else {

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
                params.put("from_id", user_id);

                if(user_id.contentEquals(buyer_id)){
                    params.put("to_id", seller_id);
                }else {
                    params.put("to_id", buyer_id);
                }

                params.put("reply_message", reply_message);
                params.put("original_enq_id", original_enq_id);



                return params;
            }

        };

        // Adding String request to request queue
        VolleyAppSingleton.getInstance(context).addToRequestQueue(free_req, REQUEST_TAG);

    }



}
