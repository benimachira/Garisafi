package ke.co.wonderkid.garisafi.models;

/**
 * Created by beni on 3/2/18.
 */

public class MyDetailsModel {

    public MyDetailsModel(String usrfname, String mobileno_one, String usremail, String company_name){

                this.usrfname=usrfname;
                this.mobileno_one=mobileno_one;
                this.usremail=usremail;
                this.company_name=company_name;

    }
    String usrfname;
    String mobileno_one;
    String usremail;
    String company_name;


    public String getUsrfname() {
        return usrfname;
    }

    public void setUsrfname(String usrfname) {
        this.usrfname = usrfname;
    }

    public String getMobileno_one() {
        return mobileno_one;
    }

    public void setMobileno_one(String mobileno_one) {
        this.mobileno_one = mobileno_one;
    }

    public String getUsremail() {
        return usremail;
    }

    public void setUsremail(String usremail) {
        this.usremail = usremail;
    }

    public String getCompany_name() {
        return company_name;
    }

    public void setCompany_name(String company_name) {
        this.company_name = company_name;
    }





}
