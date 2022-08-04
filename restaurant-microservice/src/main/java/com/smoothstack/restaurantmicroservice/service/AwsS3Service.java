package com.smoothstack.restaurantmicroservice.service;

import com.amazonaws.HttpMethod;
import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.*;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.smoothstack.common.exceptions.*;
import com.smoothstack.restaurantmicroservice.data.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class AwsS3Service {

    private final static String menuItems = "menu_items/";
    private final static String assets = "assets/";

    private final static String restaurantInformationJSON = "restaurantInformation.json";

    private final AmazonS3 amazonS3;

    @Value("${BUCKET}")
    private String bucketName;

    @Autowired
    public AwsS3Service(AmazonS3 amazonS3) {
        this.amazonS3 = amazonS3;
    }

    // Not used - and should not use, but left in as an example of pre-signed urls
    public String generatePreSignedUrl(String filePath,
                                       String bucketName,
                                       HttpMethod httpMethod) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTime(new Date());
        calendar.add(Calendar.MINUTE, 10); //validity of 10 minutes
        return amazonS3.generatePresignedUrl(bucketName, filePath, calendar.getTime(), httpMethod).toString();
    }

    public boolean doesBucketExist() {
        return amazonS3.doesBucketExistV2(bucketName);
    }

    public Boolean isRestaurantSetUp(RestaurantInformation restaurantInformation) {
        if(!doesBucketExist()) {
            throw new BucketExistsException("Bucket does not exist");
        }

        String restaurantInfo = String.format("%s%d/%s",restaurantInformation.getName(),restaurantInformation.getLocation_id(), restaurantInformationJSON);
        return amazonS3.doesObjectExist(bucketName, restaurantInfo);
    }

    public String deleteRestaurant(RestaurantInformation restaurantInformation) {
        String status = "Restaurant folders failed to delete";
        if(doesBucketExist()) {
            if(isRestaurantSetUp(restaurantInformation)) {
                try {

                    List<DeleteObjectsRequest.KeyVersion> keys = getAllMenuItemsAndAssetsKeys(restaurantInformation);

                    DeleteObjectsRequest multiObjectDeleteRequest = new DeleteObjectsRequest(bucketName)
                            .withKeys(keys)
                            .withQuiet(false);

                    DeleteObjectsResult deleteObjectsResult = amazonS3.deleteObjects(multiObjectDeleteRequest);

                    status = "Restaurant folders successfully deleted";

                } catch (Exception e) {
                    System.out.println(e);
                }

            } else {
                throw new RestaurantSetUpException("Restaurant not setup");
            }

        } else {
            throw new BucketExistsException("Restaurant image bucket already exists");
        }
        return status;
    }

    public String setUpRestaurantFolder(RestaurantInformation restaurantInformation) {
        String status = "Restaurant SetUp Failed";
        if(doesBucketExist()) {
            if(!isRestaurantSetUp(restaurantInformation)) {
                try {
                    String restaurant_root = String.format("%s%d/", restaurantInformation.getName(), restaurantInformation.getLocation_id());

                    ObjectMapper om = new ObjectMapper();
                    byte[] bytes = om.writeValueAsBytes(restaurantInformation);
                    ObjectMetadata omd = new ObjectMetadata();
                    omd.setContentLength(bytes.length);

                    amazonS3.putObject(bucketName, restaurant_root + restaurantInformationJSON, new ByteArrayInputStream(bytes), omd);
                    amazonS3.putObject(bucketName, restaurant_root + menuItems, "null");
                    amazonS3.putObject(bucketName, restaurant_root + assets, "null");

                    status = "Restaurant successfully setup";

                } catch (JsonProcessingException jsonProcessingException) {
                    throw new ObjectMappingException("Cannot parse restaurant information correctly");
                }

            } else {
                throw new RestaurantSetUpException("Restaurant already setup");
            }

        } else {
            throw new BucketExistsException("Restaurant image bucket already exists");
        }
        return status;
    }

    public List<DeleteObjectsRequest.KeyVersion> getAllMenuItemsAndAssetsKeys(RestaurantInformation restaurantInformation) {
        List<DeleteObjectsRequest.KeyVersion> keys = new ArrayList<>();
        String restaurant_root = String.format("%s%d/", restaurantInformation.getName(), restaurantInformation.getLocation_id());
        try {

            ListObjectsRequest listObjectsRequest = new ListObjectsRequest()
                    .withBucketName(bucketName)
                    .withPrefix(restaurant_root);

            ObjectListing objectListing = amazonS3.listObjects(listObjectsRequest);

            keys = objectListing.getObjectSummaries().stream().map(os -> new DeleteObjectsRequest.KeyVersion(os.getKey())).collect(Collectors.toList());

        } catch (Exception exception) {
            throw exception;
        }

        return keys;
    }

    public String addMenuItemsImage(RestaurantMenuItemImages data) {

        String status = "Failed to upload menu item data";

        try {

            if(!isRestaurantSetUp(data.getRestaurantInformation())) {
                setUpRestaurantFolder(data.getRestaurantInformation());
            }

            String menuItemPath = String.format("%s%d/%s%s%d.json",
                    data.getRestaurantInformation().getName(),
                    data.getRestaurantInformation().getLocation_id(),
                    menuItems,
                    data.getMenuItemInformation().getName(),
                    data.getMenuItemInformation().getItemId()
            );

            ObjectMapper om = new ObjectMapper();
            byte[] bytes = om.writeValueAsBytes(data);
            ObjectMetadata omd = new ObjectMetadata();
            omd.setContentLength(bytes.length);

            amazonS3.putObject(bucketName, menuItemPath, new ByteArrayInputStream(bytes), omd);
            status = String.format("Menu item: %s was uploaded successfully", data.getMenuItemInformation().getName());

        } catch (JsonProcessingException jsonProcessingException) {
            throw new MenuItemImageUploadException(String.format("Menu item: %s failed to upload. Parsing error.",data.getMenuItemInformation().getName()));
        } catch (RestaurantSetUpException restaurantSetUpException) {
            throw restaurantSetUpException;
        } catch (Exception exception) {
            throw exception;
        }

        return status;
    }


    public RestaurantMenuItemImages getMenuItem(MenuItemRequest menuItemRequest) {

        try {

            if(!isRestaurantSetUp(menuItemRequest.getRestaurantInformation())) {
                throw new RestaurantSetUpException("Restaurant not set up");
            }

            String menuItemPath = String.format("%s%d/%s%s%d.json",
                    menuItemRequest.getRestaurantInformation().getName(),
                    menuItemRequest.getRestaurantInformation().getLocation_id(),
                    menuItems,
                    menuItemRequest.getMenuItemInformation().getName(),
                    menuItemRequest.getMenuItemInformation().getItemId()
            );

            ObjectMapper om = new ObjectMapper();

            if(amazonS3.doesObjectExist(bucketName, menuItemPath)) {

                RestaurantMenuItemImages menuItemImages = om.readValue(amazonS3
                        .getObject(bucketName, menuItemPath)
                        .getObjectContent(), RestaurantMenuItemImages.class
                );

                return menuItemImages;

            } else {
                throw new MenuItemNotFoundException("Menu item not found");
            }

        } catch (RestaurantSetUpException restaurantSetUpException) {
            throw restaurantSetUpException;

        } catch (IOException ioException) {
            throw new ObjectMappingException("Not able to parse menu item after retrieval");
        } catch (Exception exception) {
            throw exception;
        }
    }

    public String deleteMenuItem(MenuItemRequest menuItemRequest) {
        String status = "Menu item failed to delete";
        try {

            if(!isRestaurantSetUp(menuItemRequest.getRestaurantInformation())) {
                throw new RestaurantSetUpException("Restaurant not set up");
            }

            String menuItemPath = String.format("%s%d/%s%s%d.json",
                    menuItemRequest.getRestaurantInformation().getName(),
                    menuItemRequest.getRestaurantInformation().getLocation_id(),
                    menuItems,
                    menuItemRequest.getMenuItemInformation().getName(),
                    menuItemRequest.getMenuItemInformation().getItemId()
            );

            if(!amazonS3.doesObjectExist(bucketName, menuItemPath)) {
                throw new MenuItemNotFoundException(String.format("Menu item: %s not found.",
                        menuItemRequest.getMenuItemInformation().getName()));
            }

            amazonS3.deleteObject(bucketName, menuItemPath);

            status = "Menu item deleted successfully";

        } catch (MenuItemNotFoundException menuItemNotFoundException) {
            throw menuItemNotFoundException;
        } catch (Exception exception) {
            throw exception;
        }
        return status;
    }

    public String addRestaurantAsset(RestaurantAssetBody restaurantAssetBody) {

        String status = "Failed to upload restaurant asset";

        try {
            if(!isRestaurantSetUp(restaurantAssetBody.getRestaurantInformation())) {
                throw new RestaurantSetUpException("Restaurant not set up");
            }

            String assetsPath = String.format("%s%d/%s%s.json",
                    restaurantAssetBody.getRestaurantInformation().getName(),
                    restaurantAssetBody.getRestaurantInformation().getLocation_id(),
                    assets, restaurantAssetBody.getRestaurantAsset().getAssetName());

            ObjectMapper om = new ObjectMapper();
            byte[] content = om.writeValueAsBytes(restaurantAssetBody.getRestaurantAsset());
            ObjectMetadata omd = new ObjectMetadata();
            omd.setContentLength(content.length);

            PutObjectResult putObjectResult = amazonS3.putObject(bucketName, assetsPath, new ByteArrayInputStream(content), omd);
            status = "Restaurant asset uploaded successfully";


        } catch (RestaurantSetUpException restaurantSetUpException) {
            throw restaurantSetUpException;
        } catch (JsonProcessingException jsonProcessingException) {
            throw new ObjectMappingException("Not able to parse restaurant asset after retrieval");
        } catch (Exception exception) {
            throw exception;
        }

        return status;
    }

    public RestaurantAsset getRestaurantAsset(RestaurantAssetBody restaurantAssetBody) {
        try {
            if(!isRestaurantSetUp(restaurantAssetBody.getRestaurantInformation())) {
                throw new RestaurantSetUpException("Restaurant not set up");
            }

            String assetsPath = String.format("%s%d/%s%s.json",
                    restaurantAssetBody.getRestaurantInformation().getName(),
                    restaurantAssetBody.getRestaurantInformation().getLocation_id(),
                    assets, restaurantAssetBody.getRestaurantAsset().getAssetName());

            if(amazonS3.doesObjectExist(bucketName, assetsPath)) {

                ObjectMapper om = new ObjectMapper();

                RestaurantAsset restaurantAsset = om.readValue(amazonS3
                        .getObject(bucketName, assetsPath)
                        .getObjectContent(), RestaurantAsset.class
                );

                return restaurantAsset;
            } else {
                throw new RestaurantAssetNotFoundException("Restaurant asset not found");
            }

        } catch (RestaurantSetUpException restaurantSetUpException) {
            throw restaurantSetUpException;
        } catch (IOException ioException) {
            throw new ObjectMappingException("Failed to parse restaurant asset");
        } catch (RestaurantAssetNotFoundException restaurantAssetNotFoundException) {
            throw restaurantAssetNotFoundException;
        } catch (Exception exception) {
            throw exception;
        }
    }

    public String deleteRestaurantAsset(RestaurantAssetBody restaurantAssetBody) {

        String status = "Failed to delete restaurant asset";

        try {

            if(!isRestaurantSetUp(restaurantAssetBody.getRestaurantInformation())) {
                throw new RestaurantSetUpException("Restuarant not set up");
            }

            String assetsPath = String.format("%s%d/%s%s.json",
                    restaurantAssetBody.getRestaurantInformation().getName(),
                    restaurantAssetBody.getRestaurantInformation().getLocation_id(),
                    assets, restaurantAssetBody.getRestaurantAsset().getAssetName());

            if(!amazonS3.doesObjectExist(bucketName, assetsPath)) {
                throw new RestaurantAssetNotFoundException("Asset not found");
            }

            amazonS3.deleteObject(bucketName, assetsPath);

            status = "Restaurant asset deleted successfully";

        } catch (RestaurantSetUpException restaurantSetUpException) {
            throw restaurantSetUpException;
        } catch (RestaurantAssetNotFoundException restaurantAssetNotFoundException) {
            throw restaurantAssetNotFoundException;
        } catch (Exception exception) {
            throw exception;
        }

        return status;
    }

}