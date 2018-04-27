package ke.co.wonderkid.garisafi.controllers;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.database.sqlite.SQLiteException;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import ke.co.wonderkid.garisafi.R;
import ke.co.wonderkid.garisafi.utils.Config;
import ke.co.wonderkid.garisafi.utils.VolleyAppSingleton;

import static ke.co.wonderkid.garisafi.utils.UniversalMethods.isValidMail;

public class Userlogin extends AppCompatActivity {
    ProgressDialog pDialog;
    private String urlJsonArry = "";

    String login_returned_username,usr_fname,usremail;
    int login_returned_id;



    int success,user_id,message,user_id_holder;
    String username,password,username_new,sign_phone_no,business_name,email_adress,sign_code,password1,password2;
    EditText ed_user_name,ed_password,ed_phone_no,signup_biz_name,editText_email,signup_confirm_no,et_signup_code,ed_pass1,ed_pass2;
    DatabaseHelper dbhelper;
    int login_choice;
    int activity_id;
    int activity_restarded=0;
    LinearLayout layout_confirm,signup_main,layout_confirm_code,layout_create_passoword;
    private final int MY_PERMISSIONS_MULTIPLE=1;
    public static Activity user_login_activity;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbhelper = new DatabaseHelper(this);
        pDialog = new ProgressDialog(Userlogin.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        user_login_activity=this;

            login_choice = getIntent().getExtras().getInt("choice");
            activity_id = getIntent().getExtras().getInt("activity_id");



        if (Build.VERSION.SDK_INT < 23) {
            //Do not need to check the permission
        } else {
            if (checkAndRequestPermissions()) {
                //If you have already permitted the permission
            }
        }


        if (login_choice == 1) {
            set_content_login();


            } else if (login_choice == 2) {

            set_content_signup();


            }



    }

