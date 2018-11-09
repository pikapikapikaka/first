package test.android.com.weather;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.map.MyLocationConfiguration;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.StringReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import app.MyApplication;
import util.*;

public class MainActivity extends AppCompatActivity {
    private ImageButton imageUpdate,first_home,first_link,first_location;//home为选择城市按钮
    private ImageView first_weather,first_pic;
    private TextView first_name,first_city,first_time,first_water,first_pm_result,first_num;
    private TextView first_week,first_temp,first_wea,first_wind;
    private final int UPDATE_TODAY_WEATHER = 1;
    public List<Weather> threeCity = new ArrayList<>();
    Manager man = new Manager();
    List<ShowCity> cityList;
    MyApplication myApplication;
   // BDLocation lo = null;
    LocationClient mLoctionClient = null;//用户位置代理类
    MyLocationListener myListener = new MyLocationListener();//用来监听用户位置代理的类，具体定义我会在下面定义
    String code = null;//传递你当前的位置在哪里，因为我用了两个函数，靠全局变量来传递参数
    ImageView one_image,two_image,three_image;
    TextView one_day,one_tem,one_wind,one_weather,two_day,two_tem,two_wind,two_weather,three_day,three_tem,three_wind,three_weather;
    private ProgressBar bar1,bar2;//两个旋转按钮
    private ViewPagerAdater vpAdater;
    private ViewPager vp;
    private List<View> views;
    //MyLocationConfiguration.
    private void startLocate() {
        /**
         * 百度定位
         * */
        mLoctionClient = new LocationClient(getApplicationContext());//创建一个用户位置代理的类
        mLoctionClient.registerLocationListener(myListener);//注册其监听事件
        LocationClientOption option = new LocationClientOption();//设置一个用户位置代理的选项类
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//设置该模式为省电的模式
        option.setCoorType("bd0911");//设置坐标的类型
        option.setOpenGps(true);//设置是否打开GPS
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setLocationNotify(true);//设置是否当GPS有效时按照1S/1次频率输出GPS结果
        option.setIsNeedAddress(true);//，设置是否需要地址信息，默认不需要！！注意，这个很重要，我们是需要返回地址的
        option.setIsNeedLocationDescribe(true);//设置是否需要位置语义化结果
        option.setIsNeedLocationPoiList(true);//设置是否需要POI结果
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
        option.setIgnoreKillProcess(false);//默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        mLoctionClient.setLocOption(option);//将这些选项加载到用户位置代理类
        mLoctionClient.start();//开始位置代理

        /**
         * int span = 1000;
         * option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
         option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
         option.setOpenGps(true);//可选，默认false,设置是否使用gps
         option.setLocationNotify(true);//可选，默认false，设置是否当GPS有效时按照1S/1次频率输出GPS结果
         option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
         option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
         option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
         option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
         option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤GPS仿真结果，默认需要
         mLoctionClient.setLocOption(option);
         * */
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);//加载布局
        startLocate();//查询用户位置的函数
        init();
        imageUpdate.setOnClickListener(new View.OnClickListener() {//注册监听事件
            @Override
            public void onClick(View v) {
               // stratLocate();//开启百度地图定位功能
                bar2.setVisibility(View.VISIBLE);
                imageUpdate.setVisibility(View.INVISIBLE);
                Toast.makeText(MainActivity.this, "正在定位城市", Toast.LENGTH_SHORT);
                String cityCode = findWhere();//定位城市所在的代码
                if (cityCode == null) {
                    Toast.makeText(MainActivity.this, "自动定位失败，请手动输入城市", Toast.LENGTH_SHORT).show();
                }
                else {
                    if (NetUtil.getNetworkState(MainActivity.this) != NetUtil.NETWORK_NONE) {
                        queryWeather(cityCode);
                        Toast.makeText(MainActivity.this, "您当前所在城市："+code+"正在查询，请稍后", Toast.LENGTH_SHORT).show();
                    } else
                        Toast.makeText(MainActivity.this, "网络错误", Toast.LENGTH_SHORT).show();
                }
                bar2.setVisibility(View.INVISIBLE);
                imageUpdate.setVisibility(View.VISIBLE);
            }
        });
        if (NetUtil.getNetworkState(this) != NetUtil.NETWORK_NONE)
        {
            //Log.d("myweather","网络良好");
            Toast.makeText(MainActivity.this,"网络良好",Toast.LENGTH_SHORT).show();
        }
        else
        {
            Log.d("myweather","网络出错");
            Toast.makeText(MainActivity.this,"网络出错",Toast.LENGTH_SHORT).show();
        }
       // stratLocate();

