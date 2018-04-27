package ke.co.wonderkid.garisafi.controllers;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.text.TextUtils;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import ke.co.wonderkid.garisafi.R;
import ke.co.wonderkid.garisafi.models.AdsModel;
import ke.co.wonderkid.garisafi.utils.Config;
import ke.co.wonderkid.garisafi.utils.VolleyAppSingleton;

/**
 * Created by beni on 2/26/18.
 */

public class Search_Activity  extends AppCompatActivity{


    CardView card_smart_search,card_standard_search;
    TextView tv_standard,tv_smart;
    Button btn_smart_search,btn_standard_search;
    private  static Context context;
    DatabaseHelper databaseHelper;
    ProgressDialog pDialog;
    private String urlJsonArry =  "";

    private Spinner spinner_make,spinner_model,spinner_fuel,spinner_transimition,spinner_car_condition,spinner_location,
            spinner_year_from,spinner_year_to,spinner_price_from,spinner_price_to;
    Spinner spinner_smart_year_from,spinner_smart_year_to ,spinner_smart_price_from,spinner_smart_price_to;

    List<String> make_labels_list= new ArrayList<String>();
    List<String>  make_id_list=new ArrayList<String>();
    List<String>  model_lables_id_list= new ArrayList<String>();
    List<String>  model_lables_list= new ArrayList<String>();
    List<String> year_list_from = new ArrayList<String>();
    List<String> year_list_to = new ArrayList<String>();
    List<String>  year_id_list= new ArrayList<String>();
    List<String> make_lables,model_lables,year_lables_from,year_lables_to=new ArrayList<String>();
    List<String>  idtbl_feature_list= new ArrayList<String>();
    List<String>  feature_group_list= new ArrayList<String>();
    List<String>  feature_name_list= new ArrayList<String>();

    String idtbl_feature,feature_group,feature_name;
    String make_id,model_id,year,fuel,transimition_mode,car_condition,location,year_from,year_to,price_from,price_to;



    ArrayAdapter<String> adapter_make,adapter_model,adapter_year_from,adapter_year_to;

    ArrayAdapter<CharSequence> adapter_fuel,adapter_transimition,adapter_car_condition,adapter_price_from,adapter_price_to,adapter_location;


ArrayList<String> makes_tokens;
ArrayAdapter adapter_tokens;



    MakeAutoCompletionView completionView;
    Person[] people;
    ArrayAdapter<Person> make_tokens_adapter;

    String make1,make2,make3,make4,make5;
    String TOKEN_MAKE_ID;
    final List<Person> make_token_list = new ArrayList<>();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.search_a_car);
        context=Search_Activity.this;

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.search_car_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override public void onClick(View v) {
                onBackPressed();
            }
        });


        pDialog = new ProgressDialog(context);
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);

        databaseHelper=new DatabaseHelper(context);


        card_smart_search=(CardView)findViewById(R.id.card_smart);
        card_standard_search=(CardView)findViewById(R.id.card_standard);
        tv_standard =(TextView) findViewById(R.id.tv_standard_search);

        tv_smart=(TextView) findViewById(R.id.tv_smart_search);
        tv_standard=(TextView) findViewById(R.id.tv_standard_search);
        btn_standard_search=(Button) findViewById(R.id.btn_standard_search);
        btn_smart_search=(Button) findViewById(R.id.btn_smart_search);


        spinner_make=(Spinner) findViewById(R.id.spinner_make_search);
        spinner_model=(Spinner) findViewById(R.id.spinner_model_search);
        spinner_fuel=(Spinner) findViewById(R.id.spinner_fuel_search);
        spinner_transimition=(Spinner) findViewById(R.id.spinner_transimition_search);
        spinner_car_condition=(Spinner) findViewById(R.id.spinner_condition_search);
        spinner_location=(Spinner) findViewById(R.id.spinner_location_search);
        spinner_year_from=(Spinner) findViewById(R.id.spinner_standard_year_from);
        spinner_year_to=(Spinner) findViewById(R.id.spinner_standard_year_to);
        spinner_price_from=(Spinner) findViewById(R.id.spinner_standard_price_from);
        spinner_price_to=(Spinner) findViewById(R.id.spinner_standard_price_to);

        spinner_smart_year_from=(Spinner) findViewById(R.id.spinner_smart_year_from);
        spinner_smart_year_to=(Spinner) findViewById(R.id.spinner_smart_year_to);
        spinner_smart_price_from=(Spinner) findViewById(R.id.spinner_smart_price_from);
        spinner_smart_price_to=(Spinner) findViewById(R.id.spinner_smart_price_to);


        show_tab_active();


        try {
            Cursor c=databaseHelper.get_make_lables();

            people = new Person[]{
                    new Person("", ""),
            };

            if(c.getCount()==0){
                Toast.makeText(context," null",Toast.LENGTH_LONG).show();

                people = new Person[]{
                        new Person("", ""),
                };

            }else {

                make_labels_list.add("Car make");
                make_id_list.add("0");

                while (c.moveToNext()) {

                    make_id_list.add(c.getString(0)) ;
                    make_labels_list.add(c.getString(1)) ;

                }





            }

            c.close();
            databaseHelper.close();



        }catch (SQLiteException e){

        }



        try {
            Cursor c=databaseHelper.get_filter_lables();

            people = new Person[]{
                    new Person("", ""),
            };

            if(c.getCount()==0){
                Toast.makeText(context," null",Toast.LENGTH_LONG).show();

                people = new Person[]{
                        new Person("", ""),
                };

            }else {

                while (c.moveToNext()) {

                    Person person_list=new Person(c.getString(3)+" - "+c.getString(1),c.getString(1));
                    make_token_list.add(person_list);
                }

            }

            c.close();
            databaseHelper.close();



        }catch (SQLiteException e){

        }






        make_tokens_adapter = new ArrayAdapter<Person>(this, R.layout.spinner_item, make_token_list);

