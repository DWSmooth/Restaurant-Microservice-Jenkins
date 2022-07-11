package com.smoothstack.restaurantmicroservice.service;

import com.google.common.base.Strings;
import com.smoothstack.common.models.Location;
import com.smoothstack.common.models.Restaurant;
import com.smoothstack.common.models.RestaurantTag;
import com.smoothstack.common.repositories.*;

import com.smoothstack.restaurantmicroservice.data.*;
import com.smoothstack.restaurantmicroservice.exception.*;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Expression;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
public class RestaurantService {

    @Autowired
    LocationRepository locationRepository;
    @Autowired
    RestaurantRepository restaurantRepository;
    @Autowired
    RestaurantTagRepository restaurantTagRepository;

    @Autowired
    ReviewRepository reviewRepository;

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
    public String createNewRestaurant(RestaurantInformation newRestaurant) throws UserNotFoundException {
        Restaurant savedRestaurant = null;
        int ownerId = newRestaurant.getOwner_id();

        if(userRepository.findById(ownerId).isPresent()) {
            List<Location> locationList = locationRepository.findAllByLocationName(newRestaurant.getLocation_name());
            
            // Create new Location Entity
            Location newLocation = new Location();
            newLocation.setLocationName(newRestaurant.getLocation_name());
            newLocation.setAddress(newRestaurant.getAddress());
            newLocation.setCity(newRestaurant.getCity());
            newLocation.setState(newRestaurant.getState());
            newLocation.setZipCode(newRestaurant.getZip_code());

            if (!locationList.isEmpty()) {
                // Find if any items in the list match the provided address
                // If so, build the Restaurant entity
                for(Location l: locationList) {
                    if (l.compareValues(newLocation)) {
                        savedRestaurant = buildRestaurant(newRestaurant, l);
                        break;
                    }
                }
            }

            if (savedRestaurant == null) {
                // If there is no saved RestaurantEntity, create a new Location, and build the Restaurant entity using it.
                Location savedLocation = locationRepository.save(newLocation);
                savedRestaurant = buildRestaurant(newRestaurant, savedLocation);
            }

            savedRestaurant = restaurantRepository.save(savedRestaurant);
            return "Restaurant '" + newRestaurant.getName() + "' created successfully. Id:" + savedRestaurant.getId() + "";
        }
        throw new UserNotFoundException("No user exists with Id " + ownerId +". Please try again.");
    }


