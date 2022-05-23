package com.smoothstack.restaurantmicroservice.service;


import com.smoothstack.common.models.*;
import com.smoothstack.common.repositories.*;
import com.smoothstack.common.services.CommonLibraryTestingService;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.*;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;

import com.smoothstack.restaurantmicroservice.data.RestaurantInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
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

    @BeforeEach
    public void setUpTestEnvironment(){
        testingService.createTestData();
    }

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
        Optional<Restaurant> testRestaurant = restaurantRepository.findById(2);
        System.out.println("restaurantID: " + testRestaurant.get().getId());
        System.out.println("restaurantName: " + testRestaurant.get().getName());
        RestaurantInformation restaurantInformation = restaurantService.getRestaurantDetails(2);
        System.out.println("Checking restaurant name is Noodles & Company");
        assertEquals(restaurantInformation.getName(), testRestaurant.get().getName());
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
        assertEquals(returnedRestaurant.getName(), testRestaurant.getName());
    }


    @Test
    @Disabled
    public void returnsUpdatedRestaurantTags(){
        Optional<Restaurant> dbRestaurant = restaurantRepository.findById(1);
        Restaurant testRestaurant = dbRestaurant.get();
        List<String> dbRestaurantTags = dbRestaurant.get().getRestaurantTags()
                .stream()
                    .map(tag -> tag.getName())
            .collect(Collectors.toList());
            for(String s: dbRestaurantTags){
                System.out.println("tags: " + s);
            }
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
    @Disabled
    void tearDown() {
//        testingService = null;
//         this doesn't work, need a way to wipe all data after each test
    }
}
