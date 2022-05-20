package com.smoothstack.restaurantmicroservice.controller;

import com.smoothstack.common.models.Restaurant;
import com.smoothstack.common.models.RestaurantTag;
import com.smoothstack.restaurantmicroservice.service.RestaurantTagService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
public class RestaurantTagController {

    @Autowired
    RestaurantTagService restaurantTagService;

    @GetMapping(value = "/restaurants/restaurantTags")
    public ResponseEntity<List<RestaurantTag>> getRestaurantTags(){
        return ResponseEntity.status(HttpStatus.OK).body(restaurantTagService.getAllRestaurantTags());
    }


    @PostMapping("/restaurants/restaurantTags")
    public ResponseEntity<RestaurantTag> createMenuItem(@RequestBody RestaurantTag restaurantTag){
        return ResponseEntity.status(HttpStatus.CREATED).body(restaurantTagService.createNewRestaurantTag(restaurantTag));
    }



    @PutMapping(value = "restaurants/restaurantTags/{restaurantTagId}")
    public ResponseEntity<RestaurantTag>updateRestaurantTag(@RequestBody RestaurantTag restaurantTag, @PathVariable Integer restaurantTagId){
        return ResponseEntity.status(HttpStatus.OK).body(restaurantTagService.updateGivenRestaurantTag(restaurantTag, restaurantTagId));
    }


    @DeleteMapping(value = "restaurants/restaurantTags/{restaurantTagId}")
    public ResponseEntity<String>deleteRestaurant(@PathVariable Integer restaurantTagId){
        return ResponseEntity.status(HttpStatus.OK).body(restaurantTagService.deleteGivenRestaurantTag(restaurantTagId));
    }

}
