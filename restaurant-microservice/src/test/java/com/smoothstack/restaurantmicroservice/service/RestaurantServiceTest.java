package com.smoothstack.restaurantmicroservice.service;

import com.smoothstack.common.models.Restaurant;
import com.smoothstack.common.repositories.RestaurantRepository;
import com.smoothstack.common.models.MenuItem;
import com.smoothstack.common.repositories.MenuItemRepository;
import com.smoothstack.common.models.RestaurantTag;
import com.smoothstack.common.repositories.RestaurantTagRepository;
import com.smoothstack.restaurantmicroservice.data.RestaurantInformation;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

@SpringBootTest
public class RestaurantServiceTest {

    @Autowired
    RestaurantService restaurantService;

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    MenuItemRepository menuItemRepository;

    @Autowired
    RestaurantTagRepository restaurantTagRepository;

    @BeforeEach
    @Disabled
    public static void setUpTestEnvironment(){

    }

    @Test
    @Disabled
    public void createRestaurantWithDetails(){
        // create restaurant

        // create menuItems

        // create restaurantTags
    }

}
