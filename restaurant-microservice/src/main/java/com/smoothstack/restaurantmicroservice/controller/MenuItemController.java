package com.smoothstack.restaurantmicroservice.controller;

import java.util.List;

import com.smoothstack.common.models.MenuItem;
import com.smoothstack.common.models.Restaurant;
import com.smoothstack.restaurantmicroservice.data.MenuItemInformation;
import com.smoothstack.restaurantmicroservice.data.RestaurantInformation;
import com.smoothstack.restaurantmicroservice.service.MenuItemService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
public class MenuItemController {

    @Autowired
    MenuItemService menuItemService;


    @GetMapping(value = "/restaurants/menuItems")
    public ResponseEntity<List<MenuItemInformation>> getAllMenuItems(){
        return ResponseEntity.status(HttpStatus.OK).body(menuItemService.getAllMenuItems());
    }


    @GetMapping(value = "/restaurants/{restaurantId}/menuItems")
    public ResponseEntity<List<MenuItemInformation>> getRestaurantMenu(@PathVariable Integer restaurantId){
        return ResponseEntity.status(HttpStatus.OK).body(menuItemService.getMenuItemDetails(restaurantId));
    }

    @PostMapping("/restaurants/menuItems")
    public ResponseEntity<MenuItem> createMenuItem(@RequestBody MenuItem menuItem){
        return ResponseEntity.status(HttpStatus.CREATED).body(menuItemService.createNewMenuItem(menuItem));
    }

    @PutMapping(value = "restaurants/menuItems/{menuItemId}")
    public ResponseEntity<MenuItem>updateRestaurant(@RequestBody MenuItem menuItem, @PathVariable Integer menuItemId){
        return ResponseEntity.status(HttpStatus.OK).body(menuItemService.updateGivenMenuItem(menuItem, menuItemId));
    }

    @DeleteMapping(value = "restaurants/menuItems/{menuItemId}")
    public ResponseEntity<String>deleteRestaurant(@PathVariable Integer menuItemId){
        return ResponseEntity.status(HttpStatus.OK).body(menuItemService.deleteGivenMenuItem(menuItemId));
    }


}
