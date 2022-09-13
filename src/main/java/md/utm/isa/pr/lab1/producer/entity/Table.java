package md.utm.isa.pr.lab1.producer.entity;

import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import md.utm.isa.pr.lab1.producer.dto.OrderDto;
import md.utm.isa.pr.lab1.producer.service.DinningHallService;
import md.utm.isa.pr.lab1.producer.service.RestaurantMenu;
import md.utm.isa.pr.lab1.producer.utils.OrderUtil;

import java.sql.Timestamp;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ThreadLocalRandom;
import java.util.stream.Collectors;

@RequiredArgsConstructor
public class Table implements Runnable {
    private final DinningHallService dinningHallService;

    private final RestaurantMenu restaurantMenu;
    private final int thread;

    @Override
    public void run() {
        while (true) {
            OrderDto order = generateRandomOrder();
            order.setTableId((long) thread);

            dinningHallService.sendOrder(order);
            try {
                Thread.sleep(3000);
            } catch (Exception e) {

            }
        }
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

        orderDto.setMaxWait(restaurantMenu.getMaxPreparationTime(randomList).getPreparationTime());
        orderDto.setPickUpTime(Timestamp.from(Instant.now()));

        return orderDto;
    }

}
