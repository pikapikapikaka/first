package util;

import android.util.Log;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.util.List;

public class Manager {
    public Weather parseXML(String xmldata, List<Weather> w){
        Weather wea = null;
        /**
         * 创建4个Weather,用来存储未来四天的天气
         * */
        Weather one = new Weather();
        Weather two = new Weather();
        Weather three = new Weather();
        Weather four = new Weather();

        int high = 0,low = 0,type = 0,date = 0,fengxiang = 0,fengli = 0;
        try {
            XmlPullParserFactory fac = XmlPullParserFactory.newInstance();//产生工厂实例
            XmlPullParser xmlPullParser = fac.newPullParser();//产生一个解析类
            Log.d("myWeather","data:"+xmldata);
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
                            //Log.d("dd", str);
                            wea.setFirst_city(str);
                            wea.setFirst_name(str);
                        } else if (xmlPullParser.getName().equals("updatetime")) {
                            eventType = xmlPullParser.next();
                            str = xmlPullParser.getText();
                            //Log.d("dd", str);
                            wea.setFirst_time(str);
                        } else if (xmlPullParser.getName().equals("wendu")) {
                            eventType = xmlPullParser.next();
                            wea.setFirst_temp(xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("fengli")&&(fengli == 0)) {
                            eventType = xmlPullParser.next();
                            wea.setFirst_wind(xmlPullParser.getText());
                            fengli++;
                        }
                        else if (xmlPullParser.getName().equals("shidu")) {
                            eventType = xmlPullParser.next();
                            wea.setFirst_water(xmlPullParser.getText());
                        } else if (xmlPullParser.getName().equals("fengxiang")&&(fengxiang == 0)) {
                            //String wind = wea.getFirst_wind();
                            eventType = xmlPullParser.next();
                            String wind = wea.getFirst_wind();
                            wind = xmlPullParser.getText()+wind;
                            wea.setFirst_wind(wind);
                            fengxiang++;
                            Log.d("liumeng","111:"+wea.getFirst_wind()+fengxiang);
                        } else if (xmlPullParser.getName().equals("fengxiang")&&(fengxiang == 1)){
                            eventType = xmlPullParser.next();
                            one.setFirst_wind(xmlPullParser.getText());
                            fengxiang++;
                            Log.d("liumeng",one.getFirst_wind());
                        }
                        else if(xmlPullParser.getName().equals("fengxiang")&&(fengxiang == 2)){
                            eventType = xmlPullParser.next();
                            two.setFirst_wind(xmlPullParser.getText());
                            fengxiang++;
                        }
                        else if(xmlPullParser.getName().equals("fengxiang")&&(fengxiang == 3)){
                            eventType = xmlPullParser.next();
                            three.setFirst_wind(xmlPullParser.getText());
                            fengxiang++;
                        }
                        else if(xmlPullParser.getName().equals("fengxiang")&&(fengxiang == 4)){
                            eventType = xmlPullParser.next();
                            four.setFirst_wind(xmlPullParser.getText());
                            fengxiang++;
                        }else if (xmlPullParser.getName().equals("pm25")) {
                            eventType = xmlPullParser.next();
                            wea.setFirst_num(Float.parseFloat(xmlPullParser.getText()));
                        } else if (xmlPullParser.getName().equals("quality")) {
                            eventType = xmlPullParser.next();
                            wea.setFirst_pm_res(xmlPullParser.getText());
                        } else if(xmlPullParser.getName().equals("date")&&(date == 0)){
                            eventType = xmlPullParser.next();
                            wea.setFirst_week(xmlPullParser.getText());
                            date++;
                        }
                        else if(xmlPullParser.getName().equals("date")&&(date == 1)){
                            eventType = xmlPullParser.next();
                            one.setFirst_week(xmlPullParser.getText());
                            //Log.d("liumeng",one.getFirst_week());
                            date++;
                        }
                        else if(xmlPullParser.getName().equals("date")&&(date == 2)){
                            eventType = xmlPullParser.next();
                            two.setFirst_week(xmlPullParser.getText());
                            date++;
                        }
                        else if(xmlPullParser.getName().equals("date")&&(date == 3)){
                            eventType = xmlPullParser.next();
                            three.setFirst_week(xmlPullParser.getText());
                            date++;
                        }
                        else if(xmlPullParser.getName().equals("date")&&(date == 4)){
                            eventType = xmlPullParser.next();
                            four.setFirst_week(xmlPullParser.getText());
                            date++;
                        }
                        else  if(xmlPullParser.getName().equals("high")&&(high == 0)){
                            eventType = xmlPullParser.next();
                            wea.setFirst_temp(xmlPullParser.getText().substring(2));
                            high++;
                        }
                        else  if(xmlPullParser.getName().equals("high")&&(high == 1)){
                            eventType = xmlPullParser.next();
                            one.setFirst_temp(xmlPullParser.getText().substring(2));
                            high++;
                        }
                        else  if(xmlPullParser.getName().equals("high")&&(high == 2)){
                            eventType = xmlPullParser.next();
                            two.setFirst_temp(xmlPullParser.getText().substring(2));
                            high++;
                        }
                        else  if(xmlPullParser.getName().equals("high")&&(high == 3)){
                            eventType = xmlPullParser.next();
                            three.setFirst_temp(xmlPullParser.getText().substring(2));
                            high++;
                        }
                        else  if(xmlPullParser.getName().equals("high")&&(high == 4)){
                            eventType = xmlPullParser.next();
                            four.setFirst_temp(xmlPullParser.getText().substring(2));
                            high++;
                        }
                        else if(xmlPullParser.getName().equals("low")&&(low == 0)){
                            String ww = wea.getFirst_temp();
                            eventType = xmlPullParser.next();
                            ww = xmlPullParser.getText().substring(2)+"~"+ww;
                            wea.setFirst_temp(ww);
                            low++;
                        }
                        else if(xmlPullParser.getName().equals("low")&&(low == 1)){
                            String ww = one.getFirst_temp();
                            eventType = xmlPullParser.next();
                            ww = xmlPullParser.getText().substring(2)+"~"+ww;
                            one.setFirst_temp(ww);
                            low++;
                        }
                        else if(xmlPullParser.getName().equals("low")&&(low == 2)){
                            String ww = two.getFirst_temp();
                            eventType = xmlPullParser.next();
                            ww = xmlPullParser.getText().substring(2)+"~"+ww;
                            two.setFirst_temp(ww);
                            low++;
                        }
                        else if(xmlPullParser.getName().equals("low")&&(low == 3)){
                            String ww = three.getFirst_temp();
                            eventType = xmlPullParser.next();
                            ww = xmlPullParser.getText().substring(2)+"~"+ww;
                            three.setFirst_temp(ww);
                            low++;
                        }
                        else if(xmlPullParser.getName().equals("low")&&(low == 4)){
                            String ww = four.getFirst_temp();
                            eventType = xmlPullParser.next();
                            ww = xmlPullParser.getText().substring(2)+"~"+ww;
                            four.setFirst_temp(ww);
                            low++;
                        }
                        else if(xmlPullParser.getName().equals("type")&&(type == 0)){
                            eventType = xmlPullParser.next();
                            wea.setFirst_wea(xmlPullParser.getText());
                            type++;
                        }
                        else if(xmlPullParser.getName().equals("type")&&(type == 1)){
                            eventType = xmlPullParser.next();
                            one.setFirst_wea(xmlPullParser.getText());
                            type++;
                        }
                        else if(xmlPullParser.getName().equals("type")&&(type == 2)){
                            eventType = xmlPullParser.next();
                            two.setFirst_wea(xmlPullParser.getText());
                            type++;
                        }
                        else if(xmlPullParser.getName().equals("type")&&(type == 3)){
                            eventType = xmlPullParser.next();
                            three.setFirst_wea(xmlPullParser.getText());
                            type++;
                        }
                        else if(xmlPullParser.getName().equals("type")&&(type == 4)){
                            eventType = xmlPullParser.next();
                            four.setFirst_wea(xmlPullParser.getText());
                            type++;
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
            w.add(one);
            w.add(two);
            w.add(three);
            w.add(four);
            Log.d("liumeng","hahahaha");
            Log.d("liumeng",one.getFirst_wea());
        }

        catch (Exception e)
        {
            e.printStackTrace();
        }
        return wea;

    }
}
