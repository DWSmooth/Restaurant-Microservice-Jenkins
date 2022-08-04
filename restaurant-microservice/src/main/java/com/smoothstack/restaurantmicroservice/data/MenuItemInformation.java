package com.smoothstack.restaurantmicroservice.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
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


