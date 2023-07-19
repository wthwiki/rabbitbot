package com.wth.entity;



import javax.persistence.*;
import java.io.Serializable;

/**
 * 这是go-cq传过来的原始消息类
 */
//@Entity
//@javax.persistence.Table(name = "message") //设置表名
//@org.hibernate.annotations.Table(appliesTo = "message", comment = "测试信息")//设置表注释
public class Message implements Serializable {
    @Id
    @Column(name = "id", nullable = false)
    private Long id;
    private String post_type;
    private String meta_event_type;
    // 这个是聊天类型。
    private String message_type;
    private String notice_type;
    // 操作人id 比如群管理员a踢了一个人,那么该值为a的qq号
    private String operator_id;
    private Long time;
    private String self_id;
    private String sub_type;
    private String user_id;
    private String sender_id;
    private String group_id;
    private String target_id;
    private String message;
    private String raw_message;
    private Integer font;
    private String message_id;
    private Integer message_seq;
    private String anonymous;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getPost_type() {
        return post_type;
    }

    public void setPost_type(String post_type) {
        this.post_type = post_type;
    }

    public String getMeta_event_type() {
        return meta_event_type;
    }

    public void setMeta_event_type(String meta_event_type) {
        this.meta_event_type = meta_event_type;
    }

    public String getMessage_type() {
        return message_type;
    }

    public void setMessage_type(String message_type) {
        this.message_type = message_type;
    }

    public String getNotice_type() {
        return notice_type;
    }

    public void setNotice_type(String notice_type) {
        this.notice_type = notice_type;
    }

    public String getOperator_id() {
        return operator_id;
    }

    public void setOperator_id(String operator_id) {
        this.operator_id = operator_id;
    }

    public Long getTime() {
        return time;
    }

    public void setTime(Long time) {
        this.time = time;
    }

    public String getSelf_id() {
        return self_id;
    }

    public void setSelf_id(String self_id) {
        this.self_id = self_id;
    }

    public String getSub_type() {
        return sub_type;
    }

    public void setSub_type(String sub_type) {
        this.sub_type = sub_type;
    }

    public String getUser_id() {
        return user_id;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public String getSender_id() {
        return sender_id;
    }

    public void setSender_id(String sender_id) {
        this.sender_id = sender_id;
    }

    public String getGroup_id() {
        return group_id;
    }

    public void setGroup_id(String group_id) {
        this.group_id = group_id;
    }

    public String getTarget_id() {
        return target_id;
    }

    public void setTarget_id(String target_id) {
        this.target_id = target_id;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getRaw_message() {
        return raw_message;
    }

    public void setRaw_message(String raw_message) {
        this.raw_message = raw_message;
    }

    public Integer getFont() {
        return font;
    }

    public void setFont(Integer font) {
        this.font = font;
    }

    public String getMessage_id() {
        return message_id;
    }

    public void setMessage_id(String message_id) {
        this.message_id = message_id;
    }

    public Integer getMessage_seq() {
        return message_seq;
    }

    public void setMessage_seq(Integer message_seq) {
        this.message_seq = message_seq;
    }

    public String getAnonymous() {
        return anonymous;
    }

    public void setAnonymous(String anonymous) {
        this.anonymous = anonymous;
    }

    @Override
    public String toString() {
        return "Message{" +
                "id=" + id +
                ", post_type='" + post_type + '\'' +
                ", meta_event_type='" + meta_event_type + '\'' +
                ", message_type='" + message_type + '\'' +
                ", notice_type='" + notice_type + '\'' +
                ", operator_id='" + operator_id + '\'' +
                ", time=" + time +
                ", self_id='" + self_id + '\'' +
                ", sub_type='" + sub_type + '\'' +
                ", user_id='" + user_id + '\'' +
                ", sender_id='" + sender_id + '\'' +
                ", group_id='" + group_id + '\'' +
                ", target_id='" + target_id + '\'' +
                ", message='" + message + '\'' +
                ", raw_message='" + raw_message + '\'' +
                ", font=" + font +
                ", message_id='" + message_id + '\'' +
                ", message_seq=" + message_seq +
                ", anonymous='" + anonymous + '\'' +
                '}';
    }
}
