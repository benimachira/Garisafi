package ke.co.wonderkid.garisafi.models;

import android.net.Uri;

/**
 * Created by Oclemy on 8/4/2016 for ProgrammingWizards Channel and http://www.camposha.com.
 */
public class SelectImageModel {
    String name;
    Uri uri;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public Uri getUri() {
        return uri;
    }

    public void setUri(Uri uri) {
        this.uri = uri;
    }
}
