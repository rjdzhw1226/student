package com.student.netty.protocol.command;

public class GroupMessageRequestPacket extends Packet{

	private String messageId;
	private String toGroupId;
	private String fromUserId;
	private String message;
	private String fileType;

	public GroupMessageRequestPacket() {

	}

	public GroupMessageRequestPacket(String toGroupId, String fromUserId, String message) {
		super();
		this.toGroupId = toGroupId;
		this.fromUserId = fromUserId;
		this.message = message;
	}

	public String getMessageId() {
		return messageId;
	}

	public void setMessageId(String messageId) {
		this.messageId = messageId;
	}

	public String getFromUserId() {
		return fromUserId;
	}

	public void setFromUserId(String fromUserId) {
		this.fromUserId = fromUserId;
	}

	public String getToGroupId() {
		return toGroupId;
	}

	public void setToGroupId(String toGroupId) {
		this.toGroupId = toGroupId;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	@Override
	public Byte getCommand() {

		return Command.GROUP_MESSAGE_REQUEST;
	}

}
