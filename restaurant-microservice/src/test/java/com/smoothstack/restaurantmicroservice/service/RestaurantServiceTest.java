package com.smoothstack.restaurantmicroservice.service;

import com.smoothstack.common.models.*;
import com.smoothstack.common.repositories.*;
import com.smoothstack.common.services.CommonLibraryTestingService;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.smoothstack.restaurantmicroservice.exception.*;
import org.junit.jupiter.api.*;

import com.smoothstack.restaurantmicroservice.data.RestaurantInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
public class RestaurantServiceTest {

    @Autowired
    RestaurantService restaurantService;
    @Autowired
    RestaurantRepository restaurantRepository;
    @Autowired
    UserRepository userRepository;
    @Autowired
    UserInformationRepository userInformationRepository;
    @Autowired
    LocationRepository locationRepository;
    @Autowired
    CommonLibraryTestingService testingService;

    @BeforeEach
//    @Disabled
    public void setUpTestEnvironment(){
        testingService.createTestData();
    }

    @Test
//    @Disabled
    public void returnsAllRestaurants() throws Exception {
        List<RestaurantInformation> testRestaurants = new ArrayList<RestaurantInformation>();
        List<Restaurant> dbRestaurants = restaurantRepository.findAll();
            for(Restaurant r: dbRestaurants){
                testRestaurants.add(restaurantService.getRestaurantInformation(r.getId()));
            }
        List<RestaurantInformation> serviceRestaurants = restaurantService.getRestaurants();

            assertEquals(testRestaurants, serviceRestaurants);
    }


    @Test
//    @Disabled
    public void returnsCorrectRestaurantDetails(){
        Optional<Restaurant> testRestaurant = restaurantRepository.findById(2);
        RestaurantInformation restaurantInformation = restaurantService.getRestaurantDetails(2);

        assertEquals(restaurantInformation.getName(), testRestaurant.get().getName());
    }


