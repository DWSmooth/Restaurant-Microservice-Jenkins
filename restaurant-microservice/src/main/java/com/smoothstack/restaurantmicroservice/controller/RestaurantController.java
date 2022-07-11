package com.smoothstack.restaurantmicroservice.controller;

import java.util.List;

import com.smoothstack.common.models.MenuItem;
import com.smoothstack.common.models.Restaurant;
import com.smoothstack.restaurantmicroservice.data.RestaurantInformation;
import com.smoothstack.restaurantmicroservice.data.RestaurantsParams;
import com.smoothstack.restaurantmicroservice.exception.*;
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

    @PutMapping(value = "/restaurant/enable/{restaurantId}/restaurantTag/{restaurantTagId}")
    public ResponseEntity<String>enableRestaurantTag(@PathVariable Integer restaurantId, @PathVariable Integer restaurantTagId) throws RestaurantNotFoundException, RestaurantTagNotFoundException, RestaurantTagAlreadyExistsException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(restaurantService.enableGivenRestaurantTags(restaurantId, restaurantTagId));
        } catch(RestaurantNotFoundException restaurantNotFoundException){
            return new ResponseEntity(restaurantNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(RestaurantTagNotFoundException restaurantTagNotFoundException){
            return new ResponseEntity(restaurantTagNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }  catch(RestaurantTagAlreadyExistsException restaurantTagAlreadyExistsException){
            return new ResponseEntity(restaurantTagAlreadyExistsException.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/restaurant/disable/{restaurantId}/restaurantTag/{restaurantTagId}")
    public ResponseEntity<String>disableRestaurantTag(@PathVariable Integer restaurantId, @PathVariable Integer restaurantTagId) throws RestaurantNotFoundException, RestaurantTagNotFoundException, RestaurantTagAlreadyExistsException {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(restaurantService.disableGivenRestaurantTags(restaurantId, restaurantTagId));
        } catch(RestaurantNotFoundException restaurantNotFoundException){
            return new ResponseEntity(restaurantNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(RestaurantTagNotFoundException restaurantTagNotFoundException){
            return new ResponseEntity(restaurantTagNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        }  catch(RestaurantTagAlreadyExistsException restaurantTagAlreadyExistsException){
            return new ResponseEntity(restaurantTagAlreadyExistsException.getMessage(), HttpStatus.CONFLICT);
        }
    }

    @PutMapping("/restaurant/enable/{restaurantId}")
    public ResponseEntity<String>enableRestaurant(@PathVariable Integer restaurantId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(restaurantService.enableRestaurant(restaurantId));
        } catch(RestaurantNotFoundException restaurantNotFoundException){
            return new ResponseEntity(restaurantNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(RestaurantAlreadyEnabledException restaurantAlreadyEnabledException){
            return new ResponseEntity(restaurantAlreadyEnabledException.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PutMapping("/restaurant/disable/{restaurantId}/")
    public ResponseEntity<String>disableRestaurant(@PathVariable Integer restaurantId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(restaurantService.disableRestaurant(restaurantId));
        } catch(RestaurantNotFoundException restaurantNotFoundException){
            return new ResponseEntity(restaurantNotFoundException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch(RestaurantAlreadyDisabledException restaurantAlreadyDisabledException){
            return new ResponseEntity(restaurantAlreadyDisabledException.getMessage(), HttpStatus.BAD_REQUEST);
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

    @GetMapping(value = "/restaurant/search")
    public ResponseEntity<List<RestaurantInformation>> searchRestaurantsMenuItems(@RequestBody RestaurantsParams restaurantsSearch) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(restaurantService.findRestaurants(restaurantsSearch));
        } catch (InvalidSearchException invalidSearchException) {
            return new ResponseEntity(invalidSearchException.getMessage(), HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

}
