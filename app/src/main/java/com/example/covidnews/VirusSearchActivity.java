package com.example.covidnews;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

public class VirusSearchActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_virus_search);
        Button btn1 = (Button)findViewById(R.id.m_btn1);
        LinearLayout layout = (LinearLayout)findViewById(R.id.mainlay);
        btn1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(VirusSearchActivity.this,VirusShowActivity.class);
                Bundle bundle = new Bundle();
                EditText item = (EditText)findViewById(R.id.items);
                bundle.putString("name",item.getText().toString());
                intent.putExtras(bundle);
                startActivity(intent);
            }
        });
    }
}