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

###        /restaurants/{restaurantId} - Get restaurant by id

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

###        /restaurants/{restaurantId}/menuItems - Get menu by restaurant id

            will return:
                [
                    {
                        "itemId": 1,
                        "restaurants_id": 1,
                        "name": "Shrimp Fried Rice",
                        "description": "fired rice with shrimp, choice of spicy or non spicy",
                        "price": 4.25,
                        "restaurant_name": null
                    }
                ]

## POST
###        /restaurants - Create a restaurant
            
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
                {
                    "id": 3,
                    "location": {
                        "id": 3,
                        "locationName": null,
                        "address": null,
                        "city": null,
                        "state": null,
                        "zipCode": null
                    },
                    "owner": {
                        "id": 3,
                        "userName": null,
                        "password": null,
                        "userRoles": null,
                        "reviews": null,
                        "savedCards": null,
                        "savedLocations": null
                    },
                    "name": "Leos Barbecue",
                    "menuItems": null,
                    "restaurantTags": null
                }


###        /restaurants/menuItems - Create a menu item

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
                {
                    "id": 3,
                    "restaurants": {
                        "id": 2,
                        "location": null,
                        "owner": null,
                        "name": null,
                        "menuItems": null,
                        "restaurantTags": null
                    },
                    "name": "Kung Pao Chicken",
                    "description": "spicy chicken served over mixed vegaetables or fried rice",
                    "price": 4.25,
                    "discounts": null
                }

###        /restaurants/restaurantTags - Create a restaurant tag
            

            Post Data:
                {
                    "name": "deleteThisTag"
                }
            
            ________________________________________________
            
            will return:
                {
                    "id": 4,
                    "name": "deleteThisTag"
                }

## PUT
###        /restaurants/{restaurantId} - Update a restaurant
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

                currently errors out with infinite loop


###        /restaurants/menuItems/{menuItemId} - Update a menu item
            

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
                {
                    "id": 3,
                    "restaurants": {
                        "id": 1,
                        "location": null,
                        "owner": null,
                        "name": null,
                        "menuItems": null,
                        "restaurantTags": null
                    },
                    "name": "Garlic Bread Sticks",
                    "description": "side order of bread sticks",
                    "price": 4.25,
                    "discounts": []
                }


###        /restaurants/restaurantTags/{restaurantTagId} - Update a restaurant tag
        
            Put Data:
                {
                    "name": "Barbecue"
                }

            
            ________________________________________________

            will return:
                {
                    "id": 4,
                    "name": "Barbecue"
                }

###         /restaurants/{restaurantId}/{restaurantTagId} - Add a restaurant tag to an existing restaurant
           

            Put Data:
                ---
                    No body data reuquired as we are passing the tag id through route params.
                ---
            
            ________________________________________________

            will return:
                currently errors out with infinite loop





## DELETE
###        /restaurants/{restaurantId} - Delete a restaurant
            
            will return:
                Restaurant has been deleted successfully


###        /restaurants/menuItems/{menuItemId} - Delete a menu item
            
            will return:
                Menu item has been deleted successfully


###        /restaurants/restaurantTags/{restaurantTagId} - Delete a restaurant tag
            will return:
                Restaurant tag has been deleted successfully

