package com.example.reamhae.findanything;

import android.app.ActionBar;
import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.os.Message;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.simple.JSONArray;
import org.json.simple.JSONObject;
import org.json.simple.parser.JSONParser;
import org.json.simple.parser.ParseException;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.text.SimpleDateFormat;
import java.util.Date;


public class searchResult extends AppCompatActivity {

    String search_key="4e624f4d4e636f63363049706a4749";
    String img_key="687a6f6c47636f6337354473687442";
    String from_date;
    String to_date;

    itemInformation[] item_info = new itemInformation[5];

    ProgressDialog mProgress;

    Thread_for_item mThread;
    Thread_for_img imgThread;

    long total_count;
    LinearLayout page_btn_area;

    int page_num;

    int before_page;

    int start;
    int end;

    static final String STATE_PAGE="state_page";
    static final String STATE_DATE="state_date_check";
    static final String STATE_BTN="state_btn_check";
    static final String STATE_FROM="state_from_date";
    static final String STATE_TO="state_to_date";

    int date_check=0;
    int btn_check=0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_result);

        Intent date_check_intent = getIntent();

        date_check = date_check_intent.getIntExtra("date_check",0);

        if(savedInstanceState!=null){
            page_num=savedInstanceState.getInt(STATE_PAGE);
            date_check=savedInstanceState.getInt(STATE_DATE);
            btn_check=savedInstanceState.getInt(STATE_BTN);
            from_date=savedInstanceState.getString(STATE_FROM);
            to_date=savedInstanceState.getString(STATE_TO);

            start=page_num;
        }else{
            page_num=1;
        }

        if(date_check != 0) {
            start=page_num;
            before_page = start; //현재 페이지 정보 저장
            end = start + 999;
        }
        else{
            start = 5 * page_num - 4;
            end = 5 * page_num;
        }

        mProgress=ProgressDialog.show(searchResult.this,"Wait","Downloading...");
        page_btn_area=(LinearLayout)findViewById(R.id.page_area);

        for(int i=0; i<5; i++)
            item_info[i]=new itemInformation();
        mThread=new Thread_for_item();
        imgThread=new Thread_for_img();
        mThread.start();

        try{
            mThread.join();

        }catch(InterruptedException e){
            e.printStackTrace();
        }

        imgThread.start();
    }

    @Override
    public void onSaveInstanceState(Bundle savedInstanceState){

        savedInstanceState.putInt(STATE_PAGE,page_num);
        savedInstanceState.putInt(STATE_DATE,date_check);
        savedInstanceState.putInt(STATE_BTN,btn_check);
        savedInstanceState.putString(STATE_FROM,from_date);
        savedInstanceState.putString(STATE_TO,to_date);

        super.onSaveInstanceState(savedInstanceState);
    }

    private class Thread_for_item extends Thread{
        Intent item_intent=getIntent();

        String addr="http://openAPI.seoul.go.kr:8088/";
        String type="/json";
        String service="/SearchLostArticleService";
        String start_index="/"+start;
        String end_index="/"+end+"/";
        String cate=item_intent.getStringExtra("item_field");
        String wb_code=item_intent.getStringExtra("location_ID");
        String get_name=item_intent.getStringExtra("item_name");
        String r_from_date = item_intent.getStringExtra("from_date");
        String r_to_date = item_intent.getStringExtra("to_date");

        int check_date = item_intent.getIntExtra("date_check",0);

        Date d_from_date;
        Date d_to_date;

        String c_cate=URLEncoder.encode(cate);
        String c_get_name=URLEncoder.encode(get_name);

        String item_addr=addr+search_key+type+service+start_index+end_index+c_cate+wb_code+c_get_name;

        String result;

        @Override
        public void run(){

                StringBuilder html = new StringBuilder();
                try {
                    URL url = new URL(item_addr);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    if(check_date!=0) {
                        d_from_date = new SimpleDateFormat("yyyy-MM-dd").parse(r_from_date);
                        d_to_date = new SimpleDateFormat("yyyy-MM-dd").parse(r_to_date);
                    }
                    else{
                        d_from_date = null;
                        d_to_date = null;
                    }
                    if (conn != null) {
                        conn.setConnectTimeout(1000);
                        conn.setUseCaches(false);
                        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                            for (; ; ) {
                                String line = br.readLine();
                                if (line == null) break;
                                html.append(line + '\n');
                            }
                            br.close();
                            result = html.toString();
                            JSON_parser(result, d_from_date, d_to_date);
                        }
                        conn.disconnect();
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
        }
    }

    private class Thread_for_img extends Thread {
        String addr="http://openAPI.seoul.go.kr:8088/";
        String type="/json";
        String service="/SearchLostArticleImageService";
        String start_index="/1";
        String end_index="/5/";

        String result;

        @Override
        public void run() {

            try {
                for(int i=0; i<5; i++){
                    StringBuilder html= new StringBuilder();
                    String item_addr=addr+img_key+type+service+start_index+end_index+item_info[i].getID();
                    URL url = new URL(item_addr);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    if (conn != null) {
                        conn.setConnectTimeout(1000);
                        conn.setUseCaches(false);
                        if (conn.getResponseCode() == HttpURLConnection.HTTP_OK) {
                            BufferedReader br = new BufferedReader(new InputStreamReader(conn.getInputStream()));
                            for (; ; ) {
                                String line = br.readLine();
                                if (line == null) break;
                                html.append(line + '\n');
                            }
                            br.close();
                            result = html.toString();
                            JSON_img_parser(result,i);
                        }
                        conn.disconnect();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }

            mAfterDown.sendEmptyMessage(0);
        }
    }

    Handler mAfterDown=new Handler(){
        public void handleMessage(Message msg){
            mProgress.dismiss();
            print_Listview();
            if(date_check==0)
                make_page_btn(page_num);
            else
                date_make_btn();
        }
    };

    public void JSON_parser(String before_result, Date d_from_date, Date d_to_date){
        try{

            JSONParser jsonparser = new JSONParser();
            JSONObject jsonObject = (JSONObject)jsonparser.parse(before_result);
            JSONObject json = (JSONObject)jsonObject.get("SearchLostArticleService");
            total_count=(long)json.get("list_total_count");
            JSONArray ja=(JSONArray)json.get("row");
            JSONObject order;

            int i = 0;

            if(d_from_date!=null && d_to_date!=null){
                SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");

                if(start==1) {
                    from_date = sdf.format(d_from_date).toString();
                    to_date = sdf.format(d_to_date).toString();
                }


                while(true){
                    order = (JSONObject) ja.get(page_num);
                    String item_date = order.get("GET_DATE").toString();

                    if(i>=5) {
                        break;
                    }
                    if(range(from_date,to_date,item_date)){
                        item_info[i].setID(order.get("ID").toString());
                        item_info[i].setTake_place(order.get("TAKE_PLACE").toString());
                        item_info[i].setName(order.get("GET_NAME").toString());
                        item_info[i].setDate(order.get("GET_DATE").toString());
                        item_info[i].setPosition(order.get("GET_POSITION").toString());

                        i++;
                    }
                    else if((item_date.compareTo(from_date))<0 || start>ja.size()-1) {
                        btn_check = 1;
                        break;
                    }
                    page_num++;
                }
            }

            else {
                for (; i < ja.size(); i++) {
                    order = (JSONObject) ja.get(i);

                    item_info[i].setID(order.get("ID").toString());
                    item_info[i].setTake_place(order.get("TAKE_PLACE").toString());
                    item_info[i].setName(order.get("GET_NAME").toString());
                    item_info[i].setDate(order.get("GET_DATE").toString());
                    item_info[i].setPosition(order.get("GET_POSITION").toString());
                }
            }
        }catch (ParseException e) {
            e.printStackTrace();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void JSON_img_parser(String before_result, int i){
        try{

            JSONParser jsonparser = new JSONParser();
            JSONObject jsonObject = (JSONObject)jsonparser.parse(before_result);
            JSONObject json = (JSONObject)jsonObject.get("SearchLostArticleImageService");
            JSONArray ja=(JSONArray)json.get("row");
            JSONObject order;
            Drawable img;

            order = (JSONObject)ja.get(0);
            img=LoadImageFromWebOperations(order.get("IMAGE_URL").toString());

            if(img!=null) {
                Bitmap b = ((BitmapDrawable)img).getBitmap();
                Bitmap bitmapResized = Bitmap.createScaledBitmap(b, 100, 100, false);
                img = new BitmapDrawable(getResources(),bitmapResized);
                item_info[i].setIcon(img);
            }
            else
                item_info[i].setIcon(ContextCompat.getDrawable(this, R.drawable.no_image));

        }catch (ParseException e) {
            e.printStackTrace();

        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public void print_Listview(){
        ListView listview;
        searchListView_Adapter adapter;

        try {
            adapter = new searchListView_Adapter();

            listview = (ListView) findViewById(R.id.item_listview);

            listview.setAdapter(adapter);

            //listview에 추가
            for(int i=0; i<5; i++) {
                adapter.addItem(item_info[i].getIcon(), item_info[i].getName(), item_info[i].getDate());
            }

        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public Drawable LoadImageFromWebOperations(String url){
        try{
            InputStream is = (InputStream) new URL(url).getContent();
            Drawable d = Drawable.createFromStream(is,"src name");

            return d;
        }catch (Exception e){
            e.printStackTrace();
            return null;
        }
    }

    public void make_page_btn(int page_num) {

        if(page_num>1)
        {
            Button prev_btn=new Button(this);
            prev_btn.setText("◀");
            prev_btn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    minus();
                    recreate();
                }
            });
            page_btn_area.addView(prev_btn, new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT));
        }

        if(page_num<total_count){
            Button next_btn=new Button(this);
            next_btn.setText("▶");
            next_btn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    plus();
                    recreate();
                }
            });
            page_btn_area.addView(next_btn, new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT));
        }
    }


    public void date_make_btn(){
        if(start>1){
            Button prev_btn=new Button(this);
            prev_btn.setText("◀");
            prev_btn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    make_pagenum();
                    recreate();
                }
            });
            page_btn_area.addView(prev_btn, new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT));
        }

        if(btn_check==0){
            Button next_btn=new Button(this);
            next_btn.setText("▶");
            next_btn.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v){
                    recreate();
                }
            });
            page_btn_area.addView(next_btn, new LinearLayout.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT, ActionBar.LayoutParams.WRAP_CONTENT));
        }
    }



    void test(String id){
        Toast.makeText(this,id,Toast.LENGTH_SHORT).show();
    }

    void plus(){
        page_num++;
    }
    void minus(){
        page_num--;
    }
    void make_pagenum(){page_num = before_page;}

    boolean range(String from_date, String to_date, String item_date){
        if((item_date.compareTo(to_date))<=0 && (item_date.compareTo(from_date))>0)
            return true;
        else if((item_date.compareTo(to_date))<0 && (item_date.compareTo(from_date))>=0)
            return true;
        else if((item_date.compareTo(to_date))==0 && (item_date.compareTo(from_date))==0)
            return true;
        else
            return false;
    }
}

