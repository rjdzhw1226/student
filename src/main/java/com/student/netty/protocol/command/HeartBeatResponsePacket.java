package com.student.netty.protocol.command;

public class HeartBeatResponsePacket extends Packet{

	@Override
	public Byte getCommand() {
		return Command.HEARTBEAT_RESPONSE;
	}

}
