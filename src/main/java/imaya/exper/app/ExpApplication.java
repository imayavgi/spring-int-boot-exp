package imaya.exper.app;

import imaya.exper.imaya.exper.framework.CuratorClientFactoryBean;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.ImportResource;
import org.springframework.context.annotation.Scope;
import org.springframework.integration.config.EnableIntegration;
import org.springframework.scheduling.annotation.EnableScheduling;

@SpringBootApplication(scanBasePackages = {"imaya.exper.svcs"})
@EnableIntegration
@EnableScheduling
//@ComponentScan("imaya.exper.svcs")
@ImportResource("classpath*:/spring-int-context.xml")
public class ExpApplication {

    public static void main(String[] args) {
        SpringApplication.run(ExpApplication.class, args);

    }

    @Bean
    @Scope(value = ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    CuratorClientFactoryBean createClientFactory() {
        return new CuratorClientFactoryBean("127.0.0.1", new ExponentialBackoffRetry(1000, 3));
    }
}
