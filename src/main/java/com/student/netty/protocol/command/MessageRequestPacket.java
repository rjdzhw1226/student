package com.student.netty.protocol.command;

public class MessageRequestPacket extends Packet{

	private String messageId;
	private String toUserId;
	private String fromUserId;
	private String message;
	private String fileType;

	public MessageRequestPacket() {
		super();
	}

	public MessageRequestPacket(String toUserId, String fromUserId, String messageId, String message) {
		super();
		this.messageId = messageId;
		this.toUserId = toUserId;
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

	public String getToUserId() {
		return toUserId;
	}

	public void setToUserId(String toUserId) {
		this.toUserId = toUserId;
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

		return Command.MESSAGE_REQUEST;
	}

}