    @Test
//    @Disabled
    public void returnsCorrectRestaurantDetailsNotFound(){
        boolean notFoundExceptionThrown = false;

        try {
            restaurantService.getRestaurantDetails(0);
        } catch(RestaurantNotFoundException restaurantNotFoundException){
            notFoundExceptionThrown = true;
        }

        assertTrue(notFoundExceptionThrown);
    }

/*
    @Test
    @Disabled
    public void returnsSavedRestaurant() throws Exception {
        Restaurant newRestaurant = new Restaurant();
        Optional<User> dbUser = userRepository.findById(1);
        User testUser = dbUser.get();
        Optional<Location> dbLocation = locationRepository.findById(1);
        Location testLocation = dbLocation.get();

        newRestaurant.setOwner(testUser);
        newRestaurant.setLocation(testLocation);
        newRestaurant.setName("Test Restaurant");
        String confirmRestaurantSave = restaurantService.createNewRestaurant(newRestaurant);

        assertEquals("Restaurant 'Test Restaurant' created successfully. Id:4", confirmRestaurantSave);
    }


    @Test
    @Disabled
    public void returnsSavedRestaurantLocationNotFound(){
        boolean locationNotFound = false;
        boolean userNotFound = false;
        Optional<User> dbUser = userRepository.findTopByUserName("unconfirmedTestCustomer");
        User testUser = dbUser.get();
        Optional<Location> dbLocation = locationRepository.findById(1);
        Location testLocation = dbLocation.get();
        testLocation.setId(0);

        Restaurant newRestaurant = new Restaurant();
        newRestaurant.setOwner(testUser);
        newRestaurant.setLocation(testLocation);
        newRestaurant.setName("Test Restaurant");

        try{
            restaurantService.createNewRestaurant(newRestaurant);
        } catch(LocationNotFoundException locationNotFoundException){
            locationNotFound = true;
        } catch(UserNotFoundException userNotFoundException){
            userNotFound = false;
        }

        assertTrue(locationNotFound);
        assertFalse(userNotFound);
    }


    @Test
    @Disabled
    public void returnsSavedRestaurantUserNotFound(){
        boolean locationNotFound = false;
        boolean userNotFound = false;
        Optional<Location> dbLocation = locationRepository.findById(1);
        Location testLocation = dbLocation.get();
        Optional<User> dbUser = userRepository.findTopByUserName("unconfirmedTestCustomer");
        User testUser = dbUser.get();
        testUser.setId(0);

        Restaurant newRestaurant = new Restaurant();
        newRestaurant.setOwner(testUser);
        newRestaurant.setLocation(testLocation);
        newRestaurant.setName("Test Restaurant");

        try{
            restaurantService.createNewRestaurant(newRestaurant);
        } catch(LocationNotFoundException locationNotFoundException){
            locationNotFound = false;
        } catch(UserNotFoundException userNotFoundException){
            userNotFound = true;
        }

        assertFalse(locationNotFound);
        assertTrue(userNotFound);
    }


    @Test
    @Disabled
    public void returnsUpdatedRestaurant(){
        Optional<Restaurant> dbRestaurant = restaurantRepository.findById(1);
        RestaurantInformation testRestaurant = new RestaurantInformation();
        testRestaurant.setRestaurantId(dbRestaurant.get().getId());
        testRestaurant.setName("Oliver & Company");
        String returnedRestaurant = restaurantService.updateGivenRestaurant(testRestaurant, 1);

        assertEquals("Restaurant has been updated successfully", returnedRestaurant);
    }


    @Test
    @Disabled
    public void returnsUpdatedRestaurantLocationNotFound(){
        boolean locationNotFound = false;
        boolean restaurantNotFound = false;
        boolean userNotFound = false;
        Optional<Restaurant> dbRestaurant = restaurantRepository.findById(1);
        RestaurantInformation testRestaurant = new RestaurantInformation();
        testRestaurant.setRestaurantId(dbRestaurant.get().getId());

        Location testLocation = new Location();
        testLocation.setId(0);
        testLocation.setAddress("400 S Duff Ave");
        testLocation.setCity("Ames");
        testLocation.setState("Iowa");
        testLocation.setZipCode(50010);
        testRestaurant.setLocation_id(locationRepository.save(testLocation).getId());

        try{
            restaurantService.updateGivenRestaurant(testRestaurant, 1);
        } catch(LocationNotFoundException locationNotFoundException){
            locationNotFound = true;
        } catch(RestaurantNotFoundException restaurantNotFoundException){
            restaurantNotFound = false;
        } catch(UserNotFoundException userNotFoundException){
            userNotFound = false;
        }

        assertTrue(locationNotFound);
        assertFalse(restaurantNotFound);
        assertFalse(userNotFound);
    }


    @Test
    @Disabled
    public void returnsUpdatedRestaurantNotFound(){
        boolean locationNotFound = false;
        boolean restaurantNotFound = false;
        boolean userNotFound = false;
        Optional<Restaurant> dbRestaurant = restaurantRepository.findById(1);
        RestaurantInformation testRestaurant = new RestaurantInformation();
        testRestaurant.setRestaurantId(dbRestaurant.get().getId());

        try{
            restaurantService.updateGivenRestaurant(testRestaurant, 0);
        } catch(LocationNotFoundException locationNotFoundException){
            locationNotFound = false;
        } catch(RestaurantNotFoundException restaurantNotFoundException){
            restaurantNotFound = true;
        } catch(UserNotFoundException userNotFoundException){
            userNotFound = false;
        }

        assertFalse(locationNotFound);
        assertTrue(restaurantNotFound);
        assertFalse(userNotFound);
    }


    @Test
    @Disabled
    public void returnsUpdatedRestaurantUserNotFound(){
        boolean locationNotFound = false;
        boolean restaurantNotFound = false;
        boolean userNotFound = false;
        Optional<Restaurant> dbRestaurant = restaurantRepository.findById(1);
        RestaurantInformation testRestaurant = new RestaurantInformation();
        testRestaurant.setRestaurantId(dbRestaurant.get().getId());

        User testUser = new User();
        testUser.setId(0);
        testUser.setUserName("abc");
        testUser.setPassword("123");
        testRestaurant.setOwner_id(userRepository.save(testUser).getId());

        try{
            restaurantService.updateGivenRestaurant(testRestaurant, 1);
        } catch(LocationNotFoundException locationNotFoundException){
            locationNotFound = false;
        } catch(RestaurantNotFoundException restaurantNotFoundException){
            restaurantNotFound = false;
        } catch(UserNotFoundException userNotFoundException){
            System.out.println("user not found !!!!!");
            userNotFound = true;
        }

        assertFalse(locationNotFound);
        assertFalse(restaurantNotFound);
        assertTrue(userNotFound);
    }
*/

