package com.student.netty.protocol.command;

public class HeartBeatRequestPacket extends Packet{

	@Override
	public Byte getCommand() {
		
		return Command.HEARTBEAT_REQUEST;
	}

}
