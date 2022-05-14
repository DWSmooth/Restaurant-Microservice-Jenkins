package com.smoothstack.restaurantmicroservice;

//import com.smoothstack.restaurantmicroservice.Model.Location;
import com.smoothstack.common.models.Location;

//import com.smoothstack.restaurantmicroservice.Model.Restaurant;
import com.smoothstack.common.models.Restaurant;

//import com.smoothstack.restaurantmicroservice.Model.User;
import com.smoothstack.common.models.User;

//import com.smoothstack.restaurantmicroservice.Repository.LocationRepository;
import com.smoothstack.common.repositories.LocationRepository;

//import com.smoothstack.restaurantmicroservice.Repository.UserRepository;
import com.smoothstack.common.repositories.UserRepository;

//import com.smoothstack.restaurantmicroservice.Repository.RestaurantRepository;
import com.smoothstack.common.repositories.RestaurantRepository;


import org.hibernate.SessionFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.domain.EntityScan;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.data.jpa.provider.HibernateUtils;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.transaction.Transactional;
import java.util.List;

@EntityScan("com.smoothstack")
@ComponentScan("com.smoothstack")
@EnableJpaRepositories("com.smoothstack")
@SpringBootApplication
//public class RestaurantMicroserviceApplication {
public class RestaurantMicroserviceApplication {

//	private static final Logger log = LoggerFactory.getLogger(RestaurantMicroserviceApplication.class);

//	@Autowired
//	LocationRepository locationRepository;

//	@Autowired
//	UserRepository userRepository;

//	@Autowired
//	RestaurantRepository restaurantRepository;


	public static void main(String[] args) {
		SpringApplication.run(RestaurantMicroserviceApplication.class, args);
	}


//	@Override
//	public void run(String... args) {
//		log.info("Start Application");

//		SessionFactory sessionFactory =

//		log.info("Creating new Location");
//		locationRepository.save(new Location("Desert Dine Inn", "2121 Fake St", "Yuma", "Arizona", 12345));
//		locationRepository.save(new Location("The Watering Hole", "3131 Fake St", "Tucson", "Arizona", 12345));
//		log.info("Locations found with findAll():");
//		log.info("------------------------------");
//		List<Location> locationList = locationRepository.findAll();
//		session.get
//		for (Location location : locationRepository.findAll()) {
//			log.info(location.toString());
//		}
//		log.info("");

//		log.info("Creating new User");
//		repository.save(new User("restaurantOwner1", "123ABC"));
//		userRepository.save(new User("restaurantOwner1", "123ABC"));
//		userRepository.save(new User("restaurantOwner2", "123ABC"));

//		log.info("Users found with findAll():");
//		log.info("------------------------------");
//		for (User user : userRepository.findAll()) {
//			log.info(user.toString());
//		}
//		log.info("");


//		log.info("Creating new Restaurant");
//		restaurantRepository.save(new Restaurant(1, 1, "Desert Dine Inn"));
//		restaurantRepository.save(new Restaurant(2, 2, "The Watering Hole"));

//		log.info("Restaurants found with findAll():");
//		log.info("------------------------------");
//		for (Restaurant restaurant : restaurantRepository.findAll()) {
//			log.info(restaurant.toString());
//		}
//	}

}