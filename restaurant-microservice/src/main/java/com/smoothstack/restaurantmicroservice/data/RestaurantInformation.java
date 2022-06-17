package com.smoothstack.restaurantmicroservice.data;

import java.util.List;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
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

    // matches to restaurant Tags;
    private List<String> restaurantTags;
}
