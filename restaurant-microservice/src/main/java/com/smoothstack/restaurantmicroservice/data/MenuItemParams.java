package com.smoothstack.restaurantmicroservice.data;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class MenuItemParams {
    String query;
    String sort;
    BigDecimal min_price;
    BigDecimal max_price;
}
