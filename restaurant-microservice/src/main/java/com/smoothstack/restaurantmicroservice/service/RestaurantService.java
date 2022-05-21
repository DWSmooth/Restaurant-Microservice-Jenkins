package com.smoothstack.restaurantmicroservice.service;

import java.util.ArrayList;
import java.util.List;

import com.smoothstack.common.models.Restaurant;
import com.smoothstack.common.models.RestaurantTag;
import com.smoothstack.common.repositories.RestaurantRepository;

import com.smoothstack.common.repositories.RestaurantTagRepository;
import com.smoothstack.restaurantmicroservice.data.RestaurantInformation;
import com.smoothstack.restaurantmicroservice.exception.RestaurantNotFoundException;
import com.smoothstack.restaurantmicroservice.exception.RestaurantTagNotFoundException;
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
            e.printStackTrace();
        }
        System.out.println("Your request returned neither a restaurant or an error. Please try again.");
        return null;
    }

    public RestaurantInformation getRestaurantDetails(Integer restaurantId){
            Restaurant restaurant = restaurantRepository.getById(restaurantId);
            if(restaurant.getId() == null) throw new RestaurantNotFoundException("restaurantId-" + restaurantId);
            return RestaurantInformation.getFrontendData(restaurant);
    }

    public Restaurant createNewRestaurant(Restaurant restaurant){
        try {
            Restaurant newRestaurant = restaurantRepository.save(restaurant);
        } catch (Exception e){
            e.printStackTrace();
        }
        return restaurant;
    }

    public Restaurant updateGivenRestaurant(Restaurant newRestaurant, Integer restaurantId){
            Restaurant currentRestaurant = restaurantRepository.getById(restaurantId);
            if(currentRestaurant.getId() == null) throw new RestaurantNotFoundException("restaurantId-" + restaurantId);
                currentRestaurant.setLocation(newRestaurant.getLocation());
                currentRestaurant.setOwner(newRestaurant.getOwner());
                currentRestaurant.setName(newRestaurant.getName());
                return restaurantRepository.save(currentRestaurant);
    }

    public Restaurant updateGivenRestaurantTags(Integer restaurantId, Integer restaurantTagId){
            Restaurant currentRestaurant = restaurantRepository.getById(restaurantId);
            if(currentRestaurant.getId() == null) throw new RestaurantNotFoundException("restaurantId-" + restaurantId);
            RestaurantTag currentRestaurantTag = restaurantTagRepository.getById(restaurantTagId);
            if(currentRestaurantTag.getId() == null) throw new RestaurantTagNotFoundException("restaurantTagId-" + restaurantTagId);
            List<RestaurantTag> dbRestaurantTags = currentRestaurant.getRestaurantTags();
            dbRestaurantTags.add(currentRestaurantTag);
            currentRestaurant.setRestaurantTags(dbRestaurantTags);
            restaurantRepository.save(currentRestaurant);
            return currentRestaurant;
    }

    public String deleteGivenRestaurant(Integer id) {
        try {
            Restaurant oldRestaurant = restaurantRepository.getById(id);
            restaurantRepository.delete(oldRestaurant);
            return "Restaurant has been deleted successfully";
        } catch(Exception e ){
            e.printStackTrace();
        }
        return "That Restaurant could not be deleted. Please try again.";
    }
}
