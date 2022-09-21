package md.utm.isa.pr.lab1.producer.entity;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import md.utm.isa.pr.lab1.producer.dto.OrderDto;
import md.utm.isa.pr.lab1.producer.dto.PreparedOrderDto;
import md.utm.isa.pr.lab1.producer.enums.TableState;
import md.utm.isa.pr.lab1.producer.service.DinningHallService;
import md.utm.isa.pr.lab1.producer.service.RestaurantMenu;
import md.utm.isa.pr.lab1.producer.service.TableService;
import md.utm.isa.pr.lab1.producer.utils.OrderUtil;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@RequiredArgsConstructor
@Slf4j
public class Table implements Runnable {
    private final ConcurrentLinkedQueue<Table> occupiedTables;

    private final RestaurantMenu restaurantMenu;
    private final int thread;

    private final long timeunit;

    private final TableService tableService;

    private TableState tableState = TableState.IDLE;

    @Override
    public void run() {
        while (true) {
            if (tableState.equals(TableState.IDLE)) {
                occupyTable();
            }
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
    }

    public OrderDto takeOrder() {
        OrderDto order = generateRandomOrder();
        order.setTableId((long) thread);

        tableState = TableState.WAIT;

        return order;
    }

    public int getId() {
        return this.thread;
    }

    private OrderDto generateRandomOrder() {
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderId(OrderUtil.getNextOrderId());

        //todo what is the max number of items?
        List<Food> randomList = restaurantMenu.getRandomFoods(ThreadLocalRandom.current().nextInt(1, 14));

        orderDto.setItems(randomList.stream()
                .map(Food::getId)
                .collect(Collectors.toList()));

        orderDto.setPriority(ThreadLocalRandom.current().nextInt(1, 6));

        orderDto.setMaxWait(restaurantMenu.getMaxPreparationTime(randomList).getPreparationTime()*1.3);
        orderDto.setPickUpTime(System.currentTimeMillis());

        return orderDto;
    }

    private void occupyTable () {
        int idleTime = ThreadLocalRandom.current().nextInt(1, 6);
        try {
            Thread.sleep(idleTime*1000L);
            tableState = TableState.OCCUPIED;
            occupiedTables.add(this);

            log.info("Table (id={}) is occupied", thread);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    public boolean servePreparedOrder(PreparedOrderDto orderDto) {
        if (checkOrder(orderDto)) {
            log.info("Order (id={}) served successfully to table (id={}). Table moved to state IDLE", orderDto.getOrderId(), thread);
            Long prepTime =  System.currentTimeMillis() - orderDto.getPickUpTime() - orderDto.getSendTime();
            log.info("Order total preparation time = {}. Rating = {}", prepTime, getRating(orderDto.getMaxWait(), prepTime, 100));
            tableState = TableState.IDLE;
            return true;
        }

        return false;
    }

    private boolean checkOrder(PreparedOrderDto orderDto) {

        // todo add more validations
        log.info("Prepared time for order (id={}) is {}, maxWait = {}", orderDto.getOrderId(), orderDto.getCookingTime() - orderDto.getPickUpTime(), orderDto.getMaxWait()*1.3*100);
        log.info("Average rating {}", tableService.getAverage((double) (orderDto.getCookingTime() - orderDto.getPickUpTime())/(orderDto.getMaxWait()*1.3*100)));
        if (orderDto.getTableId() == thread) {
            return true;
        }

        return false;
    }

    private double getRating(double maxTime, long prepTime, long timeUnit) {
        return (double) prepTime / (timeUnit * maxTime);
    }
}
