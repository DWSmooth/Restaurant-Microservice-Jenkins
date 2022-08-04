package com.smoothstack.restaurantmicroservice.controller;

import com.amazonaws.HttpMethod;
import com.smoothstack.restaurantmicroservice.data.*;
import com.smoothstack.restaurantmicroservice.service.AwsS3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@CrossOrigin(origins = "*")
@RestController
public class AwsController {
    private final AwsS3Service awsS3Service;

    @Autowired
    public AwsController(AwsS3Service awsS3Service) {
        this.awsS3Service = awsS3Service;
    }

    // Not used - and should not use, but left in as an example of pre-signed urls
    @GetMapping("/generate-upload-url")
    public ResponseEntity generateUploadUrl() {
        try {
            return ResponseEntity.ok(awsS3Service.generatePreSignedUrl(UUID.randomUUID() + ".txt", "restaurant-image-storage", HttpMethod.PUT));
        } catch (Exception exception) {
            return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/restaurant/{restaurantName}/{locationId}/setup")
    public ResponseEntity setUpRestaurant(@PathVariable String restaurantName,
                                          @PathVariable Integer locationId,
                                          @RequestBody RestaurantInformation restaurantInformation){
        try {
            return ResponseEntity.status(HttpStatus.OK).body(awsS3Service.setUpRestaurantFolder(restaurantInformation));
        } catch (Exception exception) {
            return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/restaurant/{restaurantName}/{locationId}")
    public ResponseEntity deleteRestaurant(@PathVariable String restaurantName,
                                           @PathVariable Integer locationId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(awsS3Service.deleteRestaurant(new RestaurantInformation()
                    .builder()
                    .name(restaurantName)
                    .location_id(locationId)
                    .build())
            );
        } catch (Exception exception) {
            return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @GetMapping("/restaurant/{restaurantName}/{locationId}/setup")
    public ResponseEntity isRestaurantSetUp(@PathVariable String restaurantName, @PathVariable Integer locationId) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(awsS3Service.isRestaurantSetUp(
                    new RestaurantInformation().builder().name(restaurantName).location_id(locationId).build())
            );

        } catch (Exception exception) {
            return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }


    @GetMapping("/restaurant/{restaurantName}/{locationId}/menu-item/{menuItemName}/{menuItemId}")
    public ResponseEntity<RestaurantMenuItemImages> getMenuItemImage(@PathVariable String restaurantName,
                                                                     @PathVariable Integer locationId,
                                                                     @PathVariable String menuItemName,
                                                                     @PathVariable Integer menuItemId) {

        try {
            return ResponseEntity.status(HttpStatus.OK).body(awsS3Service.getMenuItem(new MenuItemRequest()
                    .builder()
                    .restaurantInformation(new RestaurantInformation().builder()
                            .name(restaurantName)
                            .location_id(locationId)
                            .build()
                    )
                    .menuItemInformation(new MenuItemInformation()
                            .builder()
                            .name(menuItemName)
                            .itemId(menuItemId)
                            .build()
                    )
                    .build())
            );

        } catch (Exception exception) {
            return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/restaurant/{restaurantName}/{locationId}/menu-item")
    public ResponseEntity postMenuItem(@PathVariable String restaurantName,
                                       @PathVariable Integer locationId,
                                       @RequestBody RestaurantMenuItemImages restaurantMenuItemImages) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(awsS3Service.addMenuItemsImage(restaurantMenuItemImages));
        } catch (Exception exception) {
            return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/restaurant/{restaurantName}/{locationId}/menu-item")
    public ResponseEntity deleteMenuItem(@PathVariable String restaurantName,
                                         @PathVariable Integer locationId,
                                         @RequestBody MenuItemRequest menuItemRequest) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(awsS3Service.deleteMenuItem(menuItemRequest));
        } catch (Exception exception) {
            return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping("/restaurant/{restaurantName}/{locationId}/asset/{assetName}")
    public ResponseEntity getRestaurantAsset(@PathVariable String restaurantName,
                                             @PathVariable Integer locationId,
                                             @PathVariable String assetName) {
        try {

            return ResponseEntity.status(HttpStatus.OK).body(awsS3Service.getRestaurantAsset(new RestaurantAssetBody()
                    .builder()
                    .restaurantInformation(new RestaurantInformation()
                            .builder()
                            .name(restaurantName)
                            .location_id(locationId)
                            .build())
                    .restaurantAsset(new RestaurantAsset()
                            .builder()
                            .assetName(assetName)
                            .build())
                    .build())
            );

        } catch (Exception exception) {
            return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }

    }

    @PostMapping("/restaurant/{restaurantName}/{locationId}/asset/")
    public ResponseEntity postRestaurantAsset(@PathVariable String restaurantName,
                                              @PathVariable Integer locationId,
                                              @RequestBody RestaurantAssetBody restaurantAssetBody) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(awsS3Service.addRestaurantAsset(restaurantAssetBody));
        } catch (Exception exception) {
            return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }

    @DeleteMapping("/restaurant/{restaurantName}/{locationId}/asset/{assetName}")
    public ResponseEntity deleteRestaurantAsset(@PathVariable String restaurantName,
                                                @PathVariable Integer locationId,
                                                @PathVariable String assetName) {
        try {
            return ResponseEntity.status(HttpStatus.OK).body(awsS3Service.deleteRestaurantAsset(new RestaurantAssetBody()
                    .builder()
                    .restaurantInformation(new RestaurantInformation()
                            .builder()
                            .name(restaurantName)
                            .location_id(locationId)
                            .build()
                    )
                    .restaurantAsset(new RestaurantAsset()
                            .builder()
                            .assetName(assetName)
                            .build())
                    .build())
            );

        } catch (Exception exception) {
            return new ResponseEntity(exception.getMessage(), HttpStatus.BAD_REQUEST);
        }
    }
}
