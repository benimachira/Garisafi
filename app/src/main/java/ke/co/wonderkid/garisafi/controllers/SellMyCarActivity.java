package ke.co.wonderkid.garisafi.controllers;

import android.Manifest;
import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.TypedArray;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.graphics.Rect;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.StrictMode;
import android.provider.MediaStore;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.GridView;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
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

import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.mime.content.FileBody;
import org.apache.http.entity.mime.content.StringBody;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import dmax.dialog.SpotsDialog;
import droidninja.filepicker.FilePickerBuilder;
import droidninja.filepicker.FilePickerConst;
import ke.co.wonderkid.garisafi.R;
import ke.co.wonderkid.garisafi.models.SelectImageModel;
import ke.co.wonderkid.garisafi.utils.Config;
import ke.co.wonderkid.garisafi.utils.VolleyAppSingleton;
import me.relex.circleindicator.CircleIndicator;

import static ke.co.wonderkid.garisafi.controllers.MainActivity.MAKE_MODEL_PREFS;
import static ke.co.wonderkid.garisafi.utils.ResizeImageToCache.resizeAndCompressImageBeforeSend;
import static ke.co.wonderkid.garisafi.utils.UniversalMethods.unversal_treated_string;
import static ke.co.wonderkid.garisafi.utils.UniversalMethods.unversal_treated_string_Zero;

public class SellMyCarActivity extends AppCompatActivity {
    Spinner spinner_make,spinner_model,spinner_fuel,spinner_transimition,spinner_car_condition,
            spinner_exterior_color,spinner_location,spinner_year,spinner_body_type;

    EditText editText_price,editText_mileage,editText_name,editText_phone,editText_email,et_engine_size;
    Button bt_upload,btn_preview,btn_select_images;
    CheckBox checkBox_1_1,checkBox_1_2,checkBox_1_3,checkBox_1_4,checkBox_1_5,checkBox_2_1,checkBox_2_2,
            checkBox_2_3,checkBox_2_4,checkBox_2_5,checkBox_2_6,checkBox_2_7,checkBox_2_8,checkBox_2_9,
            checkBox_2_10,checkBox_2_11,checkBox_3_1,checkBox_3_2,checkBox_3_3,
            checkBox_4_1,checkBox_4_2,checkBox_4_3,checkBox_4_4,checkBox_4_5,checkBox_4_6,checkBox_4_7,checkBox_4_8,
            checkBox_4_9,checkBox_4_10,checkBox_4_11,checkBox_4_12,
            checkBox_installments,checkBox_mechanic,checkBox_car_history,checkBox_duty_paid,
    checkBox_negotiable,checkBox_test_drive;


    TextView lable_car_info,lable_more_details,lable_features,your_details,lable_select_image;

    ArrayAdapter<String> adapter_make,adapter_model,adapter_year;
    ArrayAdapter<CharSequence> adapter_fuel,adapter_transimition,adapter_car_condition, adapter_exterior_color,
            adapter_location,adapter_body_type;
    List<String> make_lables,model_lables,year_lables=new ArrayList<String>();
    DatabaseHelper databaseHelper;
    ProgressDialog pDialog;
    String urlJsonArry=new Config().GARISAFI_API+"/upload_car_details.php";
    List<String> make_labels_list= new ArrayList<String>();
    List<String>  make_id_list=new ArrayList<String>();
    List<String>  model_lables_id_list= new ArrayList<String>();
    List<String>  model_lables_list= new ArrayList<String>();
    List<String>  year_list= new ArrayList<String>();
    List<String>  year_id_list= new ArrayList<String>();

    List<String>  idtbl_feature_list= new ArrayList<String>();
    List<String>  feature_group_list= new ArrayList<String>();
    List<String>  feature_name_list= new ArrayList<String>();
    List<String>  feature_safety_list= new ArrayList<String>();
    List<String>  feature_windows_list= new ArrayList<String>();
    List<String>  feature_others_list= new ArrayList<String>();
    List<String>  selected_features_id= new ArrayList<String>();

    String make_id,model_id,year,fuel,transimition_mode,car_condition,location, exterior_color,duty_paid,engine_size,body_type;
    String price,mileage,name,phone, email, username;
    String mechanic_allowed,car_history,negotiable,test_drive="";
    String installments="0";
    String preview_make,preview_model,preview_year,preview_fuel_type,preview_location,preview_istallments;

    String idtbl_feature,feature_group,feature_name;
    GridView gv;
    int PERMISIONS_READ_EXTERNAL_STORAGE=1;
    public  final int PICK_IMAGE_REQUEST=2;
    public  final int CAPTURE_IMAGE=3;
    public  final int REQUEST_CODE_SIGN_UP=4;


    Bitmap bitmap,scaled;
    Button dialog_cancel,dialog_ok_btn,cancel_upload,try_again_upload;
    TextView tv_counter,txtPercentage,tv_fails;
    ProgressBar progressBar;
    AlertDialog upload_dialog;
    private AlertDialog progressDialog;
    SelectImageAdapter selectImageAdapter;
    LinearLayout linearLayout_dialog_progress,linearLayout_dialog_done,linearLayout_dialog_fail;
    CardView user_details_card;

    int user_id,success,ad_id;
    String confirm_phone, confirm_password,user_phone_no,username_new,create_ac_user_name,create_ac_phone_no,
            create_ac_email,defaut_image;
    String pictureImagePath;

    SelectImageModel image_model;

    ArrayList<SelectImageModel> camera_photo_array=new ArrayList<>();
//    ArrayList<SelectImageModel> photo_model_array;
    public  ArrayList<String> SELECT_IMAGE_PATHS =new ArrayList<String>();
    public  ArrayList<String> SELECT_IMAGE_PATHS_TEMP;


    String filePath;


