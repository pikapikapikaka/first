package util;

public class City {
    private String province;
    private String city;
    private String number;
    private String firstPY;
    private String allPY;
    private String allFirstPY;

    public City(String province, String city, String number, String firstPY, String allPY, String allFirstPY) {
        this.province = province;
        this.city = city;
        this.number = number;
        this.firstPY = firstPY.toLowerCase();
        this.allPY = allPY.toLowerCase();
        this.allFirstPY = allFirstPY.toLowerCase();
    }
    public City()
    {}
    public  City clone()
    {
        City copy = new City();
        copy.setAllFirstPY(this.getAllFirstPY());
        copy.setAllPY(this.getAllPY());
        copy.setCity(this.getCity());
        copy.setFirstPY(this.getFirstPY());
        copy.setNumber(this.getNumber());
        copy.setProvince(this.getProvince());
        return copy;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getFirstPY() {
        return firstPY;
    }

    public void setFirstPY(String firstPY) {
        this.firstPY = firstPY.toLowerCase();
    }

    public String getAllPY() {
        return allPY;
    }

    public void setAllPY(String allPY) {
        this.allPY = allPY.toLowerCase();
    }

    public String getAllFirstPY() {
        return allFirstPY;
    }

    public void setAllFirstPY(String allFirstPY) {
        this.allFirstPY = allFirstPY.toLowerCase();
    }
}
