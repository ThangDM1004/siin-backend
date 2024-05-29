package com.example.exe202backend.services;

import com.example.exe202backend.request.MailRequest;
import com.example.exe202backend.response.ResponseObject;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import jakarta.mail.MessagingException;
import jakarta.mail.internet.MimeMessage;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;
import org.springframework.ui.freemarker.FreeMarkerTemplateUtils;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;

@Service
public class MailService {
    @Autowired
    private JavaMailSender mail;
    @Autowired
    private Configuration config;

    public ResponseEntity<ResponseObject> sendMail(MailRequest request, Map<String,Object> model){
        MimeMessage message = mail.createMimeMessage();
        try {
            MimeMessageHelper helper = new MimeMessageHelper(message,MimeMessageHelper.MULTIPART_MODE_MIXED_RELATED,
                    StandardCharsets.UTF_8.name());
            Template t = config.getTemplate("payment-mail.ftl");
            String html = FreeMarkerTemplateUtils.processTemplateIntoString(t,model);

            helper.setTo(request.getTo());
            helper.setText(html,true);
            helper.setSubject(request.getSubject());
            helper.setFrom(request.getFrom());
            mail.send(message);

            return ResponseEntity.status(HttpStatus.OK).body(new ResponseObject(
                    "Send mail success",
                    ""
            ));
        }catch (MessagingException | IOException | TemplateException e){
        }
        return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(new ResponseObject(
                "Send mail failed",
                ""
        ));
    }
}
