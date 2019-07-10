package com.njupt.POJO;

import java.util.Date;

public class ReportRepairInfo {
    private Integer id;

    private String reporterId;

    private String reporterName;

    private Integer instrumentLabId;

    private String instrumentLabName;

    private Integer instrumentTableId;

    private String instrumentName;

    private String instrumentId;

    private String damageInfo;

    private Date reportTime;

    private String remarks;

    private String isRepaired;

    private Date repairTime;

    private String repairmanId;

    private String repairmanName;

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getReporterId() {
        return reporterId;
    }

    public void setReporterId(String reporterId) {
        this.reporterId = reporterId == null ? null : reporterId.trim();
    }

    public String getReporterName() {
        return reporterName;
    }

    public void setReporterName(String reporterName) {
        this.reporterName = reporterName == null ? null : reporterName.trim();
    }

    public Integer getInstrumentLabId() {
        return instrumentLabId;
    }

    public void setInstrumentLabId(Integer instrumentLabId) {
        this.instrumentLabId = instrumentLabId;
    }

    public String getInstrumentLabName() {
        return instrumentLabName;
    }

    public void setInstrumentLabName(String instrumentLabName) {
        this.instrumentLabName = instrumentLabName == null ? null : instrumentLabName.trim();
    }

    public Integer getInstrumentTableId() {
        return instrumentTableId;
    }

    public void setInstrumentTableId(Integer instrumentTableId) {
        this.instrumentTableId = instrumentTableId;
    }

    public String getInstrumentName() {
        return instrumentName;
    }

    public void setInstrumentName(String instrumentName) {
        this.instrumentName = instrumentName == null ? null : instrumentName.trim();
    }

    public String getInstrumentId() {
        return instrumentId;
    }

    public void setInstrumentId(String instrumentId) {
        this.instrumentId = instrumentId == null ? null : instrumentId.trim();
    }

    public String getDamageInfo() {
        return damageInfo;
    }

    public void setDamageInfo(String damageInfo) {
        this.damageInfo = damageInfo == null ? null : damageInfo.trim();
    }

    public Date getReportTime() {
        return reportTime;
    }

    public void setReportTime(Date reportTime) {
        this.reportTime = reportTime;
    }

    public String getRemarks() {
        return remarks;
    }

    public void setRemarks(String remarks) {
        this.remarks = remarks == null ? null : remarks.trim();
    }

    public String getIsRepaired() {
        return isRepaired;
    }

    public void setIsRepaired(String isRepaired) {
        this.isRepaired = isRepaired == null ? null : isRepaired.trim();
    }

    public Date getRepairTime() {
        return repairTime;
    }

    public void setRepairTime(Date repairTime) {
        this.repairTime = repairTime;
    }

    public String getRepairmanId() {
        return repairmanId;
    }

    public void setRepairmanId(String repairmanId) {
        this.repairmanId = repairmanId == null ? null : repairmanId.trim();
    }

    public String getRepairmanName() {
        return repairmanName;
    }

    public void setRepairmanName(String repairmanName) {
        this.repairmanName = repairmanName == null ? null : repairmanName.trim();
    }
}
