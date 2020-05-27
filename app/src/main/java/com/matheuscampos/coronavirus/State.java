package com.matheuscampos.coronavirus;

import java.util.Date;

class State {
    private String state;
    private Integer cases;
    private Integer deaths;
    private Integer refuses;
    private Integer suspects;
    private Date updatedAt;

    public void setState(String value) { this.state = value; }

    public void setCases(Integer value) { this.cases = value; }

    public void setDeaths(Integer value) {
        this.deaths = value;
    }

    public void setRefuses(Integer value) {
        this.refuses = value;
    }

    public void setSuspects(Integer value) {
        this.suspects = value;
    }

    public String getState() { return this.state; }

    public Integer getCases() { return this.cases; }

    public Integer getDeaths() {
        return this.deaths;
    }

    public Integer getRefuses() { return this.refuses; }

    public Integer getSuspects() { return this.suspects; }

    @Override
    public String toString() { return this.state; }
}
