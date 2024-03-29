package br.com.gomescarlosdev.codeflix.catalog.infrastructure.helper;

import br.com.gomescarlosdev.codeflix.catalog.infrastructure.config.WebServerConfiguration;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test")
@SpringBootTest(classes = WebServerConfiguration.class)
@ExtendWith(CleanUpExtension.class)
public @interface SpringBootTestHelper { }
