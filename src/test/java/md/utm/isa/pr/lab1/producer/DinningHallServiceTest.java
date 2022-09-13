package md.utm.isa.pr.lab1.producer;

import lombok.RequiredArgsConstructor;
import md.utm.isa.pr.lab1.producer.dto.OrderDto;
import md.utm.isa.pr.lab1.producer.service.DinningHallService;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit.jupiter.SpringExtension;

@SpringBootTest()
@ExtendWith(SpringExtension.class)
@RequiredArgsConstructor(onConstructor = @__(@Autowired))
public class DinningHallServiceTest {

    private final DinningHallService dinningHallService;

    @Test
    @DisplayName("Test sending orders to consumer. Run only when consumer is connected")
    void sendOrder_simpleOrder_receiveResponse() {
        OrderDto orderDto = new OrderDto();
        orderDto.setOrderId(1L);
        orderDto.setWaiterId(1L);

        String received = dinningHallService.sendOrder(orderDto);
        Assertions.assertEquals(String.format("Received new order {%s}", orderDto.toString()), received);
    }
}
