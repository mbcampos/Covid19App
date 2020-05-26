package com.example.coronavirus;

import java.util.Date;

class Country {
    private String country;
    private Integer cases;
    private Integer confirmed;
    private Integer deaths;
    private Integer recovered;
    private Date updatedAt;

    public void setCountry(String value) { this.country = value; }

    public void setCases(Integer value) { this.cases = value; }

    public void setConfirmed(Integer value) {
        this.confirmed = value;
    }

    public void setDeaths(Integer value) {
        this.deaths = value;
    }

    public void setRecovered(Integer value) {
        this.recovered = value;
    }

    public String getCountry() { return this.country; }

    public Integer getCases() { return this.cases; }

    public Integer getConfirmed() {
        return this.confirmed;
    }

    public Integer getDeaths() { return this.deaths; }

    public Integer getRecovered() { return this.recovered; }
}
