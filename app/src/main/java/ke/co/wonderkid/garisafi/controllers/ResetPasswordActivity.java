package ke.co.wonderkid.garisafi.controllers;

import android.Manifest;
import android.app.Activity;
import android.app.ProgressDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.LocalBroadcastManager;
import android.support.v7.app.AppCompatActivity;
import android.telephony.SmsMessage;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;

import ke.co.wonderkid.garisafi.R;
import ke.co.wonderkid.garisafi.utils.Config;
import ke.co.wonderkid.garisafi.utils.VolleyAppSingleton;

public class ResetPasswordActivity extends AppCompatActivity {
    ProgressDialog pDialog;
    private String urlJsonArry = "";


    int success,user_id;
    String sign_phone_no,sign_code,password1,password2,username_new,usr_fname,usremail;
    EditText signup_confirm_no,et_signup_code,ed_pass1,ed_pass2;
    DatabaseHelper dbhelper;
    int activity_id;

    LinearLayout layout_confirm,signup_main,layout_confirm_code,layout_create_passoword;
    private final int MY_PERMISSIONS_MULTIPLE=1;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        dbhelper = new DatabaseHelper(this);
        pDialog = new ProgressDialog(ResetPasswordActivity.this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        activity_id = getIntent().getExtras().getInt("activity_id");


        if (Build.VERSION.SDK_INT < 23) {
            //Do not need to check the permission
        } else {
            if (checkAndRequestPermissions()) {
                //If you have already permitted the permission
            }
        }

            setContentView(R.layout.reset_password);


        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.reset_password_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });

        signup_confirm_no= (EditText) findViewById(R.id.ed_phone_confirm_reset);
            et_signup_code= (EditText) findViewById(R.id.ed_code_confirm);
            ed_pass1= (EditText) findViewById(R.id.ed_pass_word1);
            ed_pass2= (EditText) findViewById(R.id.ed_pass_word2);

            layout_confirm = (LinearLayout) findViewById(R.id.layout_confirm);
            signup_main= (LinearLayout) findViewById(R.id.id_signup_main);
            layout_confirm_code= (LinearLayout) findViewById(R.id.layout_code);
            layout_create_passoword=(LinearLayout) findViewById(R.id.layout_create_password);

            layout_confirm.setVisibility(View.VISIBLE);
            layout_confirm_code.setVisibility(View.GONE);
            layout_create_passoword.setVisibility(View.GONE);
            urlJsonArry=new Config().GARISAFI_API+"/sign_up.php";


        Log.i("cs.fsu", "smsActivity : onResume");

        Intent intent = getIntent();
        Bundle bundle = intent.getBundleExtra("mySMS");

        if (bundle != null) {
            Object[] pdus = (Object[])bundle.get("pdus");
            SmsMessage sms = SmsMessage.createFromPdu((byte[])pdus[0]);
            Log.i("cs.fsu", "smsActivity : SMS is <" +  sms.getMessageBody() +">");

            //strip flag
            String message = sms.getMessageBody();
            while (message.contains("FLAG"))
                message = message.replace("FLAG", "");

        } else
            Log.i("cs.fsu", "smsActivity : NULL SMS bundle");


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
        finish();
    }


    private BroadcastReceiver receiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equalsIgnoreCase("otp")) {
                final String message = intent.getStringExtra("message");
                //Do whatever you want with the code here
                Log.d("messages",message);
              //  Toast.makeText(context,"boom"+message,Toast.LENGTH_LONG).show();
            }
        }
    };

    public void sign_up_submit (View view){
        sign_phone_no=signup_confirm_no.getText().toString();

        if (TextUtils.isEmpty(sign_phone_no)) {
            signup_confirm_no.setError("Please enter a password");
            return;

        }

        if(!isValidMobile(sign_phone_no)){
            return;
        }

        confirm_user_phone(sign_phone_no);

    }

    public void sign_up_code_submit (View view){
        submit_my_code(Integer.toString(user_id));

    }

    public void reset_password (View view){
        Intent intent=new Intent(this,ResetPasswordActivity.class);
        intent.putExtra("choice",3);
        startActivity(intent);
        finish();


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
                signup_confirm_no.setError("Not Valid Number");
            } else {
                check = true;
            }
        } else {
            check=false;
        }
        return check;
    }


    public void confirm_user_phone(final String phone_number){

        pDialog.show();
        urlJsonArry=new Config().GARISAFI_API+"/confirm_number_reset_password.php";
        String REQUEST_TAG = "CONFIRM_PHONE_RESET";

        StringRequest signup_req = new StringRequest(Request.Method.POST,urlJsonArry,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                    //    Toast.makeText(ResetPasswordActivity.this,"Error: "+response , Toast.LENGTH_LONG).show();

                        try {
                            // Parsing json array response
                            // loop through each json object
                            JSONArray jsonarray = new JSONArray(response.toString());
                            for (int i = 0; i < jsonarray.length(); i++) {

                                JSONObject person = (JSONObject) jsonarray.get(i);
                                success= person.getInt("success");
                                user_id= person.getInt("user_id");

                            }


                            if(success==1) {
                                layout_confirm.setVisibility(View.GONE);
                                layout_confirm_code.setVisibility(View.VISIBLE);
                                Toast.makeText(ResetPasswordActivity.this,"a text has been sent to you", Toast.LENGTH_LONG).show();

                            } else {
                                Toast.makeText(ResetPasswordActivity.this,"An  error occurred during sign up try again", Toast.LENGTH_LONG).show();

                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ResetPasswordActivity.this,"Error: " , Toast.LENGTH_LONG).show();
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
                params.put("phone_no", phone_number);

                return params;
            }

        };

        // Adding String request to request queue
        VolleyAppSingleton.getInstance(ResetPasswordActivity.this).addToRequestQueue(signup_req, REQUEST_TAG);
    }




    public void submit_my_code(final String home_user_id){

        pDialog.show();
        urlJsonArry=new Config().GARISAFI_API+"/confirm_code.php";
        String REQUEST_TAG = "CONFIRM_CODE";
        sign_code=et_signup_code.getText().toString();
        //Toast.makeText(ResetPasswordActivity.this, "Code: "+sign_code+" "+home_user_id+" "+user_id, Toast.LENGTH_LONG).show();


        StringRequest signup_req = new StringRequest(Request.Method.POST,urlJsonArry,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        //Toast.makeText(ResetPasswordActivity.this, "Error: "+response, Toast.LENGTH_LONG).show();

                        if(response.toString().contains("NO")) {
                            Toast.makeText(ResetPasswordActivity.this,"An  error occurred during sign up", Toast.LENGTH_LONG).show();

                        } else if(response.toString().contains("YES")) {
                            layout_confirm.setVisibility(View.GONE);
                            layout_confirm_code.setVisibility(View.GONE);
                            layout_create_passoword.setVisibility(View.VISIBLE);
                            Toast.makeText(ResetPasswordActivity.this,"Mobile no confirmed", Toast.LENGTH_LONG).show();

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
        VolleyAppSingleton.getInstance(ResetPasswordActivity.this).addToRequestQueue(signup_req, REQUEST_TAG);
    }


    public void create_password(final String local_user_id){

        pDialog.show();
        urlJsonArry=new Config().GARISAFI_API+"/create_new_password.php";
        String REQUEST_TAG = "CREATE_PASSWORD";

        StringRequest signup_req = new StringRequest(Request.Method.POST,urlJsonArry,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // Toast.makeText(ResetPasswordActivity.this,"An "+response, Toast.LENGTH_LONG).show();

                        try {
                            // Parsing json array response
                            // loop through each json object
                            JSONArray jsonarray = new JSONArray(response.toString());
                            for (int i = 0; i < jsonarray.length(); i++) {

                                JSONObject person = (JSONObject) jsonarray.get(i);
                                success= person.getInt("success");
                                user_id= person.getInt("user_id");
                                username_new= person.getString("mobileno_one");
                                usr_fname = person.getString("usrfname");
                                usremail = person.getString("usremail");


                            }


                            if(success==0) {
                                Toast.makeText(ResetPasswordActivity.this,"An  error occurred during sign up", Toast.LENGTH_LONG).show();

                            } else if(success==1){
                                dbhelper.insertUser(user_id,username_new,usr_fname,usremail);
                                showConfirimDialog();
                            }

                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(ResetPasswordActivity.this, "Error: " , Toast.LENGTH_LONG).show();
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
        VolleyAppSingleton.getInstance(ResetPasswordActivity.this).addToRequestQueue(signup_req, REQUEST_TAG);
    }



    public void showConfirimDialog() {
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        Button btn_continue;
        TextView tv_title,tv_message;
        final android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.layout_registration_done, null);

        btn_continue = (Button) dialogView.findViewById(R.id.ok_btn_code);
        tv_title = (TextView) dialogView.findViewById(R.id.title_ok);
        tv_message = (TextView) dialogView.findViewById(R.id.message);
        tv_title.setText("Successfully reset");
        tv_message.setText("Your password has been successfully changed please proceed to login");


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
                    Userlogin.user_login_activity.finish();
                    Intent intent=new Intent(ResetPasswordActivity.this,UserProfile.class);
                    startActivity(intent);
                    finish();
                }
                if(activity_id==2){
//
//                    b.dismiss();
//                    Userlogin.user_login_activity.finish();
//                    Intent intent=new Intent(ResetPasswordActivity.this,UserProfile.class);
//                    startActivity(intent);
//                    finish();

                    b.dismiss();
                    Userlogin.user_login_activity.finish();
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
