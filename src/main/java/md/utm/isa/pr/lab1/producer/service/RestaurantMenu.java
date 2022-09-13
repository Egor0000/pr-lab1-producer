package md.utm.isa.pr.lab1.producer.service;

import md.utm.isa.pr.lab1.producer.entity.Food;

import java.util.List;

public interface RestaurantMenu {
    List<Food> getMenu();

    Food getMaxPreparationTime(List<Food> foods);

    List<Food> getRandomFoods(int nr);
}
