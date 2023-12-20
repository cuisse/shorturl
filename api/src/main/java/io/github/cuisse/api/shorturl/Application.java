package io.github.cuisse.api.shorturl;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication(scanBasePackages = "io.github.cuisse")
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(Application.class, args);
    }

}
