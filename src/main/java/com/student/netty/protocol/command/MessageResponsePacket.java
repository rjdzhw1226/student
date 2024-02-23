package com.student.netty.protocol.command;


import com.student.pojo.vo.User;

public class MessageResponsePacket extends Packet{
    /**
     * 这条消息是谁发的
     */
    private User fromUser;

	private String message;

	public User getFromUser() {
		return fromUser;
	}

	public void setFromUser(User fromUser) {
		this.fromUser = fromUser;
	}

	public String getMessage() {
		return message;
	}

	public void setMessage(String message) {
		this.message = message;
	}

	@Override
	public Byte getCommand() {

		return null;
	}

}
