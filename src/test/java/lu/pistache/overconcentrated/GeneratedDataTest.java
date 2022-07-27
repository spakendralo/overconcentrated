package lu.pistache.overconcentrated;

import lu.pistache.overconcentrated.model.Customer;
import lu.pistache.overconcentrated.model.Portfolio;
import lu.pistache.overconcentrated.model.Security;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

public class GeneratedDataTest extends AbstractDataTest {
    @Test
    public void testGeneration() throws IOException {
       CustomerDataProducer customerDataProducer = new CustomerDataProducer();
        List<Customer> customers = customerDataProducer.getCustomers();
        List<Security> securities = customerDataProducer.getSecurities();

        List<Portfolio> portfolios = customerDataProducer.getPortfolios(100, securities, customers.size());

        printCustomers(customers);

        printSecurities(securities);

        printPortfolios(portfolios);

    }


}

