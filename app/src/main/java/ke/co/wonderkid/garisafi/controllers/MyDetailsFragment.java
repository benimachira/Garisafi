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

import android.app.ProgressDialog;
import android.database.Cursor;
import android.database.sqlite.SQLiteException;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
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

import java.util.HashMap;
import java.util.Map;

import ke.co.wonderkid.garisafi.R;
import ke.co.wonderkid.garisafi.utils.Config;
import ke.co.wonderkid.garisafi.utils.VolleyAppSingleton;

public class MyDetailsFragment extends Fragment {

    private String urlJsonArry =  "";

    ProgressDialog pDialog;
    NestedScrollView parent_view;
    DatabaseHelper db_helper;
    String user_id,local_user_name,local_phone_no,local_email_address= "";

    String usrfname ;
    String mobileno_one ;
    String mobileno_two ;
    String usremail ;
    String company_name ;

    EditText ed_acc_type,ed_country,ed_biz_name,ed_biz_location,ed_phone1,ed_phone2,ed_display_phone,ed_email;
    String business_name,business_location,phone2,display_phone;
    Button btn_save;




    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        parent_view=(NestedScrollView) inflater.inflate(R.layout.my_details_list, container, false);

        ed_acc_type= (EditText) parent_view.findViewById(R.id.ed_acc_type);
        ed_country= (EditText) parent_view.findViewById(R.id.ed_country);
        ed_biz_name= (EditText) parent_view.findViewById(R.id.ed_biz_name);
        ed_biz_location= (EditText) parent_view.findViewById(R.id.ed_biz_location);
        ed_phone1= (EditText) parent_view.findViewById(R.id.ed_phone1);
        ed_phone2= (EditText) parent_view.findViewById(R.id.ed_phone2);
        ed_display_phone= (EditText) parent_view.findViewById(R.id.ed_display_phone);
        ed_email= (EditText) parent_view.findViewById(R.id.ed_email);
        ed_biz_location= (EditText) parent_view.findViewById(R.id.ed_biz_location);
        btn_save= (Button) parent_view.findViewById(R.id.bt_save);
        set_views_fonts();

