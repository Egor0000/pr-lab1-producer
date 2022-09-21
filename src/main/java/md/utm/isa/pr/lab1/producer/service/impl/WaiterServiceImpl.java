package md.utm.isa.pr.lab1.producer.service.impl;

import lombok.RequiredArgsConstructor;
import md.utm.isa.pr.lab1.producer.entity.Waiter;
import md.utm.isa.pr.lab1.producer.service.DinningHallService;
import md.utm.isa.pr.lab1.producer.service.TableService;
import md.utm.isa.pr.lab1.producer.service.WaiterService;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentMap;

@Service
@RequiredArgsConstructor
public class WaiterServiceImpl implements WaiterService {
    @Value("${dinning.waiters}")
    private String waiterCount;

    @Value("${dinning.time-unit}")
    private int timeUnit;

    @Value("${dinning.time-duration}")
    private int timeDuration;
    private ConcurrentMap<String, Thread> waiters = new ConcurrentHashMap<>();
    private final TableService tableService;
    private final DinningHallService dinningHallService;

    @PostConstruct
    private void init() {
        initWaiters();
    }

    private void initWaiters() {
        for (int i = 0; i < Integer.parseInt(waiterCount); i++) {
            Thread r = new Thread(new Waiter(tableService, dinningHallService, i, timeUnit*timeDuration));
            waiters.put(String.format("Waiter_%s", i), r);

            r.start();
        }
    }
}
