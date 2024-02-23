package com.student.netty.protocol.command;


import com.student.pojo.vo.User;

public class RegisterRequestPacket extends Packet{
    	
	private User user;
			
	public User getUser() {
		return user;
	}

	public void setUser(User user) {
		this.user = user;
	}

	@Override
	public Byte getCommand() {
		
		return Command.REGISTER_REQUEST;
	}

}
