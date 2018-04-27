package ke.co.wonderkid.garisafi.controllers;

import android.app.AlertDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
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
import com.github.clans.fab.FloatingActionButton;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import ke.co.wonderkid.garisafi.R;
import ke.co.wonderkid.garisafi.models.FeaturesItem;
import ke.co.wonderkid.garisafi.utils.Config;
import ke.co.wonderkid.garisafi.utils.VolleyAppSingleton;
import me.relex.circleindicator.CircleIndicator;

import static ke.co.wonderkid.garisafi.utils.UniversalMethods.cooking_time;
import static ke.co.wonderkid.garisafi.utils.UniversalMethods.doubleToStringNoDecimal;
import static ke.co.wonderkid.garisafi.utils.UniversalMethods.isValidMail;
import static ke.co.wonderkid.garisafi.utils.UniversalMethods.isValidMobile;
import static ke.co.wonderkid.garisafi.utils.UniversalMethods.unversal_treated_string;
import static ke.co.wonderkid.garisafi.utils.UniversalMethods.unversal_treated_string_Zero;

/**
 * Created by Beni on 11/14/2017.
 */

public class CarDetails extends AppCompatActivity{


    ViewPager viewPager;
    MyAdapter3 myCustomPagerAdapter;
    private ArrayList<String> image_array = new ArrayList<String>();
    // Initializing chat_list view with the custom adapter
    ArrayList <FeaturesItem> itemList = new ArrayList<FeaturesItem>();
    private static int currentPage = 0;
    AlertDialog pDialog;
    String username_new,image_name;
    String urlJsonArry;
    int ad_id;
    DatabaseHelper databaseHelper;

    String local_user_name,local_phone_no,local_email_address= "";
    String ad_user_id="0";
    String my_user_id="0";


    String idtbl_ads,car_make,car_model,car_year,currency,fuel_type,car_condition,transimision_mode,location,mileage,
            price,installments, upload_date,negotiable,mechanic_allowed,car_history,test_drive,duty_paid,engine_size;
    String  idtbl_usrac,usrfname,company_name,mobileno_one,defaultcontact,usremail,seller_name;

    TextView tv_dt_price, tv_dt_make,tv_dt_year,tv_dt_city,tv_dt_installments,tv_dt_mileage,
            tv_dt_fueltype,tv_car_condition,tv_transmition,et_negotiable,et_mechanic_inspection,
            et_car_history,et_test_drive,posted_by,et_engine_size,et_duty_paid;

    JSONArray AD_ARRAY;
    JSONArray PHOTO_ARRAY;
    JSONArray FEATURE_ARRAY;
    JSONArray USER_ARRAY;

    RecyclerView feature_recyclerview;
    LinearLayout linear_installment,linear_negotiable,linear_mechanic,linear_duty_paid,linear_test_drive,linear_car_history;
    Features_array_adapter itemArrayAdapter;
    int success,new_enq_success,user_id_result;
    String usr_fname ,user_phone_result, usremail_result;
    Boolean logged_in=false;
    ArrayAdapter<CharSequence> adapter_report_ad;

//    Button bt_likes,bt_report_ad;

    //this are the variables for the dialog to the enquiry
    String phone_no,email,offer_price,custom_message,your_name,sign_code,user_id,confirm_phone_number,sms_code;
    EditText ed_phone_no,ed_email_adress,ed_offer_price,ed_custom_message,ed_your_name,ed_confirm_phone_number, ed_enter_code;
    CardView card_make_enquiry,card_make_confirm_phono_no,card_confirm_code,card_enquiry_sucess;


