package app;


import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages={"app.user", "app"})
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }
}