//        make_tokens_adapter = new FilteredArrayAdapter<Person>(this, android.R.layout.simple_list_item_1, make_token_list) {
//            @Override
//            protected boolean keepObject(Person obj, String mask) {
//                mask = mask.toLowerCase();
//                return obj.getName().toLowerCase().startsWith(mask) || obj.getMake_id().toLowerCase().startsWith(mask);
//            }
//        };

        completionView = (MakeAutoCompletionView)findViewById(R.id.searchView);
        completionView.allowDuplicates(false);
       // completionView.setSplitChar(',');


//        char[] splitChar = {','};
//        completionView.setSplitChar(splitChar);

        completionView.setThreshold(1);
        completionView.setTokenLimit(5);
        completionView.setAdapter(make_tokens_adapter);

        make_tokens_adapter.getItem(0);





















        card_standard_search.setVisibility(View.VISIBLE);
        card_smart_search.setVisibility(View.GONE);





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




        try {
            Cursor c=databaseHelper.get_year_lables();
            year_list_from.add("Year from");
            year_list_to.add("Year to");
            year_id_list.add("0");
            while (c.moveToNext()) {
                year_id_list.add(c.getString(0));
                year_list_from.add(c.getString(1));
                year_list_to.add(c.getString(1));


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



        // Spinner_make Drop down elements for standard search

        year_lables_from = year_list_from;
        adapter_year_from = new ArrayAdapter<String>(this,R.layout.spinner_item, year_lables_from);
        adapter_make.setDropDownViewResource( R.layout.spinner_item);
        spinner_year_from.setAdapter(adapter_year_from);

        adapter_fuel = ArrayAdapter.createFromResource(this,  R.array.fuel, R.layout.spinner_item);
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

//the elements that got to and from
        year_lables_from = year_list_from;
        adapter_year_from = new ArrayAdapter<String>(this,  R.layout.spinner_item, year_lables_from);
        adapter_make.setDropDownViewResource( R.layout.spinner_item);
        spinner_year_from.setAdapter(adapter_year_from);


        year_lables_to = year_list_to;
        adapter_year_to = new ArrayAdapter<String>(this,  R.layout.spinner_item, year_lables_to);
        adapter_make.setDropDownViewResource( R.layout.spinner_item);
        spinner_year_to.setAdapter(adapter_year_to);




        adapter_price_from = ArrayAdapter.createFromResource(this,  R.array.budget_from,  R.layout.spinner_item);
        adapter_make.setDropDownViewResource( R.layout.spinner_item);
        spinner_price_from.setAdapter(adapter_price_from);


        adapter_price_to = ArrayAdapter.createFromResource(this,  R.array.budget_to,  R.layout.spinner_item);
        adapter_make.setDropDownViewResource( R.layout.spinner_item);
        spinner_price_to.setAdapter(adapter_price_to);



//spinners for smart search.

        year_lables_from = year_list_from;
        adapter_year_from = new ArrayAdapter<String>(this,  R.layout.spinner_item, year_lables_from);
        adapter_make.setDropDownViewResource( R.layout.spinner_item);
        spinner_smart_year_from.setAdapter(adapter_year_from);


        year_lables_to = year_list_to;
        adapter_year_to = new ArrayAdapter<String>(this,  R.layout.spinner_item, year_lables_to);
        adapter_make.setDropDownViewResource( R.layout.spinner_item);
        spinner_smart_year_to.setAdapter(adapter_year_to);




        adapter_price_from = ArrayAdapter.createFromResource(this,  R.array.budget_from, R.layout.spinner_item);
        adapter_make.setDropDownViewResource( R.layout.spinner_item);
        spinner_smart_price_from.setAdapter(adapter_price_from);


        adapter_price_to = ArrayAdapter.createFromResource(this,  R.array.budget_to,  R.layout.spinner_item);
        adapter_make.setDropDownViewResource( R.layout.spinner_item);
        spinner_smart_price_to.setAdapter(adapter_price_to);



        spinner_make.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                ((TextView) parent.getChildAt(0)).setTextSize(13);
              //  preview_make=spinner_make.getSelectedItem().toString();
                make_id=make_id_list.get(position);

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
                    while (c.moveToNext()) {

                        model_lables_id_list.add(c.getString(0)) ;
                        model_lables_list.add(c.getString(1)) ;

                    }

                    model_lables = model_lables_list;
                    adapter_model = new ArrayAdapter<String>(context,R.layout.spinner_item, model_lables);
                    adapter_make.setDropDownViewResource( R.layout.spinner_item);
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
               String preview_model=spinner_model.getSelectedItem().toString();
                model_id=model_lables_id_list.get(position);

                try {
                    Cursor c = databaseHelper.get_model_id(preview_model.trim());
                    while (c.moveToNext()) {

                        model_id=c.getString(0);
                    }

                }catch (SQLiteException e) {

                }


            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinner_fuel.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                ((TextView) parent.getChildAt(0)).setTextSize(13);
              //  preview_fuel_type=spinner_fuel.getSelectedItem().toString();

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
             //   preview_location=spinner_location.getSelectedItem().toString();


                location=Integer.toString(position);

            }


            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });




        spinner_year_from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                ((TextView) parent.getChildAt(0)).setTextSize(13);

                year_from=year_id_list.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_year_to.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                ((TextView) parent.getChildAt(0)).setTextSize(13);

                year_to=year_id_list.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_price_from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                ((TextView) parent.getChildAt(0)).setTextSize(13);
                if(position==0){
                    price_from="0";
                }else if(position==1){
                    price_from="0";
                }else if(position==2){
                    price_from="100000";
                }else if(position==3){
                    price_from="250000";
                }else if(position==4){
                    price_from="500000";
                }else if(position==5){
                    price_from="750000";
                }else if(position==6){
                    price_from="1000000";
                }else if(position==7){
                    price_from="2500000";
                }else if(position==8){
                    price_from="5000000";
                }else if(position==9){
                    price_from="7500000";
                }else if(position==10){
                    price_from="10000000";
                }else if(position==11){
                    price_from="100000000";
                }else{
                    price_from="0";
                }


             //   price_from=spinner_price_from.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinner_price_to.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                ((TextView) parent.getChildAt(0)).setTextSize(13);

                if(position==0){
                    price_to="0";
                }else if(position==1){
                    price_to="0";
                }else if(position==2){
                    price_to="100000";
                }else if(position==3){
                    price_to="250000";
                }else if(position==4){
                    price_to="500000";
                }else if(position==5){
                    price_to="750000";
                }else if(position==6){
                    price_to="1000000";
                }else if(position==7){
                    price_to="2500000";
                }else if(position==8){
                    price_to="5000000";
                }else if(position==9){
                    price_to="7500000";
                }else if(position==10){
                    price_to="10000000";
                }else if(position==11){
                    price_to="100000000";
                }else{
                    price_to="0";
                }

                //price_to=spinner_price_to.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



