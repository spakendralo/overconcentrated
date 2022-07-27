package lu.pistache.overconcentrated.model;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PortfolioEntry {
    private Integer quantity;
    private double margin;
    private double portfolioConcentration = 0;
    private double value = -1;
    private double haircutValue = -1;

    public PortfolioEntry(Integer quantity, double margin) {
        this.quantity = quantity;
        this.margin = margin;
    }
}
