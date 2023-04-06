package rafael.projects.store.springserviceproduct;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;

@SpringBootApplication
@EnableEurekaClient
public class SpringServiceProductApplication {

	public static void main(String[] args) {
		SpringApplication.run(SpringServiceProductApplication.class, args);
	}

}
