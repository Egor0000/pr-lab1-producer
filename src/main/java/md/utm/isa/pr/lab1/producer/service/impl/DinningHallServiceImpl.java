package md.utm.isa.pr.lab1.producer.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import md.utm.isa.pr.lab1.producer.dto.OrderDto;
import md.utm.isa.pr.lab1.producer.dto.PreparedOrderDto;
import md.utm.isa.pr.lab1.producer.entity.Food;
import md.utm.isa.pr.lab1.producer.service.DinningHallService;
import md.utm.isa.pr.lab1.producer.utils.MenuUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.BodyInserters;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import javax.annotation.PostConstruct;
import java.net.URI;
import java.net.URL;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;
import java.util.concurrent.ConcurrentMap;

@Service
@Slf4j
public class DinningHallServiceImpl implements DinningHallService {
    @Value("${consumer.address}")
    private String address;

    @Value("${consumer.port}")
    private Integer port;

    private String path = "/order/";
    private WebClient webClient;

    private List<Food> menu;

    private ConcurrentHashMap<Long, ConcurrentLinkedQueue<PreparedOrderDto>> preparedOrderLists = new ConcurrentHashMap<>();

    public DinningHallServiceImpl() {
        this.menu = MenuUtil.getMenu();
    }

    @PostConstruct
    private void onInit() {
        try {
            URI uri = new URI("http", null, address, port, null, null, null);
            URL url = uri.toURL();

            webClient = WebClient.builder()
                    .defaultHeader(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON_VALUE)
                    .defaultUriVariables(Collections.singletonMap("url", url))
                    .build();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public String sendOrder(OrderDto order) {
        if (webClient != null) {
            log.info("Send order {}. Time for sending {}", order, (System.currentTimeMillis() - order.getPickUpTime()));
            Mono<String> response = webClient.post()
                    .uri(String.format("%s:%s%s", address, port, path))
                    .body(BodyInserters.fromValue(order))
                    .accept(MediaType.APPLICATION_JSON)
                    .retrieve()
                    .bodyToMono(String.class);

            return response.block();
        }
        return null;
    }

    @Override
    public void receivePreparedOrder(PreparedOrderDto orderDto) {
        log.info("Received prepared order {}", orderDto);

        if (preparedOrderLists.get(orderDto.getWaiterId()) == null) {
            preparedOrderLists.put(orderDto.getWaiterId(), new ConcurrentLinkedQueue<>());
        }
        preparedOrderLists.get(orderDto.getWaiterId()).add(orderDto);
    }

    @Override
    public PreparedOrderDto getNextPreparedOrder(int waiterId) {
        ConcurrentLinkedQueue<PreparedOrderDto> waiterOrders = preparedOrderLists.get((long) waiterId);

        if (waiterOrders!=null) {
            return waiterOrders.poll();
        }

        return null;
    }

    @Override
    public List<Food> getMenu() {
        return menu;
    }

}
