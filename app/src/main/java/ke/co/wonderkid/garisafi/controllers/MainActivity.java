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

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
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
import com.github.silvestrpredko.dotprogressbar.DotProgressBar;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import ke.co.wonderkid.garisafi.R;
import ke.co.wonderkid.garisafi.models.AdsModel;
import ke.co.wonderkid.garisafi.utils.Config;
import ke.co.wonderkid.garisafi.utils.VolleyAppSingleton;

import static ke.co.wonderkid.garisafi.utils.UniversalMethods.doubleToStringNoDecimal;
import static ke.co.wonderkid.garisafi.utils.UniversalMethods.unversal_treated_string_Zero;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private DrawerLayout mDrawerLayout;
    DatabaseHelper dbHelper;
    int login_choice=0;
    Button header_cange_acc;
    View headerView;
    String username,user_phone,user_id;
    TextView tv_header_name;
    private  final  int PERMISSION_REQUEST_READ_WRITE=1;
    SharedPreferences sharedPref;
    public static final String MyPREFERENCES = "make_changes" ;
    public static boolean logged_in=false;
    public  static String notification_count;




    private String urlJsonArry =  "";
    RecyclerView recyclerView;

    SpotsDialog pDialog;

    JSONArray AD_ARRAY;
    JSONArray MAKE_ARRAY;
    JSONArray MODEL_ARRAY;
    JSONArray YEAR_ARRAY;
    JSONArray FEATURES_ARRAY;

    DatabaseHelper db_helper;
    int make_done,model_done=0;

    SharedPreferences make_model_sharedPref;
    public static final String MAKE_MODEL_PREFS = "make_model_prefs" ;
    Context context;
    FloatingActionButton fab_refresh;
    NavigationView navigationView;
    DotProgressBar dot_progress_bar;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private  boolean first_load=true;




    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        dbHelper=new DatabaseHelper(this);
        context=MainActivity.this;
        //loading_block_Dialog();

        Toolbar toolbar = (Toolbar) findViewById(R.id.main_toolbar);
        setSupportActionBar(toolbar);



        final ActionBar ab = getSupportActionBar();
        ab.setHomeAsUpIndicator(R.drawable.ic_menu);
        ab.setDisplayHomeAsUpEnabled(true);

        mDrawerLayout = (DrawerLayout) findViewById(R.id.drawer_layout);
        recyclerView = (RecyclerView) findViewById(R.id.recyclerview_advanced);
        fab_refresh=(FloatingActionButton) findViewById(R.id.fab);


        navigationView = (NavigationView) findViewById(R.id.nav_view);
        headerView = navigationView.getHeaderView(0);
        header_cange_acc = (Button) headerView.findViewById(R.id.header_cange_acc);
        tv_header_name = (TextView) headerView.findViewById(R.id.tv_header_name);
        dot_progress_bar = (DotProgressBar) findViewById(R.id.dot_progress_bar);


        sharedPref= getSharedPreferences(MyPREFERENCES, Context.MODE_PRIVATE);
        SharedPreferences.Editor editor=sharedPref.edit();
        editor.putString("uid","user_id");
        //editor.putInt("uid",user_id);
        editor.commit();

