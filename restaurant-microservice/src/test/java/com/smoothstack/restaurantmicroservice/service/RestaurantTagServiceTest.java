package com.smoothstack.restaurantmicroservice.service;

import com.smoothstack.common.models.*;
import com.smoothstack.common.repositories.*;
import com.smoothstack.common.services.CommonLibraryTestingService;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.smoothstack.restaurantmicroservice.exception.RestaurantTagAlreadyExistsException;
import com.smoothstack.restaurantmicroservice.exception.RestaurantTagNotFoundException;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;

import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.annotation.DirtiesContext;


@SpringBootTest
@DirtiesContext(classMode = DirtiesContext.ClassMode.BEFORE_EACH_TEST_METHOD)
@AutoConfigureTestDatabase
public class RestaurantTagServiceTest {

    @Autowired
    RestaurantTagService restaurantTagService;
    @Autowired
    RestaurantTagRepository restaurantTagRepository;
    @Autowired
    CommonLibraryTestingService testingService;


    @BeforeEach
    //    @Disabled
    public void setUpTestEnvironment(){
        testingService.createTestData();
    }

    @Test
    //    @Disabled
    public void returnsAllRestaurantTags() throws Exception {
        List<RestaurantTag> dbRestaurantTags = restaurantTagRepository.findAll();
        List<String> dbRestaurantTagNames = dbRestaurantTags
         .stream()
                .map( tag -> tag.getName())
                .collect(Collectors.toList());

        List<RestaurantTag> serviceRestaurantTags  = restaurantTagService.getAllRestaurantTags();
        List<String> serviceRestaurantTagsNames = serviceRestaurantTags
                .stream()
                .map( tag -> tag.getName())
                .collect(Collectors.toList());

        assertEquals(dbRestaurantTagNames, serviceRestaurantTagsNames);
    }


    @Test
    //    @Disabled
    public void returnsSavedRestaurantTag() {
        RestaurantTag newRestaurantTag = new RestaurantTag();
        newRestaurantTag.setName("testTag");

        RestaurantTag savedRestaurantTag = restaurantTagService.createNewRestaurantTag(newRestaurantTag);

        assertEquals(newRestaurantTag, savedRestaurantTag);
    }


    @Test
    //    @Disabled
    public void returnsSavedRestaurantTagAlreadyExists(){
        boolean exceptionThrown = false;
        RestaurantTag newRestaurantTag = new RestaurantTag();

        newRestaurantTag.setName("testTag");
        restaurantTagService.createNewRestaurantTag(newRestaurantTag);

        try {
            restaurantTagService.createNewRestaurantTag(newRestaurantTag);
        } catch(RestaurantTagAlreadyExistsException restaurantTagAlreadyExistsException) {
            exceptionThrown = true;
        }

        assertTrue(exceptionThrown);
    }

    @Test
    //    @Disabled
    public void returnsUpdatedRestaurantTag(){
        Optional<RestaurantTag> dbRestaurantTag = restaurantTagRepository.findById(1);
        RestaurantTag testRestaurantTag = dbRestaurantTag.get();
        testRestaurantTag.setName("updatedTag");

        String returnedRestaurantTag = restaurantTagService.updateGivenRestaurantTag(testRestaurantTag, 1);

        assertEquals("Restaurant Tag has been updated successfully", returnedRestaurantTag);
    }

    @Test
    //    @Disabled
    public void returnsUpdatedRestaurantTagNotFound(){
        boolean notFoundExceptionThrown = false;
        boolean duplicateExceptionThrown = false;
        Optional<RestaurantTag> dbRestaurantTag = restaurantTagRepository.findById(1);
        RestaurantTag testRestaurantTag = dbRestaurantTag.get();

        try {
            restaurantTagService.updateGivenRestaurantTag(testRestaurantTag, 0);
        } catch(RestaurantTagNotFoundException restaurantTagNotFoundException) {
            notFoundExceptionThrown = true;
        } catch (RestaurantTagAlreadyExistsException restaurantTagAlreadyExistsException){
            duplicateExceptionThrown = false;
        }

        assertTrue(notFoundExceptionThrown);
        assertFalse(duplicateExceptionThrown);
    }

    @Test
    //    @Disabled
    public void returnsUpdatedRestaurantTagAlreadyExists(){
        boolean notFoundExceptionThrown = false;
        boolean duplicateExceptionThrown = false;
        Optional<RestaurantTag> dbRestaurantTag = restaurantTagRepository.findById(1);
        RestaurantTag testRestaurantTag = dbRestaurantTag.get();

        testRestaurantTag.setName("updatedTag");
        restaurantTagService.updateGivenRestaurantTag(testRestaurantTag, 1);

        try {
            restaurantTagService.updateGivenRestaurantTag(testRestaurantTag, 1);
        } catch(RestaurantTagNotFoundException restaurantTagNotFoundException) {
            notFoundExceptionThrown = false;
        } catch (RestaurantTagAlreadyExistsException restaurantTagAlreadyExistsException){
            duplicateExceptionThrown = true;
        }

        assertFalse(notFoundExceptionThrown);
        assertTrue(duplicateExceptionThrown);
    }



    @Test
    //    @Disabled
    public void confirmsDeletedRestaurantTag(){
        RestaurantTag newRestaurantTag = new RestaurantTag();
        newRestaurantTag.setName("toDelete");
        RestaurantTag savedRestaurantTag = restaurantTagRepository.saveAndFlush(newRestaurantTag);

        String deleteMessage = restaurantTagService.deleteGivenRestaurantTag(savedRestaurantTag.getId());

        assertEquals("Restaurant Tag has been deleted successfully", deleteMessage );
    }


    @Test
    //    @Disabled
    public void confirmsDeletedRestaurantTagNotFound(){
        boolean notFoundExceptionThrown = false;
        RestaurantTag newRestaurantTag = new RestaurantTag();
        newRestaurantTag.setId(0);
        newRestaurantTag.setName("toDelete");


        try {
            restaurantTagService.deleteGivenRestaurantTag(newRestaurantTag.getId());
        } catch(RestaurantTagNotFoundException restaurantTagNotFoundException) {
            notFoundExceptionThrown = true;
        }

        assertTrue(notFoundExceptionThrown);
    }

    @AfterEach
    @Disabled
    void tearDown() {
    }
    
}
