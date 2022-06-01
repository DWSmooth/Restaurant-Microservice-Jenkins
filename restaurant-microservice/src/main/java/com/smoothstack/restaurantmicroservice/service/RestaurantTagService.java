package com.smoothstack.restaurantmicroservice.service;

import com.smoothstack.common.models.RestaurantTag;
import com.smoothstack.common.repositories.RestaurantTagRepository;
import com.smoothstack.restaurantmicroservice.exception.RestaurantTagAlreadyExistsException;
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
    public List<RestaurantTag> getAllRestaurantTags() throws Exception {
        List<RestaurantTag> restaurantTags = new ArrayList<RestaurantTag>();
        if(restaurantTagRepository.findAll().isEmpty()){
            throw new Exception("No Restaurant Tags to return");
        } else {
            List<RestaurantTag> dbRestaurantTags = restaurantTagRepository.findAll();
            for(RestaurantTag rt: dbRestaurantTags){
                restaurantTags.add(rt);
            }
            return restaurantTags;
        }
    }


    @Transactional
    public RestaurantTag createNewRestaurantTag(RestaurantTag restaurantTag) throws RestaurantTagAlreadyExistsException {
        String restaurantTagName = restaurantTag.getName();
        if (restaurantTagRepository.findTopByName(restaurantTagName).isPresent()){
                Integer tagId = restaurantTagRepository.findTopByName(restaurantTagName).get().getId();
                throw new RestaurantTagAlreadyExistsException("RestaurantTag with name: '" + restaurantTagName + "' already exists in restaurant tags with ID: " + tagId + ".");
        } else {
            RestaurantTag restaurantTag1 = restaurantTagRepository.save(restaurantTag);
            return restaurantTag1;
        }
    }


    @Transactional
    public String updateGivenRestaurantTag(RestaurantTag updatedRestaurantTag, Integer restaurantTagId) throws RestaurantTagNotFoundException, RestaurantTagAlreadyExistsException {
        RestaurantTag currentRestaurantTag = null;
        String restaurantTagName = updatedRestaurantTag.getName();

        if(restaurantTagRepository.findById(restaurantTagId).isEmpty()){
            throw new RestaurantTagNotFoundException("RestaurantTag with Id:" + restaurantTagId + " does not exists. Please try again");
        } else {
            if (restaurantTagRepository.findTopByName(restaurantTagName).isPresent()) {
                Integer tagId = restaurantTagRepository.findTopByName(restaurantTagName).get().getId();
                throw new RestaurantTagAlreadyExistsException("RestaurantTag with name: '" + restaurantTagName + "' already exists in restaurant tags with ID: " + tagId + ".");
            } else {
                currentRestaurantTag = restaurantTagRepository.getById(restaurantTagId);
                currentRestaurantTag.setName(updatedRestaurantTag.getName());
                restaurantTagRepository.save(currentRestaurantTag);
                return "Restaurant Tag has been updated successfully";
            }
        }
    }


    @Transactional
    public String deleteGivenRestaurantTag(Integer restaurantTagId) throws RestaurantTagNotFoundException {
        if(restaurantTagRepository.findById(restaurantTagId).isEmpty()){
            throw new RestaurantTagNotFoundException("RestaurantTag with Id:" + restaurantTagId + " does not exists. Please try again");
        } else {
            restaurantTagRepository.deleteById(restaurantTagId);
            return "Restaurant Tag has been deleted successfully";
        }
    }
}