//        pDialog.setTitle("Configuring app..");
//        pDialog.setMessage("Please wait, this might take a while..");
        db_helper=new DatabaseHelper(this);



        // /You will setup the action bar with pull to refresh layout
        mSwipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.container);

        mSwipeRefreshLayout.setColorScheme(R.color.blue,
                R.color.colorPrimary, R.color.mycontentcolorPrimary, R.color.colorAccent);
        mSwipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Log.e(getClass().getSimpleName(), "refresh");
                first_load=false;

                select_car_ads_normal();
            }
        });




        make_model_sharedPref =this.getSharedPreferences(MAKE_MODEL_PREFS, MODE_PRIVATE);
        int strPref = make_model_sharedPref.getInt("make_model_inserted", 0);

        if (strPref ==1) {
            select_car_ads_normal();

        }else if(strPref==0) {
            Toast.makeText(this, "Invalid ", Toast.LENGTH_LONG).show();
            select_car_ads_normal();
        }




        select_user_details();



        if (navigationView != null) {
            setupDrawerContent(navigationView);
        }
        navigationView.setNavigationItemSelectedListener(this);
        // showing dot next to notifications label


        mDrawerLayout.addDrawerListener(new DrawerLayout.DrawerListener() {

            @Override
            public void onDrawerSlide(View drawerView, float slideOffset) {
                //Called when a drawer's position changes.
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                //Called when a drawer has settled in a completely open state.
                //The drawer is interactive at this point.
                // If you have 2 drawers (left and right) you can distinguish
                // them by using id of the drawerView. int id = drawerView.getId();
                // id will be your layout's id: for example R.id.left_drawer

                select_user_details();
                // get menu from navigationView
                Menu menu = navigationView.getMenu();

                // find MenuItem you want to change
                MenuItem nav_login = menu.findItem(R.id.nav_login);

                if(logged_in){
                    nav_login.setTitle("Log out");
                }else {
                    nav_login.setTitle("log in");
                }
                set_drawer_views(notification_count);
            }

            @Override
            public void onDrawerClosed(View drawerView) {
                // Called when a drawer has settled in a completely closed state.
            }

            @Override
            public void onDrawerStateChanged(int newState) {
                // Called when the drawer motion state changes. The new state will be one of STATE_IDLE, STATE_DRAGGING or STATE_SETTLING.
            }
        });


       header_cange_acc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
                mDrawerLayout.closeDrawer(GravityCompat.START);

                Intent intent=new Intent(MainActivity.this,LoginChoice.class);
                startActivity(intent);

                //showLoginChoiceDialog();

            }
        });

        fab_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                    select_car_ads_normal();

            }
        });

    }




    public void select_user_details(){
        try {
            Cursor cus = dbHelper.select_user();
            if (cus.getCount() == 0) {
                username="";
                logged_in=false;
                tv_header_name.setText("");

            }else  {
                while (cus.moveToNext()){
                    user_id=cus.getString(0);
                    user_phone=cus.getString(1);
                    username=cus.getString(2);


                }
                logged_in=true;
                tv_header_name.setText(""+username+"\n"+user_phone);
            }
        } catch (SQLiteException se) {
            Log.e(getClass().getSimpleName(), "Open the database");
        }
    }

    public void set_drawer_views(String notification_count){

        View view;
        LayoutInflater inflater = (LayoutInflater)   context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        view = inflater.inflate(R.layout.menu_dot, null);
        TextView text1 = (TextView) view.findViewById(R.id.notification);
       //Toast.makeText(context,"me "+notification_count, Toast.LENGTH_LONG).show();

        if(Integer.parseInt(unversal_treated_string_Zero(notification_count))>0){
            text1.setText(""+notification_count);

        }
        navigationView.getMenu().getItem(3).setActionView(view);

    }








    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.tools_menu, menu);
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
                mDrawerLayout.openDrawer(GravityCompat.START);
                return true;

            case R.id.menu_search:
                Intent intent=new Intent(this,Search_Activity.class);
                startActivity(intent);
                break;

        }
        return super.onOptionsItemSelected(item);
    }


    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();

        if (id == R.id.nav_home) {

            try {
                Cursor cus = dbHelper.select_user();
                if (cus.getCount() == 0) {
                   // showLoginChoiceDialog();

                    Intent intent=new Intent(this,LoginChoice.class);
                    startActivity(intent);

                }else  {
                    Intent intent=new Intent(this,UserProfile.class);
                    startActivity(intent);

                }
            } catch (SQLiteException se) {
                Log.e(getClass().getSimpleName(), "Open the database");
            }




        } if( id==R.id.sell_my_car){

            Intent intent=new Intent(this,SellMyCarActivity.class);
            startActivity(intent);


        }

        if (id == R.id.my_ads) {
            Intent intent=new Intent(this,MyAdsActivity.class);
            startActivity(intent);

        }

        if (id == R.id.enquiries) {
            Intent intent=new Intent(this,EnquiriesAllActivity.class);
            startActivity(intent);

        }

        if (id == R.id.my_likes) {

            Intent intent=new Intent(this,MyLikesActivity.class);
            startActivity(intent);

        }


        if (id == R.id.nav_login) {

            if(logged_in){
                item.setTitle("Log out");
                try {
                    Cursor cus = dbHelper.select_user();

                    if (cus.getCount() == 0) {
                        Toast.makeText(this,"Log in first", Toast.LENGTH_LONG).show();
                    }else  {
                        dbHelper.log_out();
                        Toast.makeText(this,"You have been logged out", Toast.LENGTH_LONG).show();
                        logged_in=false;

                    }

                } catch (SQLiteException se) {
                    Log.e(getClass().getSimpleName(), "Open the database");
                }


            }else {

                item.setTitle("log in");
                 try {
                    Cursor cus = dbHelper.select_user();
                    if (cus.getCount() == 0) {

                        Intent intent=new Intent(this,LoginChoice.class);
                        startActivity(intent);

                       // showLoginChoiceDialog();
                    }else  {
                        Intent intent=new Intent(this,UserProfile.class);
                        startActivity(intent);

                    }
                } catch (SQLiteException se) {
                    Log.e(getClass().getSimpleName(), "Open the database");
                }


            }



        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (requestCode == 1) {

            select_car_ads_normal();

        }
    }//onActivityResult


    private void setupDrawerContent(NavigationView navigationView) {
        navigationView.setNavigationItemSelectedListener(
                new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {
                menuItem.setChecked(true);
                mDrawerLayout.closeDrawers();
                return true;
            }
        });
    }





    private void initRecyclerView(List<AdsModel> items) {
        recyclerView.setLayoutManager(new LinearLayoutManager(recyclerView.getContext()));
        recyclerView.setAdapter(new SimpleStringRecyclerViewAdapter(MainActivity.this,items));

    }

    public class SimpleStringRecyclerViewAdapter
            extends RecyclerView.Adapter<SimpleStringRecyclerViewAdapter.ViewHolder> {

        private final TypedValue mTypedValue = new TypedValue();
        private int mBackground;
        List<AdsModel>  items;
        Context context;

        public class ViewHolder extends RecyclerView.ViewHolder {
            public String mBoundString;

            public final View mView;
            public final ImageView mImageView;
            public final TextView tv_make_model;
            TextView tv_year,tv_location,tv_installments,tv_price;
            LinearLayout liner_layout_parent;


            public ViewHolder(View view) {
                super(view);
                mView = view;
                mImageView = (ImageView) view.findViewById(R.id.avatar);
                tv_make_model = (TextView) view.findViewById(R.id.tv_make);
                tv_year = (TextView) view.findViewById(R.id.tv_year);
                tv_location = (TextView) view.findViewById(R.id.tv_location);
                tv_price = (TextView) view.findViewById(R.id.tv_price);
                tv_installments = (TextView) view.findViewById(R.id.tv_installments);
                liner_layout_parent =(LinearLayout) view.findViewById(R.id.liner_layout_parent);


            }



            @Override
            public String toString() {
                return super.toString() + " '" + tv_make_model.getText();
            }
        }

        public SimpleStringRecyclerViewAdapter(Context context, List<AdsModel> items) {
            context.getTheme().resolveAttribute(R.attr.selectableItemBackground, mTypedValue, false);
            this.items = items;
            this.context=context;


        }

        @Override
        public SimpleStringRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
            View view = LayoutInflater.from(parent.getContext())
                    .inflate(R.layout.list_item_main, parent, false);
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

            Typeface black_typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Black.ttf");
            Typeface bold_typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Bold.ttf");
            Typeface regular_typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Regular.ttf");
            Typeface medium_typeface = Typeface.createFromAsset(context.getAssets(), "fonts/Roboto-Medium.ttf");
            holder.tv_make_model.setTypeface(black_typeface);
            holder.tv_price.setTypeface(bold_typeface);
            holder.tv_year.setTypeface(regular_typeface);
            holder.tv_location.setTypeface(regular_typeface);
            holder.tv_installments.setTypeface(bold_typeface);



            String car_image1=items.get(position).getPhoto1();
            final int ad_id=items.get(position).getAd_id();
            final String ad_user_id=items.get(position).getAd_user_id();
            String custom_url="http://www.garisafi.com/ads_photos/";

            final String teams= make+ " "+model;



            holder.tv_make_model.setText(teams);
            holder.tv_year.setText(year);
            holder.tv_location.setText("Nairobi");

            String edited_price=price.split("\\.", 2)[0];
            holder.tv_price.setText("Ksh "+doubleToStringNoDecimal(Double.parseDouble(edited_price)));

            if(Integer.parseInt(installments)==0){
                holder.tv_installments.setVisibility(View.VISIBLE);
                holder.tv_installments.setText("");
            }else {
                holder.tv_installments.setText("Installments allowed");
            }


            Glide.with(holder.mImageView.getContext())
                    .load(custom_url+car_image1)
                    .placeholder( R.color.light_gray)
                    .error(R.color.light_gray)
                    .into(holder.mImageView);

            holder.liner_layout_parent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent=new Intent(context,CarDetails.class);
                    intent.putExtra("ad_id", ad_id);
                    intent.putExtra("ad_user_id", ad_user_id);

                    context.startActivity(intent);

                }
            });

            if (position == items.size() - 1) {
                first_load=false;
                select_car_ads_normal();

                Toast.makeText(context,"we at the last one",Toast.LENGTH_LONG).show();
            }







        }


        @Override
        public int getItemCount() {
            return items.size();
        }

    }


    public void showLoginChoiceDialog() {

        Button btn_reset,bill_views,cancel;
        TextView tv_dialog_title;
        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.material_design_not_logged, null);
        btn_reset = (Button) dialogView.findViewById(R.id.reset);
        bill_views = (Button) dialogView.findViewById(R.id.bill_views);
        cancel = (Button) dialogView.findViewById(R.id.cancel);
        tv_dialog_title=(TextView) dialogView.findViewById(R.id.tv_the_lable);
        dialogBuilder.setView(dialogView);
      //  dialogBuilder.setTitle("Select login if you already have an account");
        dialogBuilder.setCancelable(true);
        final AlertDialog b = dialogBuilder.create();
        b.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));

