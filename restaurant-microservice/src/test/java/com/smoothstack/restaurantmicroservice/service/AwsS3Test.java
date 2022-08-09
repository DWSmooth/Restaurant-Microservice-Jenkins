package com.smoothstack.restaurantmicroservice.service;


import com.smoothstack.common.exceptions.MenuItemNotFoundException;
import com.smoothstack.common.exceptions.ObjectMappingException;
import com.smoothstack.common.exceptions.RestaurantAssetNotFoundException;
import com.smoothstack.common.models.Restaurant;
import com.smoothstack.common.repositories.MenuItemRepository;
import com.smoothstack.common.repositories.RestaurantRepository;
import com.smoothstack.common.services.CommonLibraryTestingService;
import com.smoothstack.restaurantmicroservice.data.*;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;

import java.io.File;
import java.nio.file.Files;
import java.util.Arrays;
import java.util.Base64;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.fail;

@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
public class AwsS3Test {

    @Autowired
    MenuItemService menuItemService;
    @Autowired
    MenuItemRepository menuItemRepository;
    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    RestaurantService restaurantService;

    @Autowired
    AwsS3Service awsS3Service;

    @Autowired
    CommonLibraryTestingService testingService;


    @BeforeEach
    public void setUpTestEnvironment(){
        testingService.createTestData();
    }

    @Test
    public void restaurantFolderSetupAndTearDownTest() {
        Optional<Restaurant> restaurant1Optional = restaurantRepository.findById(1);

        if(restaurant1Optional.isPresent()) {
            Restaurant restaurant1 = restaurant1Optional.get();
            RestaurantInformation restaurantInformation1 = restaurantService.getRestaurantInformation(restaurant1.getId());
            if(awsS3Service.doesBucketExist()) {

                awsS3Service.setUpRestaurantFolder(restaurantInformation1);
                Assertions.assertTrue(awsS3Service.isRestaurantSetUp(restaurantInformation1));

                awsS3Service.deleteRestaurant(restaurantInformation1);
                Assertions.assertFalse(awsS3Service.isRestaurantSetUp(restaurantInformation1));

            } else {
                fail("Bucket does not exist");
            }
        } else {
            fail("testingService.createTestData() Failed");
        }
    }

    @Test
    public void postGetDeleteMenuItemTest() {

        try {
            // Restaurant Information
            RestaurantInformation restaurantInformation = restaurantService.getRestaurantInformation(1);

            // Wisconsin Mac & Cheese from Noodles & Company
            MenuItemInformation menuItemInformation = menuItemService.getMenuItemInformation(1);

            // encode test file to base64
            byte[] fileContent = Files.readAllBytes(new File("src/test/test_images/mac-1050x602.jpg").toPath());
            String encodedString = Base64.getEncoder().encodeToString(fileContent);

            // Create MenuItemImage
            Image menuItemImage = new Image("Wisconsin Mac & Cheese", "jpg", encodedString);

            // SetUp data to "send"
            RestaurantMenuItemImages restaurantMenuItemImages = new RestaurantMenuItemImages(restaurantInformation, menuItemInformation, Arrays.asList(menuItemImage));

            // Post Data to Bucket/restaurant-image-storage
            awsS3Service.addMenuItemsImage(restaurantMenuItemImages);

            // Set Up MenuItemRequest
            MenuItemRequest menuItemRequest = new MenuItemRequest(restaurantInformation, menuItemInformation);
            RestaurantMenuItemImages returnedRestaurantMenuItemImages = awsS3Service.getMenuItem(menuItemRequest);

            // Check that the return item is equal to the sent
            assertEquals(returnedRestaurantMenuItemImages, restaurantMenuItemImages);

            // Delete data from Bucket/restaurant-image-storage
            awsS3Service.deleteMenuItem(menuItemRequest);

            Assertions.assertThrows(MenuItemNotFoundException.class, () -> {awsS3Service.getMenuItem(menuItemRequest);});

            // Tear Down
            awsS3Service.deleteRestaurant(restaurantInformation);

        } catch (ObjectMappingException objectMappingException) {
            objectMappingException.printStackTrace();
        } catch (MenuItemNotFoundException menuItemNotFoundException) {
            menuItemNotFoundException.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    @Test
    public void postGetDeleteRestaurantAssetTest() {
        try {
            // Restaurant Information
            RestaurantInformation restaurantInformation = restaurantService.getRestaurantInformation(1);

            // encode test file to base64
            byte[] fileContent = Files.readAllBytes(new File("src/test/test_images/6456_yzKhbE3QUea1XLLie84OE6A-OzaQ_QId5a2SZzGClq4.jpg").toPath());
            String encodedString = Base64.getEncoder().encodeToString(fileContent);

            // Create Image
            Image image = new Image("StoreFrontColumbia", "jpg", encodedString);

            // Create RestaurantAsset
            RestaurantAsset restaurantAsset = new RestaurantAsset("Store Front", Arrays.asList(image));

            // SetUp data to "send"
            RestaurantAssetBody restaurantAssetBody = new RestaurantAssetBody(restaurantInformation, restaurantAsset);

            // Post Data to Bucket/restaurant-image-storage
            awsS3Service.addRestaurantAsset(restaurantAssetBody);

            // Get previously posted data
            RestaurantAsset returnedRestaurantAsset = awsS3Service.getRestaurantAsset(restaurantAssetBody);

            // Check that the return item is equal to the sent
            assertEquals(returnedRestaurantAsset, restaurantAsset);

            // Delete data from Bucket/restaurant-image-storage
            awsS3Service.deleteRestaurantAsset(restaurantAssetBody);

            Assertions.assertThrows(RestaurantAssetNotFoundException.class, () -> {awsS3Service.getRestaurantAsset(restaurantAssetBody);});

            // Tear Down
            awsS3Service.deleteRestaurant(restaurantInformation);

        } catch (ObjectMappingException objectMappingException) {
            objectMappingException.printStackTrace();
        } catch (MenuItemNotFoundException menuItemNotFoundException) {
            menuItemNotFoundException.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }



}
