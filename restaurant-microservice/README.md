# Restaurant Microservice

This microservice performs CRUD operations for

Restaurants - customers will find restaurants to order food items from.
Menu Items - the food items belonging to a specific restaurant.
Restaurant Tags - tags used to help narrow down a customers search for food items.  ( Not yet implemented )
Restaurant Reviews - customer reviews for restaurants.

## CRUD OPERATIONS

## GET
###        /restaurants - Get all restaurants

            will return:
                [
                    {
                        "restaurantId": 1,
                        "location_id": 1,
                        "owner_id": 1,
                        "name": "Spaghetti Warehouse",
                        "location_name": "Happy Buddha",
                        "address": "4901 61st Street",
                        "city": "Galveston",
                        "state": "TX",
                        "zip_code": 12345,
                        "owner_name": "John McClane",
                        "restaurantTags": [
                            "Asian"
                        ]
                    },
                    {
                        "restaurantId": 2,
                    ....
           
###        /restaurants/menuItems - Get all menu items

            will return:
                [
                    {
                        "itemId": 1,
                        "restaurants_id": 1,
                        "name": "Shrimp Fried Rice",
                        "description": "fired rice with shrimp, choice of spicy or non spicy",
                        "price": 4.25,
                        "restaurant_name": null
                    },
                    {
                        "itemId": 2,
                    ....


###       /restaurants/restaurantTags -  Get all restaurant tags

            will return:
                [
                    {
                        "id": 1,
                        "name": "Asian"
                    },
                    {
                        "id": 2,
                    ....

###        /restaurant/{restaurantId} - Get restaurant by id

            will return:
                {
                    "restaurantId": 1,
                    "location_id": 1,
                    "owner_id": 1,
                    "name": "Spaghetti Warehouse",
                    "location_name": "Happy Buddha",
                    "address": "4901 61st Street",
                    "city": "Galveston",
                    "state": "TX",
                    "zip_code": 12345,
                    "owner_name": "John McClane",
                    "restaurantTags": [
                        "Asian"
                    ]
                }

###        /restaurant/{restaurantId}/menuItems - Get menu by restaurant id

            will return:
                [
                    {
                        "itemId": 1,
                        "restaurants_id": 1,
                        "name": "Shrimp Fried Rice",
                        "description": "fired rice with shrimp, choice of spicy or non spicy",
                        "price": 4.25,
                        "restaurant_name": null
                    },
                    {
                        "itemId": 10,
                        "restaurants_id": 5,
                        "name": "to be deleted iiiiii",
                        "description": "potatoe baked, with barbecue topping",
                        "price": 5.5,
                        "restaurant_name": "Taqueria I"
                    }
                ]

## POST
###        /restaurant - Create a restaurant
            
            Post Data:
                {
                    "location": {
                        "id": 3
                    },
                    "owner": {
                        "id": 3
                    },
                    "name": "Leos Barbecue"
                }

            ________________________________________________

            will return:
                Success:
                    Restaurant 'McDonalds 09' created successfully. Id:24

                Exception:
                    No location exists with that Id. Please try again.,
                    No user exists with that Id. Please try again.



###        /restaurant/menuItem - Create a menu item

            Post Data:
                {
                    "restaurants": {
                        "id": 2
                    },
                    "name": "Kung Pao Chicken",
                    "description": "spicy chicken served over mixed vegaetables or fried rice",
                    "price": 4.25
                }
            
            ________________________________________________

            will return:
                Success:
                    Menu Item 'to be deleted i' created successfully. Id:11

                Exception:
                    MenuItem with Id:0 does not exists. Please try again

###        /restaurant/restaurantTag - Create a restaurant tag
            

            Post Data:
                {
                    "name": "deleteThisTag"
                }
            
            ________________________________________________
            
            will return:
                Success:
                    RestaurantTag with name: 'deleteThisTag 1' already exists in restaurant tags with ID: 11.
                
                Exception:
                    RestaurantTag with name: 'deleteThisTag 1' already exists in restaurant tags with ID: 11.

## PUT
###        /restaurant/{restaurantId} - Update a restaurant
            Put Data:
                {
                    "location": {
                        "id": 2
                    },
                    "owner": {
                        "id": 2
                    },
                    "name": "Taqueria"
                }

            ________________________________________________

             will return:
                Success:
                    Restaurant has been updated successfully

                Exception:
                    RestaurantTag with Id:0 does not exists. Please try again,
                    No location exists with that Id. Please try again.,
                    No user exists with that Id. Please try again.


###        /restaurant/menuItem/{menuItemId} - Update a menu item
            

            Put Data:
                {
                    "restaurants": {
                        "id": 1
                    },
                    "name": "Garlic Bread Sticks",
                    "description": "side order of bread sticks",
                    "price": 4.25
                }
            
            ________________________________________________

            will return:
                Success:
                    Menu Item has been updated successfully

                Exception:
                    MenuItem with Id:0 does not exists. Please try again


###        /restaurant/restaurantTag/{restaurantTagId} - Update a restaurant tag
        
            Put Data:
                {
                    "name": "Barbecue"
                }

            
            ________________________________________________

            will return:
                Success:
                    Restaurant Tag has been updated successfully
                
                Exception:
                    RestaurantTag with Id:0 does not exists. Please try again,
                    RestaurantTag with Id:8 already exists for this Restaurant.


###         /restaurant/{restaurantId}/restaurantTag/{restaurantTagId} - Add a restaurant tag to an existing restaurant
           

            Put Data:
                ---
                    No body data reuquired as we are passing the restaurantId and tagId through route params.
                ---
            
            ________________________________________________

            will return:
                Success:
                    Restaurant Tag successfully added to restaurant

                Exception:
                    Restaurant with Id:0 does not exists. Please try again,
                    RestaurantTag with Id:0 does not exists. Please try again,
                    RestaurantTag with Id:8 already exists for this Restaurant





## DELETE
###        /restaurant/{restaurantId} - Delete a restaurant
            
            will return:
                Success:
                    Restaurant has been deleted successfully

                Exception:
                    Restaurant with Id:0 does not exists. Please try again


###        /restaurant/menuItem/{menuItemId} - Delete a menu item
            
            will return:
                Success:
                    Menu item has been deleted successfully

                Exception:
                    MenuItem with Id:0 does not exists. Please try again

###        /restaurant/restaurantTag/{restaurantTagId} - Delete a restaurant tag
            will return:
                Success:
                    Restaurant tag has been deleted successfully

                Exception:
                    RestaurantTag with Id:0 does not exists. Please try again