        /**
         * 选择城市按钮注册监听事件
         * */
        first_home.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,select_city.class);
                //startActivity(intent);
                startActivityForResult(intent,1);

            }
        });

    }
    private class MyLocationListener implements BDLocationListener{
        @Override
        public void onReceiveLocation(BDLocation location) {
            Log.d("where","city:"+location.getCountry());
            code = location.getCity();
            code = code.replace("市","");
            code = code.replace("省","");
            Log.d("where","定位显示："+code);
        }
    }
    private String findWhere()
    {
        if(code != null)
            Log.d("where",code);
        else
            Log.d("where","no where");
        for(ShowCity city : cityList)
            if(city.getCityName().equals(code))
            {
                return city.getCityCode();
            }
        return null;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        if (requestCode == 1 && resultCode == RESULT_OK)
        {
            bar2.setVisibility(View.VISIBLE);
            imageUpdate.setVisibility(View.INVISIBLE);
            String newCityCode = data.getStringExtra("cityCode");
          //  Log.d("myWeather",newCityCode);
            if (NetUtil.getNetworkState(this) != NetUtil.NETWORK_NONE){
              //  Log.d("myWeather","网络良好");
                queryWeather(newCityCode);
            }
            else {
                Toast.makeText(this,"网络连接失败",Toast.LENGTH_SHORT).show();
            }
            bar2.setVisibility(View.INVISIBLE);
            imageUpdate.setVisibility(View.VISIBLE);
        }
    }

    /**
     * 初始化
     * */
    private void init()
    {
        /**
         * 找到界面各种控件
         * */
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
        bar1 = findViewById(R.id.title_update_progress);
        bar2 = findViewById(R.id.title_update_progress2);
        bar1.setVisibility(View.INVISIBLE);
        bar2.setVisibility(View.INVISIBLE);
        /**
         * 明天天气的控件
         * */
        one_day = findViewById(R.id.one_day);
        one_image = findViewById(R.id.one_image);
        one_tem = findViewById(R.id.one_tem);
        one_weather = findViewById(R.id.one_weather);
        one_wind = findViewById(R.id.one_wind);
        /**
         * 后天天气的控件
         * */
        two_day = findViewById(R.id.two_day);
        two_image = findViewById(R.id.two_image);
        two_tem = findViewById(R.id.two_tem);
        two_weather = findViewById(R.id.two_weather);
        two_wind = findViewById(R.id.two_wind);
        /**
         * 大后天天气的控件
         * */
        three_day = findViewById(R.id.three_day);
        three_image = findViewById(R.id.three_image);
        three_tem = findViewById(R.id.three_tem);
        three_weather = findViewById(R.id.three_weather);
        three_wind = findViewById(R.id.three_wind);


        SharedPreferences shared = getSharedPreferences("weather", Context.MODE_PRIVATE);
        /**
         * 用shanredPreference保存数据，具体的百度，以后做
         *
         * */
        String firstWea = shared.getString("first_wea","N/A");
        float firstNum = shared.getFloat("first_num",0);
        first_city.setText(shared.getString("first_city","N/A"));
        first_name.setText(shared.getString("first_name","N/A"));
        first_num.setText(String.valueOf(firstNum));
        first_pm_result.setText(shared.getString("first_pm_result","N/A"));
        first_temp.setText(shared.getString("first_temp","N/A"));
        first_time.setText(shared.getString("first_time","N/A"));
        first_water.setText(shared.getString("first_water","N/A"));
        first_week.setText(shared.getString("first_week","N/A"));
        first_wind.setText(shared.getString("first_wind","N/A"));
        first_wea.setText(firstWea);
        if(!firstWea.equals("N/A"))
            updateWeather(firstWea,first_weather);
        if(firstNum != 0)
            updatePic(firstNum,first_pic);

        one_wind.setText(shared.getString("wind0","N/A"));
        two_wind.setText(shared.getString("wind1","N/A"));
        three_wind.setText(shared.getString("wind2","N/A"));
        //four_wind.setText(shared.getString("wind1","N/A"));
        String oneW = shared.getString("weather0","N/A");
        String twoW = shared.getString("weather1","N/A");
        String threeW = shared.getString("weather2","N/A");
        one_weather.setText(oneW);
        two_weather.setText(twoW);
        three_weather.setText(threeW);
        //four_weather.setText(shared.getString("weather3","N/A"));
        one_tem.setText(shared.getString("temp0","N/A"));
        two_tem.setText(shared.getString("temp1","N/A"));
        three_tem.setText(shared.getString("temp2","N/A"));
        //four_tem.setText(shared.getString("temp1","N/A"));

        one_day.setText(shared.getString("day0","N/A"));
        two_day.setText(shared.getString("day1","N/A"));
        three_day.setText(shared.getString("day2","N/A"));
        //four_day.setText(shared.getString("day1","N/A"));
        //更新图片
        updateWeather(oneW,one_image);
        updateWeather(twoW,two_image);
        updateWeather(threeW,three_image);

        myApplication = (MyApplication) getApplication();
        cityList = myApplication.getShowCity();
        //Log.d("where","num:"+cityList.size());

    }
    /**
     * 更新UI
     * */
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
                    Weather wea = man.parseXML(responseStr,threeCity);//解析xml
                    /**
                    * 得到这几天天气以后，将其存储到sharedpreferece中
                    * */
                    SharedPreferences share = getSharedPreferences("weather",android.content.Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor= share.edit();
                    editor.putString("first_city",wea.getFirst_city());
                    editor.putString("first_name",wea.getFirst_name());
                    editor.putFloat("first_num",wea.getFirst_num());
                    editor.putString("first_time",wea.getFirst_time());
                    editor.putString("first_pm_result",wea.getFirst_pm_res());
                    editor.putString("first_water",wea.getFirst_water());
                    editor.putString("first_week",wea.getFirst_week());
                    editor.putString("first_temp",wea.getFirst_temp());
                    editor.putString("first_wea",wea.getFirst_wea());
                    editor.putString("first_wind",wea.getFirst_wind());

                    //将后三天的天气存放(其实存放了四天的天气)
                    for(int i = 0;i<3;i++)
                    {
                        Weather w = threeCity.get(i);
                        editor.putString("day"+i,w.getFirst_week().substring(3,6));
                        editor.putString("temp"+i,w.getFirst_temp());
                        editor.putString("weather"+i,w.getFirst_wea());
                        editor.putString("wind"+i,w.getFirst_wind());
                    }

                    editor.commit();//提交修改

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

    void updatePic(float num,ImageView img){
        if(num<50)
            img.setImageResource(R.drawable.biz_plugin_weather_0_50);
        else if(num<100)
            img.setImageResource(R.drawable.biz_plugin_weather_51_100);
        else if(num<150)
            img.setImageResource(R.drawable.biz_plugin_weather_101_150);
        else if(num<200)
            img.setImageResource(R.drawable.biz_plugin_weather_151_200);
        else if(num<300)
            img.setImageResource(R.drawable.biz_plugin_weather_201_300);
        else
            img.setImageResource(R.drawable.biz_plugin_weather_greater_300);

    }
    void updateWeather(String wea,ImageView img){
        if(wea.equals("晴"))
            img.setImageResource(R.drawable.biz_plugin_weather_qing);
        else if(wea.equals("雷阵雨"))
            img.setImageResource(R.drawable.biz_plugin_weather_leizhenyu);
        else if(wea.equals("雷阵雨冰雹"))
            img.setImageResource(R.drawable.biz_plugin_weather_leizhenyubingbao);
        else if(wea.equals("暴雪"))
            img.setImageResource(R.drawable.biz_plugin_weather_baoxue);
        else if(wea.equals("多云"))
            img.setImageResource(R.drawable.biz_plugin_weather_duoyun);
        else if(wea.equals("阴"))
            img.setImageResource(R.drawable.biz_plugin_weather_yin);
        else if(wea.equals("乌"))
            img.setImageResource(R.drawable.biz_plugin_weather_wu);
        else if(wea.equals("暴雨"))
            img.setImageResource(R.drawable.biz_plugin_weather_baoyu);
        else if(wea.equals("小雨"))
            img.setImageResource(R.drawable.biz_plugin_weather_xiaoyu);
        else if(wea.equals("小雪"))
            img.setImageResource(R.drawable.biz_plugin_weather_xiaoxue);
        else if(wea.equals("大暴雨"))
            img.setImageResource(R.drawable.biz_plugin_weather_dabaoyu);
        else if(wea.equals("雨夹雪"))
            img.setImageResource(R.drawable.biz_plugin_weather_yujiaxue);
        else if(wea.equals("阵雨"))
            img.setImageResource(R.drawable.biz_plugin_weather_zhenyu);
        else if(wea.equals("阵雪"))
            img.setImageResource(R.drawable.biz_plugin_weather_zhenxue);
        else if(wea.equals("中雨"))
            img.setImageResource(R.drawable.biz_plugin_weather_zhongyu);
        else if(wea.equals("中雪"))
            img.setImageResource(R.drawable.biz_plugin_weather_zhongxue);
        else if(wea.equals("大雪"))
            img.setImageResource(R.drawable.biz_plugin_weather_daxue);
        else if(wea.equals("大暴雨"))
            img.setImageResource(R.drawable.biz_plugin_weather_dabaoyu);
        else if(wea.equals("大雨"))
            img.setImageResource(R.drawable.biz_plugin_weather_dayu);

    }
    //更新今天天气
    void updateTodayWeather(Weather w){
        Log.d("update","yes");
        first_city.setText(w.getFirst_city());
        first_wea.setText(w.getFirst_wea());
        first_wind.setText(w.getFirst_wind());
        first_week.setText(w.getFirst_week());
        first_water.setText(w.getFirst_water());
        first_time.setText(w.getFirst_time());
        first_temp.setText(w.getFirst_temp());
        first_pm_result.setText(w.getFirst_pm_res());
        first_name.setText(w.getFirst_name());
        first_num.setText(String.valueOf(w.getFirst_num()));
        updatePic(w.getFirst_num(),first_pic);
        updateWeather(w.getFirst_wea(),first_weather);
        Toast.makeText(MainActivity.this,"更新成功",Toast.LENGTH_SHORT).show();

        updateNextDay();

    }
    /**
     * 更新未来几天天气
     * */
    private void updateNextDay()
    {
        //明天
        Weather wea = threeCity.get(0);
        String con = wea.getFirst_week();
        if(con != null)
        {
            Log.d("liumeng",con);
        con = con.substring(con.length()-3,con.length());
        setFirstText(one_day,con);
        setFirstText(one_tem,wea.getFirst_temp());
        setFirstText(one_wind,wea.getFirst_wind());
        setFirstText(one_weather,wea.getFirst_wea());
        updateWeather(wea.getFirst_wea(),one_image);
        //后天
        wea = threeCity.get(1);
        con = wea.getFirst_week();
        con = con.substring(con.length()-3,con.length());
        setFirstText(two_day,con);
        setFirstText(two_tem,wea.getFirst_temp());
        setFirstText(two_wind,wea.getFirst_wind());
        setFirstText(two_weather,wea.getFirst_wea());
        updateWeather(wea.getFirst_wea(),two_image);
        //大后天
        wea = threeCity.get(2);
        con = wea.getFirst_week();
        con = con.substring(con.length()-3,con.length());
        setFirstText(three_day,con);
        setFirstText(three_tem,wea.getFirst_temp());
        setFirstText(three_wind,wea.getFirst_wind());
        setFirstText(three_weather,wea.getFirst_wea());
        updateWeather(wea.getFirst_wea(),three_image);}
        else
            Log.d("liumeng","error");



    }
    private void setFirstText(TextView text,String str)
    {
        text.setText(str);
    }

}
