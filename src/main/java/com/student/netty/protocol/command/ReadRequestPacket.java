package com.student.netty.protocol.command;

public class ReadRequestPacket extends Packet{
    private String messageId;
    private String toUserId;
    private String fromUserid;

    /**
     * 已读 1 未读 0
     */
    private String readType;

    private String fileType;

    public ReadRequestPacket() {
        super();
    }

    public ReadRequestPacket(String toUserId, String fromUserId, String messageId, String readType) {
        super();
        this.messageId = messageId;
        this.toUserId = toUserId;
        this.fromUserid = fromUserId;
        this.readType = readType;
    }

    public String getFileType() {
        return fileType;
    }

    public void setFileType(String fileType) {
        this.fileType = fileType;
    }

    public String getToUserId() {
        return toUserId;
    }

    public void setToUserId(String toUserId) {
        this.toUserId = toUserId;
    }

    public String getMessageId() {
        return messageId;
    }

    public void setMessageId(String messageId) {
        this.messageId = messageId;
    }

    public String getFromUserid() {
        return fromUserid;
    }

    public void setFromUserid(String fromUserid) {
        this.fromUserid = fromUserid;
    }

    public String getReadType() {
        return readType;
    }

    public void setReadType(String readType) {
        this.readType = readType;
    }

    @Override
    public Byte getCommand() {
        return Command.READ_REQUEST;
    }

    @Override
    public String toString() {
        return "ReadRequestPacket{" +
                "messageId='" + messageId + '\'' +
                ", toUserId='" + toUserId + '\'' +
                ", fromUserid='" + fromUserid + '\'' +
                ", readType='" + readType + '\'' +
                '}';
    }
}
