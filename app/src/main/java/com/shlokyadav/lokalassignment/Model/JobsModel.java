package com.shlokyadav.lokalassignment.Model;

public class JobsModel {

    private String title;
    private String location;
    private String salary;
    private String phone;
    private String id;

    public JobsModel(String title, String location, String salary, String phone, String id) {
        this.title = title;
        this.location = location;
        this.salary = salary;
        this.phone = phone;
        this.id = id;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }
}
