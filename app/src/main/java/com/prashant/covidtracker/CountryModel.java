package com.prashant.covidtracker;

public class CountryModel {
    private String flag,country,iso3;

    public CountryModel() {
    }

    public CountryModel(String flag, String country, String iso3) {
        this.flag = flag;
        this.country = country;
        this.iso3 = iso3;
    }


    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getIso3() {
        return iso3;
    }

    public void setIso3(String iso3) {
        this.iso3 = iso3;
    }
}
