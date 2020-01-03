package datapipeline;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        KafkaManager km= KafkaManager.getInstance();
        SpringApplication.run(Application.class, args);
    }
}
