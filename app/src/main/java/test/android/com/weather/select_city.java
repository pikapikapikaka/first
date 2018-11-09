package test.android.com.weather;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.zip.Inflater;

import app.MyApplication;
import util.City;
import util.ShowCity;

public class select_city extends AppCompatActivity {
    ImageView home_back,home_search;
    ListView home_list;
    List<City> city;//所有城市
    ArrayList<String> citylist = new ArrayList<>();//用来存名字的
    List<String> cityCode = new ArrayList<>();
    List<City> temp = new ArrayList<>();
    private void initCityList(final List<City> currentCity)
    {
        home_list.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return currentCity.size();
            }

            @Override
            public Object getItem(int position) {
                return currentCity.get(position);
            }

            @Override
            public long getItemId(int position) {
                return 0;
            }

            @Override
            public View getView(int position, View convertView, ViewGroup parent) {
                View view = null;
                if (convertView == null){
                    LayoutInflater inflater = getLayoutInflater();
                    view = inflater.inflate(R.layout.listviewitem,null);
                }
                else{
                    view = convertView;
                }
                /**
                 * 在这种情况下，返回的是View对象，将其添加到listview中，
                 * 所以要找到listview的view的各种控件
                 * */
                TextView cityCode = view.findViewById(R.id.list_cityCode);
                TextView cityName = view.findViewById(R.id.list_cityName);


                City c = currentCity.get(position);//城市的名字
                String province = "";
                //String pro = nameToProvince.get(name);//城市的省份
                String name = c.getCity();
                province = c.getProvince();
                cityCode.setText(province);
                cityName.setText(name);
                return view;
            }
        });

    }
    private void gApplication()
    {
        MyApplication myApplication = (MyApplication) getApplication();
        city = myApplication.getCityList();//返回完整的城市信息
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_select_city);
        gApplication();
        initViews();//初始化listView

    }
    private void initCity()
    {
        for(City c :city)
            temp.add(c.clone());
    }
    private void initViews(){
        home_back = findViewById(R.id.home_back);//返回按钮
        home_search = findViewById(R.id.home_search);//搜索按钮
        home_list = findViewById(R.id.home_list);//获取list
        initCity();
        initCityList(temp);



        final SearchView search = findViewById(R.id.search);//搜索框
        home_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                select_city.this.finish();//
            }
        });
        /**
         * 当点击搜索城市会弹出对话框！！！
         * */
        home_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                View view = getLayoutInflater().inflate(R.layout.dialog_layout, null);
                final EditText editText = (EditText) view.findViewById(R.id.dialog_edit);
                AlertDialog dialog = new AlertDialog.Builder(select_city.this)
                        .setIcon(R.drawable.search)//设置标题的图片
                        .setTitle("pikapika")//设置对话框的标题
                        .setView(view)
                        .setNegativeButton("取消", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                dialog.dismiss();
                            }
                        })
                        .setPositiveButton("确定", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                String content = editText.getText().toString().trim();
                                content = content.replace("市","");
                                Toast.makeText(select_city.this, content+"天气正在查询", Toast.LENGTH_SHORT).show();
                                for(City c:city)
                                {
                                    if(content.equals(c.getCity()))
                                    {
                                        Intent intent = new Intent();
                                        intent.putExtra("cityCode",c.getNumber());
                                        setResult(RESULT_OK,intent);
                                        select_city.this.finish();
                                    }
                                }
                                dialog.dismiss();
                            }
                        }).create();
                dialog.show();

            }
        });
        //initLists();//初始化listview
        initCityList(temp);
        /**
         * 设置listview的条目点击监听时间
         * */
        home_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                City c = temp.get(position);
                String cityCode = c.getNumber();
                Intent intent = new Intent();
                intent.putExtra("cityCode",cityCode);
                setResult(RESULT_OK,intent);
                select_city.this.finish();
            }
        });

        //设置查询文本监听窗口
        search.setQueryHint("请输入城市名字或者拼音");
        search.setIconified(false);
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                //实际不执行
                Toast.makeText(select_city.this, "查询中", Toast.LENGTH_SHORT).show();
                return true;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                if(!TextUtils.isEmpty(newText))
                {
                    temp.clear();//清空所有搜索
                    for(City c:city)
                    {
                        if(c.getCity().contains(newText)||c.getAllFirstPY().contains(newText)||c.getFirstPY().contains(newText)
                                ||c.getAllPY().contains(newText))
                        {
                            temp.add(c);
                        }
                    }

//                    for(String str : codeToPinyin.values()){
//                        //如果满足首字母，拼音和城市名字，就执行
//                        if(nameToPinyin.get(str).contains(newText.toLowerCase())||str.contains(newText)||nameToProvince.get(str).contains(newText)||nameToAllFirstPY.get(str).contains(newText)
//                                ||nameToFirstPY.get(str).contains(newText)){
//                            citylist.add(str);
//                        }
//                    }
                    initCityList(temp);//将其初始化，就是将页面置为空

                    home_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            Log.d("2018-10-28",String.valueOf(position));
                            String res = citylist.get(position);
                            Log.d("2018-10-28",res);
                            Intent intent = new Intent();
                            intent.putExtra("cityCode",temp.get(position).getNumber());
                            setResult(RESULT_OK,intent);
                            select_city.this.finish();
                        }
                    });

                }
                else
                {
                    initCity();
                    initCityList(temp);
                    home_list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                             City c = city.get(position);
                            String cityCode = c.getNumber();
                            Intent intent = new Intent();
                            intent.putExtra("cityCode",cityCode);
                            setResult(RESULT_OK,intent);
                            select_city.this.finish();
                        }
                    });
                }
                return true;
            }
        });
    }

}