    Context context;
    FloatingActionButton fab_call_seller,fab_message_seller;
    ImageButton bt_likes,bt_likes_2;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.car_details);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.sell_my_car_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });

        pDialog =  new SpotsDialog(this, R.style.fetching);
        context=CarDetails.this;
        databaseHelper=new DatabaseHelper(this);

        urlJsonArry=new Config().GARISAFI_API+"/select_images.php";
        ad_id= getIntent().getExtras().getInt("ad_id");
        ad_user_id= getIntent().getExtras().getString("ad_user_id");
        tv_dt_price=(TextView) findViewById(R.id.dt_price);
        tv_dt_make=(TextView)findViewById(R.id.dt_make);
        tv_dt_year=(TextView)findViewById(R.id.dt_year);
        tv_dt_city=(TextView)findViewById(R.id.dt_city);
        tv_dt_installments=(TextView)findViewById(R.id.dt_installments);
        tv_dt_mileage=(TextView)findViewById(R.id.dt_mileage);
        tv_dt_fueltype=(TextView)findViewById(R.id.dt_fuel_type);
        linear_installment=(LinearLayout) findViewById(R.id.linear_installment);
        linear_negotiable=(LinearLayout) findViewById(R.id.linear_negotiable);
        linear_mechanic=(LinearLayout) findViewById(R.id.linear_mechanic);
        linear_car_history=(LinearLayout) findViewById(R.id.linear_car_history);
        linear_test_drive=(LinearLayout) findViewById(R.id.linear_test_drive);
        linear_duty_paid=(LinearLayout) findViewById(R.id.linear_duty_paid);


         bt_likes=(ImageButton) findViewById(R.id.bt_likes);
        bt_likes_2=(ImageButton) findViewById(R.id.bt_likes_2);
//        bt_report_ad=(Button) findViewById(R.id.bt_report_ad);
        et_negotiable=(TextView)findViewById(R.id.et_negotiable);


        tv_car_condition=(TextView)findViewById(R.id.et_condition);
        tv_transmition=(TextView)findViewById(R.id.et_transmission);
        et_mechanic_inspection=(TextView)findViewById(R.id.et_mechanic_inspection);
        et_car_history=(TextView)findViewById(R.id.et_car_history);
        et_test_drive=(TextView)findViewById(R.id.et_test_drive);
        posted_by=(TextView)findViewById(R.id.posted_by);
        et_engine_size=(TextView)findViewById(R.id.et_engine_size);
        et_duty_paid=(TextView) findViewById(R.id.et_duty_paid);




        itemArrayAdapter = new Features_array_adapter(R.layout.car_features_ryview_row, itemList);
        feature_recyclerview = (RecyclerView) findViewById(R.id.recyclerview_features);
        feature_recyclerview.setLayoutManager(new LinearLayoutManager(this));
        feature_recyclerview.setItemAnimator(new DefaultItemAnimator());
        feature_recyclerview.setAdapter(itemArrayAdapter);
