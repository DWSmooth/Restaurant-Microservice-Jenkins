package com.smoothstack.restaurantmicroservice.service;

import com.smoothstack.common.models.Restaurant;
import com.smoothstack.common.models.RestaurantTag;
import com.smoothstack.common.repositories.LocationRepository;
import com.smoothstack.common.repositories.RestaurantRepository;
import com.smoothstack.common.repositories.RestaurantTagRepository;
import com.smoothstack.common.repositories.UserRepository;

import com.smoothstack.restaurantmicroservice.data.RestaurantInformation;
import com.smoothstack.restaurantmicroservice.exception.LocationNotFoundException;
import com.smoothstack.restaurantmicroservice.exception.RestaurantNotFoundException;
import com.smoothstack.restaurantmicroservice.exception.RestaurantTagNotFoundException;
import com.smoothstack.restaurantmicroservice.exception.RestaurantTagAlreadyExistsException;
import com.smoothstack.restaurantmicroservice.exception.UserNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class RestaurantService {

    @Autowired
    LocationRepository locationRepository;
    @Autowired
    RestaurantRepository restaurantRepository;
    @Autowired
    RestaurantTagRepository restaurantTagRepository;
    @Autowired
    UserRepository userRepository;


    @Transactional
    public List<RestaurantInformation> getRestaurants() throws Exception {
        System.out.println("Getting Restaurants");
        List<RestaurantInformation> restaurants = new ArrayList<RestaurantInformation>();
        if(restaurantRepository.findAll().isEmpty()){
            throw new Exception("No Restaurants to return");
        } else {
            List<Restaurant> dbRestaurant = restaurantRepository.findAll();
            for(Restaurant restaurant: dbRestaurant){
                restaurants.add(getRestaurantInformation(restaurant.getId()));
                System.out.println("restaurant: " + restaurant.getId());
            }
            return restaurants;
        }
    }


    @Transactional
    public RestaurantInformation getRestaurantDetails(Integer restaurantId) throws RestaurantNotFoundException {
        if(restaurantRepository.findById(restaurantId).isEmpty()){
            throw new RestaurantNotFoundException("Restaurant with Id:" + restaurantId + " does not exists");
        } else {
            return getRestaurantInformation(restaurantId);
        }
    }


    @Transactional
    public String createNewRestaurant(Restaurant newRestaurant) throws LocationNotFoundException, UserNotFoundException {
        Restaurant savedRestaurant = null;
        int locationId = newRestaurant.getLocation().getId();
        int userId = newRestaurant.getOwner().getId();

        if(locationRepository.findById(locationId).isEmpty()) {
            throw new LocationNotFoundException("No location exists with that Id. Please try again.");
        } else {
            if(userRepository.findById(userId).isEmpty()) {
                throw new UserNotFoundException("No user exists with that Id. Please try again.");
            } else {
                savedRestaurant = restaurantRepository.saveAndFlush(newRestaurant);
                return "Restaurant '" + newRestaurant.getName() + "' created successfully. Id:" + savedRestaurant.getId() + "";
            }
        }
    }


    @Transactional
    public String updateGivenRestaurant(Restaurant updatedRestaurant, Integer restaurantId) throws LocationNotFoundException, RestaurantNotFoundException, UserNotFoundException {
        Restaurant currentRestaurant = null;
        int locationId = updatedRestaurant.getLocation().getId();
        int userId = updatedRestaurant.getOwner().getId();

        if(restaurantRepository.findById(restaurantId).isEmpty()){
            throw new RestaurantNotFoundException("Restaurant with Id:" + restaurantId + " does not exists. Please try again");
        } else {
            if(locationRepository.findById(locationId).isEmpty()){
                throw new LocationNotFoundException("No location exists with that Id. Please try again.");
            } else {
                if(userRepository.findById(userId).isEmpty()){
                    throw new UserNotFoundException("No user exists with that Id. Please try again.");
                } else {
                    currentRestaurant = restaurantRepository.getById(restaurantId);
                    currentRestaurant.setLocation(updatedRestaurant.getLocation());
                    currentRestaurant.setOwner(updatedRestaurant.getOwner());
                    currentRestaurant.setName(updatedRestaurant.getName());
                    restaurantRepository.save(currentRestaurant);
                    return "Restaurant has been updated successfully";
                }
            }
        }
    }


    @Transactional
    public String updateGivenRestaurantTags(Integer restaurantId, Integer restaurantTagId) throws RestaurantNotFoundException, RestaurantTagNotFoundException, RestaurantTagAlreadyExistsException {
        Restaurant currentRestaurant = null;
        RestaurantTag currentRestaurantTag = null;

        if(restaurantRepository.findById(restaurantId).isEmpty()){
            throw new RestaurantNotFoundException("Restaurant with Id:" + restaurantId + " does not exists. Please try again");
        } else {
            if(restaurantTagRepository.findById(restaurantTagId).isEmpty()){
                throw new RestaurantTagNotFoundException("RestaurantTag with Id:" + restaurantTagId + " does not exists. Please try again");
            } else {
                currentRestaurant = restaurantRepository.getById(restaurantId);
                currentRestaurantTag = restaurantTagRepository.getById(restaurantTagId);
                List<RestaurantTag> dbRestaurantTags = currentRestaurant.getRestaurantTags();

                for(RestaurantTag restaurantTag: dbRestaurantTags){
                    if (restaurantTag.getId() == restaurantTagId)
                        throw new RestaurantTagAlreadyExistsException("RestaurantTag with Id:" + restaurantTagId + " already exists for this Restaurant.");
                }

                dbRestaurantTags.add(currentRestaurantTag);
                currentRestaurant.setRestaurantTags(dbRestaurantTags);
                restaurantRepository.save(currentRestaurant);
                return "Restaurant Tag successfully added to restaurant";
            }
        }
    }


    @Transactional
    public String deleteGivenRestaurant(Integer restaurantId) throws RestaurantNotFoundException {
        if(restaurantRepository.findById(restaurantId).isEmpty()){
            throw new RestaurantNotFoundException("Restaurant with Id:" + restaurantId + " does not exists");
        } else {
            Restaurant oldRestaurant = restaurantRepository.getById(restaurantId);
            restaurantRepository.delete(oldRestaurant);
            return "Restaurant has been deleted successfully";
        }
    }


    @Transactional
    public RestaurantInformation getRestaurantInformation(Integer restaurantId){
        Optional<Restaurant> restaurant = restaurantRepository.findById(restaurantId);
        Restaurant restaurant1 = restaurant.get();
        RestaurantInformation restaurantInformation = new RestaurantInformation();

        restaurantInformation.setRestaurantId(restaurant1.getId());
        restaurantInformation.setName(restaurant1.getName());
        // set restaurant owner information
        restaurantInformation.setOwner_id(restaurant1.getOwner().getId());
        restaurantInformation.setOwner_name(restaurant1.getOwner().getUserName());
        // set restaurant location information
        restaurantInformation.setLocation_id(restaurant1.getLocation().getId());
        restaurantInformation.setLocation_name(restaurant1.getLocation().getLocationName());
        restaurantInformation.setAddress(restaurant1.getLocation().getAddress());
        restaurantInformation.setCity(restaurant1.getLocation().getCity());
        restaurantInformation.setState(restaurant1.getLocation().getState());
        restaurantInformation.setZip_code(restaurant1.getLocation().getZipCode());
        // set restaurantTags information
        if(restaurant1.getRestaurantTags() != null){
            restaurantInformation.setRestaurantTags(restaurant1.getRestaurantTags()
                    .stream()
                    .map(tag -> tag.getName())
                    .collect(Collectors.toList())
            );
        }
        return restaurantInformation;
    }
}