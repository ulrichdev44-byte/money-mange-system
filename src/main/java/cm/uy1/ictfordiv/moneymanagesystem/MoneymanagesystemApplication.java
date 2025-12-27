package cm.uy1.ictfordiv.moneymanagesystem;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.scheduling.annotation.EnableScheduling;

@EnableScheduling
@SpringBootApplication
public class MoneymanagesystemApplication {

    public static void main(String[] args) {
        SpringApplication.run(MoneymanagesystemApplication.class, args);
    }

}