//
//        floatingActionMenu=(FloatingActionMenu) findViewById(R.id.floating_menu);
//        fab_call_seller=(FloatingActionButton) findViewById(R.id.fab_call_seller);
//        fab_message_seller=(FloatingActionButton) findViewById(R.id.fab_message_seller);
//
//
//
//        floatingActionMenu.setIconAnimated(false);
//        floatingActionMenu.getMenuIconView().setImageResource(R.drawable.phone_msg);
        fecth_user_details();


        select_car_details(Integer.toString(ad_id));

    }

    public void like_a_car(View view){

        //if logged in

        if(logged_in) {


            if(is_car_liked()) {
                new LikeACar(this,Integer.toString(ad_id),user_id,0);

                    bt_likes.setImageResource(R.drawable.favorite_border);
                    bt_likes_2.setImageResource(R.drawable.favorite_border);
                    final int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        bt_likes.setBackgroundDrawable(ContextCompat.getDrawable(context,R.drawable.my_round_button) );

                    } else {
                        bt_likes.setBackground(ContextCompat.getDrawable(context,R.drawable.my_round_button));
                        bt_likes_2.setBackground(ContextCompat.getDrawable(context,R.drawable.my_round_button));
                    }

            }else {

                new LikeACar(this,Integer.toString(ad_id),user_id,1);

                    bt_likes.setImageResource(R.drawable.favorite_full);
                    bt_likes_2.setImageResource(R.drawable.favorite_full);

                    final int sdk = android.os.Build.VERSION.SDK_INT;
                    if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                        bt_likes.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.my_round_button_liked) );
                        bt_likes_2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.my_round_button_liked) );
                    } else {
                        bt_likes.setBackground(ContextCompat.getDrawable(context, R.drawable.my_round_button_liked));
                        bt_likes_2.setBackground(ContextCompat.getDrawable(context, R.drawable.my_round_button_liked));
                    }
            }

        }else {

            Toast.makeText(context, "log in to like a car", Toast.LENGTH_LONG).show();

        }

    }



    public  void  fecth_user_details(){

        try {
            Cursor cus = databaseHelper.select_user();
            if (cus.getCount() == 0) {

                user_id="0";
                logged_in=false;
            }else  {
                while (cus.moveToNext()){
                    user_id=cus.getString(0);
                    local_phone_no=cus.getString(1);
                    local_user_name=cus.getString(2);
                    local_email_address=cus.getString(3);

                }
                logged_in=true;

            }
        } catch (SQLiteException se) {

            Log.e(getClass().getSimpleName(), "Open the database"+se.getMessage().toString());
        }


    }

    public  boolean  is_car_liked(){

        try {
            Cursor cus = databaseHelper.is_car_liked(ad_id,user_id);
            if (cus.getCount() == 0) {
                return false;

            }else  {

              return true;

            }
        } catch (SQLiteException se) {

            Log.e(getClass().getSimpleName(), "Open the database"+se.getMessage().toString());
            return false;
        }


    }


    public void make_enquiries (View view){

        fecth_user_details();
       // Toast.makeText(context,"This "+user_id+" "+ad_user_id,Toast.LENGTH_LONG).show();
            show_dialog_contact_seller();


    }

    public void report_ad(View view){
        fecth_user_details();
        show_dialog_report_ad();

    }

    public void contact_seller(View view){
        Intent intent = new Intent(Intent.ACTION_DIAL);
        intent.setData(Uri.parse("tel:"+defaultcontact));
        startActivity(intent);
    }

    public  void share_this_car(View view){

        try {
            Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
            sharingIntent.setType("text/plain");
            String shareBody = "Toyota prado 2017\nwww.garisafi.com";
            sharingIntent.putExtra(android.content.Intent.EXTRA_SUBJECT, "Toyota prado 2017");
            sharingIntent.putExtra(android.content.Intent.EXTRA_TEXT, shareBody);
            startActivity(Intent.createChooser(sharingIntent, "Share this car via"));

        }catch (Exception e){
            Toast.makeText(context,"An error occured while trying to share this car",Toast.LENGTH_LONG).show();

        }
    }




    public void display_info(String car_make,String car_model,String car_year,String fuel_type, String car_condition,String transimision_mode,
                             String location,String mileage, String price,String installments,String negotiable,
                             String mechanic_inspection,String car_history,String test_drive){

        String edited_price=price.split("\\.", 2)[0];
        tv_dt_price.setText("Ksh "+doubleToStringNoDecimal(Double.parseDouble(edited_price)));
        tv_dt_make.setText(""+unversal_treated_string(car_make)+" "+unversal_treated_string(car_model));
        tv_dt_year.setText(""+unversal_treated_string(car_year));
        tv_dt_city.setText(""+unversal_treated_string(location));
        if(Integer.parseInt(installments)>0){

            tv_dt_installments.setText("Installments allowed");

        }else {

            linear_installment.setVisibility(View.GONE);


        }

        if(Integer.parseInt(negotiable)>0){

            et_negotiable.setText("Negotiable");

        }else {

            linear_negotiable.setVisibility(View.GONE);


        }

        if(Integer.parseInt(duty_paid)>0){

            et_duty_paid.setText("Duty paid");

        }else {

            linear_duty_paid.setVisibility(View.GONE);


        }



        tv_dt_mileage.setText(doubleToStringNoDecimal(Double.parseDouble(unversal_treated_string(mileage)))+" Km ");
        tv_dt_fueltype.setText(""+unversal_treated_string(fuel_type));
        tv_car_condition.setText(""+unversal_treated_string(car_condition));
        tv_transmition.setText(""+unversal_treated_string(transimision_mode));
        if(unversal_treated_string_Zero(engine_size).contentEquals("0")){
            et_engine_size.setVisibility(View.GONE);

        }else {
            et_engine_size.setText("" + doubleToStringNoDecimal(Double.parseDouble(unversal_treated_string_Zero(engine_size)))+" cc");
        }



//identify the if the user is logged in and the checking if they had liked this particular car

        if(logged_in) {

            if(is_car_liked()) {
                bt_likes.setImageResource(R.drawable.favorite_full);
                bt_likes_2.setImageResource(R.drawable.favorite_full);


                final int sdk = android.os.Build.VERSION.SDK_INT;
                if(sdk < android.os.Build.VERSION_CODES.JELLY_BEAN) {
                    bt_likes.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.my_round_button_liked) );
                    bt_likes_2.setBackgroundDrawable(ContextCompat.getDrawable(context, R.drawable.my_round_button_liked) );
                } else {
                    bt_likes.setBackground(ContextCompat.getDrawable(context, R.drawable.my_round_button_liked));
                    bt_likes_2.setBackground(ContextCompat.getDrawable(context, R.drawable.my_round_button_liked));
                }

            }else {

            }

        }

        if(Integer.parseInt(mechanic_inspection)>0){

            et_mechanic_inspection.setText("Mechanic inspection");

        }else {

            linear_mechanic.setVisibility(View.GONE);


        }

        if(Integer.parseInt(car_history)>0){

            et_car_history.setText("Car History");

        }else {

            linear_car_history.setVisibility(View.GONE);


        }


        if(Integer.parseInt(test_drive)>0){

            et_test_drive.setText("Test drive");

        }else {

            linear_test_drive.setVisibility(View.GONE);


        }
    }



    private void init_car_photos() {

        viewPager = (ViewPager) findViewById(R.id.viewPager_ad_images);
        viewPager.setAdapter(new AdapterDetailedImages(CarDetails.this,image_array));
        CircleIndicator indicator = (CircleIndicator) findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
    }


    public void select_car_details(final  String ad_id){

        pDialog.show();
        String REQUEST_TAG = "CAR_DETAILS";
        urlJsonArry=new Config().GARISAFI_API+"/select_car_details.php";


        StringRequest car_detail_request = new StringRequest(Request.Method.POST,urlJsonArry,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {



                            try {
                                // Parsing json array response
                                // loop through each json object

                                JSONObject jsonObject = new JSONObject(response);
                                JSONObject MOTHER_LIST = jsonObject.getJSONObject("MOTHER_LIST");
                                AD_ARRAY=MOTHER_LIST.getJSONArray("AD_LIST");
                                PHOTO_ARRAY=MOTHER_LIST.getJSONArray("PHOTO_LIST");
                                FEATURE_ARRAY=MOTHER_LIST.getJSONArray("FEATURE_LIST");
                                USER_ARRAY=MOTHER_LIST.getJSONArray("USER_LIST");


                                //FEATURE_LIST

                                if(AD_ARRAY.length()>0) {

                                    for (int i = 0; i < AD_ARRAY.length(); i++) {
                                        JSONObject person = (JSONObject) AD_ARRAY.get(i);
                                        idtbl_ads = person.getString("idtbl_ads");

                                        if (Integer.parseInt(idtbl_ads) != -1) {


                                        car_make = person.getString("car_make");
                                        car_model = person.getString("car_model");
                                        car_year = person.getString("car_year");
                                        location = person.getString("location");
                                        currency = person.getString("currency");
                                        fuel_type = person.getString("fuel_type");
                                        car_condition = person.getString("car_condition");
                                        transimision_mode = person.getString("transimision_mode");
                                        mileage = person.getString("mileage");
                                        price = person.getString("price");
                                        negotiable = person.getString("negotiable");
                                        installments = person.getString("installments");
                                        mechanic_allowed = person.getString("mechanic_allowed");
                                        car_history = person.getString("car_history");
                                        test_drive = person.getString("test_drive");
                                        upload_date = person.getString("upload_date");
                                        duty_paid= person.getString("duty_paid");
                                        engine_size= person.getString("engine_size");


                                    }


                                    }

                                    if (Integer.parseInt(idtbl_ads) != -1) {
                                        display_info(car_make, car_model, car_year, fuel_type, car_condition, transimision_mode, location, mileage, price,
                                                installments, negotiable,mechanic_allowed,car_history,test_drive);
                                    }
                                }


                                if(PHOTO_ARRAY.length()>0) {
                                    for (int i = 0; i < PHOTO_ARRAY.length(); i++) {
                                        JSONObject person = (JSONObject) PHOTO_ARRAY.get(i);
                                        image_name = person.getString("imagename");
                                        image_array.add(image_name);
                                        Log.d("result", "" + image_name);


                                    }
                                    init_car_photos();
                                }


                                if(FEATURE_ARRAY.length()>0) {

                                    for (int i = 0; i < FEATURE_ARRAY.length(); i++) {
                                        JSONObject person = (JSONObject) FEATURE_ARRAY.get(i);
                                        String feature_name = person.getString("featurename");
                                        if(feature_name.contentEquals("")){

                                        }else {
                                            itemList.add(new FeaturesItem(" " + feature_name));
                                        }
                                        Log.d("result", "" + feature_name);
                                    }
                                    itemArrayAdapter.notifyDataSetChanged();
                                }

                                if(USER_ARRAY.length()>0) {

                                    for (int i = 0; i < USER_ARRAY.length(); i++) {
                                        JSONObject person = (JSONObject) USER_ARRAY.get(i);
                                         idtbl_usrac=person.getString("idtbl_usrac");
                                         usrfname= person.getString("usrfname");
                                         company_name= person.getString("company_name");
                                         mobileno_one= person.getString("mobileno_one");
                                         defaultcontact= person.getString("defaultcontact");
                                         usremail= person.getString("usremail");


                                    }
                                    if(company_name.contentEquals("")|| company_name==null|| company_name.contentEquals("NULL")){
                                        seller_name=usr_fname;
                                    }else {
                                        seller_name=company_name;
                                    }



                                    //2018-03-29 07:48:42

                                    posted_by.setText("Posted "+cooking_time(upload_date));

                                }





                            } catch (JSONException e) {
                                e.printStackTrace();
                                Log.e("SQL",""+e.getMessage().toString());

                            }catch (SQLiteException e){
                                Log.e("SQL",""+e.getMessage().toString());

                            }

                        pDialog.dismiss();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String message = null;
                if (volleyError instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection! and try again";
                    Toast.makeText(getApplicationContext(),""+message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                    Toast.makeText(getApplicationContext(),""+message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection! and try again";
                    Toast.makeText(getApplicationContext(),""+message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof ParseError) {
                    message = "Parsing error! Please try again ";
                    Toast.makeText(getApplicationContext(),""+message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection! and try again";
                    Toast.makeText(getApplicationContext(),""+message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof TimeoutError) {
                    message = "Connection TimeOut! try again";
                    Toast.makeText(getApplicationContext(),""+message, Toast.LENGTH_LONG).show();
                }
                pDialog.dismiss();
            }
        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("ad_id", ad_id);

                return params;
            }

        };

        // Adding String request to request queue
        VolleyAppSingleton.getInstance(CarDetails.this).addToRequestQueue(car_detail_request, REQUEST_TAG);
    }




    public void make_online_enquiries(final String phone_no,final String email_adress,
    final String offer_price,final String custom_message, final String ad_id,final String your_name){
        pDialog.show();
    //  Toast.makeText(CarDetails.this,"Invalid "+ad_user_id, Toast.LENGTH_LONG).show();
        String REQUEST_TAG = "CONTACT_SELLER";
        urlJsonArry=new Config().GARISAFI_API+"/make_enquiries.php";

        StringRequest user_login_req = new StringRequest(Request.Method.POST,urlJsonArry,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            // Parsing json array response
                            // loop through each json objectr
                         //   Toast.makeText(CarDetails.this,"Invalid "+response, Toast.LENGTH_LONG).show();

                            JSONArray jsonarray = new JSONArray(response.toString());
                            for (int i = 0; i < jsonarray.length(); i++) {

                                JSONObject person = (JSONObject) jsonarray.get(i);
                                success= person.getInt("success");
                                user_id= person.getString("user_id");

                            }


                            if(success==1){
                                if(logged_in){

                                    card_make_enquiry.setVisibility(View.GONE);
                                    card_make_confirm_phono_no.setVisibility(View.GONE);
                                    card_confirm_code.setVisibility(View.GONE);
                                    card_enquiry_sucess.setVisibility(View.VISIBLE);

                                }else {
                                    card_make_enquiry.setVisibility(View.GONE);
                                    card_make_confirm_phono_no.setVisibility(View.GONE);
                                    card_confirm_code.setVisibility(View.VISIBLE);
                                    card_enquiry_sucess.setVisibility(View.GONE);

                                }


                            }else {
                                Toast.makeText(context,"An  error occurred sending sms"+response, Toast.LENGTH_LONG).show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(context, "Error: " , Toast.LENGTH_LONG).show();
                        }
                        pDialog.dismiss();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String message = null;
                if (volleyError instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection! and try again";
                    Toast.makeText(CarDetails.this,""+message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                    Toast.makeText(CarDetails.this,""+message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection! and try again";
                    Toast.makeText(CarDetails.this,""+message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof ParseError) {
                    message = "Parsing error! Please try again ";
                    Toast.makeText(CarDetails.this,""+message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection! and try again";
                    Toast.makeText(CarDetails.this,""+message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof TimeoutError) {
                    message = "Connection TimeOut! try again";
                    Toast.makeText(CarDetails.this,""+message, Toast.LENGTH_LONG).show();
                }
                pDialog.dismiss();
            }
        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("ad_user_id",treated_string_to_ints(ad_user_id ));
                params.put("phone_no", unversal_treated_string(phone_no));
                params.put("email_adress", unversal_treated_string(email_adress));
                params.put("offer_price", unversal_treated_string(offer_price));
                params.put("custom_message", unversal_treated_string(custom_message));
                params.put("ad_id",treated_string_to_ints(ad_id));
                params.put("your_name", unversal_treated_string(your_name));
                params.put("idtbl_ads",treated_string_to_ints(idtbl_ads));
                params.put("my_user_id",treated_string_to_ints(user_id));

                return params;
            }

        };

        // Adding String request to request queue
        VolleyAppSingleton.getInstance(CarDetails.this).addToRequestQueue(user_login_req, REQUEST_TAG);
    }


    public void show_dialog_contact_seller() {

        Button btn_enquire,confirm_phone,submit_code,ok_btn;




        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        final android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.contact_seller, null);
        dialogBuilder.setView(dialogView);
        btn_enquire = (Button) dialogView.findViewById(R.id.btn_enquire);
        confirm_phone = (Button) dialogView.findViewById(R.id.confirm_phone);
        submit_code = (Button) dialogView.findViewById(R.id.submit_code);
        ok_btn = (Button) dialogView.findViewById(R.id.ok_btn);
        //  dialogBuilder.setTitle("Select login if you already have an account");

        ed_your_name= (EditText) dialogView.findViewById(R.id.ed_your_name);
        ed_phone_no= (EditText) dialogView.findViewById(R.id.phone_number);
        ed_email_adress= (EditText) dialogView.findViewById(R.id.email_adress);
        ed_offer_price= (EditText) dialogView.findViewById(R.id.et_offer_price);
        ed_custom_message= (EditText) dialogView.findViewById(R.id.custom_message);
        ed_confirm_phone_number=(EditText) dialogView.findViewById(R.id.confirm_phone_number);
        ed_enter_code=(EditText) dialogView.findViewById(R.id.enter_code);


        card_make_enquiry=(CardView) dialogView.findViewById(R.id.card_make_enquiry);
        card_make_confirm_phono_no=(CardView) dialogView.findViewById(R.id.card_make_confirm_phono_no);
        card_confirm_code=(CardView) dialogView.findViewById(R.id.card_confirm_code);
        card_enquiry_sucess=(CardView) dialogView.findViewById(R.id.card_enquiry_sucess);

        if(logged_in==true){
            ed_your_name.setText(""+local_user_name);
            ed_phone_no.setText(""+local_phone_no);
            ed_email_adress.setText(""+local_email_address);

        }




        dialogBuilder.setCancelable(true);
        final android.support.v7.app.AlertDialog b = dialogBuilder.create();
        b.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        btn_enquire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //b.dismiss();
                //upload_car();


                phone_no=ed_phone_no.getText().toString();
                email=ed_email_adress.getText().toString();
                offer_price=ed_offer_price.getText().toString();
                custom_message=ed_custom_message.getText().toString();
                your_name=ed_your_name.getText().toString();

                if (TextUtils.isEmpty(phone_no)) {
                    ed_phone_no.setError("Please your mobile number");
                    return;

                }

                if (TextUtils.isEmpty(email)) {
                    ed_email_adress.setError("Please your email address");
                    return;

                }

                if(!isValidMobile(phone_no)){
                    ed_phone_no.setError("Not valid phone number");
                    return;
                }

                if(!isValidMail(email)){
                    ed_email_adress.setError("Not valid email");
                    return;
                }



                if(logged_in){
                   // Toast.makeText(context,"this",Toast.LENGTH_LONG).show();

                    //if the user is logged in dont bother to ask them to confirm mobile numbers and all just post their enquiry

                    if(user_id.trim().contentEquals(ad_user_id.trim())){

                        Toast.makeText(context,"This is your ad,you cannot enquire about it",Toast.LENGTH_LONG).show();

                    }else {
                        make_online_enquiries(phone_no, email, offer_price, custom_message, Integer.toString(ad_id), your_name);
                    }


                }else {



                        card_make_enquiry.setVisibility(View.GONE);
                        card_make_confirm_phono_no.setVisibility(View.VISIBLE);
                        card_confirm_code.setVisibility(View.GONE);
                        card_enquiry_sucess.setVisibility(View.GONE);
                        ed_confirm_phone_number.setText(""+ed_phone_no.getText().toString());


                }



            }
        });


        confirm_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                phone_no=ed_confirm_phone_number.getText().toString();
                make_online_enquiries(phone_no,email,offer_price,custom_message,Integer.toString(ad_id),your_name);



            }
        });


        submit_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sms_code=ed_enter_code.getText().toString();
                phone_no=ed_confirm_phone_number.getText().toString();

                if (TextUtils.isEmpty(sms_code)) {
                    ed_enter_code.setError("Please enter the sms code sent to you");
                    return;

                }


                if(user_id.trim().contentEquals(ad_user_id.trim())){
                    b.dismiss();
                    Toast.makeText(context,"This is your ad,you cannot enquire about it",Toast.LENGTH_LONG).show();

                }else {

                    submit_my_code(sms_code, user_id, phone_no);
                }
//                card_make_enquiry.setVisibility(View.GONE);
//                card_make_confirm_phono_no.setVisibility(View.GONE);
//                card_confirm_code.setVisibility(View.GONE);
//                card_enquiry_sucess.setVisibility(View.VISIBLE);

            }
        });


        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
             b.dismiss();


            }
        });


        b.show();

    }




    public  void show_dialog_report_ad() {

        Button btn_enquire,confirm_phone,submit_code,ok_btn;
        Spinner sp_report_ad;





        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        final android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.flag_false_ad, null);
        dialogBuilder.setView(dialogView);
        btn_enquire = (Button) dialogView.findViewById(R.id.btn_enquire);
        confirm_phone = (Button) dialogView.findViewById(R.id.confirm_phone);
        submit_code = (Button) dialogView.findViewById(R.id.submit_code);
        ok_btn = (Button) dialogView.findViewById(R.id.ok_btn);
        //  dialogBuilder.setTitle("Select login if you already have an account");

        ed_your_name= (EditText) dialogView.findViewById(R.id.ed_your_name);
        ed_phone_no= (EditText) dialogView.findViewById(R.id.phone_number);
        ed_email_adress= (EditText) dialogView.findViewById(R.id.email_adress);
        ed_offer_price= (EditText) dialogView.findViewById(R.id.et_offer_price);
        ed_custom_message= (EditText) dialogView.findViewById(R.id.custom_message);
        ed_confirm_phone_number=(EditText) dialogView.findViewById(R.id.confirm_phone_number);
        ed_enter_code=(EditText) dialogView.findViewById(R.id.enter_code);
        sp_report_ad=(Spinner) dialogView.findViewById(R.id.spinner_reporting_reason);


        card_make_enquiry=(CardView) dialogView.findViewById(R.id.card_make_enquiry);
        card_make_confirm_phono_no=(CardView) dialogView.findViewById(R.id.card_make_confirm_phono_no);
        card_confirm_code=(CardView) dialogView.findViewById(R.id.card_confirm_code);
        card_enquiry_sucess=(CardView) dialogView.findViewById(R.id.card_enquiry_sucess);

        if(logged_in==true){
            ed_your_name.setText(""+local_user_name);
            ed_phone_no.setText(""+local_phone_no);
            ed_email_adress.setText(""+local_email_address);

        }

        adapter_report_ad= ArrayAdapter.createFromResource(this,  R.array.report_ad, R.layout.spinner_item);
        adapter_report_ad.setDropDownViewResource( R.layout.spinner_item);
        sp_report_ad.setAdapter(adapter_report_ad);




        dialogBuilder.setCancelable(true);
        final android.support.v7.app.AlertDialog b = dialogBuilder.create();
        b.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        btn_enquire.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
                //upload_car();

                flag_this_ad();
            }
        });


        confirm_phone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                phone_no=ed_confirm_phone_number.getText().toString();
                make_online_enquiries(phone_no,email,offer_price,custom_message,Integer.toString(ad_id),your_name);



            }
        });


        submit_code.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                sms_code=ed_enter_code.getText().toString();
                phone_no=ed_confirm_phone_number.getText().toString();

                if (TextUtils.isEmpty(sms_code)) {
                    ed_enter_code.setError("Please enter the sms code sent to you");
                    return;

                }

                    submit_my_code(sms_code, user_id, phone_no);

