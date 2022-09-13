package md.utm.isa.pr.lab1.producer.service.impl;

import lombok.RequiredArgsConstructor;
import md.utm.isa.pr.lab1.producer.entity.Food;
import md.utm.isa.pr.lab1.producer.service.RestaurantMenu;
import md.utm.isa.pr.lab1.producer.utils.MenuUtil;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.concurrent.ThreadLocalRandom;

@Service
public class RestaurantMenuImpl implements RestaurantMenu {
    private final List<Food> menu;

    public RestaurantMenuImpl() {
        this.menu = MenuUtil.getMenu();
    }

    @Override
    public List<Food> getMenu() {
        return menu;
    }

    @Override
    public Food getMaxPreparationTime(List<Food> foods) {
        return foods.stream()
                .max(Comparator.comparing(Food::getPreparationTime))
                .orElseThrow(NoSuchElementException::new);
    }

    @Override
    public List<Food> getRandomFoods(int nr) {
        List<Food> randomizedFoodList = new ArrayList<>();

        for (int i = 0; i < nr; i++) {
            int randomIndex = ThreadLocalRandom.current().nextInt(nr) % menu.size();
            Food randomFood = menu.get(randomIndex);
            randomizedFoodList.add(randomFood);
        }

        return randomizedFoodList;
    }
}
