package lu.pistache.overconcentrated.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Date;

@Getter
@Setter
public class Security {
    private String name;
    private Double value;
    private Double margin;
    private Date creationDate;

    public Security(String name, Double value, Double initialMargin) {
        this.name = name;
        this.value = value;
        this.margin = initialMargin;
        this.setCreationDate(new Date());
    }
}
