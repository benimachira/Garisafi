package ke.co.wonderkid.garisafi.models;

import static ke.co.wonderkid.garisafi.utils.UniversalMethods.unversal_treated_string;
import static ke.co.wonderkid.garisafi.utils.UniversalMethods.unversal_treated_string_Zero;

/**
 * Created by beni on 3/6/18.
 */


public class ChatsEnqModel {



    public ChatsEnqModel(String to_id, String from_id, String enqrydate, String enqrymsg, String offer_price,
                         String byrname){
        this.setTo_id(unversal_treated_string_Zero(to_id));
        this.setFrom_id(unversal_treated_string_Zero(from_id));
        this.setEnqrydate(unversal_treated_string(enqrydate));
        this.setEnqrymsg(unversal_treated_string(enqrymsg));
        this.setOffer_price(unversal_treated_string(offer_price));
        this.setByrname(unversal_treated_string(byrname));

    }

    String to_id;
    String from_id;
    String enqrydate;
    String enqrymsg;
    String offer_price;
    String byrname;

    public String getByrname() {
        return byrname;
    }

    public void setByrname(String byrname) {
        this.byrname = byrname;
    }

    public String getTo_id() {
        return to_id;
    }

    public void setTo_id(String to_id) {
        this.to_id = to_id;
    }

    public String getFrom_id() {
        return from_id;
    }

    public void setFrom_id(String from_id) {
        this.from_id = from_id;
    }

    public String getEnqrydate() {
        return enqrydate;
    }

    public void setEnqrydate(String enqrydate) {
        this.enqrydate = enqrydate;
    }

    public String getEnqrymsg() {
        return enqrymsg;
    }

    public void setEnqrymsg(String enqrymsg) {
        this.enqrymsg = enqrymsg;
    }

    public String getOffer_price() {
        return offer_price;
    }

    public void setOffer_price(String offer_price) {
        this.offer_price = offer_price;
    }


}
