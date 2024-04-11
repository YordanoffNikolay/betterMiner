package org.yordanoffnikolay.betterminer.dtos;

public class DateRange {

    private String startYear;
    private String startMonth;
    private String startDay;
    private String endYear;
    private String endMonth;
    private String endDay;

    public String getStartYear() {
        return startYear;
    }

    public String getStartMonth() {
        return startMonth;
    }

    public String getStartDay() {
        return startDay;
    }

    public String getEndYear() {
        return endYear;
    }

    public String getEndMonth() {
        return endMonth;
    }

    public String getEndDay() {
        return endDay;
    }

    public void setStartYear(String startYear) {
        this.startYear = startYear;
    }

    public void setStartMonth(String startMonth) {
        this.startMonth = startMonth;
    }

    public void setStartDay(String startDay) {
        this.startDay = startDay;
    }

    public void setEndYear(String endYear) {
        this.endYear = endYear;
    }

    public void setEndMonth(String endMonth) {
        this.endMonth = endMonth;
    }

    public void setEndDay(String endDay) {
        this.endDay = endDay;
    }
}
