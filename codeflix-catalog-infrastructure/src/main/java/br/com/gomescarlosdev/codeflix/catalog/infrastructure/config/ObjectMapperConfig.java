package br.com.gomescarlosdev.codeflix.catalog.infrastructure.config;

import br.com.gomescarlosdev.codeflix.catalog.infrastructure.config.json.Json;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class ObjectMapperConfig {

    @Bean
    public ObjectMapper objectMapper(){
        return Json.mapper();
    }

}
