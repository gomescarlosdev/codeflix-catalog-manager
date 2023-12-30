package br.com.gomescarlosdev.codeflix.catalog.infrastructure;

import br.com.gomescarlosdev.codeflix.catalog.infrastructure.config.WebServerConfiguration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class Application {

    public static void main(String[] args) {
        SpringApplication.run(WebServerConfiguration.class, args);
    }
}
