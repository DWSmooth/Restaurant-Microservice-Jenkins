package com.smoothstack.restaurantmicroservice.data;

import com.smoothstack.common.models.MenuItem;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class MenuItemInformation {

    private Integer itemId;
    private Integer restaurants_id;
    private String name;
    private String description;
    private Double price;

    private String restaurant_name;

    public static MenuItemInformation getFrontendData(MenuItem menuItem){
        return MenuItemInformation.builder()
                .itemId(menuItem.getId())
                .restaurants_id(menuItem.getRestaurants().getId())
                .name(menuItem.getName())
                .description(menuItem.getDescription())
                .price(menuItem.getPrice())

                .build();
    }

}


