package com.smoothstack.restaurantmicroservice.service;

import com.google.common.base.Strings;
import com.smoothstack.common.exceptions.*;
import com.smoothstack.common.models.MenuItem;
import com.smoothstack.common.models.Restaurant;
import com.smoothstack.common.repositories.MenuItemRepository;
import com.smoothstack.common.repositories.RestaurantRepository;

import com.smoothstack.restaurantmicroservice.data.MenuItemInformation;
import com.smoothstack.restaurantmicroservice.data.MenuItemParams;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import javax.persistence.criteria.Predicate;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static java.util.Objects.isNull;

@Service
public class MenuItemService {

    @Autowired
    MenuItemRepository menuItemRepository;
    @Autowired
    RestaurantRepository restaurantRepository;


    @Transactional
    public List<MenuItemInformation> getAllMenuItems() throws Exception {
        List<MenuItemInformation> menuItems = new ArrayList<MenuItemInformation>();

        if(menuItemRepository.findAll().isEmpty()){
            throw new Exception("No Restaurant Tags to return");
        } else {
            List<MenuItem> dbMenuItems = menuItemRepository.findAll();
            for(MenuItem menuItem: dbMenuItems){
                menuItems.add(getMenuItemInformation(menuItem.getId()));
            }
            return menuItems;
        }
    }


    @Transactional
    public List<MenuItemInformation> getRestaurantMenu(Integer restaurantId) throws RestaurantNotFoundException {
        List<MenuItemInformation> restaurantMenuItems = new ArrayList<MenuItemInformation>();
        List<MenuItem> dbMenuItems = new ArrayList<MenuItem>();
        Restaurant restaurant = new Restaurant();

        if(restaurantRepository.findById(restaurantId).isEmpty()) {
            throw new RestaurantNotFoundException("Restaurant with Id:" + restaurantId + " does not exists");
        } else {
            restaurant = restaurantRepository.getById(restaurantId);
            dbMenuItems = menuItemRepository.findAllByRestaurants(restaurant);
            for(MenuItem menuItem: dbMenuItems){
                restaurantMenuItems.add(getMenuItemInformation(menuItem.getId()));
            }
            return restaurantMenuItems;
        }
    }


    @Transactional
    public String createNewMenuItem(MenuItem newMenuItem) throws RestaurantNotFoundException{
        MenuItem savedMenuItem = null;
        int restaurantId = newMenuItem.getRestaurants().getId();

        if(restaurantRepository.findById(restaurantId).isEmpty()) {
            throw new RestaurantNotFoundException("Restaurant with Id:" + restaurantId + " does not exists. Please try again");
        } else {
            savedMenuItem = menuItemRepository.saveAndFlush(newMenuItem);
            return "Menu Item '" + newMenuItem.getName() + "' created successfully. Id:" + savedMenuItem.getId() + "";
        }
    }


    @Transactional
    public String updateGivenMenuItem(MenuItem updatedMenuItem, Integer menuItemId) throws MenuItemNotFoundException {
        MenuItem currentMenuItem = null;

        if (menuItemRepository.findById(menuItemId).isEmpty()) {
            throw new MenuItemNotFoundException("MenuItem with Id:" + menuItemId + " does not exists. Please try again");
        } else {
            currentMenuItem = menuItemRepository.getById(menuItemId);
            currentMenuItem.setName(updatedMenuItem.getName());
            currentMenuItem.setDescription(updatedMenuItem.getDescription());
            currentMenuItem.setPrice(updatedMenuItem.getPrice());
            menuItemRepository.save(currentMenuItem);
            return "Menu Item has been updated successfully";
        }
    }


    @Transactional
    public String deleteGivenMenuItem(Integer menuItemId) throws MenuItemNotFoundException {
        if (menuItemRepository.findById(menuItemId).isEmpty()) {
            throw new MenuItemNotFoundException("MenuItem with Id:" + menuItemId + " does not exists. Please try again");
        } else {
            menuItemRepository.deleteById(menuItemId);
            return "Menu item has been deleted successfully";
        }
    }

    @Transactional
    public MenuItemInformation getMenuItemInformation(Integer menuItemId){
        Optional<MenuItem> menuItem = menuItemRepository.findById(menuItemId);
        MenuItem menuItem1 = menuItem.get();
        MenuItemInformation menuItemInformation = new MenuItemInformation();
        menuItemInformation.setItemId(menuItem1.getId());
        menuItemInformation.setRestaurants_id(menuItem1.getRestaurants().getId());
        menuItemInformation.setName(menuItem1.getName());
        menuItemInformation.setDescription(menuItem1.getDescription());
        menuItemInformation.setPrice(menuItem1.getPrice());
        menuItemInformation.setRestaurant_name(menuItem1.getRestaurants().getName());
        return menuItemInformation;
    }


