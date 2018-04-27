package ke.co.wonderkid.garisafi.models;

import java.io.Serializable;

import static ke.co.wonderkid.garisafi.utils.UniversalMethods.unversal_treated_string;

/**
 * Created by Beni on 4/2/2017.
 */




public class AdsModel  implements Serializable {

    public AdsModel(String car_make, String car_model, String year, String location, String price,
                    String installment_status, String photo1,int ad_id,String ad_user_id) {

        this.setCar_make(unversal_treated_string(car_make));
        this.setCar_model(unversal_treated_string(car_model));
        this.setYear(year);
        this.setLocation(unversal_treated_string(location));
        this.setPrice(unversal_treated_string(price));
        this.setInstallment_status(unversal_treated_string(installment_status));
        this.setPhoto1(unversal_treated_string(photo1));
        this.setAd_id(ad_id);
        this.setAd_user_id(ad_user_id);




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
