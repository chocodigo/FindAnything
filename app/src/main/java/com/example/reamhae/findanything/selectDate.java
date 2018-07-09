package com.example.reamhae.findanything;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.Window;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.TextView;
import android.widget.Toast;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;

public class selectDate extends AppCompatActivity implements View.OnClickListener {



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.activity_select_date);

        Button check_btn=(Button)findViewById(R.id.select_btn);
        check_btn.setOnClickListener(this);
    }





    @Override
    public void onClick(View v) {
        if(v.getId()==R.id.select_btn){
            int year, month, day;
            DatePicker date_picker=(DatePicker) findViewById(R.id.get_date);
            Intent main_intent=new Intent(this,MainActivity.class);

            year=date_picker.getYear();
            month=date_picker.getMonth()+1;
            day=date_picker.getDayOfMonth();

            main_intent.putExtra("year",year);
            main_intent.putExtra("month",month);
            main_intent.putExtra("day",day);

            setResult(RESULT_OK,main_intent);
            finish();
        }
    }
}
