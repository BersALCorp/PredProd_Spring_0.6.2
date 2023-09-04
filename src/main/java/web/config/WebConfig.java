package web.config;

import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.PropertySource;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;


@SpringBootApplication
@ComponentScan("web")
@PropertySource("classpath:application.properties")
@EnableJpaRepositories("web.repository")
@EntityScan("web.model")
@EnableTransactionManagement
public class WebConfig {
}