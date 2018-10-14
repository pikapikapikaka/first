package test.android.com.weather;

import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;

import util.*;

public class MainActivity extends AppCompatActivity {
    private ImageButton imageUpdate,first_home,first_link,first_location;
    private ImageView first_weather,first_pic;
    private TextView first_name,first_city,first_time,first_water,first_pm_result,first_num;
    private TextView first_week,first_temp,first_wea,first_wind;
    private final int UPDATE_TODAY_WEATHER = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);//加载布局
        init();
        imageUpdate.setOnClickListener(new View.OnClickListener() {//注册监听事件
            @Override
            public void onClick(View v) {
                SharedPreferences sharedPreference = getSharedPreferences("config",MODE_PRIVATE);
                String cityCode = sharedPreference.getString("main_city_code","101010100");
                Log.d("myWeather",cityCode);
                if(NetUtil.getNetworkState(MainActivity.this) != NetUtil.NETWORK_NONE) {
                    queryWeather(cityCode);
                    Toast.makeText(MainActivity.this,"请稍后",Toast.LENGTH_SHORT).show();
                }
                else
                    Toast.makeText(MainActivity.this,"网络错误",Toast.LENGTH_SHORT).show();
            }
        });
        if (NetUtil.getNetworkState(this) != NetUtil.NETWORK_NONE)
        {
            Log.d("myweather","网络良好");
            Toast.makeText(MainActivity.this,"网络良好",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Log.d("myweather","网络出错");
            Toast.makeText(MainActivity.this,"网络出错",Toast.LENGTH_SHORT).show();
        }

    }
    private void init()
    {
        imageUpdate = (ImageButton)findViewById(R.id.first_update);//找到更新按钮
        first_city = (TextView) findViewById(R.id.first_city);
        first_home = (ImageButton)findViewById(R.id.first_home);
        first_link = (ImageButton)findViewById(R.id.first_link);
        first_location = (ImageButton)findViewById(R.id.first_location);
        first_pic = (ImageView)findViewById(R.id.first_pic);
        first_weather = (ImageView)findViewById(R.id.first_weather);
        first_name = (TextView)findViewById(R.id.first_name);
        first_num = (TextView)findViewById(R.id.first_num);
        first_pm_result = (TextView)findViewById(R.id.first_pm_res);
        first_temp = (TextView)findViewById(R.id.first_temp);
        first_time = (TextView)findViewById(R.id.first_time);
        first_water = (TextView)findViewById(R.id.first_water);
        first_week = (TextView)findViewById(R.id.first_week);
        first_wind = (TextView)findViewById(R.id.first_wind);
        first_wea = (TextView)findViewById(R.id.first_wea);

        first_city.setText("N/A");
        first_name.setText("N/A");
        first_num.setText("N/A");
        first_pm_result.setText("N/A");
        first_temp.setText("N/A");
        first_time.setText("N/A");
        first_water.setText("N/A");
        first_week.setText("N/A");
        first_wind.setText("N/A");
        first_wea.setText("N/A");

    }
    private Handler mHandler = new Handler(){
        public void handleMessage(android.os.Message msg){
            switch (msg.what) {
                case UPDATE_TODAY_WEATHER:
                    updateTodayWeather((Weather) msg.obj);
                    //Log.d("pika","yes");
                    break;
                default:
                    break;

            }
        }
    };
    private void queryWeather(String cityCode)
    {
        final String address = "http://wthrcdn.etouch.cn/WeatherApi?citykey="+cityCode;
        Log.d("myWeather",address);
        new Thread(new Runnable() {
            @Override
            public void run() {
                HttpURLConnection con = null;
                try {
                    URL url = new URL(address);
                    con = (HttpURLConnection) url.openConnection();//建立链接
                    con.setRequestMethod("GET");//设置方法
                    con.setConnectTimeout(8000);//设置超时链接时间
                    con.setReadTimeout(8000);//设置超时读取时间
                    InputStream in = con.getInputStream();//得到返回的输出流
                    BufferedReader reader = new BufferedReader(new InputStreamReader(in));
                    StringBuilder response = new StringBuilder();
                    String str;
                    while((str=reader.readLine()) != null){
                        response.append(str);
                        //Log.d("myWeather",str);
                    }
                    String responseStr = response.toString();
                    Log.d("myWeather",responseStr);
                    Weather wea = parseXML(responseStr);//解析xml
                    Message msg = new Message();
                    msg.what = UPDATE_TODAY_WEATHER;
                    msg.obj= wea;
                    mHandler.sendMessage(msg);
                    Log.e("message",wea.toString());
                }
                catch (Exception e)
                {
                    e.printStackTrace();
                }
                finally {
                    if(con != null){
                        con.disconnect();
                    }
                }
            }
        }).start();
    }
    private Weather parseXML(String xmldata){
        Weather wea = null;
        int high = 0,low = 0,type = 0,date = 0,fengxiang = 0,fengli = 0;
        try {
            XmlPullParserFactory fac = XmlPullParserFactory.newInstance();//产生工厂实例
            XmlPullParser xmlPullParser = fac.newPullParser();//产生一个解析类
            xmlPullParser.setInput(new StringReader(xmldata));//准备解析xml
            String str = null;
            int eventType = xmlPullParser.getEventType();
            Log.d("myWeather","parseXML");
            while (eventType != XmlPullParser.END_DOCUMENT){
                switch (eventType) {
                    //判断当前事件
                    case XmlPullParser.START_DOCUMENT:
                        break;
                    case XmlPullParser.START_TAG:
                        if(xmlPullParser.getName().equals("resp"))
                        {
                            wea = new Weather();
                        }
                        else if (xmlPullParser.getName().equals("city")) {
                            eventType = xmlPullParser.next();
                            str = xmlPullParser.getText();
                            Log.d("dd", str);
                            wea.setFirst_city(str);
                            wea.setFirst_name(str);
                        } else if (xmlPullParser.getName().equals("updatetime")) {
                            eventType = xmlPullParser.next();
                            str = xmlPullParser.getText();
                            Log.d("dd", str);
                            wea.setFirst_time(str);
                        } else if (xmlPullParser.getName().equals("wendu")) {
                            eventType = xmlPullParser.next();
                            wea.setFirst_temp(xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("fengli")&&(fengli++ == 0)) {
                            eventType = xmlPullParser.next();
                            wea.setFirst_wind(xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("shidu")) {
                            eventType = xmlPullParser.next();
                            wea.setFirst_water(xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("fengxiang")&&(fengxiang++ == 0)) {
                            //String wind = wea.getFirst_wind();
                            eventType = xmlPullParser.next();
                            String wind = wea.getFirst_wind();
                            wind = xmlPullParser.getText()+wind;
                            wea.setFirst_wind(wind);
                        } else if (xmlPullParser.getName().equals("pm25")) {
                            eventType = xmlPullParser.next();
                            wea.setFirst_num(Float.parseFloat(xmlPullParser.getText()));
                        } else if (xmlPullParser.getName().equals("quality")) {
                            eventType = xmlPullParser.next();
                            wea.setFirst_pm_res(xmlPullParser.getText());
                        } else if(xmlPullParser.getName().equals("date")&&(date++ == 0)){
                            eventType = xmlPullParser.next();
                            wea.setFirst_week(xmlPullParser.getText());

                        }
                        else  if(xmlPullParser.getName().equals("high")&&(high++ == 0)){
                            eventType = xmlPullParser.next();
                            wea.setFirst_temp(xmlPullParser.getText().substring(2));
                        }
                        else if(xmlPullParser.getName().equals("low")&&(low++ == 0)){
                            String w = wea.getFirst_temp();
                            eventType = xmlPullParser.next();
                            w = xmlPullParser.getText().substring(2)+"~"+w;
                            wea.setFirst_temp(w);
                        }
                        else if(xmlPullParser.getName().equals("type")&&(type++ == 0)){
                            eventType = xmlPullParser.next();
                            wea.setFirst_wea(xmlPullParser.getText());
                        }
                        /**
                         * 不能有else{eventType = xmlPillParser.next()}
                         * 否则两个正标签在一起，会忽略
                         * */

                        break;
                    case XmlPullParser.END_TAG:
                        break;
                }
                eventType = xmlPullParser.next();
                }
            }

        catch (Exception e)
        {
            e.printStackTrace();
        }
        return wea;

    }
    void updatePic(float num){
        if(num<50)
            first_pic.setImageResource(R.drawable.biz_plugin_weather_0_50);
        else if(num<100)
            first_pic.setImageResource(R.drawable.biz_plugin_weather_51_100);
        else if(num<150)
            first_pic.setImageResource(R.drawable.biz_plugin_weather_101_150);
        else if(num<200)
            first_pic.setImageResource(R.drawable.biz_plugin_weather_151_200);
        else if(num<300)
            first_pic.setImageResource(R.drawable.biz_plugin_weather_201_300);
        else
            first_weather.setImageResource(R.drawable.biz_plugin_weather_greater_300);

    }
    void updateWeather(String wea){
        if(wea.equals("晴"))
            first_weather.setImageResource(R.drawable.biz_plugin_weather_qing);
        else if(wea.equals("雷阵雨"))
            first_weather.setImageResource(R.drawable.biz_plugin_weather_leizhenyu);
        else if(wea.equals("雷阵雨冰雹"))
            first_weather.setImageResource(R.drawable.biz_plugin_weather_leizhenyubingbao);
        else if(wea.equals("暴雪"))
            first_weather.setImageResource(R.drawable.biz_plugin_weather_baoxue);
        else if(wea.equals("多云"))
            first_weather.setImageResource(R.drawable.biz_plugin_weather_duoyun);
        else if(wea.equals("阴"))
            first_weather.setImageResource(R.drawable.biz_plugin_weather_yin);
        else if(wea.equals("乌"))
            first_weather.setImageResource(R.drawable.biz_plugin_weather_wu);
        else if(wea.equals("暴雨"))
            first_weather.setImageResource(R.drawable.biz_plugin_weather_baoyu);
        else if(wea.equals("小雨"))
            first_weather.setImageResource(R.drawable.biz_plugin_weather_xiaoyu);
        else if(wea.equals("小雪"))
            first_weather.setImageResource(R.drawable.biz_plugin_weather_xiaoxue);
        else if(wea.equals("大暴雨"))
            first_weather.setImageResource(R.drawable.biz_plugin_weather_dabaoyu);
        else if(wea.equals("雨夹雪"))
            first_weather.setImageResource(R.drawable.biz_plugin_weather_yujiaxue);
        else if(wea.equals("阵雨"))
            first_weather.setImageResource(R.drawable.biz_plugin_weather_zhenyu);
        else if(wea.equals("阵雪"))
            first_weather.setImageResource(R.drawable.biz_plugin_weather_zhenxue);
        else if(wea.equals("中雨"))
            first_weather.setImageResource(R.drawable.biz_plugin_weather_zhongyu);
        else if(wea.equals("中雪"))
            first_weather.setImageResource(R.drawable.biz_plugin_weather_zhongxue);
        else if(wea.equals("大雪"))
            first_weather.setImageResource(R.drawable.biz_plugin_weather_daxue);
        else if(wea.equals("大暴雨"))
            first_weather.setImageResource(R.drawable.biz_plugin_weather_dabaoyu);
        else if(wea.equals("大雨"))
            first_weather.setImageResource(R.drawable.biz_plugin_weather_dayu);

    }
    void updateTodayWeather(Weather w){
        Log.d("update","yes");
        first_city.setText(w.getFirst_city());
        first_wea.setText(w.getFirst_wea());
        first_wind.setText(w.getFirst_wind());
        first_week.setText("今天是"+w.getFirst_week());
        first_water.setText("湿度："+w.getFirst_water());
        first_time.setText("今天"+w.getFirst_time()+"发布");
        first_temp.setText(w.getFirst_temp());
        first_pm_result.setText(w.getFirst_pm_res());
        first_name.setText(w.getFirst_city()+"天气");
        first_num.setText(String.valueOf(w.getFirst_num()));
        updatePic(w.getFirst_num());
        updateWeather(w.getFirst_wea());
        Toast.makeText(MainActivity.this,"更新成功",Toast.LENGTH_SHORT).show();


    }

}
