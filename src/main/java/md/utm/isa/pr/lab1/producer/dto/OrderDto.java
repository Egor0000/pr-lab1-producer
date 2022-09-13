package md.utm.isa.pr.lab1.producer.dto;

import lombok.Builder;
import lombok.Data;

import java.sql.Timestamp;
import java.util.List;

@Data
public class OrderDto {
    private Long orderId;
    private Long tableId;
    private Long waiterId;
    private List<Long> items;
    private Integer priority;
    private Long maxWait;
    private Timestamp pickUpTime;
}
