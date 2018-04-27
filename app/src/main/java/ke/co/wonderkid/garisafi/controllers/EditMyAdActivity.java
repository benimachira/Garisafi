package ke.co.wonderkid.garisafi.controllers;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.Typeface;
import android.graphics.drawable.ColorDrawable;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
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

import java.util.HashMap;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import ke.co.wonderkid.garisafi.R;
import ke.co.wonderkid.garisafi.utils.Config;
import ke.co.wonderkid.garisafi.utils.VolleyAppSingleton;

import static ke.co.wonderkid.garisafi.utils.UniversalMethods.doubleToStringNoDecimal;
import static ke.co.wonderkid.garisafi.utils.UniversalMethods.unversal_treated_string;

public class EditMyAdActivity extends AppCompatActivity {
    Spinner spinner_car_condition,
            spinner_contact_mode;

    EditText editText_price,editText_mileage;
    Button bt_upload;
    CheckBox checkBox_installments,checkBox_mechanic,checkBox_car_history,checkBox_negotiable,checkBox_test_drive;

    ArrayAdapter<CharSequence> adapter_car_condition,adapter_contact_mode;
    DatabaseHelper databaseHelper;
    ProgressDialog pDialog;
    String urlJsonArry=new Config().GARISAFI_API+"/upload_car_details.php";
//
//    String transimition_mode,car_condition,exterior_color;
//    String price,mileage;
//    String mechanic_allowed,car_history,negotiable,test_drive="";
    String car_condition,contact_mode,negotiable,mechanic_allowed,car_history,test_drive,installments,mileage,price;

//    int exterior_color = person.getInt("tbl_contactmode_idtbl_contactmode");
//
//    int negotiable = person.getInt("negotiable");
//    int mechanic_allowed = person.getInt("mechanic_allowed");
//    int car_history = person.getInt("car_history");
//    int test_drive = person.getInt("test_drive");



    int PERMISIONS_READ_EXTERNAL_STORAGE=1;



