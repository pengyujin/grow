package com.xz.app.pojo;

import java.util.Date;

public class Questreply {

    public Questreply() {
    }

    public Questreply(Long questid, Date time, Integer liketime, Integer userid, String content) {
        this.questid = questid;
        this.time = time;
        this.liketime = liketime;
        this.userid = userid;
        this.content = content;
    }

    private Long replyid;

    private Long questid;

    private Date time;

    private String image;

    private Integer liketime;

    private Long replyuser;

    private Integer userid;

    private Byte is_delete;

    private String content;

    public Long getReplyid() {
        return replyid;
    }

    public void setReplyid(Long replyid) {
        this.replyid = replyid;
    }

    public Long getQuestid() {
        return questid;
    }

    public void setQuestid(Long questid) {
        this.questid = questid;
    }

    public Date getTime() {
        return time;
    }

    public void setTime(Date time) {
        this.time = time;
    }

    public String getImage() {
        return image;
    }

    public void setImage(String image) {
        this.image = image == null ? null : image.trim();
    }

    public Integer getLiketime() {
        return liketime;
    }

    public void setLiketime(Integer liketime) {
        this.liketime = liketime;
    }

    public Long getReplyuser() {
        return replyuser;
    }

    public void setReplyuser(Long replyuser) {
        this.replyuser = replyuser;
    }

    public Integer getUserid() {
        return userid;
    }

    public void setUserid(Integer userid) {
        this.userid = userid;
    }

    public Byte getIs_delete() {
        return is_delete;
    }

    public void setIs_delete(Byte is_delete) {
        this.is_delete = is_delete;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content == null ? null : content.trim();
    }
}