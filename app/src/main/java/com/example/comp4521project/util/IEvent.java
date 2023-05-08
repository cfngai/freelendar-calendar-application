package com.example.comp4521project.util;

import org.json.JSONObject;

import java.time.LocalDate;

public class IEvent {

    private int ID;
    private String title;
    private String description;
    private String company;
    private LocalDate date;
    private Time startTime;
    private Time endTime;
    private float income;
    private String remark;

    /*
     * -1 - Upcoming
     * 0 - Incomplete
     * 1 - Completed
     */
    private int status;

    public IEvent(int ID, String title, String description, String company, LocalDate date,
                  Time startTime, Time endTime, float income, String remark, int status) {
        this.ID = ID;
        this.title = title;
        this.description = description;
        this.company = company;
        this.date = date;
        this.startTime = startTime;
        this.endTime = endTime;
        this.income = income;
        this.remark = remark;
        this.status = status;
    }

    public IEvent(JSONObject jsonObject) {
        try {
            this.ID = jsonObject.getInt("ID");
            this.title = jsonObject.getString("title");
            this.description = jsonObject.getString("description");
            this.company = jsonObject.getString("company");
            this.date = LocalDate.of(jsonObject.getInt("date_year"),
                    jsonObject.getInt("date_month"),
                    jsonObject.getInt("date_day"));
            this.startTime = new Time(jsonObject.getInt("startTime"));
            this.endTime = new Time(jsonObject.getInt("endTime"));
            this.income = Float.parseFloat(jsonObject.getString("income"));
            this.remark = jsonObject.getString("remark");
            this.status = jsonObject.getInt("status");
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }

    public JSONObject toJSONObject() {
        try {
            JSONObject jsonObject = new JSONObject();
            jsonObject.put("ID", this.ID);
            jsonObject.put("title", this.title);
            jsonObject.put("description", this.description);
            jsonObject.put("company", this.company);
            jsonObject.put("date_year", this.date.getYear());
            jsonObject.put("date_month", this.date.getMonthValue());
            jsonObject.put("date_day", this.date.getDayOfMonth());
            jsonObject.put("startTime", this.startTime.toInt());
            jsonObject.put("endTime", this.endTime.toInt());
            jsonObject.put("income", Float.toString(this.income));
            jsonObject.put("remark", this.remark);
            jsonObject.put("status", this.status);
            return jsonObject;
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        return null;
    }


    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getCompany() {
        return company;
    }

    public void setCompany(String company) {
        this.company = company;
    }

    public LocalDate getLocalDate() {
        return date;
    }

    public void setLocalDate(LocalDate date) {
        this.date = date;
    }

    public Time getStartTime() {
        return startTime;
    }

    public void setStartTime(Time startTime) {
        this.startTime = startTime;
    }

    public Time getEndTime() {
        return endTime;
    }

    public void setEndTime(Time endTime) {
        this.endTime = endTime;
    }

    public float getIncome() {
        return income;
    }

    public void setIncome(float income) {
        this.income = income;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }
}
