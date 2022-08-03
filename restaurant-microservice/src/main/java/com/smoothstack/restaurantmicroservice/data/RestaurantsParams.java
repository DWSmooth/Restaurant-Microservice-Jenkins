package com.smoothstack.restaurantmicroservice.data;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
public class RestaurantsParams {

    String location;
    String query;
    String sort;
    BigDecimal min_rating;
    BigDecimal max_rating;
    BigDecimal min_dist;
    BigDecimal max_dist;
    String tags;
}
