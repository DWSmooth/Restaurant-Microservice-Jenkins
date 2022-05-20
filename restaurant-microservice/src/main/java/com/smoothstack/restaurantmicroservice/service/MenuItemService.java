package com.smoothstack.restaurantmicroservice.service;

import com.smoothstack.common.models.MenuItem;
import com.smoothstack.common.models.Restaurant;
import com.smoothstack.common.repositories.MenuItemRepository;
import com.smoothstack.common.repositories.RestaurantRepository;

import com.smoothstack.restaurantmicroservice.data.MenuItemInformation;
import com.smoothstack.restaurantmicroservice.data.RestaurantInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class MenuItemService {

    @Autowired
    MenuItemRepository menuItemRepository;

    @Autowired
    RestaurantRepository restaurantRepository;

    public List<MenuItemInformation> getAllMenuItems(){
        try {
            List<MenuItemInformation> menuItems = new ArrayList<MenuItemInformation>();
            List<MenuItem> dbMenuItems = menuItemRepository.findAll();
            for(MenuItem m: dbMenuItems){
                menuItems.add(MenuItemInformation.getFrontendData(m));
            }
            return menuItems;
        } catch (Exception e){
            return null;
        }
    }


    public List<MenuItemInformation> getMenuItemDetails(Integer restaurantId){
        try {
            List<MenuItemInformation> restaurantMenuItems = new ArrayList<MenuItemInformation>();
            List<MenuItem> menuItems = getMenuItems(restaurantId);
            for(MenuItem m: menuItems){
                restaurantMenuItems.add(MenuItemInformation.getFrontendData(m));
            }
            return restaurantMenuItems;
        } catch (Exception e){
            return null;
        }
    }

    private List<MenuItem> getMenuItems(Integer restaurantId){
        Restaurant restaurant = new Restaurant();
        List<MenuItem> menuItems = new ArrayList<MenuItem>();
        try {
            restaurant = restaurantRepository.getById(restaurantId);
            menuItems = menuItemRepository.findAllByRestaurants(restaurant);
        } catch(Exception e){
            return null;
        }
        return menuItems;
    }

    public MenuItem createNewMenuItem(MenuItem menuItem){
        try {
            MenuItem newMenuItem = menuItemRepository.save(menuItem);
            return menuItem;
        } catch (Exception e){
            return null;
        }
    }


    public MenuItem updateGivenMenuItem(MenuItem newMenuItem, Integer menuItemId){
        try {
            MenuItem currentMenuItem = menuItemRepository.getById(menuItemId);
            currentMenuItem.setRestaurants(newMenuItem.getRestaurants());
            currentMenuItem.setName(newMenuItem.getName());
            currentMenuItem.setDescription(newMenuItem.getDescription());
            currentMenuItem.setPrice(newMenuItem.getPrice());
            return menuItemRepository.save(currentMenuItem);
        } catch (Exception e){
            return null;
        }
    }


    public String deleteGivenMenuItem(Integer id) {
        try {
            MenuItem oldMenuItem = menuItemRepository.getById(id);
            menuItemRepository.deleteById(id);
            return "Menu item has been deleted successfully";
        } catch (Exception e){
            return null;
        }
    }

}
