package com.kubista.alek.virtualnurse;

import android.app.Activity;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    public void startClient(View view) {
        Intent i = new Intent(this,ClientActivity.class);
        startActivity(i);
    }

    public void startServer(View view) {
        Intent i = new Intent(this,ServerActivity.class);
        startActivity(i);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

}