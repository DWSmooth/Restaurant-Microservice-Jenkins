package com.smoothstack.restaurantmicroservice.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class RestaurantAssetBody {

    private RestaurantInformation restaurantInformation;

    private RestaurantAsset restaurantAsset;

}
