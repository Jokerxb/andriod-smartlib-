package com.njupt.POJO;


import java.util.Date;

public class OpenLabInfo {
    private Integer id;

    private Integer openWeek;

    private String openTime;

    private Date openDate;

    private String openTeacherId;

    private String openTeacherName;

    private String openLabIdOrder;

    private String openLabNameOrder;

    private String openLabOrder;

    private String openLabPeople;

    private Integer totalPeopleNum;//总人数

    private Integer bookingNum;//已经预约

    private Integer notBookingNum;//未预约

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getOpenWeek() {
        return openWeek;
    }

    public void setOpenWeek(Integer openWeek) {
        this.openWeek = openWeek;
    }

    public String getOpenTime() {
        return openTime;
    }

    public void setOpenTime(String openTime) {
        this.openTime = openTime;
    }

    public Date getOpenDate() {
        return openDate;
    }

    public void setOpenDate(Date openDate) {
        this.openDate = openDate;
    }

    public String getOpenTeacherId() {
        return openTeacherId;
    }

    public void setOpenTeacherId(String openTeacherId) {
        this.openTeacherId = openTeacherId;
    }

    public String getOpenTeacherName() {
        return openTeacherName;
    }

    public void setOpenTeacherName(String openTeacherName) {
        this.openTeacherName = openTeacherName;
    }

    public String getOpenLabIdOrder() {
        return openLabIdOrder;
    }

    public void setOpenLabIdOrder(String openLabIdOrder) {
        this.openLabIdOrder = openLabIdOrder;
    }

    public String getOpenLabNameOrder() {
        return openLabNameOrder;
    }

    public void setOpenLabNameOrder(String openLabNameOrder) {
        this.openLabNameOrder = openLabNameOrder;
    }

    public String getOpenLabOrder() {
        return openLabOrder;
    }

    public void setOpenLabOrder(String openLabOrder) {
        this.openLabOrder = openLabOrder;
    }

    public String getOpenLabPeople() {
        return openLabPeople;
    }

    public void setOpenLabPeople(String openLabPeople) {
        this.openLabPeople = openLabPeople;
    }

    public Integer getTotalPeopleNum() {
        return totalPeopleNum;
    }

    public void setTotalPeopleNum(Integer totalPeopleNum) {
        this.totalPeopleNum = totalPeopleNum;
    }

    public Integer getBookingNum() {
        return bookingNum;
    }

    public void setBookingNum(Integer bookingNum) {
        this.bookingNum = bookingNum;
    }

    public Integer getNotBookingNum() {
        return notBookingNum;
    }

    public void setNotBookingNum(Integer notBookingNum) {
        this.notBookingNum = notBookingNum;
    }

}

