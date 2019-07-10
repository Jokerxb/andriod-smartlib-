package com.njupt.POJO;

import java.util.Date;

public class LabBooking {
    private Integer id;

    private String classId;

    private String userId;

    private String userName;

    private Integer openScheduleId;

    private Date bookDate;

    private Integer bookWeek;

    private String bookTime;

    private Integer bookLabId;

    private String bookLabName;

    private Integer bookTableId;

    private String bookContent;

    private Date startTime;

    private Date endTime;

    private Date totalTime;

    private Boolean isBookValued;

    private Boolean isAttended;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId == null ? null : classId.trim();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId == null ? null : userId.trim();
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName == null ? null : userName.trim();
    }

    public Integer getOpenScheduleId() {
        return openScheduleId;
    }

    public void setOpenScheduleId(Integer openScheduleId) {
        this.openScheduleId = openScheduleId;
    }

    public Date getBookDate() {
        return bookDate;
    }

    public void setBookDate(Date bookDate) {
        this.bookDate = bookDate;
    }

    public Integer getBookWeek() {
        return bookWeek;
    }

    public void setBookWeek(Integer bookWeek) {
        this.bookWeek = bookWeek;
    }

    public String getBookTime() {
        return bookTime;
    }

    public void setBookTime(String bookTime) {
        this.bookTime = bookTime == null ? null : bookTime.trim();
    }

    public Integer getBookLabId() {
        return bookLabId;
    }

    public void setBookLabId(Integer bookLabId) {
        this.bookLabId = bookLabId;
    }

    public String getBookLabName() {
        return bookLabName;
    }

    public void setBookLabName(String bookLabName) {
        this.bookLabName = bookLabName == null ? null : bookLabName.trim();
    }

    public Integer getBookTableId() {
        return bookTableId;
    }

    public void setBookTableId(Integer bookTableId) {
        this.bookTableId = bookTableId;
    }

    public String getBookContent() {
        return bookContent;
    }

    public void setBookContent(String bookContent) {
        this.bookContent = bookContent == null ? null : bookContent.trim();
    }

    public Date getStartTime() {
        return startTime;
    }

    public void setStartTime(Date startTime) {
        this.startTime = startTime;
    }

    public Date getEndTime() {
        return endTime;
    }

    public void setEndTime(Date endTime) {
        this.endTime = endTime;
    }

    public Date getTotalTime() {
        return totalTime;
    }

    public void setTotalTime(Date totalTime) {
        this.totalTime = totalTime;
    }

    public Boolean getIsBookValued() {
        return isBookValued;
    }

    public void setIsBookValued(Boolean isBookValued) {
        this.isBookValued = isBookValued;
    }

    public Boolean getIsAttended() {
        return isAttended;
    }

    public void setIsAttended(Boolean isAttended) {
        this.isAttended = isAttended;
    }
}