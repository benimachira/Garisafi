package ke.co.wonderkid.garisafi.models;

import static ke.co.wonderkid.garisafi.utils.UniversalMethods.unversal_treated_string;
import static ke.co.wonderkid.garisafi.utils.UniversalMethods.unversal_treated_string_Zero;

/**
 * Created by Beni on 4/2/2017.
 */

public class AllEnqModel {

    public AllEnqModel(String car_make, String car_model,String enqrymsg,String ad_id,String original_enq_id,
                       String unread_count,String default_image, int ad_owner,String from_id,String to_id) {

        this.setCar_make(unversal_treated_string(car_make));
        this.setCar_model(unversal_treated_string(car_model));
        this.setEnqrymsg(unversal_treated_string(enqrymsg));
        this.setAd_id(unversal_treated_string_Zero(ad_id));
        this.setOriginal_enq_id(unversal_treated_string_Zero(original_enq_id));
        this.setUnread_count(unversal_treated_string_Zero(unread_count));
        this.setDefault_image(default_image);
        this.setAd_owner(ad_owner);
        this.setFrom_id(from_id);
        this.setTo_id(to_id);




    }

    String car_make;
    String car_model;
    String enqrymsg;
    String ad_id;
    String original_enq_id;
    String unread_count;
    String default_image;
    int ad_owner;
    String from_id;
    String to_id;

    public String getFrom_id() {
        return from_id;
    }

    public void setFrom_id(String from_id) {
        this.from_id = from_id;
    }

    public String getTo_id() {
        return to_id;
    }

    public void setTo_id(String to_id) {
        this.to_id = to_id;
    }



    public int getAd_owner() {
        return ad_owner;
    }

    public void setAd_owner(int ad_owner) {
        this.ad_owner = ad_owner;
    }



    public String getDefault_image() {
        return default_image;
    }

    public void setDefault_image(String default_image) {
        this.default_image = default_image;
    }



    public String getUnread_count() {
        return unread_count;
    }

    public void setUnread_count(String unread_count) {
        this.unread_count = unread_count;
    }



    public String getOriginal_enq_id() {
        return original_enq_id;
    }

    public void setOriginal_enq_id(String original_enq_id) {
        this.original_enq_id = original_enq_id;
    }



    public String getAd_id() {
        return ad_id;
    }

    public void setAd_id(String ad_id) {
        this.ad_id = ad_id;
    }


    public String getEnqrymsg() {
        return enqrymsg;
    }

    public void setEnqrymsg(String enqrymsg) {
        this.enqrymsg = enqrymsg;
    }

    public String getCar_make() {
        return car_make;
    }

    public void setCar_make(String car_make) {
        this.car_make = car_make;
    }

    public String getCar_model() {
        return car_model;
    }

    public void setCar_model(String car_model) {
        this.car_model = car_model;
    }



}
