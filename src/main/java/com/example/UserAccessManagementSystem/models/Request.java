package com.example.UserAccessManagementSystem.models;

public class Request {
    private int id;
    private int userId;
    private int softwareId;
    private String accessType;
    private String reason;
    private String status;

    public Request(int userId, int softwareId, String accessType, String reason, String status) {
        this.status = status;
        this.reason = reason;
        this.accessType = accessType;
        this.softwareId = softwareId;
        this.userId = userId;
    }
    public Request(int id, String status){
        this.status = status;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getSoftwareId() {
        return softwareId;
    }

    public void setSoftwareId(int softwareId) {
        this.softwareId = softwareId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getAccessType() {
        return accessType;
    }

    public void setAccessType(String accessType) {
        this.accessType = accessType;
    }

    public String getReason() {
        return reason;
    }

    public void setReason(String reason) {
        this.reason = reason;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
