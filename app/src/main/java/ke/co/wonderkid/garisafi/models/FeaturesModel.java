package ke.co.wonderkid.garisafi.models;

import java.io.Serializable;

import static ke.co.wonderkid.garisafi.utils.UniversalMethods.unversal_treated_string;

/**
 * Created by Beni on 4/2/2017.
 */




public class FeaturesModel implements Serializable {

    public FeaturesModel(String feature_id, String feature_group, String feature_name) {

        this.setFeature_id(unversal_treated_string(feature_id));
        this.setFeature_group(unversal_treated_string(feature_group));
        this.setFeature_name(feature_name);


    }

    String feature_id;
    String feature_group;
    String feature_name;





    public String getFeature_id() {
        return feature_id;
    }

    public void setFeature_id(String feature_id) {
        this.feature_id = feature_id;
    }

    public String getFeature_group() {
        return feature_group;
    }

    public void setFeature_group(String feature_group) {
        this.feature_group = feature_group;
    }

    public String getFeature_name() {
        return feature_name;
    }

    public void setFeature_name(String feature_name) {
        this.feature_name = feature_name;
    }





}
