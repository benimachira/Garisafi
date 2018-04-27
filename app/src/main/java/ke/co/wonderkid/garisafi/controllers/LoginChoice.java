package ke.co.wonderkid.garisafi.controllers;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.view.View;

import ke.co.wonderkid.garisafi.R;

/**
 * Created by beni on 4/23/18.
 */

public class LoginChoice extends AppCompatActivity {
    int login_choice=0;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.not_loged_in);

        android.support.v7.widget.Toolbar toolbar = (android.support.v7.widget.Toolbar) findViewById(R.id.not_login_toolbar);
        toolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
    }

    public void choose_login(View veiw){
        //loggin to your account
            login_choice=1;
            Intent intent=new Intent(this,Userlogin.class);
            intent.putExtra("choice",login_choice);
            intent.putExtra("activity_id",1);
            startActivity(intent);
            MainActivity.logged_in=false;
            finish();

    }

    public void choose_join(View veiw){
        //loggin to your account
        login_choice=2;
        Intent intent=new Intent(this,Userlogin.class);
        intent.putExtra("choice",login_choice);
        intent.putExtra("activity_id",1);
        startActivity(intent);
        MainActivity.logged_in=false;
        finish();


    }



}
