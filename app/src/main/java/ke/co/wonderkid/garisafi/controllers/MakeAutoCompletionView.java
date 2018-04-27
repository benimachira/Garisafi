package ke.co.wonderkid.garisafi.controllers;

import android.app.Activity;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tokenautocomplete.TokenCompleteTextView;

import ke.co.wonderkid.garisafi.R;

/**
 * Created by beni on 3/13/18.
 */
public class MakeAutoCompletionView extends TokenCompleteTextView<Person> {
    Person person;
    public MakeAutoCompletionView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    protected View getViewForObject(Person person) {

        LayoutInflater l = (LayoutInflater) getContext().getSystemService(Activity.LAYOUT_INFLATER_SERVICE);
        TextView view = (TextView) l.inflate(R.layout.makes_tokens, (ViewGroup) getParent(), false);
        view.setText(person.getName());


        return view;
    }

    @Override
    protected Person defaultObject(String completionText) {
        return null;
    }

//    @Override
//    public List<Person> getObjects() {
//        return super.getObjects();
//    }



    //    @Override
//    protected Person defaultObject(String completionText) {
//        //Stupid simple example of guessing if we have an email or not
////        int index = completionText.indexOf('@');
////        if (index == -1) {
////            return new Person(completionText, completionText.replace(" ", "") + "@example.com");
////        } else {
////            return new Person(completionText.substring(0, index), completionText);
////        }
//    }
}
