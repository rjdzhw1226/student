package com.student.mapper;

import com.student.pojo.ReadResult;
import com.student.pojo.dto.messageDto;
import com.student.pojo.user;
import com.student.pojo.dto.userDto;
import com.student.pojo.vo.MessageVo;
import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;

import java.util.Date;
import java.util.List;

@Mapper
public interface LoginMapper {
    @Select("select username, password from user where username = #{username}")
    public userDto login(String username);

    @Select("select id, username, image, password from user where username = #{username}")
    public user query(String username);

    @Select("select image from user where username = #{username}")
    public String queryI(String username);

    @Insert("insert into user( id,birthday,gender,username,password,station,telephone) values(#{id},#{birthday},#{gender},#{username},#{password},#{station},#{telephone}) ")
    public void register(user user);

    @Select("select count(1) from user where username = #{username}")
    public int registerCount(String username);

    @Update("update user set password = #{password} where username = #{username}")
    public int updatePass(String password, String username);
    @Select("select * from user where id = #{userId}")
    public user queryId(String userId);

    @Select("select username from user where id in ${userIds}")
    List<String> queryIds(String userIds);
    @Update("update message set read_type = #{readType} where msg_id = #{messageId}")
    public void updateMessage(String messageId, String readType);

    @Insert("insert into message (msg_id, to_group_id, to_id, from_id, msg, type, file_type, send_time, read_type) values (#{messageId},#{toGroupId},#{toUserId},#{fromUserId},#{message},#{type},#{fileType},NOW(),#{readType})")
    public void saveMessage(MessageVo vo);

    @Insert("insert into group_m (group_id, user_id) values (#{groupId}, #{userId})")
    public void saveUserIds(String groupId, String userId);
    @Select("select user_id from group_m where group_id = #{groupId}")
    List<String> findGroup(String groupId);
    @Insert("insert into group_a (group_id,chat_name,chat_type,status,size_m,create_by) values (#{groupId},#{title},#{type},'1',#{size}, #{userName})")
    public void saveGroup(String groupId, String userName, int size, String type, String title);

    @Select("select * from message where send_time between #{start} and #{end} and to_id = #{userId} or to_group_id in (select group_id from group_m where user_id = #{userId}) order by send_time")
    public List<MessageVo> queryMessage(messageDto message);
    @Select("select * from message where send_time < #{end} and to_id = #{userId} or to_group_id in (select group_id from group_m where user_id = #{userId}) order by send_time")
    public List<MessageVo> queryMessageEndTime(messageDto message);

    @Insert("insert into message_index (msg_id,group_id,to_id,from_id,read_time) values (#{messageId}, #{id},#{fromId}, #{userId}, NOW())")
    public void saveReadMessage(String messageId, String id, String fromId, String userId);

    @Update("update message_index set read_type = read_type + 1, read_time = #{date} where msg_id =#{messageId} and group_id =#{groupId} and to_id =#{toUserId} and from_id =#{fromUserId}")
    public void update(String fromUserId, String messageId, String groupId, String toUserId, Date date);

    @Select("select t.readCount as readCount, t.groupCount as groupCount, (t.groupCount - t.readCount - 1) as unReadCount from " +
            "(select " +
            "(select size_m as groupCount from group_a where group_id = #{groupId}) as groupCount, " +
            "readType + 1 as readCount, " +
            "from message where to_group_id = #{groupId} and msg_id = #{messageId} and from_id = #{fromUserId}) as t")
    public ReadResult selectCountGroup(String groupId, String messageId, String toUserId, String fromUserId);
}
