package com.student.netty.protocol.command;

import java.util.Date;

public abstract class Packet {

	private Date sendTime;

	private Byte version = 1;

	public Date getSendTime() {
		return sendTime;
	}

	public void setSendTime(Date sendTime) {
		this.sendTime = sendTime;
	}

	public Byte getVersion() {
		return version;
	}

	public void setVersion(Byte version) {
		this.version = version;
	}

	public abstract Byte getCommand();
}
