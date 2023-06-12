package com.example.businessanalysis.controller.message;

import com.example.businessanalysis.utils.VerCodeGenerateUtils;
import javax.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

/**
 * @ClassName MailController
 * @Description 邮件验证码发送
 * @Author 74707
 * @Date 2022/11/8 8:53
 * @Version V1.0
 */
@CrossOrigin
@RestController
public class MailController {

  @Autowired
  private JavaMailSender mailSender;

  @Value("${spring.mail.username}")
  private String from;

  /**
   * @param tos     qq邮箱地址
   * @param request
   * @return String 生成的验证码
   * @Description 发送验证码
   */
  @RequestMapping("/mail")
  String commonEmail(@RequestParam String tos, HttpServletRequest request) {
    SimpleMailMessage message = new SimpleMailMessage();
    message.setFrom(from);
    message.setTo(tos);
    message.setSubject("您本次的验证码是");
    String verCode = VerCodeGenerateUtils.generateVerCode();
    message.setText("亲爱的用户,您好:\n"
        + "\n本次请求的邮件验证码为:" + verCode + ",请及时输入。（请勿泄露此验证码）\n"
        + "\n如非本人操作，请忽略该邮件。\n(这是一封通过自动发送的邮件，请不要直接回复）");
    mailSender.send(message);
    return verCode;
  }
}
