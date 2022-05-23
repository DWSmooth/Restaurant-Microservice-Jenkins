package com.smoothstack.restaurantmicroservice.service;

import com.smoothstack.common.models.RestaurantTag;
import com.smoothstack.common.repositories.RestaurantTagRepository;
import com.smoothstack.restaurantmicroservice.exception.RestaurantTagNotFoundException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class RestaurantTagService {
    @Autowired
    RestaurantTagRepository restaurantTagRepository;


    @Transactional
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

    @Transactional
    public RestaurantTag createNewRestaurantTag(RestaurantTag restaurantTag){
        try {
            RestaurantTag restaurantTag1 = restaurantTagRepository.save(restaurantTag);
            return restaurantTag1;
        } catch (Exception e){
            return null;
        }
    }

    @Transactional
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

    @Transactional
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