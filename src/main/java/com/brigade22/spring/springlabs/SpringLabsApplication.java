package com.brigade22.spring.springlabs;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import io.swagger.v3.oas.annotations.servers.Server;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@OpenAPIDefinition(
		info = @Info(
				title = "Brigade 22 online dictionary",
				contact = @Contact(
						name = "Nikita Sutulov, Olexandr Nazarenko, Sviatoslav Shesterov, Svitlana Barytska",
						email = "notsid212@gmail.com"
				),
				description = "This is a RESTful service written on Spring framework by brigade 22, which is an online dictionary.",
				version = "1.0",
				license = @License(name = "Apache 2.0", url = "https://www.apache.org/licenses/LICENSE-2.0.html")
		),
		servers = {
				@Server(url = "http://localhost:8080", description = "test server")
		}
)
@SpringBootApplication
public class SpringLabsApplication {
	public static void main(String[] args) {
		SpringApplication.run(SpringLabsApplication.class, args);
	}
}
