package ke.co.wonderkid.garisafi.models;

/**
 * Created by Beni on 4/2/2017.
 */

public class Car_Detail_Model {

    public Car_Detail_Model(String car_make, String car_model, String year, String location, String price,
                            String installment_status, String photo1, int ad_id,String tbl_usrac_idtbl_usrac, String sellersnote,
                        String upload_date, String date_sold, String  car_regno, String car_chasisno, String carimage_count, String expiry_date
    ) {

        this.setCar_make(car_make);
        this.setCar_model(car_model);
        this.setYear(year);
        this.setLocation(location);
        this.setPrice(price);
        this.setInstallment_status(installment_status);
        this.setPhoto1(photo1);
        this.setAd_id(ad_id);
        this.setTbl_usrac_idtbl_usrac(tbl_usrac_idtbl_usrac);
        this.setSellersnote(sellersnote);
        this.setUpload_date(upload_date);
        this.setDate_sold(date_sold);
        this.setCar_regno(car_regno);
        this.setCar_chasisno(car_chasisno);
        this.setCarimage_count(carimage_count);
        this.setExpiry_date(expiry_date);



    }

    String car_make;
    String car_model;
    String year;
    String location;
    String price;
    String installment_status;
    String photo1;
    int ad_id;
    String tbl_usrac_idtbl_usrac;
    String sellersnote;
    String upload_date;
    String date_sold;
    String  car_regno;
    String car_chasisno;
    String carimage_count;
    String expiry_date;


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

    public String getTbl_usrac_idtbl_usrac() {
        return tbl_usrac_idtbl_usrac;
    }

    public void setTbl_usrac_idtbl_usrac(String tbl_usrac_idtbl_usrac) {
        this.tbl_usrac_idtbl_usrac = tbl_usrac_idtbl_usrac;
    }

    public String getSellersnote() {
        return sellersnote;
    }

    public void setSellersnote(String sellersnote) {
        this.sellersnote = sellersnote;
    }

    public String getUpload_date() {
        return upload_date;
    }

    public void setUpload_date(String upload_date) {
        this.upload_date = upload_date;
    }

    public String getDate_sold() {
        return date_sold;
    }

    public void setDate_sold(String date_sold) {
        this.date_sold = date_sold;
    }

    public String getCar_regno() {
        return car_regno;
    }

    public void setCar_regno(String car_regno) {
        this.car_regno = car_regno;
    }

    public String getCar_chasisno() {
        return car_chasisno;
    }

    public void setCar_chasisno(String car_chasisno) {
        this.car_chasisno = car_chasisno;
    }

    public String getCarimage_count() {
        return carimage_count;
    }

    public void setCarimage_count(String carimage_count) {
        this.carimage_count = carimage_count;
    }

    public String getExpiry_date() {
        return expiry_date;
    }

    public void setExpiry_date(String expiry_date) {
        this.expiry_date = expiry_date;
    }




}
