package com.njupt.POJO;


import java.io.Serializable;
import java.util.Date;

public class TeachingLogInfo implements Serializable {
    private Integer id;

    private String teacherId;

    private String teacherName;

    private String courseWeek;

    private Date courseDate;

    private String courseDayOfWeek;

    private String courseTimeOfDay;

    private String courseLabId;

    private String courseLabName;

    private String courseName;

    private String classId;

    private Integer classUserNum;

    private Integer courseClassHour;

    private String courseSafeInfo;

    private String courseEnvInfo;

    private String courseContent;

    private String remarks;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTeacherId() {
        return teacherId;
    }

    public void setTeacherId(String teacherId) {
        this.teacherId = teacherId;
    }

    public String getTeacherName() {
        return teacherName;
    }

    public void setTeacherName(String teacherName) {
        this.teacherName = teacherName;
    }

    public String getCourseWeek() {
        return courseWeek;
    }

    public void setCourseWeek(String courseWeek) {
        this.courseWeek = courseWeek;
    }

    public Date getCourseDate() {
        return courseDate;
    }

    public void setCourseDate(Date courseDate) {
        this.courseDate = courseDate;
    }

    public String getCourseDayOfWeek() {
        return courseDayOfWeek;
    }

    public void setCourseDayOfWeek(String courseDayOfWeek) {
        this.courseDayOfWeek = courseDayOfWeek;
    }

    public String getCourseTimeOfDay() {
        return courseTimeOfDay;
    }

    public void setCourseTimeOfDay(String courseTimeOfDay) {
        this.courseTimeOfDay = courseTimeOfDay;
    }

    public String getCourseLabId() {
        return courseLabId;
    }

    public void setCourseLabId(String courseLabId) {
        this.courseLabId = courseLabId;
    }

    public String getCourseLabName() {
        return courseLabName;
    }

    public void setCourseLabName(String courseLabName) {
        this.courseLabName = courseLabName;
    }

    public String getCourseName() {
        return courseName;
    }

    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public String getClassId() {
        return classId;
    }

    public void setClassId(String classId) {
        this.classId = classId;
    }

    public Integer getClassUserNum() {
        return classUserNum;
    }

    public void setClassUserNum(Integer classUserNum) {
        this.classUserNum = classUserNum;
    }

    public Integer getCourseClassHour() {
        return courseClassHour;
    }

    public void setCourseClassHour(Integer courseClassHour) {
        this.courseClassHour = courseClassHour;
    }

    public String getCourseSafeInfo() {
        return courseSafeInfo;
    }

    public void setCourseSafeInfo(String courseSafeInfo) {
        this.courseSafeInfo = courseSafeInfo;
    }

    public String getCourseEnvInfo() {
        return courseEnvInfo;
    }

    public void setCourseEnvInfo(String courseEnvInfo) {
        this.courseEnvInfo = courseEnvInfo;
    }

    public String getCourseContent() {
        return courseContent;
    }

    public void setCourseContent(String courseContent) {
        this.courseContent = courseContent;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks;
    }
}