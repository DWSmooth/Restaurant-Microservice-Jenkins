# Restaurant Microservice

This microservice performs CRUD operations for

Restaurants - customers will find restaurants to order food items from.
Menu Items - the food items belonging to a specific restaurant.
Restaurant Tags - tags used to help narrow down a customers search for food items.
***     Not yet implemented  ***
***     Restaurant Reviews - customer reviews for restaurants.  *** 

## CRUD OPERATIONS

## GET
###        Get all restaurants      -       /restaurants


            will return
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
           
###        Get all menu items
            /restaurants/menuItems

            returns:
                ```js
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
                ```

###        Get all restaurant tags
            /restaurants/restaurantTags

            returns:
                ```js
                [
                    {
                        "id": 1,
                        "name": "Asian"
                    },
                    {
                        "id": 2,
                    ....
                ```
###        Get restaurant by id
            /restaurants/{restaurantId}

            returns:
                ```js
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
                ```



###        Get menu by restaurant id
            /restaurants/{restaurantId}/menuItems

            returns:
                ```js
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
                ```



## POST
###        Create a restaurant
            /restaurants

            Post Data
                ```js
                {
                    "location": {
                        "id": 3
                    },
                    "owner": {
                        "id": 3
                    },
                    "name": "Leos Barbecue"
                }
                ```

            returns:
                ```js
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
                ```


###        Create a menu item
            /restaurants/menuItems

            Post Data
                ```js
                {
                    "restaurants": {
                        "id": 2
                    },
                    "name": "Kung Pao Chicken",
                    "description": "spicy chicken served over mixed vegaetables or fried rice",
                    "price": 4.25
                }
                ```

            returns:
                ```js
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
                ```

###        Create a restaurant tag
            /restaurants/restaurantTags

            Post Data
                ```js
                {
                    "name": "deleteThisTag"
                }
                ```
            
            returns:
                ```js
                {
                    "id": 4,
                    "name": "deleteThisTag"
                }
                ```



## PUT
###        Update a restaurant
            /restaurants/{restaurantId}
            ```js
            currently errors out
            ```

###        Update a menu item
            /restaurants/menuItems/{menuItemId}

            Put Data:
                ```js
                {
                    "restaurants": {
                        "id": 1
                    },
                    "name": "Garlic Bread Sticks",
                    "description": "side order of bread sticks",
                    "price": 4.25
                }
                ```

            returns:
                ```js
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
                ```

###        Update a restaurant tag
            /restaurants/restaurantTags/{restaurantTagId}

            Put Data:
                ```js
                {
                    "name": "Barbecue"
                }
                ```

            returns:
                ```js
                {
                    "id": 4,
                    "name": "Barbecue"
                }
                ```

###        Add a restaurant tag to an existing restaurant
            /restaurants/{restaurantId}/{restaurantTagId}

            Put Data:
                ```js
                ---
                    No body data reuquired as we are passing the tag id through route params.
                ---
                ```

            returns:
                ```js
                currently errors out
                ```




## DELETE
###        Delete a restaurant
            /restaurants/{restaurantId}
            
            returns:
            ```js
                Restaurant has been deleted successfully
            ```

###        Delete a menu item
            /restaurants/menuItems/{menuItemId}
            returns:
            ```js
                Menu item has been deleted successfully
            ```

###        Delete a restaurant tag
            returns:
            ```js
                Restaurant Tag has been deleted successfully
            ```