    Button dialog_cancel,dialog_ok_btn,cancel_upload,try_again_upload;
    TextView tv_counter,txtPercentage,tv_fails,tv_visual_progress;
    ProgressBar progressBar;
    AlertDialog upload_dialog;
    private AlertDialog progressDialog;
    LinearLayout linearLayout_dialog_progress,linearLayout_dialog_done,linearLayout_dialog_fail;
    CardView user_details_card;
    int user_id,success,ad_id;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.edit_my_car);
        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.edit_ad_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });



        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        databaseHelper=new DatabaseHelper(this);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISIONS_READ_EXTERNAL_STORAGE);
            }
        }


        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);


        spinner_car_condition= (Spinner) findViewById(R.id.spinner_condition);
        spinner_contact_mode= (Spinner) findViewById(R.id.spinner_exterior_color);
        editText_price= (EditText) findViewById(R.id.et_price);
        editText_mileage= (EditText) findViewById(R.id.et_mileage);


        bt_upload=(Button) findViewById(R.id.button_button);

        user_details_card =(CardView) findViewById(R.id.user_details_card);

        checkBox_installments=(CheckBox) findViewById(R.id.checkBox_installments);
        checkBox_mechanic=(CheckBox) findViewById(R.id.checkBox_mechanic);
        checkBox_car_history=(CheckBox) findViewById(R.id.checkBox_history);
        checkBox_negotiable=(CheckBox) findViewById(R.id.checkBox_negotiable);
        checkBox_test_drive=(CheckBox) findViewById(R.id.checkBox_testdrive);


        txtPercentage=(TextView) findViewById(R.id.txtPercentage);
        tv_visual_progress=(TextView) findViewById(R.id.txtPercentage);


        progressDialog = new SpotsDialog(this, R.style.uploading_ad_dialog);


        final AlertDialog.Builder dialogBuilder = new AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_boss_layout, null);
        dialog_cancel = (Button) dialogView.findViewById(R.id.cancel);


        dialog_ok_btn = (Button) dialogView.findViewById(R.id.ok_proceed);
        cancel_upload= (Button) dialogView.findViewById(R.id.ok_cancel_upload);
        try_again_upload= (Button) dialogView.findViewById(R.id.ok_again);



        tv_counter = (TextView) dialogView.findViewById(R.id.tv_counter);
        progressBar= (ProgressBar) dialogView.findViewById(R.id.progressBar);
        txtPercentage = (TextView) dialogView.findViewById(R.id.txtPercentage);
        tv_fails = (TextView) dialogView.findViewById(R.id.message);
        linearLayout_dialog_progress=(LinearLayout) dialogView.findViewById(R.id.ln_dialog_progress) ;
        linearLayout_dialog_done=(LinearLayout) dialogView.findViewById(R.id.ln_dialog_done) ;
        linearLayout_dialog_fail=(LinearLayout) dialogView.findViewById(R.id.ln_dialog_fail) ;
        linearLayout_dialog_progress.setVisibility(View.VISIBLE);
        linearLayout_dialog_done.setVisibility(View.GONE);
        linearLayout_dialog_fail.setVisibility(View.GONE);

        dialogBuilder.setView(dialogView);
        dialogBuilder.setCancelable(false);
        upload_dialog = dialogBuilder.create();
        upload_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

        ad_id=getIntent().getExtras().getInt("ad_id",0);
        car_condition=getIntent().getExtras().getString("car_condition","0");
        contact_mode=getIntent().getExtras().getString("exterior_color","0");
        negotiable=getIntent().getExtras().getString("negotiable","0");
        mechanic_allowed=getIntent().getExtras().getString("mechanic_allowed","0");
        car_history=getIntent().getExtras().getString("car_history","0");
        test_drive=getIntent().getExtras().getString("test_drive","0");
        installments=getIntent().getExtras().getString("installments","0");
        mileage=getIntent().getExtras().getString("mileage","0");
        price=getIntent().getExtras().getString("price","0");

        editText_mileage.setText(""+doubleToStringNoDecimal(Integer.parseInt(mileage)));
        editText_price.setText(""+doubleToStringNoDecimal(Integer.parseInt(price)));

        check_checkBox(Integer.parseInt(installments),Integer.parseInt(mechanic_allowed),Integer.parseInt(car_history),
                Integer.parseInt(negotiable),Integer.parseInt(test_drive));

        set_views_fonts();




        try {
            Cursor cus = databaseHelper.select_user();
            if (cus.getCount() == 0) {
            }else  {

                while (cus.moveToNext()){
                    user_id=cus.getInt(0);

                }

            }
        } catch (SQLiteException se) {
            Log.e(getClass().getSimpleName(), "Open the database");
        }








        dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                upload_dialog.dismiss();
            }
        });

        dialog_ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload_dialog.dismiss();
                finish();
                startActivity(getIntent());
            }
        });

        cancel_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload_dialog.dismiss();

            }
        });


        try_again_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                upload_dialog.dismiss();

            }
        });

        adapter_car_condition = ArrayAdapter.createFromResource(this,  R.array.car_condition,  R.layout.spinner_item);
        adapter_car_condition.setDropDownViewResource( R.layout.spinner_item);
        spinner_car_condition.setAdapter(adapter_car_condition);
        spinner_car_condition.setSelection(Integer.parseInt(car_condition));


        adapter_contact_mode = ArrayAdapter.createFromResource(this,  R.array.exterior_color,  R.layout.spinner_item);
        adapter_contact_mode.setDropDownViewResource( R.layout.spinner_item);
        spinner_contact_mode.setAdapter(adapter_contact_mode);
        spinner_contact_mode.setSelection(Integer.parseInt(contact_mode));



        spinner_car_condition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                ((TextView) parent.getChildAt(0)).setTextSize(13);

                car_condition=Integer.toString(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinner_contact_mode.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                ((TextView) parent.getChildAt(0)).setTextSize(13);

                contact_mode=Integer.toString(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        checkBox_installments.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //checked
                    installments="1";

                } else {
                    //not checked
                    installments="0";

                }
            }
        });

        checkBox_mechanic.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //checked
                    mechanic_allowed="1";

                } else {
                    //not checked
                    mechanic_allowed="0";

                }
            }
        });
        checkBox_car_history.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //checked
                    car_history="1";

                } else {
                    //not checked
                    car_history="0";

                }
            }
        });

        checkBox_negotiable.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //checked
                    negotiable="1";

                } else {
                    //not checked
                    negotiable="0";

                }
            }
        });

        checkBox_test_drive.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //checked
                    test_drive="1";

                } else {
                    //not checked
                    test_drive="0";

                }
            }
        });

        editText_price.addTextChangedListener(new NumberTextWatcherForThousand(editText_price));
        editText_mileage.addTextChangedListener(new NumberTextWatcherForThousand(editText_mileage));
    }





    private  void set_views_fonts(){



        Typeface black_typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Black.ttf");
        Typeface bold_typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Bold.ttf");
        Typeface regular_typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Regular.ttf");
        Typeface medium_typeface = Typeface.createFromAsset(getAssets(), "fonts/Roboto-Medium.ttf");

        //edittext
        editText_price.setTypeface(medium_typeface);
        editText_mileage.setTypeface(medium_typeface);

        //textviews
        txtPercentage.setTypeface(regular_typeface);
//        tv_visual_progress.setTypeface(medium_typeface);
        tv_counter .setTypeface(regular_typeface);
        txtPercentage.setTypeface(regular_typeface);
        tv_fails.setTypeface(regular_typeface);





        //buttons
        bt_upload.setTypeface(regular_typeface);
        dialog_cancel.setTypeface(regular_typeface);
        dialog_ok_btn.setTypeface(regular_typeface);
        cancel_upload.setTypeface(regular_typeface);
        try_again_upload.setTypeface(regular_typeface);



        //checkbox

        checkBox_installments.setTypeface(regular_typeface);
        checkBox_mechanic.setTypeface(regular_typeface);
        checkBox_car_history.setTypeface(regular_typeface);
        checkBox_negotiable.setTypeface(regular_typeface);
        checkBox_test_drive.setTypeface(regular_typeface);


        //header lables






    }


    public  void check_checkBox(int installment,int mechanic, int car_history, int negotiable, int test_drive){

        if(installment==1){
            checkBox_installments.setChecked(true);
        }
        if(mechanic==1){
            checkBox_mechanic.setChecked(true);
        }
        if(car_history==1){
            checkBox_negotiable.setChecked(true);
        }
        if(negotiable==1){
            checkBox_negotiable.setChecked(true);
        }
        if(test_drive==1){
            checkBox_test_drive.setChecked(true);
        }

    }




    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {

        if (event.getAction() == MotionEvent.ACTION_DOWN) {

            /**
             * It gets into the above IF-BLOCK if anywhere the screen is touched.
             */

            View v = getCurrentFocus();
            if ( v instanceof EditText) {


                /**
                 * Now, it gets into the above IF-BLOCK if an EditText is already in focus, and you tap somewhere else
                 * to take the focus away from that particular EditText. It could have 2 cases after tapping:
                 * 1. No EditText has focus
                 * 2. Focus is just shifted to the other EditText
                 */

                Rect outRect = new Rect();
                v.getGlobalVisibleRect(outRect);
                if (!outRect.contains((int)event.getRawX(), (int)event.getRawY())) {
                    v.clearFocus();
                    InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(v.getWindowToken(), 0);
                }
            }
        }
        return super.dispatchTouchEvent( event );
    }






    public  void upload_car(View view){
        collect_car_details();

    }

    public  void collect_car_details(){
        price=NumberTextWatcherForThousand.trimCommaOfString(editText_price.getText().toString());
        mileage=NumberTextWatcherForThousand.trimCommaOfString(editText_mileage.getText().toString());


        if (TextUtils.isEmpty(price)) {
            editText_price.setError("Please Enter Car Price");
            editText_price.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText_price, InputMethodManager.SHOW_IMPLICIT);
            return;
        }

            update_car_details();

    }





    public void update_car_details(){

        progressDialog.show();
        String REQUEST_TAG = "UPDATE_CAR";
        urlJsonArry=new Config().GARISAFI_API+"/update_car_details.php";
        StringRequest upload_car = new StringRequest(Request.Method.POST,urlJsonArry,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.contentEquals("YES")){

                            Toast.makeText(EditMyAdActivity.this, "Ad Details successfully updated", Toast.LENGTH_LONG).show();


                        }else if(response.contentEquals("NO")){
                            Toast.makeText(EditMyAdActivity.this, "Ad Details failed to update"+response, Toast.LENGTH_LONG).show();


                        }else {
                            Toast.makeText(EditMyAdActivity.this, "Ad Details successfully update"+response, Toast.LENGTH_LONG).show();

                        }

                        progressDialog.dismiss();

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
                progressDialog.dismiss();
            }
        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("car_condition", unversal_treated_string( car_condition));
                params.put("exterior_color",  unversal_treated_string(contact_mode));
                params.put("price",  unversal_treated_string(price));
                params.put("mileage",  unversal_treated_string(mileage));
                params.put("installments",  unversal_treated_string(installments));
                params.put("mechanic_allowed",  unversal_treated_string(mechanic_allowed));
                params.put("car_history",  unversal_treated_string(car_history));
                params.put("negotiable",  unversal_treated_string(negotiable));
                params.put("test_drive",  unversal_treated_string(test_drive));
                params.put("user_id",  Integer.toString(user_id));
                params.put("ad_id",  Integer.toString(ad_id));


                return params;

            }

        };



        // Adding String request to request queue
        VolleyAppSingleton.getInstance(EditMyAdActivity.this).addToRequestQueue(upload_car, REQUEST_TAG);
    }
}
