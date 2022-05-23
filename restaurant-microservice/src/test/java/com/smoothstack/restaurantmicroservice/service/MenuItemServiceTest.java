package com.smoothstack.restaurantmicroservice.service;

import com.smoothstack.common.models.*;
import com.smoothstack.common.repositories.*;
import com.smoothstack.common.services.CommonLibraryTestingService;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Order;


import com.smoothstack.restaurantmicroservice.data.MenuItemInformation;
import org.junit.jupiter.api.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@SpringBootTest
@TestMethodOrder(OrderAnnotation.class)
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


    @Test
    @Order(1)
    public void returnsAllMenuItems(){
        List<MenuItemInformation> testMenuItems = new ArrayList<MenuItemInformation>();
        List<MenuItem> dbMenuItems = menuItemRepository.findAll();
        for(MenuItem mI : dbMenuItems){
//            System.out.println("menuItemId: " + mI.getId());
            testMenuItems.add(menuItemService.getFrontendData(mI.getId()));
        }
        List<MenuItemInformation> serviceMenuItems = menuItemService.getAllMenuItems();
        assertEquals(testMenuItems, serviceMenuItems);
    }

    @Test
    @Order(2)
    public void returnsCorrectMenuItemDetails(){
        List<MenuItemInformation> testMenuItems = new ArrayList<MenuItemInformation>();
        // - Find restaurantById
        // - Unwrap Optional in order to pass into find menuItemsByRestaurant method
        // - save returned menu item list to menuItemInformation
        Optional<Restaurant> dbRestaurantOptional = restaurantRepository.findById(1);
        Restaurant dbRestaurant = dbRestaurantOptional.get();
        List<MenuItem> dbMenuItems = menuItemRepository.findAllByRestaurants(dbRestaurant);
        for(MenuItem mI : dbMenuItems){
            testMenuItems.add(menuItemService.getFrontendData(mI.getId()));
        }
        List<MenuItemInformation> serviceMenuItems = menuItemService.getMenuItemDetails(1);
        assertEquals(testMenuItems, serviceMenuItems);
    }

    @Test
    @Order(3)
    public void returnsSavedMenuItem(){
        MenuItem newMenuItem = new MenuItem();
        newMenuItem.setName("testItem");
        newMenuItem.setDescription("testDescription");
        newMenuItem.setPrice(2.50);
        MenuItem savedMenuItem = menuItemService.createNewMenuItem(newMenuItem);
        assertEquals(savedMenuItem, newMenuItem);
    }

    @Test
    @Order(4)
    public void returnsUpdatedMenuItem(){
        Optional<MenuItem> dbMenuItem = menuItemRepository.findById(1);
        MenuItem testMenuItem = dbMenuItem.get();
        testMenuItem.setName("test menu item iii");
        MenuItem returnedMenuItem = menuItemService.updateGivenMenuItem(testMenuItem, 1);
        System.out.println("Checking updated menu item name is test menu item iii");
        assertEquals("test menu item iii", returnedMenuItem.getName());
    }

    @Test
    @Order(5)
    public void confirmsDeletedMenuItem(){
        Optional<MenuItem> dbMenuItem = menuItemRepository.findById(15);
        MenuItem menuItem = dbMenuItem.get();
        String deleteMessage = menuItemService.deleteGivenMenuItem(menuItem.getId());;
        assertEquals(deleteMessage, "Menu item has been deleted successfully");
    }

    @AfterEach
    @Disabled
    void tearDown() {
    }
}