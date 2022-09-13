package md.utm.isa.pr.lab1.producer.entity;

import lombok.Data;
import md.utm.isa.pr.lab1.producer.enums.CookingApparatus;

@Data
public class Food {
    private Long id;
    private String name;
    private Long preparationTime;
    private Long complexity;
    private CookingApparatus cookingApparatus;
}
