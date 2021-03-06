package com.example.covidnews;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.covidnews.globalviews.GlobalActivity;
import com.example.covidnews.provinceviews.ProvinceActivity;
import com.google.android.material.bottomnavigation.BottomNavigationView;

public class ShowDataActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_data);
        Button btn1 = (Button)findViewById(R.id.rect_btn1);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowDataActivity.this, ProvinceActivity.class);
                startActivity(intent);
            }
        });
        Button btn2 = (Button)findViewById(R.id.rect_btn2);
        btn2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowDataActivity.this, GlobalActivity.class);
                startActivity(intent);
            }
        });
        Button btn3 = (Button)findViewById(R.id.rect_btn3);
        btn3.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ShowDataActivity.this, VirusSearchActivity.class);
                startActivity(intent);
            }
        });
    }
}