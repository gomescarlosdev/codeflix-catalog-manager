package br.com.gomescarlosdev.codeflix.catalog.infrastructure.helper;

import org.junit.jupiter.api.extension.BeforeEachCallback;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.api.extension.ExtensionContext;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.data.repository.CrudRepository;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;
import java.util.Collection;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@ActiveProfiles("test")
@ComponentScan(includeFilters = {
        @ComponentScan.Filter(type = FilterType.REGEX, pattern = ".*[GatewayImpl]")
})
@DataJpaTest
@ExtendWith(TestConfigHelper.CleanUpExtensions.class)
public @interface TestConfigHelper {

    class CleanUpExtensions implements BeforeEachCallback {
        @Override
        public void beforeEach(final ExtensionContext context) {
            final var repositories = SpringExtension.getApplicationContext(context)
                    .getBeansOfType(CrudRepository.class)
                    .values();
            cleanUp(repositories);
        }
        private void cleanUp(final Collection<CrudRepository> repositories){
            repositories.forEach(CrudRepository::deleteAll);
        }
    }

}
