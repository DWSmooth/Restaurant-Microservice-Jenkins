package com.smoothstack.restaurantmicroservice.service;


import com.smoothstack.common.models.*;
import com.smoothstack.common.repositories.*;
import com.smoothstack.common.services.CommonLibraryTestingService;

import static org.junit.jupiter.api.Assertions.assertEquals;

//import com.smoothstack.common.models.MenuItem;
//import com.smoothstack.common.repositories.MenuItemRepository;
//import com.smoothstack.common.models.RestaurantTag;
//import com.smoothstack.common.repositories.RestaurantTagRepository;
//import com.smoothstack.restaurantmicroservice.data.RestaurantInformation;

import com.smoothstack.restaurantmicroservice.data.RestaurantInformation;
import com.smoothstack.restaurantmicroservice.exception.RestaurantNotFoundException;
import com.smoothstack.restaurantmicroservice.exception.RestaurantTagNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
public class RestaurantServiceTest {

    @Autowired
    RestaurantService restaurantService;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    RestaurantTagRepository restaurantTagRepository;

    @Autowired
    UserRepository userRepository;

    @Autowired
    UserInformationRepository userInformationRepository;

    @Autowired
    LocationRepository locationRepository;

    @Autowired
    CommonLibraryTestingService testingService;

//    @Autowired
//    MenuItemRepository menuItemRepository;

//    @Autowired
//    RestaurantTagRepository restaurantTagRepository;

    @BeforeEach
//    @Disabled
    public void setUpTestEnvironment(){
        testingService.createTestData();
//        User testUser = new User();
//        testUser.setUserName("testUser");
//        testUser.setPassword("testPassword");
//        testUser = userRepository.saveAndFlush(testUser);
//
//        System.out.println("Created User");
//
//        Location noodlesLocation = new Location();
//        noodlesLocation.setAddress("400 S Duff Ave");
//        noodlesLocation.setCity("Ames");
//        noodlesLocation.setState("Iowa");
//        noodlesLocation.setZipCode(50010);
//        noodlesLocation = locationRepository.saveAndFlush(noodlesLocation);
//
//        System.out.println("Created Location");
//
//        Restaurant noodlesAndCo = new Restaurant();
//        noodlesAndCo.setName("Noodles & Company");
//        noodlesAndCo.setOwner(testUser);
//        noodlesAndCo.setLocation(noodlesLocation);
//        noodlesAndCo = restaurantRepository.saveAndFlush(noodlesAndCo);
//
//        System.out.println("Created Restaurant");

    }

//    List<RestaurantInformation> restaurants = new ArrayList<RestaurantInformation>();
//    List<Restaurant> dbRestaurant = restaurantRepository.findAll();
//            for(Restaurant r: dbRestaurant){
//        restaurants.add(getFrontendData(r.getId()));
//        System.out.println("restaurant: " + r.getId());
//    }

    @Test
    public void returnsAllRestaurants(){
        List<RestaurantInformation> testRestaurants = new ArrayList<RestaurantInformation>();
        List<Restaurant> dbRestaurants = restaurantRepository.findAll();
            for(Restaurant r: dbRestaurants){
                testRestaurants.add(restaurantService.getFrontendData(r.getId()));
            }
        List<RestaurantInformation> serviceRestaurants = restaurantService.getRestaurants();
            assertEquals(testRestaurants, serviceRestaurants);
    }



    @Test
    public void returnsCorrectRestaurantDetails(){
        Optional<Restaurant> testRestaurant = restaurantRepository.findById(1);
        RestaurantInformation restaurantInformation = restaurantService.getRestaurantDetails(1);
        System.out.println("Checking restaurant name is Noodles & Company");
        assertEquals("Noodles & Company", testRestaurant.get().getName());
    }


    @Test
    public void returnsSavedRestaurant(){
        Optional<User> dbUser = userRepository.findTopByUserName("unconfirmedTestCustomer");
        User testUser = dbUser.get();
        Optional<UserInformation> testUserInformation = userInformationRepository.findById(4);
        Location testLocation = new Location();
        testLocation.setAddress("400 S Duff Ave");
        testLocation.setCity("Ames");
        testLocation.setState("Iowa");
        testLocation.setZipCode(50010);
        testLocation = locationRepository.saveAndFlush(testLocation);
        System.out.println("Created Location");

        Restaurant newRestaurant = new Restaurant();
        newRestaurant.setName("Test Restaurant");
        newRestaurant.setOwner(testUser);
        newRestaurant.setLocation(testLocation);
//        newRestaurant = restaurantRepository.saveAndFlush(newRestaurant);
        Restaurant returnedRestaurant = restaurantService.createNewRestaurant(newRestaurant);
        System.out.println("Created Restaurant");

        System.out.println("Checking restaurant name is Test Restaurant");
        assertEquals("Test Restaurant", returnedRestaurant.getName());
    }

    @Test
    public void returnsUpdatedRestaurant(){
        Optional<Restaurant> dbRestaurant = restaurantRepository.findById(1);
        Restaurant testRestaurant = dbRestaurant.get();

        testRestaurant.setName("Oliver & Company");
        Restaurant returnedRestaurant = restaurantService.updateGivenRestaurant(testRestaurant, 1);
        System.out.println("Checking restaurant name is Oliver & Company");
        assertEquals("Oliver & Company", testRestaurant.getName());
    }


    @Test
    @Disabled
    public void returnsUpdatedRestaurantTags(){
        Optional<Restaurant> dbRestaurant = restaurantRepository.findById(1);
        List<RestaurantTag> dbRestaurantTags = dbRestaurant.get().getRestaurantTags();
        System.out.println("tags: " + dbRestaurantTags);
        Restaurant testRestaurant = dbRestaurant.get();

        testRestaurant.setName("Oliver & Company");
        Restaurant returnedRestaurant = restaurantService.updateGivenRestaurant(testRestaurant, 1);
        System.out.println("Checking restaurant name is Oliver & Company");
        assertEquals("Oliver & Company", testRestaurant.getName());
    }


    @Test
    public void confirmsDeletedRestaurant(){
        Optional<User> dbUser = userRepository.findTopByUserName("unconfirmedTestCustomer");
        User testUser = dbUser.get();
        Optional<UserInformation> testUserInformation = userInformationRepository.findById(4);
        Location testLocation = new Location();
        testLocation.setAddress("400 S Duff Ave");
        testLocation.setCity("Ames");
        testLocation.setState("Iowa");
        testLocation.setZipCode(50010);
        testLocation = locationRepository.saveAndFlush(testLocation);
        System.out.println("Created Location");

        Restaurant newRestaurant = new Restaurant();
        newRestaurant.setName("Test Restaurant");
        newRestaurant.setOwner(testUser);
        newRestaurant.setLocation(testLocation);
        newRestaurant = restaurantRepository.saveAndFlush(newRestaurant);
        System.out.println("newRestaurantId: " + newRestaurant.getId());
        String deleteMessage = restaurantService.deleteGivenRestaurant(17);
        assertEquals("Restaurant has been deleted successfully", deleteMessage);
    }

    @AfterEach
    void tearDown() {
        testingService = null;
    }
}
