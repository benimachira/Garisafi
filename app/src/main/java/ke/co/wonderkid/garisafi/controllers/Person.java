package ke.co.wonderkid.garisafi.controllers;

import java.io.Serializable;

/**
 * Created by beni on 3/13/18.
 */

public class Person implements Serializable {
    private String name;
    private String make_id;

    public Person(String n, String e) {
        name = n;
        make_id = e;
    }

    public String getName() {
        return name;
    }

    public String getMake_id() {
        return make_id;
    }

    @Override
    public String toString() {

        return name;

    }
}