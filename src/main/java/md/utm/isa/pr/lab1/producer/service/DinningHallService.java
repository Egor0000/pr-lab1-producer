package md.utm.isa.pr.lab1.producer.service;

import md.utm.isa.pr.lab1.producer.dto.OrderDto;
import md.utm.isa.pr.lab1.producer.entity.Food;

import java.net.http.HttpResponse;
import java.util.List;

public interface DinningHallService {
    String sendOrder(OrderDto order);

    List<Food> getMenu();
}
