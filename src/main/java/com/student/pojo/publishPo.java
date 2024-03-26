package com.student.pojo;

import com.fasterxml.jackson.annotation.JsonBackReference;
import io.netty.buffer.ByteBuf;

import java.util.ArrayList;
import java.util.List;

public class publishPo {
    private String messageId;
    private Integer type;
    private String id;
    private String fromId;
    private String message;
    private ByteBuf buf;

    private List<String> userIds;
    private String groupId;
    private String readType;
    private String fileType;
    private String json;

    public publishPo() {
    }

    public publishPo(String json, List<String> userIds) {
        this.json = json;
        this.userIds = new ArrayList<>(userIds);
    }

    public publishPo(Integer type, String id, String fromId, String message, ByteBuf buf, String json, String messageId, String readType, String fileType) {
        this.type = type;
        this.id = id;
        this.fromId = fromId;
        this.message = message;
        this.buf = buf;
        this.json = json;
        this.messageId = messageId;
        this.readType = readType;
        this.fileType = fileType;
    }

    public publishPo(Integer type, String id, String fromId, String message, ByteBuf buf, String json, String messageId, String readType, String fileType, List<String> userIds) {
        this.type = type;
        this.id = id;
        this.fromId = fromId;
        this.message = message;
        this.buf = buf;
        this.json = json;
        this.messageId = messageId;
        this.readType = readType;
        this.fileType = fileType;
        this.userIds = new ArrayList<>(userIds);
    }

    public publishPo(Integer type, String id, String fromId, String message, ByteBuf buf, String json, String messageId, String readType, String fileType, String groupId) {
        this.type = type;
        this.id = id;
        this.fromId = fromId;
        this.message = message;
        this.buf = buf;
        this.json = json;
        this.messageId = messageId;
        this.readType = readType;
        this.fileType = fileType;
        this.groupId = groupId;
    }

    public List<String> getUserIds() {
        return userIds;
    }

    public void setUserIds(List<String> userIds) {
        this.userIds = userIds;
    }

    public String getFileType() {
        return fileType;
    }

    public String getGroupId() {
        return groupId;
    }

    public void setGroupId(String groupId) {
        this.groupId = groupId;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getReadType() {
        return readType;
    }

    public void setReadType(String readType) {
        this.readType = readType;
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
                "messageId='" + messageId + '\'' +
                ", type=" + type +
                ", id='" + id + '\'' +
                ", fromId='" + fromId + '\'' +
                ", message='" + message + '\'' +
                ", buf=" + buf +
                ", userIds=" + userIds +
                ", groupId='" + groupId + '\'' +
                ", readType='" + readType + '\'' +
                ", fileType='" + fileType + '\'' +
                ", json='" + json + '\'' +
                '}';
    }
}
