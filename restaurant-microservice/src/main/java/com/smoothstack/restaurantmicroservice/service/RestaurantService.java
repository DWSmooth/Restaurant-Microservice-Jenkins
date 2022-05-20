package com.smoothstack.restaurantmicroservice.service;

import java.util.ArrayList;
import java.util.List;

import com.smoothstack.common.models.Restaurant;
import com.smoothstack.common.models.RestaurantTag;
import com.smoothstack.common.repositories.RestaurantRepository;

import com.smoothstack.common.repositories.RestaurantTagRepository;
import com.smoothstack.restaurantmicroservice.data.RestaurantInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class RestaurantService {

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    RestaurantTagRepository restaurantTagRepository;


    public List<RestaurantInformation> getRestaurants(){
        try {
            List<RestaurantInformation> restaurants = new ArrayList<RestaurantInformation>();
            List<Restaurant> dbRestaurant = restaurantRepository.findAll();
            for(Restaurant r: dbRestaurant){
                restaurants.add(RestaurantInformation.getFrontendData(r));
            }
            return restaurants;
        } catch (Exception e){
            return null;
        }
    }

    public RestaurantInformation getRestaurantDetails(Integer restaurantId){
        try {
            Restaurant restaurant = restaurantRepository.getById(restaurantId);
            return RestaurantInformation.getFrontendData(restaurant);
        } catch (Exception e){
            return null;
        }
    }

    public Restaurant createNewRestaurant(Restaurant restaurant){
        try {
            Restaurant newRestaurant = restaurantRepository.save(restaurant);
            return restaurant;
        } catch (Exception e){
            return null;
        }
    }

    public Restaurant updateGivenRestaurant(Restaurant newRestaurant, Integer restaurantId){
        try {
            Restaurant currentRestaurant = restaurantRepository.getById(restaurantId);
                currentRestaurant.setLocation(newRestaurant.getLocation());
                currentRestaurant.setOwner(newRestaurant.getOwner());
                currentRestaurant.setName(newRestaurant.getName());
                return restaurantRepository.save(currentRestaurant);
        } catch (Exception e){
            return null;
        }
    }

public Restaurant updateGivenRestaurantTags(Integer restaurantId, Integer restaurantTagId){
    try {
        Restaurant currentRestaurant = restaurantRepository.getById(restaurantId);
        RestaurantTag currentRestaurantTag = restaurantTagRepository.getById(restaurantTagId);
        List<RestaurantTag> dbRestaurantTags = currentRestaurant.getRestaurantTags();
        dbRestaurantTags.add(currentRestaurantTag);
        currentRestaurant.setRestaurantTags(dbRestaurantTags);
        restaurantRepository.save(currentRestaurant);
        return currentRestaurant;
    } catch (Exception e){
        return null;
    }
}

    public String deleteGivenRestaurant(Integer id) {
        try {
            Restaurant oldRestaurant = restaurantRepository.getById(id);
            restaurantRepository.delete(oldRestaurant);
            return "Restaurant has been deleted successfully";
        } catch (Exception e){
            return null;
        }
    }
}
