package guru.springframework.domain;

import lombok.*;
import lombok.extern.slf4j.Slf4j;

import javax.persistence.*;
import java.math.BigDecimal;

@Getter
@Setter
@EqualsAndHashCode
@Entity
public class Ingredient {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String description;
    private BigDecimal amount;
    @ToString.Exclude
    @EqualsAndHashCode.Exclude
    @ManyToOne
    private Recipe recipe;
    @OneToOne(fetch = FetchType.EAGER)
    private UnitOfMeasure unitOfMeasure;

    public Ingredient() {
    }

    public Ingredient(String description, String amount, UnitOfMeasure unitOfMeasure) {
        this.description = description;
        this.amount = new BigDecimal(amount);
        this.unitOfMeasure = unitOfMeasure;
    }

}
