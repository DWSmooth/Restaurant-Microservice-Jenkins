package com.smoothstack.restaurantmicroservice.controller;

import java.util.List;

import com.smoothstack.common.models.Restaurant;
import com.smoothstack.restaurantmicroservice.data.RestaurantInformation;
import com.smoothstack.restaurantmicroservice.exception.LocationNotFoundException;
import com.smoothstack.restaurantmicroservice.exception.RestaurantNotFoundException;
import com.smoothstack.restaurantmicroservice.exception.RestaurantTagNotFoundException;
import com.smoothstack.restaurantmicroservice.exception.RestaurantTagAlreadyExistsException;
import com.smoothstack.restaurantmicroservice.exception.UserNotFoundException;
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
    public ResponseEntity<List<RestaurantInformation>>getRestaurants() throws Exception {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(restaurantService.getRestaurants());
        } catch(Exception exception){
            return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping(value = "/restaurant/{restaurantId}")
    public ResponseEntity<RestaurantInformation>getRestaurantDetails(@PathVariable Integer restaurantId) throws RestaurantNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(restaurantService.getRestaurantDetails(restaurantId));
        } catch(RestaurantNotFoundException restaurantNotFoundException){
            return new ResponseEntity(restaurantNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @PostMapping("/restaurant")
    public ResponseEntity<String>createRestaurant(@RequestBody RestaurantInformation restaurant) throws UserNotFoundException {
        try{
            return ResponseEntity.status(HttpStatus.CREATED).body(restaurantService.createNewRestaurant(restaurant));
        } catch(LocationNotFoundException locationNotFoundException){
            return new ResponseEntity(locationNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(UserNotFoundException userNotFoundException){
            return new ResponseEntity(userNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping("/restaurant/{restaurantId}")
    public ResponseEntity<String>updateRestaurant(@PathVariable Integer restaurantId, @RequestBody RestaurantInformation restaurant) throws RestaurantNotFoundException, LocationNotFoundException, UserNotFoundException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(restaurantService.updateGivenRestaurant(restaurant, restaurantId));
        } catch(RestaurantNotFoundException restaurantNotFoundException){
            return new ResponseEntity(restaurantNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(LocationNotFoundException locationNotFoundException){
            return new ResponseEntity(locationNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(UserNotFoundException userNotFoundException){
            return new ResponseEntity(userNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @PutMapping("/restaurant/{restaurantId}/restaurantTag/{restaurantTagId}")
    public ResponseEntity<String>updateRestaurantTag(@PathVariable Integer restaurantId, @PathVariable Integer restaurantTagId) throws RestaurantNotFoundException, RestaurantTagNotFoundException, RestaurantTagAlreadyExistsException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(restaurantService.updateGivenRestaurantTags(restaurantId, restaurantTagId));
        } catch(RestaurantNotFoundException restaurantNotFoundException){
            return new ResponseEntity(restaurantNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(RestaurantTagNotFoundException restaurantTagNotFoundException){
            return new ResponseEntity(restaurantTagNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }  catch(RestaurantTagAlreadyExistsException restaurantTagAlreadyExistsException){
            return new ResponseEntity(restaurantTagAlreadyExistsException.getMessage(), HttpStatus.CONFLICT);
        }
    }


    @DeleteMapping(value = "/restaurant/{restaurantId}")
    public ResponseEntity<String>deleteRestaurant(@PathVariable Integer restaurantId) throws RestaurantNotFoundException{
        try {
            return ResponseEntity.status(HttpStatus.OK).body(restaurantService.deleteGivenRestaurant(restaurantId));
        }  catch(RestaurantNotFoundException restaurantNotFoundException){
            return new ResponseEntity(restaurantNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
