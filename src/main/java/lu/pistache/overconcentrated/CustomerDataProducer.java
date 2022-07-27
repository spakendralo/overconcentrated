package lu.pistache.overconcentrated;

import lu.pistache.overconcentrated.model.Customer;
import lu.pistache.overconcentrated.model.Portfolio;
import lu.pistache.overconcentrated.model.PortfolioEntry;
import lu.pistache.overconcentrated.model.Security;
import org.apache.kafka.clients.producer.KafkaProducer;
import org.apache.kafka.clients.producer.ProducerConfig;
import org.apache.kafka.clients.producer.ProducerRecord;
import org.apache.kafka.common.serialization.IntegerSerializer;
import org.apache.kafka.common.serialization.StringSerializer;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.util.*;

public class CustomerDataProducer extends AbstractProducer {
    public static final String NAMES_LIST = "src/main/resources/random_names-10000.txt";
    public static final String SECURITIES_LIST = "src/main/resources/securities.txt";
    public static final String CUSTOMERS_TOPIC = "customers";
    public static final String SECURITIES_TOPIC = "securities";
    public static final String PORTFOLIOS_TOPIC = "portfolios";
    private static int customerId = 0;

    public void createData() throws IOException, InterruptedException {
        Properties props = new Properties();
        props.put(ProducerConfig.BOOTSTRAP_SERVERS_CONFIG, "localhost:9092");
        props.put(ProducerConfig.KEY_SERIALIZER_CLASS_CONFIG, IntegerSerializer.class);
        props.put(ProducerConfig.VALUE_SERIALIZER_CLASS_CONFIG, StringSerializer.class);

        List<Customer> customers = getCustomers();
        writeCustomers(props, customers);

        List<Security> securities = getSecurities();
        writeSecurities(props, securities);

        List<Portfolio> portfolios = getPortfolios(customers.size(), securities, customers.size());
        writePortfolios(props, portfolios);

    }

    List<Customer> getCustomers() throws IOException {
        List<String> names = getRandomNames();
        List<Customer> customers = new ArrayList<>();
        for (String name : names) {
            customers.add(new Customer(customerId++, name));
        }
        return customers;
    }


    List<String> getRandomNames() throws IOException {
        File file = new File(NAMES_LIST);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String text;
        List<String> names = new ArrayList<>();

        while ((text = reader.readLine()) != null) {
            names.add(text);
        }
        return names;
    }

    List<Security> getSecurities() throws IOException {
        File file = new File(SECURITIES_LIST);
        BufferedReader reader = new BufferedReader(new FileReader(file));
        String text;
        List<Security> securities = new ArrayList<>();

        while ((text = reader.readLine()) != null) {
            String[] splitLine = text.split(" ");
            securities.add(new Security(splitLine[0], Double.parseDouble(splitLine[1]), Double.parseDouble(splitLine[2])));
        }
        return securities;
    }

    List<Portfolio> getPortfolios(int maxPortfolios, List<Security> securities, int customersNumber) {
        int portfolioId = 0;
        Random rand = new Random();
        List<Portfolio> portfolios = new ArrayList<>();
        for (int i = 0; i < maxPortfolios; i++) {
            int securitiesInPortfolio = rand.nextInt(5);
            Map<String, PortfolioEntry> portfolioEntries = new HashMap<>();
            for (int j = 0; j < securitiesInPortfolio; j++) {
                Security randomSecurity = securities.get(rand.nextInt(securities.size()));
                portfolioEntries.put("sec:" + randomSecurity.getName(), new PortfolioEntry(rand.nextInt(1, 100), randomSecurity.getMargin()));
            }
            Portfolio portfolio = new Portfolio(portfolioId++, rand.nextInt(customersNumber), portfolioEntries);
            portfolios.add(portfolio);
        }
        return portfolios;
    }

    private void writeSecurities(Properties props, List<Security> securities) {
        KafkaProducer<String, String> securitiesProducer = new KafkaProducer<String, String>(
                props);

        Random rand = new Random();

        for (Security security : securities) {
            ProducerRecord<String, String> record = new ProducerRecord<String, String>(
                    SECURITIES_TOPIC, security.getName(), Serializer.toString(security));
            securitiesProducer.send(record);
        }

        securitiesProducer.close();
    }

    private void writeCustomers(Properties props, List<Customer> customers) {
        KafkaProducer<Integer, String> customerProducer = new KafkaProducer<Integer, String>(
                props);

        for (Customer customer : customers) {
            ProducerRecord<Integer, String> record = new ProducerRecord<Integer, String>(
                    CUSTOMERS_TOPIC, customer.getId(), Serializer.toString(customer));
            customerProducer.send(record);
        }

        customerProducer.close();
    }

    private void writePortfolios(Properties props, List<Portfolio> portfolios) {
        KafkaProducer<Integer, String> portfoliosProducer = new KafkaProducer<Integer, String>(
                props);
        for (Portfolio portfolio : portfolios) {
            ProducerRecord<Integer, String> record = new ProducerRecord<Integer, String>(
                    PORTFOLIOS_TOPIC, portfolio.getId(), Serializer.toString(portfolio));
            portfoliosProducer.send(record);
        }
        portfoliosProducer.close();
    }

    public static void main(String[] args) throws IOException, InterruptedException {
        CustomerDataProducer helloProducer = new CustomerDataProducer();
        helloProducer.createData();
    }
}
