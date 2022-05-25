package com.smoothstack.restaurantmicroservice.data;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class MenuItemInformation {
    private Integer itemId;
    private Integer restaurants_id;
    private String name;
    private String description;
    private Double price;
    private String restaurant_name;
}


