package lu.pistache.overconcentrated;

import lu.pistache.overconcentrated.model.Customer;
import lu.pistache.overconcentrated.model.Portfolio;
import lu.pistache.overconcentrated.model.PortfolioEntry;
import lu.pistache.overconcentrated.model.Security;
import lu.pistache.overconcentrated.serdes.JsonPOJOSerializer;

import java.util.List;
import java.util.Map;

public abstract class AbstractDataTest {
    void printPortfolios(Map<String, PortfolioEntry> portfolios) {
        JsonPOJOSerializer<PortfolioEntry> portfolioJsonPOJOSerializer = new JsonPOJOSerializer<>();
        for (PortfolioEntry portfolioEntry : portfolios.values()) {
            System.out.println(new String(portfolioJsonPOJOSerializer.serialize("dumb", portfolioEntry)));
        }
    }

    void printPortfolios(List<Portfolio> portfolios) {
        JsonPOJOSerializer<Portfolio> portfolioJsonPOJOSerializer = new JsonPOJOSerializer<>();
        for (Portfolio portfolio : portfolios) {
            System.out.println(new String(portfolioJsonPOJOSerializer.serialize("dumb", portfolio)));
        }
    }

    void printSecurities(List<Security> securities) {
        JsonPOJOSerializer<Security> securityJsonPOJOSerializer = new JsonPOJOSerializer<>();
        for (Security security : securities) {
            System.out.println(new String(securityJsonPOJOSerializer.serialize("dumb", security)));
        }
    }

    void printCustomers(List<Customer> customers) {
        JsonPOJOSerializer<Customer> customerJsonPOJOSerializer = new JsonPOJOSerializer<>();
        int size = customers.size();
        if (size > 30) size = 30;
        for (int i = 0; i < size; i++) {
            System.out.println(new String(customerJsonPOJOSerializer.serialize("dumb", customers.get(i))));
        }
    }
}
