package com.enjoybt.scenario;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceTransactionManagerAutoConfiguration;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.ImportResource;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication
//@ImportResource(locations={"classpath:config/tx.xml"})
@Configuration
@EnableAutoConfiguration(exclude = { DataSourceTransactionManagerAutoConfiguration.class })
@EnableScheduling
@ComponentScan(basePackages={"com.enjoybt"})
public class AutoScenarioApplication {

    public static void main(String[] args) {
        SpringApplication.run(AutoScenarioApplication.class, args);
    }
}
