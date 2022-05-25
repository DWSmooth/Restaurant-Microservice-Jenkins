package com.smoothstack.restaurantmicroservice.controller;

import java.util.List;

import com.smoothstack.common.models.Restaurant;
import com.smoothstack.restaurantmicroservice.data.RestaurantInformation;
import com.smoothstack.restaurantmicroservice.service.RestaurantService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class RestaurantController {

    @Autowired
    RestaurantService restaurantService;

    @GetMapping(value = "/restaurants/{restaurantId}")
    public ResponseEntity<RestaurantInformation>getRestaurantDetails(@PathVariable Integer restaurantId){
        return ResponseEntity.status(HttpStatus.OK).body(restaurantService.getRestaurantDetails(restaurantId));
    }

    @PostMapping("/restaurants")
    public ResponseEntity<Restaurant> createRestaurant(@RequestBody Restaurant restaurant){
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurantService.createNewRestaurant(restaurant));
    }

    

}
