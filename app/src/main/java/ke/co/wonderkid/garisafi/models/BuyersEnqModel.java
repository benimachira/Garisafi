package ke.co.wonderkid.garisafi.models;

import static ke.co.wonderkid.garisafi.utils.UniversalMethods.unversal_treated_string;
import static ke.co.wonderkid.garisafi.utils.UniversalMethods.unversal_treated_string_Zero;

/**
 * Created by beni on 3/6/18.
 */


public class BuyersEnqModel {

    public BuyersEnqModel(String byrname, String byr_phno, String message_count,String ad_id,
                          String buyer_id,String seller_id){
        this.setByrname(unversal_treated_string(byrname));
        this.setByr_phno(unversal_treated_string(byr_phno));
        this.setMessage_count(unversal_treated_string(message_count));
        this.setAd_id(unversal_treated_string_Zero(ad_id));
        this.setBuyer_id(unversal_treated_string_Zero(buyer_id));
        this.setSeller_id(unversal_treated_string_Zero(seller_id));

    }


    String byrname;
    String byr_phno;
    String message_count;
    String ad_id;


    public String getSeller_id() {
        return seller_id;
    }

    public void setSeller_id(String seller_id) {
        this.seller_id = seller_id;
    }

    String seller_id;

    public String getBuyer_id() {
        return buyer_id;
    }

    public void setBuyer_id(String buyer_id) {
        this.buyer_id = buyer_id;
    }

    String buyer_id;


    public String getAd_id() {
        return ad_id;
    }

    public void setAd_id(String ad_id) {
        this.ad_id = ad_id;
    }



    public String getByrname() {
        return byrname;
    }

    public void setByrname(String byrname) {
        this.byrname = byrname;
    }

    public String getByr_phno() {
        return byr_phno;
    }

    public void setByr_phno(String byr_phno) {
        this.byr_phno = byr_phno;
    }

    public String getMessage_count() {
        return message_count;
    }

    public void setMessage_count(String message_count) {
        this.message_count = message_count;
    }

}