    @Transactional
    public String enableGivenMenuItem(Integer menuItemId) {
        Optional<MenuItem> menuItemOptional = menuItemRepository.findById(menuItemId);
        if (menuItemOptional.isEmpty()) {
            throw new MenuItemNotFoundException("MenuItem with Id:" + menuItemId + " does not exists. Please try again");

        } else if (menuItemOptional.get().isEnabled()) {
            throw new MenuItemNotFoundException("MenuItem with Id:" + menuItemId + " is already enabled. Please try again");
        } else {
            MenuItem menuItem = menuItemOptional.get();
            menuItem.setEnabled(true);
            menuItemRepository.saveAndFlush(menuItem);
            return "Menu item has been enabled successfully";
        }
    }

    @Transactional
    public String disableGivenMenuItem(Integer menuItemId) {
        Optional<MenuItem> menuItemOptional = menuItemRepository.findById(menuItemId);
        if (menuItemOptional.isEmpty()) {
            throw new MenuItemNotFoundException("MenuItem with Id:" + menuItemId + " does not exists. Please try again");

        } else if (!menuItemOptional.get().isEnabled()) {
            throw new MenuItemNotFoundException("MenuItem with Id:" + menuItemId + " is already disabled. Please try again");
        } else {
            MenuItem menuItem = menuItemOptional.get();
            menuItem.setEnabled(false);
            menuItemRepository.saveAndFlush(menuItem);
            return "Menu item has been disabled successfully";
        }
    }

    @Transactional
    public List<MenuItemInformation> findMenuItems(Integer restaurantId, MenuItemParams params) {

        System.out.println(params);

        Specification<MenuItem> specification = getFilteredMenuItems(restaurantId, params);
        List<MenuItemInformation> menuItemInformations = menuItemRepository.findAll(specification)
                .stream()
                .map(m -> getMenuItemInformation(m.getId()))
                .collect(Collectors.toList());

        return menuItemInformations;
    }

    @Transactional
    public Specification<MenuItem> getFilteredMenuItems(Integer restaurantId, MenuItemParams params) {

        Boolean hasQuery = !Strings.isNullOrEmpty(params.getQuery());
        Boolean hasSort = !Strings.isNullOrEmpty(params.getSort());
        Boolean hasMinPrice = !isNull(params.getMin_price());
        Boolean hasMaxPrice = !isNull(params.getMax_price());


        return (root, criteriaQuery, criteriaBuilder) -> {

            // list of predicates to final AND search
            List<Predicate> predicates = new ArrayList<>();

            // making sure all items come from specified restaurant id
            predicates.add(criteriaBuilder.equal(root.get("restaurants").get("id"), restaurantId));


            if(hasQuery) {
                // empty list of predicates that will be put into one big or predicate
                // this allows all words to come up through search
                List<Predicate> qs = new ArrayList<>();
                for(String q: params.getQuery().split(" ")) {
                    qs.add(criteriaBuilder.or(
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("name")), '%' + q.toLowerCase() + '%'),
                        criteriaBuilder.like(criteriaBuilder.lower(root.get("description")), '%' + q.toLowerCase() + '%')
                    ));
                }
                predicates.add(criteriaBuilder.or(qs.toArray(new Predicate[qs.size()])));
            }

            if(hasSort) {
                if(params.getSort().equals("price.a")) {
                    criteriaQuery.orderBy(criteriaBuilder.asc(root.get("price")));
                } else if (params.getSort().equals("price.d")) {
                    criteriaQuery.orderBy(criteriaBuilder.desc(root.get("price")));
                } else {
                    throw new InvalidSearchException(String.format("Sort method '%s' is not valid ", params.getSort()));
                }
            }

            if(hasMinPrice && hasMaxPrice) {
                if(params.getMin_price().floatValue() <= params.getMax_price().floatValue()) {
                    predicates.add(criteriaBuilder.ge(root.get("price"), params.getMin_price()));
                    predicates.add(criteriaBuilder.le(root.get("price"), params.getMax_price()));
                } else {
                    throw new InvalidSearchException(String.format("Either min_price is greater than max_price or max_price is less than min_price"));
                }

            }

            if(hasMinPrice && !hasMaxPrice) {
                predicates.add(criteriaBuilder.ge(root.get("price"), params.getMin_price()));
            }

            if(!hasMinPrice && hasMaxPrice) {
                predicates.add(criteriaBuilder.le(root.get("price"), params.getMax_price()));
            }

            return criteriaBuilder.and(predicates.toArray(new Predicate[0]));
        };
    }


}
