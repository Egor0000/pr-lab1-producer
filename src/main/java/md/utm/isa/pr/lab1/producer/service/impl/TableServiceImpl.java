package md.utm.isa.pr.lab1.producer.service.impl;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import md.utm.isa.pr.lab1.producer.entity.Food;
import md.utm.isa.pr.lab1.producer.entity.Table;
import md.utm.isa.pr.lab1.producer.service.RestaurantMenu;
import md.utm.isa.pr.lab1.producer.service.TableService;
import md.utm.isa.pr.lab1.producer.utils.MenuUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import javax.annotation.PreDestroy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

@Service
@AllArgsConstructor
@Slf4j
public class TableServiceImpl implements TableService {
    private ConcurrentMap<String, Thread> tableList = new ConcurrentHashMap<>();
    private ConcurrentLinkedQueue<Table> occupiedTables = new ConcurrentLinkedQueue<>();

    private ConcurrentHashMap<Integer, Table> waitingTables = new ConcurrentHashMap<>();
    private final RestaurantMenu restaurantMenu;
    private List<Food> foods = new ArrayList<>();
    @Value("${dinning.tables}")
    private String tableCnt;
    @Value("${dinning.time-unit}")
    private Long timeUnit;
    @Value("${dinning.time-duration}")
    private Long timeDuration;

    private List<Double> ratings = new ArrayList<>();

    private List<Integer> stars = new ArrayList<>();

    @Autowired
    public TableServiceImpl(RestaurantMenu restaurantMenu) {
        this.restaurantMenu = restaurantMenu;
    }

    @PostConstruct
    public void afterInit() {
        foods = MenuUtil.getMenu();
        log.info("{}", foods);
        openHall();
    }

    @PreDestroy
    public void preDestroy() {
        ratingToStarts();
    }

    @Override
    public Table takeOccupiedTable() throws InterruptedException {
        return occupiedTables.poll();
    }

    @Override
    public void moveTableToWait(Table table) {
        waitingTables.put(table.getId(), table);
    }

    @Override
    public Table getById(Long id) {
        return waitingTables.get(id.intValue());
    }

    @Override
    public void removeTableFromWait(int id) {
        waitingTables.remove(id);
    }

    @Override
    public double getAverage(double next) {
        ratings.add(next);
        stars.add(ratingToStar(next));
        return ratings.stream().mapToDouble(Double::doubleValue).average().orElse(Double.NaN);
    }

    private void openHall() {
        for (int i = 0; i < Integer.parseInt(tableCnt); i++) {
            Thread r = new Thread(new Table(occupiedTables, restaurantMenu, i, timeUnit*timeDuration, this));
            tableList.put(String.format("Table_%s", i), r);
            r.start();
        }
    }

    private void ratingToStarts() {
        double average = stars.stream().mapToDouble(Integer::intValue).average().orElse(Double.NaN);
        log.info("All stars: {}", stars);

        log.info("AVERAGE: {}", average);
    }

    public int ratingToStar(double rating) {
        if (rating < 1) {
            return 5;
        } else if (rating < 1.1) {
            return 4;
        } else if (rating < 1.2) {
            return 3;
        } else if (rating < 1.3) {
            return 2;
        } else if (rating < 1.4) {
            return 1;
        }
        return 0;
    }
}
