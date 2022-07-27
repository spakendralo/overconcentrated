package lu.pistache.overconcentrated.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class Customer {
    private Integer id;
    private String name;

    public Customer(Integer id, String name) {
        this.id = id;
        this.name = name;
    }
}
