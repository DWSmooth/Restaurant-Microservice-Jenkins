package com.smoothstack.restaurantmicroservice.data;

import com.smoothstack.common.models.Restaurant;
import com.smoothstack.common.models.RestaurantTag;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Data;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class RestaurantTagInformation {

    private Integer tagId;
    private String name;
//    private List<Restaurant> restaurants;

    public static RestaurantTagInformation getFrontEndData(RestaurantTag restaurantTag){
        return RestaurantTagInformation.builder()
                .tagId(restaurantTag.getId())
                .name(restaurantTag.getName())
//                .restaurants(restaurantTag.getRestaurants())
                .build();
    }

}
