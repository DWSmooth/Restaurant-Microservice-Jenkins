package com.smoothstack.restaurantmicroservice.service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import com.smoothstack.common.models.Restaurant;
import com.smoothstack.common.repositories.RestaurantRepository;

import com.smoothstack.restaurantmicroservice.data.RestaurantInformation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

@Service
public class RestaurantService {

    @Autowired
    RestaurantRepository restaurantRepository;

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
