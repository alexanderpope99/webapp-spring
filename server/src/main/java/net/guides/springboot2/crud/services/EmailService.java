package net.guides.springboot2.crud.services;

// import javax.mail.internet.MimeMessage;

// import org.springframework.beans.factory.annotation.Autowired;
// import org.springframework.mail.MailException;
// import org.springframework.mail.SimpleMailMessage;
// import org.springframework.mail.javamail.JavaMailSender;
// import org.springframework.mail.javamail.MimeMessageHelper;
// import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

@Service
public class EmailService {
  // @Autowired
  // private JavaMailSender mailSender;
	
  // @Async
	// public void send(String to, String email) {
  //     SimpleMailMessage message = new SimpleMailMessage();
  //     message.setText(email);
  //     message.setTo(to);
  //     message.setSubject("1 leu");
  //     message.setFrom("bogdan12167@gmail.com");
  //     mailSender.send(message);
	// }
	public String send(String to, String email) { return "DEMO TEXT"; }
}
