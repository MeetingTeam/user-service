package meetingteam.userservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;

import meetingteam.userservice.configs.AnomalyConfig;

@SpringBootApplication
@EnableConfigurationProperties(AnomalyConfig.class)
public class UserServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(UserServiceApplication.class, args);
    }
}
