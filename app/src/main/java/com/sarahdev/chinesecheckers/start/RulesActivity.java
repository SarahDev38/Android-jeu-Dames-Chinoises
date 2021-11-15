package com.sarahdev.chinesecheckers.start;

import android.os.Bundle;
import android.view.View;

import androidx.appcompat.app.AppCompatActivity;
import com.sarahdev.chinesecheckers.R;

public class RulesActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.rules_layout);
    }

    public void onClick_OK(View v){
        onBackPressed();
    }
}