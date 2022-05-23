package com.smoothstack.restaurantmicroservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.smoothstack.common.models.Restaurant;
import com.smoothstack.common.models.RestaurantTag;
import com.smoothstack.common.repositories.RestaurantRepository;

import com.smoothstack.common.repositories.RestaurantTagRepository;
import com.smoothstack.restaurantmicroservice.data.RestaurantInformation;
import com.smoothstack.restaurantmicroservice.exception.RestaurantNotFoundException;
import com.smoothstack.restaurantmicroservice.exception.RestaurantTagNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;

@Service
public class RestaurantService {

    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    RestaurantTagRepository restaurantTagRepository;

    @Transactional
    public List<RestaurantInformation> getRestaurants(){
        try {
            List<RestaurantInformation> restaurants = new ArrayList<RestaurantInformation>();
            List<Restaurant> dbRestaurant = restaurantRepository.findAll();
            for(Restaurant r: dbRestaurant){
                restaurants.add(getFrontendData(r.getId()));
                System.out.println("restaurant: " + r.getId());
            }
            return restaurants;
        } catch (Exception e){
            e.printStackTrace();
        }
        System.out.println("Your request returned neither a restaurant or an error. Please try again.");
        return null;
    }

    @Transactional
    public RestaurantInformation getRestaurantDetails(Integer restaurantId){
            Restaurant restaurant = restaurantRepository.getById(restaurantId);
        System.out.println("restaurant id: " + restaurant.getId());
        System.out.println("location id: " + restaurant.getLocation().getId());
        System.out.println("owner id: " + restaurant.getOwner().getId());
//            if(restaurant.getId() == null) throw new RestaurantNotFoundException("restaurantId-" + restaurantId);
            return getFrontendData(restaurantId);
    }

    @Transactional
    public Restaurant createNewRestaurant(Restaurant restaurant){
        try {
            Restaurant newRestaurant = restaurantRepository.save(restaurant);
        } catch (Exception e){
            e.printStackTrace();
        }
        return restaurant;
    }

    @Transactional
    public Restaurant updateGivenRestaurant(Restaurant newRestaurant, Integer restaurantId){
            Restaurant currentRestaurant = restaurantRepository.getById(restaurantId);
            if(currentRestaurant.getId() == null) throw new RestaurantNotFoundException("restaurantId-" + restaurantId);
                currentRestaurant.setLocation(newRestaurant.getLocation());
                currentRestaurant.setOwner(newRestaurant.getOwner());
                currentRestaurant.setName(newRestaurant.getName());
                return restaurantRepository.save(currentRestaurant);
    }

    @Transactional
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

    @Transactional
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

        @Transactional
        public RestaurantInformation getFrontendData(Integer restaurantId){

            Optional<Restaurant> restaurant = restaurantRepository.findById(restaurantId);
            Restaurant restaurant1 = restaurant.get();
            RestaurantInformation restaurantInformation = new RestaurantInformation();
            restaurantInformation.setRestaurantId(restaurant1.getId());
            restaurantInformation.setLocation_id(restaurant1.getLocation().getId());
            restaurantInformation.setOwner_id(restaurant1.getOwner().getId());
            restaurantInformation.setName(restaurant1.getName());

            restaurantInformation.setLocation_name(restaurant1.getLocation().getLocationName());
            restaurantInformation.setAddress(restaurant1.getLocation().getAddress());
            restaurantInformation.setCity(restaurant1.getLocation().getCity());
            restaurantInformation.setState(restaurant1.getLocation().getState());
            restaurantInformation.setZip_code(restaurant1.getLocation().getZipCode());

            restaurantInformation.setOwner_name(restaurant1.getOwner().getUserName());

            restaurantInformation.setRestaurantTags(restaurant1.getRestaurantTags()
                    .stream()
                    .map(tag -> tag.getName())
                        .collect(Collectors.toList())
            );
            return restaurantInformation;
    }
}
