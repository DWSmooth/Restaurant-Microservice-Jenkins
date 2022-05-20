package com.smoothstack.restaurantmicroservice.data;

import java.util.List;
import java.util.stream.Collectors;

import com.smoothstack.common.models.Restaurant;
import com.smoothstack.common.models.MenuItem;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestaurantInformation {

    private Integer restaurantId;
    private Integer location_id;
    private Integer owner_id;
    private String name;

    // matches to location_id
    private String location_name;
    private String address;
    private String city;
    private String state;
    private Integer zip_code;

    // matches to owner_id
    private String owner_name;

    // matches to restayrant Tags;
    private List<String> restaurantTags;



    public static RestaurantInformation getFrontendData(Restaurant restaurant){
        return RestaurantInformation.builder()
                .restaurantId(restaurant.getId())
                .location_id(restaurant.getLocation().getId())
                .owner_id(restaurant.getOwner().getId())
                .name(restaurant.getName())

                .location_name(restaurant.getLocation().getLocationName())
                .address(restaurant.getLocation().getAddress())
                .city(restaurant.getLocation().getCity())
                .state(restaurant.getLocation().getState())
                .zip_code(restaurant.getLocation().getZipCode())

                .owner_name(restaurant.getOwner().getUserName())
                .restaurantTags(restaurant.getRestaurantTags()
                        .stream()
                        .map( tag -> tag.getName())
                        .collect(Collectors.toList())
                )

                .build();
    }


    public static RestaurantInformation getNewRestaurant(Restaurant restaurant) {
        return RestaurantInformation.builder()
                .restaurantId(restaurant.getId())
                .location_id(restaurant.getLocation().getId())
                .owner_id(restaurant.getOwner().getId())
                .name(restaurant.getName())

                .location_name(restaurant.getLocation().getLocationName())
                .address(restaurant.getLocation().getAddress())
                .city(restaurant.getLocation().getCity())
                .state(restaurant.getLocation().getState())
                .zip_code(restaurant.getLocation().getZipCode())

                .owner_name(restaurant.getOwner().getUserName())
                .restaurantTags(restaurant.getRestaurantTags()
                        .stream()
                        .map( tag -> tag.getName())
                        .collect(Collectors.toList())
                )

                .build();
    }
}
