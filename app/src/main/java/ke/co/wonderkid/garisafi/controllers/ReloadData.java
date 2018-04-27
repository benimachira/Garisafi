package ke.co.wonderkid.garisafi.controllers;

import android.content.Context;
import android.content.SharedPreferences;
import android.database.sqlite.SQLiteException;
import android.os.AsyncTask;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.NetworkError;
import com.android.volley.NoConnectionError;
import com.android.volley.ParseError;
import com.android.volley.Response;
import com.android.volley.ServerError;
import com.android.volley.TimeoutError;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import ke.co.wonderkid.garisafi.models.AdsModel;
import ke.co.wonderkid.garisafi.utils.Config;
import ke.co.wonderkid.garisafi.utils.VolleyAppSingleton;

import static android.content.Context.MODE_PRIVATE;
import static ke.co.wonderkid.garisafi.controllers.MainActivity.MAKE_MODEL_PREFS;

/**
 * Created by beni on 4/9/18.
 */

public class ReloadData {
    Context context;
    String the_result = "";


    JSONArray MAKE_ARRAY;
    JSONArray MODEL_ARRAY;
    JSONArray YEAR_ARRAY;
    JSONArray FEATURES_ARRAY;
    DatabaseHelper databaseHelper;
    int make_done=0;
    int model_done=0;
    int year_done=0;
    int features_done=0;



    public ReloadData (Context context){
        this.context=context;
        databaseHelper=new DatabaseHelper(context);
        select_car_ads_new();

    }

    public void select_car_ads_new(){
        final List<AdsModel> list = new ArrayList<>();
        String REQUEST_TAG = "NEW_APP";
        String  urlJsonArry =  new Config().GARISAFI_API+"/select_set_up_data.php";


        StringRequest free_req = new StringRequest(urlJsonArry,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        BackReger backReger=new BackReger(context);
                        backReger.execute(response);


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

            }
        });

        // Adding String request to request queue
        VolleyAppSingleton.getInstance(context).addToRequestQueue(free_req, REQUEST_TAG);

    }


    class BackReger extends AsyncTask<String, Void, String> {


        BackReger(Context ctx) {

        }

        @Override
        protected void onPreExecute() {

        }

        @Override
        protected String doInBackground(String... params) {
            String response = params[0];

            try {

                JSONObject jsonObject = new JSONObject(response);
                JSONObject MOTHER_LIST = jsonObject.getJSONObject("MOTHER_LIST");
                MAKE_ARRAY=MOTHER_LIST.getJSONArray("MAKE_LIST");
                MODEL_ARRAY=MOTHER_LIST.getJSONArray("MODEL_LIST");
                YEAR_ARRAY=MOTHER_LIST.getJSONArray("YEAR_LIST");
                FEATURES_ARRAY=MOTHER_LIST.getJSONArray("FEATURES_LIST");


                for (int i = 0; i < MAKE_ARRAY.length(); i++) {
                    JSONObject person = (JSONObject) MAKE_ARRAY.get(i);
                    int idtbl_carmake = person.getInt("idtbl_carmake");
                    String make_name = person.getString("make_name");

                    boolean inserted=databaseHelper.insert_make(idtbl_carmake,make_name);
                    if(inserted==true){
                        make_done=1;
                    }


                }


                for (int i = 0; i < MODEL_ARRAY.length(); i++) {
                    JSONObject person = (JSONObject) MODEL_ARRAY.get(i);
                    int idtbl_carmodel = person.getInt("idtbl_carmodel");
                    int tbl_carmake_idtbl_carmake = person.getInt("tbl_carmake_idtbl_carmake");
                    String model_name = person.getString("model_name");

                    boolean inserted=databaseHelper.insert_model(idtbl_carmodel,tbl_carmake_idtbl_carmake,model_name);
                    if(inserted==true){
                        model_done=1;


                    }


                }

                for (int i = 0; i < YEAR_ARRAY.length(); i++) {
                    JSONObject person = (JSONObject) YEAR_ARRAY.get(i);
                    int idtbl_caryear = person.getInt("idtbl_caryear");
                    String year_name = person.getString("year_name");
                    boolean inserted = databaseHelper.insert_year(idtbl_caryear, year_name);
                    year_done=1;
                }

                for (int i = 0; i < FEATURES_ARRAY.length(); i++) {
                    JSONObject person = (JSONObject) FEATURES_ARRAY.get(i);
                    int idtbl_feature = person.getInt("idtbl_featureslist");
                    int feature_group = person.getInt("tbl_featureslistgroup_idtbl_featureslistgroup");
                    String feature_name = person.getString("featurename");
                    boolean inserted = databaseHelper.insert_features(idtbl_feature, feature_group,feature_name);
                    features_done=1;
                }


                if(make_done==1 && model_done==1 && features_done==1 && year_done==1){
                    SharedPreferences make_model_sharedPref=context.getSharedPreferences(MAKE_MODEL_PREFS, MODE_PRIVATE);
                    SharedPreferences.Editor editor=make_model_sharedPref.edit();
                    editor.putInt("make_model_inserted",1);
                    editor.commit();
                }

                return "OK";

            } catch (JSONException e) {

                return "JE";

            }catch (SQLiteException e){
                return "SQ";

            }

        }

        @Override
        protected void onPostExecute(String result) {

            if(result.equals("OK")) {
                Toast.makeText(context,"Set up complete",Toast.LENGTH_LONG).show();


            }else if(result.equals("JE")) {
                Toast.makeText(context,"Request Error occurred during set up",Toast.LENGTH_LONG).show();

            }if(result.equals("SQ")) {

                Toast.makeText(context,"Request Error occurred during set up",Toast.LENGTH_LONG).show();

            }

        }

    }
}
