package ke.co.wonderkid.garisafi.models;

import static ke.co.wonderkid.garisafi.utils.UniversalMethods.unversal_treated_string;

/**
 * Created by Beni on 4/2/2017.
 */

public class MyAdsModel {

    public MyAdsModel(String car_make, String car_model, String year, String location, String price,
                      String installment_status, String photo1, int ad_id, String ad_user_id, String unread_count,
                      String idtbl_enquiries, int car_condition , int contact_mode , String mileage , int negotiable,
                      int mechanic_allowed , int car_history , int test_drive,int flagged_status,String upload_date,
                      String likes,String ad_views) {

        this.setCar_make(unversal_treated_string(car_make));
        this.setCar_model(unversal_treated_string(car_model));
        this.setYear(year);
        this.setLocation(unversal_treated_string(location));
        this.setPrice(unversal_treated_string(price));
        this.setInstallment_status(unversal_treated_string(installment_status));
        this.setPhoto1(unversal_treated_string(photo1));
        this.setAd_id(ad_id);
        this.setAd_user_id(ad_user_id);
        this.setUnread_count(unread_count);
        this.setIdtbl_enquiries(idtbl_enquiries);
        this.setCar_condition(car_condition);
        this.setContact_mode(contact_mode);
        this.setMileage(mileage);
        this.setNegotiable(negotiable);
        this.setMechanic_allowed(mechanic_allowed);
        this.setCar_history(car_history);
        this.setTest_drive(test_drive);
        this.setFlagged_status(flagged_status);
        this.setUpload_date(upload_date);
        this.setLikes(likes);
        this.setAd_views(ad_views);





    }

    String car_make;
    String car_model;
    String year;
    String location;
    String price;
    String installment_status;
    String photo1;
    int ad_id;
    String ad_user_id;
    String unread_count;
    String idtbl_enquiries;
    int flagged_status;

    int car_condition ;
    int contact_mode ;
    String mileage ;
    int negotiable ;
    int mechanic_allowed ;
    int car_history ;
    int test_drive;
    String upload_date;
    String likes;
    String ad_views;


    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public String getAd_views() {
        return ad_views;
    }

    public void setAd_views(String ad_views) {
        this.ad_views = ad_views;
    }



    public String getUpload_date() {
        return upload_date;
    }

    public void setUpload_date(String upload_date) {
        this.upload_date = upload_date;
    }


    public int getFlagged_status() {
        return flagged_status;
    }

    public void setFlagged_status(int flagged_status) {
        this.flagged_status = flagged_status;
    }

    public int getCar_condition() {
        return car_condition;
    }

    public void setCar_condition(int car_condition) {
        this.car_condition = car_condition;
    }

    public int getContact_mode() {
        return contact_mode;
    }

    public void setContact_mode(int contact_mode) {
        this.contact_mode = contact_mode;
    }

    public String getMileage() {
        return mileage;
    }

    public void setMileage(String mileage) {
        this.mileage = mileage;
    }

    public int getNegotiable() {
        return negotiable;
    }

    public void setNegotiable(int negotiable) {
        this.negotiable = negotiable;
    }

    public int getMechanic_allowed() {
        return mechanic_allowed;
    }

    public void setMechanic_allowed(int mechanic_allowed) {
        this.mechanic_allowed = mechanic_allowed;
    }

    public int getCar_history() {
        return car_history;
    }

    public void setCar_history(int car_history) {
        this.car_history = car_history;
    }

    public int getTest_drive() {
        return test_drive;
    }

    public void setTest_drive(int test_drive) {
        this.test_drive = test_drive;
    }


    public String getIdtbl_enquiries() {
        return idtbl_enquiries;
    }

    public void setIdtbl_enquiries(String idtbl_enquiries) {
        this.idtbl_enquiries = idtbl_enquiries;
    }


    public String getUnread_count() {
        return unread_count;
    }

    public void setUnread_count(String unread_count) {
        this.unread_count = unread_count;
    }



    public String getAd_user_id() {
        return ad_user_id;
    }

    public void setAd_user_id(String ad_user_id) {
        this.ad_user_id = ad_user_id;
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

    public String getYear() {
        return year;
    }

    public void setYear(String year) {
        this.year = year;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getInstallment_status() {
        return installment_status;
    }

    public void setInstallment_status(String installment_status) {
        this.installment_status = installment_status;
    }

    public String getPhoto1() {
        return photo1;
    }

    public void setPhoto1(String photo1) {
        this.photo1 = photo1;
    }
    public int getAd_id() {
        return ad_id;
    }

    public void setAd_id(int ad_id) {
        this.ad_id = ad_id;
    }





}
