package lu.pistache.overconcentrated.model;

import lombok.Getter;
import lombok.Setter;

import java.util.Map;

@Getter
@Setter
public class Portfolio {
    private Map<String, PortfolioEntry> entries;
    private Integer id;
    private Integer customerId;

    public Portfolio() {
    }

    public Portfolio(Integer id, Integer customerId, Map<String, PortfolioEntry> entries) {
        this.entries = entries;
        this.customerId = customerId;
        this.id = id;
    }

}
