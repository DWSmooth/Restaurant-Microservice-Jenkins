package com.smoothstack.restaurantmicroservice.service;

import com.smoothstack.common.models.MenuItem;
import com.smoothstack.common.models.Restaurant;
import com.smoothstack.common.models.RestaurantTag;
import com.smoothstack.common.repositories.MenuItemRepository;
import com.smoothstack.common.repositories.RestaurantRepository;

import com.smoothstack.restaurantmicroservice.data.MenuItemInformation;
import com.smoothstack.restaurantmicroservice.exception.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class MenuItemService {

    @Autowired
    MenuItemRepository menuItemRepository;
    @Autowired
    RestaurantRepository restaurantRepository;

    @Transactional
    public List<MenuItemInformation> getAllMenuItems() throws Exception {
        List<MenuItemInformation> menuItems = new ArrayList<MenuItemInformation>();

        if(menuItemRepository.findAll().isEmpty()){
            throw new Exception("No Restaurant Tags to return");
        } else {
            List<MenuItem> dbMenuItems = menuItemRepository.findAll();
            for(MenuItem menuItem: dbMenuItems){
                menuItems.add(getMenuItemInformation(menuItem.getId()));
            }
            return menuItems;
        }
    }


    @Transactional
    public List<MenuItemInformation> getRestaurantMenu(Integer restaurantId) throws RestaurantNotFoundException{
        List<MenuItemInformation> restaurantMenuItems = new ArrayList<MenuItemInformation>();
        List<MenuItem> dbMenuItems = new ArrayList<MenuItem>();
        Restaurant restaurant = new Restaurant();

        if(restaurantRepository.findById(restaurantId).isEmpty()) {
            throw new RestaurantNotFoundException("Restaurant with Id:" + restaurantId + " does not exists");
        } else {
            restaurant = restaurantRepository.getById(restaurantId);
            dbMenuItems = menuItemRepository.findAllByRestaurants(restaurant);
            for(MenuItem menuItem: dbMenuItems){
                restaurantMenuItems.add(getMenuItemInformation(menuItem.getId()));
            }
            return restaurantMenuItems;
        }
    }


    @Transactional
    public String createNewMenuItem(MenuItem newMenuItem) throws RestaurantNotFoundException{
        MenuItem savedMenuItem = null;
        int restaurantId = newMenuItem.getRestaurants().getId();

        if(restaurantRepository.findById(restaurantId).isEmpty()) {
            throw new RestaurantNotFoundException("Restaurant with Id:" + restaurantId + " does not exists. Please try again");
        } else {
            savedMenuItem = menuItemRepository.saveAndFlush(newMenuItem);
            return "Menu Item '" + newMenuItem.getName() + "' created successfully. Id:" + savedMenuItem.getId() + "";
        }
    }


    @Transactional
    public String updateGivenMenuItem(MenuItem updatedMenuItem, Integer menuItemId) throws MenuItemNotFoundException {
        MenuItem currentMenuItem = null;

        if (menuItemRepository.findById(menuItemId).isEmpty()) {
            throw new MenuItemNotFoundException("MenuItem with Id:" + menuItemId + " does not exists. Please try again");
        } else {
            currentMenuItem = menuItemRepository.getById(menuItemId);
            currentMenuItem.setName(updatedMenuItem.getName());
            currentMenuItem.setDescription(updatedMenuItem.getDescription());
            currentMenuItem.setPrice(updatedMenuItem.getPrice());
            menuItemRepository.save(currentMenuItem);
            return "Menu Item has been updated successfully";
        }
    }


    @Transactional
    public String deleteGivenMenuItem(Integer menuItemId) throws MenuItemNotFoundException {
        if (menuItemRepository.findById(menuItemId).isEmpty()) {
            throw new MenuItemNotFoundException("MenuItem with Id:" + menuItemId + " does not exists. Please try again");
        } else {
            menuItemRepository.deleteById(menuItemId);
            return "Menu item has been deleted successfully";
        }
    }

    @Transactional
    public MenuItemInformation getMenuItemInformation(Integer menuItemId){
        Optional<MenuItem> menuItem = menuItemRepository.findById(menuItemId);
        MenuItem menuItem1 = menuItem.get();
        MenuItemInformation menuItemInformation = new MenuItemInformation();
        menuItemInformation.setItemId(menuItem1.getId());
        menuItemInformation.setRestaurants_id(menuItem1.getRestaurants().getId());
        menuItemInformation.setName(menuItem1.getName());
        menuItemInformation.setDescription(menuItem1.getDescription());
        menuItemInformation.setPrice(menuItem1.getPrice());
        menuItemInformation.setRestaurant_name(menuItem1.getRestaurants().getName());
        return menuItemInformation;
    }
}
