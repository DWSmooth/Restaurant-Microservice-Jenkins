package com.smoothstack.restaurantmicroservice.controller;

import java.util.List;

import com.smoothstack.common.models.MenuItem;
import com.smoothstack.restaurantmicroservice.data.MenuItemInformation;
import com.smoothstack.restaurantmicroservice.service.MenuItemService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MenuItemController {

    @Autowired
    MenuItemService menuItemService;

    @GetMapping(value = "/restaurants/{restaurantId}/menuItems")
    public ResponseEntity<List<MenuItemInformation>> getRestaurantMenu(@PathVariable Integer restaurantId){
        return ResponseEntity.status(HttpStatus.OK).body(menuItemService.getMenuItemDetails(restaurantId));
    }


}
