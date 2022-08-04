package com.smoothstack.restaurantmicroservice.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class RestaurantInformation {

    private Integer restaurantId;
    private Integer location_id;
    private Integer owner_id;
    private String name;
    private Boolean enabled;

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
