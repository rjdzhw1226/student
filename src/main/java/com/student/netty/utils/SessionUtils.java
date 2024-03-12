package com.student.netty.utils;

import com.alibaba.fastjson.JSON;
import com.student.pojo.vo.User;
import io.netty.channel.Channel;
import io.netty.channel.ChannelId;
import io.netty.channel.group.ChannelGroup;
import io.netty.channel.group.DefaultChannelGroup;
import io.netty.util.concurrent.GlobalEventExecutor;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class SessionUtils {
	/**
	 * 本机channelGroup用于分布式
	 */
	public static ChannelGroup channelGroup = new DefaultChannelGroup(GlobalEventExecutor.INSTANCE);
	/**
	 * userID 映射 连接channel
	 */
	private static final Map<String, Channel> userIdChannelMap = new ConcurrentHashMap<>();


	private static final Map<String, ChannelGroup> groupIdChannelGroupMap = new ConcurrentHashMap<>();

	public static void bindChannel(User user, Channel channel) {
		userIdChannelMap.put(user.getUserId(), channel);
		channel.attr(Attributes.SESSION).set(user);
	}

	public static void unbind(Channel channel) {
		if (hasLogin(channel)) {
			userIdChannelMap.remove(getUser(channel).getUserId());
			channel.attr(Attributes.SESSION).set(null);
		}
	}

	public static boolean hasLogin(Channel channel) {
		return channel.hasAttr(Attributes.SESSION);
	}

	public static User getUser(Channel channel) {
		return channel.attr(Attributes.SESSION).get();
	}

	public static Channel getChannel(String userId) {
		return userIdChannelMap.get(userId);
	}
	/**
	 * 绑定群聊Id  群聊channelGroup
	 * @param groupId
	 * @param channelGroup
	 */
	public static void bindChannelGroup(String groupId, ChannelGroup channelGroup) {
		groupIdChannelGroupMap.put(groupId, channelGroup);
	}

	public static ChannelGroup getChannelGroup(String groupId) {
		return groupIdChannelGroupMap.get(groupId);
	}

	public static boolean addChannelGroup(Channel channel) {
		if (null == channel) {
			return false;
		}
		return channelGroup.add(channel);
	}

	public static boolean removeChannelGroup(Channel channel) {
		if (null == channel) {
			return false;
		}
		return channelGroup.remove(channel);
	}

	public static Integer channelGroupSize() {
		return channelGroup.size();
	}

	public static Channel findChannelGroup(ChannelId channelId) {
		if (null == channelId) {
			return null;
		}
		return channelGroup.find(channelId);
	}

}
