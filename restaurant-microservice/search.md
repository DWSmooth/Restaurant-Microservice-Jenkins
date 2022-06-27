## SEARCH CRUD OPERATIONS

#### The following endpoints are UNIMPLEMENTED, and require revision after geolocation research.

`GEOLOCATION_INPUT`: Placeholder for a specific location.

`GEOLOCATION_DISTANCE_UNIT`: Placeholder for the unit of distance used for searches. Miles by default?

## GET
###        /restaurants/search - Search for restaurants

            query params:
                {
                    // Location
                    "l": GEOLOCATION_INPUT,
                    // Query String (URL-encoded)
                    "q": "steakhouse",
                    // Sort ("field.method": Method can be "a" Ascending or "d" Descending)
                    "sort": "rating.a",
                    // Filter By Min Rating (Float, Positive)
                    "frmin": 2.0,
                    // Filter By Max Rating (Float, Positive)
                    "frmax": 5.0,
                    // Filter By Min Distance (Float, Positive, GEOLOCATION_DISTANCE_UNIT)
                    "fdmin": 2.0,
                    // Filter By Max Distance (Float, Positive, GEOLOCATION_DISTANCE_UNIT)
                    "fdmax": 40.0,
                    // Filter By Tags (URL-encoded CSV of tag names)
                    "ft": "Mexican,Healthy,Breakfast"
                }

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

###        /restaurant/{restaurantId}/menuItems/search - Search for menu items from specific restaurant

            query params:
                {
                    // Query String (URL-encoded)
                    "q": "steak",
                    // Sort ("field.method": Method can be "a" Ascending or "d" Descending)
                    "sort": "price.d",
                    // Filter By Min Price (Float, Positive)
                    "fpmin": 2.0,
                    // Filter By Max Price (Float, Positive)
                    "fpmax": 5.0
                }

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