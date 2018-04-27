package ke.co.wonderkid.garisafi.controllers;

import android.content.Context;
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

import ke.co.wonderkid.garisafi.utils.Config;
import ke.co.wonderkid.garisafi.utils.VolleyAppSingleton;

/**
 * Created by beni on 4/9/18.
 */

public class LikeACar {
    Context context;
    String the_result = "";
    String Ad_id;
    String urlJsonArry;
    String user_id;
    int do_Action;

    DatabaseHelper databaseHelper;



    public LikeACar(Context context,String Ad_id,String user_id,int do_Action){
        this.context=context;
        this.Ad_id=Ad_id;
        this.user_id=user_id;
        this.do_Action=do_Action;
        databaseHelper=new DatabaseHelper(context);
        like_this_ad(Ad_id);

    }

    public void like_this_ad(final  String ad_id){
            urlJsonArry = new Config().GARISAFI_API + "/like_dislike_ad.php";

        String REQUEST_TAG = "LikeAd";
        StringRequest signup_req = new StringRequest(Request.Method.POST,urlJsonArry,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        if(response.contentEquals("1")){
                         //   Toast.makeText(context, "res"+do_Action+" "+response, Toast.LENGTH_LONG).show();


                            if(do_Action==1) {
                                boolean result = databaseHelper.mark_as_liked(Integer.parseInt(ad_id), user_id);
                                if(result){
                                    Toast.makeText(context, "liked", Toast.LENGTH_LONG).show();
                                }


                            }else {

                                boolean result = databaseHelper.delete_from_liked(Integer.parseInt(ad_id), user_id);
                                if(result){
                                    Toast.makeText(context, "removed from my likes", Toast.LENGTH_LONG).show();
                                }




                            }


                        }


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

            }
        }){

            @Override
            protected Map<String, String> getParams() {
                Map<String, String> params = new HashMap<String, String>();
                params.put("ad_id", ad_id);
                params.put("user_id", user_id);
                params.put("do_action", Integer.toString(do_Action));
                return params;
            }

        };

        // Adding String request to request queue
        VolleyAppSingleton.getInstance(context).addToRequestQueue(signup_req, REQUEST_TAG);
    }
}
