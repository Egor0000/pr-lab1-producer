package md.utm.isa.pr.lab1.producer.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import md.utm.isa.pr.lab1.producer.entity.Food;
import md.utm.isa.pr.lab1.producer.entity.Table;
import md.utm.isa.pr.lab1.producer.service.DinningHallService;
import md.utm.isa.pr.lab1.producer.service.RestaurantMenu;
import md.utm.isa.pr.lab1.producer.service.TableService;
import md.utm.isa.pr.lab1.producer.utils.MenuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
@AllArgsConstructor
@Slf4j
public class TableServiceImpl implements TableService {
    private ConcurrentMap<String, Thread> tableList = new ConcurrentHashMap<>();
    private final DinningHallService dinningHallService;

    private final RestaurantMenu restaurantMenu;

    private List<Food> foods = new ArrayList<>();

    @Value("${dinning.tables}")
    private String tableCnt;

    @Autowired
    public TableServiceImpl(DinningHallService dinningHallService, RestaurantMenu restaurantMenu) {
        this.dinningHallService = dinningHallService;
        this.restaurantMenu = restaurantMenu;
    }

    @PostConstruct
    public void afterInit() {
        foods = MenuUtil.getMenu();
        log.info("{}", foods);
        openHall();
    }

    private void openHall() {
        for (int i = 0; i < Integer.parseInt(tableCnt); i++) {
            Thread r = new Thread(new Table(dinningHallService, restaurantMenu, i));
            tableList.put(String.format("Table_%s", i), r);
            r.start();
        }
    }


}
