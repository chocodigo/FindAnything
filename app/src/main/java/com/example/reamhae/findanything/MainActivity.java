package com.example.reamhae.findanything;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Date;

public class MainActivity extends AppCompatActivity implements View.OnClickListener {

    String from_date = "";
    String to_date = "";
    String today;

    int date_check = 0;

    final Context context = this;

    int check_from = 0;
    int check_to = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Button to_date_btn = (Button) findViewById(R.id.to_date_btn);
        Button from_date_btn = (Button) findViewById(R.id.from_date_btn);
        Button search_btn = (Button) findViewById(R.id.search_btn);

        to_date_btn.setOnClickListener(this);
        from_date_btn.setOnClickListener(this);
        search_btn.setOnClickListener(this);

    }


    @Override
    public void onClick(View v) {
        Intent select_intent = new Intent(this, selectDate.class);
        Intent search_intent = new Intent(this, searchResult.class);
        try {
            switch (v.getId()) {
                case R.id.from_date_btn:
                    startActivityForResult(select_intent, 0);
                    break;
                case R.id.to_date_btn:
                    startActivityForResult(select_intent, 1);
                    break;
                case R.id.search_btn:
                    Spinner item_spinner = (Spinner) findViewById(R.id.items_field);
                    Spinner location_spinner = (Spinner) findViewById(R.id.lost_location);

                    EditText item_name_field = (EditText) findViewById(R.id.item_name_field);

                    String item_field = item_spinner.getSelectedItem().toString();
                    String location_field = location_spinner.getSelectedItem().toString();
                    String location_ID = "";
                    String item_name = item_name_field.getText().toString();

                    switch (location_field) {
                        case "지하철(1호선 ~ 4호선)":
                            location_ID = "/s1/";
                            break;
                        case "지하철(5호선 ~ 8호선)":
                            location_ID = "/s2/";
                            break;
                        case "지하철(9호선)":
                            location_ID = "/s4/";
                            break;
                        case "버스":
                            location_ID = "/b1/";
                            break;
                        case "마을버스":
                            location_ID = "/b2/";
                            break;
                        case "법인택시":
                            location_ID = "/t1/";
                            break;
                        case "개인택시":
                            location_ID = "/t2/";
                            break;
                        case "코레일":
                            location_ID = "/s3/";
                            break;
                    }

                    search_intent.putExtra("item_field", item_field);
                    search_intent.putExtra("location_ID", location_ID);
                    search_intent.putExtra("item_name", item_name);
                    search_intent.putExtra("from_date", from_date);
                    search_intent.putExtra("to_date", to_date);
                    search_intent.putExtra("date_check",date_check);

                    startActivity(search_intent);
                    break;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        if (resultCode == Activity.RESULT_OK) {
            int year, month, day;
            TextView to_date_field = (TextView) findViewById(R.id.to_date_field);
            TextView from_date_field = (TextView) findViewById(R.id.from_date_field);
            date_check = 1;

            year = data.getIntExtra("year", 0);
            month = data.getIntExtra("month", 0);
            day = data.getIntExtra("day", 0);

            if (requestCode == 0) {
                from_date = String.format("%d-%02d-%02d",year,month,day);
                check_from = 1;
                if(check_date(from_date))
                {
                    printError();
                    from_date=today;
                }
                from_date_field.setText(from_date);
            } else if (requestCode == 1) {
                to_date = String.format("%d-%02d-%02d",year,month,day);
                check_to = 1;
                if(check_date(to_date))
                {
                    printError();
                    to_date=today;
                }
                to_date_field.setText(to_date);
            }
            if (check_from != 0 && check_to != 0) {
                if (to_date.compareTo(from_date) < 0) {
                    AlertDialog.Builder alert2 = new AlertDialog.Builder(context);

                    alert2.setMessage("첫 번째 칸에 입력한 날짜가 두 번째 칸 날짜보다 같거나 오래된 날짜여야 합니다.");
                    alert2.setPositiveButton("확인", null);
                    AlertDialog alertDialog2 = alert2.create();
                    alertDialog2.setTitle("경고");
                    alertDialog2.show();
                    to_date = from_date;
                    to_date_field.setText(to_date);
                }
            }
        } else {
            Toast.makeText(this, "오류", Toast.LENGTH_SHORT).show();
        }
    }

    public boolean check_date(String c_date){
        Date date = new Date();
        Date actdate;

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        today=sdf.format(date).toString();
        try {
            actdate = new SimpleDateFormat("yyyy-MM-dd").parse(c_date);
        }catch(Exception e){ actdate = null;}

        if((actdate != null) && (actdate.getTime()>date.getTime()))
            return true;
        else
            return false;
    }
    public void printError(){
        AlertDialog.Builder alert2 = new AlertDialog.Builder(context);

        alert2.setMessage("현재 날짜보다 큰 값일 수 없습니다.");
        alert2.setPositiveButton("확인", null);
        AlertDialog alertDialog2 = alert2.create();
        alertDialog2.setTitle("경고");
        alertDialog2.show();
    }
}