//spinners values for the smart search


        spinner_smart_year_from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                ((TextView) parent.getChildAt(0)).setTextSize(13);

                year_from=year_id_list.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_smart_year_to.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                ((TextView) parent.getChildAt(0)).setTextSize(13);

                year_to=year_id_list.get(position);

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        spinner_smart_price_from.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                ((TextView) parent.getChildAt(0)).setTextSize(13);
                if(position==0){
                    price_from="0";
                }else if(position==1){
                    price_from="0";
                }else if(position==2){
                    price_from="100000";
                }else if(position==3){
                    price_from="250000";
                }else if(position==4){
                    price_from="500000";
                }else if(position==5){
                    price_from="750000";
                }else if(position==6){
                    price_from="1000000";
                }else if(position==7){
                    price_from="2500000";
                }else if(position==8){
                    price_from="5000000";
                }else if(position==9){
                    price_from="7500000";
                }else if(position==10){
                    price_from="10000000";
                }else if(position==11){
                    price_from="100000000";
                }else{
                    price_from="0";
                }


                //   price_from=spinner_price_from.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        spinner_smart_price_to.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
//                ((TextView) parent.getChildAt(0)).setTextSize(13);

                if(position==0){
                    price_to="0";
                }else if(position==1){
                    price_to="0";
                }else if(position==2){
                    price_to="100000";
                }else if(position==3){
                    price_to="250000";
                }else if(position==4){
                    price_to="500000";
                }else if(position==5){
                    price_to="750000";
                }else if(position==6){
                    price_to="1000000";
                }else if(position==7){
                    price_to="2500000";
                }else if(position==8){
                    price_to="5000000";
                }else if(position==9){
                    price_to="7500000";
                }else if(position==10){
                    price_to="10000000";
                }else if(position==11){
                    price_to="100000000";
                }else{
                    price_to="0";
                }

                //price_to=spinner_price_to.getSelectedItem().toString();

            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });



        tv_smart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                card_standard_search.setVisibility(View.GONE);
                card_smart_search.setVisibility(View.VISIBLE);
                tv_smart.setBackgroundResource(R.drawable.my_textview_bg_active);
                tv_standard.setBackgroundResource(R.drawable.my_textview_bg_deactive);


            }
        });
        tv_standard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                card_standard_search.setVisibility(View.VISIBLE);
                card_smart_search.setVisibility(View.GONE);
                tv_standard.setBackgroundResource(R.drawable.my_textview_bg_active);
                tv_smart.setBackgroundResource(R.drawable.my_textview_bg_deactive);

            }
        });

        btn_smart_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String full_make_list = completionView.getObjects().toString().trim();
                String new_makes_lists= full_make_list.replaceAll("\\[", "");
                new_makes_lists= new_makes_lists.replaceAll("]", "");
                String[] makes_array = new_makes_lists.split(",");

