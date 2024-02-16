package min.deadline.mail.service;

import java.io.UnsupportedEncodingException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Properties;

import javax.mail.Message;
import javax.mail.MessagingException;
import javax.mail.PasswordAuthentication;
import javax.mail.Session;
import javax.mail.Transport;
import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ClassPathResource;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor 
public class MailService {

	private final SpringTemplateEngine templateEngine;
	@Value("${mail.id}")
	private String user;
	@Value("${mail.pw}")
	private String password;	

	//@Scheduled(cron = "0 * * * * *")
	@Scheduled(cron = "0 0 18 * * *")
	public void gmailSend() throws UnsupportedEncodingException {

        // SMTP 서버 정보를 설정한다.
        Properties prop = new Properties();
        prop.put("mail.smtp.host", "smtp.gmail.com"); 
        prop.put("mail.smtp.starttls.enable", "true");
        prop.put("mail.smtp.auth", "true");
        prop.put("mail.smtp.port", "587");
        prop.put("mail.smtp.ssl.protocols", "TLSv1.2");
        
        Session session = Session.getDefaultInstance(prop, new javax.mail.Authenticator() {
            protected PasswordAuthentication getPasswordAuthentication() {
                return new PasswordAuthentication(user, password);
            }
        });

        // 현재 날짜 구하기
        LocalDate now = LocalDate.now();
        
        DayOfWeek dayOfWeek = now.getDayOfWeek();
        
        // 제목
        String subject = "[알림] 일별 업무 시간 등록";
        
        // 금
        if(dayOfWeek.getValue() == 1 || dayOfWeek.getValue() == 2 || dayOfWeek.getValue() == 3 || dayOfWeek.getValue() == 4 || dayOfWeek.getValue() == 5) {
        
        }
        // 주말
        else {
        	return;
        }
        
        try {
            MimeMessage message = new MimeMessage(session);
            MimeMessageHelper helper = new MimeMessageHelper(message, true, "UTF-8");
            helper.setFrom(new InternetAddress(user, "알림요정"));            //수신자메일주소
            helper.setTo("mhan@bsgglobal.com"); 
//            helper.setCc("hwjo@bsgglobal.com");

            // Subject
            helper.setSubject(subject); //메일 제목을 입력

            //템플릿에 전달할 데이터 설정
            HashMap<String, String> emailValues = new HashMap<>();
        	emailValues.put("name", "jimin");
        	
            Context context = new Context();
            emailValues.forEach((key, value)->{
                context.setVariable(key, value);
            });
        	              
            //메일 내용 설정 : 템플릿 프로세스
            String html = templateEngine.process("index", context);
            helper.setText(html, true);
            
            //템플릿에 들어가는 이미지 cid로 삽입
            helper.addInline("image1", new ClassPathResource("static/images/image-1.jpeg"));
            
            // send the message
            Transport.send(message); //전송
            System.out.println("message sent successfully...");
        } catch (AddressException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (MessagingException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
}