    JSONObject jsonObject;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sell_my_car);
      //  this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.sell_my_car_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });

       SharedPreferences make_model_sharedPref =this.getSharedPreferences(MAKE_MODEL_PREFS, MODE_PRIVATE);
        int strPref = make_model_sharedPref.getInt("make_model_inserted", 0);

        if (strPref ==0) {
            loading_block_Dialog();

        }



        progressBar = (ProgressBar) findViewById(R.id.progressBar);

        databaseHelper=new DatabaseHelper(this);

        if (android.os.Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[] {Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISIONS_READ_EXTERNAL_STORAGE);
            }
        }


        pDialog = new ProgressDialog(this);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);


        spinner_make = (Spinner) findViewById(R.id.spinner_make_sell);
        spinner_model = (Spinner) findViewById(R.id.spinner_model_sell);
        spinner_fuel= (Spinner) findViewById(R.id.spinner_fuel);
        spinner_transimition= (Spinner) findViewById(R.id.spinner_transimition);
        spinner_car_condition= (Spinner) findViewById(R.id.spinner_condition);
        spinner_exterior_color = (Spinner) findViewById(R.id.spinner_exterior_color);
        spinner_location= (Spinner) findViewById(R.id.spinner_location);
        spinner_year= (Spinner) findViewById(R.id.spinner_year);
        spinner_body_type= (Spinner) findViewById(R.id.spinner_body_type);

        editText_price= (EditText) findViewById(R.id.et_price);
        editText_mileage= (EditText) findViewById(R.id.et_mileage);
        editText_name= (EditText) findViewById(R.id.signup_username);
        editText_phone= (EditText) findViewById(R.id.signup_phone_no);
        editText_email= (EditText) findViewById(R.id.signup_email);
        et_engine_size= (EditText) findViewById(R.id.et_engine_size);

        bt_upload=(Button) findViewById(R.id.bt_upload);
        btn_preview=(Button) findViewById(R.id.btn_preview);
        btn_select_images=(Button) findViewById(R.id.btn_select_images);

        user_details_card =(CardView) findViewById(R.id.user_details_card);

        checkBox_1_1=(CheckBox) findViewById(R.id.checkBox_1_1);
        checkBox_1_2=(CheckBox) findViewById(R.id.checkBox_1_2);
        checkBox_1_3=(CheckBox) findViewById(R.id.checkBox_1_3);
        checkBox_1_4=(CheckBox) findViewById(R.id.checkBox_1_4);
        checkBox_1_5=(CheckBox) findViewById(R.id.checkBox_1_5);
        checkBox_2_1=(CheckBox) findViewById(R.id.checkBox_2_1);
        checkBox_2_2=(CheckBox) findViewById(R.id.checkBox_2_2);
        checkBox_2_3=(CheckBox) findViewById(R.id.checkBox_2_3);
        checkBox_2_4=(CheckBox) findViewById(R.id.checkBox_2_4);
        checkBox_2_5=(CheckBox) findViewById(R.id.checkBox_2_5);
        checkBox_2_6=(CheckBox) findViewById(R.id.checkBox_2_6);
        checkBox_2_7=(CheckBox) findViewById(R.id.checkBox_2_7);
        checkBox_2_8=(CheckBox) findViewById(R.id.checkBox_2_8);
        checkBox_2_9=(CheckBox) findViewById(R.id.checkBox_2_9);
        checkBox_2_10=(CheckBox) findViewById(R.id.checkBox_2_10);
        checkBox_2_11=(CheckBox) findViewById(R.id.checkBox_2_11);
        checkBox_3_1=(CheckBox) findViewById(R.id.checkBox_3_1);
        checkBox_3_2=(CheckBox) findViewById(R.id.checkBox_3_2);
        checkBox_3_3=(CheckBox) findViewById(R.id.checkBox_3_3);

        checkBox_4_1=(CheckBox) findViewById(R.id.checkBox_4_1);
        checkBox_4_2=(CheckBox) findViewById(R.id.checkBox_4_2);
        checkBox_4_3=(CheckBox) findViewById(R.id.checkBox_4_3);
        checkBox_4_4=(CheckBox) findViewById(R.id.checkBox_4_4);
        checkBox_4_5=(CheckBox) findViewById(R.id.checkBox_4_5);
        checkBox_4_6=(CheckBox) findViewById(R.id.checkBox_4_6);
        checkBox_4_7=(CheckBox) findViewById(R.id.checkBox_4_7);
        checkBox_4_8=(CheckBox) findViewById(R.id.checkBox_4_8);
        checkBox_4_9=(CheckBox) findViewById(R.id.checkBox_4_9);
        checkBox_4_10=(CheckBox) findViewById(R.id.checkBox_4_10);
        checkBox_4_11=(CheckBox) findViewById(R.id.checkBox_4_11);
        checkBox_4_12=(CheckBox) findViewById(R.id.checkBox_4_12);




        checkBox_installments=(CheckBox) findViewById(R.id.checkBox_installments);
        checkBox_mechanic=(CheckBox) findViewById(R.id.checkBox_mechanic);
        checkBox_car_history=(CheckBox) findViewById(R.id.checkBox_history);
        checkBox_negotiable=(CheckBox) findViewById(R.id.checkBox_negotiable);
        checkBox_test_drive=(CheckBox) findViewById(R.id.checkBox_testdrive);
        checkBox_duty_paid=(CheckBox) findViewById(R.id.checkBox_duty_paid);


        txtPercentage=(TextView) findViewById(R.id.txtPercentage);
        lable_car_info=(TextView) findViewById(R.id.lable_car_info);
        lable_more_details=(TextView) findViewById(R.id.lable_more_details);
        lable_features=(TextView) findViewById(R.id.lable_features);
        your_details=(TextView) findViewById(R.id.your_details);
        lable_select_image=(TextView) findViewById(R.id.lable_select_image);


        gv= (GridView) findViewById(R.id.gv);
        gv.setVerticalScrollBarEnabled(false);




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
        upload_dialog.getWindow().setBackgroundDrawable(new ColorDrawable(android.graphics.Color.TRANSPARENT));

        //set_views_fonts();
        try {
            Cursor c=databaseHelper.get_features_lables();
            if(c.getCount()>0) {
                while (c.moveToNext()) {
                    idtbl_feature = c.getString(0);
                    feature_group = c.getString(1);
                    feature_name = c.getString(2);

                    idtbl_feature_list.add(idtbl_feature);
                    feature_group_list.add(feature_group);
                    feature_name_list.add(feature_name);

                }
            }else {

            }
            c.close();
            databaseHelper.close();



        }catch (SQLiteException e){

        }

       //get the features for the safety checkbox
        try {
            Cursor c=databaseHelper.get_safety_lables();
            if(c.getCount()>0) {
                while (c.moveToNext()) {
                    String safety = c.getString(0);
                    feature_safety_list.add(safety);
                }
            }else {

            }
            c.close();
            databaseHelper.close();



        }catch (SQLiteException e){

        }



        // get the features for the windows checkboxes


        //get the features for the safety checkbox
        try {
            Cursor c=databaseHelper.get_windows_lables();
            if(c.getCount()>0) {
                while (c.moveToNext()) {
                    String windows = c.getString(0);
                    feature_windows_list.add(windows);
                }
            }else {

            }
            c.close();
            databaseHelper.close();



        }catch (SQLiteException e){

        }



        try {
            Cursor c=databaseHelper.get_others_lables();
            if(c.getCount()>0) {
                while (c.moveToNext()) {
                    String windows = c.getString(0);
                    feature_others_list.add(windows);
                }
            }else {

            }
            c.close();
            databaseHelper.close();



        }catch (SQLiteException e){

        }


        if(feature_safety_list.size()>0) {

            checkBox_2_1.setText(feature_safety_list.get(0));
            checkBox_2_2.setText(feature_safety_list.get(1));
            checkBox_2_3.setText(feature_safety_list.get(2));
            checkBox_2_4.setText(feature_safety_list.get(3));
            checkBox_2_5.setText(feature_safety_list.get(4));
            checkBox_2_6.setText(feature_safety_list.get(5));
            checkBox_2_7.setText(feature_safety_list.get(6));
            checkBox_2_8.setText(feature_safety_list.get(7));
            checkBox_2_9.setText(feature_safety_list.get(8));
            checkBox_2_10.setText(feature_safety_list.get(9));
            checkBox_2_11.setText(feature_safety_list.get(10));

        }


        if(feature_windows_list.size()>0) {

            checkBox_3_1.setText(feature_windows_list.get(0));
            checkBox_3_2.setText(feature_windows_list.get(1));
            checkBox_3_3.setText(feature_windows_list.get(2));

        }


        if(feature_others_list.size()>0) {

            checkBox_4_1.setText(feature_others_list.get(0));
            checkBox_4_2.setText(feature_others_list.get(1));
            checkBox_4_3.setText(feature_others_list.get(2));
            checkBox_4_4.setText(feature_others_list.get(3));
            checkBox_4_5.setText(feature_others_list.get(4));
            checkBox_4_6.setText(feature_others_list.get(5));
            checkBox_4_7.setText(feature_others_list.get(6));
            checkBox_4_8.setText(feature_others_list.get(7));
            checkBox_4_9.setText(feature_others_list.get(8));
            checkBox_4_10.setText(feature_others_list.get(9));
            checkBox_4_11.setText(feature_others_list.get(10));
            checkBox_4_12.setText(feature_others_list.get(11));

        }


        if(feature_name_list.size()>0) {

            checkBox_1_1.setText(feature_name_list.get(0));
            checkBox_1_2.setText(feature_name_list.get(1));
            checkBox_1_3.setText(feature_name_list.get(2));
            checkBox_1_4.setText(feature_name_list.get(3));
            checkBox_1_5.setText(feature_name_list.get(24));
        }



        try {
            Cursor cus = databaseHelper.select_user();
            if (cus.getCount() == 0) {
            }else  {

                while (cus.moveToNext()){
                    user_id=cus.getInt(0);
                    username=cus.getString(1);
                }
                if(user_id!=0) {
                    user_details_card.setVisibility(View.GONE);
                }
            }
        } catch (SQLiteException se) {
            Log.e(getClass().getSimpleName(), "Open the database");
        }








        //the section for uploading dialog onclicks and all
        dialog_cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                upload_dialog.dismiss();
            }
        });

        dialog_ok_btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //when all the upload is done

                upload_dialog.dismiss();
                Intent intent= new Intent(SellMyCarActivity.this,MyAdsActivity.class);
                startActivity(intent);
                finish();
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
                //try to upload the images when they failed to upload

                upload_dialog.dismiss();

            }
        });


        try {
            Cursor c=databaseHelper.get_make_lables();
            make_labels_list.add("Car make");
            make_id_list.add("0");
            while (c.moveToNext()) {
                make_id_list.add(c.getString(0)) ;
                make_labels_list.add(c.getString(1)) ;


            }
            c.close();
            databaseHelper.close();



        }catch (SQLiteException e){

        }

        try {
            Cursor c=databaseHelper.get_year_lables();
            year_list.add("Car year");
            year_id_list.add("0");
            while (c.moveToNext()) {
                year_id_list.add(c.getString(0)) ;
                year_list.add(c.getString(1)) ;


            }
            c.close();
            databaseHelper.close();



        }catch (SQLiteException e){

        }


        // Spinner_make Drop down elements
        make_lables = make_labels_list;
        adapter_make = new ArrayAdapter<String>(this,R.layout.spinner_item, make_lables);
        adapter_make.setDropDownViewResource( R.layout.spinner_item);
        spinner_make.setAdapter(adapter_make);




        // Spinner_make Drop down elements
        year_lables = year_list;
        adapter_year = new ArrayAdapter<String>(this,  R.layout.spinner_item, year_lables);
        adapter_make.setDropDownViewResource( R.layout.spinner_item);
        spinner_year.setAdapter(adapter_year);

        adapter_fuel = ArrayAdapter.createFromResource(this,  R.array.fuel,  R.layout.spinner_item);
        adapter_make.setDropDownViewResource( R.layout.spinner_item);
        spinner_fuel.setAdapter(adapter_fuel);


        adapter_transimition = ArrayAdapter.createFromResource(this,  R.array.transmission,  R.layout.spinner_item);
        adapter_make.setDropDownViewResource( R.layout.spinner_item);
        spinner_transimition.setAdapter(adapter_transimition);


        adapter_car_condition = ArrayAdapter.createFromResource(this,  R.array.car_condition,  R.layout.spinner_item);
        adapter_make.setDropDownViewResource( R.layout.spinner_item);
        spinner_car_condition.setAdapter(adapter_car_condition);

        adapter_location = ArrayAdapter.createFromResource(this,  R.array.location, R.layout.spinner_item);
        adapter_make.setDropDownViewResource( R.layout.spinner_item);
        spinner_location.setAdapter(adapter_location);


        adapter_exterior_color = ArrayAdapter.createFromResource(this,  R.array.exterior_color,  R.layout.spinner_item);
        adapter_make.setDropDownViewResource( R.layout.spinner_item);
        spinner_exterior_color.setAdapter(adapter_exterior_color);

        adapter_body_type = ArrayAdapter.createFromResource(this,  R.array.body_type,  R.layout.spinner_item);
        adapter_body_type.setDropDownViewResource( R.layout.spinner_item);
        spinner_body_type.setAdapter(adapter_body_type);



        spinner_make.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                ((TextView) parent.getChildAt(0)).setTextSize(13);
                preview_make=spinner_make.getSelectedItem().toString();
                make_id=make_id_list.get(position);

             //   Toast.makeText(SellMyCarActivity.this,"this"+preview_make+" "+make_id,Toast.LENGTH_LONG).show();

                if(model_lables!=null) {
                    model_lables.clear();
                }
                if(model_lables_list!=null) {
                    model_lables_list.clear();
                }

                try {

                    Cursor c=databaseHelper.get_model_lables(make_id);
                    model_lables_list.add("Car model");
                    model_lables_id_list.add("0");

                    if(c.getCount()==0){

                    }else {

                        while (c.moveToNext()) {

                            model_lables_id_list.add(c.getString(0));
                            model_lables_list.add(c.getString(1));


                        }
                    }

                        model_lables = model_lables_list;
                        adapter_model = new ArrayAdapter<String>(SellMyCarActivity.this, R.layout.spinner_item, model_lables);
                        adapter_make.setDropDownViewResource(R.layout.spinner_item);
                        spinner_model.setAdapter(adapter_model);

                    c.close();
                    databaseHelper.close();



                }catch (SQLiteException e){

                }




            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_model.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                ((TextView) parent.getChildAt(0)).setTextSize(13);
                preview_model=spinner_model.getSelectedItem().toString();

                try {
                    Cursor c = databaseHelper.get_model_id(preview_model.trim());
                    while (c.moveToNext()) {

                        model_id=c.getString(0);
                    }

                }catch (SQLiteException e) {

                }
             //   Toast.makeText(SellMyCarActivity.this,"this"+preview_model+" "+model_id,Toast.LENGTH_LONG).show();


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        spinner_year.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                ((TextView) parent.getChildAt(0)).setTextSize(13);
                preview_year=spinner_year.getSelectedItem().toString();

                year=year_id_list.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_fuel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                ((TextView) parent.getChildAt(0)).setTextSize(13);
                preview_fuel_type=spinner_fuel.getSelectedItem().toString();

                fuel=Integer.toString(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        spinner_transimition.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                ((TextView) parent.getChildAt(0)).setTextSize(13);

                transimition_mode=Integer.toString(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


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

        spinner_location.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                ((TextView) parent.getChildAt(0)).setTextSize(13);
                preview_location=spinner_location.getSelectedItem().toString();


                location=Integer.toString(position);

            }










            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_exterior_color.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                ((TextView) parent.getChildAt(0)).setTextSize(13);

                exterior_color =Integer.toString(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        spinner_body_type.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                ((TextView) parent.getChildAt(0)).setTextSize(13);

                body_type=Integer.toString(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        //Give all other checkboxes their specific onclick functions
        check_the_boxes();





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

        checkBox_duty_paid.setChecked(true);
        checkBox_duty_paid.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //checked
                    duty_paid="1";

                } else {
                    //not checked
                    duty_paid="0";

                }
            }
        });

        editText_price.addTextChangedListener(new NumberTextWatcherForThousand(editText_price));
        editText_mileage.addTextChangedListener(new NumberTextWatcherForThousand(editText_mileage));
        et_engine_size.addTextChangedListener(new NumberTextWatcherForThousand(et_engine_size));
    }


    public void loading_block_Dialog() {
        final AlertDialog.Builder builder;
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            builder = new AlertDialog.Builder(this, android.R.style.Theme_Material_Dialog_Alert);
        } else {
            builder = new AlertDialog.Builder(this);
        }
        final AlertDialog b = builder.create();
        builder.setTitle("Set up in progress")
                .setMessage("app set up is in progress.. please wait for a while..")
                .setPositiveButton("Dismiss", new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int which) {
                        // continue with delete
                        b.dismiss();
//
//                        Intent intent = getIntent();
//                        finish();
//                        startActivity(intent);

                        finish();


                    }
                })
                .show();

    }

    @Override
    protected void onResume() {
        super.onResume();

        try {
            Cursor cus = databaseHelper.select_user();
            if (cus.getCount() == 0) {
            }else  {

                while (cus.moveToNext()){
                    user_id=cus.getInt(0);
                    username=cus.getString(1);
                }
                if(user_id!=0) {
                    user_details_card.setVisibility(View.GONE);
                }
            }
        } catch (SQLiteException se) {
            Log.e(getClass().getSimpleName(), "Open the database");
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

    public  void select_photos(View view){

        final String [] items = new String[] {"Take Photo", "Choose From Gallery","Cancel"};
        final Integer[] icons = new Integer[] {R.drawable.camera, R.drawable.photos,R.drawable.x_mark};
        ListAdapter adapter = new ArrayAdapterWithIcon(this, items, icons);

        new AlertDialog.Builder(this).setTitle("Select Car Photos")
                .setAdapter(adapter, new DialogInterface.OnClickListener() {
                    public void onClick(DialogInterface dialog, int item ) {

                            selectImage(item,dialog);

                    }
                }).show();
//
//
//
//        final Item[] items = {
//                new Item("Take Photo", R.drawable.camera),
//                new Item("Choose From Gallery", R.drawable.photos),
//                new Item("Cancel",null),//no icon for this one
//        };
//
//        ListAdapter adapter = new ArrayAdapter<Item>(
//                this, R.layout.select_dialog_item, android.R.id.text1, items){
//            public View getView(int position, View convertView, ViewGroup parent) {
//                //Use super class to create the View
//                View v = super.getView(position, convertView, parent);
//                TextView tv = (TextView)v.findViewById(android.R.id.text1);
//
//                //Put the image on the TextView
//                tv.setCompoundDrawablesWithIntrinsicBounds( items[position].icon, 0, 0, 0);
//
//                //Add margin between image and text (support various screen densities)
//                int dp5 = (int) (5 * getResources().getDisplayMetrics().density + 0.5f);
//                tv.setCompoundDrawablePadding(5);
//
//                return v;
//            }
//        };
//



    }


    public class ArrayAdapterWithIcon extends ArrayAdapter<String> {

        private List<Integer> images;

        public ArrayAdapterWithIcon(Context context, List<String> items, List<Integer> images) {
            super(context, android.R.layout.select_dialog_item, items);
            this.images = images;
        }

        public ArrayAdapterWithIcon(Context context, String[] items, Integer[] images) {
            super(context, android.R.layout.select_dialog_item, items);
            this.images = Arrays.asList(images);
        }

        public ArrayAdapterWithIcon(Context context, int items, int images) {
            super(context, android.R.layout.select_dialog_item, (String[]) context.getResources().getTextArray(items));

            final TypedArray imgs = context.getResources().obtainTypedArray(images);
            this.images = new ArrayList<Integer>() {{ for (int i = 0; i < imgs.length(); i++) {add(imgs.getResourceId(i, -1));} }};

            // recycle the array
            imgs.recycle();
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            View view = super.getView(position, convertView, parent);
            TextView textView = (TextView) view.findViewById(android.R.id.text1);

            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN_MR1) {
                textView.setCompoundDrawablesRelativeWithIntrinsicBounds(images.get(position), 0, 0, 0);
            } else {
                textView.setCompoundDrawablesWithIntrinsicBounds(images.get(position), 0, 0, 0);
            }
            textView.setCompoundDrawablePadding(
                    (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 12, getContext().getResources().getDisplayMetrics()));
            return view;
        }

    }

    public  void preview_ad(View view){

        Toast.makeText(this, "boom"+ SELECT_IMAGE_PATHS.size(), Toast.LENGTH_SHORT).show();



        show_dialog_preview_ad();


    }





    private void selectImage(int item,DialogInterface dialog) {
        try {
            SELECT_IMAGE_PATHS_TEMP=new ArrayList<String>();
            // carry on the normal flow, as the case of  permissions  granted.

                    if (item==0) {
                        dialog.dismiss();
                        if(SELECT_IMAGE_PATHS.size()>20){

                            Toast.makeText(SellMyCarActivity.this,"You've reached the maximum limit of images",Toast.LENGTH_LONG).show();

                        }else {


                            cameraIntent();
                        }
                    } else if (item==1) {
                        dialog.dismiss();
                       // camera_photo_array.clear();
//                        SELECT_IMAGE_PATHS.clear();
                        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
                        StrictMode.setVmPolicy(builder.build());

                        FilePickerBuilder.getInstance().setMaxCount(20)
                                .setSelectedFiles(SELECT_IMAGE_PATHS_TEMP)
                                .setActivityTheme(R.style.AppTheme)
                                .pickPhoto(SellMyCarActivity.this);

                    } else if (item==2) {
                        dialog.dismiss();
                    }

        } catch (Exception e) {
            Toast.makeText(this, "Camera Permission error", Toast.LENGTH_SHORT).show();
            e.printStackTrace();
        }
    }






    public  void upload_car(View view){
        collect_car_details();

    }

    public  void collect_car_details(){
        price=NumberTextWatcherForThousand.trimCommaOfString(editText_price.getText().toString());
        mileage=NumberTextWatcherForThousand.trimCommaOfString(editText_mileage.getText().toString());
        engine_size=NumberTextWatcherForThousand.trimCommaOfString(et_engine_size.getText().toString());
        name=editText_name.getText().toString();
        phone=editText_phone.getText().toString();
        email=editText_email.getText().toString();

        try {
            jsonObject = new JSONObject();
            for (int i = 0; i < selected_features_id.size(); i++) {
                jsonObject.put("params_" + i, selected_features_id.get(i));
            }

        }catch (JSONException e){

        }

        Log.d("stri_object",""+jsonObject);



        if(make_id.contentEquals("0")){
            spinner_make.setFocusable(true);
            spinner_make.setFocusableInTouchMode(true);
            spinner_make.requestFocus();
            Toast.makeText(SellMyCarActivity.this,"Please select a make",Toast.LENGTH_LONG).show();
            return;
        }
//
//        if(model_id.contentEquals("0")){
//            spinner_model.setFocusable(true);
//            spinner_model.setFocusableInTouchMode(true);
//            spinner_model.requestFocus();
//            Toast.makeText(SellMyCarActivity.this,"Please select a model",Toast.LENGTH_LONG).show();
//            return;
//        }

        if(year.contentEquals("0")){
            spinner_year.setFocusable(true);
            spinner_year.setFocusableInTouchMode(true);
            spinner_year.requestFocus();
            Toast.makeText(SellMyCarActivity.this,"Please select the car year",Toast.LENGTH_LONG).show();
            return;
        }

        if (TextUtils.isEmpty(price)) {
            editText_price.setError("Please Enter Car Price");
            editText_price.requestFocus();
            InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.showSoftInput(editText_price, InputMethodManager.SHOW_IMPLICIT);
            return;

        }



        if(user_id==0) {

            if (TextUtils.isEmpty(name)) {
                editText_name.setError("Please Enter Your Name");
                return;

            }

            if (TextUtils.isEmpty(phone)) {
                editText_phone.setError("Please Enter Your Phone Number");
                return;

            }

            if (TextUtils.isEmpty(email)) {
                editText_email.setError("Please Enter Your Email Address");
                return;

            }
        }

        Toast.makeText(getApplicationContext(), "Image "+SELECT_IMAGE_PATHS.size(), Toast.LENGTH_LONG).show();

        if (SELECT_IMAGE_PATHS.size()<=0) {
            Toast.makeText(getApplicationContext(), "Select car images to upload ", Toast.LENGTH_LONG).show();

            return;
        }

        if(user_id==0) {
            check_usr_registered();
        }else {
            upload_car();
        }

    }




    public void upload_car(){

        progressDialog.show();
        String REQUEST_TAG = "UPLOAD_CAR";
        urlJsonArry=new Config().GARISAFI_API+"/upload_car_details.php";
        StringRequest upload_car = new StringRequest(Request.Method.POST,urlJsonArry,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       // Toast.makeText(SellMyCarActivity.this, ""+response, Toast.LENGTH_LONG).show();

                        try {
                            // Parsing json array response
                            // loop through each json object
                            JSONArray jsonarray = new JSONArray(response.toString());
                            for (int i = 0; i < jsonarray.length(); i++) {

                                JSONObject person = (JSONObject) jsonarray.get(i);
                                success= person.getInt("success");
                                ad_id= person.getInt("ad_id");


                            }


                            if(success==1) {
                                UploadFileToServer uploadFileToServer=new UploadFileToServer();
                                uploadFileToServer.execute();
                            } else if(success==0){

                                Toast.makeText(SellMyCarActivity.this, "Failed to upload", Toast.LENGTH_LONG).show();

                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(SellMyCarActivity.this, "Error: " , Toast.LENGTH_LONG).show();
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
                params.put("make", unversal_treated_string(make_id));
                params.put("model",  unversal_treated_string(model_id));
                params.put("year",  unversal_treated_string(year));
                params.put("fuel_type", unversal_treated_string( fuel));
                params.put("transimition_mode", unversal_treated_string( transimition_mode));
                params.put("car_condition", unversal_treated_string( car_condition));
                params.put("location",  unversal_treated_string(location));
                params.put("exterior_color",  unversal_treated_string(exterior_color));
                params.put("price",  unversal_treated_string(price));
                params.put("mileage",  unversal_treated_string(mileage));
                params.put("installments",  unversal_treated_string(installments));
                params.put("mechanic_allowed",  unversal_treated_string(mechanic_allowed));
                params.put("car_history",  unversal_treated_string(car_history));
                params.put("negotiable",  unversal_treated_string(negotiable));
                params.put("test_drive",  unversal_treated_string(test_drive));
                params.put("user_id",  Integer.toString(user_id));
                params.put("sellersnote", "My car is new");
                params.put("default_image",  unversal_treated_string(defaut_image));

                params.put("duty_paid",  unversal_treated_string_Zero(duty_paid));
                params.put("engine_size",  unversal_treated_string_Zero(engine_size));
                params.put("body_type",  unversal_treated_string_Zero(body_type));
                params.put("params",jsonObject.toString());

                return params;

            }

        };



        // Adding String request to request queue
        VolleyAppSingleton.getInstance(SellMyCarActivity.this).addToRequestQueue(upload_car, REQUEST_TAG);
    }



    public void check_usr_registered(){

//        progressDialog.setMessage("Uploading your ad, please wait..");

        progressDialog.show();
        String REQUEST_TAG = "IS_USR_REGISTERED";
        urlJsonArry=new Config().GARISAFI_API+"/check_user_registered.php";

        StringRequest check_usr_registered = new StringRequest(Request.Method.POST,urlJsonArry,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        try {
                            // Parsing json array response
                            // loop through each json object
                            JSONArray jsonarray = new JSONArray(response.toString());
                            for (int i = 0; i < jsonarray.length(); i++) {

                                JSONObject person = (JSONObject) jsonarray.get(i);
                                success= person.getInt("success");
                                int user_id= person.getInt("user_id");
                                user_phone_no= person.getString("mobileno");

                            }


                            if(success==1) {

                                Intent intent=new Intent(SellMyCarActivity.this,Userlogin.class);
                                intent.putExtra("choice",1);
                                intent.putExtra("activity_id",2);
                                intent.putExtra("user_phone_number", unversal_treated_string(phone));


                                startActivityForResult(intent,REQUEST_CODE_SIGN_UP);
                                Toast.makeText(SellMyCarActivity.this, "please login to continue uploading", Toast.LENGTH_LONG).show();

                            } else if(success==0){

                                Intent intent=new Intent(SellMyCarActivity.this,Userlogin.class);

                                intent.putExtra("activity_id",2);
                                intent.putExtra("choice",2);
                                intent.putExtra("user_phone_number", unversal_treated_string(phone));
                                intent.putExtra("email", unversal_treated_string(email));
                                intent.putExtra("name", unversal_treated_string(name));



                                startActivityForResult(intent,REQUEST_CODE_SIGN_UP);
                                Toast.makeText(SellMyCarActivity.this, "please register to continue uploading", Toast.LENGTH_LONG).show();
//                                showConfirimDialog(user_phone_no,2);
                            }


                        } catch (JSONException e) {
                            e.printStackTrace();
                            Toast.makeText(SellMyCarActivity.this, "Error: " , Toast.LENGTH_LONG).show();
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
                params.put("phone_no",  unversal_treated_string(phone));
                params.put("email",  unversal_treated_string(email));
                params.put("user_id",  Integer.toString(user_id));



                return params;

            }

        };



        // Adding String request to request queue
        VolleyAppSingleton.getInstance(SellMyCarActivity.this).addToRequestQueue(check_usr_registered, REQUEST_TAG);
    }




    private void cameraIntent()
    {

        StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
        StrictMode.setVmPolicy(builder.build());

        try {


            String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss",
                    Locale.getDefault()).format(new Date());

           // File storageDir = new File(getExternalFilesDir(Environment.DIRECTORY_DCIM)+Environment.getExternalStorageDirectory().getAbsolutePath() + "/garisafi");
            File storageDir = new File(Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DCIM).toString()+ "/Camera/garisafi");
//            File storageDir = new File(getExternalFilesDir(Environment.DIRECTORY_PICTURES),"garisafi");
            // Create the storage directory if it does not exist

            if (!storageDir.exists()) {
                if (!storageDir.mkdirs()) {

                }
            }


            pictureImagePath = storageDir.getAbsolutePath() + File.separator + "IMG_" + timeStamp + "_GS"  + ".jpg";
            File file_original = new File(pictureImagePath);
            Uri outputFileUri = Uri.fromFile(file_original);

            Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
            cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, outputFileUri);
            startActivityForResult(Intent.createChooser(cameraIntent, "camera"), CAPTURE_IMAGE);

        }catch (Exception e){
            Log.e("error major",""+e.getMessage().toString());
            Toast.makeText(this,"Image not saved", Toast.LENGTH_LONG).show();
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        switch(requestCode) {

            case FilePickerConst.REQUEST_CODE:

                if(resultCode==RESULT_OK && data!=null)
                {
                    SELECT_IMAGE_PATHS_TEMP=new ArrayList<String>();
                    SELECT_IMAGE_PATHS_TEMP = data.getStringArrayListExtra(FilePickerConst.KEY_SELECTED_PHOTOS);


                    try
                    {

                        String image_path=SELECT_IMAGE_PATHS_TEMP.get(0);
                        defaut_image=image_path.substring(image_path.lastIndexOf("/")+1);
                        for (String path: SELECT_IMAGE_PATHS_TEMP) {
                            SELECT_IMAGE_PATHS.add(path);

                            image_model=new SelectImageModel();
                            image_model.setName(path.substring(path.lastIndexOf("/")+1));
                            image_model.setUri(Uri.fromFile(new File(path)));
                            camera_photo_array.add(image_model);

                        }

                        selectImageAdapter=new SelectImageAdapter(this,camera_photo_array,SELECT_IMAGE_PATHS);
                        gv.setAdapter(selectImageAdapter);

                    }catch (Exception e)
                    {
                        e.printStackTrace();
                    }


                }
                break;

            case PICK_IMAGE_REQUEST:
                if ( resultCode == RESULT_OK && data != null && data.getData() != null) {
                    Uri filePath = data.getData();
                    try {

                        bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                        int nh = (int) ( bitmap.getHeight() * (512.0 / bitmap.getWidth()) );
                        scaled = Bitmap.createScaledBitmap(bitmap, 512, nh, true);

                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                break;


              case CAPTURE_IMAGE:

                if ( resultCode == RESULT_OK) {

                    File imgFileOrig = new File(pictureImagePath);
                    BitmapFactory.Options bmOptions = new BitmapFactory.Options();
                    Bitmap bitmap = BitmapFactory.decodeFile(imgFileOrig.getAbsolutePath(),bmOptions);

                    try {


                            String path = pictureImagePath;
                            SELECT_IMAGE_PATHS.add(path);

                            image_model=new SelectImageModel();
                            image_model.setName(path.substring(path.lastIndexOf("/") + 1));
                            image_model.setUri(Uri.fromFile(new File(path)));
                            camera_photo_array.add(image_model);





                            // original measurements
                        int origWidth = bitmap.getWidth();
                        int origHeight = bitmap.getHeight();

                        final int destWidth = 600;//or the width you need

                        if (origWidth > destWidth) {
                            // picture is wider than we want it, we calculate its target height
                            int destHeight = origHeight / (origWidth / destWidth);
                            // we create an scaled bitmap so it reduces the image, not just trim it
                            Bitmap b2 = Bitmap.createScaledBitmap(bitmap, destWidth, destHeight, false);
                            ByteArrayOutputStream outStream = new ByteArrayOutputStream();
                            // compress to the format you want, JPEG, PNG...
                            // 70 is the 0-100 quality percentage
                            b2.compress(Bitmap.CompressFormat.JPEG, 90, outStream);
                            // we save the file, at least until we have made use of it
                            imgFileOrig.delete();
                            File f = new File(pictureImagePath);
                            f.createNewFile();
                            //write the bytes in file
                            FileOutputStream fo = new FileOutputStream(f);
                            fo.write(outStream.toByteArray());
                            // remember close de FileOutput
                            fo.close();


                        }


                    } catch (Exception e) {
                        Log.e("ereres",e.getMessage().toString());
                        Toast.makeText(this, "Image not saved", Toast.LENGTH_LONG).show();
                    }
                    selectImageAdapter = new SelectImageAdapter(this, camera_photo_array,SELECT_IMAGE_PATHS);
                 //   photo_model_array=camera_photo_array;
                    gv.setAdapter(selectImageAdapter);
                }else {
                    //Toast.makeText(this,"Image not taken",Toast.LENGTH_LONG).show();
                }

                break;


            case REQUEST_CODE_SIGN_UP:

                if ( resultCode == RESULT_OK) {

                    int result=data.getIntExtra("result",0);
                    int result_id=data.getIntExtra("result_id",0);


                    if(result==1){

                        hide_user_info_card(result_id);


                    }

                }else {
                    Toast.makeText(this,"not true",Toast.LENGTH_LONG).show();
                }


                break;



        }


    }
    private void hide_user_info_card(int result_id){
        try {
            Cursor cus = databaseHelper.select_user();
            if (cus.getCount() == 0) {
            }else  {

                while (cus.moveToNext()){
                    user_id=cus.getInt(0);
                }
                user_details_card.setVisibility(View.GONE);

            }
        } catch (SQLiteException se) {
            Log.e(getClass().getSimpleName(), "Open the database");
        }

        show_dialog_registration_succes(result_id);
    }

    class UploadFileToServer extends AsyncTask<String, Integer, String> {
        int x=0;
        int counter=0;
        int fail=0;
        long totalSize = 0;
        String responseString="";


        @Override
        protected void onPreExecute() {
            // setting progress bar to zero
            upload_dialog.show();
            progressBar.setProgress(0);
            counter= SELECT_IMAGE_PATHS.size();
         //   tv_fails.setText("= "+counter);
//            tv_counter.setText(x+"/"+counter);
            super.onPreExecute();
        }

        @Override
        protected void onProgressUpdate(Integer... progress) {
            // Making progress bar visible
            progressBar.setVisibility(View.VISIBLE);

            // updating progress bar value
            progressBar.setProgress(progress[0]);

            // updating percentage value
            txtPercentage.setText(String.valueOf(progress[0]) + "%");
        }

        @Override
        protected String doInBackground(String... params) {

            try {

            String image_path = new Config().GARISAFI_API + "/upload_car_photos.php";


            for (String path : SELECT_IMAGE_PATHS) {

                path=resizeAndCompressImageBeforeSend(SellMyCarActivity.this,path,path.substring(path.lastIndexOf("/")+1));
                Log.d("size_of_it", ""+ad_id+" " + path);

                    HttpClient httpclient = new DefaultHttpClient();
                    HttpPost httppost = new HttpPost(image_path);


                    File sourceFile = new File(path);
                    AndroidMultiPartEntity entity = new AndroidMultiPartEntity(
                            new AndroidMultiPartEntity.ProgressListener() {

                                @Override
                                public void transferred(long num) {
                                    publishProgress((int) ((num / (float) totalSize) * 100));
                                }
                            });

                    // Adding file data to http body
                    entity.addPart("image", new FileBody(sourceFile));
                    entity.addPart("ad_id", new StringBody(Integer.toString(ad_id)));
                    totalSize = entity.getContentLength();
                    httppost.setEntity(entity);



                    // Making server call
                    final HttpResponse response = httpclient.execute(httppost);
                    HttpEntity r_entity = response.getEntity();


                    int statusCode = response.getStatusLine().getStatusCode();
                    if (statusCode == 200) {

                        //getting sever response after a status 200 to promote handshake
                        responseString = EntityUtils.toString(r_entity);
                        Log.d("Result_boom", responseString + " " + ad_id);


                        if (responseString.contains("1")) {
                            x++;

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {

                                    tv_counter.setText(x + "/" + counter);
                                    //tv_fails.setText(responseString);
                                    tv_fails.setText(x + " images uploaded  " + "\nout of  " + counter);
                                }
                            });
                        } else {
                            fail++;

                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    tv_counter.setText(fail + "/" + counter);
                                    tv_fails.setText(fail + " failed images to " + "\nout of  " + counter);
                                    //tv_fails.setText(responseString);


                                }
                            });
                        }


                    } else {
                        responseString = "Error occurred! Http Status Code: "
                                + statusCode;
                        Log.e("error_occured0", responseString.toString());
                    }

                    Log.d("fails_pass","pass "+x+" fails "+fail);

            }

            } catch (ClientProtocolException e) {
                responseString = e.toString();
                Log.e("error_occured1", e.toString());
            } catch (IOException e) {
                responseString = e.toString();
                Log.e("error_occured2", e.toString());
            }

            return responseString;
        }

        @Override
        protected void onPostExecute(String result) {
            if(fail==0){
                linearLayout_dialog_progress.setVisibility(View.GONE);
                linearLayout_dialog_done.setVisibility(View.VISIBLE);
            }else {

                linearLayout_dialog_progress.setVisibility(View.GONE);
                linearLayout_dialog_fail.setVisibility(View.VISIBLE);

            }


            super.onPostExecute(result);
        }

    }




    public void show_dialog_preview_ad() {
        Button btn_preview_dismiss;
        ViewPager viewPager;
        ArrayList<String> image_array = new ArrayList<String>();
        TextView tv_dt_price, tv_dt_make,tv_dt_year,tv_dt_city,tv_dt_installments,tv_dt_mileage,tv_dt_fueltype;

        for (String path : SELECT_IMAGE_PATHS) {
            image_array.add(path);

        }

        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        final android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.car_preview_layout, null);
        dialogBuilder.setView(dialogView);

        btn_preview_dismiss = (Button) dialogView.findViewById(R.id.btn_preview_dismiss);
        tv_dt_price=(TextView) dialogView.findViewById(R.id.dt_price);
        tv_dt_make=(TextView) dialogView.findViewById(R.id.dt_make);
        tv_dt_year=(TextView) dialogView.findViewById(R.id.dt_year);
        tv_dt_city=(TextView) dialogView.findViewById(R.id.dt_city);
        tv_dt_installments=(TextView) dialogView.findViewById(R.id.dt_installments);
        tv_dt_mileage=(TextView) dialogView.findViewById(R.id.dt_mileage);
        tv_dt_fueltype=(TextView) dialogView.findViewById(R.id.dt_fuel_type);
        viewPager = (ViewPager) dialogView.findViewById(R.id.viewPager_ad_images);
        AdapterPreviewImages images_adapter=new AdapterPreviewImages(SellMyCarActivity.this,image_array);

        viewPager.setAdapter(images_adapter);
        CircleIndicator indicator = (CircleIndicator) dialogView.findViewById(R.id.indicator);
        indicator.setViewPager(viewPager);
        price=editText_price.getText().toString();
        mileage=editText_mileage.getText().toString();


        tv_dt_price.setText("KSH "+price);
        tv_dt_make.setText(""+preview_make+" "+preview_model);
        tv_dt_year.setText(""+preview_year);
        tv_dt_city.setText(""+preview_location);

        if(Integer.parseInt(unversal_treated_string(installments))>0){

            tv_dt_installments.setText("Installments allowed");

        }else {
            tv_dt_installments.setVisibility(View.GONE);


        }
       // tv_dt_installments.setText(""+installments);

        tv_dt_mileage.setText("Mileage: "+mileage);
        tv_dt_fueltype.setText("Fuel type: "+preview_fuel_type);






        //  dialogBuilder.setTitle("Select login if you already have an account");
        dialogBuilder.setCancelable(true);
        final android.support.v7.app.AlertDialog b = dialogBuilder.create();
        b.getWindow().setBackgroundDrawable(new ColorDrawable(Color.WHITE));
        btn_preview_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
            }
        });
        b.show();

    }


    public void show_dialog_registration_succes(int result_id) {

        Button btn_preview_dismiss;
        TextView tv_title,tv_message;


        this.getWindow().setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_STATE_ALWAYS_HIDDEN);
        final android.support.v7.app.AlertDialog.Builder dialogBuilder = new android.support.v7.app.AlertDialog.Builder(this);
        LayoutInflater inflater = this.getLayoutInflater();
        final View dialogView = inflater.inflate(R.layout.dialog_ok_registration, null);
        dialogBuilder.setView(dialogView);

        tv_title = (TextView) dialogView.findViewById(R.id.title_reg_ok);
        tv_message = (TextView) dialogView.findViewById(R.id.message_reg);

        if(result_id==1){
            tv_title.setText("Successfully logged in");
            tv_message.setText("You have successfully logged in,continue to upload");

        }else if(result_id==2){
            tv_title.setText("Successfully signed up");
            tv_message.setText("You have successfully signed up,continue to upload");


        }
        btn_preview_dismiss = (Button) dialogView.findViewById(R.id.ok_btn_reg_done);
        //  dialogBuilder.setTitle("Select login if you already have an account");
        dialogBuilder.setCancelable(true);
        final android.support.v7.app.AlertDialog b = dialogBuilder.create();
        b.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        btn_preview_dismiss.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                b.dismiss();
                upload_car();

            }
        });
        b.show();

    }

    public static class Item{
        public final String text;
        public final int icon;
        public Item(String text, Integer icon) {
            this.text = text;
            this.icon = icon;
        }
        @Override
        public String toString() {
            return text;
        }
    }


    private  void check_the_boxes(){

        checkBox_1_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {


                    //checked
                    selected_features_id.add("1");

                } else {
                    //not checked

                    selected_features_id.remove("1");

                }
            }
        });

        checkBox_1_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //checked
                    selected_features_id.add("2");
                    Log.d("huh1",""+selected_features_id);

                } else {
                    //not checked
                    selected_features_id.remove("2");
                    Log.d("huh2",""+selected_features_id);

                }
            }
        });

        checkBox_1_3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //checked
                    selected_features_id.add("3");

                } else {
                    //not checked
                    selected_features_id.remove("3");

                }
            }
        });



        checkBox_1_4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //checked
                    selected_features_id.add("4");

                } else {
                    //not checked
                    selected_features_id.remove("4");

                }
            }
        });

        checkBox_1_5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //checked
                    selected_features_id.add("26");

                } else {
                    //not checked
                    selected_features_id.remove("26");

                }
            }
        });

        checkBox_2_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //checked
                    selected_features_id.add("5");

                } else {
                    //not checked
                    selected_features_id.remove("5");

                }
            }
        });

        checkBox_2_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //checked
                    selected_features_id.add("6");

                } else {
                    //not checked
                    selected_features_id.remove("6");

                }
            }
        });
        checkBox_2_3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //checked
                    selected_features_id.remove("7");

                } else {
                    //not checked
                    selected_features_id.remove("7");

                }
            }
        });
        checkBox_2_4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //checked
                    selected_features_id.add("8");

                } else {
                    //not checked
                    selected_features_id.remove("8");

                }
            }
        });
        checkBox_2_5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //checked
                    selected_features_id.add("9");

                } else {
                    //not checked
                    selected_features_id.remove("9");

                }
            }
        });
        checkBox_2_6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //checked
                    selected_features_id.add("10");

                } else {
                    //not checked
                    selected_features_id.remove("10");

                }
            }
        });

        checkBox_2_7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //checked
                    selected_features_id.add("27");

                } else {
                    //not checked
                    selected_features_id.remove("27");

                }
            }
        });

        checkBox_2_8.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //checked
                    selected_features_id.add("28");

                } else {
                    //not checked
                    selected_features_id.remove("28");

                }
            }
        });
        checkBox_2_9.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //checked
                    selected_features_id.add("29");

                } else {
                    //not checked
                    selected_features_id.remove("29");

                }
            }
        });
        checkBox_2_10.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //checked
                    selected_features_id.add("30");

                } else {
                    //not checked
                    selected_features_id.remove("30");

                }
            }
        });
        checkBox_2_11.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //checked
                    selected_features_id.add("31");

                } else {
                    //not checked
                    selected_features_id.remove("31");

                }
            }
        });



        checkBox_3_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //checked
                    selected_features_id.add("11");

                } else {
                    //not checked
                    selected_features_id.remove("11");

                }
            }
        });
        checkBox_3_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //checked
                    selected_features_id.add("13");

                } else {
                    //not checked
                    selected_features_id.remove("13");

                }
            }
        });
        checkBox_3_3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //checked
                    selected_features_id.add("13");

                } else {
                    //not checked
                    selected_features_id.remove("13");

                }
            }
        });

        checkBox_4_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //checked
                    selected_features_id.add("14");

                } else {
                    //not checked
                    selected_features_id.remove("14");

                }
            }
        });
        checkBox_4_2.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //checked
                    selected_features_id.add("15");

                } else {
                    //not checked
                    selected_features_id.remove("15");

                }
            }
        });
        checkBox_4_3.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //checked
                    selected_features_id.add("16");

                } else {
                    //not checked
                    selected_features_id.remove("16");

                }
            }
        });
        checkBox_4_4.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //checked
                    selected_features_id.add("17");

                } else {
                    //not checked
                    selected_features_id.remove("17");

                }
            }
        });
        checkBox_4_5.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //checked
                    selected_features_id.add("18");

                } else {
                    //not checked
                    selected_features_id.remove("18");

                }
            }
        });
        checkBox_4_6.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //checked
                    selected_features_id.add("19");

                } else {
                    //not checked
                    selected_features_id.remove("19");

                }
            }
        });
        checkBox_4_7.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //checked
                    selected_features_id.add("20");

                } else {
                    //not checked
                    selected_features_id.remove("20");

                }
            }
        });
        checkBox_4_8.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //checked
                    selected_features_id.add("21");

                } else {
                    //not checked
                    selected_features_id.remove("21");

                }
            }
        });
        checkBox_4_9.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //checked
                    selected_features_id.add("22");

                } else {
                    //not checked
                    selected_features_id.remove("22");

                }
            }
        });
        checkBox_4_10.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //checked
                    selected_features_id.add("23");

                } else {
                    //not checked
                    selected_features_id.remove("23");

                }
            }
        });
        checkBox_4_11.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //checked
                    selected_features_id.add("24");

                } else {
                    //not checked
                    selected_features_id.remove("24");

                }
            }
        });
        checkBox_4_12.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked) {
                    //checked
                    selected_features_id.add("25");

                } else {
                    //not checked
                    selected_features_id.remove("25");

                }
            }
        });

    }

}
