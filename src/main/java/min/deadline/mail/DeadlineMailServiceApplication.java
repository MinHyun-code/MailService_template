package min.deadline.mail;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class DeadlineMailServiceApplication {

	public static void main(String[] args) {
		SpringApplication.run(DeadlineMailServiceApplication.class, args);
	}

}