//               Toast.makeText(context,"wooo"+ makes_array[0],Toast.LENGTH_LONG).show();
//
                if(makes_array[0].contentEquals("")){
                    Toast.makeText(context,"Please select at least one car",Toast.LENGTH_LONG).show();
                }else {

                    if (makes_array.length >= 1) {
                        // make1=Integer.parseInt(makes_array[0]);
                        make1 = findTokenid(makes_array[0]);

                    }
                    if (makes_array.length >= 2) {
                        make2 = findTokenid(makes_array[1]);

                    }
                    if (makes_array.length >= 3) {
                        make3 = findTokenid(makes_array[2]);

                    }
                    if (makes_array.length > 4) {
                        make4 = findTokenid(makes_array[3]);

                    }
                    if (makes_array.length > 5) {
                        make5 = findTokenid(makes_array[4]);

                    }

                    if (makes_array.length > 0) {
                        search_activity_smart();
                    } else {
                        Toast.makeText(context, "Please select at least one car", Toast.LENGTH_LONG).show();

                    }
                }




            }
        });

        btn_standard_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                search_activity();


            }
        });


    }

    public String findTokenid(String make_name) {

        String[] edited_make=make_name.split(" - ");

        try {

            //send make and model to db
            Cursor c=databaseHelper.get_model_token_id(edited_make[0].trim(),edited_make[1].trim());
            if(c.getCount()==0){
                //  Toast.makeText(context,"null "+make_name,Toast.LENGTH_LONG).show();
                TOKEN_MAKE_ID="0";
            }else {

                while (c.moveToNext()) {
                    TOKEN_MAKE_ID = c.getString(0);
                }
            }

            // c.close();



        }catch (SQLiteException e){

        }

        return TOKEN_MAKE_ID;
    }



    public void show_tab_active (){

        if (card_standard_search.getVisibility() == View.VISIBLE) {
            // Its visible
            tv_standard.setBackgroundResource(R.drawable.my_textview_bg_active);


        } else {
            // Either gone or invisible
            // Its visible
            tv_standard.setBackgroundResource(R.drawable.my_textview_bg_deactive);
        }

        if (card_smart_search.getVisibility() == View.VISIBLE) {
            // Its visible
            tv_smart.setBackgroundResource(R.drawable.my_textview_bg_active);
        } else {
            // Either gone or invisible
            tv_smart.setBackgroundResource(R.drawable.my_textview_bg_deactive);
        }
    }



    public void search_activity(){
        pDialog.show();
        final List<AdsModel> list = new ArrayList<>();
        String REQUEST_TAG = "SEARCH_REQUEST";
        urlJsonArry =  new Config().GARISAFI_API+"/standard_search.php";

        StringRequest free_req = new StringRequest(Request.Method.POST,urlJsonArry,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                       //     Toast.makeText(context,""+response,Toast.LENGTH_LONG).show();

                            if(!response.contains("NO")){
                                Intent intent=new Intent(context,SearchResultActivity.class);
                                intent.putExtra("response_string",response);
                                startActivity(intent);
                                finish();
                            }else {
                                Toast.makeText(context,"No result found, try some other search options",Toast.LENGTH_LONG).show();
                            }
                        pDialog.dismiss();


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
                pDialog.dismiss();

            }

        }){

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("car_make", int_treated_string(make_id));
                params.put("car_model", int_treated_string(model_id));
                params.put("fuel_type", int_treated_string(fuel));
                params.put("transmission", int_treated_string(transimition_mode));
                params.put("location", int_treated_string(location));
                params.put("year_from", int_treated_string(year_from));
                params.put("year_to", int_treated_string(year_to));
                params.put("budget_from", int_treated_string(price_from));
                params.put("budget_to", int_treated_string(price_to));

                return params;
            }

        };

        // Adding String request to request queue
        VolleyAppSingleton.getInstance(context).addToRequestQueue(free_req, REQUEST_TAG);

    }



    public void search_activity_smart(){
        pDialog.show();
        final List<AdsModel> list = new ArrayList<>();
        String REQUEST_TAG = "SEARCH_SMART_REQUEST";
       // Toast.makeText(context,""+make1+" "+make2+" "+make3+" "+make4+" "+make5, Toast.LENGTH_LONG).show();

        urlJsonArry =  new Config().GARISAFI_API+"/smart_search.php";

        StringRequest free_req = new StringRequest(Request.Method.POST,urlJsonArry,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                      //  Toast.makeText(context,""+response,Toast.LENGTH_LONG).show();

                        if(!response.contains("NO")){
                            Intent intent=new Intent(context,SearchResultActivity.class);
                            intent.putExtra("response_string",response);
                            startActivity(intent);
                            finish();
                        }else {
                            Toast.makeText(context,"No result found, try some other search options",Toast.LENGTH_LONG).show();
                        }
                        pDialog.dismiss();


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
                pDialog.dismiss();

            }

        }){

            @Override
            protected Map<String, String> getParams() {

                Map<String, String> params = new HashMap<String, String>();
                params.put("car_make1", int_treated_string(make1));
                params.put("car_make2", int_treated_string(make2));
                params.put("car_make3", int_treated_string(make3));
                params.put("car_make4", int_treated_string(make4));
                params.put("car_make5", int_treated_string(make5));
                params.put("year_from", int_treated_string(year_from));
                params.put("year_to", int_treated_string(year_to));
                params.put("budget_from", int_treated_string(price_from));
                params.put("budget_to", int_treated_string(price_to));

                return params;
            }

        };

        // Adding String request to request queue
        VolleyAppSingleton.getInstance(context).addToRequestQueue(free_req, REQUEST_TAG);

    }


    public static String int_treated_string(String s) {

        if (TextUtils.isEmpty(s)) {
            return "0";
        }else {
            return s;
        }

    }



}
