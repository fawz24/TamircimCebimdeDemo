package com.tamirdunyasi.tamircimcebimdedemo;

import java.util.Date;

public class Post {
    protected String id;
    protected String category;
    protected String clientId;
    protected String clientName;
    protected String companyId;
    protected String companyName;
    protected String content;
    protected Date data;
    protected long point;
    protected String state;
    protected String title;

    public Post(){
        this.id = "";
        this.category = "";
        this.clientId = "";
        this.clientName = "";
        this.companyId = "";
        this.companyName = "";
        this.content = "";
        this.data = new Date();
        this.point = 0;
        this.state = "";
        this.title = "";
    }

    public Post(String category, String clientId, String clientName, String companyId,
                String companyName, String content, Date date, long point, String state, String title){
        this.id = "";
        this.category = category;
        this.clientId = clientId;
        this.clientName = clientName;
        this.companyId = companyId;
        this.companyName = companyName;
        this.content = content;
        this.data = date;
        this.point = point;
        this.state = state;
        this.title = title;
    }

    public Post(String id, String category, String clientId, String clientName, String companyId,
                String companyName, String content, Date date, long point, String state, String title){
        this.id = id;
        this.category = category;
        this.clientId = clientId;
        this.clientName = clientName;
        this.companyId = companyId;
        this.companyName = companyName;
        this.content = content;
        this.data = date;
        this.point = point;
        this.state = state;
        this.title = title;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getClientId() {
        return clientId;
    }

    public void setClientId(String clientId) {
        this.clientId = clientId;
    }

    public String getClientName() {
        return clientName;
    }

    public void setClientName(String clientName) {
        this.clientName = clientName;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCompanyName() {
        return companyName;
    }

    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Date getData() {
        return data;
    }

    public void setData(Date data) {
        this.data = data;
    }

    public long getPoint() {
        return point;
    }

    public void setPoint(int point) {
        this.point = point;
    }

    public String getState() {
        return state;
    }

    public void setState(String state) {
        this.state = state;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }
}
