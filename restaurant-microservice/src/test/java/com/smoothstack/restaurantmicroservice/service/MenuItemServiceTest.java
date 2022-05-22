package com.smoothstack.restaurantmicroservice.service;

import com.smoothstack.common.models.*;
import com.smoothstack.common.repositories.*;
import com.smoothstack.common.services.CommonLibraryTestingService;
import com.smoothstack.restaurantmicroservice.data.MenuItemInformation;
import com.smoothstack.restaurantmicroservice.data.RestaurantInformation;
import com.smoothstack.restaurantmicroservice.exception.MenuItemNotFoundException;
import com.smoothstack.restaurantmicroservice.exception.RestaurantNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MenuItemServiceTest {

    @Autowired
    MenuItemService menuItemService;

    @Autowired
    MenuItemRepository menuItemRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    CommonLibraryTestingService testingService;

    @BeforeEach
    public void setUpTestEnvironment(){
        testingService.createTestData();
    }


//    public List<MenuItemInformation> getAllMenuItems(){
//        try {
//            List<MenuItemInformation> menuItems = new ArrayList<MenuItemInformation>();
//            List<MenuItem> dbMenuItems = menuItemRepository.findAll();
//            for(MenuItem m: dbMenuItems){
//                menuItems.add(MenuItemInformation.getFrontendData(m));
//            }
//            return menuItems;
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//        return null;
//    }

    @Test
    public void returnsAllMenuItems(){

    }



//    public List<MenuItemInformation> getMenuItemDetails(Integer restaurantId){
//        try {
//            List<MenuItemInformation> restaurantMenuItems = new ArrayList<MenuItemInformation>();
//            List<MenuItem> menuItems = getMenuItems(restaurantId);
//            if(getMenuItems(restaurantId) == null) throw new MenuItemNotFoundException("restaurantId-" + restaurantId);
//            for(MenuItem m: menuItems){
//                restaurantMenuItems.add(MenuItemInformation.getFrontendData(m));
//            }
//            return restaurantMenuItems;
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//        return null;
//    }

//    private List<MenuItem> getMenuItems(Integer restaurantId){
//        Restaurant restaurant = new Restaurant();
//        List<MenuItem> menuItems = new ArrayList<MenuItem>();
//        try {
//            restaurant = restaurantRepository.getById(restaurantId);
//            if(restaurant.getId() == null) throw new RestaurantNotFoundException("restaurantId-" + restaurantId);
//            menuItems = menuItemRepository.findAllByRestaurants(restaurant);
//            return menuItems;
//        } catch(Exception e){
//            e.printStackTrace();
//        }
//        return null;
//    }


    @Test
    public void returnsCorrectMenuItemDetails(){

    }


//    public MenuItem createNewMenuItem(MenuItem menuItem){
//        try {
//            MenuItem newMenuItem = menuItemRepository.save(menuItem);
//            return menuItem;
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//        return null;
//    }


    @Test
    public void returnsSavedMenuItem(){

    }


//    public MenuItem updateGivenMenuItem(MenuItem newMenuItem, Integer menuItemId){
//        try {
//            MenuItem currentMenuItem = menuItemRepository.getById(menuItemId);
//            if(currentMenuItem.getId() == null) throw new MenuItemNotFoundException("menuItemId-" + menuItemId);
//            currentMenuItem.setRestaurants(newMenuItem.getRestaurants());
//            currentMenuItem.setName(newMenuItem.getName());
//            currentMenuItem.setDescription(newMenuItem.getDescription());
//            currentMenuItem.setPrice(newMenuItem.getPrice());
//            return menuItemRepository.save(currentMenuItem);
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//        return null;
//    }


    @Test
    public void returnsUpdatedMenuItem(){

    }


//    public String deleteGivenMenuItem(Integer id) {
//        try {
//            MenuItem oldMenuItem = menuItemRepository.getById(id);
//            menuItemRepository.deleteById(id);
//            return "Menu item has been deleted successfully";
//        } catch (Exception e){
//            e.printStackTrace();
//        }
//        return "That MenuItem could not be deleted. Please try again.";
//    }


    @Test
    public void confirmsDeletedMenuItem(){

    }



    @AfterEach
    void tearDown() {
        testingService = null;
    }
}











//    @Autowired
//    MenuItemRepository menuItemRepository;

//    @Autowired
//    RestaurantTagRepository restaurantTagRepository;

//    List<RestaurantInformation> restaurants = new ArrayList<RestaurantInformation>();
//    List<Restaurant> dbRestaurant = restaurantRepository.findAll();
//            for(Restaurant r: dbRestaurant){
//        restaurants.add(getFrontendData(r.getId()));
//        System.out.println("restaurant: " + r.getId());
//    }