    @Test
//    @Disabled
    public void returnsUpdatedRestaurantTags(){
        Optional<Restaurant> dbRestaurant = restaurantRepository.findById(1);
        Restaurant testRestaurant = dbRestaurant.get();
        String returnedRestaurant = restaurantService.updateGivenRestaurantTags( 1, 6);

        assertEquals("Restaurant Tag successfully added to restaurant", returnedRestaurant);
    }


    @Test
//    @Disabled
    public void returnsUpdatedRestaurantTagsRestaurantNotFound(){
        boolean restaurantNotFound = false;
        boolean restaurantTagNotFound = false;
        boolean restaurantTagAlreadyExists = false;

        try{
            restaurantService.updateGivenRestaurantTags( 0, 6);
        } catch(RestaurantNotFoundException restaurantNotFoundException){
            restaurantNotFound = true;
        } catch(RestaurantTagNotFoundException restaurantTagNotFoundException){
            restaurantTagNotFound = false;
        } catch(RestaurantTagAlreadyExistsException restaurantTagAlreadyExistsException){
            restaurantTagAlreadyExists = false;
        }

        assertTrue(restaurantNotFound);
        assertFalse(restaurantTagNotFound);
        assertFalse(restaurantTagAlreadyExists);
    }


    @Test
//    @Disabled
    public void returnsUpdatedRestaurantTagNotFound(){
        boolean restaurantNotFound = false;
        boolean restaurantTagNotFound = false;
        boolean restaurantTagAlreadyExists = false;

        try{
            restaurantService.updateGivenRestaurantTags( 1, 0);
        } catch(RestaurantNotFoundException restaurantNotFoundException){
            restaurantNotFound = true;
        } catch(RestaurantTagNotFoundException restaurantTagNotFoundException){
            restaurantTagNotFound = true;
        } catch(RestaurantTagAlreadyExistsException restaurantTagAlreadyExistsException){
            restaurantTagAlreadyExists = false;
        }

        assertFalse(restaurantNotFound);
        assertTrue(restaurantTagNotFound);
        assertFalse(restaurantTagAlreadyExists);
    }


    @Test
//    @Disabled
    public void returnsUpdatedRestaurantTagAlreadyExists(){
        boolean restaurantNotFound = false;
        boolean restaurantTagNotFound = false;
        boolean restaurantTagAlreadyExists = false;

        try{
            restaurantService.updateGivenRestaurantTags( 1, 1);
        } catch(RestaurantNotFoundException restaurantNotFoundException){
            restaurantNotFound = false;
        } catch(RestaurantTagNotFoundException restaurantTagNotFoundException){
            restaurantTagNotFound = false;
        } catch(RestaurantTagAlreadyExistsException restaurantTagAlreadyExistsException){
            restaurantTagAlreadyExists = true;
        }

        assertFalse(restaurantNotFound);
        assertFalse(restaurantTagNotFound);
        assertTrue(restaurantTagAlreadyExists);
    }


    @Test
//    @Disabled
    public void confirmsDeletedRestaurant(){
        Optional<User> dbUser = userRepository.findTopByUserName("unconfirmedTestCustomer");
        User testUser = dbUser.get();
        Optional<UserInformation> testUserInformation = userInformationRepository.findById(4);
        Optional<Location> dbLocation = locationRepository.findById(1);
        Location testLocation = dbLocation.get();

        Restaurant newRestaurant = new Restaurant();
        newRestaurant.setName("Test Restaurant");
        newRestaurant.setOwner(testUser);
        newRestaurant.setLocation(testLocation);
        newRestaurant = restaurantRepository.saveAndFlush(newRestaurant);
        String deleteMessage = restaurantService.deleteGivenRestaurant(newRestaurant.getId());

        assertEquals("Restaurant has been deleted successfully", deleteMessage);
    }


    @Test
//    @Disabled
    public void confirmsDeletedRestaurantNotFound(){
        boolean notFoundExceptionThrown = false;

        try {
            restaurantService.deleteGivenRestaurant(0);
        } catch(RestaurantNotFoundException restaurantNotFoundException){
            notFoundExceptionThrown = true;
        }

        assertTrue(notFoundExceptionThrown);
    }


    @AfterEach
    @Disabled
    void tearDown() {
    }
}
