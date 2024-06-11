package com.shihu.util;



import jakarta.annotation.Resource;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;



/**
 * @Author vocmi
 * @Email 2686782542@qq.com
 * @Date 2024-06-01
 */
@Component
public class SendMailUtil {
    @Resource
    private JavaMailSender javaMailSender;

    /**
     *  发送邮件
     * @param messageHeader 消息标题
     * @param content 正文内容
     * @param sender 发送者
     * @param receiver 接收者
     */
    @Async("smsExecutor")
    public void sendSimpleMail(String messageHeader, String content, String sender, String receiver) {
        SimpleMailMessage simpleMailMessage = new SimpleMailMessage();
        simpleMailMessage.setSubject(messageHeader);
        simpleMailMessage.setText(content);
        simpleMailMessage.setTo(receiver);
        simpleMailMessage.setFrom(sender);
        javaMailSender.send(simpleMailMessage);
    }
}