    @Transactional
    public String updateGivenRestaurant(RestaurantInformation updatedRestaurant, Integer restaurantId) throws LocationNotFoundException, RestaurantNotFoundException, UserNotFoundException {
        Restaurant currentRestaurant = null;
        int locationId = updatedRestaurant.getLocation_id();
        int ownerId = updatedRestaurant.getOwner_id();

        if(restaurantRepository.findById(restaurantId).isEmpty()){
            throw new RestaurantNotFoundException("Restaurant with Id:" + restaurantId + " does not exists. Please try again");
        } else {
            if(locationRepository.findById(locationId).isEmpty()){
                throw new LocationNotFoundException("No location exists with that Id. Please try again.");
            } else {
                if(userRepository.findById(ownerId).isEmpty()){
                    throw new UserNotFoundException("No user exists with that Id. Please try again.");
                } else {
                    currentRestaurant = restaurantRepository.getById(restaurantId);
                    currentRestaurant.setLocation(locationRepository.getById(locationId));
                    currentRestaurant.setOwner(userRepository.getById(ownerId));
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

    @Transactional
    public Restaurant buildRestaurant(RestaurantInformation information, Location location) {
        Restaurant createdRestaurant = new Restaurant();
        
        //TODO: Do proper error throwing if Id, name, etc are not provided
        createdRestaurant.setOwner(userRepository.getById(information.getOwner_id()));
        createdRestaurant.setName(information.getName());
        createdRestaurant.setLocation(location);
        //TODO: Set Tags

        return createdRestaurant;
    }

    @Transactional
    public String enableGivenRestaurantTags(Integer restaurantId, Integer restaurantTagId) {
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
                    if (restaurantTag.getId() == restaurantTagId && restaurantTag.isEnabled()) {
                        throw new RestaurantTagAlreadyExistsException("RestaurantTag with Id:" + restaurantTagId + " is already enabled for this Restaurant.");
                    } else if (restaurantTag.getId() == restaurantTagId && !restaurantTag.isEnabled()) {
                        restaurantTag.setEnabled(true);
                    }
                }

                dbRestaurantTags.add(currentRestaurantTag);
                currentRestaurant.setRestaurantTags(dbRestaurantTags);
                restaurantRepository.save(currentRestaurant);
                return "Restaurant Tag successfully enabled";
            }
        }
    }

    @Transactional
    public String disableGivenRestaurantTags(Integer restaurantId, Integer restaurantTagId) {
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
                    if (restaurantTag.getId() == restaurantTagId && !restaurantTag.isEnabled()) {
                        throw new RestaurantTagAlreadyExistsException("RestaurantTag with Id:" + restaurantTagId + " is already disabled for this Restaurant.");
                    } else if (restaurantTag.getId() == restaurantTagId && restaurantTag.isEnabled()) {
                        restaurantTag.setEnabled(false);
                    }
                }

                dbRestaurantTags.add(currentRestaurantTag);
                currentRestaurant.setRestaurantTags(dbRestaurantTags);
                restaurantRepository.save(currentRestaurant);
                return "Restaurant Tag successfully disabled";
            }
        }
    }

    @Transactional
    public String enableRestaurant(Integer restaurantId) {
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(restaurantId);
        if(restaurantOptional.isEmpty()){
            throw new RestaurantNotFoundException("Restaurant with Id:" + restaurantId + " does not exists");
        } else if (restaurantOptional.get().isEnabled()) {
            throw new RestaurantAlreadyDisabledException("Restaurant with Id:" + restaurantId + " is already enabled");

        } else {
            Restaurant restaurant = restaurantRepository.getById(restaurantId);
            restaurant.setEnabled(true);
            restaurantRepository.saveAndFlush(restaurant);
            return "Restaurant has been enabled successfully";
        }
    }

    @Transactional
    public String disableRestaurant(Integer restaurantId) {
        Optional<Restaurant> restaurantOptional = restaurantRepository.findById(restaurantId);
        if(restaurantOptional.isEmpty()){
            throw new RestaurantNotFoundException("Restaurant with Id:" + restaurantId + " does not exists");
        } else if (!restaurantOptional.get().isEnabled()) {
            throw new RestaurantAlreadyDisabledException("Restaurant with Id:" + restaurantId + " is already disabled");

        } else {
            Restaurant restaurant = restaurantRepository.getById(restaurantId);
            restaurant.setEnabled(false);
            restaurantRepository.saveAndFlush(restaurant);
            return "Restaurant has been disabled successfully";
        }
    }


    public List<RestaurantInformation> findRestaurants(RestaurantsParams restaurantsParams) throws Exception {
        // TODO
        // implement geolocation for min/max dist and location

        System.out.println(restaurantsParams);

        Boolean hasTags = !Strings.isNullOrEmpty(restaurantsParams.getTags());

        List<RestaurantInformation> restaurantInformations;

        Specification<Restaurant> specification = getFilteredRestaurants(restaurantsParams);

        if(hasTags) {
            restaurantInformations = restaurantRepository.findAll(specification)
                    .stream()
                    .map(r -> getRestaurantInformation(r.getId()))
                    .filter(r -> r.getRestaurantTags().containsAll(Arrays.asList(restaurantsParams.getTags().split(","))))
                    .collect(Collectors.toList());
        } else {
            restaurantInformations = restaurantRepository.findAll(specification)
                    .stream()
                    .map(r -> getRestaurantInformation(r.getId()))
                    .collect(Collectors.toList());
        }

        return restaurantInformations;
    }

    public static Specification<Restaurant> getFilteredRestaurants(RestaurantsParams params) {

        Boolean hasLocation = !Strings.isNullOrEmpty(params.getLocation());
        Boolean hasQuery = !Strings.isNullOrEmpty(params.getQuery());
        Boolean hasSort = !Strings.isNullOrEmpty(params.getSort());
        Boolean hasMinRating = !isNull(params.getMin_rating());
        Boolean hasMaxRating = !isNull(params.getMax_rating());
        Boolean hasMinDist = !isNull(params.getMin_dist());
        Boolean hasMaxDist = !isNull(params.getMax_dist());
        Boolean hasTags = !Strings.isNullOrEmpty(params.getTags());

        return (root, criteriaQuery, criteriaBuilder) -> {

            List<Predicate> predicates = new ArrayList<>();

            if(hasLocation) {
                predicates.add(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("location").get("locationName")), '%' + params.getLocation().toLowerCase() + '%')
                );
            }

            if(hasQuery) {

                // empty list of predicates that will be put into one big or predicate
                // this allows all words to come up through search
                List<Predicate> qs = new ArrayList<>();
                for(String q: params.getQuery().split(" ")) {
                    qs.add(criteriaBuilder.or(
                            criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), '%' + q.toLowerCase() + '%')
                    ));
                }
                predicates.add(criteriaBuilder.or(qs.toArray(new Predicate[qs.size()])));

            }

            if(hasSort) {
                // Will only sort by ratings for now, however ratings have not been implemented
                if(params.getSort().equals("ratings.a")) {

                    if(root.get("reviews").isNull().equals(false)) {
                        criteriaQuery.orderBy(criteriaBuilder.asc(criteriaBuilder.avg(root.get("reviews").get("rating"))));
                    }
                } else if(params.getSort().equals("ratings.d")) {

                    if(root.get("reviews").isNull().equals(false)) {
                        criteriaQuery.orderBy(criteriaBuilder.desc(criteriaBuilder.avg(root.get("reviews").get("rating"))));
                    }

                } else if (params.getSort().equals("distance.a")) {
                    // TODO
                    // implement geolocation

                } else if (params.getSort().equals("distance.d")) {
                    // TODO
                    // implement geolocation
                } else {
                    throw new InvalidSearchException(String.format("Sort method '%s' is not valid", params.getSort()));
                }
            }

            if(hasMinRating && hasMaxRating) {
                if(root.get("reviews").isNull().equals(false)) {
                    predicates.add(criteriaBuilder.ge(criteriaBuilder.avg(root.get("reviews").get("rating")), params.getMin_rating()));
                    predicates.add(criteriaBuilder.le(criteriaBuilder.avg(root.get("reviews").get("rating")), params.getMax_rating()));
                }
            }

            if(hasMinRating && !hasMaxRating) {
                if(root.get("reviews").isNull().equals(false)) {
                    predicates.add(criteriaBuilder.ge(criteriaBuilder.avg(root.get("reviews").get("rating")), params.getMin_rating()));
                }
            }

            if(!hasMinRating && hasMaxRating) {
                if(root.get("reviews").isNull().equals(false)) {
                    predicates.add(criteriaBuilder.le(criteriaBuilder.avg(root.get("reviews").get("rating")), params.getMax_rating()));
                }
            }

            if(hasMinDist) {
                // TODO
                // implement geolocation so this can be applied
            }

            if(hasMaxDist) {
                // TODO
                // implement geolocation so this can be applied
            }

            if(hasTags) {
                /*predicates.add(
                        root.get("restaurantTags").in(Arrays.asList(params.getTags().split(",")))
                );*/
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }
}