package com.smoothstack.restaurantmicroservice;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;

@EntityScan("com.smoothstack")
@ComponentScan("com.smoothstack")
@EnableJpaRepositories("com.smoothstack")
@EnableEurekaClient
@SpringBootApplication
public class RestaurantMicroserviceApplication {

	public static void main(String[] args) {
		SpringApplication.run(RestaurantMicroserviceApplication.class, args);
	}


}