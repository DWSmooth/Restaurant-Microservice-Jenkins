package com.smoothstack.restaurantmicroservice.service;

import com.smoothstack.common.models.*;
import com.smoothstack.common.repositories.*;
import com.smoothstack.common.services.CommonLibraryTestingService;

import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;

import com.smoothstack.restaurantmicroservice.exception.MenuItemNotFoundException;
import com.smoothstack.restaurantmicroservice.exception.RestaurantNotFoundException;

import com.smoothstack.restaurantmicroservice.data.MenuItemInformation;
import org.junit.jupiter.api.*;
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
//    @Disabled
    public void setUpTestEnvironment(){
        testingService.createTestData();
    }


    @Test
//    @Disabled
    public void returnsAllMenuItems() throws Exception {
        List<MenuItemInformation> testMenuItems = new ArrayList<MenuItemInformation>();
        List<MenuItem> dbMenuItems = menuItemRepository.findAll();

        for(MenuItem mI : dbMenuItems){
            System.out.println("menuItemId: " + mI.getId());
            testMenuItems.add(menuItemService.getMenuItemInformation(mI.getId()));
        }
        List<MenuItemInformation> serviceMenuItems = menuItemService.getAllMenuItems();

        assertEquals(testMenuItems, serviceMenuItems);
    }

    @Test
//    @Disabled
    public void returnsCorrectMenuItemDetails(){
        List<MenuItemInformation> testMenuItems = new ArrayList<MenuItemInformation>();
        Optional<Restaurant> dbRestaurantOptional = restaurantRepository.findById(1);
        Restaurant dbRestaurant = dbRestaurantOptional.get();
        List<MenuItem> dbMenuItems = menuItemRepository.findAllByRestaurants(dbRestaurant);

        for(MenuItem mI : dbMenuItems){
            testMenuItems.add(menuItemService.getMenuItemInformation(mI.getId()));
        }
        List<MenuItemInformation> serviceMenuItems = menuItemService.getRestaurantMenu(1);

        assertEquals(testMenuItems, serviceMenuItems);
    }

    @Test
//    @Disabled
    public void returnsCorrectMenuItemDetailsRestaurantNotFound(){
        boolean notFoundExceptionThrown = false;

        try {
            menuItemService.getRestaurantMenu(0);
        } catch(RestaurantNotFoundException restaurantNotFoundException){
            notFoundExceptionThrown = true;
        }

        assertTrue(notFoundExceptionThrown);
    }


    @Test
//    @Disabled
    public void returnsSavedMenuItem(){
        Optional<Restaurant> dbRestaurant = restaurantRepository.findById(1);
        Restaurant testRestaurant = dbRestaurant.get();

        MenuItem newMenuItem = new MenuItem();
        newMenuItem.setRestaurants(testRestaurant);
        newMenuItem.setName("testItem");
        newMenuItem.setDescription("testDescription");
        newMenuItem.setPrice(2.50);
        String confirmMessage = menuItemService.createNewMenuItem(newMenuItem);

        assertEquals("Menu Item 'testItem' created successfully. Id:16", confirmMessage);
    }


    @Test
//    @Disabled
    public void returnsSavedMenuItemRestaurantNotFound(){
        boolean notFoundExceptionThrown = false;
        Optional<Restaurant> dbRestaurant = restaurantRepository.findById(1);
        Restaurant testRestaurant = dbRestaurant.get();
        testRestaurant.setId(0);

        MenuItem newMenuItem = new MenuItem();
        newMenuItem.setRestaurants(testRestaurant);
        newMenuItem.setName("testItem");
        newMenuItem.setDescription("testDescription");
        newMenuItem.setPrice(2.50);

        try {
            menuItemService.createNewMenuItem(newMenuItem);
        } catch(RestaurantNotFoundException restaurantNotFoundException){
            notFoundExceptionThrown = true;
        }

        assertTrue(notFoundExceptionThrown);
    }

    @Test
//    @Disabled
    public void returnsUpdatedMenuItem(){
        Optional<MenuItem> dbMenuItem = menuItemRepository.findById(1);
        MenuItem testMenuItem = dbMenuItem.get();
        String returnedMenuItem = menuItemService.updateGivenMenuItem(testMenuItem, 1);

        testMenuItem.setName("test menu item iii");

        assertEquals("Menu Item has been updated successfully", returnedMenuItem);
    }

    @Test
//    @Disabled
    public void returnsUpdatedMenuItemNotFound(){
        boolean notFoundExceptionThrown = false;
        Optional<MenuItem> dbMenuItem = menuItemRepository.findById(1);
        MenuItem testMenuItem = dbMenuItem.get();

        testMenuItem.setName("test menu item iii");

        try {
           menuItemService.updateGivenMenuItem(testMenuItem, 0);
        } catch(MenuItemNotFoundException menuItemNotFoundException){
            notFoundExceptionThrown = true;
        }

        assertTrue(notFoundExceptionThrown);
    }

    @Test
//    @Disabled
    public void confirmsDeletedMenuItem(){
        Optional<MenuItem> dbMenuItem = menuItemRepository.findById(1);
        MenuItem menuItem = dbMenuItem.get();
        String deleteMessage = menuItemService.deleteGivenMenuItem(menuItem.getId());

        assertEquals(deleteMessage, "Menu item has been deleted successfully");
    }

    @Test
//    @Disabled
    public void confirmsDeletedMenuItemNotFound(){
        boolean notFoundExceptionThrown = false;

        try {
            menuItemService.deleteGivenMenuItem(0);
        } catch(MenuItemNotFoundException menuItemNotFoundException){
            notFoundExceptionThrown = true;
        }

        assertTrue(notFoundExceptionThrown);
    }

    @AfterEach
    @Disabled
    void tearDown() {
    }
}