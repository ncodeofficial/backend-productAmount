package dcode.domain.entity;

import lombok.*;

import javax.persistence.Entity;
import javax.persistence.Id;

@Data
@Entity
@Builder
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
public class Product {

    @Id
    private int id;
    private String name;
    private int price;
}
