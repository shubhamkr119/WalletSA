package com.sa.walletsa;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;


public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//        try {
//            sleep(5000);
//        }
//        catch (Exception e) {
//            e.printStackTrace();
//        }

        SharedPreferences preferences = getSharedPreferences("preferences", MODE_PRIVATE);

        if(preferences.contains("username")){
            Intent intent = new Intent(this, MainActivity.class);
            startActivity(intent);
        }
        else {
            Intent intent = new Intent(this, LoginActivity.class);
            startActivity(intent);
        }

        finish();
    }

}
