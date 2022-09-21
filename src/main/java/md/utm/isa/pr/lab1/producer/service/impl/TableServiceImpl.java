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

    private List<Double> ratings = new ArrayList<>();

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
        return ratings.stream().mapToDouble(Double::doubleValue).average().orElse(Double.NaN);
    }

    private void openHall() {
        for (int i = 0; i < Integer.parseInt(tableCnt); i++) {
            Thread r = new Thread(new Table(occupiedTables, restaurantMenu, i, 100L, this));
            tableList.put(String.format("Table_%s", i), r);
            r.start();
        }
    }
}
