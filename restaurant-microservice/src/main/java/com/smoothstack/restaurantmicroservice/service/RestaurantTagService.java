package com.smoothstack.restaurantmicroservice.service;

import com.smoothstack.common.models.MenuItem;
import com.smoothstack.common.models.Restaurant;
import com.smoothstack.common.models.RestaurantTag;
import com.smoothstack.common.repositories.RestaurantRepository;
import com.smoothstack.common.repositories.RestaurantTagRepository;
import com.smoothstack.restaurantmicroservice.data.MenuItemInformation;
import com.smoothstack.restaurantmicroservice.exception.MenuItemNotFoundException;
import com.smoothstack.restaurantmicroservice.exception.RestaurantTagNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class RestaurantTagService {
    @Autowired
    RestaurantRepository restaurantRepository;

    @Autowired
    RestaurantTagRepository restaurantTagRepository;



    public List<RestaurantTag> getAllRestaurantTags(){
        List<RestaurantTag> restaurantTags = new ArrayList<RestaurantTag>();
        try {

            List<RestaurantTag> dbRestaurantTags = restaurantTagRepository.findAll();
            for(RestaurantTag rt: dbRestaurantTags){
                restaurantTags.add(rt);
            }
            return restaurantTags;
        } catch (Exception e){
            return null;
        }
    }

    public RestaurantTag createNewRestaurantTag(RestaurantTag restaurantTag){
        try {
            RestaurantTag restaurantTag1 = restaurantTagRepository.save(restaurantTag);
            return restaurantTag1;
        } catch (Exception e){
            return null;
        }
    }


public RestaurantTag updateGivenRestaurantTag(RestaurantTag newRestaurantTag, Integer restaurantTagId){
    try {
        RestaurantTag currentRestaurantTag = restaurantTagRepository.getById(restaurantTagId);
        if(currentRestaurantTag.getId() == null) throw new RestaurantTagNotFoundException("restaurantTagId-" + restaurantTagId);
        currentRestaurantTag.setName(newRestaurantTag.getName());
        return restaurantTagRepository.save(currentRestaurantTag);
    } catch (Exception e){
        return null;
    }
}


    public String deleteGivenRestaurantTag(Integer id) {
        try {
            RestaurantTag oldRestaurantTag = restaurantTagRepository.getById(id);
            restaurantTagRepository.deleteById(id);
            return "Restaurant Tag has been deleted successfully";
        } catch (Exception e){
            e.printStackTrace();
        }
        return "That Restaurant Tag could not be deleted. Please try again.";
    }






}