//                card_make_enquiry.setVisibility(View.GONE);
//                card_make_confirm_phono_no.setVisibility(View.GONE);
//                card_confirm_code.setVisibility(View.GONE);
//                card_enquiry_sucess.setVisibility(View.VISIBLE);


            }
        });


        ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();


            }
        });


        b.show();

    }



    public void submit_my_code(final String confirmation_code,final String user_id,final String confirm_phone_number){

        pDialog.show();
        urlJsonArry=new Config().GARISAFI_API+"/confirm_sms_code.php";
        String REQUEST_TAG = "CONFIRM_CODE";
      //  Toast.makeText(context, "Code: "+confirmation_code+" "+user_id, Toast.LENGTH_LONG).show();


        StringRequest signup_req = new StringRequest(Request.Method.POST,urlJsonArry,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    //    Toast.makeText(context, "Error: "+response, Toast.LENGTH_LONG).show();


                        try {
                            // Parsing json array response
                            // loop through each json object

                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject person = (JSONObject) jsonArray.get(i);
                                new_enq_success = person.getInt("success");
                                user_id_result = person.getInt("user_id");
                                user_phone_result= person.getString("mobileno_one");
                                usr_fname= person.getString("usrfname");
                                usremail_result= person.getString("usremail");

                            }

                            if(new_enq_success==1) {

                                databaseHelper.insertUser(user_id_result,user_phone_result,usr_fname,usremail_result);
                                card_make_enquiry.setVisibility(View.GONE);
                                card_make_confirm_phono_no.setVisibility(View.GONE);
                                card_confirm_code.setVisibility(View.GONE);
                                card_enquiry_sucess.setVisibility(View.VISIBLE);


                            } else  {
                                Toast.makeText(context,"An  error occurred during sign up", Toast.LENGTH_LONG).show();

                            }



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
                    message = "Cannot connect to Internet...Please check your connection! and try again";
                    Toast.makeText(context,""+message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                    Toast.makeText(context,""+message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection! and try again";
                    Toast.makeText(context,""+message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof ParseError) {
                    message = "Parsing error! Please try again ";
                    Toast.makeText(context,""+message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection! and try again";
                    Toast.makeText(context,""+message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof TimeoutError) {
                    message = "Connection TimeOut! try again";
                    Toast.makeText(context,""+message, Toast.LENGTH_LONG).show();
                }
                pDialog.dismiss();
            }
        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id", user_id);
                params.put("code", confirmation_code);
                params.put("phone_no", confirm_phone_number);


                return params;
            }

        };

        // Adding String request to request queue
        VolleyAppSingleton.getInstance(context).addToRequestQueue(signup_req, REQUEST_TAG);
    }

    public void flag_this_ad(){

        pDialog.show();
        urlJsonArry=new Config().GARISAFI_API+"/report_ad.php";
        String REQUEST_TAG = "FLAG_AD";


        StringRequest signup_req = new StringRequest(Request.Method.POST,urlJsonArry,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.contentEquals("1")){
                            Toast.makeText(context, "Thank you for reporting, An investigation will soon be initiated", Toast.LENGTH_LONG).show();

                        }else {
                            Toast.makeText(context, "An error occurred while sending the report, please try again", Toast.LENGTH_LONG).show();
                        }




                        pDialog.dismiss();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String message = null;
                if (volleyError instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection! and try again";
                    Toast.makeText(context,""+message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                    Toast.makeText(context,""+message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection! and try again";
                    Toast.makeText(context,""+message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof ParseError) {
                    message = "Parsing error! Please try again ";
                    Toast.makeText(context,""+message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection! and try again";
                    Toast.makeText(context,""+message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof TimeoutError) {
                    message = "Connection TimeOut! try again";
                    Toast.makeText(context,""+message, Toast.LENGTH_LONG).show();
                }
                pDialog.dismiss();
            }
        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("ad_id", treated_string_to_ints(Integer.toString(ad_id)));


                return params;
            }

        };

        // Adding String request to request queue
        VolleyAppSingleton.getInstance(context).addToRequestQueue(signup_req, REQUEST_TAG);
    }






    public static String treated_string_to_ints(String s) {

        if (TextUtils.isEmpty(s)) {
            return "0";
        }else {
            return s;
        }

    }

    public String get_android_id(){
        String android_id="";
        android_id = Settings.Secure.getString(context.getContentResolver(), Settings.Secure.ANDROID_ID);
        return android_id;

    }





}
