package com.sp.yangshengai;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.freemarker.FreeMarkerAutoConfiguration;

@SpringBootApplication(exclude = { FreeMarkerAutoConfiguration.class })
public class YangshengAiApplication {

    public static void main(String[] args) {
        SpringApplication.run(YangshengAiApplication.class, args);
    }

}
