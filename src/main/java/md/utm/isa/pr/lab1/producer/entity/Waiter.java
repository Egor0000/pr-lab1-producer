package md.utm.isa.pr.lab1.producer.entity;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import md.utm.isa.pr.lab1.producer.dto.OrderDto;
import md.utm.isa.pr.lab1.producer.dto.PreparedOrderDto;
import md.utm.isa.pr.lab1.producer.service.DinningHallService;
import md.utm.isa.pr.lab1.producer.service.TableService;

import java.util.concurrent.ThreadLocalRandom;

@RequiredArgsConstructor
@Slf4j
public class Waiter implements Runnable{
    private final TableService tableService;

    private final DinningHallService dinningHallService;
    private final int waiterId;

    private final int timeUnit;

    @Override
    public void run() {
        try {
            while (true) {
                Table nextTable = tableService.takeOccupiedTable();

                if ( nextTable != null) {
                    OrderDto orderDto = nextTable.takeOrder();
                    orderDto.setWaiterId((long) waiterId);

                    log.info("Waiter (id={}) took order ({})", waiterId, orderDto);

                    long d = (long) ThreadLocalRandom.current().nextInt(2, 5);
                    Thread.sleep(d *timeUnit);

                    orderDto.setPickUpTime(System.currentTimeMillis());

                    tableService.moveTableToWait(nextTable);
                    dinningHallService.sendOrder(orderDto);
                }

                PreparedOrderDto orderDto = dinningHallService.getNextPreparedOrder(waiterId);

                if (orderDto != null) {
                    Table table = tableService.getById(orderDto.getTableId());

                    if (table != null) {
                        boolean isServed = table.servePreparedOrder(orderDto);
                        if (isServed) {
                            tableService.removeTableFromWait(table.getId());
                        }
                    }
                }
                Thread.sleep(100);
            }
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
        }
    }
}