        return parent_view;
    }


    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        pDialog = new ProgressDialog(getContext());
        db_helper=new DatabaseHelper(getContext());
        pDialog.setMessage("Please wait...");
        pDialog.setCancelable(false);
        try {
            Cursor cus = db_helper.select_user();
            if (cus.getCount() == 0) {


                Log.d("nkt"," fuuuuk");

            }else  {
                while (cus.moveToNext()){
                    user_id=cus.getString(0);
                    local_phone_no=cus.getString(1);
                    local_user_name=cus.getString(2);
                    local_email_address=cus.getString(3);

                }

            }
        } catch (SQLiteException se) {

            Log.e(getClass().getSimpleName(), "Open the database"+se.getMessage().toString());
        }



        select_my_details(user_id);


        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update_user_details(user_id);
            }
        });

    }



    private  void set_views_fonts(){



        Typeface black_typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Black.ttf");
        Typeface bold_typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Bold.ttf");
        Typeface regular_typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Regular.ttf");
        Typeface medium_typeface = Typeface.createFromAsset(getContext().getAssets(), "fonts/Roboto-Medium.ttf");


        ed_acc_type.setTypeface(medium_typeface);
        ed_country.setTypeface(medium_typeface);
        ed_biz_name.setTypeface(medium_typeface);
        ed_biz_location.setTypeface(medium_typeface);
        ed_phone1.setTypeface(medium_typeface);
        ed_phone2.setTypeface(medium_typeface);
        ed_display_phone.setTypeface(medium_typeface);
        ed_email.setTypeface(medium_typeface);
        ed_biz_location.setTypeface(medium_typeface);
        btn_save.setTypeface(medium_typeface);
    }



    public void select_my_details(final  String user_id_final){

        pDialog.show();

        String REQUEST_TAG = "SELECT_DETAILS";
        urlJsonArry=new Config().GARISAFI_API+"/select_my_profile.php";


        StringRequest user_login_req = new StringRequest(Request.Method.POST,urlJsonArry,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // Parsing json array response
                            // loop through each json object

                          // Toast.makeText(getContext(),""+response, Toast.LENGTH_LONG).show();


                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {

                                JSONObject person = (JSONObject) jsonArray.get(i);
                                 usrfname = person.getString("usrfname");
                                 mobileno_one = person.getString("mobileno_one");
                                 usremail = person.getString("usremail");
                                 company_name = person.getString("company_name");
                                 mobileno_two= person.getString("mobileno_two");

                            }





                            set_views(usrfname,mobileno_one,usremail,company_name,mobileno_two);


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
                    Toast.makeText(getContext(),""+message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                    Toast.makeText(getContext(),""+message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection! and try again";
                    Toast.makeText(getContext(),""+message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof ParseError) {
                    message = "Parsing error! Please try again ";
                    Toast.makeText(getContext(),""+message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection! and try again";
                    Toast.makeText(getContext(),""+message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof TimeoutError) {
                    message = "Connection TimeOut! try again";
                    Toast.makeText(getContext(),""+message, Toast.LENGTH_LONG).show();
                }
                pDialog.dismiss();
            }
        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id",user_id_final );

                return params;
            }

        };

        // Adding String request to request queue
        VolleyAppSingleton.getInstance(getContext()).addToRequestQueue(user_login_req, REQUEST_TAG);
    }


    public void update_user_details(final  String user_id_final){

        pDialog.show();

        String REQUEST_TAG = "SELECT_DETAILS";
        urlJsonArry=new Config().GARISAFI_API+"/update_my_details.php";
        business_name=ed_biz_name.getText().toString();
        business_location=ed_biz_location.getText().toString();
        phone2=ed_phone2.getText().toString();
        display_phone=ed_display_phone.getText().toString();


        StringRequest user_login_req = new StringRequest(Request.Method.POST,urlJsonArry,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.contentEquals("1")){
                            Toast.makeText(getContext(),"Your information was successfully updted ", Toast.LENGTH_LONG).show();

                        }else {
                            Toast.makeText(getContext(),"Failed to update your info ", Toast.LENGTH_LONG).show();

                        }



                        pDialog.dismiss();

                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError volleyError) {
                String message = null;
                if (volleyError instanceof NetworkError) {
                    message = "Cannot connect to Internet...Please check your connection! and try again";
                    Toast.makeText(getContext(),""+message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof ServerError) {
                    message = "The server could not be found. Please try again after some time!!";
                    Toast.makeText(getContext(),""+message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof AuthFailureError) {
                    message = "Cannot connect to Internet...Please check your connection! and try again";
                    Toast.makeText(getContext(),""+message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof ParseError) {
                    message = "Parsing error! Please try again ";
                    Toast.makeText(getContext(),""+message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof NoConnectionError) {
                    message = "Cannot connect to Internet...Please check your connection! and try again";
                    Toast.makeText(getContext(),""+message, Toast.LENGTH_LONG).show();
                } else if (volleyError instanceof TimeoutError) {
                    message = "Connection TimeOut! try again";
                    Toast.makeText(getContext(),""+message, Toast.LENGTH_LONG).show();
                }
                pDialog.dismiss();
            }
        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("user_id",user_id_final );
                params.put("business_name",treated_reverse_string(business_name) );
                params.put("location",treated_reverse_string(business_location ));
                params.put("phone2",treated_reverse_string(phone2));
                params.put("display_phone",treated_reverse_string(display_phone));
                return params;
            }

        };

        // Adding String request to request queue
        VolleyAppSingleton.getInstance(getContext()).addToRequestQueue(user_login_req, REQUEST_TAG);
    }

    public  void set_views(String usr_fname,String usr_phone1,String usr_mail, String company_name,String mobileno_two){

        TextView tv_usrname = ((UserProfile)getActivity()).get_usr_name_TV();
        tv_usrname.setText(""+usr_fname);
        ed_phone1.setText(""+treated_string(usr_phone1));
        ed_phone2.setText(""+treated_string(mobileno_two));
        ed_biz_name.setText(""+treated_string(company_name));
        ed_email.setText(""+treated_string(usr_mail));
        ed_display_phone.setText(""+treated_string(usr_phone1));
        ed_biz_location.setText(treated_string(""));
        ed_acc_type.setText(treated_string("Individual"));
        ed_country.setText("Kenya");

    }


    public static String treated_string(String s) {

        if (TextUtils.isEmpty(s) ||  s.contentEquals("null")) {
            return "none";
        }else {
            return s;
        }

    }

    public String treated_reverse_string(String s) {

        if (s.contentEquals("none") || s.contentEquals("") ||  s.contentEquals("null")) {

            return "";
        }else {
            return s;
        }

    }


}

