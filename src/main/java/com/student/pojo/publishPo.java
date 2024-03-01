package com.student.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.netty.buffer.ByteBuf;

public class publishPo {
    private String messageId;
    private Integer type;
    private String id;
    private String fromId;
    private String message;
    private ByteBuf buf;

    private String json;

    public publishPo() {
    }

    public publishPo(Integer type, String id, String fromId, String message, ByteBuf buf, String json, String messageId) {
        this.type = type;
        this.id = id;
        this.fromId = fromId;
        this.message = message;
        this.buf = buf;
        this.json = json;
        this.messageId = messageId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getJson() {
        return json;
    }

    public void setJson(String json) {
        this.json = json;
    }

    public Integer getType() {
        return type;
    }

    public void setType(Integer type) {
        this.type = type;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFromId() {
        return fromId;
    }

    public void setFromId(String fromId) {
        this.fromId = fromId;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
    @JsonBackReference
    public ByteBuf getBuf() {
        return buf;
    }
    @JsonBackReference
    public void setBuf(ByteBuf buf) {
        this.buf = buf;
    }

    @Override
    public String toString() {
        return "publishPo{" +
                "type=" + type +
                ", id='" + id + '\'' +
                ", fromId='" + fromId + '\'' +
                ", message='" + message + '\'' +
                ", buf=" + buf +
                '}';
    }
}
