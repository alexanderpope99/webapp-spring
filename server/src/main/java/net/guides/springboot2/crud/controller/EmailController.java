package net.guides.springboot2.crud.controller;

// import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

// import net.guides.springboot2.crud.services.EmailService;

@RestController
@RequestMapping("/email")
public class EmailController {

	@PostMapping("/send")
	public String sendEmailTemplate() {
		// emailService.send("apop2299@gmail.com", "Ai primit: 1 LEI");
		return "nothing happened";
	}
}