    public void set_content_login(){

        setContentView(R.layout.login);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.login_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });

        ed_user_name = (EditText) findViewById(R.id.login_username);
        ed_password = (EditText) findViewById(R.id.login_password);
        urlJsonArry=new Config().GARISAFI_API+"/login.php";
        set_login_views_fonts();


        if(activity_id==2) {
            String user_phone = getIntent().getExtras().getString("user_phone_number");
            ed_user_name.setText(""+user_phone);
        }





    }

    public void set_content_signup(){


        setContentView(R.layout.sign_up);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.sign_up_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });

        ed_user_name = (EditText) findViewById(R.id.signup_username);
        ed_password = (EditText) findViewById(R.id.signup_email);
        ed_phone_no = (EditText) findViewById(R.id.signup_phone_no);
        signup_biz_name= (EditText) findViewById(R.id.signup_biz_name);
        editText_email= (EditText) findViewById(R.id.signup_email);
        signup_confirm_no= (EditText) findViewById(R.id.ed_phone_confirm_again);
        et_signup_code= (EditText) findViewById(R.id.ed_code_confirm);
        ed_pass1= (EditText) findViewById(R.id.ed_pass_word1);
        ed_pass2= (EditText) findViewById(R.id.ed_pass_word2);



        layout_confirm = (LinearLayout) findViewById(R.id.layout_confirm);
        signup_main= (LinearLayout) findViewById(R.id.id_signup_main);
        layout_confirm_code= (LinearLayout) findViewById(R.id.layout_code);
        layout_create_passoword=(LinearLayout) findViewById(R.id.layout_create_password);


        if(activity_id==2) {
            String user_phone=getIntent().getExtras().getString("user_phone_number");
            String name=getIntent().getExtras().getString("name");
            String email =getIntent().getExtras().getString("email");

            ed_phone_no.setText(user_phone);
            ed_user_name.setText(name);
            editText_email.setText(email);
        }


        layout_confirm.setVisibility(View.GONE);
        signup_main.setVisibility(View.VISIBLE);
        layout_confirm_code.setVisibility(View.GONE);
        layout_create_passoword.setVisibility(View.GONE);
        urlJsonArry=new Config().GARISAFI_API+"/sign_up.php";
        set_signup_views_fonts();
    }


    private  void set_login_views_fonts() {


        Typeface black_typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Black.ttf");
        Typeface bold_typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");
        Typeface regular_typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        Typeface medium_typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Medium.ttf");

        ed_user_name.setTypeface(medium_typeface);
        ed_password.setTypeface(medium_typeface);
    }

    private  void set_signup_views_fonts() {


        Typeface black_typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Black.ttf");
        Typeface bold_typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");
        Typeface regular_typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        Typeface medium_typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Medium.ttf");

        ed_user_name.setTypeface(medium_typeface);
        ed_password.setTypeface(medium_typeface);
        ed_phone_no.setTypeface(medium_typeface);
        signup_biz_name.setTypeface(medium_typeface);
        editText_email.setTypeface(medium_typeface);
        signup_confirm_no.setTypeface(medium_typeface);
        et_signup_code.setTypeface(medium_typeface);
        ed_pass1.setTypeface(medium_typeface);
        ed_pass2.setTypeface(medium_typeface);

    }


        @Override
    public void onResume() {
        LocalBroadcastManager.getInstance(this).registerReceiver(receiver, new IntentFilter("otp"));
        super.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        LocalBroadcastManager.getInstance(this).unregisterReceiver(receiver);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        this.finish();
    }


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");
                //Do whatever you want with the code here
                Log.d("messages",message);
                //Toast.makeText(context,"boom"+message,Toast.LENGTH_LONG).show();
            }
        }
    };

    public void sign_up (View view){
        username=ed_user_name.getText().toString();
        sign_phone_no=ed_phone_no.getText().toString();
        business_name=signup_biz_name.getText().toString();
        email_adress=editText_email.getText().toString();
        if(!isValidMobile(sign_phone_no)){
            ed_phone_no.setError("Not valid number");
            return;
        }
        if(!isValidMail(email_adress)){
            editText_email.setError("Not valid email");
            return;
        }

        signup_main.setVisibility(View.GONE);
        layout_confirm.setVisibility(View.VISIBLE);
        signup_confirm_no.setText(""+sign_phone_no);


    }
    public void user_login (View view){
        username=ed_user_name.getText().toString();
        password=ed_password.getText().toString();


        if(!isValidMobile(username) && !isValidMail(username)){
            ed_user_name.setError("Not valid phone number or email");
            return;
        }

        if (TextUtils.isEmpty(password)) {
            ed_password.setError("Please enter a password");
            return;

        }

        login_the_user();

    }



    public void sign_up_submit (View view){

        sign_up_the_user();
      //  confirm_user_phone(Integer.toString(user_id));

    }

    public void sign_up_code_submit (View view){
        submit_my_code(Integer.toString(user_id));

    }

    public void reset_password (View view){

        Intent intent=new Intent(this,ResetPasswordActivity.class);
        intent.putExtra("activity_id",activity_id);
        startActivity(intent);


    }

    public void no_account(View view){
        set_content_signup();

    }


    public void create_password (View view){
        password1=ed_pass1.getText().toString().trim();
        password2=ed_pass2.getText().toString().trim();

        if (TextUtils.isEmpty(password1)) {
            ed_pass1.setError("Please enter a password");
            return;

        }

        if (TextUtils.isEmpty(password2)) {
            ed_pass2.setError("Please confirm your password");
            return;

        }


        if(password1.equals(password2)){
        }else {
            ed_pass2.setText("");
            ed_pass2.setError("Your passwords don't match");
            return;
        }

        create_password(Integer.toString(user_id));

    }



    private boolean isValidMobile(String phone) {
        boolean check=false;
        if(!Pattern.matches("[a-zA-Z]+", phone)) {
            if(phone.length() < 10 || phone.length() > 12) {
                // if(phone.length() != 10) {
                check = false;
            } else {
                check = true;
            }
        } else {
            check=false;
        }
        return check;
    }


    public void sign_up_the_user(){

    pDialog.show();
    String REQUEST_TAG = "SIGN_UP";



        StringRequest signup_req = new StringRequest(Request.Method.POST,urlJsonArry,
            new Response.Listener<String>() {
                @Override
                public void onResponse(String response) {
                    try {
                        // Parsing json array response
                        // loop through each json objectr
                    //   Toast.makeText(Userlogin.this, "Error: "+response, Toast.LENGTH_LONG).show();


                        JSONArray jsonarray = new JSONArray(response.toString());
                        for (int i = 0; i < jsonarray.length(); i++) {

                            JSONObject person = (JSONObject) jsonarray.get(i);
                            success= person.getInt("success");
                            user_id= person.getInt("user_id");
                            username_new= person.getString("mobileno_one");

                        }


                        if(success==0) {
                            Toast.makeText(Userlogin.this,"An  error occurred during sign up", Toast.LENGTH_LONG).show();

                        } else if(success==1){
                            
                            layout_confirm.setVisibility(View.GONE);
                            signup_main.setVisibility(View.GONE);
                            layout_confirm_code.setVisibility(View.VISIBLE);
                         //   Toast.makeText(Userlogin.this,"Signed up successfully", Toast.LENGTH_LONG).show();

                        }else if(success==3) {
//                            signup_main.setVisibility(View.GONE);
//                            layout_confirm.setVisibility(View.GONE);
//                            signup_confirm_no.setText(""+sign_phone_no);

                            set_content_login();

                            Toast.makeText(Userlogin.this,"You are already signed up, try logging in", Toast.LENGTH_LONG).show();
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        Toast.makeText(Userlogin.this, "Error: " , Toast.LENGTH_LONG).show();
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
            params.put("user_name", username);
            params.put("phone_no", sign_phone_no);
            params.put("business_name", business_name);
            params.put("email", email_adress);

            return params;
        }

    };

    // Adding String request to request queue
        VolleyAppSingleton.getInstance(Userlogin.this).addToRequestQueue(signup_req, REQUEST_TAG);
}




    public void login_the_user(){

        pDialog.show();
        String REQUEST_TAG = "LOGIN";

        StringRequest user_login_req = new StringRequest(Request.Method.POST,urlJsonArry,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                      //  Toast.makeText(Userlogin.this,"Invalid "+response, Toast.LENGTH_LONG).show();
                        try {
                            // Parsing json array response
                            // loop through each json object
                            JSONArray jsonarray = new JSONArray(response.toString());
                            for (int i = 0; i < jsonarray.length(); i++) {


                                JSONObject person = (JSONObject) jsonarray.get(i);
                                success= person.getInt("success");
                                login_returned_id= person.getInt("user_id");
                                login_returned_username= person.getString("mobileno_one");
                                usr_fname = person.getString("usrfname");
                                usremail = person.getString("usremail");

                            }


                            if(success==0) {
                                Toast.makeText(Userlogin.this,"Invalid user name or password", Toast.LENGTH_LONG).show();

                            } else if(success==1){

                                Toast.makeText(Userlogin.this,"Welcome to Garisafi.com", Toast.LENGTH_LONG).show();
                                dbhelper.insertUser(login_returned_id,login_returned_username,usr_fname,usremail);


                                if(activity_id==1) {
                                    Intent i = new Intent(Userlogin.this, UserProfile.class);
                                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                                    startActivity(i);
                                    finish();
                                }else if(activity_id==2){

                                    Intent returnIntent = new Intent();
                                    returnIntent.putExtra("result",1);
                                    returnIntent.putExtra("result_id",1);
                                    setResult(Activity.RESULT_OK,returnIntent);
                                    finish();

                                }else {
                                    finish();
                                }


                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Userlogin.this, "Error: "+e.getMessage().toString() , Toast.LENGTH_LONG).show();
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
                params.put("user_name", username);
                params.put("password", password);
                return params;
            }

        };

        // Adding String request to request queue
        VolleyAppSingleton.getInstance(Userlogin.this).addToRequestQueue(user_login_req, REQUEST_TAG);
    }




    public void submit_my_code(final String home_user_id){

        pDialog.show();
        urlJsonArry=new Config().GARISAFI_API+"/confirm_sms_code.php";
        String REQUEST_TAG = "CONFIRM_CODE";
        sign_code=et_signup_code.getText().toString();
       // Toast.makeText(Userlogin.this, "Code: "+sign_code, Toast.LENGTH_LONG).show();


        StringRequest signup_req = new StringRequest(Request.Method.POST,urlJsonArry,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    //    Toast.makeText(Userlogin.this, "Error: "+response, Toast.LENGTH_LONG).show();


                        try {
                            // Parsing json array response
                            // loop through each json object

                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject person = (JSONObject) jsonArray.get(i);
                                user_id_holder = person.getInt("success");

                            }

                            if(user_id_holder==1) {

                                layout_confirm.setVisibility(View.GONE);
                                signup_main.setVisibility(View.GONE);
                                layout_confirm_code.setVisibility(View.GONE);
                                layout_create_passoword.setVisibility(View.VISIBLE);
                                Toast.makeText(Userlogin.this,"Mobile no confirmed", Toast.LENGTH_LONG).show();

                            } else  {
                                Toast.makeText(Userlogin.this,"An  error occurred during sign up", Toast.LENGTH_LONG).show();

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
                params.put("user_id", home_user_id);
                params.put("code", sign_code);


                return params;
            }

        };

        // Adding String request to request queue
        VolleyAppSingleton.getInstance(Userlogin.this).addToRequestQueue(signup_req, REQUEST_TAG);
    }


    public void create_password(final String local_user_id){
       // Toast.makeText(getApplicationContext(),""+activity_id, Toast.LENGTH_LONG).show();


        pDialog.show();
        urlJsonArry=new Config().GARISAFI_API+"/create_new_password.php";
        String REQUEST_TAG = "CREATE_PASSWORD";

        StringRequest signup_req = new StringRequest(Request.Method.POST,urlJsonArry,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(Userlogin.this,"wooooo "+response, Toast.LENGTH_LONG).show();

                        try {
                            // Parsing json array response
                            // loop through each json object
                            JSONArray jsonarray = new JSONArray(response.toString());
                            for (int i = 0; i < jsonarray.length(); i++) {

                                JSONObject person = (JSONObject) jsonarray.get(i);
                                success= person.getInt("success");
                                login_returned_id= person.getInt("user_id");
                                login_returned_username= person.getString("mobileno_one");
                                usr_fname = person.getString("usrfname");
                                usremail = person.getString("usremail");



                            }





                            if(success==0) {
                                Toast.makeText(Userlogin.this,"An  error occurred during sign up", Toast.LENGTH_LONG).show();

                            } else if(success==1){

                                dbhelper.insertUser(login_returned_id,login_returned_username,usr_fname,usremail);
                                showConfirimDialog();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(Userlogin.this, "Error: "+e.getMessage().toString(), Toast.LENGTH_LONG).show();
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
                params.put("password", password1);
                params.put("user_id", local_user_id);

                return params;
            }

        };

        // Adding String request to request queue
        VolleyAppSingleton.getInstance(Userlogin.this).addToRequestQueue(signup_req, REQUEST_TAG);
    }



    public void showConfirimDialog() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Button btn_continue;
        final android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.layout_registration_done, null);

        btn_continue = (Button) dialogView.findViewById(R.id.ok_btn_code);

        dialogBuilder.setView(dialogView);
        //  dialogBuilder.setTitle("Select login if you already have an account");
        dialogBuilder.setCancelable(false);
        final android.support.v7.app.AlertDialog b = dialogBuilder.create();
        b.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));


        btn_continue.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                if(activity_id==1) {
                    b.dismiss();
                    Intent i = new Intent(Userlogin.this, UserProfile.class);
                    i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
                    startActivity(i);
                    finish();
                }
                if(activity_id==2){

                    Intent returnIntent = new Intent();
                    returnIntent.putExtra("result",1);
                    returnIntent.putExtra("result_id",2);
                    setResult(Activity.RESULT_OK,returnIntent);
                    finish();

                }
            }
        });
        b.show();

    }


    private boolean checkAndRequestPermissions() {
        int recieve_sms = ContextCompat.checkSelfPermission(this, Manifest.permission.RECEIVE_SMS);
        int read_sms = ContextCompat.checkSelfPermission(this, Manifest.permission.READ_SMS);
        int send_sms = ContextCompat.checkSelfPermission(this, Manifest.permission.SEND_SMS);



        List<String> listPermissionsNeeded = new ArrayList<>();
        if (recieve_sms != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.RECEIVE_SMS);
        }
        if (read_sms != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.READ_SMS);
        }

        if (send_sms != PackageManager.PERMISSION_GRANTED) {
            listPermissionsNeeded.add(Manifest.permission.SEND_SMS);
        }

        if (!listPermissionsNeeded.isEmpty()) {
            ActivityCompat.requestPermissions(this, listPermissionsNeeded.toArray(new String[listPermissionsNeeded.size()]), MY_PERMISSIONS_MULTIPLE);
            return false;
        }

        return true;
    }


    @Override    public void onRequestPermissionsResult(int requestCode, String permissions[], int[] grantResults) {
        switch (requestCode) {
            case MY_PERMISSIONS_MULTIPLE:
                if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    //Permission Granted Successfully. Write working code here.
                } else {
                    //You did not accept the request can not use the functionality.
                }
                break;
        }
    }

}
