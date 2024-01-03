package com.yanolja.scbj;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableJpaAuditing
public class ScbjApplication {

    public static void main(String[] args) {
        SpringApplication.run(ScbjApplication.class, args);
    }

}
