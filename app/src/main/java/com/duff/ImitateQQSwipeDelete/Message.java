package com.duff.ImitateQQSwipeDelete;

/**
 * Created by ezez-c on 2017/3/3.
 */
public class Message {
    private String id;
    private String address;
    private String date;
    private String type;
    private String body;

    @Override
    public String toString() {
        return address+"|"+date+"|"+type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}
