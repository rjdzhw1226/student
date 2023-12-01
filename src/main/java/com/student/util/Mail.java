package com.student.util;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import javax.mail.internet.MimeUtility;
import java.io.UnsupportedEncodingException;
import java.util.Arrays;
import java.util.List;
import java.util.Properties;

public class Mail {

    public Mail() {
    }

    public Mail(String toEmail) {
        this.toEmail = toEmail;
    }

    public String emailHost = "smtp.163.com";       //发送邮件的主机
    public String transportType = "smtp";           //邮件发送的协议
    public String fromUser = "cf656779436";           //发件人名称
    public String fromEmail = "cf656779436@163.com";  //发件人邮箱
    public String authCode = "KYUKKMXGYAAQCBHW";    //发件人邮箱授权码
    public String toEmail = "656779436@qq.com";   //默认收件人邮箱

    public String subject = "验证码";           //主题信息

    public void ClientTestA(int intFlag) throws UnsupportedEncodingException, MessagingException {

        Properties props = new Properties();
        props.setProperty("mail.transport.protocol", transportType);
        props.setProperty("mail.host", emailHost);
        props.setProperty("mail.user", fromUser);
        props.setProperty("mail.from", fromEmail);

        Session session = Session.getInstance(props, null);
        session.setDebug(true);

        MimeMessage message = new MimeMessage(session);

        String formName = MimeUtility.encodeWord("辛佳城") + " <" + fromEmail + ">";
        InternetAddress from = new InternetAddress(formName);
        message.setFrom(from);

        InternetAddress to = new InternetAddress(toEmail);
        message.setRecipient(Message.RecipientType.TO, to);
        message.setSubject(subject);
        //language
        message.setContent("<h1>验证码为："+ intFlag +"</h1>", "text/html;charset=UTF-8");
        message.saveChanges();

        Transport transport = session.getTransport();
        transport.connect(null, null, authCode);
        transport.sendMessage(message, message.getAllRecipients()); // 发送
    }
}