//            tv_dialog_title.setText(getString(R.string.login_dialog_lable));
//
//
//
//        btn_reset.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                //loggin to your account
//                login_choice=1;
//                b.dismiss();
//                Intent intent=new Intent(MainActivity.this,Userlogin.class);
//                intent.putExtra("choice",login_choice);
//                intent.putExtra("activity_id",1);
//                startActivity(intent);
//                logged_in=false;
//            }
//        });
//
//        bill_views.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//
//                //create an account
//                login_choice=2;
//              //  Toast.makeText(MainActivity.this,"this is create"+login_choice,Toast.LENGTH_LONG).show();
//                b.dismiss();
//                Intent intent=new Intent(MainActivity.this,Userlogin.class);
//                intent.putExtra("choice",login_choice);
//                intent.putExtra("activity_id",1);
//                startActivity(intent);
//                logged_in=false;
//            }
//        });
//
//        cancel.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                b.dismiss();
//            }
//        });
        b.show();
    }


    public void select_car_ads_normal(){
        urlJsonArry =  new Config().GARISAFI_API+"/select_ads_normal.php";

        final List<AdsModel> list = new ArrayList<>();
        String REQUEST_TAG = "NORMAL_NORMAL";

        if(first_load) {
            pDialog = new SpotsDialog(this, R.style.fetching);
            pDialog.setCancelable(false);
            pDialog.show();
        }else {
            mSwipeRefreshLayout.setRefreshing(true);
        }

        StringRequest free_req = new StringRequest(Request.Method.POST,urlJsonArry,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            // Parsing json array response
                            // loop through each json object/
                            //Toast.makeText(context,""+response, Toast.LENGTH_LONG).show();


                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject person = (JSONObject) jsonArray.get(i);
                                int ad_id = person.getInt("idtbl_ads");

                                if(ad_id!=-1) {

                                    String car_make = person.getString("car_make");
                                    String car_model = person.getString("car_model");
                                    String year = person.getString("car_year");
                                    String location = person.getString("location");
                                    String price = person.getString("price");
                                    String installment_status = person.getString("installments");
                                    String photo1 = person.getString("default_image");
                                    String ad_user_id = person.getString("tbl_usrac_idtbl_usrac");
                                    notification_count = person.getString("notification_count");


                                    AdsModel adsModel = new AdsModel(car_make, car_model, year, location, price, installment_status,
                                            photo1, ad_id, ad_user_id);
                                    list.add(adsModel);
                                }
                            }


                                //set_drawer_views(notification_count);
                                initRecyclerView(list);




                        } catch (JSONException e) {
                            fab_refresh.setVisibility(View.VISIBLE);
                            e.printStackTrace();

                        }catch (SQLiteException e){
                            fab_refresh.setVisibility(View.VISIBLE);

                        }


                        if(first_load) {
                            pDialog.dismiss();
                        }else {
                            if(mSwipeRefreshLayout.isRefreshing()){
                                // Notify swipeRefreshLayout that the refresh has finished
                                mSwipeRefreshLayout.setRefreshing(false);
                            }

                        }


                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                fab_refresh.setVisibility(View.VISIBLE);
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


                if(first_load) {
                    pDialog.dismiss();
                }else {
                    if(mSwipeRefreshLayout.isRefreshing()){
                        // Notify swipeRefreshLayout that the refresh has finished
                        mSwipeRefreshLayout.setRefreshing(false);
                    }

                }

            }
        }){



            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                if(logged_in) {
                    params.put("user_id", treated_string(user_id));
                }else {
                    params.put("user_id", "0");
                }


                return params;
            }

        };

        // Adding String request to request queue
        VolleyAppSingleton.getInstance(context).addToRequestQueue(free_req, REQUEST_TAG);

    }

    private String treated_string(String s) {

        if (TextUtils.isEmpty(s)) {
            return "0";
        }else {
            return s;
        }

    }
}
