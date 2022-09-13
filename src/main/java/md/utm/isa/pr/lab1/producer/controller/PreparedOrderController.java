package md.utm.isa.pr.lab1.producer.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import md.utm.isa.pr.lab1.producer.dto.PreparedOrderDto;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/distribution")
@RequiredArgsConstructor
@Slf4j
public class PreparedOrderController {

    @PostMapping("/")
    public String post(@RequestBody PreparedOrderDto order) {
        log.info("Received prepared order {}", order);
        return String.format("Received new prepared order {%s}", order.toString());
    }
}

