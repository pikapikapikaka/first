package util;

public class ShowCity {
    private String cityName;
    private String cityCode;

    public ShowCity() {
    }

    public ShowCity(String cityName, String cityCode) {
        this.cityName = cityName;
        this.cityCode = cityCode;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getCityCode() {
        return cityCode;
    }

    public void setCityCode(String cityCode) {
        this.cityCode = cityCode;
    }
}
