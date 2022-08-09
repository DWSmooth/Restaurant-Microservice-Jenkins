package com.smoothstack.restaurantmicroservice.service;

import com.smoothstack.common.exceptions.*;
import com.smoothstack.common.models.RestaurantTag;
import com.smoothstack.common.repositories.RestaurantTagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

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
    public String enableGivenRestaurantTag(Integer restaurantTagId) throws RestaurantTagNotFoundException {
        Optional<RestaurantTag> restaurantTagOptional = restaurantTagRepository.findById(restaurantTagId);
        if(restaurantTagOptional.isEmpty()){
            throw new RestaurantTagNotFoundException("RestaurantTag with Id:" + restaurantTagId + " does not exists. Please try again");
        } else if(restaurantTagOptional.get().isEnabled()) {
            throw new RestaurantTagNotFoundException("RestaurantTag with Id:" + restaurantTagId + " is already enabled. Please try again");
        } else {
            RestaurantTag restaurantTag = restaurantTagOptional.get();
            restaurantTag.setEnabled(true);
            restaurantTagRepository.saveAndFlush(restaurantTag);
            return "Restaurant Tag has been enabled successfully";
        }
    }

    @Transactional
    public String disableGivenRestaurantTag(Integer restaurantTagId) throws RestaurantTagNotFoundException {
        Optional<RestaurantTag> restaurantTagOptional = restaurantTagRepository.findById(restaurantTagId);
        if(restaurantTagOptional.isEmpty()){
            throw new RestaurantTagNotFoundException("RestaurantTag with Id:" + restaurantTagId + " does not exists. Please try again");
        } else if(!restaurantTagOptional.get().isEnabled()) {
            throw new RestaurantTagNotFoundException("RestaurantTag with Id:" + restaurantTagId + " is already disabled. Please try again");
        } else {
            RestaurantTag restaurantTag = restaurantTagOptional.get();
            restaurantTag.setEnabled(false);
            restaurantTagRepository.saveAndFlush(restaurantTag);
            return "Restaurant Tag has been disabled successfully";
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