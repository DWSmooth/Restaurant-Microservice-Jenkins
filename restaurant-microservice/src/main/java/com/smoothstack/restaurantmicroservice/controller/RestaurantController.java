package com.smoothstack.restaurantmicroservice.controller;

import java.util.List;
import java.util.Optional;

import com.smoothstack.common.models.Restaurant;
import com.smoothstack.restaurantmicroservice.data.MenuItemInformation;
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


    @GetMapping(value = "/restaurants")
    public ResponseEntity<List<RestaurantInformation>>getRestaurants(){
        return ResponseEntity.status(HttpStatus.OK).body(restaurantService.getRestaurants());
    }

    @GetMapping(value = "/restaurants/{restaurantId}")
    public ResponseEntity<RestaurantInformation>getRestaurantDetails(@PathVariable Integer restaurantId){
        return ResponseEntity.status(HttpStatus.OK).body(restaurantService.getRestaurantDetails(restaurantId));
    }

    @PostMapping("/restaurants")
    public ResponseEntity<Restaurant>createRestaurant(@RequestBody Restaurant restaurant){
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurantService.createNewRestaurant(restaurant));
    }

    @PutMapping("/restaurants/{restaurantId}")
    public ResponseEntity<Restaurant>updateRestaurant(@PathVariable Integer restaurantId, @RequestBody Restaurant restaurant){
        return ResponseEntity.status(HttpStatus.OK).body(restaurantService.updateGivenRestaurant(restaurant, restaurantId));
    }


    @PutMapping("/restaurants/{restaurantId}/{restaurantTagId}")
    public ResponseEntity<Restaurant>updateRestaurantTag(@PathVariable Integer restaurantId, @PathVariable Integer restaurantTagId) {
        return ResponseEntity.status(HttpStatus.OK).body(restaurantService.updateGivenRestaurantTags(restaurantId, restaurantTagId));
    }



        @DeleteMapping(value = "/restaurants/{restaurantId}")
    public ResponseEntity<String>deleteRestaurant(@PathVariable Integer restaurantId){
        return ResponseEntity.status(HttpStatus.OK).body(restaurantService.deleteGivenRestaurant(restaurantId));
    }

}
