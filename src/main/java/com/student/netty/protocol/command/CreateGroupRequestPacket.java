package com.student.netty.protocol.command;

import java.util.List;

public class CreateGroupRequestPacket extends Packet{
	private String title;
	/**
	 * 1 单聊 2 同事群 3讨论组 5系统消息 6公众号
	 */
	private String chatType;
    /**
     * 创建群里需要把群里的Id发过来
     */
	private List<String> userIdList;

	public List<String> getUserIdList() {
		return userIdList;
	}

	public void setUserIdList(List<String> userIdList) {
		this.userIdList = userIdList;
	}

	public String getChatType() {
		return chatType;
	}

	public void setChatType(String chatType) {
		this.chatType = chatType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	@Override
	public Byte getCommand() {

		return Command.CREATE_GROUP_REQUEST;
	}

}
