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
public class RestaurantMenuItemImages {

    private RestaurantInformation restaurantInformation;

    private MenuItemInformation menuItemInformation;

    private List<Image> images;
}
