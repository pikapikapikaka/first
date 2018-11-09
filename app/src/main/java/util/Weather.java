package util;

import android.widget.ImageView;

public class Weather {
    private String first_name;
    private String first_city;
    private String first_time;
    private String first_water;
    private float first_num;
    private ImageView first_pic;
    private String first_pm_res;
    private ImageView first_weather;
    private String first_week;
    private String first_temp;
    private String first_wea;
    private String first_wind;

    public String getFirst_name() {
        return first_name+"天气";
    }

    public void setFirst_name(String first_name) {
        this.first_name = first_name;
    }

    public String getFirst_city() {
        return first_city;
    }

    public void setFirst_city(String first_city) {
        this.first_city = first_city;
    }

    public String getFirst_time() {
        return "今天"+first_time+"发布";
    }

    public void setFirst_time(String first_time) {
        this.first_time = first_time;
    }

    public String getFirst_water() {
        return "湿度："+first_water;
    }

    public void setFirst_water(String first_water) {
        this.first_water = first_water;
    }

    public float getFirst_num() {
        return first_num;
    }

    public void setFirst_num(float first_num) {
        this.first_num = first_num;
    }

    public ImageView getFirst_pic() {
        return first_pic;
    }

    public void setFirst_pic(ImageView first_pic) {
        this.first_pic = first_pic;
    }

    public String getFirst_pm_res() {
        return first_pm_res;
    }

    public void setFirst_pm_res(String first_pm_res) {
        this.first_pm_res = first_pm_res;
    }

    public ImageView getFirst_weather() {
        return first_weather;
    }

    public void setFirst_weather(ImageView first_weather) {
        this.first_weather = first_weather;
    }

    public String getFirst_week() {
        return "今天是"+first_week;
    }

    public void setFirst_week(String first_week) {
        this.first_week = first_week;
    }

    public String getFirst_temp() {
        return first_temp;
    }

    public void setFirst_temp(String first_temp) {
        this.first_temp = first_temp;
    }

    public String getFirst_wea() {
        return first_wea;
    }

    public void setFirst_wea(String first_wea) {
        this.first_wea = first_wea;
    }

    public String getFirst_wind() {
        return first_wind;
    }

    public void setFirst_wind(String first_wind) {
        this.first_wind = first_wind;
    }

    @Override
    public String toString() {
        return first_city+"/"+first_name+"/"+first_temp;
    }
}
