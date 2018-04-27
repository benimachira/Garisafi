package ke.co.wonderkid.garisafi.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.text.TextUtils;
import android.util.Log;

import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by beni on 2/20/18.
 */

public  class UniversalMethods {

    ArrayList<String> mWordHistory = null;

    SharedPreferences prefs;
    Context context;
    List<String> sold_cars;
    DatabaseHelper databaseHelper;
    List<String>  sold_car_list  = new ArrayList<String>();


    public static final String SOLD_PREFERENCES = "sold_prefs" ;

    public UniversalMethods(Context context) {
        this.context = context;
        databaseHelper=new DatabaseHelper(context);
    }

    //This method adds comma separators for numbers at a thousand mark e.g price mileage etc
    public static String doubleToStringNoDecimal(double d) {
        DecimalFormat formatter = (DecimalFormat) NumberFormat.getInstance(Locale.US);;
        formatter .applyPattern("#,###.##");
        return formatter.format(d);
    }

    //This method treats the strings which might be null to cause a null pointer expect
    //that is if a string is null the method returns an empty string which is much safer

    public static String unversal_treated_string(String s) {

        if (TextUtils.isEmpty(s) ||  s.contentEquals("NULL") ||  s.contentEquals("null")) {
            return "";
        }else {
            return s;
        }

    }

    public static String unversal_treated_string_Zero(String s) {

        if (TextUtils.isEmpty(s) ||  s.contentEquals("NULL") ||  s.contentEquals("null")) {
            return "0";
        }else {
            return s;
        }

    }


    //check if a character string is a valid email address.

    public static boolean isValidMail(String email) {
        boolean check;
        Pattern p;
        Matcher m;

        String EMAIL_STRING = "^[_A-Za-z0-9-\\+]+(\\.[_A-Za-z0-9-]+)*@"
                + "[A-Za-z0-9-]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$";

        p = Pattern.compile(EMAIL_STRING);

        m = p.matcher(email);
        check = m.matches();

        return check;
    }


    public static boolean isValidMobile(String phone) {
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


    public static String date_formart_converter(String time) {
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "dd-mm-yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;
        String str = null;

        try {
            date = inputFormat.parse(time);
            str = outputFormat.format(date);
        } catch (ParseException e) {
            e.printStackTrace();
        }
        return str;
    }


    public  static  String cooking_time(String time){

        String display_time="";

        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        String time2 = dateFormat.format(new Date()); // Find todays date
        //HH converts hour in 24 hours format (0-23), day calculation
        SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");

        Date d1 = null;
        Date d2 = null;

        try {
            d1 = format.parse(time);
            d2 = format.parse(time2);

            if(d2.getTime()>d1.getTime()) {

                //in milliseconds
                long diff = d2.getTime() - d1.getTime();

                Log.d("ftime", d1 + " " + d2 + " " + diff);

                long diffSeconds = diff / 1000 % 60;
                long diffMinutes = diff / (60 * 1000) % 60;
                long diffHours = diff / (60 * 60 * 1000) % 24;
                long diffDays = diff / (24 * 60 * 60 * 1000);
                long diffWeeks = diff / (24 * 60 * 60 * 1000 * 7);

                if (diffDays < 1) {

                    display_time = "today";

                } else if (diffDays == 1) {

                    display_time = diffDays + " day ago";

                } else if (diffDays > 1 && diffDays < 7) {

                    display_time = diffDays + " days ago";

                } else if (diffWeeks == 1 ) {
                    display_time = diffWeeks + " Week ago";

                } else if (diffWeeks > 1 && diffDays < 30) {
                    display_time = diffWeeks + " Weeks ago";

                } else if (diffDays > 30) {

                    String inputPattern = "yyyy-MM-dd HH:mm:ss";
                    String outputPattern = "EEE, d MMM yyyy";
                    SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
                    SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

                    Date date = null;
                    date = inputFormat.parse(time);
                    display_time = "on "+outputFormat.format(date);

                } else {
                    display_time = "N/A";
                }

            }else {
                display_time="N/A";
            }


        } catch (ParseException e) {
            e.printStackTrace();
            display_time="N/A";
        }



        return display_time;
//
//        Date and Time Pattern	Result
//        "yyyy.MM.dd G 'at' HH:mm:ss z"	2001.07.04 AD at 12:08:56 PDT
//        "EEE, MMM d, ''yy"	Wed, Jul 4, '01
//        "h:mm a"	12:08 PM
//        "hh 'o''clock' a, zzzz"	12 o'clock PM, Pacific Daylight Time
//        "K:mm a, z"	0:08 PM, PDT
//        "yyyyy.MMMMM.dd GGG hh:mm aaa"	02001.July.04 AD 12:08 PM
//        "EEE, d MMM yyyy HH:mm:ss Z"	Wed, 4 Jul 2001 12:08:56 -0700
//        "yyMMddHHmmssZ"	010704120856-0700
//        "yyyy-MM-dd'T'HH:mm:ss.SSSZ"	2001-07-04T12:08:56.235-0700

    }

    public static  String formarted_time(String time){
        String display_time="";
        String inputPattern = "yyyy-MM-dd HH:mm:ss";
        String outputPattern = "EEE, d MMM yyyy";
        SimpleDateFormat inputFormat = new SimpleDateFormat(inputPattern);
        SimpleDateFormat outputFormat = new SimpleDateFormat(outputPattern);

        Date date = null;

        try {
            date = inputFormat.parse(time);
            display_time = "on "+outputFormat.format(date);
            return display_time;
        } catch (ParseException e) {
            e.printStackTrace();
            return "N/A";
        }



    }










}
