package app;

import android.app.Application;
import android.os.Environment;
import android.util.Log;

import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import util.City;
import util.CityDB;
import util.ShowCity;

public class MyApplication extends Application {
    private static final String TAG = "MyApp";
    private static MyApplication mApplication;
    private CityDB mCityDB;//初始化一个全局数据库
    private List<City> mCityList;//创建城市链表
    private List<ShowCity> showCity;//返回的界面

    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG,"MyApplication->Oncreate");
        mApplication = this;
        mCityDB = openCityDB();//查询出来该数据库，改数据库在程序运行期间一直访问
        initCityList();//从数据库访问出数据
            //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(this);
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置您使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);

    }
    private void initCityList()
    {
        mCityList = new ArrayList<>();
        //耗时的操作由子线程完成
        try {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    preparedCityList();
                }
            }).start();
        }
        catch (Exception e)
        {
            e.printStackTrace();
        }
    }
    private boolean preparedCityList(){
        mCityList = mCityDB.getAllCity();
        showCity = new ArrayList<>();//初始化City
        int i = 0;
        for(City city : mCityList){
            i++;
            String cityName = city.getCity();//返回城市名称
            String cityCode = city.getNumber();//返回城市编号
            ShowCity show = new ShowCity(cityName,cityCode);
            showCity.add(show);//在简易城市列表中增加这个选项
           // Log.d(TAG,cityCode+":"+cityName);
        }
//        for(ShowCity s : showCity)
//            Log.d(TAG,s.getCityName()+":"+s.getCityCode());
        return true;

    }

    public List<ShowCity> getShowCity() {
        return showCity;
    }

    public List<City> getCityList()
    {
        return mCityList;
    }
    public static MyApplication getInstance(){
        return mApplication;
    }
    private CityDB openCityDB(){
        /**
         * 将数据库保存到data文件夹下的自己的目录里
         * */
        String path = "/data"
                + Environment.getDataDirectory().getAbsolutePath()
                + File.separator + getPackageName()
                +File.separator+"database1"
                +File.separator+CityDB.CITY_DB_NAME;
        File db = new File(path);
        Log.d(TAG,path);
        //数据库不存在
        if(!db.exists()){
            String pathFolder = "/data"
                    + Environment.getDataDirectory().getAbsolutePath()
                    + File.separator + getPackageName()
                    +File.separator+"database1"+File.separator;
            File dirFirstFolder = new File(pathFolder);
            if(!dirFirstFolder.exists()){//该目录不存在
                dirFirstFolder.mkdirs();
            }
            Log.i(TAG,"database is not exists");
            try {
                InputStream is = getAssets().open("city.db");//读取assets里的数据库文件
                FileOutputStream fos = new FileOutputStream(db);//在data目录里的数据库
                int len = -1;
                byte[] buffer = new byte[1024];
                while ((len = is.read(buffer)) != -1)
                {
                    fos.write(buffer,0,len);
                    fos.flush();
                }
                fos.close();
                is.close();
            }
            catch (Exception e)
            {
                e.printStackTrace();
            }
        }
        return new CityDB(this,path);
    }
}