//    @Test
//    public void returnsAllRestaurants(){
//        List<RestaurantInformation> testRestaurants = new ArrayList<RestaurantInformation>();
//        List<Restaurant> dbRestaurants = restaurantRepository.findAll();
//        for(Restaurant r: dbRestaurants){
//            testRestaurants.add(restaurantService.getFrontendData(r.getId()));
//        }
//        List<RestaurantInformation> serviceRestaurants = restaurantService.getRestaurants();
//        assertEquals(testRestaurants, serviceRestaurants);
//    }
//
//
//
//    @Test
//    public void returnsCorrectRestaurantDetails(){
//        Optional<Restaurant> testRestaurant = restaurantRepository.findById(1);
//        RestaurantInformation restaurantInformation = restaurantService.getRestaurantDetails(1);
//        System.out.println("Checking restaurant name is Noodles & Company");
//        assertEquals("Noodles & Company", testRestaurant.get().getName());
//    }
//
//
//    @Test
//    public void returnsSavedRestaurant(){
//        Optional<User> dbUser = userRepository.findTopByUserName("unconfirmedTestCustomer");
//        User testUser = dbUser.get();
//        Optional<UserInformation> testUserInformation = userInformationRepository.findById(4);
//        Location testLocation = new Location();
//        testLocation.setAddress("400 S Duff Ave");
//        testLocation.setCity("Ames");
//        testLocation.setState("Iowa");
//        testLocation.setZipCode(50010);
//        testLocation = locationRepository.saveAndFlush(testLocation);
//        System.out.println("Created Location");
//
//        Restaurant newRestaurant = new Restaurant();
//        newRestaurant.setName("Test Restaurant");
//        newRestaurant.setOwner(testUser);
//        newRestaurant.setLocation(testLocation);
////        newRestaurant = restaurantRepository.saveAndFlush(newRestaurant);
//        Restaurant returnedRestaurant = restaurantService.createNewRestaurant(newRestaurant);
//        System.out.println("Created Restaurant");
//
//        System.out.println("Checking restaurant name is Test Restaurant");
//        assertEquals("Test Restaurant", returnedRestaurant.getName());
//    }
//
//    @Test
//    public void returnsUpdatedRestaurant(){
//        Optional<Restaurant> dbRestaurant = restaurantRepository.findById(1);
//        Restaurant testRestaurant = dbRestaurant.get();
//
//        testRestaurant.setName("Oliver & Company");
//        Restaurant returnedRestaurant = restaurantService.updateGivenRestaurant(testRestaurant, 1);
//        System.out.println("Checking restaurant name is Oliver & Company");
//        assertEquals("Oliver & Company", testRestaurant.getName());
//    }
//
//
//    @Test
//    @Disabled
//    public void returnsUpdatedRestaurantTags(){
//        Optional<Restaurant> dbRestaurant = restaurantRepository.findById(1);
//        List<RestaurantTag> dbRestaurantTags = dbRestaurant.get().getRestaurantTags();
//        System.out.println("tags: " + dbRestaurantTags);
//        Restaurant testRestaurant = dbRestaurant.get();
//
//        testRestaurant.setName("Oliver & Company");
//        Restaurant returnedRestaurant = restaurantService.updateGivenRestaurant(testRestaurant, 1);
//        System.out.println("Checking restaurant name is Oliver & Company");
//        assertEquals("Oliver & Company", testRestaurant.getName());
//    }
//
//
//    @Test
//    public void confirmsDeletedRestaurant(){
//        Optional<User> dbUser = userRepository.findTopByUserName("unconfirmedTestCustomer");
//        User testUser = dbUser.get();
//        Optional<UserInformation> testUserInformation = userInformationRepository.findById(4);
//        Location testLocation = new Location();
//        testLocation.setAddress("400 S Duff Ave");
//        testLocation.setCity("Ames");
//        testLocation.setState("Iowa");
//        testLocation.setZipCode(50010);
//        testLocation = locationRepository.saveAndFlush(testLocation);
//        System.out.println("Created Location");
//
//        Restaurant newRestaurant = new Restaurant();
//        newRestaurant.setName("Test Restaurant");
//        newRestaurant.setOwner(testUser);
//        newRestaurant.setLocation(testLocation);
//        newRestaurant = restaurantRepository.saveAndFlush(newRestaurant);
//        System.out.println("newRestaurantId: " + newRestaurant.getId());
//        String deleteMessage = restaurantService.deleteGivenRestaurant(17);
//        assertEquals("Restaurant has been deleted successfully", deleteMessage);
//    }