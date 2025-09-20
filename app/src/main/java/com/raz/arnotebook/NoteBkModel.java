package com.raz.arnotebook;

public class NoteBkModel {
    private int id;
    private String title;
    private String desc;
    private String secind;

    //construcots

    public NoteBkModel(int id, String title, String desc, String secind) {
        this.id = id;
        this.title = title;
        this.desc = desc;
        this.secind = secind;
    }

    @Override
    public String toString() {
        return "NoteBkModel{" +
                "id=" + id +
                ", title='" + title + '\'' +
                ", desc='" + desc + '\'' +
                ", secind='" + secind + '\'' +
                '}';
    }

    public NoteBkModel() {
    }

    //setters and getters

    public int getId() { return id;}

    public void setId(int id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDesc() {
        return desc;
    }

    public void setDesc(String desc) {
        this.desc = desc;
    }

    public String getSecind() {
        return secind;
    }

    public void setSecind(String secind) {
        this.secind = secind;
    }
}

